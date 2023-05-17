<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="ConfRendUtenteServizoi_var" encodeAttributes="true" />
<%-- PG110260 --%>
<script type="text/javascript">

	function rendicontazioneSedaOnClick() {
	 
			var  invioEmailCB = document.getElementById('invioEmail');
			var  rendicontazioneSedaCB = document.getElementById('rendicontazioneSeda');
			 
			if(rendicontazioneSedaCB.checked){ 
				invioEmailCB.checked = false;
				invioEmailCB.disabled = true;
			}
			else{ 
				invioEmailCB.disabled = false;
			}

			//aggiunta anomalia
			var  invioFtpCB = document.getElementById('invioFtp');
			var  invioFtpHidden = document.getElementById('invioFtpHidden');
			var  rendicontazioneSedaCB = document.getElementById('rendicontazioneSeda');
			 
			if(rendicontazioneSedaCB.checked){ 
				invioFtpCB.checked = true;
				invioFtpCB.disabled = true;
				if(invioFtpHidden!=null) invioFtpHidden.name = 'invioFtp'
			}
			else{ 
				invioFtpCB.disabled = false;
				if(invioFtpHidden!=null) invioFtpHidden.name = 'invioFtpHidden'
			}  
			//fine aggiunta anomalia
	}
 
</script>
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_form" action="${do_command_name}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">CONFIGURAZIONE RENDICONTAZIONE UTENTE - TIPOLOGIA SERVIZIO</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">CONFIGURAZIONE RENDICONTAZIONE UTENTE - TIPOLOGIA SERVIZIO</s:div>
			</c:if>
						
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="top" >
						<s:dropdownlist name="ddlSocietaUtenteServizio"  
							label="Societ&agrave;/Utente/Servizio: "
									cssclasslabel="label160 textright bold floatleft"
									cssclass="seda-ui-ddl tbddlMax780 floatleft" disable="${codop == 'edit'}"
									   cachedrowset="listaSocietaUtenteServizio" usexml="true" 
									   valueselected="${ddlSocietaUtenteServizio}"
									   validator="required" showrequired="true">
   							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
							<s:ddloption value="{1}|{2}|{3}" text="{14} / {15} / {16}"/>
						</s:dropdownlist>						
					</s:div>
				</s:div>

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
				<!-- REE_FREEMAIL FLAG ABILITAZIONE INVIO EMAIL -->
				
					<%--  Modifica PG110260 aggiunta clausola disabled_invioEmail --%>
					<s:div name="divElement50" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_invioEmail}" validator="ignore" 
						 cssclasslabel="bold checklabel label85" cssclass="checkleft"
						name="invioEmail" groupname="invioEmail" 
						text="Invio Email" value="Y" disable="${disabled_invioEmail}" />
					</s:div>

				<!-- REE_FREEAFTP FLAG ABILITAZIONE INVIO FTP -->
				
					<s:div name="divElement51" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_invioFtp}" validator="ignore" 
						 cssclasslabel="bold label85 checklabel" cssclass="checkleft"
						name="invioFtp" groupname="invioFtp" 
						text="Invio Ftp" value="Y" disable="${disabled_invioFtp}" />
					</s:div>
					

					<!-- aggiunta anomalia PG110260-->
					<c:if test="${disabled_invioFtp}">
						<input type="hidden" id="invioFtpHidden" name="invioFtp" value="Y" /> 
					</c:if>
					<c:if test="${not disabled_invioFtp}">
						<input type="hidden" id="invioFtpHidden" name="invioFtpHidden" value="Y" /> 
					</c:if>
					<!-- FINE aggiunta anomalia PG110260-->
					
					
					<!-- aggiunta anomalia PG110260-->
					<c:if test="${disabled_invioFtp}">
						<input type="hidden" id="invioFtpHidden" name="invioFtp" value="Y" /> 
					</c:if>
					<c:if test="${not disabled_invioFtp}">
						<input type="hidden" id="invioFtpHidden" name="invioFtpHidden" value="Y" /> 
					</c:if>
					<!-- FINE aggiunta anomalia PG110260-->
					
					
					
					 
					<%-- aggiunta PG110260 --%>
					<!-- RES_FRESTREN FLAG TIPOLOGIA RENDICONTAZIONE (Y = RENDICONTAZIONE SEDA; N = RENDICONTAZIONE STANDARD -->
					<s:div name="divElement52" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_rendicontazioneSeda}" validator="ignore" 
						 cssclasslabel="bold checklabel label200" cssclass="checkleft"
						name="rendicontazioneSeda" groupname="rendicontazioneSeda" 
						text="Rendicontazione Gestionale Entrate" value="Y" onclick="rendicontazioneSedaOnClick();"  />
						
						<noscript>
							<s:button id="tx_button_aggiorna" onclick="" text="" type="submit"
								disable="false" validate="false" 
								cssclass="btnAggiornaImgStyle" title="Aggiorna le impostazioni" /> 
						</noscript>
					</s:div>
					<%-- Fine aggiunta PG110260 --%>
					
										
					<!-- REE_NREEMAXB DIMENSIONE MASSIMA ALLEGATO  IN KB -->
					<s:div name="divElement15" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="emailAttachMaxSizeKb"
							label="MaxSize Allegato(KB):" maxlenght="5" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;digits;minlength=1;maxlength=5"
							text="${emailAttachMaxSizeKb}" />
					</s:div>
					
					
					
					<!-- RES_CRESFFRE Formato file rendicontazione -->
					<s:div name="divElement16" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist label="Formato file:" 
								cssclasslabel="label85 bold textright" cssclass="textareaman" 
								name="formatoFileRend" disable="false"  
								validator="ignore" showrequired="true"
						    	valueselected="${formatoFileRend}">
						    	<s:ddloption text="Selezionare uno degli elementi" value="" />
								<s:ddloption value="TXT" text="TXT"/>
								<s:ddloption value="XML" text="XML"/>
						</s:dropdownlist>
					</s:div>
					
					
					
					
					
				</s:div>
				
				


				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
				<!-- REE_EREEEDES EMAIL DESTINATARIO -->
					<s:div name="divElement17" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="emailDestinatario"
							label="Email Dest.:" maxlenght="256" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;maxlength=256;accept=${configurazione_emailListBySemicolon}"
							message="[accept=Email Dest.: ${msg_configurazione_lista_email}]"
							text="${emailDestinatario}" />
					</s:div>
					
				<!-- REE_EREEECCN EMAIL CCN -->
					<s:div name="divElement18" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="emailCCN"
							label="Email CCN:" maxlenght="100" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;maxlength=100;accept=${configurazione_emailListBySemicolon}"
							message="[accept=Email CCN: ${msg_configurazione_lista_email}]"
							text="${emailCCN}" />
					</s:div>
					
				<!-- REE_EREEEMIT EMAIL MITTENTE -->
					<s:div name="divElement19" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="emailMittente"
							label="Email Mitt.:" maxlenght="100" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;maxlength=100;accept=${configurazione_email_regex}"
							message="[email=Email Mitt: ${msg_configurazione_email}]"
							text="${emailMittente}" />
					</s:div>
					
				<!-- REE_DREEDMIT DESCRIZIONE MITTENTE -->
					<s:div name="divElement20" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="descrizioneMittente"
							label="Descr. Mitt.:" maxlenght="256" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione_regex100}"
							message="[accept=Descr. Mitt.: ${msg_configurazione_descrizione_regex}]"
							text="${descrizioneMittente}" />
					</s:div>
					
				
				</s:div>

				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">

				<!--  REE_DREESFTP SERVER FTP -->
					<s:div name="divElement16" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="serverFtp"
							label="Server&nbsp;Ftp:" maxlenght="50" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=^[a-zA-Z0-9\._-]{1,50}$|^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
							message="[accept=Server Ftp: ${msg_configurazione_ftp}]"
							text="${serverFtp}" />
					</s:div>
					
				<!-- REE_DREEUFTP UTENTE FTP -->
					<s:div name="divElement161" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="utenteFtp"
							label="Utente&nbsp;Ftp:" maxlenght="50" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=^[\w\_\.\-\/\\\]{1,50}$"
							message="[accept=Utente Ftp: ${msg_configurazione_descrizione_1}]"
							text="${utenteFtp}" />
					</s:div>
					
				<!-- REE_DREEPFTP PASSWORD FTP -->
					<s:div name="divElement162" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" bpassword="true"  name="passwordFtp"
							label="Password&nbsp;Ftp:" maxlenght="20" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;minlength=3;maxlength=20"
							text="${passwordFtp}" />
					</s:div>
					
				<!-- REE_DREERDIR DIRECTORY REMOTA SERVER FTP -->
					<s:div name="divElement163" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="directoryFtp"
							label="Directory&nbsp;Ftp:" maxlenght="50" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$"
							message="[accept=Directory Ftp: ${msg_configurazione_directory_ftp}]"
							text="${directoryFtp}" />
					</s:div>
					
				</s:div>
			</s:div>
			<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_indietro" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
				<c:if test="${codop == 'add'}">
					<s:button id="tx_button_aggiungi" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${codop == 'edit'}">
					<s:button id="tx_button_edit" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
			</s:div>
			<input type="hidden" name="codop" value="${codop}" />
			<input type="hidden" name="tx_societa" value="${tx_societa}" />
			<input type="hidden" name="tx_utente" value="${tx_utente}" />
			<input type="hidden" name="tx_codiceTipologiaServizio" value="${tx_codiceTipologiaServizio}" />
	</s:form>
	</s:div>
	
</s:div>
