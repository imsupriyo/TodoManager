<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>

<div class="container">
    <h6>Please fill the below fields:</h6>
    <hr>
    <form:form class="form" method="post" modelAttribute="todo">
        <fieldset class="mb-3">
            <div>
                <label for="username">User name:</label>
                <form:select path="username" id="username">
                    <c:forEach var="user" items="${users}">
                        <form:option value="${user}">${user}</form:option>
                    </c:forEach>
                </form:select>
                <form:errors path="username" cssClass="text-danger"/>
            </div>
        </fieldset>

        <fieldset class="mb-3">
            <form:label path="description">Description</form:label>
            <form:input type="text" path="description" required="required"/>
            <form:errors path="description" cssClass="text-danger"/>
        </fieldset>

        <fieldset class="mb-3">
            <form:input type="hidden" path="id"/>
            <form:label path="targetDate">Target Date</form:label>
            <form:input type="text" path="targetDate"/>
            <form:errors path="targetDate" cssClass="text-danger"/>
        </fieldset>

        <form:label path="done">Is Done?</form:label>
        <form:select path="done">
            <form:option value="true">True</form:option>
            <form:option value="false">False</form:option>
        </form:select>
<br></br>
        <input type="submit" class="btn btn-success"/>
    </form:form>
</div>

<%@ include file="common/footer.jspf" %>

<script type="text/javascript">
    $('#targetDate').datepicker({
        format: 'yyyy-mm-dd'
    });
</script>
</body>
</html>