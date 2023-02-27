<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:div name="divCommonHeader" cssclass="Header">
	<s:image src="../applications/templates/shared/img/globo.png" width="51" height="50" alt="Logo Mondo" cssclass="logomondo posleft block" />
	<s:image src="../applications/templates/shared/img/titolo.png" width="278" height="33" alt="Gestione archivi di base" cssclass="logotitolo posleft block" />

	<s:div name="divCred" cssclass="divCredenziali" >
		<c:if test="${!empty sessionScope.j_user_bean.nome}">
		    <s:hyperlink  cssclass="logout" href="../login/cambioPswd.do" text="CAMBIO PASSWORD" />
		    <s:hyperlink  cssclass="logout" href="../login/logoff.do" text="LOGOUT" />
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
