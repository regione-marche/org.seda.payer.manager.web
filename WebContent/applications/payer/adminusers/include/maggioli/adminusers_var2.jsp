<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="adminusers_var" encodeAttributes="true" />



<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>

<c:if test="${!userIsAMMI}">
	<script type="text/javascript">
	
		function toggleMonitoraggio() {
			var monitoraggio = document.getElementById('monitoraggio');
			var azioniPerTransazioniOK = document.getElementById('azioniPerTransazioniOK');
			var azioniPerTransazioniKO = document.getElementById('azioniPerTransazioniKO');
			azioniPerTransazioniOK.disabled = !monitoraggio.checked;
			azioniPerTransazioniKO.disabled = !monitoraggio.checked;
		}
		function toggleRiconciliazione() {
			var riconciliazione = document.getElementById('riconciliazione');
			var azioniPerRiconciliazioneManuale = document.getElementById('azioniPerRiconciliazioneManuale');
			azioniPerRiconciliazioneManuale.disabled = !riconciliazione.checked;
		}
		function toggleRendicontazione() {
			var rendicontazione = document.getElementById('rendicontazione');
			var downloadFlussiRendicontazione = document.getElementById('downloadFlussiRendicontazione');
			var invioFlussiRendicontazioneViaFtp = document.getElementById('invioFlussiRendicontazioneViaFtp');
			var invioFlussiRendicontazioneViaEmail = document.getElementById('invioFlussiRendicontazioneViaEmail');
			var invioFlussiRendicontazioneViaWs = document.getElementById('invioFlussiRendicontazioneViaWs');
			downloadFlussiRendicontazione.disabled = !rendicontazione.checked;
			invioFlussiRendicontazioneViaFtp.disabled = !rendicontazione.checked;
			invioFlussiRendicontazioneViaEmail.disabled = !rendicontazione.checked;
			invioFlussiRendicontazioneViaWs.disabled = !rendicontazione.checked;
		}
		function toggleRiversamento() {
			var riversamento = document.getElementById('riversamento');
			var abilitazioneProfiloRiversamento = document.getElementById('abilitazioneProfiloRiversamento');
			abilitazioneProfiloRiversamento.disabled = !riversamento.checked;
		}
			// Dom
			function toggleContogestione() {
				var contogestione = document.getElementById('contogestione');
				var mailContogestione = document.getElementById('mailContogestione');
				mailContogestione.disabled = !contogestione.checked;
			}
			
			// fine Dom

	</script>
</c:if>
<c:if test="${userIsAMMI}">
	<script type="text/javascript">
		function toggleMonitoraggio() {}
		function toggleRiconciliazione() {}
		function toggleRendicontazione() {}
		function toggleRiversamento() {}
		function toggleContogestione() {}
	</script>
