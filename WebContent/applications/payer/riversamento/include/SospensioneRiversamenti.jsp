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
<link type="text/css" rel="stylesheet"
	href="../applications/templates/shared/css/ui-lightness/jquery-ui-custom.css"
	media="screen" />
<script>
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
		<c:if test="${!empty param.rRowsPerPage}">
			<c:param name="rowsPerPage">${param.rRowsPerPage}</c:param>
		</c:if>
		<c:if test="${!empty param.rPageNumber}">  
			<c:param name="pageNumber">${param.rPageNumber}</c:param>
		</c:if>
		<c:if test="${!empty param.rOrder}">  
			<c:param name="order">${param.rOrder}</c:param>
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

	<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<s:table  border="1" cellspacing="0" cellpadding="3"  cssclass="seda-ui-datagrid">
		<s:thead>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:th cssclass="seda-ui-datagridheadercell"  icol="13"><b>Riversamento</b></s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr cssclass="seda-ui-datagridrowdispari">
				<s:td cssclass="seda-ui-datagridcell">Data Rivers.</s:td>
			    <s:td cssclass="seda-ui-datagridcell">Ente</s:td>
				<s:td cssclass="seda-ui-datagridcell">Ufficio</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Tipo Rend.</s:td>
				<s:td cssclass="seda-ui-datagridcell">Strumento Riv.</s:td>				
				<s:td cssclass="seda-ui-datagridcell">Stato Riv.</s:td>
				<s:td cssclass="seda-ui-datagridcell">Importo</s:td>
				<s:td cssclass="seda-ui-datagridcell">Contributo cittadino</s:td>
				<s:td cssclass="seda-ui-datagridcell">Commissioni Banca</s:td>
				<s:td cssclass="seda-ui-datagridcell">Spese Notifica</s:td>
				<s:td cssclass="seda-ui-datagridcell">Commissioni gestore</s:td>
				<s:td cssclass="seda-ui-datagridcell">Importo da riversare</s:td>
				<s:td cssclass="seda-ui-datagridcell">Importo da recuperare</s:td>
			</s:tr>

			<s:tr cssclass="seda-ui-datagridrowpari">
					<s:td cssclass="seda-ui-datagridcell"><c:out value="${c1S}"/></s:td>  <!-- data -->					
					<s:td cssclass="seda-ui-datagridcell"><c:out value="${c2}"/></s:td>  <!-- ANE_CANECENT -->
					<s:td cssclass="seda-ui-datagridcell"><c:out value="${c3S}"/></s:td>  <!-- ANE_TANETUFF/ANE_CANECUFF -->
					<s:td cssclass="seda-ui-datagridcell"><c:out value="${c5S}"/></s:td>  <!--  REV_FREVTIPO R Rivers. C Rendic.-->
					<s:td cssclass="seda-ui-datagridcell"><c:out value="${c6S}"/></s:td>  <!-- REV_FREVRIVE -->
					<s:td cssclass="seda-ui-datagridcell"><c:out value="${c7S}"/></s:td>  <!-- REV_FREVSTAT -->
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${c8}" minFractionDigits="2" maxFractionDigits="2"/></s:td>  <!-- REV_IREVTOTA -->
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${c9}" minFractionDigits="2" maxFractionDigits="2"/></s:td>  <!-- REV_IREVCONC -->
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${c10}" minFractionDigits="2" maxFractionDigits="2"/></s:td>  <!-- REV_IREVCGTW -->
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${c11}" minFractionDigits="2" maxFractionDigits="2"/></s:td>  <!-- REV_IREVSPES -->
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${c12}" minFractionDigits="2" maxFractionDigits="2"/></s:td>  <!-- REV_IREVGESC -->
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${c13}" minFractionDigits="2" maxFractionDigits="2"/></s:td>  <!-- REV_IREVIREV_R -->
					<s:td cssclass="seda-ui-datagridcell text_align_right"><fmt:formatNumber type="NUMBER" value="${c21}" minFractionDigits="2" maxFractionDigits="2"/></s:td>  <!-- REV_IREVIREV_C -->		
			</s:tr>
		</s:tbody>
	</s:table>
	</s:div>

	<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<s:form name="ricercaAssocImpBenForm" action="aggiornaStatoRiv.do?tx_button_avanza_stato" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:textbox name="DDLType" label="DDLType" bmodify="true" text="21" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="codSoc" label="codSoc" bmodify="true" text="${revSoc}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="codUte" label="codUte" bmodify="true" text="${revUte}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="dataRiv" label="DDLType" bmodify="true" text="${revData}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="codEnte" label="DDLType" bmodify="true" text="${revBen}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="tipoRend" label="DDLType" bmodify="true" text="${revRive}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="tipoRiv" label="DDLType" bmodify="true" text="${revTipo}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="stato" label="DDLType" bmodify="true" text="S" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rRowsPerPage" label="DDLType" bmodify="true" text="${rRowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rPageNumber" label="DDLType" bmodify="true" text="${rPageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="rOrder" label="DDLType" bmodify="true" text="${rOrder}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="codSocieta" label="DDLType" bmodify="true" text="${codSocieta}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="codProvincia" label="DDLType" bmodify="true" text="${codProvincia}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="codUtente" label="DDLType" bmodify="true" text="${codUtente}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="codBeneficiario" label="DDLType" bmodify="true" text="${codBeneficiario}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="tipoRendicontazione" label="DDLType" bmodify="true" text="${tipoRendicontazione}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="strumRiversamento" label="DDLType" bmodify="true" text="${strumRiversamento}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="statoRiversamento" label="DDLType" bmodify="true" text="${statoRiversamento}" cssclass="display_none" cssclasslabel="display_none"  />

		<s:textbox name="data_riversamento_da_day" label="DDLType" bmodify="true" text="${data_riversamento_da_day}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="data_riversamento_da_month" label="DDLType" bmodify="true" text="${data_riversamento_da_month}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="data_riversamento_da_year" label="DDLType" bmodify="true" text="${data_riversamento_da_year}" cssclass="display_none" cssclasslabel="display_none"  />

		<s:textbox name="data_riversamento_a_day" label="DDLType" bmodify="true" text="${data_riversamento_a_day}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="data_riversamento_a_month" label="DDLType" bmodify="true" text="${data_riversamento_a_month}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="data_riversamento_a_year" label="DDLType" bmodify="true" text="${data_riversamento_a_year}" cssclass="display_none" cssclasslabel="display_none"  />
		
		<s:textbox name="ext" label="ext" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none"  />
		<s:textbox name="megaBottoneAlert" label="megaBottoneAlert" bmodify="true" text="${megaBottoneAlert}" cssclass="display_none" cssclasslabel="display_none"  />
		
		<s:table  border="0" cellspacing="0" cellpadding="3" cssclass="seda-ui-datagrid">
			<s:thead>
				<s:tr cssclass="seda-ui-datagridrowpari">
					<s:th cssclass="seda-ui-datagridheadercell"><b>Sospensione riversamento</b></s:th>
				</s:tr>
			</s:thead>
			<s:tbody>
			    <s:tr>
			    	<s:td cssclass="seda-ui-datagridcell">
						<s:textbox name="nota" label="Motivazione" bmodify="true" text="${requestScope.nota}" 
						   cssclass="textareaman_note" cssclasslabel="label85 bold textright"
						   validator="required" showrequired="true"/>	
					</s:td>
				</s:tr>			
			</s:tbody>
		</s:table>

		<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
   			<s:button id="submit" cssclass="btnStyle" type="submit" text="Sospendi" onclick=""/>
		</s:div>
			
	</s:form>

				
<!-- inizio -->



	<s:div name="indietro" cssclass="divRicBottoni">
		<s:form name="ricercaRiversamenti" action="ricercaRiversamenti.do${formParameters}&DDLType=21" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
				<s:button id="tx_button_cerca" cssclass="btnStyle" type="submit" text="Indietro" onclick=""/>
		</s:form>		
	</s:div>

</s:div>