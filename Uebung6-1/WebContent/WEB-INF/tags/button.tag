<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="url" %>
<%@ attribute name="type" required="false" %>
<jsp:doBody var="label" />
<c:if test="${empty type}">
	<c:set var="type" value="link" />
</c:if>

<c:if test="${type eq 'link'}">
	<a href="<c:url value="${url}" />">
		<img src="<c:url value="/ImageButton?text=${label}" />" alt="<c:out value="${label}" />" />
	</a>
</c:if>
<c:if test="${type eq 'button'}">
	<button><c:out value="${label}" /></button>
</c:if>