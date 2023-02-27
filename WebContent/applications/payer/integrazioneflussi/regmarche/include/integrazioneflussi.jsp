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
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();
	
	$(function() {
		$.datepicker.setDefaults($.datepicker.regional[ "it" ]);
		
		$("#data_creazione_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_creazione_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_creazione_da_day_id").val(dateText.substr(0,2));
												$("#data_creazione_da_month_id").val(dateText.substr(3,2));
												$("#data_creazione_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_creazione_da_day_id",
				                            "data_creazione_da_month_id",
				                            "data_creazione_da_year_id",
				                            "data_creazione_da_hidden");
			}
		});
		
		$("#data_creazione_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_creazione_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_creazione_a_day_id").val(dateText.substr(0,2));
												$("#data_creazione_a_month_id").val(dateText.substr(3,2));
												$("#data_creazione_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_creazione_a_day_id",
				                            "data_creazione_a_month_id",
				                            "data_creazione_a_year_id",
				                            "data_creazione_a_hidden");
			}
		});
	});

	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>

<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_selezione" action="integrazioneflussi.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA INTEGRAZIONE FLUSSI</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_tipoFlusso_s" disable="false"
							cssclass="tbddlMax floatleft" label="Tipo Flusso:"
							cssclasslabel="label85 bold textright"
							valueselected="${tx_tipoFlusso_s}">
							<s:ddloption text="OPNA" value="OPNA" />
							<s:ddloption text="RTB01" value="RTB01" />
						</s:dropdownlist>
					</s:div>
					<s:div name="divElement12" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement12a" cssclass="labelData">
							<s:label name="label_data_creazione" 
								cssclass="seda-ui-label label78 bold textright" 
								text="Data Creazione"/>
						</s:div>
						<s:div name="divElement12b" cssclass="floatleft">
							<s:div name="divDataDa_cre" cssclass="divDataDa">
								<s:date label="Da:" 
									cssclasslabel="labelsmall"
									cssclass="dateman"
									prefix="data_creazione_da" 
									yearbegin="${ddlDateAnnoDa}" 
									yearend="${ddlDateAnnoA}" locale="IT-it"
								 	descriptivemonth="false" 
								 	separator="/" calendar="${data_creazione_da}" >
							 	</s:date>
								<input type="hidden" id="data_creazione_da_hidden" value="" />
							</s:div>
						
							<s:div name="divDataA_cre" cssclass="divDataA">
								<s:date label="A:" 
									cssclasslabel="labelsmall"
									cssclass="dateman"
									prefix="data_creazione_a" 
									yearbegin="${ddlDateAnnoDa}" 
									yearend="${ddlDateAnnoA}" locale="IT-it"
								 	descriptivemonth="false" 
								 	separator="/" calendar="${data_creazione_a}"></s:date>
								<input type="hidden" id="data_creazione_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
				</s:div>
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_codFiscAgg_s"
							label="Cod. Fisc. Agg.:" maxlenght="16"
							cssclasslabel="label85 bold textright" cssclass="textareaman"
							validator="ignore;accept=^[0-9a-zA-Z\-]{1,16}$"
							message="[accept=Cod. Fisc. Agg.: ${msg_configurazione_descrizione_3}]"
							text="${tx_codFiscAgg_s}" />
					</s:div>
				</s:div>
				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_nomeFile_s"
							label="Nome file:"
							cssclasslabel="label85 bold textright" cssclass="textareaman"
							text="${tx_nomeFile_s}" />
					</s:div>
				</s:div>
				
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />

				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit"
					cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick=""
					text="Reset" type="submit" cssclass="btnStyle" />
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

<c:if test="${!empty lista_flussi}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO INTEGRAZIONE FLUSSI
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_flussi"
			action="integrazioneflussi.do" border="1"
			usexml="true" rowperpage="${applicationScope.rowsPerPage}">

			<s:dgcolumn index="1" label="Id Flusso"></s:dgcolumn>
			<s:dgcolumn index="2" label="Tipo Flusso" css="text_align_left"></s:dgcolumn>
			<s:dgcolumn index="3" label="Cod. Fisc. Agg."></s:dgcolumn>
			<s:dgcolumn index="4" label="Data Flusso" css="text_align_left"></s:dgcolumn>
			<s:dgcolumn index="5" label="Nome File" css="text_align_left"></s:dgcolumn>
			<s:dgcolumn index="6" label="Data Inserimento" css="text_align_left"></s:dgcolumn>
			<s:dgcolumn label="Azioni">
				<s:hyperlink
					href="integrazioneflussi.do?idFlusso={1}"
					imagesrc="../applications/templates/shared/img/dettaglio.png"
					alt="Dettaglio" text="" cssclass="blacklink hlStyle" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>