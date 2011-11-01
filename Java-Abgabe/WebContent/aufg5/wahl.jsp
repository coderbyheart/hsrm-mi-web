<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8;">
<title>Belegung Veranstaltung</title>
</head>
<body>
	<h2>Belegung von Veranstaltungen</h2>
	<form method="POST" name="logout_form">
		<p>
			Eingeloggt als: 
			<c:out value="${vwb.loggedInUser.vorname}" /> 
			<c:out value="${vwb.loggedInUser.nachname}" />
			<input type="submit" name="logout" value="Logout">
		</p>
	</form>	
	
	<c:if test="${not empty errmessages }">
		<div class="errbox" style="color:red;">
				<c:forEach var="error" items="${errmessages}" >
				<p>
					<c:out value="${ error }"/>
				</p>
				</c:forEach>
		</div>
	</c:if>
	
	<b>Ihre gewählten Veranstaltungen</b>
	<table border="1px">
		<thead>
			<tr>
				<th>Modul ID</th>
				<th>Modul</th>
	        	<th>Veranstaltung ID</th>
	           	<th>Typ</th>
	           	<th>Max. Teilnehmer</th>
	            <th>Wochentag</th>
	            <th>Uhrzeit</th>
	            <th>Gruppe</th>
			</tr>
		</thead>
		<c:forEach var="belegung" items="${vwb.belegungen}" >
			<tr>
	            <td>${ belegung.modulid }</td>
	            <td>${ belegung.modulbez }</td>
	            <td>${ belegung.veranstaltungsid }</td>
	            <td>${ belegung.veranstaltungstyp }</td>	
	            <td>${ belegung.maximalTeilnehmer }</td>  
	            <td>${ belegung.firstGruppe.wochentag }</td> 
	            <td>${ belegung.firstGruppe.uhrzeit }</td> 
	            <td>${ belegung.firstGruppe.gruppe }</td>              
           	</tr>						
		</c:forEach>
	</table>
	<br/>
	<b>Veranstaltungen wählen/abwählen</b>
	<form method="POST" name="waehlen_form">
		<input type="submit" name="waehlen" value="Auswahl treffen">
		<table border="1px">
			<thead>
				<tr>
					<th>Modul ID</th>
					<th>Modul</th>
		        	<th>Veranstaltung ID</th>
		           	<th>Typ</th>
		            <th>Auswahl</th>
				</tr>
			</thead>
			<c:forEach var="item" items="${vwb.veranstaltungen}" >		
				<tr> 
					<td>${ item.modulid }</td>
		            <td>${ item.modulbez } </td>
		            <td>${ item.veranstaltungsid }</td>
		            <td>${ item.veranstaltungstyp }</td>
		           	<td>
		           		<select name="gruppen" size="1">
		           			<option value="${ item.veranstaltungsid }:none">Nicht belegt</option>
							<c:forEach var="gruppe" items="${item.gruppen}">
								<option value="${ item.veranstaltungsid }:${gruppe.gruppe}"
										<c:if test="${gruppe.checked}"> selected </c:if>>	
										<c:out value="Gruppe: ${ gruppe.gruppe } ${ gruppe.wochentag } ${ gruppe.uhrzeit } Plätze: ${ gruppe.teilnehmer }/${item.maximalTeilnehmer }" />
								</option>
							</c:forEach>
    					</select>
		           	</td>
	           	</tr>						
			</c:forEach>
		</table>
	</form>	
</body>
</html>