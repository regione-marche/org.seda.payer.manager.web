<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="ConfRendUtenteServizio_search" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="search_form" action="ConfRendUtenteServizioSearch.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA CONFIGURAZIONI RENDICONTAZIONE UTENTE - TIPOLOGIA SERVIZIO
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				
					<s:div name="divElement1" cssclass="divRicMetadatiLeft">
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="descrizioneSocieta"
								label="Societ&agrave;:" maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								message="[accept=Societ&agrave;: ${msg_configurazione_descrizione_regex}]"
								text="${descrizioneSocieta}" />
						</s:div>
					</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="descrizioneUtente"
								label="Utente:" maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								message="[accept=Utente: ${msg_configurazione_descrizione_regex}]"
								text="${descrizioneUtente}" />
						</s:div>
					</s:div>
										
				</s:div>
				
				
				<!-- PG22XX09_YL5 INI -->
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="descrizioneTipologiaServizio"
							label="Descr. Tipol. Serv.:" maxlenght="256"
								validator="ignore;accept=${configurazione_descrizione256_regex}"
							cssclasslabel="label85 bold textright"
							message="[accept=Descr.Tipol.Serv.: ${msg_configurazione_descrizione_regex}]"
							cssclass="textareaman" text="${descrizioneTipologiaServizio}"  />
					</s:div>				
				</s:div>
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
				    <s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
				        <s:textbox 
				        bmodify="true" 
				        name="codiceTipologiaServizio"
				        label="Tipol. Serv.:" 
				        maxlenght="3"
				        validator="ignore;accept=${configurazione_descrizione256_regex}"
				        cssclasslabel="label85 bold textright"
				        message="[accept=Tipol.Serv.: ${msg_configurazione_descrizione_regex}]"
				        cssclass="textareaman" 
				        text="${codiceTipologiaServizio}" />
				    </s:div>
				</s:div>
				<!-- PG22XX09_YL5 FINE -->

			</s:div>

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
			ELENCO CONFIGURAZIONI RENDICONTAZIONE UTENTE - TIPOLOGIA SERVIZIO
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="listaConfig"  
			action="ConfRendUtenteServizioSearch.do?vista=ConfRendUtenteServizio_search" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

		<!-- DESCRIZIONE SOCIETA' -->
			<s:dgcolumn label="Societ&agrave;"  index="4" />
			
		<!-- DESCRIZIONE UTENTE  -->
			<s:dgcolumn label="Utente" index="5" />

		
	 	<!-- PG22XX09_YL5 INI -->
			<s:dgcolumn label="Tipol. Serv." index="3" />
			<s:dgcolumn label="Descr. Tipol. Serv." index="6" />
		<!-- PG22XX09_YL5 FINE -->



		<!-- FLAG ABILITAZIONE INVIO EMAIL -->
			<s:dgcolumn label="Invio Email"  >
				<s:if right="{11}" control="eq" left="Y">
					<s:then>SI</s:then>
					<s:else>NO</s:else>
				</s:if>
			</s:dgcolumn>

		<!-- FLAG ABILITAZIONE INVIO FTP -->
			<s:dgcolumn label="Invio FTP"  >
				<s:if right="{16}" control="eq" left="Y">
					<s:then>SI</s:then>
					<s:else>NO</s:else>
				</s:if>
			</s:dgcolumn>

		<!-- AZIONI -->
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">

		<!-- MODIFICA RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="ConfRendUtenteServizioEdit.do?tx_societa={1}&tx_utente={2}&tx_codiceTipologiaServizio={3}"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />
		<!-- CANCELLAZIONE RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="ConfRendUtenteServizioCancel.do?tx_societa={1}&tx_utente={2}&tx_codiceTipologiaServizio={3}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Elimina" text=""   />
			</s:dgcolumn>
			
		</s:datagrid>
	</s:div>

</c:if>
