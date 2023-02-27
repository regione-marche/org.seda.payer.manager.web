<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="listaRecDB" encodeAttributes="true"/>
<c:if test="${listaRecDB != null}">
	<s:datagrid cachedrowset="listaRecDB" border="0" rowperpage="${applicationScope.rowsPerPage}" 
				action="enteImpostaServizioNotifica.do?action=search&vista=listaRecDB" usexml="true" viewstate="true">
		<s:dgcolumn index="7" label="Utente" />
		<s:dgcolumn label="Ente">{9} - {8}</s:dgcolumn>
		<s:dgcolumn index="3" label="Codice Imposta Servizio" />
		<s:dgcolumn label="Flag Allegati su Notifica">
			<s:if left="{4}" control="eq" right="Y"><s:then>Abilitato</s:then></s:if>
			<s:if left="{4}" control="eq" right="N"><s:then>NON Abilitato</s:then></s:if>
		</s:dgcolumn>
		<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">
			<s:hyperlink cssclass="hlStyle" 
						 href="enteImpostaServizioNotifica.do?action=edit&enteimpostaservizionotifica_codiceImpostaServizio={3}&enteimpostaservizionotifica_chiaveEnte={2}&enteimpostaservizionotifica_userCode={1}"
						 imagesrc="../applications/templates/configurazione/img/edit.png"
						 alt="Modifica" text="" />
			<s:hyperlink cssclass="hlStyle" 
						 href="enteImpostaServizioNotifica.do?action=richiestacanc&richiestacanc=y&enteimpostaservizionotifica_codiceImpostaServizio={3}&enteimpostaservizionotifica_chiaveEnte={2}&enteimpostaservizionotifica_userCode={1}"
						 imagesrc="../applications/templates/configurazione/img/cancel.png"
						 alt="Cancella" text="" />
		</s:dgcolumn>
	</s:datagrid>
</c:if>