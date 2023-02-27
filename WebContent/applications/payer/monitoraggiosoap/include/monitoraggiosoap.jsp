<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<m:view_state id="monitoraggiosoap" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>
<script src="../applications/templates/monitoraggio/js/popup.js"
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


		
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#tx_data_notifica_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_notifica_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_data_notifica_da_day_id").val(dateText.substr(0,2));
												$("#tx_data_notifica_da_month_id").val(dateText.substr(3,2));
												$("#tx_data_notifica_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_da_notifica_day_id",
				                            "tx_data_notifica_da_month_id",
				                            "tx_data_notifica_da_year_id",
				                            "tx_data_notifica_da_hidden");
			}
		});
		$("#tx_data_notifica_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_notifica_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_data_notifica_a_day_id").val(dateText.substr(0,2));
												$("#tx_data_notifica_a_month_id").val(dateText.substr(3,2));
												$("#tx_data_notifica_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_notifica_a_day_id",
				                            "tx_data_notifica_a_month_id",
				                            "tx_data_notifica_a_year_id",
				                            "tx_data_notifica_a_hidden");
			}
		});
	});
</script>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="monitoraggioSoapForm" action="monitoraggiosoap.do" method="post" 
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
						
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
				Ricerca Notifiche RT
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						
						<s:dropdownlist name="tx_societa" disable="${ddlSocietaDisabled}"
							cssclass="tbddl200 floatleft" label="Societ&agrave;:"
							cssclasslabel="label95 bold floatleft textright"
							cachedrowset="listaSocieta" usexml="true"
							onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
							valueselected="${tx_societa}">
							<s:ddloption text="Tutte le Societ&agrave;" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_societa_changed" 
								disable="${ddlSocietaDisabled}" onclick="" text="" 
								type="submit" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
						</noscript>

					</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
						<s:dropdownlist name="tx_provincia"
							disable="${ddlProvinciaDisabled}" cssclass="tbddl200 floatleft"
							label="Provincia:"
							cssclasslabel="label95 bold floatleft textright"
							cachedrowset="listaProvince" usexml="true"
							onchange="setFiredButton('tx_button_provincia_changed');this.form.submit();"
							valueselected="${tx_provincia}">
							<s:ddloption text="Tutte le Province" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_provincia_changed"
								disable="${ddlProvinciaDisabled}" onclick="" text=""
								type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false"
								 />
						</noscript>

					</s:div>
					
					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:dropdownlist name="tx_UtenteEnte"
							disable="${ddlUtenteEnteDisabled}" cssclass="tbddl200 floatleft"
							label="Ente:" cssclasslabel="label95 bold textright"
							cachedrowset="listaUtentiEnti" usexml="true"
							valueselected="${tx_UtenteEnte}">
							<s:ddloption text="Tutti gli Enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
					
				</s:div>
				
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;accept=^(?:(?!').)*$;maxlength=64" bmodify="true"
							maxlenght="64" 
							cssclass="textareamanSmall"
							cssclasslabel="label95 bold textright"
							name="txtIdTransazione" label="Id Transazione:"
							message="[accept=Id Transazione: Inserire un Id Transazione valido]"
							text="${txtIdTransazione}" />
					</s:div>
				
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="maxlength=18" bmodify="true"
							maxlenght="18" 
							cssclass="textareamanSmall"
							cssclasslabel="label95 bold textright"
							name="txtCodAvvisoPagoPA" label="Cod.avv.PagoPA:"
							text="${txtCodAvvisoPagoPA}" />
					</s:div>
					
				
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="maxlength=20" bmodify="true"
							maxlenght="20" 
							cssclass="textareamanSmall"
							cssclasslabel="label95 bold textright"
							name="txtNumeroDocumento" label="N.Documento:"
							text="${txtNumeroDocumento}" />
					</s:div>

				</s:div>
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					
					
					
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="maxlength=16" bmodify="true"
							maxlenght="16" 
							cssclass="textareamanSmall"
							cssclasslabel="label95 bold textright"
							name="txtCodiceFiscale" label="C.F:"
							text="${txtCodiceFiscale}" />
					</s:div>
					
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="ddlEsitoNotifica" disable="false"
							cssclass="tbddl200 floatleft"
							cssclasslabel="label95 bold floatleft textright"
							label="Esito Notifica:"
							valueselected="${ddlEsitoNotifica}">
							<s:ddloption value="ALL" text="Tutti" />
							<s:ddloption value="OK" text="Esito Positivo" />
							<s:ddloption value="KO" text="Esito Negativo" />
						</s:dropdownlist>
					</s:div>
				
				</s:div>
				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
								
					<s:div name="divElement18" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement18a" cssclass="labelData95">
							<s:label name="label_data" cssclass="seda-ui-label label95 bold textright" text="Data pagamento:" />
						</s:div>
						
						<s:div name="divElement18b" cssclass="floatleft">
							<s:div name="divDataPagDa" cssclass="divDataDa">
								<s:date label="Da:" prefix="tx_data_da" yearbegin="${ddlDateAnnoDa}"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_data_da}"
									cssclasslabel="labelsmall"
									cssclass="dateman">
								</s:date>
								<input type="hidden" id="tx_data_da_hidden" value="" />
							</s:div>
							
							<s:div name="divDataPagA" cssclass="divDataA">
								<s:date label="A:" prefix="tx_data_a" yearbegin="${ddlDateAnnoDa}"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_data_a}"
									cssclasslabel="labelsmall"
									cssclass="dateman" >
								</s:date>
								<input type="hidden" id="tx_data_a_hidden" value="" />
							</s:div>
						</s:div>
							
					</s:div>
					
					<s:div name="divElement18" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement18a" cssclass="labelData95">
							<s:label name="label_data" cssclass="seda-ui-label label95 bold textright" text="Data notifica pagamento:" />
						</s:div>
						
						<s:div name="divElement18b" cssclass="floatleft">
							<s:div name="divDataNotDa" cssclass="divDataDa">
								<s:date label="Da:" prefix="tx_data_notifica_da" yearbegin="${ddlDateAnnoDa}"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_data_notifica_da}"
									cssclasslabel="labelsmall"
									cssclass="dateman">
								</s:date>
								<input type="hidden" id="tx_data_notifica_da_hidden" value="" />
							</s:div>
							
							<s:div name="divDataNotA" cssclass="divDataA">
								<s:date label="A:" prefix="tx_data_notifica_a" yearbegin="${ddlDateAnnoDa}"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_data_notifica_a}"
									cssclasslabel="labelsmall"
									cssclass="dateman" >
								</s:date>
								<input type="hidden" id="tx_data_notifica_a_hidden" value="" />
							</s:div>
						</s:div>
							
					</s:div>
					

				</s:div>
				
			</s:div>
			
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				
				<s:button id="tx_button_cerca" type="submit" text="Cerca" cssclass="btnStyle" onclick="" />
				<s:button id="tx_button_reset" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
										  
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
			</s:div>
			
		</s:form>	
	</s:div>
	
