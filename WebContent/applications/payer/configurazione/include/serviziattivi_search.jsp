<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="configutentetiposervizioentes" encodeAttributes="true" />
<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>

<script src="../applications/templates/monitoraggio/js/popup.js"
	type="text/javascript"></script>

<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Servizi Attivi</s:div>
		<s:form name="form_ricerca" action="serviziattivi.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
				<s:div name="divRicMetadati" cssclass="divRicMetadati">
					<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
	
							<s:dropdownlist name="tx_societa" disable="${ddlSocietaDisabled}"
								cssclass="tbddl floatleft" label="Societ&agrave;:"
								cssclasslabel="label85 bold floatleft textright"
								cachedrowset="listaSocieta" usexml="true"
								onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
								valueselected="${tx_societa}">
								<s:ddloption text="Tutte le Societ&agrave;" value="" />
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							<noscript><s:button id="tx_button_societa_changed"
								disable="${ddlSocietaDisabled}" onclick="" text="" type="submit"
								cssclass="btnimgStyle" title="Aggiorna" validate="false" /></noscript>
						</s:div>
	
						<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
							<s:dropdownlist name="tx_provincia"
								disable="${ddlProvinciaDisabled}" cssclass="tbddl floatleft"
								label="Provincia:"
								cssclasslabel="label65 bold floatleft textright"
								cachedrowset="listaProvince" usexml="true"
								onchange="setFiredButton('tx_button_provincia_changed');this.form.submit();"
								valueselected="${tx_provincia}">
								<s:ddloption text="Tutte le Province" value="" />
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							<noscript><s:button id="tx_button_provincia_changed"
								disable="${ddlProvinciaDisabled}" onclick="" text=""
								type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
							</noscript>
						</s:div>
	
						<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
							<s:dropdownlist name="tx_UtenteEnte"
								disable="${ddlUtenteEnteDisabled}" cssclass="tbddlMax floatleft"
								label="Ente:" cssclasslabel="label65 bold textright"
								cachedrowset="listaUtentiEnti" usexml="true"
								onchange="setFiredButton('tx_button_ente_changed');this.form.submit();"
								valueselected="${tx_UtenteEnte}">
								<s:ddloption text="Tutti gli Enti" value="" />
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							<noscript><s:button id="tx_button_ente_changed"
								disable="${ddlUtenteEnteDisabled}" onclick="" text=""
								type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false" /></noscript>
						</s:div>
					</s:div>
	
					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
						<s:div name="divElement13" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="tx_tipologia_servizio" disable="false"
								cssclass="tbddlMax floatleft" label="Tip. Servizio:"
								cssclasslabel="label85 bold textright"
								cachedrowset="listaTipologieServizio" usexml="true"
								valueselected="${tx_tipologia_servizio}">
								<s:ddloption text="Tutte le tipologie" value="" />
								<s:ddloption text="{2}" value="{1}_{3}" />
							</s:dropdownlist>
						</s:div>
					</s:div>
					
				</s:div>
				
				<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle"/>
					<s:button id="tx_button_reset" onclick="" text="Reset" validate="false" type="submit" cssclass="btnStyle"/>
					<s:textbox name="fired_button_hidden" label="fired_button_hidden" bmodify="true" text="" cssclass="rend_display_none" cssclasslabel="rend_display_none" />
				</s:div>
					
		</s:form>
	</s:div>
	
	<c:if test="${configutentetiposervizioentes != null}">
		<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
				Elenco Servizio Attivi
		</s:div>
	</c:if>
		
</s:div>
