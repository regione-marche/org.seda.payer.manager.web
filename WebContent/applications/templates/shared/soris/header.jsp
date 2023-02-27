<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<s:div name="divCommonHeader" cssclass="Header">
	
	<s:image src="../applications/templates/shared/soris/img/LogoSoris.png" alt="Soris" width="118" height="40" cssclass="logomondo posleft block" />
	<s:image src="../applications/templates/shared/img/titolo.png" width="278" height="33" alt="Gestione archivi di base" cssclass="logotitolo posleft block" />

	<s:div name="divCred" cssclass="divCredenziali" >
		<c:if test="${!empty sessionScope.j_user_bean.nome}">
		<c:choose> 
			<c:when test="${sessionScope.urlPortale != null &&  sessionScope.urlPortale!= ''}">
				<s:hyperlink  cssclass="logout" href="../login/logoff.do?urlPortale=${sessionScope.urlPortale}" text="TORNA AL PORTALE" />
			</c:when>
			<c:otherwise>
				<s:hyperlink  cssclass="logout" href="../login/cambioPswd.do" text="CAMBIO PASSWORD" />
				<s:hyperlink  cssclass="logout" href="../login/logoff.do" text="LOGOUT" />
			</c:otherwise>
		</c:choose>
		    
		    <c:if test="${fn:contains(sessionScope.nascondiCredenziali,'N')}">
			    <s:image width="27" height="45" alt="login" src="../applications/templates/shared/img/loginMan.png"  cssclass="imgCredenziali" />
				<s:div name="divWelcome" cssclass="divWelcome">
				 <c:if test="${sessionScope.j_user_bean.nome != 'NTEST' && sessionScope.j_user_bean.cognome != 'CTEST'}">
					<s:label name="lblNome" text="Benvenuto, ${sessionScope.j_user_bean.nome}" cssclass="spCredential bold"/> <s:label name="lblCognome" text="${sessionScope.j_user_bean.cognome}" cssclass="spCredential bold"/><br/>
				</c:if>
					<s:label name="lblCf"   text="<b>C.F. </b> ${sessionScope.j_user_bean.codiceFiscale}" cssclass="spCredential spanCF" /> 
				</s:div>
			</c:if>
		</c:if>
		
		<c:if test="true">  <!-- Con portale CAS in produzione, mettere "true" -->
			<c:if test="${empty sessionScope.j_user_bean.nome}">
			    <s:hyperlink  cssclass="logout" href="../login/resetPswd.do" text="RESET PASSWORD" />
			</c:if>
		</c:if>
   </s:div>
   
   <s:div name="divFontSize" cssclass="divDisplayNone">
		<s:button onclick="decreaseFontSize();" type="button" id="btnDecreaseFontSize" text="A-" cssclass="linkFontSizeReduce" />
		<s:button onclick="increaseFontSize();" type="button" id="btnIncreaseFontSize" text="A+" cssclass="linkFontSizeIncrease" />
	</s:div>
	
</s:div>	
