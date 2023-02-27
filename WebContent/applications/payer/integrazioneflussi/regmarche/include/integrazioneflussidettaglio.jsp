<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link  href="../applications/templates/shared/js/jquery-ui-custom.min.css" rel="stylesheet" type="text/css">
<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js" type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js" type="text/javascript"></script>

<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="var_form" action="integrazioneflussi.do?idFlusso=${idFlusso}" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">DETTAGLIO POSIZIONE FLUSSO</s:div>
										
				<s:div name="divRicMetadati" cssclass="divRicMetadati">
				
					<s:div name="divRicercaMetadatiLeft" cssclass="divRicMetadatiLeft">
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_tipoFlusso"
								label="Tipo Flusso:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled"
								text="${tx_tipoFlusso}" />
						</s:div>
						
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_nomeFile"
								label="Nome File:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman2 colordisabled"
								text="${tx_nomeFile}" />
						</s:div>
						
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						</s:div>
						
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_idDominio" 
								label="ID Dominio:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled"
								text="${tx_idDominio}" />
						</s:div>
					
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_idFlusso" 
								label="ID Flusso:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_codIdFlusso}" />
						</s:div>
					
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_idDocumento" 
								label="Id Documento:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled"
								text="${tx_idDocumento}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_codFisc" 
								label="Codice Fiscale:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_codFisc}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_indContrib" 
								label="Indirizzo Contribuente:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_indContrib}" />
						</s:div>
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_flagAnnullamento" 
								label="Flag Annullamento:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_flagAnnullamento}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_flagPagato" 
								label="Flag Pagato:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_flagPagato}" />
						</s:div>
					</s:div>
					
					<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="codFiscAgg"
								label="Cod Fisc Agg:"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled"
								text="${tx_codFiscAgg}" />
						</s:div>
						
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="creazioneFlusso"
								label="Creazione Flusso:"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled"
								text="${tx_creazioneFlusso}" />
						</s:div>
						
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						</s:div>
						
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_idEnte" 
								label="Codice Ente:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_idEnte}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_dataCreazione"
								label="Data Creazione:"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled"
								text="${tx_dataCreazione}" />	
						</s:div>
					
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_numRata" 
								label="Numero Rata:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_numRata}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_importo" 
								label="Importo:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_importo}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_locContrib" 
								label="Localit&agrave; Contribuente:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_locContrib}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_ibanAccr" 
								label="Iban Accredito:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_ibanAccr}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_tassonomia" 
								label="Tassonomia:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_tassonomia}" />
						</s:div>
						
					</s:div>	
					
					<s:div name="divRicercaRight3" cssclass="divRicMetadatiRight">
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_timestampFlusso"
								label="Timestamp:"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled"
								text="${tx_timestampFlusso}" />
						</s:div>
						
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						</s:div>
						
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						</s:div>
						
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_numeroAvviso" 
								label="Numero Avviso:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_numeroAvviso}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_tipoRecord" 
								label="Tipo Record:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_tipoRecord}" />
						</s:div>
					
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="creazioneFlusso"
								label="Data Scadenza:"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled"
								text="${tx_dataScadenza}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_denDeb" 
								label="Denominazione Contribuente:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_denDeb}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_provContrib" 
								label="Provincia Contribuente:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_provContrib}" />
						</s:div>
												
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_idIuv" 
								label="Codice IUV:" maxlenght="17" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_idIuv}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_flagPagato_show" 
								label="Flag Inviato:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_flagInviato}" />
						</s:div>
						
					</s:div>
			</s:div>	
			<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_indietro" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />
			</s:div>
			
			<input type="hidden" name="idDominio" value="${idDominio_hidden}" />
			<input type="hidden" name="codiceEnte" value="${codiceEnte_hidden}" />
			<input type="hidden" name="codiceIuv" value="${codiceIuv_hidden}" />	
			
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
