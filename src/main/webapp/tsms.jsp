<%--
  Created by IntelliJ IDEA.
  User: Dan
  Date: 1/10/2016
  Time: 1:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Title</title>
</head>
<body>


<div id="left" style="width:789px; max-height:165px; overflow:auto; max-width: 400px">
    <font color="blue">Current Folder: </font>
    <c:forEach var="item" items="${ips}">
       ${item}    <a
            href="website/ips/${ips.indexOf(item)}"> Check this computer </a>  <br>
    </c:forEach>
</div>
</body>
</html>
