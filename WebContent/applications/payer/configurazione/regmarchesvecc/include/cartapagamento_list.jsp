<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="cartapagamento" encodeAttributes="true"/>


	<c:if test="${cartaPagamentos != null}">
		<s:datagrid cachedrowset="cartaPagamentos" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="cartapagamento.do?&action=search&vista=cartapagamento" usexml="true" viewstate="true">
			<s:dgcolumn index="1" label="Codice Carta" />
			<s:dgcolumn index="2" label="Descrizione" />
			<s:dgcolumn label="Azioni">
			</s:dgcolumn>
		</s:datagrid>
	</c:if>

