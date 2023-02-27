<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="logrequest_search" encodeAttributes="true" />

<style>
	#inizioSessioneTimeDA
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
	
	#inizioSessioneTimeA
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
		$("#inizioSessioneDA_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#inizioSessioneDA_hidden").datepicker({
			minDate: new Date(annoDa, meseDa, giornoDa),
			maxDate: new Date(annoA, meseA, giornoA),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#inizioSessioneDA_day_id").val(dateText.substr(0,2));
												$("#inizioSessioneDA_month_id").val(dateText.substr(3,2));
												$("#inizioSessioneDA_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("inizioSessioneDA_day_id",
				                            "inizioSessioneDA_month_id",
				                            "inizioSessioneDA_year_id",
				                            "inizioSessioneDA_hidden");
			}
		});
		$("#inizioSessioneA_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#inizioSessioneA_hidden").datepicker({
			minDate: new Date(annoDa, meseDa, giornoDa),
			maxDate: new Date(annoA, meseA, giornoA),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#inizioSessioneA_day_id").val(dateText.substr(0,2));
												$("#inizioSessioneA_month_id").val(dateText.substr(3,2));
												$("#inizioSessioneA_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("inizioSessioneA_day_id",
				                            "inizioSessioneA_month_id",
				                            "inizioSessioneA_year_id",
				                            "inizioSessioneA_hidden");
			}
		});
	});

