<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:setBundle name="org.seda.payer.i18n.resources.TemplateStrings" />
<m:view_state id="viewstate" encodeAttributes="true"  encodeParameters="true"  />
<script type="text/javascript" >

	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
	
</script>

<s:div name="divDettaglio" cssclass="divDettaglio">	
	<s:table border="1" cellspacing="0" cellpadding="3" cssclass="seda-ui-datagrid tableDettaglio1">
		<s:thead>
			<s:tr>
				<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="7">Riepilogo Flusso</s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Ente</s:td>
				<s:td cssclass="seda-ui-cellheader">Provenienza</s:td>
				<s:td cssclass="seda-ui-cellheader">Data Flusso</s:td>
				<s:td cssclass="seda-ui-cellheader">Identificativo Flusso</s:td>
				<s:td cssclass="seda-ui-cellheader">Movimenti Regolarizzati</s:td>
			</s:tr>
			<s:tr>
				<s:td>${gdc_ente}</s:td>
				<s:td>${gdc_prov}</s:td>
				<s:td>${gdc_data}</s:td>
				<s:td>${gdc_idflusso}</s:td>
				<s:td>${gdc_srend}</s:td>
			</s:tr>
		</s:tbody>
	</s:table>
	
	<s:table border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglio1">
			<s:thead>
				<s:tr>
					<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="4">Dettaglio Associazioni Movimento</s:th>
				</s:tr>
			</s:thead>
			<s:tbody>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Numero Documento:</s:td>
					<s:td icol="3">${mdc_numDocumento}</s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Importo:</s:td>
					<s:td icol="3">${mdc_importo}</s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Squadratura:</s:td>
					<s:td>${mdc_squadratura}</s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Regolarizzato:</s:td>
					<s:td icol="3">
						<c:choose>
							<c:when test="${mdc_rendicontato=='Y'}">Si</c:when>
							<c:when test="${mdc_rendicontato=='P'}">Provvisorio</c:when> 
							<c:otherwise>No</c:otherwise>
						</c:choose>
					</s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Data Movimento:</s:td>
					<s:td icol="3"><s:formatDate pattern="dd/MM/yyyy" value="${mdc_dataMovimento}"/></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Data Valuta:</s:td>
					<s:td icol="3"><s:formatDate pattern="dd/MM/yyyy" value="${mdc_dataValuta}"/></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Causale:</s:td>
					<s:td icol="3">${mdc_causale}</s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Azioni:</s:td>
					<s:td icol="3">
						<s:hyperlink
							href="../rendicontazione/ricercaFlussi.do?idMovimentoCassa=${mdc_id}&tx_button_cerca=cerca"
							cssclass="blacklink hlStyle"
							imagesrc="../applications/templates/riconciliazionemt/img/notifica.png" text=""
							alt="Rendicontazione" />
					</s:td>
				</s:tr>
			</s:tbody>
		</s:table>
</s:div>
<c:if test="${!empty lista_flussi_abbinati}">
	<fmt:setLocale value="it_IT" />
	
	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Flussi abbinati
	</s:div>
	<s:datagrid cachedrowset="lista_flussi_abbinati"
		action="" border="1" usexml="true"
		rowperpage="" viewstate="true">
		
		<s:dgcolumn index="2" label="Data Flusso" format="dd/MM/yyyy"></s:dgcolumn>
		<s:dgcolumn index="3" label="Flusso"></s:dgcolumn>
		<s:dgcolumn index="4" label="Importo" css="text_align_right" format="#,##0.00"></s:dgcolumn>
		<s:dgcolumn index="5" label="Codice Mittente"></s:dgcolumn>
		<s:dgcolumn index="6" label="Mittente"></s:dgcolumn>
		<s:dgcolumn label="Azioni">
			<s:hyperlink
				href="../riconciliazionenn/riconciliazioneTransazioniNodo.do?keyQuadratura={1}&tx_button_cerca=cerca"
				cssclass="blacklink hlStyle"
				imagesrc="../applications/templates/riconciliazionemt/img/dettagliMovimenti.png" text=""
				alt="Riconciliazione Movimenti Nodo" />
		</s:dgcolumn>
  		
	</s:datagrid>
