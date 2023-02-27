<%--  **  IMPORT JAVA TAG LIB  **  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>

<%--  **  My View State  **  --%>
<m:view_state id="listaTemplate" encodeAttributes="true"/>

<c:if test="${listaTemplate != null}">
	<s:datagrid cachedrowset="listaTemplate" border="0" rowperpage="${applicationScope.rowsPerPage}" 
				action="templateReportDinamici.do?action=search&vista=listaTemplate" usexml="true" viewstate="true">
		<s:dgcolumn label="Tipo Documento">
			<s:if left="{7}" control="eq" right="D"><s:then>Documento</s:then></s:if>
			<s:if left="{7}" control="eq" right="B"><s:then>Bollettino</s:then></s:if>
			<s:if left="{7}" control="eq" right="Q"><s:then>Quietanze</s:then></s:if>
		</s:dgcolumn>
		<s:dgcolumn index="2" label="Societ&agrave;" />
		<s:dgcolumn index="9" label="Tipologia Servizio" />
		<s:dgcolumn index="4" label="Utente" />
		<s:dgcolumn index="6" label="Ente" />
		<s:dgcolumn label="Tipo Bollettino">
			<s:if left="{15}" control="eq" right="PRE"><s:then>Boll. Premarcato</s:then></s:if>
			<s:if left="{15}" control="eq" right="ICI"><s:then>Boll. ICI</s:then></s:if>
			<s:if left="{15}" control="eq" right="CDS"><s:then>Boll. CDS</s:then></s:if>
			<s:if left="{15}" control="eq" right="BOL"><s:then>Boll. Bollo</s:then></s:if>
			<s:if left="{15}" control="eq" right="MAV"><s:then>Boll. MAV</s:then></s:if>
			<s:if left="{15}" control="eq" right="FRE"><s:then>Boll. Freccia</s:then></s:if>
			<s:if left="{15}" control="eq" right="ISC"><s:then>Boll. ISCOP</s:then></s:if>
			<s:if left="{15}" control="eq" right="SPO"><s:then>Boll. Spontaneo</s:then></s:if>
		</s:dgcolumn>
		<s:dgcolumn index="10" label="Inizio validit&agrave;" format="dd/MM/yyyy" />
		<s:dgcolumn index="11" label="Fine validit&agrave;" format="dd/MM/yyyy" />
		<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">
			
		</s:dgcolumn>
	</s:datagrid>
</c:if>