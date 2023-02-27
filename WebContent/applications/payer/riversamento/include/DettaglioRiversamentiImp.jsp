<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="viewstate" encodeAttributes="true" />

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
		<c:if test="${!empty param.rRowsPerPage}">
			<c:param name="rowsPerPage">${param.rRowsPerPage}</c:param>
		</c:if>
		<c:if test="${!empty param.rPageNumber}">  
			<c:param name="pageNumber">${param.rPageNumber}</c:param>
		</c:if>
		<c:if test="${!empty param.rOrder}">  
			<c:param name="order">${param.rOrder}</c:param>
		</c:if>
		<c:if test="${!empty param.codSocieta}">  
			<c:param name="codSocieta">${param.codSocieta}</c:param>
		</c:if>
		<c:if test="${!empty param.codProvincia}">  
			<c:param name="codProvincia">${param.codProvincia}</c:param>
		</c:if>
		<c:if test="${!empty param.codUtente}">  
			<c:param name="codUtente">${param.codUtente}</c:param>
		</c:if>
		<c:if test="${!empty param.codBeneficiario}">  
			<c:param name="codBeneficiario">${param.codBeneficiario}</c:param>
		</c:if>
		<c:if test="${!empty param.tipoRendicontazione}">  
			<c:param name="tipoRendicontazione">${param.tipoRendicontazione}</c:param>
		</c:if>
		<c:if test="${!empty param.strumRiversamento}">  
			<c:param name="strumRiversamento">${param.strumRiversamento}</c:param>
		</c:if>
		<c:if test="${!empty param.statoRiversamento}">  
			<c:param name="statoRiversamento">${param.statoRiversamento}</c:param>
		</c:if>
		<c:if test="${!empty param.data_riversamento_da_day}">  
			<c:param name="data_riversamento_da_day">${param.data_riversamento_da_day}</c:param>
		</c:if>
		<c:if test="${!empty param.data_riversamento_da_month}">  
			<c:param name="data_riversamento_da_month">${param.data_riversamento_da_month}</c:param>
		</c:if>
		<c:if test="${!empty param.data_riversamento_da_year}">  
			<c:param name="data_riversamento_da_year">${param.data_riversamento_da_year}</c:param>
		</c:if>
		<c:if test="${!empty param.data_riversamento_a_day}">  
			<c:param name="data_riversamento_a_day">${data_riversamento_a_day}</c:param>
		</c:if>
		<c:if test="${!empty param.data_riversamento_a_month}">  
			<c:param name="data_riversamento_a_month">${param.data_riversamento_a_month}</c:param>
		</c:if>
		<c:if test="${!empty param.data_riversamento_a_year}">  
			<c:param name="data_riversamento_a_year">${param.data_riversamento_a_year}</c:param>
		</c:if>
		<c:if test="${!empty ext}">
			<c:param name="ext">${ext}</c:param>
		</c:if>
		<c:if test="${!empty megaBottoneAlert}">
			<c:param name="megaBottoneAlert">${megaBottoneAlert}</c:param>
		</c:if>
	</c:url>

		<s:div name="div_selezione" cssclass="div_align_center divSelezione">
				<s:form name="ricercaRiversamentiDettImpForm" action="ricercaRiversamenti.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
				</s:form>
		</s:div>

	<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
		<s:thead>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:th cssclass="seda-ui-datagridheadercell"  icol="17"><b>Riversamento</b></s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr cssclass="seda-ui-datagridrowdispari">
				<s:td cssclass="seda-ui-datagridcell">Data Rivers.</s:td>
			    <s:td cssclass="seda-ui-datagridcell">Ente Ben.</s:td>
				<s:td cssclass="seda-ui-datagridcell">Ufficio Ben.</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Tipo Rend.</s:td>
				<s:td cssclass="seda-ui-datagridcell">Strumento Riv.</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Stato Riv.</s:td>
				<s:td cssclass="seda-ui-datagridcell">Importo</s:td>
				<s:td cssclass="seda-ui-datagridcell">Contributo cittadino</s:td>
				<s:td cssclass="seda-ui-datagridcell">Commis. Banca</s:td>
				<s:td cssclass="seda-ui-datagridcell">Spese Notifica</s:td>
				<s:td cssclass="seda-ui-datagridcell">Commis. gestore</s:td>
				<s:td cssclass="seda-ui-datagridcell">Importo da riversare</s:td>
				<s:td cssclass="seda-ui-datagridcell">Importo da recuperare</s:td>
			
			<!-- 	
				Data Chiusura
				Data Sospensione
				Data Esecuzione
				Data Bonifico				
				-->
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
					<s:td cssclass="seda-ui-datagridcell"><c:out value="${c1S}"/></s:td>  <!-- data -->					
					<s:td cssclass="seda-ui-datagridcell"><c:out value="${c2}"/></s:td>  <!-- ANE_CANECENT -->
					<s:td cssclass="seda-ui-datagridcell"><c:out value="${c3S}"/></s:td>  <!-- ANE_TANETUFF/ANE_CANECUFF -->
					<s:td cssclass="seda-ui-datagridcell"><c:out value="${c5S}"/></s:td>  <!--  REV_FREVTIPO R Rivers. C Rendic.-->
					<s:td cssclass="seda-ui-datagridcell"><c:out value="${c6S}"/></s:td>  <!-- REV_FREVRIVE -->
					<s:td cssclass="seda-ui-datagridcell"><c:out value="${c7S}"/></s:td>  <!-- REV_FREVSTAT -->
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${c8}" minFractionDigits="2" maxFractionDigits="2"/></s:td>  <!-- REV_IREVTOTA -->
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${c9}" minFractionDigits="2" maxFractionDigits="2"/></s:td>  <!-- REV_IREVCONC -->
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${c10}" minFractionDigits="2" maxFractionDigits="2"/></s:td>  <!-- REV_IREVCGTW -->
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${c11}" minFractionDigits="2" maxFractionDigits="2"/></s:td>  <!-- REV_IREVSPES -->
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${c12}" minFractionDigits="2" maxFractionDigits="2"/></s:td>  <!-- REV_IREVGESC -->
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${c13}" minFractionDigits="2" maxFractionDigits="2"/></s:td>  <!-- REV_IREVIREV_R -->
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${c21}" minFractionDigits="2" maxFractionDigits="2"/></s:td>  <!-- REV_IREVIREV_C -->		
					<!--  
					${c14S}
					${c15S}
					${c16S}
					${c17S}
					-->
			</s:tr>
		</s:tbody>
	</s:table>
			
			
