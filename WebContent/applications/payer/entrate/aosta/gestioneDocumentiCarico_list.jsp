<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>


<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>

<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>

<%-- FORM RICERCA --%>
<c:url value="" var="formParameters">
	<c:param name="test">ok</c:param>
	<%-- Screen mittente delle richieste di paginazione... --%>
	<c:param name="screen">search</c:param>

	<c:if test="${!empty tx_societa}">
		<c:param name="tx_societa">${tx_societa}</c:param>
	</c:if>
	<c:if test="${!empty tx_utente}">
		<c:param name="tx_utente">${tx_utente}</c:param>
	</c:if>
	<c:if test="${!empty tx_UtenteEnte}">
		<c:param name="tx_UtenteEnte">${tx_UtenteEnte}</c:param>
	</c:if>
	<c:if test="${!empty impostaServ}">
		<c:param name="impostaServ">${impostaServ}</c:param>
	</c:if>
	<c:if test="${!empty annoEmissione}">
		<c:param name="annoEmissione">${annoEmissione}</c:param>
	</c:if>
	<c:if test="${!empty codFiscale}">
		<c:param name="codFiscale">${codFiscale}</c:param>
	</c:if>
	<c:if test="${!empty numEmissione}">
		<c:param name="numEmissione">${numEmissione}</c:param>
	</c:if>
	<c:if test="${!empty tx_tipologia_servizio}">
		<c:param name="tx_tipologia_servizio">${tx_tipologia_servizio}</c:param>
	</c:if>
	<c:if test="${!empty stato_documento}">
		<c:param name="stato_documento">${stato_documento}</c:param>
	</c:if>
	<c:if test="${!empty denominazione}">
		<c:param name="denominazione">${denominazione}</c:param>
	</c:if>
	<c:if test="${!empty tipoRic}">
		<c:param name="tipoRic">${tipoRic}</c:param>
	</c:if>
</c:url>
<!--  Presentazione risultati -->

<!-- colonne -->

