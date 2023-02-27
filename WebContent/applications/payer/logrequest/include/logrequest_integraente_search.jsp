<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="logrequest_search" encodeAttributes="true" />

<style>
	#dataChiamataTimeDA
	{
	  width: 83px;
	  height: 25px;
	  font-family: Arial;
	  font-size: 105%;
	  margin-bottom: 0px;
	  padding-left: 2px;
	  border-left-width: 1px;
	  border-right-width: 1px;
	  margin-left: 3px;
	  /*margin-right: 3px; */
	  border-color: #767676;
	  border-top-width: 1px;
	  border-bottom-width: 1px;
	}
	
	#dataChiamataTimeA
	{
	  width: 83px;
	  height: 25px;
	  font-family: Arial;
	  font-size: 105%;
	  margin-bottom: 0px;
	  padding-left: 2px;
	  border-left-width: 1px;
	  border-right-width: 1px;
	  margin-left: 3px;
	  /*margin-right: 3px; */
	  border-color: #767676;
	  border-top-width: 1px;
	  border-bottom-width: 1px;
	}
	
	#dataInserimentoTime
	{
	  width: 83px;
	  height: 25px;
	  font-family: Arial;
	  font-size: 105%;
	  margin-bottom: 0px;
	  padding-left: 2px;
	  border-left-width: 1px;
	  border-right-width: 1px;
	  margin-left: 3px;
	  /*margin-right: 3px; */
	  border-color: #767676;
	  border-top-width: 1px;
	  border-bottom-width: 1px;
	}
</style>

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
	var annoDa = ${logrequestAnnoDa};
	var meseDa = ${logrequestMeseDa} - 1;
	var giornoDa = ${logrequestGiornoDa};
	var annoA = ${logrequestAnnoA};
	var meseA = ${logrequestMeseA} - 1;
	var giornoA = ${logrequestGiornoA};
	var today = new Date();

	$(function() {
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#dataChiamataDA_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#dataChiamataDA_hidden").datepicker({
			minDate: new Date(annoDa, meseDa, giornoDa),
			maxDate: new Date(annoA, meseA, giornoA),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#dataChiamataDA_day_id").val(dateText.substr(0,2));
												$("#dataChiamataDA_month_id").val(dateText.substr(3,2));
												$("#dataChiamataDA_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("dataChiamataDA_day_id",
				                            "dataChiamataDA_month_id",
				                            "dataChiamataDA_year_id",
				                            "dataChiamataDA_hidden");
			}
		});
		$("#dataChiamataA_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#dataChiamataA_hidden").datepicker({
			minDate: new Date(annoDa, meseDa, giornoDa),
			maxDate: new Date(annoA, meseA, giornoA),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#dataChiamataA_day_id").val(dateText.substr(0,2));
												$("#dataChiamataA_month_id").val(dateText.substr(3,2));
												$("#dataChiamataA_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("dataChiamataA_day_id",
				                            "dataChiamataA_month_id",
				                            "dataChiamataA_year_id",
				                            "dataChiamataA_hidden");
			}
		});

		// $("#dataInserimento_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		// $("#dataInserimento_hidden").datepicker({
		// 	minDate: new Date(annoDa, meseDa, giornoDa),
		// 	maxDate: new Date(annoA, meseA, giornoA),
		// 	yearRange: annoDa + ":" + annoA,
		// 	showOn: "button",
		// 	buttonImage: "../applications/templates/shared/img/calendar.gif",
		// 	buttonImageOnly: true,
		// 	onSelect: function(dateText, inst) {$("#dataInserimento_day_id").val(dateText.substr(0,2));
		// 										$("#dataInserimento_month_id").val(dateText.substr(3,2));
		// 										$("#dataInserimento_year_id").val(dateText.substr(6,4));
		// 										},
		// 	beforeShow: function(input, inst) {
		// 		//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	    //         updateValoreDatePickerFromDdl("dataInserimento_day_id",
		// 		                            "dataInserimento_month_id",
		// 		                            "dataInserimento_year_id",
		// 		                            "dataInserimento_hidden");
		// 	}
		// });
	});

