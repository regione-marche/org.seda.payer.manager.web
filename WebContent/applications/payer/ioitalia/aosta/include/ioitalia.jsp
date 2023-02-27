<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="ioitalia" encodeAttributes="true" />

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
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();

	$(function() {
		$.datepicker.setDefaults($.datepicker.regional["it"]);
		$("#tx_data_da_hidden")
				.datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_da_hidden")
				.datepicker(
						{
							minDate : new Date(annoDa, 0, 1),
							maxDate : new Date(annoA, 11, 31),
							yearRange : annoDa + ":" + annoA,
							showOn : "button",
							buttonImage : "../applications/templates/shared/img/calendar.gif",
							buttonImageOnly : true,
							onSelect : function(dateText, inst) {
								$("#tx_data_da_day_id").val(
										dateText.substr(0, 2));
								$("#tx_data_da_month_id").val(
										dateText.substr(3, 2));
								$("#tx_data_da_year_id").val(
										dateText.substr(6, 4));
							},
							beforeShow : function(input, inst) {
								//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
								updateValoreDatePickerFromDdl(
										"tx_data_da_day_id",
										"tx_data_da_month_id",
										"tx_data_da_year_id",
										"tx_data_da_hidden");
							}
						});
		$("#tx_data_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_a_hidden")
				.datepicker(
						{
							minDate : new Date(annoDa, 0, 1),
							maxDate : new Date(annoA, 11, 31),
							yearRange : annoDa + ":" + annoA,
							showOn : "button",
							buttonImage : "../applications/templates/shared/img/calendar.gif",
							buttonImageOnly : true,
							onSelect : function(dateText, inst) {
								$("#tx_data_a_day_id").val(
										dateText.substr(0, 2));
								$("#tx_data_a_month_id").val(
										dateText.substr(3, 2));
								$("#tx_data_a_year_id").val(
										dateText.substr(6, 4));
							},
							beforeShow : function(input, inst) {
								//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
								updateValoreDatePickerFromDdl(
										"tx_data_a_day_id",
										"tx_data_a_month_id",
										"tx_data_a_year_id", "tx_data_a_hidden");
							}
						});
	});
