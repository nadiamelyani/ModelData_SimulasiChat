<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Blocked Contact</title>
</head>
<body>

<div>
	<form action="ActionController" method="post">
		<input type="submit" name="action" value="back to contact">
		<input type="submit" name="action" value="blocked contact">
		<input type="submit" name="action" value="main">
    </form>
	
	<table border="1" cellpadding="5">
            <caption><h2>Contact</h2></caption>
            <tr>
                <th>Name</th>
                <th>Phone Number</th>
                <th>Unblock</th>
            </tr>
            <c:forEach items="${dataList}" var="dataItem">
            	<form action="ActionController" method="post">
			        <tr>
			            <td>${dataItem.getContactName()}</td>
			            <td>${dataItem.getContactPhone()}</td>
			            <td><input type="submit" name="action" value="unblock"></td>
			        </tr>
			        <input type="hidden" name="name" value="${dataItem.getContactName()}">
			        <input type="hidden" name="phone" value="${dataItem.getContactPhone()}">
			     </form>
		    </c:forEach>
        </table>
</div>

</body>
</html>