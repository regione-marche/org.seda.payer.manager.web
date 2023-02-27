<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="" encodeAttributes="true" />


<s:div name="div_sblocca" cssclass="div_align_center divSelezione">
	<s:div name="divDeleteTopName" cssclass="divRicercaTop">
	<s:form name="sblocca_form" action="seUsersSblocca.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Sblocco User</s:div>
		<br/>
		<br/>
		<s:div name="divRicMetadati" cssclass="divRicMetadati">
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_edit" disable="${confermaDisabilitata}" onclick="" text="Sblocco" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_indietro" onclick="" text="Indietro" type="submit" cssclass="btnStyle" />
			</s:div>
		</s:div>
		<input type="hidden" name="tx_username_hidden" value="${tx_username_hidden}" />
	</s:form>

</s:div>
	
</s:div>
