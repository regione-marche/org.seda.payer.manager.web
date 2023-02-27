<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="canalepagamento" encodeAttributes="true"/>


	<c:if test="${canalepagamentos != null}">
		<s:datagrid cachedrowset="canalepagamentos" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="canalepagamento.do?action=search&vista=canalepagamento" usexml="true" viewstate="true"> 
<%-- 			<s:action>
			      <c:url value="canalepagamento.do">
			            <c:param name="action">search</c:param>
			            <c:param name="canalepagamento_chiaveCanalePagamento">${canalepagamento.chiaveCanalePagamento}</c:param>
			            <c:param name="canalepagamento_descrizioneCanalePagamento">${canalepagamento.descrizioneCanalePagamento}</c:param>
			      </c:url>
			</s:action> --%>
			<s:dgcolumn index="1" label="Chiave Canale Pagamento" />
			<s:dgcolumn index="2" label="Descrizione Canale Pagamento" />
			<s:dgcolumn label="Azioni">
					<!--<s:form name="form_Cancella" action="canalepagamento.do?action=edit&canalepagamento_chiaveCanalePagamento={1}"
					method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
					<input type="hidden" name="action" value="canc" />
						<s:div name="divRicercaBottoni" cssclass="divRicBottoni">						
					<s:imagebutton onclick="" name="" imageurl="../applications/templates/configurazione/img/edit.png" alt=""/>
				</s:div>
					</s:form>
					--> 
					 <s:hyperlink
									cssclass="hlStyle" 
									href="canalepagamento.do?action=edit&canalepagamento_chiaveCanalePagamento={1}"
									imagesrc="../applications/templates/configurazione/img/edit.png"
									alt="Modifica" text="" />
									<s:hyperlink
									cssclass="hlStyle" 
									href="canalepagamento.do?action=richiestacanc&richiestacanc=y&canalepagamento_chiaveCanalePagamento={1}"
									imagesrc="../applications/templates/configurazione/img/cancel.png"
									alt="Cancella" text="" />
								
			</s:dgcolumn>
			
		</s:datagrid>
	</c:if>

