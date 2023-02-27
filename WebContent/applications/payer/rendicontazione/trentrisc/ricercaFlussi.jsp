<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="FlussiSearch" encodeAttributes="true" />

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
		$("#tx_data_pag_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_pag_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_data_pag_da_day_id").val(dateText.substr(0,2));
												$("#tx_data_pag_da_month_id").val(dateText.substr(3,2));
												$("#tx_data_pag_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_pag_da_day_id",
				                            "tx_data_pag_da_month_id",
				                            "tx_data_pag_da_year_id",
				                            "tx_data_pag_da_hidden");
			}
		});
		$("#tx_data_pag_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_pag_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_data_pag_a_day_id").val(dateText.substr(0,2));
												$("#tx_data_pag_a_month_id").val(dateText.substr(3,2));
												$("#tx_data_pag_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_pag_a_day_id",
				                            "tx_data_pag_a_month_id",
				                            "tx_data_pag_a_year_id",
				                            "tx_data_pag_a_hidden");
			}
		});
		$("#tx_data_cre_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_cre_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_data_cre_da_day_id").val(dateText.substr(0,2));
												$("#tx_data_cre_da_month_id").val(dateText.substr(3,2));
												$("#tx_data_cre_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_cre_da_day_id",
				                            "tx_data_cre_da_month_id",
				                            "tx_data_cre_da_year_id",
				                            "tx_data_cre_da_hidden");
			}
		});
		$("#tx_data_cre_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_cre_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_data_cre_a_day_id").val(dateText.substr(0,2));
												$("#tx_data_cre_a_month_id").val(dateText.substr(3,2));
												$("#tx_data_cre_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_cre_a_day_id",
				                            "tx_data_cre_a_month_id",
				                            "tx_data_cre_a_year_id",
				                            "tx_data_cre_a_hidden");
			}
		});
	});
</script>


<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="form_selezione" action="ricercaFlussi.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Flussi Pagamento
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:dropdownlist name="tx_societa" disable="${ddlSocietaDisabled}"
							cssclass="tbddl floatleft" label="Societ&agrave;:"
							cssclasslabel="label85 bold floatleft textright"
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
							disable="${ddlProvinciaDisabled}" cssclass="tbddl floatleft"
							label="Provincia:"
							cssclasslabel="label65 bold floatleft textright"
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
							disable="${ddlUtenteEnteDisabled}" cssclass="tbddlMax floatleft"
							label="Ente:" cssclasslabel="label65 bold textright"
							cachedrowset="listaUtentiEnti" usexml="true"
							valueselected="${tx_UtenteEnte}"
							onchange="setFiredButton('tx_button_ente_changed');this.form.submit();">
							<s:ddloption text="Tutti gli Enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_ente_changed" 
								disable="${ddlUtenteEnteDisabled}" onclick="" text="" 
								type="submit" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
						</noscript>
					</s:div>
				</s:div>
				
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_codice_transazione"
							label="Id Transazione:" maxlenght="36" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=^[0-9a-zA-Z\-]{1,64}$"
							message="[accept=Id Transazione: ${msg_configurazione_descrizione_3}]"
							text="${tx_codice_transazione}" />
					</s:div>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_tipo_flusso" disable="false"
							cssclass="tbddl"
							cssclasslabel="label85 bold textright"
							label="Tipo Flusso:" valueselected="${tx_tipo_flusso}"
							cachedrowset="listaBollettini" usexml="true">
							<s:ddloption text="Tutti i Tipi" value="" />
							<s:ddloption text="{1}-{2}" value="{5}" />
						</s:dropdownlist>
					</s:div>
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_canale_pagamento" disable="false"
							cssclass="tbddl"
							cssclasslabel="label85 bold textright"
							label="Canale:"
							valueselected="${tx_canale_pagamento}"
							cachedrowset="listaCanaliPagamento" usexml="true">
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_tipologia_servizio" disable="false"
							cssclass="tbddlMax floatleft" label="Tip. Servizio:"
							cssclasslabel="label85 bold textright"
							cachedrowset="listaTipologieServizio" usexml="true"
							valueselected="${tx_tipologia_servizio}">
							<s:ddloption text="Tutte le tipologie" value="" />
							<s:ddloption text="{2}" value="{1}_{3}" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_chiave_rendicontazione"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							label="Chiave Flusso:" maxlenght="30"
							text="${tx_chiave_rendicontazione}" />
					</s:div>
					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_strumento" disable="false"
							cssclass="tbddlMax floatleft" label="Strumento:"
							cssclasslabel="label85 bold textright"
							cachedrowset="listaStrumenti" usexml="true"
							valueselected="${tx_strumento}">
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
					<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_codice_gateway" disable="false"
							cssclass="tbddlMax floatleft" label="Tipo Carta:"
							cssclasslabel="label85 bold textright"
							cachedrowset="listaGateway" usexml="true"
							valueselected="${tx_codice_gateway}">
							<s:ddloption text="Tutte le carte" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
					<s:div name="divElement12" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_codice_psp" disable="false"
							cssclass="tbddlMax floatleft" label="PSP:"
							cssclasslabel="label85 bold textright"
							cachedrowset="listaPSP" usexml="true"
							valueselected="${tx_codice_psp}">
							<s:ddloption text="Tutti i PSP" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
					<s:div name="divElement101" cssclass="divRicMetadatiSingleRow">




					</s:div>
				</s:div>
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					
					<s:div name="divElement11" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement11a" cssclass="labelData">
							<s:label name="label_data_pag" cssclass="seda-ui-label label85 bold textright"
								text="Data Pagamento" />
						</s:div>
						<s:div name="divElement11b" cssclass="floatleft">
							<s:div name="divDataDa_pag" cssclass="divDataDa">
								<s:date label="Da:" prefix="tx_data_pag_da" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_data_pag_da}"></s:date>
								<input type="hidden" id="tx_data_pag_da_hidden" value="" />
							</s:div>
							<s:div name="divDataA_pag" cssclass="divDataA">
								<s:date label="A:" prefix="tx_data_pag_a" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_data_pag_a}"></s:date>
								<input type="hidden" id="tx_data_pag_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
					<s:div name="divElement13" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement13a" cssclass="labelData">
							<s:label name="label_data_cre" cssclass="seda-ui-label label85 bold textright"
								text="Data Creazione Flusso" />
						</s:div>
						<s:div name="divElement13b" cssclass="floatleft">
							<s:div name="divDataDa_cre" cssclass="divDataDa">
								<s:date label="Da:" prefix="tx_data_cre_da" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_data_cre_da}"></s:date>
								<input type="hidden" id="tx_data_cre_da_hidden" value="" />
							</s:div>
						
							<s:div name="divDataA_cre" cssclass="divDataA">
								<s:date label="A:" prefix="tx_data_cre_a" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_data_cre_a}"></s:date>
								<input type="hidden" id="tx_data_cre_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
				</s:div>
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" text="Reset" validate="false" type="submit" cssclass="btnStyle" />
				<c:if test="${!empty lista_flussi}">
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

