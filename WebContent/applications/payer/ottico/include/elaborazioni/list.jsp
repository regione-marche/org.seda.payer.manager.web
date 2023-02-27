<%--  **  IMPORT JAVA TAG LIB  **  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>

<%--  **  My View State  **  --%>
<m:view_state id="listaOttico" encodeAttributes="true"/>

<c:if test="${!empty listaOttico}">
	<s:datagrid cachedrowset="listaOttico" border="0" rowperpage="${applicationScope.rowsPerPage}" 
				action="elaborazioni.do?vista=listaOttico&tx_button_cerca=Cerca" usexml="true" viewstate="true">
		<s:dgcolumn index="4" label="Ente" asc="ENTA" desc="ENTD" />
		<s:dgcolumn index="5" label="Data Elaborazione" format="dd/MM/yyyy" asc="GDATA" desc="GDATD" />
		<s:dgcolumn index="6" label="Data Creazione" format="dd/MM/yyyy" asc="GCREA" desc="GCRED" />
		<s:dgcolumn label="Tipologia Flusso" asc="CTIPA" desc="CTIPD">
			<s:if left="{7}" control="eq" right="DOC"><s:then>DOC - Documenti</s:then></s:if>
			<s:if left="{7}" control="eq" right="BOL"><s:then>BOL - Bollettini</s:then></s:if>
			<s:if left="{7}" control="eq" right="REL"><s:then>REL - Relate</s:then></s:if>
		</s:dgcolumn>
		<s:dgcolumn index="8" label="Numero record" asc="NRECA" desc="NRECD" />
		<s:dgcolumn index="9" label="Caricati" asc="NROKA" desc="NROKD" />
		<s:dgcolumn index="10" label="Non caricati" asc="NRKOA" desc="NRKOD" />
		<s:dgcolumn label="Nome file" asc="NRKOA" desc="NRKOD">
			<s:if left="{6}" control="eq" right="1000-01-01 00:00:00.0"><s:then>{14}</s:then></s:if>
			<s:if left="{6}" control="ne" right="1000-01-01 00:00:00.0"><s:then>{11}</s:then></s:if>
		</s:dgcolumn>
		

		<s:dgcolumn label="Azioni">
			<s:if left="{6}" control="eq" right="1000-01-01 00:00:00.0"><s:then>in elaborazione</s:then></s:if>
			<s:if left="{6}" control="ne" right="1000-01-01 00:00:00.0">
				<s:then>
					<s:hyperlink cssclass="hlStyle" 
							 href="download.do?path={15}/&name={11}"
							 imagesrc="../applications/templates/ottico/img/download.png"
							 alt="Download flusso" text="" />
				</s:then>
			</s:if>
			<s:if left="{16}" control="ne" right="">
				<s:then>
	 				<s:hyperlink cssclass="hlStyle" 
							 href="download.do?path={15}/&name={16}"
							 imagesrc="../applications/templates/ottico/img/zip.png"
							 alt="Download zip flussi" text="" />
				</s:then>
			</s:if>
		</s:dgcolumn>
	</s:datagrid>
</c:if>