<%--  **  IMPORT JAVA TAG LIB  **  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<%--  **  My View State  **  --%>
<m:view_state id="listaTassonomia" encodeAttributes="true"/>

<c:if test="${listaTassonomia != null}">
	<s:datagrid cachedrowset="listaTassonomia" border="1" rowperpage="${applicationScope.rowsPerPage}" 
				action="tassonomia.do?action=search&vista=listaTassonomia" usexml="true" viewstate="true">
		<s:dgcolumn index="2" label="Codice Tipo Ente" css="text_align_center" asc="CTEA" desc="CTED" />
		<s:dgcolumn index="3" label="Tipo Ente" asc="DTEA" desc="DTED" />
		<s:dgcolumn index="4" label="Progressivo Macro Area" css="text_align_center" asc="PMAA" desc="PMAD" />
		<s:dgcolumn index="5" label="Nome Macro Area" />
		<%-- <s:dgcolumn index="6" label="Descrizione Macro Area" /> --%> 
		<s:dgcolumn index="7" label="Codice Tipologia Servizio" css="text_align_center" asc="CTSA" desc="CTSD" />
		<s:dgcolumn index="8" label="Tipologia Servizio" />
		<%-- <s:dgcolumn index="9" label="Descrizione Tipologia Servizio" /> --%>
		<s:dgcolumn index="10" label="Motivo Giuridico Riscossione" css="text_align_center" asc="MGRA" desc="MGRD" />
		<s:dgcolumn index="11" label="Versione Tassonomia" css="text_align_center" asc="VTAA" desc="VTAD" />
		<s:dgcolumn index="12" label="Tassonomia" asc="SPIA" desc="SPID" />
		<s:dgcolumn index="13" label="Inizio validit&agrave;" css="text_align_center" format="dd/MM/yyyy" asc="DTIA" desc="DTID" />
		<s:dgcolumn index="14" label="Fine validit&agrave;" css="text_align_center" format="dd/MM/yyyy" asc="DTFA" desc="DTFD" />
		<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">
			<s:hyperlink cssclass="hlStyle" 
						 href="tassonomia.do?action=edit&tassonomia_chiaveTassonomia={1}"
						 imagesrc="../applications/templates/configurazione/img/edit.png"
						 alt="Modifica" text="" />
			<s:hyperlink cssclass="hlStyle" 
					     href="tassonomia.do?action=richiestacanc&richiestacanc=y&tassonomia_chiaveTassonomia={1}"
						 imagesrc="../applications/templates/configurazione/img/cancel.png"
						 alt="Cancella" text="" />
		</s:dgcolumn>
	</s:datagrid>
</c:if>