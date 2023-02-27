<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="impostaSoggiornoFasciaTariffa_var" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js" type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js" type="text/javascript"></script>

<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}

</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="var_fascia_tariffa_form" action="ImpostaSoggiornoFasciaTariffaInsUpd.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">INSERIMENTO FASCIA TARIFFA</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">MODIFICA FASCIA TARIFFA</s:div>
				<input type="hidden" name="tx_chiaveFasciaTar" value="${tx_chiaveFasciaTar}" />
			</c:if>
		    <input type="hidden" name="tx_chiave_tariffa" value="${tx_chiave_tariffa}" />
			<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
				<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
					<s:dropdownlist name="tx_provincia"
						disable="true" 
						label="Provincia:"
						cssclasslabel="label85 bold floatleft textright"
						cssclass="tbddl floatleft"
						cachedrowset="listProvince" usexml="true"
						valueselected="${tx_provincia}">
						<s:ddloption text="Selezionare uno degli elementi" value="" />
						<s:ddloption  value="{2}" text="{1}"/>
					</s:dropdownlist>
				</s:div>
				<s:div name="divElement2" cssclass="divRicMetadatiCenterSmall">
					<s:dropdownlist name="tx_comune"
						disable="true" 
						label="Comune:"
						cssclasslabel="label65 bold textright"
						cssclass="tbddl"
						cachedrowset="listbelfiore" usexml="true"
						valueselected="${tx_comune}">
						<s:ddloption text="Selezionare uno degli elementi" value="" />
						<s:ddloption  value="{1}" text="{4}"/>
					</s:dropdownlist>
				</s:div>
				<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
					<s:dropdownlist name="tx_struttura"
						disable="true" 
						label="Struttura Ricettiva:"
						cssclasslabel="label85 bold floatleft textright"
						cssclass="tbddl floatleft"
						cachedrowset="listStrutture" usexml="true"
						valueselected="${tx_struttura}">
						<s:ddloption text="Selezionare uno degli elementi" value="" />
						<s:ddloption  value="{1}" text="{2}"/>
					</s:dropdownlist>
				</s:div>
			</s:div>
		    
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicercaMetadatiTop" cssclass="floatleft">
					<c:if test="${codop == 'add'}">
						<s:div name="divElement11" cssclass="divRicMetadatiLeft">
						    <s:textbox name="tx_codice" label="Codice:"
						       cssclasslabel="label65 bold floatleft textright"
						       cssclass="textareaman textleft floatleft"
				               bmodify="true"
				    		   bdisable="false" 
				               text="${tx_codice}" showrequired="true"
					           maxlenght="2" validator="required;accept=^[0-9]{2};maxlength=2" 
					           message="[required=Codice: Questo valore è obbligatorio£accept=Codice: ${msg_configurazione_codice_fascia_tariffa}]"
				               />
						</s:div>
					</c:if>
					<c:if test="${codop == 'edit'}">
						<s:div name="divElement11" cssclass="divRicMetadatiLeft">
						    <s:textbox name="tx_codice" label="Codice:"
						       cssclasslabel="label85 bold floatleft textright"
						       cssclass="textareaman textleft floatleft"
						       bmodify="false"
						       bdisable="true"
						       text="${tx_codice}"/>
						</s:div>
					</c:if>

					<s:div name="divElement12" cssclass="divRicMetadatiCenter">
						<s:textbox name="tx_descrizione" label="Fascia:"
						   cssclasslabel="label65 bold textright"
						   cssclass="textareaman textleft floatleft"
						   bmodify="${edit_fascia_tariffa == null || edit_fascia_tariffa == 'Y'}"
			    		   bdisable="${edit_fascia_tariffa != null && edit_fascia_tariffa == 'N'}" 
			               text="${tx_descrizione}" showrequired="true"
				           maxlenght="120" validator="required;maxlength=120"
				           message="[required=Fascia: Questo valore è obbligatorio]" 
			               />
					</s:div>

					<s:div name="divElement13" cssclass="divRicMetadatiRight">
						<s:textbox name="tx_importo" label="Importo:"
						   cssclasslabel="label85 bold floatleft textright"
						   cssclass="textareaman textright"   
						   bmodify="${edit_fascia_tariffa == null || edit_fascia_tariffa == 'Y'}" 
						   bdisable="${edit_fascia_tariffa != null && edit_fascia_tariffa == 'N'}"
						   text="${tx_importo}" showrequired="true"
				           maxlenght="16" validator="required;accept=^[0-9]{1,13}\,[0-9]{2}?$;maxlength=16" 
				           message="[required=Importo: Questo valore è obbligatorio£accept=Importo: ${msg_configurazione_importo_13_2}]"
				           />
					</s:div>
				</s:div>	
			</s:div>
			<br/>
			<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden" bmodify="true" text="" cssclass="rend_display_none" cssclasslabel="rend_display_none" />
				<s:button id="tx_button_indietro" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />
				<c:if test="${empty tx_chiaveFasciaTar}">
					<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
					<s:button id="tx_button_aggiungi_end" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${!empty tx_chiaveFasciaTar && (edit_fascia_tariffa == null || edit_fascia_tariffa == 'Y')}">
					<s:button id="tx_button_edit" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
					<s:button id="tx_button_edit_end" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
			</s:div>
		</s:form>
	</s:div>
</s:div>