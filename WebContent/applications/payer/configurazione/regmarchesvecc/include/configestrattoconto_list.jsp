<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="configestrattocontos" encodeAttributes="true" />


<c:if test="${configestrattocontos != null}">
	<s:datagrid cachedrowset="configestrattocontos" border="0"
		rowperpage="${applicationScope.rowsPerPage}"
		action="estrattoconto.do?action=search&vista=configestrattocontos"
		usexml="true" viewstate="true">

		<s:dgcolumn index="2" label="Societ&agrave;" />
		<s:dgcolumn index="4" label="Utente" />
		<s:dgcolumn index="6" label="Ente" />
		<s:dgcolumn label="Azioni">

		</s:dgcolumn>
	</s:datagrid>
</c:if>
