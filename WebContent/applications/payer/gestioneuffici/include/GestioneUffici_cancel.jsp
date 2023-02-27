<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>


<s:div name="div_delete" cssclass="div_align_center divSelezione">
	<s:div name="divDeleteTopName" cssclass="divRicercaTop">
	<s:form name="form_uffici_delete" action="gestioneuffici.do" method="get"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
			CANCELLAZIONE CONFIGURAZIONE GESTIONE UFFICI
		</s:div>
		Sei sicuro di voler cancellare il record selezionato ?
		<br/>
		<br/>
		<s:div name="divRicMetadati" cssclass="divRicMetadati">
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_indietro" onclick="" text="Indietro" cssclass="btnStyle" type="submit" />
				<s:button id="tx_button_delete" disable="" onclick="" text="Cancella" type="submit" cssclass="btnStyle" />
			</s:div>
		</s:div>	
		<input type="hidden" name="idufficio" value="${requestScope.idufficio}" />
	</s:form>
</s:div>
	
</s:div>
