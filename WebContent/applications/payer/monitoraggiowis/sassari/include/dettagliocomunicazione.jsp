<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="dettagliocomunicazione" encodeAttributes="true" />

<fmt:setLocale value="it_IT" />
<s:div name="divDettaglio" cssclass="divDettaglio">
	<s:table id="tableTestataComunicazione" border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglio1">
		<s:thead>
			<s:tr>
				<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="4">Comunicazione Imposta Soggiorno</s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Comune:</s:td>
				<s:td>${comune}</s:td>
				<s:td cssclass="seda-ui-cellheader">Numero Autorizzazione:</s:td>
				<s:td>${numeroautorizzazione}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Periodo:</s:td>
				<s:td>da ${dataInizioComunicazione} a ${dataFineComunicazione}</s:td>
				<s:td cssclass="seda-ui-cellheader">Giorni Periodo:</s:td>
				<s:td cssclass="text_align_right">${testatacomunicazione.numeroGiorniPeriodoPermanenzaTotale}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Data Inserimento:</s:td>
				<s:td>${dataInserimentoComunicazione}</s:td>
				<s:td cssclass="seda-ui-cellheader">Data Scadenza:</s:td>
				<s:td>${dataScadenzaComunicazione}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Tipo Comunicazione:</s:td>
				<s:td>
					<c:choose>
						<c:when test="${testatacomunicazione.tipoComunicazione == 'ORDINARIA'}">
							Ordinaria
						</c:when>
						<c:when test="${testatacomunicazione.tipoComunicazione == 'INTEGRATIVA'}">
							Integrativa
						</c:when>
					</c:choose>
				</s:td>
				<s:td cssclass="seda-ui-cellheader">Stato Comunicazione:</s:td>
				<s:td>
					<c:choose>
						<c:when test="${testatacomunicazione.statoComunicazione == 'DA_INVIARE_AD_ENTRATE'}">
							In compilazione
						</c:when>
						<c:when test="${testatacomunicazione.statoComunicazione == 'INVIATA_AD_ENTRATE'}">
							Acquisizione in corso
						</c:when>
						<c:when test="${testatacomunicazione.statoComunicazione == 'CARICATA_SU_ENTRATE'}">
							Acquisita
						</c:when>
						<c:when test="${testatacomunicazione.statoComunicazione == 'ANNULLATA'}">
							Annullata
						</c:when>
					</c:choose>
				</s:td>			
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Numero Documento:</s:td>
				<s:td>${testatacomunicazione.numeroDocumentoGestionaleEntrate}</s:td>
				<s:td cssclass="seda-ui-cellheader">Numero Bollettino PagoPA:</s:td>
				<s:td>${testatacomunicazione.numeroAvvisoPagoPA}</s:td>
			</s:tr>	
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Modalit&agrave; Pagamento:</s:td>
				<s:td icol="3">
					<c:choose>
						<c:when test="${testatacomunicazione.modalitaPagamento == 'BOLLETTINO'}">
							Bollettino di Pagamento PagoPa
						</c:when>
						<c:when test="${testatacomunicazione.modalitaPagamento == 'RID'}">
							- Errore -
						</c:when>
					</c:choose>
				</s:td>
			</s:tr>	
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Stato Pagamento:</s:td>
				<s:td>
					<c:choose>
						<c:when test="${testatacomunicazione.statoDocumento == 'PAGATO'}">
							Pagato
						</c:when>
						<c:otherwise>
							Non Pagato
						</c:otherwise>
					</c:choose>
				</s:td>
				<s:td cssclass="seda-ui-cellheader">Data Pagamento:</s:td>
				<s:td>${dataPagamento}</s:td>
			</s:tr>	
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Username Operatore:</s:td>
				<s:td icol="3">${testatacomunicazione.usernameUtenteUltimoAggiornamento}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Note Contribuente:</s:td>
				<s:td icol="3">${testatacomunicazione.noteAggiuntive}</s:td>
			</s:tr>
		</s:tbody>
	</s:table>
	
	<s:table id="tableAnagraficaStruttura" border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglioStruttura">
		<s:thead>
			<s:tr>
				<s:th cssclass="seda-ui-datagrid thDettaglioTitle2" icol="4">Dati Struttura Ricettiva</s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Ragione Sociale:</s:td>
				<s:td>${anagraficastrutturacomunicazione.ragioneSocialeStruttura}</s:td>
				<s:td cssclass="seda-ui-cellheader">Codice Fiscale / Partita IVA:</s:td>
				<s:td>${anagraficastrutturacomunicazione.partitaIVAStruttura}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Insegna:</s:td>
				<s:td>${anagraficastrutturacomunicazione.insegnaStruttura}</s:td>
				<s:td cssclass="seda-ui-cellheader">Tipo Struttura:</s:td>
				<s:td>${tipologiastrutturacomunicazione.descrizioneTipologiaStruttura}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Indirizzo:</s:td>
				<s:td>${anagraficastrutturacomunicazione.indirizzoStruttura}</s:td>
				<s:td cssclass="seda-ui-cellheader">Cap:</s:td>
				<s:td>${anagraficastrutturacomunicazione.capStruttura}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">E-mail:</s:td>
				<s:td icol="3">${anagraficastrutturacomunicazione.email}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Denominazione Titolare:</s:td>
				<s:td>${anagraficastrutturacomunicazione.anagraficaTitolare}</s:td>
				<s:td cssclass="seda-ui-cellheader">Codice Fiscale Titolare:</s:td>
				<s:td>${anagraficastrutturacomunicazione.codFiscaleTitolare}</s:td>
			</s:tr>	
		</s:tbody>
	</s:table>
	
	<s:div cssclass="thDettaglioTitle2 divTitleSimilTable" name="divTitleDett">Dettaglio Comunicazione</s:div>
	
	<s:datagrid cssclass="dgDettaglioComunicazione" cachedrowset="listdettaglicomunicazione" action="" usexml="true" border="0">
		<s:dgcolumn index="1" label="Soggetto" css="textleft"/>
		<s:dgcolumn index="8" label="Fascia" css="textleft" />
		<s:dgcolumn index="7" label="Importo<br />Giornaliero (&#8364;)" css="textright" />
		<s:dgcolumn index="2" label="N° Pernottamenti" css="textright" />
		<s:dgcolumn index="3" label="N° Pernottamenti <br />soggetti ad imposta" css="textright" />
		<s:dgcolumn index="4" label="N° Ospiti" css="textright" />
		<s:dgcolumn index="5" label="Importo da pagare" css="textright" />
	</s:datagrid>		
		
</s:div>
<br/>
<s:form name="frmIndietro" action="ritorna.do?vista=monitoraggiowis" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
	<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
		<s:button id="tx_button_back" type="submit" text="Indietro" onclick="" cssclass="btnStyle" />
	</s:div>
</s:form>






