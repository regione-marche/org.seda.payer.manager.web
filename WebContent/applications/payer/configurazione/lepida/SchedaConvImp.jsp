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


<!-- mappa 1.2 -->	
<script type="text/javascript">
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
		<c:if test="${!empty rCodImpositore}">  
			<c:param name="codImpositore">${rCodImpositore}</c:param>
		</c:if>
		<c:if test="${!empty rCodCanale}">  
			<c:param name="codCanale">${rCodCanale}</c:param>
		</c:if>
		<c:if test="${!empty rCodGateway}">  
			<c:param name="codGateway">${rCodGateway}</c:param>
		</c:if>
		<c:if test="${!empty rDataValidita_day}">  
			<c:param name="dataValidita_day">${rDataValidita_day}</c:param>
		</c:if>
		<c:if test="${!empty rDataValidita_month}">  
			<c:param name="dataValidita_month">${rDataValidita_month}</c:param>
		</c:if>
		<c:if test="${!empty rDataValidita_year}">  
			<c:param name="dataValidita_year">${rDataValidita_year}</c:param>
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
	
	<s:div name="divMessage">
		<c:if test="${!empty riv_message}">
			<hr/><s:label name="message" text="${riv_message}"/><hr/>
		</c:if>
		<c:if test="${!empty riv_error_message}">
			<hr/><s:label name="error_message" text="${riv_error_message}"/><hr/>
		</c:if>
	</s:div>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">

			<s:div name="divRicercaTitleName" cssclass="divRicTitle">
				Convenzione Ente Impositore
			</s:div>

	<s:form name="schedaConvImpForm" action="insUpdCancConvenzioneImp.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:textbox name="DDLType" label="DDLType" bmodify="true" text="121" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="DDLChanged" label="DDLChanged" bmodify="true" text="" cssclass="display_none" cssclasslabel="display_none"  />
