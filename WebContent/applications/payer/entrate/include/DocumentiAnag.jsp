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
		<c:if test="${!empty rowsPerPage}">
			<c:param name="r2RowsPerPage">${rowsPerPage}</c:param>
		</c:if>
		<c:if test="${!empty pageNumber}">  
			<c:param name="r2PageNumber">${pageNumber}</c:param>
		</c:if>
		<c:if test="${!empty order}">  
			<c:param name="r2Order">${order}</c:param>
		</c:if>
		<c:if test="${!empty ext}">
			<c:param name="r2Ext">${ext}</c:param>
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
		<c:if test="${!empty codFiscale}">  
			<c:param name="codFiscale">${codFiscale}</c:param>
		</c:if>
		<c:if test="${!empty tipoRic}">  
			<c:param name="tipoRic">${tipoRic}</c:param>
		</c:if>
		<c:if test="${!empty denominazione}">  
			<c:param name="denominazione">${denominazione}</c:param>
		</c:if>

		<c:if test="${!empty chiaveAnaFlusso}">
			<c:param name="chiaveAnaFlusso">${chiaveAnaFlusso}</c:param>
		</c:if>
		<c:if test="${!empty chiaveCodUte}">
			<c:param name="chiaveCodUte">${chiaveCodUte}</c:param>
		</c:if>
		<c:if test="${!empty chiaveAnaDataCrea}">
			<c:param name="chiaveAnaDataCrea">${chiaveAnaDataCrea}</c:param>
		</c:if>
		<c:if test="${!empty chiaveAnaServ}">
			<c:param name="chiaveAnaServ">${chiaveAnaServ}</c:param>
		</c:if>
		<c:if test="${!empty chiaveCodEnte}">
			<c:param name="chiaveCodEnte">${chiaveCodEnte}</c:param>
		</c:if>
		<c:if test="${!empty chiaveTipoUff}">
			<c:param name="chiaveTipoUff">${chiaveTipoUff}</c:param>
		</c:if>
		<c:if test="${!empty chiaveCodUff}">
			<c:param name="chiaveCodUff">${chiaveCodUff}</c:param>
		</c:if>
		<c:if test="${!empty chiaveIS}">
			<c:param name="chiaveIS">${chiaveIS}</c:param>
		</c:if>
		<c:if test="${!empty chiaveCodFisc}">
			<c:param name="chiaveCodFisc">${chiaveCodFisc}</c:param>
		</c:if>
		<c:if test="${!empty chiaveTomb}">
			<c:param name="chiaveTomb">${chiaveTomb}</c:param>
		</c:if>
		<c:param name="DDLType">11</c:param>
	</c:url>


	<c:url value="" var="formParametersDett">
			<c:param name="test">ok</c:param>
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
			<c:if test="${!empty rowsPerPage}">
				<c:param name="rowsPerPage">${rowsPerPage}</c:param>
			</c:if>
			<c:if test="${!empty pageNumber}">  
				<c:param name="pageNumber">${pageNumber}</c:param>
			</c:if>
			<c:if test="${!empty order}">  
				<c:param name="order">${order}</c:param>
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
			<c:if test="${!empty codFiscale}">  
				<c:param name="codFiscale">${codFiscale}</c:param>
			</c:if>
			<c:if test="${!empty tipoRic}">  
				<c:param name="tipoRic">${tipoRic}</c:param>
			</c:if>
			<c:if test="${!empty denominazione}">  
				<c:param name="denominazione">${denominazione}</c:param>
			</c:if>

			<c:if test="${!empty chiaveAnaFlusso}">
				<c:param name="chiaveAnaFlusso">${chiaveAnaFlusso}</c:param>
			</c:if>
			<c:if test="${!empty chiaveCodUte}">
				<c:param name="chiaveCodUte">${chiaveCodUte}</c:param>
			</c:if>
			<c:if test="${!empty chiaveAnaDataCrea}">
				<c:param name="chiaveAnaDataCrea">${chiaveAnaDataCrea}</c:param>
			</c:if>
			<c:if test="${!empty chiaveAnaServ}">
				<c:param name="chiaveAnaServ">${chiaveAnaServ}</c:param>
			</c:if>
			<c:if test="${!empty chiaveCodEnte}">
				<c:param name="chiaveCodEnte">${chiaveCodEnte}</c:param>
			</c:if>
			<c:if test="${!empty chiaveTipoUff}">
				<c:param name="chiaveTipoUff">${chiaveTipoUff}</c:param>
			</c:if>
			<c:if test="${!empty chiaveCodUff}">
				<c:param name="chiaveCodUff">${chiaveCodUff}</c:param>
			</c:if>
			<c:if test="${!empty chiaveIS}">
				<c:param name="chiaveIS">${chiaveIS}</c:param>
			</c:if>
			<c:if test="${!empty chiaveCodFisc}">
				<c:param name="chiaveCodFisc">${chiaveCodFisc}</c:param>
			</c:if>
			<c:if test="${!empty chiaveTomb}">
				<c:param name="chiaveTomb">${chiaveTomb}</c:param>
			</c:if>
			<c:param name="DDLType">11</c:param>
		</c:url>



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
		<s:dgcolumn index="11" label="Cod. Fiscale"></s:dgcolumn>

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

		<s:div name="div_selezione" cssclass="div_align_center divSelezione">
				<s:form name="ricercaDettaglioAnagForm" action="ricercaAnagrafiche.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
				</s:form>
		</s:div>


