<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="walletanagraficacontribuentiedit" encodeAttributes="true" />

<fmt:setLocale value="it_IT" />
<s:form name="frmIndietro" action="walletanagraficacontribuentiedit.do?vista=walletanagraficacontribuenti" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

<s:div name="divDettaglio" cssclass="divDettaglio">
	<s:table id="tableTestataComunicazione" border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglio1">
		<s:thead>
			<s:tr>
				<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="4">Dettaglio Anagrafica Contribuente Selezionata</s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Identificativo Borsellino:</s:td>
				<s:td>${wallet.idWallet}</s:td>
				<s:td cssclass="seda-ui-cellheader">Codice Fiscale:</s:td>
				<s:td>${wallet.codiceFiscaleGenitore}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Denominazione:</s:td>
				<s:td>
					<s:textbox bmodify="true" name="tx_denominazioneGenitore"
						label="" maxlenght="50" 
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" validator="maxlength=61"
						text="${wallet.denominazioneGenitore}" />
				
				</s:td>
				
				<s:td cssclass="seda-ui-cellheader">E-mail:</s:td>
				<s:td>
					<s:textbox bmodify="true" name="tx_indirizzoEmail"
						label="" maxlenght="50" 
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" validator="maxlength=50"
						text="${wallet.indirizzoEmail}" />
				
				</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Numero cellulare:</s:td>
				<s:td>
					<s:textbox bmodify="true" name="tx_numeroCell"
						label="" maxlenght="10" 
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" validator="ignore;accept=^[0-9]{1,18}$"
						message="[accept=Cellulare: ${msg_configurazione_numero}]"
						text="${wallet.numeroCell}" />
				</s:td>
				
				<s:td cssclass="seda-ui-cellheader">Anag. da Bonificare:</s:td>
				<s:td>
					<c:choose>
							<c:when test="${wallet.anagraficaDaBonificare == 'C'}">
								Cap non valido
							</c:when>
							<c:when test="${wallet.anagraficaDaBonificare == 'R'}">
								Anagrafica Rivestita
							</c:when>
							<c:when test="${wallet.anagraficaDaBonificare == 'M'}">
								Anagrafica non rivestita
							</c:when>
							<c:when test="${wallet.anagraficaDaBonificare == 'A'}">
								Anag. estera con coutrycode non valido
							</c:when>
							<c:when test="${wallet.anagraficaDaBonificare == 'S'}">
								Indirizzo troncato in fase di stampa
							</c:when>
							<c:when test="${wallet.anagraficaDaBonificare == 'I'}">
								Indirizzo incompleto
							</c:when>
							<c:otherwise>
								${wallet.anagraficaDaBonificare}
							</c:otherwise>
						</c:choose>
				</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Indirizzo:</s:td>
				<s:td>
					<s:textbox bmodify="true" name="tx_indirizzo"
						label="" maxlenght="50" 
						cssclasslabel="label85 bold textright"
						cssclass="textareaman"
						message="[accept=Indirizzo: ${msg_configurazione_descrizione_3}]"
						text="${wallet.inidirizzoGenitore}" />				
				</s:td>
				<s:td cssclass="seda-ui-cellheader">Comune:</s:td>
				<s:td>
					<s:textbox bmodify="true" name="tx_comune"
						label="" maxlenght="50" 
						cssclasslabel="label85 bold textright"
						cssclass="textareaman"
						message="[accept=Comune: ${msg_configurazione_descrizione_3}]"
						text="${wallet.denominazioneComuneGenitore}" />				
				</s:td>
			</s:tr>

			<s:tr>
				<s:td cssclass="seda-ui-cellheader">CAP:</s:td>
				<s:td>
					<s:textbox bmodify="true" name="tx_cap"
						label="" maxlenght="50" 
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" validator="ignore;accept=^[0-9]{1,18}$"
						message="[accept=Cap: ${msg_configurazione_numero}]"
						text="${wallet.capComuneGenitore}" />				
				</s:td>
				<s:td cssclass="seda-ui-cellheader">Provincia:</s:td>
				<s:td>
					<s:textbox bmodify="true" name="tx_provincia"
						label="" maxlenght="50" 
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" validator="ignore;accept=^[a-zA-Z\-]{1,18}$"
						message="[accept=Provincia: ${msg_configurazione_descrizione_3}]"
						text="${wallet.provinciaGenitore}" />				
				</s:td>
			</s:tr>
						
			
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Stato Attivazione:</s:td>
				<s:td>
					<c:choose>
							<c:when test="${chk_FlagStatoAttivazione=='Y'}">
								Attivo 
							</c:when>
							<c:when test="${chk_FlagStatoAttivazione=='N'}">
								Non Attivo
							</c:when>
						</c:choose>			
				</s:td>
				<s:td cssclass="seda-ui-cellheader">Welcome Kit</s:td>
				<s:td>
						<c:choose>
							<c:when test="${chk_FlagWelcomeKit=='Y'}">
								Prodotto 
							</c:when>
							<c:when test="${chk_FlagWelcomeKit=='N'}">
								Non Prodotto
							</c:when>
						</c:choose>				
				</s:td>
			</s:tr>
			
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Codice SEPA: </s:td>
				<s:td>${wallet.codiceRid}</s:td>
				<s:td cssclass="seda-ui-cellheader">Stato</s:td>
				<s:td>
				
					<c:choose>
						<c:when test="${wallet.attributes.stato=='INS'}">
							Da Inviare 
						</c:when>
						<c:when test="${wallet.attributes.stato=='INV'}">
							Inviata
						</c:when>
						<c:when test="${wallet.attributes.stato=='SAU'}">
							Esito Positivo 
						</c:when>
						<c:when test="${wallet.attributes.stato=='NAU'}">
							Esito Negativo
						</c:when>
						<c:when test="${wallet.attributes.stato=='REV'}">
							Revocata
						</c:when>
						<c:when test="${wallet.attributes.stato=='RES'}">
							Revocata Da Inviare
						</c:when>
						<c:when test="${wallet.attributes.stato=='ANN'}">
							Annullata
						</c:when>
					</c:choose>		
			</s:td>
			</s:tr>

			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Data Attivazione: </s:td>
				<s:td>${wallet.attributes.dataAttivazione}</s:td>
				<s:td cssclass="seda-ui-cellheader">Estratto Conto</s:td>
				<s:td>
					<c:choose>
						<c:when test="${chk_FlgEstrattoConto=='Y'}">
							Presente 
						</c:when>
						<c:when test="${chk_FlgEstrattoConto=='N'}">
							Non Presente
						</c:when>
					</c:choose>	
				</s:td>
			</s:tr>
			
		</s:tbody>
	</s:table>
	
	<s:div cssclass="thDettaglioTitle2 divTitleSimilTable" name="divTitleDett">Lista Bollettino</s:div>
	<s:datagrid cssclass="lista_servizi_wallet" cachedrowset="lista_dettaglio_bollettini" action="" usexml="true" border="0">
		<s:dgcolumn index="1" label="Data" css="textleft"/>
		<s:dgcolumn index="2" label="ID Bollettino" css="textleft" />
		<s:dgcolumn index="3" label="Importo" css="textright" format="#0.00"/>
		<s:dgcolumn index="4" label="Tipo Bollettino" css="textleft" />
		<s:dgcolumn index="5" label="Tipo Generazione" css="textleft" />
		
		
	</s:datagrid>	
</s:div>
<br/>
	<input type="hidden" name="codop" value="aggiorna" />
	<input type="hidden" name="tx_societa" value="${wallet.codiceSocieta}" />
				<input type="hidden" name="tx_utente" value="${wallet.cuteCute}" />
				<input type="hidden" name="tx_ente" value="${wallet.chiaveEnte}" />
				<input type="hidden" name="tx_idwallet" value="${wallet.idWallet}" />
	<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
		<s:button id="tx_button_indietro" type="submit" text="Indietro" onclick="" validate="false"  cssclass="btnStyle" />
		<s:button id="tx_button_edit" type="submit" text="Aggiorna" onclick="" cssclass="btnStyle" />
	</s:div>
</s:form>






