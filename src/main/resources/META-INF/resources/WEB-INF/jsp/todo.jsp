<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<div class="container">
    <h6>Welcome back ${name}!</h6>
    <h2> Your todos are:</h2>
    <hr>

    <table class="table table-bordered table-striped">
        <thead class="table-dark">
            <tr>
                <th>Description</th>
                <th>Target Date</th>
                <th>Is Done?</th>
                <th>Action</th>
            </tr>
        </thead>

        <tbody>
            <c:forEach items="${todos}" var="todo">
                <tr>
                    <td>${todo.description}</td>
                    <td>${todo.targetDate}</td>
                    <td>${todo.done}</td>
                    <td>
                    <a href="update-todo?id=${todo.id}" class="btn btn-warning btn-sm">Update</a>
                    <a href="delete-todo?id=${todo.id}" class="btn btn-danger btn-sm">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>

    </table>
    <div class="text-center">
        <a class="btn btn-success" href="add-todo">Add a Todo</a>
    </div>

</div>
<%@ include file="common/footer.jspf" %>
</body>
</html>