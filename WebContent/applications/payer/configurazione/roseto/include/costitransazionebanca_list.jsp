<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="costitransazionebanca" encodeAttributes="true"/>


	<c:if test="${costitransazionebanca != null}">
		<s:datagrid cachedrowset="costitransazionebanca" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="costitransazionebanca.do?action=search&vista=costitransazionebanca" usexml="true" viewstate="true">
<%-- 			<s:action>
			      <c:url value="costitransazionebanca.do">
			            <c:param name="action">search</c:param>
			            <c:param name="costiTransazioneBanca_ChiaveFasciaCosto">${costiTransazioneBanca_ChiaveFasciaCosto}</c:param>
			            <c:param name="costiTransazioneBanca_ChiaveGatewayPagamento">${costiTransazioneBanca_ChiaveGatewayPagamento}</c:param>
			      </c:url>
			</s:action> --%>
			<s:dgcolumn index="10" label="Societ&agrave;" />
			<s:dgcolumn index="11" label="Utente" />
			<s:dgcolumn index="12" label="Gateway" />
			<s:dgcolumn index="13" label="Fascia costo" />
						
			<s:dgcolumn label="Azioni">
				  <s:hyperlink
					cssclass="hlStyle" 
					href="costitransazionebanca.do?action=edit&costiTransazioneBanca_ChiaveFasciaCosto={1}"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />

				   <s:hyperlink
					cssclass="hlStyle" 
					href="costitransazionebanca.do?action=richiestacanc&richiestacanc=y&costiTransazioneBanca_ChiaveFasciaCosto={1}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text="" />
			</s:dgcolumn>
		</s:datagrid>
	</c:if>