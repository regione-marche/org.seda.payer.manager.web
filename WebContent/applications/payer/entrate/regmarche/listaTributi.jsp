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
	<c:url value="" var="formParameters">
		<c:param name="test">ok</c:param>
				<!-- ric PAGINA -->
				<c:if test="${!empty rRowsPerPage}">
					<c:param name="rRowsPerPage">${rRowsPerPage}</c:param> 
				</c:if>
				<c:if test="${!empty rPageNumber}">
					<c:param name="rPageNumber">${rPageNumber}</c:param> 
				</c:if>
				<c:if test="${!empty rOrder}">
					<c:param name="rOrder">${rOrder}</c:param> 
				</c:if>
				<c:if test="${!empty rExt}">
					<c:param name="rExt">${rExt}</c:param>
				</c:if>

				<!-- ric PAGINA 2 -->
	
				<c:if test="${!empty r2RowsPerPage}">
					<c:param name="r2RowsPerPage">${r2RowsPerPage}</c:param> 
				</c:if>
				<c:if test="${!empty r2PageNumber}">
					<c:param name="r2PageNumber">${r2PageNumber}</c:param> 
				</c:if>
				<c:if test="${!empty r2Order}">
					<c:param name="r2Order">${r2Order}</c:param> 
				</c:if>
				<c:if test="${!empty r2Ext}">
					<c:param name="r2Ext">${r2Ext}</c:param>
				</c:if>

				<!-- ric PAGINA attuale -->
				<c:if test="${!empty rowsPerPage}">
					<c:param name="r3RowsPerPage">${rowsPerPage}</c:param> 
				</c:if>
				<c:if test="${!empty pageNumber}">
					<c:param name="r3PageNumber">${pageNumber}</c:param> 
				</c:if>
				<c:if test="${!empty order}">
					<c:param name="r3Order">${order}</c:param> 
				</c:if>
				<c:if test="${!empty ext}">
					<c:param name="r3Ext">${ext}</c:param>
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
		<c:if test="${!empty impostaServ}">  
			<c:param name="impostaServ">${impostaServ}</c:param>
		</c:if>
		<c:if test="${!empty annoEmissione}">  
			<c:param name="annoEmissione">${annoEmissione}</c:param>
		</c:if>
		<c:if test="${!empty numEmissione}">  
			<c:param name="numEmissione">${numEmissione}</c:param>
		</c:if>
		<c:if test="${!empty codFiscale}">  
			<c:param name="codFiscale">${codFiscale}</c:param>
		</c:if>
		<c:if test="${!empty tx_tipologia_servizio}">  
			<c:param name="tx_tipologia_servizio">${tx_tipologia_servizio}</c:param>
		</c:if>
		<c:if test="${!empty stato_documento}">  
			<c:param name="stato_documento">${stato_documento}</c:param>
		</c:if>
		<c:if test="${!empty numDocumento}">  
			<c:param name="numDocumento">${numDocumento}</c:param>
		</c:if>
		<c:if test="${!empty ufficioImpositore}">  
			<c:param name="ufficioImpositore">${ufficioImpositore}</c:param>
		</c:if>
		<c:if test="${!empty stato_sospensione}">  
			<c:param name="stato_sospensione">${stato_sospensione}</c:param>
		</c:if>
		<c:if test="${!empty stato_procedure}">
			<c:param name="stato_procedure">${stato_procedure}</c:param> 
		</c:if>
		<c:if test="${!empty denominazione}">
			<c:param name="denominazione">${denominazione}</c:param> 
		</c:if>
		<c:if test="${!empty tipoRic}">
			<c:param name="tipoRic">${tipoRic}</c:param> 
		</c:if>
		<c:if test="${!empty codFiscAna}">
			<c:param name="codFiscAna">${codFiscAna}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveCodFisc}">
			<c:param name="chiaveCodFisc">${chiaveCodFisc}</c:param> 
		</c:if>
		<c:if test="${!empty ext}">
			<c:param name="ext">${ext}</c:param>
		</c:if>
				
				<!-- CHIAVI DI RICERCA
			 		IMPOSTA SERVIZIO  EH1_CISECISE -->
				<c:if test="${!empty chiaveIS}">
					<c:param name="chiaveIS">${chiaveIS}</c:param> 
				</c:if>
				<!-- NUMERO DOCUMENTO EH1_CEH1NDOC -->
				<c:if test="${!empty chiaveDoc}">
					<c:param name="chiaveDoc">${chiaveDoc}</c:param> 
				</c:if>
				<!-- TIPO UFFICIO EH1_TANETUFF -->
				<c:if test="${!empty chiaveTipoUff}">
					<c:param name="chiaveTipoUff">${chiaveTipoUff}</c:param> 
				</c:if>
				<!-- CODICE UFFICIO EH1_CANECUFF -->
				<c:if test="${!empty chiaveCodUff}">
					<c:param name="chiaveCodUff">${chiaveCodUff}</c:param> 
				</c:if>
				<!-- CODICE UTENTE EH1_CUTECUTE -->
				<c:if test="${!empty chiaveCodUte}">
					<c:param name="chiaveCodUte">${chiaveCodUte}</c:param> 
				</c:if>
				<!-- CODICE ENTE EH1_CANECENT -->
				<c:if test="${!empty chiaveCodEnte}">
					<c:param name="chiaveCodEnte">${chiaveCodEnte}</c:param> 
				</c:if>
				<!-- CODICE SERVIZIO EH1_TEH1SERV -->
				<c:if test="${!empty chiaveServ}">
					<c:param name="chiaveServ">${chiaveServ}</c:param> 
				</c:if>
				<!-- CODICE TOMBSTONE EH1_CEH1TOMB -->
				<c:if test="${!empty chiaveTomb}">
					<c:param name="chiaveTomb">${chiaveTomb}</c:param> 
				</c:if>
				<!-- PROGR.FLUSSO EH1_PEH1FLUS -->
				<c:if test="${!empty chiaveFlusso}">
					<c:param name="chiaveFlusso">${chiaveFlusso}</c:param> 
				</c:if>
				<!-- CODICE SERVIZIO EH8_PEH8FLUS  -->
				<c:if test="${!empty chiaveAnaFlusso}">
					<c:param name="chiaveAnaFlusso">${chiaveAnaFlusso}</c:param> 
				</c:if>
				<!-- CODICE SERVIZIO EH8_GEH8CREA -->
				<c:if test="${!empty chiaveAnaDataCrea}">
					<c:param name="chiaveAnaDataCrea">${chiaveAnaDataCrea}</c:param> 
				</c:if>
				<!-- CODICE SERVIZIO EH8_TEH8SERV -->
				<c:if test="${!empty chiaveAnaServ}">
					<c:param name="chiaveAnaServ">${chiaveAnaServ}</c:param> 
				</c:if>
				<!-- CHIAVE EMISSIONE AGG -->
				<c:if test="${!empty chiaveAnnoE}">
					<c:param name="chiaveAnnoE">${chiaveAnnoE}</c:param>
				</c:if>				
				<c:if test="${!empty chiaveNumeroE}">
					<c:param name="chiaveNumeroE">${chiaveNumeroE}</c:param>
				</c:if>
				
				<!-- RIF.PAG.PREC. 	value="ricercaAnagrafica" value="ricercaEmissioni" value="ricercaDocumenti" -->
				<c:if test="${!empty pagPrec}">
					<c:param name="pagPrec">${pagPrec}</c:param> 
				</c:if>
	</c:url>



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
		<s:dgcolumn index="11" label="Cod.Fiscale"></s:dgcolumn>

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
     	<%-- <s:dgcolumn index="13" format="#,##0.00" label="Rimborso" css="text_align_right"></s:dgcolumn> --%>
             
        <!-- RENDICONTATO -->
     	<%-- <s:dgcolumn index="14" format="#,##0.00" label="Rendicont." css="text_align_right"></s:dgcolumn> --%>
             
        <!-- RESIDUO -->
     	<s:dgcolumn index="15" format="#,##0.00" label="Residuo" css="text_align_right"></s:dgcolumn>


        <s:ifdatagrid left="${ext}" control="eq" right="1">
		</s:ifdatagrid>
		
	</s:datagrid>

