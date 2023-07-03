<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="viewstate" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>

<script type="text/javascript">
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();
	$(function() {
		$.datepicker.setDefaults($.datepicker.regional["it"]);
		$("#dtFlusso_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy"); 
		$("#dtFlusso_da_hidden").datepicker(
						{
							minDate : new Date(annoDa, 0, 1),
							maxDate : new Date(annoA, 11, 31),
							yearRange : annoDa + ":" + annoA,
							showOn : "button",
							buttonImage : "../applications/templates/shared/img/calendar.gif",
							buttonImageOnly : true,
							onSelect : function(dateText, inst) {
								$("#dtFlusso_da_day_id").val(dateText.substr(0, 2));
								$("#dtFlusso_da_month_id").val(dateText.substr(3, 2));
								$("#dtFlusso_da_year_id").val(dateText.substr(6, 4));
							},
							beforeShow : function(input, inst) {
								//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
							updateValoreDatePickerFromDdl(
									"dtFlusso_da_day_id",
									"dtFlusso_da_month_id",
									"dtFlusso_da_year_id",
									"dtFlusso_da_hidden");
						}
						});
		$("#dtFlusso_a_hidden").datepicker("option", "dateFormat","dd/mm/yyyy");
		$("#dtFlusso_a_hidden").datepicker(
						{
							minDate : new Date(annoDa, 0, 1),
							maxDate : new Date(annoA, 11, 31),
							yearRange : annoDa + ":" + annoA,
							showOn : "button",
							buttonImage : "../applications/templates/shared/img/calendar.gif",
							buttonImageOnly : true,
							onSelect : function(dateText, inst) {
								$("#dtFlusso_a_day_id").val(dateText.substr(0, 2));
								$("#dtFlusso_a_month_id").val(dateText.substr(3, 2));
								$("#dtFlusso_a_year_id").val(dateText.substr(6, 4));
							},
							beforeShow : function(input, inst) {
								//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
							updateValoreDatePickerFromDdl(
									"dtFlusso_a_day_id",
									"dtFlusso_a_month_id",
									"dtFlusso_a_year_id",
									"dtFlusso_a_hidden");
						}
						});
		$("#dtregol_da_hidden").datepicker("option", "dateFormat","dd/mm/yyyy");
		$("#dtregol_da_hidden").datepicker(
						{
							minDate : new Date(annoDa, 0, 1),
							maxDate : new Date(annoA, 11, 31),
							yearRange : annoDa + ":" + annoA,
							showOn : "button",
							buttonImage : "../applications/templates/shared/img/calendar.gif",
							buttonImageOnly : true,
							onSelect : function(dateText, inst) {
								$("#dtregol_da_day_id").val(dateText.substr(0, 2));
								$("#dtregol_da_month_id").val(dateText.substr(3, 2));
								$("#dtregol_da_year_id").val(dateText.substr(6, 4));
							},
							beforeShow : function(input, inst) {
								//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
							updateValoreDatePickerFromDdl(
									"dtregol_da_day_id",
									"dtregol_da_month_id",
									"dtregol_da_year_id",
									"dtregol_da_hidden");
						}
						});
		$("#dtregol_a_hidden").datepicker("option", "dateFormat","dd/mm/yyyy");
		$("#dtregol_a_hidden").datepicker(
						{
							minDate : new Date(annoDa, 0, 1),
							maxDate : new Date(annoA, 11, 31),
							yearRange : annoDa + ":" + annoA,
							showOn : "button",
							buttonImage : "../applications/templates/shared/img/calendar.gif",
							buttonImageOnly : true,
							onSelect : function(dateText, inst) {
								$("#dtregol_a_day_id").val(dateText.substr(0, 2));
								$("#dtregol_a_month_id").val(dateText.substr(3, 2));
								$("#dtregol_a_year_id").val(dateText.substr(6, 4));
							},
							beforeShow : function(input, inst) {
								//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
							updateValoreDatePickerFromDdl(
									"dtregol_a_day_id",
									"dtregol_a_month_id",
									"dtregol_a_year_id",
									"dtregol_a_hidden");
						}
						});
		$("#dtChiusuraFlusso_da_hidden").datepicker("option", "dateFormat","dd/mm/yyyy");
		$("#dtChiusuraFlusso_da_hidden").datepicker(
						{
							minDate : new Date(annoDa, 0, 1),
							maxDate : new Date(annoA, 11, 31),
							yearRange : annoDa + ":" + annoA,
							showOn : "button",
							buttonImage : "../applications/templates/shared/img/calendar.gif",
							buttonImageOnly : true,
							onSelect : function(dateText, inst) {
								$("#dtChiusuraFlusso_da_day_id").val(dateText.substr(0, 2));
								$("#dtChiusuraFlusso_da_month_id").val(dateText.substr(3, 2));
								$("#dtChiusuraFlusso_da_year_id").val(dateText.substr(6, 4));
							},
							beforeShow : function(input, inst) {
								//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
							updateValoreDatePickerFromDdl(
									"dtChiusuraFlusso_da_day_id",
									"dtChiusuraFlusso_da_month_id",
									"dtChiusuraFlusso_da_year_id",
									"dtChiusuraFlusso_da_hidden");
						}
						});
		$("#dtChiusuraFlusso_a_hidden").datepicker("option", "dateFormat","dd/mm/yyyy");
		$("#dtChiusuraFlusso_a_hidden").datepicker(
						{
							minDate : new Date(annoDa, 0, 1),
							maxDate : new Date(annoA, 11, 31),
							yearRange : annoDa + ":" + annoA,
							showOn : "button",
							buttonImage : "../applications/templates/shared/img/calendar.gif",
							buttonImageOnly : true,
							onSelect : function(dateText, inst) {
								$("#dtChiusuraFlusso_a_day_id").val(dateText.substr(0, 2));
								$("#dtChiusuraFlusso_a_month_id").val(dateText.substr(3, 2));
								$("#dtChiusuraFlusso_a_year_id").val(dateText.substr(6, 4));
							},
							beforeShow : function(input, inst) {
								//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
							updateValoreDatePickerFromDdl(
									"dtChiusuraFlusso_a_day_id",
									"dtChiusuraFlusso_a_month_id",
									"dtChiusuraFlusso_a_year_id",
									"dtChiusuraFlusso_a_hidden");
						}
						});
	});

	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}

	function setDdl(value) {
		var txtHidden = document.getElementById('DDLChanged');
		if (txtHidden != null)
			txtHidden.value = value;
	}

	function callSubmit(frm1) {
		frm1.submit();
	}

	function getCSSRule(ruleName, deleteFlag) { // Return requested style obejct
		ruleName = ruleName.toLowerCase(); // Convert test string to lower case.
		if (document.styleSheets) { // If browser can play with stylesheets
			for ( var i = 0; i < document.styleSheets.length; i++) { // For each stylesheet
				var styleSheet = document.styleSheets[i]; // Get the current Stylesheet
				var ii = 0; // Initialize subCounter.
				var cssRule = false; // Initialize cssRule. 
				do { // For each rule in stylesheet
					if (styleSheet.cssRules) { // Browser uses cssRules?
						cssRule = styleSheet.cssRules[ii]; // Yes --Mozilla Style
					} else { // Browser usses rules?
						cssRule = styleSheet.rules[ii]; // Yes IE style. 
					} // End IE check.
					if (cssRule) { // If we found a rule...
						if (cssRule.selectorText.toLowerCase() == ruleName) { //  match ruleName?
							if (deleteFlag == 'delete') { // Yes.  Are we deleteing?
								if (styleSheet.cssRules) { // Yes, deleting...
									styleSheet.deleteRule(ii); // Delete rule, Moz Style
								} else { // Still deleting.
									styleSheet.removeRule(ii); // Delete rule IE style.
								} // End IE check.
								return true; // return true, class deleted.
							} else { // found and not deleting.
								return cssRule; // return the style object.
							} // End delete Check
						} // End found rule name
					} // end found cssRule
					ii++; // Increment sub-counter
				} while (cssRule); // end While loop
			} // end For loop
		} // end styleSheet ability check
		return false; // we found NOTHING!
	} // end getCSSRule 

	function killCSSRule(ruleName) { // Delete a CSS rule   
		return getCSSRule(ruleName, 'delete'); // just call getCSSRule w/delete flag.
	} // end killCSSRule

	function addCSSRule(ruleName) { // Create a new css rule
		if (document.styleSheets) { // Can browser do styleSheets?
			if (!getCSSRule(ruleName)) { // if rule doesn't exist...
				if (document.styleSheets[0].addRule) { // Browser is IE?
					document.styleSheets[0].addRule(ruleName, null, 0); // Yes, add IE style
				} else { // Browser is IE?
					document.styleSheets[0].insertRule(ruleName + ' { }', 0); // Yes, add Moz style.
				} // End browser check
			} // End already exist check.
		} // End browser ability check.
		return getCSSRule(ruleName); // return rule we just created.
	}

	/*
	function switchVisibility() {     
	   var rule =  getCSSRule('.display_none');
	   if (rule.style.display == 'none') {
		   rule.style.display = 'inline';
	   } else {
	   	rule.style.display = 'none';
	   }
	}   
	 */
