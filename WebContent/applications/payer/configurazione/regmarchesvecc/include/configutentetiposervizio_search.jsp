<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="configutentetiposervizios" encodeAttributes="true"/>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
<s:div name="divRicercaTopName" cssclass="divRicercaTop">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Utente - Tipologia Servizio
		</s:div>
	<s:form name="form_ricerca" action="configutentetiposervizio.do"
		method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				
					<s:div name="socLabel_srch" cssclass="divRicMetadatiLeft">
						
									<s:textbox bmodify="true" 
										maxlenght="256"
										 validator="ignore;accept=${configurazione_descrizione256_regex}"
										label="Societ&agrave;:" name="configutentetiposervizio_desSoc"
										text="${configutentetiposervizio_desSoc}"
										message="[accept=Societ&agrave;: ${msg_configurazione_descrizione_regex}]"
										cssclasslabel="label85 bold textright" cssclass="textareaman" />
						
					</s:div>
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
							
								<s:textbox bmodify="true"
									maxlenght="256"
										 validator="ignore;accept=${configurazione_descrizione256_regex}"
									label="Utente:" name="configutentetiposervizio_desUte"
									text="${configutentetiposervizio_desUte}"
									message="[accept=Utente: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							
					</s:div>
					
					
				</s:div>
				<!-- PG22XX09_YL5 INI -->
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="configutentetiposervizio_desTp"
							label="Descr. Tipol. Serv.:" maxlenght="256"
								validator="ignore;accept=${configurazione_descrizione256_regex}"
							cssclasslabel="label85 bold textright"
							message="[accept=Descr.Tipol.Serv.: ${msg_configurazione_descrizione_regex}]"
							cssclass="textareaman" text="${configutentetiposervizio_desTp}"  />
					</s:div>				
				</s:div>
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
				    <s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
				        <s:textbox 
				        bmodify="true" 
				        name="configutentetiposervizio_desCod"
				        label="Tipol. Serv.:" 
				        maxlenght="3"
				        validator="ignore;accept=${configurazione_descrizione256_regex}"
				        cssclasslabel="label85 bold textright"
				        message="[accept=Tipol.Serv.: ${msg_configurazione_descrizione_regex}]"
				        cssclass="textareaman" 
				        text="${configutentetiposervizio_desCod}" />
				    </s:div>
				</s:div>
				<!-- PG22XX09_YL5 FINE -->
			</s:div>
			
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle"/>
					<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle"/>
					
			</s:div>
		</s:form>
			
</s:div>
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">Elenco Utente - Tipologia Servizio</s:div>
</s:div>