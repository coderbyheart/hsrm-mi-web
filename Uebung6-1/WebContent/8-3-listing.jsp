<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="header-dyn.jsp">
	<jsp:param name="title" value="Liste der Bilder" />
</jsp:include>
	<ul>
	<c:forEach items="${listing}" var="entry">
		<li><a href="?show=${entry}">${entry}</a></li>
	</c:forEach>
	</ul>
<jsp:include page="footer.jsp" />