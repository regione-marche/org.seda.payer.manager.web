<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>

<m:view_state id="configsesscarrellosoccanpagamentos" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Sessioni Carrello
		</s:div>
		<s:form name="form_ricerca" action="configsesscarrellosoccanpagamento.do"
			method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:div name="divRicMetadati" cssclass="divRicMetadati">
		<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
		

			<c:choose>
				<c:when test="${userAppl_codiceSocieta == ''}">
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
					<s:textbox bmodify="true" name="configsesscarrellosoccanpagamento_companyCode"
						label="Societ&agrave;:" 
						cssclasslabel="label65 bold textright"
						cssclass="textareaman"
					    text="${configsesscarrellosoccanpagamento_companyCode}"  />
					</s:div>
				</c:when>
			</c:choose>

			<c:choose>
				<c:when test="${userAppl_chiaveCanalePagamento == ''}">
					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
					<s:textbox bmodify="true" name="configsesscarrellosoccanpagamento_chiaveCanalePagamento"
						label="Canale:" 
						cssclasslabel="label65 bold textright"
						cssclass="textareaman"
						text="${configsesscarrellosoccanpagamento_chiaveCanalePagamento}"  />
						</s:div>
				</c:when>
			</c:choose>
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
		
		</s:form>
	</s:div>


	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Sessioni Carrello
	</s:div>
</s:div>
