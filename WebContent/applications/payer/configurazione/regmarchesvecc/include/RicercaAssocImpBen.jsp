<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="viewstate" encodeAttributes="true" />


<!-- 
TODO

Inserire due nuove combo gestite con i campi

codSocietaBen    (SOC_CSOCCSOC)
codUtenteBen	 (SOC_CSOCCSOC || UTE_CUTECUTE)

etichettare le modifiche con commenti

	mod090


Commenti Html per il momento
 -->

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>

<script type="text/javascript" >
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();

	$(function() 
	{
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#tx_data_validita_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_validita_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#dataValidita_day_id").val(dateText.substr(0,2));
												$("#dataValidita_month_id").val(dateText.substr(3,2));
												$("#dataValidita_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("dataValidita_day_id",
				                            "dataValidita_month_id",
				                            "dataValidita_year_id",
				                            "tx_data_validita_hidden");
			}
		});
	});

</script>

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
		<c:if test="${!empty codImpositore}">  
			<c:param name="rCodImpositore">${codImpositore}</c:param>
		</c:if>
<!-- mod090 -->
		<c:if test="${!empty codSocietaBen}">  
			<c:param name="rCodSocietaBen">${codSocietaBen}</c:param>
		</c:if>
		<c:if test="${!empty codProvinciaBen}">  
			<c:param name="rCodProvinciaBen">${codProvinciaBen}</c:param>
		</c:if>
		<c:if test="${!empty codUtenteBen}">  
			<c:param name="rCodUtenteBen">${codUtenteBen}</c:param>
		</c:if>
