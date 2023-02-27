<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="funzpagutentetiposervizioentes" encodeAttributes="true"/>


	<c:if test="${funzpagutentetiposervizioentes != null}">
		<s:datagrid cachedrowset="funzpagutentetiposervizioentes" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="funzpagutentetiposervizioente.do?action=search&vista=funzpagutentetiposervizioentes" usexml="true" viewstate="true">

			<s:dgcolumn index="2" label="Societ&agrave;" />
			<s:dgcolumn index="4" label="Utente" />
			<s:dgcolumn index="8" label="Ente" />	
			<s:dgcolumn index="6" label="Tipologia Servizio" />
			
			<s:dgcolumn label="Azioni">
				
			  <s:hyperlink
				cssclass="hlStyle" 
				href="funzpagutentetiposervizioente.do?action=edit&funzpagutentetiposervizioente_companyCode={1}&funzpagutentetiposervizioente_userCode={3}&funzpagutentetiposervizioente_chiaveEnte={7}&funzpagutentetiposervizioente_codiceTipologiaServizio={5}"
				imagesrc="../applications/templates/configurazione/img/edit.png"
				alt="Modifica" text="" />

			   <s:hyperlink
				cssclass="hlStyle" 
				href="funzpagutentetiposervizioente.do?action=richiestacanc&richiestacanc=y&funzpagutentetiposervizioente_companyCode={1}&funzpagutentetiposervizioente_userCode={3}&funzpagutentetiposervizioente_chiaveEnte={7}&funzpagutentetiposervizioente_codiceTipologiaServizio={5}"
				imagesrc="../applications/templates/configurazione/img/cancel.png"
				alt="Cancella" text="" />
			</s:dgcolumn>
		</s:datagrid>
	</c:if>

