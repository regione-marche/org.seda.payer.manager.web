<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="tipologiabanco" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>

 
<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script> 



<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="form_selezione" action="tipologiabanco.do?vista=tipologiabanco" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA CONFIGURAZIONE TIPOLOGIA BANCO
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						
						<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label160 bold textright floatleft" validator="ignore;"
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
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">			
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_tipologia_banco"
							label="Codice Tipologia:" maxlenght="10" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;maxlength=10"
							text="${tx_tipologia_banco}" />
					</s:div>
				</s:div>
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">			
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_desc_banco"
							label="Descrizione Tip. Banco:" maxlenght="20" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;maxlength=70"
							text="${tx_desc_banco}" />
					</s:div>
				</s:div>					
			</s:div>
				
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_add" validate="false"  onclick="" text="Nuovo" type="submit" cssclass="btnStyle" />
				<c:if test="${!empty lista_configurazioni_tipbanco}">
					<s:button id="tx_button_download" onclick="" text="Download" cssclass="btnStyle" type="submit" />
				</c:if>
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

<c:if test="${!empty lista_configurazioni_tipbanco}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO TIPOLOGIA BANCO
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_configurazioni_tipbanco"
			action="tipologiabanco.do?vista=tipologiabancoLista" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			
			<s:dgcolumn index="4" label="Cod. Tipologia" css="text_align_left"/>
			<s:dgcolumn index="5" label="Descrizione Tipologia Banco" css="text_align_left"/>
			<s:dgcolumn label="Azioni">
				<s:hyperlink
					href="tipologiabancoEdit.do?vista=tipologiabanco&codop=edit&ddlSocietaUtenteEnte={1}|{2}|{3}&tx_tipologia_banco={4}"
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Modifica" text=""
					cssclass="blacklink hlStyle" />
				<s:hyperlink
					href="tipologiabancoEdit.do?vista=tipologiabanco&tx_button_cancel=&ddlSocietaUtenteEnte={1}|{2}|{3}&tx_tipologia_banco={4}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text=""
					cssclass="blacklink hlStyle" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
	
	
</c:if>