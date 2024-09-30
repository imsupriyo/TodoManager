<%@ include file="common/header.jspf" %>
<%@ include file="common/navigation.jspf" %>
<div class="container" xmlns:form="http://www.w3.org/1999/html">
    <h6>Please fill the below fields:</h6>
    <hr>

    <form:form class="form" method="post" modelAttribute="todo">
        <fieldset class="mb-3">
            <form:input type="text" path="description" placeholder="Description" required="required" class="form-control mb-4 w-25"/>
            <form:errors path="description" cssClass="text-danger"/>
        </fieldset>
        <fieldset class="mb-3">
            <form:input type="hidden" path="username"/>
            <form:input type="hidden" path="id"/>
            <form:label path="targetDate">Target Date</form:label>
            <form:input type="text" path="targetDate" class="form-control mb-4 w-25"/>
            <form:errors path="targetDate" cssClass="text-danger"/>
        </fieldset>
        <form:label path="done">Is Done?</form:label>
        <form:select type="" path="done" class="form-control mb-4 w-25">
            <form:option value="true">True</form:option>
            <form:option value="false">False</form:option>
        </form:select>
        <input type="submit" class="btn btn-success col-2"/>
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