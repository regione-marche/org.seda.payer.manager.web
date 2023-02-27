<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>

<%--  <m:view_state id="viewstate" encodeAttributes="true" /> --%>

<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js" type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js" type="text/javascript"></script>

<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}              


	$(function() {
		console.log("controllo ddl js attivo");
		var element;
		if (document.readyState === "complete" || document.readyState === "interactive") {

			/*Tipologia servizio*/
			var dd = document.getElementById('tx_tipologia_servizio');
			for (var i = 0; i < dd.options.length; i++) {
				console.log(dd.options[i].text);
				if(dd.options[i].text.trim().includes("-Tutte le tipologie")) {
					console.log("Rimozione option " + dd.options[i].text);
					dd.remove(i);
					break;
				}
			}
			for (var i = 0; i < dd.options.length; i++) {
			    if (dd.options[i].value.trim() === document.getElementById("tipServVal").value.trim()) {
			        dd.selectedIndex = i;
			        break;
			    }
			}


		    /*Imposta servizio*/
			var dd = document.getElementById('tx_imposta_servizio');
			for (var i = 0; i < dd.options.length; i++) {
			    if (dd.options[i].value.trim() === document.getElementById("impostaServVal").value.trim()) {
			        dd.selectedIndex = i;
			        break;
			    }
			}

		    /*Stato documento*/
			var dd = document.getElementById('stato_documento');
			for (var i = 0; i < dd.options.length; i++) {
			    if (dd.options[i].value.trim() === document.getElementById("statoDocVal").value.trim()) {
			        dd.selectedIndex = i;
			        break;
			    }
			}
			
		}
		
	});

	function salvaImpostaServizio() {
		console.log("cambio imposta servizio");
		var e = document.getElementById("tx_imposta_servizio");
		document.getElementById("impostaServVal").value = e.options[e.selectedIndex].value;
		console.log("valore ddl : " + e.options[e.selectedIndex].value);
		console.log("valore impostaServVal : " + document.getElementById("impostaServVal").value);
	}

	function salvaTipologiaServizio() {
		console.log("cambio imposta servizio");
		var e = document.getElementById("tx_tipologia_servizio");
		document.getElementById("tipServVal").value = e.options[e.selectedIndex].value;
		console.log("valore ddl : " + e.options[e.selectedIndex].value);
		console.log("valore impostaServVal : " + document.getElementById("tipServVal").value);
	}

	function salvaStatoDocumento() {
		console.log("cambio imposta servizio");
		var e = document.getElementById("stato_documento");
		document.getElementById("statoDocVal").value = e.options[e.selectedIndex].value;
		console.log("valore ddl : " + e.options[e.selectedIndex].value);
		console.log("valore statoDocVal : " + document.getElementById("statoDocVal").value);
	}
	               
