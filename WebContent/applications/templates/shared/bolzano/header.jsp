<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="urlLogout" scope="page" value="../login/logoff.do"></c:set>
<c:if test="${!empty sessionScope.j_user_bean.nome && sessionScope.j_user_bean.tipoAutenticazione=='F'}">
	<c:set var="urlLogout" scope="page" value="../login/logoffAuthFederataBolzano.do"></c:set>
</c:if>

<s:div name="divCommonHeader" cssclass="Header">

	
	<s:image src="../applications/templates/shared/bolzano/img/logoBolzano.png" width="39" height="50" alt="Bolzano" cssclass="logomondo posleft block" />
	<s:image src="../applications/templates/shared/img/titolo.png" width="278" height="33" alt="Gestione archivi di base" cssclass="logotitolo posleft block" />

	<s:div name="divCred" cssclass="divCredenziali" >
		<c:if test="${!empty sessionScope.j_user_bean.nome && sessionScope.j_user_bean.tipoAutenticazione=='P'}">
			 <s:hyperlink  cssclass="logout" href="../login/cambioPswd.do" text="CAMBIO PASSWORD" />
		</c:if>
	
		<c:if test="${!empty sessionScope.j_user_bean.nome}">
		    
		
		    <s:hyperlink  cssclass="logout" href="${pageScope.urlLogout}" text="ESCI" />
	
		   
		    
		    <s:image width="27" height="45" alt="login" src="../applications/templates/shared/img/loginMan.png"  cssclass="imgCredenziali" />
			<s:div name="divWelcome" cssclass="divWelcome">
				<s:label name="lblNome" text="Benvenuto, ${sessionScope.j_user_bean.nome}" cssclass="spCredential bold"/> <s:label name="lblCognome" text="${sessionScope.j_user_bean.cognome}" cssclass="spCredential bold"/><br/>
				<s:label name="lblCf"   text="<b>C.F. </b> ${sessionScope.j_user_bean.codiceFiscale}" cssclass="spCredential spanCF" /> 
			</s:div>
		</c:if>
		
		<c:if test="${empty sessionScope.j_user_bean.nome}">
		    <s:hyperlink  cssclass="logout" href="../login/resetPswd.do" text="RESET PASSWORD" />
		</c:if>
   </s:div>
   
	
</s:div>	
