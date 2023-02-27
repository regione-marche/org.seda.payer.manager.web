<%--  **  IMPORT JAVA TAG LIB  **  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>

<%--  **  My View State  **  --%>
<m:view_state id="tassonomia" encodeAttributes="true" />

<%--  **  My TypeRequest Bean  **  --%>
<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />

<!-- ** JQuery Functions Library ** -->
<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js" type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js" type="text/javascript"></script>

<!-- ** Page Functions ** -->
<script type="text/javascript">
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();

	function setCopia(cosa) {
		var chi1 = document.getElementById(cosa);
		var chi2 = document.getElementById(cosa + 'Hidden');
		var appo1 = chi1.value;
		chi2.value = appo1;
	}

	function setDatiSpecificiIncasso() {
		var chi1 = document.getElementById('tassonomia_codiceTipoEnteCreditore');
		var chi2 = document.getElementById('tassonomia_progressivoMacroAreaPerEnteCreditore');
		var chi3 = document.getElementById('tassonomia_codiceTipologiaServizio');
		var chi4 = document.getElementById('tassonomia_motivoGiuridicoDellaRiscossione');
		//var chi5 = document.getElementById('tassonomia_versioneTassonomia');
		var chi = document.getElementById('tassonomia_datiSpecificiIncasso');
		var appo1 = chi1.value;
		var appo2 = chi2.value;
		var appo3 = chi3.value;
		var appo4 = chi4.value;
		//var appo5 = chi5.value;
		var appo = "";
		appo1 = appo1.trim(); 
		appo2 = appo2.trim(); 
		appo3 = appo3.trim(); 
		appo4 = appo4.trim(); 
		//appo5 = appo5.trim(); 
		if(appo1 == "") {
			appo1 = ".."
		}
		if(appo2 == "") {
			appo2 = ".."
		}
		if(appo3 == "") {
			appo3 = "..."
		}
		if(appo4 == "") {
			appo4 = ".."
		} else {
			appo4 = appo4.toUpperCase();
			chi4.value = appo4;
		}
		//if(appo5 == "") {
		//	appo5 = ".."
		//}
		//appo = "9/" + appo1 + appo2 + appo3 + appo4 + appo5;
		appo = appo1 + appo2 + appo3 + appo4;
		chi.value = appo;
	}
	
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
	
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

<c:url value="" var="formParameters">
	<c:param name="test">ok</c:param>
	<c:if test="${!empty dataA_year}">  
		<c:param name="dataA_year">${dataA_year}</c:param>
	</c:if>
	<c:if test="${!empty dataA_month}">  
		<c:param name="dataA_month">${dataA_month}</c:param>
	</c:if>
	<c:if test="${!empty dataA_day}">  
		<c:param name="dataA_day">${dataA_day}</c:param>
	</c:if>
	<c:if test="${!empty dataDa_year}">  
		<c:param name="dataDa_year">${dataDa_year}</c:param>
	</c:if>
	<c:if test="${!empty dataDa_month}">  
		<c:param name="dataDa_month">${dataDa_month}</c:param>
	</c:if>
	<c:if test="${!empty dataDa_day}">  
		<c:param name="dataDa_day">${dataDa_day}</c:param>
	</c:if>
