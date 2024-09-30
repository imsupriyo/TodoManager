<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <title>User Form</title>
</head>
<body>
    <h2>User Registration Form</h2>

    <form:form method="post" modelAttribute="userForm">

        <!-- Input field for ID -->
        <form:input path="id" type="hidden"/><br/>

        <!-- Input field for Username -->
        <form:label path="username">Username:</form:label>
        <form:input path="username" /><br/>

        <!-- Input field for Password -->
        <form:label path="password">Password:</form:label>
        <form:password path="password" /><br/>

        <!-- Checkbox group for Roles -->
        <form:label path="roles">Roles:</form:label><br/>
        <form:checkboxes path="roles" items="${roleOptions}" /><br/>

        <!-- Submit Button -->
        <input type="submit" value="Register" />
    </form:form>
</body>
</html>
