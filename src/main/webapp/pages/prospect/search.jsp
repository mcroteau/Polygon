<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Search Prospects</title>
</head>
<body>

    <div class="inside-container">

        <c:if test="${not empty message}">
            <div class="notify">${message}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/prospects/search" method="get">
            <h2 class="left-float">Search Prospects: </h2>

            <br class="clear"/>

            <p>${prospects.size()} Prospects</p>

            <input type="text" name="q" placeholder="BlueWater Trucking Co." id="prospect-search"/>

            <div class="button-wrapper-tiny">
            <input type="submit" value="Search!" class="button super green" id="search-button" style="float: right"/>
            <br class="clear"/>
            </div>
        </form>

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
            <p class="notify">No Prospects created yet.</p>
        </c:if>

    </div>

</body>
</html>