<c:if test="${!empty lista_flussi}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Flussi Pagamento
	</s:div>

	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_flussi"
			action="ricercaFlussi.do?vista=FlussiSearch" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

			<s:dgcolumn index="1" label="Tipo Flusso" asc="TFLU_A"
				desc="TFLU_D"></s:dgcolumn>
			<s:dgcolumn index="2" label="Societ&agrave;" asc="SOC_A" desc="SOC_D"></s:dgcolumn>
			<s:dgcolumn label="Utente/Ente" asc="UTE_ENT_A" desc="UTE_ENT_D">
				{3}<br />{4}&nbsp;{5}
			</s:dgcolumn>
			<s:dgcolumn index="6" label="Data&nbsp;Creazione Flusso"
				asc="CREA_A" desc="CREA_D" format="dd/MM/yyyy"></s:dgcolumn>
			<s:dgcolumn index="7" label="Tipo&nbsp;Carta" asc="GTW_A"
				desc="GTW_D"></s:dgcolumn>
			<s:dgcolumn label="Chiave&nbsp;Flusso" asc="KREN_A" desc="KREN_D">
			    <c:if test="${appMonitoraggioUteEnabled}">
					<s:hyperlink href="../monitoraggio/monitoraggioTransazioni.do?tx_chiave_rendicontazione={8}&tx_button_cerca=cerca" 
					 cssclass="blacklink" text="{8}" alt="Dettaglio flusso {8}" />
				</c:if>
			    <c:if test="${!appMonitoraggioUteEnabled}">
			    {8}
				</c:if>
			</s:dgcolumn>
			<s:dgcolumn index="16" label="PSP" asc="PSP_A"
				desc="PSP_D"></s:dgcolumn>
			<s:dgcolumn index="12" label="Numero Pagamenti" asc="NPAG_A"
				desc="NPAG_D" css="text_align_right"></s:dgcolumn>
			<s:dgcolumn index="13" label="Totale Importo" asc="TIMP_A"
				desc="TIMP_D" css="text_align_right" format="#,##0.00"></s:dgcolumn>
			<s:ifdatagrid left="${areaInvioRendicontazioneEnabled}" control="eq"
				right="true">
				<s:thendatagrid>
					<s:dgcolumn label="&nbsp;Invio Flusso&nbsp;">
						<!--<c:out value="{10}"/>---<c:out value="{14}"/>---<c:out value="{15}"/>
						<c:out value="${sessionScope.j_user_bean.downloadFlussiRendicontazioneEnabled}"/>
						<c:out value="${sessionScope.j_user_bean.invioFlussiRendicontazioneViaFtpEnabled}"/>
						
						--><%-- modifica PG110260 --%>
						<s:if
							right="${sessionScope.j_user_bean.downloadFlussiRendicontazioneEnabled}" control="eq" left="true" 
							operator="and" 
							secondright="{14}" secondcontrol="eq" secondleft="N" >
							
							<s:then>
									<s:hyperlink
										cssclass="hlStyle" 
										href="downloadFlusso.do?tx_nome_flusso={9}&nomeFile={9}"
										imagesrc="../applications/templates/rendicontazione/img/download.png"
										alt="Download del flusso {9}" text="" />
							</s:then>
							<s:else></s:else>
						</s:if>
						
						<s:if
							right="${sessionScope.j_user_bean.invioFlussiRendicontazioneViaFtpEnabled}" control="eq" left="true"
							operator="and"
							secondright="{10}{14}" secondcontrol="eq" secondleft="YY">
							<%-- fine modifica PG110260 --%>
							<s:then>
										
							</s:then>
							<s:else>
								<s:hyperlink
											cssclass="hlStyle"
											href="inviaFlusso.do?chiaveRendicontazione={8}&tx_tipo_invio_flusso=FTP&nomeFile={9}"
											imagesrc="../applications/templates/rendicontazione/img/{10}_ftp.png"
											alt="Invio del flusso {9} via ftp" text="" />
							</s:else>
						</s:if>
						
						
						
						
						
						
						<%-- modifica PG110260 --%>
						<s:if
							right="${sessionScope.j_user_bean.invioFlussiRendicontazioneViaEmailEnabled}"
							control="eq" left="true" 
							operator="and" 
							secondright="{14}" secondcontrol="eq" secondleft="N" 
							>
							<%-- Fine modifica PG110260 --%>
							<s:then>
								<s:hyperlink
									cssclass="hlStyle"
									href="inviaFlusso.do?chiaveRendicontazione={8}&tx_tipo_invio_flusso=EMAIL&nomeFile={9}"
									imagesrc="../applications/templates/rendicontazione/img/{11}_sendmail.png"
									alt="Invio del flusso {9} via email" text="" />
							</s:then>
							<s:else></s:else>
						</s:if>
						
						<s:if
							right="${sessionScope.j_user_bean.invioFlussiRendicontazioneViaWsEnabled}"
							control="eq" left="true" 
							operator="and" 
							secondright="{14}{18}" secondcontrol="eq" secondleft="NN"
							>
							<%-- Fine modifica PG110260 --%>
							<s:then>
								<s:hyperlink
									cssclass="hlStyle"
									href="inviaFlusso.do?chiaveRendicontazione={8}&tx_tipo_invio_flusso=WS&nomeFile={9}"
									imagesrc="../applications/templates/rendicontazione/img/{18}_invioWS.png"
									alt="Invio del flusso {9} via Web Service" text="" />
							</s:then>
							<s:else></s:else>
						</s:if>

					</s:dgcolumn>
				</s:thendatagrid>
			</s:ifdatagrid>

			<s:dgcolumn label="Azioni">
				<s:if right="{17}" control="eq" left="Y">
					<s:then>
						<s:hyperlink href="../riconciliazionemt/ricercaGiornaliCassa.do?chiaveRen={8}&tx_button_cerca=cerca"
							imagesrc="../applications/templates/riconciliazionenn/img/key.png" 
							alt="Ricerca Flussi" text="" cssclass="hlStyle" />
					</s:then>
					<s:else>
					</s:else>
				</s:if>
			</s:dgcolumn>

		</s:datagrid>
	</s:div>
	<s:div name="div_riepilogo" cssclass="div_align_center divRiepilogo">
		<s:table border="0" cellspacing="0" cellpadding="0"
			cssclass="seda-ui-table">
			<s:thead>
				<s:tr>
					<s:th>Riepilogo Statistico</s:th>
				</s:tr>
			</s:thead>
			<s:tbody>
				<s:tr>
					<s:td>
						<s:datagrid viewstate="" cachedrowset="lista_flussi_riepilogo"
							action="ricercaFlussi.do" border="1" usexml="true">
							<s:dgcolumn index="1" label="Tipo Flusso"></s:dgcolumn>
							<s:dgcolumn label="Tipo Carta">
								<s:if right="{2}" control="eq" left="TOTALE">
									<s:then>
										<b>{2}</b>
									</s:then>
									<s:else>{2}</s:else>
								</s:if>
							</s:dgcolumn>
							<s:dgcolumn label="Numero Pagamenti" css="text_align_right" index="3">
							</s:dgcolumn>
							<s:dgcolumn label="Importo" css="text_align_right" index="4" format="#0.00">
							</s:dgcolumn>
						</s:datagrid>
					</s:td>
				</s:tr>
			</s:tbody>
		</s:table>
	</s:div>
	
</c:if>