<!--  fine mod090 -->
		<c:if test="${!empty codBeneficiario}">  
			<c:param name="rCodBeneficiario">${codBeneficiario}</c:param>
		</c:if>
		<c:if test="${!empty codTipologiaServizio}">  
			<c:param name="rCodTipologiaServizio">${codTipologiaServizio}</c:param>
		</c:if>
		<c:if test="${!empty annoRifDa}">  
			<c:param name="rAnnoRifDa">${annoRifDa}</c:param>
		</c:if>
		<c:if test="${!empty annoRifA}">  
			<c:param name="rAnnoRifA">${annoRifA}</c:param>
		</c:if>
		<c:if test="${false}">  
			<c:param name="rDataValidita">${dataValidita}</c:param>
		</c:if>
		<c:if test="${!empty dataValidita_day}">  
			<c:param name="rDataValidita_day">${dataValidita_day}</c:param>
		</c:if>
		<c:if test="${!empty dataValidita_month}">  
			<c:param name="rDataValidita_month">${dataValidita_month}</c:param>
		</c:if>
		<c:if test="${!empty dataValidita_year}">  
			<c:param name="rDataValidita_year">${dataValidita_year}</c:param>
		</c:if>
		<c:if test="${!empty DDLChanged}">  
			<c:param name="rDDLChanged">${DDLChanged}</c:param>
		</c:if>
	</c:url>


 
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">

	<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
		Ricerca Associazioni Enti Impositori - Beneficiari
	</s:div>


	<s:form name="ricercaAssocImpBenForm" action="ricercaAssocImpBen.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

		<s:textbox name="DDLType" label="DDLType" bmodify="true" text="11" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="DDLChanged" label="DDLChanged" bmodify="true" text="" cssclass="display_none" cssclasslabel="display_none"  />
		
		<s:textbox name="rRowsPerPage" label="rowsPerPage" bmodify="true" text="${rowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rPageNumber" label="pageNumber" bmodify="true" text="${pageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rOrder" label="order" bmodify="true" text="${order}" cssclass="display_none" cssclasslabel="display_none"  />
		

				<s:div name="divRicMetadati" cssclass="divRicMetadati">

					<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="divRicercaLeftTop" cssclass="divRicMetadatiTopLeft">
								
							<s:dropdownlist name="codSocieta" disable="${sessionScope.societaDdlDisabled}" 
											label="Societ&aacute; Imp:" 
											cssclass="tbddl floatleft" 
											cssclasslabel="label85 bold floatleft textright"
											cachedrowset="listaSocieta" usexml="true" 
											onchange="setDdl('codSocieta'); callSubmit(this.form);"
											valueselected="${codSocieta}">
								<s:ddloption text="Tutte le Societ&agrave;" value=""/>
								<s:ddloption text="{2}" value="{1}"/>
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_societa_changed" type="submit" text="" onclick="" cssclass="btnimgStyle floatleft"  title="Aggiorna" validate="false "/>
							</noscript>
								
						</s:div>
						
						<s:div name="divRicercaCenterTop" cssclass="divRicMetadatiTopCenter">

							<s:dropdownlist name="codProvincia" disable="${requestScope.provinciaDdlDisabled}" 
											cssclass="tbddl floatleft" 
											label="Prov. Imp:" 
											cssclasslabel="label65 bold floatleft textright"
											cachedrowset="listaProvince" usexml="true" 
											onchange="setDdl('codProvincia'); callSubmit(this.form);"
											valueselected="${codProvincia}">
								<s:ddloption text="Tutte le Province" value=""/>
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							<noscript>
									<s:button id="tx_button_provincia_changed" type="submit" text="" onclick="" cssclass="btnimgStyle floatleft"  title="Aggiorna" validate="false" />

							</noscript>
								
						</s:div>

						<s:div name="divRicercaRightTop" cssclass="divRicMetadatiTopRight">
						

							<s:dropdownlist name="codUtente" disable="${requestScope.utenteDdlDisabled}" 
													cssclass="tbddlMax floatleft" 
													label="Ute. Imp:" 
													cssclasslabel="label65 bold textright floatleft"
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
											<s:dropdownlist name="codImpositore" disable="${sessionScope.utenteDdlDisabled}" 
																	cssclass="tbddl floatleft" 
																	label="Impositore:" 
																	cssclasslabel="label85 bold floatleft textright"
																	cachedrowset="listaUtentiEnti" usexml="true" 
																	onchange="setDdl('codImpositore'); callSubmit(this.form);"
																	valueselected="${codImpositore}">
												<s:ddloption text="Tutti gli Enti Impositori" value=""/>
												<s:ddloption text="{2}" value="{1}" />
											</s:dropdownlist>
								</s:div>
								<s:div name="divElement41" cssclass="divRicMetadatiSingleRow">

									<s:dropdownlist name="codSocietaBen" disable="${sessionScope.societaDdlDisabled}" 
												label="Societ&aacute; Ben:" 
												cssclass="tbddl floatleft" 
												cssclasslabel="label85 bold floatleft textright"
												cachedrowset="listaSocieta" usexml="true" 
												onchange="setDdl('codSocietaBen'); callSubmit(this.form);"
												valueselected="${codSocietaBen}">
									<s:ddloption text="Tutte le Societ&agrave;" value=""/>
									<s:ddloption text="{2}" value="{1}"/>
								</s:dropdownlist>
								<noscript>
									<s:button id="tx_button_societa_ben_changed" type="submit" text="" onclick="" cssclass="btnimgStyle floatleft"  title="Aggiorna" validate="false "/>
								</noscript>
							 </s:div>
								<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
											<s:dropdownlist name="codBeneficiario" disable="${sessionScope.beneficiarioDdlDisabled}" 
																	cssclass="tbddl floatleft" 
																	label="Beneficiario:" 
																	cssclasslabel="label85 bold floatleft textright"
																	cachedrowset="listaBeneficiariConv" usexml="true" 
																	valueselected="${codBeneficiario}">
												<s:ddloption text="Tutti gli Enti Beneficiari" value=""/>
												<s:ddloption text="{4}" value="{1}" />
											</s:dropdownlist>
								</s:div>					
						</s:div>

						<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
								<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
											<s:dropdownlist name="codTipologiaServizio" disable="false" 
																	cssclass="tbddlMax floatleft" 
																	label="Tip. Servizio:" 
																	cssclasslabel="label85 bold textright"
																	cachedrowset="listaTipologieServizio" usexml="true" 
																	valueselected="${codTipologiaServizio}">
												<s:ddloption text="Tutte le tipologie" value=""/>
												<s:ddloption text="{2}" value="{1}_{3}" />
											</s:dropdownlist>
								</s:div>
							<s:div name="divElement61" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="codProvinciaBen" disable="${requestScope.provinciaDdlDisabled}" 
												cssclass="tbddl floatleft" 
												label="Provincia Ben:" 
												cssclasslabel="label85 bold floatleft textright"
												cachedrowset="listaProvinceBen" usexml="true" 
												onchange="setDdl('codProvinciaBen'); callSubmit(this.form);"
												valueselected="${codProvinciaBen}">
									<s:ddloption text="Tutte le Province" value=""/>
									<s:ddloption text="{2}" value="{1}" />
								</s:dropdownlist>
								<noscript>
										<s:button id="tx_button_provincia_ben_changed" type="submit" text="" onclick="" cssclass="btnimgStyle floatleft"  title="Aggiorna" validate="false" />
	
								</noscript>
							</s:div>
													

								<s:div name="divElement18" cssclass="divRicMetadatiSingleRow">
									
									<s:label name="label_data" cssclass="seda-ui-label label85 bold textright floatleft" text="Anno Rif.:" />
									<s:textbox name="annoRifDa" label="Da:" bmodify="true" text="${requestScope.annoRifDa}" 
											   cssclass="textboxman_small floatleft" cssclasslabel="bold textright floatleft"  
									           maxlenght="4" validator="ignore;digits;minlength=4;maxlength=4" />
	
									<s:textbox name="annoRifA" label="A:" bmodify="true" text="${requestScope.annoRifA}" 
											   cssclass="textboxman_small floatleft" cssclasslabel="bold textright floatleft"  
									           maxlenght="4" validator="ignore;digits;minlength=4;maxlength=4" />
								</s:div>

						</s:div>
												
						<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
							<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
									<s:div name="divDataDa" cssclass="divDataDa">
										<s:date label="Data Validit&agrave;:" prefix="dataValidita" yearbegin="${ddlDateAnnoDa}"
											yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
											separator="/" calendar="${dataValidita}"
											cssclasslabel="label85 bold textright"
											cssclass="dateman">
										</s:date>
										<input type="hidden" id="tx_data_validita_hidden" value="" />
									</s:div>
							</s:div>
							<s:div name="divElement71" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="codUtenteBen" disable="${requestScope.utenteDdlDisabled}" 
														cssclass="tbddlMax floatleft" 
														label="Utente Ben:" 
														cssclasslabel="label85 bold textright floatleft"
														cachedrowset="listaUtentiBen" usexml="true" 
														onchange="setDdl('codUtenteBen'); callSubmit(this.form);"
														valueselected="${codUtenteBen}">
									<s:ddloption text="Tutti gli Utenti" value=""/>
									<s:ddloption text="{2}" value="{1}" />
								</s:dropdownlist>
								<noscript>
										<s:button id="tx_button_utente_ben_changed" type="submit" text="" onclick="" cssclass="btnimgStyle floatleft"  title="Aggiorna" validate="false" />
								</noscript>
							</s:div>
						</s:div>

			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" cssclass="btnStyle" type="submit" text="Cerca" onclick=""/>
				<s:button id="tx_button_reset" type="submit" text="Reset" onclick="" cssclass="btnStyle" />
				
			</s:div>

		</s:form>

