<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="configutentetiposervizios" encodeAttributes="true"/>


	<c:if test="${configutentetiposervizios != null}">
		<s:datagrid cachedrowset="configutentetiposervizios" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="configutentetiposervizio.do?action=search&vista=configutentetiposervizios" usexml="true" viewstate="true">
    	    <s:dgcolumn index="14" label="Societ&agrave;" />       
			<s:dgcolumn index="15" label="Utente" />
			<!-- PG22XX09_YL5 INI -->
			<s:dgcolumn index="3" label="Tipol. Serv." /> 
			<s:dgcolumn index="16" label="Descr. Tipol. Serv." /> 			
			<!-- PG22XX09_YL5 FINE -->
			<s:dgcolumn label="Azioni">
				
			</s:dgcolumn>
		</s:datagrid>
	</c:if>

