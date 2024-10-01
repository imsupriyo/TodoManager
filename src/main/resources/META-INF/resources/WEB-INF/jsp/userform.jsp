<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <title>User Form</title>
</head>
<body>
    <h2>User Registration Form</h2>

    <form:form method="post" modelAttribute="userForm">

        <form:input path="id" type="hidden"/><br/>

        <form:label path="username">Username:</form:label>
        <form:input path="username" /><br/>

        <form:label path="password">Password:</form:label>
        <form:password path="password" /><br/>

        <form:label path="roles">Roles:</form:label><br/>
        <form:checkboxes path="roles" items="${roleOptions}" /><br/>

        <input type="submit" value="Register" />
    </form:form>
</body>
</html>
