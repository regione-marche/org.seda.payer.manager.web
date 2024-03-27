<%@ taglib
  prefix="c"
  uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib
  prefix="x"
  uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib
  prefix="fmt"
  uri="/WEB-INF/tld/fmt.tld"%>
<%@ taglib
  prefix="s"
  uri="/WEB-INF/tld/Seda.tld"%>
<%@ taglib
  prefix="m"
  uri="/WEB-INF/tld/maftags.tld"%>
<%@ taglib
  prefix="template"
  uri="/WEB-INF/tld/template.tld"%>
  
 <%-- LP PG200360 Nota e' stato inserito nei controlli per disabilitare alcuni textbox e bottoni il flag editDocumento.modalitaAggiornamento --%>  

<%-- Salvo gli attributi in modo che il framework possa ridisegnare la pagina a seguito di errori validazione --%>
<m:view_state
  id="gestioneDocumntiCarico_var"
  encodeAttributes="true" />

<%-- Se volessi utilizzare script nella Pagina... cerco di evitare e utilizzo JSTL. Mi fa comodo
		solo per il completion dei nomi. --%>

<jsp:useBean
  id="editDocumento"
  scope="request"
  type="org.seda.payer.manager.entrate.actions.GestioneDocumentiCaricoAction.StatoEditDocumento" />
<%--
  editDocumento.listaScadenze.get(0);
--%>

<fmt:setLocale value="it_IT" />


<%-- boolean DEBUG=true; --%>
<c:set
  var="DEBUG"
  scope="page"
  value="false" />


<script
  src="../applications/js/jquery-min.js"
  type="text/javascript"></script>
<script
  src="../applications/js/jquery-ui-custom.min.js"
  type="text/javascript"></script>
<script
  src="../applications/js/i18n/jquery.ui.datepicker-it.js"
  type="text/javascript"></script>
<script type="text/javascript">
	var dateFieldList = ${dateFieldListJSArray};
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA}+10;
	var today = new Date();
	
	<%-- LUCIANO: lo scroll automatico --%>	
	function scrollToCoordinates() { 
		window.scrollTo(0,${scrollY});
	}; 
	// 
	function saveScrollCoordinates() { 
	  $("#frmAction :input[name=scrollY]")[0].value = (document.all)?document.body.scrollTop:window.pageYOffset;		    
	};

	function submitForm() { 
		saveScrollCoordinates();
		frmAction.submit();
	}; 

	//inizio LP PG200360
	function showConferma() {
		$("#divpopupshadowConferma").show();
		$("#divpopupshowConferma").show();
	}
	
	function hideConferma() {
		$("#divpopupshadowConferma").hide();
		$("#divpopupshowConferma").hide();
	}
	//fine LP PG200360
	
	$(function() {
		scrollToCoordinates();

		$("#frmAction").submit(saveScrollCoordinates);

		$.datepicker.setDefaults($.datepicker.regional["it"]);

		/** configuro ogni campo data.
		 * Nota: Questa funcion funziona da closure, in cui fieldName resta una variabile iterna alla closure  
		 */
		var setupDateField = function setupDateField(fieldName) {
			//	console.log("configuro datepicker:"+fieldName);
			//inizio LP PG200360
			var valc = $("#flagModalitaAggiornamento").val();
			//fine LP PG200360
			
			if(fieldName == 'anagrafica_dataNascita'){
				annoDa = 1900;
				annoA = new Date().getFullYear();
			}else{
				annoDa = ${ddlDateAnnoDa};
				annoA = ${ddlDateAnnoA}+10;
			}

			$("#" + fieldName + "_hidden").datepicker("option", "dateFormat",
					"dd/mm/yyyy");
			//inizio LP PG200360
			if(valc == "1") {
				$("#" + fieldName + "_hidden").datepicker("option", "disabled", true);
				$("#" + fieldName + "_hidden").datepicker("disabled", "disabled");
			}	
			//fine LP PG200360
			$("#" + fieldName + "_hidden")
					.datepicker(
							{
								minDate : new Date(annoDa, 0, 1),
								maxDate : new Date(annoA, 11, 31),
								yearRange : annoDa + ":" + annoA,
								showOn : "button",
								buttonImage : "../applications/templates/shared/img/calendar.gif",
								buttonImageOnly : true,
								onSelect : function(dateText, inst) {
									// console.log("eseguo datepicker:"+fieldName);

									$("#" + fieldName + "_day_id").val(
											dateText.substr(0, 2));
									$("#" + fieldName + "_month_id").val(
											dateText.substr(3, 2));
									$("#" + fieldName + "_year_id").val(
											dateText.substr(6, 4));
								},
								beforeShow : function(input, inst) {
									//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
								updateValoreDatePickerFromDdl("" + fieldName
										+ "_day_id", "" + fieldName
										+ "_month_id", "" + fieldName
										+ "_year_id", "" + fieldName
										+ "_hidden");
							}
							});
		};

		// ciclo su una lista di "datepicker"
		for (i in dateFieldList) {
			var fieldName = dateFieldList[i];
			setupDateField(fieldName);
		}

	});

	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>


<style>
	/* inizio LP PG200360 */
	#lblNotaStampaAvviso {
		font-weight: bold;
		color: blue;
	}
	#lblMessaggioAlert {
		font-weight: bold;
		color: black;
	}
	#divpopupshadowConferma {
		z-index: 900;
		width: 100%;
		background: #eff7d5;
		height: 130%;
		position: absolute;
		top: 0;
		opacity: 0.5;
		left: 0;
	}

	#divpopupshowConferma {
		position: absolute;
		background: white;
	    top: 60%;
	    left: 30%;
	    width: 450px;
	    height: 150px;
	    z-index: 910;
	    border:2px solid black;	
	}
	/* fine LP PG200360 */
