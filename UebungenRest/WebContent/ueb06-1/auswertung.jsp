<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.io.*" import="java.util.*" import="javax.xml.parsers.*" import="org.w3c.dom.*" import="uebung.ueb06.FragebogenReader"%>
<%

	SortedMap<String, SortedMap<String, Integer>> fragen = (SortedMap<String, SortedMap<String, Integer>>)session.getAttribute("fragen");
	
	HashMap<String, String> answers = (HashMap<String, String>)session.getAttribute("answers");
	
	int numPoints = 0;
	HashMap<String, Integer> maxPointsFrage = new HashMap<String, Integer>(); 
	for (String frage: fragen.keySet()) {
		maxPointsFrage.put(frage, 0);
		Map<String, Integer> antworten = fragen.get(frage);
		for (String antwort : antworten.keySet()) {
			int pts = antworten.get(antwort);
			if (maxPointsFrage.get(frage) < pts) {
				maxPointsFrage.put(frage, pts);
			}
			if (answers.get(frage).equals(antwort)) numPoints += pts; 
		} 
	}
	int maxPoints = 0;
	for (Integer pts: maxPointsFrage.values()) maxPoints += pts; 

%>
<%@ include file="header.jsp" %>
<p>Anzahl der Fragen: <%= fragen.size() %></p>
<p>Erreichbare Punkte: <%= maxPoints %></p>
<p>Erreichte Punkte: <%= numPoints %></p>
<p><a href="<%= response.encodeURL("fragebogen.jsp?restart=1") %>">nochmal</a></p>
<%@ include file="footer.jsp" %>