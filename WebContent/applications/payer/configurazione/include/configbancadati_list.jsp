<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="searchconfigbancadati" encodeAttributes="true"/>


	<c:if test="${searchconfigbancadati != null}">
		<s:datagrid cachedrowset="searchconfigbancadati" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="bancadati.do?action=search&vista=searchconfigbancadati" usexml="true" viewstate="true">

			<s:dgcolumn index="2" label="Societ&agrave;" />
			<s:dgcolumn index="3" label="Utente" />
			<s:dgcolumn index="4" label="Nome banca dati" />
			<s:dgcolumn label="Azioni">
				
			  <s:hyperlink
				cssclass="hlStyle" 
				href="bancadati.do?action=edit&configbancadati_cuteCute={3}&configbancadati_idBancaDati={1}"
				imagesrc="../applications/templates/configurazione/img/edit.png"
				alt="Modifica" text="" />

			   <s:hyperlink
				cssclass="hlStyle" 
				href="bancadati.do?action=richiestacanc&richiestacanc=y&configbancadati_cuteCute={3}&configbancadati_idBancaDati={1}"
				imagesrc="../applications/templates/configurazione/img/cancel.png"
				alt="Cancella" text="" />
			</s:dgcolumn>
		</s:datagrid>
	</c:if>