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
		$("#dtGiornale_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy"); 
		$("#dtGiornale_da_hidden").datepicker(
			{
				minDate : new Date(annoDa, 0, 1),
				maxDate : new Date(annoA, 11, 31),
				yearRange : annoDa + ":" + annoA,
				showOn : "button",
				buttonImage : "../applications/templates/shared/img/calendar.gif",
				buttonImageOnly : true,
				onSelect : function(dateText, inst) {
					$("#dtGiornale_da_day_id").val(dateText.substr(0, 2));
					$("#dtGiornale_da_month_id").val(dateText.substr(3, 2));
					$("#dtGiornale_da_year_id").val(dateText.substr(6, 4));
				},
				beforeShow : function(input, inst) {
					//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
				updateValoreDatePickerFromDdl(
						"dtGiornale_da_day_id",
						"dtGiornale_da_month_id",
						"dtGiornale_da_year_id",
						"dtGiornale_da_hidden");
			}

		});
		$("#dtGiornale_a_hidden").datepicker("option", "dateFormat","dd/mm/yyyy");
		$("#dtGiornale_a_hidden").datepicker(
			{
				minDate : new Date(annoDa, 0, 1),
				maxDate : new Date(annoA, 11, 31),
				yearRange : annoDa + ":" + annoA,
				showOn : "button",
				buttonImage : "../applications/templates/shared/img/calendar.gif",
				buttonImageOnly : true,
				onSelect : function(dateText, inst) {
					$("#dtGiornale_a_day_id").val(dateText.substr(0, 2));
					$("#dtGiornale_a_month_id").val(dateText.substr(3, 2));
					$("#dtGiornale_a_year_id").val(dateText.substr(6, 4));
				},
				beforeShow : function(input, inst) {
					//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
				updateValoreDatePickerFromDdl(
						"dtGiornale_a_day_id",
						"dtGiornale_a_month_id",
						"dtGiornale_a_year_id",
						"dtGiornale_a_hidden");
			}
		});
		
		$("#dtFlusso_da_hidden").datepicker("option", "dateFormat","dd/mm/yyyy");
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
</script>

<c:url value="" var="formParameters">
	<c:if test="${!empty param.tx_societa}">
		<c:param name="tx_societa">${param.tx_societa}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_provincia}">
		<c:param name="tx_provincia">${param.tx_provincia}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_UtenteEnte}">
		<c:param name="tx_UtenteEnte">${param.tx_UtenteEnte}</c:param>
	</c:if>
	<c:if test="${!empty param.numeroDocumento}">
		<c:param name="numeroDocumento">${param.numeroDocumento}</c:param>
	</c:if>
	<c:if test="${!empty param.provenienza}">
		<c:param name="provenienza">${param.provenienza}</c:param>
	</c:if>
	<c:if test="${!empty param.idcassa}">
		<c:param name="idcassa">${param.idcassa}</c:param>
	</c:if>
	<c:if test="${!empty param.dtGiornale_da_day}">
		<c:param name="dtGiornale_da_day">${param.dtGiornale_da_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtGiornale_da_month}">
		<c:param name="dtGiornale_da_month">${param.dtGiornale_da_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtGiornale_da_year}">
		<c:param name="dtGiornale_da_year">${param.dtGiornale_da_year}</c:param>
	</c:if>
	<c:if test="${!empty param.dtGiornale_a_day}">
		<c:param name="dtGiornale_a_day">${param.dtGiornale_a_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtGiornale_a_month}">
		<c:param name="dtGiornale_a_month">${param.dtGiornale_a_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtGiornale_a_year}">
		<c:param name="dtGiornale_a_year">${param.dtGiornale_a_year}</c:param>
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
	<c:if test="${!empty param.idFlusso}">
		<c:param name="idFlusso">${param.idFlusso}</c:param>
	</c:if>
	<c:if test="${!empty param.mittente}">
		<c:param name="mittente">${param.mittente}</c:param>
	</c:if>
	<c:if test="${!empty param.importoDa}">
		<c:param name="importoDa">${param.importoDa}</c:param>
	</c:if>
	<c:if test="${!empty param.mittente}">
		<c:param name="importoA">${param.importoA}</c:param>
	</c:if>
	<c:if test="${!empty ext}">
		<c:param name="ext">${ext}</c:param>
	</c:if>

