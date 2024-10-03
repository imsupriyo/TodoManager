<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>

<html>
<head>
    <title>User Form</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .form-check {
            /* Allow checkboxes to wrap and prevent overlap */
            margin-bottom: 10px; /* Space between checkboxes */
        }
        .form-check-label {
            /* Ensure long labels wrap properly */
            word-wrap: break-word;
            max-width: 300px; /* Set max width for label to avoid overflow */
            display: inline-block; /* Allows label to take max-width */
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <h2>User Registration Form</h2>

        <form:form method="post" modelAttribute="userForm" class="form">
            <form:input path="id" type="hidden"/>

            <div class="form-group">
                <form:label path="username" class="form-label">Username:</form:label>
                <form:input path="username" class="form-control" />
                <form:errors path="username" cssClass="text-danger"/>
            </div>

            <div class="form-group">
                <form:label path="password" class="form-label">Password:</form:label>
                <form:password path="password" class="form-control" />
                <form:errors path="password" cssClass="text-danger"/>
            </div>

            <div class="form-group">
                <form:label path="roles" class="form-label">Roles:</form:label><br/>
                <c:forEach var="role" items="${roleOptions}">
                    <div class="form-check">
                        <input type="checkbox" name="roles" path="roles" value="${role}" class="form-check-input" id="${role}" />
                        <form:label class="form-check-label" path="roles" for="${role}">${role}</form:label>
                    </div>
                </c:forEach>
                <form:errors path="roles" cssClass="text-danger"/>
            </div>

            <div class="form-group">
                <input type="submit" value="Register" class="btn btn-primary" />
            </div>

        </form:form>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
