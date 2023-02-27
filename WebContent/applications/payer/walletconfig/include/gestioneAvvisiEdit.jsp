<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="abilitazionediscarichiEdit" encodeAttributes="true" />

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
	<s:form name="form_selezione" action="gestioneAvvisiEdit.do?vista=gestioneAvvisiEdit" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold"> CONFIGURAZIONE EVOLUZIONE INTIMAZIONE
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">

						<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label160 bold textright floatleft" validator="required;"
									cssclass="seda-ui-ddl tbddlMax780 floatleft" disable="${codop == 'edit'}"
									onchange="setFiredButton();this.form.submit();"
									   cachedrowset="listaSocietaUtenteEnte" usexml="true" 
									   valueselected="${ddlSocietaUtenteEnte}">
   							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
							<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}"/>
						</s:dropdownlist>
						
						<input type="hidden" name="hiddenDdlSocietaUtenteEnte" value="${ddlSocietaUtenteEnte}" />
						<input type="hidden" name="nomeCampo" value="${nomeCampo}" />
						
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
			</s:div>
			
			<s:div name="divRicercaMetadatiLeft" cssclass="divRicMetadatiLeftAvvisi">
				<s:textbox bmodify="false"	maxlenght="100" 	validator="ignore;maxlength=50;accept=${configurazione_descrizione256_regex}"
						   label="Descrizione: " name="descAvviso" text="${descAvviso}"	message="[accept=Nome Campo: ${msg_configurazione_descrizione_regex}]"
						   cssclasslabel="label160 bold textright" cssclass="textareamanAvvisi" />
			</s:div>
 			
 			<s:div name="divRicercaMetadatiTextArea" cssclass="divRicMetadatiTextArea">
				<s:textarea name="testo" bmodify="true" label="Testo: " cssclasslabel="label160 bold textright" 	text="${testo}" cssclass="tbddlBollettinoFreccia textAreaFont floatleft backcolorgray" 
					row="10" col="44" showrequired="false" tabindex="8" validator="maxlength=5000"  />
			</s:div>	
			
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
			
				<s:button id="tx_button_indietro" onclick="" text="Indietro" validate="false" type="submit" cssclass="btnStyle" />
				
			 	<s:button id="tx_button_aggiorna"  onclick="" text="Salva"  type="submit" cssclass="btnStyle" />	
				
			</s:div>
			
	</s:form> 
	</s:div> 
	
</s:div>
 