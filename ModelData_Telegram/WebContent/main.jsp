<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="ISO-8859-1">
<title>Main</title>
</head>
<body>
	<h2>Hi!</h2>
	<form action="ActionController" method="post">
		<input type="submit" name="action" value="new chat">
		<input type="submit" name="action" value="saved message">
		<input type="submit" name="action" value="contact">
		<input type="submit" name="action" value="profile">
		<input type="submit" name="action" value="edit profile">
		<input type="submit" name="action" value="data usage">
		<input type="submit" name="action" value="log out"><br><br>
		search user : <input type="text" name="user">
		<input type="submit" name="action" value="search user"><br>
    </form>
    
    	<table cellpadding="5" margin-top="3">
            <tr>
                <th> </th>
                <th> </th>
                <th> </th>
            </tr>
            <c:forEach items="${dataList}" var="dataItem">
            	<form action="ActionController" method="post">
			        <tr>
			            <td><b>${dataItem.getContactName()}</b></td>
			            <td><input type="submit" name="action" value="continue chat">
			        	<td><input type="submit" name="action" value="end chat">
			        </tr>
			        <input type="hidden" name="name" value="${dataItem.getContactName()}">
			        <input type="hidden" name="phone" value="${dataItem.getContactPhone()}">
			     </form>
		    </c:forEach>
        </table>
	
</body>
</html>