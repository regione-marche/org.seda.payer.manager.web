<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>

<m:view_state id="configestrattocontos" encodeAttributes="true" />

<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />

<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
<c:choose>
	<c:when test="${done == null && richiestacanc == null}">	
		<s:div name="divRicercaTopName" cssclass="divRicercaTop">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle">Configurazione Enti Invio RT</s:div>

			<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
				hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
				method="post" action="configrtente.do">

				<c:choose>
					<c:when test="${action=='saveedit'}">
						<input type="hidden" name="action" value="<c:out value="${action}"/>" />
					</c:when>
					<c:when test="${action=='saveadd'}">
						<input type="hidden" name="action" value="<c:out value="${action}"/>" />
					</c:when>	
					<c:otherwise>
						<input type="hidden" name="action" value="save<c:out value="${action}"/>" />
					</c:otherwise>
				</c:choose>

				<s:div name="divRicercaTitleName1" cssclass="divRicMetadati">
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
						
							<c:choose>
								<c:when test="${action == 'edit' || action == 'saveedit'}">
									<input type="hidden" name="codop" value="${typeRequest.editScope}" />
									<input type="hidden" name="configrtente_userCode"	 value="<c:out value="${configrtente_userCode}"/>" />
									<input type="hidden" name="configrtente_chiaveEnte"	 value="<c:out value="${configrtente_chiaveEnte}"/>" />
									<input type="hidden" name="configrtente_companyCode" value="<c:out value="${configrtente_companyCode}"/>" />
									<s:dropdownlist label="Societ&agrave;/Utente/Ente:" name="ddlSocUteEnt" 
							 						   disable="true"
													   cachedrowset="entetiposervizios" usexml="true" 
													   valueselected="${configrtente_companyCode}|${configrtente_userCode}|${configrtente_chiaveEnte}" 
													   cssclasslabel="label160 bold textright"
													   cssclass="seda-ui-ddl tbddlMax780 floatleft" >
										<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}"/>
									</s:dropdownlist>
																												
								</c:when>
								<c:otherwise>
									<input type="hidden" name="codop" value="${typeRequest.addScope}" /> 
									<s:dropdownlist label="Societ&agrave;/Utente/Ente:" name="configrtente_strEntetiposervizios" 
							 						   disable="${action == 'edit' || action == 'saveedit'}"
													   cachedrowset="entetiposervizios" usexml="true" 
													   valueselected="${configrtente_strEntetiposervizios}" 
													   cssclasslabel="label160 bold textright"
													   validator="required"
													   cssclass="seda-ui-ddl tbddlMax780 floatleft" showrequired="true" >
										<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
										<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}"/>
									</s:dropdownlist>
									</c:otherwise>
							</c:choose>						

						
					</s:div>
					
					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					
						<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true"
									validator="ignore;maxlength=256" maxlenght="256"
									label="Cod. IDPA Ente:" name="configrtente_codiceIdpa"
									text="${configrtente_codiceIdpa}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
						</s:div>
						<s:div name="divElement5bis" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true"
									validator="ignore;url;maxlength=100" maxlenght="100"
									label="Utente:" name="configrtente_userInvioRt"
									text="${configrtente_userInvioRt}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
						</s:div>
						<s:div name="divElement5ter" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true"
									validator="ignore;url;maxlength=40" maxlenght="40"
									label="Password Accesso:" name="configrtente_passwInvioRt"
									text="${configrtente_passwInvioRt}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
						</s:div>
						
					</s:div>
					
					<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
						<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true"
									validator="ignore;url;maxlength=256"
									label="Url Invio RT:" name="configrtente_urlInvioRt"
									text="${configrtente_urlInvioRt}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
						</s:div>
						<s:div name="divElement6bis" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true"
									validator="ignore;url;maxlength=100"
									label="Email Invio Esito Consegna RT:" name="configrtente_mailInvioEsitoInvioRt"
									text="${configrtente_mailInvioEsitoInvioRt}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
						</s:div>
						<s:div name="divElement6ter" cssclass="divRicMetadatiSingleRow">
