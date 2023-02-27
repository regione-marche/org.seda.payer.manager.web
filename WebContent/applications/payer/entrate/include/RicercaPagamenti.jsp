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

	$(function() {
		console.log("controllo ddl js attivo");
		var element;
		if (document.readyState === "complete" || document.readyState === "interactive") {

			/*Tipologia servizio*/
			var dd = document.getElementById('tx_tipologia_servizio');
			for (var i = 0; i < dd.options.length; i++) {
			    if (dd.options[i].value.trim() === document.getElementById("tipServVal").value.trim()) {
			        dd.selectedIndex = i;
			        break;
			    }
			}
			
		    /*Imposta servizio*/
			var dd = document.getElementById('impostaServ');
			for (var i = 0; i < dd.options.length; i++) {
			    if (dd.options[i].value.trim() === document.getElementById("impostaServVal").value.trim()) {
			        dd.selectedIndex = i;
			        break;
			    }
			}
				
		}
	});
	
	function salvaImpostaServizio() {
		console.log("cambio imposta servizio");
		var e = document.getElementById("impostaServ");
		document.getElementById("impostaServVal").value = e.options[e.selectedIndex].value;
		console.log("valore ddl : " + e.options[e.selectedIndex].value);
		console.log("valore impostaServVal : " + document.getElementById("impostaServVal").value);
	}

	function salvaTipologiaServizio() {
		console.log("cambio imposta servizio");
		var e = document.getElementById("tx_tipologia_servizio");
		document.getElementById("tipServVal").value = e.options[e.selectedIndex].value;
		console.log("valore ddl : " + e.options[e.selectedIndex].value);
		console.log("valore impostaServVal : " + document.getElementById("tipServVal").value);
	}
	
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



