<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="anagservizi" encodeAttributes="true" />

<c:if test="${anagservizis != null}">
	<s:datagrid cachedrowset="anagservizis" border="0" rowperpage="${applicationScope.rowsPerPage}"
		action="anagservizi.do?action=search&vista=anagservizi" usexml="true" viewstate="true">
		<%-- 			<s:action>
			      <c:url value="anagservizi.do">
			            <c:param name="action">search</c:param>
			            <c:param name="anagservizi_codiceAnagServizi">${anagservizi.codiceAnagServizi}</c:param>
			            <c:param name="anagservizi_descrizioneAnagServizi">${anagservizi.descrizioneAnagServizi}</c:param>
			      </c:url>
			</s:action> --%>
		<s:dgcolumn index="1" label="Codice Servizio" />
		<s:dgcolumn index="2" label="Descrizione Servizio" />
		<s:dgcolumn label="Azioni">
			
		</s:dgcolumn>
		
	</s:datagrid>
</c:if>
