<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="seda" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="users" encodeAttributes="true"/>


	<c:if test="${users != null}">
		<seda:datagrid cachedrowset="users" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="user.do?action=search&vista=users" usexml="true" viewstate="true">
   	        <seda:dgcolumn index="1" label="Societ&agrave;" /> 
			<seda:dgcolumn index="7" label="Descrizione Societ&agrave;" />
			<seda:dgcolumn index="2" label="Utente" />
			<seda:dgcolumn index="4" label="Descrizione Utente" />
			<seda:dgcolumn index="3" label="Ambito" />			
			<seda:dgcolumn label="Azioni">
				
							
			</seda:dgcolumn>
		</seda:datagrid>
	</c:if>
