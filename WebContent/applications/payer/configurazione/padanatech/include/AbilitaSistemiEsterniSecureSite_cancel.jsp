<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="AbilitaSistemiEsterniSecureSite_cancel" encodeAttributes="true" />


<s:div name="div_delete" cssclass="div_align_center divSelezione">
	<s:div name="divDeleteTopName" cssclass="divRicercaTop">
	<s:form name="delete_form" action="AbilitaSistemiEsterniSecureSiteCancel.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle">
		ABILITAZIONE SISTEMA ESTERNO
		</s:div>
		<br/>
		<br/>
		<s:div name="divRicMetadati" cssclass="divRicMetadati">
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				
				<s:button id="tx_button_indietro" onclick="" text="Indietro" cssclass="btnStyle" type="submit" />
				<s:button id="tx_button_delete" disable="${confermaDisabilitata}" onclick="" text="Cancella" type="submit" cssclass="btnStyle" />
			</s:div>
		</s:div>
			<input type="hidden" name="tx_url" value="${tx_url}" />
			<input type="hidden" name="tx_idServizio" value="${tx_idServizio}" />			
	</s:form>

</s:div>
	
</s:div>
