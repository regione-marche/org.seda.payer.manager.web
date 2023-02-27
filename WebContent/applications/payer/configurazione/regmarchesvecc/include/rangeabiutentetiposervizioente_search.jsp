<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>

<m:view_state id="rangeabiutentetiposervizioentes" encodeAttributes="true"/>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	
	
		<s:form name="form_ricerca" action="rangeabiutentetiposervizioente.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />
			
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Range Tipologia Servizio - Ente</s:div>
			
			<s:div name="divRicercaTitleName1" cssclass="divRicMetadati">
				<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiLeft">
						<s:textbox bmodify="true" name="rangeabiutentetiposervizioente_company"
							label="Societ&agrave;:" maxlenght="256"
							validator="ignore;accept=${configurazione_descrizione256_regex}"
							cssclasslabel="label85 bold textright" cssclass="textareaman"
							message="[accept=Societ&agrave;: ${msg_configurazione_descrizione_regex}]"
							text="${rangeabiutentetiposervizioente_company}" />

					</s:div>
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:textbox bmodify="true" name="rangeabiutentetiposervizioente_utente"
							label="Utente:" maxlenght="256"
							validator="ignore;accept=${configurazione_descrizione256_regex}"
							cssclasslabel="label85 bold textright" cssclass="textareaman"
							message="[accept=Utente: ${msg_configurazione_descrizione_regex}]"
							text="${rangeabiutentetiposervizioente_utente}" />

					</s:div>
					<s:div name="divElement3" cssclass="divRicMetadatiRight">
						<s:textbox bmodify="true"
									 label="Ente:" maxlenght="256"
							validator="ignore;accept=${configurazione_descrizione256_regex}"
									name="ente_strEnte"
									text="${requestScope.ente_strEnte}"
									cssclasslabel="label85 bold floatleft textright"
									message="[accept=Ente: ${msg_configurazione_descrizione_regex}]"
									cssclass="textareaman" />
						
					</s:div>
				</s:div>
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" name="rangeabiutentetiposervizioente_codiceTipologiaServizio"
							label="Tipol. Serv.:" maxlenght="256"
							validator="ignore;accept=${configurazione_descrizione256_regex}"
							cssclasslabel="label85 bold textright" cssclass="textareaman"
							message="[accept=Tipol. Serv.: ${msg_configurazione_descrizione_regex}]"
							text="${rangeabiutentetiposervizioente_codiceTipologiaServizio}" />
									
								
						</s:div>

				</s:div>
						
						
				<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
					
					<s:button id="tx_button_cerca" onclick="" text="Cerca"
						type="submit" cssclass="btnStyle"/>
					<s:button id="tx_button_reset" onclick="" text="Reset"
						type="submit" cssclass="btnStyle"/>
					
				</s:div>
			</s:div>
		</s:form>
		<!--<s:div name="div_stampa_down" cssclass="div_align_center divRisultati divRicBottoniNoJs">
			<s:div name="divCentered" cssclass="divRicBottoni">
				<s:div name="divPdf">	
					<s:form name="form_ricerca" action="anagprovcom.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<input type="hidden" name="action" value="add" />
		<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
			<s:button id="tx_button_cerca" onclick="" text="Nuovo"
				type="submit" cssclass="btnStyle"/>
		</s:div>
	</s:form>
	
				</s:div>
			</s:div>
		
		</s:div>
		--><!--<s:form name="form_ricerca" action="anagprovcom.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<input type="hidden" name="action" value="add" />
		<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
			<s:button id="tx_button_cerca" onclick="" text="Inserisci Nuovo"
				type="submit" cssclass="btnStyle"/>
		</s:div>
	</s:form>
	--></s:div>
	
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Range Tipologia Servizio - Ente
	</s:div>
</s:div>
