<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="seda" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="ente" encodeAttributes="true"/>

	<c:if test="${entes != null}">
		<seda:datagrid cachedrowset="entes" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="ente.do?action=search&vista=ente" usexml="true" viewstate="true">
<%-- 			<seda:action>
			      <c:url value="ente.do">
			            <c:param name="action">search</c:param>
			            <c:param name="ente_companyCde">${ente.companyCode}</c:param>
			            <c:param name="ente_userCode">${ente.userCode}</c:param>
			            <c:param name="ente_chiaveEnte">${ente.chiaveEnte}</c:param>
			            <c:param name="ente_tipoEnte">${ente.tipoEnte}</c:param>
			            <c:param name="ente_numeroContoCorrente">${ente.numeroContoCorrente}</c:param>
			            <c:param name="ente_intestatarioContoCorrente">${ente.intestatarioContoCorrente}</c:param>
			      </c:url>
			</seda:action>  --%>
			<seda:dgcolumn index="8" label="Societ&agrave;" /> 
			<seda:dgcolumn index="9" label="Utente" />
    		<seda:dgcolumn index="7" label="Ente" />      
    		<seda:dgcolumn label="Tipo Ente" >
    			<seda:if left="{4}" control="eq" right="E">
    				<seda:then>Ente</seda:then>
    			</seda:if> 
    			<seda:if left="{4}" control="eq" right="C">
    				<seda:then>Consorzio</seda:then>
    			</seda:if> 
    			<seda:if left="{4}" control="neq" right="E" operator="and"
    			secondleft="{4}" secondcontrol="neq" secondright="C">
    				<seda:then>{4}</seda:then>
    			</seda:if> 
    		</seda:dgcolumn>
			<seda:dgcolumn label="Azioni">
			
			 </seda:dgcolumn>
		</seda:datagrid>
	</c:if>
