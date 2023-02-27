<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="logrequest_integraente_dettaglio" encodeAttributes="true" />


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

<c:if test="${!empty logWin}">
	<s:div name="div_selezione" cssclass="divSelezione">
		<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	
			<s:div name="divRicercaTitleName" cssclass="divRicTitle ">DETTAGLIO LOG REQUEST INTEGRAENTE</s:div>
		
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
	
	 			<s:div name="divLogReqMetadatiTop10" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="id_logWin" label="idRequest:" text="${logWin.idLog}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            
             	<s:div name="divLogReqMetadatiTop100" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="dataInizioChiamata_logWin" label="data Inizio Chiamata: " text="${dataInizioChiamataUserFormat}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />	            
	            </s:div>
	            <s:div name="divLogReqMetadatiTop110" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="dataFineChiamata_logWin" label="data Fine Chiamata: " text="${dataFineChiamataUserFormat}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />	            
	            </s:div>
	            
	            
	            <s:div name="divLogReqMetadatiTop20" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="tipoChiamata_logWin" label="Tipo chiamata:" text="${logWin.tipoChiamata}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop21" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="esito_logWin" label="Esito:" text="${logWin.esito}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	             
	            <s:div name="divLogReqMetadatiTop22" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="messaggioErrore_logWin" label="Messaggio errore:" text="${logWin.messaggioErrore}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>       
	
	            <s:div name="divLogReqMetadatiTop30" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="xmlRequest_logWin" label="Xml In:" text="${logWin.xmlRequest}"
	                bmodify="false" row="6" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop40" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="xmlResponse_logWin" label="Xml Out:" text="${logWin.xmlResponse}"
	                bmodify="false" row="6" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	                  
             	<s:div name="divLogReqMetadatiTop41" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="codiceUtente_logWin" label="Codice utente: " text="${logWin.codiceUtente}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
            
            	</s:div>
	             <s:div name="divLogReqMetadatiTop50" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="codiceSocieta_logWin" label="Codice società: " text="${logWin.codiceSocieta}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop60" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="codiceEnte_logWin" label="Codice ente: " text="${logWin.codiceEnte}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop70" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="bollettino_logWin" label="Bollettino: " text="${logWin.bollettino}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop80" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="codiceFiscale_logWin" label="Codice fiscale: " text="${logWin.codiceFiscale}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />	            
	            </s:div>
	            
             	<s:div name="divLogReqMetadatiTop90" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="idDominio_logWin" label="Id dominio: " text="${logWin.idDominio}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />	            
	            </s:div>
	            
	              
	            <s:div name="divLogReqMetadatiTop23" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="dataInserimento_logWin" label="Data inserimento:" text="${dataInserimentoUserFormat}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            
		        <s:div name="divRicMetadati" cssclass="divRicMetadati">
					<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					
					<s:form name="form_indietro"
							action="logRequestIntegraente.do?tx_button_cerca=cerca" method="post"
							hasbtn1="false" hasbtn2="false" hasbtn3="false">
							<s:button id="button_indietro" onclick="" text="Indietro" cssclass="btnStyle divButton1"
								type="submit" />
					</s:form>
			
					</s:div>
				</s:div>
			   
		
			</s:div>
	
		</s:div>
	
	</s:div>
</c:if>







