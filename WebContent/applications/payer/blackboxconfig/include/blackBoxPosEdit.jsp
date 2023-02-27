<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link  href="../applications/templates/shared/js/jquery-ui-custom.min.css" rel="stylesheet" type="text/css">
<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js" type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js" type="text/javascript"></script>

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
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#tx_dataCreazione_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_dataCreazione_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_dataCreazione_day_id").val(dateText.substr(0,2));
												$("#tx_dataCreazione_month_id").val(dateText.substr(3,2));
												$("#tx_dataCreazione_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_dataCreazione_day_id",
				                            "tx_dataCreazione_month_id",
				                            "tx_dataCreazione_year_id",
				                            "tx_dataCreazione_hidden");
			}
		});
		
		$("#tx_dataAggRecord_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_dataAggRecord_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_dataAggRecord_day_id").val(dateText.substr(0,2));
												$("#tx_dataAggRecord_month_id").val(dateText.substr(3,2));
												$("#tx_dataAggRecord_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_dataAggRecord_day_id",
				                            "tx_dataAggRecord_month_id",
				                            "tx_dataAggRecord_year_id",
				                            "tx_dataAggRecord_hidden");
			}
		});

		$("#tx_dataScadenza_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_dataScadenza_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_dataScadenza_day_id").val(dateText.substr(0,2));
												$("#tx_dataScadenza_month_id").val(dateText.substr(3,2));
												$("#tx_dataScadenza_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_dataScadenza_day_id",
				                            "tx_dataScadenza_month_id",
				                            "tx_dataScadenza_year_id",
				                            "tx_dataScadenza_hidden");
			}
		});
		
	});

