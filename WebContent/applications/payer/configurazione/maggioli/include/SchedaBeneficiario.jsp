<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="viewstate" encodeAttributes="true" />

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

	<c:url value="" var="formParameters">
		<c:param name="test">ok</c:param>
		<c:if test="${!empty rRowsPerPage}">
			<c:param name="rowsPerPage">${rRowsPerPage}</c:param>
		</c:if>
		<c:if test="${!empty rPageNumber}">  
			<c:param name="pageNumber">${rPageNumber}</c:param>
		</c:if>
		<c:if test="${!empty rOrder}">  
			<c:param name="order">${rOrder}</c:param>
		</c:if>
		<c:if test="${!empty rCodSocieta}">  
			<c:param name="codSocieta">${rCodSocieta}</c:param>
		</c:if>
		<c:if test="${!empty rCodProvincia}">  
			<c:param name="codProvincia">${rCodProvincia}</c:param>
		</c:if>
		<c:if test="${!empty rCodUtente}">  
			<c:param name="codUtente">${rCodUtente}</c:param>
		</c:if>
		<c:if test="${!empty rCodBeneficiario}">  
			<c:param name="codBeneficiario">${rCodBeneficiario}</c:param>
		</c:if>

		<c:if test="${!empty DDLChanged}">
			<c:param name="DDLChanged">${rDDLChanged}</c:param>
		</c:if>				
	</c:url>
 

	
<!--  

--------------------------------
Messaggio di errore
--------------------------------

 -->	
	
<!-- 	
	<s:div name="divMessage">
		<c:if test="${!empty riv_message}">
			<hr/><s:label name="message" text="${riv_message}"/><hr/>
		</c:if>
		<c:if test="${!empty riv_error_message}">
			<hr/><s:label name="error_message" text="${riv_error_message}"/><hr/>
		</c:if>
	</s:div>
 -->

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">

			<s:div name="divRicercaTitleName" cssclass="divRicTitle">
				Emissione Bonifico
			</s:div>

	<s:form name="schedaBeneficiarioForm" action="insUpdCancBeneficiario.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:textbox name="DDLType" label="DDLType" bmodify="true" text="131" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="DDLChanged" label="DDLChanged" bmodify="true" text="" cssclass="display_none" cssclasslabel="display_none"  />
