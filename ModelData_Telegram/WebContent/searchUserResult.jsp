<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Contact</title>
</head>
<body>

<div>
	<form action="ActionController" method="post">
		<input type="submit" name="action" value="contact">
		<input type="submit" name="action" value="main"><br><br>
		search user : <input type="text" name="user">
		<input type="submit" name="action" value="search user"><br>
    </form>
	
	<table border="1" cellpadding="5">
            <caption><h2>Search Result</h2></caption>
            <tr>
                <th>Name</th>
                <th>Username</th>
                <th>Chat</th>
            </tr>
            <c:forEach items="${dataList}" var="dataItem">
            	<form action="ActionController" method="post">
			        <tr>
			            <td>${dataItem.getName()}</td>
			            <td>${dataItem.getUsername()}</td>
			            <td><input type="submit" name="action" value="chat"></td>
			        </tr>
			        <input type="hidden" name="name" value="${dataItem.getName()}">
			        <input type="hidden" name="phone" value="${dataItem.getPhone()}">
			     </form>
		    </c:forEach>
        </table>
</div>

</body>
</html>