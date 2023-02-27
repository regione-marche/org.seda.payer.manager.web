<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="urlLogout" scope="page" value="../login/logoff.do"></c:set>
<s:div name="divCommonHeader" cssclass="HeaderCreset">
	<s:image src="../applications/templates/shared/creset/img/logo_creset.png" width="237" height="66" alt="Creset" cssclass="logomondo posleft block" />
	
	<s:div name="divCred" cssclass="divCredenziali" >
		<c:if test="${(sessionScope.urlPortale == null || empty sessionScope.urlPortale) && !empty sessionScope.j_user_bean.nome && sessionScope.j_user_bean.tipoAutenticazione=='P'}">
			 <s:hyperlink  cssclass="logout" href="../login/cambioPswd.do" text="CAMBIO PASSWORD" />
		</c:if>
	
		<c:if test="${!empty sessionScope.j_user_bean.nome}">
		    
		
		   <c:choose> 
			<c:when test="${sessionScope.urlPortale != null &&  sessionScope.urlPortale!= ''}">
				<s:hyperlink  cssclass="logout" href="../login/logoff.do?urlPortale=${sessionScope.urlPortale}" text="TORNA AL PORTALE" />
			</c:when>
			<c:otherwise>
				<s:hyperlink  cssclass="logout" href="../login/logoff.do" text="LOGOUT" />
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
		    <s:hyperlink  cssclass="logout" href="../login/resetPswd.do" text="RESET PASSWORD" />
		</c:if>
   </s:div>
   
	
</s:div>	
