<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="configutentetiposervizioentes" encodeAttributes="true"/>

	<c:if test="${configutentetiposervizioentes != null}">
		<s:datagrid cachedrowset="configutentetiposervizioentes" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="configutentetiposervizioente.do?action=search&vista=configutentetiposervizioentes" usexml="true" viewstate="true">
    	    <s:dgcolumn index="16" label="Societ&agrave;" />       
			<s:dgcolumn index="17" label="Utente" />
			<s:dgcolumn index="15" label="Ente" />
			<!-- PG22XX09_YL5 INI -->
			<s:dgcolumn index="4" label="Tipol. Serv." />
			<!-- PG22XX09_YL5 FINE -->
			<s:dgcolumn index="18" label="Descr. Tipol. Serv." />  
			<s:dgcolumn label="Azioni">
				
				<s:hyperlink
					cssclass="hlStyle" 
					href="configutentetiposervizioente.do?action=edit&configutentetiposervizioente_companyCode={1}&configutentetiposervizioente_codiceUtente={2}&configutentetiposervizioente_chiaveEnte={3}&configutentetiposervizioente_codiceTipologiaServizio={4}&configutentetiposervizioente_descrizioneEnte={15}"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />
			    
				<s:hyperlink
					cssclass="hlStyle" 
					href="configutentetiposervizioente.do?action=richiestacanc&richiestacanc=y&companyCode={1}&codiceUtente={2}&chiaveEnte={3}&codiceTipologiaServizio={4}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text="" />
			</s:dgcolumn>
		</s:datagrid>
	</c:if>
