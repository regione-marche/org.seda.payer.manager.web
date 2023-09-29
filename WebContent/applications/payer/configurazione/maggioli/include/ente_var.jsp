<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>
<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />

<m:view_state id="entes" encodeAttributes="true" />

<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<c:choose>

		<c:when test="${done == null && richiestacanc == null}">

			<s:div name="divRicercaTopName" cssclass="divRicercaTop">

				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
					hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
					method="post" action="ente.do">
					
					<c:choose>
						<c:when test="${requestScope.action=='saveedit'}">
							<input type="hidden" name="action" value="<c:out value="${requestScope.action}"/>" />
						</c:when>
						<c:when test="${requestScope.action=='saveadd'}">
							<input type="hidden" name="action" value="<c:out value="${requestScope.action}"/>" />
						</c:when>	
						<c:otherwise>
							<input type="hidden" name="action" value="save<c:out value="${requestScope.action}"/>" />
						</c:otherwise>
					</c:choose>
					
					
					
					<c:choose>
					<c:when test="${action == 'edit'|| action == 'saveedit'}">
					<input type="hidden" name="codop" value="${typeRequest.editScope}" />
					<input type="hidden" name="scope"
									value=1 />
					<s:div name="divRicMetadati" cssclass="divRicMetadati">


						<s:div name="divRicercaTitleName" cssclass="divRicTitle">Ente - Consorzio</s:div>

						<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
							<s:div name="strUsers_var" cssclass="divRicMetadatiLeft2">
								<s:dropdownlist label="Soc./Utente:" name="ddlSocUte" disable="true" 
										cssclasslabel="label85 bold textright" cssclass="textareaman2 floatleft"
										cachedrowset="entes_add" usexml="true" 
										valueselected="${ente_userCode}|${ente_companyCode}"
										validator="required" showrequired="true">
									<s:ddloption text="Selezionare uno degli elementi" value="" />	
									<s:ddloption value="{1}|{2}" text="{4} / {3}"/> 	
								</s:dropdownlist>
							</s:div>
							<s:div name="strEnti_var" cssclass="divRicMetadatiLeft2">
								<s:textbox bmodify="false" name="txtEnte" label="Ente:" text="${ente_descrizioneEnte}"
										cssclass="textareaman2 colordisabled" cssclasslabel="label85 bold textright"
										validator="required" showrequired="true"/>
							</s:div>

						</s:div>
							
						<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
							<s:div name="divElementLabel" cssclass="divRicMetadatiSingleRow">
								<center>
								<s:label name="prova" text="AREA FLUSSI CBI"  cssclass="lblTitlePadding bold "/>
								</center>
							</s:div>		
							<s:div name="divElement40" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;accept=^[a-zA-Z0-9\._]{1,50}$|^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$" 
									label="Sito Ftp:" maxlenght="50"
									name="ente_siteFtpFlussiCbi" text="${requestScope.ente_siteFtpFlussiCbi}"
									message="[accept=Cbi Sito Ftp: ${msg_configurazione_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
	
							</s:div>
							
							<s:div name="divElement40a" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$" 
									label="Dir. Ftp:" maxlenght="50"
									name="ente_directoryFtpFlussiCbi" text="${requestScope.ente_directoryFtpFlussiCbi}"
									message="[accept=Cbi Dir. Ftp: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
	
							</s:div>
								
							<s:div name="divElement41" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;accept=^\w{1,50}$" 
									label="User:" maxlenght="50"
									name="ente_userSiteFtpFlussiCbi" text="${requestScope.ente_userSiteFtpFlussiCbi}"
									message="[accept=Cbi User: ${msg_configurazione_alfanumerici}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
							</s:div>

							<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;minlength=3;maxlength=50" label="Pwd:"
									maxlenght="50"
									name="ente_pswSiteFtpFlussiCbi" text="${requestScope.ente_pswSiteFtpFlussiCbi}"
									bpassword="true"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
						
							</s:div>
							<s:div name="divElementLabel1" cssclass="divTitle">
								<center>
								<s:label name="prova" text="Input Esiti"  cssclass="lblTitlePadding bold"/>
								</center>
							</s:div>
							<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
								 	<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$"
									maxlenght="50"
									label="Cbi Path:" name="ente_dirFlussiCbiInput"
									text="${ente_dirFlussiCbiInput}"
									message="[accept=Path: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								</s:div>
								
								<s:div name="divElement44" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$"
									maxlenght="50"
									label="Cbi Path Save:" name="ente_dirSaveFlussiCbiInput"
									message="[accept=Path Save: ${msg_configurazione_directory_ftp}]"
									text="${ente_dirSaveFlussiCbiInput}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
										
								</s:div>
							<s:div name="divElementLabel2" cssclass="divTitle">
								<center>
								<s:label name="prova" text="Output Bonifici"  cssclass="lblTitlePadding bold"/>
								</center>
							</s:div>
								<s:div name="divElement45" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$"
									maxlenght="50"
									label="Path Rimb.:" name="ente_dirFlussiRimbEccInput"
									text="${ente_dirFlussiRimbEccInput}"
									message="[accept=Cbi Path Rimb.: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
							</s:div>
							
							<s:div name="divElement46" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$"
									maxlenght="50"
									label="Path Save:" name="ente_dirSaveFlussiRimbEccInput"
									text="${ente_dirSaveFlussiRimbEccInput}"
									message="[accept=Cbi Path Save: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
							</s:div>
									
						</s:div>
							
						
						<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
							<s:div name="divElementLabel3" cssclass="divRicMetadatiSingleRow">
								<center>
								<s:label name="prova" text="AREA FLUSSI ECCEDENZE"  cssclass="lblTitlePadding bold"/>
								</center>
							</s:div>	
							<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$"
									maxlenght="50"
									label="Sito Ftp:" name="ente_siteFtpFlussiRimbEcc"
									text="${ente_siteFtpFlussiRimbEcc}"
									message="[accept=Ecc. Sito Ftp: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
							</s:div>
							
							<s:div name="divElement9a" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$"
									maxlenght="50"
									label="Dir. Ftp:" name="ente_directoryFtpFlussiRimbEcc"
									text="${ente_directoryFtpFlussiRimbEcc}"
									message="[accept=Ecc. Dir. Ftp: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
							</s:div>
							
							<s:div name="divElement94" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;accept=^\w{1,50}$"
									maxlenght="50"
									label="User:" name="ente_userSiteFtpFlussiRimbEcc"
									text="${ente_userSiteFtpFlussiRimbEcc}"
									message="[accept=Ecc. User: ${msg_configurazione_alfanumerici}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />	
							</s:div>
							
							<s:div name="divElement92" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;minlength=3;maxlength=50"
									maxlenght="50"
									label="Pwd:" name="ente_pswSiteFtpFlussiRimbEcc"
									bpassword="true"
									text="${ente_pswSiteFtpFlussiRimbEcc}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							<s:div name="divElementLabel4" cssclass="divTitle">
								<center>
								<s:label name="prova" text="Input Eccedenze"  cssclass="lblTitlePadding bold"/>
								</center>
							</s:div>
							<s:div name="divElement93" cssclass="divRicMetadatiSingleRow">

								<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$"
									maxlenght="50"
									label="Path:" name="ente_dirFlussiEccInput"
									text="${ente_dirFlussiEccInput}"
									message="[accept=Ecc. Path: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />

							</s:div>
							
							<s:div name="divElement98" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$"
									maxlenght="50"
									label="Path Save:" name="ente_dirSaveFlussiEccInput"
									text="${ente_dirSaveFlussiEccInput}"
									message="[accept=Ecc. Path Save: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />

							</s:div>
							<s:div name="divElementLabel5" cssclass="divTitle">
								<center>
								<s:label name="prova" text="Output Rimborsi"  cssclass="lblTitlePadding bold"/>
								</center>
							</s:div>
							<s:div name="divElement90" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$" label="Path Rimb.:"
									maxlenght="50"
									name="ente_dirFlussiRimbOutput" text="${requestScope.ente_dirFlussiRimbOutput}"
									message="[accept=Ecc. Path Rimb.: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
									
								
								
							</s:div>
						<s:div name="divElement97" cssclass="divRicMetadatiSingleRow">	
					
							<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$" label="Path Save:"
									maxlenght="50"
									name="ente_dirSaveFlussiRimbOutput" text="${requestScope.ente_dirSaveFlussiRimbOutput}"
									message="[accept=Ecc. Path Save: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />

							</s:div>
						</s:div>

						
						<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
							
							<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
								
							</s:div>
							
							<s:div name="divElement710" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;minlength=5;accept=^[0-9A-Z]{5}$;maxlength=5"
									maxlenght="5"
									label="Cod Sia:" name="ente_codiceSia"
									text="${ente_codiceSia}"
									message="[accept=Cod Sia: ${msg_configurazione_alfanumerici_maiuscolo}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							
							<s:div name="divElement711" cssclass="divRicMetadatiSingleRow">
								
								
								<s:textbox bmodify="true"
									validator="required;accept=^[0-9]{11}|[a-zA-Z]{6}\d\d[a-zA-Z]\d\d[a-zA-Z]\d\d\d[a-zA-Z]$;maxlength=16"
									label="CF/PIva:" name="ente_cFiscalePIva"
									maxlenght="16" showrequired="true"
									text="${requestScope.ente_cFiscalePIva}"
									cssclasslabel="label85 bold floatleft textright"
									message="[accept=CF/PIva: ${msg_configurazione_codicefiscale_piva}]"
									cssclass="textareaman" />
								
								
								
							</s:div>
							
						<!--inizio CD PAGO 580 -->
							
							<s:div name="divElement711_b" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;maxlength=35"
									label="CodiceIPA:" name="ente_codIpaEnte"
									maxlenght="50" showrequired="true"
									text="${requestScope.ente_codIpaEnte}"
									cssclasslabel="label85 bold floatleft textright"
									cssclass="textareaman" />
								</s:div>
								
							<!--fine CD PAGO 580 -->
							
							<s:div name="divElement712" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;accept=${configurazione_email_regex};maxlength=50"
									maxlenght="50"
									label="Email Admin:" name="ente_emailAdmin"
									text="${ente_emailAdmin}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
								
								
							</s:div>
							
							<s:div name="divElement713" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Invio SMS:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="ente_flagAutInvSMS" disable="false"  
										validator="required" showrequired="true"
								    	valueselected="${requestScope.ente_flagAutInvSMS}">
								    	<s:ddloption text="Selezionare uno degli elementi" value="" />
										<s:ddloption value="Y" text="Si"/>
										<s:ddloption value="N" text="No"/>
										</s:dropdownlist>
							</s:div>
							<s:div name="divElement714" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Tipo Ente:" name="ente_tipoEnte" disable="false"
								cssclasslabel="label85 bold textright"
								validator="required" showrequired="true"
								cssclass="textareaman" valueselected="${ente_tipoEnte}">
								<s:ddloption text="Selezionare uno degli elementi" value="" />
								<s:ddloption value="C" text="Consorzio" />
								<s:ddloption value="E" text="Ente" />
								</s:dropdownlist>
							</s:div>