</script>

<c:url value="" var="formParameters">
	<c:if test="${!empty param.pageNumber}">
		<c:param name="pageNumber_hidden">${param.pageNumber}</c:param>
	</c:if>
	<c:if test="${!empty rowsPerPage}">
		<c:param name="rowsPerPage_hidden">${param.rowsPerPage}</c:param>
	</c:if>
	<c:if test="${!empty orderBy}">
		<c:param name="orderBy_hidden">${param.orderBy}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_societa}">
		<c:param name="tx_societa">${param.tx_societa}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_provincia}">
		<c:param name="tx_provincia">${param.tx_provincia}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_utente}">
		<c:param name="tx_utente">${param.tx_utente}</c:param>
	</c:if>
	<c:if test="${!empty param.statoSquadratura}">
		<c:param name="statoSquadratura">${param.statoSquadratura}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_tipo_carta}">
		<c:param name="tx_tipo_carta">${param.tx_tipo_carta}</c:param>
	</c:if>

	<c:if test="${!empty param.idFlusso}">
		<c:param name="idFlusso">${param.idFlusso}</c:param>
	</c:if>

	<c:if test="${!empty param.dtFlusso_da_day}">
		<c:param name="dtFlusso_da_day">${param.dtFlusso_da_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtFlusso_da_month}">
		<c:param name="dtFlusso_da_month">${param.dtFlusso_da_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtFlusso_da_year}">
		<c:param name="dtFlusso_da_year">${param.dtFlusso_da_year}</c:param>
	</c:if>
	<c:if test="${!empty param.dtFlusso_a_day}">
		<c:param name="dtFlusso_a_day">${param.dtFlusso_a_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtFlusso_a_month}">
		<c:param name="dtFlusso_a_month">${param.dtFlusso_a_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtFlusso_a_year}">
		<c:param name="dtFlusso_a_year">${param.dtFlusso_a_year}</c:param>
	</c:if>

	<c:if test="${!empty param.versOggetto}">
		<c:param name="versOggetto">${param.versOggetto}</c:param>
	</c:if>

	<c:if test="${!empty param.statoSquadratura}">
		<c:param name="statoSquadratura">${param.statoSquadratura}</c:param>
	</c:if>

	<c:if test="${!empty param.impPagamento_da}">
		<c:param name="impPagamento_da">${param.impPagamento_da}</c:param>
	</c:if>
	<c:if test="${!empty param.impPagamento_a}">
		<c:param name="impPagamento_a">${param.impPagamento_a}</c:param>
	</c:if>

	<c:if test="${!empty param.dtregol_da_day}">
		<c:param name="dtregol_da_day">${param.dtregol_da_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtregol_da_month}">
		<c:param name="dtregol_da_month">${param.dtregol_da_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtregol_da_year}">
		<c:param name="dtregol_da_year">${param.dtregol_da_year}</c:param>
	</c:if>
	<c:if test="${!empty param.dtregol_a_day}">
		<c:param name="dtregol_a_day">${param.dtregol_a_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtregol_a_month}">
		<c:param name="dtregol_a_month">${param.dtregol_a_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtregol_a_year}">
		<c:param name="dtregol_a_year">${param.dtregol_a_year}</c:param>
	</c:if>

	<c:if test="${!empty param.keyQuadratura}">
		<c:param name="keyQuadratura">${param.keyQuadratura}</c:param>
	</c:if>

	<c:if test="${!empty param.nomeFileTxt}">
		<c:param name="nomeFileTxt">${param.nomeFileTxt}</c:param>
	</c:if>

	<c:if test="${!empty param.dtChiusuraFlusso_da_day}">
		<c:param name="dtChiusuraFlusso_da_day">${param.dtChiusuraFlusso_da_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtChiusuraFlusso_da_month}">
		<c:param name="dtChiusuraFlusso_da_month">${param.dtChiusuraFlusso_da_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtChiusuraFlusso_da_year}">
		<c:param name="dtChiusuraFlusso_da_year">${param.dtChiusuraFlusso_da_year}</c:param>
	</c:if>
	<c:if test="${!empty param.dtChiusuraFlusso_a_day}">
		<c:param name="dtChiusuraFlusso_a_day">${param.dtChiusuraFlusso_a_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtChiusuraFlusso_a_month}">
		<c:param name="dtChiusuraFlusso_a_month">${param.dtChiusuraFlusso_a_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtChiusuraFlusso_a_year}">
		<c:param name="dtChiusuraFlusso_a_year">${param.dtChiusuraFlusso_a_year}</c:param>
	</c:if>

	<c:if test="${!empty ext}">
		<c:param name="ext">${ext}</c:param>
	</c:if>
	
	<c:if test="${!empty totMov}">
		<c:param name="totMov">${totMov}</c:param>
	</c:if>
	<c:if test="${!empty totImpMov}">
		<c:param name="totImpMov">${totImpMov}</c:param>
	</c:if>
	<c:if test="${!empty totTran}">
		<c:param name="totTran">${totTran}</c:param>
	</c:if>
	<c:if test="${!empty totImpQun}">
		<c:param name="totImpQun">${totImpQun}</c:param>
	</c:if>