</c:url>

<s:div name="divSelezione1" cssclass="divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="associazioniFlussiMovimentoCassaForm"
			action="associazioniFlussiMovimentoCassa.do" method="post"
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
			<s:textbox name="page" label="page" bmodify="true"
				text="${page}" cssclass="display_none" cssclasslabel="display_none" />

			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
				Associazioni Flussi A Movimento Di Cassa
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:dropdownlist name="tx_societa" disable="${societaDdlDisabled}"
							cssclass="tbddl floatleft" label="Societ&agrave;:"
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

					 <s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:dropdownlist name="tx_UtenteEnte"
							disable="${ddlUtenteEnteDisabled}" cssclass="tbddlMax floatleft"
							label="Ente:" cssclasslabel="label65 bold textright"
							cachedrowset="listaUtentiEnti" usexml="true"
							onchange="setFiredButton('tx_button_utente_changed');this.form.submit();"
							valueselected="${tx_UtenteEnte}">
							<s:ddloption text="Tutti gli Enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_utente_changed"  type="submit" text="" onclick="" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
						</noscript>
					</s:div>

				</s:div>
				
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

					<s:div name="divElement12" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement12a" cssclass="labelData">
							<s:label name="label_data_creazione" 
								cssclass="seda-ui-label label78 bold textright"
								text="Data Giornale" />
						</s:div>

						<s:div name="divElement12b" cssclass="floatleft">
							<s:div name="divDtGiornaleDA" cssclass="divDataDa">
								<s:date label="Da:" cssclasslabel="labelsmall"
									cssclass="dateman" prefix="dtGiornale_da"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
									locale="IT-it" descriptivemonth="false" separator="/"
									calendar="${dtGiornale_da}" disabled="${dtGiornale_da_disabled}">
								</s:date>
								<input type="hidden" id="dtGiornale_da_hidden" value=""/>
							</s:div>

							<s:div name="divDtGiornaleA" cssclass="divDataA">
								<s:date label="A:" cssclasslabel="labelsmall" cssclass="dateman"
									prefix="dtGiornale_a" yearbegin="${ddlDateAnnoDa}"
									yearend="${ddlDateAnnoA}" locale="IT-it"
									descriptivemonth="false" separator="/"
									calendar="${dtGiornale_a}" disabled="${dtGiornale_a_disabled}"></s:date>
								<input type="hidden" id="dtGiornale_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
				</s:div>
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">

					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="idcassa" cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright" disable="${idcassaDisabled}"
							label="Identificativo di Cassa:" valueselected="${idcassa}"
							cachedrowset="lista_giornali_di_cassa" usexml="true"
							onchange="this.form.submit();">
							<s:ddloption value="" text="Tutti" />
							<s:ddloption text="{6}" value="{6}" />
						</s:dropdownlist>
					</s:div>
					
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="provenienza" cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright" disable="${provenienzaDisabled}"
							label="Provenienza:" valueselected="${provenienza}"
							onchange="this.form.submit();">
							<s:ddloption value="" text="Tutte le provenienze" />
							<s:ddloption value="Banca" text="Banca" />
						</s:dropdownlist>
					</s:div>
					
				</s:div>
							
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="numeroDocumento" cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright" disable="${numeroDocumento_disabled}"
							label="Numero Documento:" valueselected="${numeroDocumento}"
							cachedrowset="lista_numero_documento" usexml="true"
							onchange="this.form.submit();">
							<s:ddloption value="" text="Tutti i documenti" />
							<s:ddloption value="{1}" text="{2}" />
						</s:dropdownlist>
					</s:div>

				</s:div>
		
				<c:if test="${!empty mdc_importo}">
			
			<s:div name="divCentered0" cssclass="divRicBottoni">
				<s:div name="divCentered1" cssclass="divTableTitle bold">
				Riepilogo statistico
				</s:div>
			
				<s:table cssclass="seda-ui-datagrid" border="1" cellspacing="0" cellpadding="3">
					<s:thead>
						<s:tr cssclass="seda-ui-datagridrowdispari">
							<s:th cssclass="seda-ui-datagridcell">Importo Provvisorio Cassa</s:th>
							<s:th cssclass="seda-ui-datagridcell">Importo Abbinato</s:th>
							<s:th cssclass="seda-ui-datagridcell">Squadratura</s:th>
						</s:tr>
					</s:thead>
					<s:tbody>
						<s:tr cssclass="seda-ui-datagridrowpari">
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								${mdc_importo}
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								${mdc_importo_abbinato}
							</s:td>
							<s:td cssclass="seda-ui-datagridcell text_align_right">
								${mdc_squadratura}
							</s:td>
						</s:tr>
					</s:tbody>
				</s:table>
				</s:div>
					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
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
	
					</s:div>
					
					<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
	
						<s:div name="divElement14" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="" maxlenght="35"
								cssclass="textareaman" cssclasslabel="label85 bold textright"
								bmodify="true" name="idFlusso" label="Identificativo Flusso:"
								text="${idFlusso}" />
						</s:div>
					
					</s:div>
					
					<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
				
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="mittente" cssclass="tbddl floatleft"
								cssclasslabel="label85 bold floatleft textright" disable="false"
								label="Mittente:" valueselected="${mittente}"
								cachedrowset="lista_mittente" usexml="true">
								<s:ddloption value="" text="Tutti i mittenti" />
								<s:ddloption value="{1}" text="{3}" />
							</s:dropdownlist>
						</s:div>
						
						<s:div name="divElement12" cssclass="divRicMetadatiDoubleRow">
							<s:div name="divElement12a" cssclass="labelData">
								<s:label name="label_data_creazione" 
									cssclass="seda-ui-label label78 bold textright"
									text="Importo" />
							</s:div>
		
							<s:div name="divElement12b" cssclass="floatleft">
								<s:div name="divDtGiornaleDA" cssclass="divDataDa">
									<s:textbox validator="ignore;numberIT" maxlenght="20"
									label="Da:" cssclasslabel="labelsmall" cssclass="dateman"
									bmodify="true" name="importoDa" text="${importoDa}"/>
									<input type="hidden" id="importo_da_hidden" value="" />
								</s:div>
	
								<s:div name="divDtGiornaleA" cssclass="divDataA">
									<s:textbox validator="ignore;numberIT" maxlenght="20"
									label="A:" cssclasslabel="labelsmall" cssclass="dateman"
									bmodify="true" name="importoA" text="${importoA}"/>
									<input type="hidden" id="importo_a_hidden" value="" />
								</s:div>
							</s:div>
	
						</s:div>
	
					</s:div>
				</c:if>
			</s:div>
			<s:div name="divCentered0" cssclass="divRicBottoni">
				<!--<c:if test="${!empty message}">${message}</c:if>-->
			</s:div>
			
			<s:div name="divCentered0" cssclass="divRicBottoni">

				<s:button id="tx_button_cerca" type="submit" text="Cerca" onclick=""
					cssclass="btnStyle" />
				<s:button id="tx_button_reset" type="submit" text="Reset" onclick=""
					cssclass="btnStyle" />
				<c:if test="${page == 'dettMov'}">
					<s:hyperlink name="hpAnnulla" cssclass="hlToBtnStyle" href="dettaglioMovimentoCassa.do?indietro=Y&idmdc=${numeroDocumento}" text="Indietro" />
				</c:if>
				<c:if test="${page == 'dettGior'}">
					<s:hyperlink name="hpAnnulla" cssclass="hlToBtnStyle" href="dettaglioGiornaleCassa.do?tx_button_cerca=cerca" text="Indietro" />				</c:if>
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
			</s:div>	
		</s:form>
	</s:div>