</style>

	<c:url value="" var="formParameters">
		<c:param name="test">ok</c:param>
				<!-- ric PAGINA -->
				<c:if test="${!empty rRowsPerPage}">
					<c:param name="rRowsPerPage">${rRowsPerPage}</c:param> 
				</c:if>
				<c:if test="${!empty rPageNumber}">
					<c:param name="rPageNumber">${rPageNumber}</c:param> 
				</c:if>
				<c:if test="${!empty rOrder}">
					<c:param name="rOrder">${rOrder}</c:param> 
				</c:if>
				<c:if test="${!empty rExt}">
					<c:param name="rExt">${rExt}</c:param>
				</c:if>

				<!-- ric PAGINA 2 -->
	
				<c:if test="${!empty r2RowsPerPage}">
					<c:param name="r2RowsPerPage">${r2RowsPerPage}</c:param> 
				</c:if>
				<c:if test="${!empty r2PageNumber}">
					<c:param name="r2PageNumber">${r2PageNumber}</c:param> 
				</c:if>
				<c:if test="${!empty r2Order}">
					<c:param name="r2Order">${r2Order}</c:param> 
				</c:if>
				<c:if test="${!empty r2Ext}">
					<c:param name="r2Ext">${r2Ext}</c:param>
				</c:if>

				<!-- ric PAGINA attuale -->
				<c:if test="${!empty rowsPerPage}">
					<c:param name="r3RowsPerPage">${rowsPerPage}</c:param> 
				</c:if>
				<c:if test="${!empty pageNumber}">
					<c:param name="r3PageNumber">${pageNumber}</c:param> 
				</c:if>
				<c:if test="${!empty order}">
					<c:param name="r3Order">${order}</c:param> 
				</c:if>
				<c:if test="${!empty ext}">
					<c:param name="r3Ext">${ext}</c:param>
				</c:if>

		<c:if test="${!empty tx_societa}">  
			<c:param name="tx_societa">${tx_societa}</c:param>
		</c:if>
		<c:if test="${!empty tx_utente}">  
			<c:param name="tx_utente">${tx_utente}</c:param>
		</c:if>
		<c:if test="${!empty tx_UtenteEnte}">  
			<c:param name="tx_UtenteEnte">${tx_UtenteEnte}</c:param>
		</c:if>
		<c:if test="${!empty impostaServ}">  
			<c:param name="impostaServ">${impostaServ}</c:param>
		</c:if>
		<c:if test="${!empty annoEmissione}">  
			<c:param name="annoEmissione">${annoEmissione}</c:param>
		</c:if>
		<c:if test="${!empty numEmissione}">  
			<c:param name="numEmissione">${numEmissione}</c:param>
		</c:if>
		<c:if test="${!empty codFiscale}">  
			<c:param name="codFiscale">${codFiscale}</c:param>
		</c:if>
		<c:if test="${!empty tx_tipologia_servizio}">  
			<c:param name="tx_tipologia_servizio">${tx_tipologia_servizio}</c:param>
		</c:if>
		<c:if test="${!empty stato_documento}">  
			<c:param name="stato_documento">${stato_documento}</c:param>
		</c:if>
		<c:if test="${!empty numDocumento}">  
			<c:param name="numDocumento">${numDocumento}</c:param>
		</c:if>
		<c:if test="${!empty ufficioImpositore}">  
			<c:param name="ufficioImpositore">${ufficioImpositore}</c:param>
		</c:if>
		<c:if test="${!empty stato_sospensione}">  
			<c:param name="stato_sospensione">${stato_sospensione}</c:param>
		</c:if>
		<c:if test="${!empty stato_procedure}">
			<c:param name="stato_procedure">${stato_procedure}</c:param> 
		</c:if>
		<c:if test="${!empty denominazione}">
			<c:param name="denominazione">${denominazione}</c:param> 
		</c:if>
		<c:if test="${!empty tipoRic}">
			<c:param name="tipoRic">${tipoRic}</c:param> 
		</c:if>
		<c:if test="${!empty codFiscAna}">
			<c:param name="codFiscAna">${codFiscAna}</c:param> 
		</c:if>
		<c:if test="${!empty chiaveCodFisc}">
			<c:param name="chiaveCodFisc">${chiaveCodFisc}</c:param> 
		</c:if>
		<c:if test="${!empty ext}">
			<c:param name="ext">${ext}</c:param>
		</c:if>
				
				<!-- CHIAVI DI RICERCA
			 		IMPOSTA SERVIZIO  EH1_CISECISE -->
				<c:if test="${!empty chiaveIS}">
					<c:param name="chiaveIS">${chiaveIS}</c:param> 
				</c:if>
				<!-- NUMERO DOCUMENTO EH1_CEH1NDOC -->
				<c:if test="${!empty chiaveDoc}">
					<c:param name="chiaveDoc">${chiaveDoc}</c:param> 
				</c:if>
				<!-- TIPO UFFICIO EH1_TANETUFF -->
				<c:if test="${!empty chiaveTipoUff}">
					<c:param name="chiaveTipoUff">${chiaveTipoUff}</c:param> 
				</c:if>
				<!-- CODICE UFFICIO EH1_CANECUFF -->
				<c:if test="${!empty chiaveCodUff}">
					<c:param name="chiaveCodUff">${chiaveCodUff}</c:param> 
				</c:if>
				<!-- CODICE UTENTE EH1_CUTECUTE -->
				<c:if test="${!empty chiaveCodUte}">
					<c:param name="chiaveCodUte">${chiaveCodUte}</c:param> 
				</c:if>
				<!-- CODICE ENTE EH1_CANECENT -->
				<c:if test="${!empty chiaveCodEnte}">
					<c:param name="chiaveCodEnte">${chiaveCodEnte}</c:param> 
				</c:if>
				<!-- CODICE SERVIZIO EH1_TEH1SERV -->
				<c:if test="${!empty chiaveServ}">
					<c:param name="chiaveServ">${chiaveServ}</c:param> 
				</c:if>
				<!-- CODICE TOMBSTONE EH1_CEH1TOMB -->
				<c:if test="${!empty chiaveTomb}">
					<c:param name="chiaveTomb">${chiaveTomb}</c:param> 
				</c:if>
				<!-- PROGR.FLUSSO EH1_PEH1FLUS -->
				<c:if test="${!empty chiaveFlusso}">
					<c:param name="chiaveFlusso">${chiaveFlusso}</c:param> 
				</c:if>
				<!-- CODICE SERVIZIO EH8_PEH8FLUS  -->
				<c:if test="${!empty chiaveAnaFlusso}">
					<c:param name="chiaveAnaFlusso">${chiaveAnaFlusso}</c:param> 
				</c:if>
				<!-- CODICE SERVIZIO EH8_GEH8CREA -->
				<c:if test="${!empty chiaveAnaDataCrea}">
					<c:param name="chiaveAnaDataCrea">${chiaveAnaDataCrea}</c:param> 
				</c:if>
				<!-- CODICE SERVIZIO EH8_TEH8SERV -->
				<c:if test="${!empty chiaveAnaServ}">
					<c:param name="chiaveAnaServ">${chiaveAnaServ}</c:param> 
				</c:if>
				<!-- CHIAVE EMISSIONE AGG -->
				<c:if test="${!empty chiaveAnnoE}">
					<c:param name="chiaveAnnoE">${chiaveAnnoE}</c:param>
				</c:if>				
				<c:if test="${!empty chiaveNumeroE}">
					<c:param name="chiaveNumeroE">${chiaveNumeroE}</c:param>
				</c:if>
				
				<!-- RIF.PAG.PREC. 	value="ricercaAnagrafica" value="ricercaEmissioni" value="ricercaDocumenti" -->
				<c:if test="${!empty pagPrec}">
					<c:param name="pagPrec">${pagPrec}</c:param> 
				</c:if>
	</c:url>


<br />

<c:choose>
  <c:when test="${done == null && richiestacanc == null}">

    <%-- Edit Documento --%>
    <s:form
      name="frmAction"
      action="gestioneDocumentiCarico.do"
      hasbtn3="false"
      hasbtn2="false"
      hasbtn1="false"
      method="post">

      <input
        name="scrollY"
        value="0"
        type="hidden" />
        
    <%-- inizio LP PG200360 --%>
    <c:if test="${editDocumento.modalitaAggiornamento}">
     <input
        name="flagModalitaAggiornamento"
        id="flagModalitaAggiornamento"
        value="1"
        type="hidden" />
	</c:if>        
    <c:if test="${!editDocumento.modalitaAggiornamento}">
     <input
        name="flagModalitaAggiornamento"
        id="flagModalitaAggiornamento"
        value="0"
        type="hidden" />
	</c:if>        
    <%-- fine LP PG200360 --%>

      <%-- TESTATA --%>
      <s:div
        name="divRicercaTitleName"
        cssclass="divRicTitle bold">
						TESTATA
			</s:div>

      <%-- TESTATA  --%>

      <s:div
        name=""
        cssclass="divRicMetadatiTopDoubleC" >

        <s:div
          name=""
          cssclass="divRicMetadatiSingleRow">

          <s:dropdownlist
            name="tx_soc_utente_ente"
            label="Societ&agrave;/Utente/Ente:"
            valueselected="${editDocumento.keyEnte}"
            cachedrowset="listaSocUtenteEnte"
            disable="${ddlSocietaDisabled || editDocumento.update || editDocumento.readonly  || editDocumento.delete || filtroDocumenti.ddlEnteDisabled}"
            usexml="true"
            cssclasslabel="label160 bold textright"
            cssclass="tbddlMax780"
            onchange="setFiredButton('tx_soc_utente_ente_changed');submitForm();"
            validator="required"
            showrequired="true">
            <s:ddloption
              text="Selezionare uno degli elementi della lista"
              value="" />
            <s:ddloption
              value="{1}{2}{3}"
              text="{5} / {6} / {4}" />
          </s:dropdownlist>
          <noscript>
            <s:button
              id="tx_soc_utente_ente_changed"
              disable="${ddlSocietaDisable || editDocumento.update || editDocumento.readonly || editDocumento.delete}"
              onclick=""
              text=""
              validate="false"
              type="submit"
              cssclass="btnimgStyle"
              title="Aggiorna" />
          </noscript>
        </s:div>
<%--          
        <s:div
          name=""
          cssclass="divRicMetadatiLeft width-480px">
          <s:div
            name=""
            cssclass="divRicMetadatiSingleRow">

            <s:dropdownlist
              name="tx_imposta_servizio"
              label="Imp.Serv.(I/S):"
              valueselected="${editDocumento.keyImpostaServizio}"
              cachedrowset="listaImpostaServizio"
              usexml="true"
              disable="${editDocumento.ddlImpostaServizioDisabled || editDocumento.readonly || editDocumento.delete}"
              cssclass="tbddlMax "
              cssclasslabel="label160 bold textright">
              <s:ddloption
                text="Tutti i Servizi"
                value="" />
              <s:ddloption
                text="{4}"
                value="{1}{2}{3}" />
            </s:dropdownlist>
          </s:div>
        </s:div>
