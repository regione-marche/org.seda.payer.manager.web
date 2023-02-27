<%--  **  IMPORT JAVA TAG LIB  **  --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>

<%--  **  My View State  **  --%>
<m:view_state id="listaTassonomia" encodeAttributes="true" />

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
		<s:form name="form_ricerca" action="tassonomia.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />
			<input type="hidden" name="reset" value="false" />
			<input type="hidden" name="datePattern" value="yyyy-MM-dd" />
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Tassonomie</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicercaLeftTassonomia" cssclass="divRicMetadatiLeftTassonomia">
					<%-- CODICE TIPO ENTE --%>
					<s:div name="divElementL1" cssclass="divRicMetadatiSingleRow">
						<s:textbox name="tassonomia_searchcodiceTipoEnteCreditore" label="Codice Tipo Ente:"
						   bmodify="true" text="${tassonomia_searchcodiceTipoEnteCreditore}"
						   validator="ignore;accept=^[\d]{2}$"
						   message="[accept=Codice Tipo Ente: Stringa numerica di lunghezza 2]"								   
						   cssclass="textboxman_smallTassonomia" cssclasslabel="label160 bold textright"/>
					</s:div>
					<%-- PROGRESSIVO MACRO AREA --%>
					<s:div name="divElementL2" cssclass="divRicMetadatiSingleRow">
						<s:textbox name="tassonomia_searchprogressivoMacroAreaPerEnteCreditore" label="Progressivo Macro Area:"
						   bmodify="true" text="${tassonomia_searchprogressivoMacroAreaPerEnteCreditore}"
						   validator="ignore;accept=^[\d]{2}$"
						   message="[accept=Progressivo Macro Area: Stringa numerica di lunghezza 2]"								   
						   cssclass="textboxman_smallTassonomia" cssclasslabel="label160 bold textright"/>
					</s:div>
					<%-- CODICE TIPOLOGIA SERVIZIO --%>
					<s:div name="divElementL3" cssclass="divRicMetadatiSingleRow">
						<s:textbox name="tassonomia_searchcodiceTipologiaServizio" label="Codice Tipipologia Servizio:"
						   bmodify="true" text="${tassonomia_searchcodiceTipologiaServizio}"
						   validator="ignore;accept=^[\d]{3}$"
						   message="[accept=Codice Tipipologia Servizio: Stringa numerica di lunghezza 3]"								   
						   cssclass="textboxman_smallTassonomia" cssclasslabel="label160 bold textright"/>
					</s:div>
					<%-- MOTIVO GIURIDICO --%>
					<s:div name="divElementL4" cssclass="divRicMetadatiSingleRow">
						<s:textbox name="tassonomia_searchmotivoGiuridicoDellaRiscossione" label="Motivo Giuridico:"
						   bmodify="true" text="${tassonomia_searchmotivoGiuridicoDellaRiscossione}"
						   validator="ignore;accept=^[0-9A-Za-z]{2}|[\-]{1}$"
						   message="[accept=Motivo Giuridico: Stringa Alfanumerica di lunghezza 2 o Stringa '-']"								   
						   cssclass="textboxman_smallTassonomia" cssclasslabel="label160 bold textright"/>
					</s:div>
					<%-- VERSIONE TASSONOMIA --%>
					<s:div name="divElementL5" cssclass="divRicMetadatiSingleRow">
						<s:textbox name="tassonomia_searchversioneTassonomia" label="Versione Tassonomia:"
						   bmodify="true" text="${tassonomia_searchversioneTassonomia}"
						   validator="ignore;accept=^[\d]{2}$"
						   message="[accept=Versione Tassonomia: Stringa numerica di lunghezza 2]"								   
						   cssclass="textboxman_smallTassonomia" cssclasslabel="label160 bold textright"/>
					</s:div>
				</s:div>
				<s:div name="divRicercaCenterRightTassonomia" cssclass="divRicMetadatiCenterRightTassonomia">
					<%-- TIPO ENTE --%>
					<s:div name="divElementCR1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tassonomia_searchtipoEnteCreditore"
							label="Tipo Ente:" maxlenght="150"
							cssclasslabel="label160 bold textright"
							cssclass="textArea150Tassonomia"
							text="${tassonomia_searchtipoEnteCreditore}" />
					</s:div>
					<%-- NOME MACRO AREA --%>
					<s:div name="divElementCR2" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tassonomia_searchnomeMacroArea"
							label="Nome Macro Area:" maxlenght="150"
							cssclasslabel="label160 bold textright"
							cssclass="textArea150Tassonomia"
							text="${tassonomia_searchnomeMacroArea}" />
					</s:div>
					<%-- TIPOLOGIA SERVIZIO --%>
					<s:div name="divElementCR3" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tassonomia_searchtipoServizio"
							label="Tipologia Servizio:" maxlenght="150"
							cssclasslabel="label160 bold textright"
							cssclass="textArea150Tassonomia"
							text="${tassonomia_searchtipoServizio}" />
					</s:div>
					<%-- PERIODO DI VALIDITA' --%>
					<s:div name="divElementCR4" cssclass="divRicMetadatiSingleRow">
						<s:div name="divElementCR41" cssclass="labelData">
							<s:label name="label_data" cssclass="seda-ui-label label160DataTassonomia bold textright"
								text="Periodo di validit&agrave;" />
						</s:div>
						<s:div name="divElementCR42" cssclass="floatleftDataTassonomia">
							<s:div name="div_dataDa" cssclass="divDataDa">
								<s:date label="Da:" prefix="dataDa" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${dataDa}"
									message="[dateISO=Da: ${msg_dataISO_valida}]">
									</s:date>
								<input type="hidden" id="dataDa_hidden" value="" />
							</s:div>
							<s:div name="div_dataA" cssclass="divDataA">
								<s:date label="A:" prefix="dataA" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${dataA}"
									message="[dateISO=A: ${msg_dataISO_valida}]">
									</s:date>
								<input type="hidden" id="dataA_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
				</s:div>
				<%--
				<s:div name="divRicMetadatiUnicaTassonomia" cssclass="divRicMetadatiUnicaTassonomia">
					< %-- TASSONOMIA PER DATI SPECIFICI INCASSO --% >
					<s:div name="divElementU1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tassonomia_searchdatiSpecificiIncasso"
							label="Tassonomia:" maxlenght="9"
							cssclasslabel="label160 bold textright"
							cssclass="textareaman95Tassonomia"
							text="${tassonomia_searchdatiSpecificiIncasso}" />
					</s:div>
					< %-- DESCRIZIONE MACRO AREA --% >
					<s:div name="divElementU2" cssclass="divRicMetadatiSingleRow">
						<s:textarea name="tassonomia_searchdescrizioneMacroArea" label="Descrizione Macro Area:" text="${tassonomia_searchdescrizioneMacroArea}"
						            bmodify="true" row="3" col="105" cssclasslabel="label85 bold textright"
							        cssclass="textArea600Tassonomia" validator="ignore;maxlength=600" />
					</s:div>
					<%-- DESCRIZIONE TIPOLOGIA SERVIZIO --% >
					<s:div name="divElementU3" cssclass="divRicMetadatiSingleRow">
						<s:textarea name="tassonomia_searchdescrizioneServizio" label="Descrizione Tipologia Servizio:" text="${tassonomia_searchdescrizioneServizio}"
						            bmodify="true" row="3" col="105" cssclasslabel="label85 bold textright" 
						            cssclass="textArea600Tassonomia" validator="ignore;maxlength=600" />
					</s:div>
				</s:div>
				--%>
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" text="Reset" type="submit" cssclass="btnStyle" validate="false" />
				<s:button id="tx_button_nuovo" onclick="" text="Nuovo" type="submit" cssclass="btnStyle" validate="false" />
			</s:div>
		</s:form>
	</s:div>
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">Elenco Tassonomie</s:div>
</s:div>
