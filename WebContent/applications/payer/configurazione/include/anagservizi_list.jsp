<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="anagservizi" encodeAttributes="true" />

<c:if test="${anagservizis != null}">
	<s:datagrid cachedrowset="anagservizis" border="0" rowperpage="${applicationScope.rowsPerPage}"
		action="anagservizi.do?action=search&vista=anagservizi" usexml="true" viewstate="true">
		<%-- 			<s:action>
			      <c:url value="anagservizi.do">
			            <c:param name="action">search</c:param>
			            <c:param name="anagservizi_codiceAnagServizi">${anagservizi.codiceAnagServizi}</c:param>
			            <c:param name="anagservizi_descrizioneAnagServizi">${anagservizi.descrizioneAnagServizi}</c:param>
			      </c:url>
			</s:action> --%>
		<s:dgcolumn index="1" label="Codice Servizio" />
		<s:dgcolumn index="2" label="Descrizione Servizio" />
		<s:dgcolumn label="Azioni">
			<!--<s:form name="form_Cancella" action="anagservizi.do?action=edit&anagservizi_codiceAnagServizi={1}"
					method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
					<input type="hidden" name="action" value="canc" />
						<s:div name="divRicercaBottoni" cssclass="divRicBottoni">						
					<s:imagebutton onclick="" name="" imageurl="../applications/templates/configurazione/img/edit.png" alt=""/>
				</s:div>
					</s:form>
					
			--><s:hyperlink cssclass="hlStyle"
				href="anagservizi.do?action=edit&anagservizi_codiceAnagServizi={1}"
				imagesrc="../applications/templates/configurazione/img/edit.png"
				alt="Modifica" text="" />
				
				<s:hyperlink cssclass="hlStyle"
				href="anagservizi.do?action=richiestacanc&richiestacanc=y&anagservizi_codiceAnagServizi={1}"
				imagesrc="../applications/templates/configurazione/img/cancel.png"
				alt="Cancella" text="" />
		</s:dgcolumn>
		
	</s:datagrid>
</c:if>