</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="form_selezione" action="logRequestIntegraente.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:textbox name="ext" label="ext" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="hRowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rowsPerPage}" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="hPageNumber" label="pageNumberRic" bmodify="true" text="${pageNumber}" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="hOrder" label="orderRic" bmodify="true" text="${order}" cssclass="display_none" cssclasslabel="display_none" />
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Log Request INTEGRAENTE
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">


				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				
				
					<!-- <s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:dropdownlist name="tx_societa" disable="${ddlSocietaDisabled}"
							cssclass="tbddl tbddl3col floatleft" label="Societ&agrave;:"
							cssclasslabel="label85 bold floatleft textright"
							cachedrowset="listaSocieta" usexml="true"
							onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
							valueselected="${tx_societa}">
							<s:ddloption text="Tutte le Societ&agrave;" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_societa_changed" 
								disable="${ddlSocietaDisabled}" onclick="" text="" 
								type="submit" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
						</noscript>
					</s:div> -->
					
		
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: "
									cssclasslabel="label168 bold textright floatleft" validator="ignore;"
									cssclass="seda-ui-ddl tbddlMax780 floatleft" disable="false"
									onchange="setFired();this.form.submit();"
									   cachedrowset="listaSocietaUtenteEnte" usexml="true" 
									   valueselected="${ddlSocietaUtenteEnte}">
   							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
							<s:ddloption value="{1}|{2}|{3}|{4}" text="{5} / {6} / {4}"/>
						</s:dropdownlist>
					</s:div>	
						
					<!-- <s:div name="divElement2" cssclass="divRicMetadatiTopRight">
						<s:dropdownlist name="tx_CodiceEnte"
							disable="false" cssclass="tbddl tbddl3col floatleft"
							label="Cod. Ente:" cssclasslabel="label85 bold textright"
							cachedrowset="listaUtentiEnti" usexml="true"
							onchange="setFiredButton('tx_button_socEntUt_changed');this.form.submit();"
							valueselected="${tx_CodiceEnte}">
							<s:ddloption text="Tutti gli Enti" value="" />
<!-- 							<s:ddloption text="{2}" value="{1}" /> -->
						</s:dropdownlist>
					</s:div>  -->
					
					<!-- <s:div name="divElementL3" cssclass="divRicMetadatiTopRight">
						<s:textbox bmodify="true" name="tx_cuteCute"
							label="Cod. utente:" maxlenght="50" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_cuteCute}" />
					</s:div> -->

				</s:div>
				
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					
					<s:div name="divElementL1" cssclass="divRicMetadatiSingleRow">
						
						<s:dropdownlist name="tx_tipoChiamata" disable="false"
							cssclass="tbddl tbddl3col floatleft" label="Tipo chiamata: "
							cssclasslabel="label87 bold floatleft textright"
							valueselected="${tx_tipoChiamata}">
							<s:ddloption text="..." value="" />
							
							<s:ddloption value="recuperaDatiBollettino" text="recuperaDatiBollettino"/>
							<s:ddloption value="recuperaListDoc" text="recuperaListDoc"/>
							<s:ddloption value="recuperaDettDoc" text="recuperaDettDoc"/>
							<s:ddloption value="recuperaDettDocBollettino" text="recuperaDettDocBollettino"/>
							<s:ddloption value="recuperaDettDocAttiContenzioso" text="recuperaDettDocAttiContenzioso"/>
							<s:ddloption value="recuperaListPag" text="recuperaListPag"/>
							<s:ddloption value="recuperaListEnti" text="recuperaListEnti"/>
							<s:ddloption value="recuperaListEntiEC" text="recuperaListEntiEC"/>
							<s:ddloption value="notificaPagamento" text="notificaPagamento"/>
						</s:dropdownlist>
						
					</s:div>
					
