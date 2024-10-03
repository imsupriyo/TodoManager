<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<div class="container">
    <h6>Welcome ${name}, you are an Admin!</h6>
    <h2> Here are all the TODOs</h2>
    <hr>

<p><a href="/todo/admin/completed-todo" class="link-success link-offset-2 link-underline-opacity-25 link-underline-opacity-100-hover">Completed Todos</a></p>

    <table class="table table-bordered table-striped">
        <thead class="table-dark">
            <tr>
                <th>User Name</th>
                <th>Description</th>
                <th>Target Date</th>
                <th>Action</th>
            </tr>
        </thead>

        <tbody>
            <c:forEach items="${todos}" var="todo">
                <tr>
                    <td>${todo.username}</td>
                    <td>${todo.description}</td>
                    <td>${todo.targetDate}</td>
                    <td>
                     <a href="/todo/admin/mark-done-todo?id=${todo.id}" class="btn btn-success btn-sm">Mark as Done</a>
                    <a href="/todo/admin/update-todo?id=${todo.id}" class="btn btn-warning btn-sm">Update</a>
                    <a href="/todo/admin/delete-todo?id=${todo.id}" class="btn btn-danger btn-sm">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
<div class="text-center">
    <a class="btn btn-primary d-inline-block" href="/todo/admin/add-todo">Add Todo</a>
    <a class="btn btn-success d-inline-block" href="/todo/admin/add-user">Create User</a>
    <a class="btn btn-danger d-inline-block" href="/todo/admin/del-user">Delete User</a>
</div>

</div>
<%@ include file="common/footer.jspf" %>
</body>
</html>