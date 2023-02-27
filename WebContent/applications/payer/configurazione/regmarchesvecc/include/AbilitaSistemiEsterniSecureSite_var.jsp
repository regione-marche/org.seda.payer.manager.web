<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="AbilitaSistemiEsterniSecureSite_var" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_form" action="${do_command_name}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">ABILITAZIONE SISTEMA ESTERNO</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">ABILITAZIONE SISTEMA ESTERNO</s:div>
			</c:if>
						
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<c:if test="${codop == 'add'}">
					<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<!--  SEC_CSECURL URL ACCESSO SISTEMA ESTERNO  -->
						<s:div name="top" cssclass="divRicMetadatiLeft">
							<s:textbox bmodify="${codop != 'edit'}" name="urlAccesso"
								label="Url: " maxlenght="512" showrequired="true"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="required;url"
								text="${urlAccesso}" />					
						</s:div>
						
						<!--  SEC_KSECSERV ID SERVIZIO ABILITATO  -->	
						<s:div name="divElement15" cssclass="divRicMetadatiCenter">
							<s:textbox bmodify="${codop != 'edit'}" name="idServizio"
								label="Id. Servizio: " maxlenght="64" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;maxlength=64;accept=^\w{1,64}$"
								text="${idServizio}" message="[accept=Id. Servizio: ${msg_configurazione_alfanumerici}]"/>
						</s:div>
					</s:div>
	
	
					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					
					<!--  SEC_DSECDESC DESCRIZIONE SISTEMA ESTERNO  -->
						<s:div name="divElement16" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="descrizione"
								label="Descrizione: " maxlenght="100" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione_regex100}"
								text="${descrizione}" message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"/>
						</s:div>	
					
					<!--  SEC_DSECPIMG PATH FILE IMMAGINE PER PERSONALIZZAZIONE CARRELLO  -->
						<s:div name="divElement17" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="pathFileImmagine"
								label="Path Img.: " maxlenght="200" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;minlength=1;maxlength=200"
								text="${pathFileImmagine}" />
						</s:div>
					
					</s:div>
					
					<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					
					
					<!--  SEC_CSECCTSE CODICE TIPOLOGIA SERVIZIO  -->
						<s:div name="divElement18" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="codiceTipologiaServizio"
								label="Tipol. Serv.:" maxlenght="3" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;maxlength=3"
								text="${codiceTipologiaServizio}" />
						</s:div>
						
					</s:div>
	
					
					<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
	
					<!--   SEC_FSECFATT FLAG ATTIVAZIONE URL  -->
						<s:div name="divElement50" cssclass="divRicMetadatiSingleRow">
							<s:list bradio="false" bchecked="${chk_flagAttivazione}" validator="ignore" 
							 cssclasslabel="bold checklabel label150"  cssclass="checkleft"
							name="flagAttivazione" groupname="flagAttivazione" 
							text="Attivazione" value="Y" />
						</s:div>
						
					<!--   SEC_FSECREDI FLAG REDIRECT -->
						<s:div name="divElement51" cssclass="divRicMetadatiSingleRow">
							<s:list bradio="false" bchecked="${chk_flagRedirect}" validator="ignore" 
							 cssclasslabel="bold checklabel label150" cssclass="checkleft"
							name="flagRedirect" groupname="flagRedirect" 
							text="Redirect" value="Y" />
						</s:div>
						
						
					<!--   SEC_FSECCOST FLAG CALCOLO COSTI TRANSAZIONE PER SERVIZIO DI PAGAMENTO ESTERNO  -->
						<s:div name="divElement54" cssclass="divRicMetadatiSingleRow">
							<s:list bradio="false" bchecked="${chk_flagCalcoloCosti}" validator="ignore" 
							 cssclasslabel="bold checklabel label150"  cssclass="checkleft"
							name="flagCalcoloCosti" groupname="flagCalcoloCosti" 
							text="Calcolo costi" value="Y" />
						</s:div>
						
					<!--   SEC_FSECNOTI FLAG INVIO NOTIFICA PAYER -->
						<s:div name="divElement55" cssclass="divRicMetadatiSingleRow">
							<s:list bradio="false" bchecked="${chk_flagInvioNotifica}" validator="ignore" 
							 cssclasslabel="bold checklabel label150" cssclass="checkleft"
							name="flagInvioNotifica" groupname="flagInvioNotifica" 
							text="Invio notifica" value="Y" />
						</s:div>
							
					</s:div>
				</c:if>
				
				<c:if test="${codop == 'edit'}">
					<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="top" cssclass="divRicMetadatiLeft">
							<s:textbox bmodify="false" name="urlAccesso"
								label="Url: " maxlenght="512" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled"
								text="${urlAccesso}" />					
						</s:div>
						
						<!--  SEC_KSECSERV ID SERVIZIO ABILITATO  -->	
						<s:div name="divElement19" cssclass="divRicMetadatiCenter">
							<s:textbox bmodify="false" name="idServizio"
								label="Id. Servizio: " maxlenght="64" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman colordisabled"
								text="${idServizio}" />
						</s:div>
					
					</s:div>
	
					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
						<!--  SEC_DSECDESC DESCRIZIONE SISTEMA ESTERNO  -->
						<s:div name="divElement20" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="descrizione"
								label="Descrizione: " maxlenght="100" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione_regex100}"
								text="${descrizione}" message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"/>					
						</s:div>
						
						<!--  SEC_DSECPIMG PATH FILE IMMAGINE PER PERSONALIZZAZIONE CARRELLO  -->
						<s:div name="divElement21" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="pathFileImmagine"
								label="Path Img.: " maxlenght="200" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;minlength=1;maxlength=200"
								text="${pathFileImmagine}" />
						</s:div>
						
						
					
					</s:div>
					
					<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
						<!--  SEC_CSECCTSE CODICE TIPOLOGIA SERVIZIO  -->
						<s:div name="divElement22" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="codiceTipologiaServizio"
								label="Tipol. Serv.:" maxlenght="3" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;maxlength=3"
								text="${codiceTipologiaServizio}" />
						</s:div>
					
					</s:div>	
					<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					
						<!--   SEC_FSECFATT FLAG ATTIVAZIONE URL  -->
						<s:div name="divElement52" cssclass="divRicMetadatiSingleRow">
							<s:list bradio="false" bchecked="${chk_flagAttivazione}" validator="ignore" 
							 cssclasslabel="bold checklabel label150" cssclass="checkleft"
							name="flagAttivazione" groupname="flagAttivazione" 
							text="Attivazione" value="Y" />
						</s:div>
						
					<!--   SEC_FSECREDI FLAG REDIRECT -->
						<s:div name="divElement53" cssclass="divRicMetadatiSingleRow">
							<s:list bradio="false" bchecked="${chk_flagRedirect}" validator="ignore" 
							 cssclasslabel="bold checklabel label150" cssclass="checkleft"
							name="flagRedirect" groupname="flagRedirect" 
							text="Redirect" value="Y" />
						</s:div>
						
					<!--   SEC_FSECCOST FLAG CALCOLO COSTI TRANSAZIONE PER SERVIZIO DI PAGAMENTO ESTERNO  -->
						<s:div name="divElement56" cssclass="divRicMetadatiSingleRow">
							<s:list bradio="false" bchecked="${chk_flagCalcoloCosti}" validator="ignore" 
							 cssclasslabel="bold checklabel label150"  cssclass="checkleft"
							name="flagCalcoloCosti" groupname="flagCalcoloCosti" 
							text="Calcolo costi" value="Y" />
						</s:div>
						
					<!--   SEC_FSECNOTI FLAG INVIO NOTIFICA PAYER -->
						<s:div name="divElement57" cssclass="divRicMetadatiSingleRow">
							<s:list bradio="false" bchecked="${chk_flagInvioNotifica}" validator="ignore" 
							 cssclasslabel="bold checklabel label150" cssclass="checkleft"
							name="flagInvioNotifica" groupname="flagInvioNotifica" 
							text="Invio notifica" value="Y" />
						</s:div>
						
						
					</s:div>
					
				</c:if>
				
			</s:div>
			<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_indietro" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
				
			</s:div>
			<input type="hidden" name="codop" value="${codop}" />
			<input type="hidden" name="tx_url" value="${tx_url}" />
			<input type="hidden" name="tx_idServizio" value="${tx_idServizio}" />
	</s:form>
	</s:div>
	
</s:div>
