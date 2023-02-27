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
								
			</seda:dgcolumn>			
			<seda:dgcolumn label=" ">			    
					
								  
			</seda:dgcolumn>
		</seda:datagrid>
	</c:if>
</p>
