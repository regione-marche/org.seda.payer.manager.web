<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="default" encodeAttributes="true" />
<!-- 
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">ERRORE</s:div>
		<c:if test="${!empty tx_error_message}">
			<s:div name="divRicercaTitleName" cssclass="divRicercaTop bold div_align_center background_red">${tx_error_message}</s:div>
		</c:if>
	</s:div>
</s:div>
<br/>
<s:div name="div_login_1" cssclass="div_align_center">
	<s:form name="form_login" cssclass="padding_bottom_20"
		action="../login/login.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:button id="button_login" onclick="" text="Login" cssclass="btnStyle"
			type="submit" />
	</s:form>
</s:div>
 -->
 <s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">SESSIONE SCADUTA - RIPETERE IL LOGIN</s:div>
		<s:form name="form_login" cssclass="padding_bottom_20"
			action="../login/login.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:button id="button_login" onclick="" text="Login" cssclass="btnStyle"
				type="submit" />
		</s:form>
	</s:div>
</s:div>
 