--%>
        <s:div
          name=""
          cssclass="divRicMetadatiLeft width-480px">
          <s:div
            name=""
            cssclass="divRicMetadatiSingleRow">
	          <s:textbox
	            name="tx_imposta_servizio_codice"
	            label="Imp.Serv.(I/S):"
	            text="${editDocumento.codImpostaServizio}"
	            bdisable="${editDocumento.update || editDocumento.readonly || editDocumento.delete}" 
	            bmodify="true"
	            validator="required;maxlength=4"
	            maxlenght="4"
	            showrequired="true"
	            cssclasslabel="label85 bold textright"
	            cssclass="textareaman width-3em"
	            />
	
	          <s:textbox
	            name="tx_imposta_servizio_desc"
	            label="Descriz. Imp.Ser."
	            text="${editDocumento.ruolo.descrizioneImpostaServizio}"
	            bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
	            bmodify="true"
	            validator="required;maxlength=50"
	            maxlenght="50"
	            showrequired="false"
	            cssclass="textareaman"
	            cssclasslabel="bold textright" 
	            />

          </s:div>
        </s:div>
				

        <s:div
          name=""
          cssclass="divRicMetadatiCenter width-480px">
          <s:div
            name=""
            cssclass="divRicMetadatiSingleRow">
            <%--
            <s:dropdownlist
              name="tx_tipologia_servizio"
              label="Tip. Servizio:"
              disable="${ editDocumento.update || editDocumento.delete}"
              cssclass="tbddlMax "
              cssclasslabel="label85 bold textright"
              cachedrowset="listaTipologieServizio"
              usexml="true"
              onchange="setFiredButton('tx_button_tipologia_servizio_changed');submitForm();"
              valueselected="${editDocumento.keyTipologiaServizio}">
              <s:ddloption
                text="{2}"
                value="{3}{1}" />
            </s:dropdownlist>
            <noscript>
              <s:button
                id="tx_button_tipologia_servizio_changed"
                disable="${editDocumento.readonly || editDocumento.delete}"
                onclick=""
                text=""
                type="submit"
                cssclass="btnimgStyle"
                title="Aggiorna"
                validate="false" />
            </noscript>
             --%>
              <s:textbox
	            name="tx_tipologia_servizio_codice"
	            label="Tip. Servizio:"
	            text="${editDocumento.ruolo.codiceTipologiaServizio}"
	            bdisable="${editDocumento.update || editDocumento.readonly || editDocumento.delete}" 
	            bmodify="true"
	            validator="required;maxlength=3"
	            maxlenght="3"
	            showrequired="true"
	            cssclasslabel="label85 bold textright"
	            cssclass="textareaman width-3em"
	            />
	
	          <s:textbox
	            name="tx_tipologia_servizio_desc"
	            label="Descriz. Tip.Serv."
	            text="${editDocumento.ruolo.descrizioneTipologiaServizio}"
	            bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
	            bmodify="true"
	            validator="required;maxlength=50"
	            maxlenght="50"
	            showrequired="false"
	            cssclass="textareaman"
	            cssclasslabel="bold textright"
	            />
          </s:div>
        </s:div>
      </s:div>

      <%--  SEZIONE  CONFIGURAZIONE --%>
      <s:div
        name="sezioneConfigurazione"
        cssclass="divDettaglio">
        <s:div
          name="divRicercaTitleName"
          cssclass="divRicTitle bold">Configurazione</s:div>
        <s:div
          name=""
          cssclass="divRicMetadati">

          <s:div
            name=""
            cssclass="divRicMetadatiLeft">
            <s:div
              name=""
              cssclass="divRicMetadatiSingleRow">
              <s:dropdownlist
                name="configurazione_flagGenerazioneIUV"
                valueselected="${editDocumento.configurazione.flagGenerazioneIUV}"
                label="Flag generaz. IUV:"
                cssclasslabel="label85 bold textright"
                cssclass="tbddl"
                usexml="false"
                validator="required"
                showrequired="true"
                onchange="setFiredButton('tx_button_flagGenerazioneIUV_changed');submitForm();"
                disable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}">
                <s:ddloption text="No" value="N" />
                <s:ddloption text="Si" value="Y" />
              </s:dropdownlist>
              <noscript>
                <s:button
                  id="tx_button_flagGenerazioneIUV_changed"
                  disable="${editDocumento.readonly || editDocumento.delete}"
                  onclick=""
                  text=""
                  type="submit"
                  cssclass="btnimgStyle"
                  title="Aggiorna"
                  validate="false" />
              </noscript>
            </s:div>
          </s:div>
		  <%-- inizio LP PG200360
		       Nota. Invertito in dropdownlist No/Si adesso se valueselected == '' ==> default No
		             Inserita onchange="setFiredButton('tx_button_flagstampaavviso_changed');submitForm();"
		   --%>
	          <s:div
	            name=""
	            cssclass="divRicMetadatiCenter">
	            <s:div
	              name=""
	              cssclass="divRicMetadatiSingleRow">
	              <s:dropdownlist
	                name="configurazione_flagStampaAvviso"
	                valueselected="${editDocumento.configurazione.flagStampaAvviso}"
	                label="Stampa Avviso:"
	                cssclasslabel="label85 bold  textright"
	                cssclass="tbddl "
	                usexml="false"
	                onchange="setFiredButton('tx_button_flagstampaavviso_changed');submitForm();"
	                validator="required"
	                showrequired="true"
	                disable="${editDocumento.readonly || editDocumento.delete || editDocumento.stampaAvvisoEseguita}">
	                <s:ddloption
	                  text="No"
	                  value="N" />
	                <s:ddloption
	                  text="Si"
	                  value="Y" />
	              </s:dropdownlist>
	            </s:div>
          	  </s:div>

          <s:div
            name=""
            cssclass="divRicMetadatiRight">
            <s:div
              name=""
              cssclass="divRicMetadatiSingleRow">
              <s:textbox
                name="configurazione_identificativoFlusso"
                text="${editDocumento.configurazione.identificativoFlusso}"
                label="Identificativo Flusso:"
                bmodify="true"
                bdisable="${editDocumento.update || editDocumento.readonly || editDocumento.delete|| editDocumento.modalitaAggiornamento}"
                validator="ignore;"
                maxlenght="50"
                cssclasslabel="label85 bold textright"
                cssclass="textareaman" />
            </s:div>
          </s:div>
        </s:div>
        <s:div
          name=""
          cssclass="divRicMetadatiSingleRow">

          <s:textbox
            name="iuv_identificativoDominio"
            label="Id. Dominio:"
            text="${editDocumento.configurazioneIUV.identificativoDominio}"
            bdisable="${editDocumento.configurazioneIuvDisable || editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
            bmodify="true"
            validator="ignore;maxlength=16"
            maxlenght="16"
            showrequired="true"
            cssclasslabel="label85 bold textright"
            cssclass="textareaman width-15em"
            />

          <s:textbox
            name="iuv_auxDigit"
            label="Aux Digit"
            text="${editDocumento.configurazioneIUV.auxDigit}"
            bdisable="${editDocumento.configurazioneIuvDisable || editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
            bmodify="true"
            validator="ignore;maxlength=1"
            maxlenght="1"
            showrequired="true"
            cssclass="textareaman width-3em"
            cssclasslabel="label85 bold textright" 
            />

          <s:textbox
            name="iuv_applicationCode"
            label="Appl. Code"
            text="${editDocumento.configurazioneIUV.applicationCode}"
            bdisable="${editDocumento.configurazioneIuvDisable || editDocumento.readonly || editDocumento.delete}" 
            bmodify="true"
            validator="ignore;maxlength=2"
            maxlenght="2"
            showrequired="false"
            cssclass="textareaman width-3em"
            cssclasslabel="label85 bold textright" />

          <s:textbox
            name="iuv_segregationCode"
            label="Segreg. Code"
            text="${editDocumento.configurazioneIUV.segregationCode}"
            bdisable="${editDocumento.configurazioneIuvDisable  || editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
            bmodify="true"
            validator="ignore;maxlength=2"
            maxlenght="2"
            showrequired="false"
            cssclass="textareaman width-3em"
            cssclasslabel="label85 bold textright" />
            
           <s:textbox
            name="iuv_carattereServizio"
            label="Caratt. Servizio"
            text="${editDocumento.configurazioneIUV.carattereServizio}"
            bdisable="${editDocumento.configurazione.flagGenerazioneIUV == 'N' || editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}"
            bmodify="true"
            validator="ignore;maxlength=2"
            maxlenght="2"
            showrequired="false"
            cssclass="textareaman width-3em"
            cssclasslabel="label85 bold textright" />
        </s:div>


      </s:div>
      <br />


      <%--  SEZIONE  DOCUMENTO --%>
      <s:div
        name="sezioneDocumento"
        cssclass="divDettaglio">
        <s:div
          name=""
          cssclass="divRicTitle bold">Documento</s:div>

	    <%-- inizio LP PG200360 --%>
	    <c:if test="${editDocumento.stampaAvvisoEseguita}">
	      <br />  
		    <center>
	          <span id="lblNotaStampaAvviso" name="lblNotaStampaAvviso" class="seda-ui-span">
	            NOTA: La Stampa dell'avviso &egrave; gi&agrave; stata eseguita. 
	          </span>
		    </center>
          <br />  
		</c:if>        
	    <%-- fine LP PG200360 --%>

        <s:div
          name=""
          cssclass="divRicMetadati">
          <s:div
            name=""
            cssclass="divRicMetadatiLeft">
            <s:div
              name=""
              cssclass="divRicMetadatiSingleRow">
              <s:textbox
                name="documento_numeroDocumento"
                text="${editDocumento.documento.numeroDocumento}"
                label="Numero Documento:"
                bmodify="true"
		        bdisable="${editDocumento.update || editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                validator="required;maxlength=20"
                maxlenght="20"
                showrequired="true"
                cssclasslabel="label85 bold textright"
                cssclass="textareaman" />
            </s:div>
            <s:div
              name="divElement4"
              cssclass="divRicMetadatiSingleRow">
              <s:textbox
                name="documento_annoEmissione"
                text="${editDocumento.documento.annoEmissione}"
                label="Anno Emissione:"
                bmodify="true"
		        bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                validator="required;digits;minlength=4;maxlength=4"
                maxlenght="4"
                showrequired="true"
                cssclasslabel="label85 bold textright"
                cssclass="textareaman" />
            </s:div>
            <s:div
              name="divElement4"
              cssclass="divRicMetadatiSingleRow">
              <s:textbox
                name="documento_numeroEmissione"
                text="${editDocumento.documento.numeroEmissione}"
                label="Numero Emissione:"
                bmodify="true"
		        bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                validator="required;digits;maxlength=10"
                maxlenght="10"
                showrequired="true"
                cssclasslabel="label85 bold textright"
                cssclass="textareaman" />
            </s:div>
            <s:div
              name="divElement4"
              cssclass="divRicMetadatiSingleRow">
              <s:textbox
                name="documento_causale"
                text="${editDocumento.documento.causale}"
                label="Causale:"
                bmodify="true"
		        bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                validator="required;maxlength=120"
                maxlenght="120"
                showrequired="true"
                cssclasslabel="label85 bold textright"
                cssclass="textareaman" />
            </s:div>
          </s:div>

          <s:div
            name=""
            cssclass="divRicMetadatiCenter">
            <s:div
              name=""
              cssclass="divRicMetadatiSingleRow">
              <%-- inizio LP PG200360 --%>
              <c:if test="${editDocumento.modalitaAggiornamento}">
	              <s:date
	                prefix="documento_dataNotifica"
	                calendar="${documento_dataNotifica}"
	                label="Data Notifica:"
	                yearbegin="${ddlDateAnnoDa}"
	                yearend="${ddlDateAnnoA}"
	                validator="ignore;"
	                showrequired="false"
			        disabled="true" 
	                locale="IT-it"
	                descriptivemonth="false"
	                separator="/"
	                cssclass="dateman"
	                cssclasslabel="label85" />
              </c:if>
              <c:if test="${!editDocumento.modalitaAggiornamento}">
              <%-- fine LP PG200360 --%>
              <s:date
                prefix="documento_dataNotifica"
                calendar="${documento_dataNotifica}"
                label="Data Notifica:"
                yearbegin="${ddlDateAnnoDa}"
                yearend="${ddlDateAnnoA}"
                validator="ignore;dateISO"
                showrequired="false"
		        disabled="${editDocumento.readonly || editDocumento.delete}" 
                locale="IT-it"
                descriptivemonth="false"
                separator="/"
                cssclass="dateman"
                cssclasslabel="label85" />
              <%-- inizio LP PG200360 --%>
              </c:if>
              <%-- fine LP PG200360 --%>
              <input
                type="hidden"
                id="documento_dataNotifica_hidden"
                value="" />
            </s:div>

            <s:div
              name=""
              cssclass="divRicMetadatiSingleRow">
              <s:textbox
                name="documento_numeroBollettinoPagoPA"
                text="${editDocumento.documento.numeroBollettinoPagoPA}"
                label="Num. Bollettino PagoPA:"
                bmodify="true"
                bdisable="${!editDocumento.configurazioneIuvDisable || editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}"  
                maxlenght="18"
                validator="ignore;"
                cssclass="textareaman"
                cssclasslabel="label85 bold textright" />
            </s:div>
            <s:div
              name=""
              cssclass="divRicMetadatiSingleRow">
              <s:textbox
                name="documento_impBollettinoTotaleDocumento"
                text="${documento_impBollettinoTotaleDocumento}"
                label="Imp. Bollettino Totale:"
                bmodify="true"
		        bdisable="${editDocumento.readonly || editDocumento.delete}" 
                validator="required;accept=^[0-9]{1,13}\,[0-9]{2}?$;maxlength=16"
                message="[accept=Importo Bollettino Totale Documento: ${msg_configurazione_importo_13_2}]"
                maxlenght="16"
                showrequired="true"
                cssclass="textareaman text_align_right"
                cssclasslabel="label85 bold textright" />
            </s:div>
          </s:div>

					<s:div name="" cssclass="divRicMetadatiRight">
						<s:div name="" cssclass="divRicMetadatiSingleRow">
							<s:textbox name="documento_ibanAccredito"
								text="${editDocumento.documento.ibanAccredito}"
								bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}"
								label="IBAN Accredito:" bmodify="true" maxlenght="35"
								validator="required;maxlength=35" showrequired="true"
								cssclass="textareaman" cssclasslabel="label85 bold textright" />
						</s:div>
						<s:div name="" cssclass="divRicMetadatiSingleRow">
							<s:textbox name="documento_ibanAppoggio"
								text="${editDocumento.documento.ibanAppoggio}"
								bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}"
								label="IBAN Appoggio:" bmodify="true" maxlenght="35"
								validator="maxlength=35" showrequired="false"
								cssclass="textareaman" cssclasslabel="label85 bold textright" />
						</s:div>
						<s:div name="" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="documento_flagFatturazioneElettronica"
								valueselected="${editDocumento.documento.flagFatturazioneElettronica}"
								disable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}"
								label="Fatturazione Elettronica:"
								cssclasslabel="label85 bold  textright" cssclass="tbddl "
								usexml="false" validator="required" showrequired="true">
								<s:ddloption text="Si" value="Y" />
								<s:ddloption text="No" value="N" />
							</s:dropdownlist>
						</s:div>
						<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
							<s:textbox name="documento_identificativoUnivocoVersamento"
								text="${editDocumento.documento.identificativoUnivocoVersamento}"
								label="Identificativo Univoco Versamento:"
								bdisable="${!editDocumento.configurazioneIuvDisable || editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}"
								bmodify="true" maxlenght="17" validator="ignore;maxlength=17"
								cssclass="textareaman" cssclasslabel="label85 bold textright" />
						</s:div>
					</s:div>
				</s:div>

				<%-- inizio LP PG200360 --%>
				<%-- inizio LP PG200360 - 20210224 campo non obbligatorio --%>
				<s:div name="divElementUnicaTassonomia" cssclass="divRicMetadatiUnicaTassonomiaCFECFS">
					<s:div name="divElementU1" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="documento_tassonomia"
								disable="${!editDocumento.configurazioneIuvDisable || editDocumento.readonly || editDocumento.delete}"
								label="Tassonomia:"
								cssclass="seda-ui-ddl tbddlMaxTassonomia870 floatleft" 
								cssclasslabel="seda-ui-label label85 bold textright"
								cachedrowset="tassonomie" usexml="true"
								validator="ignore" showrequired="false"
								valueselected="${editDocumento.documento.tassonomia}">
								<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
								<s:ddloption value="{12}" text="{3} / {5} / {8} / {10} => {12}"/>
						</s:dropdownlist>
					</s:div>
				</s:div>
				<%-- fine LP PG200360 --%>

      </s:div>
      <br />


      <%--  SEZIONE  AMAGRAFICA--%>
      <s:div
        name="sezioneAnagrafica"
        cssclass="divDettaglio">
        <s:div
          name=""
          cssclass="divRicTitle bold">Anagrafica</s:div>

        <s:div
          name=""
          cssclass="divRicMetadati">

          <s:div
            name="anagLeft"
            cssclass="divRicMetadatiLeft">
            <s:div
              name=""
              cssclass="divRicMetadatiSingleRow">
              <s:textbox
                name="anagrafica_codiceFiscale"
                text="${editDocumento.anagrafica.codiceFiscale}"
                label="Cod. Fiscale:"
		        bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                validator="required;minlength=3;maxlength=16"
                maxlenght="16"
                showrequired="true"
                bmodify="true"
                cssclasslabel="label85 bold textright"
                cssclass="textareaman" />
            </s:div>

            <s:div
              name=""
              cssclass="divRicMetadatiSingleRow">
              <s:dropdownlist
                name="anagrafica_siglaProvinciaNascita"
                label="Provincia Nascita:"
                valueselected="${anagrafica_siglaProvinciaNascita}"
                cachedrowset="listaProvince"
                usexml="true"
                cssclasslabel="label85 bold  textright"
                cssclass="tbddl"
                onchange="setFiredButton('tx_button_provincia_nascita_changed');submitForm();"
                validator="ignore"
                showrequired="false"
                disable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}">
                <s:ddloption
                  text="Selezionare uno degli elementi"
                  value="" />
                <s:ddloption
                  value="{2}"
                  text="{1}" />
              </s:dropdownlist>
              <noscript>
                <s:button
                  id="tx_button_provincia_nascita_changed"
                  title="Aggiorna"
                  onclick=""
                  text=""
                  validate="false"
                  type="submit"
			            disable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                  cssclass="btnimgStyle" />
              </noscript>
            </s:div>

            <s:div
              name=""
              cssclass="divRicMetadatiSingleRow">
              <s:dropdownlist
                name="anagrafica_siglaProvinciaFiscale"
                label="Provincia Fiscale:"
                valueselected="${anagrafica_siglaProvinciaFiscale}"
                cachedrowset="listaProvince"
                usexml="true"
                showrequired="true"
                validator="required"
                cssclasslabel="label85 bold  textright"
                cssclass="tbddl "
                onchange="setFiredButton('tx_button_provincia_fiscale_changed');submitForm();"
                disable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" >
                <s:ddloption
                  text="Selezionare uno degli elementi"
                  value="" />
                <s:ddloption
                  value="{2}"
                  text="{1}" />
              </s:dropdownlist>
              <noscript>
                <s:button
                  id="tx_button_provincia_fiscale_changed"
                  title="Aggiorna"
                  onclick=""
                  text=""
                  validate="false"
                  type="submit"
                  cssclass="btnimgStyle" />
              </noscript>
            </s:div>

            <s:div
              name=""
              cssclass="divRicMetadatiSingleRow">
              <s:dropdownlist
                name="anagrafica_tipoAnagrafica"
                valueselected="${editDocumento.anagrafica.tipoAnagrafica}"
                label="Tipo Anagrafica:"
                showrequired="true"
                validator="required"
		        disable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                cssclasslabel="label85 bold textright"
                cssclass="tbddl">
                <s:ddloption
                  value=""
                  text="Selezionare..." />
                <s:ddloption
                  value="A"
                  text="AZIENDA" />
                <s:ddloption
                  value="D"
                  text="DITTA INDIVIDUALE" />
                <s:ddloption
                  value="M"
                  text="MASCHIO" />
                <s:ddloption
                  value="F"
                  text="FEMMINA" />
              </s:dropdownlist>
            </s:div>
          </s:div>
          <s:div
            name="anagCenter"
            cssclass="divRicMetadatiCenter">
            <s:div
              name=""
              cssclass="divRicMetadatiSingleRow">
              <s:textbox
                name="anagrafica_denominazione"
                text="${editDocumento.anagrafica.denominazione}"
                label="Denominazione:"
                bmodify="true"
		        bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                maxlenght="50"
                showrequired="true"
                validator="required;maxlength=50"
                cssclass="textareaman"
                cssclasslabel="label85 bold textright" />
            </s:div>

            <s:div
              name=""
              cssclass="divRicMetadatiSingleRow">
              <s:dropdownlist
                name="anagrafica_codiceBelfioreComuneNascita"
                valueselected="${editDocumento.anagrafica.codiceBelfioreComuneNascita}"
                label="Cod. Belf. Nascita:"
                cachedrowset="listBelfioreNascita"
                showrequired="false"
                validator="ignore"
                cssclasslabel="label85 bold  textright"
                cssclass="tbddl "
                usexml="true"
		        disable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" >
                <s:ddloption
                  text="Selezionare uno degli elementi"
                  value="" />
                <s:ddloption
                  value="{1}"
                  text="{1} - {4}" />
              </s:dropdownlist>
            </s:div>

            <s:div
              name=""
              cssclass="divRicMetadatiSingleRow">
              <s:dropdownlist
                name="anagrafica_codiceBelfioreComuneFiscale"
                valueselected="${editDocumento.anagrafica.codiceBelfioreComuneFiscale}"
                label="Cod. Belf Fiscale:"
                cachedrowset="listBelfioreFiscale"
                usexml="true"
                validator="required"
                showrequired="true"
                cssclasslabel="label85 bold  textright"
                cssclass="tbddl "
                disable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" >
                <s:ddloption
                  text="Selezionare uno degli elementi"
                  value="" />
                <s:ddloption
                  value="{1}"
                  text="{1} - {4}" />
              </s:dropdownlist>
            </s:div>

            <s:div
              name="divElement9"
              cssclass="divRicMetadatiSingleRow">
              <s:textbox
                name="anagrafica_email"
                text="${editDocumento.anagrafica.email}"
                label="E-Mail:"
                validator="ignore;"
                maxlenght="100"
                bmodify="true"
                bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                cssclass="textareaman"
                cssclasslabel="label85 bold textright" />
            </s:div>
          </s:div>

          <s:div
            name="anagRight"
            cssclass="divRicMetadatiRight">
            <s:div
              name=""
              cssclass="divRicMetadatiSingleRow">
              <s:textbox
                name="anagrafica_indirizzoFiscale"
                text="${editDocumento.anagrafica.indirizzoFiscale}"
                label="Indirizzo Fiscale:"
                showrequired="true"
                validator="required;minlength=1;maxlength=50"
                bmodify="true"
		        bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                maxlenght="50"
                cssclass="textareaman"
                cssclasslabel="label85 bold textright" />
            </s:div>

            <s:div
              name=""
              cssclass="divRicMetadatiSingleRow">
              <s:div
                name=""
                cssclass="">
                <s:div
                  name=""
                  cssclass="divDataDa">
                  <%-- inizio LP PG200360 --%>
                  <c:if test="${editDocumento.modalitaAggiornamento}">
	                  <s:date
	                    prefix="anagrafica_dataNascita"
	                    label="Data Nascita:"
	                    calendar="${anagrafica_dataNascita}"
	                    yearbegin="1900"
	                    yearend="${ddlDateAnnoA}"
	                    validator="ignore;"
	                    showrequired="false"
	                    locale="IT-it"
	                    descriptivemonth="false"
	                    separator="/"
	                    message=""
	            		disabled="true" 
	                    cssclass="dateman"
	                    cssclasslabel="label85">
	                  </s:date>
                  </c:if>
                  <c:if test="${!editDocumento.modalitaAggiornamento}">
                  <%-- fine LP PG200360 --%>
                  <s:date
                    prefix="anagrafica_dataNascita"
                    label="Data Nascita:"
                    calendar="${anagrafica_dataNascita}"
                    yearbegin="1900"
                    yearend="${ddlDateAnnoA}"
                    validator="ignore;dateISO"
                    showrequired="false"
                    locale="IT-it"
                    descriptivemonth="false"
                    separator="/"
                    message="[dateISO=Decorrenza: ${msg_dataISO_valida}]"
		            disabled="${editDocumento.readonly || editDocumento.delete}" 
                    cssclass="dateman"
                    cssclasslabel="label85">
                  </s:date>
                  <%-- inizio LP PG200360 --%>
                  </c:if>
                  <%-- fine LP PG200360 --%>
                  <input
                    type="hidden"
                    id="anagrafica_dataNascita_hidden"
                    value="" />
                </s:div>
              </s:div>
            </s:div>
            <s:div
              name=""
              cssclass="divRicMetadatiSingleRow">
            </s:div>
            <s:div
              name="divElement13"
              cssclass="divRicMetadatiSingleRow">
              <s:textbox
                name="anagrafica_emailPec"
                text="${editDocumento.anagrafica.emailPec}"
                label="PEC E-Mail:"
                validator="ignore;maxlength=100"
		        bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                bmodify="true"
                maxlenght="100"
                message="[accept=Ufficio Statale: ${msg_configurazione_alfanumerici}]"
                cssclasslabel="label85 bold textright"
                cssclass="textareaman" />
            </s:div>

          </s:div>
        </s:div>

      </s:div>



      <%-- SEZIONE TRIBUTI --%>
      <s:div
        name="sezioneTributi"
        cssclass="divDettaglio">
        <s:div
          name="divRicercaTitleName"
          cssclass="divRicTitle bold">Lista Tributi
          <c:if test="${!editDocumento.modalitaAggiornamento}">
			  <s:button
	            id="button_add_tributo"
	            type="submit"
	            text="Nuovo"
	            onclick=""
	            cssclass="btnStyle btnStyle2"
	            validate="false"
	            disable="${editDocumento.readonly || editDocumento.delete}" 
	          />
          </c:if>
        </s:div>


        <%-- inizio LP PG210130 Step02 --%>
        <%-- Sono state modificate le width delle colonne utilizzando nuove definizioni su entrate.css --%>
        <%-- Al momento della abilitazione delle colonne Articolo, IdDominio, Iban Bancario  e postale allineare con le width presenti in regmarche (fatto 20211126) --%>
        <%-- fine LP PG210130 Step02  --%>
        <c:if test="${not empty editDocumento.listaTributi}">
          <s:table
            border="1"
            cellspacing="0"
            cellpadding="3"
            cssclass="seda-ui-datagrid">
            <s:thead>
              <s:tr cssclass="seda-ui-datagridrowpari">
		        <%-- inizio LP PG210130 Step02 --%>
		        <%-- 
                <s:th cssclass="seda-ui-datagridheadercell text_align_right ">Progr.</s:th>
                 --%>
                <s:th cssclass="seda-ui-datagridheadercell text_align_right ">Pr.</s:th>
		        <%-- inizio LP PG210130 Step02 --%>
                <s:th cssclass="seda-ui-datagridheadercell ">Trib.*</s:th>
                <s:th cssclass="seda-ui-datagridheadercell ">Anno*</s:th>
                <s:th cssclass="seda-ui-datagridheadercell text_align_right ">Importo *</s:th>
                <s:th cssclass="seda-ui-datagridheadercell ">Cod. Capitolo</s:th>
                <s:th cssclass="seda-ui-datagridheadercell ">Accertamento</s:th>
                <%-- inizio LP PG210130 --%>
                <s:th cssclass="seda-ui-datagridheadercell ">Articolo</s:th>
                <s:th cssclass="seda-ui-datagridheadercell ">IdDominio</s:th>
                <s:th cssclass="seda-ui-datagridheadercell ">Tip.Serv</s:th> <%-- LP PG22XX05 --%>
                <s:th cssclass="seda-ui-datagridheadercell ">Iban Bancario</s:th>
                <s:th cssclass="seda-ui-datagridheadercell ">Iban Postale</s:th>
                <%-- fine LP PG210130 --%>
                <s:th cssclass="seda-ui-datagridheadercell width-100perc">Note</s:th>
                <s:th cssclass="seda-ui-datagridheadercell"></s:th>

              </s:tr>
            </s:thead>
            <s:tbody>
              <c:forEach
                items="${editDocumento.listaTributi}"
                var="u"
                varStatus="loop">

                <s:tr cssclass="seda-ui-datagridrowpari">
                  <s:td cssclass="seda-ui-datagridcell">
                    <s:textbox
                      name="trib_${loop.index}_progressivoTributo"
                      label=""
                      text="${loop.index+1}"
                      maxlenght="3"
				      bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                      bmodify="false"
                      cssclass="textareaman text_align_right width-18px"
                      cssclasslabel="display_none" />
                  </s:td>
				  <%-- LP PG200360 validator a required --%>	
                  <s:td cssclass="seda-ui-datagridcell">
                    <s:textbox
                      name="trib_${loop.index}_codiceTributo"
                      label="Codice Tributo"
                      text="${u.codiceTributo}"
    		          bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                      maxlenght="4"
                      validator="required;maxlength=4"
                      showrequired="false"
                      bmodify="true"
                      cssclass="textareaman text_align_right width-30px"
                      cssclasslabel="display_none" />
                  </s:td>
				  <%-- LP PG200360 validator a required --%>	
                  <s:td cssclass="seda-ui-datagridcell">
                    <s:textbox
                      name="trib_${loop.index}_annoTributo"
                      label="Anno Tributo"
                      text="${u.annoTributo}"
    		          bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                      maxlenght="4"
                      validator="required;accept=^[0-9]{4}?$;maxlength=4"
                      message="[accept=Anno Tributo ${loop.index + 1}: numerico]"
                      showrequired="false"
                      bmodify="true"
                      cssclass="textareaman text_align_right width-30px"
                      cssclasslabel="display_none" />
                  </s:td>
				  <%-- LP PG200360 validator a required --%>	
                  <s:td cssclass="seda-ui-datagridcell">
                    <s:textbox
                      name="trib_${loop.index}_impTributo"
                      label="Importo Tributo"
                      text="${trib[loop.index].impTributo}"
    		          bdisable="${editDocumento.readonly || editDocumento.delete}" 
                      maxlenght="16"
                      validator="required;accept=^[0-9]{1,13}\,[0-9]{2}?$;maxlength=16"
                      message="[accept=Importo Tributo ${loop.index + 1}: ${msg_configurazione_importo_13_2}]"
                      showrequired="false"
                      bmodify="true"
                      cssclass="textareaman text_align_right width-55px"
                      cssclasslabel="display_none" />
                  </s:td>
