<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="ioitalia" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>

<script type="text/javascript">
	function confermaCancellazione(param1, param2) {
	
		if(confirm('Confermi di voler cancellare?')) window.location.replace("ioitaliaforniture.do?vista=ioitalia&tx_button_cancel&tx_id=" + param1 + "&tx_es=" + param2);
		else return false;
		
	}

</script>
<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_selezione" action="ioitaliaforniture.do?vista=ioitalia"
			method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA MESSAGGI E DOWNLOAD CSV APP IO
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" name="tx_societa_desc" label="Società:"
							maxlenght="16" cssclasslabel="label85 bold textright floatleft"
							cssclass="textareaman" bdisable="${disabilita}"
							text="${tx_societa_desc}" />
					</s:div>
					<br /><br />
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" name="tx_tipServ"
							label="Tipologia Servizio:" maxlenght="16"
							cssclasslabel="label85 bold textright floatleft"
							cssclass="textareaman" bdisable="${disabilita}"
							text="${tx_tipServ_desc}" />
					</s:div>
					<br /><br />
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" name="tx_fornitura"
							label="Fornitura:" maxlenght="16"
							cssclasslabel="label85 bold textright floatleft"
							cssclass="textareaman" bdisable="${disabilita}"
							text="${tx_fornitura}" />
					</s:div>
					<br /><br />
				</s:div>
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" name="tx_provincia"
							label="Provincia:" maxlenght="16" bdisable="${disabilita}"
							cssclasslabel="label85 bold textright floatleft"
							cssclass="textareaman"
							text="${tx_provincia_desc}" />
					</s:div>
					<br /><br />
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" name="tx_impServ" label="Imposta Servizio:"
							maxlenght="16" cssclasslabel="label85 bold textright floatleft"
							cssclass="textareaman" bdisable="${disabilita}"
							text="${tx_impServ_desc}" />
					</s:div>
					<br /><br />
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_cf" label="Codice Fiscale:"
							maxlenght="16" cssclasslabel="label85 bold textright floatleft"
							cssclass="textareaman"
							text="${tx_cf}" />
					</s:div>
					<br /><br />
				</s:div>
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" name="tx_UtenteEnte" label="Ente:"
							maxlenght="16" cssclasslabel="label85 bold textright floatleft"
							cssclass="textareaman" bdisable="${disabilita}"
							text="${tx_UtenteEnte_desc}" />
					</s:div>
					<br /><br />
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="false" name="tx_data" label="Data:"
							maxlenght="16" cssclasslabel="label85 bold textright floatleft"
							cssclass="textareaman" bdisable="${disabilita}"
							text="${tx_data}" />
					</s:div>
					<br /><br />
					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_esito" disable="${ddlSocietaDisabled}"
							cssclass="tbddl floatleft" label="Esito:"
							cssclasslabel="label85 bold textright floatleft"
							onchange=""
							valueselected="${tx_esito}">
							<s:ddloption text="" value="" />
							<s:ddloption text="OK" value="OK" />
							<s:ddloption text="KO" value="KO" />
						</s:dropdownlist>
					</s:div>
					<br /><br />
				</s:div>
				
			</s:div>
			<br />
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
					
						<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit"
							cssclass="btnStyle" />
						<s:button id="tx_button_reset" validate="false" onclick=""
							text="Reset" type="submit" cssclass="btnStyle" />
						<s:button id="tx_button_nuovo" validate="false" onclick=""
							text="Nuovo" type="submit" cssclass="btnStyle" />
						<s:button id="tx_button_download" validate="false" onclick=""
							text="Download" type="submit" cssclass="btnStyle" />
						<s:button id="tx_button_edit_end" validate="false" onclick=""
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

<c:if test="${!empty lista_messaggi}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO MESSAGGI
	</s:div>

	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_messaggi"
			action="ioitaliaforniture.do?vista=ioitalia" border="1"
			usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			<s:dgcolumn index="1" label="Utente" asc="MES_CUTECUTE ASC" desc="MES_CUTECUTE DESC"></s:dgcolumn>
			<s:dgcolumn index="2" label="IdDominio" asc="MES_CMESIDDO ASC" desc="MES_CMESIDDO DESC"></s:dgcolumn>
			<s:dgcolumn index="22" label="Tipologia Servizio" asc="MES_CMESTPSE ASC" desc="MES_CMESTPSE DESC"></s:dgcolumn>
			<s:dgcolumn index="21" label="Imposta Servizio" asc="MES_CISECISE ASC" desc="MES_CISECISE DESC"></s:dgcolumn>
			<s:dgcolumn index="6" label="Codice Fiscale" asc="MES_CMESCFIS ASC" desc="MES_CMESCFIS DESC"></s:dgcolumn>
			<s:dgcolumn index="9" label="Scadenza Messaggio" asc="MES_GMESSCME ASC" desc="MES_GMESSCME DESC"></s:dgcolumn>
			<s:dgcolumn index="7" label="Oggetto" asc="MES_CMESOGME ASC" desc="MES_CMESOGME DESC"></s:dgcolumn>
			<s:dgcolumn index="23" label="Esito" asc="ESITO ASC" desc="ESITO DESC"></s:dgcolumn>
			<s:dgcolumn label="Azioni">
				<s:hyperlink
					href="ioitaliamessageedit.do?vista=ioitalia&codop=edit&tx_id={20}&disabilita=true"
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Check" text="" cssclass="blacklink hlStyle" />
				<s:if right="{14}" control="eq" left="0">
					<s:then>
						<s:hyperlink 
							href="ioitaliaforniture.do?vista=ioitalia&tx_button_cancel&tx_id={20}&tx_es={14}" 
							imagesrc="../applications/templates/shared/img/cancel.png" 
							alt="Delete" text="" cssclass="blacklink hlStyle" />
					</s:then>
				</s:if>
			
										
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>