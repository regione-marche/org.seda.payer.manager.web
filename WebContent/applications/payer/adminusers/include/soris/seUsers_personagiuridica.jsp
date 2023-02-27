<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<m:view_state id="seUsers_personagiuridica" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>

<script type="text/javascript">

	$(document).ready( function() {
		//nel caso di JS attivo nascondo il pulsante per l'aggiornamento della provincia
		$("button#btnAvantiProvinciaSedeLegale").attr("class", "seda-ui-button divDisplayNone");
		$("button#btnAvantiProvinciaSedeOperativa").attr("class", "seda-ui-button divDisplayNone");
		$("button#btnAvantiFamigliaMerceologica").attr("class", "seda-ui-button divDisplayNone");
		$("button#btnAvantiTipologiaAlloggio").attr("class", "seda-ui-button divDisplayNone");
	});
	
</script>

<c:choose>
	<c:when test="${requestScope.reg_profilo.comuneSedeLegaleEstero == 'N'}">
		<c:set var="txtCapSedeLegaleReq" value="required;" scope="page" />
	</c:when>
	<c:otherwise>
		<c:set var="txtCapSedeLegaleReq" value="ignore;" scope="page" />
	</c:otherwise>
</c:choose>

<c:set var="cssNumAutorizDisabled" value="" scope="page" />
<c:set var="bModifyNumAutoriz" value="Y" scope="page" />
<c:set var="tbNumAutorizReq" value="required;" scope="page" />
<c:if test="${requestScope.reg_profilo.classificazioneMerceologicaDettaglio == '55.20.61' or requestScope.reg_profilo.classificazioneMerceologicaDettaglio == '55.20.62'}">
	<c:set var="bModifyNumAutoriz" value="N" scope="page" />
	<c:set var="cssNumAutorizDisabled" value="colordisabled" scope="page" />
	<c:set var="tbNumAutorizReq" value="ignore;" scope="page" />
</c:if>