</s:div>


<c:if test="${!empty lista_notifiche_soap}">
	<fmt:setLocale value="it_IT" />
	
	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Elenco Notifiche
	</s:div>
	
	<s:div name="divRisultati" cssclass="divRisultati">
		<s:datagrid cachedrowset="lista_notifiche_soap"
			action="monitoraggiosoap.do?vista=monitoraggiosoap" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}" viewstate="">
			
			<s:dgcolumn index="7" label="Ente" asc="ENTA" desc="ENTD" />
			<s:dgcolumn label="Id Transazione" asc="TRAA" desc="TRAD" >
				<s:hyperlink
				href="../monitoraggio/monitoraggioTransazioni.do?tx_codice_transazione={1}&tx_button_cerca=1"
				text="{1}" cssclass="blacklink"
				alt="Dettaglio Transazione {1}" />
			</s:dgcolumn>
			
			<s:dgcolumn index="12" label="Data Notifica" asc="DNOTA" desc="DNOTD" format="dd/MM/yyyy HH:mm:ss"/>
			<s:dgcolumn index="10" label="Cod.avv.PagoPA" asc="" desc="" />
			<s:dgcolumn index="9" label="Numero Documento" asc="DOCA" desc="DOCD" />
			<s:dgcolumn index="15" label="Importo" asc="" desc=""  format="#0.00" css="textright"/>
			<s:dgcolumn index="16" label="Data pagamento" asc="DPAGA" desc="DPAGD" format="dd/MM/yyyy HH:mm:ss"/>		
						
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;">
				<s:if right="{14}" control="eq" left="00">
					<s:then>
						<s:image
							src="../applications/templates/shared/img/circle_green.png"
							alt="Esito notifica OK" height="16" width="16"
							cssclass="hlStyle floatleft" />
					</s:then>
				</s:if>
				<s:if right="{14}" control="ne" left="00">
					<s:then>
						<s:image
							src="../applications/templates/shared/img/circle_red.png"
							alt="Esito notifica KO" height="16" width="16"
							cssclass="hlStyle floatleft" />
					</s:then>
				</s:if>
				<s:hyperlink
					href="notifica.do?notificaChiaveTransazione={1}&notificaChiaveDettaglioTransazione={19}&action=ricerca&form=monitoraggioSoapForm&vista=monitoraggiosoap"
					cssclass="hlStyle" 
					imagesrc="../applications/templates/shared/img/notifica.png" text="" 
					alt="Rinotifica" />
				<s:hyperlink
					href="dettaglioNotifica.do?chiaveTransazione_hidden={1}&chiaveDettaglioTransazione_hidden={19}"
					cssclass="hlStyle" 
					imagesrc="../applications/templates/shared/img/details.png" text="" 
					alt="Dettaglio" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>

		


