<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="anagservizi" encodeAttributes="true" />


<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	
		<s:div name="divRicercaTopName" cssclass="divRicercaTop">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA ANAGRAFICA SERVIZI
		</s:div>
		
		<s:form name="form_selezione" action="anagservizi.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:div name="divRicMetadati" cssclass="divRicMetadatiTop">
		
		
			<s:div name="divElement1" cssclass="divRicMetadatiLeft">

					<s:textbox bmodify="true" name="anagservizi_codiceAnagServizi"
						label="Cod. Servizio:"
						cssclasslabel="label85 bold textright"
						maxlenght="2"
						cssclass="textareaman"
						text="${anagservizi_codiceAnagServizi}" />
				
			</s:div>


			<s:div name="divElement2" cssclass="divRicMetadatiCenter">
				<s:textbox bmodify="true"
					name="anagservizi_descrizioneAnagServizi" label="Descrizione:"
					maxlenght="256" 
					cssclasslabel="label85 bold textright"
					cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
					message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"
					text="${anagservizi_descrizioneAnagServizi}" />
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
					<s:button id="tx_button_nuovo" type="submit" text="Nuovo" onclick=""
											cssclass="btnStyle" />
				</s:div>
				
			<!--<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" cssclass="btnStyle"
					type="submit" />
				<s:button id="tx_button_reset" onclick="" text="Reset" cssclass="btnStyle"
					type="reset" />
			</s:div>
			--></s:form>
		

	
	<!--<s:form name="form_ricerca" action="anagservizi.do"
		method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<input type="hidden" name="action" value="add" />
		<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
			<s:button id="tx_button_cerca" onclick="" text="Inserisci Nuovo" cssclass="btnStyle"
				type="submit" />
		</s:div>
	</s:form>
--></s:div>

	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO ANAGRAFICA SERVIZI
	</s:div>
</s:div>
	