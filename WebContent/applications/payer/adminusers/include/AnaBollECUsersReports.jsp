<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="anabollecusersreports" encodeAttributes="true" />


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
		$("#tx_periodo_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_periodo_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_periodo_da_day_id").val(dateText.substr(0,2));
												$("#tx_periodo_da_month_id").val(dateText.substr(3,2));
												$("#tx_periodo_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_periodo_da_day_id",
				                            "tx_periodo_da_month_id",
				                            "tx_periodo_da_year_id",
				                            "tx_periodo_da_hidden");
			}
		});
		$("#tx_periodo_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_periodo_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_periodo_a_day_id").val(dateText.substr(0,2));
												$("#tx_periodo_a_month_id").val(dateText.substr(3,2));
												$("#tx_periodo_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_periodo_a_day_id",
				                            "tx_periodo_a_month_id",
				                            "tx_periodo_a_year_id",
				                            "tx_periodo_a_hidden");
			}
		});
	});

</script>
<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="form_selezione" action="anabollecusersreports.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">STATISTICHE ANAGRAFICHE BORSELLINO ED ESTRATTO CONTO
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="ddlSocietaUtenteEnte"  
								label="Societ&agrave;/Utente/Ente: " showrequired="true"
										cssclasslabel="label85 bold textright" validator="ignore;"
										cssclass="floatleft" disable="${codop == 'edit'}"
										onchange="setFired();this.form.submit();"
										   cachedrowset="listaSocietaUtenteEnte" usexml="true" 
										   valueselected="${ddlSocietaUtenteEnte}">
	   							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
								<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}"/>
							</s:dropdownlist>
							<c:if test="${codop == 'add'}">
								<noscript>
									<s:button id="tx_button_societa_changed" 
										onclick="" text="" 
										validate="false"
										type="submit" cssclass="btnimgStyle"  title="Aggiorna" />
								</noscript>
							</c:if>									
						</s:div>					
					</s:div>	
					
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_denom"
							label="Denominaz.:" maxlenght="100" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_denom}" />
					</s:div>
					
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						
						
						<s:div name="divElement11a" cssclass="labelData">
							<s:label name="label_data_pag" cssclass="seda-ui-label label85 bold textright"
								text="Data" />
						</s:div>
						<s:div name="divElement11b" cssclass="floatleft">
							<s:div name="divDataDa_car" cssclass="divDataDa">
								<s:date label="Da:" prefix="tx_periodo_da" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_periodo_da}"></s:date>
								<input type="hidden" id="tx_periodo_da_hidden" value="" />
							</s:div>
							<s:div name="divDataA_car" cssclass="divDataA">
								<s:date label="A:" prefix="tx_periodo_a" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_periodo_a}"></s:date>
								<input type="hidden" id="tx_periodo_a_hidden" value="" />
							</s:div>
						</s:div>						
					</s:div>	
				</s:div>
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_codfisc"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							label="Codice Fiscale:" maxlenght="16"
							text="${tx_codfisc}" />
					</s:div>
				</s:div>
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement101" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_servizio" disable="false"
							cssclass="tbddlMax floatleft" label="Servizio:"
							cssclasslabel="label85 bold textright"
							valueselected="${tx_servizio}">
							<s:ddloption text="Tutti" value="" />
							<s:ddloption text="Estratto Conto" value="EC" />
							<s:ddloption text="Borsellino" value="BR" />
						</s:dropdownlist>
					</s:div>		
				</s:div>
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" text="Reset" validate="false" type="submit" cssclass="btnStyle" />
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

<c:if test="${!empty lista_anaboll}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO ACCESSI
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_anaboll"
			action="anabollecusersreports.do" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			<s:dgcolumn label="Data Log" asc="DATA" desc="DATD" format="dd/MM/yyyy" >
				<s:if right="{5}" control="eq" left="01-01-1900" >
					<s:then>
						&nbsp;
					</s:then>
					<s:else>
						{5}
					</s:else>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn index="3" label="Codice Fiscale" asc="CFSA" desc="CFSD"></s:dgcolumn>
			<s:dgcolumn index="2" label="Denominazione" asc="DENA" desc="DEND"></s:dgcolumn>
			<s:dgcolumn index="4" label="Servizio" asc="APPA" desc="APPD"></s:dgcolumn>
			<s:dgcolumn index="1" label="Num. Accessi" asc="TACA" desc="TACD"></s:dgcolumn>
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
			<s:datagrid viewstate="true" cachedrowset="anaboll"  
				action="anabollecusersreports.do" border="1" usexml="true" >
				<s:dgcolumn index="5" label="Estatto Conto inviati" />
				<s:dgcolumn index="4" label="Estatto Conto registrati"/>
				<s:dgcolumn index="7" label="Borsellino inviati" />
				<s:dgcolumn index="6" label="Borsellino registrati" />
				<s:dgcolumn index="8" label="Entrambi registrati" />
			</s:datagrid>
		</s:div>
	</s:div>
</c:if>





