<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<m:view_state id="seUsers_personafisica" encodeAttributes="true" />


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
		$("button#btnAvantiFamigliaMerceologica").attr("class", "seda-ui-button divDisplayNone");
		$("button#btnAvantiTipologiaAlloggio").attr("class", "seda-ui-button divDisplayNone");
	});
	
</script>	

<script type="text/javascript">

	var annoDa = ${ddlDateAnnoDa};
	var today = new Date();

	$(function() {
	$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
	initDatePicker(new Date(1900, 0, 1), today , //minDate, maxDate 
	                "1900", today.getFullYear(), //annoDa, annoA 
	                "data_nascita_day_id", "data_nascita_month_id", "data_nascita_year_id", 
	                "data_nascita_hidden");
	                
	initDatePicker(new Date(2000, 0, 1), today , //minDate, maxDate 
			        "2000", today.getFullYear(), //annoDa, annoA 
	                "data_rilascio_day_id", "data_rilascio_month_id", "data_rilascio_year_id", 
	                "data_rilascio_hidden");
	});

		$(document).ready( function() {
			//nel caso di JS attivo nascondo il pulsante per l'aggiornamento della provincia
			$("button#btnAvantiProvinciaNascita").attr("class", "seda-ui-button divDisplayNone");
			$("button#btnAvantiProvinciaResidenza").attr("class", "seda-ui-button divDisplayNone");
			$("button#btnAvantiProvinciaDomicilio").attr("class", "seda-ui-button divDisplayNone");
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

<c:set var="bImpostaSoggiorno" value="N" scope="page" />
<c:set var="classificazioneMerceologica" value="${requestScope.reg_profilo.classificazioneMerceologica}" scope="page" />
<c:if test="${requestScope.reg_profilo.personaFisica == 'Y' and requestScope.reg_profilo.numeroAutorizzazione != ''}">	
	<c:set var="bImpostaSoggiorno" value="Y" scope="page" />
	<c:set var="classificazioneMerceologica" value="55" scope="page" />
</c:if>
<c:if test="${requestScope.impostaSoggiorno == 'Y'}">
	<c:set var="bImpostaSoggiorno" value="Y" scope="page" />
	<c:set var="classificazioneMerceologica" value="55" scope="page" />
</c:if>
<c:set var="cssNumAutorizDisabled" value="" scope="page" />
<c:set var="bModifyNumAutoriz" value="Y" scope="page" />
<c:set var="tbNumAutorizReq" value="required;" scope="page" />
<c:if test="${requestScope.reg_profilo.classificazioneMerceologicaDettaglio == '55.20.61' or requestScope.reg_profilo.classificazioneMerceologicaDettaglio == '55.20.62'}">
	<c:set var="bModifyNumAutoriz" value="N" scope="page" />
	<c:set var="cssNumAutorizDisabled" value="colordisabled" scope="page" />
	<c:set var="tbNumAutorizReq" value="ignore;" scope="page" />
</c:if>

<s:div name="divRegistrazioneBody" cssclass="divRegistrazioneBody">
	<s:form name="form_personafisica" action="${do_command_name}" hasbtn1="false" hasbtn2="false" hasbtn3="false" method="POST" cssclass="formRegistrazione">	
			

		<c:if test="${codop == 'add'}">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle_adminusers1">Immettere Dati Persona Fisica/Soggetto Delegato</s:div>
		</c:if>
		<c:if test="${codop == 'edit'}">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle_adminusers1">Modifica Dati Persona Fisica/Soggetto Delegato</s:div>
		</c:if>	
		<s:div name="divDatiGenerali" cssclass="divSectionBorder">
			<s:div name="divDatiGeneraliTitle" cssclass="divSectionTitle">
				DATI GENERALI
				
				<c:if test="${requestScope.reg_profilo.personaFisica == 'Y' and requestScope.flgGiuridica != 'Y'}">	
					<s:button id="btnImpostaSoggiorno" onclick="" text="Imposta Soggiorno" type="submit"
						disable="false" validate="false"
						cssclass="btnCheckBoxImgStyle${bImpostaSoggiorno} btnCheckBoxMarginLeft3" title="" />
					<input type="hidden" name="impostaSoggiorno_hidden" value="${bImpostaSoggiorno}" />	
				</c:if>	
			</s:div>
			
			<s:div name="divLeftDatiGenerali" cssclass="divSectionLeft">
				<s:textbox label="Cognome" name="txtCognome" tabindex="1"
					bmodify="true" text="${requestScope.reg_profilo.cognome}"
					cssclass="txtRegistrazione" cssclasslabel="lblRegistrazione" maxlenght="256"
					validator="required;accept=${adminusers_descrizioneRegex256}" showrequired="true"
					message="[accept=Cognome: ${msg_configurazione_descrizione_regex}]"/>
				<s:textbox label="Codice Fiscale" name="txtCodFiscale" tabindex="3"
					bmodify="true"  text="${requestScope.reg_profilo.codiceFiscale}" maxlenght="16"
					cssclass="txtRegistrazione" cssclasslabel="lblRegistrazione"
					validator="maxlength=16;minlength=16;required;" showrequired="true"/>
			</s:div>
			<s:div name="divRightDatiGenerali" cssclass="divSectionRight">
				<s:textbox label="Nome" name="txtNome" maxlenght="50" tabindex="2"
					bmodify="true"  text="${requestScope.reg_profilo.nome}"
					cssclass="txtRegistrazione" cssclasslabel="lblRegistrazione"
					validator="required;accept=${adminusers_descrizioneRegex256}" showrequired="true"
					message="[accept=Nome: ${msg_configurazione_descrizione_regex}]"/>
				<s:div name="divSesso" cssclass="divGroup">
					<s:label name="lblSesso" cssclass="lblRegistrazione" text="Sesso *" />
					<s:list name="rbMaschio" bradio="true" groupname="grSesso" bchecked="${requestScope.reg_profilo.sesso == 'M'}"
						value="M" text="M" cssclass="rbSesso" cssclasslabel="lblSesso" tabindex="9"/>
					<s:list name="rbFemmina" bradio="true" groupname="grSesso" bchecked="${requestScope.reg_profilo.sesso == 'F'}"
						value="F" text="F" cssclass="rbSesso" cssclasslabel="lblSesso" tabindex="10"/>
				</s:div>
			</s:div>
									
			<c:if test="${bImpostaSoggiorno == 'Y' and (requestScope.reg_profilo.personaFisica == 'Y' and requestScope.flgGiuridica != 'Y')}">
				<s:div name="divBottomGenerali" cssclass="divSectionBottomClassificazione">
					<s:div name="divFamigliaMerceologica" cssclass="">
						<s:dropdownlist name="ddlFamigliaMerceologica" cssclass="ddlRegistrazioneLarge"
								label="Classificazione Merceologica" cssclasslabel="lblRegistrazione"
								disable="${bImpostaSoggiorno == 'Y'}" tabindex="8"
								cachedrowset="listfamigliemerceologiche" usexml="true" 
								valueselected="${classificazioneMerceologica}"
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
				<%--Visualizzo il codice autorizzazione solo per le imprese di categoria ALLOGGIO (cod.55)
				<c:if test="${classificazioneMerceologica == '55'}">
					<s:div name="divBottomGenerali1" cssclass="divSectionBottom">
						<s:textbox label="Num. Autoriz." name="txtNumeroAutorizzazione" maxlenght="100"
							bmodify="${pageScope.bModifyNumAutoriz == 'Y'}" text="${requestScope.reg_profilo.numeroAutorizzazione}"
							cssclass="txtRegistrazione ${pageScope.cssNumAutorizDisabled}" cssclasslabel="lblRegistrazione" tabindex="10"
							validator="${pageScope.tbNumAutorizReq}accept=${adminusers_descrizioneRegex256}" showrequired="${pageScope.tbNumAutorizReq == 'required;'}"
							message="[accept=Num. Autoriz.: ${msg_configurazione_descrizione_regex}]"/>
					</s:div>
				</c:if>
				--%>
			</c:if>
		</s:div>
		
		<c:if test="${bImpostaSoggiorno == 'Y' and (requestScope.reg_profilo.personaFisica == 'Y' and requestScope.flgGiuridica != 'Y')}">
			<s:div name="divSedeLegale" cssclass="divSectionBorder">
				<s:div name="divSedeLegaleTitle" cssclass="divSectionTitle">
					INDIRIZZO ALLOGGIO
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
		</c:if>
		
		<s:div name="divDatiNascita" cssclass="divSectionBorder">
			<s:div name="divDatiNascitaTitle" cssclass="divSectionTitle">
				DATI DI NASCITA
			</s:div>

			<s:div name="divLeftDatiNascita" cssclass="divSectionLeft">
				<s:div name="divDataNascita" cssclass="divData divHeight">
					<s:date label="Data di nascita" prefix="data_nascita" yearbegin="1900" 
						yearend="${requestScope.yearNow}" locale="IT-it" descriptivemonth="false" disabled="false"
						separator="/" calendar="${requestScope.reg_profilo.dataNascita}" showrequired="true"
						cssclasslabel="lblRegistrazione" cssclass="cssdate" validator="required;dateISO"
						message="[dateISO=Data di nascita: ${msg_dataISO_valida}]">
					</s:date>
					<input type="hidden" id="data_nascita_hidden" value="" />
				</s:div>
				
				<s:button id="btnComuneNascitaEstero" onclick="" text="Comune di nascita Estero" type="submit"
					disable="false" validate="false" tabindex="5"
					cssclass="btnCheckBoxImgStyle${requestScope.reg_profilo.comuneNascitaEstero} btnCheckBoxMarginLeft" title="" />
				
			</s:div>
			
					
			<s:div name="divRightDatiNascita" cssclass="divSectionRight">
				<s:div name="divProvincia" cssclass="block_adminusers">
					<s:dropdownlist name="ddlProvinciaNascita" cssclass="ddlRegistrazione"
							disable="${requestScope.reg_profilo.comuneNascitaEstero == 'Y'}" 
							label="Provincia" cssclasslabel="lblRegistrazione"
							cachedrowset="listprovincia" usexml="true" tabindex="6"
							valueselected="${requestScope.reg_profilo.provinciaNascitaSigla}|${requestScope.reg_profilo.provinciaNascita}"
							onchange="simulateButtonSubmit('btnAvantiProvinciaNascita');"
							validator="required" showrequired="true" >
						<s:ddloption text="seleziona..." value="" />
						<s:ddloption text="{1}" value="{2}|{1}" />
					</s:dropdownlist>
					<s:button id="btnAvantiProvinciaNascita" onclick="" text="" type="submit"
						disable="${requestScope.reg_profilo.comuneNascitaEstero == 'Y'}" validate="false"
						cssclass="btnAggiornaImgStyle" title="Seleziona Provincia" />
						
				</s:div>
				
							
				
				<s:div name="divComuneNascitaOuter" cssclass="block_adminusers">
					<s:dropdownlist name="ddlComuneNascita" disable="false" cssclass="ddlRegistrazione" 
									label="Comune" cssclasslabel="lblRegistrazione"
									cachedrowset="listcomuninascita" usexml="true" tabindex="7"
									valueselected="${requestScope.reg_profilo.comuneNascitaSelected}"
									validator="required" showrequired="true" >
						<s:ddloption text="seleziona..." value="" />
						<s:ddloption text="{1}" value="{3}|{1}" />
					</s:dropdownlist>
					 
				</s:div>
			</s:div>
		</s:div>
		
		<s:div name="divDocumento" cssclass="divSectionBorder">
			<s:div name="divDocumentoTitle" cssclass="divSectionTitle">
				DOCUMENTO
			</s:div>
			<s:div name="divLeftDocumento" cssclass="divSectionLeft">
				<s:dropdownlist name="ddlDocumento" disable="false" cssclass="ddlRegistrazione" 
								label="Documento" cssclasslabel="lblRegistrazione" tabindex="8"
								valueselected="${requestScope.reg_profilo.tipoDocumento}"
								validator="ignore" showrequired="true" >
					<s:ddloption text="seleziona..." value="" />
					<s:ddloption text="Carta d'identit&agrave;" value="CI" />
				<s:ddloption text="Patente" value="PA" />
					<s:ddloption text="Passaporto" value="PP" />
				</s:dropdownlist>
				<s:div name="divDataRilascio" cssclass="divData">
					<s:date label="Data rilascio" prefix="data_rilascio" yearbegin="2000"
						yearend="${requestScope.yearNow}" locale="IT-it" descriptivemonth="false" disabled="false"
						separator="/" calendar="${requestScope.reg_profilo.dataRilascioDocumento}" showrequired="true"
						cssclasslabel="lblRegistrazione" cssclass="cssdate" validator="ignore;dateISO"
						message="[dateISO=Data rilascio: ${msg_dataISO_valida}]">
					</s:date>
					<input type="hidden" id="data_rilascio_hidden" value="" />
				</s:div>
			</s:div>
			<s:div name="divRightDocumento" cssclass="divSectionRight">
				<s:textbox label="N° Documento" name="txtNumDocumento" maxlenght="20"
					bmodify="true" text="${requestScope.reg_profilo.numeroDocumento}"
					cssclass="txtRegistrazione" cssclasslabel="lblRegistrazione"
					validator="ignore;accept=^\w{0,20}" showrequired="true" tabindex="10"
					message="[accept=N° Documento: ${msg_configurazione_alfanumerici}]"/>
				<s:textbox label="Ente rilascio" name="txtEnteRilascio" maxlenght="256"
					bmodify="true" text="${requestScope.reg_profilo.enteRilascioDocumento}"
					cssclass="txtRegistrazione" cssclasslabel="lblRegistrazione" tabindex="11"
					validator="ignore;accept=${adminusers_descrizioneRegex256}" showrequired="true"
					message="[accept=Ente rilascio: ${msg_configurazione_descrizione_regex}]"/>
			</s:div>
		</s:div>
		
		<s:div name="divResidenza" cssclass="divSectionBorder">
			<s:div name="divResidenzaTitle" cssclass="divSectionTitle">
				RESIDENZA
			</s:div>
			<s:div name="divTopResidenza" cssclass="divSectionTop">
				<s:textbox label="Indirizzo" name="txtIndirizzoResidenza" tabindex="12"
					bmodify="true" text="${requestScope.reg_profilo.indirizzoResidenza}" maxlenght="256"
					cssclass="txtRegistrazioneMax" cssclasslabel="lblRegistrazione"
					validator="accept=${adminusers_descrizioneRegex256}"
					message="[accept=Indirizzo: ${msg_configurazione_descrizione_regex}]" />
				<s:div name="divResidenzaEstero" cssclass="divBlockLeft">
					<s:button id="btnComuneResidenzaEstero" onclick="" text="Residenza all'Estero" type="submit"
						disable="false" validate="false" tabindex="13"
						cssclass="btnCheckBoxImgStyle${requestScope.reg_profilo.comuneResidenzaEstero} btnEsteroWidth" title="" />
				</s:div>
			</s:div>
			<s:div name="divBottomResidenza" cssclass="divSectionBottom">
				<s:div name="divProvinciaResidenza" cssclass="">
					<s:dropdownlist name="ddlProvinciaResidenza" cssclass="ddlRegistrazione"
							disable="${requestScope.reg_profilo.comuneResidenzaEstero == 'Y'}" 
							label="Provincia" cssclasslabel="lblRegistrazione"
							cachedrowset="listprovincia" usexml="true" tabindex="14"
							valueselected="${requestScope.reg_profilo.provinciaResidenzaSigla}|${requestScope.reg_profilo.provinciaResidenza}"
							onchange="simulateButtonSubmit('btnAvantiProvinciaResidenza');" >
						<s:ddloption text="seleziona..." value="" />
						<s:ddloption text="{1}" value="{2}|{1}" />
					</s:dropdownlist>
					<s:button id="btnAvantiProvinciaResidenza" onclick="" text="" type="submit"
						disable="${requestScope.reg_profilo.comuneResidenzaEstero == 'Y'}" validate="false"
						cssclass="btnAggiornaImgStyle floatleft" title="Seleziona Provincia" />
				</s:div>
				<s:div name="divComuneResidenzaOuter" cssclass="">
					<s:dropdownlist name="ddlComuneResidenza" disable="false" cssclass="ddlRegistrazione" 
									label="Comune" cssclasslabel="lblRegistrazioneMin"
									cachedrowset="listcomuniresidenza" usexml="true" tabindex="15"
									valueselected="${requestScope.reg_profilo.comuneResidenzaSelected}">
						<s:ddloption text="seleziona..." value="" />
						<s:ddloption text="{1}" value="{3}|{1}" />
					</s:dropdownlist>		 
				</s:div>
				<s:textbox label="Cap" name="txtCapResidenza" tabindex="16"
					bmodify="true" text="${requestScope.reg_profilo.capResidenza}" maxlenght="5"
					cssclass="txtRegistrazioneMin" cssclasslabel="lblRegistrazioneCap"
					validator="ignore;digits;maxlength=5;minlength=5;"/>
			</s:div>
		</s:div>
		
		<s:div name="divDomicilio" cssclass="divSectionBorder">
			<s:div name="divDomicilioTitle" cssclass="divSectionTitle">
				DOMICILIO (solo se diverso dalla residenza)
			</s:div>
			<s:div name="divTopDomicilio" cssclass="divSectionTop">
				<s:textbox label="Indirizzo" name="txtIndirizzoDomicilio" tabindex="17"
					bmodify="true" text="${requestScope.reg_profilo.indirizzoDomicilio}" maxlenght="256"
					cssclass="txtRegistrazioneMax" cssclasslabel="lblRegistrazione"
					validator="accept=${adminusers_descrizioneRegex256}"
					message="[accept=Indirizzo: ${msg_configurazione_descrizione_regex}]" />
				<s:div name="divDomicilioEstero" cssclass="divBlockLeft">
					<s:button id="btnComuneDomicilioEstero" onclick="" text="Domicilio all'Estero" type="submit"
						disable="false" validate="false" tabindex="18"
						cssclass="btnCheckBoxImgStyle${requestScope.reg_profilo.comuneDomicilioEstero} btnEsteroWidth" title="" />
				</s:div>
			</s:div>
			<s:div name="divBottomDomicilio" cssclass="divSectionBottom">
				<s:div name="divProvinciaDomicilio" cssclass="">
					<s:dropdownlist name="ddlProvinciaDomicilio" cssclass="ddlRegistrazione"
							disable="${requestScope.reg_profilo.comuneDomicilioEstero == 'Y'}" 
							label="Provincia" cssclasslabel="lblRegistrazione"
							cachedrowset="listprovincia" usexml="true" tabindex="19"
							valueselected="${requestScope.reg_profilo.provinciaDomicilioSigla}|${requestScope.reg_profilo.provinciaDomicilio}"
							onchange="simulateButtonSubmit('btnAvantiProvinciaDomicilio');"  >
						<s:ddloption text="seleziona..." value="" />
						<s:ddloption text="{1}" value="{2}|{1}" />
					</s:dropdownlist>
					<s:button id="btnAvantiProvinciaDomicilio" onclick="" text="" type="submit"
						disable="${requestScope.reg_profilo.comuneDomicilioEstero == 'Y'}" validate="false"
						cssclass="btnAggiornaImgStyle floatleft" title="Seleziona Provincia" />
				</s:div>
				<s:div name="divComuneDomicilioOuter" cssclass="">
					<s:dropdownlist name="ddlComuneDomicilio" disable="false" cssclass="ddlRegistrazione" 
									label="Comune" cssclasslabel="lblRegistrazioneMin"
									cachedrowset="listcomunidomicilio" usexml="true" tabindex="20"
									valueselected="${requestScope.reg_profilo.comuneDomicilioSelected}">
						<s:ddloption text="seleziona..." value="" />
						<s:ddloption text="{1}" value="{3}|{1}" />
					</s:dropdownlist>		 
				</s:div>
				<s:textbox label="Cap" name="txtCapDomicilio" tabindex="21"
					bmodify="true" text="${requestScope.reg_profilo.capDomicilio}" maxlenght="5"
					cssclass="txtRegistrazioneMin" cssclasslabel="lblRegistrazioneCap"
					validator="ignore;digits;maxlength=5;minlength=5;" />
			</s:div>
		</s:div>
		
		<s:div name="divContatti" cssclass="divSectionBorder">
			<s:div name="divContattiTitle" cssclass="divSectionTitle">
				CONTATTI
			</s:div>
			<s:div name="divLeftContatti" cssclass="divSectionLeft">
				<s:textbox label="e-mail" name="txtEmail" tabindex="22"
					bmodify="true" text="${requestScope.reg_profilo.email}" maxlenght="55"
					cssclass="txtRegistrazione" cssclasslabel="lblRegistrazione"
					validator="ignore;accept=${adminusers_email_regex}"
					message="[accept=e-mail: ${msg_configurazione_email}]"/>
				<s:textbox label="Cellulare" name="txtCellulare" tabindex="23"
					bmodify="true" text="${requestScope.reg_profilo.cellulare}" maxlenght="15"
					cssclass="txtRegistrazione" cssclasslabel="lblRegistrazione"
					validator="ignore;digits"/>
				<s:textbox label="Fax" name="txtFax" tabindex="26"
					bmodify="true" text="${requestScope.reg_profilo.fax}" maxlenght="15"
					cssclass="txtRegistrazione" cssclasslabel="lblRegistrazione"
					validator="ignore;digits"/>
			</s:div>
			<s:div name="divRightContatti" cssclass="divSectionRight">
				<s:textbox label="e-mail PEC" name="txtEmailPEC" tabindex="24"
					bmodify="true" text="${requestScope.reg_profilo.emailPEC}" maxlenght="55"
					cssclass="txtRegistrazione" cssclasslabel="lblRegistrazione"
					validator="ignore;accept=${adminusers_email_regex}"
					message="[accept=e-mail PEC: ${msg_configurazione_email}]"/>
				<s:textbox label="Telefono" name="txtTelefono" tabindex="25"
					bmodify="true" text="${requestScope.reg_profilo.telefono}" maxlenght="15"
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
				<s:button id="tx_button_salva" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
		</s:div>
	</s:form>
</s:div>


