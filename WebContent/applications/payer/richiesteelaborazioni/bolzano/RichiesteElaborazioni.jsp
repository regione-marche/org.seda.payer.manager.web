<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="RichiesteElaborazioni" encodeAttributes="true" />

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
		$.datepicker.setDefaults($.datepicker.regional["it"]);
		$("#dtFlusso_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#dtFlusso_da_hidden").datepicker({
            minDate : new Date(annoDa, 0, 1),
            maxDate : new Date(annoA, 11, 31),
            yearRange : annoDa + ":" + annoA,
            showOn : "button",
            buttonImage : "../applications/templates/shared/img/calendar.gif",
            buttonImageOnly : true,
            onSelect : function(dateText, inst) {
                $("#dtFlusso_da_day_id").val(dateText.substr(0, 2));
                $("#dtFlusso_da_month_id").val(dateText.substr(3, 2));
                $("#dtFlusso_da_year_id").val(dateText.substr(6, 4));
            },
            beforeShow : function(input, inst) {
                updateValoreDatePickerFromDdl("dtFlusso_da_day_id",
                                            "dtFlusso_da_month_id",
                                            "dtFlusso_da_year_id",
                                            "dtFlusso_da_hidden");
            }
        });
		$("#dtFlusso_a_hidden").datepicker("option", "dateFormat","dd/mm/yyyy");
		$("#dtFlusso_a_hidden").datepicker({
            minDate : new Date(annoDa, 0, 1),
            maxDate : new Date(annoA, 11, 31),
            yearRange : annoDa + ":" + annoA,
            showOn : "button",
            buttonImage : "../applications/templates/shared/img/calendar.gif",
            buttonImageOnly : true,
            onSelect : function(dateText, inst) {
                $("#dtFlusso_a_day_id").val(dateText.substr(0, 2));
                $("#dtFlusso_a_month_id").val(dateText.substr(3, 2));
                $("#dtFlusso_a_year_id").val(dateText.substr(6, 4));
            },
            beforeShow : function(input, inst) {
                updateValoreDatePickerFromDdl("dtFlusso_a_day_id",
                                            "dtFlusso_a_month_id",
                                            "dtFlusso_a_year_id",
                                            "dtFlusso_a_hidden");
            }
        });

        $("#dtPeriodo_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
        $("#dtPeriodo_da_hidden").datepicker({
            minDate : new Date(annoDa, 0, 1),
            maxDate : new Date(annoA, 11, 31),
            yearRange : annoDa + ":" + annoA,
            showOn : "button",
            buttonImage : "../applications/templates/shared/img/calendar.gif",
            buttonImageOnly : true,
            onSelect : function(dateText, inst) {
                $("#dtPeriodo_da_day_id").val(dateText.substr(0, 2));
                $("#dtPeriodo_da_month_id").val(dateText.substr(3, 2));
                $("#dtPeriodo_da_year_id").val(dateText.substr(6, 4));
            },
            beforeShow : function(input, inst) {
                updateValoreDatePickerFromDdl(
                    "dtPeriodo_da_day_id",
                    "dtPeriodo_da_month_id",
                    "dtPeriodo_da_year_id",
                    "dtPeriodo_da_hidden");
            }
        });

        $("#dtPeriodo_a_hidden").datepicker("option", "dateFormat","dd/mm/yyyy");
        $("#dtPeriodo_a_hidden").datepicker({
            minDate : new Date(annoDa, 0, 1),
            maxDate : new Date(annoA, 11, 31),
            yearRange : annoDa + ":" + annoA,
            showOn : "button",
            buttonImage : "../applications/templates/shared/img/calendar.gif",
            buttonImageOnly : true,
            onSelect : function(dateText, inst) {
                $("#dtPeriodo_a_day_id").val(dateText.substr(0, 2));
                $("#dtPeriodo_a_month_id").val(dateText.substr(3, 2));
                $("#dtPeriodo_a_year_id").val(dateText.substr(6, 4));
            },
            beforeShow : function(input, inst) {
                updateValoreDatePickerFromDdl(
                    "dtPeriodo_a_day_id",
                    "dtPeriodo_a_month_id",
                    "dtPeriodo_a_year_id",
                    "dtPeriodo_a_hidden");
            }
        });
	});
</script>

