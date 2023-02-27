<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="riconciliazionemanualetransazioni" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>
<script src="../applications/templates/monitoraggio/js/popup.js" type="text/javascript"></script>


<script type="text/javascript">
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

	function setMovimento() {
		var txtHidden = document.getElementById('tx_button_movimento_changed');
		if (txtHidden != null)
			txtHidden.value = 'Selezione';
	}

	function getCSSRule(ruleName, deleteFlag) {               // Return requested style obejct
		   ruleName=ruleName.toLowerCase();                       // Convert test string to lower case.
		   if (document.styleSheets) {                            // If browser can play with stylesheets
		      for (var i=0; i<document.styleSheets.length; i++) { // For each stylesheet
		         var styleSheet=document.styleSheets[i];          // Get the current Stylesheet
		         var ii=0;                                        // Initialize subCounter.
		         var cssRule=false;                               // Initialize cssRule. 
		         do {                                             // For each rule in stylesheet
		            if (styleSheet.cssRules) {                    // Browser uses cssRules?
		               cssRule = styleSheet.cssRules[ii];         // Yes --Mozilla Style
		            } else {                                      // Browser usses rules?
		               cssRule = styleSheet.rules[ii];            // Yes IE style. 
		            }                                             // End IE check.
		            if (cssRule)  {                               // If we found a rule...
		               if (cssRule.selectorText.toLowerCase()==ruleName) { //  match ruleName?
		                  if (deleteFlag=='delete') {             // Yes.  Are we deleteing?
		                     if (styleSheet.cssRules) {           // Yes, deleting...
		                        styleSheet.deleteRule(ii);        // Delete rule, Moz Style
		                     } else {                             // Still deleting.
		                        styleSheet.removeRule(ii);        // Delete rule IE style.
		                     }                                    // End IE check.
		                     return true;                         // return true, class deleted.
		                  } else {                                // found and not deleting.
		                     return cssRule;                      // return the style object.
		                  }                                       // End delete Check
		               }                                          // End found rule name
		            }                                             // end found cssRule
		            ii++;                                         // Increment sub-counter
		         } while (cssRule);                               // end While loop
		      }                                                   // end For loop
		   }                                                      // end styleSheet ability check
		   return false;                                          // we found NOTHING!
		}                                                         // end getCSSRule 

		function killCSSRule(ruleName) {                          // Delete a CSS rule   
		   return getCSSRule(ruleName,'delete');                  // just call getCSSRule w/delete flag.
		}                                                         // end killCSSRule

		function addCSSRule(ruleName) {                           // Create a new css rule
		   if (document.styleSheets) {                            // Can browser do styleSheets?
		      if (!getCSSRule(ruleName)) {                        // if rule doesn't exist...
		         if (document.styleSheets[0].addRule) {           // Browser is IE?
		            document.styleSheets[0].addRule(ruleName, null,0);      // Yes, add IE style
		         } else {                                         // Browser is IE?
		            document.styleSheets[0].insertRule(ruleName+' { }', 0); // Yes, add Moz style.
		         }                                                // End browser check
		      }                                                   // End already exist check.
		   }                                                      // End browser ability check.
		   return getCSSRule(ruleName);                           // return rule we just created.
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

	
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();

	$(function() {
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#data_creazione_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_creazione_da_hidden").datepicker({	
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_creazione_da_day_id").val(dateText.substr(0,2));
												$("#data_creazione_da_month_id").val(dateText.substr(3,2));
												$("#data_creazione_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_creazione_da_day_id",
				                            "data_creazione_da_month_id",
				                            "data_creazione_da_year_id",
				                            "data_creazione_da_hidden");
			}
		});
		$("#data_creazione_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_creazione_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_creazione_a_day_id").val(dateText.substr(0,2));
												$("#data_creazione_a_month_id").val(dateText.substr(3,2));
												$("#data_creazione_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_creazione_a_day_id",
				                            "data_creazione_a_month_id",
				                            "data_creazione_a_year_id",
				                            "data_creazione_a_hidden");
			}
		});
		$("#data_pagamento_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_pagamento_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_pagamento_da_day_id").val(dateText.substr(0,2));
												$("#data_pagamento_da_month_id").val(dateText.substr(3,2));
												$("#data_pagamento_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_pagamento_da_day_id",
				                            "data_pagamento_da_month_id",
				                            "data_pagamento_da_year_id",
				                            "data_pagamento_da_hidden");
			}
		});
		$("#data_pagamento_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_pagamento_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_pagamento_a_day_id").val(dateText.substr(0,2));
												$("#data_pagamento_a_month_id").val(dateText.substr(3,2));
												$("#data_pagamento_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_pagamento_a_day_id",
				                            "data_pagamento_a_month_id",
				                            "data_pagamento_a_year_id",
				                            "data_pagamento_a_hidden");
			}
		});
	});
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
	<c:if test="${!empty param.chiaveMovimento}">
		<c:param name="chiaveMovimento">${param.chiaveMovimento}</c:param>
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
	<c:if test="${!empty param.chiaveTransazione}">
		<c:param name="chiaveTransazione">${param.chiaveTransazione}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_tipo_carta}">
		<c:param name="tx_tipo_carta">${param.tx_tipo_carta}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_canale_pagamento}">
		<c:param name="tx_canale_pagamento">${param.tx_canale_pagamento}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_strumento}">
		<c:param name="tx_strumento">${param.tx_strumento}</c:param>
	</c:if>
	<c:if test="${!empty param.codiceAbi}">
		<c:param name="codiceAbi">${param.codiceAbi}</c:param>
	</c:if>
	<c:if test="${!empty param.codiceCab}">
		<c:param name="codiceCab">${param.codiceCab}</c:param>
	</c:if>
	<c:if test="${!empty param.codiceSia}">
		<c:param name="codiceSia">${param.codiceSia}</c:param>
	</c:if>
	<c:if test="${!empty param.cro}">
		<c:param name="cro">${param.cro}</c:param>
	</c:if>
	<c:if test="${!empty param.ccb}">
		<c:param name="ccb">${param.ccb}</c:param>
	</c:if>
	<c:if test="${!empty param.nomeSupporto}">
		<c:param name="nomeSupporto">${param.nomeSupporto}</c:param>
	</c:if>
	<c:if test="${!empty data_pagamento_da_day}">
		<c:param name="data_pagamento_da_day">${data_pagamento_da_day}</c:param>
	</c:if>
	<c:if test="${!empty data_pagamento_da_month}">
		<c:param name="data_pagamento_da_month">${data_pagamento_da_month}</c:param>
	</c:if>
	<c:if test="${!empty data_pagamento_da_year}">
		<c:param name="data_pagamento_da_year">${data_pagamento_da_year}</c:param>
	</c:if>
	<c:if test="${!empty data_pagamento_a_day}">
		<c:param name="data_pagamento_a_day">${data_pagamento_a_day}</c:param>
	</c:if>
	<c:if test="${!empty data_pagamento_a_month}">
		<c:param name="data_pagamento_a_month">${data_pagamento_a_month}</c:param>
	</c:if>
	<c:if test="${!empty data_pagamento_a_year}">
		<c:param name="data_pagamento_a_year">${data_pagamento_a_year}</c:param>
	</c:if>
	<c:if test="${!empty data_creazione_da_day}">
		<c:param name="data_creazione_da_day">${data_creazione_da_day}</c:param>
	</c:if>
	<c:if test="${!empty data_creazione_da_month}">
		<c:param name="data_creazione_da_month">${data_creazione_da_month}</c:param>
	</c:if>
	<c:if test="${!empty data_creazione_da_year}">
		<c:param name="data_creazione_da_year">${data_creazione_da_year}</c:param>
	</c:if>
	<c:if test="${!empty data_creazione_a_day}">
		<c:param name="data_creazione_a_day">${data_creazione_a_day}</c:param>
	</c:if>
	<c:if test="${!empty data_creazione_a_month}">
		<c:param name="data_creazione_a_month">${data_creazione_a_month}</c:param>
	</c:if>
	<c:if test="${!empty data_creazione_a_year}">
		<c:param name="data_creazione_a_year">${data_creazione_a_year}</c:param>
	</c:if>
	<c:if test="${!empty ext}">
		<c:param name="ext">${ext}</c:param>
	</c:if>
