<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:setBundle name="com.seda.portale.i18n.resources.TemplateStrings" />
	<c:if test="${!empty federa_filter}">
		<br/>
		<s:div name="div_selezione" cssclass="divLogin div_align_center">
			<s:div name="divRicercaTopName">
				<c:if test="${!empty welcome_message}">
					<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Benvenuto sull'applicazione manager di PayER</s:div>
					<br/>
					<br/>
					<s:div name="divRicercaTitleName" cssclass="div_align_center">Per poter accedere a questa applicazione devi essere abilitato dagli amministratori di PayER<br/> e la tua utenza FedERa deve avere un livello di affidabilit&agrave; e di password policy adeguato (affidabilit&agrave;: alta; password-policy:dati personali).</s:div>
				</c:if>
					<br/>
					<br/>
				<s:form name="lepida_login" cssclass="padding_bottom_20"
					action="../default/defaultprot.do" method="post"
					hasbtn1="false" hasbtn2="false" hasbtn3="false">
					<br/>
					<br/>
					<s:button id="button_login" onclick="" text="Login" cssclass="btnStyle"
						type="submit" />
				</s:form>
			</s:div>
		</s:div>
	</c:if>

	<c:if test="${empty federa_filter}">
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

 