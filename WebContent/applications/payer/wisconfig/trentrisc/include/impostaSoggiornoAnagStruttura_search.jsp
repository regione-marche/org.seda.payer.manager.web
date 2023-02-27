<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="impostaSoggiornoAnagStruttura_search" encodeAttributes="true" />

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


	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();
	
	$(function() {
		$.datepicker.setDefaults($.datepicker.regional["it"]);
		$("#tx_data_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_da_hidden").datepicker( {
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn : "button",
			buttonImage : "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly : true,
			onSelect : function(dateText, inst) {
				$("#tx_data_da_day_id").val(dateText.substr(0, 2));
				$("#tx_data_da_month_id").val(dateText.substr(3, 2));
				$("#tx_data_da_year_id").val(dateText.substr(6, 4));
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
		$("#tx_data_a_hidden").datepicker( {
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn : "button",
			buttonImage : "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly : true,
			onSelect : function(dateText, inst) {
				$("#tx_data_a_day_id").val(dateText.substr(0, 2));
				$("#tx_data_a_month_id").val(dateText.substr(3, 2));
				$("#tx_data_a_year_id").val(dateText.substr(6, 4));
			},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_a_day_id",
				                            "tx_data_a_month_id",
				                            "tx_data_a_year_id",
				                            "tx_data_a_hidden");
			}
		});


		$("#tx_data_obbl_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_obbl_da_hidden").datepicker( {
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn : "button",
			buttonImage : "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly : true,
			onSelect : function(dateText, inst) {
				$("#tx_data_obbl_da_day_id").val(dateText.substr(0, 2));
				$("#tx_data_obbl_da_month_id").val(dateText.substr(3, 2));
				$("#tx_data_obbl_da_year_id").val(dateText.substr(6, 4));
			},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_obbl_da_day_id",
				                            "tx_data_obbl_da_month_id",
				                            "tx_data_obbl_da_year_id",
				                            "tx_data_obbl_da_hidden");
			}
		});
		$("#tx_data_obbl_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_obbl_a_hidden").datepicker( {
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn : "button",
			buttonImage : "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly : true,
			onSelect : function(dateText, inst) {
				$("#tx_data_obbl_a_day_id").val(dateText.substr(0, 2));
				$("#tx_data_obbl_a_month_id").val(dateText.substr(3, 2));
				$("#tx_data_obbl_a_year_id").val(dateText.substr(6, 4));
			},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_obbl_a_day_id",
				                            "tx_data_obbl_a_month_id",
				                            "tx_data_obbl_a_year_id",
				                            "tx_data_obbl_a_hidden");
			}
		});


	});
