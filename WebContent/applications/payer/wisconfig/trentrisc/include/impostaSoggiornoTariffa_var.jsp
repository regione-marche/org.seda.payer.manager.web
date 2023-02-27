<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="impostaSoggiornoTariffa_var" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>



<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}

</script>

<script type="text/javascript">
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();
	
	$(function() {
		$.datepicker.setDefaults($.datepicker.regional["it"]);
		$("#tx_data_validita_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_validita_hidden").datepicker( {
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn : "button",
			buttonImage : "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly : true,
			onSelect : function(dateText, inst) {
				$("#tx_data_validita_day_id").val(dateText.substr(0, 2));
				$("#tx_data_validita_month_id").val(dateText.substr(3, 2));
				$("#tx_data_validita_year_id").val(dateText.substr(6, 4));
			},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_validita_day_id",
				                            "tx_data_validita_month_id",
				                            "tx_data_validita_year_id",
				                            "tx_data_validita_hidden");
			}
		});
	});
</script>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_tariffa_form" action="ImpostaSoggiornoTariffaInsUpd.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">TARIFFA STRUTTURA</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">TARIFFA STRUTTURA</s:div>
			</c:if>
									
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
							<s:dropdownlist name="tx_provincia"
								disable="${!empty tx_chiaveTar}" 
								label="Provincia:"
								cssclasslabel="label85 bold floatleft textright"
								cssclass="tbddl floatleft"
								cachedrowset="listProvince" usexml="true"
								onchange="setFiredButton('tx_button_provincia_changed');this.form.submit();"
								validator="required" showrequired="true"
								valueselected="${tx_provincia}">
								<s:ddloption text="Selezionare uno degli elementi" value="" />
								<s:ddloption  value="{2}" text="{1}"/>
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_provincia_changed" onclick="" text="" validate="false"
									type="submit" cssclass="btnimgStyle" title="Aggiorna" disable="${!empty tx_chiaveTar}"/>
							</noscript>
						</s:div>



						
						<s:div name="tipServLabel_srch" cssclass="divRicMetadatiCenterSmall">
							<s:dropdownlist name="tx_comune"
									disable="${!empty tx_chiaveTar}" 
									label="Comune:"
									cssclasslabel="label65 bold textright"
									cssclass="tbddl"
									cachedrowset="listbelfiore" usexml="true"
									validator="required" showrequired="true"
									valueselected="${tx_comune}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption  value="{1}" text="{4}"/>
								</s:dropdownlist>
						</s:div>
						<s:div name="divElement2" cssclass="divRicMetadatiTopRight">
							<s:dropdownlist name="tx_struttura"
								disable="${!empty tx_chiaveTar}" 
								label="Struttura Ricettiva:"
								cssclasslabel="label85 bold floatleft textright"
								cssclass="tbddl floatleft"
								cachedrowset="listStrutture" usexml="true"
								validator="required" showrequired="true"
								valueselected="${tx_struttura}">
								<s:ddloption text="Selezionare uno degli elementi" value="" />
								<s:ddloption  value="{1}" text="{2}"/>
							</s:dropdownlist>


						</s:div>
							
							
					</s:div>
				<s:div name="divElement18b" cssclass="floatleft">	
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
							<s:dropdownlist label="Data Validit&agrave;:"
								cssclasslabel="label85 bold textright" cssclass="seda-ui-date dateman"
								name="tx_data_validita_day" 
								disable="${edit_tariffa != null && edit_tariffa == 'N'}"
								validator="required" showrequired="true"
								valueselected="${tx_data_validita_day}"
								message="[required=Inserire il giorno della data validit&agrave;]">
								<s:ddloption value="" text="" />
								<s:ddloption value="01" text="01"/>
							</s:dropdownlist>
							<s:dropdownlist label="/"
								cssclasslabel="label10 bold textright" cssclass="seda-ui-date dateman"
								name="tx_data_validita_month"
								disable="${edit_tariffa != null && edit_tariffa == 'N'}"
								validator="required"
								valueselected="${tx_data_validita_month}"
								message="[required=Inserire il mese della data validit&agrave;]">
							    <s:ddloption value="" text="" />
								<s:ddloption value="01" text="01" />
								<s:ddloption value="02" text="02" />
								<s:ddloption value="03" text="03" />
								<s:ddloption value="04" text="04" />
								<s:ddloption value="05" text="05" />
								<s:ddloption value="06" text="06" />
								<s:ddloption value="07" text="07" />
								<s:ddloption value="08" text="08" />
								<s:ddloption value="09" text="09" />
								<s:ddloption value="10" text="10" />
								<s:ddloption value="11" text="11" />
								<s:ddloption value="12" text="12" />
							</s:dropdownlist>

<!--  							<c:forEach var="i" begin="2012" end="2014" step="1">
								 valore ${i} = ${i}
								</c:forEach> -->

							<s:dropdownlist label="/"
								cssclasslabel="label10 bold textright" cssclass="seda-ui-date dateman"
								name="tx_data_validita_year" 
								disable="${edit_tariffa != null && edit_tariffa == 'N'}"
								validator="required"
								valueselected="${tx_data_validita_year}"
								message="[required=Inserire l'anno della data validit&agrave;]">
								<s:ddloptionbinder options="${listDataValiditaAnno}"/>
							</s:dropdownlist>
				</s:div>
				</s:div>
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">

					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">						
						<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
									<s:textbox name="tx_importo" label="Importo:" 
												bmodify="${edit_tariffa == null || edit_tariffa == 'Y'}" 
												bdisable="${edit_tariffa != null && edit_tariffa == 'N'}"
											   text="${tx_importo}" showrequired="true"
											   cssclass="textareaman" cssclasslabel="label85 bold floatleft textright"  
									           maxlenght="16" validator="required;accept=^[0-9]{1,13}\,[0-9]{2}?$;maxlength=16" 
									           message="[accept=Importo: ${msg_configurazione_importo_13_2}]"
									           />
						</s:div>
					</s:div>											
				</s:div>
				
			</s:div>
			<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none" cssclasslabel="rend_display_none" />

				<s:button id="tx_button_indietro" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />

				<c:if test="${empty tx_chiaveTar}">
					<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
					<s:button id="tx_button_aggiungi_end" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${!empty tx_chiaveTar && (edit_tariffa == null || edit_tariffa == 'Y')}">
					<s:button id="tx_button_edit" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
					<s:button id="tx_button_edit_end" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
			</s:div>
 			
	</s:form>
	</s:div>
	
</s:div>
