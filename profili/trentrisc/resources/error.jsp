<%------------------------------------------------------------------------
Copyright (C) 2011 CEFRIEL
 
Title to Software and all associated intellectual property rights is retained by 
"CEFRIEL Societa' Consortile a Responsabilita' Limitata" located in via Renato
Fucini 2, 20133 Milano (Italy)
 
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 	
COVERED CODE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT
WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, WITHOUT 
LIMITATION, WARRANTIES THAT THE COVERED CODE IS FREE OF DEFECTS, MERCHANTABLE,
FIT FOR A PARTICULAR PURPOSE OR NON-INFRINGING. THE ENTIRE RISK AS TO THE
QUALITY AND PERFORMANCE OF THE COVERED CODE IS WITH YOU. SHOULD ANY COVERED
CODE PROVE DEFECTIVE IN ANY RESPECT, YOU (NOT THE INITIAL DEVELOPER OR ANY
OTHER CONTRIBUTOR) ASSUME THE COST OF ANY NECESSARY SERVICING, REPAIR OR
CORRECTION.
------------------------------------------------------------------------%>
<%@ page isErrorPage="true"%>
<%@ page language="java" contentType="text/html"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="it" dir="ltr">
	<head>
		<link rel="stylesheet" href="styles/error.css" />
		<link rel="stylesheet" href="styles/color.css" />
		<link rel="stylesheet" href="styles/style.css" />
		<title>ERRORE</title>
	</head>
	<body>
		<div>
			<div>
				<div class="Testata">
					<img class="logo" src="images/Headersx.png" width="800" height="140" /> 
					<div class="Testata_Sfondo">
						<a href="http://www.provincia.tn.it/"> <img src="images/logo_provincia.gif" width="177" height="75" alt="Il Portale Della Provincia Autonoma Di Trento" /></a>
					</div>
				</div>
			</div>
			<%
				String errorMessage = (String) request.getAttribute("errorMessage");
			%>

			<div id="content" style="text-align:left">
				<h2>
					Si &egrave; verificato un errore
				</h2>
				<p>
					<c:choose>
						<c:when test="${errorMessage != null}">
							<b><c:out value='${errorMessage}' /> </b>
							<br/>
						</c:when>
						<c:otherwise>
							<b>Si &egrave; verificato un errore in fase di autenticazione. Impossibile proseguire.</b>
							<br/>
						</c:otherwise>
					</c:choose>
				</p>
				<%
					boolean showDetails = new Boolean((String) application.getInitParameter("showDetailInErrorPage")).booleanValue();
					if (showDetails && request.getAttribute("javax.servlet.error.exception") != null) {
				%>
					<h2>
						Errore durante l'esecuzione di
						<b><%=request.getAttribute("javax.servlet.error.request_uri")%></b>
					</h2>
					<p>
						<b><%=exception.getClass().getName() + ": " + exception.getMessage()%></b>
					</p>
					<p>
					<%
						Throwable e = (Throwable) request.getAttribute("javax.servlet.error.exception");
						StackTraceElement[] stack = e.getStackTrace();
						for (int n = 0; n < Math.min(5, stack.length); n++) {
							out.write(stack[n].toString());
							out.write("<br/>");
						}
						out.write("<hr />");
						e = (e instanceof ServletException) ? ((ServletException) e).getRootCause() : e.getCause();
						if (e != null) {
							out.write("Cause: <b>" + e.getClass().getName() + "</b><p> [ " + e.getMessage() + " ] </p>");
							stack = e.getStackTrace();
							for (int n = 0; n < Math.min(5, stack.length); n++) {
								out.write(stack[n].toString());
								out.write("<br/>");
							}
						}
					%>
					</p>
				<%
					}
				%>
			</div>
			<a name="inizio_footer"></a>
			<div>
			<div class="pie_di_pagina">
					<img class="footer" src="images/Footerdx.png" />
					<div> 
						<span>PROVINCIA AUTONOMA DI TRENTO</span> - Piazza Dante,15 - 38122 Trento (It) - tel. +39 0461 495111 - <span>numero verde 800 903606</span> - C.F. e P.IVA <span>00337460224</span>
						<br />
					</div>
				</div>
			</div>
		</div>
	</body>
</html>