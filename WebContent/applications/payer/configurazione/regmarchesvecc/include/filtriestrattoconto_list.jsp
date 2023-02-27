<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="filtriestrattoconto" encodeAttributes="true"/>


<c:if test="${listFiltriEstrattoConto != null}">
	<s:datagrid cachedrowset="listFiltriEstrattoConto" border="0" rowperpage="${applicationScope.rowsPerPage}" 
				   action="filtriec.do?action=search&vista=filtriestrattoconto" usexml="true" viewstate="true">

		<s:dgcolumn index="2" label="Codice Fiscale" />
		<s:dgcolumn index="3" label="Ente" />
		<s:dgcolumn index="4" label="Imposta Servizio" />
		<s:dgcolumn index="5" label="Numero Emissione" />
		<s:dgcolumn index="6" label="Numero Documento" />
		
		<s:dgcolumn label="Azioni">
		</s:dgcolumn>
	</s:datagrid>
</c:if>

