<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>

<m:setBundle name="com.seda.portale.i18n.resources.TemplateStrings" />

<div class="divLogin">
	<div class="divIntestazioneLogin">Accesso negato<c:if test="${!empty requestScope.messageError}">&nbsp;-&nbsp;${requestScope.messageError}</c:if></div>

	<div class="divTestoLogin">&Egrave; necessario effettuare il Login prima di poter accedere all'area selezionata</div>

	<s:form method="post" name="portal_login" action="j_signon_do"
		hasbtn3="false" hasbtn2="false" hasbtn1="false"
		btn1onclick="" cssclass="formLogin" autocomplete="off">
		
			<div class="loginTitle">LOGIN</div>
			<p><s:textbox label="Username" autocomplete="off" bmodify="true" name="j_username" text="${param.j_username}" 
			validator="required;accept=^[0-9a-zA-Z\-]{1,20}$" cssclass="login_textarea_label" cssclasslabel="login_textarea_label left" 
			message="[accept=username: caratteri non ammessi"/></p>
			<p><s:textbox label="Password" bpassword="true" autocomplete="off" bmodify="true" name="j_password" text="${param.j_password}" 
			validator="required;accept=^[A-Za-z\d@#$%^&+=!*]{4,}$" cssclass="login_textarea_label" cssclasslabel="login_textarea_label left"
			message="[accept=Password: caratteri non ammessi]"/>
			
			</p>
			<p><s:button id="btnLogin" type="submit" text="Login" onclick="" cssclass="btnBlue"/></p>
</s:form>
</div>
  