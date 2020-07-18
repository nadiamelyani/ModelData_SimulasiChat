<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Chat</title>
</head>
<body>
<div>
	<form action="ActionController" method="post">
		<input type="submit" name="action" value="cancel"><br>
		type something : <input type="text" name="message" value="${message}">
		<input type="hidden" name="chatId" value="${chatId}">
		<input type="hidden" name="sender" value="${sender}">
		<input type="submit" name="action" value="finish">
	</form>
</div>
</body>
</html>