<!-- dom -->
<!-- inizio LP PAGONET-339 -->
<!--
							<s:div name="divElement715" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Ente Benef.:" name="ente_flagBeneficiario" disable="false"
								cssclasslabel="label85 bold textright"
								validator="required" showrequired="true"
								cssclass="textareaman" valueselected="${ente_flagBeneficiario}">
								<s:ddloption text="Selezionare uno degli elementi" value="" />								
								<s:ddloption value="N" text="No" />
								<s:ddloption value="Y" text="Si" />
								</s:dropdownlist>
							</s:div>
-->
							<input type="hidden" id="ente_flagBeneficiario" name="ente_flagBeneficiario" value="N" />
<!-- fine LP PAGONET-339 -->
<!-- fine dom -->
<!-- gab -->
							<s:div name="divElement716" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Modello 21:" name="ente_flagModello" disable="false"
								cssclasslabel="label85 bold textright"
								validator="required" showrequired="true"
								cssclass="textareaman" valueselected="${ente_flagModello}">
								<s:ddloption text="Selezionare uno degli elementi" value="" />								
								<s:ddloption value="N" text="No" />
								<s:ddloption value="Y" text="Si" />
								</s:dropdownlist>
							</s:div>
							
							<s:div name="divElement717" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;accept=${configurazione_emailListBySemicolon};maxlength=256"
									message="[accept=Email Mod.21: ${msg_configurazione_lista_email}]"
									maxlenght="256"
									label="Email Mod.21:" name="ente_emailModello"
									text="${ente_emailModello}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							
							<s:div name="divElement718" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Attiv. Ruoli:" name="ente_flagRuoli" disable="false"
								cssclasslabel="label85 bold textright"
								validator="required" showrequired="true"
								cssclass="textareaman" valueselected="${ente_flagRuoli}">
								<s:ddloption text="Selezionare uno degli elementi" value="" />								
								<s:ddloption value="N" text="No" />
								<s:ddloption value="Y" text="Si" />
								</s:dropdownlist>
							</s:div>