</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="form_selezione" action="logRequest.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:textbox name="ext" label="ext" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="hRowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rowsPerPage}" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="hPageNumber" label="pageNumberRic" bmodify="true" text="${pageNumber}" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="hOrder" label="orderRic" bmodify="true" text="${order}" cssclass="display_none" cssclasslabel="display_none" />
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Log Request
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
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
					</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
						<s:dropdownlist name="tx_provincia"
							disable="${ddlProvinciaDisabled}" cssclass="tbddl tbddl3col floatleft"
							label="Provincia:"
							cssclasslabel="label65 bold floatleft textright"
							cachedrowset="listaProvince" usexml="true"
							onchange="setFiredButton('tx_button_provincia_changed');this.form.submit();"
							valueselected="${tx_provincia}">
							<s:ddloption text="Tutte le Province" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_provincia_changed"
								disable="${ddlProvinciaDisabled}" onclick="" text=""
								type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false"
								 />
						</noscript>
					</s:div>
					
					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:dropdownlist name="tx_UtenteEnte"
							disable="${ddlUtenteEnteDisabled}" cssclass="tbddl tbddl3col floatleft"
							label="Ente:" cssclasslabel="label65 bold textright"
							cachedrowset="listaUtentiEnti" usexml="true"
							valueselected="${tx_UtenteEnte}">
							<s:ddloption text="Tutti gli Enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					
					<s:div name="divElementL1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_username"
							label="User id.:" maxlenght="50" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_username}" />
					</s:div>
					<s:div name="divElementL2" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_userprofile" disable="false"
							cssclass="tbddl tbddl3col"
							cssclasslabel="label85 bold textright"
							label="Tipo utente:" valueselected="${tx_userprofile}"	>
							<s:ddloption text="Tutti i Tipi" value="" />
							<s:ddloption text="AMMI - Amm.C.S.I." value="AMMI" />
							<s:ddloption text="AMSO - Amm.Societ&agrave;" value="AMSO" />
							<s:ddloption text="AMUT - Amm.Utente" value="AMUT" />
							<s:ddloption text="AMEN - Amm.Ente" value="AMEN" />
							<s:ddloption text="PROF - Amm.Scelta Profilo " value="PROF" />
							<s:ddloption text="PYCO - Contribuente" value="PYCO" />
						</s:dropdownlist>
					</s:div>
					<s:div name="divElementL3" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElementL3a" cssclass="floatleft">
							<div id="divDataDa_pag" class="seda-ui-div divDataDa" style="margin-left: 30px;">
								<s:date label="Da:" prefix="inizioSessioneDA" yearbegin="${logrequestAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${logrequestAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${inizioSessioneDA}"></s:date>
								<input type="hidden" id="inizioSessioneDA_hidden" value="" />
								<input type="time" id="inizioSessioneTimeDA" name="inizioSessioneTimeDA"
								       min="00:00:00" max="23:59:59" step="1" value="${inizioSessioneTimeDA}">								
							</div>
							<div id="divDataA_pag" class="seda-ui-div divDataA" style="margin-left: 30px; padding-right: 0px;">
								<s:date label="A:" prefix="inizioSessioneA" yearbegin="${logrequestAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${logrequestAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${inizioSessioneA}"></s:date>
								<input type="hidden" id="inizioSessioneA_hidden" value="" />
								<input type="time" id="inizioSessioneTimeA" name="inizioSessioneTimeA"
								       min="00:00:00" max="23:59:59" step="1" value="${inizioSessioneTimeA}">								
							</div>
						</s:div>
					</s:div>
					<s:div name="divElementL4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_azione"
							label="Azione:" maxlenght="250" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_azione}" />
					</s:div>
					<s:div name="divElementL5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_request"
							label="Request:" maxlenght="250" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_request}" />
					</s:div>
					<s:div name="divElementL6" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_sessione"
							label="Session:" maxlenght="40" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_sessione}" />
					</s:div>	
						
				</s:div>
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElemenC1" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_metodo" disable="false"
							cssclass="tbddl tbddl3col"
							cssclasslabel="label85 bold textright"
							label="Metodo:" valueselected="${tx_metodo}"	>
							<s:ddloption text="Tutte" value="" />
							<s:ddloption text="GET" value="GET" />
							<s:ddloption text="POST" value="POST" />
						</s:dropdownlist>
					</s:div>
					<s:div name="divElementC2" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="indirizzo_ip"
							cssclass="textareaman textareaman3col"
							cssclasslabel="label85 bold textright"
							label="Indirizzo IP:" maxlenght="15" validator="ignore;accept=^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
							message="[accept=Indirizzo IP: ${msg_configurazione_indirizzo_ip}]"
							text="${indirizzo_ip}" />
					</s:div>
					<s:div name="divElementC3" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_app" disable="false"
							cssclass="tbddl tbddl3col"
							cssclasslabel="label85 bold textright"
							label="App:" valueselected="${tx_app}"	>
							<s:ddloption text="Tutte" value="" />
							<s:ddloption text="manager" value="manager" />
							<s:ddloption text="pagonet" value="pagonet" />
						</s:dropdownlist>
					</s:div>
					<s:div name="divElementC4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_codfis"
							label="Cod. fis.:" maxlenght="50" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_codfis}" />
					</s:div>
					<s:div name="divElementC5" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_provres" disable="false"
						    cssclass="tbddl tbddl3col"
							cssclasslabel="label85 bold textright"
							label="Prov. req.:"
							valueselected="${tx_provres}"
							cachedrowset="listprovincia" usexml="true"
							onchange="setFiredButton('tx_button_provincia_ben_changed');this.form.submit();">
							<s:ddloption text="Tutte le Province" value="" />
							<s:ddloption text="{1}" value="{2}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_provincia_ben_changed"
								disable="false" onclick="" text=""
								type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false"
								 />
						</noscript>
					</s:div>
					<s:div name="divElementC6" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_keytra"
							label="Transazione:" maxlenght="64" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_keytra}" />
					</s:div>
					
					<s:div name="divElementC7" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_error" disable="false"
							cssclass="tbddl tbddl3col floatleft" label="Presenza err.:"
							cssclasslabel="label85 bold floatleft textright"
							valueselected="${tx_error}">
							<s:ddloption text="..." value="" />
							<s:ddloption text="Si" value="1" />
							<s:ddloption text="No" value="2" />
						</s:dropdownlist>
						
					</s:div>	
					
						
				</s:div>
				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElementR1" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_canale_pagamento" disable="false"
							cssclass="tbddl tbddl3col"
							cssclasslabel="label85 bold textright"
							label="Canale:"
							valueselected="${tx_canale_pagamento}"
							cachedrowset="listaCanaliPagamento" usexml="true">
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
					<s:div name="divElementR2" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_applicazione" disable="false"
							cssclass="tbddl tbddl3col floatleft" label="Servizio:"
							cssclasslabel="label85 bold textright"
							cachedrowset="listaApplicazioniPayer" usexml="true"
							valueselected="${tx_applicazione}">
							<s:ddloption text="Tutte le tipologie" value="" />
							<s:ddloption text="{1}" value="{2}" />
						</s:dropdownlist>
					</s:div>
					<s:div name="divElementR3" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_numdoc"
							label="Num. doc.:" maxlenght="50" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_numdoc}" />
					</s:div>
					<s:div name="divElementR4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_numbol"
							label="Num. bol.:" maxlenght="50" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_numbol}" />
					</s:div>
					<s:div name="divElementR5" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_comures" disable="false"
							cssclass="tbddl tbddl3col"
							cssclasslabel="label85 bold textright"
							label="Comune Req.:"
							valueselected="${tx_comures}"
							cachedrowset="ricercacomunelistcomuni" usexml="true">
							<s:ddloption text="Tutti i Comuni" value="" />
							<s:ddloption text="{1}" value="{1}|{3}" />
						</s:dropdownlist>
					</s:div>
					<s:div name="divElementR6" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_numiuv"
							label="Num. IUV:" maxlenght="35" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col" 
							text="${tx_numiuv}" />
					</s:div>
					
					<s:div name="divElementR7" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_ope"
							label="Cod. portale:" maxlenght="40" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman textareaman3col " 
							text="${tx_ope}" />
					</s:div>	
				</s:div>
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden" bmodify="true" text="" cssclass="rend_display_none" cssclasslabel="rend_display_none" />
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<c:if test="${!empty listaLogRequest && ext != '1'}">
					<s:button id="tx_button_cerca_exp" type="submit" text="Espandi" onclick="" cssclass="btnStyle" />
				</c:if>
				<c:if test="${!empty listaLogRequest && ext == '1'}">
					<s:button id="tx_button_cerca_exp" type="submit" text="Contrai" onclick="" cssclass="btnStyle" />
				</c:if>
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
			</s:div>
	</s:form>

	</s:div>
	
