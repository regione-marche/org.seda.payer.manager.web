<%--  **  IMPORT JAVA TAG LIB  **  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>

<%--  **  My View State  **  --%>
<m:view_state id="templatereportdinamici" encodeAttributes="true" />

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
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Template Report Dinamici</s:div>
				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
						hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
						method="post" action="templateReportDinamici.do">
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
						<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
							<s:div name="divRicercaLeft" cssclass="divRicMetadatiTopLeft">
								<c:choose>
									<c:when test="${action == 'edit' || action == 'saveedit'}">
										<input type="hidden" name="codop" value="${typeRequest.editScope}" />
										<input type="hidden" name="templatereportdinamici_chiaveTemplate" value="<c:out value="${templatereportdinamici_chiaveTemplate}"/>" />
									</c:when>
									<c:otherwise>
										<input type="hidden" name="codop" value="${typeRequest.addScope}" /> 
									</c:otherwise>
								</c:choose>
								<%-- SOCIETA --%>
								<s:dropdownlist name="templatereportdinamici_companyCode" disable="${enableListaSocieta}"
									cssclass="tbddl floatleft" label="Societ&agrave;:"
									cssclasslabel="label85 bold floatleft textright"
									cachedrowset="listaSocieta" usexml="true"
									onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
									valueselected="${templatereportdinamici_companyCode}">
									<s:ddloption text="" value=""/>
									<s:ddloption text="{2}" value="{1}" />
								</s:dropdownlist>
								<noscript>
									<s:button id="tx_button_societa_changed" 
										disable="${enableListaSocieta}" onclick="" text="" validate="false"
										type="submit" cssclass="btnimgStyle" title="Aggiorna"/>
								</noscript>
							</s:div>
							<%-- UTENTE --%>
							<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
								<s:dropdownlist name="templatereportdinamici_userCode"
									disable="${enableListaUtenti}" cssclass="tbddl floatleft"
									label="Utente:" cssclasslabel="label65 bold floatleft textright"
									cachedrowset="listaUtenti" usexml="true"
									onchange="setFiredButton('tx_button_utente_changed');this.form.submit();"
									valueselected="${templatereportdinamici_userCode}">
									<s:ddloption text="" value=""/>
									<s:ddloption text="{2}" value="{1}" />
								</s:dropdownlist>
								<noscript>
									<s:button id="tx_button_utente_changed"
										disable="${enableListaUtenti}" onclick="" text="" validate="false"
										type="submit" cssclass="btnimgStyle" title="Aggiorna"
										 />
								</noscript>
							</s:div>
							<%-- ENTE --%>
							<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
								<s:dropdownlist name="templatereportdinamici_chiaveEnte"
									disable="${enableListaEnti}" 
									cssclass="textareaman"
									cssclasslabel="label65 bold textright"
									label="Ente:" 
									cachedrowset="listaEnti" usexml="true"
									valueselected="${templatereportdinamici_chiaveEnte}">
									<s:ddloption text="" value=""/>
									<s:ddloption text="{2}" value="{1}" />
								</s:dropdownlist>
							</s:div>
						</s:div>
						<%-- TIPOLOGIA SERVIZIO --%>
						<s:div name="divRicercaLeft1" cssclass="divRicMetadatiLeft">
							<s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="templatereportdinamici_tipologiaServizio"
									disable="false" 
									cssclass="tbddl floatleft"
									cssclasslabel="label85 bold floatleft textright"
									label="Tip. Servizio:" 
									cachedrowset="listaTipologiaServizi" usexml="true"
									valueselected="${templatereportdinamici_tipologiaServizio}">
									<s:ddloption text="" value="" />
									<s:ddloption text="{2}" value="{1}" />
								</s:dropdownlist>
							</s:div>
						</s:div>
						<%-- TIPO DOCUMENTO --%>
						<s:div name="divRicercaCenter1" cssclass="divRicMetadatiCenter">
							<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="templatereportdinamici_tipoDocumento" disable="false"
									label="Tip. Doc.:"
									valueselected="${templatereportdinamici_tipoDocumento}"
									cssclass="tbddl floatleft"
									cssclasslabel="label85 bold floatleft textright">
									<s:ddloption value="D" text="Documento" />
									<s:ddloption value="B" text="Bollettino" />
									<s:ddloption value="Q" text="Quietanze" />
								</s:dropdownlist>
							</s:div>
						</s:div>
						<%-- TIPO BOLLETTINO --%>
						<s:div name="divRicercaRight1" cssclass="divRicMetadatiRight">
							<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="templatereportdinamici_tipoBollettino" disable="false" label="Tip. Bol.:"
									valueselected="${templatereportdinamici_tipoBollettino}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman">
									<s:ddloption value="" text="" />
									<s:ddloption value="PRE" text="Bollettino Premarcato" />
									<s:ddloption value="ICI" text="Bollettino ICI" />
									<s:ddloption value="CDS" text="Bollettino CDS" />
									<s:ddloption value="BOL" text="Bollettino Bollo" />
									<s:ddloption value="MAV" text="Bollettino MAV" />
									<s:ddloption value="FRE" text="Bollettino Freccia" />
									<s:ddloption value="ISC" text="Bollettino ISCOP" />
									<s:ddloption value="SPO" text="Bollettino Spontaneo" />
								</s:dropdownlist>
							</s:div>
						</s:div>
						<%-- PERIODO DI VALIDITA' --%>
						<s:div name="divRicercaLeft2" cssclass="divRicMetadatiLeft">
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
						<%-- RIFERIMENTO TEMPLATE --%>
						<s:div name="divRicercaCenter2" cssclass="divRicMetadatiCenter">
							<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" maxlenght="256" validator="required;minlength=1;maxlength=256;accept=^[\\a-zA-Z0-9:_/\\.]{1,256}$"
									label="Template:" name="templatereportdinamici_riferimentoTemplate"
									text="${templatereportdinamici_riferimentoTemplate}"
									showrequired="true"
									message="[accept=Template: Inserire un path di un template valido]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
						</s:div>
						<s:div name="button_container_var" cssclass="divRicBottoni">
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
				<s:div name="divRicercaTitleName2" cssclass="divRicTitle">Template Report Dinamici</s:div>	
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
									<s:form name="indietro" action="templateReportDinamici.do?action=search" method="post" 
											hasbtn1="false" hasbtn2="false" hasbtn3="false">
										<s:button id="tx_button_indietro" cssclass="btnStyle" type="submit" text="Indietro" onclick="" />
									</s:form>
								</s:td>
								<s:td>
									<s:form name="form_Cancella" action="templateReportDinamici.do?action=cancel&templatereportdinamici_chiaveTemplate=${templatereportdinamici_chiaveTemplate}"
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
				method="post" action="templateReportDinamici.do?action=search">
				<center>
					<c:if test="${error != null}"><s:label name="lblErrore" text="${message}"/></c:if>
					<c:if test="${error == null}"><s:label name="lblMessage" text="${message}"/></c:if>
					<s:div name="divPdf">
					<br /><br />
						<s:form name="indietro" action="templateReportDinamici.do?action=search"
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