<!-- /gab -->
							
							<input type="hidden" name="codop" value="${typeRequest.editScope}" />
                    <input type="hidden" name="ente_companyCode" value="${ente_companyCode}"/>
					<input type="hidden" name="ente_userCode" value="<c:out value="${ente_userCode}"/>"/>
					<input type="hidden" name="ente_chiaveEnte" value="${ente_chiaveEnte}"/>
					<p><b><c:out value=""/></b></p>
						</s:div>
						
						</s:div>
					
					</c:when>
					
					
					<c:otherwise>
					<%--INSERIMENTO--%>
					<input type="hidden" name="scope" value=0 />
					<s:div name="divRicMetadati" cssclass="divRicMetadati">
					<input type="hidden" name="codop" value="${typeRequest.addScope}" />
					<s:div name="divRicercaTitleName" cssclass="divRicTitle">Ente - Consorzio</s:div>
						
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTopDouble">
						<s:div name="strUsers_var" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist label="Soc./Utente:" name="ente_selected" disable="false" 
									cssclasslabel="label85 bold textright" cssclass="textareaman2 floatleft"
									validator="required" showrequired="true"
									cachedrowset="entes_add" usexml="true" 
									valueselected="${ente_selected}">
								<s:ddloption text="Selezionare uno degli elementi" value="" />
								<s:ddloption value="{1}|{2}" text="{4} / {3}"/> 	
							</s:dropdownlist>
						</s:div>
						
						<s:div name="strEnti_var" cssclass="divRicMetadatiSingleRow">
							<s:div name="divProvincia" cssclass="divRicMetadatiTopLeft">
								<s:dropdownlist name="ente_siglaprovincia"
									disable="false" 
									label="Provincia:"
									cssclasslabel="label85 bold floatleft textright"
									cssclass="tbddl floatleft"
									cachedrowset="listprovince" usexml="true"
									onchange="setFiredButton('tx_button_changed');this.form.submit();"
									valueselected="${ente_siglaprovincia}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption  value="{2}" text="{1}"/>
								</s:dropdownlist>
								<noscript>
									<s:button id="tx_button_changed" onclick="" text="" 
										type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
								</noscript>
							</s:div>
							
							<s:div name="strEnti_var" cssclass="floatleft">
								<s:dropdownlist label="Ente:" name="ente_selected2" disable="false" 
									cssclasslabel="label65 bold textright"
								    validator="required" showrequired="true"
									cssclass="textareaman2" cachedrowset="entes_add2" usexml="true" valueselected="${ente_chiaveEnte}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption value="{1}" text="{6}"/> 	
								</s:dropdownlist>
							</s:div>
								
							
						</s:div>

					</s:div>
					

					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

							<s:div name="divElementLabel6" cssclass="divRicMetadatiSingleRow">
								<center>
									<s:label name="prova" text="AREA FLUSSI CBI"  cssclass="lblTitlePadding bold "/>
								</center>
							</s:div>		
							<s:div name="divElement40" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;accept=^[a-zA-Z0-9\._]{1,50}$|^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$" 
									label="Sito Ftp:" maxlenght="50"
									name="ente_siteFtpFlussiCbi" text="${requestScope.ente_siteFtpFlussiCbi}"
									message="[accept=Cbi Sito Ftp: ${msg_configurazione_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
	
							</s:div>
							
							<s:div name="divElement40a" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;accept=^[a-zA-Z0-9\._]{1,50}$|^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$" 
									label="Dir. Ftp:" maxlenght="50"
									name="ente_directoryFtpFlussiCbi" text="${requestScope.ente_directoryFtpFlussiCbi}"
									message="[accept=Cbi Dir. Ftp: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
	
							</s:div>
								
							<s:div name="divElement41" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;accept=^\w{1,50}$" label="User:"
									maxlenght="50"
									name="ente_userSiteFtpFlussiCbi" text="${requestScope.ente_userSiteFtpFlussiCbi}"
									message="[accept=Cbi User: ${msg_configurazione_alfanumerici}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
							</s:div>

							<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;minlength=6;maxlength=50" label="Pwd:"
									maxlenght="50"
									name="ente_pswSiteFtpFlussiCbi" text="${requestScope.ente_pswSiteFtpFlussiCbi}"
									bpassword="true"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
								
							</s:div>
							<s:div name="divElementLabel7" cssclass="divTitle">
								<center>
									<s:label name="prova" text="Input Esiti"  cssclass="lblTitlePadding bold"/>
								</center>
							</s:div>
							<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">

								 	<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$"
									maxlenght="50"
									label="Path:" name="ente_dirFlussiCbiInput"
									text="${ente_dirFlussiCbiInput}"
									message="[accept=Cbi Path: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
								
							<s:div name="divElement44" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$"
									maxlenght="50"
									label="Path Save:" name="ente_dirSaveFlussiCbiInput"
									text="${ente_dirSaveFlussiCbiInput}"
									message="[accept=Cbi Path Save: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
										
							</s:div>
							<s:div name="divElementLabel8" cssclass="divTitle">
								<center>
								<s:label name="prova" text="Output Bonifici"  cssclass="lblTitlePadding bold"/>
								</center>
							</s:div>
							<s:div name="divElement45" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$"
									maxlenght="50"
									label="Path Rimb.:" name="ente_dirFlussiRimbEccInput"
									text="${ente_dirFlussiRimbEccInput}"
									message="[accept=Cbi Path Rimb.: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
							</s:div>
							
							<s:div name="divElement46" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$"
									maxlenght="50"
									label="Path Save:" name="ente_dirSaveFlussiRimbEccInput"
									text="${ente_dirSaveFlussiRimbEccInput}"
									message="[accept=Cbi Path Save: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
							</s:div>
									
						</s:div>

						<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
							<s:div name="divElementLabel9" cssclass="divRicMetadatiSingleRow">
								<center>
									<s:label name="prova" text="AREA FLUSSI ECCEDENZE"  cssclass="lblTitlePadding bold"/>
								</center>
							</s:div>	
							<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;accept=^[a-zA-Z0-9\._]{1,50}$|^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
									maxlenght="50"
									label="Sito Ftp:" name="ente_siteFtpFlussiRimbEcc"
									text="${ente_siteFtpFlussiRimbEcc}"
									message="[accept=Ecc. Sito Ftp: ${msg_configurazione_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
							</s:div>
							
							<s:div name="divElement9a" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;accept=^[a-zA-Z0-9\._]{1,50}$|^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
									maxlenght="50"
									label="Dir. Ftp:" name="ente_directoryFtpFlussiRimbEcc"
									text="${ente_directoryFtpFlussiRimbEcc}"
									message="[accept=Ecc. Dir. Ftp: ${msg_configurazione_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
							</s:div>
							
							<s:div name="divElement94" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;accept=^\w{1,50}$"
									maxlenght="50"
									label="User:" name="ente_userSiteFtpFlussiRimbEcc"
									text="${ente_userSiteFtpFlussiRimbEcc}"
									message="[accept=Ecc. User: ${msg_configurazione_alfanumerici}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />	
							</s:div>
							
							<s:div name="divElement92" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;minlength=6;maxlength=50"
									maxlenght="50"
									label="Pwd:" name="ente_pswSiteFtpFlussiRimbEcc"
									bpassword="true"
									text="${ente_pswSiteFtpFlussiRimbEcc}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							<s:div name="divElementLabel10" cssclass="divTitle">
								<center>
									<s:label name="prova" text="Input Eccedenze"  cssclass="lblTitlePadding bold"/>
								</center>
							</s:div>
							<s:div name="divElement93" cssclass="divRicMetadatiSingleRow">

								<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$"
									maxlenght="50"
									label="Path:" name="ente_dirFlussiEccInput"
									text="${ente_dirFlussiEccInput}"
									message="[accept=Ecc. Path: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />

							</s:div>
							
							<s:div name="divElement96" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$"
									maxlenght="50"
									label="Path Save:" name="ente_dirSaveFlussiEccInput"
									text="${ente_dirSaveFlussiEccInput}"
									message="[accept=Ecc. Path Save: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />

							</s:div>
							<s:div name="divElementLabel11" cssclass="divTitle">
								<center>
								<s:label name="prova" text="Output Rimborsi"  cssclass="lblTitlePadding bold"/>
								</center>
							</s:div>
							<s:div name="divElement90" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$" label="Path Rimb.:"
									maxlenght="50"
									name="ente_dirFlussiRimbOutput" text="${requestScope.ente_dirFlussiRimbOutput}"
									message="[accept=Ecc. Path Rimb.: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
									
								
								
							</s:div>
							<s:div name="divElement91" cssclass="divRicMetadatiSingleRow">	
					
							<s:textbox bmodify="true"
									validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$" 
									label="Path Save:"
									maxlenght="50"
									name="ente_dirSaveFlussiRimbOutput" text="${requestScope.ente_dirSaveFlussiRimbOutput}"
									message="[accept=Ecc. Path Save: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
									
							
							</s:div>
							
						</s:div>

						<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
							<s:div name="divElementDDL" cssclass="divRicMetadatiSingleRow">
							
							</s:div>
							
							<s:div name="divElement735" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;minlength=5;accept=^[0-9A-Z]{5}$;maxlength=5"
									maxlenght="5" 
									label="Cod Sia:" name="ente_codiceSia"
									text="${ente_codiceSia}"
									message="[accept=Cod Sia: ${msg_configurazione_alfanumerici_maiuscolo}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							
							<s:div name="divElement736" cssclass="divRicMetadatiSingleRow">
								
								
								<s:textbox bmodify="true"
									validator="required;accept=^[0-9]{11}|[a-zA-Z]{6}\d\d[a-zA-Z]\d\d[a-zA-Z]\d\d\d[a-zA-Z]$;minlength=11;maxlength=16"
									label="CF/PIva:" name="ente_cFiscalePIva"
									maxlenght="16" showrequired="true"
									text="${requestScope.ente_cFiscalePIva}"
									cssclasslabel="label85 bold floatleft textright"
									message="[accept=CF/PIva: ${msg_configurazione_codicefiscale_piva}]"
									cssclass="textareaman" />

							</s:div>
							
						    <!--inizio CD PAGO 580 -->
							
							<s:div name="divElement736_b" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;maxlength=35"
									label="CodiceIPA:" name="ente_codIpaEnte"
									maxlenght="50" showrequired="true"
									text="${requestScope.ente_codIpaEnte}"
									cssclasslabel="label85 bold floatleft textright"
									cssclass="textareaman" />
								</s:div>
								
							<!--fine CD PAGO 580 -->
							
							<s:div name="divElement737" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;accept=${configurazione_email_regex};maxlength=50"
									maxlenght="50"
									label="Email Admin:" name="ente_emailAdmin"
									text="${ente_emailAdmin}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
								
								
							</s:div>
							
							<s:div name="divElement738" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Invio SMS:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="ente_flagAutInvSMS" disable="false"  
										validator="required" showrequired="true"
								    	valueselected="${requestScope.ente_flagAutInvSMS}">
								    	<s:ddloption text="Selezionare uno degli elementi" value="" />
										<s:ddloption value="Y" text="Si"/>
										<s:ddloption value="N" text="No"/>
										</s:dropdownlist>
							</s:div>
							<s:div name="divElement739" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Tipo Ente:" name="ente_tipoEnte" disable="false"
								cssclasslabel="label85 bold textright"
								validator="required" showrequired="true"
								cssclass="textareaman" valueselected="${ente_tipoEnte}">
								<s:ddloption text="Selezionare uno degli elementi" value="" />
								<s:ddloption value="C" text="Consorzio" />
								<s:ddloption value="E" text="Ente" />
								</s:dropdownlist>
							</s:div>