</c:if>
<c:if test="${!empty lista_transazioni_abbinate}">
	<fmt:setLocale value="it_IT" />
	
	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Transazioni abbinate
	</s:div>
	<s:datagrid cachedrowset="lista_transazioni_abbinate"
		action="" border="1" usexml="true"
		rowperpage="" viewstate="true">
		
		<s:dgcolumn index="2" label="Data Transazione" format="dd/MM/yyyy"></s:dgcolumn>
		<s:dgcolumn index="1" label="Transazione"></s:dgcolumn>
		<s:dgcolumn index="3" label="Identificativo Univoco Versamento"></s:dgcolumn>
		<s:dgcolumn index="4" label="Identificativo Univoco Riscossione"></s:dgcolumn>
		<s:dgcolumn index="5" label="Importo" css="text_align_right" format="#,##0.00"></s:dgcolumn>
		<s:dgcolumn label="Azioni">
			<s:hyperlink
				href="../monitoraggio/monitoraggioTransazioni.do?tx_codice_IUV={3}&tx_button_cerca=cerca"
				cssclass="blacklink hlStyle"
				imagesrc="../applications/templates/riconciliazionemt/img/dettagliMovimenti.png" text=""
				alt="Monitoraggio Transazione" />
		</s:dgcolumn>
  		
	</s:datagrid>
</c:if>

