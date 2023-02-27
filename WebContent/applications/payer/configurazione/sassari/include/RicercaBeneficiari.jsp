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

<script type="text/javascript" >
	//set combo cambiata
	function setDdl(value) {
		var txtHidden = document.getElementById('DDLChanged');
		if (txtHidden != null)
			txtHidden.value = value;
	}
	
	function callSubmit(frm1) {  
	  frm1.submit();  
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

<!-- conservazione parametri stato form successiva --> 
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
		<c:if test="${!empty codSocieta}">  
			<c:param name="rCodSocieta">${codSocieta}</c:param>
		</c:if>
		<c:if test="${!empty codProvincia}">  
			<c:param name="rCodProvincia">${codProvincia}</c:param>
		</c:if>
		<c:if test="${!empty codUtente}">  
			<c:param name="rCodUtente">${codUtente}</c:param>
		</c:if>
		<c:if test="${!empty codBeneficiario}">  
			<c:param name="rCodBeneficiario">${codBeneficiario}</c:param>
		</c:if>
		<c:if test="${!empty DDLChanged}">  
			<c:param name="rDDLChanged">${DDLChanged}</c:param>
		</c:if>
	</c:url>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">

					<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
						Ricerca Emissioni Bonifici
					</s:div>

	<s:form name="ricercaBeneficiariForm" action="ricercaBeneficiari.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:textbox name="DDLType" label="DDLType" bmodify="true" text="13" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="DDLChanged" label="DDLChanged" bmodify="true" text="" cssclass="display_none" cssclasslabel="display_none"  />

		<s:textbox name="rRowsPerPage" label="rowsPerPage" bmodify="true" text="${rowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rPageNumber" label="pageNumber" bmodify="true" text="${pageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rOrder" label="order" bmodify="true" text="${order}" cssclass="display_none" cssclasslabel="display_none"  />
		
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">

					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
								
							<s:dropdownlist name="codSocieta" disable="${sessionScope.societaDdlDisabled}" 
											label="Societ&aacute;:" 
											cssclass="tbddl floatleft" 
											cssclasslabel="label85 bold floatleft textright"
											cachedrowset="listaSocieta" usexml="true" 
											onchange="setDdl('codSocieta'); callSubmit(this.form);"
											valueselected="${codSocieta}">
								<s:ddloption text="Tutte le Societ&agrave;" value=""/>
								<s:ddloption text="{2}" value="{1}"/>
							</s:dropdownlist>
							<noscript>
									<s:button id="tx_button_societa_changed" type="submit" text="" onclick="" cssclass="btnimgStyle floatleft"  title="Aggiorna" validate="false"/>
							</noscript>

								
			        </s:div>

					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
								
						<s:dropdownlist name="codProvincia" disable="${requestScope.provinciaDdlDisabled}" 
										cssclass="tbddl floatleft" 
										label="Provincia:" 
										cssclasslabel="label65 bold floatleft textright"
										cachedrowset="listaProvinceBen" usexml="true" 
										onchange="setDdl('codProvincia'); callSubmit(this.form);"
										valueselected="${codProvincia}">
							<s:ddloption text="Tutte le Province" value=""/>
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
								<s:button id="tx_button_provincia_changed" type="submit" text="" onclick="" cssclass="btnimgStyle floatleft"  title="Aggiorna"  validate="false" />
						</noscript>
								
			        </s:div>

					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
								
							<s:dropdownlist name="codUtente" disable="${requestScope.utenteDdlDisabled}" 
													cssclass="tbddlMax floatleft" 
													label="Utente:" 
													cssclasslabel="label65 bold textright"
													cachedrowset="listaUtenti" usexml="true" 
													onchange="setDdl('codUtente'); callSubmit(this.form);"
													valueselected="${codUtente}">
								<s:ddloption text="Tutti gli Utenti" value=""/>
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							<noscript>
									<s:button id="tx_button_utente_changed" type="submit" text="" onclick="" cssclass="btnimgStyle floatleft"  title="Aggiorna" validate="false" />
							</noscript>
								
			        </s:div>

				</s:div>

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

								<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
											<s:dropdownlist name="codBeneficiario" disable="${sessionScope.beneficiarioDdlDisabled}" 
																	cssclass="tbddl floatleft" 
																	label="Beneficiario:" 
																	cssclasslabel="label85 bold textright"
																	cachedrowset="listaBeneficiari" usexml="true" 
																	valueselected="${codBeneficiario}">
												<s:ddloption text="Tutti gli Enti Beneficiari" value=""/>
												<s:ddloption text="{2} {3}" value="{1}" />
											</s:dropdownlist>
								</s:div>					

				</s:div>


					
			</s:div>

					
				<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					<s:button id="tx_button_cerca" cssclass="btnStyle" type="submit" text="Cerca" onclick=""/>
					<s:button id="tx_button_reset" type="submit" text="Reset" onclick="" cssclass="btnStyle" />
					<s:button id="tx_button_aggiungi" cssclass="btnStyle" type="submit" text="Nuovo" onclick=""/>
				</s:div>

		</s:form>

<!-- 
				<s:div name="divRicercaBottoni2" cssclass="divRicBottoni">
					<s:form name="ricercaBeneficiariForm2" action="insUpdBeneficiarioStart.do${formParameters}&tastoIns=ok" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
						<s:button id="tx_button_aggiungi" cssclass="btnStyle" type="submit" text="Nuovo" onclick=""/>
					</s:form>
				</s:div>
-->
		</s:div>
	</s:div>

										

	
 	
<!--  

--------------------------------
Presentazione risultati
--------------------------------

 -->	

	
<c:if test="${!empty listaBeneficiari2}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Elenco Emissione Bonifici
	</s:div>
	
	<s:div name="divRisultati" cssclass="divRisultati">
		<s:datagrid  viewstate="" cachedrowset="listaBeneficiari2" action="ricercaBeneficiari.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
			<s:action>
				<c:url value="ricercaBeneficiari.do">				
			<c:if test="${!empty codSocieta}">  
				<c:param name="codSocieta">${codSocieta}</c:param>
			</c:if>
			<c:if test="${!empty codProvincia}">  
				<c:param name="codProvincia">${codProvincia}</c:param>
			</c:if>
			<c:if test="${!empty codUtente}">  
				<c:param name="codUtente">${codUtente}</c:param>
			</c:if>
			<c:if test="${!empty codBeneficiario}">  
				<c:param name="codBeneficiario">${codBeneficiario}</c:param>
			</c:if>
				<c:param name="DDLType">13</c:param>
				</c:url>
			</s:action>
	
	<!-- colonne -->	
	<!--
													BEN_CSOCCSOC, 
												    BEN_CUTECUTE, 
												    BEN_KANEKANE,
											        ANE_CANECENT, ANE_TANETUFF, ANE_CANECUFF,
											  	    BEN_CBENCFIS, BEN_DBENDIND,
											  	    BEN_DBENEBEN, 
												    BEN_CBENCSIA,
											  	    CODPAESE, 
											  	    CODCTRL,
											  	    CIN, 
											  	    ABI,
											  	    CAB, 
											  	    CONTO,
											  	    BEN_CBENIBAN
  	   
	BEN_CBENCFIS, BEN_DBENDIND, BEN_DBENEBEN, BEN_CBENCSIA, 
	CODPAESE, CODCTRL, CIN, ABI, CAB, CONTO, BEN_CBENIBAN
	-->
	 
	  	    <!-- BEN_CUTECUTE -->
			<s:dgcolumn index="2" label="Utente"  asc="CUTEA" desc="CUTED"></s:dgcolumn>
	
	  	    <!-- ANE_CANECENT -->
			<s:dgcolumn index="4" label="Ente" asc="CENTA" desc="CENTD"></s:dgcolumn>
	
			<!-- ANE_TANETUFF/ANE_CANECUFF -->
			<s:dgcolumn label="Ufficio" asc="TUFFA" desc="TUFFD">
						<s:if left="{5}{6}" control="eq" right="">
								<s:then>
									&nbsp;
								</s:then>
								<s:else>
									{5}/{6}
								</s:else>
						</s:if>		    
			</s:dgcolumn>
	
	        <!--  BEN_CBENCFIS -->
			<s:dgcolumn index="7" label="Cod.Fisc./P.Iva" asc="CFISA" desc="CFISD"></s:dgcolumn>
	
			<!-- BEN_DBENDIND -->
			<s:dgcolumn index="8" label="Indirizzo" asc="DINDA" desc="DINDD"></s:dgcolumn>
	
			<!-- BEN_DBENEBEN -->
			<s:dgcolumn index="9" label="Email" asc="EBENA" desc="EBEND"></s:dgcolumn>
			
			<!-- BEN_CBENCSIA -->
			<s:dgcolumn index="10" label="Codice SIA" asc="CSIAA" desc="CSIAD"></s:dgcolumn>
	
			<!-- CODPAESE -->
			<s:dgcolumn index="11" label="Cod. Paese" asc="AESEA" desc="AESED"></s:dgcolumn>
	
			<!-- CODCTRL -->
			<s:dgcolumn index="12" label="Cod. Controllo" asc="CTRLA" desc="CTRLD"></s:dgcolumn>
	
			<!-- CIN -->
			<s:dgcolumn index="13" label="CIN" asc="CINA" desc="CIND"></s:dgcolumn>
	
			<!-- ABI -->
			<s:dgcolumn index="14" label="ABI" asc="ABIA" desc="ABID"></s:dgcolumn>
	
			<!-- CAB -->
			<s:dgcolumn index="15" label="CAB" asc="CABA" desc="CABD"></s:dgcolumn>
	
			<!-- CONTO -->
			<s:dgcolumn index="16" label="Conto" asc="ONTOA" desc="ONTOD"></s:dgcolumn>
	
			<s:dgcolumn label="&nbsp;&nbsp;Azioni&nbsp;&nbsp;">
				<s:hyperlink href="insUpdBeneficiarioStart.do${formParameters}&tx_button_edit=ok&tastoUpd=ok&codSocieta={1}&codUtente={2}&codBeneficiario={3}" 
					imagesrc="../applications/templates/configurazione/img/edit.png"
					text="" cssclass="hlStyle" />
				<s:hyperlink href="cancBeneficiarioStart.do${formParameters}&tx_button_delete=ok&tastoDel=ok&codSocieta={1}&codUtente={2}&codBeneficiario={3}" 
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					text="" cssclass="hlStyle" />
			</s:dgcolumn>
	
		</s:datagrid>
	</s:div>	
</c:if>
