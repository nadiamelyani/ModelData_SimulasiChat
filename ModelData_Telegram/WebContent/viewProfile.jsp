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
		<input type="submit" name="action" value="contact">
    </form>
	
	<table cellpadding="5">
            <caption><h2>Profile</h2></caption>
            <tr>
                <th> </th>
                <th> </th>
            </tr>
            <c:forEach items="${dataList}" var="dataItem">
            	<form action="ActionController" method="post">
			        <tr>
			            <td><b>Name :</b></b></td>
			            <td>${dataItem.getName()}</td>
			        </tr>
			        <tr>
			            <td><b>Phone :</b></td>
			            <td>${dataItem.getPhone()}</td>
			        </tr>
			        <tr>
			            <td><b>Username :</b></td>
			            <td>${dataItem.getUsername()}</td>
			        </tr>
			        <tr>
			            <td><b>Bio :</b></td>
			            <td>${dataItem.getBio()}</td>
			        </tr>
			        <input type="hidden" name="name" value="${dataItem.getName()}">
			        <input type="hidden" name="phone" value="${dataItem.getPhone()}">
			        <input type="hidden" name="username" value="${dataItem.getUsername()}">
			        <input type="hidden" name="bio" value="${dataItem.getBio()}">
			     </form>
		    </c:forEach>
        </table>
</div>

</body>
</html>