<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="compenso" encodeAttributes="true" />

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
	<s:form name="form_selezione" action="compenso.do?vista=compenso" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA CONFIGURAZIONE COMPENSO
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						
						<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label160 bold textright floatleft" validator="ignore;"
									cssclass="seda-ui-ddl tbddlMax780 floatleft" disable="${codop == 'edit'}"
									onchange="setFired();this.form.submit();"
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
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
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
									separator="/" calendar="${tx_dval_ini}"></s:date>
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
			</s:div>
				
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_add" validate="false"  onclick="" text="Nuovo" type="submit" cssclass="btnStyle" />
				<c:if test="${!empty lista_configurazioni_compenso}">
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

<c:if test="${!empty lista_configurazioni_compenso}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO COMPENSI
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_configurazioni_compenso"
			action="compenso.do?vista=compensoLista" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">			
			<s:dgcolumn index="4" format="#,##0.00" label="Importo Fisso" css="text_align_right"/>
			<s:dgcolumn index="5" format="#,##0.00" label="Percentuale Compenso" css="text_align_right"/>
			<s:dgcolumn index="6" format="#,##0.00" label="Percentuale Iva Compenso" css="text_align_right"/>
			<s:dgcolumn index="7" label="Data Inizio Validit&agrave;" css="text_align_left"/>
			<s:dgcolumn index="8" label="Data Fine Validit&agrave;" css="text_align_left"/>
			<s:dgcolumn label="Azioni">
				<s:hyperlink
					href="compensoEdit.do?vista=compenso&codop=edit&ddlSocietaUtenteEnte={1}|{2}|{3}&codiceKeyCompenso={10}"
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Modifica" text=""
					cssclass="blacklink hlStyle" />
				<s:hyperlink
					href="compensoEdit.do?vista=compenso&tx_button_cancel=&ddlSocietaUtenteEnte={1}|{2}|{3}&codiceKeyCompenso={10}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text=""
					cssclass="blacklink hlStyle" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
	
</c:if>
