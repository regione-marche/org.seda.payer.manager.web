<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="blackboxEdit" encodeAttributes="true" />

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
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}

	function bindEvent(el, eventName, eventHandler) {
		  if (el.addEventListener){
		    el.addEventListener(eventName, eventHandler, false); 
		  } else if (el.attachEvent){
		    el.attachEvent('on'+eventName, eventHandler);
		  }
		}
			
	function writeMsg(msg){
		document.getElementById('wrtMsg_message').innerHTML=msg;
		

		}

	function ControllaSalva(){
		var auxdigit =document.getElementById('tx_auxDigit').value;
		var applicationCode = document.getElementById('tx_applicationCode').value;
		var codiceSegregazione = document.getElementById('tx_codiceSegregazione').value;
		if(auxdigit == 0){
			if(applicationCode == "" || applicationCode == null){
				writeMsg("Se Aux Digit è valorizzato a 0  l'Application Code è obbligatorio");
				return false;
			}
			if(isNaN(applicationCode)){
				writeMsg("Application Code:inserisci valori numerici");
				return false;
			}
			if(applicationCode.length < 2){
				writeMsg("Application Code:il campo deve essere lungo 2");
				return false;
			}
		//inizio LP PG210070 - 20210729
		} else {
			if((applicationCode != null) && (applicationCode != "")) {
				writeMsg("Se Aux Digit non è valorizzato a 0 l'Application Code non deve essere valorizzato");
				return false;
			}
		//fine LP PG210070 - 20210729
		}
		if(auxdigit == 3){
			if((codiceSegregazione == "") ||( codiceSegregazione == null)){
				writeMsg("Se Aux Digit è valorizzato a 3 il  Codice Segregazione è obbligatorio");
				return false;
			}
			if(isNaN(codiceSegregazione)){
				writeMsg("Codice Segregazione:inserisci valori numerici");
				return false;
			}
			if(codiceSegregazione.length < 2){
				writeMsg("Codice Segregazione:il campo deve essere lungo 2");
				return false;
			}
		//inizio LP PG210070 - 20210729
		} else {
			if((codiceSegregazione != null) && (codiceSegregazione != "")) {
				writeMsg("Se Aux Digit non è valorizzato a 3 il Codice Segregazione non deve essere valorizzato");
				return false;
			}
		//inizio LP PG210070 - 20210729
		}
		
		return true;
		
	}
