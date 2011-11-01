<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;">
<title>Login - Belegung Veranstaltung</title>
</head>
<body>
	
	<h2>Login zur Belegung von Veranstaltungen</h2>
	<form method="POST"> 
	Username: <input type="text" name="username" value=""/><br/>
	Password: <input type="password" name="password" value=""/><br/>
	<input type="submit" name="login" value="Login" /> 
	</form>
	<c:if test="${not empty errmessages }">
		<div class="errbox" style="color:red;">
			<c:forEach var="error" items="${errmessages}" >
				<c:out value="${ error }"/>
			</c:forEach>
		</div>
	</c:if>
</body>
</html>