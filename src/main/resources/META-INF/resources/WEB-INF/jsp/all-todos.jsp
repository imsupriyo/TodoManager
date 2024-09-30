<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<div class="container">
    <h6>Welcome ${name}, you are an Admin!</h6>
    <h2> Here are all the TODOs</h2>
    <hr>

    <table class="table table-bordered table-striped">
        <thead class="table-dark">
            <tr>
                <th>User Name</th>
                <th>Description</th>
                <th>Target Date</th>
                <th>Is Done?</th>
                <th>Action</th>
            </tr>
        </thead>

        <tbody>
            <c:forEach items="${todos}" var="todo">
                <tr>
                    <td>${todo.username}</td>
                    <td>${todo.description}</td>
                    <td>${todo.targetDate}</td>
                    <td>${todo.done}</td>
                    <td>
                    <a href="update-admin-todo?id=${todo.id}" class="btn btn-warning btn-sm">Update</a>
                    <a href="delete-admin-todo?id=${todo.id}" class="btn btn-danger btn-sm">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <a class="btn btn-success" href="admin-add">Add Todo</a><br/>
    <a class="btn btn-primary" href="register">Create User</a><br/>
    <a class="btn btn-danger" href="del-user">Delete User</a>
</div>
<%@ include file="common/footer.jspf" %>
</body>
</html>