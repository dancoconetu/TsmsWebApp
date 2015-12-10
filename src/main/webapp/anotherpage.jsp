<%--
  Created by IntelliJ IDEA.
  User: dic
  Date: 20-11-2015
  Time: 13:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet" >
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
<div id="container">
<div id="left" style="width:789px; max-height:165px; overflow:auto; max-width: 400px">
    <font color="blue">Current Folder: </font>
    <c:forEach var ="item" items="${folderList}">
        <c:if test="${folderListBoolean.get(folderList.indexOf(item))==true}">  <a href="website/${folderList.indexOf(item)}" />   </c:if>    <c:out value="${item.absolutePath}" />   </a>   <a href="sendFiles/${folderList.indexOf(item)} " style="float:right" /> <font color = "red" >
    Send</font>  </a>  </br>
    </c:forEach>
</div>


<div  id="right" >
    Hello?
    </div>
</div>
</body>
</html>
