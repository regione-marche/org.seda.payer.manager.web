<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:setBundle name="org.seda.payer.i18n.resources.TemplateStrings" />
<m:view_state id="viewstate" encodeAttributes="true"  encodeParameters="true"  />
<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>

<script type="text/javascript" >
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}

</script>
<style>
.white_space_nowrap{
	white-space:nowrap;
}

</style>
<c:url value="" var="formParameters">
	<c:if test="${!empty param.pageNumber}">
		<c:param name="pageNumber_hidden">${param.pageNumber}</c:param>
	</c:if>
	<c:if test="${!empty rowsPerPage}">
		<c:param name="rowsPerPage_hidden">${param.rowsPerPage}</c:param>
	</c:if>
	<c:if test="${!empty orderBy}">
		<c:param name="orderBy_hidden">${param.orderBy}</c:param>
	</c:if>
	<c:if test="${!empty param.codiceIUV}">
		<c:param name="codiceIUV">${param.codiceIUV}</c:param>
	</c:if>
	<c:if test="${!empty param.statoQuadratura}">
		<c:param name="statoQuadratura">${param.statoQuadratura}</c:param>
	</c:if>
	<c:if test="${!empty param.importoUIV_da}">
		<c:param name="importoUIV_da">${param.importoUIV_da}</c:param>
	</c:if>
	<c:if test="${!empty param.importoUIV_a}">
		<c:param name="importoUIV_a">${param.importoUIV_a}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_societa}">
		<c:param name="tx_societa">${param.tx_societa}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_provincia}">
		<c:param name="tx_provincia">${param.tx_provincia}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_UtenteEnte}">
		<c:param name="tx_UtenteEnte">${param.tx_UtenteEnte}</c:param>
	</c:if>
	<c:if test="${!empty param.statoSquadratura}">
		<c:param name="statoSquadratura">${param.statoSquadratura}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_tipo_carta}">
		<c:param name="tx_tipo_carta">${param.tx_tipo_carta}</c:param>
	</c:if>
	<c:if test="${!empty param.idFlusso}">
		<c:param name="idFlusso">${param.idFlusso}</c:param>
	</c:if>
	<c:if test="${!empty param.dtFlusso_da_day}">
		<c:param name="dtFlusso_da_day">${param.dtFlusso_da_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtFlusso_da_month}">
		<c:param name="dtFlusso_da_month">${param.dtFlusso_da_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtFlusso_da_year}">
		<c:param name="dtFlusso_da_year">${param.dtFlusso_da_year}</c:param>
	</c:if>
	<c:if test="${!empty param.dtFlusso_a_day}">
		<c:param name="dtFlusso_a_day">${param.dtFlusso_a_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtFlusso_a_month}">
		<c:param name="dtFlusso_a_month">${param.dtFlusso_a_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtFlusso_a_year}">
		<c:param name="dtFlusso_a_year">${param.dtFlusso_a_year}</c:param>
	</c:if>
	<c:if test="${!empty param.versOggetto}">
		<c:param name="versOggetto">${param.versOggetto}</c:param>
	</c:if>
	<c:if test="${!empty param.dtregol_da_day}">
		<c:param name="dtregol_da_day">${param.dtregol_da_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtregol_da_month}">
		<c:param name="dtregol_da_month">${param.dtregol_da_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtregol_da_year}">
		<c:param name="dtregol_da_year">${param.dtregol_da_year}</c:param>
	</c:if>
	<c:if test="${!empty param.dtregol_a_day}">
		<c:param name="dtregol_a_day">${param.dtregol_a_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtregol_a_month}">
		<c:param name="dtregol_a_month">${param.dtregol_a_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtregol_a_year}">
		<c:param name="dtregol_a_year">${param.dtregol_a_year}</c:param>
	</c:if>
	<c:if test="${!empty param.keyQuadratura}">
		<c:param name="keyQuadratura">${param.keyQuadratura}</c:param>
	</c:if>
	<c:if test="${!empty param.nomeFileTxt}">
		<c:param name="nomeFileTxt">${param.nomeFileTxt}</c:param>
	</c:if>
	<c:if test="${!empty param.dtChiusuraFlusso_da_day}">
		<c:param name="dtChiusuraFlusso_da_day">${param.dtChiusuraFlusso_da_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtChiusuraFlusso_da_month}">
		<c:param name="dtChiusuraFlusso_da_month">${param.dtChiusuraFlusso_da_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtChiusuraFlusso_da_year}">
		<c:param name="dtChiusuraFlusso_da_year">${param.dtChiusuraFlusso_da_year}</c:param>
	</c:if>
	<c:if test="${!empty param.dtChiusuraFlusso_a_day}">
		<c:param name="dtChiusuraFlusso_a_day">${param.dtChiusuraFlusso_a_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtChiusuraFlusso_a_month}">
		<c:param name="dtChiusuraFlusso_a_month">${param.dtChiusuraFlusso_a_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtChiusuraFlusso_a_year}">
		<c:param name="dtChiusuraFlusso_a_year">${param.dtChiusuraFlusso_a_year}</c:param>
	</c:if>
	<c:if test="${!empty param.dtMakeFlusso_da_day}">
		<c:param name="dtMakeFlusso_da_day">${param.dtMakeFlusso_da_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtMakeFlusso_da_month}">
		<c:param name="dtMakeFlusso_da_month">${param.dtMakeFlusso_da_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtMakeFlusso_da_year}">
		<c:param name="dtMakeFlusso_da_year">${param.dtMakeFlusso_da_year}</c:param>
	</c:if>
	<c:if test="${!empty param.dtMakeFlusso_a_day}">
		<c:param name="dtMakeFlusso_a_day">${param.dtMakeFlusso_a_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtMakeFlusso_a_month}">
		<c:param name="dtMakeFlusso_a_month">${param.dtMakeFlusso_a_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtMakeFlusso_a_year}">
		<c:param name="dtMakeFlusso_a_year">${param.dtMakeFlusso_a_year}</c:param>
	</c:if>
	<c:if test="${!empty param.tipologiaFlusso}">
		<c:param name="tipologiaFlusso">${param.tipologiaFlusso}</c:param>
	</c:if>
	<c:if test="${!empty param.scartateFlusso}">
		<c:param name="scartateFlusso">${param.scartateFlusso}</c:param>
	</c:if>
	<c:if test="${!empty param.impPagamento_da}">
		<c:param name="impPagamento_da">${param.impPagamento_da}</c:param>
	</c:if>
	<c:if test="${!empty param.impPagamento_a}">
		<c:param name="impPagamento_a">${param.impPagamento_a}</c:param>
	</c:if>
	<c:if test="${!empty ext}">
		<c:param name="ext">${ext}</c:param>
	</c:if>