</c:if>
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var2_form" action="${do_command_name}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Inserimento Nuovo Profilo - Secondo step</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Modifica Profilo - Secondo step</s:div>
			</c:if>
						
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
				
					<s:div name="divElement91" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_configurazione}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft" disable="${configurazioneDisabled}" 
						name="configurazione" groupname="configurazione" 
						text="Configurazione Applicativa" value="Y" />
					</s:div>
					<s:div name="divElement92" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_adminusers}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft" disable="${adminusersDisabled}"   
						name="adminusers" groupname="adminusers" 
						text="Gestione Utenti" value="Y" />
					</s:div>
					<s:div name="divElement931" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_log}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft"   disable="${logDisabled}" 
						name="log" groupname="log" 
						text="Log Accessi" value="Y" />
					</s:div>
					<%-- inizio LP PG21X007 --%>
					<c:if test="${chk_logrequest != null}">
						<s:div name="divElementLOGREQUEST" cssclass="divRicMetadatiSingleRow">
							<s:list bradio="false" bchecked="${chk_logrequest}" validator="ignore" 
							 cssclasslabel="bold checklabel label220" cssclass="checkleft"   disable="${logrequestDisabled}" 
							name="logrequest" groupname="logrequest" 
							text="Log Request" value="Y" />
						</s:div>
					</c:if>
					<%-- fine LP PG21X007 --%>
					<s:div name="divElement93" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_ecmanager}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft"   disable="${ecmanagerDisabled}" 
						name="ecmanager" groupname="ecmanager" 
						text="Estratto Conto Manager" value="Y" />
					</s:div>
					<s:div name="divElement93" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_ecanagrafica}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft"   disable="${ecanagraficaDisabled}" 
						name="ecanagrafica" groupname="ecanagrafica" 
						text="Estratto Conto Anagrafica" value="Y" />
					</s:div>
					
					<s:div name="divElement293" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_monitoraggioext}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft"   disable="${monitoraggioextDisabled}" 
						name="monitoraggioext" groupname="monitoraggioext" 
						text="Monitoraggio Notifiche Portali Esterni" value="Y" />
					</s:div>
					<s:div name="divElement294" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_monitoraggiosoap}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft"   disable="${monitoraggiosoapDisabled}" 
						name="monitoraggiosoap" groupname="monitoraggiosoap" 
						text="Monitoraggio Notifiche Soap" value="Y" />
					</s:div>
					<s:div name="divElement393" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_analysis}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft"   disable="${analysisDisabled}" 
						name="analysis" groupname="analysis" 
						text="Analisi Dati Multidimensionale" value="Y" />
					</s:div>
					<!--<s:div name="divElement394" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_wismanager}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft"   disable="${wismanagerDisabled}" 
						name="wismanager" groupname="wismanager" 
						text="WIS Manager" value="Y" />
					</s:div>
					<s:div name="divElementWALLETMANAGER" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_walletmanager}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft"   disable="${walletmanagerDisabled}" 
						name="walletmanager" groupname="walletmanager" 
						text="Borsellino Manager" value="Y" />
					</s:div>
					<s:div name="divElementWALLETANAGRAFICA" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_walletanagraficacontribuenti}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft"   disable="${walletanagraficacontribuentiDisabled}" 
						name="walletanagraficacontribuenti" groupname="walletanagraficacontribuenti" 
						text="Borsellino Anagrafica Contribuenti" value="Y" />
					</s:div>
					<s:div name="divElementWALLETRICARICHE" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_walletricaricheborsellino}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft"   disable="${walletricaricheborsellinoDisabled}" 
						name="walletricaricheborsellino" groupname="walletricaricheborsellino" 
						text="Borsellino Ricariche" value="Y" />
					</s:div>
					<s:div name="divElementWALLETDISCARICO" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_walletsollecitodiscarico}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft"   disable="${walletsollecitodiscaricoDisabled}" 
						name="walletsollecitodiscarico" groupname="walletsollecitodiscarico" 
						text="Borsellino Discarichi" value="Y" /> 
					</s:div>-->
					
					
				</s:div>
				
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
				
					<s:div name="divElement94" cssclass="divRicMetadatiSingleRow ">
						<s:list bradio="false" bchecked="${chk_monitoraggio}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft" onclick="toggleMonitoraggio()"
						name="monitoraggio" groupname="monitoraggio" disable="${monitoraggioDisabled}"   
						text="Monitoraggio Pagamenti" value="Y" />
					</s:div>
					<!-- "SE000SV"."PYUSRTB"."USR_FUSRTROK" -->
					<s:div name="divElement54" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_azioniPerTransazioniOK}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleftindent" disable="${monitoraggioDisabled}"   
						name="azioniPerTransazioniOK" groupname="azioniPerTransazioniOK" 
						text="Azioni per transazioni a buon fine" value="Y" />
					</s:div>
					<!-- "SE000SV"."PYUSRTB"."USR_FUSRTRKO" -->
					<s:div name="divElement55" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_azioniPerTransazioniKO}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleftindent"   disable="${monitoraggioDisabled}" 
						name="azioniPerTransazioniKO" groupname="azioniPerTransazioniKO" 
						text="Azioni per transazioni da allineare" value="Y" />
					</s:div>
					<s:div name="divElement96" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_riconciliazione}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft" onclick="toggleRiconciliazione()"
						name="riconciliazione" groupname="riconciliazione"   disable="${riconciliazioneDisabled}" 
						text="Riconciliazione Pagamenti" value="Y" />
					</s:div>
					<!-- "SE000SV"."PYUSRTB"."USR_FUSRRICO" -->
					<s:div name="divElement56" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_azioniPerRiconciliazioneManuale}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleftindent"   disable="${riconciliazioneDisabled}" 
						name="azioniPerRiconciliazioneManuale" groupname="azioniPerRiconciliazioneManuale" 
						text="Azioni per riconciliazione manuale" value="Y" />
					</s:div>
					<s:div name="divElement61" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_entrate}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft" onclick="toggleEntrate()"
						name="entrate" groupname="entrate"   disable="${entrateDisabled}" 
						text="Entrate" value="Y" />
					</s:div>
					<s:div name="divElement62" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_ruoli}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft" onclick="toggleRuoli()"
						name="ruoli" groupname="ruoli"   disable="${ruoliDisabled}" 
						text="Ruoli" value="Y" />
					</s:div>
					<!--<s:div name="divElement395" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_wisconfig}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft" disable="${wisconfigDisabled}" 
						name="wisconfig" groupname="wisconfig" 
						text="WIS Configurazione" value="Y" />
					</s:div>
					<s:div name="divElementMONITORAGGIOWALLET" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_walletmonitoraggio}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft" disable="${walletmonitoraggioDisabled}" 
						name="walletmonitoraggio" groupname="walletmonitoraggio"                  
						text="Borsellino Monitoraggio" value="Y" />
					</s:div>-->
