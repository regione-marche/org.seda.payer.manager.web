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


 <s:div name="div_selezione" cssclass="div_align_center divSelezione">
			<s:form name="DettaglioPartitaForm" action="ricercaRiversamenti.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			</s:form>
	</s:div>

 <!-- dettaglio Anagrafica -->
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
		<!-- SOCIETA -->
		<s:dgcolumn index="1"  label="Societ&agrave;"></s:dgcolumn>
		
		<!-- UTENTE -->
		<s:dgcolumn index="2"  label="Utente"></s:dgcolumn>

		<!-- ENTE -->
		<s:dgcolumn index="3"  label="Ente" ></s:dgcolumn>

		<!-- CODICE FISCALE -->
		<s:dgcolumn index="4"  label="Cod. Fis." ></s:dgcolumn>

		<!-- DENOMINAZIONE -->
		<s:dgcolumn index="5"  label="Denominazione" ></s:dgcolumn>

		<!-- COMUNE -->
		<s:dgcolumn index="6"  label="Comune" ></s:dgcolumn>

		<!-- INDIRIZZO -->
		<s:dgcolumn index="7"  label="Indirizzo" ></s:dgcolumn>

        <s:ifdatagrid left="${ext}" control="eq" right="1">
		</s:ifdatagrid>
		
	</s:datagrid>

</s:div>
	
</c:if>
	

<!-- dettaglio ruolo -->
<c:if test="${!empty dettaglioRuo}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Dettaglio Ruolo
	</s:div>

	<s:div name="divDettaglioRuo" cssclass="divRisultati">

	<s:datagrid  viewstate="" cachedrowset="dettaglioRuo" action="nessuna.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
		<s:action>
			<c:url value="nessuna.do">				
			</c:url>
		</s:action>
		<!-- SOCIETA -->
		<s:dgcolumn index="1"  label="Societ&agrave;"></s:dgcolumn>
		
		<!-- UTENTE -->
		<s:dgcolumn index="2"  label="Utente"></s:dgcolumn>

		<!-- ENTE -->
		<s:dgcolumn index="3"  label="Ente" ></s:dgcolumn>

		<!-- RRU_GRRUGRUO -->
		<s:dgcolumn index="15"  format="dd/MM/yyyy" label="Data Consegna" ></s:dgcolumn>

  	    <!-- RRU_NRRUANNO -->
		<s:dgcolumn index="4" label="Anno"></s:dgcolumn>

        <!--  RRU_NRRUNUME -->
		<s:dgcolumn index="5" label="Num"></s:dgcolumn>

		<!-- RRU_CRRUTUFF,RRU_CRRUCUFF -->
		<s:dgcolumn  label="Uff">
				{6}/{7}
			</s:dgcolumn>

		<!-- CARICO -->
		<s:dgcolumn index="8" format="#,##0.00" label="Carico" css="text_align_right"></s:dgcolumn>

		<!-- DIM. CARICO -->
		<s:dgcolumn index="9" format="#,##0.00" label="Dim.Carico" css="text_align_right"></s:dgcolumn>

		<!-- RISCOSSO -->
		<s:dgcolumn index="10" format="#,##0.00" label="Riscosso" css="text_align_right"></s:dgcolumn>
        
		<!-- RIMBORSO -->
		<s:dgcolumn index="11" format="#,##0.00" label="Rimborso" css="text_align_right"></s:dgcolumn>
        
		<!-- RESIDUO asc="RSDA" desc="RSDD"-->
		<s:dgcolumn index="12" format="#,##0.00" label="Residuo" css="text_align_right"></s:dgcolumn>


        <s:ifdatagrid left="${ext}" control="eq" right="1">
		</s:ifdatagrid>
		
	</s:datagrid>

</s:div>
	
</c:if>

<!-- DETTAGLIO PARTITA -->

