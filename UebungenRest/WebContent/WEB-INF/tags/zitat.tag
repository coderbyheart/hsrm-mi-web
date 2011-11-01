<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="author" %>
<%@ attribute name="source" %>
<%@ attribute name="location" %>
<%@ attribute name="year" %>
<jsp:doBody var="cite" />
<blockquote>
	<p><c:out value="${cite}" /></p>
	<p>
		<%-- <c:out value="${not empty author ? author : "Unknown"}"> --%>
		<c:if test="${empty author}">
			<c:set var="author" value="Unknown" />
		</c:if>
		<strong>-- <c:out value="${author}" /></strong>
		<c:if test="${not empty source}">
			, &quot;<c:out value="${source}" />&quot;
		</c:if>
		<c:if test="${not empty location}">
			, <c:out value="${location}" />
		</c:if>
		<c:if test="${not empty year}">
			<c:out value="${year}" />
		</c:if>
	</p>
</blockquote>