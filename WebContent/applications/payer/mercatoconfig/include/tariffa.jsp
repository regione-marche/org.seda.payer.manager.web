<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="tariffe" encodeAttributes="true" />

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
	$("#tx_dval_ini_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
	$("#tx_dval_ini_hidden").datepicker({
		minDate: new Date(annoDa, 0, 1),
		maxDate: new Date(annoA, 11, 31),
		yearRange: annoDa + ":" + annoA,
		showOn: "button",
		buttonImage: "../applications/templates/shared/img/calendar.gif",
		buttonImageOnly: true,
		onSelect: function(dateText, inst) {$("#tx_dval_ini_day_id").val(dateText.substr(0,2));
											$("#tx_dval_ini_month_id").val(dateText.substr(3,2));
											$("#tx_dval_ini_year_id").val(dateText.substr(6,4));
											},
		beforeShow: function(input, inst) {
		//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
        	updateValoreDatePickerFromDdl("tx_dval_ini_day_id",
		    	                          "tx_dval_ini_month_id",
		        	                      "tx_dval_ini_year_id",
		            	                  "tx_dval_ini_hidden");
		}
	});
	$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
	$("#tx_dval_a_ini_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
	$("#tx_dval_a_ini_hidden").datepicker({
		minDate: new Date(annoDa, 0, 1),
		maxDate: new Date(annoA, 11, 31),
		yearRange: annoDa + ":" + annoA,
		showOn: "button",
		buttonImage: "../applications/templates/shared/img/calendar.gif",
		buttonImageOnly: true,
		onSelect: function(dateText, inst) {$("#tx_dval_a_ini_day_id").val(dateText.substr(0,2));
											$("#tx_dval_a_ini_month_id").val(dateText.substr(3,2));
											$("#tx_dval_a_ini_year_id").val(dateText.substr(6,4));
											},
		beforeShow: function(input, inst) {
		//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
        	updateValoreDatePickerFromDdl("tx_dval_a_ini_day_id",
		    	                          "tx_dval_a_ini_month_id",
		        	                      "tx_dval_a_ini_year_id",
		            	                  "tx_dval_a_ini_hidden");
		}
	});	
	$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
	$("#tx_dval_fin_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
	$("#tx_dval_fin_hidden").datepicker({
		minDate: new Date(annoDa, 0, 1),
		maxDate: new Date(annoA, 11, 31),
		yearRange: annoDa + ":" + annoA,
		showOn: "button",
		buttonImage: "../applications/templates/shared/img/calendar.gif",
		buttonImageOnly: true,
		onSelect: function(dateText, inst) {$("#tx_dval_fin_day_id").val(dateText.substr(0,2));
											$("#tx_dval_fin_month_id").val(dateText.substr(3,2));
											$("#tx_dval_fin_year_id").val(dateText.substr(6,4));
											},
		beforeShow: function(input, inst) {
		//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
        	updateValoreDatePickerFromDdl("tx_dval_fin_day_id",
		    	                          "tx_dval_fin_month_id",
		        	                      "tx_dval_fin_year_id",
		            	                  "tx_dval_fin_hidden");
		}
	});
	$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
	$("#tx_dval_a_fin_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
	$("#tx_dval_a_fin_hidden").datepicker({
		minDate: new Date(annoDa, 0, 1),
		maxDate: new Date(annoA, 11, 31),
		yearRange: annoDa + ":" + annoA,
		showOn: "button",
		buttonImage: "../applications/templates/shared/img/calendar.gif",
		buttonImageOnly: true,
		onSelect: function(dateText, inst) {$("#tx_dval_a_fin_day_id").val(dateText.substr(0,2));
											$("#tx_dval_a_fin_month_id").val(dateText.substr(3,2));
											$("#tx_dval_a_fin_year_id").val(dateText.substr(6,4));
											},
		beforeShow: function(input, inst) {
		//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
        	updateValoreDatePickerFromDdl("tx_dval_a_fin_day_id",
		    	                          "tx_dval_a_fin_month_id",
		        	                      "tx_dval_a_fin_year_id",
		            	                  "tx_dval_a_fin_hidden");
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
	<s:form name="form_selezione" action="tariffe.do?vista=tariffe" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA CONFIGURAZIONE TARIFFE
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
				<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist name="tx_piazzola" disable="false" label="Codice Piazzola:"
						multiple="false" valueselected="${tx_piazzola}" 
						cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
						<s:ddloption text="seleziona..." value="" />
						<s:ddloptionbinder options="${piazzolaDDLList}"/>
					</s:dropdownlist>		
				</s:div>
				<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
					<s:div name="divElement11a" cssclass="labelData">
						<s:label name="label_data_ini_val" cssclass="seda-ui-label label85 bold textright"
							text="Data Inizio Validità:" />
					</s:div>
					<s:div name="divElement11b" cssclass="floatleft">
						<s:div name="divDataDa_car" cssclass="divDataDa">
							<s:date label="Da:" prefix="tx_dval_ini" yearbegin="${ddlDateAnnoDa}"
								cssclasslabel="labelsmall"
								cssclass="dateman"
								yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
								separator="/" calendar="${tx_dval_da_ini}"></s:date>
							<input type="hidden" id="tx_dval_ini_hidden" value="" />
						</s:div>
						<s:div name="divDataA_car" cssclass="divDataA">
							<s:date label="A:" prefix="tx_dval_a_ini" yearbegin="${ddlDateAnnoDa}"
								cssclasslabel="labelsmall"
								cssclass="dateman"
								yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
								separator="/" calendar="${tx_dval_a_ini}"></s:date>
							<input type="hidden" id="tx_dval_a_ini_hidden" value="" />
						</s:div>
					</s:div>
				</s:div>							
			</s:div>
			<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">			
				<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist name="tx_giorno_settimana" disable="false" label="Giorno della Settimana:"
						multiple="false" valueselected="${tx_giorno_settimana}" 
						cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
						<s:ddloption text="seleziona..." value="0" />
						<s:ddloption text="Lunedì" value="1" />
						<s:ddloption text="Martedì" value="2" />
						<s:ddloption text="Mercoledì" value="3" />
						<s:ddloption text="Giovedì" value="4" />
						<s:ddloption text="Venerdì" value="5" />
						<s:ddloption text="Sabato" value="6" />
						<s:ddloption text="Domenica" value="7" />
					</s:dropdownlist>		
				</s:div>
				<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" name="tx_num_autor"
						label="Codice Autorizzazione:" maxlenght="30" 
						cssclasslabel="label85 bold textright" bdisable="${codop == 'edit'}"
						cssclass="textareaman" validator="ignore"
						text="${tx_num_autor}" />
				</s:div>
				<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
					<s:div name="divElement11a" cssclass="labelData">
						<s:label name="label_data_fin_val" cssclass="seda-ui-label label85 bold textright"
							text="Data Fine Validità:" />
					</s:div>
					<s:div name="divElement11b" cssclass="floatleft">
						<s:div name="divDataDa_car" cssclass="divDataDa">
							<s:date label="Da:" prefix="tx_dval_fin" yearbegin="${ddlDateAnnoDa}"
								cssclasslabel="labelsmall"
								cssclass="dateman"
								yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
								separator="/" calendar="${tx_dval_fin}"></s:date>
							<input type="hidden" id="tx_dval_fin_hidden" value="" />
						</s:div>
						<s:div name="divDataA_car" cssclass="divDataA">
							<s:date label="A:" prefix="tx_dval_a_fin" yearbegin="${ddlDateAnnoDa}"
								cssclasslabel="labelsmall"
								cssclass="dateman"
								yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
								separator="/" calendar="${tx_dval_a_fin}"></s:date>
							<input type="hidden" id="tx_dval_a_fin_hidden" value="" />
						</s:div>
					</s:div>
				</s:div>				
			</s:div>
			
			<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
				<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist name="tx_tipologia_banco" disable="false" label="Tipologia Banco:"
						multiple="false" valueselected="${tx_tipologia_banco}" 
						cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
						<s:ddloption text="seleziona..." value="" />
						<s:ddloptionbinder options="${tipbancoDDLList}"/>
					</s:dropdownlist>		
				</s:div>
				<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist name="tx_tx_periodo_giornal" disable="false" label="Periodo Giornaliero:"
						multiple="false" valueselected="${tx_periodo_giornal}" 
						cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
						<s:ddloption text="seleziona..." value="" />
						<s:ddloptionbinder options="${pergiornDDLList}"/>
					</s:dropdownlist>		
				</s:div>				
			</s:div>				
				
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_add" validate="false"  onclick="" text="Nuovo" type="submit" cssclass="btnStyle" />
				<c:if test="${!empty lista_configurazioni_tariffe}">
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

<c:if test="${!empty lista_configurazioni_tariffe}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO TARIFFE
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_configurazioni_tariffe"
			action="tariffe.do?vista=tariffeLista" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			
			<s:dgcolumn index="6" label="Area Mercantile" css="text_align_left"/>
			<s:dgcolumn index="8" label="Descr. Piazzola" css="text_align_left"/>
			<s:dgcolumn index="9" label="Cod. Autorizzazione" css="text_align_left"/>
			<s:dgcolumn index="11" label="Giorno della Settimana" css="text_align_left"/>
			<s:dgcolumn index="14" label="Data Inizio Validit&agrave;" css="text_align_left"/>
			<s:dgcolumn index="15" label="Data Fine Validit&agrave;" css="text_align_left"/>
			<s:dgcolumn label="Azioni">
				<s:hyperlink
					href="tariffeEdit.do?vista=tariffe&codop=edit&ddlSocietaUtenteEnte={1}|{2}|{3}&tx_cod_key_tariffa_h={4}&tx_area_mercantile={5}&tx_piazzola={7}&tx_num_autor={9}&tx_giorno_settimana={10}&tx_periodo_giornal={12}&tx_tipologia_banco={13}"
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Modifica" text=""
					cssclass="blacklink hlStyle" />
				<s:hyperlink
					href="tariffeEdit.do?vista=tariffe&tx_button_cancel=&ddlSocietaUtenteEnte={1}|{2}|{3}&tx_cod_key_tariffa_h={4}&tx_area_mercantile={5}&tx_piazzola={7}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text=""
					cssclass="blacklink hlStyle" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
	
</c:if>