<c:if test="${!empty dettaglioPart}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Dettaglio Partita
	</s:div>

	<s:div name="divDettaglioPart" cssclass="divRisultati">

	<s:datagrid  viewstate="" cachedrowset="dettaglioPart" action="nessuna.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
		<s:action>
			<c:url value="nessuna.do">				
			</c:url>
		</s:action>
		<!-- SOCIETA -->
		<s:dgcolumn index="1"  label="Societ&agrave;"></s:dgcolumn>
		
		<!-- UTENTE -->
		<s:dgcolumn index="2"  label="Utente"></s:dgcolumn>

		<!-- ENTE -->
		<s:dgcolumn index="3"  label="Ente" ></s:dgcolumn>

		<!-- RAR_CRARCCAR -->
		<s:dgcolumn index="4"  label="Nr Cartella" ></s:dgcolumn>

  	    <!-- RRU_NRRUANNO -->
		<s:dgcolumn index="5" label="Anno"></s:dgcolumn>

        <!--  RRU_NRRUNUME -->
		<s:dgcolumn index="6" label="Num"></s:dgcolumn>

		<!-- RRU_CRRUTUFF,RRU_CRRUCUFF -->
		<s:dgcolumn  label="Uff">
				{7}/{8}
			</s:dgcolumn>
		<!-- RPA_CRPACFIS -->
		<s:dgcolumn index="9" label="Cod. Fisc." ></s:dgcolumn>
		<!-- RPA_CRPACIDE -->
		<s:dgcolumn index="10" label="Id Partita" ></s:dgcolumn>

		<!-- CARICO -->
		<s:dgcolumn index="11" format="#,##0.00" label="Carico" css="text_align_right"></s:dgcolumn>

		<!-- DIM. CARICO -->
		<s:dgcolumn index="12" format="#,##0.00" label="Dim.Carico" css="text_align_right"></s:dgcolumn>

		<!-- RISCOSSO -->
		<s:dgcolumn index="13" format="#,##0.00" label="Riscosso" css="text_align_right"></s:dgcolumn>
        
		<!-- RIMBORSO -->
		<s:dgcolumn index="14" format="#,##0.00" label="Rimborso" css="text_align_right"></s:dgcolumn>
        
		<!-- RESIDUO asc="RSDA" desc="RSDD"-->
		<s:dgcolumn index="15" format="#,##0.00" label="Residuo" css="text_align_right"></s:dgcolumn>


        <s:ifdatagrid left="${ext}" control="eq" right="1">
		</s:ifdatagrid>
		
	</s:datagrid>

</s:div>
	
</c:if>

<s:div name="div_partita" cssclass="div_align_center divSelezione">
	<!-- 
	
	-----------------
	tabella dettaglio
	-----------------
	 -->
	
	<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
		<s:thead>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:th cssclass="seda-ui-datagridheadercell" icol="2"><b>Partita</b></s:th>
			</s:tr>
		</s:thead>
		<s:tbody>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Anno Ruolo</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettAnnoRuo}</s:td>	
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Numero Ruolo</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettNumRuo}</s:td>				
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Data Consegna Ruolo</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettDataConsegna}</s:td>				
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Ufficio</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettTpUffImp}/${dettCodUffImp}</s:td>				
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Codice Fiscale</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettCodFis}</s:td>				
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Id Partita</s:td>
				<s:td cssclass="seda-ui-datagridcell">${dettIdPartita}</s:td>				
			</s:tr>

	</s:tbody>
	</s:table>
