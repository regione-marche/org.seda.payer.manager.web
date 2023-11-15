<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="adminusers_var" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js" type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js" type="text/javascript"></script>

<script src="../applications/templates/adminusers/js/jquery.autocomplete.js" type="text/javascript"></script>
<script src="../applications/templates/adminusers/js/autocomplete.js" type="text/javascript"></script>

<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>

<c:if test="${codop == 'add'}">
	<script type="text/javascript">
		$(document).ready(function(){
			//loadAutoCompletion("tx_username");
		});
	</script>
</c:if>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_form" action="${do_command_name}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Inserimento Nuovo Profilo - Primo step</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Modifica Profilo - Primo step</s:div>
			</c:if>
						
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					
				<!-- PROFILO ATTIVO -->
				
					<s:div name="divElement50" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_utenzaAttiva}" validator="ignore" 
						 cssclasslabel="bold checklabel label150" cssclass="checkleft"
						name="utenzaAttiva" groupname="utenzaAttiva" 
						text="Profilo attivo" value="Y" />
					</s:div>

				<!-- SOCIETA' -->
					
					<s:div name="divElement12" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_societa" disable="${ddlSocietaDisabled}"
							cssclass="tbddl floatleft" label="Societ&agrave;:"
							cssclasslabel="label65 bold floatleft textright"
							cachedrowset="listaSocieta" usexml="true"
							onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
							valueselected="${tx_societa}">
							<s:ddloption text="Tutte le Societ&agrave;" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_societa_changed" validate="false" 
								disable="${ddlSocietaDisabled}" onclick="" text=""
								type="submit" cssclass="btnimgStyle"/>
						</noscript>
					</s:div>
					
				<!-- ENTE DI PERTINENZA -->
					
					<s:div name="divElement15" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_pertinenza" disable="false"
							cssclass="tbddl floatleft" cssclasslabel="label65 bold floatleft textright" 
							label="Pertinenza:" valueselected="${tx_pertinenza}"	>
							<s:ddloption text="Tutti gli enti" value="" />
							<s:ddloption text="Comune di Beinasco" value="81721" />
                            <s:ddloption text="Comune di Grugliasco" value="81711" />
							<s:ddloption text="Comune di San Mauro Torinese" value="81030" />
                            <s:ddloption text="Comune di Torino" value="06954" />
                            <s:ddloption text="Regione Piemonte" value="73348" />
							<!-- FINE SVILUPPO_001_LP -->
						</s:dropdownlist>
					</s:div>
				</s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">

				<!-- USERID -->

					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<c:if test="${codop == 'add'}">
							<s:textbox bmodify="${usernameModificabile}" name="tx_username" 
								label="UserId:" maxlenght="50" showrequired="true"
								cssclasslabel="label65 bold textright"
								cssclass="textareaman" validator="required;minlength=8;maxlength=50;accept=^[a-zA-Z0-9]{8,50}$"
								message="[accept=UserId: ${msg_configurazione_alfanumerici}]"
								text="${tx_username}" />
						</c:if>
						<c:if test="${codop == 'edit'}">
							<s:textbox bmodify="${usernameModificabile}" name="tx_username" 
								label="UserId:" maxlenght="50" showrequired="true"
								cssclasslabel="label65 bold textright"
								cssclass="textareaman border_none" validator="required;minlength=8;maxlength=50;accept=^[a-zA-Z0-9]{8,50}$"
								message="[accept=UserId: ${msg_configurazione_alfanumerici}]"
								text="${tx_username}" />
						</c:if>
					</s:div>

					<!-- UTENTE -->
				
					<s:div name="divElement13" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_utente" disable="${ddlUtenteDisabled}"
							cssclass="tbddl floatleft" label="Utente:"
							cssclasslabel="label65 bold floatleft textright"
							cachedrowset="listaUtenti" usexml="true"
							onchange="setFiredButton('tx_button_utente_changed');this.form.submit();"
							valueselected="${tx_utente}">
							<s:ddloption text="Tutti gli utenti" value="" />
							<s:ddloption text="{2}" value="{3}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_utente_changed" validate="false" 
								disable="${ddlUtenteDisabled}" onclick="" text=""
								type="submit" cssclass="btnimgStyle"/>
						</noscript>
					</s:div>
									
				</s:div>

				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">

				<!-- PROFILO UTENTE -->
					
					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_userprofile" disable="${ddlProfiloUtenteDisabled}"
							cssclass="tbddl floatleft" validator="required" showrequired="true"
							cssclasslabel="label65 bold floatleft textright" 
							onchange="setFiredButton('tx_button_profilo_utente_changed');this.form.submit();"
							label="Profilo:" valueselected="${tx_userprofile}"	>
							<s:ddloption text="Selezionare un valore" value="" />
							<s:ddloption text="AMMI - Amm.C.S.I." value="AMMI" />
							<s:ddloption text="AMSO - Amm.Societ&agrave;" value="AMSO" />
							<s:ddloption text="AMUT - Amm.Utente" value="AMUT" />
							<s:ddloption text="AMEN - Amm.Ente" value="AMEN" />
							<s:ddloption text="PYCO - Contribuente" value="PYCO" />
							<s:ddloption text="OPER - Operatore" value="OPER" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_profilo_utente_changed"
								onclick="" text="" validate="false" 
								type="submit" cssclass="btnimgStyle"/>
						</noscript>
					</s:div>
					
				<!-- ENTE -->
					
					<s:div name="divElement14" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_ente" disable="${ddlEnteDisabled}"
							cssclass="tbddl floatleft" label="Ente:"
							cssclasslabel="label65 bold floatleft textright"
							onchange="setFiredButton('tx_button_ente_changed');this.form.submit();"
							cachedrowset="listaEnti" usexml="true"
							valueselected="${tx_ente}">
							<s:ddloption text="Tutti gli enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_ente_changed" validate="false" 
								disable="${ddlEnteDisabled}" onclick="" text=""
								type="submit" cssclass="btnimgStyle"/>
						</noscript>
					</s:div>
					
					<!-- GRUPPO AGENZIA -->
					<s:div name="divElement15" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_gruppo_agenzia" disable=""
							cssclass="tbddl floatleft" label="Gruppo Agenzia:"
							cssclasslabel="label65 bold floatleft textright"
							onchange=""
							cachedrowset="listaGruppiAgenzia" usexml="true"
							valueselected="${tx_gruppo_agenzia}">
							<s:ddloption text="Seleziona..." value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_gruppo_agenzia_changed" validate="false" 
								disable="" onclick="" text=""
								type="submit" cssclass="btnimgStyle"/>
						</noscript>
					</s:div>
					
				</s:div>
					
			</s:div>
			
			<!-- TIPOLOGIE SERVIZIO -->
			<s:div name="divTipologieServizio" cssclass="profilo_divRicMetadati">
				
				<s:div name="divElement60" cssclass="profilo_divTipologiaServizioLeft">
					<s:dropdownlist name="tx_tipologia_servizio" disable="${ddlTipologieServizioDisabled}"
						cssclass="profilo_ddlTipologiaServizio" cssclasslabel="profilo_ddlTipologiaServizioLabel"
						label="Tipologie servizio disponibili:" size="10" multiple="true"> 
						<s:ddloptionbinder options="${listTipologieServizio}"/>
					</s:dropdownlist>
				</s:div>
				
				<s:div name="divElement61" cssclass="profilo_divTipologiaServizioButtons">
					<s:button id="tx_button_add" disable="${addTipologiaServizioDisabled}" onclick="" validate="false" text="Aggiungi" type="submit" cssclass="profilo_btnAdd" />
					<s:button id="tx_button_remove" disable="${removeTipologiaServizioDisabled}" onclick="" validate="false" text="Rimuovi" type="submit" cssclass="profilo_btnRemove" />
				</s:div>	
				
				<!-- Tipologie servizio selezionate -->
			
				<s:div name="divElement62" cssclass="profilo_divTipologiaServizioRight">
					<s:dropdownlist name="tx_tipologia_servizio_sel" disable="${ddlTipologieServizioSelDisabled}"
						cssclass="profilo_ddlTipologiaServizio" cssclasslabel="profilo_ddlTipologiaServizioLabel"
						label="Tipologie servizio selezionate:" size="10" multiple="true">
						<s:ddloptionbinder options="${listTipologieServizioSel}"/>
					</s:dropdownlist>
				</s:div>
			</s:div>
			
			
			<s:div name="divRicMetadatiBottom" cssclass="divRicMetadati">
			
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<!--        FLAG GESTIONE MULTIUTENTE               -->

				<c:choose>
					<c:when test="${tx_userprofile == 'PYCO' || tx_userprofile == 'OPER'}">
						<%--Per i profili PYCO e OPER non è prevista la gestione del multiutente --%>
						<s:div name="divElement59" cssclass="divRicMetadatiSingleRow">
							<s:list bradio="false" bchecked="${chk_abilitazioneMultiUtente}" validator="ignore" 
						 	cssclasslabel="bold checklabel label150" cssclass="checkleft"
							name="abilitazioneMultiUtente" groupname="abilitazioneMultiUtente" 
							text="Gestione multi utente" value="Y" disable="true"/>
						</s:div>
					</c:when>
					<c:otherwise>
						<s:div name="divElement59" cssclass="divRicMetadatiSingleRow">
							<s:list bradio="false" bchecked="${chk_abilitazioneMultiUtente}" validator="ignore" 
							 cssclasslabel="bold checklabel label150" cssclass="checkleft"
						     name="abilitazioneMultiUtente" groupname="abilitazioneMultiUtente" 
							 text="Gestione multi utente" value="Y" />
						</s:div>
					</c:otherwise>
				</c:choose>

				</s:div>
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div cssclass="seda-ui-div divRicMetadatiSingleRow"  name="divElement66">
					</s:div>
				</s:div>
				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div cssclass="seda-ui-div divRicMetadatiSingleRow"  name="divElement66">
					</s:div>
				</s:div>
			</s:div>
			
			
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_cerca" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
				<c:choose>
					<c:when test="${tx_userprofile == 'PYCO'}">
						<%--Per il profilo PYCO = contribuente non c'è lo step 2 --%>
						<c:if test="${codop == 'add'}">
							<s:button id="tx_button_aggiungi_end" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
						</c:if>
						<c:if test="${codop == 'edit'}">
							<s:button id="tx_button_edit_end" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
						</c:if>
					</c:when>
					<c:when test="${tx_userprofile == 'OPER'}">
						<%--Per il profilo OPER = operatore non c'è lo step 2 --%>
						<c:if test="${codop == 'add'}">
							<s:button id="tx_button_aggiungi_operatore" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
						</c:if>
						<c:if test="${codop == 'edit'}">
							<s:button id="tx_button_edit_operatore" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
						</c:if>
					</c:when>
					<c:otherwise>
						<s:button id="tx_button_avanti" onclick="" text="Avanti" type="submit" cssclass="btnStyle" />
					</c:otherwise>
				</c:choose>
			</s:div>
			<input type="hidden" name="codop" value="${codop}" />
			<input type="hidden" name="tx_chiaveutente_hidden" value="${tx_chiaveutente_hidden}" />
			<input type="hidden" name="tx_username_hidden" value="${tx_username_hidden}" />
			<input type="hidden" name="tx_userprofile_hidden" value="${tx_userprofile_hidden}" />
			<input type="hidden" name="tx_societa_hidden" value="${tx_societa_hidden}" />
			<input type="hidden" name="tx_utente_hidden" value="${tx_utente_hidden}" />
			<input type="hidden" name="tx_ente_hidden" value="${tx_ente_hidden}" />
			<input type="hidden" name="tx_codicefiscale_hidden" value="${tx_codicefiscale_hidden}" />
			<input type="hidden" name="tx_pertinenza_hidden" value="${tx_pertinenza_hidden}" />	
			<input type="hidden" name="tx_gruppo_agenzia_hidden" value="${tx_gruppo_agenzia_hidden}" />	
	</s:form>
	</s:div>
	
</s:div>