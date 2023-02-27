<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>

<m:view_state id="tipologiaservizio" encodeAttributes="true"/>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
<s:div name="divRicercaTopName" cssclass="divRicercaTop">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Tipologie Servizio
		</s:div>
	<s:form name="form_ricerca" action="tipologiaservizio.do"
		method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				<c:choose>
					<c:when test="${userAppl_codiceSocieta == ''}">
						

							<s:div name="divElement1" cssclass="divRicMetadatiLeft">
								<s:textbox bmodify="true"
							name="tipologiaservizio_company"
							label="Societ&agrave;:" maxlenght="256"
							cssclasslabel="label85 bold textright" cssclass="textareaman"
							validator="ignore;accept=${configurazione_descrizione256_regex}"
							message="[accept=Societ&agrave;: ${msg_configurazione_descrizione_regex}]"
							text="${tipologiaservizio_company}" />
							</s:div>
						
					</c:when>
				</c:choose>

				
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:textbox bmodify="true"
							name="tipologiaservizio_codiceTipologiaServizio"
							label="Tipol. Serv.:" maxlenght="3"
							cssclasslabel="label85 bold textright" cssclass="textareaman"
							validator="ignore;accept=${configurazione_codice1_3obblchar_regex}"
							message="[accept=Tipol. Serv.: ${msg_configurazione_alfanumerici}]"
							text="${tipologiaservizio_codiceTipologiaServizio}" />
					
				</s:div>

				
					<s:div name="divElement3" cssclass="divRicMetadatiRight">
						<s:textbox bmodify="true"
							name="tipologiaservizio_descrizioneTipologiaServizio"
							label="Descrizione:" maxlenght="256"
							cssclasslabel="label85 bold textright" cssclass="textareaman"
							validator="ignore;accept=${configurazione_descrizione256_regex}"
							message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"
							text="${tipologiaservizio_descrizioneTipologiaServizio}" />
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
					<s:form name="form_ricerca" action="tipologiaservizio.do"
		method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<input type="hidden" name="action" value="add" />
		<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
			<s:button id="tx_button_cerca" onclick="" text="Nuovo"
				type="submit" cssclass="btnStyle"/>
		</s:div>
	--></s:form>
			
		</s:div>
	

	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Tipologie Servizio
	</s:div>
</s:div>