<c:if test="${!empty documentiAnag}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle2" cssclass="divTableTitle bold">
		Lista Documenti
	</s:div>

	<s:div name="divDocumentiAnag" cssclass="divRisultati">

	<s:datagrid  viewstate="" cachedrowset="documentiAnag" action="ricercaDocumentiAnag.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
		<s:action>
			<c:url value="ricercaDettaglioAnag.do">
				<c:param name="test">ok</c:param>
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
				<c:if test="${!empty tipoRic}">  
					<c:param name="tipoRic">${tipoRic}</c:param>
				</c:if>
				<c:if test="${!empty denominazione}">  
					<c:param name="denominazione">${denominazione}</c:param>
				</c:if>
				<c:if test="${!empty ext}">
					<c:param name="ext">${ext}</c:param>
				</c:if>

				<c:if test="${!empty chiaveAnaFlusso}">
					<c:param name="chiaveAnaFlusso">${chiaveAnaFlusso}</c:param>
				</c:if>
				<c:if test="${!empty chiaveCodUte}">
					<c:param name="chiaveCodUte">${chiaveCodUte}</c:param>
				</c:if>
				<c:if test="${!empty chiaveAnaDataCrea}">
					<c:param name="chiaveAnaDataCrea">${chiaveAnaDataCrea}</c:param>
				</c:if>
				<c:if test="${!empty chiaveAnaServ}">
					<c:param name="chiaveAnaServ">${chiaveAnaServ}</c:param>
				</c:if>
				<c:if test="${!empty chiaveCodEnte}">
					<c:param name="chiaveCodEnte">${chiaveCodEnte}</c:param>
				</c:if>
				<c:if test="${!empty chiaveTipoUff}">
					<c:param name="chiaveTipoUff">${chiaveTipoUff}</c:param>
				</c:if>
				<c:if test="${!empty chiaveCodUff}">
					<c:param name="chiaveCodUff">${chiaveCodUff}</c:param>
				</c:if>
				<c:if test="${!empty chiaveIS}">
					<c:param name="chiaveIS">${chiaveIS}</c:param>
				</c:if>
				<c:if test="${!empty chiaveCodFisc}">
					<c:param name="chiaveCodFisc">${chiaveCodFisc}</c:param>
				</c:if>
				<c:if test="${!empty chiaveTomb}">
					<c:param name="chiaveTomb">${chiaveTomb}</c:param>
				</c:if>
				<c:param name="DDLType">11</c:param>
			</c:url>
		</s:action>

		<!-- SOCIETA -->
		<s:dgcolumn index="1"  label="Societ&agrave;" asc="SOCA" desc="SOCD"></s:dgcolumn>
		
		<!-- UTENTE -->
		<s:dgcolumn index="2"  label="Utente" asc="UTEA" desc="UTED"></s:dgcolumn>

		<!-- ENTE -->
		<s:dgcolumn index="3"  label="Ente" asc="ENTA" desc="ENTD"></s:dgcolumn>

  	    <!-- EH1_CISECISE -->
		<s:dgcolumn index="4" label="I/S" asc="ISEA" desc="ISED" ></s:dgcolumn>

  	    <!-- EH1_CEH1ADOC -->
		<s:dgcolumn index="5" label="Anno"  asc="DANA" desc="DAND"></s:dgcolumn>


        <!--  EH1_CEH1EMIS -->
		<s:dgcolumn index="6" label="Numero" asc="EMISA" desc="EMISD"></s:dgcolumn>

		<!-- EH1_TANETUFF,EH1_CANECUFF -->
		<s:dgcolumn  label="Ufficio" asc="UFFA" desc="UFFD">
				<s:if left="{7}{8}" control="eq" right="">
						<s:then>
						</s:then>
						<s:else>
							{7}/{8}
						</s:else>
				</s:if>		    							
			</s:dgcolumn>

		<!-- EH1_CTSECTSE -->
		<s:dgcolumn index="9" label="Tip. Serv." asc="CTSA" desc="CTSA"></s:dgcolumn>

		<!-- FLAG DOCUMENTI RENDICONTATI -->
		<s:dgcolumn index="10" label="R" asc="RENDA" desc="RENDD"></s:dgcolumn>

		<!-- EH1_CEH1CFIS -->
		<s:dgcolumn index="11" label="Cod.Fiscale" asc="CFSA" desc="CFSD"></s:dgcolumn>
		
		<!-- TOT_RATE -->
		<s:dgcolumn index="12" label="RT" asc="NRTA" desc="NRTD"></s:dgcolumn>

		<!-- CARICO -->
		<s:dgcolumn index="13" format="#,##0.00" label="Carico" asc="CARA" desc="CARD" css="text_align_right"></s:dgcolumn>

		<!-- DIM. CARICO -->
		<s:dgcolumn index="14" format="#,##0.00" label="Dim.Carico" asc="DCAA" desc="DCAD" css="text_align_right"></s:dgcolumn>

		<!-- RISCOSSO -->
		<s:dgcolumn index="15" format="#,##0.00" label="Riscosso" asc="RSCA" desc="RSCD" css="text_align_right"></s:dgcolumn>
        
		<!-- RIMBORSO -->
		<%-- <s:dgcolumn index="16" format="#,##0.00" label="Rimborso" asc="RIMA" desc="RIMD" css="text_align_right"></s:dgcolumn> --%>
        
		<!-- RENDICONTATO  -->
		<%-- <s:dgcolumn index="17" format="#,##0.00" label="Rendicont." asc="RNDA" desc="RNDD" css="text_align_right"></s:dgcolumn> --%>

		<!-- RESIDUO -->
		<s:dgcolumn index="18" format="#,##0.00" label="Residuo" asc="RSDA" desc="RSDD" css="text_align_right"></s:dgcolumn>


		<!-- RESIDUO SCAD-->
        <s:ifdatagrid left="${ext}" control="eq" right="1">
			<s:thendatagrid>
				<s:dgcolumn index="26" format="#,##0.00" label="Residuo Scad." asc="RESA" desc="RESD" css="text_align_right"></s:dgcolumn>
			</s:thendatagrid>
		</s:ifdatagrid>

	    <s:dgcolumn label="Dett" >
			<s:hyperlink href="documentoDettaglio.do${formParameters}&tx_button_cerca_exp=OK&chiaveDoc={19}&chiaveServ={21}&chiaveFlusso={20}&pagPrec=ricercaAnagrafica" 
			             imagesrc="../applications/templates/shared/img/dettaglio.png"
			             alt="Dettaglio Documento"
			             text=""
			             cssclass="blacklink hlStyle" />
        </s:dgcolumn>

	    <s:dgcolumn label="Tr" >
			<s:if left="{24}" control="ne" right="0">
			<s:then>
			{24}
			<s:hyperlink href="ricercaTributi.do${formParameters}&tx_button_cerca_exp=OK&chiaveDoc={19}&chiaveServ={21}&chiaveFlusso={20}&pagPrec=ricercaAnagrafica" 
			            imagesrc="../applications/templates/shared/img/listaDati.png"
			            alt="Lista Tributi"
			            text=""
			            cssclass="blacklink hlStyle" />
			</s:then>
			<s:else>
			&nbsp;&nbsp;
			</s:else>
			</s:if>
        </s:dgcolumn>
	    <s:dgcolumn label="Sc">
			<s:if left="{12}" control="ne" right="0">
			<s:then>
			{12}
			<s:hyperlink href="ricercaScadenze.do${formParameters}&tx_button_cerca_exp=OK&chiaveDoc={19}&chiaveServ={21}&chiaveFlusso={20}&pagPrec=ricercaAnagrafica" 
			             imagesrc="../applications/templates/shared/img/listaDati.png"
			             alt="Lista Scadenze"
			             text=""
			             cssclass="blacklink hlStyle" />
  			</s:then>
			<s:else>
			&nbsp;&nbsp;
			</s:else>
			</s:if>
        </s:dgcolumn>
	    <s:dgcolumn label="Pg">
			<s:if left="{25}" control="ne" right="0">
			<s:then>
			{25}
			<s:hyperlink href="ricercaPagamentiDocumenti.do${formParameters}&tx_button_cerca_exp=OK&chiaveDoc={19}&chiaveServ={21}&chiaveFlusso={20}&pagPrec=ricercaAnagrafica" 
			             imagesrc="../applications/templates/shared/img/listaDati.png"
			             alt="Lista Pagamenti"
			             text=""
			             cssclass="blacklink hlStyle" />
        	</s:then>
			<s:else>
			&nbsp;&nbsp;
			</s:else>
			</s:if>
        </s:dgcolumn>


    </s:datagrid>
	
