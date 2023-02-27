<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="monitoraggiomercati" encodeAttributes="true" />

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
	$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
	$("#tx_data_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
	$("#tx_data_da_hidden").datepicker({
		minDate: new Date(annoDa, 0, 1),
		maxDate: new Date(annoA, 11, 31),
		yearRange: annoDa + ":" + annoA,
		showOn: "button",
		buttonImage: "../applications/templates/shared/img/calendar.gif",
		buttonImageOnly: true,
		onSelect: function(dateText, inst) {$("#tx_data_da_day_id").val(dateText.substr(0,2));
											$("#tx_data_da_month_id").val(dateText.substr(3,2));
											$("#tx_data_da_year_id").val(dateText.substr(6,4));
											},
		beforeShow: function(input, inst) {
		//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
        	updateValoreDatePickerFromDdl("tx_data_da_day_id",
		    	                          "tx_data_da_month_id",
		        	                      "tx_data_da_year_id",
		            	                  "tx_data_da_hidden");
		}
	});
	$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
	$("#tx_data_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
	$("#tx_data_a_hidden").datepicker({
		minDate: new Date(annoDa, 0, 1),
		maxDate: new Date(annoA, 11, 31),
		yearRange: annoDa + ":" + annoA,
		showOn: "button",
		buttonImage: "../applications/templates/shared/img/calendar.gif",
		buttonImageOnly: true,
		onSelect: function(dateText, inst) {$("#tx_data_a_day_id").val(dateText.substr(0,2));
											$("#tx_data_a_month_id").val(dateText.substr(3,2));
											$("#tx_data_a_year_id").val(dateText.substr(6,4));
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

	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script> 

<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="form_selezione" action="monitoraggiomercati.do?vista=monitoraggiomercati" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">MONITORAGGIO MERCATI
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				 <s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				 	<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						
						<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " 
									cssclasslabel="label160 bold textright floatleft" validator="ignore;"
									cssclass="seda-ui-ddl tbddlMax780 floatleft" disable="${codop == 'edit'}"
									onchange="setFiredButton();this.form.submit();"
									cachedrowset="listaSocietaUtenteEnte" usexml="true" 
									valueselected="${ddlSocietaUtenteEnte}">
   							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
							<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}"/>
						</s:dropdownlist>
						
						<c:if test="${codop == 'add'}">
							<noscript>
								<s:button id="tx_button_societa_changed" 
									onclick="" text="" 
									validate="false"
									type="submit" cssclass="btnimgStyle"  title="Aggiorna" />
							</noscript>
						</c:if>									
					</s:div>		 			
				</s:div> 
			</s:div>
			<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">			
				<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist name="tx_area_mercantile" disable="false" label="Area Mercantile:"
						multiple="false" valueselected="${tx_area_mercantile}" 
						onchange="setFiredButton();this.form.submit();"
						cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
						<s:ddloption text="seleziona..." value="" />
						<s:ddloptionbinder options="${areemercDDLList}"/>
					</s:dropdownlist>		
				</s:div>
				<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
					<s:div name="divElement11a" cssclass="labelData">
						<s:label name="label_data_periodo" cssclass="seda-ui-label label85 bold textright"
							text="Data Periodo:" />
					</s:div>
					<s:div name="divElement11b" cssclass="floatleft">
						<s:div name="divDataDa_car" cssclass="divDataDa">
							<s:date label="Da:" prefix="tx_data_da" yearbegin="${ddlDateAnnoDa}"
								cssclasslabel="labelsmall"
								cssclass="dateman"
								yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
								separator="/" calendar="${tx_data_da}"></s:date>
							<input type="hidden" id="tx_data_da_hidden" value="" />
						</s:div>
						<s:div name="divDataA_car" cssclass="divDataA">
							<s:date label="A:" prefix="tx_data_a" yearbegin="${ddlDateAnnoDa}"
								cssclasslabel="labelsmall"
								cssclass="dateman"
								yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
								separator="/" calendar="${tx_data_a}"></s:date>
							<input type="hidden" id="tx_data_a_hidden" value="" />
						</s:div>
					</s:div>
				</s:div>							
			</s:div>
			<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">	
				<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist name="tx_piazzola" disable="false" label="Codice Piazzola:"
						multiple="false" valueselected="${tx_piazzola}" 
						cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
						<s:ddloption text="seleziona..." value="" />
						<s:ddloptionbinder options="${piazzolaDDLList}"/>
					</s:dropdownlist>		
				</s:div>		
				<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" name="tx_num_autor"
						label="Codice Autorizzazione:" maxlenght="30" 
						cssclasslabel="label85 bold textright" bdisable="${codop == 'edit'}"
						cssclass="textareaman" validator="ignore"
						text="${tx_num_autor}" />
				</s:div>
			</s:div>
			
			<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
				<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" name="tx_cod_fisc"
						label="Codice Fiscale/P.Iva:" maxlenght="16" 
						cssclasslabel="label85 bold textright" 
						cssclass="textareaman" validator="ignore"
						text="${tx_cod_fisc}" />	
				</s:div>
				<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist name="tx_stato_pagam" disable="false" label="Stato Pagam.:"
						multiple="false" valueselected="${tx_stato_pagam}" 
						cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
						<s:ddloption text="Tutti gli stati" value="" />
						<s:ddloption text="Pagato" value="Y" />
						<s:ddloption text="Non Pagato" value="N" />
					</s:dropdownlist>		
				</s:div>		
			</s:div>				
				
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				<c:if test="${!empty lista_monitor_mercati}">
					<s:button id="tx_button_download" onclick="" text="Download" cssclass="btnStyle" type="submit" />
				</c:if>
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

<c:if test="${!empty lista_monitor_mercati}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO MERCATI
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_monitor_mercati" 
			action="monitoraggiomercati.do?vista=monitoraggiomercatiLista" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

			<s:dgcolumn index="6" label="Mercato" css="text_align_left"/>
			<s:dgcolumn index="8" label="Piazzola" css="text_align_left"/>
			<s:dgcolumn index="9" label="Num.Autorizzazione" css="text_align_left"/>
			<s:dgcolumn index="10" label="Nominativo" css="text_align_left"/>
			<s:dgcolumn index="11" label="Codice Fiscale" css="text_align_left"/>
			<s:dgcolumn index="18" label="Data Prenotazione" css="text_align_left"/>
			<s:dgcolumn label="Pagato/Non Pag." css="text_align_left">
				<s:if left="{19}" control="eq" right="Y">
					<s:then>Pagato</s:then>
				</s:if>
				<s:if left="{19}" control="eq" right="N">
					<s:then>Non Pagato</s:then>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;">
				<s:hyperlink
					href="dettagliomonitoraggio.do?chiave_tariffa={4}&cod_prenotazione={17}"
					imagesrc="../applications/templates/shared/img/Info.png"
					alt="Dettaglio Mercato" text=""
					cssclass="blacklink hlStyle" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
	
</c:if>
