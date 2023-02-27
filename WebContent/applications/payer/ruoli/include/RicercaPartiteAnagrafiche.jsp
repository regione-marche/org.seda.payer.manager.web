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
		<c:if test="${!empty data_consegna_da_day}">  
			<c:param name="data_consegna_da_day">${data_consegna_da_day}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_da_month}">  
			<c:param name="data_consegna_da_month">${data_consegna_da_month}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_da_year}">  
			<c:param name="data_consegna_da_year">${data_consegna_da_year}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_a_day}">  
			<c:param name="data_consegna_a_day">${data_consegna_a_day}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_a_month}">  
			<c:param name="data_consegna_a_month">${data_consegna_a_month}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_a_year}">  
			<c:param name="data_consegna_a_year">${data_consegna_a_year}</c:param>
		</c:if>
		<!-- ricerca Anagrafica -->
		<c:if test="${!empty codFisc}">  
			<c:param name="codFisc">${codFisc}</c:param>
		</c:if>
		<c:if test="${!empty tipoRic}">  
			<c:param name="tipoRic">${tipoRic}</c:param>
		</c:if>
		<c:if test="${!empty denominazione}">  
			<c:param name="denominazione">${denominazione}</c:param>
		</c:if>

		<c:if test="${!empty ext}">
			<c:param name="ext">${ext}</c:param>
		</c:if>

		<c:if test="${!empty rRowsPerPage}">
			<c:param name="rRowsPerPage">${rRowsPerPage}</c:param> 
		</c:if>
		<c:if test="${!empty rPageNumber}">
			<c:param name="rPageNumber">${rPageNumber}</c:param> 
		</c:if>
		<c:if test="${!empty rOrder}">
			<c:param name="rOrder">${rOrder}</c:param> 
		</c:if>
		<%-- chiavi ricerca partite --%>
		<c:if test="${!empty chiaveCodSoc}">
			<c:param name="chiaveCodSoc">${chiaveCodSoc}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveCodUte}">
			<c:param name="chiaveCodUte">${chiaveCodUte}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveCodEnte}">
			<c:param name="chiaveCodEnte">${chiaveCodEnte}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveAnnoRuo}">
			<c:param name="chiaveAnnoRuo">${chiaveAnnoRuo}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveNumRuo}">
			<c:param name="chiaveNumRuo">${chiaveNumRuo}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveFlusso}">
			<c:param name="chiaveFlusso">${chiaveFlusso}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveConc}">
			<c:param name="chiaveConc">${chiaveConc}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveTomb}">
			<c:param name="chiaveTomb">${chiaveTomb}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveCodFisc}">
			<c:param name="chiaveCodFisc">${chiaveCodFisc}</c:param> 
		</c:if>

	</c:url>
	
	 <s:div name="div_selezione" cssclass="div_align_center divSelezione">
			<s:form name="ricercaPartiteAnagraficheForm" action="ricercaRiversamenti.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			</s:form>
	</s:div>
	

<%--  

--------------------------------
Presentazione risultati
--------------------------------

 --%>	
 <%-- dettaglio Anagrafica --%>
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
		<%-- SOCIETA --%>
		<s:dgcolumn index="1"  label="Societ&agrave;"></s:dgcolumn>
		
		<%-- UTENTE --%>
		<s:dgcolumn index="2"  label="Utente"></s:dgcolumn>

		<%-- ENTE --%>
		<s:dgcolumn index="3"  label="Ente" ></s:dgcolumn>

		<%-- CODICE FISCALE --%>
		<s:dgcolumn index="4"  label="Cod. Fis." ></s:dgcolumn>

		<%-- DENOMINAZIONE --%>
		<s:dgcolumn index="5"  label="Denominazione" ></s:dgcolumn>

		<%-- COMUNE --%>
		<s:dgcolumn index="6"  label="Comune" ></s:dgcolumn>

		<%-- INDIRIZZO --%>
		<s:dgcolumn index="7"  label="Indirizzo" ></s:dgcolumn>

        <s:ifdatagrid left="${ext}" control="eq" right="1">
		</s:ifdatagrid>
		
	</s:datagrid>

</s:div>
	
</c:if>

<%-- colonne --%>	
	