<%-- 					<s:div name="divElementL1" cssclass="divRicMetadatiSingleRow"> --%>
<%-- 						<s:textbox bmodify="true" name="tx_tipoChiamata" --%>
<%-- 							label="Tipologia:" maxlenght="250"  --%>
<%-- 							cssclasslabel="bold label85 textright" --%>
<%-- 							cssclass="textareaman textareaman3col"  --%>
<%-- 							text="${tx_tipoChiamata}" /> --%>
<%-- 					</s:div> --%>
				
				    <!-- <s:div name="divElementL2" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_idDominio"
							label="ID dominio:" maxlenght="40" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_idDominio}" />
					</s:div>  -->
			
					<s:div name="divElementL3" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElementL3a" cssclass="floatleft">
							<div id="divDataDa_pag" class="seda-ui-div divDataDa" style="margin-left: 30px;">
								<s:date label="Da:" prefix="dataChiamataDA" yearbegin="${logrequestAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${logrequestAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${dataChiamataDA}"></s:date>
								<input type="hidden" id="dataChiamataDA_hidden" value="" />
								<input type="time" id="dataChiamataTimeDA" name="dataChiamataTimeDA"
								       min="00:00:00" max="23:59:59" step="1" value="${dataChiamataTimeDA}">								
							</div>
							<div id="divDataA_pag" class="seda-ui-div divDataA" style="margin-left: 30px; padding-right: 0px;">
								<s:date label="A:" prefix="dataChiamataA" yearbegin="${logrequestAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${logrequestAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${dataChiamataA}"></s:date>
								<input type="hidden" id="dataChiamataA_hidden" value="" />
								<input type="time" id="dataChiamataTimeA" name="dataChiamataTimeA"
								       min="00:00:00" max="23:59:59" step="1" value="${dataChiamataTimeA}">								
							</div>
						</s:div>
					</s:div>
					
						
				</s:div>
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
			
			
					<s:div name="divElementC1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_bollettino"
							label="Num. bol.: " maxlenght="40" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_bollettino}" />
					</s:div>	
					
					<s:div name="divElementC4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_esito"
							label="Esito:" maxlenght="40" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_esito}" />
					</s:div>	
					
					
					<s:div name="divElementC5" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_error" disable="false"
							cssclass="tbddl tbddl3col floatleft" label="Mess. errore:"
							cssclasslabel="label85 bold floatleft textright"
							valueselected="${tx_error}">
							<s:ddloption text="..." value="" />
							<s:ddloption text="Si" value="1" />
							<s:ddloption text="No" value="2" />
						</s:dropdownlist>
						
						<%-- <s:textbox bmodify="true" name="tx_error"
							label="Errore:" maxlenght="40" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_error}" /> --%>
					</s:div>	
	
				</s:div>
				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElementR1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_codfis"
							label="Cod. fis.:" maxlenght="50" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_codfis}" />
					</s:div>
                    
                    <!-- <div id="divDataInserimento_pag" class="seda-ui-div divDataInserimento">
                        <s:date label="Ins :" prefix="dataInserimento" yearbegin="${logrequestAnnoDa}"
                            cssclasslabel="label65 textright"
                            cssclass="dateman"
                            yearend="${logrequestAnnoA}" locale="IT-it" descriptivemonth="false"
                            separator="/" calendar="${dataInserimento}"></s:date>
                        <input type="hidden" id="dataInserimento_hidden" value="" />
                        <input type="time" id="dataInserimentoTime" name="dataInserimentoTime"
                                min="00:00:00" max="23:59:59" step="1" value="${dataInserimentoTime}">								
                    </div> -->

					<s:div name="divElementR2" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_operatore"
							label="Operatore: " maxlenght="50" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_operatore}" />
					</s:div>
					
					
					
					<!-- <s:div name="divElementR3" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_xmlIn"
							label="XML in:" maxlenght="250" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_xmlIn}" />
					</s:div>
					<s:div name="divElementR4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_xmlOut"
							label="XML out:" maxlenght="250" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_xmlOut}" />
					</s:div> -->
						
				</s:div>
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden" bmodify="true" text="" cssclass="rend_display_none" cssclasslabel="rend_display_none" />
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<c:if test="${!empty listaLogWin && ext != '1'}">
					<s:button id="tx_button_cerca_exp" type="submit" text="Espandi" onclick="" cssclass="btnStyle" />
				</c:if>
				<c:if test="${!empty listaLogWin && ext == '1'}">
					<s:button id="tx_button_cerca_exp" type="submit" text="Contrai" onclick="" cssclass="btnStyle" />
				</c:if>
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
			</s:div>
	</s:form>

	</s:div>
	
</s:div>

