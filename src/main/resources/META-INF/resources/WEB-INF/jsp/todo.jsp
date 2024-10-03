<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<div class="container">
    <h6>Welcome back ${name}!</h6>
    <h2> Your todos are:</h2>
    <hr>
<p><a href="/todo/completed-todo" class="link-success link-offset-2 link-underline-opacity-25 link-underline-opacity-100-hover">Completed Todos</a></p>

    <table class="table table-bordered table-striped">
        <thead class="table-dark">
            <tr>
                <th>Description</th>
                <th>Target Date</th>
                <th>Action</th>
            </tr>
        </thead>

        <tbody>
            <c:forEach items="${todos}" var="todo">
                <tr>
                    <td>${todo.description}</td>
                    <td>${todo.targetDate}</td>
                    <td>
                    <a href="/todo/mark-done-todo?id=${todo.id}" class="btn btn-success btn-sm">Mark as Done</a>
                    <a href="/todo/update-todo?id=${todo.id}" class="btn btn-warning btn-sm">Update</a>
                    <a href="/todo/delete-todo?id=${todo.id}" class="btn btn-danger btn-sm">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>

    </table>

    <div class="text-center">
        <a class="btn btn-primary" href="/todo/add-todo">Add a Todo</a>
    </div>

</div>
<%@ include file="common/footer.jspf" %>
</body>
</html>