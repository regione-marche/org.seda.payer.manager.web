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
		<c:if test="${!empty rowsPerPage}">
			<c:param name="rRowsPerPage">${rowsPerPage}</c:param>
		</c:if>
		<c:if test="${!empty pageNumber}">  
			<c:param name="rPageNumber">${pageNumber}</c:param>
		</c:if>
		<c:if test="${!empty order}">  
			<c:param name="rOrder">${order}</c:param>
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
			<c:param name="rExt">${ext}</c:param>
		</c:if>
	</c:url>



<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">

		<s:form name="ricercaAnagForm" action="ricercaAnagrafiche.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:textbox name="DDLType" label="DDLType" bmodify="true" text="11" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="ext" label="ext" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none" />
	
			<s:textbox name="hRowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="hPageNumber" label="pageNumberRic" bmodify="true" text="${pageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="hOrder" label="orderRic" bmodify="true" text="${order}" cssclass="display_none" cssclasslabel="display_none"  />
	
				<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
						Ricerca Anagrafiche
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
					
					<!-- -->
					
					
					
					
					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
	
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="impostaServ"
								disable="${ddlImpostaServizioDisabled}" cssclass="tbddlMax floatleft"
								label="Imp.Ser. (I/S):" cssclasslabel="label85 bold textright"
								cachedrowset="listaImpostaServizio" usexml="true"
								valueselected="${impostaServ}">
								<s:ddloption text="Tutti i Servizi" value="" />
								<s:ddloption text="{4}" value="{1}{2}{3}" />
							</s:dropdownlist>
						</s:div>
						<s:div name="divElement41" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist  name="tipoRic" disable="false" label="Denominazione:" valueselected="${tipoRic}" 
								                 cssclass="tbddlMax floatleft" cssclasslabel="label85 bold textright">
									<s:ddloption value="I" text="Inizia per"/>
									<s:ddloption value="C" text="Contiene"/>
									<s:ddloption value="F" text="Finisce per"/>
								</s:dropdownlist>
						</s:div>
						
						
					</s:div>
	
					<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
	
						<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">						
							<s:textbox validator="ignore"
								bmodify="true" name="codFiscale" label="Cod. Fiscale:"
								maxlenght="16"
								text="${codFiscale}"
								cssclass="textareaman"
								cssclasslabel="label85 bold textright" />
						</s:div>
	
	
	
						<s:div name="divElement42" cssclass="divRicMetadatiSingleRow">
								<s:textbox validator="ignore"
									bmodify="true" name="denominazione" label=""
									text="${denominazione}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
						</s:div>
	
					</s:div>
	
					<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
						<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						</s:div>
	
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						</s:div>
	
	
					</s:div>
	
						
				</s:div>
	
				<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					<s:button id="tx_button_cerca" type="submit" text="Cerca" onclick="" cssclass="btnStyle"/>
					<s:button id="tx_button_reset" type="submit" text="Reset" onclick="" cssclass="btnStyle" />
	
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
	
<c:if test="${!empty listaAnag}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Elenco Anagrafiche
	</s:div>

	<s:div name="divRisultati" cssclass="divRisultati">

		<s:datagrid  viewstate="" cachedrowset="listaAnag" action="ricercaAnagrafiche.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
			<s:action>
				<c:url value="ricercaAnagrafiche.do">
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
	
			<!-- EH1_CEH1CFIS -->
			<s:dgcolumn index="5" label="Cod.Fiscale" asc="CFISA" desc="CFISD"></s:dgcolumn>
	
	       <!-- EH8_DEH8DENO -->	
			<s:dgcolumn index="6" label="Denominazione" asc="DENOA" desc="DENOD"></s:dgcolumn>
	
	       <!-- APC_DAPCDCOM -->
			<s:dgcolumn index="7" label="Comune" asc="DCOMA" desc="DCOMD"></s:dgcolumn>
	
	       <!-- EH8_DEH8INDI -->	
			<s:dgcolumn index="8" label="Indirizzo" asc="INDIA" desc="INDID"></s:dgcolumn>
	
	       <!-- APC_CAPCCCAP -->	
			<s:dgcolumn index="9" label="CAP" asc="CCAPA" desc="CCAPD"></s:dgcolumn>
	
	       <!-- APC_CAPCSIGL -->	
			<s:dgcolumn index="10" label="Prov" asc="SIGLA" desc="SIGLD"></s:dgcolumn>
	
	       <!-- NUM_DOCUMENTI -->	
			<s:dgcolumn label="Doc">
			{11}&nbsp;
				<s:hyperlink href="ricercaDettaglioAnag.do${formParameters}&tx_button_cerca=OK&chiaveAnaFlusso={12}&chiaveCodUte={2}&chiaveAnaDataCrea={13}&chiaveAnaServ={14}
				&chiaveCodEnte={3}&chiaveTipoUff={15}&chiaveCodUff={16}&chiaveIS={4}&chiaveCodFisc={5}&chiaveTomb={17}"
				 imagesrc="../applications/templates/shared/img/listaDati.png"
				 alt="Lista Documenti"
				 text=""
				 cssclass="blacklink hlStyle" />
			</s:dgcolumn>
	
	
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
