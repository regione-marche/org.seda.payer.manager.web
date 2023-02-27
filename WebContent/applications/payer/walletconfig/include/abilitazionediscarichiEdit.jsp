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
	<s:form name="form_selezione" action="abilitazionediscarichiEdit.do?vista=abilitazionediscarichiEdit" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA CONFIGURAZIONE RACCORDO PAGONET
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						<!--<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label85 bold textright" validator="ignore;"
									cssclass="floatleft" disable="${codop == 'edit'}"
									onchange="setFired();this.form.submit();"
									   cachedrowset="listaSocietaUtenteEnte" usexml="true" 
									   valueselected="${ddlSocietaUtenteEnte}">
   							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
							<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}"/>
						</s:dropdownlist>-->
						
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
			
			<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
			            <s:dropdownlist name="tx_codice_tributo2" label="Onere/tributo:" showrequired="true"
						 multiple="false" valueselected="${tx_codice_tributo2}" validator="required;" disable="${codop == 'edit'}"
						 cssclasslabel="label85 bold textright"  cssclass="tbddlMax floatleft">
						 <s:ddloption text="seleziona..." value="" />
						 <s:ddloptionbinder options="${tributiDDLList}"/>
						</s:dropdownlist>
			</s:div>
			
			<input type="hidden" name="hiddenTx_codice_tributo2" value="${tx_codice_tributo2}" />
			
			<!-- 
			<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">					
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_tipologia_servizio" disable="" label="Servizio:"
							 multiple="false" valueselected="${tx_tipologia_servizio}" 
							 cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
						 	<s:ddloption text="Tutti" value="" />
							<s:ddloptionbinder options="${serviziDDLList}"/>
						</s:dropdownlist>
					</s:div>
				</s:div>
			 -->	
				
			<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">					
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_flag_servizio" disable="" label="Discaricabile:" showrequired="true"
							 multiple="false" valueselected="${tx_flag_servizio}" validator="required;"
							 cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
							<s:ddloption text="seleziona..." value="" />
							<s:ddloptionbinder options="${flagDDLList}"/>
						</s:dropdownlist>
					</s:div>
				</s:div>
				
			
			 
				
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
			
				<s:button id="tx_button_indietro" onclick="" text="Indietro" validate="false" type="submit" cssclass="btnStyle" />
				
				<c:if test="${codop == 'edit'}">
				 <s:button id="tx_button_aggiorna"  onclick="" text="Modifica"  type="submit" cssclass="btnStyle" />	
				</c:if>
				
				<c:if test="${codop == 'add'}">
				 <s:button id="tx_button_aggiungi"  onclick="" text="Salva"  type="submit" cssclass="btnStyle" />	
				</c:if>
				
			</s:div>
	</s:form> 
	</s:div> 
	
</s:div>
 