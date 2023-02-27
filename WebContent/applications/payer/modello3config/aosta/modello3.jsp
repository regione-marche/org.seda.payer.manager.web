<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="modello3" encodeAttributes="true" />

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
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#tx_validita_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_validita_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_validita_da_day_id").val(dateText.substr(0,2));
												$("#tx_validita_da_month_id").val(dateText.substr(3,2));
												$("#tx_validita_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_validita_da_day_id",
				                            "tx_validita_da_month_id",
				                            "tx_validita_da_year_id",
				                            "tx_validita_da_hidden");
			}
		});
		$("#tx_validita_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_validita_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_validita_a_day_id").val(dateText.substr(0,2));
												$("#tx_validita_a_month_id").val(dateText.substr(3,2));
												$("#tx_validita_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_validita_a_day_id",
				                            "tx_validita_a_month_id",
				                            "tx_validita_a_year_id",
				                            "tx_validita_a_hidden");
			}
		});
		
	});
</script>


<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="form_selezione" action="modello3.do?vista=modello3" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA CONFIGURAZIONI PAGOPA
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label160 bold textright floatleft" validator="ignore;"
									cssclass="seda-ui-ddl tbddlMax780 floatleft" disable="${codop == 'edit'}"
									onchange="setFired();this.form.submit();"
									   cachedrowset="listaSocietaUtenteEnte" usexml="true" 
									   valueselected="${ddlSocietaUtenteEnte}">
   							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
							<s:ddloption value="{1}|{2}|{3}|{4}" text="{5} / {6} / {4}"/>
						</s:dropdownlist>
						<c:if test="${codop == 'add'}">
							<noscript>
								<s:button id="tx_button_societa_changed" 
									onclick="" text="" 
									validate="false"
									type="submit" cssclass="btnimgStyle"  title="Aggiorna" />
							</noscript>
						</c:if>									
					</s:div>					
				</s:div>				
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_idDominio_s"
							label="Id Dominio:" maxlenght="16" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=^[0-9a-zA-Z\-]{1,16}$"
							message="[accept=id Dominio: ${msg_configurazione_descrizione_3}]"
							text="${tx_idDominio_s}" />
					</s:div>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_carattereDiServizio"
							label="Carattere di Servizio:" maxlenght="2" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore"
							message="[accept= Carattere Di Servizio: non valido]"
							text="${tx_carattereDiServizio}" />
					</s:div>
				</s:div>
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_auxDigit" disable="false"
							cssclass="tbddlMax floatleft" label="AuxitDigit:"
							cssclasslabel="label85 bold textright"
							valueselected="${tx_auxDigit}">
							<s:ddloption text="" value="" />
							<s:ddloption text="0" value="0" />
							<s:ddloption text="1" value="1" />
							<s:ddloption text="2" value="2" />
							<s:ddloption text="3" value="3" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_codiceSegregazione"
							label="Codice Segregazione:" maxlenght="2" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=^[0-9a-zA-Z\-]{1,2}$"
							message="[accept=Codice Segregazione: non valido]"
							text="${tx_codiceSegregazione}" />
					</s:div>
				</s:div>
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_add" validate="false"  onclick="" text="Nuovo" type="submit" cssclass="btnStyle" />
			</s:div>
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

<c:if test="${!empty lista_configurazionemodello3}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO CONFIGURAZIONI PAGOPA
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_configurazionemodello3"
			action="modello3.do?vista=inviomodello3Lista" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			
			<s:dgcolumn index="1" label="Società"></s:dgcolumn>
			<s:dgcolumn index="2" label="Utente"></s:dgcolumn>
			<s:dgcolumn index="3" label="Chiave ente"></s:dgcolumn>
			<s:dgcolumn index="12" label="Descr. ente"></s:dgcolumn> <!-- //PG22XX09_YL5 -->
			<s:dgcolumn index="4" label="Id dominio"></s:dgcolumn>
			<s:dgcolumn index="5" label="AuxDigit"></s:dgcolumn>
			<s:dgcolumn index="6" label="Cod. segregazione"></s:dgcolumn>
			<s:dgcolumn index="11" label="Carattere serv."></s:dgcolumn>
			
			<s:dgcolumn label="Azioni">
				<s:hyperlink					
					href="modello3Edit.do?vista=modello3&codop=edit&tx_ented={3}&tx_idDominio={4}&tx_auxitDigit={5}&tx_codiceSegregazione={6}&tx_carattereDiServizio={11}&ddlSocietaUtenteEnte={1}|{2}|{3}" 
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Modifica" text=""
					cssclass="blacklink hlStyle" />
				<s:hyperlink
					href="modello3Edit.do?vista=modello3&tx_button_cancel=&tx_ente={3}&tx_idDominio={4}&tx_auxDigit={5}&tx_codiceSegregazione={6}&tx_carattereDiServizio={11}&ddlSocietaUtenteEnte={1}|{2}|{3}" 
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text=""
					cssclass="blacklink hlStyle" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>