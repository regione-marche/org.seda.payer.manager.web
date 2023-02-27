<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="configutentetiposervizioentes" encodeAttributes="true"/>

	<c:if test="${configutentetiposervizioentes != null}">
		<s:datagrid cachedrowset="configutentetiposervizioentes" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="serviziattivi.do?action=search&vista=configutentetiposervizioentes" usexml="true" viewstate="true">
			<s:dgcolumn index="15" label="Ente" />
			<s:dgcolumn index="20" label="Denominazione Servizio" /> 
			<s:dgcolumn index="18" label="Tipologia Servizio" />
			<s:dgcolumn index="21" label="Tassonomia" /> 
			<s:dgcolumn index="19" label="Tipo Bollettino" /> 
			<s:dgcolumn index="23" label="Stampa Avviso PagoPA" /> 
			<s:dgcolumn label="Azioni">
				<!--				
				<s:hyperlink
					cssclass="hlStyle" 
					href="serviziattivi.do?action=edit&configutentetiposervizioente_companyCode={1}&configutentetiposervizioente_codiceUtente={2}&configutentetiposervizioente_chiaveEnte={3}&configutentetiposervizioente_codiceTipologiaServizio={4}&configutentetiposervizioente_descrizioneEnte={15}&tx_societa={1}&tx_provincia=${tx_provincia}&tx_UtenteEnte={1}{2}{3}"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="ModificaBad" text="" />
  				-->
				<s:hyperlink
					cssclass="hlStyle" 
					href="serviziattivi.do?action=edit&tx_button_edit=1&configutentetiposervizioente_companyCode={1}&configutentetiposervizioente_codiceUtente={2}&configutentetiposervizioente_chiaveEnte={3}&configutentetiposervizioente_codiceTipologiaServizio={4}&configutentetiposervizioente_descrizioneEnte={15}&tx_societa={1}&tx_provincia=${tx_provincia}&tx_UtenteEnte={1}{2}{3}"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />
			</s:dgcolumn>
		</s:datagrid>
	</c:if>
