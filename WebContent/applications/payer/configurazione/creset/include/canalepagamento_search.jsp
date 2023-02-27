<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<script type="text/javascript">
function indietro(){
	//alert("OK");
	var a =document.getElementById("frmAction");
	a.reset();
	a.submit();
}
</script>
<br />

<m:view_state id="canalepagamento" encodeAttributes="true"/>
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	
		<s:div name="divRicercaTopName" cssclass="divRicercaTop">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Canali Pagamento</s:div>
			
			<s:form name="form_selezione" action="canalepagamento.do"
		method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<input type="hidden" name="action" value="search" />

				<s:div name="divRicercaMetadatiName" cssclass="divRicMetadatiTop">
				
					<s:div name="divElement1" cssclass="divRicMetadatiLeft">
						<!--<s:textbox bmodify="true"
							name="canalepagamento_chiaveCanalePagamento" label="Can. Pag.:"
							maxlenght="3" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman"
							text="${canalepagamento_chiaveCanalePagamento}" />
							-->
							<s:textbox bmodify="true"
							name="canalepagamento_chiaveCanalePagamento" label="Canale:"
							maxlenght="3" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman"
							text="${canalepagamento_chiaveCanalePagamento}" />
					</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:textbox bmodify="true"
							name="canalepagamento_descrizioneCanalePagamento"
							label="Descrizione:" maxlenght="256"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
							message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"
							text="${canalepagamento_descrizioneCanalePagamento}" />

					</s:div>
					<s:div name="divElement3" cssclass="divRicMetadatiRight">
						

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
					<%-- 
					<s:hyperlink name="Cerca"
										imagesrc="../applications/templates/configurazione/img/search_icon.gif"
												href="/manager/configurazione/canalepagamento.do?action=search&canalepagamento_chiaveCanalePagamento=${requestScope.canalepagamento_chiaveCanalePagamento}" text="" cssclass="hlStyle" />
					<s:hyperlink name="Reset"
										imagesrc="../applications/templates/configurazione/img/reset_icon.gif"
												href="" text="" cssclass="hlStyle" />
					--%>
					<s:button id="tx_button_cerca" onclick="" text="Cerca" cssclass="btnStyle"
						type="submit" />
					<s:button id="tx_button_reset" onclick="" text="Reset" cssclass="btnStyle"
						type="reset" /> 
				</s:div>
			
	--></s:form>

	<!--<s:form name="form_ricerca" action="canalepagamento.do"
		method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<input type="hidden" name="action" value="add" />
		<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
			

			<s:button id="tx_button_cerca" onclick="" text="Nuovo" cssclass="btnStyle"
				type="submit" />
		</s:div>
	</s:form>
	--></s:div>

	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Canali Pagamento
	</s:div>
</s:div>