</c:url>

<s:div name="divSelezione1" cssclass="divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="dettaglioIUVFlussoForm"
			action="dettaglioIUVFlusso.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:textbox name="DDLChanged" label="DDLChanged" bmodify="true"
				text="" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="ext" label="ext" bmodify="true" text="${ext}"
				cssclass="display_none" cssclasslabel="display_none" />

			<s:textbox name="hRowsPerPage" label="rowsPerPageRic" bmodify="true"
				text="${rowsPerPage}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="hPageNumber" label="pageNumberRic" bmodify="true"
				text="${pageNumber}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="hOrder" label="orderRic" bmodify="true"
				text="${order}" cssclass="display_none" cssclasslabel="display_none" />

			<s:textbox name="tx_societa" label="tx_societa" bmodify="true"
				text="${tx_societa}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="tx_provincia" label="tx_provincia" bmodify="true"
				text="${tx_provincia}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="tx_UtenteEnte" label="tx_UtenteEnte" bmodify="true"
				text="${tx_UtenteEnte}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="statoSquadratura" label="statoSquadratura" bmodify="true"
				text="${statoSquadratura}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="tx_tipo_carta" label="tx_tipo_carta" bmodify="true"
				text="${tx_tipo_carta}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="idFlusso" label="idFlusso" bmodify="true"
				text="${idFlusso}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtFlusso_da_day" label="dtFlusso_da_day" bmodify="true"
				text="${dtFlusso_da_day}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtFlusso_da_month" label="dtFlusso_da_month" bmodify="true"
				text="${dtFlusso_da_month}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtFlusso_da_year" label="dtFlusso_da_year" bmodify="true"
				text="${dtFlusso_da_year}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtFlusso_a_day" label="dtFlusso_a_day" bmodify="true"
				text="${dtFlusso_a_day}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtFlusso_a_month" label="dtFlusso_a_month" bmodify="true"
				text="${dtFlusso_a_month}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtFlusso_a_year" label="dtFlusso_a_year" bmodify="true"
				text="${dtFlusso_a_year}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="versOggetto" label="versOggetto" bmodify="true"
				text="${versOggetto}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtregol_da_day" label="dtregol_da_day" bmodify="true"
				text="${dtregol_da_day}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtregol_da_month" label="dtregol_da_month" bmodify="true"
				text="${dtregol_da_month}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtregol_da_year" label="dtregol_da_year" bmodify="true"
				text="${dtregol_da_year}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtregol_a_day" label="dtregol_a_day" bmodify="true"
				text="${dtregol_a_day}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtregol_a_month" label="dtregol_a_month" bmodify="true"
				text="${dtregol_a_month}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtregol_a_year" label="dtregol_a_year" bmodify="true"
				text="${dtregol_a_year}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="keyQuadratura" label="keyQuadratura" bmodify="true"
				text="${keyQuadratura}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="nomeFileTxt" label="nomeFileTxt" bmodify="true"
				text="${nomeFileTxt}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtChiusuraFlusso_da_day" label="dtChiusuraFlusso_da_day" bmodify="true"
				text="${dtChiusuraFlusso_da_day}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtChiusuraFlusso_da_month" label="dtChiusuraFlusso_da_month" bmodify="true"
				text="${dtChiusuraFlusso_da_month}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtChiusuraFlusso_da_year" label="dtChiusuraFlusso_da_year" bmodify="true"
				text="${dtChiusuraFlusso_da_year}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtChiusuraFlusso_a_day" label="dtChiusuraFlusso_a_day" bmodify="true"
				text="${dtChiusuraFlusso_a_day}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtChiusuraFlusso_a_month" label="dtChiusuraFlusso_a_month" bmodify="true"
				text="${dtChiusuraFlusso_a_month}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtChiusuraFlusso_a_year" label="dtChiusuraFlusso_a_year" bmodify="true"
				text="${dtChiusuraFlusso_a_year}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtMakeFlusso_da_day" label="dtMakeFlusso_da_day" bmodify="true"
				text="${dtMakeFlusso_da_day}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtMakeFlusso_da_month" label="dtMakeFlusso_da_month" bmodify="true"
				text="${dtMakeFlusso_da_month}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtMakeFlusso_da_year" label="dtMakeFlusso_da_year" bmodify="true"
				text="${dtMakeFlusso_da_year}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtMakeFlusso_a_day" label="dtMakeFlusso_a_day" bmodify="true"
				text="${dtMakeFlusso_a_day}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtMakeFlusso_a_month" label="dtMakeFlusso_a_month" bmodify="true"
				text="${dtMakeFlusso_a_month}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="dtMakeFlusso_a_year" label="dtMakeFlusso_a_year" bmodify="true"
				text="${dtMakeFlusso_a_year}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="tipologiaFlusso" label="tipologiaFlusso" bmodify="true"
				text="${tipologiaFlusso}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="scartateFlusso" label="scartateFlusso" bmodify="true"
				text="${scartateFlusso}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="impPagamento_da" label="impPagamento_da" bmodify="true"
				text="${impPagamento_da}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="impPagamento_a" label="impPagamento_a" bmodify="true"
				text="${impPagamento_a}" cssclass="display_none"
				cssclasslabel="display_none" />
				
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
				Dettaglio Flusso
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="">
					<s:div name="divElement1" cssclass="divTableDettaglioOnTop">	
						<s:table cssclass="seda-ui-datagrid" border="1" cellspacing="0" cellpadding="3">
							<s:thead>
								<s:tr cssclass="seda-ui-datagridrowdispari">
									<s:th cssclass="seda-ui-datagridcell">Società</s:th>
									<s:th cssclass="seda-ui-datagridcell">Id. Flusso</s:th>
									<s:th cssclass="seda-ui-datagridcell">Data Flusso</s:th>
									<s:th cssclass="seda-ui-datagridcell">Numero RT</s:th>
									<s:th cssclass="seda-ui-datagridcell">I.U.V. scartate</s:th>
									<s:th cssclass="seda-ui-datagridcell">Id. Quadratura</s:th>
								</s:tr>
							</s:thead>
							<s:tbody>
								<s:tr cssclass="seda-ui-datagridrowpari">
									<s:td cssclass="seda-ui-datagridcell text_align_center">
										${flusso_soc}
									</s:td>
									<s:td cssclass="seda-ui-datagridcell text_align_center">
										${flusso_id}
									</s:td>
									<s:td cssclass="seda-ui-datagridcell text_align_center">
										${flusso_data}
									</s:td>
									<s:td cssclass="seda-ui-datagridcell text_align_center">
										${flusso_numrt}
									</s:td>
									<s:td cssclass="seda-ui-datagridcell text_align_center">
										${flusso_iuvsca}
									</s:td>
									<s:td cssclass="seda-ui-datagridcell text_align_center">
										${flusso_idquad}
									</s:td>
								</s:tr>
							</s:tbody>
						</s:table>
					</s:div>
				</s:div>
				
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
				
					<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;" maxlenght="35"
							cssclass="textareaman" cssclasslabel="label85 bold textright"
							bmodify="true" name="codiceIUV" label="I.U.V.:"
							text="${codiceIUV}" />
					</s:div>
					
				</s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					
					<s:div name="divElement3" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement3a" cssclass="labelData">
							<s:label name="label_data_creazione" 
								cssclass="seda-ui-label label78 bold textright"
								text="Importo I.U.V." />
						</s:div>

						<s:div name="divElement3b" cssclass="floatleft">
							<s:div name="divDtGiornaleDA" cssclass="divDataDa">
								<s:textbox validator="ignore;numberIT" maxlenght="20"
								label="Da:" cssclasslabel="labelsmall" cssclass="dateman"
								bmodify="true" name="importoUIV_da" text="${importoUIV_da}"/>
							</s:div>

							<s:div name="divDtGiornaleA" cssclass="divDataA">
								<s:textbox validator="ignore;numberIT" maxlenght="20"
								label="A:" cssclasslabel="labelsmall" cssclass="dateman"
								bmodify="true" name="importoUIV_a" text="${importoUIV_a}"/>
							</s:div>
						</s:div>

					</s:div>
				</s:div>

				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement2a" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="statoQuadratura" cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright" disable="false"
							label="Quadrate:" valueselected="${statoQuadratura}">
							<s:ddloption value="" text="Tutte" />
							<s:ddloption value="Y" text="Si" />
							<s:ddloption value="N" text="No" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				
			</s:div>
			
			<s:div name="divCentered0" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" type="submit" text="Cerca" onclick="" cssclass="btnStyle" />
				<s:button id="tx_button_reset" type="submit" text="Reset" onclick="" cssclass="btnStyle" />
				<s:hyperlink name="hpAnnulla" cssclass="hlToBtnStyle" href="riconciliazioneTransazioniNodo.do${formParameters}&indietro=Y" text="Indietro" />
			</s:div>
		</s:form>
		
	</s:div>

