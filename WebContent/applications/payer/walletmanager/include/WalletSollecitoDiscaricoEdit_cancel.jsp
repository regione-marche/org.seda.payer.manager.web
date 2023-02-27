<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="raccorodpagonet_cancel" encodeAttributes="true" />


<s:div name="div_delete" cssclass="div_align_center divSelezione">
	<s:div name="divDeleteTopName" cssclass="divRicercaTop">
	<s:form name="delete_form" action="walletsollecitodiscaricoedit.do?vista=${vista}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
		<c:if test="${Show_Action_Discarico}">
		SEI SICURO DI VOLER PROCEDERE AL DISCARICO ?
		</c:if>
		<c:if test="${Show_Action_Annullamento}">
		SEI SICURO DI VOLER PROCEDERE CON L'ANNULLAMENTO ?
		</c:if>
		</s:div>
		
		<c:if test="${SollecitoInsertEsito&&Show_Action_Discarico}">
				Inserimento effettuato correttamente.
		</c:if>	
		<c:if test="${SollecitoInsertEsito&&Show_Action_Annullamento}">
				Annullamento effettuato correttamente.
		</c:if>
		<s:div name="divRicMetadati" cssclass="divRicMetadati marginTop">
		
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				
				<c:if test="${Show_Action_Discarico&&!SollecitoInsertEsito}">	 
				  <s:button id="tx_button_discarica" disable="${confermaDisabilitata}" onclick="" text="Procedi" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${Show_Action_Annullamento&&!SollecitoInsertEsito}">
				   <s:button id="tx_button_annulla" disable="${confermaDisabilitata}" onclick="" text="Procedi" type="submit" cssclass="btnStyle" />
				</c:if>
				<s:button id="tx_button_indietro" onclick="" text="Indietro" cssclass="btnStyle" type="submit" />
			</s:div>
		</s:div>
				<input type="hidden" name="tx_societa" value="${tx_societa}" />
				<input type="hidden" name="tx_utente" value="${tx_utente}" />
				<input type="hidden" name="tx_ente" value="${tx_ente}" />
				<input type="hidden" name="tx_tipologia_servizio_h" value="${tx_tipologia_servizio_h}" />
				
				<input type="hidden" name="IdwalletInfo" value="${IdwalletInfo}" />
				<input type="hidden" name="tx_flagDiscaricoSollecito" value="${tx_flagDiscaricoSollecito}" />
				<input type="hidden" name="tx_flagAnnullamentoSollecito" value="${tx_flagAnnullamentoSollecito}" />
				<input type="hidden" name="prog_sollecito" value="${prog_sollecito}" />
				<input type="hidden" name="data_sollecito" value="${data_sollecito}" />
				
				<input type="hidden" name="Show_Action_Discarico" value="${Show_Action_Discarico}" />
				<input type="hidden" name="Show_Action_Annullamento" value="${Show_Action_Annullamento}" />
				<input type="hidden" name="Sollecito_Annullamento_inseriti" value="${SollecitoInsertEsito}" />		 
				
				
	</s:form>
</s:div>
	
</s:div>