</s:div>
<s:div name="divblank" cssclass="divTableTitle bold text_align_right" />
	<s:div name="indietro" cssclass="divRicBottoni">
		<s:form name="dettaglioPartita" action="dettaglioPartita.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		
			<s:textbox name="rRowsPerPage" label="rRowsPerPageRic" bmodify="true" text="${rRowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="rPageNumber" label="rPageNumberRic" bmodify="true" text="${rPageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="rOrder" label="rOrderRic" bmodify="true" text="${rOrder}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="rExt" label="extRic" bmodify="true" text="${rExt}" cssclass="display_none" cssclasslabel="display_none"  />
	
			<s:textbox name="rowsPerPage" label="rowsPerPageRic" bmodify="true" text="${r2RowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="pageNumber" label="pageNumberRic" bmodify="true" text="${r2PageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="order" label="orderRic" bmodify="true" text="${r2Order}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="ext" label="extRic" bmodify="true" text="${r2Ext}" cssclass="display_none" cssclasslabel="display_none"  />

			<s:textbox name="tx_societa" label="tx_societaRic" bmodify="true" text="${tx_societa}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tx_utente" label="tx_utenteRic" bmodify="true" text="${tx_utente}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tx_UtenteEnte" label="tx_UtenteEnteRic" bmodify="true" text="${tx_UtenteEnte}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="annoRuolo" label="annoRuoloRic" bmodify="true" text="${annoRuolo}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="numRuolo" label="numRuoloRic" bmodify="true" text="${numRuolo}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="ufficioImpositore" label="ufficioImpositoreRic" bmodify="true" text="${ufficioImpositore}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_consegna_da_day" label="data_consegna_da_day_Ric" bmodify="true" text="${data_consegna_da_day}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_consegna_da_month" label="data_consegna_da_month_Ric" bmodify="true" text="${data_consegna_da_month}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_consegna_da_year" label="data_consegna_da_year_Ric" bmodify="true" text="${data_consegna_da_year}" cssclass="display_none" cssclasslabel="display_none"  />

			<s:textbox name="data_consegna_a_day" label="dataconsegnaadayRic" bmodify="true" text="${data_consegna_a_day}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_consegna_a_month" label="dataconsegnaamonthRic" bmodify="true" text="${data_consegna_a_month}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_consegna_a_year" label="dataconsegnaayearRic" bmodify="true" text="${data_consegna_a_year}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- ricerca Anagrafica -->
			<s:textbox name="codFisc1" label="codFiscRic" bmodify="true" text="${codFisc}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tipoRic" label="tipoRicRic" bmodify="true" text="${tipoRic}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="denominazione" label="denominazioneRic" bmodify="true" text="${denominazione}" cssclass="display_none" cssclasslabel="display_none"  />

			
			<!-- chiavi lista partiteS -->
			<s:textbox name="chiaveCodSoc" label="chiaveCodSoc" bmodify="true" text="${chiaveCodSoc}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="chiaveCodUte" label="chiaveCodUte" bmodify="true" text="${chiaveCodUte}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="chiaveCodEnte" label="chiaveCodEnte" bmodify="true" text="${chiaveCodEnte}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="chiaveAnnoRuo" label="chiaveAnnoRuo" bmodify="true" text="${chiaveAnnoRuo}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="chiaveNumRuo" label="chiaveNumRuo" bmodify="true" text="${chiaveNumRuo}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="chiaveFlusso" label="chiaveFlusso" bmodify="true" text="${chiaveFlusso}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="chiaveConc" label="chiaveConc" bmodify="true" text="${chiaveConc}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="chiaveTomb" label="chiaveTomb" bmodify="true" text="${chiaveTomb}" cssclass="display_none" cssclasslabel="display_none"  />

			<!-- chiavi lista partiteAnagrafica -->
			<s:textbox name="chiaveCodFisc" label="chiaveCodFisc" bmodify="true" text="${chiaveCodFisc}" cssclass="display_none" cssclasslabel="display_none"  />

			<!-- chiavi ricerca partita  -->
			<s:textbox name="stato_partita" label="statopartita" bmodify="true" text="${stato_partita}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="stato_cartella" label="statocartella" bmodify="true" text="${stato_cartella}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="codFisc" label="codFisc" bmodify="true" text="${codFisc}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="codCartella" label="codCartella" bmodify="true" text="${codCartella}" cssclass="display_none" cssclasslabel="display_none"  />
			
	
			<s:textbox name="pagPrec" label="pagPrec" bmodify="true" text="${pagPrec}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="loadDett" label="loadDett" bmodify="true" text="false" cssclass="display_none" cssclasslabel="display_none"  />
			
			<s:button id="tx_button_cerca" cssclass="btnStyle" type="submit" text="Indietro" onclick=""/>

		</s:form>		
	</s:div>

	