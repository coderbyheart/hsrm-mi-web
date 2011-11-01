<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setBundle basename="ueb07-2.texte" />
<!-- TODO: use fmt message as param jsp include -->
<jsp:include page="header-dyn.jsp">
	<jsp:param name="title" value="Liste der Blogs" />
</jsp:include>
<jsp:useBean id="bloglist" type="uebung.ueb07.BlogList" scope="session" />
	<h1>
		<fmt:message key="bloglist.title" /> äöü
	</h1>
	<c:forEach items="${bloglist.blogs}" var="blog">
		<h2>${blog.title}</h2>
		<p>Verfasst am <fmt:formatDate value="${blog.date}" pattern="dd.MM.yyyy" /></p>
		<p><img src="${blog.image}" alt="" /></p>
		<p>
			${blog.text}
		</p>
	</c:forEach>
	<p><a href="?logout=1">
		<fmt:message key="logout" />
	</a></p>
	<p><a href="?add=1">
		<fmt:message key="blog.add" />
	</a></p>
<jsp:include page="footer.jsp" />