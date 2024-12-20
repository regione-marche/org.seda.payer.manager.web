<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="ioitaliaedit" encodeAttributes="true" />

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

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_selezione" action="ioitalianewmessage.do?vista=ioitalia"
			method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">INVIO MESSAGGIO
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			<c:if test="${ioitaliacheck == 'ioitalia'}">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:dropdownlist name="tx_societa" disable="${ddlSocietaDisabled}"
							cssclass="tbddl floatleft" label="Societ&agrave;:"
							cssclasslabel="label85 bold floatleft textright"
							cachedrowset="listaSocieta" usexml="true" showrequired="true"
							onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
							valueselected="${tx_societa}"
							validator="required;">
							<s:ddloption text="Tutte le Societ&agrave;" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>

					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
						<s:dropdownlist name="tx_provincia"
							disable="${ddlSocietaDisabled}" cssclass="tbddl floatleft"
							label="Provincia:"
							cssclasslabel="label85 bold floatleft textright" showrequired="true"
							cachedrowset="listaProvince" usexml="true" validator="required;"
							onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
							valueselected="${tx_provincia}">
							<s:ddloption text="Tutte le Province" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>

					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:dropdownlist name="tx_UtenteEnte"
							disable="${ddlSocietaDisabled}" cssclass="tbddlMax floatleft"
							label="Ente:" cssclasslabel="label85 bold floatleft textright"
							cachedrowset="listaUtentiEnti" usexml="true"
							valueselected="${tx_UtenteEnte}" showrequired="true"
							onchange="setFiredButton('tx_button_ente_changed');this.form.submit();"
							validator="required;">
							<s:ddloption text="Tutti gli Enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>

				</s:div>
				<br />
				
			<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
			
				<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
				
					<s:dropdownlist name="tx_tipServ" disable="${disabilita}"
						cssclass="tbddl floatleft" label="Tipologia Servizio:"
						cssclasslabel="label85 bold textright"
						cachedrowset="listaTipologieServizio" usexml="true"
						onchange="setFiredButton('tx_button_tipo_servizio_changed');this.form.submit();"
						valueselected="${tx_tipServ}" showrequired="true"
						validator="required;">
						<s:ddloption text="Tutti i servizi" value="" />
						<s:ddloption text="{2}" value="{1}" />
					</s:dropdownlist>
				</s:div>
			
				<br /><br />
			</s:div>
			
			<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
				<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist name="tx_impServ" disable="${disabilita}"
						cssclass="tbddl floatleft" label="Imposta Servizio:"
						cssclasslabel="label85 bold textright" validator="required;"
						cachedrowset="listaImpostaServizio" usexml="true" showrequired="true"
						valueselected="${tx_impServ}">
						<s:ddloption text="Imposta Servizio" value="" />
						<s:ddloption text="{3} - {4}" value="{3}" />
					</s:dropdownlist>
				</s:div>
				
				<br /><br />
			</s:div>
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_cf_s"
							label="Codice Fiscale:" maxlenght="16"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" showrequired="true"
							validator="required;accept=^[0-9a-zA-Z\-]{1,16}$"
							text="${tx_cf_s}" />
					</s:div>
					<br /><br />
				</s:div>
		</c:if>
		<c:if test="${ioitaliacheck == 'forniture' }">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:dropdownlist name="tx_societa" disable="${disabilita}"
							cssclass="tbddl floatleft" label="Societ&agrave;:" showrequired="true"
							cssclasslabel="label85 bold textright floatleft" validator="required;"
							onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
							valueselected="${tx_societa_desc}">
							<s:ddloption text="${tx_societa_desc}" value="${tx_societa}" />
						</s:dropdownlist>
					</s:div>

					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
						<s:dropdownlist name="tx_provincia"
							disable="${disabilita}" cssclass="tbddl floatleft"
							label="Provincia:" validator="required;" showrequired="true"
							cssclasslabel="label85 bold textright floatleft"
							onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
							valueselected="${tx_provincia_desc}"> 
							<s:ddloption text="${tx_provincia_desc}" value="${tx_provincia}" />
						</s:dropdownlist>
					</s:div>

					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:dropdownlist name="tx_UtenteEnte"
							disable="${disabilita}" cssclass="tbddlMax floatleft"
							label="Ente:" cssclasslabel="label85 bold textright floatleft"
							valueselected="${tx_UtenteEnte_desc}" validator="required;" showrequired="true"
							onchange="setFiredButton('tx_button_ente_changed');this.form.submit();">
							<s:ddloption text="${tx_UtenteEnte_desc}" value="${tx_UtenteEnte}" />
						</s:dropdownlist>
					</s:div>

				</s:div>
				<br />
			<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
				<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist name="tx_tipServ" disable="${disabilita}"
						cssclass="tbddl" label="Tipologia Servizio:" showrequired="true"
						cssclasslabel="label85 bold textright" validator="required;"
						cachedrowset="listaTipologieServizio" usexml="true"
						onchange="setFiredButton('tx_button_tipo_servizio_changed');this.form.submit();"
						valueselected="${tx_tipServ_desc}">
						<s:ddloption text="${tx_tipServ_desc}" value="${tx_tipServ}" />
					</s:dropdownlist>
				</s:div>
			</s:div>
			<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
				<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist name="tx_impServ" disable="${disabilita}"
						cssclass="tbddl" label="Imposta Servizio:" showrequired="true"
						cssclasslabel="label85 bold textright" validator="required;"
						cachedrowset="listaImpostaServizio" usexml="true"
						valueselected="${tx_impServ_desc}">
						<s:ddloption text="${tx_impServ_desc}" value="${tx_impServ}" />
					</s:dropdownlist>
				</s:div>
			</s:div>
			<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
				<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_cf_s"
							label="Codice Fiscale:" maxlenght="16"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" showrequired="true"
							validator="required;accept=^[0-9a-zA-Z\-]{1,16}$"
							text="${tx_cf_s}" />
					</s:div>
				</s:div>
		</c:if>

				<div style="clear: left">
					<s:div name="divRicercaTitleNameDue" cssclass="divRicTitle bold">Dati Messaggio</s:div>
				</div>
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
				<div style="padding: 10px;">
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:div name="divDataDa" cssclass="divDataDa">
							<s:date label="Scadenza Messaggio:" prefix="tx_data_da"
								yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
								locale="IT-it" descriptivemonth="false" separator="/"
								calendar="${tx_data_da}"
								cssclasslabel="label85 bold textright"
								validator="required;" showrequired="true"
								cssclass="dateman">
							</s:date>
							<input type="hidden" id="tx_data_da_hidden" value="" />
						</s:div>
					</s:div>
				</div>
				</s:div>
				<div style="clear: left;padding: 10px;">
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_oggetto_s"
							label="Oggetto Messaggio:"
							validator="required;minlength=10;maxlength=120;"
							cssclasslabel="label85 bold textright" showrequired="true"
							cssclass="textareaman tbddlMax780"
							text="${tx_oggetto_s}" />
					</s:div>
				</div>
				<div style="padding: 10px;">
					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_corpo_s"
							label="Corpo messaggio:"
							validator="required;minlength=80;maxlength=10000;"
							cssclasslabel="label85 bold textright" showrequired="true"
							cssclass="textareaman tbddlMax780"
							text="${tx_corpo_s}" />
					</s:div>
				</div>
				<div style="clear: left;">
					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
						<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_avviso_s"
								label="Avviso pagoPA:" maxlenght="18"
								validator="ignore;minlength=18;maxlength=18;"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								text="${tx_avviso_s}" />
						</s:div>
						<s:div name="divElement13" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" bdisable="true" name="tx_esito_s"
							label="Esito messaggio:"  
							cssclasslabel="label85 bold textright" 
							cssclass="textareaman"
							text="${tx_esito_s}" />
						</s:div>
					</s:div>
					<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
						<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_importo_s" label="Importo:" maxlenght="15"
								cssclasslabel="label85 bold textright" message="[accept=Importo: Inserire un importo valido e compreso tra 0,00 e 99999999999,99 (includere decimali).]"
								cssclass="textareaman" validator="ignore;accept=^\d{1,13}\.\d{2}$|^\d{1,13}\,\d{2}$"
								text="${tx_importo_s}" />
						</s:div>
						<s:div name="divElement14" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_email_s"
								label="E-Mail:" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman"
								text="${tx_email_s}" />
						</s:div>
					</s:div>
	
					<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					
							<s:dropdownlist name="tx_flag" 
								label="Pag. dopo scadenza:"  
								disable="${codop == 'edit'}" 
								cssclasslabel="label85 bold"
								 cssclass="textareaman"
								valueselected="${tx_flag}">
								<s:ddloption value="0" text="NO" />
								<s:ddloption value="1" text="SI" />
							</s:dropdownlist>
							<input type="hidden" id="tx_auxDigit_h" value="${tx_auxDigit_h}" />
						
					</s:div>
				</div>
			</s:div>
			<br />
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="false" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_rinotifica" onclick="" 
					text="Salva e invia" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_add" onclick="" 
					text="Salva" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_indietro" onclick="" validate="false"
					text="Indietro" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" validate="false"
					text="Reset" type="submit" cssclass="btnStyle" />
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

<!-- RIMOSSO DATAGRID -->