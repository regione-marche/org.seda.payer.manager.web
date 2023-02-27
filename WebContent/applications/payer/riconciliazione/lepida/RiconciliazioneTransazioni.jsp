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
		$("#data_creazione_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_creazione_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_creazione_da_day_id").val(dateText.substr(0,2));
												$("#data_creazione_da_month_id").val(dateText.substr(3,2));
												$("#data_creazione_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_creazione_da_day_id",
				                            "data_creazione_da_month_id",
				                            "data_creazione_da_year_id",
				                            "data_creazione_da_hidden");
			}
		});
		$("#data_creazione_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_creazione_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_creazione_a_day_id").val(dateText.substr(0,2));
												$("#data_creazione_a_month_id").val(dateText.substr(3,2));
												$("#data_creazione_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_creazione_a_day_id",
				                            "data_creazione_a_month_id",
				                            "data_creazione_a_year_id",
				                            "data_creazione_a_hidden");
			}
		});
		$("#data_valuta_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_valuta_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_valuta_da_day_id").val(dateText.substr(0,2));
												$("#data_valuta_da_month_id").val(dateText.substr(3,2));
												$("#data_valuta_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_valuta_da_day_id",
				                            "data_valuta_da_month_id",
				                            "data_valuta_da_year_id",
				                            "data_valuta_da_hidden");
			}
		});
		$("#data_valuta_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_valuta_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_valuta_a_day_id").val(dateText.substr(0,2));
												$("#data_valuta_a_month_id").val(dateText.substr(3,2));
												$("#data_valuta_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_valuta_a_day_id",
				                            "data_valuta_a_month_id",
				                            "data_valuta_a_year_id",
				                            "data_valuta_a_hidden");
			}
		});
		$("#data_contabile_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_contabile_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_contabile_da_day_id").val(dateText.substr(0,2));
												$("#data_contabile_da_month_id").val(dateText.substr(3,2));
												$("#data_contabile_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_contabile_da_day_id",
				                            "data_contabile_da_month_id",
				                            "data_contabile_da_year_id",
				                            "data_contabile_da_hidden");
			}
		});
		$("#data_contabile_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_contabile_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_contabile_a_day_id").val(dateText.substr(0,2));
												$("#data_contabile_a_month_id").val(dateText.substr(3,2));
												$("#data_contabile_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_contabile_a_day_id",
				                            "data_contabile_a_month_id",
				                            "data_contabile_a_year_id",
				                            "data_contabile_a_hidden");
			}
		});
	});

	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}

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

		/*
		function switchVisibility() {     
		   var rule =  getCSSRule('.display_none');
		   if (rule.style.display == 'none') {
			   rule.style.display = 'inline';
		   } else {
		   	rule.style.display = 'none';
		   }
		}   
		*/                                
