<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="ricercaAssocBen" encodeAttributes="true" />

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
<script type="text/javascript">
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();
	
	$(function() {
		$.datepicker.setDefaults($.datepicker.regional["it"]);
		$("#tx_data_validitaS_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_validitaS_hidden").datepicker( {
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn : "button",
			buttonImage : "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly : true,
			onSelect : function(dateText, inst) {
				$("#tx_data_validitaS_day_id").val(dateText.substr(0, 2));
				$("#tx_data_validitaS_month_id").val(dateText.substr(3, 2));
				$("#tx_data_validitaS_year_id").val(dateText.substr(6, 4));
			},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_validitaS_day_id",
	            							"tx_data_validitaS_month_id",
	            							"tx_data_validitaS_year_id",
	            							"tx_data_validitaS_hidden");
			}
		});
	});

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
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="search_assoc_imp_form" action="ricercaAssocBenD.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Riversamento Beneficiari Portali
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">


						<s:div name="divRicercaLeftTop" cssclass="divRicMetadatiLeft">
								
							<s:dropdownlist name="codSocieta" disable="${societaDdlDisabled}" 
											label="Societ&aacute;:" 
											cssclass="tbddl floatleft" 
											cssclasslabel="label85 bold floatleft textright"
											cachedrowset="listaSocieta" usexml="true" 
											onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
											valueselected="${codSocieta}">
								<s:ddloption text="Tutte le Societ&agrave;" value=""/>
								<s:ddloption text="{2}" value="{1}"/>
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_societa_changed" 
									disable="${societaDdlDisabled}" onclick="" text="" 
									type="submit" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
							</noscript>
								
						</s:div>
						
						<s:div name="divRicercaCenterTop" cssclass="divRicMetadatiCenter">

							<s:dropdownlist name="codProvincia" disable="${provinciaDdlDisabled}" 
											cssclass="tbddl floatleft" 
											label="Provincia:" 
											cssclasslabel="label85 bold floatleft textright"
											cachedrowset="listaProvince" usexml="true" 
											onchange="setFiredButton('tx_button_provincia_changed');this.form.submit();"
											valueselected="${codProvincia}">
								<s:ddloption text="Tutte le Province" value=""/>
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_provincia_changed" 
									disable="${provinciaDdlDisabled}" onclick="" text="" 
									type="submit" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
							</noscript>
						</s:div>

						<s:div name="divRicercaRightTop" cssclass="divRicMetadatiRight">
						

							<s:dropdownlist name="codUtente" disable="${utenteDdlDisabled}" 
													cssclass="tbddlMax floatleft" 
													label="Utente:" 
													cssclasslabel="label85 bold textright floatleft"
													cachedrowset="listaUtenti" usexml="true" 
													onchange="setFiredButton('tx_button_utente_changed');this.form.submit();"
													valueselected="${codUtente}">
								<s:ddloption text="Tutti gli Utenti" value=""/>
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_provincia_changed" 
									disable="${utenteDdlDisabled}" onclick="" text="" 
									type="submit" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
							</noscript>

						</s:div>
				</s:div>


						<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
						
								<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
											<s:dropdownlist name="codBeneficiario" disable="${sessionScope.beneficiarioDdlDisabled}" 
																	cssclass="tbddl floatleft" 
																	label="Beneficiario:" 
																	cssclasslabel="label85 bold floatleft textright"
																	cachedrowset="listaBeneficiariBoll" usexml="true" 
																	valueselected="${codBeneficiario}">
												<s:ddloption text="Tutti gli Enti Beneficiari" value=""/>
												<s:ddloption text="{4}" value="{1}" />
											</s:dropdownlist>
								</s:div>					

						</s:div>

						<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
								<s:div name="divElement18" cssclass="divRicMetadatiSingleRow">
									
									<s:label name="label_data" cssclass="seda-ui-label label85 bold textright floatleft" text="Anno Rif.:" />
									<s:textbox name="annoRifDaS" label="Da:" bmodify="true" text="${annoRifDaS}" 
											   cssclass="textboxman_small floatleft" cssclasslabel="bold textright floatleft"  
									           maxlenght="4" validator="ignore;digits;minlength=4;maxlength=4" />
	
									<s:textbox name="annoRifAS" label="A:" bmodify="true" text="${annoRifAS}" 
											   cssclass="textboxman_small floatleft" cssclasslabel="bold textright floatleft"  
									           maxlenght="4" validator="ignore;digits;minlength=4;maxlength=4" />
								</s:div>

						</s:div>
												
						<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
							<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
									<s:div name="divDataDa" cssclass="divDataDa">
										<s:date label="Data Validit&agrave;:" prefix="tx_data_validitaS" yearbegin="${ddlDateAnnoDa}"
											yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
											separator="/" calendar="${tx_data_validitaS}"
											cssclasslabel="label85 bold textright"
											cssclass="dateman">
										</s:date>
										<input type="hidden" id="tx_data_validitaS_hidden" value="" />
									</s:div>
							</s:div>
						</s:div>

			</s:div>
			<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none" cssclasslabel="rend_display_none" />
			
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_aggiungi" validate="false"  onclick="" text="Nuovo" type="submit" cssclass="btnStyle" />
			</s:div>
	</s:form>

	</s:div>
	
