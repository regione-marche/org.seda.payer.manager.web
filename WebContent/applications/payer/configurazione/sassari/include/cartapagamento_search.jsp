<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="cartapagamento" encodeAttributes="true"/>
<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Metodi Pagamento</s:div>
	
		<s:form name="form_ricerca" action="cartapagamento.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />
			
			<s:div name="divRicercaTitleName1" cssclass="divRicMetadati">
				<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiLeft">
						<s:textbox bmodify="true" name="carta_codiceCarta"
							label="Cod. Carta:" maxlenght="2"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman"
							text="${carta_codiceCarta}" />

					</s:div>
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:textbox bmodify="true" name="carta_descrizioneCarta"
							label="Descrizione:" maxlenght="128"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione_regex128}"
							message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"
							text="${carta_descrizioneCarta}" />

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
			</s:div>
		</s:form>
	</s:div>
	
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Metodi Pagamento
	</s:div>
</s:div>
