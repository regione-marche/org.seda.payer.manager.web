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
<script src="../applications/templates/riconciliazionemt/js/popup.js"
	type="text/javascript"></script>

<script type="text/javascript" >
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}

	//function popup(error){
	//	alert(error);
	//}
	
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
	<c:if test="${!empty param.tx_societa}">
		<c:param name="tx_societa">${param.tx_societa}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_provincia}">
		<c:param name="tx_provincia">${param.tx_provincia}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_UtenteEnte}">
		<c:param name="tx_UtenteEnte">${param.tx_UtenteEnte}</c:param>
	</c:if>
	<c:if test="${!empty param.sospRegolarizzati}">
		<c:param name="sospRegolarizzati">${param.sospRegolarizzati}</c:param>
	</c:if>
	<c:if test="${!empty param.numDocumento}">
		<c:param name="numDocumento">${param.numDocumento}</c:param>
	</c:if>
	<c:if test="${!empty param.provenienza}">
		<c:param name="provenienza">${param.provenienza}</c:param>
	</c:if>
	<c:if test="${!empty param.sospRendicontati}">
		<c:param name="sospRendicontati">${param.sospRendicontati}</c:param>
	</c:if>
	<c:if test="${!empty param.psp}">
		<c:param name="psp">${param.psp}</c:param>
	</c:if>
	<c:if test="${!empty param.dtGiornale_da_day}">
		<c:param name="dtGiornale_da_day">${param.dtGiornale_da_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtGiornale_da_month}">
		<c:param name="dtGiornale_da_month">${param.dtGiornale_da_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtGiornale_da_year}">
		<c:param name="dtGiornale_da_year">${param.dtGiornale_da_year}</c:param>
	</c:if>
	<c:if test="${!empty param.dtGiornale_a_day}">
		<c:param name="dtGiornale_a_day">${param.dtGiornale_a_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtGiornale_a_month}">
		<c:param name="dtGiornale_a_month">${param.dtGiornale_a_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtGiornale_a_year}">
		<c:param name="dtGiornale_a_year">${param.dtGiornale_a_year}</c:param>
	</c:if>
		<c:if test="${!empty param.dtMovimento_da_day}">
		<c:param name="dtMovimento_da_day">${param.dtMovimento_da_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtMovimento_da_month}">
		<c:param name="dtMovimento_da_month">${param.dtMovimento_da_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtMovimento_da_year}">
		<c:param name="dtMovimento_da_year">${param.dtMovimento_da_year}</c:param>
	</c:if>
	<c:if test="${!empty param.dtMovimento_a_day}">
		<c:param name="dtMovimento_a_day">${param.dtMovimento_a_day}</c:param>
	</c:if>
	<c:if test="${!empty param.dtMovimento_a_month}">
		<c:param name="dtMovimento_a_month">${param.dtMovimento_a_month}</c:param>
	</c:if>
	<c:if test="${!empty param.dtMovimento_a_year}">
		<c:param name="dtMovimento_a_year">${param.dtMovimento_a_year}</c:param>
	</c:if>
	<c:if test="${!empty param.chiaveRen}">
		<c:param name="chiaveRen">${param.chiaveRen}</c:param>
	</c:if>
	<c:if test="${!empty param.idcassa}">
		<c:param name="idcassa">${param.idcassa}</c:param>
	</c:if>
	<c:if test="${!empty ext}">
		<c:param name="ext">${ext}</c:param>
	</c:if>

</c:url>

