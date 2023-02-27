<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
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
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#data_registrazione_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_registrazione_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_registrazione_da_day_id").val(dateText.substr(0,2));
												$("#data_registrazione_da_month_id").val(dateText.substr(3,2));
												$("#data_registrazione_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_registrazione_da_day_id",
				                            "data_registrazione_da_month_id",
				                            "data_registrazione_da_year_id",
				                            "data_registrazione_da_hidden");
			}
		});
		$("#data_registrazione_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_registrazione_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_registrazione_a_day_id").val(dateText.substr(0,2));
												$("#data_registrazione_a_month_id").val(dateText.substr(3,2));
												$("#data_registrazione_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_registrazione_a_day_id",
				                            "data_registrazione_a_month_id",
				                            "data_registrazione_a_year_id",
				                            "data_registrazione_a_hidden");
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


	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
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

		function switchVisibility() {     
		   var rule =  getCSSRule('.display_none');
		   if (rule.style.display == 'none') {
			   rule.style.display = 'inline';
		   } else {
		   	rule.style.display = 'none';
		   }
		}                                   
</script>


	<c:url value="" var="formParameters">
		<c:param name="test">ok</c:param>
		<c:if test="${!empty rowsPerPage}">
			<c:param name="rowsPerPage">${rowsPerPage}</c:param> 
		</c:if>
		<c:if test="${!empty pageNumber}">
			<c:param name="pageNumber">${pageNumber}</c:param> 
		</c:if>
		<c:if test="${!empty order}">
			<c:param name="order">${order}</c:param> 
		</c:if>
		<c:if test="${!empty tx_societa}">  
			<c:param name="tx_societa">${tx_societa}</c:param>
		</c:if>
		<c:if test="${!empty tx_utente}">  
			<c:param name="tx_utente">${tx_utente}</c:param>
		</c:if>
		<c:if test="${!empty tx_UtenteEnte}">  
			<c:param name="tx_UtenteEnte">${tx_UtenteEnte}</c:param>
		</c:if>
		<c:if test="${!empty annoRuolo}">  
			<c:param name="annoRuolo">${annoRuolo}</c:param>
		</c:if>
		<c:if test="${!empty numRuolo}">  
			<c:param name="numRuolo">${numRuolo}</c:param>
		</c:if>
		<c:if test="${!empty data_registrazione_da_day}">  
			<c:param name="data_registrazione_da_day">${data_registrazione_da_day}</c:param>
		</c:if>
		<c:if test="${!empty data_registrazione_da_month}">  
			<c:param name="data_registrazione_da_month">${data_registrazione_da_month}</c:param>
		</c:if>
		<c:if test="${!empty data_registrazione_da_year}">  
			<c:param name="data_registrazione_da_year">${data_registrazione_da_year}</c:param>
		</c:if>
		<c:if test="${!empty data_registrazione_a_day}">  
			<c:param name="data_registrazione_a_day">${data_registrazione_a_day}</c:param>
		</c:if>
		<c:if test="${!empty data_registrazione_a_month}">  
			<c:param name="data_registrazione_a_month">${data_registrazione_a_month}</c:param>
		</c:if>
		<c:if test="${!empty data_registrazione_a_year}">  
			<c:param name="data_registrazione_a_year">${data_registrazione_a_year}</c:param>
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
		<c:if test="${!empty codFisc}">  
			<c:param name="codFisc">${codFisc}</c:param>
		</c:if>

		<c:if test="${!empty ext}">
			<c:param name="ext">${ext}</c:param>
		</c:if>
		<!-- 
		<c:if test="${!empty rRowsPerPage}">
			<c:param name="rRowsPerPage">${param.rRowsPerPage}</c:param> 
		</c:if>
		<c:if test="${!empty rPageNumber}">
			<c:param name="rPageNumber">${rPageNumber}</c:param> 
		</c:if>
		<c:if test="${!empty rOrder}">
			<c:param name="order">${param.rOrder}</c:param> 
		</c:if>
		 -->
	</c:url>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">

		<s:form name="ricercaPagamentiForm" action="ricercaPagamenti.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:textbox name="DDLType" label="DDLType" bmodify="true" text="11" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="ext" label="ext" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none" />
	<!--  
			<s:textbox name="hRowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="hPageNumber" label="pageNumberRic" bmodify="true" text="${pageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="hOrder" label="orderRic" bmodify="true" text="${order}" cssclass="display_none" cssclasslabel="display_none"  />
	-->
				<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
						Ricerca Pagamenti
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
									type="submit" cssclass="btnimgStyle"  title="Aggiorna"  />
							</noscript>
				        </s:div>
	
						<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
							<s:dropdownlist name="tx_utente"
								disable="${ddlUtenteDisabled}" cssclass="tbddl floatleft"
								label="Utente:"
								cssclasslabel="label65 bold floatleft textright"
								cachedrowset="listaUtenti" usexml="true"
								onchange="setFiredButton('tx_button_utente_changed');this.form.submit();"
								valueselected="${tx_utente}">
								<s:ddloption text="Tutti gli Utenti" value="" />
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_utente_changed"
									disable="${ddlUtenteDisabled}" onclick="" text=""
									type="submit" cssclass="btnimgStyle" title="Aggiorna"	 />
							</noscript>
				        </s:div>
						<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
							<s:dropdownlist name="tx_UtenteEnte"
								disable="${ddlUtenteEnteDisabled}" cssclass="tbddlMax floatleft"
								label="Ente:" cssclasslabel="label65 bold textright"
								cachedrowset="listaUtentiEnti" usexml="true"
								onchange="setFiredButton('tx_button_impositore_changed');this.form.submit();"
								valueselected="${tx_UtenteEnte}">
								<s:ddloption text="Tutti gli Enti" value="" />
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
				        <noscript>
							<s:button id="tx_button_impositore_changed"
								disable="${ddlUtenteEnteDisabled}" onclick="" text=""
								type="submit" cssclass="btnimgStyle" title="Aggiorna" />
						</noscript>
				        
				        </s:div>
	
					</s:div>
					
					<!--   -->
					
					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
							<!-- 
							<s:dropdownlist name="ufficioImpositore"
								disable="${ddlUtenteImpositoreDisabled}" cssclass="tbddlMax floatleft"
								label="Ufficio Impositore:" cssclasslabel="label85 bold textright"
								cachedrowset="listaUfficioImpositore" usexml="true"
								valueselected="${ufficioImpositore}">
								<s:ddloption text="Tutti gli Uffici" value="" />
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							 -->
	
							<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
								<s:textbox validator="ignore"
										bmodify="true" name="codFisc" label="Codice Fiscale:"
										maxlenght="16"
										text="${codFisc}"
										cssclass="textareaman"
										cssclasslabel="label85 bold textright" />
							</s:div>
							<s:div name="divElement5" cssclass="divRicMetadatiDoubleRow">
								<s:div name="divElement5a" cssclass="labelData">
									<s:label name="label_data_registrazione" cssclass="seda-ui-label label85 bold textright" text="Data Registrazione" />
								</s:div>
								<s:div name="divElement5b" cssclass="floatleft">
									<s:div name="divDataRegistrazioneDa" cssclass="divDataDa">
										<s:date label="Da:" prefix="data_registrazione_da" yearbegin="${ddlDateAnnoDa}"
											yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
											separator="/" calendar="${data_registrazione_da}"
											validator="required;dateISO" showrequired="true"
											message="[required=Data Registrazione Da: Questo valore è obbligatorio£dateISO=Data Registrazione Da: ${msg_dataISO_valida}]"
											cssclasslabel="labelsmall"
											cssclass="dateman">
										</s:date>
										<input type="hidden" id="data_registrazione_da_hidden" value="" />
									</s:div>
									
									<s:div name="divDataRegistrazioneA" cssclass="divDataA">
										<s:date label="A:" prefix="data_registrazione_a" yearbegin="${ddlDateAnnoDa}"
											yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
											separator="/" calendar="${data_registrazione_a}"
											validator="required;dateISO" showrequired="true"
											message="[required=Data Registrazione A: Questo valore è obbligatorio£dateISO=Data Registrazione A: ${msg_dataISO_valida}]"
											cssclasslabel="labelsmall"
											cssclass="dateman" >
										</s:date>
										<input type="hidden" id="data_registrazione_a_hidden" value="" />
									</s:div>
								</s:div>
						</s:div>
							
					</s:div>	
					
					<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
							<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
								<s:textbox validator="ignore"
									bmodify="true" name="annoRuolo" label="Anno Ruolo:"
									maxlenght="4"
									text="${annoRuolo}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
							</s:div>
							<s:div name="divElement5s" cssclass="divRicMetadatiDoubleRow">
								<s:div name="divElement5t" cssclass="labelData">
									<s:label name="label_data_pagamento" cssclass="seda-ui-label label85 bold textright" text="Data Effettivo Pagamento" />
								</s:div>
								<s:div name="divElement5v" cssclass="floatleft">
									<s:div name="divDataPagamentoDa" cssclass="divDataDa">
										<s:date label="Da:" prefix="data_pagamento_da" yearbegin="${ddlDateAnnoDa}"
											yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
											separator="/" calendar="${data_pagamento_da}"
											validator="required;dateISO" showrequired="true"
											message="[required=Data Pagamento Da: Questo valore è obbligatorio£dateISO=Data Pagamento Da: ${msg_dataISO_valida}]"
											cssclasslabel="labelsmall"
											cssclass="dateman">
										</s:date>
										<input type="hidden" id="data_pagamento_da_hidden" value="" />
									</s:div>
									
									<s:div name="divDataPagamentoA" cssclass="divDataA">
										<s:date label="A:" prefix="data_pagamento_a" yearbegin="${ddlDateAnnoDa}"
											yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
											separator="/" calendar="${data_pagamento_a}"
											validator="required;dateISO" showrequired="true"
											message="[required=Data Pagamento A: Questo valore è obbligatorio£dateISO=Data Pagamento A: ${msg_dataISO_valida}]"
											cssclasslabel="labelsmall"
											cssclass="dateman" >
										</s:date>
										<input type="hidden" id="data_pagamento_a_hidden" value="" />
									</s:div>
								</s:div>
						</s:div>
							
					</s:div>
					<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
						<s:div name="divElement61" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
									bmodify="true" name="numRuolo" label="Num. Ruolo:"
									maxlenght="10"
									text="${numRuolo}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
						</s:div>
	 			  </s:div>
				</s:div>
	
	
				<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					<s:button id="tx_button_cerca" type="submit" text="Cerca" onclick="" cssclass="btnStyle"/>
					<s:button id="tx_button_reset" type="submit" text="Reset" onclick="" cssclass="btnStyle" validate="false"/>
	
					<c:if test="${!empty listaPagamenti}">
						<s:button id="tx_button_download" type="submit" text="Download" onclick="" cssclass="btnStyle" />
					</c:if>
					<s:textbox name="reloadPagamenti" label="reloadPagamenti" bmodify="true" text="ok" cssclass="display_none" cssclasslabel="display_none"  />
	
	<!-- 
					<c:if test="${!empty listaDocumenti && ext=='0'}">
						<s:button id="tx_button_cerca_exp" type="submit" text="Espandi" onclick="" cssclass="btnStyle" />
					</c:if>
					<c:if test="${!empty listaDocumenti && ext=='1'}">
						<s:button id="tx_button_cerca_exp" type="submit" text="Contrai" onclick="" cssclass="btnStyle" />
					</c:if>
	 -->
					<s:textbox name="fired_button_hidden" label="fired_button_hidden"
						bmodify="true" text="" cssclass="rend_display_none"
						cssclasslabel="rend_display_none" />
				</s:div>
	
		</s:form>

	</s:div>
</s:div>
	
<!--  Presentazione risultati -->	

<!-- colonne -->	

<c:if test="${!empty listaPagamenti}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Lista Pagamenti
	</s:div>

	<s:div name="divRisultati" cssclass="divRisultati">

		<s:datagrid  viewstate="" cachedrowset="listaPagamenti" action="ricercaPagamenti.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
			<s:action>
				<c:url value="ricercaPagamenti.do" >
			<c:if test="${!empty tx_societa}">  
				<c:param name="tx_societa">${tx_societa}</c:param>
			</c:if>
			<c:if test="${!empty tx_utente}">  
				<c:param name="tx_utente">${tx_utente}</c:param>
			</c:if>
			<c:if test="${!empty tx_UtenteEnte}">  
				<c:param name="tx_UtenteEnte">${tx_UtenteEnte}</c:param>
			</c:if>
			<c:if test="${!empty annoRuolo}">  
				<c:param name="annoRuolo">${annoRuolo}</c:param>
			</c:if>
			<c:if test="${!empty numRuolo}">  
				<c:param name="numRuolo">${numRuolo}</c:param>
			</c:if>
			<c:if test="${!empty data_registrazione_da_day}">  
				<c:param name="data_registrazione_da_day">${data_registrazione_da_day}</c:param>
			</c:if>
			<c:if test="${!empty data_registrazione_da_month}">  
				<c:param name="data_registrazione_da_month">${data_registrazione_da_month}</c:param>
			</c:if>
			<c:if test="${!empty data_registrazione_da_year}">  
				<c:param name="data_registrazione_da_year">${data_registrazione_da_year}</c:param>
			</c:if>
			<c:if test="${!empty data_registrazione_a_day}">  
				<c:param name="data_registrazione_a_day">${data_registrazione_a_day}</c:param>
			</c:if>
			<c:if test="${!empty data_registrazione_a_month}">  
				<c:param name="data_registrazione_a_month">${data_registrazione_a_month}</c:param>
			</c:if>
			<c:if test="${!empty data_registrazione_a_year}">  
				<c:param name="data_registrazione_a_year">${data_registrazione_a_year}</c:param>
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
			<c:if test="${!empty codFisc}">  
				<c:param name="codFisc">${codFisc}</c:param>
			</c:if>
			<c:if test="${!empty reloadPagamenti}">  
				<c:param name="reloadPagamenti">${reloadPagamenti}</c:param>
			</c:if>
			
			<c:if test="${!empty tx_button_cerca}">
				<c:param name="tx_button_cerca">OK</c:param> 
			</c:if>
			
			
			<c:param name="DDLType">11</c:param>
			</c:url>
			</s:action>
			
			<!-- SOCIETA -->
			<s:dgcolumn index="11"  label="Societ&agrave;" asc="SOCA" desc="SOCD"></s:dgcolumn>
			
			<!-- UTENTE -->
			<s:dgcolumn index="12"  label="Utente" asc="UTEA" desc="UTED"></s:dgcolumn>
	
			<!-- ENTE -->
			<s:dgcolumn index="13"  label="Ente" asc="ENTA" desc="ENTD"></s:dgcolumn>
			
			<!-- RMO_CRMOANNO -->
			<s:dgcolumn index="1"  label="Anno" asc="DANA" desc="DAND"></s:dgcolumn>
	
	  	    <!-- RMO_NRMONUME -->
			<s:dgcolumn index="2" label="Nr"  asc="DNMA" desc="DNMD"></s:dgcolumn>
	
	  	    <!-- RRU_CRRUTUFF RRU_CRRUCUFF -->
			<s:dgcolumn label="UFF" asc="UFFA" desc="UFFD">
							{3}/{4}
			</s:dgcolumn>
			<!-- RPA_CRPACFIS -->
			<s:dgcolumn index="5" label="Codice Fiscale" asc="CFSA" desc="CFSD"></s:dgcolumn>
			
			<!-- RMO_GRMOGREG -->
			<s:dgcolumn index="7" format="dd/MM/yyyy" label="Dt Registraz" asc="DEPA" desc="DEPD"></s:dgcolumn>
	
			<!-- RMO_GRMOGEFF -->
			<s:dgcolumn index="6" format="dd/MM/yyyy" label="Dt Pagamento" asc="DTRA" desc="DTRD"></s:dgcolumn>
	
			<!-- SEGNO -->
			<s:dgcolumn index="8" label="Segno"></s:dgcolumn>
			
			<!-- RMO_IRMOIART -->
			<s:dgcolumn index="9" format="#,##0.00" label="Imp Articolo" asc="IMPA" desc="IMPD" css="text_align_right"></s:dgcolumn>
	
			<!-- RMO_IRMOIMOR -->
			<s:dgcolumn index="10" format="#,##0.00" label="Mora" asc="MORA" desc="MORD" css="text_align_right"></s:dgcolumn>
	
	        <s:ifdatagrid left="${ext}" control="eq" right="1">
			</s:ifdatagrid>
	
	    </s:datagrid>
		<!-- tabella riassunto -->
	
		
	
			 <%--<fmt:formatDate value="${dataUltimoAgg}" pattern="dd/MM/yyyy"/>--%>
		<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
			<s:thead>
				<s:tr cssclass="seda-ui-datagridrowpari">
					<s:th cssclass="seda-ui-datagridheadercell"  icol="8"><b>Totali Pagamenti</b></s:th>
				</s:tr>
			</s:thead>
			<s:tbody>
				<s:tr cssclass="seda-ui-datagridrowpari">
						<s:td cssclass="seda-ui-datagridcell">Imp. Articoli</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoArticoli}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
						<s:td cssclass="seda-ui-datagridcell">Imp. Mora</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoMora}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
	
				</s:tr>
			</s:tbody>
		</s:table>

	</s:div>


</c:if>
		<s:div name="divDatiAgg" cssclass="divTableTitle bold text_align_right">
			<c:if test="${userProfile=='AMEN'}" >
					 Dati aggiornati al: <s:formatDate pattern="dd/MM/yyyy" value="${dataAggiornamento}"/>
			</c:if>
		</s:div>



			  