<!-- inizio -->


<c:if test="${!empty listaDettaglioImp}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Dettaglio Riversamento
	</s:div>

	<s:div name="divRisultati" cssclass="divRisultati">

	<s:datagrid  viewstate="" cachedrowset="listaDettaglioImp" action="riversamentiDettaglioImp.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
		<s:action>
			<c:url value="riversamentiDettaglioImp.do">				
				<c:if test="${!empty param.rRowsPerPage}">
					<c:param name="rRowsPerPage">${param.rRowsPerPage}</c:param>
				</c:if>
				<c:if test="${!empty param.rPageNumber}">  
					<c:param name="rPageNumber">${param.rPageNumber}</c:param>
				</c:if>
				<c:if test="${!empty param.rOrder}">  
					<c:param name="rOrder">${param.rOrder}</c:param>
				</c:if>
				<c:if test="${!empty param.codSocieta}">  
					<c:param name="codSocieta">${param.codSocieta}</c:param>
				</c:if>
				<c:if test="${!empty param.codProvincia}">  
					<c:param name="codProvincia">${param.codProvincia}</c:param>
				</c:if>
				<c:if test="${!empty param.codUtente}">  
					<c:param name="codUtente">${param.codUtente}</c:param>
				</c:if>
				<c:if test="${!empty param.codBeneficiario}">  
					<c:param name="codBeneficiario">${param.codBeneficiario}</c:param>
				</c:if>
				<c:if test="${!empty param.tipoRendicontazione}">  
					<c:param name="tipoRendicontazione">${param.tipoRendicontazione}</c:param>
				</c:if>
				<c:if test="${!empty param.strumRiversamento}">  
					<c:param name="strumRiversamento">${param.strumRiversamento}</c:param>
				</c:if>
				<c:if test="${!empty param.statoRiversamento}">  
					<c:param name="statoRiversamento">${param.statoRiversamento}</c:param>
				</c:if>
				<c:if test="${!empty param.data_riversamento_da_day}">  
					<c:param name="data_riversamento_da_day">${param.data_riversamento_da_day}</c:param>
				</c:if>
				<c:if test="${!empty param.data_riversamento_da_month}">  
					<c:param name="data_riversamento_da_month">${param.data_riversamento_da_month}</c:param>
				</c:if>
				<c:if test="${!empty param.data_riversamento_da_year}">  
					<c:param name="data_riversamento_da_year">${param.data_riversamento_da_year}</c:param>
				</c:if>
				<c:if test="${!empty param.data_riversamento_a_day}">  
					<c:param name="data_riversamento_a_day">${data_riversamento_a_day}</c:param>
				</c:if>
				<c:if test="${!empty param.data_riversamento_a_month}">  
					<c:param name="data_riversamento_a_month">${param.data_riversamento_a_month}</c:param>
				</c:if>
				<c:if test="${!empty param.data_riversamento_a_year}">  
					<c:param name="data_riversamento_a_year">${param.data_riversamento_a_year}</c:param>
				</c:if>
				<c:if test="${!empty ext}">
					<c:param name="ext">${ext}</c:param>
				</c:if>
				<c:if test="${!empty megaBottoneAlert}">
					<c:param name="megaBottoneAlert">${megaBottoneAlert}</c:param>
				</c:if>
				
				<!-- inizio parametri query -->
				<c:if test="${!empty param.revSoc}">  
					<c:param name="revSoc">${param.revSoc}</c:param>
				</c:if>
				<c:if test="${!empty param.revUte}">  
					<c:param name="revUte">${param.revUte}</c:param>
				</c:if>
				<c:if test="${!empty param.revData}">  
					<c:param name="revData">${param.revData}</c:param>
				</c:if>
				<c:if test="${!empty param.revBen}">  
					<c:param name="revBen">${param.revBen}</c:param>
				</c:if>
				<c:if test="${!empty param.revTipo}">  
					<c:param name="revTipo">${param.revTipo}</c:param>
				</c:if>
				<c:if test="${!empty param.revRive}">  
					<c:param name="revRive">${param.revRive}</c:param>
				</c:if>
				<c:param name="c2">${c2}</c:param>
				<c:param name="c3">${c3}</c:param>
				<c:param name="c4">${c4}</c:param>
				<c:param name="c7">${c7}</c:param>
				<c:param name="c8">${c8}</c:param>
				<c:param name="c9">${c9}</c:param>
				<c:param name="c10">${c10}</c:param>
				<c:param name="c11">${c11}</c:param>
				<c:param name="c12">${c12}</c:param>
				<c:param name="c13">${c13}</c:param>
				<c:param name="c14">${c14}</c:param>
				<c:param name="c15">${c15}</c:param>
				<c:param name="c16">${c16}</c:param>
				<c:param name="c17">${c17}</c:param>
				<c:param name="c18">${c18}</c:param>
				<c:param name="c21">${c21}</c:param>
				<c:param name="c22">${c22}</c:param>
				<c:param name="c23">${c23}</c:param>
			</c:url>
		</s:action>

 
  	    <!-- ANE_CANECENT -->
		<s:dgcolumn index="1" label="Ente Impositore" asc="CENTA" desc="CENTD"></s:dgcolumn>


		<!-- ANE_TANETUFF/ANE_CANECUFF -->
		<s:dgcolumn label="Ufficio Impositore" asc="TUFFA" desc="TUFFD">
			<s:if left="{2}{3}" control="eq" right="">
					<s:then>
						&nbsp;
					</s:then>
					<s:else>
						{2}/{3}
					</s:else>
			</s:if>		    
		</s:dgcolumn>



        <!--  RED_CTSECTSE -->
		<s:dgcolumn index="4" label="Tipo Serv." asc="CTSEA" desc="CTSED"></s:dgcolumn>

		<!-- RED_CREDANNO -->
		<s:dgcolumn index="5" label="Anno Riferim." asc="ANNOA" desc="ANNOD" css="text_align_right"></s:dgcolumn>

		<!-- CAN_DCANDCAN -->
		<s:dgcolumn index="6" label="Canale" asc="DCANA" desc="DCAND"></s:dgcolumn>

		<!-- GTW_DGTWDGTW -->
		<s:dgcolumn index="7" label="Tipo Carta" asc="DGTWA" desc="DGTWD"></s:dgcolumn>

		<!-- RED_GREDPAGA -->
		<s:dgcolumn index="8" format="dd/MM/yyyy" label="Data Pagamento" asc="PAGAA" desc="PAGAD"></s:dgcolumn>