<!-- 
				<s:div name="divRicercaBottoni2" cssclass="divRicBottoni">
					<s:form name="ricercaBeneficiariForm2" action="insUpdAssocImpBenStart.do${formParameters}&tastoIns=ok" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
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

	
<c:if test="${!empty listaAssociazioni}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Elenco Associazioni Enti Impositori - Beneficiari
	</s:div>
	
	<s:div name="divRisultati" cssclass="divRisultati">
		<s:datagrid  viewstate="" cachedrowset="listaAssociazioni" action="ricercaAssocImpBen.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
			<s:action>
				<c:url value="ricercaAssocImpBen.do">				
			<c:if test="${!empty codSocieta}">  
				<c:param name="codSocieta">${codSocieta}</c:param>
			</c:if>
			<c:if test="${!empty codProvincia}">  
				<c:param name="codProvincia">${codProvincia}</c:param>
			</c:if>
			<c:if test="${!empty codUtente}">  
				<c:param name="codUtente">${codUtente}</c:param>
			</c:if>
			<c:if test="${!empty codSocietaBen}">  
				<c:param name="codSocietaBen">${codSocietaBen}</c:param>
			</c:if>
			<c:if test="${!empty codProvinciaBen}">  
				<c:param name="codProvinciaBen">${codProvinciaBen}</c:param>
			</c:if>
			<c:if test="${!empty codUtenteBen}">  
				<c:param name="codUtenteBen">${codUtenteBen}</c:param>
			</c:if>
			
		<c:if test="${!empty codImpositore}">  
			<c:param name="codImpositore">${codImpositore}</c:param>
		</c:if>
		<c:if test="${!empty codBeneficiario}">  
			<c:param name="codBeneficiario">${codBeneficiario}</c:param>
		</c:if>
		<c:if test="${!empty codTipologiaServizio}">  
			<c:param name="codTipologiaServizio">${codTipologiaServizio}</c:param>
		</c:if>
		<c:if test="${!empty annoRifDa}">  
			<c:param name="annoRifDa">${annoRifDa}</c:param>
		</c:if>
		<c:if test="${!empty annoRifA}">  
			<c:param name="annoRifA">${annoRifA}</c:param>
		</c:if>
		<c:if test="${false}">  
			<c:param name="dataValidita">${dataValidita}</c:param>
		</c:if>
		<c:if test="${!empty dataValidita_day}">  
			<c:param name="dataValidita_day">${dataValidita_day}</c:param>
		</c:if>
		<c:if test="${!empty dataValidita_month}">  
			<c:param name="dataValidita_month">${dataValidita_month}</c:param>
		</c:if>
		<c:if test="${!empty dataValidita_year}">  
			<c:param name="dataValidita_year">${dataValidita_year}</c:param>
		</c:if>
		<c:param name="DDLType">11</c:param>


				</c:url>
			</s:action>
	
	<!-- colonne -->	
	<!--
													 CEB_CSOCCSOC, CEB_KANEKENT_IMP, CEB_CTSECTSE,
													 CEB_CUTECUTE, 
	                                                 ANE_CANECENT_IMP, ANE_TANETUFF_IMP, ANE_CANECUFF_IMP, 
	                                                 ANE_CANECENT_BEN, ANE_TANETUFF_BEN, ANE_CANECUFF_BEN,
												     TSE_DTSEDTSE, CEB_NCEBANNO_DA,
												     CEB_NCEBANNO_A, CEB_GCEBGDAT,
												     CEB_FCEBRIVE, CEB_FCEBREND,
												     CEB_FCEBTIPO   	

												    --> 
												     
												     
												    	 
	  	    <!-- CEB_CUTECUTE -->
			<s:dgcolumn index="4" label="Utente Imp"  asc="CUTEA" desc="CUTED"></s:dgcolumn>
	
	  	    <!-- ANE_CANECENT_IMP -->
			<s:dgcolumn index="5" label="Ente Imp" asc="CENTIA" desc="CENTID"></s:dgcolumn>
	
			<!-- ANE_TANETUFF_IMP/ANE_CANECUFF_IMP -->
			<s:dgcolumn label="Ufficio Imp" asc="TUFFIA" desc="TUFFID">
						<s:if left="{6}{7}" control="eq" right="">
								<s:then>
									&nbsp;
								</s:then>
								<s:else>
									{6}/{7}
								</s:else>
						</s:if>		    
			</s:dgcolumn>

	  	    <!-- CEB_CUTECUTE -->
			<s:dgcolumn index="23" label="Utente Ben"  asc="CUTEA" desc="CUTED"></s:dgcolumn>
	  	    
	  	    <!-- ANE_CANECENT_BEN -->
			<s:dgcolumn index="8" label="Ente Ben" asc="CENTBA" desc="CENTBD"></s:dgcolumn>
	
			<!-- ANE_TANETUFF_BEN/ANE_CANECUFF_BEN -->
			<s:dgcolumn label="Ufficio Ben" asc="TUFFBA" desc="TUFFBD">
						<s:if left="{9}{10}" control="eq" right="">
								<s:then>
									&nbsp;
								</s:then>
								<s:else>
									{9}/{10}
								</s:else>
						</s:if>		    
			</s:dgcolumn>
	
	        <!--  TSE_DTSEDTSE -->
			<s:dgcolumn index="11" label="Tipologia Servizio" asc="DTSEA" desc="DTSED"></s:dgcolumn>
	
			<!-- CEB_NCEBANNO_DA, CEB_NCEBANNO_A -->
			<s:dgcolumn label="Periodo Riferimento" asc="ANNOA" desc="ANNOD">da {12} a {13}</s:dgcolumn>
	
			<!-- CEB_GCEBGDAT -->
			<s:dgcolumn index="14" format="dd/MM/yyyy" label="Data Validit&agrave;" asc="GDATA" desc="GDATD"></s:dgcolumn>
			
			<!-- CEB_FCEBRIVE -->
			<s:dgcolumn index="19" label="Strumento Riversam." asc="RIVEA" desc="RIVED">
