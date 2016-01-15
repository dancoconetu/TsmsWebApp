<%--
  Created by IntelliJ IDEA.
  User: dic
  Date: 20-11-2015
  Time: 13:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet">
<html>
<head>
    <title>Pick Folder</title>
</head>

<body>
<%--Hello!!!!--%>

<%--<input type="file" name="file" />--%>

<%--<input type="file" id="ctrl" webkitdirectory directory multiple/>--%>



<%
    // Set refresh, autoload time as 5 seconds
    response.setIntHeader("Refresh", 5);
    %>
<div id="OsDiv" >
OsName: ${OsName} <br>
PcName: ${PcName} <br>
Python versions:
<c:forEach var="item" items="${PythonVersions}">
  ${item}

</c:forEach>
    <a href="/showFiles" >Show files available</a>
</div>

<h2> Files</h2>

<a href="/website/goToParentFolder"> ... Up folder </a> </br>
<div id="container">
    <div id="left" style="width:789px; max-height:165px; overflow:auto; max-width: 400px">
        <font color="blue">Current Folder: </font>
        <c:forEach var="item" items="${folderList}">
            <c:if test="${folderListBoolean.get(folderList.indexOf(item))==true}"> <a
                    href="/website/${folderList.indexOf(item)}"/> </c:if> <c:out value="${item.absolutePath}"/> </a> <a
                href="/sendFiles/${folderList.indexOf(item)} " style="float:right"/> <font color="red">
            Send</font> </a> </br>
        </c:forEach>

    </div>


    <div id="right" style="width:789px; max-height:265px; overflow:auto; max-width: 600px">

            <font color="blue"> Available scripts on the slave </font>
            <c:forEach var="item" items="${Scripts}">
                <font color="red"> <br>${item} </font>  </br>
                <c:forEach var="version" items="${PythonVersions}">  <a href="/runScript/${Scripts.indexOf(item)}/${version}"> ${version} </a>></c:forEach>


            </c:forEach>

    </div>

    <div id ="bottom">
        <div id ="bottomTop">
        <br>
        Status: ${Status}
        <br>
        Script results:
        <br>
        <pre>${LastScriptResults}</pre>
        </div>
    </div>


    <div id="bottomright" style="width:789px; max-height:265px; overflow:auto; max-width: 600px">
    <c:forEach var="item" items="${XmlResults}">
    <c:if test="${XmlResultsBooleans.get(XmlResults.indexOf(item))==true}" >
        <font color="green" > </c:if> <c:if test="${XmlResultsBooleans.get(XmlResults.indexOf(item))==false}" >
            <font color="red" > </c:if>
        <br> <a href="/showXml/${XmlResults.indexOf(item)}"> <c:out value = "${item}"/> </a> Failures: ${XmlResultsHashTables.get(XmlResults.indexOf(item)).get("failures")}
  </c:forEach>
    </div>
</div>
</body>
</html>
