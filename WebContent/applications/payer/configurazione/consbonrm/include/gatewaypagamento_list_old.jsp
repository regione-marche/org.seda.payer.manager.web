<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="seda" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>

<m:view_state id="Gatewaypagamentos_list" encodeAttributes="true" />
<c:out value="gatewaypagamento_userDesc">${gatewaypagamento_userDesc}</c:out>

<p>
	<c:if test="${gatewaypagamentos != null}">
		<seda:datagrid cachedrowset="gatewaypagamentos" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="gatewaypagamentosearch.do?action=search" usexml="true" viewstate="true">
					   
 
    	    <seda:dgcolumn index="37" label="Societ&agrave;" />
			<seda:dgcolumn index="34" label="Utente" />
			<seda:dgcolumn index="35" label="Canale di Pagamento" />
			<seda:dgcolumn index="36" label="Carta di Pagamento" />
			<seda:dgcolumn index="5" label="Gateway" />			
			<seda:dgcolumn label=" ">
								  <seda:hyperlink
									cssclass="hlStyle" 
									href="gatewaypagamento_edit.do?action=edit&gatewaypagamento_chiaveGateway={1}&gatewaypagamento_codiceCartaPagamento={24}&gatewaypagamento_chiaveCanalePagamento={4}"
									imagesrc="../applications/templates/configurazione/img/edit.png"
									alt="Modifica" text="" />
			</seda:dgcolumn>			
			<seda:dgcolumn label=" ">			    
					<seda:hyperlink
									cssclass="hlStyle" 
									href="gatewaypagamento_cancel.do?action=richiestacanc&richiestacanc=y&chiaveGateway={1}"
									imagesrc="../applications/templates/configurazione/img/cancel.png"
									alt="Cancella" text="" />
								  
			</seda:dgcolumn>
		</seda:datagrid>
	</c:if>
</p>
