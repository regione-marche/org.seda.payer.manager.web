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

<style>
.txtArea{
	float:left;
	margin-bottom:20px;	
	max-width: 800px;
}
</style>

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
							label="Provincia:" validator="required;" showrequired="true"
							cssclasslabel="label85 bold floatleft textright"
							cachedrowset="listaProvince" usexml="true"
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
							valueselected="${tx_UtenteEnte}"
							validator="required;" showrequired="true"
							onchange="setFiredButton('tx_button_ente_changed');this.form.submit();">
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
						cssclasslabel="label85 bold textright" showrequired="true"
						cachedrowset="listaTipologieServizio" usexml="true"
						onchange="setFiredButton('tx_button_tipo_servizio_changed');this.form.submit();"
						valueselected="${tx_tipServ}"
						validator="required;">
						<s:ddloption text="Tutti i servizi" value="" />
						<s:ddloption text="{2}" value="{1}" />
					</s:dropdownlist>
				</s:div>
			
				<br /><br />
			</s:div>
			
			<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
				<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" name="tx_cf_s"
							label="Codice Fiscale:" maxlenght="16"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" showrequired="true"
							validator="required;accept=^[0-9a-zA-Z\-]{1,16}$"
							text="${tx_cf_s}" />
				</s:div>
				
				<br /><br />
			</s:div>
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						
					</s:div>
					<br /><br />
				</s:div>
		</c:if>
		<c:if test="${ioitaliacheck == 'forniture' }">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:textbox cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="required;"
							bmodify="false" bdisable="true" showrequired="true"
							name="tx_societa_desc" label="Societ&agrave;:" text="${tx_societa_desc}"/>
							
					</s:div>

					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
						<s:textbox cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="required;"
							bmodify="false" bdisable="true" showrequired="true"
							name="tx_provincia_desc" label="Provincia:" text="${tx_provincia_desc}"/>
					</s:div>

					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:textbox cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="required;"
							bmodify="false" bdisable="true" showrequired="true"
							name="tx_UtenteEnte_desc" label="Ente:" text="${tx_UtenteEnte_desc}"/>
					</s:div>

				</s:div>
				<br />
			<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
				<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					<s:textbox cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="required;"
							bmodify="false" bdisable="true" showrequired="true"
							name="tx_tipServ_desc" label="Tipologia Servizio:" text="${tx_tipServ_desc}"/>
					</s:div>
			</s:div>
			<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
				<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" name="tx_cf_s"
							label="Codice Fiscale:" maxlenght="16"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" showrequired="true"
							validator="required;accept=^[0-9a-zA-Z\-]{1,16}$"
							text="${tx_cf_s}" />
				</s:div>
			</s:div>
			<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
				<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						
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
								validator="required;dateISO" showrequired="true"
								message="[dateISO=Scadenza Messaggio: ${msg_dataISO_valida}]"
								cssclasslabel="label85 bold textright"
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
							label="Oggetto Messaggio:" showrequired="true"
							validator="required;minlength=10;maxlength=120;"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman tbddlMax780"
							text="${tx_oggetto_s}" />
					</s:div>
				</div>
				<div style="padding: 10px;">
<%-- 					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow"> --%>
<%-- 						<s:textbox bmodify="true" name="tx_corpo_s" --%>
<%-- 							label="Corpo messaggio:" showrequired="true" --%>
<%-- 							validator="required;minlength=80;maxlength=10000;" --%>
<%-- 							cssclasslabel="label85 bold textright" --%>
<%-- 							cssclass="textareaman tbddlMax780" --%>
<%-- 							text="${tx_corpo_s}" /> --%>
<%-- 					</s:div> --%>
				<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
	                <s:textarea name="tx_corpo_s" label="Corpo messaggio:" text="${tx_corpo_s}"
	                bmodify="true" row="6" col="104" 
	                cssclasslabel="label85 bold textright" 
	                cssclass = "txtArea"
	                />
	            
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
							<s:textbox bmodify="true" name="tx_importo_s" label="Importo:"
								cssclasslabel="label85 bold textright" maxlenght="15"
								cssclass="textareaman" message="[accept=Importo: Inserire un importo valido e compreso tra 0,00 e 99999999999,99 (Includere decimali).]"
								validator="ignore;accept=^\d{1,13}\.\d{2}$|^\d{1,13}\,\d{2}$"
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