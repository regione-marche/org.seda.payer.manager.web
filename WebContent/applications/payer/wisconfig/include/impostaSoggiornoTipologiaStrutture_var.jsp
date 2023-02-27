<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="impostasoggiornoTipologiaStrutture_var" encodeAttributes="true" />

<script type="text/javascript">
function setFiredButton(buttonName) {
	var buttonFired = document.getElementById('fired_button_hidden');
	if (buttonFired != null)
		buttonFired.value = buttonName;
}
</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_form" action="${do_command_name}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">TIPOLOGIA STRUTTURA RICETTIVA</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">TIPOLOGIA STRUTTURA RICETTIVA</s:div>
			</c:if>
									
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">

						<c:if test="${bmodify}">
							<s:textbox bmodify="true" name="tx_codice"
								label="Codice: " maxlenght="3" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="required;accept=${configurazione_descrizione256_regex}" showrequired="true"
								text="${tx_codice}" message="[accept=Codice: ${msg_configurazione_descrizione_regex}]"/>
						</c:if>

						<c:if test="${!bmodify}">
							<s:textbox bmodify="false" name="tx_codice"
								label="Codice: " maxlenght="3" 
								cssclasslabel="label85 bold textright" 
								cssclass="textareaman colordisabled" validator="required" showrequired="true"
								text="${tx_codice}"/>
						</c:if>
						</s:div>
						<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
							<s:textbox bmodify="true" name="tx_descrizione"
								label="Descrizione: " maxlenght="100" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="required" showrequired="true"
								text="${tx_descrizione}"/>
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
			<input type="hidden" name="tx_chiave_tipologia" value="${tx_chiave_tipologia}" />
	</s:form>
	</s:div>
	
</s:div>
