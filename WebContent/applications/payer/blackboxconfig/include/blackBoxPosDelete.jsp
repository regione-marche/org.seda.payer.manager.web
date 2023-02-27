<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>


<s:div name="div_delete" cssclass="div_align_center divSelezione">
	<s:div name="divDeleteTopName" cssclass="divRicercaTop">
	<s:form name="delete_form" action="blackboxpos.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
		CANCELLAZIONE POSIZIONE BLACK BOX
		</s:div>
		Sei sicuro di voler cancellare il record selezionato ?
		<br/>
		<br/>
		<s:div name="divRicMetadati" cssclass="divRicMetadati">
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_delete" disable="" onclick="" text="Cancella" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_indietro" onclick="" text="Indietro" cssclass="btnStyle" type="submit" />
			</s:div>
		</s:div>
				
		<input type="hidden" id="codOp" name="codOp" value="${codOp}" />
		<input type="hidden" name="tx_idDominio_hidden" value="${tx_idDominio_hidden}" />
		<input type="hidden" name="tx_idEnte_hidden" value="${tx_idEnte_hidden}" />
		<input type="hidden" name="tx_numeroAvviso_hidden" value="${tx_numeroAvviso_hidden}" />
								
	</s:form>
</s:div>
	
</s:div>
