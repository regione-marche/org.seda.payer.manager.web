<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="funzpagutentetiposerviziows" encodeAttributes="true"/>


	<c:if test="${funzpagutentetiposervizios != null}">
		<s:datagrid cachedrowset="funzpagutentetiposervizios" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="funzpagutentetiposervizio.do?action=search&vista=funzpagutentetiposerviziows" usexml="true" viewstate="true">

			<s:dgcolumn index="2" label="Societ&agrave;" />
			<s:dgcolumn index="4" label="Utente" />
			<s:dgcolumn index="6" label="Tipologia Servizio" />
			
			<s:dgcolumn label="Azioni">
				
			  <s:hyperlink
				cssclass="hlStyle" 
				href="funzpagutentetiposervizio.do?action=edit&funzpagutentetiposervizio_companyCode={1}&funzpagutentetiposervizio_userCode={3}&funzpagutentetiposervizio_codiceTipologiaServizio={5}"
				imagesrc="../applications/templates/configurazione/img/edit.png"
				alt="Modifica" text="" />

			   <s:hyperlink
				cssclass="hlStyle" 
				href="funzpagutentetiposervizio.do?action=richiestacanc&richiestacanc=y&funzpagutentetiposervizio_companyCode={1}&funzpagutentetiposervizio_userCode={3}&funzpagutentetiposervizio_codiceTipologiaServizio={5}"
				imagesrc="../applications/templates/configurazione/img/cancel.png"
				alt="Cancella" text="" />
			</s:dgcolumn>
		</s:datagrid>
	</c:if>