<!-- dom -->
					<!-- "SE000SV"."PYUSRTB"."USR_FUSRCMAI" -->
					<s:div name="divElement199" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_mailContogestione}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleftindent" disable="${contogestioneDisabled}" 
						name="mailContogestione" groupname="mailContogestione" 
						text="Invio Mail Conto Gestione" value="Y" />
					</s:div>

<!-- fine dom -->
				</s:div>

				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					
					<s:div name="divElement95" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_rendicontazione}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft" onclick="toggleRendicontazione()"
						name="rendicontazione" groupname="rendicontazione" disable="${rendicontazioneDisabled}"   
						text="Rendicontazione Pagamenti" value="Y" />
					</s:div>
					<!-- "SE000SV"."PYUSRTB"."USR_FUSRRDWN" -->
					<s:div name="divElement51" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_downloadFlussiRendicontazione}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleftindent"   disable="${rendicontazioneDisabled}" 
						name="downloadFlussiRendicontazione" groupname="downloadFlussiRendicontazione" 
						text="Download file rendicontazione" value="Y" />
					</s:div>
					<!-- "SE000SV"."PYUSRTB"."USR_FUSRRFTP" -->
					<s:div name="divElement52" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_invioFlussiRendicontazioneViaFtp}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleftindent" disable="${rendicontazioneDisabled}"   
						name="invioFlussiRendicontazioneViaFtp" groupname="invioFlussiRendicontazioneViaFtp" 
						text="Invio file rendicontazione via ftp" value="Y" />
					</s:div>
					<!-- "SE000SV"."PYUSRTB"."USR_FUSRRMAI" -->
					<s:div name="divElement53" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_invioFlussiRendicontazioneViaEmail}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleftindent"   disable="${rendicontazioneDisabled}" 
						name="invioFlussiRendicontazioneViaEmail" groupname="invioFlussiRendicontazioneViaEmail" 
						text="Invio file rendicontazione via email" value="Y" />
					</s:div>
					<!-- "SE000SV"."PYUSRTB"."USR_FUSRRWS" -->
					<s:div name="divElement54" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_invioFlussiRendicontazioneViaWs}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleftindent" disable="${rendicontazioneDisabled}"   
						name="invioFlussiRendicontazioneViaWs" groupname="invioFlussiRendicontazioneViaWs" 
						text="Invio file rendicontazione via Web Service" value="Y" />
					</s:div>
					<!-- "SE000SV"."PYUSRTB"."USR_FUSRRIVE" -->
					<s:div name="divElement58" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_abilitazioneProfiloRiversamento}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleftindent" disable="${riversamentoDisabled}" 
						name="abilitazioneProfiloRiversamento" groupname="abilitazioneProfiloRiversamento" 
						text="Azioni per Riversamento" value="Y" />
					</s:div>
					<s:div name="divElement98" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_ottico}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft" onclick="toggleOttico()"
						name="ottico" groupname="ottico" disable="${otticoDisabled}"   
						text="Ottico" value="Y" />
					</s:div>
					<!-- PG150180_001 GG - inizio -->
					<s:div name="divElement493" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_monitoraggionn}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft"   disable="${monitoraggionnDisabled}" 
						name="monitoraggionn" groupname="monitoraggionn" 
						text="Monitoraggio Nodo Nazionale" value="Y" />
					</s:div>
					<s:div name="divElement593" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_riconciliazionenn}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft"   disable="${riconciliazionennDisabled}" 
						name="riconciliazionenn" groupname="riconciliazionenn" 
						text="Riconciliazione movimenti Nodo Nazionale" value="Y" />
					</s:div>
					<!-- PG150180_001 GG - fine -->
					<!--<s:div name="divElement198" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_monitoraggiowis}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft" disable="${monitoraggiowisDisabled}" 
						name="monitoraggiowis" groupname="monitoraggiowis" 
						text="WIS Monitoraggio" value="Y" />
					</s:div>  
					<s:div name="divElementWALLETSERVIZIO" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_walletconfig}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft" disable="${walletconfigDisabled}" 
						name="walletconfig" groupname="walletconfig" 
						text="Borsellino Configurazione" value="Y" />
					</s:div>  
					<s:div name="divElementWALLETSERVIZIO" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_walletservizio}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft" disable="${walletservizioDisabled}" 
						name="walletservizio" groupname="walletservizio" 
						text="Borsellino Monitoraggio per Servizio" value="Y" />
					</s:div>  
					<s:div name="divElementBLACKBOXSERVIZIO" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_blackboxconfig}" validator="ignore" 
						 cssclasslabel="bold checklabel label220" cssclass="checkleft" disable="${blackboxconfigDisabled}" 
						name="blackboxconfig" groupname="blackboxconfig" 
						text="Configurazione Black Box" value="Y" />
					</s:div>  -->

					<%-- inizio LP PG200360 --%>
					<c:if test="${chk_serviziattivi != null}">
						<s:div name="divElementSERVIZIATTIVI" cssclass="divRicMetadatiSingleRow">
							<s:list bradio="false" bchecked="${chk_serviziattivi}" validator="ignore" 
							 cssclasslabel="bold checklabel label220" cssclass="checkleft"   disable="${serviziattiviDisabled}" 
							name="serviziattivi" groupname="serviziattivi" 
							text="Servizi Attivi" value="Y" />
						</s:div>
					</c:if>
					<%-- fine LP PG200360 --%>

				</s:div>


			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_step1" onclick="" text="Indietro" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
				<c:if test="${codop == 'add'}">
					<s:button id="tx_button_aggiungi" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${codop == 'edit'}">
					<s:button id="tx_button_edit" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
			</s:div>
			<input type="hidden" name="codop" value="${codop}" />
			<input type="hidden" name="tx_username_hidden" value="${tx_username_hidden}" />
			<input type="hidden" name="tx_userprofile_hidden" value="${tx_userprofile_hidden}" />
			<input type="hidden" name="tx_societa_hidden" value="${tx_societa_hidden}" />
			<input type="hidden" name="tx_utente_hidden" value="${tx_utente_hidden}" />
			<input type="hidden" name="tx_ente_hidden" value="${tx_ente_hidden}" />
			<input type="hidden" name="tx_nome_hidden" value="${tx_nome_hidden}" />
			<input type="hidden" name="tx_cognome_hidden" value="${tx_cognome_hidden}" />
			<input type="hidden" name="tx_codiceFiscale_hidden" value="${tx_codiceFiscale_hidden}" />
	</s:form>
	</s:div>
	<script type="text/javascript">
		toggleMonitoraggio();
		toggleRiconciliazione();
		toggleRendicontazione();
		toggleRiversamento();
		toggleContogestione();
	</script>
	
</s:div>