<c:url value="" var="formParameters">
	<c:if test="${!empty param.pageNumber}">
		<c:param name="pageNumber_hidden">${param.pageNumber}</c:param>
	</c:if>
	<c:if test="${!empty rowsPerPage}">
		<c:param name="rowsPerPage_hidden">${param.rowsPerPage}</c:param>
	</c:if>
	<c:if test="${!empty orderBy}">
		<c:param name="orderBy_hidden">${param.orderBy}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_societa}">
		<c:param name="tx_societa">${param.tx_societa}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_provincia}">
		<c:param name="tx_provincia">${param.tx_provincia}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_utente}">
		<c:param name="tx_utente">${param.tx_utente}</c:param>
	</c:if>
	<c:if test="${!empty param.statoSquadratura}">
		<c:param name="statoSquadratura">${param.statoSquadratura}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_tipo_carta}">
		<c:param name="tx_tipo_carta">${param.tx_tipo_carta}</c:param>
	</c:if>

	<c:if test="${!empty param.idFlusso}">
		<c:param name="idFlusso">${param.idFlusso}</c:param>
	</c:if>

	<c:if test="${!empty param.dtFlusso_da_day}">
		<c:param name="dtFlusso_da_day">${param.dtFlusso_da_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtFlusso_da_month}">
		<c:param name="dtFlusso_da_month">${param.dtFlusso_da_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtFlusso_da_year}">
		<c:param name="dtFlusso_da_year">${param.dtFlusso_da_year}</c:param>
	</c:if>
	<c:if test="${!empty param.dtFlusso_a_day}">
		<c:param name="dtFlusso_a_day">${param.dtFlusso_a_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtFlusso_a_month}">
		<c:param name="dtFlusso_a_month">${param.dtFlusso_a_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtFlusso_a_year}">
		<c:param name="dtFlusso_a_year">${param.dtFlusso_a_year}</c:param>
	</c:if>

	<c:if test="${!empty param.versOggetto}">
		<c:param name="versOggetto">${param.versOggetto}</c:param>
	</c:if>

	<c:if test="${!empty param.statoSquadratura}">
		<c:param name="statoSquadratura">${param.statoSquadratura}</c:param>
	</c:if>

	<c:if test="${!empty param.impPagamento_da}">
		<c:param name="impPagamento_da">${param.impPagamento_da}</c:param>
	</c:if>
	<c:if test="${!empty param.impPagamento_a}">
		<c:param name="impPagamento_a">${param.impPagamento_a}</c:param>
	</c:if>

	<c:if test="${!empty param.dtregol_da_day}">
		<c:param name="dtregol_da_day">${param.dtregol_da_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtregol_da_month}">
		<c:param name="dtregol_da_month">${param.dtregol_da_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtregol_da_year}">
		<c:param name="dtregol_da_year">${param.dtregol_da_year}</c:param>
	</c:if>
	<c:if test="${!empty param.dtregol_a_day}">
		<c:param name="dtregol_a_day">${param.dtregol_a_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtregol_a_month}">
		<c:param name="dtregol_a_month">${param.dtregol_a_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtregol_a_year}">
		<c:param name="dtregol_a_year">${param.dtregol_a_year}</c:param>
	</c:if>

	<c:if test="${!empty param.keyQuadratura}">
		<c:param name="keyQuadratura">${param.keyQuadratura}</c:param>
	</c:if>

	<c:if test="${!empty param.nomeFileTxt}">
		<c:param name="nomeFileTxt">${param.nomeFileTxt}</c:param>
	</c:if>

	<c:if test="${!empty param.dtChiusuraFlusso_da_day}">
		<c:param name="dtChiusuraFlusso_da_day">${param.dtChiusuraFlusso_da_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtChiusuraFlusso_da_month}">
		<c:param name="dtChiusuraFlusso_da_month">${param.dtChiusuraFlusso_da_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtChiusuraFlusso_da_year}">
		<c:param name="dtChiusuraFlusso_da_year">${param.dtChiusuraFlusso_da_year}</c:param>
	</c:if>
	<c:if test="${!empty param.dtChiusuraFlusso_a_day}">
		<c:param name="dtChiusuraFlusso_a_day">${param.dtChiusuraFlusso_a_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtChiusuraFlusso_a_month}">
		<c:param name="dtChiusuraFlusso_a_month">${param.dtChiusuraFlusso_a_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtChiusuraFlusso_a_year}">
		<c:param name="dtChiusuraFlusso_a_year">${param.dtChiusuraFlusso_a_year}</c:param>
	</c:if>

	<c:if test="${!empty ext}">
		<c:param name="ext">${ext}</c:param>
	</c:if>

	<c:if test="${!empty totMov}">
		<c:param name="totMov">${totMov}</c:param>
	</c:if>
	<c:if test="${!empty totImpMov}">
		<c:param name="totImpMov">${totImpMov}</c:param>
	</c:if>
	<c:if test="${!empty totTran}">
		<c:param name="totTran">${totTran}</c:param>
	</c:if>
	<c:if test="${!empty totImpQun}">
		<c:param name="totImpQun">${totImpQun}</c:param>
	</c:if>
</c:url>

<s:form name="frmIndietro" action="" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
	<s:div name="divCentered0" cssclass="divRicBottoni">
		<c:if test="${requestScope.page == 'ricTraNodo'}">
			<s:hyperlink name="hpAnnulla" cssclass="hlToBtnStyle" href="dettaglioMovimentoCassa.do?indietro=Y&idmdc=${mdc_id}" text="Indietro" />
		</c:if>
		<c:if test="${requestScope.page == 'monTra'}">
			<s:hyperlink name="hpAnnulla" cssclass="hlToBtnStyle" href="dettaglioMovimentoCassa.do?indietro=Y&idmdc=${mdc_id}" text="Indietro" />
		</c:if>
		<c:if test="${requestScope.page == 'dettMov'}">
			<s:hyperlink name="hpAnnulla" cssclass="hlToBtnStyle" href="dettaglioMovimentoCassa.do?indietro=Y&idmdc=${mdc_id}" text="Indietro" />
		</c:if>	
	</s:div>
</s:form>
	