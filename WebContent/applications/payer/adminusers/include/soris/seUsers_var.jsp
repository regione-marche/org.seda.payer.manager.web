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
	
<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
	//inizio LP PG200130
	//nel caso di modifica della scadenza non serve
	//la funzione javascript, si fa il submit del form 
	function setNoteObb() {
		var uAtt = $('#utenzaAttiva').attr('checked');
		var nSca = $('#nessunaScadenza').attr('checked');
		if(uAtt && nSca) {
			$('label[for=note]').text('Note:');
		} else {
			$('label[for=note]').text('(*) Note:');
		}
	}	
	//fine LP PG200130

	
</script>

<script type="text/javascript">
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
					
				<!-- UTENZA ATTIVA -->
				
					<s:div name="divElement50" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${requestScope.reg_profilo.utenzaAttiva}" validator="ignore" 
						 cssclasslabel="bold checklabel label150" cssclass="checkleft"
						name="utenzaAttiva" groupname="utenzaAttiva" 
						text="Utenza attiva" value="Y" 
						onclick="setNoteObb();"/>
					</s:div>

				<!-- TIPOLOGIA UTENTE -->
					
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tipologiaUtente" disable="${requestScope.tipologiaDisabled}"
							cssclass="tbddl"
							cssclasslabel="label65 bold textright"
							label="(*)&nbsp;Tipolog. Utente:" valueselected="${requestScope.reg_profilo.tipologiaUtente}" 
							validator="ignore"
							onchange="setFiredButton('btnChangeTipologiaSelected');this.form.submit();"
							>
							<s:ddloption text="Selezionare un valore" value="" />
							<s:ddloption text="Ditta individuale" value="D" />
							<s:ddloption text="Persona fisica" value="F" />
							<s:ddloption text="Persona giuridica" value="G" />
						</s:dropdownlist>
						<noscript>
							<s:button id="btnChangeTipologiaSelected"
								onclick="" text="" validate="false" 
								type="submit" cssclass="btnimgStyle"
								 />
						</noscript>
					</s:div>
					
					<!-- OPERATORE BACK OFFICE -->
					<!-- ********NASCOSTO******* -->
					<c:if test="false">
						<s:div name="divOpeBackOffice" cssclass="divRicMetadatiSingleRow">
							<s:list bradio="false" bchecked="${requestScope.reg_profilo.operatoreBackOffice}" validator="ignore" 
							 cssclasslabel="bold checklabel label150" cssclass="checkleft"
							name="operatoreBackOffice" groupname="operatoreBackOffice" 
							text="Operatore back office" value="Y" />
						</s:div> 
					</c:if>
					
				</s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">

				<!-- USERID -->

					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<c:if test="${codop == 'add'}">
							<s:textbox bmodify="${usernameModificabile}" name="tx_username" 
								label="(*)&nbsp;UserId:" maxlenght="50" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;minlength=8;accept=^[a-zA-Z0-9]{8,50}$"
								message="[accept=UserId: ${msg_configurazione_alfanumerici}]"
								text="${requestScope.reg_profilo.username}" />
						</c:if>
						<c:if test="${codop == 'edit'}">
							<s:textbox bmodify="${usernameModificabile}" name="tx_username" 
								label="(*)&nbsp;UserId:" maxlenght="50" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman border_none" 
								text="${requestScope.reg_profilo.username}" />
						</c:if>
						<!-- PASSWORD AUTOGENERATA -->

						<c:if test="${codop == 'add'}">
						<!-- Flag per la generazione automatica della password -->
							<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:list bradio="false" bchecked="${requestScope.reg_profilo.passwordAutogenerata}" validator="ignore" 
								 cssclasslabel="bold checklabel label150" cssclass="checkleft"
								 onclick="setFiredButton('tx_button_password_autogenerata_changed');this.form.submit();"
								name="passwordAutogenerata" groupname="passwordAutogenerata" 
								text="Password autogenerata" value="Y" />
								<noscript>
									<s:button id="tx_button_password_autogenerata_changed"
										onclick="" text="" validate="false" 
										type="submit" cssclass="btnimgStyle"
										 />
								</noscript>
							</s:div>
						</c:if>

				<!-- PASSWORD -->

						<c:if test="${codop == 'add'}">
							<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="${!requestScope.reg_profilo.passwordAutogenerata}" name="tx_password" 
										label="(*)&nbsp;Password:" maxlenght="15" bpassword="true" 
										cssclasslabel="label85 bold textright" 
										cssclass="textareaman" validator="ignore;minlength=8;accept=^[a-zA-Z0-9\.,!#%]{8,15}$"
										message="[accept=Password: ${msg_configurazione_descrizione_2}]"
										text="${requestScope.reg_profilo.password}" />
							</s:div>
						</c:if>
					</s:div>				
				</s:div>
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<!-- Flag per la scadenza dell'utenza  -->
					<s:div name="divElement48" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${requestScope.reg_profilo.nessunaScadenza}" validator="ignore" 
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
									cssclass="dateman" disabled="${requestScope.reg_profilo.nessunaScadenza}"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${requestScope.reg_profilo.dataFineValiditaUtenza}"></s:date>
								<input type="hidden" id="dataScadenza_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
					
				
					
				</s:div>
			</s:div>
			<s:div name="divElement16" cssclass="divRicMetadatiSingleRow">
				<c:if test="${requestScope.reg_profilo.utenzaAttiva && requestScope.reg_profilo.nessunaScadenza}"> 
						<s:textbox bmodify="true" name="note"
							label="Note:" maxlenght="256" 
							cssclasslabel="note65 bold textright"
							cssclass="note" validator="ignore"
							text="${requestScope.reg_profilo.note}" />
				</c:if>							
				<c:if test="${!requestScope.reg_profilo.utenzaAttiva || !requestScope.reg_profilo.nessunaScadenza}"> 
						<s:textbox bmodify="true" name="note"
							label="(*)&nbsp;Note:" maxlenght="256" 
							cssclasslabel="note65 bold textright"
							cssclass="note" validator="ignore"
							text="${requestScope.reg_profilo.note}" />
				</c:if>							
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_cerca" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_avanti" onclick="" text="Avanti" type="submit" cssclass="btnStyle" />
			</s:div>
			<input type="hidden" name="codop" value="${codop}" />
			<input type="hidden" name="tx_username_hidden" value="${requestScope.reg_profilo.username}" />
			<input type="hidden" name="tx_tipologiautente_hidden" value="${requestScope.reg_profilo.tipologiaUtente}" />
		</s:form>
	</s:div>
	
</s:div>