</c:url>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<c:choose>
		<c:when test="${done == null && richiestacanc == null}">
			<%-- VIEW VALIDATION ERROR MESSAGES --%>
			<c:if test="${error != null}"><br/><center><s:label name="lblErrore" text="${message}"/></center><br/></c:if>
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Tassonomia</s:div>
				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
						hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
						method="post" action="tassonomia.do">
					<s:textbox name="fired_button_hidden" label="fired_button_hidden" bmodify="true" text="" cssclass="display_none" cssclasslabel="display_none" />
					<input type="hidden" name="datePattern" value="yyyy-MM-dd" />
					<%-- CHECK ACTION TYPE --%>
					<c:choose>
						<c:when test="${action == 'saveedit'}">
							<input type="hidden" name="action" value="<c:out value="${action}"/>" />
						</c:when>
						<c:when test="${action == 'saveadd'}">
							<input type="hidden" name="action" value="<c:out value="${action}"/>" />
						</c:when>	
						<c:otherwise>
							<input type="hidden" name="action" value="save<c:out value="${action}"/>" />
						</c:otherwise>
					</c:choose>
					<s:div name="divRicercaTitleName1" cssclass="divRicMetadati">
						<c:choose>
							<c:when test="${action == 'edit' || action == 'saveedit'}">
								<input type="hidden" name="codop" value="${typeRequest.editScope}" />
								<input type="hidden" name="tassonomia_chiaveTassonomia" value="<c:out value="${tassonomia_chiaveTassonomia}"/>" />
							</c:when>
							<c:otherwise>
								<input type="hidden" name="codop" value="${typeRequest.addScope}" /> 
							</c:otherwise>
						</c:choose>
						<s:div name="divRicercaLeftTassonomia" cssclass="divRicMetadatiLeftTassonomia">
							<%-- CODICE TIPO ENTE --%>
							<s:div name="divElementL1" cssclass="divRicMetadatiSingleRow">
								<label class="seda-ui-label label175 bold textright" for="tassonomia_codiceTipoEnteCreditore">Codice Tipo Ente:<strong title="Richiesto" class="seda-ui-required">*</strong></label>
								<input type="text" id="tassonomia_codiceTipoEnteCreditore" name="tassonomia_codiceTipoEnteCreditore" class="seda-ui-textarea textboxman_smallTassonomia"
									   onchange="setDatiSpecificiIncasso();setCopia('tassonomia_codiceTipoEnteCreditore');" 
								       maxlength="2" value="${tassonomia_codiceTipoEnteCreditore}" />
								       
								<s:textbox name="tassonomia_codiceTipoEnteCreditoreHidden" label="Codice Tipo Ente:"
								   bmodify="true" text="${tassonomia_codiceTipoEnteCreditore}"
								   validator="required;accept=^[\d]{2}$"
								   message="[accept=Codice Tipo Ente: Stringa numerica di lunghezza 2]"								   
								   cssclass="display_none" cssclasslabel="display_none"  />
							</s:div>
							<%-- PROGRESSIVO MACRO AREA --%>
							<s:div name="divElementL2" cssclass="divRicMetadatiSingleRow">
								<label class="seda-ui-label label175 bold textright" for="tassonomia_progressivoMacroAreaPerEnteCreditore">Progressivo Macro Area:<strong title="Richiesto" class="seda-ui-required">*</strong></label>
								<input type="text" id="tassonomia_progressivoMacroAreaPerEnteCreditore" name="tassonomia_progressivoMacroAreaPerEnteCreditore" class="seda-ui-textarea textboxman_smallTassonomia"
									   onchange="setDatiSpecificiIncasso();setCopia('tassonomia_progressivoMacroAreaPerEnteCreditore');" 
								       maxlength="2" value="${tassonomia_progressivoMacroAreaPerEnteCreditore}" />
								       
								<s:textbox name="tassonomia_progressivoMacroAreaPerEnteCreditoreHidden" label="Progressivo Macro Area:"
								   bmodify="true" text="${tassonomia_progressivoMacroAreaPerEnteCreditore}"
								   validator="required;accept=^[\d]{2}$"
								   message="[accept=Progressivo Macro Area: Stringa numerica di lunghezza 2]"								   
								   cssclass="display_none" cssclasslabel="display_none"  />
							</s:div>
							<%-- CODICE TIPOLOGIA SERVIZIO --%>
							<s:div name="divElementL3" cssclass="divRicMetadatiSingleRow">
								<label class="seda-ui-label label175 bold textright" for="tassonomia_codiceTipologiaServizio">Codice Tipipologia Servizio:<strong title="Richiesto" class="seda-ui-required">*</strong></label>
								<input type="text" id="tassonomia_codiceTipologiaServizio" name="tassonomia_codiceTipologiaServizio" class="seda-ui-textarea textboxman_smallTassonomia"
									   onchange="setDatiSpecificiIncasso();setCopia('tassonomia_codiceTipologiaServizio');" 
								       maxlength="3" value="${tassonomia_codiceTipologiaServizio}" />

								<s:textbox name="tassonomia_codiceTipologiaServizioHidden" label="Codice Tipipologia Servizio:"
								   bmodify="true" text="${tassonomia_codiceTipologiaServizio}"
								   validator="required;accept=^[\d]{3}$"
								   message="[accept=Codice Tipipologia Servizio: Stringa numerica di lunghezza 3]"								   
								   cssclass="display_none" cssclasslabel="display_none"  />
							</s:div>
							<%-- MOTIVO GIURIDICO --%>
							<s:div name="divElementL4" cssclass="divRicMetadatiSingleRow">
								<label class="seda-ui-label label175 bold textright" for="tassonomia_motivoGiuridicoDellaRiscossione">Motivo Giuridico:<strong title="Richiesto" class="seda-ui-required">*</strong></label>
								<input type="text" id="tassonomia_motivoGiuridicoDellaRiscossione" name="tassonomia_motivoGiuridicoDellaRiscossione" class="seda-ui-textarea textboxman_smallTassonomia"
									   onchange="setDatiSpecificiIncasso();setCopia('tassonomia_motivoGiuridicoDellaRiscossione');" 
								       maxlength="2" value="${tassonomia_motivoGiuridicoDellaRiscossione}" />

								<s:textbox name="tassonomia_motivoGiuridicoDellaRiscossioneHidden" label="Motivo Giuridico:"
								   bmodify="true" text="${tassonomia_motivoGiuridicoDellaRiscossione}"
								   validator="required;accept=^[0-9A-Z]{2}$"
								   message="[accept=Motivo Giuridico: Stringa Alfanumerica di lunghezza 2]"								   
								   cssclass="display_none" cssclasslabel="display_none"  />
							</s:div>
							<%-- VERSIONE TASSONOMIA --%>
							<s:div name="divElementL5" cssclass="divRicMetadatiSingleRow">
								<label class="seda-ui-label label175 bold textright" for="tassonomia_versioneTassonomia">Versione Tassonomia:<strong title="Richiesto" class="seda-ui-required">*</strong></label>
								<input type="text" id="tassonomia_versioneTassonomia" name="tassonomia_versioneTassonomia" class="seda-ui-textarea textboxman_smallTassonomia"
									   onchange="setDatiSpecificiIncasso();setCopia('tassonomia_versioneTassonomia');" 
								       maxlength="2" value="${tassonomia_versioneTassonomia}" />

								<s:textbox name="tassonomia_versioneTassonomiaHidden" label="Versione Tassonomia:"
								   bmodify="true" text="${tassonomia_versioneTassonomia}"
								   validator="required;accept=^[\d]{2}$"
								   message="[accept=Versione Tassonomia: Stringa numerica di lunghezza 2]"								   
								   cssclass="display_none" cssclasslabel="display_none"  />
							</s:div>
						</s:div>
						<s:div name="divRicercaCenterRightTassonomia" cssclass="divRicMetadatiCenterRightTassonomia">
							<%-- TIPO ENTE --%>
							<s:div name="divElementCR1" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" name="tassonomia_tipoEnteCreditore"
									validator="required" showrequired="true"
									label="Tipo Ente:" maxlenght="150"
									cssclasslabel="label160 bold textright"
									cssclass="textArea150Tassonomia"
									text="${tassonomia_tipoEnteCreditore}" />
							</s:div>
							<%-- NOME MACRO AREA --%>
							<s:div name="divElementCR2" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" name="tassonomia_nomeMacroArea"
									validator="required" showrequired="true"
									label="Nome Macro Area:" maxlenght="150"
									cssclasslabel="label160 bold textright"
									cssclass="textArea150Tassonomia"
									text="${tassonomia_nomeMacroArea}" />
							</s:div>
							<%-- TIPOLOGIA SERVIZIO --%>
							<s:div name="divElementCR3" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" name="tassonomia_tipoServizio"
									validator="required" showrequired="true"
									label="Tipologia Servizio:" maxlenght="150"
									cssclasslabel="label160 bold textright"
									cssclass="textArea150Tassonomia"
									text="${tassonomia_tipoServizio}" />
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
											validator="required;dateISO" showrequired="true"
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
											validator="required;dateISO" showrequired="true"
											message="[dateISO=A: ${msg_dataISO_valida}]">
											</s:date>
										<input type="hidden" id="dataA_hidden" value="" />
									</s:div>
								</s:div>
							</s:div>
						</s:div>
						<s:div name="divElementUnicaTassonomia" cssclass="divRicMetadatiUnicaTassonomia">
							<%-- TASSONOMIA PER DATI SPECIFICI INCASSO --%>
							<s:div name="divElementU1" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="false" name="tassonomia_datiSpecificiIncasso"
									label="Tassonomia:" maxlenght="9"
									cssclasslabel="label175 bold textright"
									cssclass="textareaman95Tassonomia colordisabled"
									text="${tassonomia_datiSpecificiIncasso}" />
							</s:div>
							<%-- DESCRIZIONE MACRO AREA --%>
							<s:div name="divElementU2" cssclass="divRicMetadatiSingleRow">
								<s:textarea name="tassonomia_descrizioneMacroArea" label="Descrizione Macro Area:" text="${tassonomia_descrizioneMacroArea}"
								            bmodify="true" row="3" col="105" cssclasslabel="label85 bold textright"
									        cssclass="textArea600Tassonomia" validator="ignore;maxlength=600" />
							</s:div>
							<%-- DESCRIZIONE TIPOLOGIA SERVIZIO --%>
							<s:div name="divElementU3" cssclass="divRicMetadatiSingleRow">
								<s:textarea name="tassonomia_descrizioneServizio" label="Descrizione Tipologia Servizio:" text="${tassonomia_descrizioneServizio}"
								            bmodify="true" row="3" col="105" cssclasslabel="label85 bold textright" 
								            cssclass="textArea600Tassonomia" validator="ignore;maxlength=600" />
							</s:div>
						</s:div>
						<s:div name="button_container_var" cssclass="divRicBottoniTassonomia">
							<s:button id="tx_button_indietro" type="submit" text="Indietro" onclick="" cssclass="btnStyle" validate="false" />
							<s:button id="tx_button_reset" onclick="" text="Reset" type="submit" cssclass="btnStyle" validate="false"/>
							<s:button id="save_btn" type="submit" text="Salva" onclick="" cssclass="btnStyle" />
						</s:div>
					</s:div>
				</s:form>
			</s:div>
		</c:when>

		<%-- ACTION - RICHIESTA CANCELLAZIONE --%>
		<c:when test="${richiestacanc != null}">
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">
				<s:div name="divRicercaTitleName2" cssclass="divRicTitle">Tassonomia</s:div>	
				<s:label name="lblCanc" text="${message}" cssclass="lblMessage" />
				<s:label name="lblCancmsg" text="Sei sicuro di voler cancellare il record selezionato?" cssclass="lblMessage" />
				<br />
				<br />
				<center>
					<s:table border="0" cssclass="container_btn">
						<s:thead>
							<s:tr>
								<s:td></s:td>
							</s:tr>
						</s:thead>
						<s:tbody>
							<s:tr>
								<s:td>
									<s:form name="indietro" action="tassonomia.do?action=search" method="post" 
											hasbtn1="false" hasbtn2="false" hasbtn3="false">
										<s:button id="tx_button_indietro" cssclass="btnStyle" type="submit" text="Indietro" onclick="" />
									</s:form>
								</s:td>
								<s:td>
									<s:form name="form_Cancella" action="tassonomia.do?action=cancel&tassonomia_chiaveTassonomia=${tassonomia_chiaveTassonomia}"
											method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
										<input type="hidden" name="action" value="canc" />
										<s:button id="canc" onclick="" text="Cancella" type="submit" cssclass="btnStyle" />
									</s:form>
								</s:td>
							</s:tr>
						</s:tbody>
					</s:table>
				</center>
			</s:div>
		</c:when>
		<c:otherwise>
			<%-- ESITO VARIAZIONE --%>
			<s:form name="frmConfirm" hasbtn3="false" hasbtn2="false"
				hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
				method="post" action="tassonomia.do?action=search">
				<center>
					<c:if test="${error != null}"><s:label name="lblErrore" text="${message}"/></c:if>
					<c:if test="${error == null}"><s:label name="lblMessage" text="${message}"/></c:if>
					<s:div name="divPdf">
					<br /><br />
						<s:form name="indietro" action="tassonomia.do?action=search"
							method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
							<s:button id="tx_button_indietro" onclick="" text="Indietro"
								cssclass="btnStyle" type="submit" />
						</s:form>
					</s:div>
				</center>
			</s:form>
		</c:otherwise>
	</c:choose>
</s:div>