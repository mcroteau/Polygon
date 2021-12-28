<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>Edit Profile</title>
</head>
<body>

	<div class="inside-container">

		<a href="${pageContext.request.contextPath}/users" id="prospect-back" class="href-dotted">&larr;&nbsp;Back</a>

		<c:if test="${not empty message}">
			<div class="notify">${message}</div>
		</c:if>

		<h1>New User</h1>

		<form action="${pageContext.request.contextPath}/users/save" method="post">

			<label>User's Full Name</label>
			<p class="information"></p>
			<input type="text" name="name" placeholder="Dirk Nanovitz" value=""/>

			<label>Cell Phone</label>
			<span class="tiny">No special characters or spaces.</span>
			<input type="text" name="phone" placeholder="" value=""/>

			<label>Password</label>
			<input type="text" name="password" placeholder="" value=""/>

			<div class="button-wrapper">
				<input type="submit" value="Save User" class="button green"/>
			</div>
		</form>

		<a href="${pageContext.request.contextPath}/signout" class="href-dotted">Signout</a>

	</div>
</body>
</html>

