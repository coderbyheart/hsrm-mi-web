<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>
<%@ include file="header.jsp"%>
<h1>Zitate</h1>
<p>Hier zum Beispiel drei Zitate:</p>
<my:zitat>less is more</my:zitat>
<my:zitat author="Terry Pratchett" source="The Light Fantastic">
Unseen University had never admitted women, muttering something about
problems with the plumbing, but the real reason was an unspoken dread
that if women were allowed to mess around with magic they would
probably be embarrassingly good at it ...
</my:zitat>
<my:zitat author="Max Grundig" location="CeBit"
year="1982">
Scheiß-Japaner
</my:zitat>
<p>Das wars.</p>
<%@ include file="footer.jsp"%>