<!-- 
       											     
		 RED_IREDTOTA, RED_IREDCONC, RED_IREDCGTW, RED_IREDSPES, RED_IREDGESC, 
		 RED_IREDIREV_R, 
		 RED_IREDIREV_C
 -->

		<!-- RED_IREDTOTA -->
		<s:dgcolumn index="9" format="#,##0.00" label="Importo" asc="TOTA" desc="TOTA" css="text_align_right"></s:dgcolumn>

		<!-- RED_IREDCONC -->
		<s:dgcolumn index="10" format="#,##0.00" label="Contributo cittadino" asc="CONCA" desc="CONCD" css="text_align_right"></s:dgcolumn>

		<!-- RED_IREDCGTW -->
		<s:dgcolumn index="11" format="#,##0.00" label="Commissioni Banca" asc="CGTWA" desc="CGTWD" css="text_align_right"></s:dgcolumn>

		<!-- RED_IREDSPES -->
		<s:dgcolumn index="12" format="#,##0.00" label="Spese Notifica" asc="SPESA" desc="SPESD" css="text_align_right"></s:dgcolumn>

		<!-- RED_IREDGESC -->
		<s:dgcolumn index="13" format="#,##0.00" label="Commissioni gestore" asc="GESCA" desc="GESCD" css="text_align_right"></s:dgcolumn>

		<!-- RED_IREDIREV_R -->
		<s:dgcolumn index="14" format="#,##0.00" label="Importo Rive. Benef." asc="IREVRA" desc="IREVRD" css="text_align_right"></s:dgcolumn>

		<!-- RED_IREDIREV_C -->
		<s:dgcolumn index="15" format="#,##0.00" label="Importo Recupero" asc="IREVCA" desc="IREVCD" css="text_align_right"></s:dgcolumn>
		
	</s:datagrid>
	

	<!-- 
	
	-----------------
	tabella riassunto
	-----------------
	 -->

	<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
		<s:thead>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:th cssclass="seda-ui-datagridheadercell"  icol="8"><b>Riepilogo Statistico</b></s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
		
			<s:tr cssclass="seda-ui-datagridrowdispari">
				<s:td cssclass="seda-ui-datagridcell">Importo</s:td>
				<s:td cssclass="seda-ui-datagridcell">Contributo Cittadino</s:td>
				<s:td cssclass="seda-ui-datagridcell">Commissioni Banca</s:td>
				<s:td cssclass="seda-ui-datagridcell">Spese Notifica</s:td>
				<s:td cssclass="seda-ui-datagridcell">Commissioni Gestore</s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${TotImportoContribuenti}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${TotImportoCittadino}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${TotCommissioniGateway}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${TotSpeseNotifica}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${TotCommissioneGestore}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
			</s:tr>
		</s:tbody>
	</s:table>
</s:div>

</c:if>
			

	<s:div name="indietro" cssclass="divRicBottoni">
		<s:form name="ricercaRiversamenti" action="ricercaRiversamenti.do${formParameters}&DDLType=21" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
				<s:button id="tx_button_cerca" cssclass="btnStyle" type="submit" text="Indietro" onclick=""/>
		</s:form>		
	</s:div>
