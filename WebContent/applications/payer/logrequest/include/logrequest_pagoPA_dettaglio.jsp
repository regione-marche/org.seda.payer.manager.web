<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="logrequest_pagoPA_dettaglio" encodeAttributes="true" />


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

<c:if test="${!empty logPap}">
	<s:div name="div_selezione" cssclass="divSelezione">
		<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	
			<s:div name="divRicercaTitleName" cssclass="divRicTitle ">DETTAGLIO LOG REQUEST INTEGRAENTE</s:div>
		
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
	
	 			<s:div name="divLogReqMetadatiTop10" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="id_logPap" label="idRequest:" text="${logPap.idLog}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            
             	<s:div name="divLogReqMetadatiTop100" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="dataInizioChiamata_logPap" label="data Inizio Chiamata: " text="${dataInizioChiamataUserFormat}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />	            
	            </s:div>
	            <s:div name="divLogReqMetadatiTop110" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="dataFineChiamata_logPap" label="data Fine Chiamata: " text="${dataFineChiamataUserFormat}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />	            
	            </s:div>

                
             	<s:div name="divLogReqMetadatiTop90" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="idDominio_logPap" label="Id dominio: " text="${logPap.idDominio}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />	            
	            </s:div>

                <s:div name="divLogReqMetadatiTop90" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="iuv_logPap" label="Codice IUV: " text="${logPap.iuv}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />	            
	            </s:div>

                <s:div name="divLogReqMetadatiTop41" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="codiceUtente_logPap" label="Codice utente: " text="${logPap.codiceUtente}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
            
            	</s:div>
            	
            	<s:div name="divLogReqMetadatiTop42" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="operazione_logPap" label="Operazione: " text="${logPap.operazione}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
            
            	</s:div>
	            
	            
	            <s:div name="divLogReqMetadatiTop21" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="esito_logPap" label="Esito:" text="${logPap.esito}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	             
	            <s:div name="divLogReqMetadatiTop22" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="errore_logPap" label="Errore:" text="${logPap.errore}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>       
	
	            <s:div name="divLogReqMetadatiTop30" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="xmlRequest_logPap" label="Xml In:" text="${logPap.xmlRequest}"
	                bmodify="false" row="6" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop40" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="xmlResponse_logPap" label="Xml Out:" text="${logPap.xmlResponse}"
	                bmodify="false" row="6" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	                  
             	
	            
	              
	            <s:div name="divLogReqMetadatiTop23" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="dataInserimento_logPap" label="Data inserimento:" text="${dataInserimentoUserFormat}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div>
	            
		        <s:div name="divRicMetadati" cssclass="divRicMetadati">
					<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					
					<s:form name="form_indietro"
							action="logRequestPagoPA.do?tx_button_cerca=cerca" method="post"
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







