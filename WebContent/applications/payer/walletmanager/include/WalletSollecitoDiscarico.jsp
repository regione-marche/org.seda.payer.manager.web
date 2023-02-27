<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="walletsollecitodiscarico" encodeAttributes="true" />

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
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#tx_periodo_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_periodo_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_periodo_da_day_id").val(dateText.substr(0,2));
												$("#tx_periodo_da_month_id").val(dateText.substr(3,2));
												$("#tx_periodo_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_periodo_da_day_id",
				                            "tx_periodo_da_month_id",
				                            "tx_periodo_da_year_id",
				                            "tx_periodo_da_hidden");
			}
		});
		$("#tx_periodo_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_periodo_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_periodo_a_day_id").val(dateText.substr(0,2));
												$("#tx_periodo_a_month_id").val(dateText.substr(3,2));
												$("#tx_periodo_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_periodo_a_day_id",
				                            "tx_periodo_a_month_id",
				                            "tx_periodo_a_year_id",
				                            "tx_periodo_a_hidden");
			}
		});
		
	});
</script>
<script type="text/javascript">
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();
	
	$(function() {
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#tx_periodoDis_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_periodoDis_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_periodoDis_da_day_id").val(dateText.substr(0,2));
												$("#tx_periodoDis_da_month_id").val(dateText.substr(3,2));
												$("#tx_periodoDis_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_periodoDis_da_day_id",
				                            "tx_periodoDis_da_month_id",
				                            "tx_periodoDis_da_year_id",
				                            "tx_periodoDis_da_hidden");
			}
		});
		$("#tx_periodoDis_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_periodoDis_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_periodoDis_a_day_id").val(dateText.substr(0,2));
												$("#tx_periodoDis_a_month_id").val(dateText.substr(3,2));
												$("#tx_periodoDis_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_periodoDis_a_day_id",
				                            "tx_periodoDis_a_month_id",
				                            "tx_periodoDis_a_year_id",
				                            "tx_periodoDis_a_hidden");
			}
		});
		
	});
</script>


<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="form_selezione" action="walletsollecitodiscarico.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA MONITORAGGIO PER DISCARICHI BORSELLINO</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label85 bold textright" validator="ignore;"
									cssclass="floatleft" disable="${codop == 'edit'}"
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
					
					<!-- 
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_idwallet"
							label="Codice Bors.:" maxlenght="18" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=^[0-9a-zA-Z\-]{1,18}$"
							message="[accept=Codice Bors.: ${msg_configurazione_descrizione_3}]"
							text="${tx_idwallet}" />
					</s:div>
 					-->
					
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_codiceborsellino"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							label="Codice Borsellino:" maxlenght="18"
							text="${tx_codiceborsellino}" />
					</s:div>		
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						
						
						<s:div name="divElement11a" cssclass="labelData">
							<s:label name="label_data_pag" cssclass="seda-ui-label label85 bold textright"
								text="Data Sollecito" />
						</s:div>
						<s:div name="divElement11b" cssclass="floatleft">
							<s:div name="divDataDa_car" cssclass="divDataDa">
								<s:date label="Da:" prefix="tx_periodo_da" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_periodo_da}"></s:date>
								<input type="hidden" id="tx_periodo_da_hidden" value="" />
							</s:div>
							<s:div name="divDataA_car" cssclass="divDataA">
								<s:date label="A:" prefix="tx_periodo_a" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_periodo_a}"></s:date>
								<input type="hidden" id="tx_periodo_a_hidden" value="" />
							</s:div>
						</s:div>						
					</s:div>									
					
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
					  <s:div name="divElement11a" cssclass="labelData">
							<s:label name="label_data_pag" cssclass="seda-ui-label label85 bold textright"
								text="Data Discarico Sollecito" />
						</s:div>
						<s:div name="divElement12b" cssclass="floatleft">
							<s:div name="divDataDaDis_car" cssclass="divDataDa">
								<s:date label="Da:" prefix="tx_periodoDis_da" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_periodoDis_da}"></s:date>
								<input type="hidden" id="tx_periodoDis_da_hidden" value="" />
							</s:div>
							<s:div name="divDataA_car" cssclass="divDataA">
								<s:date label="A:" prefix="tx_periodoDis_a" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_periodoDis_a}"></s:date>
								<input type="hidden" id="tx_periodoDis_a_hidden" value="" />
							</s:div>
						</s:div>						
					</s:div>					
				</s:div>

				 
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">					
					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_codicefiscalegenitore"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							label="Codice Fiscale:" maxlenght="16"
							text="${tx_codicefiscalegenitore}" />
					</s:div>

					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					
					 <s:dropdownlist name="tx_flagDiscaricoSollecito" disable="false"
							cssclass="tbddlMax floatleft" label="A carico Ente o Soris:"
							cssclasslabel="label85 bold textright"
							valueselected="${tx_flagDiscaricoSollecito}">
							<s:ddloption text="Tutti" value=" " />
							<s:ddloption text="A carico Ente" value="Y" />
							<s:ddloption text="A carico Soris" value="N" />
					 </s:dropdownlist>
					
											
					</s:div>

					<s:div name="divElement101" cssclass="divRicMetadatiRight">
					
					
						
					</s:div>
					
				</s:div>
				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_denominazione"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							label="Denominaz.:" maxlenght="61"
							text="${tx_denominazione}" />
					</s:div>
					
					<s:dropdownlist name="tx_flagDiscaricoAttivo" disable="false"
							cssclass="tbddlMax floatleft" label="Discaricato:"
							cssclasslabel="label85 bold textright"
							valueselected="${tx_flagDiscaricoAttivo}">
							<s:ddloption text="Tutti" value=" " />
							<s:ddloption text="Discaricato" value="D" />
							<s:ddloption text="Non Discaricato" value="N" />
					 </s:dropdownlist>
				</s:div>
				
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" text="Reset" validate="false" type="submit" cssclass="btnStyle" />
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