</c:url>


<s:div name="divSelezione1" cssclass="divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="riconciliazioneTransazioniNodoForm"
			action="riconciliazioneTransazioniNodo.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:textbox name="DDLChanged" label="DDLChanged" bmodify="true"
				text="" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="ext" label="ext" bmodify="true" text="${ext}"
				cssclass="display_none" cssclasslabel="display_none" />

			<s:textbox name="hRowsPerPage" label="rowsPerPageRic" bmodify="true"
				text="${rowsPerPage}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="hPageNumber" label="pageNumberRic" bmodify="true"
				text="${pageNumber}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="hOrder" label="orderRic" bmodify="true"
				text="${order}" cssclass="display_none" cssclasslabel="display_none" />

			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
				Ricerca Movimenti Nodo Nazionale Pagamenti
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:dropdownlist name="tx_societa" disable="${societaDdlDisabled}"
							cssclass="tbddl floatleft" label="Societ&aacute;:"
							cssclasslabel="label85 bold floatleft textright"
							cachedrowset="listaSocieta" usexml="true"
							onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
							valueselected="${tx_societa}">
							<s:ddloption text="Tutte le Societ&agrave;" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<!-- onchange="setDdl('societa'); callSubmit(this.form);" -->
						<noscript><s:button id="tx_button_societa_changed"
							type="submit" disable="${societaDdlDisabled}" text="" onclick=""
							cssclass="btnimgStyle" title="Aggiorna" validate="false" /></noscript>
					</s:div>

					
					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
						<s:dropdownlist name="tx_provincia"
							disable="${provinciaDdlDisabled}" cssclass="tbddl floatleft"
							label="Provincia:"
							cssclasslabel="label65 bold floatleft textright"
							cachedrowset="listaProvince" usexml="true"
							onchange="setFiredButton('tx_button_provincia_changed');this.form.submit();"
							valueselected="${tx_provincia}">
							<s:ddloption text="Tutte le Province" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<!-- onchange="setDdl('provincia'); callSubmit(this.form);" -->
						<noscript><s:button id="tx_button_provincia_changed"
							type="submit" text="" onclick="" cssclass="btnimgStyle"
							title="Aggiorna" validate="false" /></noscript>
					</s:div>
