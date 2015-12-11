<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--
  Created by IntelliJ IDEA.
  User: dic
  Date: 11-12-2015
  Time: 10:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DropDown</title>
</head>
<body>
<div>
    <form:form method="POST" commandName="SlaveDropDown">

        <table>

            <tr>
                <td>Country :</td>
                <td><form:select path="computerName">
                    <form:option value="NONE" label="--- Select ---" />
                    <form:options items="${computerNames}" />
                </form:select>
                </td>

            </tr>


            <tr>
                <td colspan="3"><input type="submit" /></td>
            </tr>
        </table>
    </form:form>

    Slave selected: ${comp.name}
</div>
</body>
</html>
