<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="tipologiaservizioappioedit" encodeAttributes="true" />

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

</script>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="var_form" action="tipologiaservizioappioedit.do?vista=${vista}"
			method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'  || empty codop}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">NUOVA TIPOLOGIA APP IO</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">EDIT TIPOLOGIA APP IO - ${tx_codice}</s:div>
			</c:if>

			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<c:if test="${codop == 'add'  || empty codop}">
					<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
							
						<s:textbox bmodify="true" maxlenght="3"
							cssclass="textareaman" validator="required"
							label="Cod. Tipologia Servizi:" name="up_codiceTipologiaServizi"
							text="${up_codiceTipologiaServizi}"
							cssclasslabel="label160 bold textright" />
									
						<s:textbox bmodify="true" maxlenght="256"
						cssclass="textareaman" validator="required"
							label="Descr. Tipologia Servizi:" name="up_descrTipologiaServizi"
							text="${up_descrTipologiaServizi}"
							cssclasslabel="label160 bold textright" />
							
						</s:div>
					</s:div>
				</c:if>
				<c:if test="${codop == 'edit'}">
					<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" maxlenght="3"
							cssclass="textareaman" validator="required"
							label="Cod. Tipologia Servizi:" name="up_codiceTipologiaServizi"
							text="${up_codiceTipologiaServizi}"
							cssclasslabel="label160 bold textright floatleft" />
					
								
						<s:textbox bmodify="true" maxlenght="256"
							cssclass="textareaman" validator="required"
							label="Descr. Tipologia Servizi:" name="up_descrTipologiaServizi"
							text="${up_descrizioneTipologiaServizi}"
							cssclasslabel="label160 bold textright floatleft" />
						
					</s:div>
					<s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" name="up_codiceOperatore"
							bdisable="true" validator="ignore"
							label="cod. operatore:"
							cssclasslabel="label160 bold textright floatleft" cssclass="textareaman"
							text="${up_codiceOperatore}" />
					</s:div>
						
				
				</c:if>
			
			</s:div>
		<br />
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<c:if test="${codop == 'add' || empty codop}">
					<s:button id="tx_button_aggiungi" onclick="return ControllaSalva()"
						text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${codop == 'edit'}">
					<s:button id="tx_button_edit" onclick="return ControllaSalva()"
						text="Aggiorna" type="submit" cssclass="btnStyle" />
				</c:if>
				<s:button id="tx_button_reset" onclick="" validate="false"
					text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_indietro" onclick="" validate="false"
					text="Indietro" type="submit" cssclass="btnStyle" />
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
