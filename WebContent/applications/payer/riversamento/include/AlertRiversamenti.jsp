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
		<c:if test="${!empty rowsPerPage}">
			<c:param name="hRowsPerPage">${rowsPerPage}</c:param>
			<c:param name="rowsPerPage">${rowsPerPage}</c:param>
		</c:if>
		<c:if test="${!empty pageNumber}">  
			<c:param name="hPageNumber">${pageNumber}</c:param>
			<c:param name="pageNumber">${pageNumber}</c:param>
		</c:if>
		<c:if test="${!empty order}">  
			<c:param name="hOrder">${order}</c:param>
			<c:param name="order">${order}</c:param>
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
			<c:param name="megaBottoneAlert">0</c:param>
		</c:if>
	</c:url>


	<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<s:form name="ConfermaForm" action="ricercaRiversamenti.do${formParameters}&DDLType=21" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

		<s:table  border="0" cellspacing="0" cellpadding="3" cssclass="seda-ui-datagrid">
			<s:thead>
				<s:tr cssclass="seda-ui-datagridrowpari">
					<s:th cssclass="seda-ui-datagridheadercell"><b>Avanzamento massivo</b></s:th>
				</s:tr>
			</s:thead>
			<s:tbody>
			    <s:tr>
			    	<s:td cssclass="seda-ui-datagridcell textcenter">
						<s:label name="avviso" text="Esistono dei Riversamenti con 'Data Riversamento' superiore alla data odierna. Continuare?" cssclass="textred" />	
					</s:td>
				</s:tr>			
			</s:tbody>
		</s:table>

		<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
   			<s:button id="tx_button_mega_riv" cssclass="btnStyle" type="submit" text="Si" onclick=""/>
   			<s:button id="tx_button_cerca" cssclass="btnStyle" type="submit" text="No" onclick=""/>
		</s:div>
			
	</s:form>

				

</s:div>