</s:div>

<c:if test="${!empty listaAssociazioni}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Enti Beneficiari Riversamento
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="listaAssociazioni"  
			action="ricercaAssocBenD.do?vista=ricercaAssocBen&tx_button_default=C" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

	  	    <!-- CEB_CUTECUTE -->
			<s:dgcolumn index="3" label="Utente"  asc="CUTEA" desc="CUTED"></s:dgcolumn>
	

	  	    <!-- ANE_CANECENT_BEN -->
			<s:dgcolumn index="4" label="Ente Ben" asc="CENTBA" desc="CENTBD"></s:dgcolumn>
			
			<!-- ANE_TANETUFF_BEN/ANE_CANECUFF_BEN -->
			<s:dgcolumn label="Ufficio Ben" asc="TUFFBA" desc="TUFFBD">
						<s:if left="{5}{6}" control="eq" right="">
								<s:then>
									&nbsp;
								</s:then>
								<s:else>
									{5}/{6}
								</s:else>
						</s:if>		    
			</s:dgcolumn>
			<!-- CBP_NCBPANNO_DA, CBP_NCBPANNO_A -->
			<s:dgcolumn label="Periodo Riferimento" asc="ANNOA" desc="ANNOD">da {7} a {8}</s:dgcolumn>
			

			<!-- CBP_GCBPGDAT -->
			<s:dgcolumn index="9" format="dd/MM/yyyy" label="Inizio Validit&agrave;" asc="GDATA" desc="GDATD"></s:dgcolumn>
	
			<!-- CBP_FCBPRIVE -->
			<s:dgcolumn index="13" label="Strumento Riversam." asc="RIVEA" desc="RIVED"></s:dgcolumn>

			<!-- CBP_FCBPREND -->
			<s:dgcolumn index="14" label="Notifica Riversam." asc="RENDA" desc="RENDD"></s:dgcolumn>

	       	<!-- CBP_FCBPTIPO -->
			<s:dgcolumn index="15" label="Tipo Rendicontazione" asc="TIPOA" desc="TIPOD"></s:dgcolumn>
	
		<!-- AZIONI -->
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">

		<!-- MODIFICA RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="AssocBenActionInsUpd.do?tx_button_edit=B&chiaveSocieta={1}&chiaveUtente={3}&chiaveBenef={2}&chiavedataVal={9}&vista=ricercaAssocBen&start=A&codop=edit"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />
		<!-- CANCELLAZIONE RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="AssocBenActionCancel.do?tx_button_delete=B&chiaveSocieta={1}&chiaveUtente={3}&chiaveBenef={2}&chiavedataVal={9}&vista=ricercaAssocBen&start=A"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Elimina" text=""   />
			</s:dgcolumn>
			
		</s:datagrid>
	</s:div>

</c:if>
