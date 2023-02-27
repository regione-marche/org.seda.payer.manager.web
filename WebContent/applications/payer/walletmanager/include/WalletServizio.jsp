<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="walletservizio" encodeAttributes="true" />

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


<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="form_selezione" action="walletservizio.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA MONITORAGGIO PER SERVIZIO</s:div>
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
							<s:textbox bmodify="true" name="tx_codicefiscalegenitore"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							label="Codice Fiscale:" maxlenght="16"
							text="${tx_codicefiscalegenitore}" />
					</s:div>					
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
								text="Periodo Carico" />
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
				</s:div>

				 
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">					
					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_tipologia_servizio" disable="" label="Per Servizio:"
						 	multiple="false" valueselected="${tx_tipologia_servizio}" 
						 	cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
						 	<s:ddloption text="Tutti" value="" />
							<s:ddloptionbinder options="${serviziDDLList}"/>
						</s:dropdownlist>
					</s:div>

					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_codice_tributo" disable="" label="Tributo:"
							 multiple="false" valueselected="${tx_codice_tributo}" 
							 cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
   							<s:ddloption text="Tutti" value="" />
   							<s:ddloptionbinder options="${tributiDDLList}"/>
						</s:dropdownlist>						
					</s:div>

					<s:div name="divElement101" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_flagrendicontato" disable="false"
							cssclass="tbddlMax floatleft" label="Rendicontato:"
							cssclasslabel="label85 bold textright"
							valueselected="${tx_flagrendicontato}">
							<s:ddloption text="Tutti" value="" />
							<s:ddloption text="Si" value="Y" />
							<s:ddloption text="No" value="N" />
						</s:dropdownlist>
					</s:div>
					
				</s:div>
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" text="Reset" validate="false" type="submit" cssclass="btnStyle" />
				<c:if test="${!empty lista_wallet}">
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

<c:if test="${!empty lista_wallet}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			RICERCA MONITORAGGIO PER SERVIZIO
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_wallet"
			action="walletservizio.do?vista=walletservizio" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			<s:dgcolumn index="1" label="Servizio" ></s:dgcolumn>
			<s:dgcolumn index="2" label="Codice Borsellino" asc="LST_KBRS_A" desc="LST_KBRS_D"></s:dgcolumn>
			
			<s:dgcolumn index="3" label="Codice Fiscale" asc="LST_CF_A" desc="LST_CF_D"></s:dgcolumn>
			<s:dgcolumn index="4" label="Anno/Mese Carico" ></s:dgcolumn>
			<s:dgcolumn index="5" label="Codice Tributo" ></s:dgcolumn>
			<s:dgcolumn index="6" label="Carico" css="textright" format="#0.00"></s:dgcolumn>
			<s:dgcolumn index="7" label="Pagato" css="textright" format="#0.00"></s:dgcolumn>
			<s:dgcolumn index="8" label="Residuo" css="textright" format="#0.00"></s:dgcolumn>
			<s:dgcolumn index="9" label="Rendicontanto" css="textright" format="#0.00"></s:dgcolumn>
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;">
				<s:hyperlink
					href="walletservizioedit.do?IdwalletInfo={2}&codServ={10}&tributo={5}&amcar={4}&descServ={1}"
					imagesrc="../applications/templates/shared/img/Info.png"
					alt="Dettaglio Servizio" text=""
					cssclass="blacklink hlStyle" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
	<s:div name="div_riepilogo" cssclass="div_align_center divRiepilogo">
		<s:table border="0" cellspacing="0" cellpadding="0"
			cssclass="seda-ui-table">
			<s:thead>
				<s:tr>
					<s:th>RIEPILOGO STATISTICO</s:th>
				</s:tr>
			</s:thead>
			<s:tbody>
				<s:tr>
					<s:td>
						<s:datagrid viewstate="" cachedrowset="lista_walletservizio_riepilogo"
							action="walletservizio.do" border="1" usexml="true">
							<s:dgcolumn label="Servizio" css="text_align_right" index="1" ></s:dgcolumn>
							<s:dgcolumn label="Codice Tributo" css="text_align_right" index="2" format="#0.00"></s:dgcolumn>
							<s:dgcolumn label="Carico" css="text_align_right" index="3" format="#0.00"></s:dgcolumn>
							<s:dgcolumn label="Pagato" css="text_align_right" index="4" format="#0.00"></s:dgcolumn>
							<s:dgcolumn label="Residuo" css="text_align_right" index="5" format="#0.00"></s:dgcolumn>
							<s:dgcolumn label="Rendicontato" css="text_align_right" index="6" format="#0.00"></s:dgcolumn>
						</s:datagrid>
					</s:td>
				</s:tr>
			</s:tbody>
		</s:table>
	</s:div>
</c:if>





