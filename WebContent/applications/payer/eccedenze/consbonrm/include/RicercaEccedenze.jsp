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
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();

	$(function() {
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#data_ricezione_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_ricezione_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_ricezione_da_day_id").val(dateText.substr(0,2));
												$("#data_ricezione_da_month_id").val(dateText.substr(3,2));
												$("#data_ricezione_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_ricezione_da_day_id",
				                            "data_ricezione_da_month_id",
				                            "data_ricezione_da_year_id",
				                            "data_ricezione_da_hidden");
			}
		});
		$("#data_ricezione_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_ricezione_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_ricezione_a_day_id").val(dateText.substr(0,2));
												$("#data_ricezione_a_month_id").val(dateText.substr(3,2));
												$("#data_ricezione_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_ricezione_a_day_id",
				                            "data_ricezione_a_month_id",
				                            "data_ricezione_a_year_id",
				                            "data_ricezione_a_hidden");
			}
		});
		$("#data_ordine_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_ordine_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_ordine_da_day_id").val(dateText.substr(0,2));
												$("#data_ordine_da_month_id").val(dateText.substr(3,2));
												$("#data_ordine_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_ordine_da_day_id",
				                            "data_ordine_da_month_id",
				                            "data_ordine_da_year_id",
				                            "data_ordine_da_hidden");
			}
		});
		$("#data_ordine_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_ordine_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_ordine_a_day_id").val(dateText.substr(0,2));
												$("#data_ordine_a_month_id").val(dateText.substr(3,2));
												$("#data_ordine_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_ordine_a_day_id",
				                            "data_ordine_a_month_id",
				                            "data_ordine_a_year_id",
				                            "data_ordine_a_hidden");
			}
		});

	});

	
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
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
		<c:if test="${!empty param.rowsPerPage}">
			<c:param name="rRowsPerPage">${param.rowsPerPage}</c:param>
		</c:if>
		<c:if test="${!empty param.pageNumber}">  
			<c:param name="rPageNumber">${param.pageNumber}</c:param>
		</c:if>
		<c:if test="${!empty param.order}">  
			<c:param name="rOrder">${param.order}</c:param>
		</c:if>
		<c:if test="${!empty param.tx_societa}">  
			<c:param name="tx_societa">${param.tx_societa}</c:param>
		</c:if>
		<c:if test="${!empty param.tx_provincia}">  
			<c:param name="tx_provincia">${param.tx_provincia}</c:param>
		</c:if>
		<c:if test="${!empty param.tx_UtenteEnte}">  
			<c:param name="tx_UtenteEnte">${param.tx_UtenteEnte}</c:param>
		</c:if>
		<c:if test="${!empty param.codFiscale}">  
			<c:param name="codFiscale">${param.codFiscale}</c:param>
		</c:if>
		<c:if test="${!empty param.documento}">  
			<c:param name="documento">${param.documento}</c:param>
		</c:if>
		<c:if test="${!empty param.data_ricezione_da_day}">  
			<c:param name="data_ricezione_da_day">${param.data_ricezione_da_day}</c:param>
		</c:if>
		<c:if test="${!empty param.data_ricezione_da_month}">  
			<c:param name="data_ricezione_da_month">${param.data_ricezione_da_month}</c:param>
		</c:if>
		<c:if test="${!empty param.data_ricezione_da_year}">  
			<c:param name="data_ricezione_da_year">${param.data_ricezione_da_year}</c:param>
		</c:if>
		<c:if test="${!empty param.data_ricezione_a_day}">  
			<c:param name="data_ricezione_a_day">${param.data_ricezione_a_day}</c:param>
		</c:if>
		<c:if test="${!empty param.data_ricezione_a_month}">  
			<c:param name="data_ricezione_a_month">${param.data_ricezione_a_month}</c:param>
		</c:if>
		<c:if test="${!empty param.data_ricezione_a_year}">  
			<c:param name="data_ricezione_a_year">${param.data_ricezione_a_year}</c:param>
		</c:if>
		<c:if test="${!empty param.esitoRim}">  
			<c:param name="esitoRim">${param.esitoRim}</c:param>
		</c:if>
		<c:if test="${!empty param.strumento}">  
			<c:param name="strumento">${param.strumento}</c:param>
		</c:if>
		<c:if test="${!empty param.data_ordine_da_day}">  
			<c:param name="data_ordine_da_day">${data_ordine_da_day}</c:param>
		</c:if>
		<c:if test="${!empty param.data_ordine_da_month}">  
			<c:param name="data_ordine_da_month">${param.data_ordine_da_month}</c:param>
		</c:if>
		<c:if test="${!empty param.data_ordine_da_year}">  
			<c:param name="data_ordine_da_year">${param.data_ordine_da_year}</c:param>
		</c:if>
		<c:if test="${!empty param.data_ordine_a_day}">  
			<c:param name="data_ordine_a_day">${data_ordine_a_day}</c:param>
		</c:if>
		<c:if test="${!empty param.data_ordine_a_month}">  
			<c:param name="data_ordine_a_month">${param.data_ordine_a_month}</c:param>
		</c:if>
		<c:if test="${!empty param.data_ordine_a_year}">  
			<c:param name="data_ordine_a_year">${param.data_ordine_a_year}</c:param>
		</c:if>
		<c:if test="${!empty ext}">
			<c:param name="ext">${ext}</c:param>
		</c:if>
	</c:url>



		<s:div name="div_selezione" cssclass="div_align_center divSelezione">
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">

	<s:form name="ricercaEccedenzeForm" action="ricercaEccedenze.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:textbox name="DDLType" label="DDLType" bmodify="true" text="11" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="ext" label="ext" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none" />

		<s:textbox name="hRowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="hPageNumber" label="pageNumberRic" bmodify="true" text="${pageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="hOrder" label="orderRic" bmodify="true" text="${order}" cssclass="display_none" cssclasslabel="display_none"  />

			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
					Ricerca Rimborsi
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
						<s:dropdownlist name="tx_provincia"
							disable="${ddlProvinciaDisabled}" cssclass="tbddl floatleft"
							label="Provincia:"
							cssclasslabel="label65 bold floatleft textright"
							cachedrowset="listaProvince" usexml="true"
							onchange="setFiredButton('tx_button_provincia_changed');this.form.submit();"
							valueselected="${tx_provincia}">
							<s:ddloption text="Tutte le Province" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_provincia_changed"
								disable="${ddlProvinciaDisabled}" onclick="" text=""
								type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
						</noscript>
			        </s:div>

					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:dropdownlist name="tx_UtenteEnte"
							disable="${ddlUtenteEnteDisabled}" cssclass="tbddlMax floatleft"
							label="Ente:" cssclasslabel="label65 bold textright"
							cachedrowset="listaUtentiEnti" usexml="true"
							valueselected="${tx_UtenteEnte}">
							<s:ddloption text="Tutti gli Enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
			        </s:div>

				</s:div>
				
				<%--  ---------------------------------------------- --%>
				
				
				
				
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore"
							bmodify="true" name="codFiscale" label="Cod. Fiscale/P.IVA:"
							maxlenght="16"
							text="${codFiscale}"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright" />
					</s:div>
					<s:div name="divElement41" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore"
							bmodify="true" name="documento" label="Documento:"
							maxlenght="20"
							text="${documento}"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright" />
					</s:div>

				</s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement5" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement5a" cssclass="labelData">
							<s:label name="label_data_ricezione" cssclass="seda-ui-label label85 bold textright" text="Data Ricezione" />
						</s:div>
						
						<s:div name="divElement5b" cssclass="floatleft">
							<s:div name="divDataRicezioneDa" cssclass="divDataDa">
								<s:date label="Da:" prefix="data_ricezione_da" yearbegin="${ddlDateAnnoDa}"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${data_ricezione_da}"
									cssclasslabel="labelsmall"
									cssclass="dateman">
								</s:date>
								<input type="hidden" id="data_ricezione_da_hidden" value="" />
							</s:div>
							
							<s:div name="divDataRicezioneA" cssclass="divDataA">
								<s:date label="A:" prefix="data_ricezione_a" yearbegin="${ddlDateAnnoDa}"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${data_ricezione_a}"
									cssclasslabel="labelsmall"
									cssclass="dateman" >
								</s:date>
								<input type="hidden" id="data_ricezione_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>

					<s:div name="divElement51" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement51a" cssclass="labelData">
							<s:label name="label_data_ordine" cssclass="seda-ui-label label85 bold textright" text="Data Ordine" />
						</s:div>
						
						<s:div name="divElement51b" cssclass="floatleft">
							<s:div name="divDataOrdineDa" cssclass="divDataDa">
								<s:date label="Da:" prefix="data_ordine_da" yearbegin="${ddlDateAnnoDa}"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${data_ordine_da}"
									cssclasslabel="labelsmall"
									cssclass="dateman">
								</s:date>
								<input type="hidden" id="data_ordine_da_hidden" value="" />
							</s:div>
							
							<s:div name="divDataOrdineA" cssclass="divDataA">
								<s:date label="A:" prefix="data_ordine_a" yearbegin="${ddlDateAnnoDa}"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${data_ordine_a}"
									cssclasslabel="labelsmall"
									cssclass="dateman" >
								</s:date>
								<input type="hidden" id="data_ordine_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>


				</s:div>

				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist  name="esitoRim" disable="false" label="Esito Rimborso:" valueselected="${esitoRim}" 
							                 cssclass="tbddlMax floatleft" cssclasslabel="label85 bold textright">
								<s:ddloption value="" text="Tutti gli esiti"/>
								<s:ddloption value="0" text="Rimborso da fare"/>
								<s:ddloption value="1" text="Rimborso eseguito con esito positivo"/>
								<s:ddloption value="2" text="Rimborso eseguito con esito negativo"/>
								<s:ddloption value="A" text="Rimborso eseguito in attesa di esito"/>								
							</s:dropdownlist>
					</s:div>

					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist  name="strumento" disable="false" label="Strumento:" valueselected="${strumento}" 
						                 cssclass="tbddlMax floatleft" cssclasslabel="label85 bold textright">
							<s:ddloption value="" text="Tutti gli strumenti"/>
							<s:ddloption value="A" text="Assegno di traenza"/>
							<s:ddloption value="B" text="Bonifico"/>
						</s:dropdownlist>
					</s:div>
				</s:div>

					
			</s:div>

			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" type="submit" text="Cerca" onclick="" cssclass="btnStyle"/>
				<s:button id="tx_button_reset" type="submit" text="Reset" onclick="" cssclass="btnStyle" />
				
<%--  
				<c:if test="${!empty listaEccedenze && ext=='0'}">
					<s:button id="tx_button_cerca_exp" type="submit" text="Espandi" onclick="" cssclass="btnStyle" />
				</c:if>
				<c:if test="${!empty listaEccedenze && ext=='1'}">
					<s:button id="tx_button_cerca_exp" type="submit" text="Contrai" onclick="" cssclass="btnStyle" />
				</c:if>
 --%>
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
			</s:div>
	
	</s:form>

	</s:div>
</s:div>


	
	
	
<%--  

--------------------------------
Presentazione risultati
--------------------------------

 --%>	

<%-- colonne --%>	
	
<c:if test="${!empty listaEccedenze}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Elenco Rimborsi
	</s:div>

	<s:div name="divRisultati" cssclass="divRisultati">

	<s:datagrid  viewstate="" cachedrowset="listaEccedenze" action="ricercaEccedenze.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
		<s:action>
			<c:url value="ricercaEccedenze.do">
				<c:if test="${!empty param.tx_societa}">  
					<c:param name="tx_societa">${param.tx_societa}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_provincia}">  
					<c:param name="tx_provincia">${param.tx_provincia}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_UtenteEnte}">  
					<c:param name="tx_UtenteEnte">${param.tx_UtenteEnte}</c:param>
				</c:if>
				<c:if test="${!empty param.codFiscale}">  
					<c:param name="codFiscale">${param.codFiscale}</c:param>
				</c:if>
				<c:if test="${!empty param.documento}">  
					<c:param name="documento">${param.documento}</c:param>
				</c:if>
				<c:if test="${!empty param.data_ricezione_da_day}">  
					<c:param name="data_ricezione_da_day">${param.data_ricezione_da_day}</c:param>
				</c:if>
				<c:if test="${!empty param.data_ricezione_da_month}">  
					<c:param name="data_ricezione_da_month">${param.data_ricezione_da_month}</c:param>
				</c:if>
				<c:if test="${!empty param.data_ricezione_da_year}">  
					<c:param name="data_ricezione_da_year">${param.data_ricezione_da_year}</c:param>
				</c:if>
				<c:if test="${!empty param.data_ricezione_a_day}">  
					<c:param name="data_ricezione_a_day">${param.data_ricezione_a_day}</c:param>
				</c:if>
				<c:if test="${!empty param.data_ricezione_a_month}">  
					<c:param name="data_ricezione_a_month">${param.data_ricezione_a_month}</c:param>
				</c:if>
				<c:if test="${!empty param.data_ricezione_a_year}">  
					<c:param name="data_ricezione_a_year">${param.data_ricezione_a_year}</c:param>
				</c:if>
				<c:if test="${!empty param.esitoRim}">  
					<c:param name="esitoRim">${param.esitoRim}</c:param>
				</c:if>
				<c:if test="${!empty param.strumento}">  
					<c:param name="strumento">${param.strumento}</c:param>
				</c:if>
				<c:if test="${!empty param.data_ordine_da_day}">  
					<c:param name="data_ordine_da_day">${data_ordine_da_day}</c:param>
				</c:if>
				<c:if test="${!empty param.data_ordine_da_month}">  
					<c:param name="data_ordine_da_month">${param.data_ordine_da_month}</c:param>
				</c:if>
				<c:if test="${!empty param.data_ordine_da_year}">  
					<c:param name="data_ordine_da_year">${param.data_ordine_da_year}</c:param>
				</c:if>
				<c:if test="${!empty param.data_ordine_a_day}">  
					<c:param name="data_ordine_a_day">${data_ordine_a_day}</c:param>
				</c:if>
				<c:if test="${!empty param.data_ordine_a_month}">  
					<c:param name="data_ordine_a_month">${param.data_ordine_a_month}</c:param>
				</c:if>
				<c:if test="${!empty param.data_ordine_a_year}">  
					<c:param name="data_ordine_a_year">${param.data_ordine_a_year}</c:param>
				</c:if>
				<c:if test="${!empty ext}">
					<c:param name="ext">${ext}</c:param>
				</c:if>
				<c:param name="DDLType">11</c:param>
			</c:url>
		</s:action>

  	    <%-- ANE_CANECENT/ANE_TANETUFF/ANE_CANECUFF --%>
		<s:dgcolumn label="Ente" asc="CENTA" desc="CENTD">
			<s:if left="{2}{3}" control="eq" right="">
					<s:then>
						{1}
					</s:then>
					<s:else>
						{1}/{2}/{3}
					</s:else>
			</s:if>		    
		</s:dgcolumn>

  	    <%-- ECT_GETCGDAT --%>
		<s:dgcolumn format="dd/MM/yyyy" index="4" label="Data Ricezione"  asc="GDATA" desc="GDATD"></s:dgcolumn>

        <%--  ECC_CECCCFIS --%>
		<s:dgcolumn index="5" label="Cod. Fiscale" asc="CFISA" desc="CFISD"></s:dgcolumn>

		<%-- ECC_CECCNDOC --%>
		<s:dgcolumn index="6" label="Documento" asc="NDOCA" desc="NDOCD"></s:dgcolumn>

		<%-- ECC_NECCRIMB --%>
		<s:dgcolumn index="7" format="#,##0.00" label="Importo Rimborso" asc="RIMBA" desc="RIMBD" css="text_align_right"></s:dgcolumn>

		<%-- ECC_FECCFTIP --%>
		<s:dgcolumn index="9" label="Strumento" asc="FTIPA" desc="FTIPD"></s:dgcolumn>

		<%-- ECC_GECCGRIM --%>
		<s:dgcolumn index="10" format="dd/MM/yyyy" label="Data Ordine" asc="GRIMA" desc="GRIMD" ></s:dgcolumn>

		<%-- ECC_FECCFESI --%>
		<s:dgcolumn index="12" label="Esito" asc="FESIA" desc="FESID"></s:dgcolumn>

        <s:ifdatagrid left="${ext}" control="eq" right="1">
		</s:ifdatagrid>

	    <s:dgcolumn label="Azioni ">
				<s:hyperlink href="eccedenzeDettaglio.do${formParameters}&tx_button_cerca=OK&chiaveEcc={13}" alt="Dettaglio Rimborso" text="" cssclass="blacklink hlStyle" imagesrc="../applications/templates/shared/img/Info.png" />
        </s:dgcolumn>
				
    </s:datagrid>


	<%-- 
	-----------------
	tabella riassunto
	-----------------
	 --%>

	<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
		<s:thead>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:th cssclass="seda-ui-datagridheadercell"  icol="8"><b>Riepilogo statistico</b></s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr cssclass="seda-ui-datagridrowdispari">
				<s:td cssclass="seda-ui-datagridcell" icol="2">Importo Rimborsi</s:td>
				<s:td cssclass="seda-ui-datagridcell" icol="2">Importo Assegni</s:td>
				<s:td cssclass="seda-ui-datagridcell" icol="2">Importo Bonifici</s:td>
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowdispari">
				<s:td cssclass="seda-ui-datagridcell">Positivi</s:td>
				<s:td cssclass="seda-ui-datagridcell">Negativi</s:td>
				<s:td cssclass="seda-ui-datagridcell">Positivi</s:td>
				<s:td cssclass="seda-ui-datagridcell">Negativi</s:td>
				<s:td cssclass="seda-ui-datagridcell">Positivi</s:td>
				<s:td cssclass="seda-ui-datagridcell">Negativi</s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoRimborsiPos}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoRimborsiNeg}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoAssegniPos}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoAssegniNeg}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoBonificiPos}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoBonificiNeg}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
			</s:tr>
		</s:tbody>
	</s:table>
</s:div>

</c:if>

