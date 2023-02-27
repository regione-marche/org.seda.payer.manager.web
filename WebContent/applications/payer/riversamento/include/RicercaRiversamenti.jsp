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


<script src="../applications/templates/riversamento/js/popup.js"
	type="text/javascript"></script>

<script type="text/javascript">
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();
		
	$(function() {
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#data_riversamento_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_riversamento_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_riversamento_da_day_id").val(dateText.substr(0,2));
												$("#data_riversamento_da_month_id").val(dateText.substr(3,2));
												$("#data_riversamento_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_riversamento_da_day_id",
				                            "data_riversamento_da_month_id",
				                            "data_riversamento_da_year_id",
				                            "data_riversamento_da_hidden");
			}
		});
		$("#data_riversamento_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_riversamento_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_riversamento_a_day_id").val(dateText.substr(0,2));
												$("#data_riversamento_a_month_id").val(dateText.substr(3,2));
												$("#data_riversamento_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_riversamento_a_day_id",
				                            "data_riversamento_a_month_id",
				                            "data_riversamento_a_year_id",
				                            "data_riversamento_a_hidden");
			}
		});
	});

	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}

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
		<c:if test="${!empty param.rowsPerPage}">
			<c:param name="rowsPerPage">${param.rowsPerPage}</c:param>
		</c:if>
		<c:if test="${!empty param.pageNumber}">  
			<c:param name="pageNumber">${param.pageNumber}</c:param>
		</c:if>
		<c:if test="${!empty param.order}">  
			<c:param name="order">${param.order}</c:param>
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

	<c:url value="" var="formParameters2">
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

<!--  
<s:div name="link">
	<s:hyperlink name="home" text="homepage" href="default.do"/> <br>
