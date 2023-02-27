<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="costinotifica" encodeAttributes="true"/>


	<c:if test="${costinotifica != null}">
		<s:datagrid cachedrowset="costinotifica" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="costinotifica.do?action=search&vista=costinotifica" usexml="true" viewstate="true">
<%-- 			<s:action>
			      <c:url value="costinotifica.do">
			            <c:param name="action">search</c:param>
			            <c:param name="costiNotifica_costiNotificaCompanyCode">${costiNotifica_costiNotificaCompanyCode}</c:param>
			            <c:param name="costiNotifica_costiNotificaCompanyCode">${costiNotifica_costiNotificaUserCode}</c:param>
			      </c:url>
			</s:action> --%>
			<s:dgcolumn index="8" label="Societ&agrave;" />
			<s:dgcolumn index="7" label="Utente" />
			<s:dgcolumn index="3" label="Costo Posta Ordinaria"  format="#,##0.00" css="textright"/>
			<s:dgcolumn index="4" label="Costo SMS"  format="#,##0.00" css="textright"/>
			<s:dgcolumn label="Azioni">
				 
			</s:dgcolumn>
		</s:datagrid>
	</c:if>