</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="var_form" action="blackboxpos.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">MODIFICA POSIZIONE BLACK BOX</s:div>
										
				<s:div name="divRicMetadati" cssclass="divRicMetadati">
				
					<s:div name="divRicercaMetadatiLeft" cssclass="divRicMetadatiLeft">
	
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_idDominio" 
								label="ID Dominio:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								
								text="${tx_idDominio}" />
						</s:div>
					
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_idFlusso" 
								label="ID Flusso:" bdisable="false"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								
								text="${tx_idFlusso}" />
						</s:div>
					
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_idDocumento" 
								label="Id Documento:" bdisable="false"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								
								text="${tx_idDocumento}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_codFisc" 
								label="Codice Fiscale:" bdisable="false"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								
								text="${tx_codFisc}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_indContrib" 
								label="Indirizzo Contribuente:" bdisable="false"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								
								text="${tx_indContrib}" />
						</s:div>
						<!--  inizio LP PG200370 bmodify ==> "false" -->
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_flagAnnullamento" 
								label="Flag Annullamento:" bdisable="false"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								text="${tx_flagAnnullamento}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<!--  inizio LP PG200370-->
							<!--
							<s:dropdownlist name="tx_flagPagato"
								disable="false" 
								label="Flag Pagato:"
								cssclasslabel="label85 bold floatleft textright"
								cssclass="tbddl floatleft"
								usexml="false"
								valueselected="${tx_flagPagato}">
								<s:ddloption value="" text="No"/>
								<s:ddloption value="X" text="Si"/>
							</s:dropdownlist>
							-->
							<c:if test="${tx_flagPagato != 'X'}">
								<s:textbox bmodify="false" name="tx_flagPagato_show" 
									label="Flag Pagato:" bdisable="false"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman colordisabled" 
									text="No" />
							</c:if>
							<c:if test="${tx_flagPagato == 'X'}">
								<s:textbox bmodify="false" name="tx_flagPagato_show" 
									label="Flag Pagato:" bdisable="false"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman colordisabled" 
									text="Si" />
							</c:if>
							<input type="hidden" id="tx_flagPagato" value="${tx_flagPagato}"/>
							<!--  fine LP PG200370-->
						</s:div>
				
					</s:div>
					
					<s:div name="divRicercaRight2" cssclass="divRicMetadatiCenter">
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_idEnte" 
								label="Codice Ente:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								text="${tx_idEnte}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								
							<s:div name="divData" cssclass="seda-ui-date divData">
										 <s:date label="Data Creazione:" prefix="tx_dataCreazione" yearbegin="2000" 
									yearend="2030" locale="IT-it" descriptivemonth="false" 
									separator="/" showrequired="false" calendar="${tx_dataCreazione}"
									cssclasslabel="label85 bold textright" >
								</s:date>				
								<input type="hidden" id="tx_dataCreazione_hidden" value=""/>
							</s:div>
								
						</s:div>
					
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_numRata" 
								label="Numero Rata:" bdisable="false"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								text="${tx_numRata}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_importo" 
								label="Importo:" bdisable="false"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								text="${tx_importo}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_locContrib" 
								label="Localit&agrave; Contribuente:" bdisable="false"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								text="${tx_locContrib}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_ibanAccr" 
								label="Iban Accredito:" bdisable="false"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								text="${tx_ibanAccr}" />
						</s:div>
						
					</s:div>	
					
					<s:div name="divRicercaRight3" cssclass="divRicMetadatiRight">				
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_numeroAvviso" 
								label="Numero Avviso:" bdisable="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								
								text="${tx_numeroAvviso}" />
						</s:div>
						<!--  inizio LP PG200370 bmodify ==> "false" -->
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_tipoRecord" 
								label="Tipo Record:" bdisable="false"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled" 
								
								text="${tx_tipoRecord}" />
						</s:div>
					
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:div name="divData" cssclass="seda-ui-date divData">
										 <s:date label="Data Scadenza:" prefix="tx_dataScadenza" yearbegin="2000" 
									yearend="2030" locale="IT-it" descriptivemonth="false" 
									separator="/" showrequired="true" calendar="${tx_dataScadenza}"
									cssclasslabel="label85 bold textright" >
								</s:date>				
								<input type="hidden" id="tx_dataScadenza_hidden" value=""/>
							</s:div>
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_denDeb" 
								label="Denominazione Contribuente:" bdisable="false"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								
								text="${tx_denDeb}" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_provContrib" 
								label="Provincia Contribuente:" bdisable="false"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								text="${tx_provContrib}" />
						</s:div>
												
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_idIuv" 
								label="Codice IUV:" maxlenght="17" bdisable="false"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" 
								text="${tx_idIuv}" />
						</s:div>
						
					</s:div>
					
					<%-- inizio LP PG200370 --%>
					<s:div name="divElementUnicaTassonomia" cssclass="divRicMetadatiUnicaTassonomiaCFECFS">
						<s:div name="divElementU1" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="tx_tassonomia"
									disable="false"
									label="Tassonomia:"
									cssclass="seda-ui-ddl tbddlMaxTassonomia870 floatleft" 
									cssclasslabel="seda-ui-label label85 bold textright"
									cachedrowset="tassonomie" usexml="true"
									validator="ignore" showrequired="false"
									valueselected="${tx_tassonomia}">
									<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
									<s:ddloption value="{12}" text="{3} / {5} / {8} / {10} => {12}"/>
							</s:dropdownlist>
						</s:div>
					</s:div>
					<%-- fine LP PG200370 --%>
					
					
			</s:div>	
			<br/> 
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_indietro" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_edit" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
			</s:div>
			<input type="hidden" id="codOp" name="codOp" value="${codOp}" />
			<input type="hidden" name="tx_idDominio_hidden" value="${tx_idDominio_hidden}" />
			<input type="hidden" name="tx_idEnte_hidden" value="${tx_idEnte_hidden}" />
			<input type="hidden" name="tx_numeroAvviso_hidden" value="${tx_numeroAvviso_hidden}" />
			<input type="hidden" name="tx_idIuv_hidden" value="${tx_idIuv_hidden}" />	
			
		</s:form>
	</s:div>
	
</s:div>



<s:div name="div_messaggi" cssclass="div_align_center"> 
	<c:if test="${!empty tx_message}"> 
		<s:div name="div_messaggio_info"> 
			<hr /> 
			<s:label name="tx_message" text="${tx_message}" /> 
			<hr /> 
		</s:div> 
	</c:if>
	<c:if test="${!empty tx_error_message}">
		<s:div name="div_messaggio_errore">
			<hr />
			<s:label name="tx_error_message" text="${tx_error_message}" />
			<hr />
		</s:div>
	</c:if>
</s:div>