<!--  stato pagina -->
<!--  TODO -->
		<s:textbox name="rCodSocieta" label="codSocietaRic" bmodify="true" text="${rCodSocieta}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rCodProvincia" label="codProvinciaRic" bmodify="true" text="${rCodProvincia}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rCodUtente" label="codUtenteRic" bmodify="true" text="${rCodUtente}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rCodImpositore" label="codImpositoreRic" bmodify="true" text="${rCodImpositore}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rCodCanale" label="codCanaleRic" bmodify="true" text="${rCodCanale}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rCodGateway" label="codGataway" bmodify="true" text="${rCodGateway}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rDataValidita_day" label="dataValiditaDayRic" bmodify="true" text="${rDataValidita_day}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rDataValidita_month" label="dataValiditaMonthRic" bmodify="true" text="${rDataValidita_month}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rDataValidita_year" label="dataValiditaYearRic" bmodify="true" text="${rDataValidita_year}" cssclass="display_none" cssclasslabel="display_none"  />

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
<!--  								<s:button id="tx_button_societa_changed"  type="submit" text="Aggiorna" onclick=""/> -->
									<s:button id="tx_button_societa_changed" type="submit" text="" onclick="" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
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
<!--  								<s:button id="tx_button_utente_changed"  type="submit" text="Aggiorna" onclick=""/> -->
									<s:button id="tx_button_utente_changed" type="submit" text="" onclick="" cssclass="btnimgStyle"  title="Aggiorna" validate="false"/>
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
							<s:dropdownlist name="codImpositore" disable="${requestScope.impositoreDdlDisabled}" 
													validator="required" showrequired="true"
													cssclass="tbddlMax floatleft" 
													label="Impositore:" 
													cssclasslabel="label65 bold textright"
													cachedrowset="listaUtentiEnti" usexml="true" 
													onchange="setDdl('codImpositore'); callSubmit(this.form);"
													valueselected="${requestScope.codImpositore}">
								<s:ddloption text="" value=""/>
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							<noscript>
<!--  								<s:button id="tx_button_impositore_changed"  type="submit" text="Aggiorna" onclick=""/> -->
									<s:button id="tx_button_impositore_changed" type="submit" text="" onclick="" cssclass="btnimgStyle"  title="Aggiorna" validate="false"/>
							</noscript>
						</c:if>
						<c:if test="${!empty tastoUpd or !empty tastoDel}">
								<s:dropdownlist name="codImpositore2" disable="true" 
														validator="required" showrequired="true"
														cssclass="tbddlMax floatleft" 
														label="Impositore:" 
														cssclasslabel="label65 bold textright"
														cachedrowset="listaUtentiEnti" usexml="true" 
														valueselected="${requestScope.codImpositore}">
									<s:ddloption text="{2}" value="{1}" />
								</s:dropdownlist>
								<s:textbox name="codImpositore" label="codImpositore" bmodify="true" text="${requestScope.codImpositore}" cssclass="display_none" cssclasslabel="display_none"  />
						</c:if>
			        </s:div>
				
				</s:div>
				
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
					<c:if test="${!empty tastoIns}">
						<s:dropdownlist name="codGateway" disable="${requestScope.gatewayDdlDisabled}" 
												validator="required" showrequired="true"
												cssclass="tbddl floatleft" 
												label="Tipo Carta:" 
												cssclasslabel="label85 bold floatleft textright"
												cachedrowset="listaGatewayCan" usexml="true" 
												valueselected="${requestScope.codGateway}">
							<s:ddloption text="" value=""/>
							<s:ddloption text="{2}" value="{1}" />
							<s:ddloption text="Tutti i tipi" value="0000000000" />
						</s:dropdownlist>
					</c:if>
					
					<c:if test="${!empty tastoUpd or !empty tastoDel}">
						<s:dropdownlist name="codGateway2" disable="true" 
												validator="required" showrequired="true"
												cssclass="tbddl floatleft" 
												label="Tipo Carta:" 
												cssclasslabel="label85 bold floatleft textright"
												cachedrowset="listaGatewayCan" usexml="true" 
												valueselected="${requestScope.codGateway}">
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
							<s:textbox name="codGateway" label="codGateway" bmodify="true" text="${requestScope.codGateway}" cssclass="display_none" cssclasslabel="display_none"  />
					</c:if>
			        </s:div>

					<c:if test="${!empty tastoIns or !empty tastoUpd}">
						<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="aggregPagamenti" disable="false" 
													validator="required" showrequired="true"
													cssclass="tbddl floatleft" 
													label="Aggregaz.pag.:" 
													cssclasslabel="label85 bold floatleft textright"
													valueselected="${requestScope.aggregPagamenti}">
								<s:ddloption text="1" value="1"/>
								<s:ddloption text="10" value="10"/>
								<s:ddloption text="15" value="15"/>
								<s:ddloption text="30" value="30"/>
							</s:dropdownlist>						
						</s:div>
					</c:if>

					<c:if test="${!empty tastoDel}">
						<s:div name="divElement71" cssclass="divRicMetadatiSingleRow">
							<!--<s:label name="aggregPagamentilbl" cssclass="label110 bold" text="Aggregaz.pag.: ${requestScope.aggregPagamenti}" />-->
							
							<s:dropdownlist name="aggregPagamentilbl" disable="true" 
													validator="required" showrequired="true"
													cssclass="tbddl floatleft" 
													label="Aggregaz.pag.:" 
													cssclasslabel="label85 bold floatleft textright"
													valueselected="${requestScope.aggregPagamenti}">
								<s:ddloption text="1" value="1"/>
								<s:ddloption text="10" value="10"/>
								<s:ddloption text="15" value="15"/>
								<s:ddloption text="30" value="30"/>
							</s:dropdownlist>	
						</s:div>

						<s:textbox name="aggregPagamenti" label="aggregPagamenti" bmodify="true" text="${requestScope.aggregPagamenti}" cssclass="display_none" cssclasslabel="display_none"  />
					</c:if>			        			

<!-- data validita -->
					<c:if test="${!empty tastoIns}">
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:div name="divDataValidita" cssclass="divDataDa">
										<s:date label="Data Validit&agrave;:" prefix="dataValidita" yearbegin="${ddlDateAnnoDa}"
											yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
											separator="/" calendar="${requestScope.dataValidita}"
											cssclasslabel="label85 bold textright"
											cssclass="dateman"
											validator="required;dateISO" showrequired="true"
											message="[dateISO=Data Validit&agrave;: ${msg_dataISO_valida}]">
										</s:date>
										<input type="hidden" id="tx_data_validita_hidden" value="" />
								</s:div>
						</s:div>
					</c:if>

					<c:if test="${!empty tastoUpd or !empty tastoDel}">
						<s:div name="divElement91" cssclass="divRicMetadatiSingleRow">
							<!--<s:label name="datalbl" cssclass="label85 bold textright" text="Data Validità: ${requestScope.dataValiditaS}" />&nbsp;-->
							
							<s:date label="Data Validit&agrave;:" prefix="dataValiditaLbl" yearbegin="${ddlDateAnnoDa}"
								yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
								separator="/" calendar="${requestScope.dataValidita}"
								cssclasslabel="label85 bold textright"
								cssclass="dateman" disabled="true" >
							</s:date>
						</s:div>
						
						<s:textbox name="dataValiditaS" label="dataValidita" bmodify="true" text="${requestScope.dataValiditaS}" cssclass="display_none" cssclasslabel="display_none"  />
						<s:date label="Data Validit&agrave;:" prefix="dataValidita" yearbegin="${ddlDateAnnoDa}"
							yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
							separator="/" calendar="${requestScope.dataValidita}"
							cssclasslabel="display_none"
							cssclass="display_none">
						</s:date>
					</c:if>