<%--
                  <s:td cssclass="seda-ui-datagridcell">
                    <s:textbox
                      name="trib_${loop.index}_discaricoTributo"
                      label=""
                      text="${trib[loop.index].discaricoTributo}"
    		            bdisable="${editDocumento.readonly || editDocumento.delete}" 
                      maxlenght="15"
                      validator="ignore;accept=^[0-9]{1,12}\,[0-9]{2}?$;maxlength=15"
                      message="[accept=Importo Discarico: ${msg_configurazione_importo_13_2}]"
                      showrequired="false"
                      bmodify="true"
                      cssclass="textareaman text_align_right width-8em"
                      cssclasslabel="display_none" />
                  </s:td>

                  <s:td cssclass="seda-ui-datagridcell">
                    <s:textbox
                      name="trib_${loop.index}_residuoTributo"
                      label=""
                      text="${trib[loop.index].residuoTributo}"
                      maxlenght="15"
                      validator="ignore;maxlength=15"
                      showrequired="false"
                      bmodify="false"
                      bdisable="true"
                      cssclass="textareaman text_align_right width-8em"
                      cssclasslabel="display_none" />
                  </s:td>
 --%>

                  <s:td cssclass="seda-ui-datagridcell">
                    <s:textbox
                      name="trib_${loop.index}_codiceCapitolo"
                      label="Cod. Capitolo"
                      text="${u.codiceCapitolo}"
    		          bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                      maxlenght="60"
                      validator="ignore;maxlength=60"
                      showrequired="false"
                      bmodify="true"
                      cssclass="textareaman width-60px"
                      cssclasslabel="display_none" />
                  </s:td>
                  
                  <s:td cssclass="seda-ui-datagridcell">
                    <s:textbox
                      name="trib_${loop.index}_accertamento"
                      label="Accertamento"
                      text="${u.accertamento}"
    		          bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                      maxlenght="60"
                      validator="ignore;maxlength=60"
                      showrequired="false"
                      bmodify="true"
                      cssclass="textareaman width-8em"
                      cssclasslabel="display_none" />
                  </s:td>

                <%-- inizio LP PG210130 --%>
                   <s:td cssclass="seda-ui-datagridcell">
                     <s:textbox
                       name="trib_${loop.index}_articolo"
                       label="Articolo"
                       text="${u.articolo}"
                       bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                       maxlenght="64"
                       validator="ignore;maxlength=64"
                       showrequired="false"
                       bmodify="true"
                       cssclass="textareaman width-60px"
                       cssclasslabel="display_none" />
                   </s:td>
                   <s:td cssclass="seda-ui-datagridcell">
                     <s:textbox
                       name="trib_${loop.index}_identificativoDominio"
                       label="IdDominio"
                       text="${u.identificativoDominio}"
                       bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                       maxlenght="16"
                       validator="ignore;maxlength=16"
                       showrequired="false"
                       bmodify="true"
                        cssclass="textareaman width-95px"
                       cssclasslabel="display_none" />
                   </s:td>
                   
                  <%-- inizio LP PG22XX05 --%>
				  <%-- TODO da fare una ddl e un dropdownlist 							DA FARE --%>
                  <s:td cssclass="seda-ui-datagridcell">
                    <s:textbox
                      name="trib_${loop.index}_codiceTipologiaServizio"
                      label="CTSE"
                      text="${u.codiceTipologiaServizio}"
    		          bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                      maxlenght="3"
                      validator="ignore;maxlength=3"
                      showrequired="false"
                      bmodify="true"
                      cssclass="textareaman width-30px"
                      cssclasslabel="display_none" />
                  </s:td>
				  <%-- fine LP PG22XX05 --%>
				  
                   <s:td cssclass="seda-ui-datagridcell">
                     <s:textbox
                       name="trib_${loop.index}_ibanBancario"
                       label="Iban Bancario"
                       text="${u.IBANBancario}"
                       bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                       maxlenght="27"
                       validator="ignore;accept=^(IT|SM)\d{2}[a-zA-Z]\d{10}[0-9a-zA-Z]{12}|(IT|SM)\d{2}[a-zA-Z]\d{22}$;minlength=27;maxlength=27"
                       message="[accept=Iban Bancario ${loop.index + 1}: iban bancario!]"
                       showrequired="false"
                       bmodify="true"
                       cssclass="textareaman width-195px"
                       cssclasslabel="display_none" />
                   </s:td>
                   <s:td cssclass="seda-ui-datagridcell">
                     <s:textbox
                       name="trib_${loop.index}_ibanPostale"
                       label="Iban Postale"
                       text="${u.IBANPostale}"
                       bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                       maxlenght="27"
                       validator="ignore;accept=^(IT|SM)\d{2}[a-zA-Z]07601\d{5}[0-9a-zA-Z]{12}|(IT|SM)\d{2}[a-zA-Z]07601\d{17}$;minlength=27;maxlength=27"
                       message="[accept=Iban Postale ${loop.index + 1}: iban postale!]"
                       showrequired="false"
                       bmodify="true"
                       cssclass="textareaman width-195px"
                       cssclasslabel="display_none" />
                   </s:td>
                  <%-- fine LP PG210130 --%>
                  
                  <s:td cssclass="seda-ui-datagridcell">
                    <s:textbox
                      name="trib_${loop.index}_noteTributo"
                      label=""
                      text="${u.noteTributo}"
    		          bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                      maxlenght="60"
                      validator="ignore;maxlength=60"
                      showrequired="false"
                      bmodify="true"
                      cssclass="textareaman width-100px"
                      cssclasslabel="display_none" />
                  </s:td>

                  <s:td cssclass="seda-ui-datagridcell">
                  	<c:if test="${!editDocumento.modalitaAggiornamento}">
                    <s:button
                      id="button_delete_tributo"
    		          disable="${editDocumento.readonly || editDocumento.delete}" 
                      title="Elimina"
                      onclick=""
                      text="${loop.index}"
                      validate="false"
                      type="submit"
                      cssclass="btnimgDeletable" />
                      </c:if>
                  </s:td>
                </s:tr>
              </c:forEach>
            </s:tbody>
          </s:table>
        </c:if>
      </s:div>




      <%-- SEZIONE SCADENZE --%>
      <s:div
        name="sezioneScadenze"
        cssclass="divDettaglio">
        <s:div
          name=""
          cssclass="divRicTitle bold">Lista Scadenze
           	<c:if test="${!editDocumento.modalitaAggiornamento}">
              <s:button
	            id="button_add_scadenza"
	            type="submit"
	            text="Nuova"
	            onclick=""
	            cssclass="btnStyle btnStyle2"
	            disable="${editDocumento.readonly || editDocumento.delete}" 
	            validate="false" />
            </c:if>
        </s:div>

        <c:if test="${not empty editDocumento.listaScadenze}">
          <s:table
            border="1"
            cellspacing="0"
            cellpadding="3"
            cssclass="seda-ui-datagrid">
            <s:thead>
              <s:tr cssclass="seda-ui-datagridrowpari">
                <s:th cssclass="seda-ui-datagridheadercell">N.Rata</s:th>
                <s:th cssclass="seda-ui-datagridheadercell ">Data Scadenza *</s:th>
                <s:th cssclass="seda-ui-datagridheadercell">Importo Boll. Rata *</s:th>
                <s:th cssclass="seda-ui-datagridheadercell">Numero Bollettino Pago PA</s:th>
                <s:th cssclass="seda-ui-datagridheadercell">Ident.Univoco Versamento</s:th>
                <s:th cssclass="seda-ui-datagridheadercell"></s:th>
              </s:tr>
            </s:thead>
            <s:tbody>
              <c:forEach
                items="${editDocumento.listaScadenze}"
                var="u"
                varStatus="loop">
                <s:tr cssclass="seda-ui-datagridrowpari">
                  <s:td cssclass="seda-ui-datagridcell">
                    <s:textbox
                      name="scad_${loop.index}_row"
                      label=""
 		              bdisable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                      text="${loop.index+1}"
                      maxlenght="4"
                      bmodify="false"
                      cssclass="textareaman text_align_right width-50px"
                      cssclasslabel="display_none" />
                  </s:td>

                  <s:td cssclass="seda-ui-datagridcell">
                  	<%-- inizio LP PG200360 --%>
                  	<c:if test="${editDocumento.modalitaAggiornamento}">
	                    <s:date
	                      prefix="scad_${loop.index}_dataScadenzaRata"
	                      label=""
	                      yearbegin="${ddlDateAnnoDa}"
	                      yearend="${ddlDateAnnoA}"
	                      calendar="${scad[loop.index].dataScadenzaRata}"
	                      validator="ignore;"
	                      locale="IT-it"
	                      descriptivemonth="false"
	                      separator="/"
	                      message=""
	   		              disabled="true" 
	                      cssclass="dateman"
	                      cssclasslabel="display_none">
	                    </s:date>
                  	</c:if>
                  	<c:if test="${!editDocumento.modalitaAggiornamento}">
                  	<%-- fine LP PG200360 --%>
				    <%-- LP PG200360 validator a required --%>	
                    <s:date
                      prefix="scad_${loop.index}_dataScadenzaRata"
                      label="Data Scadenza"
                      yearbegin="${ddlDateAnnoDa}"
                      yearend="${ddlDateAnnoA}"
                      calendar="${scad[loop.index].dataScadenzaRata}"
                      validator="required;dateISO"
                      locale="IT-it"
                      descriptivemonth="false"
                      separator="/"
                      message="[dateISO=Data Scadenza ${loop.index + 1}: ${msg_dataISO_valida}]"
   		              disabled="${editDocumento.readonly || editDocumento.delete}" 
                      cssclass="dateman"
                      cssclasslabel="display_none">
                    </s:date>
                  	<%-- inizio LP PG200360 --%>
                  	</c:if>
                  	<%-- fine LP PG200360 --%>
                    <input
                      type="hidden"
                      id="scad_${loop.index}_dataScadenzaRata_hidden"
                      value="" />
                  </s:td>
				  <%-- LP PG200360 validator a required --%>	
                  <s:td cssclass="seda-ui-datagridcell">
                    <s:textbox
                      name="scad_${loop.index}_impBollettinoRata"
                      label="Importo Scadenza"
                      text="${scad[loop.index].importoBollettinoRata}"
    		          bdisable="${editDocumento.readonly || editDocumento.delete}" 
                      maxlenght="16"
                      validator="required;accept=^[0-9]{1,13}\,[0-9]{2}?$;maxlength=16"
                      message="[accept=Importo Scadenza ${loop.index + 1}: ${msg_configurazione_importo_13_2}]"
                      showrequired="false"
                      bmodify="true"
                      cssclass="textareaman text_align_right width-8em"
                      cssclasslabel="display_none" />
                  </s:td>


                  <s:td cssclass="seda-ui-datagridcell">
                    <s:textbox
                      name="scad_${loop.index}_numeroBollettinoPagoPA"
                      text="${u.numeroBollettinoPagoPA}"
                      label=""
                      validator="ignore;maxlength=20"
                      bmodify="true"
                      maxlenght="18"
       		          bdisable="${!editDocumento.configurazioneIuvDisable || editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                      cssclass="textareaman text_align_right"
                      cssclasslabel="display_none" />
                  </s:td>

                  <s:td cssclass="seda-ui-datagridcell">
                    <s:textbox
                      name="scad_${loop.index}_identificativoUnivocoVersamento"
                      text="${u.identificativoUnivocoVersamento}"
                      label=""
                      validator="ignore;maxlength=30"
                      bmodify="true"
                      maxlenght="17"
       		          bdisable="${!editDocumento.configurazioneIuvDisable || editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
                      cssclass="textareaman "
                      cssclasslabel="display_none" />
                  </s:td>

                  <s:td cssclass="seda-ui-datagridcell">
                  	<c:if test="${!editDocumento.modalitaAggiornamento}">
	                    <s:button
	                      id="button_delete_scadenza"
	                      title="Elimina"
	                      onclick=""
	                      text="${loop.index}"
	                      validate="false"
	          		      disable="${editDocumento.readonly || editDocumento.delete || editDocumento.modalitaAggiornamento}" 
	                      type="submit"
	                      cssclass="btnimgDeletable" />
					</c:if>
                  </s:td>
                </s:tr>
              </c:forEach>
            </s:tbody>
          </s:table>
        </c:if>
      </s:div>
      
      <%-- inizio LP PG200360 --%>
	  <div id="divpopupshadowConferma" name="divpopupshadowConferma" style="display: none;" ></div>
	  <div id="divpopupshowConferma" name="divpopupshowConferma" style="display: none;" >
	      <br />  
		    <center>
	          <span id="lblMessaggioAlert" name="lblMessaggioAlert" class="seda-ui-span">
	          	${messaggiosostituisci}
	          </span>
		    </center>
          <br />  
		  	
	      <s:div name="button_container_var2" cssclass="divRicBottoni">
	        <s:button
	          id="tx_button_abort"
	          type="button"
	          text="NO"
	          onclick="hideConferma();"
	          cssclass="btnStyle"
	          validate="true" />
	        <s:button
	          id="tx_button_save"
	          type="submit"
	          text="SI"
	          onclick=""
	          cssclass="btnStyle"
	          validate="true" />
		  </s:div>	          
	  </div>
      <%-- fine LP PG200360 --%>


      <%-- Messages --%>
      <center>
        <c:if test="${message != null}">
          <s:label
            name="lblMessage"
            text="${message}" />
        </c:if>
        <c:if test="${errorMessage != null}">
          <s:label
            name="lblErrore"
            text="${errorMessage}" />
        </c:if>
      </center>
      
      <br />
      <s:div
        name="button_container_var"
        cssclass="divRicBottoni">
        <s:textbox
          name="fired_button_hidden"
          label="fired_button_hidden"
          bmodify="true"
          text=""
          cssclass="rend_display_none"
          cssclasslabel="rend_display_none" />

        <s:button
          id="tx_button_indietro"
          type="submit"
          text="Indietro"
          onclick=""
          cssclass="btnStyle"
          validate="false" />
          
         <%-- Al posto dello stesso button "salva" sarebbe più chiaro con due button? "aggiungi","sotituisci" --%>
         <%-- LP PG200360 
              NOTA. Per eseguire il salvataggio in caso di:
                mode create,
                update + modalitaAggiornamento true
                update + (modalitaAggiornamento true o stampaAvvisoEseguita true)
                update + modalitaAggiornamento false + stampaAvvisoEseguita false
                uso 3 button
          --%>
         <c:if test="${editDocumento.create}">
	        <s:button
	          id="tx_button_save"
	          type="submit"
	          text="Salva"
	          onclick=""
	          cssclass="btnStyle"
	          validate="true" />
		 </c:if>

         <c:if test="${editDocumento.update && (editDocumento.modalitaAggiornamento || editDocumento.stampaAvvisoEseguita)}">
	        <s:button
	          id="tx_button_save"
	          type="submit"
	          text="Aggiorna"
	          onclick=""
	          cssclass="btnStyle"
	          validate="true" />
		 </c:if>

         <c:if test="${editDocumento.update && !editDocumento.modalitaAggiornamento && !editDocumento.stampaAvvisoEseguita}">
	        <s:button
	          id="tx_button_save"
	          type="submit"
	          text="Aggiorna"
	          onclick=""
	          cssclass="btnStyle"
	          validate="true" />
		 </c:if>

         <c:if test="${editDocumento.delete}">
	        <s:button
	          id="tx_button_delete"
	          type="submit"
	          text="Elimina"
	          onclick=""
	          cssclass="btnStyle"
	          validate="false" />
				</c:if>

      </s:div>
      
      
			<s:textbox name="rRowsPerPage" label="rowsPerPageRic" bmodify="true" text="${rRowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="rPageNumber" label="pageNumberRic" bmodify="true" text="${rPageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="rOrder" label="orderRic" bmodify="true" text="${rOrder}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="rExt" label="rExt" bmodify="true" text="${rExt}" cssclass="display_none" cssclasslabel="display_none"  />

			<s:textbox name="rowsPerPage" label="rowsPerPageRic2" bmodify="true" text="${r2RowsPerPage}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="pageNumber" label="pageNumberRic2" bmodify="true" text="${r2PageNumber}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="order" label="orderRic2" bmodify="true" text="${r2Order}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="ext" label="ext" bmodify="true" text="${r2Ext}" cssclass="display_none" cssclasslabel="display_none"  />
	
			<s:textbox name="tx_societa" label="tx_societaRic" bmodify="true" text="${tx_societa}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tx_provincia" label="tx_provinciaRic" bmodify="true" text="${tx_provincia}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tx_utente" label="tx_utenteRic" bmodify="true" text="${tx_utente}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tx_UtenteEnte" label="tx_UtenteEnteRic" bmodify="true" text="${tx_UtenteEnte}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="impostaServ" label="ImpostaServRic" bmodify="true" text="${impostaServ}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="annoEmissione" label="annoEmissioneRic" bmodify="true" text="${annoEmissione}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="numEmissione" label="numEmissioneRic" bmodify="true" text="${numEmissione}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="codFiscale" label="codFiscaleDoc" bmodify="true" text="${codFiscale}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="ufficioImpositore" label="ufficioImpositoreRic" bmodify="true" text="${ufficioImpositore}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="stato_documento" label="statoDocumentoRic" bmodify="true" text="${stato_documento}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="numDocumento" label="numDocumentoRic" bmodify="true" text="${numDocumento}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="stato_sospensione" label="stato_sospensioneRic" bmodify="true" text="${stato_sospensione}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="stato_procedure" label="stato_procedureRic" bmodify="true" text="${stato_procedure}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="denominazione" label="denominazione" bmodify="true" text="${denominazione}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tipoRic" label="tipoRic" bmodify="true" text="${tipoRic}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CHIAVI DI RICERCA
			 	IMPOSTA SERVIZIO  EH1_CISECISE -->
			<s:textbox name="chiaveIS" label="chiaveISDett" bmodify="true" text="${chiaveIS}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- NUMERO DOCUMENTO EH1_CEH1NDOC -->
			<s:textbox name="chiaveDoc" label="chiaveDocDett" bmodify="true" text="${chiaveDoc}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- TIPO UFFICIO EH1_TANETUFF -->
			<s:textbox name="chiaveTipoUff" label="chiaveTipoUffDett" bmodify="true" text="${chiaveTipoUff}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CODICE UFFICIO EH1_CANECUFF -->
			<s:textbox name="chiaveCodUff" label="chiaveCodUffDett" bmodify="true" text="${chiaveCodUff}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CODICE UTENTE EH1_CUTECUTE -->
			<s:textbox name="chiaveCodUte" label="chiaveCodUteDett" bmodify="true" text="${chiaveCodUte}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CODICE ENTE EH1_CANECENT -->
			<s:textbox name="chiaveCodEnte" label="chiaveCodEnteDett" bmodify="true" text="${chiaveCodEnte}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CODICE SERVIZIO EH1_TEH1SERV -->
			<s:textbox name="chiaveServ" label="chiaveServDett" bmodify="true" text="${chiaveServ}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CODICE TOMBSTONE EH1_CEH1TOMB -->
			<s:textbox name="chiaveTomb" label="chiaveTombDett" bmodify="true" text="${chiaveTomb}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- PROGR.FLUSSO EH1_PEH1FLUS -->
			<s:textbox name="chiaveFlusso" label="chiaveFlussoDett" bmodify="true" text="${chiaveFlusso}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- DATA CREAZIONE EH1_GEH1CREA -->
			<s:textbox name="chiaveDataCrea" label="chiaveDataCrea" bmodify="true" text="${chiaveDataCrea}" cssclass="display_none" cssclasslabel="display_none"  />
		
			<!-- CODICE SERVIZIO EH8_PEH8FLUS  -->
			<s:textbox name="chiaveAnaFlusso" label="chiaveAnaFlussoDett" bmodify="true" text="${chiaveAnaFlusso}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CODICE SERVIZIO EH8_GEH8CREA -->
			<s:textbox name="chiaveAnaDataCrea" label="chiaveAnaDataCreaDett" bmodify="true" text="${chiaveAnaDataCrea}" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- CODICE SERVIZIO EH8_TEH8SERV -->
			<s:textbox name="chiaveAnaServ" label="chiaveAnaServDett" bmodify="true" text="${chiaveAnaServ}" cssclass="display_none" cssclasslabel="display_none"  />

			<!-- CHIAVE EMISSIONE AGG -->
			<s:textbox name="chiaveAnnoE" label="chiaveAnnoE" bmodify="true" text="${chiaveAnnoE}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="chiaveNumeroE" label="chiaveNumeroE" bmodify="true" text="${chiaveNumeroE}" cssclass="display_none" cssclasslabel="display_none"  />

			<!-- RIF.PAG.PREC. 	value="ricercaAnagrafica" value="ricercaEmissioni" value="ricercaDocumenti" -->
			<s:textbox name="pagPrec" label="pagPrecRifChiamante" bmodify="true" text="${pagPrec}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="loadDett" label="loadDett" bmodify="true" text="false" cssclass="display_none" cssclasslabel="display_none"  />
			<!-- RIF.ANAGRAFICA -->
			<s:textbox name="denominazione1" label="denominazione" bmodify="true" text="${denominazione}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="tipoRic1" label="tipoRicerca" bmodify="true" text="${tipoRic}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="codFiscAna" label="tipoRicerca" bmodify="true" text="${codFiscAna}" cssclass="display_none" cssclasslabel="display_none"  />
			<s:textbox name="chiaveCodFisc" label="chiaveCodFisc" bmodify="true" text="${chiaveCodFisc}" cssclass="display_none" cssclasslabel="display_none"  />
	
			<s:textbox name="ext" label="extRic" bmodify="true" text="${ext}" cssclass="display_none" cssclasslabel="display_none"  />
	
			<!-- CHIAVE I/S EH1_CISECISE -->
			<s:textbox name="dettImpostaServ" label="dettEnte" bmodify="true" text="${dettImpostaServ}" cssclass="display_none" cssclasslabel="display_none"  />
		
      
    </s:form>
  </c:when>

</c:choose>

