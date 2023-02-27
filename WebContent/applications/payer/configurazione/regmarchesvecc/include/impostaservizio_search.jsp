<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="impostaservizio" encodeAttributes="true"/>
<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
<s:div name="divRicercaTopName" cssclass="divRicercaTop">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Imposte Servizio</s:div>
	<s:form name="form_ricerca" action="impostaservizio.do"
		method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				<c:choose>
					<c:when test="${userAppl_codiceSocieta == ''}">
						

							<s:div name="divElement1" cssclass="divRicMetadatiLeft">
								
								<s:textbox bmodify="true"
									name="impostaservizio_companyCode" label="Societ&agrave;:"
									maxlenght="256" 
									cssclasslabel="label85 bold textright"
									cssclass="textareaman"
									validator="ignore;accept=${configurazione_descrizione256_regex}"
									message="[accept=Societ&agrave;: ${msg_configurazione_descrizione_regex}]"
									text="${impostaservizio_companyCode}" />
														
								
							</s:div>
						
					</c:when>
				</c:choose>

				
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:textbox bmodify="true"
							name="impostaservizio_codiceTipologiaServizio"
							label="Tipol. Serv.:" maxlenght="256"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman"
							validator="ignore;accept=${configurazione_descrizione256_regex}"
							message="[accept=Tipol. Serv.: ${msg_configurazione_descrizione_regex}]"
							text="${impostaservizio_codiceTipologiaServizio}" />
					
				</s:div>

				
					<s:div name="divElement3" cssclass="divRicMetadatiRight">
						<s:textbox bmodify="true"
							name="impostaservizio_codiceImpostaServizio"
							label="Cod. Tp. Serv. SE:" maxlenght="20"
							validator="ignore;minlength=2;accept=${configurazione_imposta_servizio_regex}"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman"
							message="[accept=Imposta Serv.: ${msg_configurazione_alfanumerici_maiuscolo}]"
							text="${impostaservizio_codiceImpostaServizio}" />
						
					</s:div>
				
				</s:div>
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true"
							name="impostaservizio_descrizioneImpostaServizio"
							label="Descrizione:" maxlenght="256"
							validator="ignore;accept=${configurazione_descrizione256_regex}"
							message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman"
							text="${impostaservizio_descrizioneImpostaServizio}" />
					</s:div>
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
				
				
			<!--<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca"
					type="submit" cssclass="btnStyle"/>
				<s:button id="tx_button_reset" onclick="" text="Reset"
					type="reset" cssclass="btnStyle"/>
					</s:div>
			</s:form>
					<s:form name="form_ricerca" action="impostaservizio.do"
		method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<input type="hidden" name="action" value="add" />
		<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
			<s:button id="tx_button_cerca" onclick="" text="Nuovo"
				type="submit" cssclass="btnStyle"/>
		</s:div>
	--></s:form>
			
	</s:div>
	

	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Imposte Servizio
	</s:div>
</s:div>
