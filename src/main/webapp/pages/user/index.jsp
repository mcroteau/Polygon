<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>Users</title>
</head>
<body>

<div class="inside-container">
	<c:if test="${not empty message}">
		<div class="notify">${message}</div>
	</c:if>

	<h1 class="left-float">Users</h1>

	<a href="${pageContext.request.contextPath}/users/create" class="button orange right-float">New User!</a>
	<br class="clear"/>

	<c:choose>
		<c:when test="${users.size() > 0}">
					
			<div class="span12">
				<table class="table table-condensed">
					<thead>
						<tr>
							<th>Id</th>
							<th>Name</th>
							<th>Phone</th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="user" items="${users}">
							<tr>
								<td>${user.id}</td>
								<td>${user.name}</td>
								<td>${user.phone}</td>
								<td><a href="${pageContext.request.contextPath}/users/edit/${user.id}" title="Edit" class="button retro">Edit</a>
							</tr>									
						</c:forEach>
					</tbody>
				</table>
			</div>
			
		</c:when>
		<c:when test="${users.size() == 0}">
			<p>No users created yet.</p>
		</c:when>
	</c:choose>
</div>
</body>
</html>