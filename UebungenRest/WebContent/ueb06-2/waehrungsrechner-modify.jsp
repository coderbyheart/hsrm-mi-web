<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.io.*" import="java.util.*" import="javax.xml.parsers.*" import="org.w3c.dom.*"%>
<jsp:include page="header-dyn.jsp">
	<jsp:param name="title"  value="Währungsrechner" />
</jsp:include>
<jsp:useBean id="rechner" type="uebung.ueb06.WaehrungsUmrechnerBean" scope="session" />
	<section>
		<form method="post" action="WaehrungsUmrechner">
			<fieldset>
				<legend>Währungen einstellen</legend>
				
				<p>
					<label>Quell-Währung:<br /> 
					<input type="text" name="sourceCurrency" value="${rechner.sourceCurrency}" /></label> 
				</p>
				<p>
					<label>Ziel-Währung:<br /> 
					<input type="text" name="targetCurrency" value="${rechner.targetCurrency}" /></label> 
				</p>
				<p>
					<label>Umrechnungskurs:<br /> 
					1 Einheit in der Quellwährung entspricht <input type="text" name="rate" value="${rechner.rate}" /> Einheiten in der Zielwährung</label> 
				</p>
				<p>
					<button type="submit">Ändern</button>
					<input type="hidden" name="modify" value="1" />
				</p>
			</fieldset>
		</form>
	</section>
<jsp:include page="footer.jsp" />