</script>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="search_form" action="ImpostaSoggiornoAnagStrutturaSearch.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA ANAGRAFICHE STRUTTURE
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				
					<s:div name="divElement1" cssclass="divRicMetadatiLeft">
						<s:textbox bmodify="true" name="descrizioneComune"
								label="Comune:" maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${descrizioneComune}" message="[accept=Comune: ${msg_configurazione_descrizione_regex}]"/>
					</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:textbox bmodify="true" name="numeroAutorizzazione"
								label="Num. Autor.: " maxlenght="100" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore"
								text="${numeroAutorizzazione}" message="[accept=Numero Autorizzazione: ${msg_configurazione_descrizione_regex}]"/>
					</s:div>
					
					<s:div name="divElement3" cssclass="divRicMetadatiRight">
						<s:textbox bmodify="true" name="codiceFiscale"
								label="Cod.Fisc./P.IVA:" maxlenght="16" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore"
								text="${codiceFiscale}" message="[accept=Codice Fiscale: ${msg_configurazione_descrizione_regex}]"/>
					</s:div>
				</s:div>

				<s:div name="divRicMetadatiTop" cssclass="">
					<s:div name="divLeft" cssclass="divRicMetadatiLeft">
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="descrizioneInsegna"
								label="Insegna:" maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_ragionesociale_regex}"
								text="${descrizioneInsegna}" message="[accept=Insegna: ${msg_configurazione_ragionesociale_regex}]"/>
						</s:div>	
					</s:div>
					
					<s:div name="divCenter" cssclass="divRicMetadatiCenter">
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="strutturaRicettiva"
									disable="false" 
									label="Tipol. Strut.:"
									cssclasslabel="label85 bold textright"
									cssclass="tbddl"
									cachedrowset="listStrutture" usexml="true"
									validator="ignore" showrequired="true"
									valueselected="${strutturaRicettiva}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption  value="{1}" text="{2}"/>
								</s:dropdownlist>
						</s:div>
					</s:div>
					
					<s:div name="divRight" cssclass="divRicMetadatiRight">
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							
							
						</s:div>
					</s:div>
					
				</s:div>
				<s:div name="divRicMetadatiTop" cssclass="">
				
					<s:div name="divElement1" cssclass="divRicMetadatiLeft">
						<s:dropdownlist name="flagSubentro"
									disable="false" 
									label="Subentro:"
									cssclasslabel="label85 bold textright"
									cssclass="tbddl"
									cachedrowset="" usexml="true"
									validator="ignore" showrequired="true"
									valueselected="${flagSubentro}">
									<s:ddloption text="Tutti" value="" />
									<s:ddloption  value="S" text="Solo subentri"/>
									<s:ddloption  value="N" text="Non subentri"/>
								</s:dropdownlist>
					</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:div name="divElement18a" cssclass="labelData">
							<s:label name="label_data"
								cssclass="seda-ui-label label85 bold textright"
								text="Data Validità Struttura" />
						</s:div>

						<s:div name="divElement18b" cssclass="floatleft">
							<s:div name="divDataDa" cssclass="divDataDa">
								<s:date label="Da:" prefix="tx_data_da"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
									locale="IT-it" descriptivemonth="false" separator="/"
									calendar="${tx_data_da}" cssclasslabel="labelsmall"
									cssclass="dateman">
								</s:date>
								<input type="hidden" id="tx_data_da_hidden" value="" />
							</s:div>

							<s:div name="divDataA" cssclass="divDataA">
								<s:date label="A:" prefix="tx_data_a"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
									locale="IT-it" descriptivemonth="false" separator="/"
									calendar="${tx_data_a}" cssclasslabel="labelsmall"
									cssclass="dateman">
								</s:date>
								<input type="hidden" id="tx_data_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
					
					<s:div name="divElement3" cssclass="divRicMetadatiRight">
						<s:div name="divElement18a" cssclass="labelData">
							<s:label name="label_data"
								cssclass="seda-ui-label label85 bold textright"
								text="Data Obbligo Comunicazione" />
						</s:div>

						<s:div name="divElement18b" cssclass="floatleft">
							<s:div name="divDataDa" cssclass="divDataDa">
								<s:date label="Da:" prefix="tx_data_obbl_da"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
									locale="IT-it" descriptivemonth="false" separator="/"
									calendar="${tx_data_obbl_da}" cssclasslabel="labelsmall"
									cssclass="dateman">
								</s:date>
								<input type="hidden" id="tx_data_obbl_da_hidden" value="" />
							</s:div>

							<s:div name="divDataA" cssclass="divDataA">
								<s:date label="A:" prefix="tx_data_obbl_a"
									yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
									locale="IT-it" descriptivemonth="false" separator="/"
									calendar="${tx_data_obbl_a}" cssclasslabel="labelsmall"
									cssclass="dateman">
								</s:date>
								<input type="hidden" id="tx_data_obbl_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
				</s:div>
					
			</s:div>	
				<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_nuovo" validate="false"  onclick="" text="Nuovo" type="submit" cssclass="btnStyle" />
				<c:if test="${!empty listaConfig}">
					<s:button id="tx_button_download" validate="false" onclick="" text="Download" type="submit" cssclass="btnStyle" />
				</c:if>
			</s:div>
	</s:form>

	</s:div>
	
</s:div>

<c:if test="${!empty listaConfig}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO ANAGRAFICHE STRUTTURE
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="listaConfig"  
			action="ImpostaSoggiornoAnagStrutturaSearch.do?vista=impostaSoggiornoAnagStruttura_search" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

		<!-- DESCRIZIONE COMUNE -->
			<s:dgcolumn label="Comune" index="3" />
			
		<!-- NUMERO AUTORIZZAZIONE -->
			<s:dgcolumn label="Nr. Autorizzazione" index="4" />

		<!-- DESCRIZIONE CODICE FISCALE --> 
			<s:dgcolumn label="Codice Fiscale/ P.IVA" index="5" />
		
		<!-- DESCRIZIONE INSEGNA --> 
			<s:dgcolumn label="Insegna" index="6" />

		<!-- DESCRIZIONE STRUTTURA RICETTIVA --> 
			<s:dgcolumn label="Tipologia Struttura" index="7" />
			
		<!-- FLAG SUBENTRO PG170240 CT --> 
			<s:dgcolumn label="Subentro"  >
				<s:if right="{10}" control="eq" left="S">
					<s:then>Si</s:then>
				</s:if>
				<s:if right="{10}" control="ne" left="S">
					<s:then>No</s:then>
				</s:if>
			</s:dgcolumn>
			
			<!-- PRIORITA' --> 
			<s:dgcolumn label="Priorità" index="15" />
			

		<!-- AZIONI -->
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">

		<!-- MODIFICA RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="ImpostaSoggiornoAnagStrutturaEdit.do?tx_comune={2}&tx_anag_struttura={1}&tx_provincia={8}"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />
		<!-- CANCELLAZIONE RECORD -->
				<s:if right="{16}" control="eq" left="          ">
					<s:then>
					<s:hyperlink
					cssclass="hlStyle" 
					href="ImpostaSoggiornoAnagStrutturaCancel.do?tx_comune={2}&tx_anag_struttura={1}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Elimina" text=""   />
					</s:then>
					<s:else>
					<s:image src="../applications/templates/configurazione/img/cancel_gray.png" alt="Non è possibile eliminare la struttura, in quanto già censita su Gest. Entrate" width="" height="" cssclass="" />
					</s:else>
				</s:if>
			</s:dgcolumn>
			
		</s:datagrid>
	</s:div>

</c:if>
