<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="configsesscarrellosoccanpagamento" encodeAttributes="true"/>


	<c:if test="${configsesscarrellosoccanpagamentos!= null}">
		<s:datagrid cachedrowset="configsesscarrellosoccanpagamentos" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="configsesscarrellosoccanpagamento.do?action=search&vista=configsesscarrellosoccanpagamento" usexml="true" viewstate="true">
<%--			<s:action>
			      <c:url value="configutentetiposervizio.do">
			            <c:param name="action">search</c:param>
			            <c:param name="configutentetiposervizio_companyCode">${configutentetiposervizio.companyCode}</c:param>
			            <c:param name="configutentetiposervizio_codiceUtente">${configutentetiposervizio.codiceUtente}</c:param>
			            <c:param name="configutentetiposervizio_codiceTipologiaServizio">${configutentetiposervizio.codiceTipologiaServizio}</c:param>
			      </c:url>
			</s:action> --%> 
    	    <s:dgcolumn index="5" label="Societ&agrave;" />       
			<s:dgcolumn index="6" label="Canale Pagamento" />
			<s:dgcolumn index="3" label="Num Sessioni" />
			<s:dgcolumn label="Azioni">
				
				<s:hyperlink
					cssclass="hlStyle" 
					href="configsesscarrellosoccanpagamento.do?action=edit&configsesscarrellosoccanpagamento_companyCode={1}&configsesscarrellosoccanpagamento_chiaveCanalePagamento={2}"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />
			
				<s:hyperlink
					cssclass="hlStyle" 
					href="configsesscarrellosoccanpagamento.do?action=richiestacanc&richiestacanc=y&companyCode={1}&chiaveCanalePagamento={2}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text="" />
			</s:dgcolumn>
		</s:datagrid>
	</c:if>

