<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<html>
<head>
    <title>Delete User Form</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <form method="post" class="form-inline">
            <div class="form-group mb-2">
                <label for="username" class="mr-2">User name:</label>
                <select id="username" name="username" class="form-control">
                    <c:forEach var="user" items="${users}">
                        <option value="${user}">${user}</option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit" class="btn btn-danger mb-2 ml-2">Delete</button>
        </form>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
