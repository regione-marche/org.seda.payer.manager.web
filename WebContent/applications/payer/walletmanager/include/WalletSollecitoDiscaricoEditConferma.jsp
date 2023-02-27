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
		<c:if test="${tx_presenze_action=='applica_discarico'}">
		SEI SICURO DI VOLER PROCEDERE AL DISCARICO ?
		</c:if>
		<c:if test="${tx_presenze_action=='annulla_discarico'}">
		SEI SICURO DI VOLER PROCEDERE CON L'ANNULLAMENTO ?
		</c:if>
		</s:div>
		
		<c:if test="${tx_presenze_action=='annulla_discarico'&&SollecitoInsertEsito}">
				Annullamento effettuato correttamente.
		</c:if>	
		<c:if test="${tx_presenze_action=='applica_discarico'&&SollecitoInsertEsito}">
				Discarico effettuato correttamente.
		</c:if>
		<s:div name="divRicMetadati" cssclass="divRicMetadati marginTop">
		
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				
				<c:if test="${tx_presenze_action=='applica_discarico'&&!SollecitoInsertEsito}">	 
				  <s:button id="tx_button_discarica" disable="${confermaDisabilitata}" onclick="" text="Procedi" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${tx_presenze_action=='annulla_discarico'&&!SollecitoInsertEsito}">
				   <s:button id="tx_button_annulla" disable="${confermaDisabilitata}" onclick="" text="Procedi" type="submit" cssclass="btnStyle" />
				</c:if>
				<s:button id="tx_button_richiesta_conferma" onclick="" text="Indietro" cssclass="btnStyle" type="submit" />
			</s:div>
		</s:div>
				<input type="hidden" name="codiceSocieta" value="${tx_societa}" />
				<input type="hidden" name="codiceUtente" value="${tx_utente}" />
				<input type="hidden" name="chiaveEnte" value="${tx_ente}" />
				<input type="hidden" name="tx_presenze_action" value="calendario" />
		        <input type="hidden" name="IdwalletInfo" value="${IdwalletInfo} " />
		        <input type="hidden" name="causale" value="${causale}" />
		        <input type="hidden" name="progressivo" value="${progressivo}" />
		        <input type="hidden" name="dataPresenza" value="${dataPresenza}" />
		        
				
				
	</s:form>
</s:div>
	
</s:div>




	
	
	







