<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="entiinviort" encodeAttributes="true" />

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



<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_selezione" action="nuovoenteinviort.do?vista=entiinviort"
			method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">CONFIGURAZIONE ENTI - INVIO RT
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<c:if test="${codop == 'edit'}">
							<s:textbox bmodify="true" name="ddlSocietaUtenteEnte" 
								cssclasslabel="labellarge bold textright" showrequired="true"
								validator="required;" bdisable="true" cssclass="textareamandouble"
								label="Societ&agrave;/Utente/Ente:" text="${ddlSocietaUtenteEnte}"/>
							<input type="hidden" name="ddlSocietaUtenteEnte" value="${ddlSocietaUtenteEnte}" />
							<input type="hidden" name="tx_societa" value="${tx_societa}" />
							<input type="hidden" name="tx_utente" value="${tx_utente}" />
							<input type="hidden" name="tx_ente" value="${tx_ente}" />
							<input type="hidden" name="desc_soc" value="${desc_soc}" />
							<input type="hidden" name="desc_ute" value="${desc_ute}" />
							<input type="hidden" name="desc_ente" value="${desc_ente}" />
							<input type="hidden" name="codop" value="edit" />
						</c:if>
						<c:if test="${codop != 'edit'}">
							<s:dropdownlist name="ddlSocietaUtenteEnte"
								label="Societ&agrave;/Utente/Ente:" showrequired="true"
								cssclasslabel="labellarge bold textright floatleft"
								validator="required;" cssclass="seda-ui-ddl tbddlMax500 floatleft"
								disable="false" 
								onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
								cachedrowset="listaSocietaUtenteEnte" usexml="true"
								valueselected="${ddlSocietaUtenteEnte}">
								<s:ddloption text="Selezionare uno degli elementi della lista"
									value="" />
								<s:ddloption value="{1}|{2}|{3}|{4}" text="{5} / {6} / {4}" />
							</s:dropdownlist>
						</c:if>
					</s:div>

					
				</s:div>
				<br />
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiFill">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					<c:if test="${codop != 'edit'}">
						<s:textbox bmodify="true" name="tx_codiceIPA"
							bdisable="false"
							label="Codice IPA Ente:" validator="required;" showrequired="true"
							cssclasslabel="labellarge bold textright"
							cssclass="textareamandouble"
							text="${tx_codiceIPA}" />
					</c:if>
					<c:if test="${codop == 'edit'}">
						<s:textbox bmodify="false" name="tx_codiceIPA_s"
							bdisable="true"
							label="Codice IPA Ente:" validator="required;" showrequired="true"
							cssclasslabel="labellarge bold textright"
							cssclass="textareamandouble"
							text="${tx_codiceIPA}" />
						<input type="hidden" name="tx_codiceIPA" value="${tx_codiceIPA}" />
					</c:if>
					</s:div>
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_utentelogin"
							label="Utente:" validator="required;" showrequired="true"
							cssclasslabel="labellarge bold textright"
							cssclass="textareamandouble"
							text="${tx_utentelogin}" />
					
					</s:div>
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_password"
						label="Password:" validator="required;" showrequired="true"
						cssclasslabel="labellarge bold textright"
						cssclass="textareamandouble"
						text="${tx_password}" />
					</s:div>
					<br />
					<br />
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_URL"
						label="URL Conservazione:" validator="required;" showrequired="true"
						cssclasslabel="labellarge bold textright"
						cssclass="textareamandouble"
						text="${tx_URL}" />
					</s:div>
					<br />
					<br />
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_mail" validator="required;" showrequired="true"
						label="Mail statistiche elaborazioni:"
						cssclasslabel="labellarge bold textright"
						cssclass="textareamandouble"
						text="${tx_mail}" />
					</s:div>
					<br />
					<br />
					</s:div>
				
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_ente_abilitato"
							label="Ente Abilitato: " showrequired="true"
							cssclasslabel="labellarge bold textright"
							validator="required;" cssclass="seda-ui-ddl tbddl floatleft"
							disable="false"
							valueselected="${tx_ente_abilitato}">
							<s:ddloption value="0" text="NO" />
							<s:ddloption value="1" text="SI" />
						</s:dropdownlist>
					</s:div>
				</s:div>
			<br />
			<br />
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_indietro" onclick="" text="Indietro" type="submit"
					cssclass="btnStyle" validate="false" />
				<s:button id="tx_button_reset" validate="false" onclick=""
					text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_add" onclick="" text="Salva" type="submit"
					cssclass="btnStyle" />
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