<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">

		<s:form name="ricercaPagamentiForm" action="ricercaPagamenti.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:textbox name="DDLType" label="DDLType" bmodify="true" text="11" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="ext" label="ext" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none" />
	
			<s:textbox name="impostaServVal" label="imposta servizio" bmodify="false" text="${tx_imposta_servizio_value}" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="tipServVal" label="tipologia servizio" bmodify="false" text="${tx_tipologia_servizio_value}" cssclass="display_none" cssclasslabel="display_none" />
	
			<s:textbox name="hRowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="hPageNumber" label="pageNumberRic" bmodify="true" text="${pageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="hOrder" label="orderRic" bmodify="true" text="${order}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="hExt" label="hExt" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none" />
	
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
									type="submit" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
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
									type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
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
									type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
							</noscript>
				        </s:div>
	
					</s:div>
					
					<!--   -->
							
					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
	
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="impostaServ"
								disable="${ddlImpostaServizioDisabled}" cssclass="tbddlMax floatleft"
								label="Imp.Serv.(I/S):" cssclasslabel="label85 bold textright"
								onchange="salvaImpostaServizio()"
								cachedrowset="listaImpostaServizio" usexml="true"
								valueselected="${impostaServ}">
								<s:ddloption text="Tutti i Servizi" value="" />
								<s:ddloption text="{3}-{4}" value="{1}{2}{3}" />
							</s:dropdownlist>
						</s:div>
						<s:div name="divElement52" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore;digits"
								bmodify="true" name="annoEmissione" label="Anno Emis.:"
								maxlenght="4"
								text="${annoEmissione}"
								cssclass="textareaman"
								cssclasslabel="label85 bold textright" />
						</s:div>
						
						<!-- 
						<s:div name="divElement42" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist  name="modalita" disable="false" label="Modalit&agrave;:" valueselected="${modalita}" 
								                 cssclass="tbddlMax floatleft" cssclasslabel="label85 bold textright">
									<s:ddloption value="" text="Tutte le modalit&agrave;"/>
									<s:ddloption value="A" text="Assegno di traenza"/>
									<s:ddloption value="B" text="Bonifico"/>
									<s:ddloption value="C" text="Cassa/Sportello"/>
								</s:dropdownlist>
						</s:div>
						
						<s:div name="divElement43" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist  name="tipoMovimento" disable="false" label="Tipologia Mov:" valueselected="${tipoMovimento}" 
								                 cssclass="tbddlMax floatleft" cssclasslabel="label85 bold textright">
									<s:ddloption value="" text="Tutte le tipologie"/>
									<s:ddloption value="N" text="Pagamento"/>
									<s:ddloption value="E" text="Eccedenze"/>
									<s:ddloption value="P" text="Preavviso"/>
									<s:ddloption value="S" text="Sgravio"/>
									<s:ddloption value="A" text="Abbuono"/>
									<s:ddloption value="D" text="Discarico"/>
									<s:ddloption value="I" text="Rimborso in corso"/>
									<s:ddloption value="R" text="Rimborso eseguito"/>
								</s:dropdownlist>
						</s:div>
						-->
						<s:div name="divElement51" cssclass="divRicMetadatiDoubleRow">
							<s:div name="divElement51a" cssclass="labelData">
								<s:label name="label_data_pagamento" cssclass="seda-ui-label label85 bold textright" text="Data Pagamento" />
							</s:div>
							
							<s:div name="divElement51b" cssclass="floatleft">
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
	
					<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
						<!-- 
						<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="ufficioImpositore"
								disable="${ddlUtenteImpositoreDisabled}" cssclass="tbddlMax floatleft"
								label="Ufficio Impositore:" cssclasslabel="label85 bold textright"
								cachedrowset="listaUfficioImpositore" usexml="true"
								valueselected="${ufficioImpositore}">
								<s:ddloption text="Tutti gli Uffici" value="" />
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
						</s:div>
						 -->
						 <s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="tx_tipologia_servizio" disable="false"
								cssclass="tbddlMax floatleft" label="Tip. Servizio:"
								cssclasslabel="label85 bold textright"
								cachedrowset="listaTipologieServizio" usexml="true"
								onchange="setFiredButton('tx_button_tipo_servizio_changed');salvaTipologiaServizio();this.form.submit();"
								valueselected="${tx_tipologia_servizio}">
								<s:ddloption text="Tutte le tipologie" value="" />
								<s:ddloption text="{1}-{2}" value="{1}_{3}" />
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_tipo_servizio_changed"
									disable="false" onclick="" text=""
									type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
							</noscript>
						</s:div>
						<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
									bmodify="true" name="numEmissione" label="Numero Emis.:"
									maxlenght="10"
									text="${numEmissione}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
						</s:div>
					</s:div>
	
					<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
						<s:div name="divElement41" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
								bmodify="true" name="codFiscale" label="Cod. Fiscale:"
								maxlenght="16"
								text="${codFiscale}"
								cssclass="textareaman"
								cssclasslabel="label85 bold textright" />
						</s:div>
						
	
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
									bmodify="true" name="id_documento" label="Numero Doc.:"
									maxlenght="20"
									text="${id_documento}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
						</s:div>
						
						<!--
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore;digits"
									bmodify="true" name="progRiscossione" label="Numero Risc.:"
									maxlenght="10"
									text="${progRiscossione}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
						</s:div>
						-->
	
					</s:div>
	
						
				</s:div>
	
				<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					<s:button id="tx_button_cerca" type="submit" text="Cerca" onclick="" cssclass="btnStyle"/>
					<s:button id="tx_button_reset" type="submit" text="Reset" onclick="" cssclass="btnStyle" />
	
					<c:if test="${!empty listaPagamenti}">
						<s:button id="tx_button_download" type="submit" text="Download" onclick="" cssclass="btnStyle" />
					</c:if>
	
	<!-- 
					<c:if test="${!empty listaPagamenti && ext=='0'}">
						<s:button id="tx_button_cerca_exp" type="submit" text="Espandi" onclick="" cssclass="btnStyle" />
					</c:if>
					<c:if test="${!empty listaPagamenti && ext=='1'}">
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
		Elenco Pagamenti
	</s:div>

	<s:div name="divRisultati" cssclass="divRisultati">

		<s:datagrid  viewstate="" cachedrowset="listaPagamenti" action="ricercaPagamenti.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
			<s:action>
				<c:url value="ricercaPagamenti.do">
					<c:if test="${!empty tx_societa}">  
						<c:param name="tx_societa">${tx_societa}</c:param>
					</c:if>
					<c:if test="${!empty tx_utente}">  
						<c:param name="tx_utente">${tx_utente}</c:param>
					</c:if>
					<c:if test="${!empty tx_UtenteEnte}">  
						<c:param name="tx_UtenteEnte">${tx_UtenteEnte}</c:param>
					</c:if>
					<c:if test="${!empty impostaServ}">  
						<c:param name="impostaServ">${impostaServ}</c:param>
					</c:if>
					<c:if test="${!empty codFiscale}">  
						<c:param name="codFiscale">${codFiscale}</c:param>
					</c:if>
					<c:if test="${!empty modalita}">  
						<c:param name="modalita">${modalita}</c:param>
					</c:if>
					<c:if test="${!empty tipoMovimento}">  
						<c:param name="tipoMovimento">${tipoMovimento}</c:param>
					</c:if>
					<c:if test="${!empty ufficioImpositore}">  
						<c:param name="ufficioImpositore">${ufficioImpositore}</c:param>
					</c:if>
					<c:if test="${!empty annoEmissione}">  
						<c:param name="annoEmissione">${annoEmissione}</c:param>
					</c:if>
					<c:if test="${!empty numEmissione}">  
						<c:param name="numEmissione">${numEmissione}</c:param>
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
					<c:if test="${!empty tx_tipologia_servizio}">  
						<c:param name="tx_tipologia_servizio">${tx_tipologia_servizio}</c:param>
					</c:if>
					<c:if test="${!empty id_documento}">  
						<c:param name="id_documento">${id_documento}</c:param>
					</c:if>
					<c:if test="${!empty progRiscossione}">  
						<c:param name="progRiscossione">${progRiscossione}</c:param>
					</c:if>
					<c:if test="${!empty ext}">
						<c:param name="ext">${ext}</c:param>
					</c:if>
					<c:param name="DDLType">11</c:param>
				</c:url>
			</s:action>
	
	
	  	    <!-- SOC_CSOCCSOC -->
			<s:dgcolumn index="1" label="Societ&agrave;"  asc="CSOCA" desc="CSOCD"></s:dgcolumn>
	
	  	    <!-- EH3_CUTECUTE -->
			<s:dgcolumn index="2" label="Utente"  asc="CUTEA" desc="CUTED"></s:dgcolumn>
	
	  	    <!-- EH3_CANECENT -->
			<s:dgcolumn index="3" label="Ente"  asc="CENTA" desc="CENTD"></s:dgcolumn>
	
	  	    <!-- EH3_CISECISE -->
			<s:dgcolumn index="4" label="I/S"  asc="CISEA" desc="CISED"></s:dgcolumn>
	
	  	    <!-- EH1_CEH1ADOC -->
			<s:dgcolumn index="5" label="Anno"  asc="ADOCA" desc="ADOCD"></s:dgcolumn>
	
	  	    <!-- EH1_CEH1EMIS -->
			<s:dgcolumn index="6" label="Numero"  asc="EMISA" desc="EMISD"></s:dgcolumn>
	
	  	    <!-- EH3_TANETUFF/EH3_CANECUFF -->
			<s:dgcolumn label="Ufficio" asc="UFFIA" desc="UFFID">
				<s:if left="{7}{8}" control="eq" right="">
						<s:then>
						</s:then>
						<s:else>
							{7}/{8}
						</s:else>
				</s:if>		    		
			</s:dgcolumn>
	
	  	    <!-- EH1_CTSECTSE -->
			<s:dgcolumn index="9" label="Tip.Serv." asc="CTSEA" desc="CTSED"></s:dgcolumn>
	
	        <!-- EH3_GEH3MOVI -->
			<s:dgcolumn format="dd/MM/yyyy" index="10" label="Data Pag."  asc="DMOVA" desc="DMOVD"></s:dgcolumn>
	
			<!-- EH1_CEH1CFIS -->
			<s:dgcolumn index="11" label="Cod.Fiscale" asc="CFISA" desc="CFISD"></s:dgcolumn>
	
			<!-- EH3_CEH3NDOC -->
			<s:dgcolumn index="12" label="Documento" asc="NDOCA" desc="NDOCD"></s:dgcolumn>
	
			<!-- EH3_PEH3PAGA -->
			<s:dgcolumn index="13" label="N.r." asc="PPAGA" desc="PPAGD" css="text_align_right"></s:dgcolumn>
	
			<!-- EH3_CEH3CANA -->
			<s:dgcolumn label="Mod." asc="DMOVA" desc="DMOVD" ><s:hyperlink text="{19}" alt="{14}" href=""/></s:dgcolumn>
	
			<!-- EH3_CEH3CAUS -->
			<s:dgcolumn label="Tipo Mov." asc="TMOVA" desc="TMOVD">
				<s:if left="{15}" control="eq" right="PAGAMENTO">
					<s:then>
						<s:hyperlink text="PG" alt="{15}" href=""/>
					</s:then>
					<s:else>
						<s:hyperlink text="{20}" alt="{15}" href=""/>
		 			</s:else>
				</s:if>
			</s:dgcolumn>
	
			<!-- EH3_CEH3SEGN -->
			<s:dgcolumn index="16" label="Segno" asc="SEGNA" desc="SEGND"></s:dgcolumn>
	
			<!-- EH3_IEH3PAGA -->
			<s:dgcolumn index="17" format="#,##0.00" label="Imp.Tri." asc="TRIBA" desc="TRIBD" css="text_align_right"></s:dgcolumn>
	
			<!-- EH3_MORA -->
			<s:dgcolumn index="18" format="#,##0.00" label="Imp.Mora" asc="MORAA" desc="MORAD" css="text_align_right"></s:dgcolumn>
	
	        <s:ifdatagrid left="${ext}" control="eq" right="1">
			</s:ifdatagrid>
					
	    </s:datagrid>


	</s:div>

	<s:div name="divDatiAgg" cssclass="divTableTitle bold text_align_right">
		<c:if test="${userProfile=='AMEN'}" >
				 Dati aggiornati al: <s:formatDate pattern="dd/MM/yyyy" value="${dataAggiornamento}"/>
		</c:if>
	</s:div>


</c:if>

