<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="gestioneAvvisi" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>


<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>



<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_selezione"
			action="gestioneAvvisi.do?vista=agestioneAvvisi" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA AVVISI BORSELLINO
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">

						<s:dropdownlist name="ddlSocietaUtenteEnte"
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
							cssclasslabel="label160 bold textright floatleft"
							validator="ignore;" cssclass="seda-ui-ddl tbddlMax780 floatleft"
							disable="${codop == 'edit'}"
							onchange="setFiredButton();this.form.submit();"
							cachedrowset="listaSocietaUtenteEnte" usexml="true"
							valueselected="${ddlSocietaUtenteEnte}">
							<s:ddloption text="Selezionare uno degli elementi della lista"
								value="" />
							<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}" />
						</s:dropdownlist>

						<c:if test="${codop == 'add'}">
							<noscript>
								<s:button id="tx_button_societa_changed" onclick="" text=""
									validate="false" type="submit" cssclass="btnimgStyle"
									title="Aggiorna" />
							</noscript>
						</c:if>
					</s:div>
				</s:div>
			</s:div>

			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit"
					cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick=""
					text="Reset" type="submit" cssclass="btnStyle" />
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

<c:if test="${!empty lista_gestione_avvisi}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			LISTA AVVISI
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_gestione_avvisi"
			action="gestioneAvvisi.do?vista=gestioneAvvisi" border="1"
			usexml="true" rowperpage="${applicationScope.rowsPerPage}">

			<s:dgcolumn index="9" label="Societ&agrave;" css="text_align_left" />
			<s:dgcolumn index="8" label="Utente" css="text_align_left" />
			<s:dgcolumn index="7" label="Ente" css="text_align_left" />
			<s:dgcolumn index="5" label="Descrizione Avviso"
				css="text_align_left" />
			<s:dgcolumn label="Azioni">
				<s:hyperlink
					href="gestioneAvvisiEdit.do?vista=gestioneAvvisi&codop=edit&ddlSocietaUtenteEnte={2}|{1}|{3}&descAvviso={5}&nomeCampo={4}&cuteCute={1}&codSoc={2}&ente={3}"
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Modifica" text="" cssclass="blacklink hlStyle" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>





