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

		<h1>Edit User</h1>

		<c:if test="${activityCounts.size() > 0}">
			<h3>Great Job!</h3>
			<p>You're always doing a great job.
				Let's see what you've been up to.</p>
			<c:forEach items="${activityCounts}" var="activityCount">
				<p>${activityCount.count} ${activityCount.name}s</p>
			</c:forEach>
			<p>Not bad...</p>
		</c:if>
		<c:if test="${activityCounts.size() == 0}">
			Nothing to show yet.
		</c:if>

		<form action="${pageContext.request.contextPath}/users/update/${user.id}" method="post">

			<label>Name</label>
			<p class="information"></p>
			<input type="text" name="name" placeholder="" value="${user.name}"/>

			<label>Cell Phone</label>
			<span class="tiny">The application uses your cell phone to send notification updates.</span>
			<input type="text" name="phone" placeholder="9079878652" value="${user.phone}"/>

			<div class="button-wrapper">
				<input type="submit" value="Update User" class="button green"/>
			</div>
		</form>

		<a href="${pageContext.request.contextPath}/signout" class="href-dotted">Signout</a>

	</div>
</body>
</html>