</script>

	<c:url value="" var="formParameters">
		<c:if test="${!empty param.pageNumber}">  
			<c:param name="pageNumber_hidden">${param.pageNumber}</c:param>
		</c:if>
		<c:if test="${!empty rowsPerPage}">  
			<c:param name="rowsPerPage_hidden">${param.rowsPerPage}</c:param>
		</c:if>
		<c:if test="${!empty orderBy}">  
			<c:param name="orderBy_hidden">${param.orderBy}</c:param>
		</c:if>
		<c:if test="${!empty param.chiaveMovimento}">  
			<c:param name="chiaveMovimento">${param.chiaveMovimento}</c:param>
		</c:if>
		<c:if test="${!empty param.tx_societa}">  
			<c:param name="tx_societa">${param.tx_societa}</c:param>
		</c:if>
		<c:if test="${!empty param.tx_provincia}">  
			<c:param name="tx_provincia">${param.tx_provincia}</c:param>
		</c:if>
		<c:if test="${!empty param.tx_utente}">  
			<c:param name="tx_utente">${param.tx_utente}</c:param>
		</c:if>
		<c:if test="${!empty param.mostraMovimenti}">  
			<c:param name="mostraMovimenti">${param.mostraMovimenti}</c:param>
		</c:if>
		<c:if test="${!empty param.tx_tipo_carta}">  
			<c:param name="tx_tipo_carta">${param.tx_tipo_carta}</c:param>
		</c:if>
		<c:if test="${!empty param.codiceAbi}">  
			<c:param name="codiceAbi">${param.codiceAbi}</c:param>
		</c:if>
		<c:if test="${!empty param.codiceCab}">  
			<c:param name="codiceCab">${param.codiceCab}</c:param>
		</c:if>
		<c:if test="${!empty param.codiceSia}">  
			<c:param name="codiceSia">${param.codiceSia}</c:param>
		</c:if>
		<c:if test="${!empty param.cro}">  
			<c:param name="cro">${param.cro}</c:param>
		</c:if>
		<c:if test="${!empty param.ccb}">  
			<c:param name="ccb">${param.ccb}</c:param>
		</c:if>
		<c:if test="${!empty param.nomeSupporto}">  
			<c:param name="nomeSupporto">${param.nomeSupporto}</c:param>
		</c:if>
		<c:if test="${!empty param.importo_movimento_da}">  
			<c:param name="importo_movimento_da">${param.importo_movimento_da}</c:param>
		</c:if>
		<c:if test="${!empty param.importo_movimento_a}">  
			<c:param name="importo_movimento_a">${param.importo_movimento_a}</c:param>
		</c:if>
		<c:if test="${!empty param.data_creazione_da_day}">  
			<c:param name="data_creazione_da_day">${param.data_creazione_da_day}</c:param>
		</c:if>
		<c:if test="${!empty param.data_creazione_da_month}">  
			<c:param name="data_creazione_da_month">${param.data_creazione_da_month}</c:param>
		</c:if>
		<c:if test="${!empty param.data_creazione_da_year}">  
			<c:param name="data_creazione_da_year">${param.data_creazione_da_year}</c:param>
		</c:if>
		<c:if test="${!empty param.data_creazione_a_day}">  
			<c:param name="data_creazione_a_day">${data_creazione_a_day}</c:param>
		</c:if>
		<c:if test="${!empty param.data_creazione_a_month}">  
			<c:param name="data_creazione_a_month">${param.data_creazione_a_month}</c:param>
		</c:if>
		<c:if test="${!empty param.data_creazione_a_year}">  
			<c:param name="data_creazione_a_year">${param.data_creazione_a_year}</c:param>
		</c:if>
		<c:if test="${!empty param.data_valuta_da_day}">  
			<c:param name="data_valuta_da_day">${param.data_valuta_da_day}</c:param>
		</c:if>
		<c:if test="${!empty param.data_valuta_da_month}">  
			<c:param name="data_valuta_da_month">${param.data_valuta_da_month}</c:param>
		</c:if>
		<c:if test="${!empty param.data_valuta_da_year}">  
			<c:param name="data_valuta_da_year">${param.data_valuta_da_year}</c:param>
		</c:if>
		<c:if test="${!empty param.data_valuta_a_day}">  
			<c:param name="data_valuta_a_day">${data_valuta_a_day}</c:param>
		</c:if>
		<c:if test="${!empty param.data_valuta_a_month}">  
			<c:param name="data_valuta_a_month">${param.data_valuta_a_month}</c:param>
		</c:if>
		<c:if test="${!empty param.data_valuta_a_year}">  
			<c:param name="data_valuta_a_year">${param.data_valuta_a_year}</c:param>
		</c:if>
		<c:if test="${!empty param.data_contabile_da_day}">  
			<c:param name="data_contabile_da_day">${param.data_contabile_da_day}</c:param>
		</c:if>
		<c:if test="${!empty param.data_contabile_da_month}">  
			<c:param name="data_contabile_da_month">${param.data_contabile_da_month}</c:param>
		</c:if>
		<c:if test="${!empty param.data_contabile_da_year}">  
			<c:param name="data_contabile_da_year">${param.data_contabile_da_year}</c:param>
		</c:if>
		<c:if test="${!empty param.data_contabile_a_day}">  
			<c:param name="data_contabile_a_day">${data_contabile_a_day}</c:param>
		</c:if>
		<c:if test="${!empty param.data_contabile_a_month}">  
			<c:param name="data_contabile_a_month">${param.data_contabile_a_month}</c:param>
		</c:if>
		<c:if test="${!empty param.data_contabile_a_year}">  
			<c:param name="data_contabile_a_year">${param.data_contabile_a_year}</c:param>
		</c:if>
		<c:if test="${!empty ext}">
			<c:param name="ext">${ext}</c:param>
		</c:if>
	</c:url>

	
	<s:div name="divSelezione1" cssclass="divSelezione" >
	  <s:div name="divRicercaTopName" cssclass="divRicercaTop">
		 <s:form name="riconciliazioneTransazioniForm" action="riconciliazioneTransazioni.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:textbox name="DDLChanged" label="DDLChanged" bmodify="true" text="" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="ext" label="ext" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none" />
