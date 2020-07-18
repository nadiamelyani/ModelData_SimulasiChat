<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Create New Account</title>
</head>
<body>
	<form action="ActionController" method="post">
		Enter phone : <input type="text" name="phone"> <BR>
		Enter name : <input type="text" name="name"> <BR>
		Enter new password : <input type="password" name="new_password"> <BR>
		Confirm password  : <input type="password" name="confirm_password"> <BR>
		<input type="hidden" name="action" value="create">
		<input type="submit" />
	</form>
</body>
</html>