</s:div>
-->


		<s:div name="div_selezione" cssclass="div_align_center divSelezione">
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">

	<s:form name="ricercaRiversamentiForm" action="ricercaRiversamenti.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:textbox name="DDLType" label="DDLType" bmodify="true" text="21" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="DDLChanged" label="DDLChanged" bmodify="true" text="" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="ext" label="ext" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none" />

		<s:textbox name="hRowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="hPageNumber" label="pageNumberRic" bmodify="true" text="${pageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="hOrder" label="orderRic" bmodify="true" text="${order}" cssclass="display_none" cssclasslabel="display_none"  />

		<s:textbox name="megaBottoneAlert" label="megaBottoneAlert" bmodify="true" text="${megaBottoneAlert}" cssclass="display_none" cssclasslabel="display_none"  />

			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
					Ricerca Riversamenti
			</s:div>

			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">

					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:dropdownlist name="codSocieta" disable="${requestScope.societaDdlDisabled}" cssclass="tbddl floatleft" 
										label="Societ&aacute;:" cssclasslabel="label85 bold floatleft textright"
										cachedrowset="listaSocieta" usexml="true" 
										onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
										valueselected="${codSocieta}">
							<s:ddloption text="Tutte le Societ&agrave;" value=""/>
							<s:ddloption text="{2}" value="{1}"/>
						</s:dropdownlist>
<!-- 										onchange="setDdl('codSocieta'); callSubmit(this.form);" -->
						<noscript>
							<s:button id="tx_button_societa_changed" type="submit" 
							disable="${requestScope.societaDdlDisabled}" text="" onclick="" 
							cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
						</noscript>
			        </s:div>

					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
						<s:dropdownlist name="codProvincia" disable="${requestScope.provinciaDdlDisabled}" cssclass="tbddl floatleft" 
										label="Provincia:" cssclasslabel="label65 bold floatleft textright"
										cachedrowset="listaProvinceBen" usexml="true" 
										onchange="setFiredButton('tx_button_provincia_changed');this.form.submit();"
										valueselected="${codProvincia}">
							<s:ddloption text="Tutte le Province" value=""/>
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
<!-- 										onchange="setDdl('codProvincia'); callSubmit(this.form);" -->
						<noscript>
							<s:button id="tx_button_provincia_changed" type="submit" 
							disable="${requestScope.provinciaDdlDisabled}" text="" onclick="" 
							cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
						</noscript>
			        </s:div>

					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:dropdownlist name="codUtente" disable="${requestScope.utenteDdlDisabled}" cssclass="tbddlMax floatleft" 
												label="Utente:" cssclasslabel="label65 bold textright"
												cachedrowset="listaUtenti" usexml="true" 
												onchange="setFiredButton('tx_button_utente_changed');this.form.submit();"
												valueselected="${codUtente}">
							<s:ddloption text="Tutti gli Utenti" value=""/>
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<!-- onchange="setDdl('codUtente'); callSubmit(this.form);" -->
						<noscript>
							<s:button id="tx_button_utente_changed" type="submit" 
							disable="${requestScope.utenteDdlDisabled}" text="" onclick="" 
							cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
						</noscript>
			        </s:div>

				</s:div>
				
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="codBeneficiario" disable="${requestScope.beneficiarioDdlDisabled}" cssclass="tbddl floatleft" 
												label="Beneficiario:" cssclasslabel="label85 bold floatleft textright"
												cachedrowset="listaBeneficiari" usexml="true" 
												valueselected="${codBeneficiario}">
							<s:ddloption text="Tutti gli Enti Beneficiari" value=""/>
							<s:ddloption text="{2} {3}" value="{1}" />
						</s:dropdownlist>
					</s:div>

				</s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement5" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement5a" cssclass="labelData">
							<s:label name="label_data_crea" cssclass="seda-ui-label label85 bold textright" text="Data Riversamento" />
						</s:div>
						
						<s:div name="divElement5b" cssclass="floatleft">
							<s:div name="divDataCreaDa" cssclass="divDataDa">
								<s:date label="Da:" prefix="data_riversamento_da" yearbegin="${ddlDateAnnoDa}"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${data_riversamento_da}"
									cssclasslabel="labelsmall"
									cssclass="dateman">
								</s:date>
								<input type="hidden" id="data_riversamento_da_hidden" value="" />
							</s:div>
							
							<s:div name="divDataCreaA" cssclass="divDataA">
								<s:date label="A:" prefix="data_riversamento_a" yearbegin="${ddlDateAnnoDa}"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${data_riversamento_a}"
									cssclasslabel="labelsmall"
									cssclass="dateman" >
								</s:date>
								<input type="hidden" id="data_riversamento_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>



				</s:div>

				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist  name="tipoRendicontazione" disable="false" label="Tipo Rend.:" valueselected="${tipoRendicontazione}" 
							                 cssclass="tbddlMax floatleft" cssclasslabel="label85 bold textright">
								<s:ddloption value="" text="Tutto"/>
								<s:ddloption value="R" text="Riversamento"/>
								<s:ddloption value="C" text="Rendicontazione"/>
							</s:dropdownlist>
					</s:div>

					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist  name="strumRiversamento" disable="false" label="Strumento Riv.:" valueselected="${strumRiversamento}" 
						                 cssclass="tbddlMax floatleft" cssclasslabel="label85 bold textright">
							<s:ddloption value="" text="Tutti i tipi di riversamento"/>
							<s:ddloption value="B" text="Bonifico"/>
							<s:ddloption value="N" text="Altro"/>
						</s:dropdownlist>
					</s:div>

					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
					    <c:if test="${requestScope.abilitazione == 'Y'}">					
							<s:dropdownlist  name="statoRiversamento" disable="false" label="Stato Riv.:" valueselected="${(statoRiversamento==null) ? 'X' : statoRiversamento}" 
							                 cssclass="tbddlMax floatleft" cssclasslabel="label85 bold textright">
								<s:ddloption value="T" text="Tutti gli stati"/>
								<s:ddloption value="A" text="Aperto"/>
								<s:ddloption value="C" text="Chiuso"/>
								<s:ddloption value="X" text="Non Eseguito"/>
								<s:ddloption value="S" text="Sospeso"/>
								<s:ddloption value="E" text="Eseguito"/>
								<s:ddloption value="N" text="Eseguito Non Prenotato"/>
								<s:ddloption value="Z" text="Eseguito Prenotato"/>								
								<s:ddloption value="P" text="Eseguito Emesso"/>								
							</s:dropdownlist>
						</c:if>
					    <c:if test="${requestScope.abilitazione == 'N'}">					
							<s:dropdownlist  name="statoRiversamento" disable="false" label="Stato Riv.:" valueselected="${statoRiversamento}" 
							                 cssclass="tbddlMax floatleft" cssclasslabel="label85 bold textright">
								<s:ddloption value="E" text="Eseguito"/>
							</s:dropdownlist>
						</c:if>
					</s:div>


				</s:div>
					
			</s:div>

			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" type="submit" text="Cerca" onclick="" cssclass="btnStyle"/>
				<s:button id="tx_button_reset" type="submit" text="Reset" onclick="" cssclass="btnStyle" />
				
				<c:if test="${!empty listaRiversamenti && ext=='0'}">
					<s:button id="tx_button_cerca_exp" type="submit" text="Espandi" onclick="" cssclass="btnStyle" />
				</c:if>
				<c:if test="${!empty listaRiversamenti && ext=='1'}">
					<s:button id="tx_button_cerca_exp" type="submit" text="Contrai" onclick="" cssclass="btnStyle" />
				</c:if>
				<c:if test="${!empty listaRiversamenti && abilitazione=='Y' && presenza_megab=='Y' && (statoRiversamento=='A' || statoRiversamento=='C' || statoRiversamento=='N')}">
					<s:button id="tx_button_mega_riv" type="submit" text="Bonifici" onclick="" cssclass="btnStyle"/>
				</c:if>
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
			</s:div>
	
	</s:form>

	</s:div>
</s:div>


	
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
	
<!--  

--------------------------------
Presentazione risultati
--------------------------------

 -->	

<!-- colonne -->	
	
<c:if test="${!empty listaRiversamenti}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Elenco Riversamenti
	</s:div>

	<s:div name="divRisultati" cssclass="divRisultati">

	<s:datagrid  viewstate="" cachedrowset="listaRiversamenti" action="ricercaRiversamenti.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
		<s:action>
			<c:url value="ricercaRiversamenti.do">
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
				<c:param name="DDLType">21</c:param>
			</c:url>
		</s:action>
  	    <!-- REV_GREVGDAT -->
		<s:dgcolumn format="dd/MM/yyyy" index="1" label="Data Rivers."  asc="GDATA" desc="GDATD"></s:dgcolumn>

  	    <!-- ANE_CANECENT -->
		<s:dgcolumn label="Ente" asc="CENTA" desc="CENTD">
				<s:hyperlink
					href="#"
					onclick="showDettaglio('Coordinate Bancarie', 'Codice Disposizione Bonifico','{29}','Data Bonifico','{17}');return false"
					cssclass="blacklinknounder"
					text="{2}"
					alt="{29}" />
		</s:dgcolumn>

		<!-- ANE_TANETUFF/ANE_CANECUFF -->
		<s:dgcolumn label="Ufficio" asc="TUFFA" desc="TUFFD">
			<s:if left="{3}{4}" control="eq" right="">
					<s:then>
						&nbsp;
					</s:then>
					<s:else>
						{3}/{4}
					</s:else>
			</s:if>		    
		</s:dgcolumn>

        <!--  REV_FREVTIPO -->
		<s:dgcolumn index="24" label="Tipo Rend." asc="TIPOA" desc="TIPOD"></s:dgcolumn>

		<!-- REV_FREVRIVE -->
		<s:dgcolumn index="25" label="Strumento Riv." asc="RIVEA" desc="RIVED"></s:dgcolumn>

		<!-- REV_FREVSTAT -->
		<s:dgcolumn index="26" label="Stato Riv." asc="STATA" desc="STATD">	</s:dgcolumn>
		
		<!-- REV_IREVTOTA -->
		<s:dgcolumn index="8" format="#,##0.00" label="Importo" asc="TOTAA" desc="TOTAD" css="text_align_right"></s:dgcolumn>

		<!-- REV_IREVCONC -->
		<s:dgcolumn index="9" format="#,##0.00" label="Contributo cittadino" asc="CONCA" desc="CONCD" css="text_align_right"></s:dgcolumn>

		<!-- REV_IREVCGTW -->
		<s:dgcolumn index="10" format="#,##0.00" label="Commissioni Banca" asc="CGTWA" desc="CGTWD" css="text_align_right"></s:dgcolumn>

		<!-- REV_IREVSPES -->
		<s:dgcolumn index="11" format="#,##0.00" label="Spese Notifica" asc="SPESA" desc="SPESD" css="text_align_right"></s:dgcolumn>

		<!-- REV_IREVGESC -->
		<s:dgcolumn index="12" format="#,##0.00" label="Commissioni gestore" asc="GESCA" desc="GESCD" css="text_align_right"></s:dgcolumn>

		<!-- REV_IREVIREV_R -->
		<s:dgcolumn index="13" format="#,##0.00" label="Importo da riversare" asc="IREVRA" desc="IREVRD" css="text_align_right"></s:dgcolumn>

		<!-- REV_IREVIREV_C -->
		<s:dgcolumn index="21" format="#,##0.00" label="Importo da recuperare" asc="IREVCA" desc="IREVCD" css="text_align_right"></s:dgcolumn>
		
		<!-- REV_GREVCHIU -->
		<s:dgcolumn index="14" format="dd/MM/yyyy" label="Data Chiusura" asc="CHIUA" desc="CHIUD" ></s:dgcolumn>

        <s:ifdatagrid left="${ext}" control="eq" right="1">
        	<s:thendatagrid>
				<!-- REV_GREVSOSP -->
				<s:dgcolumn index="15" format="dd/MM/yyyy" label="Data Sospensione" asc="SOSPA" desc="SOSPD"></s:dgcolumn>
		
				<!-- REV_GREVEFFE -->
				<s:dgcolumn index="16" format="dd/MM/yyyy" label="Data Esecuzione" asc="EFFEA" desc="EFFED"></s:dgcolumn>
		
				<!-- CBI_GCBIGDAT -->
				<s:dgcolumn index="17" format="dd/MM/yyyy" label="Data Bonifico" asc="GDATA" desc="GDATD"></s:dgcolumn>
			</s:thendatagrid>
		</s:ifdatagrid>
	    <s:dgcolumn label=" ">
			        <s:if left="${requestScope.abilitazione}" control="eq" right="Y">                                                                                                                                                                             
						<s:then>	
										<s:hyperlink href="riversamentiDettaglioImp.do${formParameters2}&tx_button_cerca=OK&revData={1}&c2={2}&c3={3}&c4={4}&revTipo={5}&revRive={6}&c7={7}&c8={8}&c9={9}&c10={10}&c11={11}&c12={12}&c13={13}&c14={14}&c15={15}&c16={16}&c17={17}&revSoc={18}&revUte={19}&revBen={20}&c21={21}&c22={22}&c23={23}" alt="Dettaglio Riversamento" text="R" cssclass="blacklink" />
										<s:hyperlink href="riversamentiDettaglioContr.do${formParameters2}&tx_button_cerca=OK&revData={1}&c2={2}&c3={3}&c4={4}&revTipo={5}&revRive={6}&c7={7}&c8={8}&c9={9}&c10={10}&c11={11}&c12={12}&c13={13}&c14={14}&c15={15}&c16={16}&c17={17}&revSoc={18}&revUte={19}&revBen={20}&c21={21}&c22={22}&c23={23}" alt="Dettaglio Riversamento per Contribuente" text="C" cssclass="blacklink" />
						</s:then>
						<s:else>
										<s:hyperlink href="riversamentiDettaglioContr.do${formParameters2}&tx_button_cerca=OK&revData={1}&c2={2}&c3={3}&c4={4}&revTipo={5}&revRive={6}&c7={7}&c8={8}&c9={9}&c10={10}&c11={11}&c12={12}&c13={13}&c14={14}&c15={15}&c16={16}&c17={17}&revSoc={18}&revUte={19}&revBen={20}&c21={21}&c22={22}&c23={23}" alt="Dettaglio Riversamento per Contribuente" text="C" cssclass="blacklink" />
						</s:else>
					</s:if>
        </s:dgcolumn>
		
		
		
		<s:ifdatagrid left="${requestScope.abilitazione}" control="eq" right="Y">
			<s:thendatagrid>         
				<s:dgcolumn label="&nbsp;&nbsp;Operazioni&nbsp;&nbsp;">
					    <s:if left="{24}{25}{27}{7}" control="eq" right="Rivers.BonificoNE">		    
							<s:then>
<!-- richiedi flusso CBI -->
								<s:hyperlink href="aggiornaFlagCBI.do${formParameters}&codSoc={18}&codUte={19}&dataRiv={1}&codEnte={20}&tipoRend={6}&tipoRiv={5}&fcbi=S&coord={28}&tx_button_set_flag_cbi" alt="Richiedi Flusso CBI" text="" imagesrc="../applications/templates/riversamento/img/richiediFlussoCBI.png"cssclass="hlStyle" />
							</s:then>
						</s:if>
					    <s:if left="{27}" control="eq" right="P">		    
							<s:then>
<!-- download flusso CBI -->
								<s:hyperlink href="downloadFlussoCBI.do${formParameters}&codSoc={18}&codUte={19}&dataRiv={1}&codEnte={20}&tipoRend={6}&tipoRiv={5}" text="" alt="Download Flusso CBI" imagesrc="../applications/templates/riversamento/img/downloadCBI.png" cssclass="hlStyle" />
							</s:then>
						</s:if>
					    <s:if left="{7}" control="eq" right="C">		    
							<s:then>
<!-- apri -->
								<s:hyperlink href="aggiornaStatoRiv.do${formParameters}&codSoc={18}&codUte={19}&dataRiv={1}&codEnte={20}&tipoRend={6}&tipoRiv={5}&stato=A&tx_button_avanza_stato" alt="Apri" text="" imagesrc="../applications/templates/riversamento/img/azioni.png" cssclass="hlStyle" />
<!-- sospendi -->
								<s:hyperlink href="riversamentiSospensioneStart.do${formParameters2}&revData={1}&c2={2}&c3={3}&c4={4}&revTipo={5}&revRive={6}&c7={7}&c8={8}&c9={9}&c10={10}&c11={11}&c12={12}&c13={13}&c14={14}&c15={15}&c16={16}&c17={17}&revSoc={18}&revUte={19}&revBen={20}&c21={21}&c22={22}&c23={23}" alt="Sospendi" text="" imagesrc="../applications/templates/riversamento/img/azioniRed.png" cssclass="hlStyle" />				
<!-- esegui -->
								<s:hyperlink href="aggiornaStatoRiv.do${formParameters}&codSoc={18}&codUte={19}&dataRiv={1}&codEnte={20}&tipoRend={6}&tipoRiv={5}&stato=E&tx_button_avanza_stato" alt="Esegui" text="" imagesrc="../applications/templates/riversamento/img/azioniGreen.png" cssclass="hlStyle" />
							</s:then>
						</s:if>
					    <s:if left="{7}" control="eq" right="A">		    
							<s:then>
<!-- chiudi -->
								<s:hyperlink href="aggiornaStatoRiv.do${formParameters}&codSoc={18}&codUte={19}&dataRiv={1}&codEnte={20}&tipoRend={6}&tipoRiv={5}&stato=C&tx_button_avanza_stato" alt="Chiudi" text="" imagesrc="../applications/templates/riversamento/img/azioni.png" cssclass="hlStyle" />
							</s:then>
						</s:if>
					    <s:if right="{7}" control="eq" left="S">		    
							<s:then>
<!-- revoca sospensione -->
								<s:hyperlink href="aggiornaStatoRiv.do${formParameters}&codSoc={18}&codUte={19}&dataRiv={1}&codEnte={20}&tipoRend={6}&tipoRiv={5}&stato=R&tx_button_avanza_stato" alt="Revoca Sospensione" text="" imagesrc="../applications/templates/riversamento/img/azioniRed.png" cssclass="hlStyle" />
							</s:then>
						</s:if>
					    <s:if left="{22}" control="eq" right="">		    
							<s:then>
							   &nbsp;
							</s:then>
							<s:else>
							 	<s:hyperlink href="stampaPDFRiv.do${formParameters}&codSoc={18}&codUte={19}&dataRiv={1}&codEnte={20}&tipoRend={6}&tipoRiv={5}" alt="Stampa pdf" text="" imagesrc="../applications/templates/riversamento/img/stampaPDF.png" cssclass="hlStyle" /> 
							</s:else>
						</s:if>
						<s:if left="{23}" control="eq" right="">
							<s:then>
							   &nbsp;
							</s:then>
							<s:else>
							 	<s:hyperlink href="stampaCSVRiv.do${formParameters}&codSoc={18}&codUte={19}&dataRiv={1}&codEnte={20}&tipoRend={6}&tipoRiv={5}" alt="File csv / tracciato specifico" text="" imagesrc="../applications/templates/riversamento/img/dettagliCSV.png" cssclass="hlStyle" />  
							</s:else>
						</s:if>
		        </s:dgcolumn>
			</s:thendatagrid>
			<s:elsedatagrid>
				<s:dgcolumn label="Operazioni">
					    <s:if left="{22}" control="eq" right="">		    
							<s:then>
							   &nbsp;
							</s:then>
							<s:else>
							 	<s:hyperlink href="stampaPDFRiv.do${formParameters}&codSoc={18}&codUte={19}&dataRiv={1}&codEnte={20}&tipoRend={6}&tipoRiv={5}" alt="Stampa pdf" text="" imagesrc="../applications/templates/riversamento/img/stampaPDF.png" cssclass="hlStyle" /> 
							</s:else>
						</s:if>
						<s:if left="{23}" control="eq" right="">
							<s:then>
							   &nbsp;
							</s:then>
							<s:else>
							 	<s:hyperlink href="stampaCSVRiv.do${formParameters}&codSoc={18}&codUte={19}&dataRiv={1}&codEnte={20}&tipoRend={6}&tipoRiv={5}" alt="File csv / tracciato specifico" text="" imagesrc="../applications/templates/riversamento/img/dettagliCSV.png" cssclass="hlStyle" />  
							</s:else>
						</s:if>
		        </s:dgcolumn>
			</s:elsedatagrid>
		</s:ifdatagrid>
    </s:datagrid>


	<!-- 
	
	-----------------
	tabella riassunto
	-----------------
	 -->

	<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
		<s:thead>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:th cssclass="seda-ui-datagridheadercell"  icol="8"><b>Riepilogo statistico</b></s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr cssclass="seda-ui-datagridrowdispari">
				<s:td cssclass="seda-ui-datagridcell">Numero Riversamenti</s:td>
				<s:td cssclass="seda-ui-datagridcell">Importo</s:td>
				<s:td cssclass="seda-ui-datagridcell">Contributo Cittadino</s:td>
				<s:td cssclass="seda-ui-datagridcell">Commissioni Banca</s:td>
				<s:td cssclass="seda-ui-datagridcell">Spese Notifica</s:td>
				<s:td cssclass="seda-ui-datagridcell">Commissioni Gestore</s:td>
				<s:td cssclass="seda-ui-datagridcell">Importo da Riversare</s:td>
				<s:td cssclass="seda-ui-datagridcell">Importo da Recuperare</s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
					<s:td cssclass="seda-ui-datagridcell text_align_right"><c:out value="${NumRighe}"/></s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${TotImportoContribuenti}" minFractionDigits="2" maxFractionDigits="2"/></s:td>

					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${TotImportoCittadino}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${TotCommissioniGateway}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${TotSpeseNotifica}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${TotCommissioneGestore}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${TotImportoRiversare}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${TotImportoRecuperare}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
			</s:tr>
		</s:tbody>
	</s:table>
</s:div>

</c:if>

	<!-- POPUP -->
	<s:div cssclass="seda-ui-div" name="popupDettaglio">
		<s:div name="div_table_trans">
			<s:table border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglio1">
				<s:thead>
					<s:tr>
						<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="4" id="testata">Intestazione</s:th>
					</s:tr>
				</s:thead>
				<s:tbody>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader" id="nomeCampo">Nome Campo1:</s:td>
						<s:td id="valoreCampo" icol="3">Valore Campo1</s:td>
					</s:tr>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader" id="nomeCampo2">Nome Campo2:</s:td>
						<s:td id="valoreCampo2" icol="3">Valore Campo2</s:td>
					</s:tr>
				</s:tbody>
			</s:table>
		</s:div>
		
		<s:div name="chiudiPopupDettaglio">
			<input type="image" src="../applications/templates/shared/img/close_gray2.png" alt="Chiudi" title="Chiudi" style="width:25px;height:25px"> 
			<!--  <input type="button" value="Chiudi" src="" />-->
		</s:div>
	</s:div>
	<s:div cssclass="seda-ui-div" name="backgroundPopup"> &nbsp; </s:div>	