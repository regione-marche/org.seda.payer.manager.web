<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="walletservizioedit" encodeAttributes="true" />

<fmt:setLocale value="it_IT" />
<s:form name="frmIndietro" action="walletservizioedit.do?vista=walletservizio" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

<s:div name="divDettaglio" cssclass="divDettaglio">
	<s:table id="tableTestataComunicazione" border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglio1">
		<s:thead>
			<s:tr>
				<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="4">Dettaglio Servizio Selezionato</s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Servizio:</s:td>
				<s:td>${descServ}</s:td>
				<s:td cssclass="seda-ui-cellheader">Anno/Mese del Carico:</s:td>
				<s:td>${amcar}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Codice Fiscale:</s:td>
				<s:td>${wallet.codiceFiscaleGenitore}</s:td>
				<s:td cssclass="seda-ui-cellheader">Codice Borsellino:</s:td>
				<s:td>${wallet.idWallet}</s:td>
			</s:tr>
		</s:tbody>
	</s:table>
	
	<s:div cssclass="thDettaglioTitle2 divTitleSimilTable" name="divTitleDett">Dettaglio</s:div>
	<s:datagrid cssclass="lista_servizi_wallet" cachedrowset="lista_dettaglio_servizi" action="" usexml="true" border="0">
		<s:dgcolumn index="1" label="C.F. Figlio" css="textleft"/>
		<s:dgcolumn index="2" label="Scuola" css="textleft" />
		<s:dgcolumn index="3" label="Tariffa" css="textright" format="#0.00"/>
		<s:dgcolumn index="4" label="Tipologia Tariffa" css="textleft" />
		<s:dgcolumn index="5" label="Tributo" css="textleft" />
		<s:dgcolumn index="6" format="#0.00" label="Importo Carico" css="textright" />
		<s:dgcolumn index="7" format="#0.00" label="Importo Pagato" css="textright" />
		<s:dgcolumn index="8" format="#0.00" label="Importo Residuo" css="textright" />
		<s:dgcolumn index="9" format="#0.00" label="Importo Rendicontato" css="textright" />
		
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
	</s:div>
</s:form>






