<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Edit Contact</title>
</head>
<body>
	<form action="ActionController" method="post">
		Update name : <input type="text" name="name" value="${contactName}"> <BR> 
		<input type="hidden" name="action" value="submit edit contact">
		<input type="submit" />
		</form>	
</body>
</html>