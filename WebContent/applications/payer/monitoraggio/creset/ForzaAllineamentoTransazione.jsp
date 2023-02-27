<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>


<script type="text/javascript">
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();

	$(function() {
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#tx_data_pagamento_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_pagamento_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_data_pagamento_day_id").val(dateText.substr(0,2));
												$("#tx_data_pagamento_month_id").val(dateText.substr(3,2));
												$("#tx_data_pagamento_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_pagamento_day_id",
				                            "tx_data_pagamento_month_id",
				                            "tx_data_pagamento_year_id",
				                            "tx_data_pagamento_hidden");
			}
		});
	});
</script>


<m:view_state id="forzaallineamentotransazione" encodeAttributes="true" />
<c:if test="${!empty tx_transazione}">

	<s:datagrid cssclass="tableMargin" viewstate=""
		cachedrowset="tx_transazione" action="forzaAllineamentoTransazione.do"
		border="1" usexml="true" rowperpage="">
		<s:dgcolumn index="1" label="Id Transazione"/>
		<s:dgcolumn index="4" label="Data Transazione" format="dd/MM/yyyy hh:mm:ss"></s:dgcolumn>
		<s:dgcolumn index="7" label="Cod. ident. Banca"></s:dgcolumn>
		<s:dgcolumn index="8" label="Cod. aut. Banca"></s:dgcolumn>
		<s:dgcolumn index="30" label="Note"></s:dgcolumn>
		<s:dgcolumn index="24" label="Importo totale" format="#,##0.00"></s:dgcolumn>
	</s:datagrid>
	
	<s:div name="divExternal" cssclass="divContainer600">
		<s:form name="tx_force_form" action="forzaAllineamentoTransazione.do"
			method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
				Forza allineamento transazione
			</s:div>
			<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
				<s:textbox validator="required;maxlength=20" cssclass="textareaman"
					cssclasslabel="labellarge bold textright" bmodify="true"
					name="tx_codice_identificativo_banca" showrequired="true"
					label="Codice Identificativo Banca:"
					text="${tx_codice_identificativo_banca}" maxlenght="50" />
			</s:div>
			<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
				<s:textbox validator="required;maxlength=50" cssclass="textareaman"
					cssclasslabel="labellarge bold textright" bmodify="true"
					name="tx_codice_autorizzazione_banca" showrequired="true"
					label="Codice Autorizzazione Banca:"
					text="${tx_codice_autorizzazione_banca}" maxlenght="20" />
			</s:div>
			<s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
				<s:date label="Data pagamento:"
					cssclasslabel="labellarge bold textright" cssclass="dateman"
					prefix="tx_data_pagamento" yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
					locale="IT-it" descriptivemonth="false" separator="/"
					calendar="${tx_data_pagamento}" 
					validator="required;dateISO" showrequired="true"
					message="[dateISO=Data Pagamento: ${msg_dataISO_valida}]">
				</s:date>
				<input type="hidden" id="tx_data_pagamento_hidden" value="" />
			</s:div>
			<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
				<s:label name="label_ora" text="Ora pagamento:"
					cssclass="seda-ui-label labellarge bold textright" />
				<s:label name="label_oratext" text="Ora" cssclass="labeltext" />
				<select name="tx_ora_pagamento" class="floatleft">
					<c:forEach var="i" begin="0" end="23">
						<c:choose>
							<c:when test="${i == tx_ora_pagamento}">
								<option value="${i}" selected="selected">${i}</option>
							</c:when>
							<c:otherwise>
								<option value="${i}">${i}</option>
							</c:otherwise>
						</c:choose>
						
					</c:forEach>
				</select>
				<s:label name="label_minutitext" text="Minuti" cssclass="labeltext" />
				<select name="tx_minuti_pagamento" class="floatleft">
					<c:forEach var="i" begin="0" end="59">
						<c:choose>
							<c:when test="${i == tx_minuti_pagamento}">
								<option value="${i}" selected="selected">${i}</option>
							</c:when>
							<c:otherwise>
								<option value="${i}">${i}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</s:div>
			<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
				<s:textbox validator="maxlength=256" cssclass="textareaman_note"
					cssclasslabel="labellarge bold textright" bmodify="true"
					name="tx_note" label="Note:" text="${tx_note}" maxlenght="200" />
			</s:div>

			<s:div name="divButtonLeft" cssclass="divButton1">
				<s:button id="tx_button_edit" type="submit" text="Aggiorna"
					onclick="" cssclass="btnStyle" />
			</s:div>

			<s:div name="divHidden" cssclass="divHidden">
				<s:textbox name="tx_codice_transazione_hidden" label=""
					bmodify="true" text="${tx_codice_transazione_hidden}"
					cssclass="rend_display_none btnStyle" />
			</s:div>
		</s:form>
	</s:div>

</c:if>

<s:form name="frmIndietro"
	action="ritorna.do?vista=monitoraggiotransazioni" method="post"
	hasbtn1="false" hasbtn2="false" hasbtn3="false">
	<s:div name="divIndietro" cssclass="divButton1">
		<s:button id="tx_button_transazioni" type="submit" text="Indietro"
			onclick="" cssclass="btnStyle" />
	</s:div>
</s:form>







