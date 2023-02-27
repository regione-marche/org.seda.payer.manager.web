<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="log_search" encodeAttributes="true" />

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
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#inizioSessioneDA_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#inizioSessioneDA_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
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
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
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
	<s:form name="form_selezione" action="logAccessi.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Log Accessi
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
						<noscript>
							<s:button id="tx_button_societa_changed" 
								disable="${ddlSocietaDisabled}" onclick="" text="" 
								type="submit" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
						</noscript>
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
						<noscript>
							<s:button id="tx_button_provincia_changed"
								disable="${ddlProvinciaDisabled}" onclick="" text=""
								type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false"
								 />
						</noscript>
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
					
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_username"
							label="User id.:" maxlenght="50" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_username}" />
					</s:div>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_userprofile" disable="false"
							cssclass="tbddl"
							cssclasslabel="label85 bold textright"
							label="Tipologia Utente:" valueselected="${tx_userprofile}"	>
							<s:ddloption text="Tutti i Tipi" value="" />
							<s:ddloption text="AMMI - Amm.C.S.I." value="AMMI" />
							<s:ddloption text="AMSO - Amm.Societ&agrave;" value="AMSO" />
							<s:ddloption text="AMUT - Amm.Utente" value="AMUT" />
							<s:ddloption text="AMEN - Amm.Ente" value="AMEN" />
							<s:ddloption text="PYCO - Contribuente" value="PYCO" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="indirizzo_ip"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							label="Indirizzo IP:" maxlenght="15" validator="ignore;accept=^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
							message="[accept=Indirizzo IP: ${msg_configurazione_indirizzo_ip}]"
							text="${indirizzo_ip}" />
					</s:div>
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_canale_pagamento" disable="false"
							cssclass="tbddl"
							cssclasslabel="label85 bold textright"
							label="Canale:"
							valueselected="${tx_canale_pagamento}"
							cachedrowset="listaCanaliPagamento" usexml="true">
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					
					<s:div name="divElement77" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_applicazione" disable="false"
							cssclass="tbddlMax floatleft" label="Servizio:"
							cssclasslabel="label85 bold textright"
							cachedrowset="listaApplicazioniPayer" usexml="true"
							valueselected="${tx_applicazione}">
							<s:ddloption text="Tutte le tipologie" value="" />
							<s:ddloption text="{1}" value="{2}" />
						</s:dropdownlist>
					</s:div>
					
					<s:div name="divElement11" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement11a" cssclass="labelData">
							<s:label name="label_data_pag" cssclass="seda-ui-label label85 bold textright"
								text="Inizio Sessione" />
						</s:div>
						<s:div name="divElement11b" cssclass="floatleft">
							<s:div name="divDataDa_pag" cssclass="divDataDa">
								<s:date label="Da:" prefix="inizioSessioneDA" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${inizioSessioneDA}"></s:date>
								<input type="hidden" id="inizioSessioneDA_hidden" value="" />
							</s:div>
							<s:div name="divDataA_pag" cssclass="divDataA">
								<s:date label="A:" prefix="inizioSessioneA" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${inizioSessioneA}"></s:date>
								<input type="hidden" id="inizioSessioneA_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
				</s:div>
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
			</s:div>
	</s:form>

	</s:div>
	
</s:div>

<c:if test="${!empty listaLog}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill">
			Risultati ricerca
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="listaLog"
			action="logAccessi.do?vista=log_search" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

			<s:dgcolumn index="1" label="Ente" asc="ENTE_A" desc="ENTE_D"></s:dgcolumn>
			<s:dgcolumn index="2" label="UserId" asc="USER_A" desc="USER_D"></s:dgcolumn>
			<s:dgcolumn index="3" label="Tipologia User" asc="TIPOUSER_A" desc="TIPOUSER_D"></s:dgcolumn>
			<s:dgcolumn index="5" label="IP" asc="IP_A" desc="IP_D"></s:dgcolumn>
			<s:dgcolumn index="4" label="Canale" asc="CANALE_A" desc="CANALE_D"></s:dgcolumn>
			<s:dgcolumn index="9" label="Servizi visitati" asc="APPL_A" desc="APPL_D"></s:dgcolumn>
			<s:dgcolumn index="6" label="Inizio sessione" asc="INIZIO_A" desc="INIZIO_D"></s:dgcolumn>
			<s:dgcolumn label="Fine sessione" asc="FINE_A" desc="FINE_D" >
				<s:if right="{8}" control="eq" left="00:00:00" >
					<s:then>&nbsp;</s:then>
					<s:else>{7}</s:else>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn label="Durata connessione" asc="DURATA_A" desc="DURATA_D">
				<s:if right="{8}" control="eq" left="00:00:00" >
					<s:then>&nbsp;</s:then>
					<s:else>{8}</s:else>
				</s:if>
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
	<s:div name="div_riepilogo" cssclass="div_align_center divRiepilogo">
		<s:div name="divRicercaLeft1" cssclass="divRicMetadatiLeft">
		&nbsp;
		<br/>
		&nbsp;
		</s:div>

		<s:div name="divRicercaCenter1" cssclass="divRicMetadatiCenter">
			<s:div name="divRicercaFillName1" cssclass="divRicercaFill">
					Riepilogo Statistico
			</s:div>
			<s:datagrid viewstate="true" cachedrowset="riepilogoLog"  
				action="logAccessi.do" border="1" usexml="true" >
				<s:dgcolumn index="1" label="Totale Numero Accessi" />
				<s:dgcolumn index="2" label="Totale Durata Connessioni"/>
			</s:datagrid>
		</s:div>
	</s:div>
</c:if>







