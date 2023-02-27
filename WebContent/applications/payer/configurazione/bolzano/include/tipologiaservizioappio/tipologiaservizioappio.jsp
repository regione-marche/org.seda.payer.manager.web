<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="tipologiaservizioappio" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_selezione" action="tipologiaservizioappio.do?vista=tipologiaservizioappio"
			method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA TIPOLOGIA SERVIZIO APP IO	
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" maxlenght="3"
							cssclass="textareaman" validator="ignore"
							label="Cod. Tipologia Servizi AppIO:" name="tx_codiceTipologiaServizi"
							text="${tx_codiceTipologiaServizi}"
							cssclasslabel="label160 bold textright" />
					</s:div>
				</s:div>
				
					
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" maxlenght="256"
						cssclass="textareaman" validator="ignore"
							label="Descr. Tipologia Servizi AppIO:" name="tx_descrizioneTipologiaServizi"
							text="${tx_descrizioneTipologiaServizi}"
							cssclasslabel="label160 bold textright" />
					</s:div>
				</s:div>
			</s:div>
			<br />
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit"
					cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick=""
					text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_add" validate="false" onclick=""
					text="Nuovo" type="submit" cssclass="btnStyle" />
			</s:div>
		</s:form>
	</s:div>
</s:div>


<s:div name="div_messaggi" cssclass="div_align_center">
	<c:if test="${!empty tx_message}">
		<s:div name="div_messaggio_info">
			<hr />
			<s:label name="tx_message" text="${tx_message}" />
			<hr />
		</s:div>
	</c:if>
	<c:if test="${!empty tx_error_message}">
		<s:div name="div_messaggio_errore">
			<hr />
			<s:label name="tx_error_message" text="${tx_error_message}" />
			<hr />
		</s:div>
	</c:if>
</s:div>
<c:if test="${!empty lista_tipologieServiziAPPIO }">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO TIPOLOGIE SERVIZI APP IO 

	</s:div>
	
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_tipologieServiziAPPIO"
			action="tipologiaservizioappio.do" border="1"
			usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			<s:dgcolumn index="1" label="Cod. tipologia servizio AppIO" css="textcenter"></s:dgcolumn>
			<s:dgcolumn index="2" label="Descr. tipologia servizio AppIO" css="textcenter"></s:dgcolumn>
			<s:dgcolumn index="4" label="Operatore" css="textcenter"></s:dgcolumn>
			<s:dgcolumn index="3" label="Ultimo aggiornamento" format="dd/MM/yyyy HH:mm:ss" css="textcenter"></s:dgcolumn>
			<s:dgcolumn label="Azioni">
			
			<s:hyperlink
					href="tipologiaservizioappioedit.do?vista=tipologiaservizioappio&codop=edit&tx_codice={1}"
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Modifica" text="" cssclass="blacklink hlStyle" />

				<s:hyperlink
					href="tipologiaservizioappio.do?vista=tipologiaservizioappio&tx_button_cancel&tx_codice={1}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text="" cssclass="blacklink hlStyle" />
				
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>