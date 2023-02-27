<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="ioitaliaconf" encodeAttributes="true" />

<script src="../applications/js/jquery-1.4.3.min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-1.8.6.custom.min.js"
	type="text/javascript"></script>
<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_selezione" action="ioitaliaconf.do?vista=ioitaliaconf"
			method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA CONFIGURAZIONI APP IO
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="ddlSocietaUtenteEnte"
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
							cssclasslabel="label160 bold textright floatleft"  cssclass="seda-ui-ddl tbddlMax780 floatleft"
							disable="${codop == 'edit'}"
							onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
							cachedrowset="listaSocietaUtenteEnte" usexml="true"
							valueselected="${ddlSocietaUtenteEnte}">
							<s:ddloption text="Selezionare uno degli elementi della lista"
								value="" />
							<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				
					
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_tipServ" disable="${codop == 'edit'}"
							cssclass="tbddl" label="Tipologia Servizio:"
							cssclasslabel="label85 bold textright"
							cachedrowset="listaTipologieServizio" usexml="true"
							onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
							valueselected="${tx_tipServ}">
							<s:ddloption text="Tutti i servizi" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
					<br /><br />
				</s:div>
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_impServ" disable="${codop == 'edit'}"
							cssclass="tbddl floatleft" label="Imposta Servizio:"
							cssclasslabel="label85 bold textright"
							cachedrowset="listaImpostaServizio" usexml="true"
							onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
							valueselected="${tx_impServ}">
							<s:ddloption text="Seleziona l'imposta" value="" />
							<s:ddloption text="{3} - {4}" value="{3}" />
						</s:dropdownlist>
					</s:div>
					<br /><br />
				</s:div>
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
						
					</s:div>
					<br /><br />
				</s:div>
					
			</s:div>
			<br />
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit"
					cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick=""
					text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_add" validate="false" onclick=""
					text="Nuovo" type="submit" cssclass="btnStyle" />
			</s:div>
		</s:form>
	</s:div>
</s:div>


<s:div name="div_messaggi" cssclass="div_align_center">
	<c:if test="${!empty tx_message}">
		<s:div name="div_messaggio_info">
			<hr />
			<s:label name="tx_message" text="${tx_message}" />
			<hr />
		</s:div>
	</c:if>
	<c:if test="${!empty tx_error_message}">
		<s:div name="div_messaggio_errore">
			<hr />
			<s:label name="tx_error_message" text="${tx_error_message}" />
			<hr />
		</s:div>
	</c:if>
</s:div>
<c:if test="${!empty lista_configurazioni}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO CONFIGURAZIONI APP IO
	</s:div>
	
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_configurazioni"
			action="ioitaliaconf.do" border="1"
			usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			<s:dgcolumn index="3" label="Società"></s:dgcolumn>
			<s:dgcolumn index="5" label="Utente"></s:dgcolumn>
			<s:dgcolumn index="7" label="Ente"></s:dgcolumn>
			<s:dgcolumn index="9" label="Tipologia Servizio"></s:dgcolumn>
			<s:dgcolumn index="11" label="Imposta Servizio"></s:dgcolumn>
			<s:dgcolumn label="Azioni">
				<s:hyperlink
					href="ioitaliaconfedit.do?vista=ioitaliaconf&codop=edit&tx_id={1}"
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Modifica" text="" cssclass="blacklink hlStyle" />

				<s:hyperlink
					href="ioitaliaconf.do?vista=ioitaliaconf&tx_button_cancel&tx_id={1}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text="" cssclass="blacklink hlStyle" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>