<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<m:view_state id="monitoraggionn" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>

<script src="../applications/templates/monitoraggio/js/popup.js"
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
		$("#tx_data_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_data_da_day_id").val(dateText.substr(0,2));
												$("#tx_data_da_month_id").val(dateText.substr(3,2));
												$("#tx_data_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_da_day_id",
				                            "tx_data_da_month_id",
				                            "tx_data_da_year_id",
				                            "tx_data_da_hidden");
			}
		});
		$("#tx_data_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_data_a_day_id").val(dateText.substr(0,2));
												$("#tx_data_a_month_id").val(dateText.substr(3,2));
												$("#tx_data_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_a_day_id",
				                            "tx_data_a_month_id",
				                            "tx_data_a_year_id",
				                            "tx_data_a_hidden");
			}
		});
	});
</script>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="monitoraggioNnForm" action="monitoraggionn.do" method="post" 
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
						
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
				Ricerca Nodo Nazionale
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
						<s:textbox validator="ignore;accept=^(?:(?!').)*$;maxlength=64" bmodify="true"
							maxlenght="64" 
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							name="txtIdTransazione" label="Id Transazione:"
							message="[accept=Id Transazione: Inserire un Id Transazione valido]"
							text="${txtIdTransazione}" />
					</s:div>

					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_StatoInvioRT"
							disable="false" cssclass="tbddlMax floatleft"
							label="Stato Invio RT:" cssclasslabel="label85 bold textright"
							valueselected="${tx_StatoInvioRT}">
							<s:ddloption text="Tutti gli Stati" value="ALL" />
							<s:ddloption text="Protocollato" value="PROTOCOLLATO"/> 
							<s:ddloption text="Non Protocollato" value="NON PROTOCOLLATO"/>
						</s:dropdownlist>					
					</s:div>

					<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="maxlength=35" bmodify="true" maxlenght="35" cssclass="textareaman" cssclasslabel="label85 bold textright"
							name="tx_NumeroProtocolloRT" label="Num. Prot. Invio RT:"
							text="${tx_NumeroProtocolloRT}" />
					</s:div>
					
				</s:div>
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="maxlength=35" bmodify="true" maxlenght="35" cssclass="textareaman" cssclasslabel="label85 bold textright"
							name="codiceIUV" label="Codice I.U.V.:"
							text="${codiceIUV}" />
					</s:div>
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_EsitoInvioRT"
							disable="false" cssclass="tbddlMax floatleft"
							label="Esito Invio RT:" cssclasslabel="label85 bold textright"
							valueselected="${tx_EsitoInvioRT}">
							<s:ddloption text="Tutti gli Esiti" value="ALL" />
							<s:ddloption text="OK" value="OK"/> 
							<s:ddloption text="KO" value="KO"/>
						</s:dropdownlist>					
					</s:div>

<!-- 				
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:textbox validator="maxlength=128" bmodify="true"
							maxlenght="128" 
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							name="txtNumeroDocumento" label="N.Documento:"
							text="${txtNumeroDocumento}" />
					</s:div>
-->				
				</s:div>
				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
								
					<s:div name="divElement18" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement18a" cssclass="labelData">
							<s:label name="label_data" cssclass="seda-ui-label label85 bold textright" text="Data Aggiornamento:" />
						</s:div>
						
						<s:div name="divElement18b" cssclass="floatleft">
							<s:div name="divDataDa" cssclass="divDataDa">
								<s:date label="Da:" prefix="tx_data_da" yearbegin="${ddlDateAnnoDa}"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_data_da}"
									cssclasslabel="labelsmall"
									cssclass="dateman">
								</s:date>
								<input type="hidden" id="tx_data_da_hidden" value="" />
							</s:div>
							
							<s:div name="divDataA" cssclass="divDataA">
								<s:date label="A:" prefix="tx_data_a" yearbegin="${ddlDateAnnoDa}"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_data_a}"
									cssclasslabel="labelsmall"
									cssclass="dateman" >
								</s:date>
								<input type="hidden" id="tx_data_a_hidden" value="" />
							</s:div>
						</s:div>
							
					</s:div>

				</s:div>
				
			</s:div>
			
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				
				<s:button id="tx_button_cerca" type="submit" text="Cerca" cssclass="btnStyle" onclick="" />
				<s:button id="tx_button_reset" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
										  
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
			</s:div>
			
		</s:form>	
	</s:div>
	
</s:div>


<c:if test="${!empty listaMIP}">
	<fmt:setLocale value="it_IT" />
	
	<s:div name="divTableTitle1" cssclass="divTableTitle bold">
		Elenco Transazioni Nodo
	</s:div>
	
	<s:div name="divRisultati" cssclass="divRisultati">
		<s:datagrid cachedrowset="listaMIP"
			action="monitoraggionn.do?vista=monitoraggionn" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}" viewstate="">
			
			<s:dgcolumn index="16" label="Ente" asc="ENTA" desc="ENTD" />
			<s:dgcolumn label="Id Transazione" asc="TRAA" desc="TRAD" >
				<s:if left="{17}" control="eq" right="Y">
					<s:then>
						<s:hyperlink
						href="../monitoraggio/monitoraggioTransazioni.do?tx_codice_transazione={1}&tx_button_cerca=1&jump=1"
						text="{1}" cssclass="blacklink"
						alt="Dettaglio Transazione {1}" />
					</s:then>
					<s:else>{1}</s:else>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn index="7" label="Codice I.U.V." asc="OPEA" desc="OPED" />
			<s:dgcolumn index="18" label="Esito Invio RT" />
			<s:dgcolumn index="19" label="Stato Prot." />
			<s:dgcolumn index="20" label="Nm. Prot." />
			<s:dgcolumn index="15" label="Data/Ora Aggiornamento" asc="AGGA" desc="AGGD" format="dd/MM/yyyy HH:mm:ss"/>
						
			<s:dgcolumn label="&nbsp;Azioni&nbsp;">
				<s:hyperlink
					href="dettaglioNodo.do?chiave_transazione_hidden={1}"
					cssclass="hlStyle" 
					imagesrc="../applications/templates/monitoraggionn/img/details.png" text="" 
					alt="Dettaglio Nodo" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>

		


