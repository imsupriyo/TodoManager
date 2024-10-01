<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Delete User Form</title>
</head>
<body>
    <h2>User Deletion Form</h2>
    <form method="post">
        <label for="username">User name:</label>
        <select id="username" name="username">

            <c:forEach var="user" items="${users}">
                <option value="${user}">${user}</option>
            </c:forEach>
        </select>

        <input type="submit" value="Delete" />
    </form>
</body>
</html>
