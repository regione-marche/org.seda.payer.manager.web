<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<m:view_state id="monitoraggiowis" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js" type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js" type="text/javascript"></script>
	
<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}

	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();
	
	$(function() {
		$.datepicker.setDefaults($.datepicker.regional["it"]);
		$("#tx_data_presentazione_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_presentazione_da_hidden").datepicker( {
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn : "button",
			buttonImage : "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly : true,
			onSelect : function(dateText, inst) {
				$("#tx_data_presentazione_da_day_id").val(dateText.substr(0, 2));
				$("#tx_data_presentazione_da_month_id").val(dateText.substr(3, 2));
				$("#tx_data_presentazione_da_year_id").val(dateText.substr(6, 4));
			},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_presentazione_da_day_id",
				                            "tx_data_presentazione_da_month_id",
				                            "tx_data_presentazione_da_year_id",
				                            "tx_data_presentazione_da_hidden");
			}
		});
		$("#tx_data_presentazione_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_presentazione_a_hidden").datepicker( {
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn : "button",
			buttonImage : "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly : true,
			onSelect : function(dateText, inst) {
				$("#tx_data_presentazione_a_day_id").val(dateText.substr(0, 2));
				$("#tx_data_presentazione_a_month_id").val(dateText.substr(3, 2));
				$("#tx_data_presentazione_a_year_id").val(dateText.substr(6, 4));
			},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_presentazione_a_day_id",
				                            "tx_data_presentazione_a_month_id",
				                            "tx_data_presentazione_a_year_id",
				                            "tx_data_presentazione_a_hidden");
			}
		});
		$("#tx_data_comunicazione_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_comunicazione_da_hidden").datepicker( {
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn : "button",
			buttonImage : "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly : true,
			onSelect : function(dateText, inst) {
				$("#tx_data_comunicazione_da_day_id").val(dateText.substr(0, 2));
				$("#tx_data_comunicazione_da_month_id").val(dateText.substr(3, 2));
				$("#tx_data_comunicazione_da_year_id").val(dateText.substr(6, 4));
			},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_comunicazione_da_day_id",
				                            "tx_data_comunicazione_da_month_id",
				                            "tx_data_comunicazione_da_year_id",
				                            "tx_data_comunicazione_da_hidden");
			}
		});
		$("#tx_data_comunicazione_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_comunicazione_a_hidden").datepicker( {
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn : "button",
			buttonImage : "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly : true,
			onSelect : function(dateText, inst) {
				$("#tx_data_comunicazione_a_day_id").val(dateText.substr(0, 2));
				$("#tx_data_comunicazione_a_month_id").val(dateText.substr(3, 2));
				$("#tx_data_comunicazione_a_year_id").val(dateText.substr(6, 4));
			},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_comunicazione_a_day_id",
				                            "tx_data_comunicazione_a_month_id",
				                            "tx_data_comunicazione_a_year_id",
				                            "tx_data_comunicazione_a_hidden");
			}
		});
	});
	
