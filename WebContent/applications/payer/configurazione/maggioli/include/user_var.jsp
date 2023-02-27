<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="seda" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="users" encodeAttributes="true"/>
<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />

<seda:div name="div_selezione" cssclass="div_align_center divSelezione">
<c:choose>
	<c:when test="${done == null && richiestacanc == null}">
	<seda:div name="divRicercaTopName" cssclass="divRicercaTop">
		<seda:div name="divRicercaTitleName" cssclass="divRicTitle">Utente</seda:div>			
		<seda:form name="frmAction" hasbtn3="false" hasbtn2="false" hasbtn1="false" btn1text="..."  
			btn1onclick="this.form.submit();" method="post" action="user.do" >
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
			<seda:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
			<seda:div name="divElement1" cssclass="divRicMetadatiLeft">
			<c:choose>
				<c:when test="${action == 'edit'|| action == 'saveedit'}">
					<input type="hidden" name="codop" value="${typeRequest.editScope}" />
					<input type="hidden" name="user_companyCode" value="<c:out value="${requestScope.user_companyCode}"/>"/>
					
					<seda:dropdownlist label="Societ&agrave;:" name="ddlSoc" disable="true" cssclasslabel="label85 bold textright"
									cssclass="textareaman" cachedrowset="companies" usexml="true" valueselected="${requestScope.user_companyCode}"
									validator="required" showrequired="true">
							<seda:ddloption text="Selezionare uno degli elementi" value="" />
							<seda:ddloption value="{1}" text="{2}"/>
					</seda:dropdownlist>					
				</c:when>
				<c:otherwise>
					<input type="hidden" name="codop" value="${typeRequest.addScope}" />
					
						<seda:dropdownlist label="Societ&agrave;:" name="user_companyCode" disable="false" cssclasslabel="label85 bold textright"
										cssclass="textareaman" cachedrowset="companies" usexml="true" valueselected="${requestScope.user_companyCode}"
										validator="required" showrequired="true">
								<seda:ddloption text="Selezionare uno degli elementi" value="" />
								<seda:ddloption value="{1}" text="{2}"/>
						</seda:dropdownlist>
					
				</c:otherwise>
			</c:choose>
		</seda:div>

        <seda:div name="divElement2" cssclass="divRicMetadatiCenter">
			<c:choose>
				<c:when test="${action == 'edit' || action == 'saveedit'}">
					<input type="hidden" name="user_userCode" value="<c:out value="${user_userCode}"/>"/>
					
					<seda:textbox bmodify="false" label="Utente:" name="txtCodUte" text="${user_userCode}" cssclasslabel="label85 bold textright" cssclass="textareaman colordisabled" validator="required" showrequired="true"/>					
				</c:when>
				<c:otherwise>
					
                        <seda:textbox bmodify="true" label="Utente:" name="user_userCode" 
                        validator="required;minlength=5;accept=^\w{1,5}$;maxlength=5" showrequired="true"
                        maxlenght="5" text="${requestScope.user_userCode}" 
                        message="[accept=Utente: ${msg_configurazione_alfanumerici}]"
                        cssclasslabel="label85 bold textright" cssclass="textareaman"/>
					
				</c:otherwise>
			</c:choose>
		</seda:div>

         <seda:div name="divElement3" cssclass="divRicMetadatiRight">			 
                <seda:textbox bmodify="true" label="Descr.Utente:" name="user_userDescription" 
				validator="ignore;accept=${configurazione_descrizione256_regex}"
				message="[accept=Descr.Utente: ${msg_configurazione_descrizione_regex}]"
                maxlenght="256" text="${user_userDescription}" cssclasslabel="label85 bold textright" cssclass="textareaman"/>
         </seda:div>
		</seda:div>
		
		<seda:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">		
			<seda:div name="divElement4" cssclass="divRicMetadatiSingleRow">
				<seda:textbox bmodify="true" validator="ignore;accept=^\w{1,3}$;maxlength=3" 
				maxlenght="3"label="Ambito:" name="user_scopeCncCode" text="${user_scopeCncCode}" 
				message="[accept=Ambito: ${msg_configurazione_alfanumerici}]"
				cssclasslabel="label85 bold textright" cssclass="textareaman"/>
	         </seda:div>
			
	        <seda:div name="divElement5" cssclass="divRicMetadatiSingleRow">
	                 <seda:textbox bmodify="true" validator="ignore;accept=^(IT|SM)\d{2}[a-zA-Z]\d{10}[0-9a-zA-Z]{12}|(IT|SM)\d{2}[a-zA-Z]\d{22}$;minlength=1;maxlength=27" 
	                 maxlenght="27" label="IBAN:" name="user_codiceIban" text="${user_codiceIban}" 
	                 message="[accept=IBAN: ${msg_configurazione_IBAN}]"
	                 cssclasslabel="label85 bold textright" cssclass="textareaman"/>            
	        </seda:div>
			
	        <seda:div name="divElement6" cssclass="divRicMetadatiSingleRow">
	                 <seda:textbox bmodify="true" validator="ignore;minlength=5;accept=^[0-9A-Z]{5}$" 
	                 maxlenght="5" label="Codice SIA:" name="user_codiceSia" text="${user_codiceSia}" 
	                 message="[accept=Codice SIA: ${msg_configurazione_alfanumerici_maiuscolo}]"
	                 cssclasslabel="label85 bold textright" cssclass="textareaman"/>
	        </seda:div>
		</seda:div>
		
		<seda:div name="divRicercaLeft1" cssclass="divRicMetadatiCenter">
			<seda:div name="divElement70" cssclass="divRicMetadatiSingleRow">
	                 <seda:textbox bmodify="true"  validator="ignore" 
	                 label="Descrizione Ordinante:" name="user_ordinanteDescription" maxlenght="150" text="${user_ordinanteDescription}" 
	                 cssclasslabel="label85 bold textright" cssclass="textareaman"/>
	        </seda:div>
			<seda:div name="divElement7" cssclass="divRicMetadatiSingleRow">
	                 <seda:textbox bmodify="true"  validator="ignore;accept=^[0-9]{11}|[a-zA-Z]{6}\d\d[a-zA-Z]\d\d[a-zA-Z]\d\d\d[a-zA-Z]$" 
	                 label="Cod.Fisc./P.Iva:" name="user_codiceFiscale" maxlenght="16" text="${user_codiceFiscale}" 
	                 message="[accept=Cod.Fisc./P.Iva: ${msg_configurazione_codicefiscale_piva}]"
	                 cssclasslabel="label85 bold textright" cssclass="textareaman"/>
	        </seda:div>
	        <seda:div name="divElement8" cssclass="divRicMetadatiSingleRow">
	                 <seda:textbox bmodify="true" validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,50}$" label="Dir. Ftp:" 
	                 name="user_dirRemotaServerFtp" text="${user_dirRemotaServerFtp}"
	                 message="[accept=Dir. Ftp: ${msg_configurazione_directory_ftp}]" 
	                 cssclasslabel="label85 bold textright" cssclass="textareaman"/>
	        </seda:div>
	        <seda:div name="divElement9" cssclass="divRicMetadatiSingleRow">
				    <seda:dropdownlist label="Invio Ftp:" name="user_flagAbilitazioneInvioFtp" disable="false" cssclasslabel="label85 bold textright"
											cssclass="textareaman" 
									    valueselected="${requestScope.user_flagAbilitazioneInvioFtp}">
						<seda:ddloption value="Y" text="SI"/>
						<seda:ddloption value="N" text="NO"/>
					</seda:dropdownlist>
			</seda:div>
		
		</seda:div>
		
		<seda:div name="divRicercaLeft2" cssclass="divRicMetadatiRight">
		
		<seda:div name="divElement10" cssclass="divRicMetadatiSingleRow">
                 <seda:textbox bmodify="true" validator="ignore;accept=^\w{1,50}$;maxlength=50" 
                 maxlenght="50" label="User Ftp:" name="user_utenteFtp" text="${user_utenteFtp}" 
                 message="[accept=User Ftp: ${msg_configurazione_alfanumerici}]"
                 cssclasslabel="label85 bold textright" cssclass="textareaman"/>
        </seda:div>
        
        <seda:div name="divElement11" cssclass="divRicMetadatiSingleRow">
                 <seda:textbox bmodify="true" bpassword="true"  validator="ignore;minlength=3;maxlength=20;" 
                 maxlenght="20" label="Pwd Ftp:" name="user_passwordFtp" text="${user_passwordFtp}" 
                 cssclasslabel="label85 bold textright" cssclass="textareaman"/>
        </seda:div>
        
        <seda:div name="divElement12" cssclass="divRicMetadatiSingleRow">
                 <seda:textbox bmodify="true" validator="ignore;accept=^[a-zA-Z0-9\._]{1,50}$|^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$" 
                 label="Server Ftp:" name="user_serverFtp" maxlenght="20" text="${user_serverFtp}" 
                 message="[accept=Server Ftp: ${msg_configurazione_ftp}]"
                 cssclasslabel="label85 bold textright" cssclass="textareaman"/>
        </seda:div>
        
		</seda:div>
		<seda:div name="button_container_var" cssclass="divRicBottoni">
						
						<seda:button id="tx_button_indietro" type="submit" text="Indietro"
							onclick="" cssclass="btnStyle" validate="false" />
						<seda:button id="tx_button_reset" onclick="" text="Reset"
							type="submit" cssclass="btnStyle" validate="false"/>
						<seda:button id="save_btn" type="submit" text="Salva" onclick=""
							cssclass="btnStyle" />
					</seda:div>
		<!--<seda:div name="divRicercaBottoni" cssclass="divRicBottoni">
			<center>
				<seda:table border="0" cssclass="container_btn">
                    <seda:thead><seda:tr/></seda:thead>
					<seda:tbody>
						<seda:tr>
							<seda:td>
								<seda:button id="save_btn" type="submit" cssclass="save_btn" text="Salva" onclick="" />							
							</seda:td>
							<seda:td>
								<seda:hyperlink name="backButton" imagesrc="../applications/templates/configurazione/img/back_icon.gif" 
												href="/manager/configurazione/user.do" text="" cssclass="image_hyperlink" />
							</seda:td>
						</seda:tr>
					</seda:tbody>
				</seda:table>
			</center>
		</seda:div>
		-->
		</seda:form>
	</seda:div>
	</c:when>
	<c:when test="${richiestacanc != null}">
	<seda:div name="divRicercaTopName" cssclass="divRicercaTop">
	<seda:div name="divRicercaTitleName" cssclass="divRicTitle">Utente</seda:div>	

			<seda:label name="lblCanc" text="${message}"
				cssclass="lblMessage" />
				<seda:label name="lblCancmsg" text="Sei sicuro di voler cancellare il record selezionato?"
				cssclass="lblMessage" />
				
			<br />
			<br />
			<center>
			<seda:table border="0" cssclass="container_btn">
				<seda:thead>
					<seda:tr>
						<seda:td>
						</seda:td>
					</seda:tr>
				</seda:thead>
				<seda:tbody>
					<seda:tr>
						<seda:td>
						
						<seda:form name="indietro" action="user.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<seda:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
						</seda:form>
						</seda:td>
						<seda:td>
						
							<seda:form name="form_Cancella"
										action="user.do?action=cancel&user_companyCode=${requestScope.companyCode}&user_userCode=${requestScope.userCode}"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
										<input type="hidden" name="action" value="canc" />

										<seda:button id="canc" onclick="" text="Cancella" type="submit"
											cssclass="btnStyle" />

									</seda:form>
						
									
						</seda:td>
					</seda:tr>
				</seda:tbody>
			</seda:table>
			</center>
		</seda:div>
		<!--<seda:div name="divRicercaBottoni" cssclass="divRicBottoni">
			<center>
				<seda:table border="0" cssclass="container_btn">
	                   <seda:thead><seda:tr/></seda:thead>
					<seda:tbody>
						<seda:tr>
							<seda:td>
								<seda:hyperlink name="cancButton" imagesrc="../applications/templates/shared/img/canc_icon.gif" 
												href="/manager/configurazione/user.do?action=cancel&user_companyCode=${requestScope.companyCode}&user_userCode=${requestScope.userCode}" text="" cssclass="image_hyperlink" />								
							</seda:td>
							<seda:td>
								<seda:hyperlink name="backButton2" imagesrc="../applications/templates/shared/img/back_icon.gif" 
												href="/manager/configurazione/user.do" text="" cssclass="image_hyperlink" />
							</seda:td>
						</seda:tr>
					</seda:tbody>
				</seda:table>
			</center>
		</seda:div>
    --></c:when>
	<c:otherwise>
		<seda:form name="frmConfirm" hasbtn3="false" hasbtn2="false" hasbtn1="false" btn1text="..."  
			btn1onclick="this.form.submit();" method="post" action="user.do?action=search">
			<center>
		          <c:if test="${error != null}">
			             <seda:label name="lblErrore" text="${message}" cssclass="lblMessage"/>
		          </c:if>
		          <c:if test="${error == null}">
			            <seda:label name="lblMessage" text="${message}" cssclass="lblMessage"/>
		          </c:if>				
			</center>
			<br /><br />
			<center>			
			<seda:button id="button_indietro" cssclass="btnStyle" type="submit" text="Indietro" onclick="" />
			</center>
		</seda:form>
	</c:otherwise>
</c:choose>
</seda:div>
