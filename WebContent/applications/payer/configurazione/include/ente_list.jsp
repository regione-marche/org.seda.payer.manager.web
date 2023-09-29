<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="seda" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="ente" encodeAttributes="true"/>

	<c:if test="${entes != null}">
		<seda:datagrid cachedrowset="entes" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="ente.do?action=search&vista=ente" usexml="true" viewstate="true">
<%-- 			<seda:action>
			      <c:url value="ente.do">
			            <c:param name="action">search</c:param>
			            <c:param name="ente_companyCde">${ente.companyCode}</c:param>
			            <c:param name="ente_userCode">${ente.userCode}</c:param>
			            <c:param name="ente_chiaveEnte">${ente.chiaveEnte}</c:param>
			            <c:param name="ente_tipoEnte">${ente.tipoEnte}</c:param>
			            <c:param name="ente_numeroContoCorrente">${ente.numeroContoCorrente}</c:param>
			            <c:param name="ente_intestatarioContoCorrente">${ente.intestatarioContoCorrente}</c:param>
			      </c:url>
			</seda:action>  --%>
			<seda:dgcolumn index="8" label="Societ&agrave;" /> 
			<seda:dgcolumn index="9" label="Utente" />
    		<seda:dgcolumn index="7" label="Ente" />      
    		<seda:dgcolumn label="Tipo Ente" >
    			<seda:if left="{4}" control="eq" right="E">
    				<seda:then>Ente</seda:then>
    			</seda:if> 
    			<seda:if left="{4}" control="eq" right="C">
    				<seda:then>Consorzio</seda:then>
    			</seda:if> 
    			<seda:if left="{4}" control="ne" right="E" operator="and"
    			secondleft="{4}" secondcontrol="ne" secondright="C">
    				<seda:then>{4}</seda:then>
    			</seda:if> 
    		</seda:dgcolumn>
    		
    			<seda:dgcolumn index="11" label="Cod Ente Ipa" />   <!-- cd pago-580 --> 
    		
			<seda:dgcolumn label="Azioni">
			
			 	<seda:hyperlink
					cssclass="hlStyle" 
					href="ente.do?action=edit&ente_companyCode={1}&ente_userCode={2}&ente_chiaveEnte={3}"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />
			
			<!--<seda:imagebutton alt="Modifica" 
								  imageurl="../applications/templates/shared/img/edit.gif" 
								  onclick="window.location='ente.do?action=edit&ente_companyCode={1}&ente_userCode={2}&ente_chiaveEnte={3}&ente_descrizioneEnte={16}';" 
								  name="modifyButton"/>
			-->
			
				<seda:hyperlink
					cssclass="hlStyle" 
					href="ente.do?action=richiestacanc&richiestacanc=y&companyCode={1}&userCode={2}&chiaveEnte={3}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text="" />
			
			    <!--<seda:imagebutton alt="Cancella" 
								  imageurl="../applications/templates/shared/img/cancel.gif" 
								  onclick="window.location='ente.do?action=richiestacanc&richiestacanc=y&companyCode={1}&userCode={2}&chiaveEnte={3}';" 
								  name="cancButton" />
			--></seda:dgcolumn>
		</seda:datagrid>
	</c:if>