</s:div>

<c:if test="${!empty lista_movimenti_di_cassa_nonrend}">
	<fmt:setLocale value="it_IT" />
	
	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Elenco Movimenti Di Cassa
	</s:div>
	
	<s:datagrid cachedrowset="lista_movimenti_di_cassa_nonrend"
		action="associazioniFlussiMovimentoCassa.do" border="1" usexml="true"
		rowperpage="${applicationScope.rowsPerPage}" viewstate="true">
		
		<s:dgcolumn index="2" label="Conto" asc="CONA" desc="COND"></s:dgcolumn>
		<s:dgcolumn index="3" label="Stato Sospeso" asc="STSA" desc="STSD"></s:dgcolumn>
		<s:dgcolumn index="4" label="Numero Documento" asc="DOCA" desc="DOCD"></s:dgcolumn>
		<s:dgcolumn index="5" label="Cliente" asc="CLIA" desc="CLID"></s:dgcolumn>
		<s:dgcolumn index="6" label="Importo" css="text_align_right" format="#,##0.00" asc="IMPA" desc="IMPD"></s:dgcolumn>
		<s:dgcolumn index="7" label="Squadratura" asc="SQUA" desc="SQUD"></s:dgcolumn>
		<s:dgcolumn label="Azioni">
			<s:hyperlink
				href="associazioniFlussiMovimentoCassa.do?numeroDocumento={1}"
				cssclass="blacklink hlStyle"
				imagesrc="../applications/templates/shared/img/Info.png" text=""
				alt="Dettaglio Movimento di Cassa" />
	
		</s:dgcolumn>
  		
	</s:datagrid>
