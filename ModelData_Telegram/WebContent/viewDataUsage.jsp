<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile</title>
</head>
<body>

<div>
	<form action="ActionController" method="post">
		<input type="submit" name="action" value="main">
    </form>
	<table cellpadding="5">
            <caption><h3>Message</h3></caption>
            <tr>
                <th> </th>
                <th> </th>
            </tr>
            	<form action="ActionController" method="post">
			        <tr>
			            <td><b>Sended Message :</b></b></td>
			            <td>${sended}</td>
			        </tr>
			        <tr>
			            <td><b>Received Message :</b></td>
			            <td>${received}</td>
			        </tr>>
			     </form>
        </table>