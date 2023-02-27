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

	function submitForm(){
		
	}
	
</script>

<s:textbox name="idgdc" label="idgdc" bmodify="true"
	text="${idgdc}" cssclass="display_none" cssclasslabel="display_none" />
	
<s:div name="divDettaglio" cssclass="divDettaglio">	
	<s:table border="1" cellspacing="0" cellpadding="3" cssclass="seda-ui-datagrid tableDettaglio1">
		<s:thead>
			<s:tr>
				<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="7">Riepilogo Giornale di Cassa</s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Ente</s:td>
				<s:td cssclass="seda-ui-cellheader">Provenienza</s:td>
				<s:td cssclass="seda-ui-cellheader">Data Giornale</s:td>
				<s:td cssclass="seda-ui-cellheader">Identificativo Flusso</s:td>
				<s:td cssclass="seda-ui-cellheader">Esercizio</s:td>
				<s:td cssclass="seda-ui-cellheader">Sospesi Regolarizzati</s:td>
				<s:td cssclass="seda-ui-cellheader">Sospesi Rendicontati</s:td>
			</s:tr>
			<s:tr>
				<s:td>${gdc_ente}</s:td>
				<s:td>${gdc_prov}</s:td>
				<s:td>${gdc_data}</s:td>
				<s:td>${gdc_idflusso}</s:td>
				<s:td>${gdc_esercizio}</s:td>
				<s:td>${gdc_srego}</s:td>
				<s:td>${gdc_srend}</s:td>
			</s:tr>
		</s:tbody>
	</s:table>

	<s:table border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglio1">
			<s:thead>
				<s:tr>
					<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="4">Dettaglio Movimento di Cassa</s:th>
				</s:tr>
			</s:thead>
			<s:tbody>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Conto:</s:td>
					<s:td icol="3">${mdc_conto}</s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Stato Sospeso:</s:td>
					<s:td icol="3">${mdc_statoSospeso}</s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Numero Documento:</s:td>
					<s:td>${mdc_numDocumento}</s:td>
					<s:td cssclass="seda-ui-cellheader">Cliente:</s:td>
					<s:td>${mdc_cliente}</s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Importo:</s:td>
					<s:td>${mdc_importo}</s:td>
					<s:td cssclass="seda-ui-cellheader">Squadratura:</s:td>
					<s:td>${mdc_squadratura}</s:td>
				</s:tr>
				<s:tr>
				
					<s:td cssclass="seda-ui-cellheader">Rendicontato:</s:td>
					<s:td>
						<c:choose>
							<c:when test="${mdc_rendicontato=='Y'}">Si</c:when>
							<c:when test="${mdc_rendicontato=='P'}">Provvisorio</c:when> 
							<c:otherwise>No</c:otherwise>
						</c:choose>
					</s:td>
					<s:td cssclass="seda-ui-cellheader">Regolarizzato:</s:td>
					<s:td>
						<c:choose>
							<c:when test="${mdc_regolarizzato=='Y'}">Si</c:when> 
							<c:otherwise>No</c:otherwise>
						</c:choose>
					</s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Progressivo Documento:</s:td>
					<s:td>${mdc_progressivoDoc}</s:td>
					<s:td cssclass="seda-ui-cellheader">Numero Bolletta:</s:td>
					<s:td>${mdc_numBolletta}</s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Data Movimento:</s:td>
					<s:td><s:formatDate pattern="dd/MM/yyyy" value="${mdc_dataMovimento}"/></s:td>
					<s:td cssclass="seda-ui-cellheader">Data Valuta:</s:td>
					<s:td><s:formatDate pattern="dd/MM/yyyy" value="${mdc_dataValuta}"/></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Tipo Esecuzione:</s:td>
					<s:td>${mdc_tipoEsecuzione}</s:td>
					<s:td cssclass="seda-ui-cellheader">Codice Riferimento:</s:td>
					<s:td>${mdc_codiceRiferimento}</s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Cliente:</s:td>
					<s:td icol="3">${mdc_cliente}</s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Causale:</s:td>
					<s:td icol="3">${mdc_causale}</s:td>
				</s:tr>
				<c:if test="${mdc_rendicontato=='Y'}">
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Data Rendicontazione:</s:td>
						<s:td><s:formatDate pattern="dd/MM/yyyy" value="${mdc_dataRendicontazione}"/></s:td>
					</s:tr>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Nota:</s:td>
						<s:td icol="3">${mdc_nota}</s:td>
					</s:tr>
				</c:if>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Azioni:</s:td>
					<s:td icol="3">
					<c:if test="${mdc_rendicontato != 'Y'}">
						<s:hyperlink
							href="associazioniTransazioniMovimentoCassa.do?numeroDocumento=${mdc_id}&page=dettMov"
							cssclass="blacklink hlStyle"
							imagesrc="../applications/templates/riconciliazionemt/img/dettagliMovimenti.png" text=""
							alt="Associazioni Transazioni a Movimento di Cassa" />
						<s:hyperlink
							href="associazioniFlussiMovimentoCassa.do?numeroDocumento=${mdc_id}&page=dettMov"
							cssclass="blacklink hlStyle"
							imagesrc="../applications/templates/riconciliazionemt/img/Y_ftp.png" text=""
							alt="Associazione Flussi a Movimento di Cassa" />
						<c:if test="${mdc_rendicontato == 'P' && sessionScope.j_user_bean.associazioniDefinitiveRiconciliazionemtEnabled}">
							<s:hyperlink
								href="regolarizzaSospeso.do?idmdc=${mdc_id}&conto=${mdc_conto}&stato=${mdc_statoSospeso}&documento=${mdc_numDocumento}&cliente=${mdc_cliente}&importo=${mdc_importo}&squa=${mdc_squadratura}&page=dettMov"
								cssclass="blacklink hlStyle"
								imagesrc="../applications/templates/riconciliazionemt/img/quadraturaManuale.png" text=""
								alt="Regolarizza Sospeso" />
						</c:if>
					</c:if>
					<c:if test="${mdc_regolarizzato == 'Y'}">
						<s:hyperlink
							href="dettaglioAssociazioniMovimentoCassa.do?idmdc=${mdc_id}&page=dettMov"
							cssclass="blacklink hlStyle"
							imagesrc="../applications/templates/riconciliazionemt/img/details.png" text=""
							alt="Dettaglio Associazioni Movimento di Cassa" />
							
						<s:hyperlink
							href="../rendicontazione/ricercaFlussi.do?idMovimentoCassa=${mdc_id}&giornaleCassa=${fn:replace(gdc_idflusso, '#', '%23')}&movimentoCassa=${mdc_numDocumento}&tx_button_cerca=cerca"
							cssclass="blacklink hlStyle"
							imagesrc="../applications/templates/riconciliazionemt/img/notifica.png" text=""
							alt="Rendicontazione" />
					</c:if>

					</s:td>
				</s:tr>
			</s:tbody>
		</s:table>
</s:div>
<s:div name="divCentered0" cssclass="divRicBottoni">
	<s:hyperlink name="hpAnnulla" cssclass="hlToBtnStyle" href="dettaglioGiornaleCassa.do?tx_button_cerca=cerca" text="Indietro" />				
</s:div>
	