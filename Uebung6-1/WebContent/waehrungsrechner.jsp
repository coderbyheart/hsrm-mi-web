<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.io.*" import="java.util.*" import="javax.xml.parsers.*" import="org.w3c.dom.*" import="ueb6a1.FragebogenReader"%>
<jsp:include page="header-dyn.jsp">
	<jsp:param name="title"  value="Währungsrechner" />
</jsp:include>
<jsp:useBean id="rechner" type="beans.WaehrungsUmrechner" scope="session" />
	<section>
		<form method="get" action="WaehrungsUmrechner">
			<fieldset>
				<legend>Währungen umrechnen</legend>
				<p>
					Aktueller Kurs: 1 ${rechner.sourceCurrency} entspricht ${rechner.rate}  ${rechner.targetCurrency} <a href="?modify=1">ändern</a><br />
					${rechner.sourceValue} ${rechner.sourceCurrency} entspricht ${rechner.targetValue} ${rechner.targetCurrency}
				</p>
				<p>
					<label>Betrag in ${rechner.sourceCurrency}: 
					<input type="text" name="sourceValue" value="${rechner.sourceValue}" /></label> 
				</p>
				<p>
					<label><strong>oder</strong> Betrag in ${rechner.targetCurrency}:
					<input type="text" name="targetValue" value="${rechner.targetValue}" /></label> 
				</p>
				<p>
					<button type="submit">Berechnen</button>
				</p>
			</fieldset>
		</form>
	</section>
<jsp:include page="footer.jsp" />