<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="modello3Edit" encodeAttributes="true" />

<jsp:useBean id="typeRequest"
	class="com.seda.payer.commons.bean.TypeRequest" />


<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
	
<script type="text/javascript">

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
	
	function changeAuxDigit(){
		document.getElementById('wrtMsg_message').innerHTML = "";
		var auxdigit =document.getElementById('tx_auxDigit').value;
		if(auxdigit == 1 || auxdigit == 2){
			document.getElementById('tx_codiceSegregazione').value = "";
		}
	}
	
	function ControllaSalva(){
		var auxdigit =document.getElementById('tx_auxDigit').value;
		var codiceSegregazione = document.getElementById('tx_codiceSegregazione').value;
		var carattereDiServizio = document.getElementBuId('tx_carattereDiServizio').value;

		if(auxdigit == 1 || auxdigit == 2){
			if((codiceSegregazione != null) && (codiceSegregazione != "")){
				writeMsg("Se Aux Digit è valorizzato a 1 o 2 il  Codice Segregazione non deve essere valorizzato");
				return false;
			}
		}
		
		if(auxdigit == 0 || auxdigit == 3){
			if((codiceSegregazione == "") ||( codiceSegregazione == null)){
				writeMsg("Se Aux Digit è valorizzato a 0 o 3 il  Codice Segregazione è obbligatorio");
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
			if(carattereDiServizio.length < 2){
				writeMsg("Carattere di Servizio:il campo deve essere lungo 2");
				return false;
			}
			

		}
		
		return true;
		
	}
</script>

	

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_form" action="modello3Edit.do?vista=${vista}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
		
		
		<div class="seda-ui-divvalidator"><span id="wrtMsg_message"class="seda-ui-spanvalidator"><br></span></div>
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">CONFIGURAZIONE MODELLO 3</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">CONFIGURAZIONE MODELLO 3 - ${tx_ente}</s:div>
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
						<s:textbox bmodify="false" name="tx_idDominio" 
							label="Id Dominio:" maxlenght="16" bdisable="true"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							validator="required;maxlength=16"
							text="${tx_idDominio}" />
							<input type="hidden" name="tx_idDominio" value="${tx_idDominio}" />							
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
						</s:div>
						
					</c:if>
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_tipoServizio" showrequired="true"
							label="Tipologia Servizio:" maxlenght="3"
							validator="required;maxlength=3"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_tipoServizio}" />

					</s:div>		
				</s:div>
				
				
				<s:div name="divRicercaRight2" cssclass="divRicMetadatiCenter">
				 	<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<c:if test="${codop == 'edit'}">
							<s:textbox bmodify="false" name="tx_auxDigit"
							label="Aux Digit:" maxlenght="1" bdisable="true"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_auxDigit}" />
							<input type="hidden" name="tx_auxDigit" value="${tx_auxDigit}" />
						</c:if>
						<c:if test="${codop != 'edit'}">
							<s:dropdownlist name="tx_auxDigit" showrequired="true" 
								label="Aux Digit:" disable="false" 
								onchange="changeAuxDigit();"
								cssclasslabel="label85 bold textright" validator="required;"
								cssclass="textareaman" 
								valueselected="${tx_auxDigit}">
								<s:ddloption text="0" value="0" />
								<s:ddloption text="1" value="1" />
								<s:ddloption text="2" value="2" />
								<s:ddloption text="3" value="3" />
							</s:dropdownlist>
						</c:if>
					</s:div>
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_urlIntegrazione"
							label="Url Integrazione:" maxlenght="256" showrequired="true"
							validator="required;url;maxlength=256"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_urlIntegrazione}" />
					</s:div>	
				</s:div>	
				
				
				
				<s:div name="divRicercaRight3" cssclass="divRicMetadatiRight">				
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<c:if test="${codop == 'edit'}">
							<s:textbox bmodify="false" name="tx_codiceSegregazione"
									label="Codice Segregazione:" maxlenght="2"
									validator="ignore;digits;maxlength=2" bdisable="true" 
									cssclasslabel="label85 bold textright"
									cssclass="textareaman" 
									text="${tx_codiceSegregazione}" />
							<input type="hidden" name="tx_codiceSegregazione" value="${tx_codiceSegregazione}" />	
						</c:if>
						<c:if test="${codop != 'edit'}">
							<s:textbox bmodify="true" name="tx_codiceSegregazione"
									label="Codice Segregazione:" maxlenght="2"
									validator="required;digits;maxlength=2"
									cssclasslabel="label85 bold textright"
									bdisable="false" showrequired="true" 
									cssclass="textareaman" 
									text="${tx_codiceSegregazione}" />
						</c:if>
					</s:div>	
					<%-- SVILUPPO_002_SB --%>
					<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
					<c:if test="${codop == 'edit'}">
						<s:textbox bmodify="false" name="tx_carattereDiServizio"
							label="Carattere di Servizio:" maxlenght="2" showrequired="false"
							validator="ignore;maxlength=2"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" bdisable="true" 
							text="${tx_carattereDiServizio}" />
						<input type="hidden" name="tx_carattereDiServizio" value="${tx_carattereDiServizio}" />
					</c:if>
					<c:if test="${codop != 'edit'}">
						<s:textbox bmodify="true" name="tx_carattereDiServizio"
							label="Carattere di Servizio:" maxlenght="2" showrequired="false"
							validator="ignore;maxlength=2"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" bdisable="false" 
							text="${tx_carattereDiServizio}" />
					</c:if>
					</s:div>			
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
		<input type="hidden" name="tx_societa_h" value="${tx_societa_h}" />
		<input type="hidden" name="tx_utente_h" value="${tx_utente_h}" />
		<input type="hidden" name="tx_ente_h" value="${tx_ente_h}" />
		<input type="hidden" name="tx_idDominio_h" value="${tx_idDominio_h}" />	
		<input type="hidden" name="tx_codiceOperatore" value="${tx_codiceOperatore}" />	
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
