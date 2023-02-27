<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="inviosollecito" encodeAttributes="true" />

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
<script type="text/javascript">
	var annoDa = $
	{
		ddlDateAnnoDa
	};
	var annoA = $
	{
		ddlDateAnnoA
	};
	var today = new Date();

	$(function() {
		$.datepicker.setDefaults($.datepicker.regional["it"]);
		$("#tx_validita_da_hidden").datepicker("option", "dateFormat",
				"dd/mm/yyyy");
		$("#tx_validita_da_hidden")
				.datepicker(
						{
							minDate : new Date(annoDa, 0, 1),
							maxDate : new Date(annoA, 11, 31),
							yearRange : annoDa + ":" + annoA,
							showOn : "button",
							buttonImage : "../applications/templates/shared/img/calendar.gif",
							buttonImageOnly : true,
							onSelect : function(dateText, inst) {
								$("#tx_validita_da_day_id").val(
										dateText.substr(0, 2));
								$("#tx_validita_da_month_id").val(
										dateText.substr(3, 2));
								$("#tx_validita_da_year_id").val(
										dateText.substr(6, 4));
							},
							beforeShow : function(input, inst) {
								//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
								updateValoreDatePickerFromDdl(
										"tx_validita_da_day_id",
										"tx_validita_da_month_id",
										"tx_validita_da_year_id",
										"tx_validita_da_hidden");
							}
						});
		$("#tx_validita_a_hidden").datepicker("option", "dateFormat",
				"dd/mm/yyyy");
		$("#tx_validita_a_hidden")
				.datepicker(
						{
							minDate : new Date(annoDa, 0, 1),
							maxDate : new Date(annoA, 11, 31),
							yearRange : annoDa + ":" + annoA,
							showOn : "button",
							buttonImage : "../applications/templates/shared/img/calendar.gif",
							buttonImageOnly : true,
							onSelect : function(dateText, inst) {
								$("#tx_validita_a_day_id").val(
										dateText.substr(0, 2));
								$("#tx_validita_a_month_id").val(
										dateText.substr(3, 2));
								$("#tx_validita_a_year_id").val(
										dateText.substr(6, 4));
							},
							beforeShow : function(input, inst) {
								//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
								updateValoreDatePickerFromDdl(
										"tx_validita_a_day_id",
										"tx_validita_a_month_id",
										"tx_validita_a_year_id",
										"tx_validita_a_hidden");
							}
						});

	});
</script>


<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_selezione"
			action="inviosollecito.do?vista=inviosollecito" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA INVIO SOLLECITO
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="ddlSocietaUtenteEnte"
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
							cssclasslabel="label160 bold textright floatleft"
							validator="ignore;" cssclass="seda-ui-ddl tbddlMax780 floatleft"
							disable="${codop == 'edit'}"
							onchange="setFired();this.form.submit();"
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

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_codiceoneresollecito"
							label="Codice Onere per sollecito:" maxlenght="3"
							cssclasslabel="label85 bold textright" cssclass="textareaman"
							validator="ignore;accept=^[0-9a-zA-Z\-]{1,18}$"
							message="[accept=Codice Onere.: ${msg_configurazione_descrizione_3}]"
							text="${tx_codiceoneresollecito}" />
					</s:div>

					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:div name="divElement11a" cssclass="labelData">
							<s:label name="label_data_pag"
								cssclass="seda-ui-label label85 bold textright"
								text="Data Validità:" />
						</s:div>
						<s:div name="divElement11b" cssclass="floatleft">
							<s:div name="divDataDa_car" cssclass="divDataDa">
								<s:date label="Da:" prefix="tx_validita_da"
									yearbegin="${ddlDateAnnoDa}" cssclasslabel="labelsmall"
									cssclass="dateman" yearend="${ddlDateAnnoA}" locale="IT-it"
									descriptivemonth="false" separator="/"
									calendar="${tx_validita_da}"></s:date>
								<input type="hidden" id="tx_validita_da_hidden" value="" />
							</s:div>
							<s:div name="divDataA_car" cssclass="divDataA">
								<s:date label="A:" prefix="tx_validita_a"
									yearbegin="${ddlDateAnnoDa}" cssclasslabel="labelsmall"
									cssclass="dateman" yearend="${ddlDateAnnoA}" locale="IT-it"
									descriptivemonth="false" separator="/"
									calendar="${tx_validita_a}"></s:date>
								<input type="hidden" id="tx_validita_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
				</s:div>
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_descrizioneoneresollecito"
							cssclass="textareaman" cssclasslabel="label85 bold textright"
							label="Descrizione Onere:" maxlenght="100"
							text="${tx_descrizioneoneresollecito}" />
					</s:div>
				</s:div>


				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">

					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_tiposollecito" disable="false"
							cssclass="tbddlMax floatleft" label="Tipo Soll.:"
							cssclasslabel="label85 bold textright"
							valueselected="${tx_tiposollecito}">
							<s:ddloption text="Tutti" value="" />
							<s:ddloption text="SMS Cortesia" value="C" />
							<s:ddloption text="SMS Sollecito" value="S" />
							<s:ddloption text="Cartaceo" value="P" />
						</s:dropdownlist>
					</s:div>

				</s:div>



			</s:div>
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
				<c:if test="${!empty lista_flussi}">
					<s:button id="tx_button_download" onclick="" text="Download"
						cssclass="btnStyle" type="submit" />
					<s:button id="tx_button_stampa" onclick="" text="Stampa"
						cssclass="btnStyle" type="submit" />
				</c:if>
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

<c:if test="${!empty lista_solleciti}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO INVIO SOLLECITO
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_solleciti"
			action="inviosollecito.do?vista=inviosollecitoLista" border="1"
			usexml="true" rowperpage="${applicationScope.rowsPerPage}">

			<s:dgcolumn index="11" label="Data Validit&agrave;"></s:dgcolumn>
			<s:dgcolumn label="Tipologia Sollecito" css="text_align_left">
				<s:if right="{5}" control="eq" left="C">
					<s:then>
						SMS Cortesia
					</s:then>
				</s:if>
				<s:if right="{5}" control="eq" left="S">
					<s:then>
						SMS Sollecito
					</s:then>
				</s:if>
				<s:if right="{5}" control="eq" left="P">
					<s:then>
						Sollecito Cartaceo
					</s:then>
				</s:if>
			</s:dgcolumn>

			<s:dgcolumn index="6" label="Importo Residuo" css="text_align_right"
				format="#0.00"></s:dgcolumn>
			<s:dgcolumn index="7" label="Descrizione Onere"></s:dgcolumn>
			<s:dgcolumn index="8" label="Importo Onere" css="text_align_right"
				format="#0.00"></s:dgcolumn>
			<s:dgcolumn label="Azioni">
				<s:hyperlink
					href="inviosollecitoEdit.do?vista=inviosollecito&codop=edit&ddlSocietaUtenteEnte={1}|{2}|{3}&tx_dval_sms={4}&tx_tipo_sms={5}"
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Modifica" text="" cssclass="blacklink hlStyle" />

				<s:hyperlink
					href="inviosollecitoEdit.do?vista=inviosollecito&tx_button_cancel=&ddlSocietaUtenteEnte={1}|{2}|{3}&tx_dval_sms={4}&tx_tipo_sms={5}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text="" cssclass="blacklink hlStyle" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>





