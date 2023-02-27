<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="ConfRendUtenteServizioEnte_search" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="search_form" action="ConfRendUtenteServizioEnteSearch.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA CONFIGURAZIONI RENDICONTAZIONE UTENTE - TIPOLOGIA SERVIZIO - ENTE
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				
					<s:div name="divElement1" cssclass="divRicMetadatiLeft">
						
							<s:textbox bmodify="true" name="descrizioneSocieta"
								label="Societ&agrave;:" maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								message="[accept=Societ&agrave;: ${msg_configurazione_descrizione_regex}]"
								text="${descrizioneSocieta}" />
						
					</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						
							<s:textbox bmodify="true" name="descrizioneUtente"
								label="Utente:" maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								message="[accept=Utente: ${msg_configurazione_descrizione_regex}]"
								text="${descrizioneUtente}" />
						
					</s:div>
					
					<s:div name="divElement3" cssclass="divRicMetadatiRight">
						
							<s:textbox bmodify="true" name="descrizioneEnte"
								label="Ente:" maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman"
								text="${descrizioneEnte}" />
						
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
			ELENCO CONFIGURAZIONI RENDICONTAZIONE UTENTE - TIPOLOGIA SERVIZIO - ENTE
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="listaConfig"  
			action="ConfRendUtenteServizioEnteSearch.do?vista=ConfRendUtenteServizioEnte_search" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

		<!-- DESCRIZIONE SOCIETA' -->
			<s:dgcolumn label="Societ&agrave;" index="5" />
			
		<!-- DESCRIZIONE UTENTE  -->
			<s:dgcolumn label="Utente" index="6" />

		<!-- DESCRIZIONE ENTE  --> 
			<s:dgcolumn label="Ente" index="7" />


	 	<!-- PG22XX09_YL5 INI -->
			<s:dgcolumn label="Tipol. Serv." index="4" />
			<s:dgcolumn label="Descr. Tipol. Serv." index="8" />
		<!-- PG22XX09_YL5 FINE -->
		<!-- FLAG ABILITAZIONE INVIO EMAIL -->
			<s:dgcolumn label="Invio Email"   >
				<s:if right="{13}" control="eq" left="Y">
					<s:then>SI</s:then>
					<s:else>NO</s:else>
				</s:if>
			</s:dgcolumn>

		<!-- FLAG ABILITAZIONE INVIO FTP -->
			<s:dgcolumn label="Invio FTP" >
				<s:if right="{18}" control="eq" left="Y">
					<s:then>SI</s:then>
					<s:else>NO</s:else>
				</s:if>
			</s:dgcolumn>
			
		<!-- features/2/PAGONET-284 INI-->
			<s:dgcolumn label="Invio WS" >
				<s:if right="{23}" control="eq" left="Y">
					<s:then>SI</s:then>
					<s:else>NO</s:else>
				</s:if>
			</s:dgcolumn>
		<!-- features/2/PAGONET-284 FINE-->

		<!-- AZIONI -->
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">

		<!-- MODIFICA RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="ConfRendUtenteServizioEnteEdit.do?tx_societa={1}&tx_utente={2}&tx_ente={3}&tx_codiceTipologiaServizio={4}"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />
		<!-- CANCELLAZIONE RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="ConfRendUtenteServizioEnteCancel.do?tx_societa={1}&tx_utente={2}&tx_ente={3}&tx_codiceTipologiaServizio={4}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Elimina" text=""   />
			</s:dgcolumn>
			
		</s:datagrid>
	</s:div>

</c:if>
