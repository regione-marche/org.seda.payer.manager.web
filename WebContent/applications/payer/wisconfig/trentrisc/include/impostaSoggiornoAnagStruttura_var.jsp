<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="impostaSoggiornoAnagStruttura_var" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>

<link href="../applications/templates/wismanager/trentrisc/anagstruttura.css" rel="stylesheet" type="text/css" >

<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		console.log(buttonFired);
		console.log(buttonName);
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}

	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();
	
	$(function() {

		$("#tx_autorizzazione_corr").attr("disabled",true);

		var correlata = "${tx_autorizzazione_corr}";
		console.log(correlata);
		if(correlata=='' || correlata==null){
			$("#tx_autorizzazione_corr").attr("disabled",true);
		}else{
			$("#tx_autorizzazione_corr").attr("disabled",false);
		}

		$('input:radio').change(function() { 
			console.log($(this).val());
			if ($(this).val() == "P") {
				$("#tx_autorizzazione_corr").attr("disabled",true);
				$("#tx_autorizzazione_corr").val("");
			}else{
				$("#tx_autorizzazione_corr").attr("disabled",false);
			}
		});
		
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
		$.datepicker.setDefaults($.datepicker.regional["it"]);
		$("#tx_data_nascita_tit_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_nascita_tit_hidden").datepicker( {
			minDate: new Date(1900, 0, 1),
			maxDate: new Date(new Date().getFullYear(), 11, 31),
			yearRange: 1900 + ":" + new Date().getFullYear(),
			showOn : "button",
			buttonImage : "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly : true,
			onSelect : function(dateText, inst) {
				$("#tx_data_nascita_tit_day_id").val(dateText.substr(0, 2));
				$("#tx_data_nascita_tit_month_id").val(dateText.substr(3, 2));
				$("#tx_data_nascita_tit_year_id").val(dateText.substr(6, 4));
			},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_nascita_tit_day_id",
				                            "tx_data_nascita_tit_month_id",
				                            "tx_data_nascita_tit_year_id",
				                            "tx_data_nascita_tit_hidden");
			}
		});
		$.datepicker.setDefaults($.datepicker.regional["it"]);
		$("#tx_data_nascita_gest_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_nascita_gest_hidden").datepicker( {
			minDate: new Date(1900, 0, 1),
			maxDate: new Date(new Date().getFullYear(), 11, 31),
			yearRange: 1900 + ":" + new Date().getFullYear(),
			showOn : "button",
			buttonImage : "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly : true,
			onSelect : function(dateText, inst) {
				$("#tx_data_nascita_gest_day_id").val(dateText.substr(0, 2));
				$("#tx_data_nascita_gest_month_id").val(dateText.substr(3, 2));
				$("#tx_data_nascita_gest_year_id").val(dateText.substr(6, 4));
			},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_nascita_gest_day_id",
				                            "tx_data_nascita_gest_month_id",
				                            "tx_data_nascita_gest_year_id",
				                            "tx_data_nascita_gest_hidden");
			}
		});


	});


</script>

<style>
.lblRegistrazione{
	width:95px !important;
}
</style>


