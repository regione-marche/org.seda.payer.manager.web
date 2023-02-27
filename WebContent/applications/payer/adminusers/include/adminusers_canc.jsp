<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="adminusers_canc" encodeAttributes="true" />

<style>
form{
display: inline-block;
}
</style>

<s:div name="div_delete" cssclass="div_align_center divSelezione">
	<s:div name="divDeleteTopName" cssclass="divRicercaTop">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Cancellazione Profilo</s:div>
	
		<span class="seda-ui-span lblMessage"></span>
		<span class="seda-ui-span lblMessage">Sei sicuro di voler cancellare il record selezionato?</span>
		<br/>
		<br/>
		<s:div name="divRicMetadati" cssclass="divRicMetadati">
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
			<s:form name="delete_form" action="userDelete.do" method="post"
				hasbtn1="false" hasbtn2="false" hasbtn3="false">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_delete" disable="${confermaDisabilitata}" onclick="" text="Cancella" type="submit" cssclass="btnStyle" />
				<input type="hidden" name="tx_chiaveutente_hidden" value="${tx_chiaveutente_hidden}" />
			</s:form>
			<s:form name="form_indietro"
					action="ritorna.do?vista=adminusers_search&tx_button_cerca=cerca" method="post"
					hasbtn1="false" hasbtn2="false" hasbtn3="false">
					<s:button id="button_indietro" onclick="" text="Indietro" cssclass="btnStyle"
						type="submit" />
			</s:form>
	
			</s:div>
		</s:div>
		
</s:div>
	
</s:div>