</c:url>

<s:div name="divSelezione1" cssclass="divSelezione">
	<s:form name="riconciliazioneManualeTransazioniForm"
		action="riconciliazioneManualeTransazioni.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:textbox name="DDLChanged" label="DDLChanged" bmodify="true" text=""
			cssclass="display_none" cssclasslabel="display_none" />
		<s:textbox name="ext" label="ext" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none" />

			<s:textbox name="hRowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="hPageNumber" label="pageNumberRic" bmodify="true" text="${pageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="hOrder" label="orderRic" bmodify="true" text="${order}" cssclass="display_none" cssclasslabel="display_none"  />
	
		<s:div name="divRicercaTopName" cssclass="divRicercaTop">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
				Ricerca Transazioni
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:dropdownlist name="tx_societa"
							disable="${societaDdlDisabled}"
							cssclass="tbddl floatleft" label="Societ&aacute;:"
							cssclasslabel="label85 bold floatleft textright"
							cachedrowset="listaSocieta" usexml="true"
							onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
							valueselected="${tx_societa}">
							<s:ddloption text="Tutte le Societ&agrave;" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<!-- onchange="setDdl('societa'); callSubmit(this.form);" -->
						<noscript>
							<s:button id="tx_button_societa_changed" type="submit" 
							disable="${societaDdlDisabled}" text="" onclick="" 
							cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
						</noscript>
					</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
						<s:dropdownlist name="tx_provincia"
							disable="${provinciaDdlDisabled}"
							cssclass="tbddl floatleft" label="Provincia:"
							cssclasslabel="label65 bold floatleft textright"
							cachedrowset="listaProvince" usexml="true"
							onchange="setFiredButton('tx_button_provincia_changed');this.form.submit();"
							valueselected="${tx_provincia}">
							<s:ddloption text="Tutte le Province" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<!-- onchange="setDdl('provincia'); callSubmit(this.form);" -->
						<noscript>
							<s:button id="tx_button_provincia_changed" type="submit" 
							disable="${provinciaDdlDisabled}" text="" onclick="" 
							cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
						</noscript>
							
					</s:div>
					
					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:dropdownlist name="tx_utente"
							disable="${utenteDdlDisabled}"
							cssclass="tbddlMax floatleft" label="Utente:"
							cssclasslabel="label65 bold textright" cachedrowset="listaUtenti"
							onchange="setFiredButton('tx_button_utente_changed');this.form.submit();"
							usexml="true" valueselected="${tx_utente}">
							<s:ddloption text="Tutti gli Utenti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_utente_changed"  type="submit" text="" onclick="" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
						</noscript>
					</s:div>
				</s:div>
				
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="maxlength=64" bmodify="true"
					        maxlenght="64" 
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							name="chiaveTransazione" label="Id Transazione:"
							text="${chiaveTransazione}" />
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
						<s:textbox validator="ignore;digits;minlength=5;maxlength=5"
					        maxlenght="5" 
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							bmodify="true" name="codiceAbi" label="ABI:" text="${codiceAbi}" />
					</s:div>
					<s:div name="divElement60" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;digits;minlength=5;maxlength=5"
						    maxlenght="5" 
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							bmodify="true" name="codiceCab" label="CAB:" text="${codiceCab}" />
					</s:div>
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="maxlength=16" bmodify="true" name="cro"
					        maxlenght="16" 
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							label="CRO:" text="${cro}" />
					</s:div>					
				</s:div>				
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_tipo_carta" disable="false"
							cssclass="tbddlMax floatleft" label="Tipo Carta:"
							cssclasslabel="label85 bold textright" cachedrowset="listaGateway"
							usexml="true" valueselected="${tx_tipo_carta}">
							<s:ddloption text="Tutti tipi di carte" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
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
						<s:textbox validator="ignore;minlength=5;maxlength=5"
						    maxlenght="5" 
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							bmodify="true" name="codiceSia" label="SIA:" text="${codiceSia}" />
					</s:div>
					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;digits;maxlength=12" bmodify="true"
							maxlenght="12" 
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							name="ccb" label="CCB:" text="${ccb}" />
					</s:div>
				</s:div>
				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement12" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="maxlength=20" bmodify="true"
							cssclass="textareaman"
							maxlenght="20" 
							cssclasslabel="label85 bold textright"
							name="nomeSupporto" label="Nome Supporto:" text="${nomeSupporto}"
							 />
					</s:div>
					<s:div name="divElement13" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement13a" cssclass="labelData">
							<s:label name="label_data_pag"
								cssclass="seda-ui-label label85 bold textright" 
								text="Data Pagamento Transazione" />
						</s:div>
						<s:div name="divElement13b" cssclass="floatleft">
							<s:div name="divDataDa_pag" cssclass="divDataDa">
								<s:date label="Da:" prefix="data_pagamento_da" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${data_pagamento_da}"></s:date>
								<input type="hidden" id="data_pagamento_da_hidden" value="" />
							</s:div>
						
							<s:div name="divDataA_val" cssclass="divDataA">
								
								<s:date label="A:" prefix="data_pagamento_a" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${data_pagamento_a}"></s:date>
								<input type="hidden" id="data_pagamento_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
					<s:div name="divElement15" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement15a" cssclass="labelData">
							<s:label name="label_data_creazione"
								cssclass="seda-ui-label label85 bold textright"
								text="Data Creazione Flusso" />
						</s:div>
						
						<s:div name="divElement15b" cssclass="floatleft">
							<s:div name="divDataDa_cont" cssclass="divDataDa">
								<s:date label="Da:" prefix="data_creazione_da" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${data_creazione_da}"></s:date>
								<input type="hidden" id="data_creazione_da_hidden" value="" />
							</s:div>
					
							<s:div name="divDataA_cont" cssclass="divDataA">
								<s:date label="A:" prefix="data_creazione_a" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${data_creazione_a}"></s:date>
								<input type="hidden" id="data_creazione_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
				</s:div>
		</s:div>
	
	
	<s:div name="divCentered" cssclass="divRicBottoni">
		<s:button id="tx_button_cerca" type="submit" text="Cerca" onclick="" cssclass="btnStyle" />
		<s:button id="tx_button_reset" onclick="" text="Reset" type="submit" cssclass="btnStyle" />

		<c:if test="${!empty listaTransazioniNonQuadrate && ext=='0'}">
			<s:button id="tx_button_cerca_exp" type="submit" text="Espandi" onclick="" cssclass="btnStyle" />
		</c:if>
		<c:if test="${!empty listaTransazioniNonQuadrate && ext=='1'}">
			<s:button id="tx_button_cerca_exp" type="submit" text="Contrai" onclick="" cssclass="btnStyle" />
		</c:if>
		<s:textbox name="fired_button_hidden" label="fired_button_hidden"
			bmodify="true" text="" cssclass="rend_display_none"
			cssclasslabel="rend_display_none" />
	</s:div>
	
	
	

	</s:div>
	
	<br />
	<s:div name="divRicercaMovimento" cssclass="divRicMetadatiSingleRow">
		<s:textbox name="tx_button_movimento_changed"
			text="tx_button_movimento_changed" cssclass="display_none"
			label="tx_button_movimento_changed" bmodify="true"
			cssclasslabel="display_none"/>
		<s:dropdownlist name="chiaveMovimento" disable="false"
			cssclass="tbddlMax600 floatleft" label="Rif Movimento:"
			cssclasslabel="label110 bold textright"
			cachedrowset="listaMovimentiAperti" usexml="true"
			valueselected="${chiaveMovimento}"
			onchange="setMovimento('societa'); callSubmit(this.form);">
			<s:ddloption text="Nessun movimento" value="0" />
			<s:ddloption text="Id. Qua. {1} - {2} - {3} - {4} - {5} - {6} - {7}" value="{1}" />
		</s:dropdownlist>
		<noscript>
			<s:button id="tx_button_movimento_changed_no_js"
				type="submit" text="" onclick="" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
		</noscript>
	</s:div>
	</s:form>
</s:div>

<s:div name="divMessage">
	<c:if test="${!empty tx_message}">
		<hr />
		<s:label name="tx_message" text="${tx_message}" />
		<hr />
	</c:if>
	<c:if test="${!empty tx_error_message}">
		<hr />
		<s:label name="tx_error_message" text="${tx_error_message}" />
		<hr />
	</c:if>
</s:div>
<c:if test="${!empty listaTransazioniNonQuadrate}">
	<fmt:setLocale value="it_IT" />
	
	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Elenco Transazioni
	</s:div>
	


	<s:datagrid cachedrowset="listaTransazioniNonQuadrate"
		action="riconciliazioneManualeTransazioni.do?vista=riconciliazionemanualetransazioni" border="1" usexml="true"
		rowperpage="${applicationScope.rowsPerPage}" viewstate="true">
		
		<s:dgcolumn label="Id">
			<s:hyperlink
				href="dettaglioTransazione.do?${formParameters}&tx_codice_transazione_hidden={1}"
				onclick="showDettaglio('../monitoraggio/dettaglioTransazioneJson.do?tx_codice_transazione_hidden={1}');return false"
				cssclass="blacklink hlStyle"
				imagesrc="../applications/templates/shared/img/Info.png" text=""
				alt="Dettaglio Transazione" />
		</s:dgcolumn>
		<s:dgcolumn index="17" label="Societ&aacute;" asc="SOCA" desc="SOCD"></s:dgcolumn>
		<s:dgcolumn index="2" label="Utente" asc="UTEA" desc="UTED"></s:dgcolumn>
		<s:dgcolumn index="18" label="Data Transazione" format="dd/MM/yyyy HH:mm:ss" asc="DTRA" desc="DTRD"></s:dgcolumn>
		<s:dgcolumn index="3" label="Data Effettivo Pagamento" format="dd/MM/yyyy HH:mm:ss" asc="DPGA"
			desc="DPGD"></s:dgcolumn>
		<s:dgcolumn index="4" label="Canale" asc="CANA" desc="CAND"></s:dgcolumn>
		<s:dgcolumn index="5" label="Tipo Carta" asc="CARA" desc="CARD"></s:dgcolumn>

		<s:ifdatagrid left="${ext}" control="eq" right="1">
			<s:thendatagrid>
				<s:dgcolumn label="SIA / ABI /  CAB / CCB" asc="CODA" desc="CODD">
					{6}<br />{7}<br />{8}<br />{9} 
				</s:dgcolumn>
				<s:dgcolumn index="10" label="Data  Creazione Movimento" format="dd/MM/yyyy" asc="DCRA" desc="DCRD"></s:dgcolumn>
				<s:dgcolumn index="11" label="Supporto" asc="NSUA" desc="NSUD"></s:dgcolumn>
			</s:thendatagrid>
		</s:ifdatagrid>

		<s:dgcolumn index="12" label="CRO" asc="CROA" desc="CROD"></s:dgcolumn>
  		
		<s:dgcolumn index="13" label="Importo Transazione" asc="ITOA"
			desc="ITOD" css="text_align_right" format="#,##0.00"></s:dgcolumn>
		<s:dgcolumn index="14" label="Costi Banca" css="text_align_right" asc="IGWA" desc="IGWD" format="#,##0.00"></s:dgcolumn>
		<s:dgcolumn index="15" label="Importo Netto Transazione" css="text_align_right" asc="INEA"
			desc="INED" format="#,##0.00"></s:dgcolumn>
		<s:dgcolumn label="Azioni">
			<s:if right="{16}" control="eq" left="0">
				<s:then>
					<c:if test="${sessionScope.j_user_bean.azioniPerRiconciliazioneManualeEnabled}">
						<s:hyperlink
							href="associaTransazione.do${formParameters}&chiaveTransazioneMov={1}"
							imagesrc="../applications/templates/riconciliazione/img/aggiungi.png" 
							alt="Aggiungi" text="" cssclass="hlStyle" />
					</c:if>
				</s:then>
				<s:else>
					<c:if test="${sessionScope.j_user_bean.azioniPerRiconciliazioneManualeEnabled}">
						<s:hyperlink
							href="eliminaAssociazione.do${formParameters}&chiaveTransazioneMov={1}"
							imagesrc="../applications/templates/riconciliazione/img/elimina.png"
							alt="Elimina" text="" cssclass="hlStyle" />
					</c:if>
				</s:else>
			</s:if>
		</s:dgcolumn>
	</s:datagrid>





	
	<s:div name="divCentered1" cssclass="divTableTitle bold">
			Riepilogo statistico
		</s:div>
	
	<!--<s:div name="divCentered">
		<s:table border="0" cellspacing="0" cellpadding="3">
			<s:thead>
				<s:tr>
					<s:th>
						<b>Riepilogo statistico</b>
					</s:th>
				</s:tr>
			</s:thead>
			<s:tbody>
				<s:tr>
					<s:td>&nbsp;</s:td>
				</s:tr>
			</s:tbody>
		</s:table>
	</s:div>-->
	<s:table cssclass="seda-ui-datagrid" border="1" cellspacing="0"
		cellpadding="3">
		<s:thead>
			<s:tr cssclass="seda-ui-datagridrowdispari">
				<s:th cssclass="seda-ui-datagridcell">Importo Movimento (A)</s:th>
				<s:th cssclass="seda-ui-datagridcell">Importo Netto Totale Transazioni (B)</s:th>
				<s:th cssclass="seda-ui-datagridcell">Squadratura (A) - (B)</s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell text_align_right">
					<fmt:formatNumber type="NUMBER"
						value="${riepilogoTransazioniNonQuadrate.importoMovimenti}"
						minFractionDigits="2" maxFractionDigits="2" />
				</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right">
					<fmt:formatNumber type="NUMBER"
						value="${riepilogoTransazioniNonQuadrate.importoNettoTransazioni}"
						minFractionDigits="2" maxFractionDigits="2" />
				</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right">
					<fmt:formatNumber type="NUMBER"
						value="${riepilogoTransazioniNonQuadrate.squadratura}"
						minFractionDigits="2" maxFractionDigits="2" />
				</s:td>
			</s:tr>
		</s:tbody>
	</s:table>

</c:if>
