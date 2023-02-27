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
				<s:form name="ricercaRiversamentiDettContrForm" action="ricercaRiversamenti.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
				</s:form>
		</s:div>


<c:if test="${!empty dettaglioEcc}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Dettaglio Rimborso
	</s:div>

	<s:div name="divRisultati" cssclass="divRisultati">

	<s:datagrid  viewstate="" cachedrowset="dettaglioEcc" action="nessuna.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
		<s:action>
			<c:url value="nessuna.do">				
			</c:url>
		</s:action>

  	    <!-- ANE_CANECENT/ANE_TANETUFF/ANE_CANECUFF -->
		<s:dgcolumn label="Ente">
			<s:if left="{2}{3}" control="eq" right="">
					<s:then>
						{1}
					</s:then>
					<s:else>
						{1}/{2}/{3}
					</s:else>
			</s:if>		    
		</s:dgcolumn>

  	    <!-- ECT_GETCGDAT -->
		<s:dgcolumn format="dd/MM/yyyy" index="4" label="Data Ricezione" ></s:dgcolumn>

        <!--  ECC_CECCCFIS -->
		<s:dgcolumn index="5" label="Cod. Fiscale"></s:dgcolumn>

		<!-- ECC_CECCNDOC -->
		<s:dgcolumn index="6" label="Documento"></s:dgcolumn>

		<!-- ECC_NECCRIMB -->
		<s:dgcolumn index="7" format="#,##0.00" label="Importo Rimborso" css="text_align_right"></s:dgcolumn>

		<!-- ECC_FECCFTIP -->
		<s:dgcolumn index="9" label="Strumento"></s:dgcolumn>

		<!-- ECC_GECCGRIM -->
		<s:dgcolumn index="10" format="dd/MM/yyyy" label="Data Ordine" ></s:dgcolumn>

		<!-- ECC_FECCFESI -->
		<s:dgcolumn index="12" label="Esito"></s:dgcolumn>

        <s:ifdatagrid left="${ext}" control="eq" right="1">
		</s:ifdatagrid>
		
	</s:datagrid>
	

	<!-- 
	
	-----------------
	tabella dettaglio
	-----------------
	 -->

	<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
		<s:thead>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:th cssclass="seda-ui-datagridheadercell"  icol="4"></s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
		
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Denominazione</s:td>
				<s:td cssclass="seda-ui-datagridcell">${ecc.denominazione}</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Importo Doc</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${ecc.importoTotale}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Indirizzo</s:td>
				<s:td cssclass="seda-ui-datagridcell">${ecc.indirizzo}</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Importo Pagato</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${ecc.importoPagato}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Comune Fiscale</s:td>
				<s:td cssclass="seda-ui-datagridcell">${ecc.descrizioneComuneFiscale}&nbsp;${ecc.provincia}&nbsp;${ecc.cap}</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Importo Discaricato</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${ecc.importoDiscaricato}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Email</s:td>
				<s:td cssclass="seda-ui-datagridcell">${ecc.email}</s:td>				
				<s:td cssclass="seda-ui-datagridcell">SMS</s:td>
				<s:td cssclass="seda-ui-datagridcell">${ecc.sms}</s:td>				
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Causale</s:td>
				<s:td cssclass="seda-ui-datagridcell">${ecc.causale}</s:td>				
				<s:td cssclass="seda-ui-datagridcell" icol="2">&nbsp;</s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">IBAN Ordinante</s:td>
				<s:td cssclass="seda-ui-datagridcell">${ecc.ibanOrd}</s:td>				
				<s:td cssclass="seda-ui-datagridcell" icol="2">&nbsp;</s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">IBAN Destinatario</s:td>
				<s:td cssclass="seda-ui-datagridcell">${ecc.ibanDest}</s:td>				
				<s:td cssclass="seda-ui-datagridcell" icol="2">&nbsp;</s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Modalit&agrave; Notifica</s:td>
				<s:td cssclass="seda-ui-datagridcell">${ecc.modalitaNotificaDec}</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Stato Notifica</s:td>
				<s:td cssclass="seda-ui-datagridcell">${ecc.flagEmailDec}</s:td>				
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Codice Univoco Disp.</s:td>
				<s:td cssclass="seda-ui-datagridcell">${ecc.codiceUnivoco}</s:td>				
				<s:td cssclass="seda-ui-datagridcell" icol="2">&nbsp;</s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell">Data Esito</s:td>
				<s:td cssclass="seda-ui-datagridcell">
					<c:if test="${dataEsitoPres}">
						<s:formatDate pattern="dd/MM/yyyy" value="${ecc.dataEsito}"/>
					</c:if>
				</s:td>				
				<s:td cssclass="seda-ui-datagridcell" icol="2">&nbsp;</s:td>
			</s:tr>

		</s:tbody>
	</s:table>
