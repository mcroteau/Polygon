<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Search Results</title>
</head>
<body>

<div class="inside-container">

    <a href="${pageContext.request.contextPath}/" id="prospect-back" class="href-dotted">&larr;&nbsp;Back</a>

    <br class="clear"/>

    <h2 id="results-h1">${prospects.size()} Results</h2>

    <br class="clear"/>

    <c:if test="${prospects.size() > 0}">
        <table id="results">
            <c:forEach var="prospect" items="${prospects}">
                <tr>
                    <td><a href="${pageContext.request.contextPath}/prospects/${prospect.id}" style="text-decoration: none;">${prospect.name}</a></td>
                    <td class="center" style="width:190px;">${prospect.phone}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <c:if test="${prospects.size() == 0}">
        <p>No Prospects created yet.</p>
    </c:if>
</div>
</body>
</html>
