<%--  **  IMPORT JAVA TAG LIB  **  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<%--  **  My View State  **  --%>
<m:view_state id="listaGruppo" encodeAttributes="true"/>

<c:if test="${listaGruppo != null}">
	<s:datagrid cachedrowset="listaGruppo" border="1" rowperpage="${applicationScope.rowsPerPage}" 
				action="gruppo.do?action=search&vista=listaGruppo" usexml="true" viewstate="true">
		<s:dgcolumn index="2" label="Codice Gruppo" css="text_align_center" asc="CODA" desc="CODD" />
		<s:dgcolumn index="3" label="Descrizione Lingua Italia" css="text_align_center" asc="DITA" desc="DITD" />
		<s:dgcolumn index="4" label="Descrizione Altra Lingua" css="text_align_center" asc="DALA" desc="DALD" />
		<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">
			<s:hyperlink cssclass="hlStyle" 
						 href="gruppo.do?action=edit&gruppo_chiaveGruppo={1}"
						 imagesrc="../applications/templates/configurazione/img/edit.png"
						 alt="Modifica" text="" />
			<s:hyperlink cssclass="hlStyle" 
					     href="gruppo.do?action=richiestacanc&richiestacanc=y&gruppo_chiaveGruppo={1}"
						 imagesrc="../applications/templates/configurazione/img/cancel.png"
						 alt="Cancella" text="" />
		</s:dgcolumn>
	</s:datagrid>
</c:if>