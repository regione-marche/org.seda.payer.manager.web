<%--  **  IMPORT JAVA TAG LIB  **  --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>

<%--  **  My View State  **  --%>
<m:view_state id="listaOttico" encodeAttributes="true" />

<!-- ** JQuery Functions Library ** -->
<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js" type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js" type="text/javascript"></script>

<!-- ** Page Functions ** -->
<script type="text/javascript">
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();

	$(function() {
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#dataElaborazioneDa_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#dataElaborazioneDa_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#dataElaborazioneDa_day_id").val(dateText.substr(0,2));
												$("#dataElaborazioneDa_month_id").val(dateText.substr(3,2));
												$("#dataElaborazioneDa_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("dataElaborazioneDa_day_id",
				                            "dataElaborazioneDa_month_id",
				                            "dataElaborazioneDa_year_id",
				                            "dataElaborazioneDa_hidden");
			}
		});
		$("#dataElaborazioneA_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#dataElaborazioneA_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#dataElaborazioneA_day_id").val(dateText.substr(0,2));
												$("#dataElaborazioneA_month_id").val(dateText.substr(3,2));
												$("#dataElaborazioneA_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("dataElaborazioneA_day_id",
				                            "dataElaborazioneA_month_id",
				                            "dataElaborazioneA_year_id",
				                            "dataElaborazioneA_hidden");
			}
		});
		$("#dataCreazioneDa_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#dataCreazioneDa_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#dataCreazioneDa_day_id").val(dateText.substr(0,2));
												$("#dataCreazioneDa_month_id").val(dateText.substr(3,2));
												$("#dataCreazioneDa_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("dataCreazioneDa_day_id",
				                            "dataCreazioneDa_month_id",
				                            "dataCreazioneDa_year_id",
				                            "dataCreazioneDa_hidden");
			}
		});
		$("#dataCreazioneA_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#dataCreazioneA_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#dataCreazioneA_day_id").val(dateText.substr(0,2));
												$("#dataCreazioneA_month_id").val(dateText.substr(3,2));
												$("#dataCreazioneA_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("dataCreazioneA_day_id",
				                            "dataCreazioneA_month_id",
				                            "dataCreazioneA_year_id",
				                            "dataCreazioneA_hidden");
			}
		});
	});
</script>