<%--
					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:dropdownlist name="tx_utente" disable="${utenteDdlDisabled}"
							cssclass="tbddlMax floatleft" label="Utente:"
							cssclasslabel="label65 bold textright" cachedrowset="listaUtenti"
							usexml="true"
							onchange="setFiredButton('tx_button_utente_changed');this.form.submit();"
							valueselected="${tx_utente}">
							<s:ddloption text="Tutti gli Utenti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript><s:button id="tx_button_utente_changed"
							type="submit" text="" onclick="" cssclass="btnimgStyle"
							title="Aggiorna" validate="false" /></noscript>
					</s:div>
					 --%>
					 <s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:dropdownlist name="tx_UtenteEnte"
							disable="${ddlUtenteEnteDisabled}" cssclass="tbddlMax floatleft"
							label="Ente:" cssclasslabel="label65 bold textright"
							cachedrowset="listaUtentiEnti" usexml="true"
							valueselected="${tx_UtenteEnte}">
							<s:ddloption text="Tutti gli Enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore" maxlenght="35"
							cssclass="textareaman" cssclasslabel="label85 bold textright"
							bmodify="true" name="idFlusso" label="Id. Flusso:"
							text="${idFlusso}" />
					</s:div>

					<s:div name="divElement12" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement12a" cssclass="labelData">
							<s:label name="label_data_creazione" 
								cssclass="seda-ui-label label78 bold textright"
								text="Data Flusso" />
						</s:div>

						<s:div name="divElement12b" cssclass="floatleft">
							<s:div name="divDtFlussoDA" cssclass="divDataDa">
								<s:date label="Da:" cssclasslabel="labelsmall"
									cssclass="dateman" prefix="dtFlusso_da"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
									locale="IT-it" descriptivemonth="false" separator="/"
									calendar="${dtFlusso_da}">
								</s:date>
								<input type="hidden" id="dtFlusso_da_hidden" value="" />
							</s:div>

							<s:div name="divDtFlussoA" cssclass="divDataA">
								<s:date label="A:" cssclasslabel="labelsmall" cssclass="dateman"
									prefix="dtFlusso_a" yearbegin="${ddlDateAnnoDa}"
									yearend="${ddlDateAnnoA}" locale="IT-it"
									descriptivemonth="false" separator="/"
									calendar="${dtFlusso_a}"></s:date>
								<input type="hidden" id="dtFlusso_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
					
					<s:div name="divElement14f" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;accept=^(?:(?!').)*$;maxlength=64" bmodify="true"
							name="chiaveTransazione" label="Id Transazione:"
							text="${chiaveTransazione}" maxlenght="64" cssclass="textareaman"
							cssclasslabel="label85 bold textright" />
					</s:div>

				</s:div>


				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">

					<%-- L'importo nella PYQUNTB non è presente
					<s:div name="divElement10" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement10a" cssclass="labelData">
							<s:label name="label_importo"
								cssclass="seda-ui-label label85 bold textright" text="Importo" />
						</s:div>
						<s:div name="divElement10b" cssclass="floatleft">
							<s:div name="divImportoDa" cssclass="divDataDa">
								<s:textbox validator="ignore;numberIT;maxlength=15"
									maxlenght="15" bmodify="true" name="impPagamento_da"
									label="Da:" text="${impPagamento_da}"
									cssclass="textareamanImporto" cssclasslabel="labelsmall" />
							</s:div>

							<s:div name="divImportoA" cssclass="divDataA">
								<s:textbox validator="ignore;numberIT;maxlength=15"
									maxlenght="15" bmodify="true" name="impPagamento_a"
									label="A:" text="${impPagamento_a}"
									cssclass="textareamanImporto" cssclasslabel="labelsmall" />
							</s:div>
						</s:div>
					</s:div>
					 --%>

					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="statoSquadratura" cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright" disable="false"
							label="Mostra:" valueselected="${statoSquadratura}">
							<s:ddloption value="" text="Tutti i movimenti del Nodo" />
							<s:ddloption value="C" text="Movimenti Nodo riconciliati" />
							<s:ddloption value="A" text="Movimenti Nodo non riconciliati" />
						</s:dropdownlist>
					</s:div>
<!-- 
					<s:div name="divElement12" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement12a" cssclass="labelData">
							<s:label name="label_dtRegol"
								cssclass="seda-ui-label label78 bold textright"
								text="Data Regolamento" />
						</s:div>

						<s:div name="divElement12b" cssclass="floatleft">
							<s:div name="divDtRegolDA" cssclass="divDataDa">
								<s:date label="Da:" cssclasslabel="labelsmall"
									cssclass="dateman" prefix="dtregol_da"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
									locale="IT-it" descriptivemonth="false" separator="/"
									calendar="${dtregol_da}">
								</s:date>
								<input type="hidden" id="dtregol_da_hidden" value="" />
							</s:div>

							<s:div name="divDtRegolA" cssclass="divDataA">
								<s:date label="A:" cssclasslabel="labelsmall" cssclass="dateman"
									prefix="dtregol_a" yearbegin="${ddlDateAnnoDa}"
									yearend="${ddlDateAnnoA}" locale="IT-it"
									descriptivemonth="false" separator="/"
									calendar="${dtregol_a}"></s:date>
								<input type="hidden" id="dtregol_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
 -->
				</s:div>

				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					
					<s:div name="divElement14" cssclass="divRicMetadatiSingleRow">
						<%-- inizio LP PG200200 --%>
						<%--
						<s:textbox validator="maxlength=20" maxlenght="20"
							cssclass="textareaman" cssclasslabel="label85 bold textright"
							bmodify="true" name="nomeFileTxt" label="Supporto:"
							text="${nomeFileTxt}" />
						 --%>
						<s:textbox validator="maxlength=100" maxlenght="100"
							cssclass="textareaman" cssclasslabel="label85 bold textright"
							bmodify="true" name="nomeFileTxt" label="Supporto:"
							text="${nomeFileTxt}" />
						<%-- fine LP PG200200 --%>
					</s:div>

					<s:div name="divElement14b" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;digits;maxlength=20" bmodify="true"
							name="keyQuadratura" label="Id Quadrat.:"
							text="${keyQuadratura}" maxlenght="20" cssclass="textareaman"
							cssclasslabel="label85 bold textright" />
					</s:div>
					<%-- inizio LP PG200200 --%>
					<s:div name="divElement14c" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tipologiaFlusso" cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright" disable="false"
							label="Tp. Flusso:" valueselected="${tipologiaFlusso}">
							<s:ddloption value="" text="Tutte le tipologia" />
							<s:ddloption value="POS" text="POS" />
						</s:dropdownlist>
					</s:div>

					<s:div name="divElement14d" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="scartateFlusso" cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright" disable="false"
							label="Scartate:" valueselected="${scartateFlusso}">
							<s:ddloption value="" text="Tutte" />
							<s:ddloption value="Y" text="Si" />
							<s:ddloption value="N" text="No" />
						</s:dropdownlist>
					</s:div>
					<%-- fine LP PG200200 --%>
				</s:div>

			</s:div>

			<s:div name="divCentered0" cssclass="divRicBottoni">

				<s:button id="tx_button_cerca" type="submit" text="Cerca" onclick=""
					cssclass="btnStyle" />
				<s:button id="tx_button_reset" type="submit" text="Reset" onclick=""
					cssclass="btnStyle" />

				<%--
				<c:if test="${!empty listaMovimenti}">
					<s:button id="tx_button_stampa" type="submit" text="Stampa"
						onclick="" cssclass="btnStyle" />
				</c:if>
				 --%>

				<c:if test="${!empty listaMovimenti && ext=='0'}">
					<s:button id="tx_button_cerca_exp" type="submit" text="Espandi"
						onclick="" cssclass="btnStyle" />
				</c:if>
				<c:if test="${!empty listaMovimenti && ext=='1'}">
					<s:button id="tx_button_cerca_exp" type="submit" text="Contrai"
						onclick="" cssclass="btnStyle" />
				</c:if>
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
			</s:div>
		</s:form>
	</s:div>

</s:div>

<c:if test="${!empty listaMovimenti}">
	<fmt:setLocale value="it_IT" />
	<s:div name="divCentered" cssclass="divRicercaFill bold">
		Elenco Movimenti Nodo Nazionale Pagamenti
		<!--<s:table border="0" cellpadding="0" cellspacing="0">
			<s:thead>
				<s:tr><s:th>Elenco Flussi Pagamenti</s:th></s:tr>
			</s:thead>
			<s:tbody><s:tr><s:td></s:td></s:tr></s:tbody>
		</s:table>-->
	</s:div>


	<s:datagrid viewstate="" cachedrowset="listaMovimenti"
		action="riconciliazioneTransazioniNodo.do" border="1" usexml="true"
		rowperpage="${applicationScope.rowsPerPage}">
		<s:action>
			<c:url value="riconciliazioneTransazioniNodo.do">
				<c:if test="${!empty param.pageNumber}">
					<c:param name="pageNumber_hidden">${param.pageNumber}</c:param>
				</c:if>
				<c:if test="${!empty rowsPerPage}">
					<c:param name="rowsPerPage_hidden">${param.rowsPerPage}</c:param>
				</c:if>
				<c:if test="${!empty orderBy}">
					<c:param name="orderBy_hidden">${param.orderBy}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_societa}">
					<c:param name="tx_societa">${param.tx_societa}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_provincia}">
					<c:param name="tx_provincia">${param.tx_provincia}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_UtenteEnte}">
					<c:param name="tx_UtenteEnte">${param.tx_UtenteEnte}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_utente}">
					<c:param name="tx_utente">${param.tx_utente}</c:param>
				</c:if>
				<c:if test="${!empty param.statoSquadratura}">
					<c:param name="statoSquadratura">${param.statoSquadratura}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_tipo_carta}">
					<c:param name="tx_tipo_carta">${param.tx_tipo_carta}</c:param>
				</c:if>
			
				<c:if test="${!empty param.idFlusso}">
					<c:param name="idFlusso">${param.idFlusso}</c:param>
				</c:if>
			
				<c:if test="${!empty param.dtFlusso_da_day}">
					<c:param name="dtFlusso_da_day">${dtFlusso_da_day}</c:param>
				</c:if>
				<c:if test="${!empty param.dtFlusso_da_month}">
					<c:param name="dtFlusso_da_month">${param.dtFlusso_da_month}</c:param>
				</c:if>
				<c:if test="${!empty param.dtFlusso_da_year}">
					<c:param name="dtFlusso_da_year">${param.dtFlusso_da_year}</c:param>
				</c:if>
				<c:if test="${!empty param.dtFlusso_a_day}">
					<c:param name="dtFlusso_a_day">${param.dtFlusso_a_day}</c:param>
				</c:if>
				<c:if test="${!empty param.dtFlusso_a_month}">
					<c:param name="dtFlusso_a_month">${param.dtFlusso_a_month}</c:param>
				</c:if>
				<c:if test="${!empty param.dtFlusso_a_year}">
					<c:param name="dtFlusso_a_year">${param.dtFlusso_a_year}</c:param>
				</c:if>
			
				<c:if test="${!empty param.versOggetto}">
					<c:param name="versOggetto">${param.versOggetto}</c:param>
				</c:if>
			
				<c:if test="${!empty param.statoSquadratura}">
					<c:param name="statoSquadratura">${param.statoSquadratura}</c:param>
				</c:if>
			
				<c:if test="${!empty param.impPagamento_da}">
					<c:param name="impPagamento_da">${param.impPagamento_da}</c:param>
				</c:if>
				<c:if test="${!empty param.impPagamento_a}">
					<c:param name="impPagamento_a">${param.impPagamento_a}</c:param>
				</c:if>
			
				<c:if test="${!empty param.dtregol_da_day}">
					<c:param name="dtregol_da_day">${dtregol_da_day}</c:param>
				</c:if>
				<c:if test="${!empty param.dtregol_da_month}">
					<c:param name="dtregol_da_month">${param.dtregol_da_month}</c:param>
				</c:if>
				<c:if test="${!empty param.dtregol_da_year}">
					<c:param name="dtregol_da_year">${param.dtregol_da_year}</c:param>
				</c:if>
				<c:if test="${!empty param.dtregol_a_day}">
					<c:param name="dtregol_a_day">${param.dtregol_a_day}</c:param>
				</c:if>
				<c:if test="${!empty param.dtregol_a_month}">
					<c:param name="dtregol_a_month">${param.dtregol_a_month}</c:param>
				</c:if>
				<c:if test="${!empty param.dtregol_a_year}">
					<c:param name="dtregol_a_year">${param.dtregol_a_year}</c:param>
				</c:if>
			
				<c:if test="${!empty param.keyQuadratura}">
					<c:param name="keyQuadratura">${param.keyQuadratura}</c:param>
				</c:if>
			
				<c:if test="${!empty param.nomeFileTxt}">
					<c:param name="nomeFileTxt">${param.nomeFileTxt}</c:param>
				</c:if>
			
				<c:if test="${!empty param.dtChiusuraFlusso_a_day}">
					<c:param name="dtChiusuraFlusso_da_day">${dtChiusuraFlusso_da_day}</c:param>
				</c:if>
				<c:if test="${!empty param.dtChiusuraFlusso_da_month}">
					<c:param name="dtChiusuraFlusso_da_month">${param.dtChiusuraFlusso_da_month}</c:param>
				</c:if>
				<c:if test="${!empty param.dtChiusuraFlusso_da_year}">
					<c:param name="dtChiusuraFlusso_da_year">${param.dtChiusuraFlusso_da_year}</c:param>
				</c:if>
				<c:if test="${!empty param.dtChiusuraFlusso_a_day}">
					<c:param name="dtChiusuraFlusso_a_day">${param.dtChiusuraFlusso_a_day}</c:param>
				</c:if>
				<c:if test="${!empty param.dtChiusuraFlusso_a_month}">
					<c:param name="dtChiusuraFlusso_a_month">${param.dtChiusuraFlusso_a_month}</c:param>
				</c:if>
				<c:if test="${!empty param.dtChiusuraFlusso_a_year}">
					<c:param name="dtChiusuraFlusso_a_year">${param.dtChiusuraFlusso_a_year}</c:param>
				</c:if>
			
				<c:if test="${!empty ext}">
					<c:param name="ext">${ext}</c:param>
				</c:if>

			</c:url>
		</s:action>
		
		<s:dgcolumn label="Societ&aacute;" index="3" asc="SOCA" desc="SOCD"></s:dgcolumn>

