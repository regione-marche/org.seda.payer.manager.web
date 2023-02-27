<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>

<s:div name="div_selezione" cssclass="divProfiloOuter">

	<s:div name="divRicercaTitleName" cssclass="divRicTitle div_align_center bold">Selezione del profilo</s:div>
	<br/>
	<s:form name="sceltaProfilo_form" action="sceltaProfilo.do" method="post" 
	hasbtn1="false" hasbtn2="false" hasbtn3="false" >

	<c:if test="${!empty sessionScope.profili_utente}">
		
	   <c:forEach var="profiloItem" items="${sessionScope.profili_utente}">
	   		<c:set var="descrizione" scope="page" value="" />
	   		<c:if test="${profiloItem.descProfilo != 'PYCO'}">
		   		<c:choose>
		   			<c:when test="${profiloItem.descProfilo == 'AMMI'}">
		   				<c:set var="descrizione" scope="page" value="Amministratore C.S.I." />
		   			</c:when>
		   			<c:when test="${profiloItem.descProfilo == 'AMSO'}">
		   				<c:set var="descrizione" scope="page" value="Amministratore Società" />
		   			</c:when>
		   			<c:when test="${profiloItem.descProfilo == 'AMUT'}">
		   				<c:set var="descrizione" scope="page" value="Amministratore Utente" />
		   			</c:when>
		   			<c:when test="${profiloItem.descProfilo == 'AMEN'}">
		   				<c:set var="descrizione" scope="page" value="Amministratore Ente" />
		   			</c:when>
		   			<c:when test="${profiloItem.descProfilo == 'OPER'}">
		   				<c:set var="descrizione" scope="page" value="Operatore" />
		   			</c:when>
		   		</c:choose>
	   		
				<s:div name="div_${profiloItem.chiaveUtente}" cssclass="divProfilo">
					<s:list name="profilo_${profiloItem.chiaveUtente}" cssclass="checkleft rbProfilo" bradio="true" groupname="profilo" 
							text="${descrizione}" cssclasslabel="rbProfiloLbl" 
							bchecked="false" value="${profiloItem.chiaveUtente}"
							validator="ignore" />	
					<s:div name="divDescr_${profiloItem.chiaveUtente}" cssclass="divDescrProfilo">
						${profiloItem.descrSocieta}
						<c:if test="${profiloItem.descrUtente != ''}">
							- ${profiloItem.descrUtente}
							<c:if test="${profiloItem.descrEnte != ''}">
								- ${profiloItem.descrEnte}
							</c:if>
						</c:if>
					</s:div>
				</s:div>
			</c:if>
	   </c:forEach>

	</c:if>		
	
	<s:div name="divRicercaBottoni" cssclass="divRicBottoni divMarginTop">
		<s:button id="tx_button_avanti" onclick="" text="Avanti" type="submit" cssclass="btnStyle" />
	</s:div>

	</s:form>	
</s:div>