<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_ricerca" action="elaborazioni.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />
			<input type="hidden" name="reset" value="false" />
			<input type="hidden" name="datePattern" value="yyyy-MM-dd" />
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Lista Elaborazioni - Ottico</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
					<%-- SOCIETA --%>
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:dropdownlist name="codiceSocieta" disable="${enableListaSocieta}"
							cssclass="tbddl floatleft" label="Societ&agrave;:"
							cssclasslabel="label85 bold floatleft textright"
							cachedrowset="listaSocieta" usexml="true"
							onchange="this.form.submit();"
							valueselected="${codiceSocieta}">
							<s:ddloption text="Tutte le Societ&agrave;" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_societa_changed" 
								disable="${enableListaSocieta}" onclick="" text="" 
								type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false"/>
						</noscript>
					</s:div>
					<%-- PROVINCIA --%>
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:dropdownlist name="elaborazioni_siglaProvincia"
							disable="${enableListaProvince}" cssclass="tbddl floatleft"
							label="Provincia:"
							cssclasslabel="label65 bold floatleft textright"
							cachedrowset="listaProvince" usexml="true"
							onchange="this.form.submit();"
							valueselected="${elaborazioni_siglaProvincia}">
							<s:ddloption text="Tutte le Province" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_provincia_changed"
								disable="${enableListaProvince}" onclick="" text=""
								type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false"
								 />
						</noscript>
					</s:div>
					<%-- UTENTE --%>
					<s:div name="divElement3" cssclass="divRicMetadatiRight">
						<s:dropdownlist name="elaborazioni_searchuserCode"
							disable="${enableListaUtenti}" cssclass="tbddl floatleft"
							label="Utente:" cssclasslabel="label65 bold textright"
							cachedrowset="listaUtenti" usexml="true"
							onchange="this.form.submit();"
							valueselected="${elaborazioni_searchuserCode}">
							<s:ddloption text="Tutti gli Utenti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_utente_changed"
								disable="${enableListaUtenti}" onclick="" text=""
								type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false"
								 />
						</noscript>
					</s:div>
				</s:div>
				<s:div name="divRicercaMetadatiLeft" cssclass="divRicMetadatiLeft">
					<%-- ENTE --%>
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="elaborazioni_searchchiaveEnte"
							disable="${enableListaEnti}" 
							cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright"
							label="Ente:" 
							cachedrowset="listaEnti" usexml="true"
							valueselected="${elaborazioni_searchchiaveEnte}">
							<s:ddloption text="Tutti gli Enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				<%-- DATA ELABORAZIONE --%>
				<s:div name="divRicercaMetadatiCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:div name="divElement5a" cssclass="labelData">
							<s:label name="label_dataElaborazione" cssclass="seda-ui-label label85 bold textright"
								text="Data Invio Flusso" />
						</s:div>
						<s:div name="divElement5b" cssclass="floatleft">
							<s:div name="div_dataElaborazioneDa" cssclass="divDataDa">
								<s:date label="Da:" prefix="dataElaborazioneDa" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${dataElaborazioneDa}"></s:date>
								<input type="hidden" id="dataElaborazioneDa_hidden" value="" />
							</s:div>
							<s:div name="div_dataElaborazioneA" cssclass="divDataA">
								<s:date label="A:" prefix="dataElaborazioneA" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${dataElaborazioneA}"></s:date>
								<input type="hidden" id="dataElaborazioneA_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
				</s:div>
				<%-- DATA CREAZIONE --%>
				<s:div name="divRicercaMetadatiRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:div name="divElement6a" cssclass="labelData">
							<s:label name="label_dataCreazione" cssclass="seda-ui-label label85 bold textright"
								text="Data Ricez. Flusso" />
						</s:div>
						<s:div name="divElement6b" cssclass="floatleft">
							<s:div name="div_dataCreazioneDa" cssclass="divDataDa">
								<s:date label="Da:" prefix="dataCreazioneDa" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${dataCreazioneDa}"></s:date>
								<input type="hidden" id="dataCreazioneDa_hidden" value="" />
							</s:div>
							<s:div name="div_dataCreazioneA" cssclass="divDataA">
								<s:date label="A:" prefix="dataCreazioneA" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${dataCreazioneA}"></s:date>
								<input type="hidden" id="dataCreazioneA_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
				</s:div>
				<%-- TIPOLOGIA FLUSSO --%>
				<s:div name="divRicercaMetadatiLeft2" cssclass="divRicMetadatiLeft">
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="elaborazioni_tipologiaFlusso" disable="false"
							label="Tip. Flusso:"
							valueselected="${elaborazioni_tipologiaFlusso}"
							cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright">
							<s:ddloption value="" text="Tutti i Tipi" />
							<s:ddloption value="FTE" text="FTE - Fatturazione Elettr." />
							<s:ddloption value="PEC" text="PEC - Mail PEC" />
							<s:ddloption value="MAL" text="MAL - MAIL" />
							<s:ddloption value="DOI" text="DOI - Doc. per Italia" />
							<s:ddloption value="DOE" text="DOE - Doc. per Estero" />
							<s:ddloption value="FEP" text="FEP - Fatturazione Elettr. - Poste" />
							<s:ddloption value="PEP" text="PEP - Mail PEC - Poste" />
							<s:ddloption value="MAP" text="MAP - MAIL - Poste" />
							<s:ddloption value="DIP" text="DIP - Doc. per Italia - Poste" />
							<s:ddloption value="DEP" text="DEP - Doc. per Estero - Poste" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				<!-- PG22XX09_YL5 INI -->
				<%-- Stato --%>
				<s:div name="divRicercaMetadatiCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="elaborazioni_statoFlusso" disable="false"
							label="Stato:"
							valueselected="${elaborazioni_statoFlusso}"
							cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright">
							<s:ddloption value="" text="Tutti" />
							<s:ddloption value="1" text="Elaborati" />
							<s:ddloption value="0" text="In elaborazione" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				<!-- PG22XX09_YL5 FINE -->
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" text="Reset" type="submit" cssclass="btnStyle" validate="false" />
			</s:div>
		</s:form>
	</s:div>
	<c:if test="${!empty listaOttico}">
		<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">Elenco Elaborazioni</s:div>
	</c:if>
</s:div>