<!-- data validita -->

			    </s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<c:if test="${!empty tastoIns or !empty tastoUpd}">

						<s:div name="divElement72" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="indFesta" disable="false" 
													validator="required" showrequired="true"
													cssclass="tbddl floatleft" 
													label="Gest.Festivit&agrave;:" 
													cssclasslabel="label85 bold floatleft textright"
													valueselected="${requestScope.indFesta}">
								<s:ddloption text="Anticipo" value="A"/>
								<s:ddloption text="Posticipo" value="D"/>
								<s:ddloption text="Invariato" value="U"/>
								<s:ddloption text="Giorni Lavorativi" value="L"/>
							</s:dropdownlist>
						</s:div>

						<s:div name="divElement73" cssclass="divRicMetadatiSingleRow">
									<s:textbox name="giorniLatenza" label="Gg Latenza:" bmodify="true" text="${requestScope.giorniLatenza}" 
											   cssclass="textareaman" cssclasslabel="label85 bold textright" showrequired="true"
									           maxlenght="3" validator="required;digits;minlength=1;maxlength=3" />
						</s:div>
	
	
						<s:div name="divElement74" cssclass="divRicMetadatiSingleRow">
									<s:textbox name="limMaxGiorni" label="Limite Giorni:" bmodify="true" text="${requestScope.limMaxGiorni}" 
											   cssclass="textareaman" cssclasslabel="label85 bold textright"  showrequired="true"
									           maxlenght="3" validator="required;accept=^\b([1-9][0-9]*)$;minlength=1;maxlength=3" message="[accept=Limite Giorni: ${msg_configurazione_numero_maggiore_zero}]"/>
						</s:div>
					</c:if>				

					<c:if test="${!empty tastoDel}">

						<s:div name="divElement75" cssclass="divRicMetadatiSingleRow">
<!-- 							<s:label name="indFestalbl" cssclass="label110 bold" text="Gestione festività: ${requestScope.indFesta}" /> -->
 
							<s:dropdownlist name="indFesta2" disable="true" 
													validator="required" showrequired="true"
													cssclass="tbddl floatleft" 
													label="Gest.Festivit&agrave;:" 
													cssclasslabel="label85 bold floatleft textright"
													valueselected="${requestScope.indFesta}">
								<s:ddloption text="Anticipo" value="A"/>
								<s:ddloption text="Posticipo" value="D"/>
								<s:ddloption text="Invariato" value="U"/>
								<s:ddloption text="Giorni Lavorativi" value="L"/>
							</s:dropdownlist>

						</s:div>

						<s:div name="divElement76" cssclass="divRicMetadatiSingleRow">
							<!--<s:label name="giorniLatenzalbl" cssclass="label110 bold" text="Giorni di latenza: ${requestScope.giorniLatenza}" />-->
							
							<s:textbox name="giorniLatenzalbl" label="Gg Latenza:" bmodify="false" text="${requestScope.giorniLatenza}" 
											   cssclass="textareaman colordisabled" cssclasslabel="label85 bold textright"  showrequired="true"
									           maxlenght="3" validator="required;digits;minlength=1;maxlength=3" />
						</s:div>
		
						<s:div name="divElement77" cssclass="divRicMetadatiSingleRow">
							<!--<s:label name="limMaxGiornilbl" cssclass="label110 bold" text="Limite Giorni: ${requestScope.limMaxGiorni}" />-->
							
							<s:textbox name="limMaxGiornilbl" label="Limite Giorni:" bmodify="false" text="${requestScope.limMaxGiorni}" 
											   cssclass="textareaman colordisabled" cssclasslabel="label85 bold textright"  showrequired="true"
									           maxlenght="3" validator="required;accept=^\b([1-9][0-9]*)$;minlength=1;maxlength=3" message="[accept=Limite Giorni: ${msg_configurazione_numero_maggiore_zero}]"/>
						</s:div>

						<s:textbox name="limMaxGiorni" label="limMaxGiorni" bmodify="true" text="${requestScope.limMaxGiorni}" cssclass="display_none" cssclasslabel="display_none"  />
						<s:textbox name="giorniLatenza" label="giorniLatenza" bmodify="true" text="${requestScope.giorniLatenza}" cssclass="display_none" cssclasslabel="display_none"  />
						<s:textbox name="indFesta" label="indFesta" bmodify="true" text="${requestScope.indFesta}" cssclass="display_none" cssclasslabel="display_none"  />
					</c:if>

				</s:div>

				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">

