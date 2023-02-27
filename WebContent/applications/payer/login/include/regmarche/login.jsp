<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>

<m:setBundle name="com.seda.portale.i18n.resources.TemplateStrings" />

<c:if test="${sessionScope.flagFedera == 'Y'}">
	<s:div name="div_selezione" cssclass="divLogin div_align_center">
		<s:div name="divRicercaTopName">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Benvenuto in MPay</s:div>
			<br/>
			<s:div name="divError" cssclass="divError">
				<c:if test="${!empty tx_message}">
					<hr/><b><s:label name="tx_message" text="${tx_message}"/></b><hr/>
				</c:if>
			</s:div>
			<br/>
			<s:div name="divRicercaTitleName" cssclass="div_align_center">Per poter accedere a questa applicazione devi essere abilitato dagli amministratori di MPay<br/> e la tua utenza Cohesion deve avere un livello di affidabilit&agrave; e di password policy adeguato (affidabilit&agrave;: alta; password-policy:dati personali).</s:div>
			<br/>
			<br/>
			<s:form name="lepida_login" cssclass="padding_bottom_20"
				action="../federaCohesion/Authentication" method="post"
				hasbtn1="false" hasbtn2="false" hasbtn3="false">
				<br/>
				<br/>
				<s:button id="button_login" onclick="" text="Login" cssclass="hlGreen"
					type="submit" />
			</s:form>
		</s:div>
	</s:div>
</c:if>

<c:if test="${sessionScope.flagFedera == 'N'}">
<div class="divLogin">
	<div class="divIntestazioneLogin">Accesso negato<c:if test="${!empty param.message}">&nbsp;-&nbsp;${param.message}</c:if></div>

	<div class="divTestoLogin">&Egrave; necessario effettuare il Login prima di poter accedere all'area selezionata</div>

	<s:form method="post" name="portal_login" action="j_signon_do"
		hasbtn3="false" hasbtn2="false" hasbtn1="false"
		btn1onclick="" cssclass="formLogin">
		
			<div class="loginTitle">LOGIN</div>
			<p><s:textbox label="Username" bmodify="true" name="j_username" text="${param.j_username}" validator="required" cssclass="login_textarea_label" cssclasslabel="login_textarea_label left" /></p>
			<p><s:textbox label="Password" bpassword="true" bmodify="true" name="j_password" text="${param.j_password}" validator="required" cssclass="login_textarea_label" cssclasslabel="login_textarea_label left" /></p>
			<p><s:button id="btnLogin" type="submit" text="Login" onclick="" cssclass="btnBlue"/></p>
	</s:form>
</div>
</c:if>
  