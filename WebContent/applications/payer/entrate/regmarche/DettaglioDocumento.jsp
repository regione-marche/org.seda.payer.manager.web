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

<c:if test="${!empty dettaglioAnag}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Dettaglio Anagrafica
	</s:div>

	<s:div name="divDettaglioAnag" cssclass="divRisultati">

	<s:datagrid  viewstate="" cachedrowset="dettaglioAnag" action="nessuna.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
		<s:action>
			<c:url value="nessuna.do">				
			</c:url>
		</s:action>

  	    <!-- SOC_CSOCCSOC -->
		<s:dgcolumn index="1" label="Societ&agrave;"></s:dgcolumn>

  	    <!-- EH3_CUTECUTE -->
		<s:dgcolumn index="4" label="Utente"></s:dgcolumn>

  	    <!-- EH3_CANECENT -->
		<s:dgcolumn index="7" label="Ente"></s:dgcolumn>

  	    <!-- EH3_CISECISE -->
		<s:dgcolumn index="10" label="I/S"></s:dgcolumn>

		<!-- EH1_CEH1CFIS -->
		<s:dgcolumn index="11" label="Cod.Fisc."></s:dgcolumn>

       <!-- EH8_DEH8DENO -->	
		<s:dgcolumn index="12" label="Denominazione"></s:dgcolumn>

       <!-- APC_DAPCDCOM -->	
		<s:dgcolumn index="20" label="Comune"></s:dgcolumn>

       <!-- EH8_DEH8INDI -->
		<s:dgcolumn index="17" label="Indirizzo"></s:dgcolumn>

       <!-- APC_CAPCCCAP -->	
		<s:dgcolumn index="21" label="CAP"></s:dgcolumn>

       <!-- APC_CAPCSIGL -->	
		<s:dgcolumn index="22" label="Prov"></s:dgcolumn>

        <s:ifdatagrid left="${ext}" control="eq" right="1">
		</s:ifdatagrid>
		
	</s:datagrid>

</s:div>
	
</c:if>


<c:if test="${!empty dettaglioEmi}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle2" cssclass="divTableTitle bold">
		Dettaglio Emissione
	</s:div>

	<s:div name="divDettaglioEmi" cssclass="divRisultati">

	<s:datagrid  viewstate="" cachedrowset="dettaglioEmi" action="nessuna.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
		<s:action>
			<c:url value="nessuna.do">				
			</c:url>
		</s:action>

  	    <!-- SOC_CSOCCSOC -->
		<s:dgcolumn index="1" label="Societ&agrave;"></s:dgcolumn>

  	    <!-- EH3_CUTECUTE -->
		<s:dgcolumn index="2" label="Utente"></s:dgcolumn>

  	    <!-- EH3_CANECENT -->
		<s:dgcolumn index="3" label="Ente"></s:dgcolumn>

  	    <!-- EH3_CISECISE -->
		<s:dgcolumn index="4" label="I/S"></s:dgcolumn>

       <!-- EH1_CEH1ADOC -->	
		<s:dgcolumn index="5" label="Anno"></s:dgcolumn>

        <!-- EH1_CEH1EMIS -->
		<s:dgcolumn index="6" label="Numero"></s:dgcolumn>

        <!-- EH1_TANETUFF/EH1_CANECUFF -->
     	<s:dgcolumn label="Ufficio">
				<s:if left="{7}{8}" control="eq" right="">
						<s:then>
						</s:then>
						<s:else>
							{7}/{8}
						</s:else>
				</s:if>		    							     	 
     	</s:dgcolumn>     
      
        <!-- EH1_TEH1SERV -->
		<s:dgcolumn index="9" label="Tip. Serv."></s:dgcolumn>
      
        <!-- CARICO -->
     	<s:dgcolumn index="10" format="#,##0.00" label="Carico" css="text_align_right"></s:dgcolumn>
             
        <!-- DIM_CARICO -->
     	<s:dgcolumn index="11" format="#,##0.00" label="Dim.Carico" css="text_align_right"></s:dgcolumn>

        <!-- RISCOSSO -->
     	<s:dgcolumn index="12" format="#,##0.00" label="Riscosso" css="text_align_right"></s:dgcolumn>
             
        <!-- RIMBORSO -->
     	<s:dgcolumn index="13" format="#,##0.00" label="Rimborso" css="text_align_right"></s:dgcolumn>
             
        <!-- RENDICONTATO -->
     	<s:dgcolumn index="14" format="#,##0.00" label="Rendicont." css="text_align_right"></s:dgcolumn>
             
        <!-- RESIDUO -->
     	<s:dgcolumn index="15" format="#,##0.00" label="Residuo" css="text_align_right"></s:dgcolumn>


        <s:ifdatagrid left="${ext}" control="eq" right="1">
		</s:ifdatagrid>
		
	</s:datagrid>