<!-- mod mp -->
			<s:textbox name="multiGTW" label="multiGTW" bmodify="true" text="1" cssclass="display_none" cssclasslabel="display_none"  />
<!-- fine mod mp -->

<!--  lep cro -->
			<s:textbox name="cro" label="cro" bmodify="true" text="" cssclass="display_none" cssclasslabel="display_none" />

			<s:textbox name="hRowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="hPageNumber" label="pageNumberRic" bmodify="true" text="${pageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="hOrder" label="orderRic" bmodify="true" text="${order}" cssclass="display_none" cssclasslabel="display_none"  />
						
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
				Ricerca Movimenti Banca
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:dropdownlist name="tx_societa" disable="${societaDdlDisabled}" cssclass="tbddl floatleft" 
										label="Societ&aacute;:" cssclasslabel="label85 bold floatleft textright"
										cachedrowset="listaSocieta" usexml="true" 
										onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
										valueselected="${tx_societa}">
							<s:ddloption text="Tutte le Societ&agrave;" value=""/>
							<s:ddloption text="{2}" value="{1}"/>
						</s:dropdownlist>
<!-- onchange="setDdl('societa'); callSubmit(this.form);" -->
						<noscript>
							<s:button id="tx_button_societa_changed"  type="submit" 
							disable="${societaDdlDisabled}" text="" onclick="" 
							cssclass="btnimgStyle" title="Aggiorna" validate="false" />
						</noscript>
					</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
						<s:dropdownlist name="tx_provincia" disable="${provinciaDdlDisabled}" cssclass="tbddl floatleft" 
										label="Provincia:" cssclasslabel="label65 bold floatleft textright"
										cachedrowset="listaProvince" usexml="true" 
										onchange="setFiredButton('tx_button_provincia_changed');this.form.submit();"
										valueselected="${tx_provincia}">
							<s:ddloption text="Tutte le Province" value=""/>
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
<!-- onchange="setDdl('provincia'); callSubmit(this.form);" -->
						<noscript>
							<s:button id="tx_button_provincia_changed"  type="submit" text="" onclick="" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
						</noscript>
					</s:div>
					
					<!-- 
					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:dropdownlist name="tx_utente" disable="${utenteDdlDisabled}" cssclass="tbddlMax floatleft" 
												label="Utente:" cssclasslabel="label65 bold textright"
												cachedrowset="listaUtenti" usexml="true" 
												onchange="setFiredButton('tx_button_utente_changed');this.form.submit();"
												valueselected="${tx_utente}">
							<s:ddloption text="Tutti gli Utenti" value=""/>
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_utente_changed"  type="submit" text="" onclick="" cssclass="btnimgStyle" title="Aggiorna" />
						</noscript>
					</s:div>
					 -->
				</s:div>
				
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox  validator="ignore;digits;minlength=5"
									maxlenght="5"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright"
									bmodify="true" name="codiceAbi" 
									label="ABI:" text="${codiceAbi}"/>
					</s:div>

					<s:div name="divElement40" cssclass="divRicMetadatiSingleRow">
						<s:textbox  validator="ignore;digits;minlength=5"
									maxlenght="5"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright"
									bmodify="true" name="codiceCab" 
									label="CAB:" text="${codiceCab}"/>
					</s:div>
					
					<!--  lep cro -->
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:textbox 	validator="ignore;minlength=5"
									maxlenght="5"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright"
									bmodify="true" 
									name="codiceSia" label="SIA:" text="${codiceSia}"/>
					</s:div>
					
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:textbox 	validator="ignore;digits"
									maxlenght="12"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright"
									bmodify="true" 
									name="ccb" label="CCB:" text="${ccb}"/>
					</s:div>
					
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="mostraMovimenti" 
										cssclass="tbddl floatleft"
										cssclasslabel="label85 bold floatleft textright"
										disable="false" 
										label="Mostra:" 
										valueselected="${mostraMovimenti}" >
							<s:ddloption value="" text="Tutti i movimenti banca"/>
							<s:ddloption value="C" text="Movimenti banca riconciliati"/>
							<s:ddloption value="A" text="Movimenti banca non riconciliati"/>
						</s:dropdownlist>
					</s:div>
					
				</s:div>
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<!-- 
					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_tipo_carta" 
										disable="false" 
										cssclass="tbddlMax floatleft" 
										label="Tipo Carta:" 
										cssclasslabel="label85 bold textright"
										cachedrowset="listaGateway" usexml="true" 
										valueselected="${tx_tipo_carta}">
							<s:ddloption text="Tutti tipi di carte" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>	
					</s:div>
					 -->
					<s:div name="divElement10" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement10a" cssclass="labelData">
							<s:label name="label_importo" cssclass="seda-ui-label label85 bold textright"
								text="Importo" />
						</s:div>
						<s:div name="divElement10b" cssclass="floatleft">
							<s:div name="divImportoDa" cssclass="divDataDa">
								<s:textbox validator="ignore;numberIT;maxlength=15"
								        maxlenght="15" 
										bmodify="true"
										name="importo_movimento_da" 
										label="Da:" 
										text="${importo_movimento_da}"
										cssclass="textareamanImporto"
										cssclasslabel="labelsmall" />
							</s:div>
							
							<s:div name="divImportoA" cssclass="divDataA">
								<s:textbox validator="ignore;numberIT;maxlength=15" 
										maxlenght="15"
										bmodify="true" 
										name="importo_movimento_a" 
										label="A:" 
										text="${importo_movimento_a}"
										cssclass="textareamanImporto" 
										cssclasslabel="labelsmall" />
							</s:div>
						</s:div>
					</s:div>
					
					
					<s:div name="divElement12" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement12a" cssclass="labelData">
							<s:label name="label_data_creazione" 
								cssclass="seda-ui-label label78 bold textright" 
								text="Data Creazione"/>
						</s:div>
						
						<s:div name="divElement12b" cssclass="floatleft">
							<s:div name="divDataDa_cre" cssclass="divDataDa">
								<s:date label="Da:" 
									cssclasslabel="labelsmall"
									cssclass="dateman"
									prefix="data_creazione_da" 
									yearbegin="${ddlDateAnnoDa}" 
									yearend="${ddlDateAnnoA}" locale="IT-it"
								 	descriptivemonth="false" 
								 	separator="/" calendar="${data_creazione_da}" >
							 	</s:date>
								<input type="hidden" id="data_creazione_da_hidden" value="" />
							</s:div>
						
							<s:div name="divDataA_cre" cssclass="divDataA">
								<s:date label="A:" 
									cssclasslabel="labelsmall"
									cssclass="dateman"
									prefix="data_creazione_a" 
									yearbegin="${ddlDateAnnoDa}" 
									yearend="${ddlDateAnnoA}" locale="IT-it"
								 	descriptivemonth="false" 
								 	separator="/" calendar="${data_creazione_a}"></s:date>
								<input type="hidden" id="data_creazione_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
					
					<s:div name="divElement14b" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;digits;maxlength=20" bmodify="true"
							name="chiaveMovimento" label="Id Quadrat.:" text="${chiaveMovimento}"
							maxlenght="20"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright" />
					</s:div>
					
					
					
				</s:div>
				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement14" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="maxlength=20"
								maxlenght="20" 
								cssclass="textareaman"
								cssclasslabel="label85 bold textright"
								bmodify="true" 
								name="nomeSupporto" 
								label="Supporto:" 
								text="${nomeSupporto}"/>
					</s:div>
					
					<s:div name="divElement15" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement15a" cssclass="labelData">
							<s:label name="label_data_valuta" 
								cssclass="seda-ui-label label85 bold textright" 
								text="Data Valuta"/>
						</s:div>
						<s:div name="divElement15b" cssclass="floatleft">
							<s:div name="divDataDa_val" cssclass="divDataDa">
								<s:date label="Da:" prefix="data_valuta_da" 
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearbegin="${ddlDateAnnoDa}" 
									yearend="${ddlDateAnnoA}" locale="IT-it"
								 	descriptivemonth="false" 
								 	separator="/" calendar="${data_valuta_da}" ></s:date>
								<input type="hidden" id="data_valuta_da_hidden" value="" />
							</s:div>
							<s:div name="divDataA_val" cssclass="divDataA">
								<s:date label="A:" prefix="data_valuta_a" 
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}" locale="IT-it"
								 	descriptivemonth="false" 
								 	separator="/" calendar="${data_valuta_a}"></s:date>
								<input type="hidden" id="data_valuta_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
					
					
					<s:div name="divElement17" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement17a" cssclass="labelData">
							<s:label name="label_data_contabile" 
								cssclass="seda-ui-label label85 bold textright" 
								text="Data Contabile"/>
						</s:div>
						<s:div name="divElement17b" cssclass="floatleft">
							<s:div name="divDataDa_cont" cssclass="divDataDa">
								<s:date label="Da:" prefix="data_contabile_da" 
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}" locale="IT-it"
								 	descriptivemonth="false" 
								 	separator="/" calendar="${data_contabile_da}" ></s:date>
								<input type="hidden" id="data_contabile_da_hidden" value="" />
							</s:div>
							<s:div name="divDataA_cont" cssclass="divDataA">
								<s:date label="A:" prefix="data_contabile_a" 
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}" locale="IT-it"
								 	descriptivemonth="false" separator="/" calendar="${data_contabile_a}"></s:date>
								<input type="hidden" id="data_contabile_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
				</s:div>
				
			</s:div>
	
			<s:div name="divCentered0" cssclass="divRicBottoni">

				<s:button id="tx_button_cerca" type="submit" text="Cerca" onclick="" cssclass="btnStyle"/>
				<s:button id="tx_button_reset" type="submit" text="Reset" onclick="" cssclass="btnStyle" />
				
				<c:if test="${!empty listaMovimenti}">
						<s:button id="tx_button_stampa" type="submit" text="Stampa" onclick="" cssclass="btnStyle"/>
				</c:if>
				
				<c:if test="${!empty listaMovimenti && ext=='0'}">
					<s:button id="tx_button_cerca_exp" type="submit" text="Espandi" onclick="" cssclass="btnStyle" />
				</c:if>
				<c:if test="${!empty listaMovimenti && ext=='1'}">
					<s:button id="tx_button_cerca_exp" type="submit" text="Contrai" onclick="" cssclass="btnStyle" />
				</c:if>
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
			</s:div>
	 	</s:form>
	  </s:div>
	 
	</s:div>