</c:if>

<c:if test="${!empty lista_flussi}">
	<fmt:setLocale value="it_IT" />
	
	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Elenco Flussi
	</s:div>
	
	<s:datagrid cachedrowset="lista_flussi"
		action="associazioniFlussiMovimentoCassa.do${formParameters}&page=${page}" border="1" usexml="true"
		rowperpage="${applicationScope.rowsPerPage}" viewstate="true">

		<s:dgcolumn index="2" label="Data Flusso" format="dd/MM/yyyy" asc="DFLA" desc="DFLD"></s:dgcolumn>
		<s:dgcolumn index="3" label="Flusso" asc="IDFA" desc="IDFD"></s:dgcolumn>
		<s:dgcolumn index="4" label="Importo" css="text_align_right" format="#,##0.00" asc="IMPA" desc="IMPD"></s:dgcolumn>
		<s:dgcolumn index="5" label="Codice Mittente" asc="CMTA" desc="CMTD"></s:dgcolumn>
		<s:dgcolumn index="6" label="Mittente" asc="MITA" desc="MITD"></s:dgcolumn>
		<s:dgcolumn label="Azioni">
			<s:if right="{7}" control="eq" left="0">
				<s:then>
					<c:if test="${sessionScope.j_user_bean.associazioniProvvisorieRiconciliazionemtEnabled}">
						<s:hyperlink
							href="associazioniFlussiMovimentoCassa.do?numeroDocumento=${numeroDocumento}&chiaveFlusso={1}&action=aggiungi&page=${page}"
							cssclass="blacklink hlStyle"
							imagesrc="../applications/templates/riconciliazionemt/img/aggiungi.png" text=""
							alt="Associa al Sospeso"
							onclick=""/>
					</c:if>
				</s:then>		
				<s:else>
					<c:if test="${sessionScope.j_user_bean.associazioniProvvisorieRiconciliazionemtEnabled}">
						<s:hyperlink
							href="associazioniFlussiMovimentoCassa.do?numeroDocumento=${numeroDocumento}&chiaveFlusso={1}&action=elimina&page=${page}"
							cssclass="blacklink hlStyle"
							imagesrc="../applications/templates/riconciliazionemt/img/elimina.png" text=""
							alt="Disassocia dal Sospeso"
							onclick=""/>
					</c:if>
				</s:else>
			</s:if>
		</s:dgcolumn>
  		
	</s:datagrid>
</c:if>