<s:div name="divSelezione1" cssclass="divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="dettaglioGiornaleCassaForm"
			action="dettaglioGiornaleCassa.do" method="post"
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
				
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
				Dettaglio Flusso
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="">
					<s:div name="divElement1" cssclass="divTableDettaglioOnTop">	
						<s:table cssclass="seda-ui-datagrid" border="1" cellspacing="0" cellpadding="3">
							<s:thead>
								<s:tr cssclass="seda-ui-datagridrowdispari">
									<s:th cssclass="seda-ui-datagridcell">Ente</s:th>
									<s:th cssclass="seda-ui-datagridcell">Provenienza</s:th>
									<s:th cssclass="seda-ui-datagridcell">Data Flusso</s:th>
									<s:th cssclass="seda-ui-datagridcell">Identificativo Flusso</s:th>
									<s:th cssclass="seda-ui-datagridcell">Sospesi Regolarizzati</s:th>
								</s:tr>
							</s:thead>
							<s:tbody>
								<s:tr cssclass="seda-ui-datagridrowpari">
									<s:td cssclass="seda-ui-datagridcell">
										${gdc_ente}
									</s:td>
									<s:td cssclass="seda-ui-datagridcell">
										${gdc_prov}
									</s:td>
									<s:td cssclass="seda-ui-datagridcell text_align_center">
										${gdc_data}
									</s:td>
									<s:td cssclass="seda-ui-datagridcell">
										${gdc_idflusso}
									</s:td>
									<s:td cssclass="seda-ui-datagridcell text_align_center">
										${gdc_srend}
									</s:td>
								</s:tr>
							</s:tbody>
						</s:table>
					</s:div>
				</s:div>
				
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					
					<s:div name="divElement14" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="ignore;numberIT" maxlenght="20"
							cssclass="textareaman" cssclasslabel="label85 bold textright"
							bmodify="true" name="numDocumento" label="Numero Movimento:"
							text="${numDocumento}" />
					</s:div>
					
					<s:div name="divElement12" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement12a" cssclass="labelData">
							<s:label name="label_data_creazione" 
								cssclass="seda-ui-label label78 bold textright"
								text="Importo" />
						</s:div>

						<s:div name="divElement12b" cssclass="floatleft">
							<s:div name="divDtGiornaleDA" cssclass="divDataDa">
								<s:textbox validator="ignore;numberIT" maxlenght="20"
								label="Da:" cssclasslabel="labelsmall" cssclass="dateman"
								bmodify="true" name="importoDa" text="${importoDa}"/>
								<input type="hidden" id="importo_da_hidden" value="" />
							</s:div>

							<s:div name="divDtGiornaleA" cssclass="divDataA">
								<s:textbox validator="ignore;numberIT" maxlenght="20"
								label="A:" cssclasslabel="labelsmall" cssclass="dateman"
								bmodify="true" name="importoA" text="${importoA}"/>
								<input type="hidden" id="importo_a_hidden" value="" />
							</s:div>
						</s:div>

					</s:div>
				</s:div>


				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">

					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="sospRendicontati" cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright" disable="false"
							label="Movimenti Regolarizzati:" valueselected="${sospRendicontati}">
							<s:ddloption value="" text="Tutti i Movimenti" />
							<s:ddloption value="Y" text="Si" />
							<s:ddloption value="N" text="No" />
							<s:ddloption value="P" text="Provvisorio" />
						</s:dropdownlist>
					</s:div>
					
				</s:div>

				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="squadratura" cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright" disable="false"
							label="Squadratura:" valueselected="${squadratura}">
							<s:ddloption value="" text="Tutte le squadrature" />
							<s:ddloption value="Y" text="Si" />
							<s:ddloption value="N" text="No" />
						</s:dropdownlist>
					</s:div>

				</s:div>
				
			</s:div>
			
			<s:div name="divCentered0" cssclass="divRicBottoni">

				<s:button id="tx_button_cerca" type="submit" text="Cerca" onclick=""
					cssclass="btnStyle" />
				<s:button id="tx_button_reset" type="submit" text="Reset" onclick=""
					cssclass="btnStyle" />
				<%--
				<s:hyperlink name="hpAnnulla" cssclass="hlToBtnStyle" href="ricercaGiornaliCassa.do${formParameters}&indietro=Y" text="Indietro" />
				--%>
				<s:hyperlink name="hpAnnulla" cssclass="hlToBtnStyle" href="ritorna.do?vista=ricercagiornalicassa" text="Indietro" />
							
			</s:div>
		</s:form>
		
	</s:div>

</s:div>