<c:if test="${!empty listaMovimenti}">
	<fmt:setLocale value="it_IT"/>
	<s:div name="divCentered" cssclass="divRicercaFill bold">
		Elenco Movimenti Banca
		<!--<s:table border="0" cellpadding="0" cellspacing="0">
			<s:thead>
				<s:tr><s:th>Elenco Flussi Pagamenti</s:th></s:tr>
			</s:thead>
			<s:tbody><s:tr><s:td></s:td></s:tr></s:tbody>
		</s:table>-->
	</s:div>
	
	
	<s:datagrid  viewstate="" cachedrowset="listaMovimenti" action="riconciliazioneTransazioni.do" border="1" usexml="true"  rowperpage="${applicationScope.rowsPerPage}">
		<s:action>
			<c:url value="riconciliazioneTransazioni.do">
				
					<c:if test="${!empty param.chiaveMovimento}">  
						<c:param name="chiaveMovimento">${param.chiaveMovimento}</c:param>
					</c:if>
					<c:if test="${!empty param.tx_societa}">  
						<c:param name="tx_societa">${param.tx_societa}</c:param>
					</c:if>
					<c:if test="${!empty param.tx_provincia}">  
						<c:param name="tx_provincia">${param.tx_provincia}</c:param>
					</c:if>
					<c:if test="${!empty param.tx_utente}">  
						<c:param name="tx_utente">${param.tx_utente}</c:param>
					</c:if>
					<c:if test="${!empty param.mostraMovimenti}">  
						<c:param name="mostraMovimenti">${param.mostraMovimenti}</c:param>
					</c:if>
					<c:if test="${!empty param.tx_tipo_carta}">  
						<c:param name="tx_tipo_carta">${param.tx_tipo_carta}</c:param>
					</c:if>
					<c:if test="${!empty param.codiceAbi}">  
						<c:param name="codiceAbi">${param.codiceAbi}</c:param>
					</c:if>
					<c:if test="${!empty param.codiceCab}">  
						<c:param name="codiceCab">${param.codiceCab}</c:param>
					</c:if>
					<c:if test="${!empty param.codiceSia}">  
						<c:param name="codiceSia">${param.codiceSia}</c:param>
					</c:if>
					<c:if test="${!empty param.cro}">  
						<c:param name="cro">${param.cro}</c:param>
					</c:if>
					<c:if test="${!empty param.ccb}">  
						<c:param name="ccb">${param.ccb}</c:param>
					</c:if>
					<c:if test="${!empty param.nomeSupporto}">  
						<c:param name="nomeSupporto">${param.nomeSupporto}</c:param>
					</c:if>
					<c:if test="${!empty param.importo_movimento_da}">  
						<c:param name="importo_movimento_da">${param.importo_movimento_da}</c:param>
					</c:if>
					<c:if test="${!empty param.importo_movimento_a}">  
						<c:param name="importo_movimento_a">${param.importo_movimento_a}</c:param>
					</c:if>
					<c:if test="${!empty param.data_creazione_da_day}">  
						<c:param name="data_creazione_da_day">${param.data_creazione_da_day}</c:param>
					</c:if>
					<c:if test="${!empty param.data_creazione_da_month}">  
						<c:param name="data_creazione_da_month">${param.data_creazione_da_month}</c:param>
					</c:if>
					<c:if test="${!empty param.data_creazione_da_year}">  
						<c:param name="data_creazione_da_year">${param.data_creazione_da_year}</c:param>
					</c:if>
					<c:if test="${!empty param.data_creazione_a_day}">  
						<c:param name="data_creazione_a_day">${data_creazione_a_day}</c:param>
					</c:if>
					<c:if test="${!empty param.data_creazione_a_month}">  
						<c:param name="data_creazione_a_month">${param.data_creazione_a_month}</c:param>
					</c:if>
					<c:if test="${!empty param.data_creazione_a_year}">  
						<c:param name="data_creazione_a_year">${param.data_creazione_a_year}</c:param>
					</c:if>
					<c:if test="${!empty param.data_valuta_da_day}">  
						<c:param name="data_valuta_da_day">${param.data_valuta_da_day}</c:param>
					</c:if>
					<c:if test="${!empty param.data_valuta_da_month}">  
						<c:param name="data_valuta_da_month">${param.data_valuta_da_month}</c:param>
					</c:if>
					<c:if test="${!empty param.data_valuta_da_year}">  
						<c:param name="data_valuta_da_year">${param.data_valuta_da_year}</c:param>
					</c:if>
					<c:if test="${!empty param.data_valuta_a_day}">  
						<c:param name="data_valuta_a_day">${data_valuta_a_day}</c:param>
					</c:if>
					<c:if test="${!empty param.data_valuta_a_month}">  
						<c:param name="data_valuta_a_month">${param.data_valuta_a_month}</c:param>
					</c:if>
					<c:if test="${!empty param.data_valuta_a_year}">  
						<c:param name="data_valuta_a_year">${param.data_valuta_a_year}</c:param>
					</c:if>
					<c:if test="${!empty param.data_contabile_da_day}">  
						<c:param name="data_contabile_da_day">${param.data_contabile_da_day}</c:param>
					</c:if>
					<c:if test="${!empty param.data_contabile_da_month}">  
						<c:param name="data_contabile_da_month">${param.data_contabile_da_month}</c:param>
					</c:if>
					<c:if test="${!empty param.data_contabile_da_year}">  
						<c:param name="data_contabile_da_year">${param.data_contabile_da_year}</c:param>
					</c:if>
					<c:if test="${!empty param.data_contabile_a_day}">  
						<c:param name="data_contabile_a_day">${data_contabile_a_day}</c:param>
					</c:if>
					<c:if test="${!empty param.data_contabile_a_month}">  
						<c:param name="data_contabile_a_month">${param.data_contabile_a_month}</c:param>
					</c:if>
					<c:if test="${!empty param.data_contabile_a_year}">  
						<c:param name="data_contabile_a_year">${param.data_contabile_a_year}</c:param>
					</c:if>
					<c:if test="${!empty ext}">
						<c:param name="ext">${ext}</c:param>
					</c:if>
			</c:url>
		</s:action>
		<s:dgcolumn label="Societ&aacute;" index="20" asc="SOCA" desc="SOCD"></s:dgcolumn>

