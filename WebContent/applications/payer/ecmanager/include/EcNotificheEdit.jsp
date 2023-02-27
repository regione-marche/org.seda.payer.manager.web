<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="ecnotificheedit" encodeAttributes="true" />

<fmt:setLocale value="it_IT" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="frmIndietro" action="ecnotificheedit.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Dettaglio Notifica Contribuente Selezionata</s:div>
			Testo Messaggio:
			<br />
			<s:div name="divECContainerGrayName" cssclass="divECContainerGray"><br /><br />${notificaDetail}<br /><br /></s:div>
		
		<!-- bottoni -->
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_indietro" type="submit" text="Indietro" onclick="" validate="false"  cssclass="btnStyle" />
			</s:div>
		</s:form> 
	</s:div> 
</s:div>