</s:div>

<c:if test="${!empty lista_iuv_di_flusso}">
	<fmt:setLocale value="it_IT" />
	
	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Elenco I.U.V. nel Flusso
	</s:div>
	<s:datagrid cachedrowset="lista_iuv_di_flusso"
		action="dettaglioIUVFlusso.do" border="1" usexml="true"
		rowperpage="${applicationScope.rowsPerPage}" viewstate="true">
		<s:action>
			<c:url value="dettaglioIUVFlusso.do">
				<c:if test="${!empty param.pageNumber}">
					<c:param name="pageNumber_hidden">${param.pageNumber}</c:param>
				</c:if>
				<c:if test="${!empty rowsPerPage}">
					<c:param name="rowsPerPage_hidden">${param.rowsPerPage}</c:param>
				</c:if>
				<c:if test="${!empty orderBy}">
					<c:param name="orderBy_hidden">${param.orderBy}</c:param>
				</c:if>
				<%-- inizio parametri filtri form dettaglio --%>
				<c:if test="${!empty param.codiceIUV}">
					<c:param name="codiceIUV">${param.codiceIUV}</c:param>
				</c:if>
				<c:if test="${!empty param.statoQuadratura}">
					<c:param name="statoQuadratura">${param.statoQuadratura}</c:param>
				</c:if>
				<c:if test="${!empty param.importoUIV_da}">
					<c:param name="importoUIV_da">${param.importoUIV_da}</c:param>
				</c:if>
				<c:if test="${!empty param.importoUIV_a}">
					<c:param name="importoUIV_a">${param.importoUIV_a}</c:param>
				</c:if>
				<%-- fine parametri filtri form dettaglio --%>
				<c:if test="${!empty param.tx_societa}">
					<c:param name="tx_societa">${param.tx_societa}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_provincia}">
					<c:param name="tx_provincia">${param.tx_provincia}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_UtenteEnte}">
					<c:param name="tx_UtenteEnte">${param.tx_UtenteEnte}</c:param>
				</c:if>
				<c:if test="${!empty param.statoSquadratura}">
					<c:param name="statoSquadratura">${param.statoSquadratura}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_tipo_carta}">
					<c:param name="tx_tipo_carta">${param.tx_tipo_carta}</c:param>
				</c:if>
				<c:if test="${!empty param.idFlusso}">
					<c:param name="idFlusso">${param.idFlusso}</c:param>
				</c:if>
				<c:if test="${!empty param.dtFlusso_da_day}">
					<c:param name="dtFlusso_da_day">${param.dtFlusso_da_day}</c:param>
				</c:if>
				<c:if test="${!empty param.dtFlusso_da_month}">
					<c:param name="dtFlusso_da_month">${param.dtFlusso_da_month}</c:param>
				</c:if>
				<c:if test="${!empty param.dtFlusso_da_year}">
					<c:param name="dtFlusso_da_year">${param.dtFlusso_da_year}</c:param>
				</c:if>
				<c:if test="${!empty param.dtFlusso_a_day}">
					<c:param name="dtFlusso_a_day">${param.dtFlusso_a_day}</c:param>
				</c:if>
				<c:if test="${!empty param.dtFlusso_a_month}">
					<c:param name="dtFlusso_a_month">${param.dtFlusso_a_month}</c:param>
				</c:if>
				<c:if test="${!empty param.dtFlusso_a_year}">
					<c:param name="dtFlusso_a_year">${param.dtFlusso_a_year}</c:param>
				</c:if>
				<c:if test="${!empty param.versOggetto}">
					<c:param name="versOggetto">${param.versOggetto}</c:param>
				</c:if>
				<c:if test="${!empty param.dtregol_da_day}">
					<c:param name="dtregol_da_day">${param.dtregol_da_day}</c:param>
				</c:if>
				<c:if test="${!empty param.dtregol_da_month}">
					<c:param name="dtregol_da_month">${param.dtregol_da_month}</c:param>
				</c:if>
				<c:if test="${!empty param.dtregol_da_year}">
					<c:param name="dtregol_da_year">${param.dtregol_da_year}</c:param>
				</c:if>
				<c:if test="${!empty param.dtregol_a_day}">
					<c:param name="dtregol_a_day">${param.dtregol_a_day}</c:param>
				</c:if>
				<c:if test="${!empty param.dtregol_a_month}">
					<c:param name="dtregol_a_month">${param.dtregol_a_month}</c:param>
				</c:if>
				<c:if test="${!empty param.dtregol_a_year}">
					<c:param name="dtregol_a_year">${param.dtregol_a_year}</c:param>
				</c:if>
				<c:if test="${!empty param.keyQuadratura}">
					<c:param name="keyQuadratura">${param.keyQuadratura}</c:param>
				</c:if>
				<c:if test="${!empty param.nomeFileTxt}">
					<c:param name="nomeFileTxt">${param.nomeFileTxt}</c:param>
				</c:if>
				<c:if test="${!empty param.dtChiusuraFlusso_da_day}">
					<c:param name="dtChiusuraFlusso_da_day">${param.dtChiusuraFlusso_da_day}</c:param>
				</c:if>
				<c:if test="${!empty param.dtChiusuraFlusso_da_month}">
					<c:param name="dtChiusuraFlusso_da_month">${param.dtChiusuraFlusso_da_month}</c:param>
				</c:if>
				<c:if test="${!empty param.dtChiusuraFlusso_da_year}">
					<c:param name="dtChiusuraFlusso_da_year">${param.dtChiusuraFlusso_da_year}</c:param>
				</c:if>
				<c:if test="${!empty param.dtChiusuraFlusso_a_day}">
					<c:param name="dtChiusuraFlusso_a_day">${param.dtChiusuraFlusso_a_day}</c:param>
				</c:if>
				<c:if test="${!empty param.dtChiusuraFlusso_a_month}">
					<c:param name="dtChiusuraFlusso_a_month">${param.dtChiusuraFlusso_a_month}</c:param>
				</c:if>
				<c:if test="${!empty param.dtChiusuraFlusso_a_year}">
					<c:param name="dtChiusuraFlusso_a_year">${param.dtChiusuraFlusso_a_year}</c:param>
				</c:if>
				<c:if test="${!empty param.dtMakeFlusso_da_day}">
					<c:param name="dtMakeFlusso_da_day">${param.dtMakeFlusso_da_day}</c:param>
				</c:if>
				<c:if test="${!empty param.dtMakeFlusso_da_month}">
					<c:param name="dtMakeFlusso_da_month">${param.dtMakeFlusso_da_month}</c:param>
				</c:if>
				<c:if test="${!empty param.dtMakeFlusso_da_year}">
					<c:param name="dtMakeFlusso_da_year">${param.dtMakeFlusso_da_year}</c:param>
				</c:if>
				<c:if test="${!empty param.dtMakeFlusso_a_day}">
					<c:param name="dtMakeFlusso_a_day">${param.dtMakeFlusso_a_day}</c:param>
				</c:if>
				<c:if test="${!empty param.dtMakeFlusso_a_month}">
					<c:param name="dtMakeFlusso_a_month">${param.dtMakeFlusso_a_month}</c:param>
				</c:if>
				<c:if test="${!empty param.dtMakeFlusso_a_year}">
					<c:param name="dtMakeFlusso_a_year">${param.dtMakeFlusso_a_year}</c:param>
				</c:if>
				<c:if test="${!empty param.tipologiaFlusso}">
					<c:param name="tipologiaFlusso">${param.tipologiaFlusso}</c:param>
				</c:if>
				<c:if test="${!empty param.scartateFlusso}">
					<c:param name="scartateFlusso">${param.scartateFlusso}</c:param>
				</c:if>
				<c:if test="${!empty param.impPagamento_da}">
					<c:param name="impPagamento_da">${param.impPagamento_da}</c:param>
				</c:if>
				<c:if test="${!empty param.impPagamento_a}">
					<c:param name="impPagamento_a">${param.impPagamento_a}</c:param>
				</c:if>
				<c:if test="${!empty ext}">
					<c:param name="ext">${ext}</c:param>
				</c:if>
			</c:url>
		</s:action>
		
		<s:dgcolumn index="12" label="#" css="text_align_right" asc="POSA" desc="POSD"></s:dgcolumn>
		<s:dgcolumn index="2" label="I.U.V." asc="IUVA" desc="IUVD"></s:dgcolumn>
		<s:dgcolumn index="7" label="I.U.R." asc="IURA" desc="IURD"></s:dgcolumn>
		<s:dgcolumn index="3" label="Esito" css="text_align_center" asc="ESIA" desc="ESID"></s:dgcolumn>
		<s:dgcolumn index="8" label="Data P." format="dd/MM/yyyy" css="text_align_center" asc="GDPA" desc="GDPD"></s:dgcolumn>
		<%-- <s:dgcolumn index="6" label="Spese" css="text_align_right" format="#,##0.00" asc="ISPA" desc="ISPD"></s:dgcolumn>  --%>
		<s:dgcolumn index="4" label="Importo" css="text_align_right" format="#,##0.00" asc="IMPA" desc="IMPD"></s:dgcolumn>
		<s:dgcolumn index="5" label="Importo R.P.T." css="text_align_right" format="#,##0.00" asc="IM2A" desc="IM2D"></s:dgcolumn>
		<s:dgcolumn index="9" label="Cod.Qua." asc="CODA" css="text_align_center" desc="CODD"></s:dgcolumn>
		<s:dgcolumn index="10" label="Messagio Quadratura" asc="MESA" desc="MESD"></s:dgcolumn>
		<s:dgcolumn label="Quadrato" css="textcenter">
			<s:if right="{11}" control="eq" left="Y">
				<s:then>
					<s:image src="../applications/templates/monitoraggio/img/circle_green.png"
					 alt="I.U.V. Quadrato" height="16" width="16" cssclass="hlStyle" />
				</s:then>
			</s:if>
			<s:if right="{11}" control="eq" left="N">
				<s:then>
					<s:image src="../applications/templates/monitoraggio/img/circle_red.png"
					 alt="I.U.V. NON Quadrato" height="16" width="16" cssclass="hlStyle" />
				</s:then>
			</s:if>
		</s:dgcolumn>
		<%--
		<s:dgcolumn label="Azioni" css="white_space_nowrap">
			<s:hyperlink
				href="<>.do?idquad={1}&iuv={2}"
				cssclass="blacklink hlStyle"
				imagesrc="../applications/templates/riconciliazionemt/img/details.png" text=""
				alt="..." />
		</s:dgcolumn>
		--%>	
	</s:datagrid>
</c:if>