<c:if test="${!empty listaPartite}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Lista Partite
	</s:div>

	<s:div name="divRisultati" cssclass="divRisultati">

	<s:datagrid  viewstate="" cachedrowset="listaPartite" action="ricercaPartiteAnagrafiche.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
		<s:action>
			<c:url value="ricercaPartiteAnagrafiche.do" >
		<c:if test="${!empty rRowsPerPage}">
			<c:param name="rRowsPerPage">${rRowsPerPage}</c:param> 
		</c:if>
		<c:if test="${!empty rPageNumber}">
			<c:param name="rPageNumber">${rPageNumber}</c:param> 
		</c:if>
		<c:if test="${!empty rOrder}">
			<c:param name="order">${rOrder}</c:param> 
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
		<c:if test="${!empty ufficioImpositore}">  
			<c:param name="ufficioImpositore">${ufficioImpositore}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_da_day}">  
			<c:param name="data_consegna_da_day">${data_consegna_da_day}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_da_month}">  
			<c:param name="data_consegna_da_month">${data_consegna_da_month}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_da_year}">  
			<c:param name="data_consegna_da_year">${data_consegna_da_year}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_a_day}">  
			<c:param name="data_consegna_a_day">${data_consegna_a_day}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_a_month}">  
			<c:param name="data_consegna_a_month">${data_consegna_a_month}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_a_year}">  
			<c:param name="data_consegna_a_year">${data_consegna_a_year}</c:param>
		</c:if>
		<%-- ricerca Anagrafica --%>
		<c:if test="${!empty codFisc}">  
			<c:param name="codFisc">${codFisc}</c:param>
		</c:if>
		<c:if test="${!empty tipoRic}">  
			<c:param name="tipoRic">${tipoRic}</c:param>
		</c:if>
		<c:if test="${!empty denominazione}">  
			<c:param name="denominazione">${denominazione}</c:param>
		</c:if>
		<c:if test="${!empty ext}">
			<c:param name="ext">${ext}</c:param>
		</c:if>
		<%-- chiavi ricerca partite --%>
		<c:if test="${!empty chiaveCodSoc}">
			<c:param name="chiaveCodSoc">${chiaveCodSoc}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveCodUte}">
			<c:param name="chiaveCodUte">${chiaveCodUte}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveCodEnte}">
			<c:param name="chiaveCodEnte">${chiaveCodEnte}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveAnnoRuo}">
			<c:param name="chiaveAnnoRuo">${chiaveAnnoRuo}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveNumRuo}">
			<c:param name="chiaveNumRuo">${chiaveNumRuo}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveFlusso}">
			<c:param name="chiaveFlusso">${chiaveFlusso}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveConc}">
			<c:param name="chiaveConc">${chiaveConc}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveTomb}">
			<c:param name="chiaveTomb">${chiaveTomb}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveCodFisc}">
			<c:param name="chiaveCodFisc">${chiaveCodFisc}</c:param> 
		</c:if>
		<c:if test="${!empty tx_button_cerca}">
			<c:param name="tx_button_cerca">OK</c:param> 
		</c:if>
		
		
		<c:param name="DDLType">11</c:param>
		</c:url>
		</s:action>


		<%-- SOCIETA --%>
		<s:dgcolumn index="1"  label="Societ&agrave;" asc="SOCA" desc="SOCD"></s:dgcolumn>
		
		<%-- UTENTE --%>
		<s:dgcolumn index="2"  label="Utente" asc="UTEA" desc="UTED"></s:dgcolumn>

		<%-- ENTE --%>
		<s:dgcolumn index="3"  label="Ente" asc="ENTA" desc="ENTD"></s:dgcolumn>

		<!-- RAR_CRARCCAR -->
		<s:dgcolumn index="9"  label="Nr Cartella" asc="DNPA" desc="DNPD"></s:dgcolumn>

  	    <!-- RPA_NRPAANNO -->
		<s:dgcolumn index="4" label="Anno"  asc="DANA" desc="DAND"></s:dgcolumn>

        <!--  RPA_NRPANUME -->
		<s:dgcolumn index="5" label="Num" asc="DNMA" desc="DNMD"></s:dgcolumn>

		<!-- RPA_CRPACFIS -->
		<s:dgcolumn index="6" label="Cod. Fisc." asc="CFSA" desc="CFSD"></s:dgcolumn>
		<!-- RPA_CRPACIDE -->
		<s:dgcolumn index="8" label="Partita" ></s:dgcolumn>
		<!-- CARICO -->
		<s:dgcolumn index="10" format="#,##0.00" label="Carico" asc="CARA" desc="CARD" css="text_align_right"></s:dgcolumn>

		<!-- DIM. CARICO -->
		<s:dgcolumn index="11" format="#,##0.00" label="Dim.Carico" asc="DCAA" desc="DCAD" css="text_align_right"></s:dgcolumn>

		<!-- RISCOSSO -->
		<s:dgcolumn index="13" format="#,##0.00" label="Riscosso" asc="RSCA" desc="RSCD" css="text_align_right"></s:dgcolumn>
        
		<!-- RIMBORSO -->
		<s:dgcolumn index="14" format="#,##0.00" label="Rimborso" asc="RIMA" desc="RIMD" css="text_align_right"></s:dgcolumn>
        
		<!-- RESIDUO asc="RSDA" desc="RSDD"-->
		<s:dgcolumn index="15" format="#,##0.00" label="Residuo" asc="RSDA" desc="RSDD" css="text_align_right"></s:dgcolumn>

        <s:ifdatagrid left="${ext}" control="eq" right="1">
		</s:ifdatagrid>

	    <s:dgcolumn label="Dett." >
	    	&nbsp;
			
			<s:hyperlink href="dettaglioPartita.do${formParameters}&tx_button_cerca_exp=OK&chiaveNumPar={7}&chiaveConcPar={18}&chiaveTombPar={19}&chiaveAnnoRuo={4}&chiaveNumRuo={5}&r2RowsPerPage=${param.rowsPerPage}&r2PageNumber=${param.pageNumber}&r2Order=${param.order}&pagPrec=ricercaAnagrafica" 
			alt="Dettaglio Partita" 
			imagesrc="../applications/templates/shared/img/dettaglio.png"			
			text="" 
			cssclass="blacklink hlStyle" />
			&nbsp;
        </s:dgcolumn>
	    <s:dgcolumn label="Art." >
			{16}&nbsp;
			<s:hyperlink href="ricercaArticoli.do${formParameters}&tx_button_cerca_exp=OK&chiaveNumPar={7}&chiaveConcPar={18}&chiaveTombPar={19}&chiaveAnnoRuo={4}&chiaveNumRuo={5}&r2RowsPerPage=${param.rowsPerPage}&r2PageNumber=${param.pageNumber}&r2Order=${param.order}&pagPrec=ricercaAnagrafica" 
			alt="Lista Articoli" 
			imagesrc="../applications/templates/shared/img/listaDati.png"
			text="" 
			cssclass="blacklink hlStyle" />
    
		</s:dgcolumn>	    
	    
	   	    
	    <s:dgcolumn label="Pag." >
			<s:if left="{17}" control="eq" right="0">
			<s:then>
			&nbsp;&nbsp;
			</s:then>
			<s:else>
			{17}&nbsp;
			<s:hyperlink href="ricercaPagamentiDett.do${formParameters}&tx_button_cerca_exp=OK&chiaveNumPar={7}&chiaveConcPar={18}&chiaveTombPar={19}&chiaveAnnoRuo={4}&chiaveNumRuo={5}&r2RowsPerPage=${param.rowsPerPage}&r2PageNumber=${param.pageNumber}&r2Order=${param.order}&pagPrec=ricercaAnagrafica" 
			alt="Lista Pagamenti" 
			imagesrc="../applications/templates/shared/img/listaDati.png"
			text="" 
			cssclass="blacklink hlStyle" />
			</s:else>
			</s:if>
        </s:dgcolumn>
    </s:datagrid>
