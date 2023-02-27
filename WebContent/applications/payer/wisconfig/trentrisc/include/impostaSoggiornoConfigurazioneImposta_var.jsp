<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="impostaSoggiornoConfigurazioneImposta_var" encodeAttributes="true" />

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
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">RACCORDO ENTRATE</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">RACCORDO ENTRATE</s:div>
			</c:if>
									
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
							<s:dropdownlist name="tx_provincia"
								disable="${codop == 'add'?'false':'true'}" 
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
									disable="${codop == 'add'?'false':'true'}" 
									label="Comune:"
									cssclasslabel="label65 bold textright"
									cssclass="tbddl"
									cachedrowset="listbelfiore" usexml="true"
									onchange="setFiredButton('tx_button_changed');this.form.submit();"
									validator="required" showrequired="true"
									valueselected="${tx_comune}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption  value="{1}" text="{4}"/>
								</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_changed" onclick="" text="" validate="false"
									type="submit" cssclass="btnimgStyle" title="Aggiorna" />
							</noscript>
						</s:div>
						
						<s:div name="divElement2" cssclass="divRicMetadatiTopRight">
							<s:dropdownlist name="tx_ente"
									disable="${codop == 'add'?'false':'true'}" 
									label="Ente:"
									cssclasslabel="label65 bold textright"
									cssclass="tbddl"
									cachedrowset="listente" usexml="true"
									validator="required" showrequired="true"
									valueselected="${tx_ente}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption  value="{1}|{2}|{3}" text="{4} {5}"/>
								</s:dropdownlist>
						</s:div>
							
							
						</s:div>
					
					<s:div name="divLeft" cssclass="divRicMetadatiLeft">
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_cod_ges"
								label="Cod. Ent.: " maxlenght="6" 
								cssclasslabel="label85 bold textright"
								showrequired="true"
								cssclass="textareaman" validator="required;accept=${configurazione_descrizione256_regex}"
								text="${tx_cod_ges}" message="[accept=Cod.Gest.Ent.: ${msg_configurazione_descrizione_regex}]"/>
						
						</s:div>
					</s:div>
						<s:div name="divCenter" cssclass="divRicMetadatiCenter">
							<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" name="tx_is_ges"
									label="Cod.IS.Ent.: " maxlenght="2" 
									cssclasslabel="label85 bold textright"
									showrequired="true"
									cssclass="textareaman" validator="required;accept=${configurazione_descrizione256_regex}"
									text="${tx_is_ges}" message="[accept=Cod.IS.Ent.: ${msg_configurazione_descrizione_regex}]"/>
							</s:div>
						</s:div>
						<s:div name="divCenter" cssclass="divRicMetadatiRight">
							<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" name="tx_trib_ges"
									label="Cod.Tr.Ent.: " maxlenght="4" 
									cssclasslabel="label85 bold textright"
									showrequired="true"
									cssclass="textareaman" validator="required;accept=${configurazione_descrizione256_regex}"
									text="${tx_trib_ges}" message="[accept=Cod.Tr.Ent.: ${msg_configurazione_descrizione_regex}]"/>
							</s:div>
						</s:div>	
						<s:div name="divLeft" cssclass="divRicMetadatiLeft">
						</s:div>
						<s:div name="divCenter" cssclass="divRicMetadatiCenter">
						</s:div>
						<s:div name="divCenter" cssclass="divRicMetadatiRight">
							<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" name="tx_anno_doc"
									label="Anno Doc.: " maxlenght="4" 
									cssclasslabel="label85 bold textright"
									showrequired="true"
									cssclass="textareaman" validator="required;digits;maxlength=4"
									text="${tx_anno_doc}" />
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
			<c:if test="${codop == 'edit'}">
				<input type="hidden" name="tx_provincia" value="${tx_provincia}" />
				<input type="hidden" name="tx_comune" value="${tx_comune}" />
				<input type="hidden" name="tx_ente" value="${tx_ente}" />
				<input type="hidden" name="tx_cod_ges_sel" value="${tx_cod_ges_sel}" />
				<input type="hidden" name="tx_is_ges_sel" value="${tx_is_ges_sel}" />
			</c:if>
	</s:form>
	</s:div>
	
</s:div>