<!-- lep utente eliminato -->

		<s:ifdatagrid left="${ext}" control="eq" right="1">
			<s:thendatagrid>		
				<%-- inizio LP PG200200 --%>
				<%--
				<s:dgcolumn index="14" label="Data Creazione Movimento" format="dd/MM/yyyy" asc="DCRA" desc="DCRD"></s:dgcolumn>
				<s:dgcolumn index="6" label="Nome Supporto" asc="NSUA" desc="NSUD"></s:dgcolumn>
				<s:dgcolumn index="22" label="Codice Identificativo Univoco Mittente" asc="KEYA" desc="KEYD" css="text_align_right"></s:dgcolumn>
				<s:dgcolumn index="23" label="Denominazione Mittente" asc="KEYA" desc="KEYD" css="text_align_right"></s:dgcolumn>
				<s:dgcolumn index="25" label="Codice Identificativo Univoco Ricevente" asc="KEYA" desc="KEYD" css="text_align_right"></s:dgcolumn>
				<s:dgcolumn index="26" label="Denominazione Ricevente" asc="KEYA" desc="KEYD" css="text_align_right"></s:dgcolumn>
				 --%>
				<s:dgcolumn index="14" label="Data Creazione Movimento" format="dd/MM/yyyy" asc="AGGA" desc="AGGD"></s:dgcolumn>
				<s:dgcolumn index="6" label="Nome Supporto" asc="SUPA" desc="SUPD"></s:dgcolumn>
				<s:dgcolumn index="22" label="Codice Identificativo Univoco Mittente" asc="CIMA" desc="CIMD" css="text_align_right"></s:dgcolumn>
				<s:dgcolumn index="23" label="Denominazione Mittente" asc="DEMA" desc="DEMD" css="text_align_right"></s:dgcolumn>
				<s:dgcolumn index="25" label="Codice Identificativo Univoco Ricevente" asc="CIRA" desc="CIRD" css="text_align_right"></s:dgcolumn>
				<s:dgcolumn index="26" label="Denominazione Ricevente" asc="DERA" desc="DERD" css="text_align_right"></s:dgcolumn>
				<%-- fine LP PG200200 --%>
			</s:thendatagrid>
		</s:ifdatagrid>
 
 		<s:dgcolumn index="17" label="Id. Flusso" css="text_align_right" asc="CFLA" desc="CFLD"></s:dgcolumn>
		<s:dgcolumn index="18" label="Data Flusso" format="dd/MM/yyyy" asc="GFLA" desc="GFLD"></s:dgcolumn>
		<s:dgcolumn index="28" label="Importo Quadratura" css="text_align_right" format="#,##0.00" asc="ITOA" desc="ITOD"></s:dgcolumn>
		<%-- inizio LP PG200200 --%>
		<%--
		<s:dgcolumn index="27" label="Numero RT" asc="NTRA" desc="NTRD" css="text_align_right"></s:dgcolumn>
		 --%>
		<s:dgcolumn index="27" label="Numero RT" asc="NTOA" desc="NTOD" css="text_align_right"></s:dgcolumn>
		<%-- fine LP PG200200 --%>
		<s:dgcolumn index="10" label="IUV scartate" asc="NSQA" desc="NSQD" css="text_align_right"></s:dgcolumn>
		<s:dgcolumn index="30" label="IUV recuperate" asc="NREA" desc="NRED" css="text_align_right"></s:dgcolumn>
		<s:dgcolumn index="1" label="Id. Quadratura" asc="KEYA" desc="KEYD" css="text_align_right"></s:dgcolumn>
			<s:ifdatagrid left="${ext}" control="eq" right="1">
			<s:thendatagrid>
				<%-- inizio LP PG200200 --%>
				<%--
				<s:dgcolumn index="13" label="Data Chiusura" format="dd/MM/yyyy" asc="CHIUA" desc="CHIUD"></s:dgcolumn>
				 --%>
				<s:dgcolumn index="13" label="Data Chiusura" format="dd/MM/yyyy" asc="NCHA" desc="NCHD"></s:dgcolumn>
				<%-- fine LP PG200200 --%>
			</s:thendatagrid>
		</s:ifdatagrid>
		<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;">

			<%-- inizio LP PG200200 --%>
			<%-- se si vuole introdurre dettaglio flusso e operazione Ritenta quadratura --%>
			<s:if right="{35}" control="eq" left="Y">
				<s:then>
					<s:hyperlink href="dettaglioIUVFlusso.do${formParameters}&idflusso={17}&societa={3}&data={18}&numrt={27}&iuvsca={10}&idquad={1}&tx_button_cerca=1"
					imagesrc="../applications/templates/riconciliazionemt/img/details.png" 
					alt="Lista I.U.V. nel Flusso" text="" cssclass="hlStyle" />
				</s:then>
				<s:else></s:else>
			</s:if>
			<s:if right="{36}" control="eq" left="Y">
				<s:then>
					<s:hyperlink href="ritentaQuadraturaFlusso.do${formParameters}&idquad={1}&vista=riconciliazionetransazioninodo&indietro=Y"
					imagesrc="../applications/templates/riconciliazionenn/img/quadraturaManuale.png" 
					alt="Ritenta Quadratura Flusso" text="" cssclass="hlStyle" />
				</s:then>
				<s:else></s:else>
			</s:if>
			<%-- fine LP PG200200 --%>
		
			<s:if right="{11}" control="eq" left="C">
				<s:then>
					<s:hyperlink href="../monitoraggio/monitoraggioTransazioni.do?tx_chiave_quadratura={17}&tx_button_cerca=1"
					imagesrc="../applications/templates/riconciliazione/img/dettagliMovimenti.png" 
					alt="Dettaglio Associazioni Movimento di Cassa" text="" cssclass="hlStyle" />
				</s:then>
				<s:else></s:else>
			</s:if>

			<s:if right="{6}" control="eq" left="FLUSSO FITTIZIO CREATO DA PROCEDURA">
				<s:then>
				</s:then>
				<s:else>
						<s:hyperlink href="scaricaMovimento.do?FileNameFlusso={6}"
						imagesrc="../applications/templates/riconciliazionenn/img/download.png" 
						alt="Download" text="" cssclass="hlStyle" />
					
				</s:else>
			</s:if>
			
			<s:if right="{29}" control="eq" left="0">
				<s:then>
				</s:then>
				<s:else>
					<s:hyperlink href="../riconciliazionemt/dettaglioAssociazioniMovimentoCassa.do?idmdc={29}&page=ricTraNodo"
					imagesrc="../applications/templates/riconciliazionenn/img/key.png" 
					alt="Dettaglio movimento" text="" cssclass="hlStyle" />
				</s:else>
			</s:if>
		</s:dgcolumn>
	</s:datagrid>

