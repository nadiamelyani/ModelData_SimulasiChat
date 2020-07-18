<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Saved Message</title>
</head>
<body>
<div>
	<form action="ActionController" method="post">
		<input type="submit" name="action" value="main">
		<input type="submit" name="action" value="delete saved message"><br><br>
		type something : <input type="text" name="message">
		<input type="submit" name="action" value="send"><br><br>
	</form>
	<table cellpadding="5">
            <caption><h2> </h2></caption>
            <tr>
                <th> </th>
                <th> </th>
            </tr>
            <c:forEach items="${dataList}" var="dataItem">
            	<form action="ActionController" method="post">
			        <tr>
			            <td><b>${dataItem.getSender()} :</b></td>
			            <td>${dataItem.getMessage().getText()}</td>
			        </tr>
			        <input type="hidden" name="chatId" value="${dataItem.getChat_id()}">
			        <input type="hidden" name="sender" value="${dataItem.getSender()}">
			     </form>
		    </c:forEach>
        </table>
</div>
</body>
</html>