<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="impostaServizioTipologiaSoggetti_var" encodeAttributes="true" />

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
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">TIPOLOGIA SOGGETTO</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">TIPOLOGIA SOGGETTO</s:div>
			</c:if>
									
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
							<s:dropdownlist name="tx_provincia"
								disable="true" 
								label="Provincia:"
								cssclasslabel="label85 bold floatleft textright"
								cssclass="tbddl floatleft"
								cachedrowset="listProvince" usexml="true"
								onchange="setFiredButton('tx_button_changed');this.form.submit();"
								validator="required" showrequired="true"
								valueselected="${tx_provincia}">
								<s:ddloption text="Selezionare uno degli elementi" value="" />
								<s:ddloption  value="{2}" text="{1}"/>
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_changed" onclick="" text="" validate="false"
									type="submit" cssclass="btnimgStyle" title="Aggiorna" />
							</noscript>
						</s:div>
						
						<s:div name="tipServLabel_srch" cssclass="divRicMetadatiCenterSmall">
							<s:dropdownlist name="tx_comune"
									disable="true" 
									label="Comune:"
									cssclasslabel="label65 bold textright"
									cssclass="tbddl"
									cachedrowset="listbelfiore" usexml="true"
									validator="required" showrequired="true"
									valueselected="${tx_comune}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption  value="{1}" text="{4}"/>
								</s:dropdownlist>
						</s:div>
						
						<s:div name="divElement2" cssclass="divRicMetadatiTopRight">
							
							<c:if test="${codop == 'add'}">
								<s:textbox bmodify="true" name="tx_cod_soggetto"
									label="Codice: " maxlenght="3" 
									cssclasslabel="label85 bold textright"
									showrequired="true"
									cssclass="textareaman" validator="required;accept=${configurazione_descrizione256_regex}"
									text="${tx_cod_soggetto}" message="[accept=Codice: ${msg_configurazione_descrizione_regex}]"/>
							
							</c:if>
							<c:if test="${codop == 'edit'}">
								<s:textbox bmodify="true" name="tx_cod_soggetto"
									label="Codice: " maxlenght="3" 
									cssclasslabel="label85 bold textright"
									cssclass="textareaman colordisabled" validator="ignore"
									text="${tx_cod_soggetto}" />
							</c:if>
						</s:div>
							
							
						</s:div>
					
					<s:div name="divElement4" cssclass="divRicMetadatiLeft">
						
							<s:textbox bmodify="true" name="tx_desc_soggetto"
								label="Descrizione: " maxlenght="256" 
								cssclasslabel="label85 bold floatleft textright"
								showrequired="true"
								cssclass="textareaman" validator="required;accept=${configurazione_descrizione256_regex}"
								text="${tx_desc_soggetto}" message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"/>
						
						
					</s:div>
					<s:div name="divElement5" cssclass="divRicMetadatiCenter">
						<c:if test="${codop == 'add'}">
							<s:dropdownlist label="Esente:"
								cssclasslabel="label85 bold textright" cssclass="textareaman"
								name="ddl_flag_esenzione" disable="false"
								validator="required" showrequired="true"
								valueselected="${ddl_flag_esenzione}">
								<s:ddloption value="" text="Selezionare uno degli elementi" />
								<s:ddloption value="Y" text="Si" />
								<s:ddloption value="N" text="No" />
							</s:dropdownlist>
						</c:if>
					
						<c:if test="${codop == 'edit'}">
							<s:dropdownlist label="Esente:"
								cssclasslabel="label85 bold textright" cssclass="textareaman"
								name="ddl_flag_esenzione" disable="false"
								validator="required" showrequired="true"
								valueselected="${ddl_flag_esenzione}">
								<s:ddloption value="Y" text="Si" />
								<s:ddloption value="N" text="No" />
							</s:dropdownlist>
						</c:if>
						
					</s:div>
					<s:div name="divElement6" cssclass="divRicMetadatiRight">
							
							
					</s:div>
						
						
				</s:div>
			<br /><br /><br />
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
			<input type="hidden" name="tx_chiave_soggetti" value="${tx_chiave_soggetti}" />
			<c:if test="${codop == 'edit'}">
				<input type="hidden" name="tx_provincia" value="${tx_provincia}" />
				<input type="hidden" name="tx_comune" value="${tx_comune}" />
			</c:if>
	</s:form>
	</s:div>
	
</s:div>
