<%--
  Created by IntelliJ IDEA.
  User: Dan
  Date: 1/10/2016
  Time: 1:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>
    .download {
        position: absolute;
        float:right;
        right:0;
        top:0;
        background-color: #EEE; /*to make it visible*/
    }
</style>
<html>
<head>
    <title>Title</title>
</head>
<body>

<%
    // Set refresh, autoload time as 5 seconds
    response.setIntHeader("Refresh", 5);
%>
<div id="left" style="width:789px; max-height:165px; overflow:auto; max-width: 400px">
    <font color="blue">Available Slaves: </font>
    <br>
    <c:forEach var="item" items="${ips}">
       <a
            href="website/ips/${ips.indexOf(item)}">  ${item}    </a>  <br>
    </c:forEach>
</div>
<div class ="download">
<a href="/resources/tsms-client-1.0-SNAPSHOT-jar-with-dependencies.jar" >download </a>
</div>
</body>
</html>
