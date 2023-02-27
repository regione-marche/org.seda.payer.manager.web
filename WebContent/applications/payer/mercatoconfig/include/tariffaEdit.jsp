<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="tariffaEdit" encodeAttributes="true" />

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
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#tx_dval_ini_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_dval_ini_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_dval_ini_day_id").val(dateText.substr(0,2));
												$("#tx_dval_ini_month_id").val(dateText.substr(3,2));
												$("#tx_dval_ini_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
			//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
            	updateValoreDatePickerFromDdl("tx_dval_ini_day_id",
			    	                          "tx_dval_ini_month_id",
			        	                      "tx_dval_ini_year_id",
			            	                  "tx_dval_ini_hidden");
			}
		});
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#tx_dval_fin_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_dval_fin_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_dval_fin_day_id").val(dateText.substr(0,2));
												$("#tx_dval_fin_month_id").val(dateText.substr(3,2));
												$("#tx_dval_fin_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
			//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
            	updateValoreDatePickerFromDdl("tx_dval_fin_day_id",
			    	                          "tx_dval_fin_month_id",
			        	                      "tx_dval_fin_year_id",
			            	                  "tx_dval_fin_hidden");
			}
		});
	});

	function setFired() {
			var buttonFired = document.getElementById('fired_button_hidden');
			buttonFired.value = 'tx_button_societa_changed';
	}
</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_form" action="tariffeEdit.do?vista=${vista}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">CONFIGURAZIONE TARIFFE</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">CONFIGURAZIONE TARIFFE</s:div>
			</c:if>
									
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<%-- TODO da verificare la ddl che segue ripresa dal prototipo e non dalla pagina originale di riferimento --%>
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTopDouble">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">

						<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label160 bold textright floatleft" validator="required;"
									cssclass="seda-ui-ddl tbddlMax780 floatleft" disable="${codop == 'edit'}"
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
			         <s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_area_mercantile"  label="Area Mercantile: " showrequired="true"  
							disable="${codop == 'edit'}" multiple="false"  valueselected="${tx_area_mercantile}" 
							onchange="setFired();this.form.submit();" validator="required;"
							cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
							<s:ddloption text="seleziona..." value="xx" />
							<s:ddloptionbinder options="${areemercDDLList}"/>
						</s:dropdownlist>		
					</s:div>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:div name="divDataIniValidita" cssclass="divDataDa">
							<s:date label="Data Inizio Validit&agrave;: " prefix="tx_dval_ini" yearbegin="${ddlDateAnnoDa}"
								yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
								separator="/" calendar="${tx_dval_ini}"
								cssclasslabel="label85 bold textright" cssclass="dateman"
								disabled="false"  
								message="[dateISO=Data Inizio Validit&agrave;: ${msg_dataISO_valida}]">
							</s:date>
							<input type="hidden" id="tx_dval_ini_hidden" value="" />
						</s:div>
					</s:div>
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_giorno_settimana" disable="false" label="Giorno della Settimana: "
							multiple="false" valueselected="${tx_giorno_settimana}" 
							cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
							<s:ddloption text="seleziona..." value="0" />
							<s:ddloption text="Lunedì" value="1" />
							<s:ddloption text="Martedì" value="2" />
							<s:ddloption text="Mercoledì" value="3" />
							<s:ddloption text="Giovedì" value="4" />
							<s:ddloption text="Venerdì" value="5" />
							<s:ddloption text="Sabato" value="6" />
							<s:ddloption text="Domenica" value="7" />
						</s:dropdownlist>		
					</s:div>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_tariffa_tari"
							label="Tariffa Tari: " maxlenght="9" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=^[0-9]{1,4}\,[0-9]{2}?$;maxlength=9"
							message="[accept=Tariffa Tari: ${msg_configurazione_importo_4_2}]"
							text="${tx_tariffa_tari}" />
					</s:div>
				</s:div>
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">										
			        <s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_piazzola" disable="${codop == 'edit'}" label="Piazzola: "
							showrequired="true" multiple="false" valueselected="${tx_piazzola}" 
							cssclasslabel="label85 bold textright" validator="required;" cssclass="tbddlMax floatleft">
							<s:ddloption text="seleziona..." value="" />
							<s:ddloptionbinder options="${piazzolaDDLList}"/>
						</s:dropdownlist>		
					</s:div>						    
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:div name="divDataFinValidita" cssclass="divDataDa">
							<s:date label="Data Fine Validit&agrave;: " prefix="tx_dval_fin" yearbegin="${ddlDateAnnoDa}"
								yearend="2099" locale="IT-it" descriptivemonth="false"
								separator="/" calendar="${tx_dval_fin}"
								cssclasslabel="label85 bold textright" cssclass="dateman"
								disabled="false"  
								message="[dateISO=Data Fine Validit&agrave;: ${msg_dataISO_valida}]">
							</s:date>
							<input type="hidden" id="tx_dval_fin_hidden" value="" />
						</s:div>
					</s:div>
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_periodo_giornal" disable="false" label="Periodo Giornal.: "
							showrequired="true" multiple="false" valueselected="${tx_periodo_giornal}" 
							cssclasslabel="label85 bold textright" validator="required;" cssclass="tbddlMax floatleft">
							<s:ddloption text="seleziona..." value="" />
							<s:ddloptionbinder options="${pergiornDDLList}"/>
						 </s:dropdownlist>		
					</s:div>				
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_tariffa_cosap"
							label="Tariffa Cosap: " maxlenght="9" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=^[0-9]{1,4}\,[0-9]{2}?$;maxlength=9"
							message="[accept=Tariffa Cosap: ${msg_configurazione_importo_4_2}]"
							text="${tx_tariffa_cosap}" />
					</s:div>																			
				</s:div>
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_num_autor"
							label="Codice Autorizzazione: " showrequired="true" maxlenght="30" 
							cssclasslabel="label85 bold textright" bdisable="${codop == 'edit'}"
							cssclass="textareaman" validator="required;"
							text="${tx_num_autor}" />
					</s:div>		
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_tipologia_banco" disable="${codop == 'edit'}" label="Tipologia Banco: "
							showrequired="true" multiple="false" valueselected="${tx_tipologia_banco}" 
							cssclasslabel="label85 bold textright" validator="required;" cssclass="tbddlMax floatleft">
							<s:ddloption text="seleziona..." value="" />
							<s:ddloptionbinder options="${tipbancoDDLList}"/>
						</s:dropdownlist>		
					</s:div>					
				</s:div>				
		</s:div>
			<br/> 
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_indietro" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
				<c:if test="${codop == 'add'}">
					<s:button id="tx_button_aggiungi" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${codop == 'edit'}">
					<s:button id="tx_button_edit" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
			</s:div>
			<input type="hidden" name="codop" value="${codop}" />
			<input type="hidden" name="tx_societa" value="${tx_societa}" />
			<input type="hidden" name="tx_utente" value="${tx_utente}" />
			<input type="hidden" name="tx_ente" value="${tx_ente}" />
			<input type="hidden" name="tx_area_mercantile_h" value="${tx_area_mercantile_h}" />				
			<input type="hidden" name="tx_piazzola_h" value="${tx_piazzola_h}" />
			<input type="hidden" name="tx_num_autor_h" value="${tx_num_autor_h}" />
			<input type="hidden" name="tx_tipologia_banco_h" value="${tx_tipologia_banco_h}" />
			<input type="hidden" name="tx_giorno_settimana_h" value="${tx_giorno_settimana_h}" />
			<input type="hidden" name="tx_cod_key_tariffa_h" value="${tx_cod_key_tariffa_h}" />
	</s:form>
	</s:div>
	
</s:div>