<!-- tipo quota fissa -->
					<c:if test="${!empty tastoIns or !empty tastoUpd}">

						<s:div name="divElement78" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="tipoQuotaFissa" disable="false" 
													validator="required" showrequired="true"
													cssclass="tbddl floatleft" 
													label="Tipo Q.Fissa:" 
													cssclasslabel="label85 bold floatleft textright"
													valueselected="${requestScope.tipoQuotaFissa}">
								<s:ddloption text="Transazione" value="T"/>
								<s:ddloption text="Pagamento" value="P"/>
							</s:dropdownlist>
						</s:div>
	
	<!-- percentuale -->
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
									<s:textbox name="compPercRisc" label="% Compenso:" bmodify="true" 
									           text="${requestScope.compPercRisc}" 
											   cssclass="textareaman" cssclasslabel="label85 bold textright"  
									           maxlenght="6" validator="ignore;numberIT;maxlength=6" />
						</s:div>
	<!-- importo in euro -->
						<s:div name="divElement92" cssclass="divRicMetadatiSingleRow">
									<s:textbox name="compQuotaFissa" label="Quota fissa:" bmodify="true" 
											   text="${requestScope.compQuotaFissa}" 
											   cssclass="textareaman" cssclasslabel="label85 bold textright"  
									           maxlenght="18" validator="ignore;numberIT;maxlength=15" />
						</s:div>
				</c:if>
				<c:if test="${!empty tastoDel}">
						<s:div name="divElement79" cssclass="divRicMetadatiSingleRow">

							<s:dropdownlist name="tipoQuotaFissa2" disable="true" 
													validator="required" showrequired="true"
													cssclass="tbddl floatleft" 
													label="Tipo Q.Fissa:" 
													cssclasslabel="label85 bold floatleft textright"
													valueselected="${requestScope.tipoQuotaFissa}">
								<s:ddloption text="Transazione" value="T"/>
								<s:ddloption text="Pagamento" value="P"/>
							</s:dropdownlist>
						</s:div>
	
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<!--<s:label name="compPercRisclbl" cssclass="label110 bold" text="% compenso: ${requestScope.compPercRisc}" />-->
							
							<s:textbox name="compPercRisclbl" label="% Compenso:" bmodify="false" 
									           text="${requestScope.compPercRisc}" 
											   cssclass="textareaman colordisabled" cssclasslabel="label85 bold textright"  
									           maxlenght="6" validator="ignore;numberIT;maxlength=6" />
						</s:div>
	
						<s:div name="divElement93" cssclass="divRicMetadatiSingleRow">
							<!--<s:label name="compQuotaFissalbl" cssclass="label110 bold" text="Quota fissa: ${requestScope.compQuotaFissa}" />-->
							
							<s:textbox name="compQuotaFissa1" label="Quota fissa:" bmodify="false" 
											   text="${requestScope.compQuotaFissa}" 
											   cssclass="textareaman colordisabled" cssclasslabel="label85 bold textright"  
									           maxlenght="18" validator="ignore;numberIT;maxlength=15" />
							
						</s:div>

						<s:textbox name="tipoQuotaFissa" label="tipoQuotaFissa" bmodify="true" text="${requestScope.tipoQuotaFissa}" cssclass="display_none" cssclasslabel="display_none"  />
						<s:textbox name="compPercRisc" label="compPercRisc" bmodify="true" text="${requestScope.compPercRisc}" cssclass="display_none" cssclasslabel="display_none"  />
						<s:textbox name="compQuotaFissa2" label="compQuotaFissa" bmodify="true" text="${requestScope.compQuotaFissa}" cssclass="display_none" cssclasslabel="display_none"  />
				</c:if>	
			</s:div>
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
		<s:form name="schedaConvenzioneForm2" action="ricercaConvenzioniImp.do${formParameters}&DDLType=12" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
				<s:button id="tx_button_cerca" cssclass="btnStyle" type="submit" text="Indietro" onclick=""/>
		</s:form>		
	</s:div>
-->

				
	</s:div>
</s:div>




	
	
	
