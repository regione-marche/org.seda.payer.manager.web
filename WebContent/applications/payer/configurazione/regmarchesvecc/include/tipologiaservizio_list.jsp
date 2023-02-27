<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="tipologiaservizio" encodeAttributes="true"/>


	<c:if test="${tipologiaservizios != null}">
		<s:datagrid cachedrowset="tipologiaservizios" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="tipologiaservizio.do?action=search&vista=tipologiaservizio" usexml="true" viewstate="true"> 


 		    <s:dgcolumn index="6" label="Societ&agrave;" />
			<s:dgcolumn index="2" label="Tipologia Servizio" />
			<s:dgcolumn index="3" label="Descrizione Tipologia Servizio" />
			
			<s:dgcolumn label="Azioni">
				
			</s:dgcolumn>
		</s:datagrid>
	</c:if>