<s:div name="divRegistrazioneBody" cssclass="divRegistrazioneBody">
	<s:form name="form_personagiuridica" action="${do_command_name}" hasbtn1="false" hasbtn2="false" hasbtn3="false" method="POST" cssclass="formRegistrazione">
			
		<c:if test="${codop == 'add'}">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle_adminusers1">Immettere Dati Azienda</s:div>
		</c:if>
		<c:if test="${codop == 'edit'}">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle_adminusers1">Modifica Dati Azienda</s:div>
		</c:if>	
		<s:div name="divDatiGenerali" cssclass="divSectionBorder">
			<s:div name="divDatiGeneraliTitle" cssclass="divSectionTitle">
				DATI GENERALI
			</s:div>
			<s:div name="divLeftDatiGenerali" cssclass="divSectionLeft">
				<s:textbox label="P. IVA / Cod. Fiscale" name="txtPartitaIVA" maxlenght="16"
					bmodify="true"  text="${requestScope.reg_profilo.partitaIVA}"
					validator="required;minlength=11;accept=${adminusers_codFiscPIvaRegex};maxlength=16" showrequired="true" tabindex="6"
					cssclass="txtRegistrazione" cssclasslabel="lblRegistrazione"
					message="[accept=P. IVA / Cod. Fiscale: ${msg_configurazione_alfanumerici}]"/>
			</s:div>
			<s:div name="divRightDatiGenerali" cssclass="divSectionRight">
				<s:textbox label="Ragione Sociale" name="txtRagioneSociale" maxlenght="256"
					bmodify="true"  text="${requestScope.reg_profilo.ragioneSociale}"
					cssclass="txtRegistrazione" cssclasslabel="lblRegistrazione" tabindex="7"
					validator="required;accept=${adminusers_descrizioneRegex256}" showrequired="true"
					message="[accept=Ragione Sociale: ${msg_configurazione_ragionesociale_regex}]"/>
			</s:div>
			<s:div name="divBottomGenerali" cssclass="divSectionBottomClassificazione">
				<s:div name="divFamigliaMerceologica" cssclass="">
					<s:dropdownlist name="ddlFamigliaMerceologica" cssclass="ddlRegistrazioneLarge"
							label="Classificazione Merceologica" cssclasslabel="lblRegistrazione"
							disable="false" tabindex="8"
							cachedrowset="listfamigliemerceologiche" usexml="true" 
							valueselected="${requestScope.reg_profilo.classificazioneMerceologica}"
							onchange="simulateButtonSubmit('btnAvantiFamigliaMerceologica');"  
							validator="required;" showrequired="true">
						<s:ddloption text="seleziona..." value="" />
						<s:ddloption text="{2}" value="{1}" />
					</s:dropdownlist>
					<s:button id="btnAvantiFamigliaMerceologica" onclick="" text="" type="submit"
						disable="false" validate="false"
						cssclass="btnAggiornaImgStyle floatleft_adminusers" title="Seleziona Classificazione Merceologica" />
				</s:div>
				<s:dropdownlist name="ddlCategoriaMerceologica" cssclass="ddlRegistrazioneLarge ddlClassificazione"
						label="&nbsp" cssclasslabel="displayNone"
						disable="false" tabindex="9"
						cachedrowset="listcategoriemerceologiche" usexml="true" 
						valueselected="${requestScope.reg_profilo.classificazioneMerceologicaDettaglio}"
						onchange="simulateButtonSubmit('btnAvantiTipologiaAlloggio');"  
						validator="required;" showrequired="true"
						message="[required=Classificazione Merceologica: Questo valore &egrave; obbligatorio]">
					<s:ddloption text="seleziona..." value="" />
					<s:ddloption text="{2}" value="{1}" />
				</s:dropdownlist>
				<s:button id="btnAvantiTipologiaAlloggio" onclick="" text="" type="submit"
						disable="false" validate="false"
						cssclass="btnAggiornaImgStyle floatleft_adminusers" title="Seleziona Dettaglio Classificazione Merceologica" />
				
			</s:div>
			<%--Visualizzo il codice autorizzazione solo per le imprese di categoria ALLOGGIO (cod.55) --%>
			<c:if test="${requestScope.reg_profilo.classificazioneMerceologica == '55'}">
				<s:div name="divBottomGenerali1" cssclass="divSectionBottom">
					<s:textbox label="Num. Autoriz." name="txtNumeroAutorizzazione" maxlenght="100"
						bmodify="${pageScope.bModifyNumAutoriz == 'Y'}" text="${requestScope.reg_profilo.numeroAutorizzazione}"
						cssclass="txtRegistrazione ${pageScope.cssNumAutorizDisabled}" cssclasslabel="lblRegistrazione" tabindex="10"
						validator="${pageScope.tbNumAutorizReq}accept=${adminusers_descrizioneRegex256}" showrequired="${pageScope.tbNumAutorizReq == 'required;'}"
						message="[accept=Num. Autoriz.: ${msg_configurazione_descrizione_regex}]"/>
				</s:div>
			</c:if>
		</s:div>
		
		<s:div name="divSedeLegale" cssclass="divSectionBorder">
			<s:div name="divSedeLegaleTitle" cssclass="divSectionTitle">
				SEDE LEGALE
			</s:div>
			<s:div name="divTopSedeLegale" cssclass="divSectionTop">
				<s:textbox label="Indirizzo" name="txtIndirizzoSedeLegale" maxlenght="256"
					bmodify="true" text="${requestScope.reg_profilo.indirizzoSedeLegale}"
					cssclass="txtRegistrazioneMax" cssclasslabel="lblRegistrazione" tabindex="11"
					validator="required;accept=${adminusers_descrizioneRegex256}" showrequired="true"
					message="[accept=Indirizzo: ${msg_configurazione_descrizione_regex}]" />
				<s:div name="divSedeLegaleEstero" cssclass="divBlockLeft">
					<s:button id="btnComuneSedeLegaleEstero" onclick="" text="Sede Legale all'Estero" type="submit"
						disable="false" validate="false" tabindex="12"
						cssclass="btnCheckBoxImgStyle${requestScope.reg_profilo.comuneSedeLegaleEstero} btnEsteroWidth" title="" />
				</s:div>
			</s:div>
			<s:div name="divBottomSedeLegale" cssclass="divSectionBottom">
				<s:div name="divProvinciaSedeLegale" cssclass="floatleft_adminusers">
					<s:dropdownlist name="ddlProvinciaSedeLegale" cssclass="ddlRegistrazione"
							disable="${requestScope.reg_profilo.comuneSedeLegaleEstero == 'Y'}" 
							label="Provincia" cssclasslabel="lblRegistrazione"
							cachedrowset="listprovincia" usexml="true" tabindex="13"
							valueselected="${requestScope.reg_profilo.provinciaSedeLegaleSigla}|${requestScope.reg_profilo.provinciaSedeLegale}"
							onchange="simulateButtonSubmit('btnAvantiProvinciaSedeLegale');" 
							validator="required;" showrequired="true">
						<s:ddloption text="seleziona..." value="" />
						<s:ddloption text="{1}" value="{2}|{1}" />
					</s:dropdownlist>
					<s:button id="btnAvantiProvinciaSedeLegale" onclick="" text="" type="submit"
						disable="${requestScope.reg_profilo.comuneSedeLegaleEstero == 'Y'}" validate="false"
						cssclass="btnAggiornaImgStyle" title="Seleziona Provincia" />
				</s:div>
				<s:div name="divComuneSedeLegaleOuter" cssclass="">
					<s:dropdownlist name="ddlComuneSedeLegale" disable="false" cssclass="ddlRegistrazione" 
									label="Comune" cssclasslabel="lblRegistrazioneMin"
									cachedrowset="listcomunisedelegale" usexml="true" tabindex="14"
									valueselected="${requestScope.reg_profilo.comuneSedeLegaleSelected}"
									validator="required;" showrequired="true" >
						<s:ddloption text="seleziona..." value="" />
						<s:ddloption text="{1}" value="{3}|{1}" />
					</s:dropdownlist>		 
				</s:div>
				<s:textbox label="Cap" name="txtCapSedeLegale" tabindex="15"
					bmodify="true" text="${requestScope.reg_profilo.capSedeLegale}" maxlenght="5"
					cssclass="txtRegistrazioneMin" cssclasslabel="lblRegistrazioneCap"
					validator="${pageScope.txtCapSedeLegaleReq}digits;maxlength=5;minlength=5;" showrequired="true"/>
			</s:div>
		</s:div>
		
		<s:div name="divSedeOperativa" cssclass="divSectionBorder">
			<s:div name="divSedeOperativaTitle" cssclass="divSectionTitle">
				SEDE OPERATIVA (solo se diversa dalla sede legale)
			</s:div>
			<s:div name="divTopSedeOperativa" cssclass="divSectionTop">
				<s:textbox label="Indirizzo" name="txtIndirizzoSedeOperativa" maxlenght="256"
					bmodify="true" text="${requestScope.reg_profilo.indirizzoSedeOperativa}"
					cssclass="txtRegistrazioneMax" cssclasslabel="lblRegistrazione"
					validator="accept=${adminusers_descrizioneRegex256}" tabindex="16"
					message="[accept=Indirizzo: ${msg_configurazione_descrizione_regex}]"/>
			</s:div>
			<s:div name="divBottomSedeOperativa" cssclass="divSectionBottom">
				<s:div name="divProvinciaSedeOperativa" cssclass="floatleft_adminusers">
					<s:dropdownlist name="ddlProvinciaSedeOperativa" disable="false" cssclass="ddlRegistrazione" 
							label="Provincia" cssclasslabel="lblRegistrazione"
							cachedrowset="listprovincia" usexml="true" tabindex="17"
							valueselected="${requestScope.reg_profilo.provinciaSedeOperativaSigla}|${requestScope.reg_profilo.provinciaSedeOperativa}"
							onchange="simulateButtonSubmit('btnAvantiProvinciaSedeOperativa');"  >
						<s:ddloption text="seleziona..." value="" />
						<s:ddloption text="{1}" value="{2}|{1}" />
					</s:dropdownlist>
					<s:button id="btnAvantiProvinciaSedeOperativa" onclick="" text="" type="submit"
						disable="false" validate="false"
						cssclass="btnAggiornaImgStyle" title="Seleziona Provincia" />
				</s:div>
				<s:div name="divComuneSedeOperativaOuter" cssclass="">
					<s:dropdownlist name="ddlComuneSedeOperativa" disable="false" cssclass="ddlRegistrazione" 
									label="Comune" cssclasslabel="lblRegistrazioneMin" tabindex="18"
									cachedrowset="listcomunisedeoperativa" usexml="true" 
									valueselected="${requestScope.reg_profilo.comuneSedeOperativaSelected}">
						<s:ddloption text="seleziona..." value="" />
						<s:ddloption text="{1}" value="{3}|{1}" />
					</s:dropdownlist>		 
				</s:div>
				<s:textbox label="Cap" name="txtCapSedeOperativa" maxlenght="5" tabindex="19"
					bmodify="true" text="${requestScope.reg_profilo.capSedeOperativa}"
					cssclass="txtRegistrazioneMin" cssclasslabel="lblRegistrazioneCap"
					validator="ignore;digits;maxlength=5;minlength=5;"/>
			</s:div>
		</s:div>
		
		<s:div name="divContatti" cssclass="divSectionBorder">
			<s:div name="divContattiTitle" cssclass="divSectionTitle">
				CONTATTI
			</s:div>
			<s:div name="divLeftContatti" cssclass="divSectionLeft">
				<s:textbox label="e-mail" name="txtEmail" tabindex="20"
					bmodify="true" text="${requestScope.reg_profilo.emailImpresa}" maxlenght="50"
					cssclass="txtRegistrazione" cssclasslabel="lblRegistrazione"
					validator="ignore;accept=${adminusers_email_regex}"
					message="[accept=e-mail: ${msg_configurazione_email}]"/>
				<s:textbox label="Cellulare" name="txtCellulare" tabindex="22"
					bmodify="true" text="${requestScope.reg_profilo.cellulareImpresa}" maxlenght="15"
					cssclass="txtRegistrazione" cssclasslabel="lblRegistrazione"
					validator="ignore;digits"/>
				<s:textbox label="Fax" name="txtFax" tabindex="24"
					bmodify="true" text="${requestScope.reg_profilo.faxImpresa}" maxlenght="15"
					cssclass="txtRegistrazione" cssclasslabel="lblRegistrazione"
					validator="required;digits" showrequired="true"/>
			</s:div>
			<s:div name="divRightContatti" cssclass="divSectionRight">
				<s:textbox label="e-mail PEC" name="txtEmailPEC" tabindex="21"
					bmodify="true" text="${requestScope.reg_profilo.emailPECImpresa}" maxlenght="50"
					cssclass="txtRegistrazione" cssclasslabel="lblRegistrazione"
					validator="ignore;accept=${adminusers_email_regex}"
					message="[accept=e-mail PEC: ${msg_configurazione_email}]"/>
				<s:textbox label="Telefono" name="txtTelefono" tabindex="23"
					bmodify="true" text="${requestScope.reg_profilo.telefonoImpresa}" maxlenght="15"
					cssclass="txtRegistrazione" cssclasslabel="lblRegistrazione"
					validator="ignore;digits"/>
			</s:div>
		</s:div>
		
		<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_step1" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_avanti" onclick="" text="Avanti" type="submit" cssclass="btnStyle" />
		</s:div>

	</s:form>
</s:div>

