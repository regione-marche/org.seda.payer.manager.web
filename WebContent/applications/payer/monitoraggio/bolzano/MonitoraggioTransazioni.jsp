<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<m:view_state id="monitoraggiotransazioni" encodeAttributes="true" />

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
	});
</script>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="monitoraggioTransazioniForm"
			action="monitoraggioTransazioni.do" method="post" hasbtn1="false"
			hasbtn2="false" hasbtn3="false">

			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
				Ricerca Transazioni
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
						<noscript><s:button id="tx_button_societa_changed"
							disable="${ddlSocietaDisabled}" onclick="" text="" type="submit"
							cssclass="btnimgStyle" title="Aggiorna" validate="false" /></noscript>



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
						<noscript><s:button id="tx_button_provincia_changed"
							disable="${ddlProvinciaDisabled}" onclick="" text=""
							type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
						</noscript>
					</s:div>

					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:dropdownlist name="tx_UtenteEnte"
							disable="${ddlUtenteEnteDisabled}" cssclass="tbddlMax floatleft"
							label="Ente:" cssclasslabel="label65 bold textright"
							cachedrowset="listaUtentiEnti" usexml="true"
							onchange="setFiredButton('tx_button_ente_changed');this.form.submit();"
							valueselected="${tx_UtenteEnte}">
							<s:ddloption text="Tutti gli Enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript><s:button id="tx_button_ente_changed"
							disable="${ddlUtenteEnteDisabled}" onclick="" text=""
							type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false" /></noscript>
					</s:div>


				</s:div>

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;accept=^(?:(?!').)*$;maxlength=64"
							bmodify="true" maxlenght="64" cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							name="tx_codice_transazione" label="Id Transazione:"
							message="[accept=Id Transazione: Inserire un Id Transazione valido]"
							text="${tx_codice_transazione}" />
					</s:div>

					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_canale_pagamento" disable="false"
							cssclass="tbddlMax floatleft" label="Canale:"
							cssclasslabel="label85 bold textright"
							cachedrowset="listaCanaliPagamento" usexml="true"
							valueselected="${tx_canale_pagamento}">
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>

					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="maxlength=128" bmodify="true"
							maxlenght="128" cssclass="textareaman"
							cssclasslabel="label85 bold textright" name="tx_id_bollettino"
							label="Id Bollettino:" text="${tx_id_bollettino}" />
					</s:div>

					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;accept=^\b(?:\d{1,3}\.){3}\d{1,3}\b$"
							bmodify="true" name="tx_indirizzo_ip" label="Indirizzo IP:"
							maxlenght="15" text="${tx_indirizzo_ip}" cssclass="textareaman"
							message="[accept=Indirizzo IP: ${msg_configurazione_indirizzo_ip}]"
							cssclasslabel="label85 bold textright" />
					</s:div>

					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_rendicontata" disable="false"
							cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright"
							label="Rendicontata:" valueselected="${tx_rendicontata}">
							<s:ddloption value="" text="Tutte" />
							<s:ddloption value="1" text="Si" />
							<s:ddloption value="0" text="No" />
							<s:ddloption value="2" text="Parziali" />
						</s:dropdownlist>
					</s:div>

					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;accept=${monitoraggio_email_regex};maxlength=50" bmodify="true"
							name="tx_user_email" label="Email:" text="${tx_user_email}"
							maxlenght="50" cssclass="textareaman"
							cssclasslabel="label85 bold textright" />
					</s:div>
					
					<s:div name="divElement20" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;maxlength=30" bmodify="true"
							name="tx_chiave_rendicontazione" label="Chiave Flusso:"
							text="${tx_chiave_rendicontazione}" maxlenght="30"
							cssclass="textareaman" cssclasslabel="label85 bold textright" />
					</s:div>
					
					<s:div name="divElement21" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;maxlength=35" bmodify="true"
							name="tx_codice_IUV" label="Codice I.U.V.:"
							text="${tx_codice_IUV}" maxlenght="35"
							cssclass="textareaman" cssclasslabel="label85 bold textright" />
					</s:div>

				</s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_tipo_carta" disable="false"
							cssclass="tbddlMax floatleft" label="Tipo Carta:"
							cssclasslabel="label85 bold textright"
							cachedrowset="listaGateway" usexml="true"
							valueselected="${tx_tipo_carta}">
							<s:ddloption text="Tutte" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>

					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_strumento" disable="false"
							cssclass="tbddlMax floatleft" label="Strumento:"
							cssclasslabel="label85 bold textright"
							cachedrowset="listaStrumenti" usexml="true"
							valueselected="${tx_strumento}">
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>

					<s:div name="divElement12" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_mostra" disable="false" cssclass="tbddl"
							cssclasslabel="label85 bold textright" label="Stato:"
							valueselected="${tx_mostra}">
							<s:ddloption value="" text="Tutte le transazioni" />
							<s:ddloption value="1" text="Transazioni concluse con Successo" />
							<s:ddloption value="3"
								text="Transazioni con successo e riversate" />
							<s:ddloption value="4"
								text="Transazioni con successo e non riversate" />
							<s:ddloption value="5"
								text="Transazioni con successo e non riversabili" />
							<s:ddloption value="2" text="Transazioni Fallite" />
							<s:ddloption value="0" text="Transazioni Sospese" />
						</s:dropdownlist>
					</s:div>

					<s:div name="divElement13" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_tipologia_servizio" disable="false"
							cssclass="tbddlMax floatleft" label="Tip. Servizio:"
							cssclasslabel="label85 bold textright"
							cachedrowset="listaTipologieServizio" usexml="true"
							valueselected="${tx_tipologia_servizio}">
							<s:ddloption text="Tutte le tipologie" value="" />
							<s:ddloption text="{2}" value="{1}_{3}" />
						</s:dropdownlist>
					</s:div>

					<s:div name="divElement14" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;digits;maxlength=20" bmodify="true"
							name="tx_user_sms" label="Numero SMS:" text="${tx_user_sms}"
							maxlenght="20" cssclass="textareaman"
							cssclasslabel="label85 bold textright" />
					</s:div>

					<s:div name="divElement14a" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;digits;maxlength=20" bmodify="true"
							name="tx_id_transaz_atm" label="Id Transaz. Atm:"
							text="${tx_id_transaz_atm}" maxlenght="20" cssclass="textareaman"
							cssclasslabel="label85 bold textright" />
					</s:div>

					<s:div name="divElement14b" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;maxlength=35" bmodify="true"
							name="tx_chiave_quadratura" label="Chiave Quadratura:"
							text="${tx_chiave_quadratura}" maxlenght="35"
							cssclass="textareaman" cssclasslabel="label85 bold textright" />
					</s:div>
					
					<s:div name="divElement22" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;maxlength=50" bmodify="true"
							name="tx_codice_IUR" label="Codice I.U.R.:"
							text="${tx_codice_IUR}" maxlenght="50"
							cssclass="textareaman" cssclasslabel="label85 bold textright" />
					</s:div>
				</s:div>

				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">

					<s:div name="divElement17" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement17a" cssclass="labelData">
							<s:label name="label_importo"
								cssclass="seda-ui-label label85 bold textright" text="Importo" />
						</s:div>

						<s:div name="divElement17b" cssclass="floatleft">
							<s:div name="divImportoDa" cssclass="divDataDa">
								<s:textbox validator="ignore;numberIT;maxlength=15"
									bmodify="true" name="tx_importo_da" label="Da:"
									text="${tx_importo_da}" maxlenght="15"
									cssclass="textareamanImporto" cssclasslabel="labelsmall" />
							</s:div>

							<s:div name="divImportoA" cssclass="divDataA">
								<s:textbox validator="ignore;numberIT;maxlength=15"
									bmodify="true" name="tx_importo_a" label="A:"
									text="${tx_importo_a}" maxlenght="15"
									cssclass="textareamanImporto" cssclasslabel="labelsmall" />
							</s:div>
						</s:div>
					</s:div>

					<s:div name="divElement18" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement18a" cssclass="labelData">
							<s:label name="label_data"
								cssclass="seda-ui-label label85 bold textright"
								text="Data Transazione" />
						</s:div>

						<s:div name="divElement18b" cssclass="floatleft">
							<s:div name="divDataDa" cssclass="divDataDa">
								<s:date label="Da:" prefix="tx_data_da"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
									locale="IT-it" descriptivemonth="false" separator="/"
									calendar="${tx_data_da}" cssclasslabel="labelsmall"
									cssclass="dateman">
								</s:date>
								<input type="hidden" id="tx_data_da_hidden" value="" />
							</s:div>

							<s:div name="divDataA" cssclass="divDataA">
								<s:date label="A:" prefix="tx_data_a"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
									locale="IT-it" descriptivemonth="false" separator="/"
									calendar="${tx_data_a}" cssclasslabel="labelsmall"
									cssclass="dateman">
								</s:date>
								<input type="hidden" id="tx_data_a_hidden" value="" />
							</s:div>
						</s:div>

					</s:div>

					<s:div name="divElement19" cssclass="divRicMetadatiSingleRow">
						<%-- TODO verificare applicazione quadratura --%>
						<c:if test="${requestScope.isRiconciliazioneAct}">
							<s:dropdownlist name="tx_riconciliata" disable="false"
								cssclass="tbddlMax floatleft"
								cssclasslabel="label85 bold textright" label="Riconciliata:"
								valueselected="${tx_riconciliata}">
								<s:ddloption value="" text="Tutte" />
								<s:ddloption value="C" text="Si" />
								<s:ddloption value="A" text="No" />
							</s:dropdownlist>
						</c:if>
					</s:div>
					
					<s:div name="divElement9a" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;maxlength=8" bmodify="true"
							name="tx_id_terminale_pos_fisico" label="Id Terminale POS fisico:"
							text="${tx_id_terminale_pos_fisico}" maxlenght="8"
							cssclass="textareaman" cssclasslabel="label85 bold textright" />
					</s:div>
					
					<s:div name="tipoQuery" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="tx_scelta_query" disable="false"
								cssclass="tbddlMax floatleft"
								cssclasslabel="label85 bold textright" label="Tipo Vista:"
								valueselected="${tx_scelta_query}">
								<s:ddloption value="A" text="Tutte" />
								<s:ddloption value="C" text="Lista Transazioni" />
								<s:ddloption value="B" text="Report Riepilogo" />
							</s:dropdownlist>
					</s:div>

				</s:div>

			</s:div>

			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">

				<s:button id="tx_button_cerca" type="submit" text="Cerca"
					cssclass="btnStyle" onclick="" />
				<s:button id="tx_button_reset" onclick="" text="Reset" type="submit"
					cssclass="btnStyle" />

				<c:if test="${!empty tx_lista_transazioni}">
					<s:button id="tx_button_stampa" onclick="" text="Stampa"
						type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${!empty tx_lista_transazioni}">
					<s:button id="tx_button_download" onclick="" text="Download"
						type="submit" cssclass="btnStyle" />
				</c:if>

				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
			</s:div>
		</s:form>
	</s:div>

</s:div>

<c:if test="${!empty tx_lista_transazioni}">
	<fmt:setLocale value="it_IT" />

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Elenco Transazioni
	</s:div>



	<s:div name="divRisultati" cssclass="divRisultati">
		<s:datagrid cachedrowset="tx_lista_transazioni"
			action="monitoraggioTransazioni.do?vista=monitoraggiotransazioni"
			border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}"
			viewstate="">

			<s:dgcolumn label="Id">
				<s:hyperlink
					href="dettaglioTransazione.do?tx_codice_transazione_hidden={1}"
					onclick=""
					cssclass="blacklink hlStyle"
					imagesrc="../applications/templates/shared/img/Info.png" text=""
					alt="Dettaglio Transazione {1}" />
			</s:dgcolumn>


			<s:dgcolumn index="2" label="Societ&agrave;" asc="SOCA" desc="SOCD" />

			<s:dgcolumn label="Utente" index="3" asc="UTEA" desc="UTED"></s:dgcolumn>
			<s:dgcolumn index="4" label="Data Transazione" asc="DTRA" desc="DTRD"
				format="dd/MM/yyyy HH:mm:ss" />
			<s:dgcolumn index="5" label="Data Eff.Pag." asc="DPGA" desc="DPGD"
				format="dd/MM/yyyy HH:mm:ss" />
			<s:dgcolumn label="Indirizzo IP<br/>Email<br/>SMS">
				{6}<br />
				<s:if
					left="{15}${sessionScope.j_user_bean.azioniPerTransazioniOkEnabled}"
					control="eq" right="1true">
					<s:then>
						<s:hyperlink
							href="modificaEmailSms.do?tx_codice_transazione_hidden={1}&newUserEmail={7}&newUserSms={8}"
							text="{7}" cssclass="blacklink" />
						<br />
						<s:hyperlink
							href="modificaEmailSms.do?tx_codice_transazione_hidden={1}&newUserEmail={7}&newUserSms={8}"
							text="{8}" cssclass="blacklink" />
					</s:then>
					<s:else>
					{7}<br />
					{8}
				</s:else>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn index="9" label="Canale" asc="CANA" desc="CAND"></s:dgcolumn>
			<s:dgcolumn index="10" label="Tipo Carta" asc="CARA" desc="CARD"></s:dgcolumn>

			<!-- inizio LP PG190220 -->
			<!--  Stato Annullto Tecnico -->
			<s:dgcolumn label="Annullo" asc="SATA" desc="SATD">
				<s:if right="{34}" control="eq" left="0">
					<s:then>No</s:then>
				</s:if>
				<s:if right="{34}" control="eq" left="1">
					<s:then>Si</s:then>
				</s:if>
				<s:if right="{34}" control="eq" left="2">
					<s:then>Attesa RT</s:then>
				</s:if>
			</s:dgcolumn>
			<!-- fine LP PG190220 -->

			<!-- ---------------------------------------- -->
			<!-- rendicontazione - areaInvioRendicontazioneEnabled  -->
			<!-- ---------------------------------------- -->
			<s:dgcolumn label="Rendicontata" asc="SREA" desc="SRED">
				<s:if right="{11}" control="eq" left="SI" operator="and"
				secondright="{27}" secondcontrol="ne" secondleft="Migrazione">
					<s:then>
						<c:if test="${appRendicontazioneUteEnabled}">
							<s:hyperlink
								href="../rendicontazione/ricercaFlussi.do?tx_codice_transazione={1}&tx_button_cerca=cerca"
								text="Si" cssclass="blacklink" />
						</c:if>
						<c:if test="${!appRendicontazioneUteEnabled}">
			                  Si
						</c:if>
					</s:then>
				</s:if>
				<s:if right="{11}" control="eq" left="NO">
					<s:then>No</s:then>
				</s:if>
				<s:if right="{11}" control="eq" left="PARZIALE">
					<s:then>
						<c:if test="${appRendicontazioneUteEnabled}">
							<s:hyperlink
										href="../rendicontazione/ricercaFlussi.do?tx_codice_transazione={1}&tx_button_cerca=cerca"
										text="Parziale" cssclass="blacklink" />
							</c:if>
						<c:if test="${!appRendicontazioneUteEnabled}">
			                  Parziale
						</c:if>
					</s:then>					
				</s:if>
				<s:if right="{11}" control="eq" left="SI" operator="and"
				secondright="{27}" secondcontrol="eq" secondleft="Migrazione">
					<s:then>Si</s:then>
				</s:if>
			</s:dgcolumn>

			<!-- ---------------------------------------- -->
			<!-- riconciliata - isRiconciliazioneAct isUteRiconQuadEnabled      -->
			<!-- ---------------------------------------- -->
			<s:ifdatagrid left="${requestScope.isRiconciliazioneAct}"
				control="eq" right="true">
				<s:thendatagrid>
					<s:dgcolumn label="Riconciliata" asc="SRID" desc="SRID">					
						<s:if right="{12}{28}" control="eq" left="SIS" operator="and"
							secondright="{27}" secondcontrol="ne" secondleft="Migrazione">
							<s:then>
								<c:if test="${appRiconciliazioneUteEnabled}">
									<s:hyperlink
										href="../riconciliazionenn/riconciliazioneTransazioniNodo.do?chiaveTransazione={1}&tx_button_cerca=cerca"
										text="Si" cssclass="blacklink" />
								</c:if>
								<c:if test="${!appRiconciliazioneUteEnabled}">
							    Si
								</c:if>
							</s:then>
						</s:if>
						<s:if right="{12}{28}" control="eq" left="SIN" operator="and"
							secondright="{27}" secondcontrol="ne" secondleft="Migrazione">
							<s:then>
								<c:if test="${appRiconciliazioneUteEnabled}">
									<s:hyperlink
										href="../riconciliazione/riconciliazioneTransazioni.do?chiaveMovimento={13}&tx_button_cerca=cerca"
										text="Si" cssclass="blacklink" />
								</c:if>
								<c:if test="${!appRiconciliazioneUteEnabled}">
							    Si
								</c:if>
							</s:then>
						</s:if>
						<s:if right="{12}" control="eq" left="SI" operator="and"
						secondright="{27}" secondcontrol="eq" secondleft="Migrazione">
							<s:then>Si</s:then>
						</s:if>
						<s:if right="{12}" control="eq" left="NO" >
						<s:then>
							No
							</s:then>
						</s:if>
					</s:dgcolumn>
				</s:thendatagrid>
			</s:ifdatagrid>
			<s:dgcolumn index="31" label="N. Dett." css="text_align_right" asc="NDTA" desc="NDTD" />
			<s:dgcolumn format="#,##0.00" index="14" label="Importo totale"
				css="text_align_right" asc="IMPA" desc="IMPB" />
			<s:dgcolumn label="Stato" css="textcenter">
				<s:if right="{15}" control="eq" left="0">
					<s:then>
						<s:image
							src="../applications/templates/monitoraggio/img/circle_grey.png"
							alt="Transazione sospesa" height="16" width="16"
							cssclass="hlStyle" />
					</s:then>
				</s:if>
				<s:if right="{15}" control="eq" left="1">
					<s:then>
						<s:image
							src="../applications/templates/monitoraggio/img/circle_green.png"
							alt="Transazione completata" height="16" width="16"
							cssclass="hlStyle" />
					</s:then>
				</s:if>
				<s:if right="{15}" control="eq" left="2">
					<s:then>
						<s:image
							src="../applications/templates/monitoraggio/img/circle_red.png"
							alt="Transazione fallita" height="16" width="16"
							cssclass="hlStyle" />
					</s:then>
				</s:if>
			</s:dgcolumn>

			<!-- inizio LP PG190220                                            -->
			<!--  TODO: Quali azioni bloccare per stato annullo {34} = 0, 1, 2 -->
			<!--        per adesso se stato anullo == 2 non consento azioni    -->
			<!--        se stato annullo == 1 o 0 lascio le azioni come prima  -->
			<!-- fine LP PG190220                                              -->

			<%-- ** Se il canale di provenienza e' ATM vengono inibite tutte le azioni a meno dell'invio mail all'amministratore ** --%>
			<s:dgcolumn
				label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;">
				<!-- stato annullo 0 -->
				<s:if right="{15}{21}{34}" control="eq" left="1Y0" operator="and"
				secondright="{27}" secondcontrol="ne" secondleft="Migrazione">
					<s:then>
						<c:if test="${sessionScope.j_user_bean.azioniPerTransazioniOkEnabled}">
							<s:hyperlink
								href="inviaNotificaContribuente.do?tx_codice_transazione_hidden={1}&vista=monitoraggiotransazioni"
								imagesrc="../applications/templates/monitoraggio/img/notificaUtente.png"
								alt="Invia Notifica al Contribuente" text=""
								cssclass="hlStyle hlChannel{19}Style" />
							<s:hyperlink
								href="inviaNotificaAmministratore.do?tx_codice_transazione_hidden={1}&vista=monitoraggiotransazioni"
								imagesrc="../applications/templates/monitoraggio/img/notificaAdmin.png"
								alt="Invia Notifica all'Amministratore" text=""
								cssclass="hlStyle" />
						</c:if>
						<c:if
							test="${sessionScope.j_user_bean.azioniPerTransazioniOkEnabled}">
							<s:hyperlink
								href="stampaPDFBollettini.do?tx_codice_transazione_hidden={1}"
								imagesrc="../applications/templates/monitoraggio/img/stampaBollettini.png"
								alt="Stampa Allegati PDF" text=""
								cssclass="hlStyle hlChannel{19}Style" />
						</c:if>
					</s:then>
					<s:else></s:else>
				</s:if>
				<s:if right="{15}{21}{34}" control="eq" left="1N0" operator="and"
				secondright="{27}" secondcontrol="ne" secondleft="Migrazione">
					<s:then>
						<c:if test="${sessionScope.j_user_bean.azioniPerTransazioniOkEnabled}">
							<s:hyperlink
								href="inviaNotificaContribuente.do?tx_codice_transazione_hidden={1}&vista=monitoraggiotransazioni"
								imagesrc="../applications/templates/monitoraggio/img/notificaUtente.png"
								alt="Invia Notifica al Contribuente" text=""
								cssclass="hlStyle hlChannel{19}Style" />
							<s:hyperlink
								href="inviaNotificaAmministratore.do?tx_codice_transazione_hidden={1}&vista=monitoraggiotransazioni"
								imagesrc="../applications/templates/monitoraggio/img/notificaAdmin.png"
								alt="Invia Notifica all'Amministratore" text=""
								cssclass="hlStyle" />
						</c:if>
						<c:if
							test="${sessionScope.j_user_bean.azioniPerTransazioniOkEnabled}">
							<s:hyperlink
								href="stampaPDFBollettini.do?tx_codice_transazione_hidden={1}"
								imagesrc="../applications/templates/monitoraggio/img/stampaBollettini.png"
								alt="Stampa Allegati PDF" text=""
								cssclass="hlStyle hlChannel{19}Style" />
						</c:if>
					</s:then>
					<s:else></s:else>
				</s:if>
				<s:if right="{15}{21}{34}" control="eq" left="1X0" operator="and"
				secondright="{27}" secondcontrol="ne" secondleft="Migrazione">
					<s:then>
						<c:if
							test="${sessionScope.j_user_bean.azioniPerTransazioniOkEnabled}">
							<s:hyperlink
								href="inviaNotificaAmministratore.do?tx_codice_transazione_hidden={1}&vista=monitoraggiotransazioni"
								imagesrc="../applications/templates/monitoraggio/img/notificaAdmin.png"
								alt="Invia Notifica all'Amministratore" text=""
								cssclass="hlStyle" />
						</c:if>
						<c:if
							test="${sessionScope.j_user_bean.azioniPerTransazioniOkEnabled}">
							<s:hyperlink
								href="stampaPDFBollettini.do?tx_codice_transazione_hidden={1}"
								imagesrc="../applications/templates/monitoraggio/img/stampaBollettini.png"
								alt="Stampa Allegati PDF" text=""
								cssclass="hlStyle hlChannel{19}Style" />
						</c:if>
					</s:then>
					<s:else></s:else>
				</s:if>
				<s:if right="{15}{34}" control="eq" left="00" operator="and"
				secondright="{27}" secondcontrol="ne" secondleft="Migrazione">
					<s:then>
						<c:if
							test="${sessionScope.j_user_bean.azioniPerTransazioniKoEnabled}">
							<s:hyperlink
								href="forzaAllineamentoTransazione.do?tx_codice_transazione_hidden={1}"
								imagesrc="../applications/templates/monitoraggio/img/forzaAllineamento.png"
								alt="Forza allineamento" text=""
								cssclass="hlStyle hlChannel{19}Style" />
							<s:hyperlink
								href="allineaManualmenteTransazione.do?tx_codice_transazione_hidden={1}&tx_chiave_gateway={16}&vista=monitoraggiotransazioni&tx_button_allinea=tx_button_allinea"
								imagesrc="../applications/templates/monitoraggio/img/allineamentoAutomatico.png"
								alt="Allineamento Automatico" text=""
								cssclass="hlStyle hlChannel{19}Style hlGtwType{20}Style" />
							
						</c:if>
					</s:then>
					<s:else></s:else>
				</s:if>
				<s:if right="{15}{20}{34}" control="eq" left="0M0" operator="and"
				secondright="{27}" secondcontrol="ne" secondleft="Migrazione">
					<s:then>
						<c:if
							test="${requestScope.isRiconciliazioneAct}">
							<s:hyperlink
								href="sendPdfMavTransazione.do?tx_codice_transazione_hidden={1}&vista=monitoraggiotransazioni&tx_button_send_pdf_mav=tx_button_send_pdf_mav"
								imagesrc="../applications/templates/monitoraggio/img/sendPdfMav.png"
								alt="Invia PDF MAV" text=""
								cssclass="hlStyle hlChannel{19}Style" />
						</c:if>
					</s:then>
					<s:else></s:else>
				</s:if>
				<!-- stato annullo 1 -->
				<s:if right="{15}{21}{34}" control="eq" left="1Y1" operator="and"
				secondright="{27}" secondcontrol="ne" secondleft="Migrazione">
					<s:then>
						<c:if test="${sessionScope.j_user_bean.azioniPerTransazioniOkEnabled}">
							<s:hyperlink
								href="inviaNotificaContribuente.do?tx_codice_transazione_hidden={1}&vista=monitoraggiotransazioni"
								imagesrc="../applications/templates/monitoraggio/img/notificaUtente.png"
								alt="Invia Notifica al Contribuente" text=""
								cssclass="hlStyle hlChannel{19}Style" />
							<s:hyperlink
								href="inviaNotificaAmministratore.do?tx_codice_transazione_hidden={1}&vista=monitoraggiotransazioni"
								imagesrc="../applications/templates/monitoraggio/img/notificaAdmin.png"
								alt="Invia Notifica all'Amministratore" text=""
								cssclass="hlStyle" />
						</c:if>
						<c:if
							test="${sessionScope.j_user_bean.azioniPerTransazioniOkEnabled}">
							<s:hyperlink
								href="stampaPDFBollettini.do?tx_codice_transazione_hidden={1}"
								imagesrc="../applications/templates/monitoraggio/img/stampaBollettini.png"
								alt="Stampa Allegati PDF" text=""
								cssclass="hlStyle hlChannel{19}Style" />
						</c:if>
					</s:then>
					<s:else></s:else>
				</s:if>
				<s:if right="{15}{21}{34}" control="eq" left="1N1" operator="and"
				secondright="{27}" secondcontrol="ne" secondleft="Migrazione">
					<s:then>
						<c:if test="${sessionScope.j_user_bean.azioniPerTransazioniOkEnabled}">
							<s:hyperlink
								href="inviaNotificaContribuente.do?tx_codice_transazione_hidden={1}&vista=monitoraggiotransazioni"
								imagesrc="../applications/templates/monitoraggio/img/notificaUtente.png"
								alt="Invia Notifica al Contribuente" text=""
								cssclass="hlStyle hlChannel{19}Style" />
							<s:hyperlink
								href="inviaNotificaAmministratore.do?tx_codice_transazione_hidden={1}&vista=monitoraggiotransazioni"
								imagesrc="../applications/templates/monitoraggio/img/notificaAdmin.png"
								alt="Invia Notifica all'Amministratore" text=""
								cssclass="hlStyle" />
						</c:if>
						<c:if
							test="${sessionScope.j_user_bean.azioniPerTransazioniOkEnabled}">
							<s:hyperlink
								href="stampaPDFBollettini.do?tx_codice_transazione_hidden={1}"
								imagesrc="../applications/templates/monitoraggio/img/stampaBollettini.png"
								alt="Stampa Allegati PDF" text=""
								cssclass="hlStyle hlChannel{19}Style" />
						</c:if>
					</s:then>
					<s:else></s:else>
				</s:if>
				<s:if right="{15}{21}{34}" control="eq" left="1X1" operator="and"
				secondright="{27}" secondcontrol="ne" secondleft="Migrazione">
					<s:then>
						<c:if
							test="${sessionScope.j_user_bean.azioniPerTransazioniOkEnabled}">
							<s:hyperlink
								href="inviaNotificaAmministratore.do?tx_codice_transazione_hidden={1}&vista=monitoraggiotransazioni"
								imagesrc="../applications/templates/monitoraggio/img/notificaAdmin.png"
								alt="Invia Notifica all'Amministratore" text=""
								cssclass="hlStyle" />
						</c:if>
						<c:if
							test="${sessionScope.j_user_bean.azioniPerTransazioniOkEnabled}">
							<s:hyperlink
								href="stampaPDFBollettini.do?tx_codice_transazione_hidden={1}"
								imagesrc="../applications/templates/monitoraggio/img/stampaBollettini.png"
								alt="Stampa Allegati PDF" text=""
								cssclass="hlStyle hlChannel{19}Style" />
						</c:if>
					</s:then>
					<s:else></s:else>
				</s:if>
				<s:if right="{15}{34}" control="eq" left="01" operator="and"
				secondright="{27}" secondcontrol="ne" secondleft="Migrazione">
					<s:then>
						<c:if
							test="${sessionScope.j_user_bean.azioniPerTransazioniKoEnabled}">
							<s:hyperlink
								href="forzaAllineamentoTransazione.do?tx_codice_transazione_hidden={1}"
								imagesrc="../applications/templates/monitoraggio/img/forzaAllineamento.png"
								alt="Forza allineamento" text=""
								cssclass="hlStyle hlChannel{19}Style" />
							<s:hyperlink
								href="allineaManualmenteTransazione.do?tx_codice_transazione_hidden={1}&tx_chiave_gateway={16}&vista=monitoraggiotransazioni&tx_button_allinea=tx_button_allinea"
								imagesrc="../applications/templates/monitoraggio/img/allineamentoAutomatico.png"
								alt="Allineamento Automatico" text=""
								cssclass="hlStyle hlChannel{19}Style hlGtwType{20}Style" />
							
						</c:if>
					</s:then>
					<s:else></s:else>
				</s:if>
				<s:if right="{15}{20}{34}" control="eq" left="0M1" operator="and"
				secondright="{27}" secondcontrol="ne" secondleft="Migrazione">
					<s:then>
						<c:if
							test="${requestScope.isRiconciliazioneAct}">
							<s:hyperlink
								href="sendPdfMavTransazione.do?tx_codice_transazione_hidden={1}&vista=monitoraggiotransazioni&tx_button_send_pdf_mav=tx_button_send_pdf_mav"
								imagesrc="../applications/templates/monitoraggio/img/sendPdfMav.png"
								alt="Invia PDF MAV" text=""
								cssclass="hlStyle hlChannel{19}Style" />
						</c:if>
					</s:then>
					<s:else></s:else>
				</s:if>

			</s:dgcolumn>

		</s:datagrid>
		
	  </s:div>
		
  </c:if>

	<c:if test="${!empty listaTransazioniGroupedSuccess}">
		<s:div name="divTableTitle2" cssclass="divTableTitle bold">
			Riepilogo statistico
		</s:div>
			<s:table cssclass="seda-ui-datagrid" border="1" cellspacing="0"
				cellpadding="0">
				<s:thead>
					<s:tr>
						<s:th cssclass="seda-ui-datagridheadercell" icol="7">
							<b>Totali Transazioni completate con successo</b>
						</s:th>
					</s:tr>
				</s:thead>
				<s:tbody>
					<s:tr cssclass="seda-ui-datagridrowdispari">
						<s:td irow="2" cssclass="seda-ui-datagridcell">&nbsp;<br />Canale</s:td>
						<s:td icol="2" cssclass="seda-ui-datagridcell">Pagamenti On-Line</s:td>
						<s:td icol="2" cssclass="seda-ui-datagridcell">Estratto Conto</s:td>
						<s:td icol="2" cssclass="seda-ui-datagridcell">Totale</s:td>
					</s:tr>
					<s:tr cssclass="seda-ui-datagridrowdispari">
						<s:td cssclass="seda-ui-datagridcell">Num. Boll.</s:td>
						<s:td cssclass="seda-ui-datagridcell">Importo</s:td>
						<s:td cssclass="seda-ui-datagridcell">Num. Boll.</s:td>
						<s:td cssclass="seda-ui-datagridcell">Importo</s:td>
						<s:td cssclass="seda-ui-datagridcell">Num. Boll.</s:td>
						<s:td cssclass="seda-ui-datagridcell">Importo</s:td>
					</s:tr>
					<c:forEach items="${requestScope.listaTransazioniGroupedSuccess}"
						var="grouped">
						<s:tr cssclass="seda-ui-datagridrowpari">
							<s:td cssclass="seda-ui-datagridcell">
								<c:out value="${grouped.canale}" />
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								<c:out value="${grouped.numeroBollettiniPG}" />
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								<fmt:formatNumber type="NUMBER" value="${grouped.importoPG}"
									minFractionDigits="2" maxFractionDigits="2" />
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								<c:out value="${grouped.numeroBollettiniEC}" />
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								<fmt:formatNumber type="NUMBER" value="${grouped.importoEC}"
									minFractionDigits="2" maxFractionDigits="2" />
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								<c:out value="${grouped.numeroBollettini}" />
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								<fmt:formatNumber type="NUMBER" value="${grouped.importoTotale}"
									minFractionDigits="2" maxFractionDigits="2" />
							</s:td>
						</s:tr>
					</c:forEach>
					<s:tr cssclass="seda-ui-datagridrowdispari">
						<s:td cssclass="seda-ui-datagridheadercell" icol="1">
							<b>Totale</b>
						</s:td>
						<s:td cssclass="seda-ui-datagridcellbordered text_align_right">
							<b>${requestScope.TotPGNum}</b>
						</s:td>
						<s:td cssclass="seda-ui-datagridcellbordered text_align_right">
							<b><fmt:formatNumber type="NUMBER"
								value="${requestScope.TotPGImp}" minFractionDigits="2"
								maxFractionDigits="2" /></b>
						</s:td>
						<s:td cssclass="seda-ui-datagridcellbordered text_align_right">
							<b>${requestScope.TotECNum}</b>
						</s:td>
						<s:td cssclass="seda-ui-datagridcellbordered text_align_right">
							<b><fmt:formatNumber type="NUMBER"
								value="${requestScope.TotECImp}" minFractionDigits="2"
								maxFractionDigits="2" /></b>
						</s:td>
						<s:td cssclass="seda-ui-datagridcellbordered text_align_right">
							<b>${requestScope.TotBol}</b>
						</s:td>
						<s:td cssclass="seda-ui-datagridcellbordered text_align_right">
							<b><fmt:formatNumber type="NUMBER"
								value="${requestScope.grouped_success_total}"
								minFractionDigits="2" maxFractionDigits="2" /></b>
						</s:td>
					</s:tr>
				</s:tbody>
			</s:table>
		</c:if>

	   <c:if test="${userProfile!='AMEN'}" >
	   	  <c:if test="${tx_scelta_query == 'A' || tx_scelta_query == 'B'}" >
			<s:table cssclass="seda-ui-datagrid" border="1" cellspacing="0"
				cellpadding="3">
				<s:thead>
					<s:tr cssclass="seda-ui-datagridrowpari">
						<s:th cssclass="seda-ui-datagridheadercell" icol="7">
							<b>Importi Totali sui rispettivi Gateway</b>
						</s:th>
					</s:tr>
				</s:thead>
				<s:tbody>
					<s:tr cssclass="seda-ui-datagridrowdispari">
						<s:td irow="2" cssclass="seda-ui-datagridcell">&nbsp;<br />Carte Utilizzate</s:td>
						<s:td cssclass="seda-ui-datagridcell">Importi Totali</s:td>
						<s:td cssclass="seda-ui-datagridcell">Costi Transazioni</s:td>
						<s:td cssclass="seda-ui-datagridcell">Spese Notifica</s:td>
						<s:td cssclass="seda-ui-datagridcell">Costi Banca</s:td>
						<s:td cssclass="seda-ui-datagridcell">Totale Netto Boll.</s:td>
						<s:td cssclass="seda-ui-datagridcell">Totale Netto Banca</s:td>
					</s:tr>
					<s:tr cssclass="seda-ui-datagridrowdispari">
						<s:td cssclass="seda-ui-datagridcell">(A)</s:td>
						<s:td cssclass="seda-ui-datagridcell">(B)</s:td>
						<s:td cssclass="seda-ui-datagridcell">(C)</s:td>
						<s:td cssclass="seda-ui-datagridcell">(D)</s:td>
						<s:td cssclass="seda-ui-datagridcell">(A-B-C)</s:td>
						<s:td cssclass="seda-ui-datagridcell">(A-C-D)</s:td>
					</s:tr>
					<c:forEach items="${requestScope.listaTransazioniGrouped}"
						var="grouped">
						<s:tr cssclass="seda-ui-datagridrowpari">
							<s:td cssclass="seda-ui-datagridcell">
								<c:out value="${grouped.carta}" />
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								<fmt:formatNumber type="NUMBER" value="${grouped.importiTotali}"
									minFractionDigits="2" maxFractionDigits="2" />
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								<fmt:formatNumber type="NUMBER"
									value="${grouped.costiTransazioni}" minFractionDigits="2"
									maxFractionDigits="2" />
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								<fmt:formatNumber type="NUMBER" value="${grouped.speseNotifica}"
									minFractionDigits="2" maxFractionDigits="2" />
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								<fmt:formatNumber type="NUMBER" value="${grouped.costiBanca}"
									minFractionDigits="2" maxFractionDigits="2" />
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								<fmt:formatNumber type="NUMBER"
									value="${grouped.totaleNettoBanca}" minFractionDigits="2"
									maxFractionDigits="2" />
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								<fmt:formatNumber type="NUMBER"
									value="${grouped.totaleNettoBollettini}" minFractionDigits="2"
									maxFractionDigits="2" />
							</s:td>
						</s:tr>
					</c:forEach>
					<s:tr cssclass="seda-ui-datagridrowdispari">
						<s:td cssclass="seda-ui-datagridheadercell">
							<b>Totale</b>
						</s:td>
						<s:td cssclass="seda-ui-datagridcellbordered text_align_right">
							<b><fmt:formatNumber type="NUMBER"
								value="${requestScope.grouped_total}" minFractionDigits="2"
								maxFractionDigits="2" /></b>
						</s:td>
						<s:td cssclass="seda-ui-datagridcellbordered text_align_right">
							<b><fmt:formatNumber type="NUMBER"
								value="${requestScope.TotB}" minFractionDigits="2"
								maxFractionDigits="2" /></b>
						</s:td>
						<s:td cssclass="seda-ui-datagridcellbordered text_align_right">
							<b><fmt:formatNumber type="NUMBER"
								value="${requestScope.TotC}" minFractionDigits="2"
								maxFractionDigits="2" /></b>
						</s:td>
						<s:td cssclass="seda-ui-datagridcellbordered text_align_right">
							<b><fmt:formatNumber type="NUMBER"
								value="${requestScope.TotD}" minFractionDigits="2"
								maxFractionDigits="2" /></b>
						</s:td>
						<s:td cssclass="seda-ui-datagridcellbordered text_align_right">
							<b><fmt:formatNumber type="NUMBER"
								value="${requestScope.TotABC}" minFractionDigits="2"
								maxFractionDigits="2" /></b>
						</s:td>
						<s:td cssclass="seda-ui-datagridcellbordered text_align_right">
							<b><fmt:formatNumber type="NUMBER"
								value="${requestScope.TotACD}" minFractionDigits="2"
								maxFractionDigits="2" /></b>
						</s:td>
					</s:tr>
				</s:tbody>
			</s:table>
		</c:if>
	</c:if>
		
		<c:if test="${!empty requestScope.listaTransazioniOneriGrouped}">
			<s:table cssclass="seda-ui-datagrid" border="1" cellspacing="0"
				cellpadding="3">
				<s:thead>
					<s:tr cssclass="seda-ui-datagridrowpari">
						<s:th cssclass="seda-ui-datagridheadercell" icol="7">
							<b>Importi Totali Oneri suddivisi per Ente Destinatario</b>
						</s:th>
					</s:tr>
				</s:thead>
				<s:tbody>
					<s:tr cssclass="seda-ui-datagridrowdispari">
						<s:td cssclass="seda-ui-datagridcell">Ente</s:td>
						<s:td cssclass="seda-ui-datagridcell">Importo Onere</s:td>
						<s:td cssclass="seda-ui-datagridcell">Importo Contabile in ingresso</s:td>
						<s:td cssclass="seda-ui-datagridcell">Importo Contabile in uscita</s:td>
					</s:tr>
					
					<c:forEach items="${requestScope.listaTransazioniOneriGrouped}" var="grouped">
						<s:tr cssclass="seda-ui-datagridrowpari">
							<s:td cssclass="seda-ui-datagridcell">
								<c:out value="${grouped.descrizioneEntePortaleEsterno}" />
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								<fmt:formatNumber type="NUMBER" value="${grouped.importoOnere}"
									minFractionDigits="2" maxFractionDigits="2" />
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								<fmt:formatNumber type="NUMBER"
									value="${grouped.importoContabileInIngresso}" minFractionDigits="2"
									maxFractionDigits="2" />
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								<fmt:formatNumber type="NUMBER" value="${grouped.importoContabileInUscita}"
									minFractionDigits="2" maxFractionDigits="2" />
							</s:td>
						</s:tr>
					</c:forEach>
					<s:tr cssclass="seda-ui-datagridrowdispari">
						<s:td cssclass="seda-ui-datagridheadercell">
							<b>Totale</b>
						</s:td>
						<s:td cssclass="seda-ui-datagridcellbordered text_align_right">
							<b><fmt:formatNumber type="NUMBER"
								value="${requestScope.totImportoOnere}" minFractionDigits="2"
								maxFractionDigits="2" /></b>
						</s:td>
						<s:td cssclass="seda-ui-datagridcellbordered text_align_right">
							<b><fmt:formatNumber type="NUMBER"
								value="${requestScope.totImportoContabileInIngresso}" minFractionDigits="2"
								maxFractionDigits="2" /></b>
						</s:td>
						<s:td cssclass="seda-ui-datagridcellbordered text_align_right">
							<b><fmt:formatNumber type="NUMBER"
								value="${requestScope.totImportoContabileInUscita}" minFractionDigits="2"
								maxFractionDigits="2" /></b>
						</s:td>
					</s:tr>
				</s:tbody>
			</s:table>
		</c:if>
