<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="viewstate" encodeAttributes="true" />

<div align="center">
	<s:form name="back_to_ricercaFlussi" action="ritorna.do?vista=FlussiSearch" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<input name="tx_button_ricercaFlussi" type="submit" value="Indietro" class="seda-ui-button btnStyle"/> 
	</s:form>
</div>
