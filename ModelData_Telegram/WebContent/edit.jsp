<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Edit Profile</title>
</head>
<body>
	<form action="ActionController" method="post">
		Update name : <input type="text" name="name" value="${name}"> <BR> 
		Update username : <input type="text" name="username" value="${username}"> <BR>
		Update bio : <input type="text" name="bio" value="${bio}"> <BR>
		<input type="hidden" name="action" value="submit edit profile">
		<input type="submit" />
		
		<input type="hidden" name="name" value="${name}">
		<input type="hidden" name="username" value="${username}">
		<input type="hidden" name="bio" value="${bio}">
		</form>	
</body>
</html>