<script type="text/javascript">
function setFiredButton(buttonName) {
	var buttonFired = document.getElementById('fired_button_hidden');
	console.log(buttonFired);
	console.log(buttonName);
	if (buttonFired != null)
		buttonFired.value = buttonName;
}
</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_form_anag_struttura" action="${do_command_name}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">ANAGRAFICA STRUTTURA</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">ANAGRAFICA STRUTTURA</s:div>
			</c:if>
			
			
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			<s:div name="divSectionBorderStruttura" cssclass="divSectionBorder">
				<s:div name="divSectionTitleStruttura" cssclass="divSectionTitle">STRUTTURA</s:div>
				
				<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				
				<s:div name="divRicMetadatiTopLeftAnag" cssclass="divRicMetadatiTopLeft">
				<s:dropdownlist name="tx_provincia"
							disable="${codop == 'add'?'false':'true'}" 
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
				<s:div name="divRicMetadatiTopCenterAnag" cssclass="divRicMetadatiTopCenter">
				<s:dropdownlist name="tx_comune"
									disable="${codop == 'add'?'false':'true'}" 
									label="Comune:"
									cssclasslabel="label85 bold textright"
									cssclass="tbddl"
									cachedrowset="listbelfiore" usexml="true"
									validator="required" showrequired="true"
									valueselected="${tx_comune}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption  value="{1}" text="{4}"/>
								</s:dropdownlist></s:div>
				<s:div name="divRicMetadatiTopRightAnag" cssclass="divRicMetadatiTopRigth">
				<c:choose>
				<c:when test="${codop == 'edit'}">
					<s:textbox bmodify="false" name="tx_autorizzazione"
								label="Num. Auto.: "
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled"
								text="${tx_autorizzazione}"/>
				</c:when>
				<c:otherwise>
				
					<s:textbox bmodify="true" name="tx_autorizzazione"
								label="Num. Auto.: " maxlenght="100" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="required"
								showrequired="true"
								text="${tx_autorizzazione}" message="[accept=num. Autor.: ${msg_configurazione_descrizione_regex}]"/>
				</c:otherwise>
				</c:choose>
			
							
					
						</s:div>
				
				</s:div>
				</s:div>
				
				
				<s:div name="divLeft" cssclass="divRicMetadatiLeftAnagStruttura">

					<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">	
							
								<s:textbox bmodify="true" name="tx_fiscale"
								label="Cod.Fisc./P.IVA: " maxlenght="16" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${tx_fiscale}" message="[accept=Cod.Fisc./P.IVA: ${msg_configurazione_descrizione_regex}]"/>
						
					</s:div>
					<s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
					
					<s:textbox bmodify="true" name="tx_cap"
								label="CAP: " maxlenght="5" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${tx_cap}" message="[accept=CAP: ${msg_configurazione_descrizione_regex}]"/>
							
						</s:div>
					
					
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_telefono"
								label="Telefono: " maxlenght="10" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${tx_telefono}" message="[accept=Telefono: ${msg_configurazione_descrizione_regex}]"/>
						
						</s:div>
						<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
							
						<s:dropdownlist name="flagSubentro"
									disable="false" 
									label="Flag Subentro:"
									cssclasslabel="label85 bold textright"
									cssclass="tbddl"
									cachedrowset="listStrutture" usexml="true"
									validator="ignore" showrequired="true"
									valueselected="${flagSubentro}">
									
									<s:ddloption  value="" text="No"/>
									<s:ddloption  value="S" text="Si"/>
								</s:dropdownlist>
						</s:div>
						<s:div name="divElement33" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_subalterno"
								label="Subalterno: " maxlenght="4" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=^[0-9]*$;"
								text="${tx_subalterno}" message="[accept=Subalterno: valore non corretto]"/>
						</s:div>
						<s:div name="divElement34" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_piano"
								label="Piano: " maxlenght="50" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;"
								text="${tx_piano}" message="[accept=Piano: valore non corretto]"/>
						</s:div>
						
				
				</s:div>
				
				<s:div name="divCenter" cssclass="divRicMetadatiCenterAnagStruttura">

						<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_rag_soc"
								label="Rag. Sociale Strutt.: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_ragionesociale_regex}"
								text="${tx_rag_soc}" message="[accept=Rag. Sociale Strutt.: ${msg_configurazione_ragionesociale_regex}]"/>
						</s:div>
						<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_indirizzo"
								label="Indirizzo: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${tx_indirizzo}" message="[accept=Indirizzo: ${msg_configurazione_descrizione_regex}]"/>
						</s:div>
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_mail"
								label="Mail: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${wisconfig_email_regex};"
								text="${tx_mail}" message="[accept=Mail: indirizzo mail non corretto]"/>
						</s:div>
						<s:div name="divElement29" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" name="tx_comuneCatastale"
								label="Comune Catastale:" maxlenght="4" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								text="${tx_comuneCatastale}"/>
						</s:div>
						<s:div name="divElement31" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_superficie"
								label="Metri Quadri: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=^\s*(?=.*[1-9])\d*(?:\.\d{1,2})?\s*$;"
								text="${tx_superficie}" message="[accept=Metri Quadri: Inserire un valore valido]"/>
						</s:div>
				</s:div>
				
				<s:div name="divRight" cssclass="divRicMetadatiRightAnagStruttura">
						<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_insegna"
								label="Insegna: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_ragionesociale_regex}"
								text="${tx_insegna}" message="[accept=Insegna: ${msg_configurazione_ragionesociale_regex}]"/>
						</s:div>
						
						<s:div name="divElement12" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="strutturaRicettiva"
									disable="false" 
									label="Tipol. Strut.:"
									cssclasslabel="label85 bold textright"
									cssclass="tbddl"
									cachedrowset="listStrutture" usexml="true"
									validator="required" showrequired="true"
									valueselected="${strutturaRicettiva}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption  value="{1}" text="{2}"/>
								</s:dropdownlist>
							
						</s:div>
						<s:div name="divElement13" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_mailPec"
								label="Mail Pec: " maxlenght="50" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${wisconfig_email_regex};"
								text="${tx_mailPec}" message="[accept=Mail Pec: indirizzo mail non corretto]"/>
						</s:div>
						<s:div name="divElement30" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_particellaEdificiale"
								label="Particella Edificiale: " maxlenght="10" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=^[0-9]*$;"
								text="${tx_particellaEdificiale}" message="[accept=Perticella Edificiale: valore non corretto]"/>
						</s:div>
						<s:div name="divElement32" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_postiLetto"
								label="Posti Letto: " maxlenght="3" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=^[0-9]*$;"
								text="${tx_postiLetto}" message="[accept=Posti Letto: valore non corretto]"/>
						</s:div>
				</s:div>
				
				<s:div name="divElement14" cssclass="divRicMetadatiLeftAnagStruttura">
				
					<s:div name="divTipoStruttura" cssclass="divGroup">
					<s:label name="lblTipoStruttura" cssclass="lblRegistrazione" text="Tipo Struttura *" />
					<s:list name="rbPrincipale" bradio="true" groupname="grTipoStruttura" bchecked="${requestScope.tx_autorizzazione_corr=='' || requestScope.tx_autorizzaizone_corr==null}"
						value="P" text="Principale" cssclass="rbSesso" cssclasslabel="lblSesso" tabindex="9"/>
					<s:list name="rbCorrelata" bradio="true" groupname="grTipoStruttura" bchecked="${requestScope.tx_autorizzazione_corr!='' && requestScope.tx_autorizzazione_corr!=null}"
						value="C" text="Correlata" cssclass="rbSesso" cssclasslabel="lblSesso" tabindex="10"/>
				</s:div>
				<s:div name="divElement17" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_autorizzazione_corr"
								label="Numero Aut. Correlata: " maxlenght="50" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator=""
								text="${tx_autorizzazione_corr}" message="[accept=num. Autor. Corr.: ${msg_configurazione_descrizione_regex}]"/>
						</s:div>
							
						</s:div>
				
				<s:div name="divElement15" cssclass="divRicMetadatiCenterAnagStruttura">
							<s:div name="divElement18a" cssclass="labelData">
								<s:label name="label_data"
									cssclass="seda-ui-label label85 bold textright"
									text="Data Obbligo Comunicazione" />
							</s:div>
						<s:div name="divElement15b" cssclass="floatleft">
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
							
				<s:div name="divElement16" cssclass="divRicMetadatiRightAnagStruttura">
			<s:div name="divElement18a" cssclass="labelData">
								<s:label name="label_data"
									cssclass="seda-ui-label label85 bold textright"
									text="Data Validità Struttura" />
							</s:div>
	
							<s:div name="divElement14b" cssclass="floatleft">
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
				
				
				
										
			</s:div>
				
			<s:div name="divSectionBorderTitolare" cssclass="divSectionBorder">
				<s:div name="divSectionTitleTitolare" cssclass="divSectionTitle">ANAGRAFICA TITOLARE</s:div>
				<s:div name="divLeft" cssclass="divRicMetadatiLeftAnagStruttura">
						
						<s:div name="divElement18" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_fiscale_tit"
								label="Cod.Fisc.Tit.: " maxlenght="16" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${tx_fiscale_tit}" message="[accept=Cod.Fisc.Tit.: ${msg_configurazione_descrizione_regex}]"/>
						</s:div>	
						
						<s:div name="divElement19" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_provincia_tit"
							disable="false" 
							label="Provincia:"
							cssclasslabel="label85 bold floatleft textright"
							cssclass="tbddl floatleft"
							cachedrowset="listProvince" usexml="true"
							onchange="setFiredButton('tx_button_provincia_tit');this.form.submit();"
							validator="" showrequired="false"
							valueselected="${tx_provincia_tit}">
							<s:ddloption text="Selezionare uno degli elementi" value="" />
							<s:ddloption  value="{2}" text="{1}"/>
						</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_provincia_tit" onclick="" text="" validate="false"
								type="submit" cssclass="btnimgStyle" title="Aggiorna" />
							</noscript>
					</s:div>
						
										
					</s:div>
					
					<s:div name="divCenter" cssclass="divRicMetadatiCenterAnagStruttura">
						
						<s:div name="divElement20" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_titolare"
								label="Titolare: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${tx_titolare}" message="[accept=Titolare: ${msg_configurazione_descrizione_regex}]"/>
						</s:div>
						<s:div name="divElement21" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="tx_comune_tit"
									disable="false" 
									label="Comune:"
									cssclasslabel="label85 bold textright"
									cssclass="tbddl"
									cachedrowset="listbelfioreTit" usexml="true"
									validator="" showrequired="false"
									valueselected="${tx_comune_tit}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption  value="{1}" text="{4}"/>
								</s:dropdownlist>
						</s:div>
					</s:div>
					<s:div name="divRight" cssclass="divRicMetadatiRightAnagStruttura">
					<s:div name="divElement22" cssclass="divRicMetadatiSingleRow">
								<s:div name="divDataNascitaTit" cssclass="divDataDa">
									<s:date label="Data Nascita:" prefix="tx_data_nascita_tit"
										yearbegin="1900" yearend="${requestScope.yearNow}"
										locale="IT-it" descriptivemonth="false" separator="/"
										calendar="${tx_data_nascita_tit}" cssclasslabel="label85 bold textright"
										cssclass="dateman" validator="required">
									</s:date>
									<input type="hidden" id="tx_data_nascita_tit_hidden" value="" />
								</s:div>					
							</s:div>
					</s:div>
				
			</s:div>
			<s:div name="divSectionBorderGestore" cssclass="divSectionBorder">
				<s:div name="divSectionTitleGestore" cssclass="divSectionTitle">ANAGRAFICA GESTORE</s:div>
				<s:div name="divLeft" cssclass="divRicMetadatiLeftAnagStruttura">
						
						<s:div name="divElement23" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_fiscale_gest"
								label="Cod.Fisc.Gest.: " maxlenght="16" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${tx_fiscale_gest}" message="[accept=Cod.Fisc.Gest.: ${msg_configurazione_descrizione_regex}]"/>
						</s:div>
						
						<s:div name="divElement24" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_provincia_gest"
							disable="false" 
							label="Provincia:"
							cssclasslabel="label85 bold floatleft textright"
							cssclass="tbddl floatleft"
							cachedrowset="listProvince" usexml="true"
							onchange="setFiredButton('tx_button_provincia_gest');this.form.submit();"
							validator="" showrequired="false"
							valueselected="${tx_provincia_gest}">
							<s:ddloption text="Selezionare uno degli elementi" value="" />
							<s:ddloption  value="{2}" text="{1}"/>
						</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_changed" onclick="" text="" validate="false"
								type="submit" cssclass="btnimgStyle" title="Aggiorna" />
							</noscript>
					</s:div>	
											
					</s:div>
					
					<s:div name="divCenter" cssclass="divRicMetadatiCenterAnagStruttura">
						
						<s:div name="divElement25" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_gestore"
								label="Gestore: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${tx_gestore}" message="[accept=Titolare: ${msg_configurazione_descrizione_regex}]"/>
						</s:div>
						
						<s:div name="divElement26" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="tx_comune_gest"
									disable="false" 
									label="Comune:"
									cssclasslabel="label85 bold textright"
									cssclass="tbddl"
									cachedrowset="listbelfioreGest" usexml="true"
									validator="" showrequired="false"
									valueselected="${tx_comune_gest}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption  value="{1}" text="{4}"/>
								</s:dropdownlist>
						</s:div>
					</s:div>
					<s:div name="divRight" cssclass="divRicMetadatiRightAnagStruttura">
					<s:div name="divElement27" cssclass="divRicMetadatiSingleRow">
								<s:div name="divDataNascitaGest" cssclass="divDataDa">
									<s:date label="Data Nascita:" prefix="tx_data_nascita_gest"
										yearbegin="1900" yearend="${requestScope.yearNow}"
										locale="IT-it" descriptivemonth="false" separator="/"
										calendar="${tx_data_nascita_gest}" cssclasslabel="label85 bold textright"
										cssclass="dateman" validator="required">
									</s:date>
									<input type="hidden" id="tx_data_nascita_gest_hidden" value="" />
								</s:div>					
							</s:div>
					</s:div>
			</s:div>			
					
					<s:div name="divElement28" cssclass="divRicMetadatiSingleRow">
						<s:textarea name="tx_note" label="Note:" text="${tx_note}" bmodify="true" row="5" col="105" 
							cssclasslabel="label85 bold textright" cssclass="textAreaNote" validator="maxlength=3000" />
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
