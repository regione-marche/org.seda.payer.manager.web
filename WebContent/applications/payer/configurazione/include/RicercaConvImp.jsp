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
		<c:if test="${!empty codCanale}">  
			<c:param name="rCodCanale">${codCanale}</c:param>
		</c:if>
		<c:if test="${!empty codGateway}">  
			<c:param name="rCodGateway">${codGateway}</c:param>
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
		Ricerca Convenzioni Enti Impositori
	</s:div>


	<s:form name="ricercaConvImpForm" action="ricercaConvenzioniImp.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

		<s:textbox name="DDLType" label="DDLType" bmodify="true" text="12" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="DDLChanged" label="DDLChanged" bmodify="true" text="" cssclass="display_none" cssclasslabel="display_none"  />
		
		<s:textbox name="rRowsPerPage" label="rowsPerPage" bmodify="true" text="${rowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rPageNumber" label="pageNumber" bmodify="true" text="${pageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rOrder" label="order" bmodify="true" text="${order}" cssclass="display_none" cssclasslabel="display_none"  />
		

				<s:div name="divRicMetadati" cssclass="divRicMetadati">

					<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="divRicercaLeftTop" cssclass="divRicMetadatiTopLeft">

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
											<s:button id="tx_button_societa_changed" type="submit" text="" onclick="" cssclass="btnimgStyle floatleft"  title="Aggiorna" validate="false" />
									</noscript>

						</s:div>
						
						<s:div name="divRicercaCenterTop" cssclass="divRicMetadatiTopCenter">

									<s:dropdownlist name="codProvincia" disable="${requestScope.provinciaDdlDisabled}" 
													cssclass="tbddl floatleft" 
													label="Provincia:" 
													cssclasslabel="label65 bold floatleft textright"
													cachedrowset="listaProvince" usexml="true" 
													onchange="setDdl('codProvincia'); callSubmit(this.form);"
													valueselected="${codProvincia}">
										<s:ddloption text="Tutte le Province" value=""/>
										<s:ddloption text="{2}" value="{1}" />
									</s:dropdownlist>
									<noscript>
											<s:button id="tx_button_provincia_changed" type="submit" text="" onclick="" cssclass="btnimgStyle floatleft"  title="Aggiorna" validate="false"/>
									</noscript>

						</s:div>

						<s:div name="divRicercaRightTop" cssclass="divRicMetadatiTopRight">
						
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
											<s:button id="tx_button_utente_changed" type="submit" text="" onclick="" cssclass="btnimgStyle floatleft"  title="Aggiorna" validate="false"/>
									</noscript>
						</s:div>
						
					</s:div>

						<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeftLarge">
						
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
										<noscript>
											<s:button id="tx_button_impositore_changed" type="submit" text="" onclick="" cssclass="btnimgStyle floatleft"  title="Aggiorna" validate="false"/>
										</noscript>
								
								</s:div>
								
								<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
										<s:dropdownlist name="codGateway" disable="false" 
																cssclass="tbddl floatleft" 
																label="Tipo Carta:" 
																cssclasslabel="label85 bold textright"
																cachedrowset="listaGatewayCan" usexml="true" 
																valueselected="${codGateway}">
											<s:ddloption text="Tutti i tipi di carta" value=""/>
											<s:ddloption text="{2}" value="{1}" />
										</s:dropdownlist>
								</s:div>						
	

						</s:div>

						<s:div name="divRicercaCenter" cssclass="divRicMetadatiTopCenter">

								<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
											<s:dropdownlist name="codCanale" disable="${sessionScope.canaleDdlDisabled}" 
																	cssclass="tbddl floatleft" 
																	label="Canale:" 
																	cssclasslabel="label65 bold floatleft textright"
																	cachedrowset="listaCanaliPagamento" usexml="true" 
																	onchange="setDdl('codCanale'); callSubmit(this.form);"
																	valueselected="${codCanale}">
												<s:ddloption text="{2}" value="{1}" />
											</s:dropdownlist>
									<noscript>
											<s:button id="tx_button_canale_changed" type="submit" text="" onclick="" cssclass="btnimgStyle floatleft"  title="Aggiorna" validate="false" />
									</noscript>
								</s:div>					

						</s:div>
						
						<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
							<s:div name="divElement70" cssclass="divRicMetadatiSingleRow">
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
					<s:form name="ricercaBeneficiariForm2" action="insUpdConvenzioneImpStart.do${formParameters}&tastoIns=ok" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
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

	
<c:if test="${!empty listaConvenzioni}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Elenco Convenzioni Enti Impositori
	</s:div>
	
	<s:div name="divRisultati" cssclass="divRisultati">
		<s:datagrid  viewstate="" cachedrowset="listaConvenzioni" action="ricercaConvenzioniImp.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
			<s:action>
				<c:url value="ricercaConvenzioniImp.do">				
			<c:if test="${!empty codSocieta}">  
				<c:param name="codSocieta">${codSocieta}</c:param>
			</c:if>
			<c:if test="${!empty codProvincia}">  
				<c:param name="codProvincia">${codProvincia}</c:param>
			</c:if>
			<c:if test="${!empty codUtente}">  
				<c:param name="codUtente">${codUtente}</c:param>
			</c:if>
			<c:if test="${!empty codImpositore}">  
				<c:param name="codImpositore">${codImpositore}</c:param>
			</c:if>
			<c:if test="${!empty codCanale}">  
				<c:param name="codCanale">${codCanale}</c:param>
			</c:if>
			<c:if test="${!empty codGateway}">  
				<c:param name="codGateway">${codGateway}</c:param>
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
				<c:param name="DDLType">12</c:param>
			</c:url>
			</s:action>
	
	<!-- colonne -->	
	<!--
	
  CEN_CSOCCSOC, CEN_CUTECUTE, CEN_KANEKENT_IMP, CEN_KGTWKGTW, CEN_GCENGDAT, 
  ANE_CANECENT, ANE_TANETUFF, ANE_CANECUFF, 
  CAN_DCANDCAN, GTW_DGTWDGTW, 
  CEN_CCENPAGA, CEN_NCENGLAT,
  CEN_FCENFEST, CEN_NCENNGIO, CEN_NCENPERC,
  CEN_NCENQUOF, CEN_FCENQUOF
							--> 
												     
												     
												    	 
	  	    <!-- CEN_CUTECUTE -->
			<s:dgcolumn index="2" label="Utente"  asc="CUTEA" desc="CUTED"></s:dgcolumn>
	
	  	    <!-- ANE_CANECENT -->
			<s:dgcolumn index="6" label="Ente Imp" asc="CENTIA" desc="CENTID"></s:dgcolumn>
	
			<!-- ANE_TANETUFF/ANE_CANECUFF -->
			<s:dgcolumn label="Ufficio Imp" asc="TUFFIA" desc="TUFFID">
						<s:if left="{7}{8}" control="eq" right="">
					<s:then>
						&nbsp;
					</s:then>
					<s:else>
						{7}/{8}
					</s:else>
			</s:if>		    
			</s:dgcolumn>

	  	    <!-- CAN_DCANDCAN -->
			<s:dgcolumn index="9" label="Canale" asc="DCANA" desc="DCAND"></s:dgcolumn>
	
			<!-- GTW_DGTWDGTW -->
			<s:dgcolumn index="10" label="Tipo Carta" asc="DGTWA" desc="DGTWD"></s:dgcolumn>
	
			<!-- CEN_GCENGDAT -->
			<s:dgcolumn format="dd/MM/yyyy" index="5" label="Data Validit&agrave;" asc="GDATA" desc="GDATD"></s:dgcolumn>
	
			<!-- CEN_CCENPAGA -->
			<s:dgcolumn index="11" label="Periodo Aggreg." asc="PAGAA" desc="PAGAD" css="text_align_right"></s:dgcolumn>

			<!-- CEN_NCENGLAT -->
			<s:dgcolumn index="12" label="Giorni Latenza" asc="GLATA" desc="GLATD" css="text_align_right"></s:dgcolumn>
	
			<!-- CEN_FCENFEST -->
			<s:dgcolumn index="18" label="Festivit&agrave;" asc="FESTA" desc="FESTD">
			</s:dgcolumn>

			<!-- CEN_NCENNGIO -->
			<s:dgcolumn index="14" label="Limite Max" asc="NGIOA" desc="NGIOD" css="text_align_right"></s:dgcolumn>

			<!-- CEN_NCENPERC -->
			<s:dgcolumn index="15" format="#0.00" label="% / Risc" asc="PERCA" desc="PERCD" css="text_align_right"></s:dgcolumn>

			<!-- CEN_NCENQUOF -->
			<s:dgcolumn index="16" format="#,##0.00" label="Quota Fissa" asc="NQUOFA" desc="NQUOFD" css="text_align_right"></s:dgcolumn>

			<!-- CEN_FCENQUOF -->
			<s:dgcolumn index="19" label="Tipo Quota" asc="FQUOFA" desc="FQUOFD">
			</s:dgcolumn>



			<s:dgcolumn label="&nbsp;&nbsp;Azioni&nbsp;&nbsp;">
			<!-- chiave 
			chiave 1 				codSocieta={1}&codUtente={2}&codImpositore={3}&codGateway={4}&dataValidita={5}" 
			
			-->
			
				<s:hyperlink href="insUpdConvenzioneImpStart.do${formParameters}&tx_button_edit=ok&tastoUpd=ok&codSocieta={1}&codUtente={2}&codImpositore={3}&codGateway={4}&dataValidita={5}" 
					imagesrc="../applications/templates/configurazione/img/edit.png"
					text="" cssclass="hlStyle" />
				<s:hyperlink href="cancConvenzioneImpStart.do${formParameters}&tx_button_delete=ok&tastoDel=ok&codSocieta={1}&codUtente={2}&codImpositore={3}&codGateway={4}&dataValidita={5}" 
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					text="" cssclass="hlStyle" />
			</s:dgcolumn>
  	
		</s:datagrid>
	</s:div>	
</c:if>