</script>
<input type="hidden" value="1.0.0" />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_selezione" action="ioitalia.do?vista=ioitalia"
			method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA FORNITURE APP IO
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:dropdownlist name="tx_societa" disable="${ddlSocietaDisabled}"
							cssclass="tbddl floatleft" label="Societ&agrave;:"
							cssclasslabel="label85 bold floatleft textright"
							cachedrowset="listaSocieta" usexml="true"
							onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
							valueselected="${tx_societa}">
							<s:ddloption text="Tutte le Societ&agrave;" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>

					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
						<s:dropdownlist name="tx_provincia"
							disable="${ddlProvinciaDisabled}" cssclass="tbddl floatleft"
							label="Provincia:"
							cssclasslabel="label65 bold floatleft textright"
							cachedrowset="listaProvince" usexml="true"
							onchange="setFiredButton('tx_button_provincia_changed');this.form.submit();"
							valueselected="${tx_provincia}">
							<s:ddloption text="Tutte le Province" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>

					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:dropdownlist name="tx_UtenteEnte"
							disable="${ddlUtenteEnteDisabled}" cssclass="tbddlMax floatleft"
							label="Ente:" cssclasslabel="label65 bold textright"
							cachedrowset="listaUtentiEnti" usexml="true"
							onchange="setFiredButton('tx_button_ente_changed');this.form.submit();"
							valueselected="${tx_UtenteEnte}">
							<s:ddloption text="Tutti gli Enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				<br />
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_tipServ" disable="false"
							cssclass="tbddl" label="Tipologia Servizio:"
							cssclasslabel="label85 bold textright"
							cachedrowset="listaTipologieServizio" usexml="true"
							onchange="setFiredButton('btnChangeTipologiaSelected');this.form.submit();"
							valueselected="${tx_tipServ}">
							<s:ddloption text="Tutti i servizi" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
					<br />
					<br />
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_esito" disable="false"
							cssclass="tbddl" label="Esito:"
							cssclasslabel="label85 bold textright"
							onchange=""
							valueselected="${tx_esito}">
							<s:ddloption text="" value=""  />
							<s:ddloption text="OK" value="OK" />
							<s:ddloption text="KO" value="KO" />
						</s:dropdownlist>
					</s:div>
					
				</s:div>
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_impServ" disable="false"
							cssclass="tbddl floatleft" label="Imposta Servizio:"
							cssclasslabel="label85 bold textright"
							cachedrowset="listaImpostaServizio" usexml="true"
							onchange=""
							valueselected="${tx_impServ}">
							<s:ddloption text="Seleziona l'imposta" value="" />
							<s:ddloption text="{3} - {4}" value="{3}" />
						</s:dropdownlist>
					</s:div>
					<br />
					<br />
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_fornitura_s"
							label="Fornitura:"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman"
							text="${tx_fornitura_s}" />
					</s:div>
					
				</s:div>
					<s:div name="divRicercaRight" cssclass="divRicMetadatiLeft">
						<s:div name="divElement8" cssclass="divRicMetadatiDoubleRow">
							<s:div name="divElement8a" cssclass="labelData">
								<s:label name="label_data" cssclass="seda-ui-label label85 bold textright"
									text="Data" />
							</s:div>
							<s:div name="divElement8b" cssclass="floatleft">
								<s:div name="divDataDa" cssclass="divDataDa">
									<s:date label="da:" prefix="tx_data_da"
										yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
										locale="IT-it" descriptivemonth="false" separator="/"
										calendar="${tx_data_da}"
										cssclasslabel="labelsmall"
										cssclass="dateman">
									</s:date>
									<input type="hidden" id="tx_data_da_hidden" value="" />
								</s:div>
	
								<s:div name="divDataA" cssclass="divDataA">
									<s:date label="a:" prefix="tx_data_a"
										yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
										locale="IT-it" descriptivemonth="false" separator="/"
										calendar="${tx_data_a}"
										cssclasslabel="labelsmall"
										cssclass="dateman">
									</s:date>
									<input type="hidden" id="tx_data_a_hidden" value="" />
								</s:div>
							</s:div>
						</s:div>
					</s:div>
				</s:div>
			<br />
			<br />
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit"
					cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick=""
					text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_nuovo" validate="false" onclick=""
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

<c:if test="${!empty lista_forniture}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO FORNITURE APP IO
	</s:div>

	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_forniture"
			action="ioitalia.do" border="1"
			usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			<s:dgcolumn index="3" label="Società" asc="CSOC_A" desc="CSOC_D"></s:dgcolumn>
			<s:dgcolumn index="5" label="Utente" asc="CUTE_A" desc="CUTE_D"></s:dgcolumn>
			<s:dgcolumn index="7" label="Ente" asc="CENT_A" desc="CENT_D"></s:dgcolumn>
			<s:dgcolumn index="9" label="Tipologia Servizio" asc="TIP_A" desc="TIP_D"></s:dgcolumn>
			<s:dgcolumn index="14" label="Imposta Servizio" asc="IMP_A" desc="IMP_D"></s:dgcolumn>
			<s:dgcolumn index="11" label="Fornitura" asc="FOR_A" desc="FOR_D"></s:dgcolumn>
			<s:dgcolumn index="12" label="Data" asc="DATA_A" desc="DATA_D"></s:dgcolumn>
			<s:dgcolumn index="15" label="Esito" asc="ES_A" desc="ES_D"></s:dgcolumn> 
			<s:dgcolumn label="Azioni">
				<s:hyperlink
					href="ioitaliaforniture.do?vista=ioitalia&codop=edit&tx_id={1}&disabilita=true"
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Dettagli" text="" cssclass="blacklink hlStyle" />

				
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>