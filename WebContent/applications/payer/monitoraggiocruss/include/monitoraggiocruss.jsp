<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<m:view_state id="monitoraggioext" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<%-- TABLE PAGAMENTI EFFETTUATI --%>
	<s:div name="" cssclass="divMCRUSS">
		<s:div name="divTableTitle1" cssclass="divTableTitle bold">
			Pagamenti effettuati dal ${requestScope.dataDa} al ${requestScope.dataAl}
		</s:div>
		<s:div name="divMCRUSSName1" cssclass="tableMCRUSS">
			<s:datagrid cachedrowset="lista_pagamenti"
				action="" border="1" usexml="true"
				viewstate="true">
				<s:dgcolumn index="1" label="Ente"></s:dgcolumn>
				<s:dgcolumn index="3" label="Servizio"></s:dgcolumn>
				<s:dgcolumn index="5" label="Num. trans."></s:dgcolumn>
				<s:dgcolumn index="4" label="Importo" format="#,##0.00"></s:dgcolumn>
			</s:datagrid>
		</s:div>
	</s:div>
	
	<%-- TABLE TRANSAZIONI SOSPESE --%>
	<s:div name="" cssclass="divMCRUSS">
		<s:div name="divTableTitle2" cssclass="divTableTitle bold">
			Transazioni sospese
		</s:div>
		<s:div name="divMCRUSSName2" cssclass="tableMCRUSS">
			<s:datagrid cachedrowset="lista_transazioni"
				action="" border="1" usexml="true"
				viewstate="true">
				<s:dgcolumn index="1" label="Data trans." format="dd/MM/yyyy"></s:dgcolumn>
				<s:dgcolumn label="Id. trans." >
					<s:hyperlink
					href="../monitoraggio/monitoraggioTransazioni.do?tx_codice_transazione={5}&tx_button_cerca=1"
					text="{5}" cssclass="blacklink"
					alt="Dettaglio Transazione {5}" />
				</s:dgcolumn>
				<s:dgcolumn index="2" label="Ente"></s:dgcolumn>
				<s:dgcolumn index="4" label="Servizio"></s:dgcolumn>
				<s:dgcolumn index="6" label="Importo" format="#,##0.00"></s:dgcolumn>
			</s:datagrid>
		</s:div>
	</s:div>
	
	<%-- TABLE RENDICONTAZIONI EFFETTUATI --%>
	<s:div name="" cssclass="divMCRUSS">
		<s:div name="divTableTitle3" cssclass="divTableTitle bold">
			Rendicontazioni effettuate dal ${requestScope.dataDa} al ${requestScope.dataAl}
		</s:div>
		<s:div name="divMCRUSSName3" cssclass="tableMCRUSS">
			<s:datagrid cachedrowset="lista_rendicontazioni"
				action="" border="1" usexml="true"
				viewstate="true">
				<s:dgcolumn index="1" label="Ente"></s:dgcolumn>
				<s:dgcolumn index="3" label="Servizio"></s:dgcolumn>
				<s:dgcolumn index="5" label="Num. trans."></s:dgcolumn>
				<s:dgcolumn index="4" label="Importo" format="#,##0.00"></s:dgcolumn>
			</s:datagrid>
		</s:div>
	</s:div>
	
	<%-- TABLE NOTIFICHE FALLITE --%>
	<s:div name="" cssclass="divMCRUSS">
		<s:div name="divTableTitle4" cssclass="divTableTitle bold">
			Notifiche verso portali esterni fallite
		</s:div>
		<s:div name="divMCRUSSName4" cssclass="tableMCRUSS">
			<s:datagrid cachedrowset="lista_notifiche"
				action="" border="1" usexml="true"
				viewstate="true">
				<s:dgcolumn index="1" label="Data trans." format="dd/MM/yyyy"></s:dgcolumn>
				<s:dgcolumn label="Id. trans." >
					<s:hyperlink
					href="../monitoraggio/monitoraggioTransazioni.do?tx_codice_transazione={5}&tx_button_cerca=1"
					text="{5}" cssclass="blacklink"
					alt="Dettaglio Transazione {5}" />
				</s:dgcolumn>
				<s:dgcolumn index="2" label="Ente"></s:dgcolumn>
				<s:dgcolumn index="4" label="Servizio"></s:dgcolumn>
				<s:dgcolumn index="6" label="Importo" format="#,##0.00"></s:dgcolumn>
			</s:datagrid>
		</s:div>
	</s:div>

</s:div>