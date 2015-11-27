<%--
  Created by IntelliJ IDEA.
  User: dic
  Date: 20-11-2015
  Time: 13:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Sometitle</title>
</head>

<body>
Hello!!!!

<input type="file" name="file" />

<input type="file" id="ctrl" webkitdirectory directory multiple/>

<h2> Files</h2>

<c:forEach var ="item" items="${folderList}">
    <c:out value="${item}"/> <br/>
</c:forEach>



</body>
</html>
