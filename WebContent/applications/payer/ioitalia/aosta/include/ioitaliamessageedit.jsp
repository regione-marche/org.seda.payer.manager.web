<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="ioitalia" encodeAttributes="true" />

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
		$("#tx_data_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_data_scadenza_aggiornata_day_id").val(dateText.substr(0,2));
												$("#tx_data_scadenza_aggiornata_month_id").val(dateText.substr(3,2));
												$("#tx_data_scadenza_aggiornata_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_scadenza_aggiornata_day_id",
				                            "tx_data_scadenza_aggiornata_month_id",
				                            "tx_data_scadenza_aggiornata_year_id",
				                            "tx_data_scadenza_aggiornata_hidden");
			}
		});
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
</script>

<div style="display: none">
	<c:choose>
		<c:when test="${tx_esito == '0'}">
			${tx_check = false}
		</c:when>
		<c:otherwise>
			${tx_check = true}
		</c:otherwise>
	</c:choose>
</div>
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_selezione" action="ioitaliamessageedit.do?vista=ioitalia"
			method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${tx_esito == '2'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RITENTA INVIO MESSAGGIO
				</s:div>
			</c:if>
			<c:if test="${tx_esito == '1'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">MESSAGGIO INVIATO CON SUCCESSO</s:div>
			</c:if>
			<c:if test="${tx_esito == '0'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">IL MESSAGGIO NON E' ANCORA STATO INVIATO</s:div>
			</c:if>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" bdisable="${disabilita}" name="tx_societa" label="Società:"
							cssclasslabel="label85 bold textright floatleft"
							cssclass="textareaman" validator="required;" showrequired="true"
							text="${tx_societa_desc}" />
					</s:div>
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" bdisable="${disabilita}" name="tx_tipServ" label="Tip. Serv.:"
							cssclasslabel="label85 bold textright floatleft"
							cssclass="textareaman" validator="required;" showrequired="true"
							text="${tx_tipServ_desc}" />
					</s:div>
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="false" name="tx_fornitura" label="Fornitura:"
						 bdisable="${disabilita}" cssclasslabel="label85 bold textright floatleft"
						cssclass="textareaman" validator="required;" showrequired="true"
						text="${tx_fornitura}" />
					</s:div>
					<br /><br />
				</s:div>
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" bdisable="${disabilita}" name="tx_provincia" label="Provincia:"
							cssclasslabel="label85 bold textright floatleft"
							cssclass="textareaman" validator="required;" showrequired="true"
							text="${tx_provincia_desc}" />
					</s:div>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" bdisable="${disabilita}" name="tx_impServ" label="Im. Serv.:"
							cssclasslabel="label85 bold textright floatleft"
							cssclass="textareaman" validator="required;" showrequired="true"
							text="${tx_impServ_desc}" />
					</s:div>
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="${!tx_check}" bdisable="${tx_check}" name="tx_cf_s" label="Codice Fiscale:"
							cssclasslabel="label85 bold textright floatleft"
							cssclass="textareaman" showrequired="true"
							text="${tx_cf_s}" 
							validator="required;maxlength=16"/>
					</s:div>
					<br /><br />
				</s:div>
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" bdisable="${disabilita}" name="tx_codice_ente" label="Ente:"
							cssclasslabel="label85 bold textright floatleft"
							cssclass="textareaman" validator="required;" showrequired="true"
							text="${tx_codice_ente_desc}" />
					</s:div>
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" bdisable="${disabilita}" name="tx_data" label="Data:"
							cssclasslabel="label85 bold textright floatleft"
							cssclass="textareaman" validator="required;" showrequired="true"
							text="${tx_data}" />
					</s:div>
				</s:div>
				
				<br />
				<br />
				
				<div style="clear: left">
					<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Dati Messaggio</s:div>
				</div>
				<br />
					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="${disabilita}" name="tx_data_scadenza"
							label="Scadenza attuale:" bdisable="true"
							cssclasslabel="label160 bold textright floatleft"
							cssclass="textareaman tbddlMax780" 
							text="${tx_data_scadenza}" />
							<br />
							</s:div>
							<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<c:if test="${!tx_check}">
								<s:date label="Aggiorna scadenza:" prefix="tx_data_scadenza_aggiornata"
								yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
								locale="IT-it" descriptivemonth="false" separator="/"
								calendar="${tx_data_scadenza_aggiornata}"
								cssclasslabel="label160 bold textright floatleft"
								cssclass="dateman">
								</s:date>
								<input type="hidden" id="tx_data_da_hidden" value="" />
							</c:if>
					</s:div>
				
			<br />
					<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="${!tx_check}" name="tx_oggetto"
							label="Oggetto messaggio:" bdisable="${tx_check}"
							cssclasslabel="label160 bold textright floatleft"
							cssclass="textareaman tbddlMax780" showrequired="true"
							text="${tx_oggetto}"  validator="required;minlength=10;maxlength=120"/>
					</s:div>
			
			<br />
					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="${!tx_check}" bdisable="${tx_check}" name="tx_corpo"
							label="Corpo messaggio:"
							cssclasslabel="label160 bold textright floatleft"
							cssclass="textareaman tbddlMax780" showrequired="true"
							text="${tx_corpo}" validator="required;minlength=80;maxlength=10000"/>
					</s:div>
			<br />
			<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
				<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="${!tx_check}" name="tx_email"
						label="E-Mail:" bdisable="${tx_check}"
						cssclasslabel="label85 bold textright floatleft"
						cssclass="textareaman" 
						text="${tx_email}" />
				</s:div>
				<s:div name="divElement14" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist name="tx_esito" disable="true"
							cssclass="tbddl floatleft" label="Esito:"
							cssclasslabel="label85 bold textright floatleft"
							onchange="" 
							valueselected="${tx_esito}">
							<s:ddloption text="" value="" />
							<s:ddloption text="OK" value="1" />
							<s:ddloption text="KO" value="2" />
							<s:ddloption text="KO" value="0" />
							</s:dropdownlist>
							
					</s:div>
			</s:div>
			<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
				<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="${!tx_check}" bdisable="${tx_check}" name="tx_avviso"
						label="Avviso PagoPA:" maxlenght="18"
						cssclasslabel="label85 bold textright floatleft"
						cssclass="textareaman"
						text="${tx_avviso}" 
						validator="ignore;minlength=18;maxlength=18" />
				</s:div>
				<s:div name="divElement13" cssclass="divRicMetadatiSingleRow">
					
				</s:div>
			</s:div>
			<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
				<s:div name="divElement12" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="${!tx_check}" bdisable="${tx_check}" name="tx_importo" label="Importo:"
						cssclasslabel="label85 bold textright floatleft" message="[accept=Importo: Inserire un importo valido e compreso tra 0,00 e 99999999999,99 (Includere decimali).]"
						cssclass="textareaman" validator="ignore;accept=^\d{1,13}\.\d{2}$|^\d{1,13}\,\d{2}$" maxlenght="15"
						text="${tx_importo}" />
				</s:div>
				<s:div name="divElement15" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist name="tx_flag" disable="${tx_check}"
							cssclass="tbddl floatleft" label="Pag. dopo scadenza:"
							cssclasslabel="label85 bold floatleft textright"
							onchange=""
							valueselected="${tx_flag}">
							<s:ddloption text="NO" value="0" />
							<s:ddloption text="SI" value="1" />
					</s:dropdownlist>
				</s:div>
			</s:div>
			
				
			
			<br />
			<br />
			<br />
					
			
			</s:div>
<br />
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="false" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
					<c:if test="${tx_esito == '2' }">
						<s:button id="tx_button_rinotifica" onclick="" type="submit"
							cssclass="btnStyle" text="Ritenta Invio" />
					</c:if>
					<c:if test="${tx_check == false }">
						<s:button id="tx_button_add" onclick="" type="submit"
							cssclass="btnStyle" text="Salva e invia" />
						<s:button id="tx_button_aggiungi" onclick="" type="submit"
							cssclass="btnStyle" text="Salva" />
					</c:if>
				<s:button id="tx_button_indietro" onclick="" validate="false"
					text="Indietro" type="submit" cssclass="btnStyle" />
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

<c:if test="${!empty lista_blackbox}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO CONFIGURAZIONI APP IO
	</s:div>

	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_blackbox"
			action="ioitaliamessageedit.do?vista=ioitalia" border="1"
			usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			<s:dgcolumn index="1" label="Società"></s:dgcolumn>
			<s:dgcolumn index="2" label="Utente"></s:dgcolumn>
			<s:dgcolumn index="3" label="Ente"></s:dgcolumn>
			<s:dgcolumn index="4" label="Tipologia Servizio"></s:dgcolumn>
			<s:dgcolumn label="Azioni">
				<s:hyperlink
					href="blackboxEdit.do?vista=blackbox&codop=edit&tx_enteda5={1}&tx_idDominio={2}&ddlSocietaUtenteEnte=${requestScope.tx_societa}|${requestScope.tx_utente}|${requestScope.tx_ente}&tx_applicationCode={3}&tx_auxDigit={5}&tx_codiceSegregazione={4}&tx_codiceServizio={15}"
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Modifica" text="" cssclass="blacklink hlStyle" />

				<s:hyperlink
					href="blackboxEdit.do?vista=blackbox&tx_button_cancel=&tx_enteda5={1}&tx_applicationCode={3}&tx_auxDigit={5}&tx_codiceSegregazione={4}&tx_codiceServizio={15}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text="" cssclass="blacklink hlStyle" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>