<c:if test="${!empty listaLogWin}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill">
			Risultati Ricerca
	</s:div>
	<s:div name="div_datagrid">
	    <s:datagrid viewstate="" cachedrowset="listaLogWin" action="logRequestIntegraente.do" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
		<s:action>
			<c:url value="logRequestIntegraente.do">
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
				<c:if test="${!empty param.tx_CodiceEnte}">
					<c:param name="tx_CodiceEnte">${param.tx_CodiceEnte}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_cuteCute}">
					<c:param name="tx_cuteCute">${param.tx_cuteCute}</c:param>
				</c:if>
				<c:if test="${!empty param.chiamataDA_day}">
					<c:param name="dataChiamataDA_day">${chiamataDA_day}</c:param>
				</c:if>
				<c:if test="${!empty param.chiamataDA_month}">
					<c:param name="dataChiamataDA_month">${param.chiamataDA_month}</c:param>
				</c:if>
				<c:if test="${!empty param.chiamataDA_year}">
					<c:param name="dataChiamataDA_year">${param.chiamataDA_year}</c:param>
				</c:if>
				<c:if test="${!empty param.chiamataTimeDA}">
					<c:param name="dataChiamataTimeDA">${param.chiamataTimeDA}</c:param>
				</c:if>
				<c:if test="${!empty param.chiamataA_day}">
					<c:param name="dataChiamataA_day">${param.chiamataA_day}</c:param>
				</c:if>
				<c:if test="${!empty param.chiamataA_month}">
					<c:param name="dataChiamataA_month">${param.chiamataA_month}</c:param>
				</c:if>
				<c:if test="${!empty param.chiamataA_year}">
					<c:param name="dataChiamataA_year">${param.chiamataA_year}</c:param>
				</c:if>
				<c:if test="${!empty param.chiamataTimeA}">
					<c:param name="dataChiamataTimeA">${param.chiamataTimeA}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_xmlIn}">
					<c:param name="tx_xmlIn">${param.tx_xmlIn}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_tipoChiamata}">
					<c:param name="tx_tipoChiamata">${param.tx_tipoChiamata}</c:param>
				</c:if>
			
				<c:if test="${!empty param.tx_bollettino}">
					<c:param name="tx_bollettino">${param.tx_bollettino}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_codfis}">
					<c:param name="tx_codfis">${param.tx_codfis}</c:param>
				</c:if>
			
			
				<c:if test="${!empty param.tx_idDominio}">
					<c:param name="tx_idDominio">${param.tx_idDominio}</c:param>
				</c:if>
			
				<c:if test="${!empty param.tx_error}">
					<c:param name="tx_error">${param.tx_error}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_operatore}">
					<c:param name="tx_operatore">${param.tx_operatore}</c:param>
				</c:if>
				
				<c:if test="${!empty ext}">
					<c:param name="ext">${ext}</c:param>
				</c:if>

			</c:url>
		</s:action>
		    <s:dgcolumn label="#" >
				<s:hyperlink
					href="dettaglioIntegraenteLogRequest.do?idLogRequest={1}&tagSuffissoTablogRequest={20}" 
					onclick=""
					cssclass="blacklink hlStyle"
					imagesrc="../applications/templates/shared/img/Info.png" text=""
					alt="Dettaglio LogRequest {1}" 
			   	/>
	        </s:dgcolumn>
	        
			<s:dgcolumn index="2" label="Tipo Chiamata" asc="TIPOCHIAMATA_A" desc="TIPOCHIAMATA_D"/>
			
			<s:dgcolumn index="9" label="Data inizio chiamata" asc="DATA_INIZIO_CHIAMATA_A" desc="DATA_INIZIO_CHIAMATA_D" format="dd/MM/yyyy HH:mm:ss"/>
			
			<s:ifdatagrid left="${ext}" control="eq" right="1">
				<s:thendatagrid>                 
					
					<s:dgcolumn index="10" label="Data fine chiamata" asc="DATA_FINE_CHIAMATA_A" desc="DATA_FINE_CHIAMATA_D" format="dd/MM/yyyy HH:mm:ss"/>
					<s:dgcolumn index="3" label="Codice Utente" asc="CUTE_A" desc="CUTE_D"/>
					
				</s:thendatagrid>
			</s:ifdatagrid>
			
			
			<s:dgcolumn index="7" label="Codice Fiscale" asc="CFIS_A" desc="CFIS_D"/>
			
			<s:dgcolumn index="6" label="Bollettino" asc="BOLLETTINO_A" desc="BOLLETTINO_D"/>
			
			<s:dgcolumn index="19" label="Operatore" asc="OPERATORE_A" desc="OPERATORE_D"/>
			
			<s:ifdatagrid left="${ext}" control="eq" right="1">
				<s:thendatagrid>       		
					<s:dgcolumn label="Xml IN" asc="XMLIN_A" desc="XMLIN_D">
						<s:label name="xmlIn" text="{12}" title="{11}"/>
					</s:dgcolumn>
				
					<s:dgcolumn label="Xml Out" asc="XMLOUT_A" desc="XMLOUT_D">
						<s:label name="xmlOut" text="{14}" title="{13}"/>
					</s:dgcolumn>
				</s:thendatagrid>
			</s:ifdatagrid>
			
 			<s:dgcolumn label="Mes. Err." asc="ERROR_A" desc="ERROR_D"> 
				<s:label name="error" text="{16}" title="{17}"/>
			</s:dgcolumn>
			
			<s:ifdatagrid left="${ext}" control="eq" right="1">
				<s:thendatagrid>                 
					<s:dgcolumn index="15" label="Esito" />	
				</s:thendatagrid>
			</s:ifdatagrid>
			
			
			<s:ifdatagrid left="${ext}" control="eq" right="1">
				<s:thendatagrid>
					<s:dgcolumn index="5" label="Codice ente" ></s:dgcolumn>                  
					
				</s:thendatagrid>
			</s:ifdatagrid>
		</s:datagrid>
	</s:div>
</c:if>







