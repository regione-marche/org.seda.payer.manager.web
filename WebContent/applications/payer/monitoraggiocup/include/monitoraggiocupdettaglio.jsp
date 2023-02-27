<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="monitoraggiocupdettaglio" encodeAttributes="true" />

<s:div name="divOuter" cssclass="div_align_center">
<!-- form per i messaggi di errore -->
	<s:form name="frmMonitoraggioExtDetails" action="none" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:div name="divMargin" cssclass="divMargin">
		</s:div>
	</s:form>
</s:div>

<c:if test="${!empty listaMIC}">
	<s:div name="divTableTitle" cssclass="divTableTitle bold">
		Notifica Selezionata
	</s:div>
	<s:div name="divRiepilogo" cssclass="divRisultati">
		<s:datagrid cachedrowset="listaMIC" action="" border="1" usexml="true" viewstate="">
			
			<s:dgcolumn index="17" label="Ente" />
			<s:dgcolumn label="Id Transazione" >
				<s:if left="{18}" control="eq" right="Y">
					<s:then>
						<s:hyperlink
						href="../monitoraggio/monitoraggioTransazioni.do?tx_codice_transazione={1}&tx_button_cerca=1"
						text="{1}" cssclass="blacklink"
						alt="Dettaglio Transazione {1}" />
					</s:then>
					<s:else>{1}</s:else>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn index="8" label="Numero Operazione" />
			<s:dgcolumn index="15" label="Numero Documento" />
			<s:dgcolumn index="16" label="Data/Ora Aggiornamento" format="dd/MM/yyyy HH:mm:ss"/>
			<s:dgcolumn label="PaymentRequest">
				<s:hyperlink
					href="downloadXml.do?idtrans={1}&source=MIC&type=paymentrequest&form=frmMonitoraggioExtDetails"
					cssclass="blacklink hlStyle"
					text="{19}"
					alt="Download PaymentRequest" />
			</s:dgcolumn>
			<s:dgcolumn label="PaymentData">
				<s:hyperlink
					href="downloadXml.do?idtrans={1}&source=MIC&type=paymentdata&form=frmMonitoraggioExtDetails"
					cssclass="blacklink hlStyle"
					text="{20}"
					alt="Download PaymentData" />
			</s:dgcolumn>
			<s:dgcolumn index="11" label="Esito Notifica" />
						
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;">
				<s:hyperlink
					href="notifica.do?idtrans={1}&codiceFiscale={2}&codicePagamento={3}&numop={8}&idportale={7}&action=dettaglio&form=frmMonitoraggioExtDetails&vista=monitoraggiocupdettaglio"
					cssclass="hlStyle" 
					imagesrc="../applications/templates/monitoraggiocup/img/notifica.png" text=""
					alt="Rinotifica" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>

<c:if test="${!empty listaMCS}">
	<s:div name="divTableTitle1" cssclass="divTableTitle  bold">
		Elenco Dettagli Notifica
	</s:div>
	
	<s:div name="divRisultati" cssclass="divRisultati">
		<s:datagrid cachedrowset="listaMCS" rowperpage="${applicationScope.rowsPerPage}"
			action="dettaglioNotifica.do?vista=monitoraggiocupdettaglio&chiave_transazione_hidden=${chiave_transazione_hidden}" border="1" usexml="true" viewstate="">
			
			<s:dgcolumn index="18" label="Data/Ora" format="dd/MM/yyyy HH:mm:ss"/>
			<s:dgcolumn index="4" label="Gruppo Tentativi" />
			<s:dgcolumn index="5" label="Progressivo Tentativo" />
			<s:dgcolumn label="Modalita" >
				<s:if right="{6}" control="eq" left="O">
					<s:then>On-Line</s:then>
				</s:if>
				<s:if right="{6}" control="eq" left="B">
					<s:then>Batch</s:then>
				</s:if>
				<s:if right="{6}" control="eq" left="M">
					<s:then>Manager</s:then>
				</s:if>
				<s:if right="{6}" control="eq" left="A">
					<s:then>Batch</s:then>
				</s:if>
				<s:if right="{6}" control="eq" left="R">
					<s:then>Batch</s:then>
				</s:if>
				<s:if right="{6}" control="eq" left="E">
					<s:then>Batch</s:then>
				</s:if>
				<s:if right="{6}" control="eq" left="T">
					<s:then>On-Line</s:then>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn label="PaymentData">
				<s:hyperlink
					href="downloadXml.do?idtrans={1}&codiceFiscale={2}&codicePagamento={3}&gruppo={4}&progr={5}&source=MCS&type=paymentdata&form=frmMonitoraggioExtDetails"
					cssclass="blacklink hlStyle"
					text="{11}"
					alt="Download PaymentData" />
			</s:dgcolumn>
			<s:dgcolumn label="CommitMsg">
				<s:hyperlink
					href="downloadXml.do?idtrans={1}&codiceFiscale={2}&codicePagamento={3}&gruppo={4}&progr={5}&source=MCS&type=commitmsg&form=frmMonitoraggioExtDetails"
					cssclass="blacklink hlStyle"
					text="{12}"
					alt="Download CommitMsg" />
			</s:dgcolumn>
			<s:dgcolumn index="13" label="Esito Notifica" />
			<s:dgcolumn index="19" label="Operatore" />
		</s:datagrid>
	</s:div>
</c:if>


<s:form name="frmIndietro"
	action="ritorna.do?vista=monitoraggiocup"
	method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
	<s:div name="divIndietro" cssclass="divButton1">
		<s:button id="tx_button_transazioni" type="submit" text="Indietro"
			onclick="" cssclass="btnStyle" />
	</s:div>
</s:form>

