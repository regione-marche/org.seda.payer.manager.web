<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="configrtentes" encodeAttributes="true"/>


	<c:if test="${configrtentes != null}">
		<s:datagrid cachedrowset="configrtentes" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="configrtente.do?action=search&vista=configrtentes" usexml="true" viewstate="true">

			<s:dgcolumn index="2" label="Societ&agrave;" />
			<s:dgcolumn index="4" label="Utente" />
			<s:dgcolumn index="6" label="Ente" />
			<s:dgcolumn index="7" label="Codice IDPA" />
			<s:dgcolumn label="Azioni">
				
			  <s:hyperlink
				cssclass="hlStyle" 
				href="configrtente.do?action=edit&configrtente_companyCode={1}&configrtente_chiaveEnte={5}&configrtente_userCode={3}&configrtente_codiceIdpa={7}"
				imagesrc="../applications/templates/configurazione/img/edit.png"
				alt="Modifica" text="" />

			   <s:hyperlink
				cssclass="hlStyle" 
				href="configrtente.do?action=richiestacanc&richiestacanc=y&configrtente_companyCode={1}&configrtente_chiaveEnte={5}&configrtente_userCode={3}&configrtente_codiceIdpa={7}"
				imagesrc="../applications/templates/configurazione/img/cancel.png"
				alt="Cancella" text="" />
			</s:dgcolumn>
		</s:datagrid>
	</c:if>

