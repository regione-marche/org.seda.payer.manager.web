<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="impostaSoggiornoTariffa_search" encodeAttributes="true" />

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
<script type="text/javascript">
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();
	
	$(function() {
		$.datepicker.setDefaults($.datepicker.regional["it"]);
		$("#tx_data_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_da_hidden").datepicker( {
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn : "button",
			buttonImage : "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly : true,
			onSelect : function(dateText, inst) {
				$("#tx_data_da_day_id").val(dateText.substr(0, 2));
				$("#tx_data_da_month_id").val(dateText.substr(3, 2));
				$("#tx_data_da_year_id").val(dateText.substr(6, 4));
			},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_da_day_id",
				                            "tx_data_da_month_id",
				                            "tx_data_da_year_id",
				                            "tx_data_da_hidden");
			}
		});
		$("#tx_data_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_a_hidden").datepicker( {
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn : "button",
			buttonImage : "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly : true,
			onSelect : function(dateText, inst) {
				$("#tx_data_a_day_id").val(dateText.substr(0, 2));
				$("#tx_data_a_month_id").val(dateText.substr(3, 2));
				$("#tx_data_a_year_id").val(dateText.substr(6, 4));
			},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_a_day_id",
				                            "tx_data_a_month_id",
				                            "tx_data_a_year_id",
				                            "tx_data_a_hidden");
			}
		});
	});
</script>



<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="search_tariffa_form" action="ImpostaSoggiornoTariffaSearch.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA TARIFFE STRUTTURE
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">

					<s:div name="divElement1" cssclass="divRicMetadatiLeft">
						<s:textbox bmodify="true" name="descrizioneComune"
								label="Comune: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${descrizioneComune}" message="[accept=Comune: ${msg_configurazione_descrizione_regex}]"/>
					</s:div>				
					
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:dropdownlist name="descrizioneStrutturaRicettiva"
									disable="false" 
									label="Strutt. Ric.:"
									cssclasslabel="label85 bold textright"
									cssclass="tbddl"
									cachedrowset="listStrutture" usexml="true"
									validator="ignore" showrequired="true"
									valueselected="${descrizioneStrutturaRicettiva}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption  value="{2}" text="{2}"/>
								</s:dropdownlist>
					</s:div>
					
					<s:div name="divElement3" cssclass="divRicMetadatiRight">
						
					</s:div>
					
				</s:div>


					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:div name="divElement18a" cssclass="labelData">
								<s:label name="label_data"
									cssclass="seda-ui-label label85 bold textright"
									text="Data Validit&agrave;" />
							</s:div>
	
							<s:div name="divElement18b" cssclass="floatleft">
								<s:div name="divDataDa" cssclass="divDataDa">
									<s:date label="Da:" prefix="tx_data_da"
										yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
										locale="IT-it" descriptivemonth="false" separator="/"
										calendar="${tx_data_da}" cssclasslabel="labelsmall"
										cssclass="dateman">
									</s:date>
									<input type="hidden" id="tx_data_da_hidden" value="" />
								</s:div>
	
								<s:div name="divDataA" cssclass="divDataA">
									<s:date label="A:" prefix="tx_data_a"
										yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
										locale="IT-it" descriptivemonth="false" separator="/"
										calendar="${tx_data_a}" cssclasslabel="labelsmall"
										cssclass="dateman">
									</s:date>
									<input type="hidden" id="tx_data_a_hidden" value="" />
								</s:div>
							</s:div>
						</s:div>	
					</s:div>


				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement18" cssclass="divRicMetadatiDoubleRow">
						

					</s:div>
				</s:div>

			</s:div>
			<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_aggiungi" validate="false"  onclick="" text="Nuovo" type="submit" cssclass="btnStyle" />
				<c:if test="${!empty listaTariffe}">
					<s:button id="tx_button_download" validate="false" onclick="" text="Download" type="submit" cssclass="btnStyle" />
				</c:if>
			</s:div>
	</s:form>

	</s:div>
	
</s:div>

<c:if test="${!empty listaTariffe}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO TARIFFE STRUTTURE
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="listaTariffe"  
			action="ImpostaSoggiornoTariffaSearch.do?vista=impostaSoggiornoTariffa_search&tx_button_default=C" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

		<!-- DESCRIZIONE COMUNE -->
			<s:dgcolumn label="Comune" index="7" />
			
		<!-- DESCRIZIONE Struttura ricettiva -->
			<s:dgcolumn label="Struttura Ricettiva" index="6" />

		<!-- DATA VALIDITA' --> 
			<s:dgcolumn format="dd/MM/yyyy" index="4" label="Inizio Validit&agrave;"></s:dgcolumn>

		<!-- IMPORTO --> 
			<s:dgcolumn index="5" format="#,##0.00" label="Importo" css="text_align_right"></s:dgcolumn>

		<!-- AZIONI -->
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">

		<!-- MODIFICA RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="ImpostaSoggiornoTariffaInsUpd.do?tx_button_edit=B&tx_chiaveTar={1}&tx_provincia={8}&vista=impostaSoggiornoTariffa_search&start=A"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />
		<!-- CANCELLAZIONE RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="ImpostaSoggiornoTariffaCancel.do?tx_button_delete=B&tx_chiaveTar={1}&tx_provincia={8}&vista=impostaSoggiornoTariffa_search&start=A"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Elimina" text=""   />
			</s:dgcolumn>
			
		</s:datagrid>
	</s:div>

</c:if>