</s:div>
	
</c:if>


<!-- DETTAGLIO DOCUMENTO -->
<fmt:setLocale value="it_IT"/>



<s:div name="div_documento" cssclass="divRisultati">
	<!-- 	tabella dettaglio	 -->
	  
	<s:div name="divTableTitle3" cssclass="divTableTitle bold">
		Dettaglio Documento
	</s:div>
	
	<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
		<s:thead>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:th cssclass="seda-ui-datagridheadercell">Societ&agrave;</s:th>
				<s:th cssclass="seda-ui-datagridheadercell">Utente</s:th>
				<s:th cssclass="seda-ui-datagridheadercell">Ente</s:th>
				<s:th cssclass="seda-ui-datagridheadercell">I/S</s:th>
				<s:th cssclass="seda-ui-datagridheadercell">Anno</s:th>	
				<s:th cssclass="seda-ui-datagridheadercell">Numero</s:th>
				<s:th cssclass="seda-ui-datagridheadercell">Ufficio</s:th>	
				<s:th cssclass="seda-ui-datagridheadercell">Tip. Serv.</s:th>
				<s:th cssclass="seda-ui-datagridheadercell">R</s:th>	
				<s:th cssclass="seda-ui-datagridheadercell">Cod.Fiscale</s:th>
				<s:th cssclass="seda-ui-datagridheadercell">Carico</s:th>
				<s:th cssclass="seda-ui-datagridheadercell">Dim.Carico</s:th>
				<s:th cssclass="seda-ui-datagridheadercell">Riscosso</s:th>
				<%-- <s:th cssclass="seda-ui-datagridheadercell">Rimborso</s:th> --%>
				<%-- <s:th cssclass="seda-ui-datagridheadercell">Rendicont.</s:th> --%>
				<s:th cssclass="seda-ui-datagridheadercell">Residuo</s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">${codSoc}</s:td>
				<s:td cssclass="seda-ui-datagridcell">${codUte}</s:td>
				<s:td cssclass="seda-ui-datagridcell">${codEnte}</s:td>
				<s:td cssclass="seda-ui-datagridcell">${chiaveIS}</s:td>	
				<s:td cssclass="seda-ui-datagridcell">${dettAnnoEmis}</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettNumEmis}</s:td>	
				<s:td cssclass="seda-ui-datagridcell">${ufficio}</s:td>
				<s:td cssclass="seda-ui-datagridcell">${codTipoServ}</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettDocRend}</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettCodFisc}</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoCarico}" minFractionDigits="2" maxFractionDigits="2"/></s:td>				
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoDiminuzioneCarico}" minFractionDigits="2" maxFractionDigits="2"/></s:td>				
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoRiscosso}" minFractionDigits="2" maxFractionDigits="2"/></s:td>				
				<%-- <s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoTotaleRimborso}" minFractionDigits="2" maxFractionDigits="2"/></s:td> --%>				
				<%-- <s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoRendiconto}" minFractionDigits="2" maxFractionDigits="2"/></s:td> --%>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoTotaleResiduo}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
			</s:tr>
		</s:tbody>
	</s:table>
