<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="listaOttico" encodeAttributes="true"/>
<c:if test="${listaOttico != null}">
	<s:datagrid cachedrowset="listaOttico" border="0" rowperpage="${applicationScope.rowsPerPage}" 
				action="parametriOttico.do?action=search&vista=listaOttico" usexml="true" viewstate="true">
		<s:dgcolumn index="2" label="Societ&agrave;" />
		<s:dgcolumn index="4" label="Utente" />
		<s:dgcolumn index="6" label="Ente" />
		<s:dgcolumn label="Sorgente Immagini">
			<s:if left="{7}" control="eq" right="P"><s:then>Accesso a PayER</s:then></s:if>
			<s:if left="{7}" control="eq" right="A"><s:then>Accesso Esterno</s:then></s:if>
		</s:dgcolumn>
		<s:dgcolumn label="Documento">
			<s:if left="{8}" control="eq" right="N"><s:then>No</s:then></s:if>
			<s:if left="{8}" control="eq" right="I"><s:then>Immagine</s:then></s:if>
			<s:if left="{8}" control="eq" right="T"><s:then>Template</s:then></s:if>
		</s:dgcolumn>
		<s:dgcolumn label="Relata">
			<s:if left="{9}" control="eq" right="N"><s:then>No</s:then></s:if>
			<s:if left="{9}" control="eq" right="I"><s:then>Immagine</s:then></s:if>
		</s:dgcolumn>
		<s:dgcolumn label="Bollettino">
			<s:if left="{10}" control="eq" right="N"><s:then>No</s:then></s:if>
			<s:if left="{10}" control="eq" right="Y"><s:then>Si</s:then></s:if>
		</s:dgcolumn>
		<s:dgcolumn label="Quietanza">
			<s:if left="{11}" control="eq" right="N"><s:then>No</s:then></s:if>
			<s:if left="{11}" control="eq" right="Y"><s:then>Si</s:then></s:if>
		</s:dgcolumn>
		<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">
			<s:hyperlink cssclass="hlStyle" 
						 href="parametriOttico.do?action=edit&parametriottico_companyCode={1}&parametriottico_chiaveEnte={5}&parametriottico_userCode={3}"
						 imagesrc="../applications/templates/configurazione/img/edit.png"
						 alt="Modifica" text="" />
			<s:hyperlink cssclass="hlStyle" 
					     href="parametriOttico.do?action=richiestacanc&richiestacanc=y&parametriottico_companyCode={1}&parametriottico_chiaveEnte={5}&parametriottico_userCode={3}"
						 imagesrc="../applications/templates/configurazione/img/cancel.png"
						 alt="Cancella" text="" />
		</s:dgcolumn>
	</s:datagrid>
</c:if>