<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="urlLogout" scope="page" value="../login/logoff.do"></c:set>
<c:if test="${!empty sessionScope.j_user_bean.nome && sessionScope.j_user_bean.tipoAutenticazione=='F'}">
	<c:set var="urlLogout" scope="page" value="../login/logoffAuthFederataaosta.do"></c:set>
</c:if>

<s:div name="divCommonHeader" cssclass="HeaderAosta">

	
	<s:image src="../applications/templates/shared/aosta/img/logo_aosta.png" width="192" height="92" alt="Valle d'Aosta" cssclass="logomondo posleft block" />
	<s:image src="../applications/templates/shared/img/titolo.png" width="278" height="33" alt="Gestione archivi di base" cssclass="logotitolo posleft block" />

	<s:div name="divCred" cssclass="divCredenziali2" >
		
		<c:if test="${!empty sessionScope.j_user_bean.nome}">
		    <c:choose> 
			<c:when test="${sessionScope.urlPortale != null &&  sessionScope.urlPortale!= ''}">
				<s:hyperlink  cssclass="logout2" href="../login/logoff.do?urlPortale=${sessionScope.urlPortale}" text="TORNA AL PORTALE" />
			</c:when>
			<c:otherwise>
				<c:if test="${sessionScope.j_user_bean.tipoAutenticazione=='P'}">
				    <s:hyperlink  cssclass="logout2" href="../login/cambioPswd.do" text="CAMBIO PASSWORD" />
				</c:if>
				<s:hyperlink  name="buttonLogout" cssclass="logout2" href="../login/logoff.do" text="LOGOUT" />
			</c:otherwise>
			</c:choose>
	
	
		   
		    
		    <s:image width="27" height="45" alt="login" src="../applications/templates/shared/img/loginMan.png"  cssclass="imgCredenziali" />
			<s:div name="divWelcome" cssclass="divWelcome">
				<c:if test="${sessionScope.j_user_bean.nome != 'NTEST' && sessionScope.j_user_bean.cognome != 'CTEST'}">
					<s:label name="lblNome" text="Benvenuto, ${sessionScope.j_user_bean.nome}" cssclass="spCredential bold"/> <s:label name="lblCognome" text="${sessionScope.j_user_bean.cognome}" cssclass="spCredential bold"/><br/>
				</c:if>
				<s:label name="lblCf"   text="<b>C.F. </b> ${sessionScope.j_user_bean.codiceFiscale}" cssclass="spCredential spanCF" /> 
			</s:div>
		</c:if>
		
		<c:if test="${empty sessionScope.j_user_bean.nome}">
		    <s:hyperlink  cssclass="logout2" href="../login/resetPswd.do" text="RESET PASSWORD" />
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
	   	
	   		<label for="ddlProfiliTest" class="label85 bold floatleft textright ddlProfiliTest">Profilo: </label>
		   <select class="tbddl floatleft ddlProfiliTestDLL" name="ddlProfiliTest" id="ddlProfiliTest" onchange="submitCambiaProfilo()">
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
   
   
<%--    <s:div name="divFontSize" cssclass="divDisplayNone"> --%>
<%-- 		<s:button onclick="decreaseFontSize();" type="button" id="btnDecreaseFontSize" text="A-" cssclass="linkFontSizeReduce" /> --%>
<%-- 		<s:button onclick="increaseFontSize();" type="button" id="btnIncreaseFontSize" text="A+" cssclass="linkFontSizeIncrease" /> --%>
<%-- 	</s:div> --%>
	
</s:div>	
