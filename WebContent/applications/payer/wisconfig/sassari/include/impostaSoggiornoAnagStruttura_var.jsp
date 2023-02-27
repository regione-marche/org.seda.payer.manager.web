<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="impostaSoggiornoAnagStruttura_var" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js" type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js" type="text/javascript"></script>

<script type="text/javascript">
function setFiredButton(buttonName) {
	var buttonFired = document.getElementById('fired_button_hidden');
	if (buttonFired != null)
		buttonFired.value = buttonName;
}
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


	$("#tx_data_obbl_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
	$("#tx_data_obbl_da_hidden").datepicker( {
		minDate: new Date(annoDa, 0, 1),
		maxDate: new Date(annoA, 11, 31),
		yearRange: annoDa + ":" + annoA,
		showOn : "button",
		buttonImage : "../applications/templates/shared/img/calendar.gif",
		buttonImageOnly : true,
		onSelect : function(dateText, inst) {
			$("#tx_data_obbl_da_day_id").val(dateText.substr(0, 2));
			$("#tx_data_obbl_da_month_id").val(dateText.substr(3, 2));
			$("#tx_data_obbl_da_year_id").val(dateText.substr(6, 4));
		},
		beforeShow: function(input, inst) {
			//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
            updateValoreDatePickerFromDdl("tx_data_obbl_da_day_id",
			                            "tx_data_obbl_da_month_id",
			                            "tx_data_obbl_da_year_id",
			                            "tx_data_obbl_da_hidden");
		}
	});
	$("#tx_data_obbl_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
	$("#tx_data_obbl_a_hidden").datepicker( {
		minDate: new Date(annoDa, 0, 1),
		maxDate: new Date(annoA, 11, 31),
		yearRange: annoDa + ":" + annoA,
		showOn : "button",
		buttonImage : "../applications/templates/shared/img/calendar.gif",
		buttonImageOnly : true,
		onSelect : function(dateText, inst) {
			$("#tx_data_obbl_a_day_id").val(dateText.substr(0, 2));
			$("#tx_data_obbl_a_month_id").val(dateText.substr(3, 2));
			$("#tx_data_obbl_a_year_id").val(dateText.substr(6, 4));
		},
		beforeShow: function(input, inst) {
			//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
            updateValoreDatePickerFromDdl("tx_data_obbl_a_day_id",
			                            "tx_data_obbl_a_month_id",
			                            "tx_data_obbl_a_year_id",
			                            "tx_data_obbl_a_hidden");
		}
	});
});

