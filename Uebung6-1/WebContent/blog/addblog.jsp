<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.io.*" import="java.util.*" import="javax.xml.parsers.*" import="org.w3c.dom.*" import="ueb6a1.FragebogenReader"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setBundle basename="blog.texte" />
<!-- TODO: use fmt message as param jsp include -->
<jsp:include page="../header-dyn.jsp">
	<jsp:param name="title" value="Einen Blogeintrag hinzufügen" />
</jsp:include>
	<h1>
		<fmt:message key="blog.add.title" /> 
	</h1>
	<form method="post" action="${pageContext.request.contextPath}/Blog/">
		<p>
			<label>
				<fmt:message key="blog.entry.title" />:<br />
				<input type="text" name="title" value="" />
			</label>
		</p>
		<p>
			<label>
				<fmt:message key="blog.entry.image" />:<br />
				<input type="text" name="image" value="" />
			</label>
		</p>
		<p>
			<label>
				<fmt:message key="blog.entry.text" />:<br />
				<textarea rows="10" cols="40" name="text"></textarea>
			</label>
		</p>
		<p>
			<button type="submit"><fmt:message key="blog.entry.add" /></button>
			<input type="hidden" name="addblog" value="1" />
		</p>
	</form>
	<p><a href="?logout=1">
		<fmt:message key="logout" />
	</a></p>
	<p><a href="?">
		<fmt:message key="blog.back" />
	</a></p>
<jsp:include page="../footer.jsp" />