<!--  
				<s:if right="{15}" control="eq" left="B">
					<s:then>
					Bonifico
					</s:then>
					<s:else>Altro</s:else>
				</s:if>
-->	
 			</s:dgcolumn>
			<!-- CEB_FCEBREND -->
			<s:dgcolumn index="20" label="Notifica Riversam." asc="RENDA" desc="RENDD">
<!-- 
				<s:if right="{16}" control="eq" left="Y">
					<s:then>
					E-mail
					</s:then>
					<s:else>Altro</s:else>
				</s:if>
-->
			</s:dgcolumn>
	
			<!-- CEB_FCEBTIPO -->
			<s:dgcolumn index="21" label="Tipo Rendicont." asc="TIPOA" desc="TIPOD">
<!-- 
				<s:if right="{17}" control="eq" left="R">
					<s:then>
					Riversamento
					</s:then>
				</s:if>
				<s:if right="{17}" control="eq" left="C">
					<s:then>
					Rendicontazione
					</s:then>
				</s:if>
-->
			</s:dgcolumn>
		
			<s:dgcolumn label="&nbsp;&nbsp;Azioni&nbsp;&nbsp;">
			<!-- chiave 
			chiave 1 				codSocieta={1}&codUtente={4}&codImpositore={2}&codTipologiaServizio={3}&annoRifDa={12}&annoRifA={13}&dataValidita={14}" 
			chiave 2 				codSocieta={1}&codUtente={4}&codImpositore={2}&codBeneficiario={18}" 
			
			-->
			
				
			</s:dgcolumn>
	
		</s:datagrid>
	</s:div>	
</c:if>