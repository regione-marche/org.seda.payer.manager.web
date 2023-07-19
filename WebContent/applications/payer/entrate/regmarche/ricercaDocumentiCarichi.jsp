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
		<c:if test="${!empty rowsPerPage}">
			<c:param name="r2RowsPerPage">${rowsPerPage}</c:param> 
		</c:if>
		<c:if test="${!empty pageNumber}">
			<c:param name="r2PageNumber">${pageNumber}</c:param> 
		</c:if>
		<c:if test="${!empty order}">
			<c:param name="r2Order">${order}</c:param> 
		</c:if>
		<c:if test="${!empty ext}">
			<c:param name="r2Ext">${ext}</c:param>
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
		<c:if test="${!empty annoEmissione}">  
			<c:param name="annoEmissione">${annoEmissione}</c:param>
		</c:if>
		<c:if test="${!empty codFiscale}">  
			<c:param name="codFiscale">${codFiscale}</c:param>
		</c:if>
		<c:if test="${!empty numEmissione}">  
			<c:param name="numEmissione">${numEmissione}</c:param>
		</c:if>
		<c:if test="${!empty tx_tipologia_servizio}">  
			<c:param name="tx_tipologia_servizio">${tx_tipologia_servizio}</c:param>
		</c:if>
		<c:if test="${!empty stato_documento}">  
			<c:param name="stato_documento">${stato_documento}</c:param>
		</c:if>
		<c:if test="${!empty numDocumento}">  
			<c:param name="numDocumento">${numDocumento}</c:param>
		</c:if>
		<c:if test="${!empty ufficioImpositore}">  
			<c:param name="ufficioImpositore">${ufficioImpositore}</c:param>
		</c:if>
		<c:if test="${!empty stato_sospensione}">  
			<c:param name="stato_sospensione">${stato_sospensione}</c:param>
		</c:if>
		<c:if test="${!empty stato_procedure}">
			<c:param name="stato_procedure">${stato_procedure}</c:param> 
		</c:if>
		<c:if test="${!empty denominazione}">
			<c:param name="denominazione">${denominazione}</c:param> 
		</c:if>
		<c:if test="${!empty tipoRic}">
			<c:param name="tipoRic">${tipoRic}</c:param> 
		</c:if>
	</c:url>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">

		<s:form name="ricercaDocumentiForm" action="ricercaDocumenti.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:textbox name="DDLType" label="DDLType" bmodify="true" text="11" cssclass="display_none" cssclasslabel="display_none"  />

			<s:textbox name="r2RowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="r2PageNumber" label="pageNumberRic" bmodify="true" text="${pageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="r2Order" label="orderRic" bmodify="true" text="${order}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="r2Ext" label="r2ext" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none" />
			
			<s:textbox name="ext" label="ext" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none" />

				<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
						Ricerca Documenti
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
					
					<!--   -->
					
					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="impostaServ"
								disable="${ddlImpostaServizioDisabled}" cssclass="tbddlMax floatleft"
								label="Imp.Serv.(I/S):" cssclasslabel="label85 bold textright"
								cachedrowset="listaImpostaServizio" usexml="true"
								valueselected="${impostaServ}">
								<s:ddloption text="Tutti i Servizi" value="" />
								<s:ddloption text="{3}-{4}" value="{1}{2}{3}" />
							</s:dropdownlist>
						</s:div>
						<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
								bmodify="true" name="annoEmissione" label="Anno Emis.:"
								maxlenght="4"
								text="${annoEmissione}"
								cssclass="textareaman"
								cssclasslabel="label85 bold textright" />
						</s:div>
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist  name="stato_documento" disable="false" label="Stato Doc.:" valueselected="${stato_documento}" 
							                 cssclass="tbddlMax floatleft" cssclasslabel="label85 bold textright">
								<s:ddloption value="" text="tutti i documenti"/>
								<s:ddloption value="S" text="Saldato"/>
								<s:ddloption value="P" text="Parzialmente Movimentato"/>
							</s:dropdownlist>
						</s:div>
					</s:div>	
					
					
					
					<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
						<!-- 
						<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="ufficioImpositore"
								disable="${ddlUtenteImpositoreDisabled}" cssclass="tbddlMax floatleft"
								label="Ufficio Impositore:" cssclasslabel="label85 bold textright"
								cachedrowset="listaUfficioImpositore" usexml="true"
								valueselected="${ufficioImpositore}">
								<s:ddloption text="Tutti gli Uffici" value="" />
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
						</s:div>
						 -->
						 <s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="tx_tipologia_servizio" disable="false"
								cssclass="tbddlMax floatleft" label="Tip. Servizio:"
								cssclasslabel="label85 bold textright"
								cachedrowset="listaTipologieServizio" usexml="true"
								onchange="setFiredButton('tx_button_tipo_servizio_changed');this.form.submit();"
								valueselected="${tx_tipologia_servizio}">
								<s:ddloption text="Tutte le tipologie" value="" />
								<s:ddloption text="{1}-{2}" value="{1}_{3}" />
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_tipo_servizio_changed"
									disable="false" onclick="" text=""
									type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
							</noscript>
						</s:div>
						<s:div name="divElement61" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
									bmodify="true" name="numEmissione" label="Numero Emis.:"
									maxlenght="10"
									text="${numEmissione}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
						</s:div>
						
					<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist  name="stato_procedure" disable="false" label="Proc.Esecutive:" valueselected="${stato_procedure}" 
							                 cssclass="tbddlMax floatleft" cssclasslabel="label85 bold textright">
								<s:ddloption value="" text="tutti"/>
								<s:ddloption value="S" text="Sono Presenti"/>
								<s:ddloption value="N" text="Non Sono Presenti"/>
							</s:dropdownlist>
						</s:div>
					</s:div>
					
					<s:div name="divRicercaLeft1" cssclass="divRicMetadatiRight">
						<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
								bmodify="true" name="codFiscale" label="Cod. Fiscale:"
								maxlenght="16"
								text="${codFiscale}"
								cssclass="textareaman"
								cssclasslabel="label85 bold textright" />
						</s:div>
						<s:div name="divNumDocumenti" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
								bmodify="true" name="numDocumento" label="Numero Doc.:"
								maxlenght="20"
								text="${numDocumento}"
								cssclass="textareaman"
								cssclasslabel="label85 bold textright" />
						</s:div>
						<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist  name="stato_sospensione" disable="false" label="Sospensione:" valueselected="${stato_sospensione}" 
							                 cssclass="tbddlMax floatleft" cssclasslabel="label85 bold textright">
								<s:ddloption value="" text="tutti"/>
								<s:ddloption value="S" text="Sono Presenti"/>
								<s:ddloption value="N" text="Non Sono Presenti"/>
							</s:dropdownlist>
						</s:div>
	 			  </s:div>
				</s:div>
	
	
				<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					<s:button id="tx_button_cerca" type="submit" text="Cerca" onclick="" cssclass="btnStyle"/>
					<s:button id="tx_button_reset" type="submit" text="Reset" onclick="" cssclass="btnStyle" />
	 
					<c:if test="${!empty listaDocumenti && ext=='0'}">
						<s:button id="tx_button_cerca_exp" type="submit" text="Espandi" onclick="" cssclass="btnStyle" />
					</c:if>
					<c:if test="${!empty listaDocumenti && ext=='1'}">
						<s:button id="tx_button_cerca_exp" type="submit" text="Contrai" onclick="" cssclass="btnStyle" />
					</c:if>
	
					<s:textbox name="fired_button_hidden" label="fired_button_hidden"
						bmodify="true" text="" cssclass="rend_display_none"
						cssclasslabel="rend_display_none" />
				</s:div>
		
			</s:form>

	</s:div>
</s:div>
	
<!--  Presentazione risultati -->	

<!-- colonne -->	
	
<c:if test="${!empty listaDocumenti}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Lista Documenti
	</s:div>

	<s:div name="divRisultati" cssclass="divRisultati">

		<s:datagrid  viewstate="" cachedrowset="listaDocumenti" action="ricercaDocumenti.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
			<s:action>
				<c:url value="ricercaDocumenti.do" >
					<c:if test="${!empty param.tx_societa}">  
						<c:param name="tx_societa">${param.tx_societa}</c:param>
					</c:if>
					<c:if test="${!empty param.tx_utente}">  
						<c:param name="tx_utente">${param.tx_utente}</c:param>
					</c:if>
					<c:if test="${!empty param.tx_UtenteEnte}">  
						<c:param name="tx_UtenteEnte">${param.tx_UtenteEnte}</c:param>
					</c:if>
					<c:if test="${!empty param.codFiscale}">  
						<c:param name="codFiscale">${param.codFiscale}</c:param>
					</c:if>
					<c:if test="${!empty param.impostaServ}">  
						<c:param name="impostaServ">${param.impostaServ}</c:param>
					</c:if>
					<c:if test="${!empty param.annoEmissione}">  
						<c:param name="annoEmissione">${param.annoEmissione}</c:param>
					</c:if>
					<c:if test="${!empty param.numEmissione}">  
						<c:param name="numEmissione">${param.numEmissione}</c:param>
					</c:if>
					<c:if test="${!empty param.tx_tipologia_servizio}">  
						<c:param name="tx_tipologia_servizio">${param.tx_tipologia_servizio}</c:param>
					</c:if>
					<c:if test="${!empty param.stato_documento}">  
						<c:param name="stato_documento">${param.stato_documento}</c:param>
					</c:if>
					<c:if test="${!empty param.numDocumento}">  
						<c:param name="numDocumento">${param.numDocumento}</c:param>
					</c:if>
					<c:if test="${!empty param.ufficioImpositore}">  
						<c:param name="ufficioImpositore">${param.ufficioImpositore}</c:param>
					</c:if>
					<c:if test="${!empty param.stato_sospensione}">  
						<c:param name="stato_sospensione">${param.stato_sospensione}</c:param>
					</c:if>
					<c:if test="${!empty param.stato_procedure}">
						<c:param name="stato_procedure">${param.stato_procedure}</c:param> 
					</c:if>
					<c:if test="${!empty ext}">
						<c:param name="ext">${ext}</c:param>
					</c:if>
					<c:param name="DDLType">11</c:param>
				</c:url>
			</s:action>
	
			<!-- SOCIETA -->
			<s:dgcolumn index="26"  label="Soc." asc="SOCA" desc="SOCD"></s:dgcolumn>
			
			<!-- UTENTE -->
			<s:dgcolumn index="9"  label="Utente" asc="UTEA" desc="UTED"></s:dgcolumn>
	
			<!-- ENTE -->
			<s:dgcolumn index="10"  label="Ente" asc="ENTA" desc="ENTD"></s:dgcolumn>
	
	  	    <!-- EH1_CISECISE -->
			<s:dgcolumn label="I/S" asc="ISEA" desc="ISED" index="1" ></s:dgcolumn>
	
	  	    <!-- EH1_CEH1ADOC -->
			<s:dgcolumn index="2" label="Anno"  asc="DANA" desc="DAND"></s:dgcolumn>
	
	        <!--  EH1_CEH1NEMIS -->
			<s:dgcolumn index="3" label="Numero" asc="DNMA" desc="DNMD"></s:dgcolumn>
	
			<!-- EH1_TANETUFF,EH1_CANECUFF -->
			<s:dgcolumn  label="Ufficio" asc="UFFA" desc="UFFD">
					<s:if left="{5}{6}" control="eq" right="">
							<s:then>
							</s:then>
							<s:else>
								{5}/{6}
							</s:else>
					</s:if>		    							
				</s:dgcolumn>
	
			<!-- EH1_CTSECTSE -->
			<s:dgcolumn index="7" label="Tip. Serv." asc="CTSA" desc="CTSD" css="text_align_right"></s:dgcolumn>
	
			<!-- DOCUMENTI RENDICONTATI -->
			<s:dgcolumn index="11" label="R" css="text_align_right" asc="FRDA" desc="FRDD"></s:dgcolumn>
	
			<!-- EH1_CEH1CFIS -->
			<s:dgcolumn index="4" label="Cod.Fiscale" asc="CFSA" desc="CFSD"></s:dgcolumn>
			
			<!-- TOT_RATE -->
			<s:dgcolumn index="16" label="RT" asc="NRTA" desc="NRTD" ></s:dgcolumn>
	
			<!-- CARICO -->
			<s:dgcolumn index="12" format="#,##0.00" label="Carico" asc="CARA" desc="CARD" css="text_align_right"></s:dgcolumn>
	
			<!-- DIM. CARICO -->
			<s:dgcolumn index="14" format="#,##0.00" label="Dim.Carico" asc="DCAA" desc="DCAD" css="text_align_right"></s:dgcolumn>
	
			<!-- RISCOSSO -->
			<s:dgcolumn index="13" format="#,##0.00" label="Riscosso" asc="RSCA" desc="RSCD" css="text_align_right"></s:dgcolumn>
	        
			<!-- RIMBORSO -->
			<s:dgcolumn index="15" format="#,##0.00" label="Rimborso" asc="RIMA" desc="RIMD" css="text_align_right"></s:dgcolumn>
	        
			<!-- RENDICONTATO  -->
			<s:dgcolumn index="19" format="#,##0.00" label="Rendicont." asc="RNDA" desc="RNDD" css="text_align_right"></s:dgcolumn>
	
			<!-- RESIDUO -->
			<s:dgcolumn index="20" format="#,##0.00" label="Residuo" asc="RSDA" desc="RSDD" css="text_align_right"></s:dgcolumn>
	
	        <s:ifdatagrid left="${ext}" control="eq" right="1">
				<s:thendatagrid>
					<s:dgcolumn index="27" format="#,##0.00" label="Residuo Scad." asc="RESA" desc="RESD" css="text_align_right"></s:dgcolumn>
				</s:thendatagrid>
			</s:ifdatagrid>
	
		    <s:dgcolumn label="Dett" >
				<s:hyperlink href="documentoDettaglio.do${formParameters}&tx_button_cerca_exp=OK&chiaveIS={1}&chiaveDoc={8}&chiaveTipoUff={5}&chiaveCodUff={6}&chiaveCodUte={9}&chiaveCodEnte={10}&chiaveServ={24}&chiaveTomb={25}&chiaveFlusso={23}&pagPrec=ricercaDocumenti"
				 imagesrc="../applications/templates/shared/img/dettaglio.png"
				 alt="Dettaglio Documento"
				 text=""
				 cssclass="blacklink hlStyle" />
	        </s:dgcolumn>
		    <s:dgcolumn label="Tr" >
				<s:if left="{17}" control="ne" right="0">
				<s:then>
		        {17}
				<s:hyperlink href="ricercaTributi.do${formParameters}&tx_button_cerca_exp=OK&chiaveIS={1}&chiaveDoc={8}&chiaveTipoUff={5}&chiaveCodUff={6}&chiaveCodUte={9}&chiaveCodEnte={10}&chiaveServ={24}&chiaveTomb={25}&chiaveFlusso={23}&pagPrec=ricercaDocumenti"
				 imagesrc="../applications/templates/shared/img/listaDati.png"
				 alt="Lista Tributi"
				 text=""
				 cssclass="blacklink hlStyle" />
				</s:then>
				<s:else>
				&nbsp;&nbsp;
				</s:else>
				</s:if>
	        </s:dgcolumn>
		    <s:dgcolumn label="Sc">
				<s:if left="{16}" control="ne" right="0">
				<s:then>
				{16}
				<s:hyperlink href="ricercaScadenze.do${formParameters}&tx_button_cerca_exp=OK&chiaveIS={1}&chiaveDoc={8}&chiaveTipoUff={5}&chiaveCodUff={6}&chiaveCodUte={9}&chiaveCodEnte={10}&chiaveServ={24}&chiaveTomb={25}&chiaveFlusso={23}&pagPrec=ricercaDocumenti"
				 imagesrc="../applications/templates/shared/img/listaDati.png"
				 alt="Lista Scadenze"
				 text=""
				 cssclass="blacklink hlStyle" />
				</s:then>
				<s:else>
				&nbsp;&nbsp;
				</s:else>
				</s:if>
	        </s:dgcolumn>
		    <s:dgcolumn label="Pg">
				<s:if left="{18}" control="ne" right="0">
				<s:then>
				{18}
				<s:hyperlink href="ricercaPagamentiDocumenti.do${formParameters}&tx_button_cerca_exp=OK&chiaveIS={1}&chiaveDoc={8}&chiaveTipoUff={5}&chiaveCodUff={6}&chiaveCodUte={9}&chiaveCodEnte={10}&chiaveServ={24}&chiaveTomb={25}&chiaveFlusso={23}&pagPrec=ricercaDocumenti"
				 imagesrc="../applications/templates/shared/img/listaDati.png"
				 alt="Lista Pagamenti"
				 text=""
				 cssclass="blacklink hlStyle" />
				</s:then>
				<s:else>
				&nbsp;&nbsp;
				</s:else>
				</s:if>
	        </s:dgcolumn>
	
	    </s:datagrid>
	
		<!-- tabella riassunto	 -->
	
			<s:div name="divDatiAgg" cssclass="divTableTitle bold text_align_right">
				<c:if test="${userProfile=='AMEN'}" >
						 Dati aggiornati al: <s:formatDate pattern="dd/MM/yyyy" value="${dataAggiornamento}"/>
				</c:if>
			</s:div>
	
			 <%--<fmt:formatDate value="${dataUltimoAgg}" pattern="dd/MM/yyyy"/>--%>
		<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
			<s:thead>
				<s:tr cssclass="seda-ui-datagridrowpari">
					<s:th cssclass="seda-ui-datagridheadercell"  icol="8"><b>Totali Documenti</b></s:th>
				</s:tr>
			</s:thead>
			<s:tbody>
				<s:tr cssclass="seda-ui-datagridrowpari">
						<s:td cssclass="seda-ui-datagridcell">Totale Carico</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoCarico}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
						<s:td cssclass="seda-ui-datagridcell">Totale Diminuzione Carico</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoDiminuzioneCarico}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
				</s:tr>
				<s:tr>
						<s:td cssclass="seda-ui-datagridcell">Totale Rendicontato</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoRendiconto}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
						<s:td cssclass="seda-ui-datagridcell">Totale Riscosso</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoRiscosso}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
				
				</s:tr>
				<s:tr cssclass="seda-ui-datagridrowpari">
						<s:td cssclass="seda-ui-datagridcell">Totale Carico (esclusi Rendicontati)</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoFinCarico}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
						<s:td cssclass="seda-ui-datagridcell">Totale Rimborso</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoTotaleRimborso}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
				</s:tr>
				<s:tr cssclass="seda-ui-datagridrowpari">
						<s:td cssclass="seda-ui-datagridcell">Totale Residuo</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoTotaleResiduo}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
						<s:td cssclass="seda-ui-datagridcell">Totale Residuo Scaduto </s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoResiduoScaduto}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
				</s:tr>
	
				
			</s:tbody>
		</s:table>
	
		<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
			<s:thead>
				<s:tr cssclass="seda-ui-datagridrowpari">
					<s:th cssclass="seda-ui-datagridheadercell"  icol="8"><b>Statistiche</b></s:th>
				</s:tr>
			</s:thead>
			<s:tbody>
				<s:tr cssclass="seda-ui-datagridrowpari">
						<s:td cssclass="seda-ui-datagridcell">Riscosso su Carico</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${percRiscossoCarico}" minFractionDigits="2" maxFractionDigits="2" /> %</s:td>
						<s:td cssclass="seda-ui-datagridcell">Sgravato su Carico</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${percSgravatoCarico}" minFractionDigits="2" maxFractionDigits="2"/>  %</s:td>
				</s:tr>
				<s:tr cssclass="seda-ui-datagridrowpari">
						<s:td cssclass="seda-ui-datagridcell">Rendicontato su Carico</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${percRendicontatoCarico}" minFractionDigits="2" maxFractionDigits="2"/>  %</s:td>
						<s:td cssclass="seda-ui-datagridcell">Residuo su Carico</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${percRensiduoCarico}" minFractionDigits="2" maxFractionDigits="2"/>  %</s:td>
				</s:tr>
				<s:tr cssclass="seda-ui-datagridrowpari">	
						<s:td cssclass="seda-ui-datagridcell">Rimborso su Carico</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${percRimborsoCarico}" minFractionDigits="2" maxFractionDigits="2"/>  %</s:td>
						<s:td cssclass="seda-ui-datagridcell">Residuo Scaduto su Carico</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${percResiduoScadCarico}" minFractionDigits="2" maxFractionDigits="2"/>  %</s:td>
				</s:tr>
			</s:tbody>
		</s:table>

	</s:div>

</c:if>