</script>

	

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_form" action="blackboxEdit.do?vista=${vista}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
		
		
		<div class="seda-ui-divvalidator"><span id="wrtMsg_message"class="seda-ui-spanvalidator"><br></span></div>
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">CONFIGURAZIONE BLACK BOX</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">CONFIGURAZIONE BLACK BOX - ${tx_enteda5}</s:div>
			</c:if>
									
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<%-- TODO da verificare la ddl che segue ripresa dal prototipo e non dalla pagina originale di riferimento --%>
				<c:if test="${codop == 'add'}">
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
								<s:ddloption value="{1}|{2}|{3}|{4}" text="{5} / {6} / {4}"/>
							</s:dropdownlist>
						
						
						
							<noscript>
								<s:button id="tx_button_societa_changed" 
									onclick="" text="" 
									validate="false"
									type="submit" cssclass="btnimgStyle"  title="Aggiorna" />
							</noscript>
														
					</s:div>
				</s:div>
			</c:if>		
			
			
				<s:div name="divRicercaMetadatiLeft" cssclass="divRicMetadatiLeft">
				<c:if test="${codop == 'edit'}">

						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" name="tx_enteda5" 
							label="Ente:" maxlenght="5" bdisable="true"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							validator="required;maxlength=5"
							text="${tx_enteda5}" />
							<input type="hidden" id="tx_enteda5_h" value="${tx_enteda5_h}" />
					</s:div>
				</c:if>
				
					<c:if test="${codop == 'edit'}">

						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" name="tx_idDominio" 
							label="Id Dominio:" maxlenght="16" bdisable="true"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							validator="required;maxlength=16"
							text="${tx_idDominio}" />
							
					</s:div>
					</c:if>
					<c:if test="${codop != 'edit'}">
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" name="tx_idDominio" showrequired="true"
							label="Id Dominio:" maxlenght="16"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							validator="required;maxlength=16"
							text="${tx_idDominio}" />
									<input type="hidden" id="tx_idDominio" value="" />
								</s:div>
						
					</c:if>
					<%--<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_auxDigit" 
							label="Aux Digit:" maxlenght="1"
							validator="required;digits;maxlength=1;accept=^[0-3]{1}"
							bdisable="false" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							message="[accept=Aux Digit: ${msg_configurazione_AuxDigit}]"
							text="${tx_auxDigit}" />
					</s:div>--%>
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_auxDigit" showrequired="true" 
							label="Aux Digit:" disable="${codop == 'edit'}"
							onchange="setRequired();"
							cssclasslabel="label85 bold textright" validator="required;"
							cssclass="textareaman" 
							valueselected="${tx_auxDigit}">
							<s:ddloption text="0" value="0" />
  							<s:ddloption text="1" value="1" />
  							<s:ddloption text="2" value="2" />
  							<s:ddloption text="3" value="3" />
						</s:dropdownlist>
						<input type="hidden" id="tx_auxDigit_h" value="${tx_auxDigit_h}" />
					</s:div>	
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">	
						<s:textbox bmodify="true" name="tx_applicationCode"
							label="Application Code:" maxlenght="2"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							validator="maxlength=2"
							bdisable="${codop == 'edit'}"
							showrequired="${applicationCodeRequired}" 
							text="${tx_applicationCode}" />
							<input type="hidden" id="tx_applicationCode_h" value="${tx_applicationCode_h}" />
					</s:div> 
					<%-- inizio LP PG210070 - 20210729 --%>
					<%-- s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_codiceSegregazione"
							label="Codice Segregazione:" maxlenght="2"
							validator="digits;maxlength=2"
							cssclasslabel="label85 bold textright"
							bdisable="${codop == 'edit'}" showrequired="${codiceSegregazioneRequired}" 
							cssclass="textareaman" 
							text="${tx_codiceSegregazione}" />
					</s:div --%>					
					<s:div name="divElement1a" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_codiceSegregazione"
							label="Codice Segregazione:" maxlenght="2"
							validator="maxlength=2"
							cssclasslabel="label85 bold textright"
							bdisable="${codop == 'edit'}" showrequired="${codiceSegregazioneRequired}" 
							cssclass="textareaman" 
							text="${tx_codiceSegregazione}" />
					</s:div>					
					<%-- fine LP PG210070 - 20210729 --%>

							<input type="hidden" id="tx_codiceSegregazione_h" value="${tx_codiceSegregazione_h}" />					
					
					<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_codiceServizio"
							label="Codice Servizio:" maxlenght="2"
							validator="maxlength=2"
							cssclasslabel="label85 bold textright"
							bdisable="${codop == 'edit'}" showrequired="${codiceServizioRequired}" 
							cssclass="textareaman" 
							text="${tx_codiceServizio}" />
							<input type="hidden" id="tx_codiceServizio_h" value="${tx_codiceServizio_h}" />
					</s:div>		
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_calcoloIuv" showrequired="true" 
							label="Calcolo Iuv: " disable="false"
							cssclasslabel="label85 bold textright" validator="required;"
							cssclass="textareaman" 
							valueselected="${tx_calcoloIuv}">
  							<s:ddloption text="Sì" value="Y" />
  							<s:ddloption text="No" value="N" />
						</s:dropdownlist>
					</s:div>			
				</s:div>
				</s:div>
				
				
				<s:div name="divRicercaRight2" cssclass="divRicMetadatiCenter">
				 	<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_urlFtp" showrequired="true"
							label="Url FTP:" maxlenght="256"
							validator="required;maxlength=256"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_urlFtp}" />
					</s:div>
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">	
						<s:textbox bmodify="true" name="tx_usrFtp" showrequired="true"
							label="Usr FTP:" maxlenght="256"
							validator="required;maxlength=256"
							bdisable="false" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_usrFtp}" />
					</s:div>
					
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_passwordFtp"
							label="Password FTP:" maxlenght="50" showrequired="true"
							bdisable="false" bpassword="true"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							validator="required;minlength=3;maxlength=50"
							text="${tx_passwordFtp}" />
						</s:div>
						<s:div name="divElement11b" cssclass="floatleft">
							<s:textbox bmodify="true" name="tx_dirFtpDownload"
							label="Directory FTP Download:" maxlenght="50" showrequired="true"
							validator="required;maxlength=50"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_dirFtpDownload}" />
						</s:div>
					</s:div>	
				
				
				
				<s:div name="divRicercaRight3" cssclass="divRicMetadatiRight">				
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_dirFtpUpload"
							label="Directory FTP Upload:" maxlenght="50" showrequired="true"
							validator="required;maxlength=50"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_dirFtpUpload}" />
					</s:div>

					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" name="tx_pathInput"
							label="Path Locale Input:" maxlenght="256" showrequired="true"
							validator="required;maxlength=256" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_pathInput}" />
					</s:div>
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_pathScarti"
							label="Path Locale Scarti:" maxlenght="256" showrequired="true"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							validator="required;maxlength=256" 
							text="${tx_pathScarti}" />
					</s:div>	
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_pathOutput"
							label="Path Locale Output:"  maxlenght="256"  showrequired="true"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							validator="required;maxlength=256" 
							text="${tx_pathOutput}" />
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
					<s:button id="tx_button_aggiungi" onclick="return ControllaSalva()" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${codop == 'edit'}">
					<s:button id="tx_button_edit" onclick="return ControllaSalva()" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
			</s:div>
		<input type="hidden" id="codop" name="codop" value="${codop}" />
		<input type="hidden" name="tx_enteda5_h" value="${tx_enteda5_h}" />
		<input type="hidden" name="tx_idDominio_h" value="${tx_idDominio_h}" />	
		<input type="hidden" name="tx_codiceServizio_h" value="${tx_codiceServizio_h}" />
		<input type="hidden" name="tx_codiceSegregazione_h" value="${tx_codiceSegregazione_h}" />
		<input type="hidden" name="tx_applicationCode_h" value="${tx_applicationCode_h}" />
		<input type="hidden" name="tx_auxDigit_h" value="${tx_auxDigit_h}" />
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
