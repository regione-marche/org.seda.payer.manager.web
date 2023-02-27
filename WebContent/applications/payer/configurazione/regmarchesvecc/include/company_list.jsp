<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="companies" encodeAttributes="true"/>


	<c:if test="${companys != null}">
		<s:datagrid cachedrowset="companys" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="company.do?action=search&vista=companies" usexml="true" viewstate="true">
<%-- 			<s:action>
			      <c:url value="company.do">
			            <c:param name="action">search</c:param>
			            <c:param name="company_companyCode">${company.companyCode}</c:param>
			            <c:param name="company_companyDescription">${company.companyDescription}</c:param>
			      </c:url>
			</s:action> --%>
			<s:dgcolumn index="1" label="Societ&agrave;" />
			<s:dgcolumn index="2" label="Descrizione Societ&agrave;" />
			<s:dgcolumn label="Azioni">
				
				
			</s:dgcolumn>
		</s:datagrid>
	</c:if>

