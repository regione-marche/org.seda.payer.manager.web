<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>

<m:view_state id="anagente" encodeAttributes="true"/>
	<c:if test="${anagentes != null}">
		<s:datagrid cachedrowset="anagentes" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="anagente.do?action=search&vista=anagente" usexml="true" viewstate="true">
<%--			<s:action>
			      <c:url value="anagente.do">
			            <c:param name="action">search</c:param>
			            <c:param name="anagente_chiaveEnte">${anagente.chiaveEnte}</c:param>
			            <c:param name="anagente_codiceBelfiore">${anagente.codiceBelfiore}</c:param>
			            <c:param name="anagente_codiceEnte">${anagente.codiceEnte}</c:param>
			            <c:param name="anagente_descrizioneEnte">${anagente.descrizioneEnte}</c:param>
			      </c:url>
			</s:action> --%>

			<s:dgcolumn index="6" label="Ente" />
			<!-- ini PG22XX09_YL5 -->
<%-- 		<s:dgcolumn index="3" label="Ente" /> --%>
			<s:dgcolumn index="1" label="Chiave Ente" />
			<!-- fine PG22XX09_YL5 -->
			<s:dgcolumn index="8" label="Codice Belfiore" />
			<s:dgcolumn label="Tipo Ente" >
				<s:if left="{5}" control="eq" right="VA"><s:then>AMMINISTRAZIONE PUBBLICA</s:then></s:if>
				<s:if left="{5}" control="eq" right="AP"><s:then>AUTORIT&Agrave; PORTUALE</s:then></s:if>
				<s:if left="{5}" control="eq" right="AT"><s:then>AZIENDA DI TRASPORTO</s:then></s:if>
				<s:if left="{5}" control="eq" right="AS"><s:then>AZIENDA SANITARIA</s:then></s:if>
				<s:if left="{5}" control="eq" right="AC"><s:then>AZIENDA SERVIZI COMUNALI</s:then></s:if>
				<s:if left="{5}" control="eq" right="M"><s:then>CAMERA DI COMMERCIO</s:then></s:if>
				<s:if left="{5}" control="eq" right="F"><s:then>CAPITANERIA DI PORTO</s:then></s:if>
				<s:if left="{5}" control="eq" right="CC"><s:then>CARABINIERI</s:then></s:if>
				<s:if left="{5}" control="eq" right="Z"><s:then>CASSA DI PREVIDENZA</s:then></s:if>
				<s:if left="{5}" control="eq" right="G"><s:then>COLLEGIO PROFESSIONALE</s:then></s:if>
				<s:if left="{5}" control="eq" right="C"><s:then>COMUNE</s:then></s:if>
				<s:if left="{5}" control="eq" right="N"><s:then>CONSORZIO</s:then></s:if>
				<s:if left="{5}" control="eq" right="D"><s:then>DIREZIONE PROVINCIALE DEL LAVORO</s:then></s:if>
				<s:if left="{5}" control="eq" right="A"><s:then>ENTE LOCALE</s:then></s:if>
				<s:if left="{5}" control="eq" right="VN"><s:then>ENTE NAZIONALE</s:then></s:if>
				<s:if left="{5}" control="eq" right="EP"><s:then>ENTE PREVIDENZIALE</s:then></s:if>
				<s:if left="{5}" control="eq" right="PU"><s:then>ENTE PUBBLICO</s:then></s:if>
				<s:if left="{5}" control="eq" right="V"><s:then>ENTI VARI</s:then></s:if>
				<s:if left="{5}" control="eq" right="H"><s:then>FOGLIO ANNUNZI LEGALI</s:then></s:if>
				<s:if left="{5}" control="eq" right="GF"><s:then>GUARDIA DI FINANZA</s:then></s:if>
				<s:if left="{5}" control="eq" right="IL"><s:then>INAIL</s:then></s:if>
				<s:if left="{5}" control="eq" right="IS"><s:then>INPS</s:then></s:if>
				<s:if left="{5}" control="eq" right="IN"><s:then>ISTITUTO DI PREVIDENZA</s:then></s:if>
				<s:if left="{5}" control="eq" right="MG"><s:then>MINISTERO DELLA GIUSTIZIA</s:then></s:if>
				<s:if left="{5}" control="eq" right="E"><s:then>MINISTERO DELLE FINANZE</s:then></s:if>
				<s:if left="{5}" control="eq" right="O"><s:then>ORDINE PROFESSIONALE</s:then></s:if>
				<s:if left="{5}" control="eq" right="PS"><s:then>POLIZIA DELLA STRADA</s:then></s:if>
				<s:if left="{5}" control="eq" right="T"><s:then>PREFETTURA</s:then></s:if>
				<s:if left="{5}" control="eq" right="P"><s:then>PROVINCIA</s:then></s:if>
				<s:if left="{5}" control="eq" right="AU"><s:then>PROVINCIA AUTONOMA</s:then></s:if>
				<s:if left="{5}" control="eq" right="R"><s:then>REGIONE</s:then></s:if>
				<s:if left="{5}" control="eq" right="S"><s:then>SEZIONE TERRITORIALE POLSTRADA</s:then></s:if>
				<s:if left="{5}" control="eq" right="K"><s:then>SIAE</s:then></s:if>
				<s:if left="{5}" control="eq" right="SG"><s:then>SOCIET&Agrave; GESTIONE FONDO SOLIDARIET&Agrave;</s:then></s:if>
				<s:if left="{5}" control="eq" right="US"><s:then>UFFICI STATALI</s:then></s:if>
				<s:if left="{5}" control="eq" right="UC"><s:then>UNIONE DEI COMUNI</s:then></s:if>
				<s:if left="{5}" control="eq" right="U"><s:then>UPICA</s:then></s:if>
				<s:if left="{5}" control="eq" right="YY"><s:then>ALTRO</s:then></s:if>
			</s:dgcolumn>
			<s:dgcolumn label="Azioni">
			<s:hyperlink
				cssclass="hlStyle" 
				href="anagente.do?action=edit&anagente_chiaveEnte={1}"
				imagesrc="../applications/templates/configurazione/img/edit.png"
				alt="Modifica" text="" />

			   <s:hyperlink
				cssclass="hlStyle" 
				href="anagente.do?action=richiestacanc&richiestacanc=y&chiaveEnte={1}"
				imagesrc="../applications/templates/configurazione/img/cancel.png"
				alt="Cancella" text="" />
			
			
			
			<%-- 
				<s:imagebutton alt="Modifica" 
								  imageurl="../applications/templates/shared/img/edit.gif" 
								  onclick="window.location='anagente.do?action=edit&anagente_chiaveEnte={1}';" 
								  name="modifyButton"/>
			
			
			    <s:imagebutton alt="Cancella" 
								  imageurl="../applications/templates/shared/img/cancel.gif" 
								  onclick="window.location='anagente.do?action=richiestacanc&richiestacanc=y&chiaveEnte={1}';" 
								  name="cancButton" />--%>
			</s:dgcolumn>
		</s:datagrid>
	</c:if>

