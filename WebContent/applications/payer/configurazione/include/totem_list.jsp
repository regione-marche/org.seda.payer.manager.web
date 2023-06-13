<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="totem" encodeAttributes="true" />

<c:if test="${totem_list != null}">
	<s:datagrid cachedrowset="totem_list" border="0" rowperpage="${applicationScope.rowsPerPage}"
		action="totem.do?action=search&vista=totem"
		usexml="true" viewstate="true">

		<s:dgcolumn index="1" label="Codice Ente" />
		<s:dgcolumn index="2" label="Imposta Servizio" />
		<s:dgcolumn index="3" label="Tipologia Imposta" />
		<s:dgcolumn label="Azioni">
			<s:hyperlink cssclass="hlStyle"
				href="totem.do?action=edit&codice_ente={1}&imposta_servizio={2}"
				imagesrc="../applications/templates/configurazione/img/edit.png"
				alt="Modifica" text="" />

			<s:hyperlink cssclass="hlStyle"
				href="totem.do?action=richiestacanc&richiestacanc=y&codice_ente={1}&imposta_servizio={2}"
				imagesrc="../applications/templates/configurazione/img/cancel.png"
				alt="Cancella" text="" />
		</s:dgcolumn>
	</s:datagrid>
</c:if>