<!-- dom -->
<!-- inizio LP PAGONET-339 -->
<!--
							<s:div name="divElement740" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Ente Benef.:" name="ente_flagBeneficiario" disable="false"
								cssclasslabel="label85 bold textright"
								validator="required" showrequired="true"
								cssclass="textareaman" valueselected="${ente_flagBeneficiario}">
								<s:ddloption text="Selezionare uno degli elementi" value="" />								
								<s:ddloption value="N" text="No" />
								<s:ddloption value="Y" text="Si" />
								</s:dropdownlist>
							</s:div>
-->
							<input type="hidden" id="ente_flagBeneficiario" name="ente_flagBeneficiario" value="N" />
<!-- fine LP PAGONET-339 -->
<!-- fine dom -->

							<s:div name="divElement741" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Modello 21:" name="ente_flagModello" disable="false"
								cssclasslabel="label85 bold textright"
								validator="required" showrequired="true"
								cssclass="textareaman" valueselected="${ente_flagModello}">
								<s:ddloption text="Selezionare uno degli elementi" value="" />								
								<s:ddloption value="N" text="No" />
								<s:ddloption value="Y" text="Si" />
								</s:dropdownlist>
							</s:div>
							
							<s:div name="divElement742" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;accept=${configurazione_emailListBySemicolon};maxlength=256"
									message="[accept=Email Mod.21: ${msg_configurazione_lista_email}]"
									maxlenght="256"
									label="Email Mod.21:" name="ente_emailModello"
									text="${ente_emailModello}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
							</s:div>
							
							<s:div name="divElement743" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Attiv. Ruoli:" name="ente_flagRuoli" disable="false"
								cssclasslabel="label85 bold textright"
								validator="required" showrequired="true"
								cssclass="textareaman" valueselected="${ente_flagRuoli}">
								<s:ddloption text="Selezionare uno degli elementi" value="" />								
								<s:ddloption value="N" text="No" />
								<s:ddloption value="Y" text="Si" />
								</s:dropdownlist>
							</s:div>
						</s:div>
					</s:div>
					
					</c:otherwise>
					
					</c:choose>



						



					<s:div name="button_container_var" cssclass="divRicBottoni">
						<s:textbox name="fired_button_hidden" label="fired_button_hidden"
							bmodify="true" text="" cssclass="rend_display_none"
							cssclasslabel="rend_display_none" />


						<s:button id="tx_button_indietro" type="submit" text="Indietro"
							onclick="" cssclass="btnStyle" validate="false" />
						<s:button id="tx_button_reset" type="submit" text="Reset"
							onclick="" cssclass="btnStyle" validate="false" />
						<s:button id="save_btn" type="submit" text="Salva" onclick=""
							cssclass="btnStyle" />
					</s:div>
					</s:form>


				
			</s:div>
		</c:when>

		<c:when test="${richiestacanc != null}">
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Ente - Consorzio</s:div>
				<center><s:label name="lblCanc" text="${message}"
					cssclass="lblMessage" /> <s:label name="lblCancmsg"
					text="Sei sicuro di voler cancellare il record selezionato?"
					cssclass="lblMessage" /></center>

				<br /><br />

				<s:div name="button_container_var" cssclass="divCenteredButtons">
					<s:table border="0" cssclass="container_btn">
						<s:thead>
							<s:tr>
								<s:td>
								</s:td>
							</s:tr>
						</s:thead>
						<s:tbody>
							<s:tr>
								<s:td>

									<s:form name="indietro" action="ente.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
									</s:form>


								</s:td>
								<s:td>

									<s:form name="form_Cancella"
										action="ente.do?action=cancel&ente_companyCode=${requestScope.companyCode}&ente_userCode=${requestScope.userCode}&ente_chiaveEnte=${requestScope.chiaveEnte}"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
										<input type="hidden" name="action" value="canc" />

										<s:button id="canc" onclick="" text="Cancella" type="submit"
											cssclass="btnStyle" />

									</s:form>

								</s:td>
							</s:tr>
						</s:tbody>
					</s:table>
				</s:div>

				</s:div>

		</c:when>
		<c:otherwise>

			<s:form name="frmConfirm" hasbtn3="false" hasbtn2="false"
				hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
				method="post" action="ente.do?action=search">
				<center><c:if test="${error != null}">
					<s:label name="lblErrore" text="${message}" cssclass="lblMessage" />
				</c:if> <c:if test="${error == null}">
					<s:label name="lblMessage" text="${message}" cssclass="lblMessage" />
				</c:if> <s:div name="divPdf">
				<br /><br />
					<s:form name="indietro" action="ente.do?action=search"
						method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
						<s:button id="tx_button_stampa_pdf" onclick="" text="Indietro"
							cssclass="btnStyle" type="submit" />
					</s:form>
				</s:div></center>
				<!--<s:hyperlink name="backButton2"
					imagesrc="../applications/templates/configurazione/img/back_icon.gif"
					href="anagprovcom.do?action=search" text=""
					cssclass="image_hyperlink" />-->
			</s:form>

		</c:otherwise>
	</c:choose>
</s:div>



