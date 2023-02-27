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
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>


<script type="text/javascript" >

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

<fmt:setLocale value="it_IT"/>
 <s:div name="div_selezione" cssclass="div_align_center divSelezione">
			<s:form name="ricercaRiversamentiDettContrForm" action="ricercaRiversamenti.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			</s:form>
	</s:div>
	
<s:div name="div_documento" cssclass="div_align_center divSelezione">

	<!-- 	tabella dettaglio	 -->
	
	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Dettaglio Emissione
	</s:div>
	
	<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
		<s:thead>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:th cssclass="seda-ui-datagridheadercell" icol="4"><b>Emissione</b></s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Societ&agrave;: ${dettSoc}</s:td>
				<s:td cssclass="seda-ui-datagridcell">Utente: ${dettUte}</s:td>
				<s:td cssclass="seda-ui-datagridcell" icol="2">Ente: ${dettEnte}</s:td>
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Imposta Servizio (I/S)</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettImpostaServ}</s:td>	
				<s:td cssclass="seda-ui-datagridcell">Tipologia Servizio</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettTipoServ}</s:td>				
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Tipo/Codice Uff.Impositore</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettUffImp}</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Num.Docum.Rendicontati</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettTotDocRend}</s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Anno Emissione</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettAnnoEmis}</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Numero Emissione</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettNumEmis}</s:td>				
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Numero Documenti</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettTotDoc}</s:td>
				<s:td cssclass="seda-ui-datagridcell"></s:td>
				<s:td cssclass="seda-ui-datagridcell"></s:td>
			</s:tr>
	</s:tbody>
	</s:table>
	<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
		<s:thead>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:th cssclass="seda-ui-datagridheadercell" icol="4"><b>Importi</b></s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Carico</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoCarico}" minFractionDigits="2" maxFractionDigits="2"/></s:td>				
				<%--
				<s:td cssclass="seda-ui-datagridcell">Rendicontato</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoRendiconto}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
				--%>
				<s:td cssclass="seda-ui-datagridcell">Residuo</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoTotaleResiduo}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Sgravato</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoDiminuzioneCarico}" minFractionDigits="2" maxFractionDigits="2"/></s:td>				
				<%--
				<s:td cssclass="seda-ui-datagridcell">Residuo</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoTotaleResiduo}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
				--%>
				<s:td cssclass="seda-ui-datagridcell">Residuo Scaduto</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totResScaduto}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Riscosso</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoRiscosso}" minFractionDigits="2" maxFractionDigits="2"/></s:td>				
				<%--
				<s:td cssclass="seda-ui-datagridcell">Residuo Scaduto</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totResScaduto}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
				--%>
				<s:td cssclass="seda-ui-datagridcell"></s:td>
				<s:td cssclass="seda-ui-datagridcell"></s:td>				
			</s:tr>
			<%--
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Rimborso</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoTotaleRimborso}" minFractionDigits="2" maxFractionDigits="2"/></s:td>				
				<s:td cssclass="seda-ui-datagridcell"></s:td>
				<s:td cssclass="seda-ui-datagridcell"></s:td>
			</s:tr>
			--%>
		</s:tbody>
	</s:table>
	<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
		<s:thead>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:th cssclass="seda-ui-datagridheadercell" icol="4"><b>Statistiche</b></s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Riscosso su Carico</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${percRiscossoCarico}" minFractionDigits="2" maxFractionDigits="2"/>%</s:td>				
				<%--
				<s:td cssclass="seda-ui-datagridcell">Sgravato su Carico</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${percSgravatoCarico}" minFractionDigits="2" maxFractionDigits="2"/>%</s:td>
				--%>
				<s:td cssclass="seda-ui-datagridcell">Residuo su Carico</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${percResiduoCarico}" minFractionDigits="2" maxFractionDigits="2"/>%</s:td>
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<%--
				<s:td cssclass="seda-ui-datagridcell">Rendicontato su Carico</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${percRendicontatoCarico}" minFractionDigits="2" maxFractionDigits="2"/>%</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Residuo su Carico</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${percResiduoCarico}" minFractionDigits="2" maxFractionDigits="2"/>%</s:td>
				--%>
				<s:td cssclass="seda-ui-datagridcell">Sgravato su Carico</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${percSgravatoCarico}" minFractionDigits="2" maxFractionDigits="2"/>%</s:td>
				<s:td cssclass="seda-ui-datagridcell">Residuo Scaduto su Carico</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${percResiduoScadCarico}" minFractionDigits="2" maxFractionDigits="2"/>%</s:td>
			</s:tr>
			<%--
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Rimborso su Carico</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${percRimborsoCarico}" minFractionDigits="2" maxFractionDigits="2"/>%</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Residuo Scaduto su Carico</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${percResiduoScadCarico}" minFractionDigits="2" maxFractionDigits="2"/>%</s:td>				
			</s:tr>
			--%>
		</s:tbody>
	</s:table>
