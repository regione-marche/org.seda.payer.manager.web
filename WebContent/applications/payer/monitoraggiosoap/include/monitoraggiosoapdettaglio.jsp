<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="monitoraggiosoapdettaglio" encodeAttributes="true" />

<s:div name="divOuter" cssclass="div_align_center">
<!-- form per i messaggi di errore -->
	<s:form name="monitoraggioSoapDetailForm" action="none" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:div name="divMargin" cssclass="divMargin">
		</s:div>
	</s:form>
</s:div>


<!-- //  NEX_KTRAKTRA chiaveTransazione
//	NEX_CSOCCSOC codiceSocieta
//	SOC_DSOCDSOC descrizioneSocieta
//	NEX_CUTECUTE codiceUtente
//	UTE_DUTEDUTE descrizioneUtente
//	NEX_KANEKENT codiceEnte
//	ANE_DANEDENT descrizioneEnte
//	NEX_CNEXPORT urlPortale
//	NEX_CNEXNDOC numeroDocumento
//	NEX_CNEXNAVV numeroAvvisoPagoPA
//	NEX_CNEXCFIS codiceFiscale
//	NEX_GNEXDNOT dataNotifica
//	NEX_GNEXDRNO dataRispostaNotifica
//	NEX_CNEXUESI esito
//	NEX_INEXIMPO importoPagato
//	NEX_GNEXDPAG dataPagamento
//	NEX_CNEXRXML xmlRicevuta
//	NEX_NNEXCORR numeroTentativiInvioNotifica
//	NEX_KTDTKTDT chiaveDettaglioTransazione
//	NEX_NNEXCOAN numeroTentativiInvioNotificaAnnulloTecnico
//	NEX_GNEXDIAN dataInvioNotificaAnnulloTecnico
//	NEX_GNEXDRAN dataRispostaNotificaAnnulloTecnico
//	NEX_CNEXUEAN ultimoEsitoNotificaAnnullo
//	NEX_CNEXRRAN xmlRichiestaRevoca -->

<c:if test="${!empty detail_notifica_soap}">
	<s:div name="div_selezione" cssclass="divSelezione">
		<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	
			<s:div name="divRicercaTitleName" cssclass="divRicTitle "><b>Elenco Dettagli Notifica RT</b></s:div>
		
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
	
	 			<s:div name="divLogReqMetadatiTop1" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="key_transazione" label="Chiave transazione:" text="${detail_notifica_soap.chiaveTransazione}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            <s:div name="divLogReqMetadatiTop2" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="key_dettaglio_transazione" label="Chiave dettaglio transazione:" text="${detail_notifica_soap.chiaveDettaglioTransazione}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
                <s:div name="divLogReqMetadatiTop3" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="descr_societa" label="Società:" text="${detail_notifica_soap.descrizioneSocieta}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
                <s:div name="divLogReqMetadatiTop3" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="descr_utente" label="Utente:" text="${detail_notifica_soap.descrizioneUtente}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            <s:div name="divLogReqMetadatiTop4" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="descr_ente" label="Ente:" text="${detail_notifica_soap.descrizioneEnte}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            <s:div name="divLogReqMetadatiTop5" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="url_portale" label="Url portale:" text="${detail_notifica_soap.urlPortale}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            <s:div name="divLogReqMetadatiTop6" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="numero_documento" label="N. documento:" text="${detail_notifica_soap.numeroDocumento}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop7" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="numero_avviso_pagopa" label="N. Avviso PagoPA:" text="${detail_notifica_soap.numeroAvvisoPagoPA}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            <s:div name="divLogReqMetadatiTop8" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="codice_fiscale" label="Cod. Fiscale:" text="${detail_notifica_soap.codiceFiscale}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            	            
				<s:div name="divLogReqMetadatiTop9" cssclass="divLogReqMetadatiTop">
					<s:textarea name="dataNotificaUserFormat" label="Data Notifica: " text="${dataNotificaUserFormat}"
			                bmodify="false" row="1" col="80" 
			                cssclasslabel="logReqDettaglioLabel" 
			                cssclass = "logReqDettaglioTextarea"
	                />
                </s:div>
                <s:div name="divLogReqMetadatiTop10" cssclass="divLogReqMetadatiTop">
					<s:textarea name="dataRispostaNotificaUserFormat" label="Data Risposta Notifica: " text="${dataRispostaNotificaUserFormat}"
			                bmodify="false" row="1" col="80" 
			                cssclasslabel="logReqDettaglioLabel" 
			                cssclass = "logReqDettaglioTextarea"
	                />
                </s:div>

				<s:div name="divLogReqMetadatiTop11" cssclass="divLogReqMetadatiTop">
					<s:textarea name="tentativi_invio_notifica" label="N. Tentativi Invio Notifica:" text="${detail_notifica_soap.numeroTentativiInvioNotifica}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop12" cssclass="divLogReqMetadatiTop">
					<s:textarea name="esito_notifica" label="Esito:" text="${detail_notifica_soap.esito}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop13" cssclass="divLogReqMetadatiTop">
					<s:textarea name="importo_pagato" label="Importo pagato:" text="${detail_notifica_soap.importoPagato}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop14" cssclass="divLogReqMetadatiTop">
					<s:textarea name="dataPagamentoUserFormat" label="Data pagamento:" text="${dataPagamentoUserFormat}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
				
				<s:div name="divLogReqMetadatiTop15" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="xml_ricevuta" label="Xml ricevuta:" text="${detail_notifica_soap.xmlRicevuta}"
	                bmodify="false" row="6" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
		
	            
	            
	        
		        <s:div name="divRicMetadati" cssclass="divRicMetadati">
					<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					
				

					<s:form name="frmIndietro"
						action="ritorna.do?vista=monitoraggiosoap"
						method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
						<s:div name="divIndietro" cssclass="divButton1">
							<s:button id="tx_button_notifiche_soap" type="submit" text="Indietro"
								onclick="" cssclass="btnStyle" />
						</s:div>
					</s:form>

			
					</s:div>
				</s:div>
			   
		
			</s:div>
	
		</s:div>
	
	</s:div>
</c:if>