<!-- 							<s:textbox bmodify="true"
									validator="ignore;url;maxlength=1"
									label="Ente Attivo:" name="configrtente_flagAttivo"
									text="${configrtente_flagAttivo}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />  -->
							<s:dropdownlist label="Ente Attivo:" name="configrtente_flagAttivo" 
							 		disable="false" 
									valueselected="${configrtente_flagAttivo}" 
									cssclasslabel="label85 bold textright"
									cssclass="textareaman" showrequired="true" >
									<s:ddloption value="Y" text="Si"/>
									<s:ddloption value="N" text="No"/>
							</s:dropdownlist>

						</s:div>

					</s:div>
					
					<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
						<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true"
									validator="ignore;url;maxlength=256"					
									label="Url Recupero Esiti:" name="configrtente_urlRecuperoEsitoInvio"
									text="${configrtente_urlRecuperoEsitoInvio}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
						</s:div>
						<s:div name="divElement7bis" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true"
									validator="ignore;url;maxlength=100"					
									label="Email Invio Esito Recupero Esiti:" name="configrtente_mailInvioEsitoRecuperoEsitoRt"
									text="${configrtente_mailInvioEsitoRecuperoEsitoRt}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
						</s:div>
						<s:div name="divElement7ter" cssclass="divRicMetadatiSingleRow">
<!--  							<s:textbox bmodify="true"
									validator="ignore;url;maxlength=1"					
									label="Ente Abilitato:" name="configrtente_flagAbilitato"
									text="${configrtente_flagAbilitato}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />  -->
							<s:dropdownlist label="Ente Abilitato:" name="configrtente_flagAbilitato" 
							 		disable="false" 
									valueselected="${configrtente_flagAbilitato}" 
									cssclasslabel="label85 bold textright"
									cssclass="textareaman" showrequired="true" >
									<s:ddloption value="Y" text="Si"/>
									<s:ddloption value="N" text="No"/>
							</s:dropdownlist>
									
						</s:div>

					</s:div>
					
					<s:div name="button_container_var" cssclass="divRicBottoni">
						
						<s:button id="tx_button_indietro" type="submit" text="Indietro"
							onclick="" cssclass="btnStyle" validate="false" />
						<s:button id="tx_button_reset" onclick="" text="Reset"
							type="submit" cssclass="btnStyle" validate="false"/>
						<s:button id="save_btn" type="submit" text="Salva" onclick=""
							cssclass="btnStyle" />
					</s:div>
				</s:div>
			</s:form>
		</s:div>
	</c:when>
	<c:when test="${richiestacanc != null}">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:div name="divRicercaTitleName2" cssclass="divRicTitle">Configurazione Enti Invio RT</s:div>	

			<s:label name="lblCanc" text="${message}"
				cssclass="lblMessage" />
				<s:label name="lblCancmsg" text="Sei sicuro di voler cancellare il record selezionato?"
				cssclass="lblMessage" />
				
			<br />
			<br />
			<center>
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
						
						<s:form name="indietro" action="configrtente.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
						</s:form>
						</s:td>
						<s:td>
						
							<s:form name="form_Cancella"
										action="configrtente.do?action=cancel&configrtente_companyCode=${configrtente_companyCode}&configrtente_userCode=${configrtente_userCode}&configrtente_chiaveEnte=${configrtente_chiaveEnte}&&configrtente_codiceIdpa=${configrtente_codiceIdpa}"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
										<input type="hidden" name="action" value="canc" />

										<s:button id="canc" onclick="" text="Cancella" type="submit"
											cssclass="btnStyle" />

									</s:form>
						
									
						</s:td>
					</s:tr>
				</s:tbody>
			</s:table>
			</center>
		</s:div>

	</c:when>
	<c:otherwise>
	
	<s:form name="frmConfirm" hasbtn3="false" hasbtn2="false"
			hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
			method="post" action="configrtente.do?action=search">
			<center><c:if test="${error != null}">
				<s:label name="lblErrore" text="${message}"/>
			</c:if> <c:if test="${error == null}">
				<s:label name="lblMessage" text="${message}"/>
			</c:if></center>
			<br />
			<br />
			<center>			
			<s:button id="button_indietro" cssclass="btnStyle" type="submit" text="Indietro" onclick="" />
			</center>
		</s:form>

	</c:otherwise>
</c:choose>
</s:div>