<s:div name="divSelezione1" cssclass="divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="richiesteElaborazioniForm" action="richiesteElaborazioni.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:textbox name="DDLChanged" label="DDLChanged" bmodify="true" text="" cssclass="display_none" cssclasslabel="display_none" />

			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca - Richieste Elaborazioni</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:dropdownlist name="tx_societa"
						    disable="${societaDdlDisabled}" cssclass="tbddl floatleft"
						    label="Societ&aacute;:"
							cssclasslabel="label85 bold floatleft textright"
							cachedrowset="listaSocieta" usexml="true"
							onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
							valueselected="${tx_societa}">
							<s:ddloption text="Tutte le Societ&agrave;" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript><s:button id="tx_button_societa_changed" type="submit" disable="${societaDdlDisabled}" text="" onclick=""
							cssclass="btnimgStyle" title="Aggiorna" validate="false"/>
						</noscript>
					</s:div>

					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
						<s:dropdownlist name="tx_provincia"
							disable="${provinciaDdlDisabled}" cssclass="tbddl floatleft"
							label="Provincia:"
							cssclasslabel="label65 bold floatleft textright"
							cachedrowset="listaProvince" usexml="true"
							onchange="setFiredButton('tx_button_provincia_changed');this.form.submit();"
							valueselected="${tx_provincia}">
							<s:ddloption text="Tutte le Province" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript><s:button id="tx_button_provincia_changed"
							type="submit" text="" onclick="" cssclass="btnimgStyle"
							title="Aggiorna" validate="false" /></noscript>
					</s:div>
					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
					    <s:dropdownlist name="tx_UtenteEnte"
							disable="${ddlUtenteEnteDisabled}" cssclass="tbddlMax floatleft"
							label="Ente:" cssclasslabel="label65 bold textright"
							cachedrowset="listaUtentiEnti" usexml="true"
							valueselected="${tx_UtenteEnte}">
							<s:ddloption text="Tutti gli Enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
				</s:div>

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

					<s:div name="divElement12" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement12a" cssclass="labelData">
							<s:label name="label_data_creazione"
								cssclass="seda-ui-label label78 bold textright"
								text="Data Richiesta" />
						</s:div>

						<s:div name="divElement12b" cssclass="floatleft">
							<s:div name="divDtFlussoDA" cssclass="divDataDa">
								<s:date label="Da:" cssclasslabel="labelsmall"
									cssclass="dateman" prefix="dtFlusso_da"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
									locale="IT-it" descriptivemonth="false" separator="/"
									calendar="${dtFlusso_da}">
								</s:date>
								<input type="hidden" id="dtFlusso_da_hidden" value="" />
							</s:div>

							<s:div name="divDtFlussoA" cssclass="divDataA">
								<s:date label="A:" cssclasslabel="labelsmall" cssclass="dateman"
									prefix="dtFlusso_a" yearbegin="${ddlDateAnnoDa}"
									yearend="${ddlDateAnnoA}" locale="IT-it"
									descriptivemonth="false" separator="/"
									calendar="${dtFlusso_a}"></s:date>
								<input type="hidden" id="dtFlusso_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>

                    <s:div name="divElement13" cssclass="divRicMetadatiDoubleRow">
                        <s:div name="divElement13a" cssclass="labelData">
                            <s:label name="label_data_creazione"
                                cssclass="seda-ui-label label78 bold textright"
                                text="Periodo Richiesto" />
                        </s:div>

                        <s:div name="divElement13b" cssclass="floatleft">
                            <s:div name="divDtPeriodoDA" cssclass="divDataDa">
                                <s:date label="Da:" cssclasslabel="labelsmall"
                                    cssclass="dateman" prefix="dtPeriodo_da"
                                    yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
                                    locale="IT-it" descriptivemonth="false" separator="/"
                                    calendar="${dtPeriodo_da}">
                                </s:date>
                                <input type="hidden" id="dtPeriodo_da_hidden" value="" />
                            </s:div>

                            <s:div name="divDtPeriodoA" cssclass="divDataA">
                                <s:date label="A:" cssclasslabel="labelsmall" cssclass="dateman"
                                    prefix="dtPeriodo_a" yearbegin="${ddlDateAnnoDa}"
                                    yearend="${ddlDateAnnoA}" locale="IT-it"
                                    descriptivemonth="false" separator="/"
                                    calendar="${dtPeriodo_a}"></s:date>
                                <input type="hidden" id="dtPeriodo_a_hidden" value="" />
                            </s:div>
                        </s:div>
                    </s:div>
				</s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter"></s:div>
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
                    <s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
                        <s:dropdownlist name="statoElaborazione" cssclass="tbddl floatleft"
                            cssclasslabel="label85 bold floatleft textright" disable="false"
                            label="Stato elaborazione:" valueselected="${statoElaborazione}">
                            <s:ddloption value="" text="Tutte le prenotazioni" />
                            <s:ddloption value="0" text="Terminata" />
                            <s:ddloption value="1" text="In elaborazione" />
                        </s:dropdownlist>
                    </s:div>
				</s:div>
			</s:div>

			<s:div name="divCentered0" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" type="submit" text="Cerca" onclick="" cssclass="btnStyle" />
				<s:button id="tx_button_reset" type="submit" text="Reset" onclick="" cssclass="btnStyle" />
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


<c:if test="${!empty listaPrenotazioni}">
	<fmt:setLocale value="it_IT" />
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
		Elenco Richieste Elaborazioni
	</s:div>
	<s:datagrid viewstate="" cachedrowset="listaPrenotazioni"
		action="richiesteElaborazioni.do?vista=RichiesteElaborazioni" border="1" usexml="true"
		rowperpage="${applicationScope.rowsPerPage}">

        <s:dgcolumn index="1" label="Societ&aacute;" asc="SOCA" desc="SOCD"></s:dgcolumn>
 		<s:dgcolumn index="2" label="Utente" css="text_align_left" asc="UTEA" desc="UTED"></s:dgcolumn>
		<s:dgcolumn index="3" label="Ente" asc="ANEA" desc="ANED"></s:dgcolumn>
		<s:dgcolumn index="4" label="Data richiesta" format="dd/MM/yyyy" asc="DPREA" desc="DPREDD"></s:dgcolumn>
		<s:dgcolumn index="5" label="Periodo richiesto" css="text_align_left"></s:dgcolumn>
		<s:dgcolumn index="6" label="Stato" css="text_align_right" asc="FLAGA" desc="FLAGD"></s:dgcolumn>
        <s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;">
        	<s:if right="{6}" control="eq" left="In elaborazione">
        	    <s:then>
        	        <s:hyperlink href="richiesteElaborazioni.do" imagesrc="../applications/templates/richiesteelaborazioni/img/circle_red.png" alt="no download" text="" cssclass="hlStyle" />
            	</s:then>
                <s:else>
                    <s:hyperlink href="scaricaFatturazione.do?" imagesrc="../applications/templates/richiesteelaborazioni/img/download.png" alt="Download" text="" cssclass="hlStyle" />
                </s:else>
            </s:if>
		</s:dgcolumn>
	</s:datagrid>
</c:if>
