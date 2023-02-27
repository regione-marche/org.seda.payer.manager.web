<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="walletricaricheborsellinoedit" encodeAttributes="true" />

<fmt:setLocale value="it_IT" />
<s:form name="frmIndietro" action="walletricaricheborsellino.do?vista=walletricaricheborsellino" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

<s:div name="divDettaglio" cssclass="divDettaglio">
	<s:table id="tableTestataComunicazione" border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglio1">
		<s:thead>
			<s:tr>
				<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="4">Dettaglio Borsellino Selezionato</s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Codice Borsellino:</s:td>
				<s:td>${codice_borsellino}</s:td>
				<s:td cssclass="seda-ui-cellheader">Codice Fiscale:</s:td>
				<s:td>${codice_fiscale}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Denominazione:</s:td>
				<s:td>${denominazione}</s:td>
				<s:td cssclass="seda-ui-cellheader">Residuo Borsellino:</s:td>
				<s:td>${RESIDUO}</s:td>
			</s:tr>
			<s:tr>
				<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="4">Dettaglio Importi</s:th>
			</s:tr>
			<s:tr>
				<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="2">Riepilogo Pagamenti</s:th>
				<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="2">Riepilogo Consumi</s:th>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Importo ricariche eseguite:</s:td>
				<s:td>${RICARICHE_BORSELLINO}</s:td>
				<s:td cssclass="seda-ui-cellheader">Importo consumi pagati:</s:td>
				<s:td>${RICARICHE_CONSUMI}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Importo ricariche acquisite:</s:td>
				<s:td>${RICARICHE_ACQUISITE}</s:td>
				<s:td cssclass="seda-ui-cellheader">Importo consumi inviati al gestionale:</s:td>
				<s:td>${RICARICHE_CONSUMI_INVIATE_A_GESTIONALE}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Importo ricariche non acquisite:</s:td>
				<s:td>${RICARICHE_NON_ACQUISITE}</s:td>
				<s:td cssclass="seda-ui-cellheader">Importo consumi non inviati al gestionale:</s:td>
				<s:td>${RICARICHE_CONSUMI_NON_INVIATE_A_GESTIONALE}</s:td>
			</s:tr>
		</s:tbody>
	</s:table>
		
	<s:div cssclass="seda-ui-datagrid thDettaglioTitle1" name="divTitleDett">Lista Pagamenti eseguiti nella data selezionata</s:div>
	<s:datagrid cssclass="lista_servizi_wallet" cachedrowset="lista_dettaglio_pagamenti" action="" usexml="true" border="0">
		<s:dgcolumn index="3" label="Data Pagamento" css="textleft"/>
		<s:dgcolumn index="2" label="Importo Pagato" css="textright" format="#0.00" />
		<s:dgcolumn index="4" label="Luogo Movimento" css="textleft" />
		
	</s:datagrid>
</s:div>
	

<br/>
	<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
		<s:button id="tx_button_indietro" type="submit" text="Indietro" onclick="" cssclass="btnStyle" />
	</s:div>
</s:form>






