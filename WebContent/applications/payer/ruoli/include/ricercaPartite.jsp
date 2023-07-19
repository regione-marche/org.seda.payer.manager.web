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
		$("#data_consegna_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_consegna_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_consegna_da_day_id").val(dateText.substr(0,2));
												$("#data_consegna_da_month_id").val(dateText.substr(3,2));
												$("#data_consegna_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_consegna_da_day_id",
				                            "data_consegna_da_month_id",
				                            "data_consegna_da_year_id",
				                            "data_consegna_da_hidden");
			}
		});
		$("#data_consegna_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_consegna_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_consegna_a_day_id").val(dateText.substr(0,2));
												$("#data_consegna_a_month_id").val(dateText.substr(3,2));
												$("#data_consegna_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_consegna_a_day_id",
				                            "data_consegna_a_month_id",
				                            "data_consegna_a_year_id",
				                            "data_consegna_a_hidden");
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
		<c:if test="${!empty annoRuolo}">  
			<c:param name="annoRuolo">${annoRuolo}</c:param>
		</c:if>
		<c:if test="${!empty numRuolo}">  
			<c:param name="numRuolo">${numRuolo}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_da_day}">  
			<c:param name="data_consegna_da_day">${data_consegna_da_day}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_da_month}">  
			<c:param name="data_consegna_da_month">${data_consegna_da_month}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_da_year}">  
			<c:param name="data_consegna_da_year">${data_consegna_da_year}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_a_day}">  
			<c:param name="data_consegna_a_day">${data_consegna_a_day}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_a_month}">  
			<c:param name="data_consegna_a_month">${data_consegna_a_month}</c:param>
		</c:if>
		<c:if test="${!empty data_consegna_a_year}">  
			<c:param name="data_consegna_a_year">${data_consegna_a_year}</c:param>
		</c:if>
		<c:if test="${!empty stato_partita}">  
			<c:param name="stato_partita">${stato_partita}</c:param>
		</c:if>
		<c:if test="${!empty stato_cartella}">  
			<c:param name="stato_cartella">${stato_cartella}</c:param>
		</c:if>
		<c:if test="${!empty codFisc}">  
			<c:param name="codFisc">${codFisc}</c:param>
		</c:if>
		<c:if test="${!empty codCartella}">  
			<c:param name="codCartella">${codCartella}</c:param>
		</c:if>

	</c:url>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">

		<s:form name="ricercaPartiteForm" action="ricercaPartite.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:textbox name="DDLType" label="DDLType" bmodify="true" text="11" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="ext" label="ext" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none" />

			<s:textbox name="hRowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="hPageNumber" label="pageNumberRic" bmodify="true" text="${pageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="hOrder" label="orderRic" bmodify="true" text="${order}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="hExt" label="hExt" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none"  />

				<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
						Ricerca Partite
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
						<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
							<!-- 
							<s:dropdownlist name="ufficioImpositore"
								disable="${ddlUtenteImpositoreDisabled}" cssclass="tbddlMax floatleft"
								label="Ufficio Impositore:" cssclasslabel="label85 bold textright"
								cachedrowset="listaUfficioImpositore" usexml="true"
								valueselected="${ufficioImpositore}">
								<s:ddloption text="Tutti gli Uffici" value="" />
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							 -->
							<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
								<s:textbox validator="ignore"
									bmodify="true" name="annoRuolo" label="Anno Ruolo:"
									maxlenght="4"
									text="${annoRuolo}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
							</s:div>
						</s:div>
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist  name="stato_partita" disable="false" label="Stato Partita:" valueselected="${stato_partita}" 
							                 cssclass="tbddlMax floatleft" cssclasslabel="label85 bold textright">
								<s:ddloption value="" text="tutti gli stati partita"/>
								<s:ddloption value="S" text="Saldato"/>
								<s:ddloption value="P" text="Parzialmente Movimentato"/>
							</s:dropdownlist>
						</s:div>
						<s:div name="divElement5" cssclass="divRicMetadatiDoubleRow">
							<s:div name="divElement5a" cssclass="labelData">
								<s:label name="label_data_consegna" cssclass="seda-ui-label label85 bold textright" text="Data Consegna" />
							</s:div>
							
							<s:div name="divElement5b" cssclass="floatleft">
								<s:div name="divDataConsegnaDa" cssclass="divDataDa">
									<s:date label="Da:" prefix="data_consegna_da" yearbegin="${ddlDateAnnoDa}"
										yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
										separator="/" calendar="${data_consegna_da}"
										cssclasslabel="labelsmall"
										cssclass="dateman">
									</s:date>
									<input type="hidden" id="data_consegna_da_hidden" value="" />
								</s:div>
								
								<s:div name="divDataConsegnaA" cssclass="divDataA">
									<s:date label="A:" prefix="data_consegna_a" yearbegin="${ddlDateAnnoDa}"
										yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
										separator="/" calendar="${data_consegna_a}"
										cssclasslabel="labelsmall"
										cssclass="dateman" >
									</s:date>
									<input type="hidden" id="data_consegna_a_hidden" value="" />
								</s:div>
							</s:div>
						</s:div>
						
					</s:div>	
					
					<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
						<s:div name="divElement61" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
									bmodify="true" name="numRuolo" label="Num. Ruolo:"
									maxlenght="10"
									text="${numRuolo}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
						</s:div>
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist  name="stato_cartella" disable="false" label="Stato Cartella:" valueselected="${stato_cartella}" 
							                 cssclass="tbddlMax floatleft" cssclasslabel="label85 bold textright">
								<s:ddloption value="" text="tutti gli stati partita"/>
								<s:ddloption value="S" text="Partita Cartellata"/>
								<s:ddloption value="N" text="Partita non Cartellata"/>
							</s:dropdownlist>
						</s:div>
						
					</s:div>
					<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
						<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
									bmodify="true" name="codFisc" label="Cod. Fiscale:"
									maxlenght="16"
									text="${codFisc}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
						</s:div>
						<s:div name="divElement72" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
									bmodify="true" name="codCartella" label="Cartella:"
									maxlenght="20"
									text="${codCartella}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
						</s:div>
	
	 			  </s:div>
				</s:div>
	
				<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					<s:button id="tx_button_cerca" type="submit" text="Cerca" onclick="" cssclass="btnStyle"/>
					<s:button id="tx_button_reset" type="submit" text="Reset" onclick="" cssclass="btnStyle" />

					<c:if test="${!empty listaPartite && ext=='0'}">
						<s:button id="tx_button_cerca_exp" type="submit" text="Espandi" onclick="" cssclass="btnStyle" />
					</c:if>
					<c:if test="${!empty listaPartite && ext=='1'}">
						<s:button id="tx_button_cerca_exp" type="submit" text="Contrai" onclick="" cssclass="btnStyle" />
					</c:if>

					<s:textbox name="fired_button_hidden" label="fired_button_hidden"
						bmodify="true" text="" cssclass="rend_display_none"
						cssclasslabel="rend_display_none" />
				</s:div>
		
		</s:form>

	</s:div>
</s:div>
	
<!-- Presentazione risultato -->	

<!-- colonne -->	
	
<c:if test="${!empty listaPartite}">
	<fmt:setLocale value="it_IT"/>

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Lista Partite
	</s:div>

	<s:div name="divRisultati" cssclass="divRisultati">

		<s:datagrid  viewstate="" cachedrowset="listaPartite" action="ricercaPartite.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
			<s:action>
				<c:url value="ricercaPartite.do" >
			<c:if test="${!empty ext}">
				<c:param name="ext">${ext}</c:param>
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
			<c:if test="${!empty annoRuolo}">  
				<c:param name="annoRuolo">${annoRuolo}</c:param>
			</c:if>
			<c:if test="${!empty numRuolo}">  
				<c:param name="numRuolo">${numRuolo}</c:param>
			</c:if>
			<c:if test="${!empty ufficioImpositore}">  
				<c:param name="ufficioImpositore">${ufficioImpositore}</c:param>
			</c:if>
			<c:if test="${!empty data_consegna_da_day}">  
				<c:param name="data_consegna_da_day">${data_consegna_da_day}</c:param>
			</c:if>
			<c:if test="${!empty data_consegna_da_month}">  
				<c:param name="data_consegna_da_month">${data_consegna_da_month}</c:param>
			</c:if>
			<c:if test="${!empty data_consegna_da_year}">  
				<c:param name="data_consegna_da_year">${data_consegna_da_year}</c:param>
			</c:if>
			<c:if test="${!empty data_consegna_a_day}">  
				<c:param name="data_consegna_a_day">${data_consegna_a_day}</c:param>
			</c:if>
			<c:if test="${!empty data_consegna_a_month}">  
				<c:param name="data_consegna_a_month">${data_consegna_a_month}</c:param>
			</c:if>
			<c:if test="${!empty data_consegna_a_year}">  
				<c:param name="data_consegna_a_year">${data_consegna_a_year}</c:param>
			</c:if>
			<c:if test="${!empty stato_partita}">  
				<c:param name="stato_partita">${stato_partita}</c:param>
			</c:if>
			<c:if test="${!empty stato_cartella}">  
				<c:param name="stato_cartella">${stato_cartella}</c:param>
			</c:if>
			<c:if test="${!empty codFisc}">  
				<c:param name="codFisc">${codFisc}</c:param>
			</c:if>
			<c:if test="${!empty codCartella}">  
				<c:param name="codCartella">${codCartella}</c:param>
			</c:if>
			
			<c:param name="DDLType">11</c:param>
			</c:url>
			</s:action>
	
	
			<!-- SOCIETA -->
			<s:dgcolumn index="1"  label="Societ&agrave;" asc="SOCA" desc="SOCD"></s:dgcolumn>
			
			<!-- UTENTE -->
			<s:dgcolumn index="2"  label="Utente" asc="UTEA" desc="UTED"></s:dgcolumn>
	
			<!-- ENTE -->
			<s:dgcolumn index="3"  label="Ente" asc="ENTA" desc="ENTD"></s:dgcolumn>
	
			<!-- RAR_CRARCCAR -->
			<s:dgcolumn index="11"  label="Nr Cartella" asc="DNPA" desc="DNPD"></s:dgcolumn>
	
	  	    <!-- RPA_NRPAANNO -->
			<s:dgcolumn index="4" label="Anno"  asc="DANA" desc="DAND"></s:dgcolumn>
	
	        <!--  RPA_NRPANUME -->
			<s:dgcolumn index="5" label="Num" asc="DNMA" desc="DNMD"></s:dgcolumn>
	
			<!-- RRU_GRRUGRUO -->
			<s:dgcolumn index="23"  format="dd/MM/yyyy" label="Data Consegna" asc="DTCA" desc="DTCD"></s:dgcolumn>
			
			<!-- RPA_CRPATUFF,RPA_CRPACUFF -->
			<s:dgcolumn  label="Uff" asc="UFFA" desc="UFFD">
					{6}/{7}
				</s:dgcolumn>
			<!-- RPA_CRPACFIS -->
			<s:dgcolumn index="8" label="Cod. Fisc." asc="CFSA" desc="CFSD"></s:dgcolumn>
			<!-- RPA_CRPACIDE -->
			<s:dgcolumn index="10" label="Partita" ></s:dgcolumn>
			<!-- CARICO -->
			<s:dgcolumn index="12" format="#,##0.00" label="Carico" asc="CARA" desc="CARD" css="text_align_right"></s:dgcolumn>
	
			<!-- DIM. CARICO -->
			<s:dgcolumn index="13" format="#,##0.00" label="Dim.Carico" asc="DCAA" desc="DCAD" css="text_align_right"></s:dgcolumn>
	
			<!-- RISCOSSO -->
			<s:dgcolumn index="14" format="#,##0.00" label="Riscosso" asc="RSCA" desc="RSCD" css="text_align_right"></s:dgcolumn>
	        
	        
	        <s:ifdatagrid left="${ext}" control="eq" right="1">
				<s:thendatagrid>
					<!-- RIMBORSO -->
					<s:dgcolumn index="15" format="#,##0.00" label="Rimborso" asc="RIMA" desc="RIMD" css="text_align_right"></s:dgcolumn>

					<!-- RESIDUO asc="RSDA" desc="RSDD"-->
					<s:dgcolumn index="16" format="#,##0.00" label="Residuo" asc="RSDA" desc="RSDD" css="text_align_right"></s:dgcolumn>

				</s:thendatagrid>
			</s:ifdatagrid>
	        	
		    <s:dgcolumn label="Dett." >
				<s:hyperlink href="dettaglioPartita.do${formParameters}&tx_button_cerca_exp=OK&chiaveNumPar={9}&chiaveConcPar={20}&chiaveTombPar={21}&chiaveFlusso={17}&chiaveCodSoc={1}&chiaveCodUte={2}&chiaveCodEnte={22}&chiaveAnnoRuo={4}&chiaveNumRuo={5}&pagPrec=ricercaPartite" 
				alt="Dettaglio Partita" 
				imagesrc="../applications/templates/shared/img/dettaglio.png"			
				text="" 
				cssclass="blacklink hlStyle" />
	        </s:dgcolumn>
		    <s:dgcolumn label="Art." >
				{18}&nbsp;
				<s:hyperlink href="ricercaArticoli.do${formParameters}&tx_button_cerca_exp=OK&chiaveNumPar={9}&chiaveConcPar={20}&chiaveTombPar={21}&chiaveFlusso={17}&chiaveCodSoc={1}&chiaveCodUte={2}&chiaveCodEnte={22}&chiaveAnnoRuo={4}&chiaveNumRuo={5}&pagPrec=ricercaPartite" 
				alt="Lista Articoli" 
				imagesrc="../applications/templates/shared/img/listaDati.png"
				text="" 
				cssclass="blacklink hlStyle" />
	        </s:dgcolumn>
		    <s:dgcolumn label="Pag." >
				<s:if left="{19}" control="eq" right="0">
					<s:then>
					&nbsp;&nbsp;
					</s:then>
					<s:else>
						{19}&nbsp;
						<s:hyperlink href="ricercaPagamentiDett.do${formParameters}&tx_button_cerca_exp=OK&chiaveNumPar={9}&chiaveConcPar={20}&chiaveTombPar={21}&chiaveFlusso={17}&chiaveCodSoc={1}&chiaveCodUte={2}&chiaveCodEnte={22}&chiaveAnnoRuo={4}&chiaveNumRuo={5}&pagPrec=ricercaPartite" 
						alt="Lista Pagamenti" 
						imagesrc="../applications/templates/shared/img/listaDati.png"
						text="" 
						cssclass="blacklink hlStyle" />
					</s:else>
				</s:if>
	        </s:dgcolumn>
	    </s:datagrid>
		<!-- 	tabella riassunto -->
	
	
			 <%--<fmt:formatDate value="${dataUltimoAgg}" pattern="dd/MM/yyyy"/>--%>
		<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
			<s:thead>
				<s:tr cssclass="seda-ui-datagridrowpari">
					<s:th cssclass="seda-ui-datagridheadercell"  icol="8"><b>Totali Partite</b></s:th>
				</s:tr>
			</s:thead>
			<s:tbody>
				<s:tr cssclass="seda-ui-datagridrowpari">
						<s:td cssclass="seda-ui-datagridcell">Carico</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoCarico}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
						<s:td cssclass="seda-ui-datagridcell">Riscosso</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoRiscosso}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
	
				</s:tr>
				<s:tr>
						<s:td cssclass="seda-ui-datagridcell">Diminuzione Carico</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoDiminuzioneCarico}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
						<s:td cssclass="seda-ui-datagridcell">Rimborso</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoTotaleRimborso}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
				</s:tr>
				<s:tr cssclass="seda-ui-datagridrowpari">
						<s:td cssclass="seda-ui-datagridcell">Residuo</s:td>
						<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImportoTotaleResiduo}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
						<s:td cssclass="seda-ui-datagridcell"></s:td>
						<s:td cssclass="seda-ui-datagridcell"></s:td>
				</s:tr>
			</s:tbody>
		</s:table>
	
	</s:div>
	<s:div name="divDatiAgg" cssclass="divTableTitle bold text_align_right">
		<c:if test="${userProfile=='AMEN'}" >
				 Dati aggiornati al: <s:formatDate pattern="dd/MM/yyyy" value="${dataAggiornamento}"/>
		</c:if>
	</s:div>
</c:if>


			  