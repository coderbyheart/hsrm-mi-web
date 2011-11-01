<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setBundle basename="ueb07-2.texte" />
<jsp:useBean id="user" scope="session" type="uebung.ueb07.User" />
<!-- TODO: use fmt message as param jsp include -->
<jsp:include page="header-dyn.jsp">
	<jsp:param name="title" value="Blog" />
</jsp:include>
	<h1>
		<fmt:message key="app.title">
			<fmt:param>
				<fmt:message key="login" />
			</fmt:param>
		</fmt:message> 
	</h1>
	<section>
		<form method="post" action="${pageContext.request.contextPath}/Blog/">
			<fieldset>
				<legend>
					Bitte einloggen
				</legend>
				<c:if test="${user.loginState gt 1}">
					<p class="error">Fehler: <fmt:message key="user.loginerror.${user.loginState}" /></p>
				</c:if>
				<p>
					<label>Nutzername:<br /><input type="text" name="email" placeholder="user@domain.tld" value="${param.email}" /></label>
				</p>
				<p> 
					<label>Passwort:<br /><input type="password" name="password" placeholder="your password" value="" /></label>
				</p>
				<p> 
					<button type="submit">Login</button>
					<input type="hidden" name="login" value="1" />
				</p>
			</fieldset>
		</form>
	</section>
<jsp:include page="footer.jsp" />