</s:div>

</c:if>

	<s:div name="indietro" cssclass="divRicBottoni">
		<s:form name="ricercaEccedenze" action="ricercaEccedenze.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:textbox name="rowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rRowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="pageNumber" label="pageNumberRic" bmodify="true" text="${rPageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="order" label="orderRic" bmodify="true" text="${rOrder}" cssclass="display_none" cssclasslabel="display_none"  />
	
			<s:textbox name="tx_societa" label="tx_societaRic" bmodify="true" text="${tx_societa}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tx_provincia" label="tx_provinciaRic" bmodify="true" text="${tx_provincia}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tx_UtenteEnte" label="tx_UtenteEnteRic" bmodify="true" text="${tx_UtenteEnte}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="codFiscale" label="codFiscaleRic" bmodify="true" text="${codFiscale}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="documento" label="documentoRic" bmodify="true" text="${documento}" cssclass="display_none" cssclasslabel="display_none"  />
	
			<s:textbox name="data_ricezione_da_day" label="data_ricezione_da_dayRic" bmodify="true" text="${data_ricezione_da_day}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_ricezione_da_month" label="data_ricezione_da_monthRic" bmodify="true" text="${data_ricezione_da_month}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_ricezione_da_year" label="data_ricezione_da_yearRic" bmodify="true" text="${data_ricezione_da_year}" cssclass="display_none" cssclasslabel="display_none"  />

			<s:textbox name="data_ricezione_a_day" label="data_ricezione_a_dayRic" bmodify="true" text="${data_ricezione_a_day}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_ricezione_a_month" label="data_ricezione_a_monthRic" bmodify="true" text="${data_ricezione_a_month}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_ricezione_a_year" label="data_ricezione_a_yearRic" bmodify="true" text="${data_ricezione_a_year}" cssclass="display_none" cssclasslabel="display_none"  />
	
			<s:textbox name="esitoRim" label="esitoRimRic" bmodify="true" text="${esitoRim}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="strumento" label="strumentoRic" bmodify="true" text="${strumento}" cssclass="display_none" cssclasslabel="display_none"  />
	
			<s:textbox name="data_ordine_da_day" label="data_ordine_da_dayRic" bmodify="true" text="${data_ordine_da_day}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_ordine_da_month" label="data_ordine_da_monthRic" bmodify="true" text="${data_ordine_da_month}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_ordine_da_year" label="data_ordine_da_yearRic" bmodify="true" text="${data_ordine_da_year}" cssclass="display_none" cssclasslabel="display_none"  />

			<s:textbox name="data_ordine_a_day" label="data_ordine_a_dayRic" bmodify="true" text="${data_ordine_a_day}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_ordine_a_month" label="data_ordine_a_monthRic" bmodify="true" text="${data_ordine_a_month}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="data_ordine_a_year" label="data_ordine_a_yearRic" bmodify="true" text="${data_ordine_a_year}" cssclass="display_none" cssclasslabel="display_none"  />
	
			<s:textbox name="ext" label="extRic" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none"  />
	
			<s:button id="tx_button_cerca" cssclass="btnStyle" type="submit" text="Indietro" onclick=""/>
		</s:form>		
	</s:div>

	