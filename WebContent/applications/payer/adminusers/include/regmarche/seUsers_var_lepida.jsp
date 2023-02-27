<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="adminusers_var" encodeAttributes="true" />


<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>
<link type="text/css" rel="stylesheet"
	href="../applications/templates/shared/css/ui-lightness/jquery-ui-custom.css"
	media="screen" />

<!-- DEVE ESSERE ANCORA MODIFICATA -->

<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>

<script>
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();
	$(function() {
		
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#dataScadenza_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#dataScadenza_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#dataScadenza_day_id").val(dateText.substr(0,2));
												$("#dataScadenza_month_id").val(dateText.substr(3,2));
												$("#dataScadenza_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("dataScadenza_day_id",
				                            "dataScadenza_month_id",
				                            "dataScadenza_year_id",
				                            "dataScadenza_hidden");
			}
		});
	});
</script>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_form" action="${do_command_name}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Inserimento Nuovo User</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Modifica User</s:div>
			</c:if>
						
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<!-- "SE000SV"."SEUSRTB"."USR_FUSRATTI" -->
					
				<!--  UTENZA ATTIVA  -->
				
					<s:div name="divElement50" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_utenzaAttiva}" validator="ignore" 
						 cssclasslabel="bold checklabel label150" cssclass="checkleft"
						name="utenzaAttiva" groupname="utenzaAttiva" 
						text="Utenza attiva" value="Y" />
					</s:div>

				<!--  TIPOLOGIA UTENTE  -->
					
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tipologiaUtente" disable="false"
							cssclass="tbddl"
							cssclasslabel="label65 bold textright"
							label="(*)&nbsp;Tipolog. Utente:" valueselected="${tipologiaUtente}" 
							validator="ignore"
							>
							<s:ddloption text="Selezionare un valore" value="" />
							<s:ddloption text="Ditta individuale" value="D" />
							<s:ddloption text="Persona fisica" value="F" />
							<s:ddloption text="Persona giuridica" value="G" />
						</s:dropdownlist>
					</s:div>
					
				</s:div>


				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">

				<!--  CODICE FISCALE  -->
				<!-- Il codice fiscale viene visualizzato con la label "UserId"  -->
					
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<c:if test="${codop == 'add'}">
							<s:textbox bmodify="true" name="tx_codiceFiscale"
								label="(*)&nbsp;UserId (CF):" maxlenght="16" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=^[a-zA-Z]{6}[\d]{2}[a-zA-Z][\d]{2}[a-zA-Z][\d]{3}[a-zA-Z]|^[0-9]{11}$"
								message="[accept=UserId (CF): ${msg_configurazione_codicefiscale_piva}]"
								text="${tx_codiceFiscale}" />
						</c:if>
						<c:if test="${codop == 'edit'}">
							<s:textbox bmodify="${codiceFiscaleModificabile}" name="tx_codiceFiscale"
								label="(*)&nbsp;UserId (CF):" maxlenght="16" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman border_none" validator="ignore;accept=^[a-zA-Z]{6}[\d]{2}[a-zA-Z][\d]{2}[a-zA-Z][\d]{3}[a-zA-Z]|^[0-9]{11}$"
								message="[accept=UserId (CF): ${msg_configurazione_codicefiscale_piva}]"
								text="${tx_codiceFiscale}" />
						</c:if>
					</s:div>

				<!--  NOME  -->
					
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<c:if test="${codop == 'add'}">
							<s:textbox bmodify="true" name="tx_nome"
								label="(*)&nbsp;Nome:" maxlenght="50" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${adminusers_descrizione_testo_spazio_50_regex}"
								message="[accept=Nome: ${msg_configurazione_testo_spazio}]"
								text="${tx_nome}" />
						</c:if>
						<c:if test="${codop == 'edit'}">
							<s:textbox bmodify="${nomeModificabile}" name="tx_nome"
								label="(*)&nbsp;Nome:" maxlenght="50" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman border_none" validator="ignore;accept=${adminusers_descrizione_testo_spazio_50_regex}"
								message="[accept=Nome: ${msg_configurazione_testo_spazio}]"
								text="${tx_nome}" />
						</c:if>
					</s:div>
					
				<!--  COGNOME -->
					
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<c:if test="${codop == 'add'}">
							<s:textbox bmodify="true" name="tx_cognome"
								label="(*)&nbsp;Cognome:" maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${adminusers_descrizione_testo_spazio_256_regex}"
								message="[accept=Cognome: ${msg_configurazione_testo_spazio}]"
								text="${tx_cognome}" />
						</c:if>
						<c:if test="${codop == 'edit'}">
							<s:textbox bmodify="${cognomeModificabile}" name="tx_cognome"
								label="(*)&nbsp;Cognome:" maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman border_none" validator="ignore;accept=${adminusers_descrizione_testo_spazio_256_regex}"
								message="[accept=Cognome: ${msg_configurazione_testo_spazio}]"
								text="${tx_cognome}" />
						</c:if>
					</s:div>
					
					
				<!--  NUMERO SMS  -->
					
					<s:div name="divElement16" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="smsNotifiche"
							label="Numero per invio SMS:" maxlenght="15" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;digits"
							text="${smsNotifiche}" />
					</s:div>

				<!--             INDIRIZZO EMAIL PER LEPIDA NON SERVE         -->
					
					<s:div name="divElement15" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="emailNotifiche"
							label="(*)&nbsp;Indirizzo email:" maxlenght="50" 
							cssclasslabel="rend_display_none"
							cssclass="rend_display_none" validator="ignore;accept=${adminusers_email_regex};maxlength=50"
							text="infodummy@dummy.it" />
					</s:div>
					
				<!--              USERID           -->
				<!-- Lo USERID non viene più visualizzato per LEPIDA     -->
				<!-- perché è uguale al codice fiscale                   -->
				<!-- Nell'inserimanto di una nuova utenza lo USERID      -->
				<!-- assume lo stesso valore del CODICE FISCALE          -->

					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<c:if test="${codop == 'add'}">
							<s:textbox bmodify="${usernameModificabile}" name="tx_username" 
								label="(*)&nbsp;UserId:" maxlenght="20" 
								cssclasslabel="rend_display_none"
								cssclass="rend_display_none" validator="ignore;minlength=8;accept=^[a-zA-Z0-9]{8,20}$;maxlength=20"
								message="[accept=UserId ${msg_configurazione_alfanumerici}]"
								text="dummyuser" />
						</c:if>
					<c:if test="${codop == 'edit'}">
						<s:textbox bmodify="${usernameModificabile}" name="tx_username" 
							label="(*)&nbsp;UserId:" maxlenght="20" 
							cssclasslabel="rend_display_none"
							cssclass="rend_display_none" 
							text="${tx_username}" />
					</c:if>
					</s:div>
					
				</s:div>

				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">

				<!--  FLAG PER PASSWORD AUTOGENERATA  -->
				<!--  NOTA: PER LEPIDA LA PASSWORD È SEMPRE AUTOGENERATA         -->	

					<c:if test="${codop == 'add'}">
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:list bradio="false" bchecked="true" validator="ignore" disable="true"
							 cssclasslabel="bold checklabel label150" cssclass="checkleft"
							name="passwordAutogenerata_dummy" groupname="passwordAutogenerata_dummy" 
							text="Password autogenerata" value="Y" />
							<s:textbox name="passwordAutogenerata" label="passwordAutogenerata_hidden"
								bmodify="false" text="Y" cssclass="rend_display_none"
								cssclasslabel="rend_display_none" />
						</s:div>
					</c:if>

				<!--                FLAG PER LA SCADENZA DELL'UTENZA                    -->

					<s:div name="divElement48" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_nessunaScadenza}" validator="ignore" 
						 cssclasslabel="bold checklabel label150" cssclass="checkleft"
						 onclick="setFiredButton('tx_button_scadenza_changed');this.form.submit();"
						name="nessunaScadenza" groupname="nessunaScadenza" 
						text="L'utenza non scade mai" value="Y" />
						<noscript>
							<s:button id="tx_button_scadenza_changed"
								onclick="" text="" validate="false" 
								type="submit" cssclass="btnimgStyle"
								 />
						</noscript>
					</s:div>
					<s:div name="divElement49" cssclass="divRicMetadatiSingleRow">
						<s:div name="divElement49b" cssclass="floatleft">
							<s:div name="divDataDa_scadenza" cssclass="divDataDa">
								<s:date label="(*)&nbsp;Data Scadenza:" prefix="dataScadenza" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="label85"
									cssclass="dateman" disabled="${chk_nessunaScadenza}"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${dataScadenza}"></s:date>
								<input type="hidden" id="dataScadenza_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
					<s:div name="divElement16" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="note"
							label="Note:" maxlenght="256" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore"
							text="${note}" />
					</s:div>
					<c:if test="${codop == 'edit'}">
						<s:div name="divElement61" cssclass="divRicMetadatiSingleRow textright">
						&nbsp;
						</s:div>
						<s:div name="divElement62" cssclass="divRicMetadatiSingleRow textright">
						&nbsp;
						</s:div>
					</c:if>
					
				</s:div>
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_cerca" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_aggiungi" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
			</s:div>
			<input type="hidden" name="codop" value="${codop}" />
			<input type="hidden" name="tx_username_hidden" value="${tx_username_hidden}" />
			<input type="hidden" name="tx_nome_hidden" value="${tx_nome_hidden}" />
			<input type="hidden" name="tx_cognome_hidden" value="${tx_cognome_hidden}" />
			<input type="hidden" name="tx_codiceFiscale_hidden" value="${tx_codiceFiscale_hidden}" />
	</s:form>
	</s:div>
	
</s:div>