<!--           

PG170300     MODIFCATO  PER EFFETTUARE IL DOWNLOAD DELL XML GIUSTO
						<s:hyperlink href="scaricaMovimento.do?FileNameFlusso={17}.xml"
						imagesrc="../applications/templates/riconciliazionenn/img/download.png" 
						alt="Download" text="" cssclass="hlStyle" />
	

 -->
	
	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Riepilogo statistico
	</s:div>
		
	<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
		<s:thead>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:th cssclass="seda-ui-datagridheadercell"  icol="7"><b>Importi Totali</b></s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr cssclass="seda-ui-datagridrowdispari">
				<s:td cssclass="seda-ui-datagridcell">&nbsp;<br/></s:td>
				<s:td cssclass="seda-ui-datagridcell">Numero Movimenti</s:td>
				<s:td cssclass="seda-ui-datagridcell">Numero RT</s:td>
				<s:td cssclass="seda-ui-datagridcell">Numero IUV scartate</s:td>
				<s:td cssclass="seda-ui-datagridcell">Importo Totale Quadrature</s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowdispari">
				<s:td cssclass="seda-ui-datagridcell">Totali</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><c:out value="${totMov}"/></s:td>
				<%-- 
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImpMov}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
				 --%>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><c:out value="${totImpMov}"/></s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><c:out value="${totTran}"/></s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><c:out value="${totImpQun}"/></s:td>
			</s:tr>
		</s:tbody>
	</s:table>

</c:if>
