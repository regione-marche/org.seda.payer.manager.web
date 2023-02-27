<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="AbilitaCanalePagamentoTipoServizioEnte_search" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="search_form" action="AbilitaCanalePagamentoTipoServizioEnteSearch.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA ABILITAZIONI PER CANALE PAGAMENTO - TIPOLOGIA SERVIZIO - ENTE
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				
					<s:div name="divElement1" cssclass="divRicMetadatiLeft">
						
							<s:textbox bmodify="true" name="descrizioneSocieta"
								label="Societ&agrave;: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${descrizioneSocieta}" message="[accept=Societ&agrave;: ${msg_configurazione_descrizione_regex}]"/>
						
					</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						
							<s:textbox bmodify="true" name="descrizioneUtente"
								label="Utente: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${descrizioneUtente}" message="[accept=Utente: ${msg_configurazione_descrizione_regex}]"/>
						
					</s:div>
					
					<s:div name="divElement3" cssclass="divRicMetadatiRight">
						
							<s:textbox bmodify="true" name="descrizioneEnte"
								label="Ente: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${descrizioneEnte}" message="[accept=Ente: ${msg_configurazione_descrizione_regex}]"/>
						
					</s:div>
					
				</s:div>

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="descrizioneTipologiaServizio"
								label="Tipol. Serv.: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${descrizioneTipologiaServizio}" message="[accept=Tipol. Serv.: ${msg_configurazione_descrizione_regex}]"/>
					</s:div>
				</s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
						<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="descrizioneCanalePagamento"
								label="Canale Pag.: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${descrizioneCanalePagamento}" message="[accept=Canale Pag.: ${msg_configurazione_descrizione_regex}]"/>
						</s:div>
				</s:div>

			</s:div>
			<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_nuovo" validate="false"  onclick="" text="Nuovo" type="submit" cssclass="btnStyle" />
			</s:div>
	</s:form>

	</s:div>
	
</s:div>

<c:if test="${!empty listaConfig}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO ABILITAZIONI PER CANALE PAGAMENTO - TIPOLOGIA SERVIZIO - ENTE
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="listaConfig"  
			action="AbilitaCanalePagamentoTipoServizioEnteSearch.do?vista=AbilitaCanalePagamentoTipoServizioEnte_search" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

		<!-- DESCRIZIONE SOCIETA' -->
			<s:dgcolumn label="Societ&agrave;" index="13" />
			
		<!-- DESCRIZIONE UTENTE -->
			<s:dgcolumn label="Utente" index="10" />

		<!-- DESCRIZIONE ENTE --> 
			<s:dgcolumn label="Ente" index="14" />

		<!-- DESCRIZIONE TIPOLOGIA SERVIZIO -->
			<s:dgcolumn label="Tipologia Servizio" index="12" />
			
		<!-- DESCRIZIONE CANALE PAGAMENTO -->
			<s:dgcolumn label="Canale Pagamento" index="11" />

		<!-- AZIONI -->
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">

		<!-- MODIFICA RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="AbilitaCanalePagamentoTipoServizioEnteEdit.do?tx_societa={1}&tx_utente={2}&tx_ente={3}&tx_canalePagamento={4}&tx_codiceTipologiaServizio={5}"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />
		<!-- CANCELLAZIONE RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="AbilitaCanalePagamentoTipoServizioEnteCancel.do?tx_societa={1}&tx_utente={2}&tx_ente={3}&tx_canalePagamento={4}&tx_codiceTipologiaServizio={5}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Elimina" text=""   />
			</s:dgcolumn>
			
		</s:datagrid>
	</s:div>

</c:if>