</s:div>
	
</c:if>
	
<s:div name="div_documento" cssclass="div_align_center divSelezione">
	<!-- tabella dettaglio -->
	<s:div name="divTableTitle3" cssclass="divTableTitle bold">
		Dettaglio Documento
	</s:div>
	
	<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
		<s:thead>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:th cssclass="seda-ui-datagridheadercell" icol="4"><b>Documento</b></s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Societ&agrave;: ${dettSoc}</s:td>
				<s:td cssclass="seda-ui-datagridcell">Utente: ${dettUte}</s:td>
				<s:td cssclass="seda-ui-datagridcell"icol="2">Ente: ${dettEnte}</s:td>
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Imposta Servizio (I/S)</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettImpostaServ}</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Numero Rate</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettNumRate}</s:td>
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Tipo/Codice Uff.Impositore</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettUffImp}</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Scad.I Rata</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettScadPRata}</s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Anno Emissione</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettAnnoEmis}</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Sospensione</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettSosp}</s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Numero Emissione</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettNumEmis}</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Data Notifica</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettDataNotif}</s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Tipologia Servizio</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettTipoServ}</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Proc.Esecutive</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettProcEse}</s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Docum.Rendicontato</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettDocRend}</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Documenti Precedenti</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettNumDocColl}</s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Cod.Fiscale Principale</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettCodFisc}</s:td>				
				<s:td cssclass="seda-ui-datagridcell" icol="2"></s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Id.Doc.Soc.Riscoss.</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettNumDoc}</s:td>				
				<s:td cssclass="seda-ui-datagridcell" icol="2"></s:td>
			</s:tr>
	</s:tbody>
	</s:table>
	<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
		<s:thead>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:th cssclass="seda-ui-datagridheadercell" icol="4"><b>Importo</b></s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Carico</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoCarico}" minFractionDigits="2" maxFractionDigits="2"/></s:td>				
				<s:td cssclass="seda-ui-datagridcell">Rendicontato</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoRendiconto}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Sgravato</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoDiminuzioneCarico}" minFractionDigits="2" maxFractionDigits="2"/></s:td>				
				<s:td cssclass="seda-ui-datagridcell">Residuo</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoTotaleResiduo}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Riscosso</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoRiscosso}" minFractionDigits="2" maxFractionDigits="2"/></s:td>				
				<s:td cssclass="seda-ui-datagridcell">Residuo Scaduto</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totResScaduto}" minFractionDigits="2" maxFractionDigits="2"/></s:td>				
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Rimborso</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoTotaleRimborso}" minFractionDigits="2" maxFractionDigits="2"/></s:td>				
				<s:td cssclass="seda-ui-datagridcell"></s:td>
				<s:td cssclass="seda-ui-datagridcell"></s:td>
			</s:tr>
		</s:tbody>
	</s:table>