</script>



	<c:url value="" var="formParameters">
		<c:param name="test">ok</c:param>
		<c:if test="${!empty rowsPerPage}">
			<c:param name="r2RowsPerPage">${rowsPerPage}</c:param> 
		</c:if>
		<c:if test="${!empty pageNumber}">
			<c:param name="r2PageNumber">${pageNumber}</c:param> 
		</c:if>
		<c:if test="${!empty order}">
			<c:param name="r2Order">${order}</c:param> 
		</c:if>
		<c:if test="${!empty ext}">
			<c:param name="r2Ext">${ext}</c:param>
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
		<c:if test="${!empty codFiscale}">  
			<c:param name="codFiscale">${codFiscale}</c:param>
		</c:if>
		<c:if test="${!empty numEmissione}">  
			<c:param name="numEmissione">${numEmissione}</c:param>
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
		
		
		<c:if test="${!empty numeroBollettino}">
			<c:param name="numeroBollettino">${numeroBollettino}</c:param> 
		</c:if>
		<c:if test="${!empty numeroIUV}">
			<c:param name="numeroIUV">${numeroIUV}</c:param> 
		</c:if>
		
	</c:url>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<%-- obsolete
						<s:textbox name="screen" label="" bmodify="true" text="search" cssclass="display_none" 
					cssclasslabel="display_none"  /> --%>
					
		<s:form name="gestioneDocumentiCaricoForm" action="gestioneDocumentiCarico.do" method="post" 
				hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:textbox name="DDLType" label="DDLType" bmodify="true" text="11" cssclass="display_none" 
					cssclasslabel="display_none"  />
					
			<s:textbox name="tipServVal" label="tipologia servizio" bmodify="false" text="${tx_tipologia_servizio_value}" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="impostaServVal" label="imposta servizio" bmodify="false" text="${tx_imposta_servizio_value}" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="statoDocVal" label="stato documento" bmodify="false" text="${stato_documento_value}" cssclass="display_none" cssclasslabel="display_none" />
		
				<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
						Ricerca Documenti
				</s:div>
	
				<s:div name="divRicMetadati" cssclass="divRicMetadati">
					<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="" cssclass="divRicMetadatiTopLeft">
							<s:dropdownlist name="tx_societa" disable="${filtroDocumenti.ddlSocietaDisabled}"
									cssclass="tbddl floatleft" label="Societ&agrave;:"
									cssclasslabel="label85 bold floatleft textright"
									cachedrowset="listaSocieta" usexml="true"
									onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
									valueselected="${filtroDocumenti.keySocieta}">
								<s:ddloption text="Tutte le Societ&agrave;" value="" />
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_societa_changed" 
										disable="${filtroDocumenti.ddlSocietaDisabled}" onclick="" text="" 
										type="submit" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
							</noscript>
				    </s:div>
	
						<s:div name="" cssclass="divRicMetadatiTopCenter">
							<s:dropdownlist name="tx_utente"
									disable="${filtroDocumenti.ddlUtenteDisabled}" cssclass="tbddl floatleft"
									label="Utente:"
									cssclasslabel="label65 bold floatleft textright"
									cachedrowset="listaUtenti" usexml="true"
									onchange="setFiredButton('tx_button_utente_changed');this.form.submit();"
									valueselected="${filtroDocumenti.keyUtente}">
								<s:ddloption text="Tutti gli Utenti" value="" />
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_utente_changed"
										disable="${filtroDocumenti.ddlUtenteDisabled}" onclick="" text=""
										type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
							</noscript>
				    </s:div>
						
						<s:div name="" cssclass="divRicMetadatiTopRight">
							<s:dropdownlist 
									name="tx_ente"
									label="Ente:" 
									valueselected="${filtroDocumenti.keyEnte}"
									cachedrowset="listaEnti" usexml="true"
									disable="${filtroDocumenti.ddlEnteDisabled}" 
									cssclass="tbddlMax floatleft"
									cssclasslabel="label65 bold textright"
									onchange="setFiredButton('tx_button_ente_changed');this.form.submit();">
								<s:ddloption text="Tutti gli Enti" value="" />
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
				      <noscript>
								<s:button id="tx_button_ente_changed"
										disable="${filtroDocumenti.ddlEnteDisabled}" onclick="" text=""
										type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
							</noscript>
				    </s:div>
					</s:div>
					
					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
						
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist 
									name="tx_tipologia_servizio" 
									label="Tip. Servizio:"
									disable="false"	cssclass="tbddlMax floatleft" 
									cssclasslabel="label85 bold textright"
									cachedrowset="listaTipologieServizio" usexml="true"
									onchange="setFiredButton('tx_button_tipologia_servizio_changed');salvaTipologiaServizio();this.form.submit();"
									valueselected="${filtroDocumenti.keyTipologiaServizio}">
								<s:ddloption text="Tutte le tipologie" value="" />
								<s:ddloption text="{1}-{2}" value="{3}{1}{2}" />
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_tipologia_servizio_changed"
										disable="false" onclick="" text=""
										type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
							</noscript>
						</s:div>
						
						<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
									bmodify="true" name="tx_anno_emissione" label="Anno Emis.:"
									maxlenght="4"
									text="${filtroDocumenti.annoEmissione}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
						</s:div>
						
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist  name="stato_documento" disable="false" label="Stato Doc.:"
									valueselected="${filtroDocumenti.stato_documento}" 
									onchange="salvaStatoDocumento();"
							  	cssclass="tbddlMax floatleft" cssclasslabel="label85 bold textright">
								<s:ddloption value="" text="tutti i documenti"/>
								<s:ddloption value="S" text="Saldato"/>
								<s:ddloption value="P" text="Parzialmente Movimentato"/>
								<s:ddloption value="N" text="Non Movimentato"/>
							</s:dropdownlist>
						</s:div>
					</s:div>	
					
					<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
						
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="tx_imposta_servizio"
									disable="${filtroDocumenti.ddlImpostaServizioDisabled}" cssclass="tbddlMax floatleft"
									label="Imp.Serv.(I/S):" cssclasslabel="label85 bold textright"
									onchange="salvaImpostaServizio()"
									cachedrowset="listaImpostaServizio" usexml="true"
									valueselected="${filtroDocumenti.keyImpostaServizio}">
								<s:ddloption text="Tutti i Servizi" value="" />
								<s:ddloption text="{3}-{4}" value="{1}{2}{3}{4}" />
							</s:dropdownlist>
						</s:div>
						
						<s:div name="divElement61" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
									bmodify="true" name="numEmissione" label="Numero Emis.:"
									maxlenght="10"
									text="${filtroDocumenti.numEmissione}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
						</s:div>
						
						
						<s:div name="divElement161" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
									bmodify="true" name="numeroBollettino" label="num. Avviso pagoPA:"
									maxlenght="18"
									text="${filtroDocumenti.numeroBollettino}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
						</s:div>
						
						
					</s:div>
					
					<s:div name="divRicercaLeft1" cssclass="divRicMetadatiRight">
						<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
									bmodify="true" name="codFiscale" label="Cod. Fiscale:"
									maxlenght="16"
									text="${filtroDocumenti.codFiscale}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
						</s:div>
						
						<s:div name="divNumDocumenti" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
									bmodify="true" name="numDocumento" label="Numero Doc.:"
									maxlenght="20"
									text="${filtroDocumenti.numDocumento}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
						</s:div>
						
						<s:div name="divElement162" cssclass="divRicMetadatiSingleRow">
							<s:textbox validator="ignore"
									bmodify="true" name="numeroIUV" label="codice IUV:"
									maxlenght="17"
									text="${filtroDocumenti.numeroIUV}"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright" />
						</s:div>
						
	 			  </s:div>
				</s:div>
	
				<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					<s:button id="tx_button_cerca" type="submit" text="Cerca" onclick="" cssclass="btnStyle"/>
					<s:button id="tx_button_reset" type="submit" text="Reset" onclick="" cssclass="btnStyle" />
	 
					<c:if test="${!empty listaDocumenti && ext=='0'}">
						<s:button id="tx_button_cerca_exp" type="submit" text="Espandi" onclick="" cssclass="btnStyle" />
					</c:if>
					<c:if test="${!empty listaDocumenti && ext=='1'}">
						<s:button id="tx_button_cerca_exp" type="submit" text="Contrai" onclick="" cssclass="btnStyle" />
					</c:if>
					
					<s:button id="button_nuovo" type="submit" text="Nuovo" onclick="" cssclass="btnStyle" />
	
					<s:textbox name="fired_button_hidden" label="fired_button_hidden"
							bmodify="true" text="" cssclass="rend_display_none"
							cssclasslabel="rend_display_none" />
				</s:div>
		
		
		
			</s:form>
	</s:div>
</s:div>

<%-- se sono stato invocato per la prima volta da menu... eseguo un reset, per visualizzare corretamente la barra menu --%>
<c:if test="${workaroundMenu == true}">
	<script type="text/javascript">		

	$(function() {
		setFiredButton("tx_button_reset");    

		$("#gestioneDocumentiCaricoForm").submit();
	});
	</script>
</c:if>
