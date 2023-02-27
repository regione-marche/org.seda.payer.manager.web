<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="blackbox" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>

<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>

<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_selezione" action="integrazioneflussi.do?idFlusso=${idFlusso}" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA DETTAGLI FLUSSO</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="ddlSocietaUtenteEnte"
 							label="Societ&agrave;/Utente/Ente: " showrequired="true"
 							cssclasslabel="label160 bold textright floatleft"
 							validator="ignore;" cssclass="seda-ui-ddl tbddlMax780 floatleft"
 							disable="false"
 							onchange="setFired();this.form.submit();"
 							cachedrowset="listaSocietaUtenteEnte" usexml="true"
 							valueselected="${ddlSocietaUtenteEnte}">
 							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
 							<s:ddloption value="{1}|{2}|{3}|{4}" text="{5} / {6} / {4}" />
 						</s:dropdownlist>
 					</s:div>
 				</s:div>

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" name="tx_tipoFlusso"
							label="Tipo Flusso:" bdisable="true"
							cssclasslabel="label85 bold textright" cssclass="textareaman colordisabled"
							text="${tx_tipoFlusso}" />
					</s:div>
					
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" name="tx_nomeFile"
							label="Nome File:" bdisable="true"
							cssclasslabel="label85 bold textright" cssclass="textareaman2 colordisabled"
							text="${tx_nomeFile}" />
					</s:div>
				
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_idDominio_s"
							label="Id Dominio:" maxlenght="16"
							cssclasslabel="label85 bold textright" cssclass="textareaman"
							validator="ignore;accept=^[0-9a-zA-Z\-]{1,16}$"
							message="[accept=Id Dominio.: ${msg_configurazione_descrizione_3}]"
							text="${tx_idDominio_s}" />
					</s:div>
				</s:div>
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" name="tx_codFiscAgg"
							label="Cod Fisc Agg:" bdisable="true"
							cssclasslabel="label85 bold textright" cssclass="textareaman colordisabled"
							text="${tx_codFiscAgg}" />
					</s:div>
					
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" name="tx_creazioneFlusso"
							label="Creazione:" bdisable="true"
							cssclasslabel="label85 bold textright" cssclass="textareaman colordisabled"
							text="${tx_creazioneFlusso}" />
					</s:div>
					
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_codiceIuv_s"
							label="Codice IUV:" maxlenght="20"
							cssclasslabel="label85 bold textright" cssclass="textareaman"
							validator="ignore;accept=^[0-9a-zA-Z\-]{1,20}$"
							message="[accept=Codice IUV: ${msg_configurazione_descrizione_3}]"
							text="${tx_codiceIuv_s}" />
					</s:div>
				</s:div>
				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" name="tx_timestampFlusso"
							label="Timestamp:" bdisable="true"
							cssclasslabel="label85 bold textright" cssclass="textareaman colordisabled"
							text="${tx_timestampFlusso}" />
					</s:div>
					
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					</s:div>
					
					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_flagPagato_s" disable="false"
							cssclass="tbddlMax floatleft" label="Flag Pagato:"
							cssclasslabel="label85 bold textright"
							valueselected="${tx_flagPagato_s}">
							<s:ddloption text="Tutti" value="" />
							<s:ddloption text="Sì" value="Y" />
							<s:ddloption text="No" value="N" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />

				<s:button id="tx_button_indietro" onclick="" text="Indietro" type="submit"
					cssclass="btnStyle" />
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit"
					cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit"
					cssclass="btnStyle" />
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

<c:if test="${!empty lista_flussodettagli}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO DETTAGLI FLUSSO
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_flussodettagli"
			action="integrazioneflussi.do?idFlusso=${idFlusso}" border="1"
			usexml="true" rowperpage="${applicationScope.rowsPerPage}">

			<s:dgcolumn index="1" label="Id Dominio"></s:dgcolumn>
			<s:dgcolumn index="2" label="Codice Ente" css="text_align_left"></s:dgcolumn>
			<s:dgcolumn index="3" label="Codice IUV"></s:dgcolumn>
			<s:dgcolumn label="Pagato">
				<s:if left="{20}" control="eq" right="X">
					<s:then>
						Si
					</s:then>
					<s:else>
						No
					</s:else>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn label="Azioni">
				<s:hyperlink
					href="integrazioneflussi.do?idFlusso=${idFlusso}&idDominio={1}&codiceEnte={2}&codiceIuv={3}"
					imagesrc="../applications/templates/shared/img/dettaglio.png"
					alt="Dettaglio" text="" cssclass="blacklink hlStyle" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>