</s:div>

<c:if test="${!empty listaLogRequest}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill">
			Risultati Ricerca
	</s:div>
	<s:div name="div_datagrid">
	    <s:datagrid viewstate="" cachedrowset="listaLogRequest" action="logRequest.do" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
		<s:action>
			<c:url value="logRequest.do">
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
				<c:if test="${!empty param.tx_username}">
					<c:param name="tx_username">${param.tx_username}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_userprofile}">
					<c:param name="tx_userprofile">${param.tx_userprofile}</c:param>
				</c:if>
				<c:if test="${!empty param.inizioSessioneDA_day}">
					<c:param name="inizioSessioneDA_day">${inizioSessioneDA_day}</c:param>
				</c:if>
				<c:if test="${!empty param.inizioSessioneDA_month}">
					<c:param name="inizioSessioneDA_month">${param.inizioSessioneDA_month}</c:param>
				</c:if>
				<c:if test="${!empty param.inizioSessioneDA_year}">
					<c:param name="inizioSessioneDA_year">${param.inizioSessioneDA_year}</c:param>
				</c:if>
				<c:if test="${!empty param.inizioSessioneTimeDA}">
					<c:param name="inizioSessioneTimeDA">${param.inizioSessioneTimeDA}</c:param>
				</c:if>
				<c:if test="${!empty param.inizioSessioneA_day}">
					<c:param name="inizioSessioneA_day">${param.inizioSessioneA_day}</c:param>
				</c:if>
				<c:if test="${!empty param.inizioSessioneA_month}">
					<c:param name="inizioSessioneA_month">${param.inizioSessioneA_month}</c:param>
				</c:if>
				<c:if test="${!empty param.inizioSessioneA_year}">
					<c:param name="inizioSessioneA_year">${param.inizioSessioneA_year}</c:param>
				</c:if>
				<c:if test="${!empty param.inizioSessioneTimeA}">
					<c:param name="inizioSessioneTimeA">${param.inizioSessioneTimeA}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_azione}">
					<c:param name="tx_azione">${param.tx_azione}</c:param>
				</c:if>
			
				<c:if test="${!empty param.tx_request}">
					<c:param name="tx_request">${param.tx_request}</c:param>
				</c:if>
				<%-- inizio LP PG21X007 - 20211230 --%>	
				<c:if test="${!empty param.tx_sessione}">
					<c:param name="tx_sessione">${param.tx_sessione}</c:param>
				</c:if>
				<%-- fine LP PG21X007 - 20211230 --%>	
				<c:if test="${!empty param.tx_metodo}">
					<c:param name="tx_metodo">${param.tx_metodo}</c:param>
				</c:if>
			
				<c:if test="${!empty param.indirizzo_ip}">
					<c:param name="indirizzo_ip">${param.indirizzo_ip}</c:param>
				</c:if>
			
				<c:if test="${!empty param.tx_app}">
					<c:param name="tx_app">${param.tx_app}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_codfis}">
					<c:param name="tx_codfis">${param.tx_codfis}</c:param>
				</c:if>
			
				<c:if test="${!empty param.tx_provres}">
					<c:param name="tx_provres">${tx_provres}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_keytra}">
					<c:param name="tx_keytra">${param.tx_keytra}</c:param>
				</c:if>
				
				<c:if test="${!empty param.tx_error}">
					<c:param name="tx_error">${param.tx_error}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_ope}">
					<c:param name="tx_ope">${param.tx_ope}</c:param>
				</c:if>
				
				
				<c:if test="${!empty param.tx_canale_pagamento}">
					<c:param name="tx_canale_pagamento">${param.tx_canale_pagamento}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_applicazione}">
					<c:param name="tx_applicazione">${param.tx_applicazione}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_numdoc}">
					<c:param name="tx_numdoc">${param.tx_numdoc}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_numbol}">
					<c:param name="tx_numbol">${param.tx_numbol}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_comures}">
					<c:param name="tx_comures">${param.tx_comures}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_numiuv}">
					<c:param name="tx_numiuv">${param.tx_numiuv}</c:param>
				</c:if>
				<c:if test="${!empty ext}">
					<c:param name="ext">${ext}</c:param>
				</c:if>

			</c:url>
		</s:action>
		    <s:dgcolumn label="#" >
				<s:hyperlink
					href="dettaglioLogRequest.do?idLogRequest={1}&tagSuffissoTablogRequest={30}" 
					onclick=""
					cssclass="blacklink hlStyle"
					imagesrc="../applications/templates/shared/img/Info.png" text=""
					alt="Dettaglio LogRequest {1}" 
			   	/>
	        </s:dgcolumn>
			<s:dgcolumn index="9" label="Data" asc="DATA_A" desc="DATA_D" format="dd/MM/yyyy HH:mm:ss"/>
			<s:dgcolumn index="4" label="Metodo" asc="TIPA" desc="TIPD"></s:dgcolumn>
			<%-- inizio LP PG21X007 - 20211230 --%>	
			<s:dgcolumn index="3" label="Session" asc="IDSESS_A" desc="IDSESS_D"></s:dgcolumn>
			<%-- fine LP PG21X007 - 20211230 --%>
			
			<s:dgcolumn index="10" label="Codice Portale" asc="OPE_A" desc="OPE_D"></s:dgcolumn>
			
			<s:dgcolumn label="Error" asc="ERROR_A" desc="ERROR_D">
				<s:label name="error" text="{32}" title="{31}"/>
			</s:dgcolumn>
			<s:dgcolumn label="Request" asc="QUERY_A" desc="QUERY_D">
				<s:label name="request" text="{33}" title="{7}"/>
			</s:dgcolumn>
			<%-- 
			<s:dgcolumn index="33" label="Request" asc="QUERY_A" desc="QUERY_D"></s:dgcolumn>
			<s:dgcolumn index="32" label="Error" asc="ERROR_A" desc="ERROR_D"></s:dgcolumn>
			<s:dgcolumn index="33" title="{7}" label="Request" asc="QUERY_A" desc="QUERY_D"></s:dgcolumn> --%>
			<%-- inizio LP PG21X007 - 20211230 --%>			
			<%--
			<s:dgcolumn index="5" label="App" asc="APP_A" desc="APP_D"></s:dgcolumn>
			<s:dgcolumn index="6" label="Azione" asc="AZIONE_A" desc="AZIONE_D"></s:dgcolumn>
			<s:dgcolumn index="13" label="Sezione" asc="SEZ_A" desc="SEZ_D"></s:dgcolumn>
			<s:dgcolumn index="2" label="IP" asc="IP_A" desc="IP_D"></s:dgcolumn>
			--%>
			<%-- fine LP PG21X007 - 20211230 --%>
			<s:ifdatagrid left="${ext}" control="eq" right="1">
				<s:thendatagrid>
				    <%-- inizio LP PG21X007 - 20211230 --%>
					<s:dgcolumn index="5" label="App" asc="APP_A" desc="APP_D"></s:dgcolumn>                  <%-- sito: manager\web--%>
					<s:dgcolumn index="6" label="Azione" asc="AZIONE_A" desc="AZIONE_D"></s:dgcolumn>         <%-- azione senza .do --%>
					<s:dgcolumn index="13" label="Sezione" asc="SEZ_A" desc="SEZ_D"></s:dgcolumn>             <%-- sezione applicativa: adminuser, log, logrequest etc.. --%>
					<s:dgcolumn index="2" label="IP" asc="IP_A" desc="IP_D"></s:dgcolumn>
					<%-- fine LP PG21X007 - 20211230 --%>	
					<s:dgcolumn index="8" label="Url" asc="REQUEST_A" desc="REQUEST_D"></s:dgcolumn>
					<s:dgcolumn index="14" label="Canale" asc="CANALE_A" desc="CANALE_D"></s:dgcolumn>
					<s:dgcolumn index="16" label="Tipo User" asc="USERPROF_A" desc="USERPROF_D"></s:dgcolumn>
					<s:dgcolumn index="15" label="User" asc="USER_A" desc="USER_D"></s:dgcolumn>
					<s:dgcolumn index="17" label="Cod. Societ&agrave;" asc="CODSOC_A" desc="CODSOC_D"></s:dgcolumn>
					<s:dgcolumn index="20" label="Descrizione Ente" asc="DESCENTE_A" desc="DESCENTE_D"></s:dgcolumn>
					<s:dgcolumn index="22" label="CodFis" asc="CODFIS_A" desc="CODFIS_D"></s:dgcolumn>
					<s:dgcolumn index="27" label="Prov." asc="REQPRV_A" desc="REREQPRV_D"></s:dgcolumn>
					<s:dgcolumn index="26" label="Comune" asc="REQCOM_A" desc="REQCOM_D"></s:dgcolumn>
					<s:dgcolumn index="23" label="Num. Documento" asc="NUMDOC_A" desc="NUMDOC_D"></s:dgcolumn>
					<s:dgcolumn index="24" label="Num. Bollettino" asc="NUMBOL_A" desc="NUMBOL_D"></s:dgcolumn>
					<s:dgcolumn index="28" label="Transazione" asc="TRANSA_A" desc="TRANSA_D"></s:dgcolumn>
					<s:dgcolumn index="29" label="IUV" asc="NUMIUV_A" desc="NUMIUV_D"></s:dgcolumn>
				</s:thendatagrid>
			</s:ifdatagrid>
		</s:datagrid>
	</s:div>
</c:if>







