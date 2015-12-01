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
    <title>Pick Folder</title>
</head>

<body>
<%--Hello!!!!--%>

<%--<input type="file" name="file" />--%>

<%--<input type="file" id="ctrl" webkitdirectory directory multiple/>--%>

<h2> Files</h2>

 <a href="website/goToParentFolder"> ... Up folder </a> </br>
<c:forEach var ="item" items="${folderList}">
    <c:out value="${item.absolutePath}"/>  <c:if test="${folderListBoolean.get(folderList.indexOf(item))==true}"> <a href="website/${folderList.indexOf(item)}"/>  Go Into</a> <a href="sendFiles/${folderList.indexOf(item)}"/>  Send Folder</a></c:if> </br>
</c:forEach>



</body>
</html>
