<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="InvioSollecitoEdit" encodeAttributes="true" />

<jsp:useBean id="typeRequest"
	class="com.seda.payer.commons.bean.TypeRequest" />


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
		$("#tx_dval_sms_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_dval_sms_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_dval_sms_day_id").val(dateText.substr(0,2));
												$("#tx_dval_sms_month_id").val(dateText.substr(3,2));
												$("#tx_dval_sms_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_dval_sms_day_id",
				                              "tx_dval_sms_month_id",
				                              "tx_dval_sms_year_id",
				                              "tx_dval_sms_hidden");
			}
		});
	});

	function setFired() {
			var buttonFired = document.getElementById('fired_button_hidden');
			buttonFired.value = 'tx_button_societa_changed';
	}
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>

	

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_form" action="inviosollecitoEdit.do?vista=${vista}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">CONFIGURAZIONE INVIO SOLLECITO</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">CONFIGURAZIONE INVIO SOLLECITO</s:div>
			</c:if>
									
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<%-- TODO da verificare la ddl che segue ripresa dal prototipo e non dalla pagina originale di riferimento --%>
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTopDouble">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
					
						<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label160 bold textright floatleft" validator="required;"
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
						
				<s:div name="divRicercaMetadatiLeft" cssclass="divRicMetadatiLeft">
				
					<!--<s:div name="divElement11b" cssclass="divRicMetadatiSingleRow">
						<s:div name="divDataDa" cssclass="floatleft">
							<s:div name="divDataDa_validita" cssclass="divDataDa">
								<s:date label="Data Validità:"  prefix="tx_dval_sms" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="label85"
									cssclass="dateman" showrequired="true"
									disabled="${codop == 'edit'}"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_dval_sms}">
								</s:date>
								<input type="hidden" id="tx_dval_sms_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>-->
					<c:if test="${codop == 'edit'}">

						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:div name="divDataValidita" cssclass="divDataDa">
									<!--<s:date label="Data Validit&agrave;:" prefix="tx_dval_sms" yearbegin="${ddlDateAnnoDa}"
										yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
										separator="/" calendar="${tx_dval_sms}"
										cssclasslabel="label85 bold textright"
										cssclass="dateman"
										disabled="true" 
										validator="ignore" showrequired="true">
									</s:date>-->
									
									<s:date label="Data Validit&agrave;:" prefix="tx_dval_sms" yearbegin="${ddlDateAnnoDa}"
										yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
										separator="/" calendar="${tx_dval_sms}"
										cssclasslabel="label85 bold textright"
										cssclass="dateman" disabled="true" >
									</s:date>
									
									<!--  <input type="hidden" id="tx_dval_sms_hidden" value="" />-->
								</s:div>
								
								
						</s:div>
					</c:if>
					<c:if test="${codop != 'edit'}">
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:div name="divDataValidita" cssclass="divDataDa">
									<s:date label="Data Validit&agrave;:" prefix="tx_dval_sms" yearbegin="${ddlDateAnnoDa}"
										yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
										separator="/" calendar="${tx_dval_sms}"
										cssclasslabel="label85 bold textright"
										cssclass="dateman"
										disabled="false" 
										validator="dateISO;required" showrequired="true"
										message="[dateISO=Data Validit&agrave;: ${msg_dataISO_valida}]">
									</s:date>
									<input type="hidden" id="tx_dval_sms_hidden" value="" />
								</s:div>
						</s:div>
					</c:if>


			        <s:div name="divElement1" cssclass="divRicMetadatiSingleRow">	
						<s:textbox bmodify="true" name="tx_importo_residuo"
							label="Imp. Res.:" maxlenght="18"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							validator="required;accept=^[-][0-9]{1,13}\,[0-9]{2}?|^[0-9]{1,13}\,[0-9]{2}?$;maxlength=11"
							message="[accept=Imp. Residuo ${msg_configurazione_importo_8_2}]"
							text="${tx_importo_residuo}" />
					</s:div> 
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_intervallo_gg"
							label="Inter. GG Invii SMS:" maxlenght="3"
							validator="required;digits;maxlength=3"
							bdisable="${tx_tipo_sms=='' || tx_tipo_sms=='P'}" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_intervallo_gg}" />
					</s:div>					
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_tipo_schedulazione"  
							label="Schedulaz. SMS: " showrequired="true"
							disable="${tx_tipo_sms=='' || tx_tipo_sms=='P'}"
							cssclasslabel="label85 bold textright" 
							cssclass="textareaman" 
							valueselected="${tx_tipo_schedulazione}">
  							<s:ddloption text="Giornaliera" value="G" />
  							<s:ddloption text="Settimanale" value="S" />
  							<s:ddloption text="Quindicinale" value="Q" />
							<s:ddloption text="Mensile" value="M" />
						</s:dropdownlist>
					</s:div>	
					
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_flagAttivazione"  
							label="Regola Attiva: " showrequired="true"
							disable="false"
							cssclasslabel="label85 bold textright" 
							cssclass="textareaman" 
							valueselected="${tx_flagAttivazione}">
  							<s:ddloption text="Attiva" value="Y" />
  							<s:ddloption text="Non Attiva" value="N" />
  							
						</s:dropdownlist>
					</s:div>			
				</s:div>
				
				<s:div name="divRicercaRight2" cssclass="divRicMetadatiCenter">
				 	<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_tipo_sms" 
							label="Tipo Sollecito: " 
							validator="required;"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman"
							onchange="setFiredButton('tx_button_tipo_sms');this.form.submit();"
							disable="${codop == 'edit'}"
							valueselected="${tx_tipo_sms}">
							<s:ddloption text="Seleziona" value="" />
							<s:ddloption value="C" text="SMS di Cortesia"/>
							<s:ddloption value="S" text="SMS di Sollecito"/>
							<s:ddloption value="P" text="Sollecito Cartaceo"/>
						</s:dropdownlist>
					</s:div>
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">	
						<s:dropdownlist name="tx_inviomail"  
									label="Invio Email:" showrequired="true"
									disable="${tx_tipo_sms=='' || tx_tipo_sms=='P'  || tx_tipo_sms=='C'}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman" 
									valueselected="${tx_inviomail}">
   							<s:ddloption value="Y" text="SI"/>
							<s:ddloption value="N" text="NO"/>
						</s:dropdownlist>
					</s:div>
					
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:div name="divElement11a" cssclass="labelData">
							<s:label name="label_data_pag" cssclass="seda-ui-label label85 bold textright"
								text="Perido invio SMS" />
						</s:div>
						<s:div name="divElement11b" cssclass="floatleft">
							<s:dropdownlist name="tx_mese_inizio"  
								label="Mese inizio:" showrequired="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								disable="${tx_tipo_sms == '' || tx_tipo_sms=='P'}"
								valueselected="${tx_mese_inizio}">
	   							<s:ddloption text="" value="0" />
								<s:ddloption text="Gennaio" value="1" />
								<s:ddloption text="Febbraio" value="2" />
								<s:ddloption text="Marzo" value="3" />
								<s:ddloption text="Aprile" value="4" />
								<s:ddloption text="Maggio" value="5" />
								<s:ddloption text="Giugno" value="6" />
								<s:ddloption text="Luglio" value="7" />
								<s:ddloption text="Agosto" value="8" />
								<s:ddloption text="Settembre" value="9" />
								<s:ddloption text="Ottobre" value="10" />
								<s:ddloption text="Novembre" value="11" />
								<s:ddloption text="Dicembre" value="12" />
							</s:dropdownlist>

							<s:dropdownlist name="tx_mese_fine"  
								label="Mese fine:" showrequired="true"								
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								disable="${tx_tipo_sms=='' || tx_tipo_sms=='P'}"
								valueselected="${tx_mese_fine}">
	   							<s:ddloption text="" value="0" />
								<s:ddloption text="Gennaio" value="1" />
								<s:ddloption text="Febbraio" value="2" />
								<s:ddloption text="Marzo" value="3" />
								<s:ddloption text="Aprile" value="4" />
								<s:ddloption text="Maggio" value="5" />
								<s:ddloption text="Giugno" value="6" />
								<s:ddloption text="Luglio" value="7" />
								<s:ddloption text="Agosto" value="8" />
								<s:ddloption text="Settembre" value="9" />
								<s:ddloption text="Ottobre" value="10" />
								<s:ddloption text="Novembre" value="11" />
								<s:ddloption text="Dicembre" value="12" />
							</s:dropdownlist>							
						</s:div>
					</s:div>

				</s:div>	
				
				
				
				<s:div name="divRicercaRight3" cssclass="divRicMetadatiRight">				
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:label cssclass="label85 bold textcenter" name="sottotitolo" text="Dati per sollecito cartaceo" />
					</s:div>
			
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_codiceonere_cart"
							label="Cod. Onere:" maxlenght="3" 
							bdisable="${tx_tipo_sms!='P'}"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							message="[accept=Codice Onere.: ${msg_configurazione_descrizione_3}]"
							text="${tx_codiceonere_cart}" />
					</s:div>

					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" name="tx_desconere_cart"
							label="Desc. Onere:" maxlenght="100" 
							cssclasslabel="label85 bold textright"
							bdisable="${tx_tipo_sms!='P'}"
							cssclass="textareaman" 
							message="[accept=Intervallo Giorni.: ${msg_configurazione_descrizione_3}]"
							text="${tx_desconere_cart}" />
					</s:div>
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_importo_onere"
							label="Imp Onere:" maxlenght="18" 
							cssclasslabel="label85 bold textright"
							bdisable="${tx_tipo_sms!='P'}"
							cssclass="textareaman" 
							validator="accept=^[0-9]{1,13}\,[0-9]{2}?$;maxlength=11"
							message="[accept=Imp. Oner ${msg_configurazione_importo_8_2}]"
							text="${tx_importo_onere}" />
					</s:div>	
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_ggrivis_ana"
							label="GG. Rivis. Ana.:" maxlenght="3" 
							cssclasslabel="label85 bold textright"
							bdisable="${tx_tipo_sms!='P'}"
							cssclass="textareaman" 
							validator="digits;maxlength=3"
							text="${tx_ggrivis_ana}" />
					</s:div>
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_intervallo_gg_sol"
							label="Inter. GG Invii SOL:" maxlenght="3"
							validator="required;digits;maxlength=3"
							bdisable="${tx_tipo_sms!='P'}" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_intervallo_gg_sol}" />
					</s:div>	
					<c:if test="${tx_tipo_sms!='P'}">
						<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_tribX_evoluzione"
								label="Tributo per Evoluzione:" maxlenght="4"
								validator="maxlength=4"
								bdisable="${tx_tipo_sms!='P'}" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								text="${tx_tribX_evoluzione}" />
						</s:div>	
					</c:if>
					<c:if test="${tx_tipo_sms=='P'}">
						<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_tribX_evoluzione"
								label="Tributo per Evoluzione:" maxlenght="4"
								validator="required;maxlength=4"
								bdisable="${tx_tipo_sms!='P'}" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								text="${tx_tribX_evoluzione}" />
						</s:div>	
					</c:if>
					
					<!--<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_conto_postale"
							label="Conto Pos:" maxlenght="12" 
							cssclasslabel="label85 bold textright"
							bdisable="${tx_tipo_sms!='P'}"
							cssclass="textareaman" 
							text="${tx_conto_postale}" />
					</s:div>
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_conto_postale_desc"
							label="Desc.Conto Pos:" maxlenght="100" 
							cssclasslabel="label85 bold textright"
							bdisable="${tx_tipo_sms!='P'}"
							cssclass="textareaman" 
							text="${tx_conto_postale_desc}" />
					</s:div>-->

				</s:div>
			</s:div>
		
			<br/> 
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_indietro" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />
				<!-- 
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
				 -->
				<c:if test="${codop == 'add'}">
					<s:button id="tx_button_aggiungi" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${codop == 'edit'}">
					<s:button id="tx_button_edit" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
			</s:div>
			<input type="hidden" name="codop" value="${codop}" />
			<input type="hidden" name="tx_societa" value="${tx_societa}" />
			<input type="hidden" name="tx_utente" value="${tx_utente}" />
			<input type="hidden" name="tx_ente" value="${tx_ente}" />
			<input type="hidden" name="tx_dval_sms_h" value="${tx_dval_sms_h}" />
			<input type="hidden" name="tx_tipo_sms_h" value="${tx_tipo_sms_h}" />
			
			
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
