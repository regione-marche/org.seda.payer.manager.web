<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="impostaSoggiornoTariffa_cancel" encodeAttributes="true" />


<s:div name="div_delete" cssclass="div_align_center divSelezione">
	<s:div name="divDeleteTopName" cssclass="divRicercaTop">
	<s:form name="delete_form" action="ImpostaSoggiornoTariffaCancel.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle">
		TARIFFA STRUTTURA
		</s:div>
		<c:if test="${!confermaDisabilitata}">
			<div class="divAlertRight">
					Sei sicuro di voler cancellare il record selezionato ?
			</div>
		</c:if>	
		<br/>
		<br/>
		<s:div name="divRicMetadati" cssclass="divRicMetadati">
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_indietro" onclick="" text="Indietro" cssclass="btnStyle" type="submit" />
				<s:button id="tx_button_delete_end" disable="${confermaDisabilitata}" onclick="" text="Cancella" type="submit" cssclass="btnStyle" />
			</s:div>
		</s:div>
	</s:form>

</s:div>
	
</s:div>