<c:if test="${!empty lista_wallet}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			RICERCA MONITORAGGIO PER DISCARICHI BORSELLINO
	</s:div>
	<s:div name="div_datagrid"> 
		<s:datagrid viewstate="walletsollecitodiscarico" cachedrowset="lista_wallet"
			action="walletsollecitodiscarico.do?vista=walletsollecitodiscarico" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			<s:dgcolumn label="Codice Borsellino" asc="BRS_KBRSKBRS_A" desc="BRS_KBRSKBRS_D">
			{1}
			</s:dgcolumn>   
			<s:dgcolumn label="Codice Fiscale" asc="BRS_CFISCGEN_A" desc="BRS_CFISCGEN_D">
			{2}
			</s:dgcolumn>
			<s:dgcolumn  label="Denominazione" asc="BRS_DBRSGENI_A" desc="BRS_DBRSGENI_D">
			{3}
			</s:dgcolumn>
			<s:dgcolumn index="4" label="Residuo Borsellino" asc="BRS_IBRSIMPO_A" desc="BRS_IBRSIMPO_D" css="textright" format="#0.00">
			</s:dgcolumn>
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;"> 
			  <s:if right="0" control="ne" left="{9}" >
			  <s:then>
			   <s:hyperlink
					href="walletsollecitodiscaricoedit.do?IdwalletInfo={1}&data_da=${data_pagamento_da}&data_a=${data_pagamento_a}&tx_presenze_action=presenze&codiceSocieta={6}&codiceUtente={7}&chiaveEnte={8}&codiceFiscale={2}&denominazione={3}&residuoBorsellino={4}"
					imagesrc="../applications/templates/shared/img/calendar.gif"
					alt="Dettaglio Presenze" text=""
					cssclass="blacklink hlStyle" />
			 </s:then>
			 </s:if> 
			 
			  <s:if right="0" control="ne" left="{5}" >
			  <s:then>
			    <s:hyperlink
					href="walletsollecitodiscaricoedit.do?IdwalletInfo={1}&data_da=${data_pagamento_da}&data_a=${data_pagamento_a}&dataDis_da=${data_pagamentoDis_da}&dataDis_a=${data_pagamentoDis_a}&tx_presenze_action=sollecito&codiceFiscale={2}&denominazione={3}&residuoBorsellino={4}"
					imagesrc="../applications/templates/shared/img/Info.png"
					alt="Dettaglio Solleciti" text=""
					cssclass="blacklink hlStyle" />
			  </s:then>
			  </s:if>
			  
			  
			
			
			<!-- 
				<s:hyperlink
					href="walletsollecitodiscaricoedit.do?IdwalletInfo={1}&data_da=${data_pagamento_da}&data_a=${data_pagamento_a}"
					imagesrc="../applications/templates/shared/img/Info.png"
					alt="Dettaglio" text=""
					cssclass="blacklink hlStyle" /> -->
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
	
	
	
	
	
	
	
	
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			RIEPILOGO STATISTICO
	</s:div>
	<s:div name="div_datagrid"> 
		<s:datagrid viewstate="" cachedrowset="lista_wallet_riepilogo"
			action="walletsollecitodiscarico.do?vista=walletsollecitodiscarico" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			<s:dgcolumn index="6" label="Importo Discarico Solleciti &euro;"  css="textright" format="#0.00">
			</s:dgcolumn>   
			<s:dgcolumn label="Numero solleciti"  css="textright">
			{7}
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>