</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_form" action="${do_command_name}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">ANAGRAFICA STRUTTURA</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">ANAGRAFICA STRUTTURA</s:div>
			</c:if>
									
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
							<s:dropdownlist name="tx_provincia"
								disable="true" 
								label="Provincia:"
								cssclasslabel="label85 bold floatleft textright"
								cssclass="tbddl floatleft"
								cachedrowset="listProvince" usexml="true"
								onchange="setFiredButton('tx_button_changed');this.form.submit();"
								validator="required" showrequired="true"
								valueselected="${tx_provincia}">
								<s:ddloption text="Selezionare uno degli elementi" value="" />
								<s:ddloption  value="{2}" text="{1}"/>
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_changed" onclick="" text="" validate="false"
									type="submit" cssclass="btnimgStyle" title="Aggiorna" />
							</noscript>
						</s:div>
						
						<s:div name="tipServLabel_srch" cssclass="divRicMetadatiCenterSmall">
							<s:dropdownlist name="tx_comune"
									disable="true" 
									label="Comune:"
									cssclasslabel="label65 bold textright"
									cssclass="tbddl"
									cachedrowset="listbelfiore" usexml="true"
									validator="required" showrequired="true"
									valueselected="${tx_comune}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption  value="{1}" text="{4}"/>
								</s:dropdownlist>
						</s:div>
						
						<s:div name="divElement2" cssclass="divRicMetadatiTopRight">
							<s:textbox bmodify="true" name="tx_autorizzazione"
								label="Num. Auto.: " maxlenght="100" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="required"
								showrequired="true"
								text="${tx_autorizzazione}" message="[accept=num. Autor.: ${msg_configurazione_descrizione_regex}]"/>
						</s:div>
							
							
						</s:div>
					
					<s:div name="divLeft" cssclass="divRicMetadatiLeft">
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_fiscale"
								label="Cod.Fisc./P.IVA: " maxlenght="16" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${tx_fiscale}" message="[accept=Cod.Fisc./P.IVA: ${msg_configurazione_descrizione_regex}]"/>
						
						</s:div>
						<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_fiscale_tit"
								label="Cod.Fisc.Tit.: " maxlenght="16" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${tx_fiscale_tit}" message="[accept=Cod.Fisc.Tit.: ${msg_configurazione_descrizione_regex}]"/>
						</s:div>
						<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_indirizzo"
								label="Indirizzo: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${tx_indirizzo}" message="[accept=Indirizzo: ${msg_configurazione_descrizione_regex}]"/>
						</s:div>
						<%-- inizio LP PG190010_002_LP --%>
						<s:div name="divElementDataVal" cssclass="divRicMetadatiCenter">
							<s:div name="divElementdv1" cssclass="labelData">
								<s:label name="label_data"
									cssclass="seda-ui-label label85 bold textright"
									text="Data Validità Struttura" />
							</s:div>
	
							<s:div name="divElementdv2" cssclass="floatleft">
								<s:div name="divDataDa" cssclass="divDataDa">
									<s:date label="Da:" prefix="tx_data_da"
										yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
										locale="IT-it" descriptivemonth="false" separator="/"
										calendar="${tx_data_da}" cssclasslabel="labelsmall"
										cssclass="dateman" validator="required">
									</s:date>
									<input type="hidden" id="tx_data_da_hidden" value="" />
								</s:div>
	
								<s:div name="divDataA" cssclass="divDataA">
									<s:date label="A:" prefix="tx_data_a"
										yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
										locale="IT-it" descriptivemonth="false" separator="/"
										calendar="${tx_data_a}" cssclasslabel="labelsmall"
										cssclass="dateman" validator="required">
									</s:date>
									<input type="hidden" id="tx_data_a_hidden" value="" />
								</s:div>
							</s:div>
						</s:div>												
						<%-- fine LP PG190010_002_LP --%>
					</s:div>
					<s:div name="divCenter" cssclass="divRicMetadatiCenter">
						<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_rag_soc"
								label="Rag. Sociale Strutt.: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_ragionesociale_regex}"
								text="${tx_rag_soc}" message="[accept=Rag. Sociale Strutt.: ${msg_configurazione_ragionesociale_regex}]"/>
						</s:div>
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_titolare"
								label="Titolare: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${tx_titolare}" message="[accept=Titolare: ${msg_configurazione_descrizione_regex}]"/>
						</s:div>
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_mail"
								label="Mail: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${wisconfig_email_regex}"
								text="${tx_mail}" message="[accept=Mail: indirizzo mail non corretto]"/>
						</s:div>
						<%-- inizio LP PG190010_002_LP --%>
						<s:div name="divElementDataObb" cssclass="divRicMetadatiRight">
							<s:div name="divElementdo1" cssclass="labelData">
								<s:label name="label_data"
									cssclass="seda-ui-label label85 bold textright"
									text="Data Obbligo Comunicazione" />
							</s:div>
	
							<s:div name="divElementdo2" cssclass="floatleft">
								<s:div name="divDataDa" cssclass="divDataDa">
									<s:date label="Da:" prefix="tx_data_obbl_da"
										yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
										locale="IT-it" descriptivemonth="false" separator="/"
										calendar="${tx_data_obbl_da}" cssclasslabel="labelsmall"
										cssclass="dateman" validator="required">
									</s:date>
									<input type="hidden" id="tx_data_obbl_da_hidden" value="" />
								</s:div>
	
								<s:div name="divDataA" cssclass="divDataA">
									<s:date label="A:" prefix="tx_data_obbl_a"
										yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
										locale="IT-it" descriptivemonth="false" separator="/"
										calendar="${tx_data_obbl_a}" cssclasslabel="labelsmall"
										cssclass="dateman" validator="required">
									</s:date>
									<input type="hidden" id="tx_data_obbl_a_hidden" value="" />
								</s:div>
							</s:div>
						</s:div>
						<%-- fine LP PG190010_002_LP --%>
						
					</s:div>
					<s:div name="divRight" cssclass="divRicMetadatiRight">
						<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_insegna"
								label="Insegna: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_ragionesociale_regex}"
								text="${tx_insegna}" message="[accept=Insegna: ${msg_configurazione_ragionesociale_regex}]"/>
						</s:div>
						<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">	
							<s:textbox bmodify="true" name="tx_cap"
								label="CAP: " maxlenght="5" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${tx_cap}" message="[accept=CAP: ${msg_configurazione_descrizione_regex}]"/>
						</s:div>
						<s:div name="divElement13" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="strutturaRicettiva"
									disable="false" 
									label="Tipol. Strut.:"
									cssclasslabel="label85 bold textright"
									cssclass="tbddl"
									cachedrowset="listStrutture" usexml="true"
									validator="ignore" showrequired="true"
									valueselected="${strutturaRicettiva}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption  value="{1}" text="{2}"/>
								</s:dropdownlist>
						</s:div>	
					</s:div>
						
				</s:div>
			<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_indietro" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
				<c:if test="${codop == 'add'}">
					<s:button id="tx_button_aggiungi" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${codop == 'edit'}">
					<s:button id="tx_button_edit" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
			</s:div>
			<input type="hidden" name="codop" value="${codop}" />
			<input type="hidden" name="tx_anag_struttura" value="${tx_anag_struttura}" />
			<input type="hidden" name="tx_cutecute" value="${tx_cutecute}" />
			<input type="hidden" name="tx_sannfacc" value="${tx_sannfacc}" />
			<input type="hidden" name="tx_csancges" value="${tx_csancges}" />
			<input type="hidden" name="tx_csancise" value="${tx_csancise}" />
			<input type="hidden" name="tx_csanccon" value="${tx_csanccon}" />
			<c:if test="${codop == 'edit'}">
				<input type="hidden" name="tx_provincia" value="${tx_provincia}" />
				<input type="hidden" name="tx_comune" value="${tx_comune}" />
			</c:if>
	</s:form>
	</s:div>
	
</s:div>