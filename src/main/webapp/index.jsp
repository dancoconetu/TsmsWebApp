<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<c:forEach var="i" begin="1" end="5">
NAME <c:out value="${i}"/><p>
    </c:forEach>
</body>
</html>
