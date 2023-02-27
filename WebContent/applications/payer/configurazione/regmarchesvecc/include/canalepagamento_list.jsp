<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="canalepagamento" encodeAttributes="true"/>


	<c:if test="${canalepagamentos != null}">
		<s:datagrid cachedrowset="canalepagamentos" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="canalepagamento.do?action=search&vista=canalepagamento" usexml="true" viewstate="true"> 
<%-- 			<s:action>
			      <c:url value="canalepagamento.do">
			            <c:param name="action">search</c:param>
			            <c:param name="canalepagamento_chiaveCanalePagamento">${canalepagamento.chiaveCanalePagamento}</c:param>
			            <c:param name="canalepagamento_descrizioneCanalePagamento">${canalepagamento.descrizioneCanalePagamento}</c:param>
			      </c:url>
			</s:action> --%>
			<s:dgcolumn index="1" label="Chiave Canale Pagamento" />
			<s:dgcolumn index="2" label="Descrizione Canale Pagamento" />
			<s:dgcolumn label="Azioni">
					
			</s:dgcolumn>
			
		</s:datagrid>
	</c:if>

