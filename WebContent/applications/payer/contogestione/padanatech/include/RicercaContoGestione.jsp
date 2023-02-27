<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="ricercaContoGestione" encodeAttributes="true" />

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
	            updateValoreDatePickerFromDdl("#tx_data_validitaS_day_id",
	            							"#tx_data_validitaS_month_id",
	            							"#tx_data_validitaS_year_id",
	            							"#tx_data_validitaS_hidden");
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
	<s:form name="search_assoc_imp_form" action="ricercaContoGestioneD.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Conto Gestione
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">


						<s:div name="divRicercaLeftTop" cssclass="divRicMetadatiLeft">
								
							<s:dropdownlist name="codSocieta" disable="${societaDdlDisabled}" 
											label="Societ&aacute;:" 
											cssclass="tbddl floatleft" 
											cssclasslabel="label65 bold floatleft textright"
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
											cssclasslabel="label65 bold floatleft textright"
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
							<s:dropdownlist name="tx_UtenteEnte" disable="${utenteDdlDisabled}" 
													cssclass="tbddlMax floatleft" 
													label="Ente:" 
													cssclasslabel="label65 bold textright floatleft"
													cachedrowset="listaUtentiEntiAll" usexml="true"
													onchange="setFiredButton('tx_button_ente_changed');this.form.submit();"
													valueselected="${tx_UtenteEnte}">
								<s:ddloption text="Tutti gli Enti" value=""/>
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_ente_changed" 
									disable="${utenteDdlDisabled}" onclick="" text="" 
									type="submit" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
							</noscript>
						</s:div>
				</s:div>
						<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
						
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
									<s:dropdownlist name="tipoModello" disable="false" 
																cssclass="tbddl floatleft" 
																label="Modello:" 
																cssclasslabel="label65 bold floatleft textright"
															onchange="setFiredButton('tx_button_modello_changed');this.form.submit();" 
																valueselected="${tipoModello}">
												<s:ddloption text="Tutte le tipologie" value="" />
												<s:ddloption text="Annuale" value="A" />
												<s:ddloption text="Mensile" value="M" />
									</s:dropdownlist>
									<noscript>
									<s:button id="tx_button_modello_changed" 
										disable="false" onclick="" text="" 
										type="submit" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
									</noscript>
						</s:div>					
					</s:div>




						<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
						
								<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
											<s:dropdownlist name="periodo" disable="false" 
																	cssclass="tbddl floatleft" 
																	label="Periodo:" 
																	cssclasslabel="label65 bold floatleft textright"
																	valueselected="${periodo}">
												<s:ddloption text="Tutti i periodi" value=""/>
												<s:ddloptionbinder options="${listaPeriodi}"/>
											</s:dropdownlist>
								</s:div>					

						</s:div>

				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">

					<s:div name="divElement17" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement17a" cssclass="labelData">
							<s:label name="label_importo"
								cssclass="seda-ui-label label85 bold textright" text="Importo:" />
						</s:div>

						<s:div name="divElement17b" cssclass="floatleft">
							<s:div name="divImportoDa" cssclass="divDataDa">
								<s:textbox validator="ignore;numberIT;maxlength=15"
									bmodify="true" name="tx_importo_da" label="Da:"
									text="${tx_importo_da}" maxlenght="15"
									cssclass="textareamanImporto" cssclasslabel="labelsmall" />
							</s:div>

							<s:div name="divImportoA" cssclass="divDataA">
								<s:textbox validator="ignore;numberIT;maxlength=15"
									bmodify="true" name="tx_importo_a" label="A:"
									text="${tx_importo_a}" maxlenght="15"
									cssclass="textareamanImporto" cssclasslabel="labelsmall" />
							</s:div>
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
			</s:div>
	</s:form>

	</s:div>
	
</s:div>

<c:if test="${!empty listaContiGestione}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Elaborazioni
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="listaContiGestione"  
			action="ricercaContoGestioneD.do?vista=ricercaContoGestione&tx_button_cerca=C" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
	  	    
	  	    
	  	    <!-- UTE_DUTEDUTE -->
			<s:dgcolumn index="5" label="Utente"  asc="DUTEA" desc="DUTED"></s:dgcolumn>
			
			<!-- ANE_DANEDENT/ANE_DANEDUFF -->
			<s:dgcolumn label="Ente" asc="DUFFA" desc="DUFFD">
						<s:if left="{6}{7}" control="eq" right="">
								<s:then>
									&nbsp;
								</s:then>
								<s:else>
									{6}/{7}
								</s:else>
						</s:if>		    
			</s:dgcolumn>
			
			<!-- MCG_FMCGFTIP-->
			<s:dgcolumn label="Tipo Modello" asc="FTMOA" desc="FTMOD">{8}</s:dgcolumn>
			
			<!-- MCG_CMCGCPER -->
			<s:dgcolumn label="Periodo" asc="CPERA" desc="CPERD">{9}</s:dgcolumn>

			<!-- MCG_IMCGITOT -->
			<s:dgcolumn index="10" format="#,##0.00" label="Importo" asc="NTOTA" desc="NTOTD" css="text_align_right"></s:dgcolumn>
	
			<!-- MCG_DMCGDFIL -->
			<s:dgcolumn index="11" label="File prospetto" asc="DFILA" desc="DFILD"></s:dgcolumn>

		<!-- AZIONI -->
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">

		<!-- DOWNLOAD FILE PDF -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="ricercaContoGestioneD.do?tx_button_download=1&filename={11}&vista=ricercaContoGestione"
					imagesrc="../applications/templates/ottico/img/download.png"
					alt="Download Report" text="" />
		<!-- INVIA MAIL -->
				<s:if left="{8}" control="eq" right="MENSILE">
					<s:then>
						&nbsp;
					</s:then>
					<s:else>
						<c:if test="${requestScope.abilitazione == 'Y'}">
							<s:hyperlink
								cssclass="hlStyle" 
								href="ricercaContoGestioneD.do?tx_button_step1=1&chiaveCG={1}&vista=ricercaContoGestione"
								imagesrc="../applications/templates/rendicontazione/img/Y_sendmail.png"
								alt="Invio Report" text=""   />
						</c:if>
					</s:else>
				</s:if>		    
		
			</s:dgcolumn>
			
		</s:datagrid>
	</s:div>

</c:if>
