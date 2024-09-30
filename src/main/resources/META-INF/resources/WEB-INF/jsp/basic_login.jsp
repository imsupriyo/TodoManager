<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Log In</title>
</head>
<body>
    <h2>Custom Login Page</h2>

    <!-- Login Form -->
    <form action="/todo" method="post">
        <p>
            Username: <input type="text" path="username">
        </p>
        <p>
            Password: <input type="password" path="password">
        </p>
        <input type="submit" value="Login">
    </form>

    <!-- Error Message -->
    <c:if test="${not empty param.error}">
        <div style="color: red;">Invalid username or password.</div>
    </c:if>
</body>
</html>