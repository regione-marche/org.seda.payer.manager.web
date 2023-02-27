<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<m:setBundle name="org.seda.payer.i18n.resources.TemplateStrings" />
<m:view_state id="dettagliomonitoraggio" encodeAttributes="true" />

<s:div name="divNumAutorizzazione" cssclass="divSectionBorder">
	<s:table id="tableTestataComunicazione" border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglio1">
		<s:thead>
			<s:tr>
				<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="4">Dettagli Autorizzazione</s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Numero Autorizzazione:</s:td>
				<s:td>${tx_numero_autor}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Nominativo:</s:td>
				<s:td>${tx_nome_autorizz}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Cod. Fisc./P. Iva::</s:td>
				<s:td>${tx_codice_fisc}</s:td>
			</s:tr>			
		</s:tbody>
	</s:table>	
	<br>
	<s:table id="tableDettaglioMercato" border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglioStruttura">
		<s:thead>
			<s:tr>
				<s:th cssclass="seda-ui-datagrid thDettaglioTitle2" icol="4">Dati Dettaglio Monitoraggio Mercati</s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Cod Mercato:</s:td>
				<s:td>${tx_codice_mercato}</s:td>
				<s:td cssclass="seda-ui-cellheader">Mercato:</s:td>
				<s:td>${tx_desc_mercato}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Cod Piazzola:</s:td>
				<s:td>${tx_cod_piazzola}</s:td>
				<s:td cssclass="seda-ui-cellheader">Piazzola:</s:td>
				<s:td>${tx_desc_piazzola}</s:td>				
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Data Prenotazione:</s:td>
				<s:td>${tx_data_prenotazione}</s:td>
				<s:td cssclass="seda-ui-cellheader">Giorno della settimana:</s:td>
				<s:td>${tx_giorno_sett}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Tipologia Banco:</s:td>
				<s:td>${tx_tipo_banco}</s:td>
				<s:td cssclass="seda-ui-cellheader">Periodo Giornaliero:</s:td>
				<s:td>${tx_periodo_giorn}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Tariffa Cosap:</s:td>
				<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_tariffa_cosap}" minFractionDigits="2" maxFractionDigits="2"/> </s:td>
				<s:td cssclass="seda-ui-cellheader">Tariffa Tari:</s:td>
				<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_tariffa_tari}" minFractionDigits="2" maxFractionDigits="2"/> </s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Pagato:</s:td>
				<s:td>${tx_flag_pagato}</s:td>
				<s:td cssclass="seda-ui-cellheader">Importo Pagato:</s:td>
				<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_impo_pagato}" minFractionDigits="2" maxFractionDigits="2"/> </s:td>
			</s:tr>
			<c:choose>
			<c:when test="${tx_flag_pagato == 'SI'}">
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Data Pagamento:</s:td>
					<s:td>${tx_data_pagamento}</s:td>
					<s:td cssclass="seda-ui-cellheader">Compenso:</s:td>
					<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_impo_compenso}" minFractionDigits="2" maxFractionDigits="2"/> </s:td>
				</s:tr>	
			</c:when>
			</c:choose>			
		</s:tbody>
	</s:table>
</s:div>
<br>
<s:form name="frmIndietro" action="ritorna.do?vista=monitoraggiomercati" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
	<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
		<s:button id="tx_button_back" type="submit" text="Indietro" onclick="" cssclass="btnStyle" />
	</s:div>
</s:form>