<c:if test="${!empty lista_movimenti_di_cassa}">
	<fmt:setLocale value="it_IT" />
	
	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Elenco Movimenti
	</s:div>
	
	<s:datagrid cachedrowset="lista_movimenti_di_cassa"
		action="dettaglioGiornaleCassa.do" border="1" usexml="true"
		rowperpage="${applicationScope.rowsPerPage}" viewstate="true">
		
		<s:dgcolumn index="4" label="Numero Movimento" asc="DOCA" desc="DOCD"></s:dgcolumn>
		<s:dgcolumn index="6" label="Importo" css="text_align_right" format="#,##0.00" asc="IMPA" desc="IMPD"></s:dgcolumn>
		<s:dgcolumn index="7" label="Squadratura" css="text_align_right" format="#,##0.00" asc="SQUA" desc="SQUD"></s:dgcolumn>
		<s:dgcolumn index="8" label="Regolarizzato" asc="RENA" desc="REND"></s:dgcolumn>
		<s:dgcolumn index="12" label="Data Regolarizzazione" format="dd/MM/yyyy" asc="DRNA" desc="DRND"></s:dgcolumn>
		<s:dgcolumn label="Azioni" css="white_space_nowrap">
		
			<s:hyperlink
				href="dettaglioMovimentoCassa.do?idmdc={1}"
				cssclass="blacklink hlStyle"
				imagesrc="../applications/templates/riconciliazionemt/img/details.png" text=""
				alt="Dettaglio Movimento" />
			<s:if right="{11}" control="ne" left="">
				<s:then>
					<s:hyperlink
						href=""
						cssclass="blacklink hlStyle"
						imagesrc="../applications/templates/shared/img/listaDati.png" text=""
						alt="Errore in fase di abbinamento del movimento"
						onclick="showAnomalia('{11}')"/>
				</s:then>
				<s:else>
				</s:else>
			</s:if>
			<s:if right="{8}" control="ne" left="Si">
				<s:then>
					<s:hyperlink
						href="associazioniTransazioniMovimentoCassa.do?numeroDocumento={1}&page=dettGior"
						cssclass="blacklink hlStyle"
						imagesrc="../applications/templates/riconciliazionemt/img/dettagliMovimenti.png" text=""
						alt="Associazioni Transazioni a Movimento" />
					<s:hyperlink
						href="associazioniFlussiMovimentoCassa.do?numeroDocumento={1}&page=dettGior"
						cssclass="blacklink hlStyle"
						imagesrc="../applications/templates/riconciliazionemt/img/Y_ftp.png" text=""
						alt="Associazione Flussi a Movimento" />
				</s:then>
				<s:else>
				</s:else>
			</s:if>
			<s:if right="{8}" control="eq" left="Provvisorio" operator="and"
				secondright="${sessionScope.j_user_bean.associazioniDefinitiveRiconciliazionemtEnabled}" secondcontrol="eq" secondleft="true">
				<s:then>
					<s:hyperlink
						href="regolarizzaSospeso.do?idmdc={1}&conto={2}&stato={3}&documento={4}&cliente={5}&importo={6}&squa={7}&page=dettGior"
						onclick=""
						cssclass="blacklink hlStyle"
						imagesrc="../applications/templates/riconciliazionemt/img/quadraturaManuale.png" text=""
						alt="Regolarizza Movimento" />
				</s:then>
				<s:else></s:else>
			</s:if>
		
		</s:dgcolumn>
  		
	</s:datagrid>
</c:if>

<!-- POPUP -->
	<s:div cssclass="seda-ui-div" name="popupDettaglio">
		<p id="error"/>
		
		<s:div name="chiudiPopupDettaglio">
			<input type="image" src="../applications/templates/shared/img/close_gray2.png" alt="Chiudi" title="Chiudi" style="width:25px;height:25px"> 
			<!--  <input type="button" value="Chiudi" src="" />-->
		</s:div>
	</s:div>
	<s:div cssclass="seda-ui-div" name="backgroundPopup"> &nbsp; </s:div>	