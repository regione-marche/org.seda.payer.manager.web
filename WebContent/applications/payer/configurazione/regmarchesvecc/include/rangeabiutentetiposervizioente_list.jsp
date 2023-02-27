<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="rangeabiutentetiposervizioentes" encodeAttributes="true"/>


	<c:if test="${rangeabiutentetiposervizioentes != null}">
		<s:datagrid cachedrowset="rangeabiutentetiposervizioentes" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="rangeabiutentetiposervizioente.do?action=search&vista=rangeabiutentetiposervizioentes" usexml="true" viewstate="true">
<%-- 	    <s:dgcolumn index="1" label="Chiave Range" />    --%>
    	    <s:dgcolumn index="12" label="Societ&agrave;" />       
			<s:dgcolumn index="13" label="Utente" />
			<s:dgcolumn index="11" label="Ente" />
			<s:dgcolumn index="14" label="Tipologia Servizio" /> 
			<s:dgcolumn index="6" label="Inizio Range Da" />
			<s:dgcolumn index="7" label="Fine Range A" />
			<s:dgcolumn index="8" label="Inizio Range Per" /> 
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;">
			</s:dgcolumn>
		</s:datagrid>
	</c:if>

