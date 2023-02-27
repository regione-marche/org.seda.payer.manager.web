<%--  **  IMPORT JAVA TAG LIB  **  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>

<%-- QUI METTE IL NOME FILE INVIATO SE IN ELABORAZIONE ALTRIMENTI METTE IL NOME DEL FLUSSO DA MFARE IL DOWNLOAD DA GEOS--%>

<%--  **  My View State  **  --%>
<m:view_state id="listaOttico" encodeAttributes="true"/>

<c:if test="${!empty listaOttico}">
	<s:datagrid cachedrowset="listaOttico" border="0" rowperpage="${applicationScope.rowsPerPage}" 
				action="elaborazioni.do?vista=listaOttico&tx_button_cerca=Cerca" usexml="true" viewstate="true">
		<s:dgcolumn index="4" label="Ente" asc="ENTA" desc="ENTD" />
		<s:dgcolumn index="5" label="Data invio Flusso" format="dd/MM/yyyy" asc="GDATA" desc="GDATD" />
		
		<s:dgcolumn index="6" label="Data ricezione Avvisi" format="dd/MM/yyyy" asc="GCREA" desc="GCRED">
		</s:dgcolumn>
		
		
		<s:dgcolumn label="Tipologia Flusso" asc="CTIPA" desc="CTIPD">
			<s:if left="{7}" control="eq" right="FTE"><s:then>FTE - Fatturazione Elettr.</s:then></s:if>
			<s:if left="{7}" control="eq" right="PEC"><s:then>PEC - Mail PEC</s:then></s:if>
			<s:if left="{7}" control="eq" right="MAL"><s:then>MAL - MAIL</s:then></s:if>
			<s:if left="{7}" control="eq" right="DOI"><s:then>DOI - Doc. per Italia</s:then></s:if>
			<s:if left="{7}" control="eq" right="DOE"><s:then>DOE - Doc. per Estero</s:then></s:if>
			<s:if left="{7}" control="eq" right="FEP"><s:then>FEP - Fatturazione Elettr. - Poste</s:then></s:if>
			<s:if left="{7}" control="eq" right="PEP"><s:then>PEP - Mail PEC - Poste</s:then></s:if>
			<s:if left="{7}" control="eq" right="MAP"><s:then>MAP - MAIL - Poste</s:then></s:if>
			<s:if left="{7}" control="eq" right="DIP"><s:then>DIP - Doc. per Italia - Poste</s:then></s:if>
			<s:if left="{7}" control="eq" right="DEP"><s:then>DEP - Doc. per Estero - Poste</s:then></s:if>
		</s:dgcolumn>
		<s:dgcolumn index="8" label="Numero avvisi" asc="NRECA" desc="NRECD" />
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