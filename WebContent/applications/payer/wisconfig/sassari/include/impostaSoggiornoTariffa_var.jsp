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
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">INSERIMENTO TARIFFA STRUTTURA</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">MODIFICA TARIFFA STRUTTURA</s:div>
			</c:if>
									
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
							<s:dropdownlist name="tx_provincia"
								disable="true" 
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
									disable="true" 
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
								<s:ddloption value="04" text="04" />
								<s:ddloption value="07" text="07" />
								<s:ddloption value="10" text="10" />
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
				<!--  
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
				-->
				
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

<c:if test="${codop == 'edit'}">
	<c:set var="disableOperazioniFasce" value="Y" scope="page" />
	<c:choose>
		<c:when test="${!empty tx_chiaveTar && (edit_tariffa == null || edit_tariffa == 'Y')}">
			<c:set var="disableOperazioniFasce" value="N" scope="page" />
		</c:when>
		<c:otherwise>
			<c:set var="disableOperazioniFasce" value="Y" scope="page" />
		</c:otherwise>
	</c:choose>

	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO FASCE TARIFFA&nbsp;&nbsp;&nbsp;
			<!-- NUOVO RECORD -->
			<c:if test="${disableOperazioniFasce == 'N'}">
				<s:hyperlink
					cssclass="hlStyle" 
					href="ImpostaSoggiornoFasciaTariffaInsUpd.do?tx_button_aggiungi=B&tx_chiaveTar=${tx_chiaveTar}&tx_provincia=${tx_provincia}&tx_comune=${tx_comune}&tx_struttura=${tx_struttura}&vista=impostaSoggiornoTariffa_var&start=A"
					imagesrc="../applications/templates/configurazione/img/aggiungi.png"
					alt="Nuova Fascia" text="" />
			</c:if>
	</s:div>
	<c:if test="${!empty listaFasceTariffa}">
		<c:if test="${disableOperazioniFasce == 'N'}"> 
			<s:div name="div_datagrid">
				<s:datagrid viewstate="true" cachedrowset="listaFasceTariffa"  
					action="ImpostaSoggiornoTariffaInsUpd.do?vista=impostaSoggiornoTariffa_search&tx_button_default=C" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
		
					<!-- CODICE Fascia -->
					<s:dgcolumn label="Codice" index="3" />
					
					<!-- DESCRIZIONE Fascia -->
					<s:dgcolumn label="Fascia" index="4" />
		
					<!-- IMPORTO  -->
					<s:dgcolumn index="5" format="#,##0.00" label="Importo" css="text_align_right"></s:dgcolumn>
					
					<!-- AZIONI -->
					<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">
					<!-- MODIFICA RECORD -->
					<s:hyperlink
						cssclass="hlStyle" 
						href="ImpostaSoggiornoFasciaTariffaInsUpd.do?tx_button_edit=B&tx_chiaveFasciaTar={1}&tx_provincia=${tx_provincia}&tx_comune=${tx_comune}&tx_struttura=${tx_struttura}&vista=impostaSoggiornoTariffa_var&start=A"
						imagesrc="../applications/templates/configurazione/img/edit.png"
						alt="Modifica" text="" />
					<!-- CANCELLAZIONE RECORD -->
					<s:hyperlink
						cssclass="hlStyle" 
						href="ImpostaSoggiornoFasciaTariffaCancel.do?tx_button_delete=B&tx_chiaveFasciaTar={1}&vista=impostaSoggiornoTariffa_var&start=A"
						imagesrc="../applications/templates/configurazione/img/cancel.png"
						alt="Elimina" text=""   />
					</s:dgcolumn>
				</s:datagrid>
			</s:div>
		</c:if>	
		<c:if test="${disableOperazioniFasce == 'Y'}"> 
			<s:div name="div_datagrid">
				<s:datagrid viewstate="true" cachedrowset="listaFasceTariffa"  
					action="ImpostaSoggiornoTariffaInsUpd.do?vista=impostaSoggiornoTariffa_search&tx_button_default=C" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
					<!-- CODICE Fascia -->
					<s:dgcolumn label="Codice" index="3" />
					<!-- DESCRIZIONE Fascia -->
					<s:dgcolumn label="Fascia" index="4" />
					<!-- IMPORTO  -->
					<s:dgcolumn index="5" format="#,##0.00" label="Importo" css="text_align_right"></s:dgcolumn>
				</s:datagrid>
			</s:div>
		</c:if>	
	</c:if>
</c:if>
	