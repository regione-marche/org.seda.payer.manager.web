<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="piazzolaEdit" encodeAttributes="true" />

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
	<s:form name="var_form" action="piazzolaEdit.do?vista=${vista}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">CONFIGURAZIONE PIAZZOLA</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">CONFIGURAZIONE PIAZZOLA</s:div>
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
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_area_mercantile" 
						    label="Area Mercantile: " showrequired="true"
							multiple="false" valueselected="${tx_area_mercantile}" disable="${codop == 'edit'}" 
							cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
							<s:ddloption text="seleziona..." value="" />
							<s:ddloptionbinder options="${areemercDDLList}"/>
						</s:dropdownlist>		
					</s:div>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:div name="divDataIniValidita" cssclass="divDataDa">
							<s:date label="Data Inizio Validit&agrave;:" prefix="tx_dval_ini" yearbegin="${ddlDateAnnoDa}"
								yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
								separator="/" calendar="${tx_dval_ini}"
								cssclasslabel="label85 bold textright" cssclass="dateman"
								disabled="false"  
								message="[dateISO=Data Inizio Validit&agrave;: ${msg_dataISO_valida}]">
							</s:date>
							<input type="hidden" id="tx_dval_ini_hidden" value="" />
						</s:div>
					</s:div>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_Ang1Lat" 
				        	label="Angolo 1 Lat.:" maxlenght="15"
							validator="ignore;maxlength=15" 
							cssclasslabel="label85 bold textright" cssclass="ttextareaman"
							text="${tx_Ang1Lat}" />
					</s:div>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_Ang2Lat" 
				        	label="Angolo 2 Lat.:" maxlenght="15"
							validator="ignore;maxlength=15" 
							cssclasslabel="label85 bold textright" cssclass="ttextareaman"
							text="${tx_Ang2Lat}" />
					</s:div>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_Ang3Lat" 
				        	label="Angolo 3 Lat.:" maxlenght="15"
							validator="maxlength=15" 
							cssclasslabel="label85 bold textright" cssclass="ttextareaman"
							text="${tx_Ang3Lat}" />
					</s:div>	
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_Ang4Lat" 
				        	label="Angolo 4 Lat.:" maxlenght="15"
							validator="maxlength=15" 
							cssclasslabel="label85 bold textright" cssclass="ttextareaman"
							text="${tx_Ang4Lat}" />
					</s:div>																		
				</s:div>
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">										
			        <s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_piazzola" 
				       		label="Codice Piazzola:" showrequired="true" maxlenght="10"
							validator="required;maxlength=10" bdisable="${codop == 'edit'}"
							cssclasslabel="label85 bold textright" cssclass="ttextareaman"
							text="${tx_piazzola}" />
					</s:div>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:div name="divDataFinValidita" cssclass="divDataDa">
							<s:date label="Data Fine Validit&agrave;:" prefix="tx_dval_fin" yearbegin="${ddlDateAnnoDa}"
								yearend="2099" locale="IT-it" descriptivemonth="false"
								separator="/" calendar="${tx_dval_fin}"
								cssclasslabel="label85 bold textright" cssclass="dateman"
								disabled="false"  
								message="[dateISO=Data Fine Validit&agrave;: ${msg_dataISO_valida}]">
							</s:date>
							<input type="hidden" id="tx_dval_fin_hidden" value="" />
						</s:div>
					</s:div>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_Ang1Lon" 
				        	label="Angolo 1 Long.:" maxlenght="15"
							validator="maxlength=15" 
							cssclasslabel="label85 bold textright" cssclass="ttextareaman"
							text="${tx_Ang1Lon}" />
					</s:div>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_Ang2Lon" 
				        	label="Angolo 2 Long.:" maxlenght="15"
							validator="maxlength=15" 
							cssclasslabel="label85 bold textright" cssclass="ttextareaman"
							text="${tx_Ang2Lon}" />
					</s:div>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_Ang3Lon" 
				        	label="Angolo 3 Long.:" maxlenght="15"
							validator="maxlength=15" 
							cssclasslabel="label85 bold textright" cssclass="ttextareaman"
							text="${tx_Ang3Lon}" />
					</s:div>	
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_Ang4Lon" 
				        	label="Angolo 4 Long.:" maxlenght="15"
							validator="maxlength=15" 
							cssclasslabel="label85 bold textright" cssclass="ttextareaman"
							text="${tx_Ang4Lon}" />
					</s:div>																														
				</s:div>
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_descr_piazzola" 
				        	label="Descr. Piazzola:" showrequired="true" maxlenght="70"
							validator="required;maxlength=70" bdisable="${codop == 'edit'}"
							cssclasslabel="label85 bold textright" cssclass="ttextareaman"
							text="${tx_descr_piazzola}" />
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
				<input type="hidden" name="tx_piazzola_h" value="${tx_piazzola_h}" />
				<input type="hidden" name="tx_area_mercantile_h" value="${tx_area_mercantile_h}" />
				<input type="hidden" name="tx_descr_piazzola_h" value="${tx_descr_piazzola_h}" />
	</s:form>
	</s:div>
	
</s:div>