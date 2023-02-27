<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="gruppoAgenziaEdit" encodeAttributes="true" />

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
</script>

	

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_form" action="gruppoAgenziaEdit.do?vista=${vista}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
		
		

			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">NUOVO GRUPPO AGENZIA</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">MODIFICA GRUPPO AGENZIA</s:div>
			</c:if>
		
		<s:div cssclass="seda-ui-div divRicMetadatiTop" name="divRicercaMetadatiTop"> 
					
				 
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
						
				<s:div name="divRicercaMetadatiLeft" cssclass="divRicMetadatiLeft">
					<c:if test="${codop == 'edit'}">

						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" name="tx_codiceGruppoAgenzia" 
							label="Cod. Gruppo Agenzia:" maxlenght="3" bdisable="true"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							validator="required"
							text="${tx_codiceGruppoAgenzia}" />
							<input type="hidden" name="tx_codiceGruppoAgenzia" value="${tx_codiceGruppoAgenzia}" />							
					</s:div>
					</c:if>
					<c:if test="${codop != 'edit'}">
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" name="tx_codiceGruppoAgenzia" showrequired="true"
							label="Cod. Gruppo Agenzia:" maxlenght="3"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							validator="required;maxlength=3"
							text="${tx_codiceGruppoAgenzia}" />
						</s:div>
						
					</c:if>	
				</s:div>
				
				
				<s:div name="divRicercaRight2" cssclass="divRicMetadatiCenter">
				 	<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">

							<s:textbox bmodify="true" name="tx_descrizioneGruppoAgenzia"
							label="Descrizione:" maxlenght="50" bdisable="false" showrequired="true"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							validator="required;maxlength=50"
							text="${tx_descrizioneGruppoAgenzia}" />
					</s:div>
				</s:div>	
			</s:div>	
			</s:div>
			<br/> 
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_indietro" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />

				<c:if test="${codop == 'add'}">
					<s:button id="tx_button_aggiungi" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${codop == 'edit'}">
					<s:button id="tx_button_edit" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
			</s:div>
		<input type="hidden" id="codop" name="codop" value="${codop}" />
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
