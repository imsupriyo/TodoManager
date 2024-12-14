<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Log In</title>
</head>
<body>
<h2>Login Page</h2>

<form action="/login" method="post">
        <p>
            Username: <input type="text" name="username" id="username">
        </p>
        <p>
            Password: <input type="password" name="password" id="password">
        </p>
        <input type="submit" value="Login">
    </form>

    <!-- Error Message -->
    <c:if test="${not empty param.error}">
        <div style="color: red;">Invalid username or password.</div>
    </c:if>
</body>
</html>