<!--  stato pagina -->

		<s:textbox name="rCodSocieta" label="codSocietaRic" bmodify="true" text="${rCodSocieta}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rCodProvincia" label="codProvinciaRic" bmodify="true" text="${rCodProvincia}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rCodUtente" label="codUtenteRic" bmodify="true" text="${rCodUtente}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rCodBeneficiario" label="codBeneficiarioRic" bmodify="true" text="${rCodBeneficiario}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rRowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rRowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rPageNumber" label="pageNumberRic" bmodify="true" text="${rPageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rOrder" label="orderRic" bmodify="true" text="${rOrder}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rDDLChanged" label="rDDLChanged" bmodify="true" text="${rDDLChanged}" cssclass="display_none" cssclasslabel="display_none"  />
				
    	<c:if test="${!empty tastoUpd}">
			<s:textbox name="tastoUpd" label="tastoUpd" bmodify="true" text="${tastoUpd}" cssclass="display_none" cssclasslabel="display_none"  />
		</c:if>				
		<c:if test="${!empty tastoIns}">
			<s:textbox name="tastoIns" label="tastoIns" bmodify="true" text="${tastoIns}" cssclass="display_none" cssclasslabel="display_none"  />
		</c:if>				
		<c:if test="${!empty tastoDel}">
			<s:textbox name="tastoDel" label="tastoDel" bmodify="true" text="${tastoDel}" cssclass="display_none" cssclasslabel="display_none"  />
		</c:if>				


			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">

					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<c:if test="${!empty tastoIns}">					
							<s:dropdownlist name="codSocieta" disable="${requestScope.societaDdlDisabled}" 
											validator="required" showrequired="true"
							                cssclass="tbddl floatleft" 
											label="Societ&aacute;:" 
											cssclasslabel="label85 bold floatleft textright"
											cachedrowset="listaSocieta" usexml="true" 
											onchange="setDdl('codSocieta'); callSubmit(this.form);"
											valueselected="${requestScope.codSocieta}">
								<s:ddloption text="" value=""/>
								<s:ddloption text="{2}" value="{1}"/>
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_societa_changed"  type="submit" text="" onclick="" cssclass="btnimgStyle floatleft"  title="Aggiorna" validate="false" />
							</noscript>
						</c:if>	
						<c:if test="${!empty tastoUpd or !empty tastoDel}">
							<s:dropdownlist name="codSocieta2" disable="true" 
											validator="required" showrequired="true"
							                cssclass="tbddl floatleft"
											label="Societ&aacute;:" 
											cssclasslabel="label85 bold textright"
											cachedrowset="listaSocieta" usexml="true" 
											valueselected="${requestScope.codSocieta}">
								<s:ddloption text="{2}" value="{1}"/>
							</s:dropdownlist>
							<s:textbox name="codSocieta" label="codSocieta" bmodify="true" text="${requestScope.codSocieta}" cssclass="display_none" cssclasslabel="display_none"  />
						</c:if>
					</s:div>

					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
						<c:if test="${!empty tastoIns}">					
							<s:dropdownlist name="codUtente" disable="${requestScope.utenteDdlDisabled}" 
													validator="required" showrequired="true"
													cssclass="tbddl floatleft" 
													label="Utente:" 
													cssclasslabel="label65 bold floatleft textright"
													cachedrowset="listaUtenti" usexml="true" 
													onchange="setDdl('codUtente'); callSubmit(this.form);"
													valueselected="${requestScope.codUtente}">
								<s:ddloption text="" value=""/>
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_utente_changed"  type="submit" text="" onclick="" cssclass="btnimgStyle floatleft"  title="Aggiorna" validate="false" />
							</noscript>
						</c:if>
						<c:if test="${!empty tastoUpd or !empty tastoDel}">
							<s:dropdownlist name="codUtente2" disable="true" 
													validator="required" showrequired="true"
													cssclass="tbddl floatleft" 
													label="Utente:" 
													cssclasslabel="label65 bold textright"
													cachedrowset="listaUtenti" usexml="true" 
													valueselected="${requestScope.codUtente}">
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							<s:textbox name="codUtente" label="codUtente" bmodify="true" text="${requestScope.codUtente}" cssclass="display_none" cssclasslabel="display_none"  />
						</c:if>
					</s:div>

					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<c:if test="${!empty tastoIns}">					
							<s:dropdownlist name="codAnagrafica" disable="${requestScope.anagraficaDdlDisabled}" 
													validator="required" showrequired="true"
													cssclass="tbddlMax floatleft" 
													label="Ente Benef.:" 
													cssclasslabel="label65 bold textright"
													cachedrowset="listaUtentiEntiAll" usexml="true" 
													valueselected="${requestScope.codAnagrafica}">
								<s:ddloption text="" value=""/>
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
						</c:if>
						<c:if test="${!empty tastoUpd or !empty tastoDel}">
							<s:dropdownlist name="codAnagrafica2" disable="true" 
													validator="required" showrequired="true"
													cssclass="tbddlMax floatleft" 
													label="Ente Benef.:" 
													cssclasslabel="label65 bold textright"
													cachedrowset="listaUtentiEntiAll" usexml="true" 
													valueselected="${requestScope.codAnagrafica}">
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							<s:textbox name="codAnagrafica" label="codAnagrafica" bmodify="true" text="${requestScope.codAnagrafica}" cssclass="display_none" cssclasslabel="display_none"  />
						</c:if>
			        </s:div>
				
				</s:div>
				
				
			<c:if test="${!empty tastoIns or !empty tastoUpd}">					
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
				
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_codSia" label="Codice Sia:" bmodify="true" text="${requestScope.im_codSia}" 
										   cssclass="textareaman" cssclasslabel="label85 bold textright"  
								           maxlenght="5" validator="ignore;minlength=5;maxlength=5" />
					</s:div>

					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_codFiscale" label="Cod.Fisc./P.Iva:" bmodify="true" text="${requestScope.im_codFiscale}" 
										   cssclass="textareaman" cssclasslabel="label85 bold textright"  
								           maxlenght="16" validator="ignore" />
					</s:div>
					
					<s:div name="divElement51" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_descBeneficiario" label="Descrizione:" bmodify="true" text="${requestScope.im_descBeneficiario}" 
										   cssclass="textareaman" cssclasslabel="label85 bold textright"  
								           maxlenght="256" validator="ignore" />
					</s:div>
					

			
			    </s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">

					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_iban_codPaese" label="Cod. Paese:" bmodify="true" text="${requestScope.im_iban_codPaese}" 
										   cssclass="textareaman" cssclasslabel="label85 bold textright"  
								           maxlenght="2" validator="ignore;minlength=2;maxlength=2" />
					</s:div>

					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_iban_codControllo" label="Cod.Controllo:" bmodify="true" text="${requestScope.im_iban_codControllo}" 
										   cssclass="textareaman" cssclasslabel="label85 bold textright"  
								           maxlenght="2" validator="ignore;digits;minlength=2;maxlength=2" />
					</s:div>

					<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_iban_cin" label="Codice CIN:" bmodify="true" text="${requestScope.im_iban_cin}"
										   cssclass="textareaman" cssclasslabel="label85 bold textright"  
								           maxlenght="1" validator="ignore;minlength=1;maxlength=1" />		
					</s:div>

					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_iban_abi" label="ABI:" bmodify="true" text="${requestScope.im_iban_abi}" 
										   cssclass="textareaman" cssclasslabel="label85 bold textright"  
								           maxlenght="5" validator="ignore;digits;minlength=5;maxlength=5" />	
					</s:div>

					<s:div name="divElement12" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_iban_cab" label="CAB:" bmodify="true" text="${requestScope.im_iban_cab}" 
										   cssclass="textareaman" cssclasslabel="label85 bold textright"  
								           maxlenght="5" validator="ignore;digits;minlength=5;maxlength=5" />	
					</s:div>

					<s:div name="divElement13" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_iban_cc" label="C\C:" bmodify="true" text="${requestScope.im_iban_cc}" 
										   cssclass="textareaman" cssclasslabel="label85 bold textright"  
								           maxlenght="12" validator="ignore;minlength=12;maxlength=12" />	
					</s:div>
				
				</s:div>

				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_indirizzo" label="Indirizzo:" bmodify="true" text="${requestScope.im_indirizzo}" 
										   cssclass="textareaman" cssclasslabel="label85 bold textright"  
								           maxlenght="256" validator="ignore" />
					</s:div>

					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_email" label="Email:" bmodify="true" text="${requestScope.im_email}" 
										   message="[accept=Email: ${msg_configurazione_lista_email}]"
										   cssclass="textareaman" cssclasslabel="label85 bold textright"  
								           maxlenght="256" validator="ignore;maxlength=256;accept=${configurazione_emailListBySemicolon}" />
					</s:div>
					
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist label="Tipologia File:" name="im_flagTipologiaFile" disable="false" cssclasslabel="label85 bold textright"
										  cssclass="textareaman"
										  valueselected="${requestScope.im_flagTipologiaFile}">
							<s:ddloption value="S" text="Tracciato Csv standard"/>
							<s:ddloption value="T" text="Tracciato Azienda Trasporti"/>
						</s:dropdownlist>
					</s:div>
				</s:div>
			</c:if>

			<c:if test="${!empty tastoDel}">					
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
				
					<!--<s:div name="divElement4" cssclass="divRicMetadatiSingleRowRead">
						<s:label name="im_codSialbl" cssclass="label110 bold" text="Codice Sia: ${requestScope.im_codSia}" />
					</s:div>

					<s:div name="divElement5" cssclass="divRicMetadatiSingleRowRead">
						<s:label name="im_codFiscalelbl" cssclass="label110 bold" text="Codice Fiscale: ${requestScope.im_codFiscale}" />
					</s:div>-->
					
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_codSialbl" label="Codice Sia:" bmodify="false" text="${requestScope.im_codSia}" 
										   cssclass="textareaman colordisabled" cssclasslabel="label85 bold textright"  />
					</s:div>

					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_codFiscalelbl" label="Cod.Fisc./P.Iva:" bmodify="false" text="${requestScope.im_codFiscale}" 
										   cssclass="textareaman colordisabled" cssclasslabel="label85 bold textright" />
					</s:div>
					<s:div name="divElement51" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_descBeneficiariolbl" label="Descrizione:" bmodify="false" text="${requestScope.im_descBeneficiario}" 
										   cssclass="textareaman colordisabled" cssclasslabel="label85 bold textright" />
					</s:div>
					

					<s:textbox name="im_codSia" label="im_codSia" bmodify="true" text="${requestScope.im_codSia}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="im_codFiscale" label="im_codFiscale" bmodify="true" text="${requestScope.im_codFiscale}" cssclass="display_none" cssclasslabel="display_none"  />
					<s:textbox name="im_descBeneficiario" label="Descrizione" bmodify="true" text="${requestScope.im_descBeneficiario}" cssclass="display_none" cssclasslabel="display_none"  />
			    </s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">

					<!--<s:div name="divElement8" cssclass="divRicMetadatiSingleRowRead">
						<s:label name="im_iban_codPaeselbl" cssclass="label110 bold" text="Cod. Paese: ${requestScope.im_iban_codPaese}" />
					</s:div>-->
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_iban_codPaeselbl" label="Cod. Paese:" bmodify="false" text="${requestScope.im_iban_codPaese}" 
										   cssclass="textareaman colordisabled" cssclasslabel="label85 bold textright"/>
					</s:div>
					<s:textbox name="im_iban_codPaese" label="im_iban_codPaese" bmodify="true" text="${requestScope.im_iban_codPaese}" cssclass="display_none" cssclasslabel="display_none"  />

					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_iban_codControllolbl" label="Cod.Controllo:" bmodify="false" text="${requestScope.im_iban_codControllo}" 
										   cssclass="textareaman colordisabled" cssclasslabel="label85 bold textright" />
					</s:div>
					<!--<s:div name="divElement9" cssclass="divRicMetadatiSingleRowRead">
						<s:label name="im_iban_codControllolbl" cssclass="label110 bold" text="Codice Controllo: ${requestScope.im_iban_codControllo}" />
					</s:div>-->
					<s:textbox name="im_iban_codControllo" label="im_iban_codControllo" bmodify="true" text="${requestScope.im_iban_codControllo}" cssclass="display_none" cssclasslabel="display_none"  />

					<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_iban_cinlbl" label="Codice CIN:" bmodify="false" text="${requestScope.im_iban_cin}"
										   cssclass="textareaman colordisabled" cssclasslabel="label85 bold textright" />		
					</s:div>
					<!--<s:div name="divElement10" cssclass="divRicMetadatiSingleRowRead">
						<s:label name="im_iban_cinlbl" cssclass="label110 bold" text="Codice CIN: ${requestScope.im_iban_cin}" />
					</s:div>-->
					<s:textbox name="im_iban_cin" label="im_iban_cin" bmodify="true" text="${requestScope.im_iban_cin}" cssclass="display_none" cssclasslabel="display_none"  />

					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_iban_abilbl" label="ABI:" bmodify="false" text="${requestScope.im_iban_abi}" 
										   cssclass="textareaman colordisabled" cssclasslabel="label85 bold textright"  />	
					</s:div>
					<!--<s:div name="divElement11" cssclass="divRicMetadatiSingleRowRead">
						<s:label name="im_iban_abilbl" cssclass="label110 bold" text="ABI: ${requestScope.im_iban_abi}" />
					</s:div>-->
					<s:textbox name="im_iban_abi" label="ABI" bmodify="true" text="${requestScope.im_iban_abi}" cssclass="display_none" cssclasslabel="display_none"  />

					<s:div name="divElement12" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_iban_cablbl" label="CAB:" bmodify="false" text="${requestScope.im_iban_cab}" 
										   cssclass="textareaman colordisabled" cssclasslabel="label85 bold textright" />	
					</s:div>
					<!--<s:div name="divElement12" cssclass="divRicMetadatiSingleRowRead">
						<s:label name="im_iban_cablbl" cssclass="label110 bold" text="CAB: ${requestScope.im_iban_cab}" />
					</s:div>-->
					<s:textbox name="im_iban_cab" label="CAB" bmodify="true" text="${requestScope.im_iban_cab}" cssclass="display_none" cssclasslabel="display_none"  />

					<s:div name="divElement13" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_iban_cclbl" label="C\C:" bmodify="false" text="${requestScope.im_iban_cc}" 
										   cssclass="textareaman colordisabled" cssclasslabel="label85 bold textright" />	
					</s:div>
					<!--<s:div name="divElement13" cssclass="divRicMetadatiSingleRowRead">
						<s:label name="im_iban_cclbl" cssclass="label110 bold" text="C\C: ${requestScope.im_iban_cc}" />
					</s:div>-->
					<s:textbox name="im_iban_cc" label="C\C" bmodify="true" text="${requestScope.im_iban_cc}" cssclass="display_none" cssclasslabel="display_none"  />
				
				</s:div>



					
					
					
					
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_indirizzolbl" label="Indirizzo:" bmodify="false" text="${requestScope.im_indirizzo}" 
										   cssclass="textareaman colordisabled" cssclasslabel="label85 bold textright"  />
					</s:div>
					<!--<s:div name="divElement6" cssclass="divRicMetadatiSingleRowRead">
						<s:label name="im_indirizzolbl" cssclass="label110 bold" text="Indirizzo: ${requestScope.im_indirizzo}" />
					</s:div>-->
					<s:textbox name="im_indirizzo" label="Indirizzo" bmodify="true" text="${requestScope.im_indirizzo}" cssclass="display_none" cssclasslabel="display_none"  />

					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
								<s:textbox name="im_emaillbl" label="Email:" bmodify="false" text="${requestScope.im_email}" 
										   cssclass="textareaman colordisabled" cssclasslabel="label85 bold textright" />
					</s:div>
					<!--<s:div name="divElement7" cssclass="divRicMetadatiSingleRowRead">
						<s:label name="im_emaillbl" cssclass="label110 bold" text="Email: ${requestScope.im_email}" />
					</s:div>-->
					<s:textbox name="im_email" label="Email" bmodify="true" text="${requestScope.im_email}" cssclass="display_none" cssclasslabel="display_none"  />
					
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist label="Tipologia File" name="im_flagTipologiaFile" disable="true" cssclasslabel="label85 bold textright"
										  cssclass="textareaman"
										  valueselected="${requestScope.im_flagTipologiaFile}">
							<s:ddloption value="S" text="Tracciato Csv standard"/>
							<s:ddloption value="T" text="Tracciato Azienda Trasporti"/>
						</s:dropdownlist>
					</s:div>
				</s:div>
			</c:if>


			</s:div>

			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
						<s:button id="tx_button_indietro" cssclass="btnStyle" type="submit" text="Indietro" onclick="" validate="false"/>	
						<c:if test="${!empty tastoIns}">
							<s:button id="tx_button_reset_ins" cssclass="btnStyle" type="submit" text="Reset" onclick="" validate="false"/>
							<s:button id="tx_button_aggiungi_end" cssclass="btnStyle" type="submit" text="Salva" onclick=""/>
						</c:if>
						<c:if test="${!empty tastoUpd}">
							<s:button id="tx_button_reset_mod" cssclass="btnStyle" type="submit" text="Reset" onclick="" validate="false"/>
							<s:button id="tx_button_edit_end" cssclass="btnStyle" type="submit" text="Salva" onclick=""/>
						</c:if>				
						<c:if test="${!empty tastoDel}">
								<s:button id="tx_button_delete_end" cssclass="btnStyle" type="submit" text="Cancella" onclick="" disable="${disableCancella}"/>
						</c:if>				
			</s:div>	
	</s:form>

<!-- 
	<s:div name="indietro" cssclass="divRicBottoni">
		<s:form name="schedaBeneficiarioForm2" action="ricercaBeneficiari.do${formParameters}&DDLType=13" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
				<s:button id="tx_button_cerca" cssclass="btnStyle" type="submit" text="Indietro" onclick=""/>
		</s:form>		
	</s:div>
 -->

				
	</s:div>
</s:div>




	
	
	
