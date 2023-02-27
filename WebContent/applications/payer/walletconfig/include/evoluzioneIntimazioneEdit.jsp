<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="evoluzioneintimazioneEdit" encodeAttributes="true" />

<jsp:useBean id="typeRequest"
	class="com.seda.payer.commons.bean.TypeRequest" />


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
		$("#tx_dval_sms_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_dval_sms_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_dval_sms_day_id").val(dateText.substr(0,2));
												$("#tx_dval_sms_month_id").val(dateText.substr(3,2));
												$("#tx_dval_sms_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_dval_sms_day_id",
				                              "tx_dval_sms_month_id",
				                              "tx_dval_sms_year_id",
				                              "tx_dval_sms_hidden");
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
	<s:form name="var_form" action="evoluzioneintimazioneEdit.do?vista=${vista}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">CONFIGURAZIONE EVOLUZIONE INTIMAZIONE</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">CONFIGURAZIONE EVOLUZIONE INTIMAZIONE</s:div>
			</c:if>
									
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<%-- TODO da verificare la ddl che segue ripresa dal prototipo e non dalla pagina originale di riferimento --%>
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTopDouble">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						<!--<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label85 bold textright" validator="required;"
									cssclass="floatleft" disable="${codop == 'edit'}"
									onchange="setFired();this.form.submit();"
									   cachedrowset="listaSocietaUtenteEnte" usexml="true" 
									   valueselected="${ddlSocietaUtenteEnte}">
   							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
							<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}"/>
						</s:dropdownlist>-->
						
						
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
						
				<s:div name="divRicercaMetadatiLeft" cssclass="divRicMetadatiLeft">
		            <!--<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
		            	<s:div name="divDataDa_validita" cssclass="divDataDa">
		        			<s:date label="Data Validità:" prefix="tx_dval_sms" yearbegin="${ddlDateAnnoDa}"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman"
								disabled="${codop == 'edit'}"
								yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
								separator="/" calendar="${tx_dval_sms}">
							</s:date>
							<input type="hidden" id="tx_dval_sms_hidden" value="" />
						</s:div>
					</s:div>-->
					<c:if test="${codop == 'edit'}">

						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:div name="divDataValidita" cssclass="divDataDa">
									<!--<s:date label="Data Validit&agrave;:" prefix="tx_dval_sms" yearbegin="${ddlDateAnnoDa}"
										yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
										separator="/" calendar="${tx_dval_sms}"
										cssclasslabel="label85 bold textright"
										cssclass="dateman"
										disabled="true" 
										validator="ignore" showrequired="true">
									</s:date>-->
									
									<s:date label="Data Validit&agrave;:" prefix="tx_dval_sms" yearbegin="${ddlDateAnnoDa}"
										yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
										separator="/" calendar="${tx_dval_sms}"
										cssclasslabel="label85 bold textright"
										cssclass="dateman" disabled="true" >
									</s:date>
									
									<!--  <input type="hidden" id="tx_dval_sms_hidden" value="" />-->
								</s:div>
								
								
						</s:div>
					</c:if>
					<c:if test="${codop != 'edit'}">
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:div name="divDataValidita" cssclass="divDataDa">
									<s:date label="Data Validit&agrave;:" prefix="tx_dval_sms" yearbegin="${ddlDateAnnoDa}"
										yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
										separator="/" calendar="${tx_dval_sms}"
										cssclasslabel="label85 bold textright"
										cssclass="dateman"
										disabled="false" 
										validator="dateISO;required" showrequired="true"
										message="[dateISO=Data Validit&agrave;: ${msg_dataISO_valida}]">
									</s:date>
									<input type="hidden" id="tx_dval_sms_hidden" value="" />
								</s:div>
						</s:div>
					</c:if>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_flg_sollecito_cartaceo" disable="false"
							cssclass="tbddlMax floatleft" label="Sollecito Cartaceo:"
							cssclasslabel="label85 bold textright" validator="required;"
							valueselected="${tx_flg_sollecito_cartaceo}">
							<s:ddloption text="Si" value="Y" />
							<s:ddloption text="No" value="N" />
						</s:dropdownlist>
					</s:div>
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
				            	<s:textbox bmodify="true" name="tx_ente_srv"
								label="Ente:" maxlenght="6"
								validator="required;maxlength=6" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman"
								text="${tx_ente_srv}" />
						</s:div>
					
					
				</s:div>


				<s:div name="divRicercaRight2" cssclass="divRicMetadatiCenter">
				 	<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_importo_residuo"
								label="Imp. Res.:" maxlenght="15"
								validator="required;accept=^[-][0-9]{1,13}\,[0-9]{2}?$;maxlength=11"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman"
								message="[accept=Imp. Residuo ${msg_configurazione_importo_N8_2}]" 
								text="${tx_importo_residuo}" />
						</s:div>
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_intervallo_gg"
							label="Inter. GG Evoluzione:" maxlenght="3"
							validator="digits;maxlength=3"
							bdisable="false" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_intervallo_gg}" />
					</s:div>
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
				            	<s:textbox bmodify="true" name="tx_is_srv"
								label="Imposta Servizio:" maxlenght="2"
								validator="required;maxlength=2" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman"
								text="${tx_is_srv}" />
						</s:div>
					
				</s:div>
					
					
				<s:div name="divRicercaRight3" cssclass="divRicMetadatiRight">
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_sms_sollecito"  
							label="SMS Soll: " showrequired="true"
									cssclasslabel="label65 bold textright" validator="required;"
									cssclass="textareaman" disable="false"
									valueselected="${tx_sms_sollecito}">
   							<s:ddloption text="Si" value="Y" />
							<s:ddloption text="No" value="N" />
						</s:dropdownlist>
					</s:div>
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_flagAttivazione"  
							label="Regola Attiva: " showrequired="true"
							disable="false"
							cssclasslabel="label85 bold textright" 
							cssclass="textareaman" 
							valueselected="${tx_flagAttivazione}">
  							<s:ddloption text="Attiva" value="Y" />
  							<s:ddloption text="Non Attiva" value="N" />
  							
						</s:dropdownlist>
					</s:div>

					<c:if test="${codop == 'edit'}">
						<s:div name="divElement16" cssclass="divRicMetadatiSingleRow">
				        	<s:dropdownlist name="tx_tipoServizio_srv" disable="true" label="Per Servizio:"
					 			multiple="false" valueselected="${tx_tipoServizio_srv}" 
						 		cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
						 		<s:ddloptionbinder options="${serviziDDLList}"/>
							</s:dropdownlist>
							<input type="hidden" id="tx_tipoServizio_srv" value="${tx_tipoServizio_srv}" />
						</s:div>
					</c:if>
					<c:if test="${codop != 'edit'}">
						<s:div name="divElement16" cssclass="divRicMetadatiSingleRow">
				        	<s:dropdownlist name="tx_tipoServizio_srv" disable="false" label="Per Servizio:"
					 			multiple="false" valueselected="${tx_tipoServizio_srv}" 
						 		cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
						 		<s:ddloptionbinder options="${serviziDDLList}"/>
							</s:dropdownlist>
						</s:div>
					</c:if>

								
				</s:div>
			
		</s:div>
			<br/> 
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_indietro" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />
				<!-- 
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
				 -->
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
				<input type="hidden" name="tx_tipoServizio_h" value="${tx_tipoServizio_srv}" />
				<input type="hidden" name="tx_dval_sms_h" value="${tx_dval_sms_h}" />
	</s:form>
	</s:div>
	
</s:div>