<c:if test="${!empty listaDocumenti}">
	<fmt:setLocale value="it_IT" />

	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Lista Documenti
	</s:div>

	<s:div name="divRisultati" cssclass="divRisultati">

		<s:datagrid viewstate="" cachedrowset="listaDocumenti"
			rowperpage="${applicationScope.rowsPerPage}"
			action="gestioneDocumentiCarico.do" border="1" usexml="true">

			<s:action>
			</s:action>

			<!-- SOCIETA -->
			<s:dgcolumn index="26" label="Soc." asc="SOCA" desc="SOCD"></s:dgcolumn>

			<!-- UTENTE -->
			<s:dgcolumn index="9" label="Utente" asc="UTEA" desc="UTED"></s:dgcolumn>

			<!-- ENTE -->
			<s:dgcolumn index="10" label="Ente" asc="ENTA" desc="ENTD"></s:dgcolumn>

			<!-- EH1_CISECISE -->
			<s:dgcolumn label="I/S" asc="ISEA" desc="ISED" index="1"></s:dgcolumn>

			<!-- EH1_CEH1ADOC -->
			<s:dgcolumn index="2" label="Anno" asc="DANA" desc="DAND"></s:dgcolumn>

			<!--  EH1_CEH1NEMIS -->
			<s:dgcolumn index="3" label="Numero" asc="DNMA" desc="DNMD"></s:dgcolumn>

			<!-- EH1_TANETUFF,EH1_CANECUFF -->
			<s:dgcolumn label="Ufficio" asc="UFFA" desc="UFFD">
				<s:if left="{5}{6}" control="eq" right="">
					<s:then>
					</s:then>
					<s:else>
								{5}/{6}
							</s:else>
				</s:if>
			</s:dgcolumn>

			<!-- EH1_CTSECTSE -->
			<s:dgcolumn index="7" label="Tip. Serv." asc="CTSA" desc="CTSD"
				css="text_align_right"></s:dgcolumn>

			<!-- DOCUMENTI RENDICONTATI -->
			<s:dgcolumn index="11" label="R" css="text_align_right" asc="FRDA"
				desc="FRDD"></s:dgcolumn>

			<!-- EH1_CEH1CFIS -->
			<s:dgcolumn index="4" label="Cod.Fiscale" asc="CFSA" desc="CFSD"></s:dgcolumn>

			<!-- TOT_RATE -->
			<s:dgcolumn index="16" label="RT" asc="NRTA" desc="NRTD"></s:dgcolumn>

			<!-- CARICO -->
			<s:dgcolumn index="12" format="#,##0.00" label="Carico" asc="CARA"
				desc="CARD" css="text_align_right"></s:dgcolumn>

			<!-- DIM. CARICO -->
			<s:dgcolumn index="14" format="#,##0.00" label="Dim.Carico"
				asc="DCAA" desc="DCAD" css="text_align_right"></s:dgcolumn>

			<!-- RISCOSSO -->
			<s:dgcolumn index="13" format="#,##0.00" label="Riscosso" asc="RSCA"
				desc="RSCD" css="text_align_right"></s:dgcolumn>

			<!-- RESIDUO -->
			<s:dgcolumn index="20" format="#,##0.00" label="Residuo" asc="RSDA"
				desc="RSDD" css="text_align_right"></s:dgcolumn>

			<s:ifdatagrid left="${ext}" control="eq" right="1">
				<s:thendatagrid>
					<s:dgcolumn index="27" format="#,##0.00" label="Residuo Scad."
						asc="RESA" desc="RESD" css="text_align_right"></s:dgcolumn>
				</s:thendatagrid>
			</s:ifdatagrid>

			<s:dgcolumn label="Dett">
				<s:hyperlink
					href="documentoDettaglio.do${formParameters}&tx_button_cerca_exp=OK&chiaveIS={1}&chiaveDoc={8}&chiaveTipoUff={5}&chiaveCodUff={6}&chiaveCodUte={9}&chiaveCodEnte={10}&chiaveServ={24}&chiaveTomb={25}&chiaveFlusso={23}&pagPrec=gestioneDocumentiCarico"
					imagesrc="../applications/templates/shared/img/dettaglio.png"
					alt="Dettaglio Documento" text="" cssclass="blacklink hlStyle" />
			</s:dgcolumn>
			<s:dgcolumn label="Tr">
				<s:if left="{17}" control="ne" right="0">
					<s:then>{17}
						<s:hyperlink
							href="ricercaTributi.do${formParameters}&tx_button_cerca_exp=OK&chiaveIS={1}&chiaveDoc={8}&chiaveTipoUff={5}&chiaveCodUff={6}&chiaveCodUte={9}&chiaveCodEnte={10}&chiaveServ={24}&chiaveTomb={25}&chiaveFlusso={23}&pagPrec=gestioneDocumentiCarico"
							imagesrc="../applications/templates/shared/img/listaDati.png"
							alt="Lista Tributi" text="" cssclass="blacklink hlStyle" />
					</s:then>
					<s:else>
						&nbsp;&nbsp;
					</s:else>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn label="Sc">
				<s:if left="{16}" control="ne" right="0">
					<s:then>{16}
						<s:hyperlink
							href="ricercaScadenze.do${formParameters}&tx_button_cerca_exp=OK&chiaveIS={1}&chiaveDoc={8}&chiaveTipoUff={5}&chiaveCodUff={6}&chiaveCodUte={9}&chiaveCodEnte={10}&chiaveServ={24}&chiaveTomb={25}&chiaveFlusso={23}&pagPrec=gestioneDocumentiCarico"
							imagesrc="../applications/templates/shared/img/listaDati.png"
							alt="Lista Scadenze" text="" cssclass="blacklink hlStyle" />
					</s:then>
					<s:else>
						&nbsp;&nbsp;
					</s:else>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn label="Pg">
				<s:if left="{18}" control="ne" right="0">
					<s:then>
						<s:hyperlink
							href="ricercaPagamentiDocumenti.do${formParameters}&tx_button_cerca_exp=OK&chiaveIS={1}&chiaveDoc={8}&chiaveTipoUff={5}&chiaveCodUff={6}&chiaveCodUte={9}&chiaveCodEnte={10}&chiaveServ={24}&chiaveTomb={25}&chiaveFlusso={23}&pagPrec=gestioneDocumentiCarico"
							imagesrc="../applications/templates/shared/img/listaDati.png"
							alt="Lista Pagamenti" text="" cssclass="blacklink hlStyle" />
					</s:then>
					<s:else>
						&nbsp;&nbsp;
					</s:else>
				</s:if>
			</s:dgcolumn>

			<!-- MODIFICA -->
			<s:dgcolumn label="Mod.">
				<s:if left="{18}" control="eq" right="0">
					<s:then>
							<s:hyperlink
							href="gestioneDocumentiCarico.do${formParameters}
									&button_modifica=MODIFICA
									&codSocieta={26}
									&codUtente={9}
									&codUtenteEnte={10}
									&codTipologiaServizio={24}
									&codImpostaServizio={1}
									&codTipoUfficio={5}
									&codUfficio={6}
									&numeroDocumento={8}
									&chiaveFlusso={23}
									&codiceFiscale={4}
									&tipoServizio={24}
									&achiaveTomb={25}
									&annoEmissione={2}
									&numeroEmissione={3}"
							imagesrc="../applications/templates/shared/img/modifica.gif"
							alt="Modifica" text="" cssclass="blacklink hlStyle" />
							</s:then>
					<s:else>
						&nbsp;&nbsp;
					</s:else>
				</s:if>
			</s:dgcolumn>
			
			<!-- ELINIMA -->
			<s:dgcolumn label="El">
				<s:if left="{18}" control="eq" right="0">
					<s:then>
						<s:hyperlink
							href="gestioneDocumentiCarico.do${formParameters}&button_elimina=ELIMINA
									&codSocieta={26}
									&codUtente={9}
									&codUtenteEnte={10}
									&codTipologiaServizio={24}
									&codImpostaServizio={1}
									&codTipoUfficio={5}
									&codUfficio={6}
									&numeroDocumento={8}
									&chiaveFlusso={23}
									&chiaveTomb={25}"
							imagesrc="../applications/templates/shared/img/deletable.gif"
							alt="Elimina" text="" cssclass="blacklink hlStyle" />

					</s:then>
					<s:else>
						&nbsp;&nbsp;
					</s:else>
				</s:if>
			</s:dgcolumn>
			
			<s:dgcolumn label="Stampa">
				<s:if left="{30}" control="eq" right="Y" operator="and" secondleft="{20}" secondcontrol="ne" secondright="0.00">
					<s:then>
						<s:hyperlink
							href="gestioneDocumentiCarico.do${formParameters}&button_stampa=STAMPA
									&codSocieta={26}
									&codUtente={9}
									&tipoServizio={24}
									&codUtenteEnte={10}
									&codTipoUfficio={5}
									&codUfficio={6}
									&codImpostaServizio={1}
									&codiceFiscale={4}
									&numeroDocumento={8}
									&impResiduo={20}"
							imagesrc="../applications/templates/shared/img/pdf.png"
							alt="Stampa Avviso PagoPA" text="" cssclass="blacklink hlStyle" />
					</s:then>
					<s:else>
						&nbsp;&nbsp;
					</s:else>
				</s:if>
			</s:dgcolumn>

		</s:datagrid>

		<!-- tabella riassunto	 -->

		<s:div name="divDatiAgg"
			cssclass="divTableTitle bold text_align_right">
			<c:if test="${userProfile=='AMEN'}">
						 Dati aggiornati al: <s:formatDate pattern="dd/MM/yyyy"
					value="${dataAggiornamento}" />
			</c:if>
		</s:div>

		<%--<fmt:formatDate value="${dataUltimoAgg}" pattern="dd/MM/yyyy"/>--%>
		<s:table border="1" cellspacing="0" cellpadding="3"
			cssclass="seda-ui-datagrid">
			<s:thead>
				<s:tr cssclass="seda-ui-datagridrowpari">
					<s:th cssclass="seda-ui-datagridheadercell" icol="8">
						<b>Totali Documenti</b>
					</s:th>
				</s:tr>
			</s:thead>
			<s:tbody>
				<s:tr cssclass="seda-ui-datagridrowpari">
					<s:td cssclass="seda-ui-datagridcell">Totale Carico</s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right">
						<fmt:formatNumber type="NUMBER" value="${totImportoCarico}"
							minFractionDigits="2" maxFractionDigits="2" />
					</s:td>
					<s:td cssclass="seda-ui-datagridcell">Totale Residuo</s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right">
						<fmt:formatNumber type="NUMBER" value="${totImportoTotaleResiduo}"
							minFractionDigits="2" maxFractionDigits="2" />
					</s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-datagridcell">Totale Diminuzione Carico</s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right">
						<fmt:formatNumber type="NUMBER"
							value="${totImportoDiminuzioneCarico}" minFractionDigits="2"
							maxFractionDigits="2" />
					</s:td>
					<s:td cssclass="seda-ui-datagridcell">Totale Residuo Scaduto </s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right">
						<fmt:formatNumber type="NUMBER"
							value="${totImportoResiduoScaduto}" minFractionDigits="2"
							maxFractionDigits="2" />
					</s:td>

				</s:tr>
				<s:tr cssclass="seda-ui-datagridrowpari">
					<s:td cssclass="seda-ui-datagridcell">Totale Riscosso</s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right">
						<fmt:formatNumber type="NUMBER" value="${totImportoRiscosso}"
							minFractionDigits="2" maxFractionDigits="2" />
					</s:td>
					<s:td cssclass="seda-ui-datagridcell"></s:td>
					<s:td cssclass="seda-ui-datagridcell"></s:td>
				</s:tr>


			</s:tbody>
		</s:table>

		<s:table border="1" cellspacing="0" cellpadding="3"
			cssclass="seda-ui-datagrid">
			<s:thead>
				<s:tr cssclass="seda-ui-datagridrowpari">
					<s:th cssclass="seda-ui-datagridheadercell" icol="8">
						<b>Statistiche</b>
					</s:th>
				</s:tr>
			</s:thead>
			<s:tbody>
				<s:tr cssclass="seda-ui-datagridrowpari">
					<s:td cssclass="seda-ui-datagridcell">Riscosso su Carico</s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right">
						<fmt:formatNumber type="NUMBER" value="${percRiscossoCarico}"
							minFractionDigits="2" maxFractionDigits="2" /> %</s:td>
					<s:td cssclass="seda-ui-datagridcell">Residuo su Carico</s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right">
						<fmt:formatNumber type="NUMBER" value="${percRensiduoCarico}"
							minFractionDigits="2" maxFractionDigits="2" />  %</s:td>
				</s:tr>
				<s:tr cssclass="seda-ui-datagridrowpari">
					<s:td cssclass="seda-ui-datagridcell">Sgravato su Carico</s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right">
						<fmt:formatNumber type="NUMBER" value="${percSgravatoCarico}"
							minFractionDigits="2" maxFractionDigits="2" />  %</s:td>
					<s:td cssclass="seda-ui-datagridcell">Residuo Scaduto su Carico</s:td>
					<s:td cssclass="seda-ui-datagridcell text_align_right">
						<fmt:formatNumber type="NUMBER" value="${percResiduoScadCarico}"
							minFractionDigits="2" maxFractionDigits="2" />  %</s:td>
				</s:tr>
			</s:tbody>
		</s:table>

	</s:div>

</c:if>

