<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.io.*" import="java.util.*" import="javax.xml.parsers.*" import="org.w3c.dom.*" import="ueb6a1.FragebogenReader"%>
<%!
	String getParamUTF8(HttpServletRequest request, String name) {
		String param = request.getParameter(name);
		if (param == null) return null;
		try {
			return new String(request.getParameter(name).getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return param;
		}
	}
%>
<%

	SortedMap<String, SortedMap<String, Integer>> fragen = (SortedMap<String, SortedMap<String, Integer>>)session.getAttribute("fragen");
	if (fragen == null) { 
		
		String xmlfile = application.getInitParameter("fragebogen");
		InputStream is = application.getResourceAsStream(xmlfile);
		fragen = FragebogenReader.getFragebogen(is);
		session.setAttribute("fragen", fragen);
	}
	
	HashMap<String, String> answers = (HashMap<String, String>)session.getAttribute("answers");
	if (answers == null || request.getParameter("restart") != null) answers = new HashMap<String, String>();
	
	String answer = getParamUTF8(request, "answer");
	if (answer != null) {
		answers.put(getParamUTF8(request, "question"), answer);
	}
	session.setAttribute("answers", answers);
	
	boolean complete = answers.size() >= fragen.size();

%>
<%@ include file="header.jsp" %>
		<% if (complete) { %>
		<section>
			<p>Geschafft!</p>
			<p><a href="<%= response.encodeURL("auswertung.jsp") %>">zur Auswertung</a>.</p>
		</section>
		<% } else { %>
		<section>
			<form method="post" action="<%= response.encodeURL("fragebogen.jsp") %>">
			<%
			for (String frage: fragen.keySet()) {
				if (answers.containsKey(frage)) continue;
				%>
				<h2><%= frage %></h2>
				<%
				Map<String, Integer> antworten = fragen.get(frage);
				for (String antwort : antworten.keySet()) {
					int wert = antworten.get(antwort);
					%>
					<p>
						<label><input type="radio" name="answer" value="<%= antwort %>" /> <%= antwort %></label>
					</p>
					<%
				}
				%><p><button type="submit" name="submit" value="1">weiter</button><input type="hidden" name="question" value="<%= frage %>" /></p><%
				break;
			}
			%>
			</form>
		<section>
		<%  } %>
<%@ include file="footer.jsp" %>