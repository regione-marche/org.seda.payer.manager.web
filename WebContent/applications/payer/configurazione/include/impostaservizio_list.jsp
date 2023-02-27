<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="impostaservizio" encodeAttributes="true"/>


	<c:if test="${impostaservizios != null}">
		<s:datagrid cachedrowset="impostaservizios" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="impostaservizio.do?action=search&vista=impostaservizio" usexml="true" viewstate="true"> 
<%-- 			<s:action>
			      <c:url value="impostaservizio.do">
			            <c:param name="action">search</c:param>
			            <c:param name="impostaservizio_codiceImpostaServizio">${impostaservizio.codiceImpostaServizio}</c:param>
			            <c:param name="impostaservizio_descrizioneImpostaServizio">${impostaservizio.descrizioneImpostaServizio}</c:param>
			            <c:param name="impostaservizio_codiceTipologiaServizio">${impostaservizio.codiceTipologiaServizio}</c:param>
			            <c:param name="impostaservizio_companyCode">${impostaservizio.companyCode}</c:param>
			      </c:url>
			</s:action>   --%>
			<s:dgcolumn index="7" label="Societ&agrave;" />
			<s:dgcolumn index="8" label="Tipologia Servizio" />
			<s:dgcolumn index="3" label="Cod. Tipologia Servizio Sistema Esterno" />
            <s:dgcolumn index="4" label="Descrizione Imposta Servizio" />
            			
			<s:dgcolumn label="Azioni">
				  <s:hyperlink
					cssclass="hlStyle" 
					href="impostaservizio.do?action=edit&impostaservizio_codiceImpostaServizio={3}&impostaservizio_codiceTipologiaServizio={2}&impostaservizio_companyCode={1}"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />

				  <s:hyperlink
					cssclass="hlStyle" 
					href="impostaservizio.do?action=richiestacanc&richiestacanc=y&companyCode={1}&codiceTipologiaServizio={2}&codiceImpostaServizio={3}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text="" />
			</s:dgcolumn>
		</s:datagrid>
	</c:if>