</s:div>
<s:div name="divDatiAgg" cssclass="divTableTitle bold text_align_right">
	<c:if test="${userProfile=='AMEN'}" >
			 Dati aggiornati al: <s:formatDate pattern="dd/MM/yyyy" value="${dataAggiornamento}"/>
	</c:if>
</s:div>


</c:if>
	<s:div name="indietro" cssclass="divRicBottoni">
		<s:form name="ricercaRuoli" action="ricercaAnagrafiche.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:textbox name="rowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rRowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="pageNumber" label="pageNumberRic" bmodify="true" text="${rPageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="order" label="orderRic" bmodify="true" text="${rOrder}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="ext" label="extRic" bmodify="true" text="${rExt}" cssclass="display_none" cssclasslabel="display_none"  />
			
			<s:textbox name="tx_societa" label="tx_societaRic" bmodify="true" text="${tx_societa}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tx_utente" label="tx_utenteRic" bmodify="true" text="${tx_utente}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tx_UtenteEnte" label="tx_UtenteEnteRic" bmodify="true" text="${tx_UtenteEnte}" cssclass="display_none" cssclasslabel="display_none"  />

			<s:textbox name="annoRuolo" label="annoRuoloRic" bmodify="true" text="${annoRuolo}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="numRuolo" label="numRuoloRic" bmodify="true" text="${numRuolo}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="ufficioImpositore" label="ufficioImpositoreRic" bmodify="true" text="${ufficioImpositore}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_consegna_da_day" label="dataconsegnadadayRic" bmodify="true" text="${data_consegna_da_day}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_consegna_da_month" label="dataconsegnadamonthRic" bmodify="true" text="${data_consegna_da_month}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_consegna_da_year" label="dataconsegnadayearRic" bmodify="true" text="${data_consegna_da_year}" cssclass="display_none" cssclasslabel="display_none"  />

			<s:textbox name="data_consegna_a_day" label="dataconsegnaadayRic" bmodify="true" text="${data_consegna_a_day}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_consegna_a_month" label="dataconsegnaamonthRic" bmodify="true" text="${data_consegna_a_month}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_consegna_a_year" label="dataconsegnaayearRic" bmodify="true" text="${data_consegna_a_year}" cssclass="display_none" cssclasslabel="display_none"  />

			<!-- ricerca Anagrafica -->
			<s:textbox name="codFisc" label="codFiscRic" bmodify="true" text="${codFisc}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tipoRic" label="tipoRicRic" bmodify="true" text="${tipoRic}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="denominazione" label="denominazioneRic" bmodify="true" text="${denominazione}" cssclass="display_none" cssclasslabel="display_none"  />

			<s:button id="tx_button_cerca" cssclass="btnStyle" type="submit" text="Indietro" onclick=""/>
		</s:form>		
	</s:div>


			  