</s:div>

 	
<c:if test="${!empty listaTributi}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle4" cssclass="divTableTitle bold">
		Lista Tributi
	</s:div>

	<s:div name="divRisultati" cssclass="divRisultati">

	<s:datagrid  viewstate="" cachedrowset="listaTributi" action="ricercaTributi.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
		<s:action>
			<c:url value="ricercaTributi.do">
				<!-- ric PAGINA -->
				<c:if test="${!empty rRowsPerPage}">
					<c:param name="rRowsPerPage">${rRowsPerPage}</c:param> 
				</c:if>
				<c:if test="${!empty rPageNumber}">
					<c:param name="rPageNumber">${rPageNumber}</c:param> 
				</c:if>
				<c:if test="${!empty rOrder}">
					<c:param name="rOrder">${rOrder}</c:param> 
				</c:if>
				<c:if test="${!empty rExt}">
					<c:param name="rExt">${rExt}</c:param>
				</c:if>

				<!-- ric PAGINA 2 -->
	
				<c:if test="${!empty r2RowsPerPage}">
					<c:param name="r2RowsPerPage">${r2RowsPerPage}</c:param> 
				</c:if>
				<c:if test="${!empty r2PageNumber}">
					<c:param name="r2PageNumber">${r2PageNumber}</c:param> 
				</c:if>
				<c:if test="${!empty r2Order}">
					<c:param name="r2Order">${r2Order}</c:param> 
				</c:if>
				<c:if test="${!empty r2Ext}">
					<c:param name="r2Ext">${r2Ext}</c:param>
				</c:if>


				<c:if test="${!empty ext}">
					<c:param name="ext">${ext}</c:param>
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
		<c:if test="${!empty impostaServ}">  
			<c:param name="impostaServ">${impostaServ}</c:param>
		</c:if>
		<c:if test="${!empty annoEmissione}">  
			<c:param name="annoEmissione">${annoEmissione}</c:param>
		</c:if>
		<c:if test="${!empty numEmissione}">  
			<c:param name="numEmissione">${numEmissione}</c:param>
		</c:if>
		<c:if test="${!empty codFiscale}">  
			<c:param name="codFiscale">${codFiscale}</c:param>
		</c:if>
		<c:if test="${!empty tx_tipologia_servizio}">  
			<c:param name="tx_tipologia_servizio">${tx_tipologia_servizio}</c:param>
		</c:if>
		<c:if test="${!empty stato_documento}">  
			<c:param name="stato_documento">${stato_documento}</c:param>
		</c:if>
		<c:if test="${!empty numDocumento}">  
			<c:param name="numDocumento">${numDocumento}</c:param>
		</c:if>
		<c:if test="${!empty ufficioImpositore}">  
			<c:param name="ufficioImpositore">${ufficioImpositore}</c:param>
		</c:if>
		<c:if test="${!empty stato_sospensione}">  
			<c:param name="stato_sospensione">${stato_sospensione}</c:param>
		</c:if>
		<c:if test="${!empty stato_procedure}">
			<c:param name="stato_procedure">${stato_procedure}</c:param> 
		</c:if>
		<c:if test="${!empty denominazione}">
			<c:param name="denominazione">${denominazione}</c:param> 
		</c:if>
		<c:if test="${!empty tipoRic}">
			<c:param name="tipoRic">${tipoRic}</c:param> 
		</c:if>
		<c:if test="${!empty codFiscAna}">
			<c:param name="codFiscAna">${codFiscAna}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveCodFisc}">
			<c:param name="chiaveCodFisc">${chiaveCodFisc}</c:param> 
		</c:if>
		<c:if test="${!empty tx_button_cerca_exp}">
			<c:param name="tx_button_cerca_exp">OK</c:param> 
		</c:if>
		<c:if test="${!empty chiaveIS}">
			<c:param name="chiaveIS">${chiaveIS}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveDoc}">
			<c:param name="chiaveDoc">${chiaveDoc}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveTipoUff}">
			<c:param name="chiaveTipoUff">${chiaveTipoUff}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveCodUff}">
			<c:param name="chiaveCodUff">${chiaveCodUff}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveCodUte}">
			<c:param name="chiaveCodUte">${chiaveCodUte}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveCodEnte}">
			<c:param name="chiaveCodEnte">${chiaveCodEnte}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveServ}">
			<c:param name="chiaveServ">${chiaveServ}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveTomb}">
			<c:param name="chiaveTomb">${chiaveTomb}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveFlusso}">
			<c:param name="chiaveFlusso">${chiaveFlusso}</c:param> 
		</c:if>
		<!-- CODICE SERVIZIO EH8_PEH8FLUS  -->
		<c:if test="${!empty chiaveAnaFlusso}">
			<c:param name="chiaveAnaFlusso">${chiaveAnaFlusso}</c:param> 
		</c:if>
		<!-- CODICE SERVIZIO EH8_GEH8CREA -->
		<c:if test="${!empty chiaveAnaDataCrea}">
			<c:param name="chiaveAnaDataCrea">${chiaveAnaDataCrea}</c:param> 
		</c:if>
		<!-- CODICE SERVIZIO EH8_TEH8SERV -->
		<c:if test="${!empty chiaveAnaServ}">
			<c:param name="chiaveAnaServ">${chiaveAnaServ}</c:param> 
		</c:if>
		<!-- CHIAVE EMISSIONE AGG -->
		<c:if test="${!empty chiaveAnnoE}">
			<c:param name="chiaveAnnoE">${chiaveAnnoE}</c:param>
		</c:if>				
		<c:if test="${!empty chiaveNumeroE}">
			<c:param name="chiaveNumeroE">${chiaveNumeroE}</c:param>
		</c:if>

		<c:if test="${!empty pagPrec}">
			<c:param name="pagPrec">${pagPrec}</c:param> 
		</c:if>
				<!--  
				<c:if test="${!empty ext}">
					<c:param name="ext">${ext}</c:param>
				</c:if>
				-->
				<c:param name="DDLType">11</c:param>
			</c:url>
		</s:action>
  	    <!-- EH1_CISECISE -->
		<s:dgcolumn label=" " index="1" css="text_align_right"></s:dgcolumn>
  	    <!-- EH1_CISECISE -->
		<s:dgcolumn label="Tributo" asc="CTRA" desc="CTRD" index="2" ></s:dgcolumn>

  	    <!-- EH1_CEH1ADOC -->
		<s:dgcolumn index="3" label="Anno Riferimento"  asc="ATRA" desc="ATRD"></s:dgcolumn>

 		<!-- CARICO -->
		<s:dgcolumn index="4" format="#,##0.00" label="Carico" asc="ITRA" desc="ITRD" css="text_align_right"></s:dgcolumn>

		<!-- RISCOSSO -->
		<s:dgcolumn index="5" format="#,##0.00" label="Riscosso" asc="PGAA" desc="PGAD" css="text_align_right"></s:dgcolumn>
        
		<!-- RESIDUO -->
		<s:dgcolumn index="6" format="#,##0.00" label="Residuo" asc="RSDA" desc="RSDD" css="text_align_right"></s:dgcolumn>

  	    <%--  inizio LP PG210130 --%>
 	    <%-- 

   			TODO. Da decidere se visualizzare nuove colonne IdDominio,Iban Bancario e/o qualche altra... 
  	    		  Stessa scelta poi da estendere o meno in listaTributiDiscarico.	
  	    			
		<s:dgcolumn index="17" label="IdDominio"  asc="IDDA" desc="IDDD"></s:dgcolumn>
		<s:dgcolumn index="18" label="Iban Bancario"  asc="IBBA" desc="IBBD"></s:dgcolumn>
 		--%>
  	    <%--  fine LP PG210130 --%>

        <s:ifdatagrid left="${ext}" control="eq" right="1">
		</s:ifdatagrid>
	    
	    <s:dgcolumn label="Note">
				<s:hyperlink href="ricercaNote.do${formParameters}&chiaveCodTrib={2}&chiaveAnnoTrib={3}&chiaveProgTrib={1}&chiaveFlTrib={11}&chiaveSrTrib={12}&chiaveTbTrib={13}"
				 imagesrc="../applications/templates/shared/img/listaDati.png"
				 alt="Lista Note"
				 text=""
				 cssclass="blacklink hlStyle" />
        </s:dgcolumn>
    </s:datagrid>
     </s:div>
 </c:if>
	
	<s:div name="indietro" cssclass="divRicBottoni">
	
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
			
			
	<%-- FUNZIONE DISCARICO --%>
	<s:form name="discaricoForm" action="ricercaTributi.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

         <c:if test="${totImportoTotaleResiduo!='0.00'}">
	         <s:button
	          id="tx_button_discarico"
	          type="submit"
	          text="Discarico"
	          onclick=""
	          cssclass="btnDiscarico"
	          validate="false" />
				 </c:if>
      
				<s:textbox name="codSoc" label="" bmodify="true" text="${codSoc}" cssclass="display_none" cssclasslabel="display_none"  />
				<s:textbox name="codUte" label="" bmodify="true" text="${codUte}" cssclass="display_none" cssclasslabel="display_none"  />
				<s:textbox name="codEnte" label="" bmodify="true" text="${codEnte}" cssclass="display_none" cssclasslabel="display_none"  />
				<s:textbox name="chiaveIS" label="" bmodify="true" text="${chiaveIS}" cssclass="display_none" cssclasslabel="display_none"  />
				<s:textbox name="dettAnnoEmis" label="" bmodify="true" text="${dettAnnoEmis}" cssclass="display_none" cssclasslabel="display_none"  />
				<s:textbox name="dettNumEmis" label="" bmodify="true" text="${dettNumEmis}" cssclass="display_none" cssclasslabel="display_none"  />
				<s:textbox name="ufficio" label="" bmodify="true" text="${ufficio}" cssclass="display_none" cssclasslabel="display_none"  />
				<s:textbox name="codTipoServ" label="" bmodify="true" text="${codTipoServ}" cssclass="display_none" cssclasslabel="display_none"  />
				<s:textbox name="dettDocRend" label="" bmodify="true" text="${dettDocRend}" cssclass="display_none" cssclasslabel="display_none"  />
				<s:textbox name="dettCodFisc" label="" bmodify="true" text="${dettCodFisc}" cssclass="display_none" cssclasslabel="display_none"  />
				<s:textbox name="totImportoCarico" label="" bmodify="true" text="${totImportoCarico}" cssclass="display_none" cssclasslabel="display_none"  />
				<s:textbox name="totImportoDiminuzioneCarico" label="" bmodify="true" text="${totImportoDiminuzioneCarico}" cssclass="display_none" cssclasslabel="display_none"  />
				<s:textbox name="totImportoRiscosso" label="" bmodify="true" text="${totImportoRiscosso}" cssclass="display_none" cssclasslabel="display_none"  />
				<s:textbox name="totImportoTotaleResiduo" label="" bmodify="true" text="${totImportoTotaleResiduo}" cssclass="display_none" cssclasslabel="display_none"  />
	


					<s:textbox name="rRowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rRowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="rPageNumber" label="pageNumberRic" bmodify="true" text="${rPageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="rOrder" label="orderRic" bmodify="true" text="${rOrder}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="rExt" label="rExt" bmodify="true" text="${rExt}" cssclass="display_none" cssclasslabel="display_none"  />
		
					<s:textbox name="rowsPerPage" label="rowsPerPageRic2" bmodify="true" text="${r2RowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="pageNumber" label="pageNumberRic2" bmodify="true" text="${r2PageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="order" label="orderRic2" bmodify="true" text="${r2Order}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="ext" label="ext" bmodify="true" text="${r2Ext}" cssclass="display_none" cssclasslabel="display_none"  />
			
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
		
					<!-- CHIAVE EMISSIONE AGG -->
					<s:textbox name="chiaveAnnoE" label="chiaveAnnoE" bmodify="true" text="${chiaveAnnoE}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="chiaveNumeroE" label="chiaveNumeroE" bmodify="true" text="${chiaveNumeroE}" cssclass="display_none" cssclasslabel="display_none"  />
		
					<!-- RIF.PAG.PREC. 	value="ricercaAnagrafica" value="ricercaEmissioni" value="ricercaDocumenti" -->
					<s:textbox name="pagPrec" label="pagPrecRifChiamante" bmodify="true" text="${pagPrec}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="loadDett" label="loadDett" bmodify="true" text="false" cssclass="display_none" cssclasslabel="display_none"  />
					<!-- RIF.ANAGRAFICA -->
					<s:textbox name="denominazione1" label="denominazione" bmodify="true" text="${denominazione}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="tipoRic1" label="tipoRicerca" bmodify="true" text="${tipoRic}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="codFiscAna" label="tipoRicerca" bmodify="true" text="${codFiscAna}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="chiaveCodFisc" label="chiaveCodFisc" bmodify="true" text="${chiaveCodFisc}" cssclass="display_none" cssclasslabel="display_none"  />
			
					<s:textbox name="ext" label="extRic" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none"  />
			
					<!-- CHIAVE I/S EH1_CISECISE -->
					<s:textbox name="dettImpostaServ" label="dettEnte" bmodify="true" text="${dettImpostaServ}" cssclass="display_none" cssclasslabel="display_none"  />
						
		

	</s:form>	
			
			
			
				<s:form name="ricercaTributiForm" action="documentoDettaglio.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
				
					<s:textbox name="rRowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rRowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="rPageNumber" label="pageNumberRic" bmodify="true" text="${rPageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="rOrder" label="orderRic" bmodify="true" text="${rOrder}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="rExt" label="rExt" bmodify="true" text="${rExt}" cssclass="display_none" cssclasslabel="display_none"  />
		
					<s:textbox name="rowsPerPage" label="rowsPerPageRic2" bmodify="true" text="${r2RowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="pageNumber" label="pageNumberRic2" bmodify="true" text="${r2PageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="order" label="orderRic2" bmodify="true" text="${r2Order}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="ext" label="ext" bmodify="true" text="${r2Ext}" cssclass="display_none" cssclasslabel="display_none"  />
			
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
		
					<!-- CHIAVE EMISSIONE AGG -->
					<s:textbox name="chiaveAnnoE" label="chiaveAnnoE" bmodify="true" text="${chiaveAnnoE}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="chiaveNumeroE" label="chiaveNumeroE" bmodify="true" text="${chiaveNumeroE}" cssclass="display_none" cssclasslabel="display_none"  />
		
					<!-- RIF.PAG.PREC. 	value="ricercaAnagrafica" value="ricercaEmissioni" value="ricercaDocumenti" -->
					<s:textbox name="pagPrec" label="pagPrecRifChiamante" bmodify="true" text="${pagPrec}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="loadDett" label="loadDett" bmodify="true" text="false" cssclass="display_none" cssclasslabel="display_none"  />
					<!-- RIF.ANAGRAFICA -->
					<s:textbox name="denominazione1" label="denominazione" bmodify="true" text="${denominazione}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="tipoRic1" label="tipoRicerca" bmodify="true" text="${tipoRic}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="codFiscAna" label="tipoRicerca" bmodify="true" text="${codFiscAna}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="chiaveCodFisc" label="chiaveCodFisc" bmodify="true" text="${chiaveCodFisc}" cssclass="display_none" cssclasslabel="display_none"  />
			
					<s:textbox name="ext" label="extRic" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none"  />
			
					<!-- CHIAVE I/S EH1_CISECISE -->
					<s:textbox name="dettImpostaServ" label="dettEnte" bmodify="true" text="${dettImpostaServ}" cssclass="display_none" cssclasslabel="display_none"  />
						
					<s:textbox name="fired_button_hidden" label="fired_button_hidden"
						bmodify="true" text="" cssclass="rend_display_none"
						cssclasslabel="rend_display_none" />
					
					<s:button id="tx_button_cerca" cssclass="btnStyle btnIndietro" type="submit" text="Indietro" onclick="" />
				</s:form>
		
		
			</s:div>
				
	</s:div>