</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="monitoraggioWISForm" action="monitoraggiowis.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
				Ricerca Comunicazioni Imposta Soggiorno
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">

						<s:dropdownlist name="ddlProvincia" disable="false"
							cssclass="tbddl floatleft" label="Provincia:"
							cssclasslabel="label85 bold floatleft textright"
							cachedrowset="listprovince" usexml="true"
							onchange="setFiredButton('tx_button_provincia_changed');this.form.submit();"
							valueselected="${ddlProvincia}">
							<s:ddloption text="Tutte le Province" value="" />
							<s:ddloption  value="{2}" text="{1}"/>
						</s:dropdownlist>
						<noscript><s:button id="tx_button_provincia_changed"
							onclick="" text="" type="submit"
							cssclass="btnimgStyle" title="Aggiorna" validate="false" /></noscript>
					</s:div>

					<s:div name="divElement2" cssclass="divRicMetadatiCenterSmall">
						<s:dropdownlist name="ddlComune"
							disable="false" cssclass="tbddl floatleft"
							label="Comune:"
							cssclasslabel="label65 bold floatleft textright"
							cachedrowset="listcomuni" usexml="true"
							valueselected="${ddlComune}">
							<s:ddloption text="Tutti i Comuni" value="" />
							<s:ddloption value="{3}" text="{1} ({3})"/>
						</s:dropdownlist>
					</s:div>

					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:textbox validator="ignore;"
							bmodify="true" maxlenght="100" cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							name="txtNumAutorizzazione" label="Num. Autoriz.:"
							text="${txtNumAutorizzazione}" />
					</s:div>

				</s:div>

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement4" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement18a" cssclass="labelData">
							<s:label name="label_data"
								cssclass="seda-ui-label label85 bold textright" text="Data Presentazione" />
						</s:div>

						<s:div name="divElement5" cssclass="floatleft">
							<s:div name="divDataPresentazioneDa" cssclass="divDataDa">
								<s:date label="Da:" prefix="tx_data_presentazione_da"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
									locale="IT-it" descriptivemonth="false" separator="/"
									calendar="${tx_data_presentazione_da}" cssclasslabel="labelsmall"
									cssclass="dateman">
								</s:date>
								<input type="hidden" id="tx_data_presentazione_da_hidden" value="" />
							</s:div>

							<s:div name="divDataPresentazioneA" cssclass="divDataA">
								<s:date label="A:" prefix="tx_data_presentazione_a"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
									locale="IT-it" descriptivemonth="false" separator="/"
									calendar="${tx_data_presentazione_a}" cssclasslabel="labelsmall"
									cssclass="dateman">
								</s:date>
								<input type="hidden" id="tx_data_presentazione_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>

					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="txtTipoComunicazione" disable="false"
							cssclass="tbddlMax floatleft" label="Tipo Comunic.:"
							cssclasslabel="label85 bold textright"
							valueselected="${txtTipoComunicazione}">
							<s:ddloption text="Tutte le Comunicazioni" value="" />
							<s:ddloption text="Ordinaria" value="O" />
							<s:ddloption text="Integrativa" value="I" />
						</s:dropdownlist>
					</s:div>
					
					<s:div name="divElement6a" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="ddlTipoStruttura" disable="false"
							cssclass="tbddlMax floatleft" label="Tipo Strutt.:"
							cachedrowset="listtipologiestrutture" usexml="true"
							cssclasslabel="label85 bold textright"
							valueselected="${ddlTipoStruttura}">
							<s:ddloption text="Tutte le Tipologie" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>

				</s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement7" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement18a" cssclass="labelData">
							<s:label name="label_data"
								cssclass="seda-ui-label label85 bold textright" text="Periodo Comunicazione" />
						</s:div>

						<s:div name="divElement8" cssclass="floatleft">
							<s:div name="divDataComunicazioneDa" cssclass="divDataDa">
								<s:date label="Da:" prefix="tx_data_comunicazione_da"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
									locale="IT-it" descriptivemonth="false" separator="/"
									calendar="${tx_data_comunicazione_da}" cssclasslabel="labelsmall"
									cssclass="dateman">
								</s:date>
								<input type="hidden" id="tx_data_comunicazione_da_hidden" value="" />
							</s:div>

							<s:div name="divDataComunicazioneA" cssclass="divDataA">
								<s:date label="A:" prefix="tx_data_comunicazione_a"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
									locale="IT-it" descriptivemonth="false" separator="/"
									calendar="${tx_data_comunicazione_a}" cssclasslabel="labelsmall"
									cssclass="dateman">
								</s:date>
								<input type="hidden" id="tx_data_comunicazione_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>

					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="txtStatoComunicazione" disable="false"
							cssclass="tbddlMax floatleft" label="Stato Comun.:"
							cssclasslabel="label85 bold textright"
							valueselected="${txtStatoComunicazione}">
							<s:ddloption text="Tutti gli Stati" value="" />
							<s:ddloption text="In compilazione" value="N" />
							<s:ddloption text="Acquisizione in corso" value="T" />
							<s:ddloption text="Acquisita" value="C" />
							<s:ddloption text="Annullata" value="A" />
						</s:dropdownlist>
					</s:div>
					
					<s:div name="divElement9a" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;" bmodify="true"
							name="txtInsegna" label="Insegna:"
							text="${txtInsegna}" maxlenght="256"
							cssclass="textareaman" cssclasslabel="label85 bold textright" />
					</s:div>
				</s:div>

				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">

					<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;" bmodify="true"
							name="txtNumeroDocumento" label="N°Documento:"
							text="${txtNumeroDocumento}" maxlenght="20"
							cssclass="textareaman" cssclasslabel="label85 bold textright" />
					</s:div>
					
					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;" bmodify="true"
							name="txtUsernameOperatore" label="Username op.:"
							text="${txtUsernameOperatore}" maxlenght="50"
							cssclass="textareaman" cssclasslabel="label85 bold textright" />
					</s:div>
					
					<s:div name="divElement12" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="txtStatoPagamento" disable="false"
							cssclass="tbddlMax floatleft" label="Stato Pagam.:"
							cssclasslabel="label85 bold textright"
							valueselected="${txtStatoPagamento}">
							<s:ddloption text="Tutti gli Stati" value="" />
							<s:ddloption text="Pagato" value="Y" />
							<s:ddloption text="Non Pagato" value="N" />
						</s:dropdownlist>
					</s:div>
				</s:div>

			</s:div>

			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<br />
				<s:button id="tx_button_cerca" type="submit" text="Cerca" cssclass="btnStyle" onclick="" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />

				<c:if test="${!empty listaComunicazioni}">
					<s:button id="tx_button_download" validate="false" onclick="" text="Download" type="submit" cssclass="btnStyle" />
				</c:if>

				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
			</s:div>
		</s:form>
	</s:div>