<!-- lep utente eliminato -->

		<s:ifdatagrid left="${ext}" control="eq" right="1">
			<s:thendatagrid>
				<s:dgcolumn label="SIA / ABI /  CAB / CCB" asc="CODA" desc="CODD">
					{3}<br/>{4}<br/>{5}<br/>{6}
				</s:dgcolumn>			
				<s:dgcolumn index="7" label="Data Creazione Movimento" format="dd/MM/yyyy" asc="DCRA" desc="DCRD"></s:dgcolumn>
				<s:dgcolumn index="8" label="Nome Supporto" asc="NSUA" desc="NSUD"></s:dgcolumn>
			</s:thendatagrid>
		</s:ifdatagrid>
<!-- lep cro -->

<!-- lep tipo carta eliminato -->

		<s:dgcolumn index="11" label="Data Valuta" format="dd/MM/yyyy" asc="DVAA" desc="DVAD"></s:dgcolumn>
		<s:dgcolumn index="12" label="Data Contabile" format="dd/MM/yyyy" asc="DCOA" desc="DCOD">
		</s:dgcolumn>
<!-- lep periodo da da -->
<!-- lep periodo da a -->
		<s:dgcolumn index="15" label="Numero Transazioni" asc="NTRA" desc="NTRD" css="text_align_right"></s:dgcolumn>
		<s:dgcolumn index="16" label="Importo Movimento" format="#,##0.00" asc="IMPA" desc="IMPD" css="text_align_right"></s:dgcolumn>
		<s:dgcolumn index="17" label="Squadratura" format="#,##0.00" asc="SQAA" desc="SQAD" css="text_align_right"></s:dgcolumn>
		<s:dgcolumn index="1" label="Id Quadratura" asc="KEYA" desc="KEYD" css="text_align_right"></s:dgcolumn>
		<s:ifdatagrid left="${ext}" control="eq" right="1">
			<s:thendatagrid>
				<s:dgcolumn index="23" label="Data Chiusura" format="dd/MM/yyyy" asc="CHIUA" desc="CHIUD"></s:dgcolumn>			
			</s:thendatagrid>
		</s:ifdatagrid>
		<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;">
			<s:if right="{18}" control="eq" left="C">
				<s:then>
					<s:hyperlink href="../monitoraggio/monitoraggioTransazioni.do?$&tx_chiave_quadratura={1}&tx_button_cerca=1"
					imagesrc="../applications/templates/riconciliazione/img/dettagliMovimenti.png" 
					alt="Dettaglio movimento" text="" cssclass="hlStyle" />
				</s:then>
				<s:else></s:else>
			</s:if>
			<s:if right="{18}" control="eq" left="A">
				<s:then>
					<c:if test="${sessionScope.j_user_bean.azioniPerRiconciliazioneManualeEnabled}">
						<s:hyperlink href="riconciliazioneManualeTransazioni.do?chiaveMovimento={1}&codiceAbi={4}&codiceSia={3}&codiceCab={5}&ccb={6}&nomeSupporto={8}&cro={9}&data_pagamento_da={13}&data_pagamento_a={14}&tx_societa={20}&tx_utente={21}&tx_provincia=${tx_provincia}&data_creazione={7}&tx_tipo_carta={22}&tx_button_movimento_changed=Selezione"
						imagesrc="../applications/templates/riconciliazione/img/quadraturaManuale.png" 
						alt="Quadratura Manuale" text="" cssclass="hlStyle" />
						<s:hyperlink href="forzaChiusuraQuadratura.do?${formParameters}&chiaveMovimento2={1}"
						imagesrc="../applications/templates/riconciliazione/img/quadraturaChiusura.png" 
						alt="Chiudi quadratura" text="" cssclass="hlStyle" />
					</c:if>
				</s:then>
				<s:else></s:else>
			</s:if>
			<s:if right="{19}" control="eq" left="FLUSSO FITTIZIO CREATO DA PROCEDURA">
				<s:then>
				</s:then>
				<s:else>
					<c:if test="${sessionScope.j_user_bean.azioniPerRiconciliazioneManualeEnabled}">
						<s:hyperlink href="scaricaMovimento.do?FileNameFlusso={19}"
						imagesrc="../applications/templates/riconciliazione/img/download.png" 
						alt="Download" text="" cssclass="hlStyle" />
					</c:if>
				</s:else>
			</s:if>

		</s:dgcolumn>
	</s:datagrid>
	
	
	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Riepilogo statistico
	</s:div>
		
	<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
		<s:thead>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:th cssclass="seda-ui-datagridheadercell"  icol="7"><b>Importi Totali</b></s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr cssclass="seda-ui-datagridrowdispari">
				<s:td cssclass="seda-ui-datagridcell">&nbsp;<br/></s:td>
				<s:td cssclass="seda-ui-datagridcell">Numero Movimenti</s:td>
				<s:td cssclass="seda-ui-datagridcell">Importo Movimenti</s:td>
				<s:td cssclass="seda-ui-datagridcell">Numero Transazioni</s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowdispari">
				<s:td cssclass="seda-ui-datagridcell">Totali</s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><c:out value="${totMov}"/></s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${totImpMov}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><c:out value="${totTran}"/></s:td>
			</s:tr>
		</s:tbody>
	</s:table>

</c:if>
