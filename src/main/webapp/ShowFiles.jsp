<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Dan
  Date: 1/15/2016
  Time: 3:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <%
        // Set refresh, autoload time as 5 seconds
        response.setIntHeader("Refresh", 5);
    %>

</head>
<body>
<title>Files Available</title>
Files Available <br>
    <pre>

<c:forEach var="item" items="${filesAvailable}">
    ${item} <a href="/deleteFile/${filesAvailable.indexOf(item)}" >Delete the file</a>
</c:forEach>
    </pre>
</body>
</html>