</s:div>
	<s:div name="indietro" cssclass="divRicBottoni">
		<s:form name="dettaglioEmissione" action="ricercaEmissioni.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		
			<s:textbox name="rowsPerPage" label="rowsPerPageRic2" bmodify="true" text="${rRowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="pageNumber" label="pageNumberRic2" bmodify="true" text="${rPageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="order" label="orderRic2" bmodify="true" text="${rOrder}" cssclass="display_none" cssclasslabel="display_none"  />
	
			<s:textbox name="tx_tipologia_servizio" label="tx_tipologia_servizioRic" bmodify="true" text="${tx_tipologia_servizio}" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="tx_societa" label="tx_societaRic" bmodify="true" text="${tx_societa}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tx_utente" label="tx_utenteRic" bmodify="true" text="${tx_utente}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tx_UtenteEnte" label="tx_UtenteEnteRic" bmodify="true" text="${tx_UtenteEnte}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="impostaServ" label="ImpostaServRic" bmodify="true" text="${impostaServ}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="annoEmissione" label="annoEmissioneRic" bmodify="true" text="${annoEmissione}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="numEmissione" label="numEmissioneRic" bmodify="true" text="${numEmissione}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="ufficioImpositore" label="ufficioImpositoreRic" bmodify="true" text="${ufficioImpositore}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CHIAVI DI RICERCA chiaveCodUte={7}&chiaveCodEnte={8}&chiaveTipoUff={4}
									&chiaveCodUff={5}&chiaveIS={1}&chiaveAnnoE={2}&chiaveNumeroE={3}-->

			<!-- CODICE UTENTE EH1_CUTECUTE -->
			<s:textbox name="chiaveCodUte" label="chiaveCodUteDett" bmodify="true" text="${chiaveCodUte}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CODICE ENTE EH1_CANECENT -->
			<s:textbox name="chiaveCodEnte" label="chiaveCodEnteDett" bmodify="true" text="${chiaveCodEnte}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- TIPO UFFICIO EH1_TANETUFF -->
			<s:textbox name="chiaveTipoUff" label="chiaveTipoUffDett" bmodify="true" text="${chiaveTipoUff}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CODICE UFFICIO EH1_CANECUFF -->
			<s:textbox name="chiaveCodUff" label="chiaveCodUffDett" bmodify="true" text="${chiaveCodUff}" cssclass="display_none" cssclasslabel="display_none"  />
			 <!--	IMPOSTA SERVIZIO  EH1_CISECISE -->
			<s:textbox name="chiaveIS" label="chiaveISDett" bmodify="true" text="${chiaveIS}" cssclass="display_none" cssclasslabel="display_none"  />
			 <!-- ANNO EMISSIONE -->
			<s:textbox name="chiaveAnnoE" label="chiaveAnnoE" bmodify="true" text="${chiaveAnnoE}" cssclass="display_none" cssclasslabel="display_none"  />
			 <!-- NUMERO EMISSIONE -->
			<s:textbox name="chiaveNumeroE" label="chiaveNumeroE" bmodify="true" text="${chiaveNumeroE}" cssclass="display_none" cssclasslabel="display_none"  />
	
			<s:textbox name="ext" label="extRic" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none"  />
	
			<!-- CHIAVE I/S EH1_CISECISE -->
			<s:textbox name="dettImpostaServ" label="dettEnte" bmodify="true" text="${dettImpostaServ}" cssclass="display_none" cssclasslabel="display_none"  />

			<s:button id="tx_button_cerca" cssclass="btnStyle" type="submit" text="Indietro" onclick=""/>

		</s:form>		
	</s:div>

	