</s:div>

<c:if test="${!empty listaComunicazioni}">

	<fmt:setLocale value="it_IT" />
	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Elenco Comunicazioni Imposta Soggiorno
	</s:div>

	<s:div name="divRisultati" cssclass="divRisultati">
		<s:datagrid cachedrowset="listaComunicazioni" action="monitoraggiowis.do?vista=monitoraggiowis" 
			border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}" viewstate="">

			<s:dgcolumn index="1" label="Comune" asc="COMUNEA" desc="COMUNED" />
			<s:dgcolumn label="Num. Autorizzazione" asc="AUTORIZA" desc="AUTORIZD" >
				<s:hyperlink href="#" text="{2}" alt="{30}" cssclass="blacklinknocursor"/>
			</s:dgcolumn>
			<s:dgcolumn index="29" label="Tipologia Struttura" asc="TPSTRUTTA" desc="TPSTRUTTD" />
			<s:dgcolumn index="8" label="Data Presentaz." format="dd/MM/yyyy" asc="DATAINSEA" desc="DATAINSED" />
			<s:dgcolumn index="28" label="Periodo Comunicaz." asc="PERIODOA" desc="PERIODOD" />
			<s:dgcolumn label="Tipo Comunicaz." asc="TIPOA" desc="TIPOD" >
				<s:if left="{12}" control="eq" right="O">
					<s:then>Ordinaria</s:then>
					<s:else>Integrativa</s:else>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn index="24" label="N° Ospiti" css="textright" asc="SOGGETTIA" desc="SOGGETTID" />
			<s:dgcolumn index="26" label="N° Pernott. soggetti<br /> ad imposta" css="textright" asc="GGPAGA" desc="GGPAGD"/>
			<s:dgcolumn index="27" label="Importo" format="#0.00" css="textright" asc="IMPORTOA" desc="IMPORTOD"/>
			<s:dgcolumn label="Stato" asc="STATOCOMA" desc="STATOCOMD">
				<s:if left="{16}" control="eq" right="N">
					<s:then>In compilazione</s:then>
				</s:if>
				<s:if left="{16}" control="eq" right="T">
					<s:then>Acquisizione in corso</s:then>
				</s:if>
				<s:if left="{16}" control="eq" right="C">
					<s:then>Acquisita</s:then>
				</s:if>
				<s:if left="{16}" control="eq" right="A">
					<s:then>Annullata</s:then>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn index="22" label="Data Pagamento" format="dd/MM/yyyy" asc="DATAPAGA" desc="DATAPAGD"/>
			<s:dgcolumn index="23" label="Username operatore" asc="USERA" desc="USERD" />
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;">
				<s:hyperlink
					href="dettagliocomunicazione.do?chiave_comunicazione={3}&comune={1}&numaut={2}"
					imagesrc="../applications/templates/shared/img/Info.png"
					alt="Dettaglio Comunicazione" text=""
					cssclass="blacklink hlStyle" />
			</s:dgcolumn>
		</s:datagrid>

	</s:div>
	
	
	<s:div name="divTableTitle2" cssclass="divTableTitle bold">
		Riepilogo statistico
	</s:div>

	
	<s:table id="tableRiepilogoComunicazioni" cssclass="seda-ui-datagrid" border="1" cellspacing="0"
		cellpadding="0">
		<s:thead>
			<s:tr>
				<s:th cssclass="seda-ui-datagridheadercell" icol="7">
					<b>Totali</b>
				</s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr cssclass="seda-ui-datagridrowdispari">
				<s:td cssclass="seda-ui-datagridcell">N° Comunicazioni</s:td>
				<s:td cssclass="seda-ui-datagridcell">Totale Ospiti</s:td>
				<s:td cssclass="seda-ui-datagridcell">Totale Pernottamenti Soggetti ad Imposta</s:td>
				<s:td cssclass="seda-ui-datagridcell">Importo Totale</s:td>
			</s:tr>
			<s:tr cssclass="seda-ui-datagridrowpari">
				<s:td cssclass="seda-ui-datagridcell text_align_right"><b>${numcomunicazionitotali}</b></s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><b>${soggettitotali}</b></s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right"><b>${pernottamentiimpostatotali}</b></s:td>
				<s:td cssclass="seda-ui-datagridcell text_align_right">
					<b><fmt:formatNumber type="NUMBER" value="${importototale}" minFractionDigits="2" maxFractionDigits="2" /> &#8364;</b>
				</s:td>
			</s:tr>
		</s:tbody>
	</s:table>

</c:if>





