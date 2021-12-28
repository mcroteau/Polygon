<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Signin</title>
</head>
<body>

<div style="margin-top:20px;">

    <c:if test="${not empty message}">
        <p class="notify">${message}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/authenticate" method="post" >

        <div class="form-group">
            <label for="phone">Phone <span class="tiny">The cell number associated with your account.</span></label>
            <input type="text" name="phone" class="form-control" placeholder="" value=""  style="width:100%;">
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" name="password" class="form-control" id="password" style="width:100%;" value=""  placeholder="&#9679;&#9679;&#9679;&#9679;&#9679;&#9679;">
        </div>

        <div style="text-align:right; margin:30px 0px;">
            <input type="submit" class="button retro" value="Signin" style="width:100%;">
        </div>

    </form>

</div>

</body>
</html>
