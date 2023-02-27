<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="configutentetiposervizioentes" encodeAttributes="true"/>

	<c:if test="${configutentetiposervizioentes != null}">
		<s:datagrid cachedrowset="configutentetiposervizioentes" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="configutentetiposervizioente.do?action=search&vista=configutentetiposervizioentes" usexml="true" viewstate="true">
    	    <s:dgcolumn index="16" label="Societ&agrave;" />       
			<s:dgcolumn index="17" label="Utente" />
			<s:dgcolumn index="15" label="Ente" />
			<!-- PG22XX09_YL5 INI -->
			<s:dgcolumn index="4" label="Tipol. Serv." />
			<!-- PG22XX09_YL5 FINE -->
			<s:dgcolumn index="18" label="Descr. Tipol. Serv." /> 
			<s:dgcolumn label="Azioni">
				
			</s:dgcolumn>
		</s:datagrid>
	</c:if>
