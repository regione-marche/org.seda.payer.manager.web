<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="dettagliotransazione" encodeAttributes="true" />

<c:set var="spom3" value="${requestScope.spom3}" />
	
<s:div name="divDettaglio" cssclass="divDettaglio">
	<c:if test="${!empty tx_transazione}">
		<s:table border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglio1">
			<s:thead>
				<s:tr>
					<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="4">Dettaglio Transazione</s:th>
				</s:tr>
			</s:thead>
			<s:tbody>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Id:</s:td>
					<s:td icol="3">${tx_transazione.chiave_transazione}</s:td>
				</s:tr>
				<c:if test="${tx_transazione.canale_pagamento == 'LOT' || tx_transazione.canale_pagamento == 'TOT' || tx_transazione.canale_pagamento == 'SIS'}">
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Id Atm:</s:td>
						<s:td icol="3">${tx_transazione.codice_autorizzazione_banca} - ${tx_transazione.codice_identificativo_banca}</s:td>
					</s:tr>
				</c:if> 
				
				
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Importo totale:</s:td>
					<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_transazione.importo_totale_transazione}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
					<s:td cssclass="seda-ui-cellheader">Costi del servizio:</s:td>
					<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_transazione.importo_costo_transazione + tx_transazione.importo_costo_spese_di_notifica}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Codice Autorizzazione Banca:</s:td>
					<s:td>${tx_transazione.codice_autorizzazione_banca}</s:td>
					<s:td cssclass="seda-ui-cellheader">Data Eff.Pag.:</s:td>
					<s:td><s:formatDate pattern="dd/MM/yyyy" value="${tx_transazione.data_effettivo_pagamento_su_gateway}"/></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Numero MAV:</s:td>
					<s:td>${tx_transazione.codice_ordine_gateway}</s:td>
					<s:td cssclass="seda-ui-cellheader">Riversamento Automatico:</s:td>
					<s:td>
						<c:choose>
							<c:when test="${tx_transazione.flag_riversamento_automatico == 'Y'}">Si</c:when>
							<c:otherwise>No</c:otherwise>
						</c:choose>
					</s:td>			
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Id Terminale POS fisico:</s:td>
					<s:td>${tx_transazione.id_terminale_pos}</s:td>
					<s:td cssclass="seda-ui-cellheader">
						<c:choose>
							<c:when test="${tx_transazione.canale_pagamento == 'POS'}">
								Operatore POS fisico:
							</c:when>
							<c:otherwise>
								Operatore Call Center Dispositivo:
							</c:otherwise>
						</c:choose>
					</s:td>
					<s:td>
						<c:if test="${tx_transazione.canale_pagamento == 'CCD' || tx_transazione.canale_pagamento == 'POS'}">
							${tx_transazione.operatore_inserimento}
						</c:if> 
					</s:td>
				</s:tr>
				<!--  inizio LP PG190220 -->
				<c:set var="lastIuv" value="" />
				<!--  fine LP PG190220 -->	
				<c:if test="${!empty tx_lista_dettaglio}">
					<c:set var="lastIuv"  value="" />
					<c:forEach items="${tx_lista_dettaglio}" var="tx_dettaglio" >
						<c:if test="${!empty tx_dettaglio.nodoSpcIuv && tx_dettaglio.nodoSpcIuv!=lastIuv}">
							<c:set  var="lastIuv"  value="${tx_dettaglio.nodoSpcIuv}" />
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Codice IUV:</s:td>
							<s:td icol="2">${tx_dettaglio.nodoSpcIuv}</s:td>
							<s:td >
								<c:if test="${!empty (tx_dettaglio.nodoSpcRt)}">
									<s:hyperlink
									href="dettaglioTransazioneViewRT.do?tx_button_download_rt=1&iuv=${tx_dettaglio.nodoSpcIuv}&tx_codice_transazione_hidden=${tx_transazione.chiave_transazione}"
									imagesrc="../applications/templates/monitoraggio/img/icoRicevuta.png"
									alt="Scarica Ricevuta Telematica RT" text=""
									cssclass="hlStyle" />
								</c:if>
							</s:td>
						</s:tr>
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Codice IUR:</s:td>
							<s:td icol="3">${tx_transazione.codice_identificativo_banca}</s:td>
						</s:tr>
						<!--  inizio LP PG1902020 -->
						<c:set var="statoAnnullo" value="" />	
						<c:if test="${tx_dettaglio.nodoSpcErEsito == 'OK'}">
							<c:set var="statoAnnullo" value="Si" />
							<c:if test="${empty (tx_dettaglio.nodoSpcRt)}">
								<c:set var="statoAnnullo" value="Attesa RT" />
							</c:if>	
							<s:tr>
								<s:td cssclass="seda-ui-cellheader">Annullo:</s:td>
								<s:td icol="2">${statoAnnullo}</s:td>
								<s:td >
									<c:if test="${!empty (tx_dettaglio.nodoSpcRtAnnullata)}">
										<s:hyperlink
										href="dettaglioTransazioneViewRT.do?tx_button_download_rt=1&rt_annullata=1&iuv=${tx_dettaglio.nodoSpcIuv}&tx_codice_transazione_hidden=${tx_transazione.chiave_transazione}"
										imagesrc="../applications/templates/monitoraggio/img/icoRicevuta.png"
										alt="Scarica Ricevuta Telematica RT Annullata" text=""
										cssclass="hlStyle" />
									</c:if>
								</s:td>
							</s:tr>
							<c:if test="${tx_dettaglio.nodoSpcEsitoInvioRevocaEmailContribuente == 'Y'}">
								<s:tr>
									<s:td cssclass="seda-ui-cellheader">Email Annullo:</s:td>
									<s:td>Inviata</s:td>
									<s:td cssclass="seda-ui-cellheader">Data invio:</s:td>
									<s:td><s:formatDate pattern="dd/MM/yyyy" value="${tx_dettaglio.nodoSpcDataInvioRevocaEmailContribuente}"/></s:td>
								</s:tr>
							</c:if>
							<c:if test="${tx_dettaglio.nodoSpcEsitoInvioRevocaEmailContribuente != 'Y'}">
								<s:tr>
									<s:td cssclass="seda-ui-cellheader">Email Annullo:</s:td>
									<s:td>Non Inviata</s:td>
									<s:td cssclass="seda-ui-cellheader">Data Invio:</s:td>
									<s:td></s:td>
								</s:tr>
							</c:if>
						</c:if>
						<!--  fine LP PG1902020 -->
						</c:if>
					</c:forEach>
				</c:if>
				<!--  inizio LP PG190220 -->
				<c:if test="${lastIuv == ''}" >	
					<c:if test="${!empty tx_lista_ici}">
						<c:forEach items="${tx_lista_ici}" var="tx_dettaglio" >
							<c:if test="${!empty tx_dettaglio.nodoSpcIuv && tx_dettaglio.nodoSpcIuv!=lastIuv}">
								<c:set  var="lastIuv"  value="${tx_dettaglio.nodoSpcIuv}" />
							<s:tr>
								<s:td cssclass="seda-ui-cellheader">Codice IUV:</s:td>
								<s:td icol="2">${tx_dettaglio.nodoSpcIuv}</s:td>
								<s:td >
									<c:if test="${!empty (tx_dettaglio.nodoSpcRt)}">
										<s:hyperlink
										href="dettaglioTransazioneViewRT.do?tx_button_download_rt=1&iuv=${tx_dettaglio.nodoSpcIuv}&tx_codice_transazione_hidden=${tx_transazione.chiave_transazione}"
										imagesrc="../applications/templates/monitoraggio/img/icoRicevuta.png"
										alt="Scarica Ricevuta Telematica RT" text=""
										cssclass="hlStyle" />
									</c:if>
								</s:td>
							</s:tr>
							<s:tr>
								<s:td cssclass="seda-ui-cellheader">Codice IUR:</s:td>
								<s:td icol="3">${tx_transazione.codice_identificativo_banca}</s:td>
							</s:tr>
							<c:set var="statoAnnullo" value="" />	
							<c:if test="${tx_dettaglio.nodoSpcErEsito == 'OK'}">
								<c:set var="statoAnnullo" value="Si" />
								<c:if test="${empty (tx_dettaglio.nodoSpcRt)}">
									<c:set var="statoAnnullo" value="Attesa RT" />
								</c:if>	
								<s:tr>
									<s:td cssclass="seda-ui-cellheader">Annullo:</s:td>
									<s:td icol="2">${statoAnnullo}</s:td>
									<s:td >
										<c:if test="${!empty (tx_dettaglio.nodoSpcRtAnnullata)}">
											<s:hyperlink
											href="dettaglioTransazioneViewRT.do?tx_button_download_rt=1&rt_annullata=1&iuv=${tx_dettaglio.nodoSpcIuv}&tx_codice_transazione_hidden=${tx_transazione.chiave_transazione}"
											imagesrc="../applications/templates/monitoraggio/img/icoRicevuta.png"
											alt="Scarica Ricevuta Telematica RT Annullata" text=""
											cssclass="hlStyle" />
										</c:if>
									</s:td>
								</s:tr>
								<c:if test="${tx_dettaglio.nodoSpcEsitoInvioRevocaEmailContribuente == 'Y'}">
									<s:tr>
										<s:td cssclass="seda-ui-cellheader">Email Annullo:</s:td>
										<s:td>Inviata</s:td>
										<s:td cssclass="seda-ui-cellheader">Data invio:</s:td>
										<s:td><s:formatDate pattern="dd/MM/yyyy" value="${tx_dettaglio.nodoSpcDataInvioRevocaEmailContribuente}"/></s:td>
									</s:tr>
								</c:if>
								<c:if test="${tx_dettaglio.nodoSpcEsitoInvioRevocaEmailContribuente != 'Y'}">
									<s:tr>
										<s:td cssclass="seda-ui-cellheader">Email Annullo:</s:td>
										<s:td>Non Inviata</s:td>
										<s:td cssclass="seda-ui-cellheader">Data Invio:</s:td>
										<s:td></s:td>
									</s:tr>
								</c:if>
							</c:if>
							</c:if>
						</c:forEach>
					</c:if>
				</c:if>
				<c:if test="${lastIuv == ''}" >	
					<c:if test="${!empty tx_lista_freccia}">
						<c:forEach items="${tx_lista_freccia}" var="tx_dettaglio" >
							<c:if test="${!empty tx_dettaglio.nodoSpcIuv && tx_dettaglio.nodoSpcIuv!=lastIuv}">
								<c:set  var="lastIuv"  value="${tx_dettaglio.nodoSpcIuv}" />
							<s:tr>
								<s:td cssclass="seda-ui-cellheader">Codice IUV:</s:td>
								<s:td icol="2">${tx_dettaglio.nodoSpcIuv}</s:td>
								<s:td >
									<c:if test="${!empty (tx_dettaglio.nodoSpcRt)}">
										<s:hyperlink
										href="dettaglioTransazioneViewRT.do?tx_button_download_rt=1&iuv=${tx_dettaglio.nodoSpcIuv}&tx_codice_transazione_hidden=${tx_transazione.chiave_transazione}"
										imagesrc="../applications/templates/monitoraggio/img/icoRicevuta.png"
										alt="Scarica Ricevuta Telematica RT" text=""
										cssclass="hlStyle" />
									</c:if>
								</s:td>
							</s:tr>
							<s:tr>
								<s:td cssclass="seda-ui-cellheader">Codice IUR:</s:td>
								<s:td icol="3">${tx_transazione.codice_identificativo_banca}</s:td>
							</s:tr>
							<c:set var="statoAnnullo" value="" />	
							<c:if test="${tx_dettaglio.nodoSpcErEsito == 'OK'}">
								<c:set var="statoAnnullo" value="Si" />
								<c:if test="${empty (tx_dettaglio.nodoSpcRt)}">
									<c:set var="statoAnnullo" value="Attesa RT" />
								</c:if>	
								<s:tr>
									<s:td cssclass="seda-ui-cellheader">Annullo:</s:td>
									<s:td icol="2">${statoAnnullo}</s:td>
									<s:td >
										<c:if test="${!empty (tx_dettaglio.nodoSpcRtAnnullata)}">
											<s:hyperlink
											href="dettaglioTransazioneViewRT.do?tx_button_download_rt=1&rt_annullata=1&iuv=${tx_dettaglio.nodoSpcIuv}&tx_codice_transazione_hidden=${tx_transazione.chiave_transazione}"
											imagesrc="../applications/templates/monitoraggio/img/icoRicevuta.png"
											alt="Scarica Ricevuta Telematica RT Annullata" text=""
											cssclass="hlStyle" />
										</c:if>
									</s:td>
								</s:tr>
								<c:if test="${tx_dettaglio.nodoSpcEsitoInvioRevocaEmailContribuente == 'Y'}">
									<s:tr>
										<s:td cssclass="seda-ui-cellheader">Email Annullo:</s:td>
										<s:td>Inviata</s:td>
										<s:td cssclass="seda-ui-cellheader">Data invio:</s:td>
										<s:td><s:formatDate pattern="dd/MM/yyyy" value="${tx_dettaglio.nodoSpcDataInvioRevocaEmailContribuente}"/></s:td>
									</s:tr>
								</c:if>
								<c:if test="${tx_dettaglio.nodoSpcEsitoInvioRevocaEmailContribuente != 'Y'}">
									<s:tr>
										<s:td cssclass="seda-ui-cellheader">Email Annullo:</s:td>
										<s:td>Non Inviata</s:td>
										<s:td cssclass="seda-ui-cellheader">Data Invio:</s:td>
										<s:td></s:td>
									</s:tr>
								</c:if>
							</c:if>
							</c:if>
						</c:forEach>
					</c:if>
				</c:if>
				<!--  fine LP PG1902020 -->
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Note:</s:td>
					<s:td icol="3">${tx_transazione.note_generiche}</s:td>
				</s:tr>
			</s:tbody>
		</s:table>
	</c:if>
	<c:if test="${!empty tx_lista_dettaglio}">
		<c:forEach items="${tx_lista_dettaglio}" var="tx_dettaglio" >
		    <s:table border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglio2" >
				<s:thead>
					<s:tr> 
						<s:th cssclass="seda-ui-cellheader thDettaglioTitle2" icol="4">${tx_dettaglio.descrizione_bollettino}</s:th>
					</s:tr>
				</s:thead>
				<s:tbody>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Ente Impositore:</s:td>
						<s:td icol="3">${tx_dettaglio.descrizione_ente} ${tx_dettaglio.descrizione_ufficio}</s:td>
					</s:tr>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Tipologia Servizio:</s:td>
						<s:td icol="3">${tx_dettaglio.codice_tipologia_servizio} ${tx_dettaglio.descrizione_tipologia_servizio}</s:td>
					</s:tr>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Importo bollettino:</s:td>
						<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_dettaglio.importo_totale_bollettino}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
						<s:td cssclass="seda-ui-cellheader">Rendicontato:</s:td>
						<s:td>
							<c:choose>
								<c:when test="${empty tx_dettaglio.chiave_spedizione}">No</c:when> 
								<c:otherwise>Si</c:otherwise>
							</c:choose>
						</s:td>
					</s:tr>
					<c:if test="${(tx_dettaglio.tipo_bollettino eq 'PREM') or (tx_dettaglio.tipo_bollettino eq 'PREA')}">
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Id. Bollettino:</s:td>
							<s:td>${tx_dettaglio.codice_bollettino_premarcato_mav}</s:td>
							<s:td cssclass="seda-ui-cellheader">TD:</s:td>
							<s:td>${tx_dettaglio.tipo_documento_host}</s:td>
						</s:tr>
					</c:if>				
					<c:if test="${(tx_dettaglio.tipo_bollettino eq 'CDSM') or (tx_dettaglio.tipo_bollettino eq 'CDSA')}">
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Id. Bollettino:</s:td>
							<s:td>${tx_dettaglio.codice_bollettino_premarcato_mav}</s:td>
							<s:td cssclass="seda-ui-cellheader">TD:</s:td>
							<s:td>${tx_dettaglio.tipo_documento_host}</s:td>
						</s:tr>
					</c:if>				
					<c:if test="${(tx_dettaglio.tipo_bollettino eq 'MAVM') or (tx_dettaglio.tipo_bollettino eq 'MAVA')}">
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Cod. MAV:</s:td>
							<s:td>${tx_dettaglio.codice_bollettino_premarcato_mav}</s:td>
							<s:td cssclass="seda-ui-cellheader">TD:</s:td>
							<s:td>${tx_dettaglio.tipo_documento_host}</s:td>
						</s:tr>
					</c:if>				
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">C/C n.:</s:td>
						<s:td>${tx_dettaglio.numero_conto_corrente}</s:td>
						<s:td cssclass="seda-ui-cellheader">Intestato a:</s:td>
						<s:td>${tx_dettaglio.descrizione_intestatario_conto_corrente}</s:td>
					</s:tr>
	
					<%--  Dati bollettino Bollo --%>
					<c:if test="${(tx_dettaglio.tipo_bollettino eq 'BOLM') or (tx_dettaglio.tipo_bollettino eq 'BOLA')}">
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Importo tassa:</s:td>
							<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_dettaglio.importo_oneri}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
							<s:td cssclass="seda-ui-cellheader">Targa:</s:td>
							<s:td>${tx_dettaglio.targa}</s:td>
						</s:tr>
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Sanzioni:</s:td>
							<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_dettaglio.importo_residuo_totale}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
							<s:td cssclass="seda-ui-cellheader">Categoria:</s:td>
							<s:td>${tx_dettaglio.categoria_bollo_auto}</s:td>
						</s:tr>
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Interessi:</s:td>
							<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_dettaglio.importo_residuo_compenso}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
							<s:td cssclass="seda-ui-cellheader">Riduzione:</s:td>
							<s:td cssclass="text_align_right">${tx_dettaglio.mesi_riduzione_bollo_auto}</s:td>
						</s:tr>
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Scadenza:</s:td>
							<s:td>${tx_dettaglio.mese_scadenza_bollo_auto} ${tx_dettaglio.anno_scadenza_bollo_auto}</s:td>
							<s:td cssclass="seda-ui-cellheader">Mesi di validit&agrave;:</s:td>
							<s:td cssclass="text_align_right">${tx_dettaglio.mesi_validita_bollo_auto}</s:td>
						</s:tr>
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Codice fiscale/P.IVA:</s:td>
							<s:td>${tx_dettaglio.codice_fiscale}</s:td>
							<s:td cssclass="seda-ui-cellheader">Strumento:</s:td>
							<s:td>${tx_dettaglio.codice_servizio}</s:td>
						</s:tr>
					</c:if>
					<%--  Dati bollettino CDS --%>
					<c:if test="${(tx_dettaglio.tipo_bollettino eq 'CDSM') or (tx_dettaglio.tipo_bollettino eq 'CDSA')}">
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Numero verbale:</s:td>
							<s:td>${tx_dettaglio.numero_documento}</s:td>
							<s:td cssclass="seda-ui-cellheader">Data verbale:</s:td>
							<s:td><s:formatDate pattern="dd/MM/yyyy" value="${tx_dettaglio.data_sanzione}"/></s:td>
							
						</s:tr>
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Codice fiscale/P.IVA:</s:td>
							<s:td>${tx_dettaglio.codice_fiscale}</s:td>
							<s:td cssclass="seda-ui-cellheader">Targa:</s:td>
							<s:td>${tx_dettaglio.targa}</s:td>
						</s:tr>
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Strumento:</s:td>
							<s:td icol="3">${tx_dettaglio.codice_servizio}</s:td>
						</s:tr>
					</c:if>
					<%--  Dati bollettino MAV --%>
					<c:if test="${(tx_dettaglio.tipo_bollettino eq 'MAVM') or (tx_dettaglio.tipo_bollettino eq 'MAVA')}">
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Riferimento:</s:td>
							<s:td>${tx_dettaglio.numero_documento}</s:td>
							<s:td cssclass="seda-ui-cellheader">Strumento:</s:td>
							<s:td>${tx_dettaglio.codice_servizio}</s:td>
						</s:tr>
					</c:if>
	
					<%--  Dati bollettino Premarcato --%>
					<c:if test="${(tx_dettaglio.tipo_bollettino eq 'PREM') or (tx_dettaglio.tipo_bollettino eq 'PREA')}">
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Numero Documento:</s:td>
							<s:td>${tx_dettaglio.numero_documento}</s:td>
							<s:td cssclass="seda-ui-cellheader">Scadenza:</s:td>
							<s:td><s:formatDate pattern="dd/MM/yyyy hh:mm:ss" value="${tx_dettaglio.data_scadenza_rata}"/></s:td>
							
						</s:tr>
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Codice fiscale/P.IVA:</s:td>
							<s:td>${tx_dettaglio.codice_fiscale}</s:td>
							<s:td cssclass="seda-ui-cellheader">Numero Rata:</s:td>
							<s:td>${tx_dettaglio.progressivo_rata}</s:td>
						</s:tr>
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Strumento:</s:td>
							<s:td icol="3">${tx_dettaglio.codice_servizio}</s:td>
						</s:tr>
					</c:if>
					<%--  Dati bollettino spontaneo --%>
					<c:if test="${(tx_dettaglio.tipo_bollettino eq 'SPOM') or (tx_dettaglio.tipo_bollettino eq 'SPOA')}">
						<!-- PG200110 -->
						<c:choose>
							<c:when test="${spom3}">
								<s:tr>
									<s:td cssclass="seda-ui-cellheader">Strumento:</s:td>
									<s:td icol="3">${tx_dettaglio.codice_servizio}</s:td>
								</s:tr>
								<s:tr>
									<s:td cssclass="seda-ui-cellheader">Causale:</s:td>
									<s:td icol="3">${requestScope.causale}</s:td>
								</s:tr>
								<s:tr>
									<s:td cssclass="seda-ui-cellheader">Data riferimento:</s:td>
									<s:td icol="3">${requestScope.dataRiferimento}</s:td>
								</s:tr>
								<s:tr>
									<s:td cssclass="seda-ui-cellheader">Tipologia pagamento:</s:td>
									<s:td icol="3">${requestScope.cespite}</s:td>
								</s:tr>
							</c:when>
							<c:otherwise>
								<s:tr>
									<s:td cssclass="seda-ui-cellheader">Strumento:</s:td>
									<s:td icol="3">${tx_dettaglio.codice_servizio}</s:td>
								</s:tr>
								<s:tr>
									<s:td cssclass="seda-ui-cellheader">Causale:</s:td>
									<s:td icol="3">comprensivo di Causale del Servizio, Anno Riferimento, Cespite ${tx_dettaglio.note_premarcato}</s:td>
								</s:tr>
							</c:otherwise>
						</c:choose>
						<!-- FINE PG200110 -->
					</c:if>
					
					<c:choose>
							<c:when test="${spom3}">
								<s:tr>
									<s:td cssclass="seda-ui-cellheader">Eseguito da:</s:td>
									<s:td icol="3">${tx_dettaglio.denominazione}</s:td>									
								</s:tr>
								<s:tr>
									<s:td cssclass="seda-ui-cellheader">Codice Fiscale:</s:td>
									<s:td>${tx_dettaglio.codice_fiscale}</s:td>
									<s:td cssclass="seda-ui-cellheader">Provincia:</s:td>
									<s:td>${tx_dettaglio.provincia}</s:td>
								</s:tr>
							</c:when>
							<c:otherwise>
								<s:tr>
									<s:td cssclass="seda-ui-cellheader">Eseguito da:</s:td>
									<s:td icol="3">${tx_dettaglio.denominazione} ${tx_dettaglio.indirizzo} ${tx_dettaglio.cap} ${tx_dettaglio.citta} ${tx_dettaglio.provincia}</s:td>
								</s:tr>
							</c:otherwise>
					</c:choose>

				</s:tbody>
			</s:table>
		</c:forEach>
	</c:if>
	
	
	<c:if test="${!empty tx_lista_ici}">
		
		<c:forEach items="${tx_lista_ici}" var="tx_ici" >
			<s:table border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglio2" >
				<s:thead>
					<s:tr> 
						<s:th cssclass="seda-ui-cellheader thDettaglioTitle2" icol="4">
							<c:if test="${(tx_ici.tipo_bollettino eq 'ICIM') or (tx_ici.tipo_bollettino eq 'ICIA')}">Bollettino ICI</c:if>
							<c:if test="${(tx_ici.tipo_bollettino eq 'ISCM') or (tx_ici.tipo_bollettino eq 'ISCA')}">Bollettino ISCOP</c:if>
						</s:th>
					</s:tr>
				</s:thead>
				<s:tbody>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Ente Impositore:</s:td>
						<s:td icol="3">${tx_ici.descrizione_ente} ${tx_ici.descrizione_ufficio}</s:td>
					</s:tr>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Tipologia Servizio:</s:td>
						<s:td icol="3">${tx_ici.codice_tipologia_servizio} ${tx_ici.descrizione_tipologia_servizio}</s:td>
					</s:tr>			
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Importo bollettino:</s:td>
						<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_ici.importo_movimento}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
						<s:td cssclass="seda-ui-cellheader">Rendicontato:</s:td>
						<s:td>
							<c:choose>
								<c:when test="${empty tx_ici.chiave_spedizione}">No</c:when> 
								<c:otherwise>Si</c:otherwise>
							</c:choose>
						</s:td>
					</s:tr>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">C/C n.:</s:td>
						<s:td>${tx_ici.numero_conto_corrente}</s:td>
						<s:td cssclass="seda-ui-cellheader">Intestato a:</s:td>
						<s:td>${tx_ici.descrizione_intestatario_conto_corrente}</s:td>
					</s:tr>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Comune Ubicazione:</s:td>
						<s:td>${tx_ici.comune_di_ubicazione_immobile}</s:td>
						<s:td cssclass="seda-ui-cellheader">CAP Ubicazione:</s:td>
						<s:td>${tx_ici.cap_comune_ubicazione_immobile}</s:td>
					</s:tr>
					<c:if test="${(tx_ici.tipo_bollettino eq 'ICIM') or (tx_ici.tipo_bollettino eq 'ICIA')}">
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Terreni Agricoli:</s:td>
							<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_ici.importo_terreni_agricoli}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
							<s:td cssclass="seda-ui-cellheader">Numero Fabbricati:</s:td>
							<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_ici.numero_fabbricati}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
						</s:tr>
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Aree Fabbricabili:</s:td>
							<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_ici.importo_aree_fabbricabili}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
							<s:td cssclass="seda-ui-cellheader">Anno d&quot;Imposta:</s:td>
							<s:td cssclass="text_align_right">${tx_ici.anno_imposta}</s:td>
						</s:tr>
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Abitaz. Principale:</s:td>
							<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_ici.importo_abitazione_principale}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
							<s:td cssclass="seda-ui-cellheader">Versamento Acconto:</s:td>
							<s:td>
								<c:choose>
									<c:when test="${(tx_ici.flag_rata eq '0') or (tx_ici.flag_rata eq '3')}">Si</c:when> 
									<c:otherwise>No</c:otherwise>
								</c:choose>
							</s:td>
						</s:tr>
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Altri Fabbricati:</s:td>
							<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_ici.importo_altri_fabbricati}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
							<s:td cssclass="seda-ui-cellheader">Versamento Saldo:</s:td>
							<s:td>
								<c:choose>
									<c:when test="${(tx_ici.flag_rata eq '1') or (tx_ici.flag_rata eq '3')}">Si</c:when> 
									<c:otherwise>No</c:otherwise>
								</c:choose>
							</s:td>
						</s:tr>
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Ravvedimento:</s:td>
							<s:td>
								<c:choose>
									<c:when test="${tx_ici.flag_ravvedimento eq 'N'}">No</c:when> 
									<c:otherwise>Si</c:otherwise>
								</c:choose>
							</s:td>
							<s:td cssclass="seda-ui-cellheader">Detr.COM. Ab.Princ.:</s:td>
							<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_ici.importo_detrazione_comunale}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
						</s:tr>
						
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Codice fiscale/P.IVA:</s:td>
							<s:td>${tx_ici.codice_fiscale}</s:td>
							<s:td cssclass="seda-ui-cellheader">Detr.STAT. Ab.Princ.:</s:td>
							<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_ici.importo_detrazione_statale}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
						</s:tr>
					</c:if>
	
					<c:if test="${(tx_ici.tipo_bollettino eq 'ISCM') or (tx_ici.tipo_bollettino eq 'ISCA')}">
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Imponibile ICI:</s:td>
							<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_ici.imponibile_ici_per_iscop}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
							<s:td cssclass="seda-ui-cellheader">Anno d&quot;Imposta:</s:td>
							<s:td cssclass="text_align_right">${tx_ici.anno_imposta}</s:td>
						</s:tr>
						
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Detrazione:</s:td>
							<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_ici.detrazione_per_iscop}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
							<s:td cssclass="seda-ui-cellheader">Versamento Acconto:</s:td>
							<s:td>
								<c:choose>
									<c:when test="${(tx_ici.flag_rata eq '0') or (tx_ici.flag_rata eq '3')}">Si</c:when> 
									<c:otherwise>No</c:otherwise>
								</c:choose>
							</s:td>
						</s:tr>
						
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Riduzione:</s:td>
							<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_ici.riduzione_per_iscop}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
							<s:td cssclass="seda-ui-cellheader">Versamento Saldo:</s:td>
							<s:td>
								<c:choose>
									<c:when test="${(tx_ici.flag_rata eq '1') or (tx_ici.flag_rata eq '3')}">Si</c:when> 
									<c:otherwise>No</c:otherwise>
								</c:choose>
							</s:td>
						</s:tr>
						<s:tr>
							<s:td cssclass="seda-ui-cellheader">Codice fiscale/P.IVA:</s:td>
							<s:td>${tx_ici.codice_fiscale}</s:td>  
							<s:td cssclass="seda-ui-cellheader">Ravvedimento:</s:td>
							<s:td>
								<c:choose>
									<c:when test="${tx_ici.flag_ravvedimento eq 'N'}">No</c:when> 
									<c:otherwise>Si</c:otherwise>
								</c:choose>
							</s:td>
						</s:tr>
					</c:if>
	
	
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Strumento:</s:td>
						<s:td icol="3">${tx_ici.codice_servizio}</s:td>
					</s:tr>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Codice IUV:</s:td>
						<s:td icol="2">${tx_ici.nodoSpcIuv}</s:td>
						<s:td >
							<c:if test="${!empty (tx_ici.nodoSpcRt)}">
								<s:hyperlink
								href="dettaglioTransazioneViewRT.do?tx_button_download_rt=1&iuv=${tx_ici.nodoSpcIuv}&tx_codice_transazione_hidden=${tx_transazione.chiave_transazione}"
								imagesrc="../applications/templates/monitoraggio/img/icoRicevuta.png"
								alt="Scarica Ricevuta Telematica RT" text=""
								cssclass="hlStyle" />
							</c:if>
						</s:td>
					</s:tr>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Eseguito da:</s:td>
						<s:td icol="3">${fn:replace(tx_ici.denominazione,"|"," ")} ${tx_ici.indirizzo_domicilio_fiscale} ${tx_ici.domicilio_fiscale}</s:td>
					</s:tr>
				</s:tbody>
			</s:table>	
		</c:forEach>
	</c:if>
	
	
	<c:if test="${!empty tx_lista_freccia}">
		<c:forEach items="${tx_lista_freccia}" var="tx_freccia" >
			<s:table border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglio2" >
				<s:thead>
					<s:tr> 
						<s:th cssclass="seda-ui-cellheader thDettaglioTitle2" icol="4">Bollettino FRECCIA</s:th>
					</s:tr>
				</s:thead>
				<s:tbody>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Ente Impositore:</s:td>
						<s:td icol="3">${tx_freccia.descrizione_ente} ${tx_freccia.descrizione_ufficio}</s:td>
					</s:tr>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Tipologia Servizio:</s:td>
						<s:td icol="3">${tx_freccia.codice_tipologia_servizio} ${tx_freccia.descrizione_tipologia_servizio}</s:td>
					</s:tr>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Importo bollettino:</s:td>
						<s:td cssclass="text_align_right"><fmt:formatNumber type="NUMBER" value="${tx_freccia.importo_totale_bollettino}" minFractionDigits="2" maxFractionDigits="2"/></s:td>
						<s:td cssclass="seda-ui-cellheader">Rendicontato:</s:td>
						<s:td>
							<c:choose>
								<c:when test="${empty tx_freccia.chiave_spedizione}">No</c:when> 
								<c:otherwise>Si</c:otherwise>
							</c:choose>
						</s:td>
					</s:tr>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Cod. Id. Pagamento:</s:td>
						<s:td>${tx_freccia.codice_identificativo_pagamento}</s:td>
						<s:td cssclass="seda-ui-cellheader">Motivo Pagamento:</s:td>
						<s:td>${tx_freccia.motivo_del_pagamento}</s:td>
					</s:tr>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Coor. Bancarie:</s:td>
						<s:td>${tx_freccia.codice_iban}</s:td>
						<s:td cssclass="seda-ui-cellheader">Intestato a:</s:td>
						<s:td>${tx_freccia.descrizione_intestatario_conto_corrente}</s:td>
					</s:tr>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Codice Freccia:</s:td>
						<s:td encode="true">${tx_freccia.codice_freccia}</s:td>
						<s:td cssclass="seda-ui-cellheader">Strumento:</s:td>
						<s:td>${tx_freccia.codice_servizio}</s:td>
					</s:tr>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Codice IUV:</s:td>
						<s:td icol="2">${tx_freccia.nodoSpcIuv}</s:td>
						<s:td >
							<c:if test="${!empty (tx_freccia.nodoSpcRt)}">
								<s:hyperlink
								href="dettaglioTransazioneViewRT.do?tx_button_download_rt=1&iuv=${tx_freccia.nodoSpcIuv}&tx_codice_transazione_hidden=${tx_transazione.chiave_transazione}"
								imagesrc="../applications/templates/monitoraggio/img/icoRicevuta.png"
								alt="Scarica Ricevuta Telematica RT" text=""
								cssclass="hlStyle" />
							</c:if>
						</s:td>
					</s:tr>
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Eseguito da:</s:td>
						<s:td icol="3">${tx_freccia.denominazione_contribuente} ${tx_freccia.indirizzo} ${tx_freccia.codice_cap} ${tx_freccia.citta} ${tx_freccia.provincia}</s:td>
					</s:tr>
				</s:tbody>
			</s:table>
		</c:forEach>
	</c:if>
	
	<c:if test="${!empty tx_lista_oneri}">
		<s:div cssclass="thDettaglioTitle2 divTitleSimilTable" name="divTitleOneri">Ripartizione Oneri</s:div>
		<s:datagrid cachedrowset="tx_lista_oneri" action="" border="1" usexml="true">		
			<s:dgcolumn index="3" label="Ente Destinatario Onere" />
			<s:dgcolumn label="Beneficiario <br />[Cod.Utente - Cod.Ente - Tp.Uff - Cod.Uff]">
				[{9} - {11} - {12} - {13}]<br />{14}
			</s:dgcolumn>
			<s:dgcolumn index="4" label="Importo Totale" format="#,##0.00" css="text_align_right" />
			<s:dgcolumn index="6" label="Importo Contabile Ingresso Totale" format="#,##0.00" css="text_align_right"/>
			<s:dgcolumn index="7" label="Importo Contabile Uscita Totale" format="#,##0.00" css="text_align_right"/>
			<s:dgcolumn index="5" label="Causale" />
		</s:datagrid>
	</c:if>
</s:div>
<br/>







