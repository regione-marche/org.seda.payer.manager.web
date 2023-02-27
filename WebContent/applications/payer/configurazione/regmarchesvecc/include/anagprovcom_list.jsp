<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>

<m:view_state id="anagprovcom" encodeAttributes="true"/>


	<c:if test="${anagprovcoms != null}">
		<s:datagrid cachedrowset="anagprovcoms" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="anagprovcom.do?action=search&vista=anagprovcom" usexml="true" viewstate="true">
<%--			<s:action>
			      <c:url value="anagprovcom.do">
			            <c:param name="action">search</c:param>
			            <c:param name="anagprovcom_codiceBelfiore">${anagprovcom.codiceBelfiore}</c:param>
			            <c:param name="anagprovcom_codiceProvincia">${anagprovcom.codiceProvincia}</c:param>
			            <c:param name="anagprovcom_codiceComune">${anagprovcom.codiceComune}</c:param>
			      </c:url>
			</s:action> --%>
    		<s:dgcolumn index="1" label="Codice Belfiore" />
			<s:dgcolumn index="2" label="Provincia" />
			<s:dgcolumn index="3" label="Comune" />
			<s:dgcolumn index="4" label="Descr. Comune" />
			<s:dgcolumn label="Azioni">
				
			</s:dgcolumn>
		</s:datagrid>
	</c:if>

