<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>

<script type="text/javascript" >

$(document).ready( function() {
	    // se javascript attivo abilito il pulsante stampa
	    $("button#tx_button_stampa").attr("class", "seda-ui-button btnStyle");
});

</script>

<s:div name="divMessage">
	<c:if test="${!empty tx_message}">
		<hr/><s:label name="tx_message" text="${tx_message}"/><hr/>
	</c:if>
	<c:if test="${!empty tx_error_message}">
		<hr/><s:label name="tx_error_message" text="${tx_error_message}"/><hr/>
	</c:if>
	<s:form name="frmIndietro" action="ritorna.do?vista=monitoraggiotransazioni" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_back" type="submit" text="Indietro" onclick="" cssclass="btnStyle" />
				<s:button id="tx_button_stampa" onclick="print();" text="Stampa" type="button" cssclass="divDisplayNone" />
			</s:div>
	</s:form>
</s:div>






