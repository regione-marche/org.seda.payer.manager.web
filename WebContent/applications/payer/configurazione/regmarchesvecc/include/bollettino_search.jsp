<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>

<m:view_state id="bollettino" encodeAttributes="true"/>
 
<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Tipi Bollettino</s:div>

		<s:form name="form_ricerca" action="bollettino.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />

			<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
				
					<s:div name="divElement1" cssclass="divRicMetadatiLeft">
						<s:textbox bmodify="true" name="bollettino_bollettinoType"
							label="Tipo:" maxlenght="4"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman"
							text="${bollettino_bollettinoType}" />
					</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:textbox bmodify="true"
							name="bollettino_bollettinoDescription"
							label="Descrizione:" maxlenght="256"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
							message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"
							text="${bollettino_bollettinoDescription}" />
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
			
		--></s:form>
		
		<!--<s:form name="form_ricerca" action="bollettino.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="add" />
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Nuovo"
					type="submit" cssclass="btnStyle"/>
			</s:div>
		</s:form>
	--></s:div>

	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Tipi Bollettino
	</s:div>
</s:div>
