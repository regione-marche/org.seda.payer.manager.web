<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="Gatewaypagamento_var" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_form" action="${do_command_name}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle ">GATEWAY PAGAMENTO</s:div>
								<input type="hidden" name="prova" value=0 />
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle ">GATEWAY PAGAMENTO</s:div>
				<input type="hidden" name="prova" value=1 />
			</c:if>
						
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTopDouble">
					<s:div name="divElementTop1" cssclass="divRicMetadatiSingleRow">	
						<s:dropdownlist name="gatewaypagamento_strUsers"
								label="Soc/Ute:" disable="${codop == 'edit'}" 
								cssclasslabel="label85 bold textright"
								cssclass="floatleft" cachedrowset="users" usexml="true" 
								validator="required" showrequired="true"
								valueselected="${requestScope.gatewaypagamento_strUsers}">
								<s:ddloption text="Selezionare uno degli elementi" value="" />
								<s:ddloption value="{1}|{2}" text="{7} / {4}"/>
						</s:dropdownlist>
					</s:div>
					<s:div name="divElementTop2" cssclass="divRicMetadatiSingleRow">
						<s:div name="divRicercaRight" cssclass="divRicMetadatiLeft">	
					 		<s:dropdownlist name="gatewaypagamento_chiaveCanalePagamento" 
					 			label="Canale Pag.:" disable="${codop == 'edit'}" 
					 					cssclasslabel="label85 bold textright"
										cssclass="textareaman" 
										cachedrowset="canalepagamentos" usexml="true"
										validator="required" showrequired="true"
										valueselected="${requestScope.gatewaypagamento_chiaveCanalePagamento}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption value="{1}" text="{2}"/>
							</s:dropdownlist>
						</s:div>
						<s:div name="divRicercaRight1" cssclass="divRicMetadatiCenter">	
							<s:dropdownlist name="gatewaypagamento_codiceCartaPagamento"
								label="Carta Pag.:"  disable="${codop == 'edit'}" 
										cssclasslabel="label85 bold textright"
										cssclass="textareaman" cachedrowset="cartapagamentos"
										usexml="true" 
										validator="required" showrequired="true"
										valueselected="${requestScope.gatewaypagamento_codiceCartaPagamento}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption value="{1}" text="{2}"/>
							</s:dropdownlist>		
						</s:div>
<%--
						<s:div name="divRicercaRightChiave" cssclass="divRicMetadatiRight">
							<c:if test="${codop == 'edit'}">
								<s:textbox bmodify="false" 
									  label="Chiave Gtw.:" name="txtChiaveGateway" 
									  text="${gatewaypagamento_chiaveGateway}" cssclasslabel="label85 bold textright"
									  cssclass="textareaman colordisabled"/>	
							</c:if>
						</s:div>