</s:div>

</c:if>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<table width="100%">
	<tr align="center">
	<td>
	<table>
		<tr>
			<td>
					<s:form name="ricercaAnagrafiche" action="ricercaAnagrafiche.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
						<s:textbox name="rowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rRowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
						<s:textbox name="pageNumber" label="pageNumberRic" bmodify="true" text="${rPageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
						<s:textbox name="order" label="orderRic" bmodify="true" text="${rOrder}" cssclass="display_none" cssclasslabel="display_none"  />
						<s:textbox name="ext" label="extRic" bmodify="true" text="${rExt}" cssclass="display_none" cssclasslabel="display_none"  />
				
						<s:textbox name="tx_societa" label="tx_societaRic" bmodify="true" text="${tx_societa}" cssclass="display_none" cssclasslabel="display_none"  />
						<s:textbox name="tx_utente" label="tx_utenteRic" bmodify="true" text="${tx_utente}" cssclass="display_none" cssclasslabel="display_none"  />
						<s:textbox name="tx_UtenteEnte" label="tx_UtenteEnteRic" bmodify="true" text="${tx_UtenteEnte}" cssclass="display_none" cssclasslabel="display_none"  />
			
						<s:textbox name="impostaServ" label="impostaServRic" bmodify="true" text="${impostaServ}" cssclass="display_none" cssclasslabel="display_none"  />
						<s:textbox name="codFiscale" label="codFiscaleRic" bmodify="true" text="${codFiscale}" cssclass="display_none" cssclasslabel="display_none"  />
						<s:textbox name="tipoRic" label="tipoRicRic" bmodify="true" text="${tipoRic}" cssclass="display_none" cssclasslabel="display_none"  />
						<s:textbox name="denominazione" label="denominazioneRic" bmodify="true" text="${denominazione}" cssclass="display_none" cssclasslabel="display_none"  />
				
						<s:button id="tx_button_cerca" cssclass="btnStyle" type="submit" text="Indietro" onclick=""/>
					</s:form>		

			</td>
			<td>
				<s:form name="ricercaDocumentiAnag" action="ricercaDettaglioAnag.do?${formParametersDett}" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			
						<c:if test="${!empty documentiAnag && ext=='0'}">
							<s:button id="tx_button_cerca_exp" type="submit" text="Espandi" onclick="" cssclass="btnStyle" />
						</c:if>
						<c:if test="${!empty documentiAnag && ext=='1'}">
							<s:button id="tx_button_cerca_exp" type="submit" text="Contrai" onclick="" cssclass="btnStyle" />
						</c:if>			
				</s:form>			
			</td>
		</tr>
	</table>
	</td></tr>
	</table>
			
</s:div>
