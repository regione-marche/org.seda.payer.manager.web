<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="tipologiaservizio" encodeAttributes="true"/>


	<c:if test="${tipologiaservizios != null}">
		<s:datagrid cachedrowset="tipologiaservizios" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="tipologiaservizio.do?action=search&vista=tipologiaservizio" usexml="true" viewstate="true"> 


 		    <s:dgcolumn index="6" label="Societ&agrave;" />
			<s:dgcolumn index="2" label="Tipologia Servizio" />
			<s:dgcolumn index="3" label="Descrizione Tipologia Servizio" />
			
			<s:dgcolumn label="Azioni">
				 <s:hyperlink
					cssclass="hlStyle" 
					href="tipologiaservizio.do?action=edit&tipologiaservizio_codiceTipologiaServizio={2}&tipologiaservizio_companyCode={1}"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />
			
				<s:hyperlink
					cssclass="hlStyle" 
					href="tipologiaservizio.do?action=richiestacanc&richiestacanc=y&companyCode={1}&codiceTipologiaServizio={2}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text="" />
									
			</s:dgcolumn>
		</s:datagrid>
	</c:if>
