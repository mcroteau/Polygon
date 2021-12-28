<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="inside-container">

    <a href="${pageContext.request.contextPath}/prospects" id="prospect-back" class="href-dotted">&larr;&nbsp;Back</a>

    <c:if test="${not empty message}">
        <div class="notify">${message}</div>
    </c:if>

    <h1>Add Prospect</h1>
    <p>Add a new prospect to your mining effort.</p>
    <form action="${pageContext.request.contextPath}/prospects/save" method="post">

        <label>Name</label>
        <input type="text" name="name" style="width:100%;" />

        <label>Phone</label>
        <input type="text" name="phone" />

        <label>Status</label>
        <select name="status" style="display: block">
            <c:forEach items="${statuses}" var="status">
                <option value="${status.id}">${status.name}</option>
            </c:forEach>
        </select>


        <div class="button-wrapper-lonely">
            <input type="submit" class="button green" value="Save Prospect"/>
        </div>
    </form>
</div>
