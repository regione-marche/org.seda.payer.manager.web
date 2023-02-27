<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="walletanagraficacontribuenti" encodeAttributes="true" />


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
	<s:form name="form_selezione" action="walletanagraficacontribuenti.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA ANAGRAFICA DEI CONTRIBUENTI</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label85 bold textright" validator="ignore;"
									cssclass="floatleft" disable="${codop == 'edit'}"
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
						<s:textbox bmodify="true" name="tx_idwallet"
							label="Codice Bors.:" maxlenght="18" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=^[0-9a-zA-Z\-]{1,18}$"
							message="[accept=Codice Bors.: ${msg_configurazione_descrizione_3}]"
							text="${tx_idwallet}" />
					</s:div>
					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
						
						<s:textbox bmodify="true" name="tx_idBollettino"
							label="Codice Bollettino.:" maxlenght="18" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=^[0-9a-zA-Z\-]{1,18}$"
							message="[accept=Codice Bollettino.: ${msg_configurazione_descrizione_3}]"
							text="${tx_idBollettino}" />
						
					</s:div>
					
					<s:div name="divElement14" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_sepa"
							label="Codice SEPA:" maxlenght="35" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=^[0-9a-zA-Z\-]{1,22}$"
							message="[accept=Codice SEPA: ${msg_configurazione_descrizione_3}]"
							text="${tx_sepa}" />
					</s:div>
					
				</s:div>
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_codicefiscalegenitore"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							label="Codice Fiscale:" maxlenght="16"
							text="${tx_codicefiscalegenitore}" />
					</s:div>
					
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
					
					<s:dropdownlist name="tx_anagrafica_da_bonificare" disable="false"
							cssclass="tbddlMax floatleft" label="Anag. Bon.:"
							cssclasslabel="label85 bold textright"
							valueselected="${tx_anagrafica_da_bonificare}">
							<s:ddloption text="Tutti" value="" />
							<s:ddloption text="Rivestite" value="R" />
							<s:ddloption text="Mai rivestite" value="M" />
							<s:ddloption text="Cap non valido" value="C" />
							<s:ddloption text="Countrycode non valido" value="A" />
							<s:ddloption text="Ind. troncato in stampa" value="S" />
							<s:ddloption text="Ind. incompleto" value="I" />
						</s:dropdownlist>
					
							
					</s:div>
				</s:div>

				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_denominazione"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							label="Denominaz.:" maxlenght="61"
							text="${tx_denominazione}" />
					</s:div>
					
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_flagAttivazione" disable="false"
							cssclass="tbddlMax floatleft" label="Attivi:"
							cssclasslabel="label85 bold textright"
							valueselected="${tx_flagAttivazione}">
							<s:ddloption text="Tutti" value="" />
							<s:ddloption text="Si" value="Y" />
							<s:ddloption text="No" value="N" />
							</s:dropdownlist>
					</s:div>
				</s:div>
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" text="Reset" validate="false" type="submit" cssclass="btnStyle" />
				<c:if test="${!empty lista_wallet}">
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

<c:if test="${!empty lista_wallet}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO ANAGRAFICA DEI CONTRIBUENTI
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_wallet"
			action="walletanagraficacontribuenti.do?vista=walletanagraficacontribuenti" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			
			<s:dgcolumn label="Identificativo Borsellino" asc="BRS_KBRSKBRS_A" desc="BRS_KBRSKBRS_D">
			<s:hyperlink
					href="walletmonitoraggio.do?tx_idwallet={4}&tx_daanacont=Y"					
					alt="Monitoraggio Borsellino" text="{4}"
					cssclass="blacklink hlStyle" />
					
			</s:dgcolumn>		
			
			<s:dgcolumn index="5" label="Codice Fiscale" asc="BRS_CFISCGEN_A" desc="BRS_CFISCGEN_D"></s:dgcolumn>
			<s:dgcolumn index="6" label="Denominazione" asc="BRS_DBRSGENI_A" desc="BRS_DBRSGENI_D"></s:dgcolumn>
			<s:dgcolumn index="7" label="Indirizzo" asc="BRS_DBRSINDI_A" desc="BRS_DBRSINDI_D"></s:dgcolumn>
			<s:dgcolumn index="9" label="Stato Anagrafica" ></s:dgcolumn>
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;">
				<s:hyperlink
					href="walletanagraficacontribuentiedit.do?Idwallet={4}&ddlSocietaUtenteEnte={1}|{2}|{3}"					
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Dettaglio Anagrafica" text=""
					cssclass="blacklink hlStyle" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>





