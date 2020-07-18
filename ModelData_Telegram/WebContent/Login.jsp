<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome - Login</title>
</head>
<body>
	<form action="ActionController" method="post">
		Enter phone : <input type="text" name="phone"> <BR>
		Enter password : <input type="password" name="password"> <BR>
		<input type="submit" name="action" value="Login" />
		<p> or <br>
		<input type="submit" name="action" value="Create Account"/>
	</form>
 
</body>
</html>