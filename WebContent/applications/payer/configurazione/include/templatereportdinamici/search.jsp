<%--  **  IMPORT JAVA TAG LIB  **  --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>

<%--  **  My View State  **  --%>
<m:view_state id="listaTemplate" encodeAttributes="true" />

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
		$("#dataDa_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#dataDa_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#dataDa_day_id").val(dateText.substr(0,2));
												$("#dataDa_month_id").val(dateText.substr(3,2));
												$("#dataDa_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
					//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
		            updateValoreDatePickerFromDdl("dataDa_day_id",
					                            "dataDa_month_id",
					                            "dataDa_year_id",
					                            "dataDa_hidden");
				}
		});

		$("#dataA_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#dataA_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#dataA_day_id").val(dateText.substr(0,2));
												$("#dataA_month_id").val(dateText.substr(3,2));
												$("#dataA_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("dataA_day_id",
				                            "dataA_month_id",
				                            "dataA_year_id",
				                            "dataA_hidden");
			}
		});
	});
</script>

<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_ricerca" action="templateReportDinamici.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />
			<input type="hidden" name="reset" value="false" />
			<input type="hidden" name="datePattern" value="yyyy-MM-dd" />
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Template Report Dinamici</s:div>
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
								type="submit" cssclass="btnimgStyle" title="Aggiorna"  validate="false"/>
						</noscript>
					</s:div>
					<%-- PROVINCIA --%>
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:dropdownlist name="templatereportdinamici_siglaProvincia"
							disable="${enableListaProvince}" cssclass="tbddl floatleft"
							label="Provincia:"
							cssclasslabel="label65 bold floatleft textright"
							cachedrowset="listaProvince" usexml="true"
							onchange="this.form.submit();"
							valueselected="${templatereportdinamici_siglaProvincia}">
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
						<s:dropdownlist name="templatereportdinamici_searchuserCode"
							disable="${enableListaUtenti}" cssclass="tbddl floatleft"
							label="Utente:" cssclasslabel="label65 bold textright"
							cachedrowset="listaUtenti" usexml="true"
							onchange="this.form.submit();"
							valueselected="${templatereportdinamici_searchuserCode}">
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
						<s:dropdownlist name="templatereportdinamici_searchchiaveEnte"
							disable="${enableListaEnti}" 
							cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright"
							label="Ente:" 
							cachedrowset="listaEnti" usexml="true"
							valueselected="${templatereportdinamici_searchchiaveEnte}">
							<s:ddloption text="Tutti gli Enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				<s:div name="divRicercaMetadatiCenter" cssclass="divRicMetadatiCenter">
					<%-- TIPO DOCUMENTO --%>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="templatereportdinamici_searchtipoDocumento" disable="false"
							label="Tip. Doc.:"
							valueselected="${templatereportdinamici_searchtipoDocumento}"
							cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright">
							<s:ddloption text="Tutti" value="" />
							<s:ddloption value="D" text="Documento" />
							<s:ddloption value="B" text="Bollettino" />
							<s:ddloption value="Q" text="Quietanze" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				<s:div name="divRicercaMetadatiRight" cssclass="divRicMetadatiRight">
					<%-- TIPOLOGIA SERVIZIO --%>
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="templatereportdinamici_searchtipologiaServizio"
							disable="false" 
							cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright"
							label="Tip. Servizio:" 
							cachedrowset="listaTipologiaServizi" usexml="true"
							valueselected="${templatereportdinamici_searchtipologiaServizio}">
							<s:ddloption text="Tutte le tipologie" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				<%-- PERIODO DI VALIDITA' --%>
				<s:div name="divRicercaMetadatiLeft1" cssclass="divRicMetadatiLeft">
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:div name="divElement7a" cssclass="labelData">
							<s:label name="label_data" cssclass="seda-ui-label label85 bold textright"
								text="Periodo di validit&agrave;" />
						</s:div>
						<s:div name="divElement7b" cssclass="floatleft">
							<s:div name="div_dataDa" cssclass="divDataDa">
								<s:date label="Da:" prefix="dataDa" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${dataDa}"></s:date>
								<input type="hidden" id="dataDa_hidden" value="" />
							</s:div>
							<s:div name="div_dataA" cssclass="divDataA">
								<s:date label="A:" prefix="dataA" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${dataA}"></s:date>
								<input type="hidden" id="dataA_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
				</s:div>
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" text="Reset" type="submit" cssclass="btnStyle" validate="false" />
				<s:button id="tx_button_nuovo" onclick="" text="Nuovo" type="submit" cssclass="btnStyle" validate="false" />
			</s:div>
		</s:form>
	</s:div>
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">Elenco Template Report Dinamici</s:div>
</s:div>
