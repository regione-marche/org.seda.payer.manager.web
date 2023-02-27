<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:div name="divCommonHeader" cssclass="Header">
	
	<s:image src="../applications/templates/shared/img/LogoRM.png" alt="MPay Regione Marche" width="127" height="40" cssclass="logomondo posleft block" />
	<s:image src="../applications/templates/shared/img/titolo.png" width="278" height="33" alt="Gestione archivi di base" cssclass="logotitolo posleft block" />

	<s:div name="divCred" cssclass="divCredenziali" >
		<c:if test="${!empty sessionScope.j_user_bean.nome}">
			<c:if test="${sessionScope.flagFedera == 'Y'}">
		    	<s:hyperlink name="buttonLogout" cssclass="logout" href="../login/logoffAuthFederataRegMarche.do" text="ESCI" /> <!--  LP PG200060 da logoffAuthFederata.do logoffAuthFederataRegMarche.do -->
		    </c:if>
		    <c:if test="${sessionScope.flagFedera == 'N'}">
		    	<s:hyperlink name="buttonLogout" cssclass="logout" href="../login/logoff.do" text="ESCI" />
		    	
		    </c:if>
		    <s:image width="27" height="45" alt="login" src="../applications/templates/shared/img/loginMan.png"  cssclass="imgCredenziali" />
			<s:div name="divWelcome" cssclass="divWelcome">
				<s:label name="lblNome" text="Benvenuto, ${sessionScope.j_user_bean.nome}" cssclass="spCredential bold"/> <s:label name="lblCognome" text="${sessionScope.j_user_bean.cognome}" cssclass="spCredential bold"/><br/>
				<s:label name="lblCf"   text="<b>C.F. </b> ${sessionScope.j_user_bean.codiceFiscale}" cssclass="spCredential spanCF" /> 
			</s:div>
		</c:if>
   </s:div>
   
    <c:if test="${sessionScope.listaVisibileProfili == 'Y' && !empty sessionScope.profili_utente && !empty sessionScope.userBeanInitProf}">
		<s:div name="divCred" cssclass="divCredenziali" >
	   		<input type="hidden" id="nextProf" name="nextProf" value="${sessionScope.nextProf}">
	   		<input type="hidden" id="userBeanInitProf" name="userBeanInitProf" value="${sessionScope.userBeanInitProf}">
	   		
	   		<script type="text/javascript">
	   		
		   		window.addEventListener('load', function() {
		   			var ddlValue = "";

		   			//Se è il primo accesso, viene visualizzato l'attuale profilo sulla ddl
		   			if(document.getElementById("userBeanInitProf")!=null) {
				   		
			   			ddlValue = document.getElementById("userBeanInitProf").value;
				   	}
		   			//Se non è il primo accesso, viene visualizzato il profilo selezionato dalla ddl (sovrascrivo)
			   		if(document.getElementById("nextProf")!=null) {
		   				ddlValue = document.getElementById("nextProf").value;
			   		} 
				   	
			   		var ddl = document.getElementById("ddlProfiliTest");
		   			for (var i = 0; i < ddl.options.length; i++) {
		   			    if (ddl.options[i].value === ddlValue) {
		   			    	ddl.selectedIndex = i;
		   			        break;
		   			    }
		   			}
		   			
			   	});
	   		
		   		function submitCambiaProfilo() {
		   			var userSelected = document.getElementById("ddlProfiliTest").value;
		   			var buttonLogout = document.getElementById("buttonLogout");
		   			//i campi vanno messi prima del token altrimenti non viene effettuato il redirect al logout
		   			var token = buttonLogout.getAttribute("href").substring(buttonLogout.getAttribute("href").lastIndexOf("csrfToken="));
		   			var link = "../default/sceltaProfilo.do?profilo="+userSelected+"&"+token+"&"+"tx_button_avanti=1";
		   			buttonLogout.setAttribute("href",link);
		   			buttonLogout.click();
		   		}
		   		
	   		</script>
	   	
	   		<label for="ddlProfiliTest" class="label85 bold floatleft textright ddlProfiliTestRM">Profilo: </label>
		   <select class="tbddlRM floatleft ddlProfiliTestRM" name="ddlProfiliTest" id="ddlProfiliTest" onchange="submitCambiaProfilo()">
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
						
						<option value="${profiloItem.chiaveUtente}" ${profiloItem.chiaveUtente == sessionScope.userBeanInitProf ? 'selected="selected"' : ''}>
							${descrizione}
							<c:if test="${profiloItem.descrEnte != ''}">
								- ${profiloItem.descrEnte}
							</c:if>
							
							
						</option>
						
					</c:if>
			   </c:forEach>
		   </select>
		</s:div>		   
	</c:if>
	
</s:div>	
