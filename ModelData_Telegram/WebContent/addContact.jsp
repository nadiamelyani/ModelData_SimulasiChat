<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Add Contact</title>
</head>
<body>
	<form action="ActionController" method="post">
		Enter phone : <input type="text" name="phone"> <BR>
		Enter name : <input type="text" name="name"> <BR>
		<input type="hidden" name="action" value="insert contact">
		<input type="submit" />
	</form>
</body>
</html>