<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="logrequest_dettaglio" encodeAttributes="true" />


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

<c:if test="${!empty logRequest}">
	<s:div name="div_selezione" cssclass="divSelezione">
		<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	
			<s:div name="divRicercaTitleName" cssclass="divRicTitle ">DETTAGLIO LOG REQUEST</s:div>
		
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
	
	 			<s:div name="divLogReqMetadatiTop10" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="idRequest_logRequest" label="Id Request:" text="${logRequest.idRequest}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
              	<s:div name="divLogReqMetadatiTop20" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="sessionID_logRequest" label="Session Id:" text="${logRequest.sessionID}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	            
	 			<s:div name="divLogReqMetadatiTop21" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="dataRequest_logRequest" label="data Request:" text="${dataRequestUserFormat}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	           
	           <s:div name="divLogReqMetadatiTop30" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="indirizzoIP_logRequest" label="indirizzo IP:" text="${logRequest.indirizzoIP}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div> 
	            
	            <s:div name="divLogReqMetadatiTop31" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="operatoreRequest_logRequest" label="Codice portale:" text="${logRequest.operatoreRequest}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	            
	            
	          <%--   <s:div name="divLogReqMetadatiTop40" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="dbSchemaCodSocieta_logRequest" label="dbSchemaCodSocieta:" text="${logRequest.dbSchemaCodSocieta}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            </s:div> --%>
	            

 				<s:div name="divLogReqMetadatiTop50" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="tipoRequest_logRequest" label="Tipo Request:" text="${logRequest.tipoRequest}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
				<s:div name="divLogReqMetadatiTop60" cssclass="divLogReqMetadatiTop">
				    <s:textarea name="action_logRequest" label="Action:" text="${logRequest.action}"
				        bmodify="false" row="1" col="80" 
				        cssclasslabel="logReqDettaglioLabel" 
				        cssclass = "logReqDettaglioTextarea"
				    />
				</s:div>
				
				
				<s:div name="divLogReqMetadatiTop61" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="applicativo_logRequest" label="Applicativo:" text="${logRequest.applicativo}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	                                
	            <%-- <s:div name="divLogReqMetadatiTop62" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="belfioreRequest_logRequest" label="belfioreRequest:" text="${logRequest.belfioreRequest}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div> --%>
	            
	            <s:div name="divLogReqMetadatiTop63" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="canalePagamento_logRequest" label="Canale pagamento:" text="${logRequest.canalePagamento}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	          
	            
	           <%--  <s:div name="divLogReqMetadatiTop64" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="dataFine_a_logRequest" label="dataFine_a:" text="${logRequest.dataFine_a}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div> --%>
	            
	            <%-- <s:div name="divLogReqMetadatiTop70" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="dataInizio_da_logRequest" label="dataInizio_da:" text="${logRequest.dataInizio_da}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div> --%>
	            
	           
	            
	    
	            
	            <s:div name="divLogReqMetadatiTop71" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="error_logRequest" label="Errore:" text="${logRequest.error}"
	                bmodify="false" row="3" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	            
	            
	            <s:div name="divLogReqMetadatiTop72" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="param_logRequest" label="Param:" text="${logRequest.param}"
	                bmodify="false" row="6" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	           
	                   <s:div name="divLogReqMetadatiTop80" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="descrizioneEnte_logRequest" label="Descrizione ente:" text="${logRequest.descrizioneEnte}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	            
	            
	            <s:div name="divLogReqMetadatiTop81" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="numeroBollettino_logRequest" label="Numero bollettino:" text="${logRequest.numeroBollettino}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop82" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="numeroDocumento_logRequest" label="Numero documento:" text="${logRequest.numeroDocumento}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop83" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="numeroIUV_logRequest" label="Numero IUV:" text="${logRequest.numeroIUV}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	              <s:div name="divLogReqMetadatiTop84" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="chiaveEnte_logRequest" label="Chiave ente:" text="${logRequest.chiaveEnte}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop85" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="chiaveTransazione_logRequest" label="Chiave transazione:" text="${logRequest.chiaveTransazione}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop86" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="codiceFiscale_logRequest" label="Codice fiscale:" text="${logRequest.codiceFiscale}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop90" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="codiceSocieta_logRequest" label="Codice societa:" text="${logRequest.codiceSocieta}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	                                
	            <s:div name="divLogReqMetadatiTop91" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="codiceUtente_logRequest" label="Codice utente:" text="${logRequest.codiceUtente}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	                                
	            <s:div name="divLogReqMetadatiTop92" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="comuneRequest_logRequest" label="Comune request:" text="${logRequest.comuneRequest}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop93" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="queryString_logRequest" label="Query String:" text="${logRequest.queryString}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	            <s:div name="divLogReqMetadatiTop100" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="request_logRequest" label="Request:" text="${logRequest.request}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	          
	            <s:div name="divLogReqMetadatiTop101" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="sezioneApplicativa_logRequest" label="Sezione applicativa:" text="${logRequest.sezioneApplicativa}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div>
	            
	            <%-- <s:div name="divLogReqMetadatiTop102" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="siglaProvinciaEnte_logRequest" label="siglaProvinciaEnte:" text="${logRequest.siglaProvinciaEnte}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div> --%>
	            
	            <%-- <s:div name="divLogReqMetadatiTop103" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="siglaProvinciaRequest_logRequest" label="siglaProvinciaRequest:" text="${logRequest.siglaProvinciaRequest}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div> --%>
	            
	           <%--  <s:div name="divLogReqMetadatiTop104" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="tagSuffissoTabella_logRequest" label="tagSuffissoTabella:" text="${logRequest.tagSuffissoTabella}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div> --%>
	            
	           <%--  <s:div name="divLogReqMetadatiTop105" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="template_logRequest" label="template:" text="${logRequest.template}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div> --%>
	            
	           
	            
	           <%--  <s:div name="divLogReqMetadatiTop110" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="userName_logRequest" label="userName:" text="${logRequest.userName}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea"
	                />
	            
	            </s:div> --%>
	            
	            <%-- <s:div name="divLogReqMetadatiTop110" cssclass="divLogReqMetadatiTop">
	                <s:textarea name="userProfile_logRequest" label="userProfile:" text="${logRequest.userProfile}"
	                bmodify="false" row="1" col="80" 
	                cssclasslabel="logReqDettaglioLabel" 
	                cssclass = "logReqDettaglioTextarea" 
	                />
	     
	            </s:div> --%>
        
        		<s:div name="divRicMetadati" cssclass="divRicMetadati">
					<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					
					<s:form name="form_indietro"
							action="logRequest.do?tx_button_cerca=cerca" method="post"
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







