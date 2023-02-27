<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="walletmonitoraggioedit" encodeAttributes="true" />

<fmt:setLocale value="it_IT" />
<s:form name="frmIndietro" action="walletmonitoraggioedit.do?vista=walletmonitoraggio" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

<s:div name="divDettaglio" cssclass="divDettaglio">
	<s:table id="tableTestataComunicazione" border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglio1">
		<s:thead>
			<s:tr>
				<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="4">Dettaglio Borsellino Selezionato</s:th>
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
				<s:td cssclass="seda-ui-cellheader">SMS di Cortesia:</s:td>
				<s:td>${wallet.attributes.SMSCORTESIAINVIATI}</s:td>
				<s:td cssclass="seda-ui-cellheader">SMS di Sollecito:</s:td>
				<s:td>${wallet.attributes.SMSSOLLECITOINVIATI}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Solleciti e-mail inviati:</s:td>
				<s:td>${wallet.attributes.EMAILSOLLECITOINVIATE}</s:td>
				<s:td cssclass="seda-ui-cellheader">Solleciti Cartacei emessi:</s:td>
				<s:td>${wallet.attributes.SOLLECATICARTACEI}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Data Ultimo Sollecito:</s:td>
				<s:td>${wallet.attributes.DATAULTIMOSOLLECITO}</s:td>
				<s:td cssclass="seda-ui-cellheader">Importo Oneri di stampa/postalizzazione:</s:td>
				<s:td><fmt:formatNumber type="NUMBER" value="${wallet.attributes.IMPORTOONERISTAMPACARICATI}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
				
				
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Importo Oneri di stampa/postalizzazione pagati:</s:td>
				<s:td><fmt:formatNumber type="NUMBER" value="${wallet.attributes.IMPORTOONERISTAMPAPAGATI}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
				<s:td cssclass="seda-ui-cellheader">Importo Oneri Rendicontati:</s:td>
				<s:td><fmt:formatNumber type="NUMBER" value="${wallet.attributes.IMPORTOONERIRENDICONTATI}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
			</s:tr>
			<s:tr>
				<s:td>
					
						<s:list bradio="false" bchecked="${chk_FlagEsclusioneSMSCortesia}" validator="ignore" 
						 cssclasslabel="bold checklabel label200" cssclass="" 
						name="FlagEsclusioneSMSCortesia" groupname="FlagEsclusioneSMSCortesia" 
						text="Esclusione SMS Cortesia" value="Y" disable=""  /> 
				</s:td>
				<s:td>
					
						<s:list bradio="false" bchecked="${chk_FlagEsclusioneSMSSollecito}" validator="ignore" 
						 cssclasslabel="bold checklabel label200" cssclass="" 
						name="FlagEsclusioneSMSSollecito" groupname="FlagEsclusioneSMSSollecito" 
						text="Esclusione SMS Sollecito" value="Y" disable=""  /> 
				</s:td>
				<s:td>
					
						<s:list bradio="false" bchecked="${chk_FlagEsclusioneSollecitoCartaceo}" validator="ignore" 
						 cssclasslabel="bold checklabel label200" cssclass="" 
						name="FlagEsclusioneSollecitoCartaceo" groupname="FlagEsclusioneSollecitoCartaceo" 
						text="Esclusione Sollecito Cartaceo" value="Y" disable=""  /> 
				</s:td>
				<s:td>
						<s:list bradio="false" bchecked="${chk_FlagEsclusioneEvoluzioneIntimazione}" validator="ignore" 
						 cssclasslabel="bold checklabel label200" cssclass="" 
						name="FlagEsclusioneEvoluzioneIntimazione" groupname="FlagEsclusioneEvoluzioneIntimazione" 
						text="Esclusione Evoluzione Intimazione" value="Y" disable=""  /> 
					
				</s:td>
			</s:tr>
			
		</s:tbody>
	</s:table>
	
	<s:div cssclass="thDettaglioTitle2 divTitleSimilTable" name="divTitleDett">Dettaglio per Servizio</s:div>
	<s:datagrid cssclass="lista_servizi_wallet" cachedrowset="lista_servizi_wallet" action="" usexml="true" border="0">
		<s:dgcolumn index="8" label="Descrizione Servizio" css="textleft"/>
		<s:dgcolumn index="3" label="Importo Carico" css="textright" format="#0.00" />
		<s:dgcolumn index="4" label="Importo Pagato" css="textright" format="#0.00" />
		<s:dgcolumn index="5" label="Importo Residuo" css="textright" format="#0.00" />
		<s:dgcolumn index="6" label="Importo Rendicontato" css="textright" format="#0.00" />
		
	</s:datagrid>		
	
</s:div>
<br/>
	<input type="hidden" name="codop" value="aggiorna" />
	<input type="hidden" name="tx_societa" value="${wallet.codiceSocieta}" />
				<input type="hidden" name="tx_utente" value="${wallet.cuteCute}" />
				<input type="hidden" name="tx_ente" value="${wallet.chiaveEnte}" />
				<input type="hidden" name="tx_idwallet" value="${wallet.idWallet}" />
	<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
		<s:button id="tx_button_indietro" type="submit" text="Indietro" onclick="" cssclass="btnStyle" />
		<s:button id="tx_button_edit" type="submit" text="Aggiorna" onclick="" cssclass="btnStyle" />
	</s:div>
</s:form>