--%>
					</s:div>		
				</s:div>

		        <s:div name="divRicercaMetadatiLeft" cssclass="divRicMetadatiLeft">
			        <s:div name="divElement1" cssclass="divRicMetadatiSingleRow">	
						<s:textbox bmodify="true" 
									validator="ignore;accept=${configurazione_descrizione256_regex}"
									  label="Desc.Gateway" name="gatewaypagamento_descrizioneGateway" 
									  text="${gatewaypagamento_descrizioneGateway}" cssclasslabel="label85 bold textright"
									  message="[accept=Desc.Gateway: ${msg_configurazione_descrizione_regex}]"
									  cssclass="textareaman" maxlenght="256"/>	
					</s:div>
					<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" validator="ignore;url;minlength=1;maxlength=256"  
								label="Url Quietanza" name="gatewaypagamento_urlSitoWebGateway" text="${requestScope.gatewaypagamento_urlSitoWebGateway}" cssclasslabel="label85 bold textright"
								  cssclass="textareaman" maxlenght="256"/>
					</s:div>
					<s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist label="Tipo Gateway" name="gatewaypagamento_tipoGateway" disable="false" cssclasslabel="label85 bold textright"
									  cssclass="textareaman"
									  valueselected="${requestScope.gatewaypagamento_tipoGateway}">
						<s:ddloption value="P" text="PayPal"/>
						<s:ddloption value="O" text="Unicredit - PagOnline"/>
						<s:ddloption value="I" text="InfoGroup - Carta di credito"/>
						<s:ddloption value="C" text="InfoGroup - PagoInConto"/>
						<s:ddloption value="R" text="InfoGroup - RID On-Line"/>
						<s:ddloption value="M" text="InfoGroup - MAV On-Line"/>
						<s:ddloption value="S" text="InfoGroup - POS fisico"/>
					</s:dropdownlist>
					</s:div>
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" validator="ignore;minlength=1;maxlength=256;accept=${configurazione_emailListBySemicolon}"  
					label="Email Notifica" name="gatewaypagamento_emailNotificaAdmin" text="${requestScope.gatewaypagamento_emailNotificaAdmin}" cssclasslabel="label85 bold textright"
					message="[accept=Email Notifica: ${msg_configurazione_lista_email}]"
					cssclass="textareaman" maxlenght="256"/>
					</s:div>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" validator="ignore;url;minlength=1;maxlength=128"  
					label="UrlApiEndpoint" name="gatewaypagamento_urlApiEndpoint" text="${requestScope.gatewaypagamento_urlApiEndpoint}" cssclasslabel="label85 bold textright"
								  cssclass="textareaman" maxlenght="128"/>
					</s:div>
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" validator="ignore;accept=^[\w\_\.\-]{1,50}$"  
						label="Api User" name="gatewaypagamento_apiUser" text="${requestScope.gatewaypagamento_apiUser}" cssclasslabel="label85 bold textright"
						message="[accept=Api User: ${msg_configurazione_descrizione_1}]"
						cssclass="textareaman" maxlenght="50"/>
					</s:div>	
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" bpassword="true"  validator="ignore;minlength=1;maxlength=30;"  
						label="Api Password" name="gatewaypagamento_apiPassword" text="${requestScope.gatewaypagamento_apiPassword}" cssclasslabel="label85 bold textright"
								  cssclass="textareaman" maxlenght="30"/>
					</s:div>
					<%--<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" validator="ignore;accept=^[\w\-\=\!\.]{1,128}$"  
					label="Api Signature" name="gatewaypagamento_apiSignature" text="${requestScope.gatewaypagamento_apiSignature}" cssclasslabel="label85 bold textright"
					message="[accept=Api Signature: ${msg_configurazione_alfanumerici_signature_API}]"
					cssclass="textareaman" maxlenght="128"/>
					</s:div>
					--%>
					
					
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" 
						label="Api Signature" name="gatewaypagamento_apiSignature" text="${requestScope.gatewaypagamento_apiSignature}" cssclasslabel="label85 bold textright"
							 cssclass="textareaman" maxlenght="128"/>
				 	</s:div>
					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" validator="ignore;url;minlength=1;maxlength=256"  
					label="Url Api Image" name="gatewaypagamento_urlApiImage" text="${requestScope.gatewaypagamento_urlApiImage}" cssclasslabel="label85 bold textright"
								  cssclass="textareaman" maxlenght="256"/>
					</s:div>
					<s:div name="divElement9_1" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist label="Img Logo" name="gatewaypagamento_pathImgLogo" disable="false" cssclasslabel="label85 bold textright"
							  cssclass="textareaman"
							  valueselected="${requestScope.gatewaypagamento_pathImgLogo}">
							  	<s:ddloption value="" text=""/>
								<s:ddloption value="PayPal_eu.png" text="Logo PayPal Europeo"/>
								<s:ddloption value="PayPal_in.png" text="Logo PayPal Internazionale"/>
								<s:ddloption value="PagOnline.png" text="Logo Unicredit - PagOnline"/>
								<s:ddloption value="Infogroup.png" text="Logo InfoGroup - Carta di credito"/>
								<s:ddloption value="PagoInConto.png" text="Logo InfoGroup - PagoInConto"/>
								<s:ddloption value="RIDOnline.png" text="Logo InfoGroup - RID On-Line"/>
								<s:ddloption value="MAVOnline.png" text="Logo InfoGroup - MAV On-Line"/>
								<s:ddloption value="POS.png" text="Logo InfoGroup - POS fisico"/>
						</s:dropdownlist>
					</s:div>
					<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" validator="ignore;accept=^[\w\_\.\-]{1,10}$"  
						label="Api Version" name="gatewaypagamento_apiVersion" text="${requestScope.gatewaypagamento_apiVersion}" cssclasslabel="label85 bold textright"
						message="[accept=Api Version: ${msg_configurazione_descrizione_1}]"
								  cssclass="textareaman" maxlenght="10"/>
					</s:div>				
				</s:div>
			
				<s:div name="divRicercaRight2" cssclass="divRicMetadatiCenter">
				
				<s:div name="divElement11_0" cssclass="divRicMetadatiSingleRow">	
						<s:textbox bmodify="true" validator="ignore;url;minlength=1;maxlength=128"  
						label="Url Api Cancel" name="gatewaypagamento_urlApiCancel" text="${requestScope.gatewaypagamento_urlApiCancel}" cssclasslabel="label85 bold textright"
								  cssclass="textareaman"/>
					</s:div>
					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">	
						<s:textbox bmodify="true" validator="ignore;url;minlength=1;maxlength=128" 
						label="Url Api Red." name="gatewaypagamento_urlApiRedirect" text="${requestScope.gatewaypagamento_urlApiRedirect}" cssclasslabel="label85 bold textright"
								  cssclass="textareaman"/>
					</s:div>
					<s:div name="divElement12" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" validator="ignore;minlength=1;maxlength=20;accept=^\w{1,20}$" 
						label="Cod.Negozio" name="gatewaypagamento_codiceNegozio" text="${requestScope.gatewaypagamento_codiceNegozio}" cssclasslabel="label85 bold textright"
						message="[accept=Cod.Negozio: ${msg_configurazione_alfanumerici}]"
								  cssclass="textareaman" maxlenght="20"/>
					</s:div>
					<s:div name="divElement13" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" validator="ignore;minlength=1;maxlength=50;accept=^\w{1,50}$" 
						label="Cod.MacAvvio" name="gatewaypagamento_codiceMacAvvio" text="${requestScope.gatewaypagamento_codiceMacAvvio}" cssclasslabel="label85 bold textright"
						message="[accept=Cod.MacAvvio: ${msg_configurazione_alfanumerici}]"
								  cssclass="textareaman" maxlenght="50"/>
					</s:div>
					<s:div name="divElement14" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" validator="ignore;minlength=1;maxlength=50;accept=^\w{1,50}$" 
						label="Cod.MacEsito" name="gatewaypagamento_codiceMacEsito" text="${requestScope.gatewaypagamento_codiceMacEsito}" cssclasslabel="label85 bold textright"
						message="[accept=Cod.MacEsito: ${msg_configurazione_alfanumerici}]"
								  cssclass="textareaman" maxlenght="50"/>
					</s:div>
					<s:div name="divElement15" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist label="Tipo Autorizz." name="gatewaypagamento_tipoAutorizzazione" disable="false" cssclasslabel="label85 bold textright"
								  		cssclass="textareaman" 
									    valueselected="${requestScope.gatewaypagamento_tipoAutorizzazione}">
						<s:ddloption value="I" text="Immediata"/>
						<s:ddloption value="D" text="Differita"/>
					</s:dropdownlist>
					</s:div>
					<s:div name="divElement16" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist label="Tipo Contab." name="gatewaypagamento_tipoContabilizzazione" disable="false" cssclasslabel="label85 bold textright"
								  		cssclass="textareaman" 
									    valueselected="${requestScope.gatewaypagamento_tipoContabilizzazione}">
						<s:ddloption value="I" text="Immediata"/>
						<s:ddloption value="D" text="Differita"/>
					</s:dropdownlist>
					</s:div>	
					<s:div name="divElement17" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist label="Gateway Zone" name="gatewaypagamento_opzioniAggiuntive" disable="false" cssclasslabel="label85 bold textright"
								  		cssclass="textareaman" 
									    valueselected="${requestScope.gatewaypagamento_opzioniAggiuntive}">
						<s:ddloption value="E" text="Europea"/>
						<s:ddloption value="I" text="Internazionale"/>
					</s:dropdownlist>
					</s:div>
				</s:div>
	
				<s:div name="divRicercaRight3" cssclass="divRicMetadatiRight">	
					<s:div name="divElement23" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist label="Attivazione" name="gatewaypagamento_flagAttivazione" disable="false" cssclasslabel="label85 bold textright"
								  		cssclass="textareaman"  
									    valueselected="${requestScope.gatewaypagamento_flagAttivazione}">
						<s:ddloption value="Y" text="SI"/>
						<s:ddloption value="N" text="NO"/>
					</s:dropdownlist>
					</s:div>
					<s:div name="divElement24" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" validator="ignore;minlength=5;accept=^[0-9A-Z]{5}$;maxlength=5" 
					label="Cod. SIA Dest." name="gatewaypagamento_codiceSIAAziendaDestinataria" text="${requestScope.gatewaypagamento_codiceSIAAziendaDestinataria}" cssclasslabel="label85 bold textright"
					message="[accept=Cod. SIA Dest: ${msg_configurazione_alfanumerici_maiuscolo}]"
								  cssclass="textareaman" maxlenght="5"/>
					</s:div>
					<s:div name="divElement25" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true"  validator="ignore;accept=^[A-Z]{1}$" 
					label="Cod. CIN Mitt." name="gatewaypagamento_codiceCINBancaMittente" text="${requestScope.gatewaypagamento_codiceCINBancaMittente}" cssclasslabel="label85 bold textright"
					message="[accept=Cod. CIN Mitt.: ${msg_configurazione_testo_maiuscolo}]"
								  cssclass="textareaman" maxlenght="1"/>
					</s:div>
					<s:div name="divElement26" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" validator="ignore;digits;minlength=5;maxlength=5" 
					label="Cod. ABI Mitt." name="gatewaypagamento_codiceABIBancaMittente" text="${requestScope.gatewaypagamento_codiceABIBancaMittente}" cssclasslabel="label85 bold textright"
								  cssclass="textareaman" maxlenght="5" />
					</s:div>	
					<s:div name="divElement27" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" validator="ignore;digits;minlength=5;maxlength=5" 
					label="Cod. CAB Mitt." name="gatewaypagamento_codiceCABBancaMittente" text="${requestScope.gatewaypagamento_codiceCABBancaMittente}" cssclasslabel="label85 bold textright"
								  cssclass="textareaman" maxlenght="5" />
					</s:div>
					<s:div name="divElement28" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" validator="ignore;minlength=12;accept=^[0-9A-Z]{12}$;maxlength=12" 
					label="Cod. C.C." name="gatewaypagamento_codiceContoCorrente" text="${requestScope.gatewaypagamento_codiceContoCorrente}" cssclasslabel="label85 bold textright"
					message="[accept=Cod. C.C.: ${msg_configurazione_alfanumerici_maiuscolo}]"
								  cssclass="textareaman" maxlenght="12"/>
					</s:div>
					<s:div name="divElement29" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" validator="ignore;digits;maxlength=11" 
					label="GG Data Cont." name="gatewaypagamento_deltaGiorniDataContabile" text="${requestScope.gatewaypagamento_deltaGiorniDataContabile}" cssclasslabel="label85 bold textright"
								  cssclass="textareaman" maxlenght="11"/>
					</s:div>
					<s:div name="divElement30" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" label="Importo Scost." name="gatewaypagamento_importoScostamento" text="${requestScope.gatewaypagamento_importoScostamento}" cssclasslabel="label85 bold textright"
								validator="ignore;numberIT;maxlength=15"
								message="[accept=Importo scostamento: ${msg_configurazione_importo_4_2}]"
								cssclass="textareaman" maxlenght="15"/>
					</s:div>
					<s:div name="divElement31" cssclass="divRicMetadatiSingleRow">				
					<input type="hidden" name="gatewaypagamento_urlRedirectPayerPerQuietanza" value="${requestScope.gatewaypagamento_urlRedirectPayerPerQuietanza}"/>
					</s:div>		
							
				</s:div>
			</s:div>
			<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_indietro" type="submit" text="Indietro"	onclick="" cssclass="btnStyle" validate="false" />
				<s:button id="tx_button_reset" onclick="" text="Reset" type="submit" cssclass="btnStyle" validate="false"/>
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
			<input type="hidden" name="tx_carta" value="${tx_carta}" />
			<input type="hidden" name="tx_canale" value="${tx_canale}" />
			<input type="hidden" name="gatewaypagamento_chiaveGateway" value="<c:out value="${gatewaypagamento_chiaveGateway}"/>"/>	
	</s:form>
	</s:div>
	
</s:div>