</s:div>
	<s:div name="indietro" cssclass="divRicBottoni">
		<s:form name="dettaglioDocumento" action="documentoDettaglio.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		
			<s:textbox name="rRowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rRowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="rPageNumber" label="pageNumberRic" bmodify="true" text="${rPageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="rOrder" label="orderRic" bmodify="true" text="${rOrder}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="rExt" label="extRic" bmodify="true" text="${rExt}" cssclass="display_none" cssclasslabel="display_none"  />

			<s:textbox name="rowsPerPage" label="rowsPerPageRic2" bmodify="true" text="${r2RowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="pageNumber" label="pageNumberRic2" bmodify="true" text="${r2PageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="order" label="orderRic2" bmodify="true" text="${r2Order}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="ext" label="orderRic2" bmodify="true" text="${r2Ext}" cssclass="display_none" cssclasslabel="display_none"  />
	
			<s:textbox name="tx_tipologia_servizio" label="tx_tipologia_servizioRic" bmodify="true" text="${tx_tipologia_servizio}" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="tx_societa" label="tx_societaRic" bmodify="true" text="${tx_societa}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tx_provincia" label="tx_provinciaRic" bmodify="true" text="${tx_provincia}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tx_utente" label="tx_utenteRic" bmodify="true" text="${tx_utente}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tx_UtenteEnte" label="tx_UtenteEnteRic" bmodify="true" text="${tx_UtenteEnte}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="impostaServ" label="ImpostaServRic" bmodify="true" text="${impostaServ}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="annoEmissione" label="annoEmissioneRic" bmodify="true" text="${annoEmissione}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="numEmissione" label="numEmissioneRic" bmodify="true" text="${numEmissione}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="codFiscale" label="codFiscaleDoc" bmodify="true" text="${codFiscale}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="ufficioImpositore" label="ufficioImpositoreRic" bmodify="true" text="${ufficioImpositore}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="stato_documento" label="statoDocumentoRic" bmodify="true" text="${stato_documento}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="numDocumento" label="numDocumentoRic" bmodify="true" text="${numDocumento}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="stato_sospensione" label="stato_sospensioneRic" bmodify="true" text="${stato_sospensione}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="stato_procedure" label="stato_procedureRic" bmodify="true" text="${stato_procedure}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="denominazione" label="denominazione" bmodify="true" text="${denominazione}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tipoRic" label="tipoRic" bmodify="true" text="${tipoRic}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CHIAVI DI RICERCA
			 	IMPOSTA SERVIZIO  EH1_CISECISE -->
			<s:textbox name="chiaveIS" label="chiaveISDett" bmodify="true" text="${chiaveIS}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- NUMERO DOCUMENTO EH1_CEH1NDOC -->
			<s:textbox name="chiaveDoc" label="chiaveDocDett" bmodify="true" text="${chiaveDoc}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- TIPO UFFICIO EH1_TANETUFF -->
			<s:textbox name="chiaveTipoUff" label="chiaveTipoUffDett" bmodify="true" text="${chiaveTipoUff}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CODICE UFFICIO EH1_CANECUFF -->
			<s:textbox name="chiaveCodUff" label="chiaveCodUffDett" bmodify="true" text="${chiaveCodUff}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CODICE UTENTE EH1_CUTECUTE -->
			<s:textbox name="chiaveCodUte" label="chiaveCodUteDett" bmodify="true" text="${chiaveCodUte}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CODICE ENTE EH1_CANECENT -->
			<s:textbox name="chiaveCodEnte" label="chiaveCodEnteDett" bmodify="true" text="${chiaveCodEnte}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CODICE SERVIZIO EH1_TEH1SERV -->
			<s:textbox name="chiaveServ" label="chiaveServDett" bmodify="true" text="${chiaveServ}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CODICE TOMBSTONE EH1_CEH1TOMB -->
			<s:textbox name="chiaveTomb" label="chiaveTombDett" bmodify="true" text="${chiaveTomb}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- PROGR.FLUSSO EH1_PEH1FLUS -->
			<s:textbox name="chiaveFlusso" label="chiaveFlussoDett" bmodify="true" text="${chiaveFlusso}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- DATA CREAZIONE EH1_GEH1CREA -->
			<s:textbox name="chiaveDataCrea" label="chiaveDataCrea" bmodify="true" text="${chiaveDataCrea}" cssclass="display_none" cssclasslabel="display_none"  />
		
			<!-- CODICE SERVIZIO EH8_PEH8FLUS  -->
			<s:textbox name="chiaveAnaFlusso" label="chiaveAnaFlussoDett" bmodify="true" text="${chiaveAnaFlusso}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CODICE SERVIZIO EH8_GEH8CREA -->
			<s:textbox name="chiaveAnaDataCrea" label="chiaveAnaDataCreaDett" bmodify="true" text="${chiaveAnaDataCrea}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CODICE SERVIZIO EH8_TEH8SERV -->
			<s:textbox name="chiaveAnaServ" label="chiaveAnaServDett" bmodify="true" text="${chiaveAnaServ}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- RIF.PAG.PREC. 	value="ricercaAnagrafica" value="ricercaEmissioni" value="ricercaDocumenti" -->
			<s:textbox name="pagPrec" label="pagPrecRifChiamante" bmodify="true" text="${pagPrec}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="loadDett" label="loadDett" bmodify="true" text="false" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CHIAVE EMISSIONE AGG -->
			<s:textbox name="chiaveAnnoE" label="chiaveAnnoE" bmodify="true" text="${chiaveAnnoE}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="chiaveNumeroE" label="chiaveNumeroE" bmodify="true" text="${chiaveNumeroE}" cssclass="display_none" cssclasslabel="display_none"  />
			
			<!-- RIF.ANAGRAFICA -->
			<s:textbox name="denominazione1" label="denominazione" bmodify="true" text="${denominazione}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tipoRic1" label="tipoRicerca" bmodify="true" text="${tipoRic}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="codFiscAna" label="tipoRicerca" bmodify="true" text="${codFiscAna}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="chiaveCodFisc" label="chiaveCodFisc" bmodify="true" text="${chiaveCodFisc}" cssclass="display_none" cssclasslabel="display_none"  />
			
			<s:textbox name="ext" label="extRic" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none"  />
	
			<!-- CHIAVE I/S EH1_CISECISE -->
			<s:textbox name="dettImpostaServ" label="dettEnte" bmodify="true" text="${dettImpostaServ}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:button id="tx_button_cerca" cssclass="btnStyle" type="submit" text="Indietro" onclick=""/>

		</s:form>		
	</s:div>

	