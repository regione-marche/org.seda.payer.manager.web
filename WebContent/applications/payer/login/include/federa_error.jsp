<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
 <s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Autenticazione Federata</s:div>
			<br/>
			<br/>
			<br/>
		<s:form name="form_federa_error" cssclass="padding_bottom_20"
			action="../default/default.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<br/>
			<br/>
			<br/>
			<s:button id="button_login" onclick="" text="Login" cssclass="btnStyle"
				type="submit" />
		</s:form>
	</s:div>
</s:div>
 