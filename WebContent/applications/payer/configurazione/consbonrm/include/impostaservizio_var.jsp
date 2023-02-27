<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>

<m:view_state id="impostaservizio" encodeAttributes="true" />

<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<c:choose>

		<c:when test="${done == null && richiestacanc == null}">

			<s:div name="divRicercaTopName" cssclass="divRicercaTop">

				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
					hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
					method="post" action="impostaservizio.do">
					
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
						<input type="hidden" name="codop"
										value="${typeRequest.editScope}" />
						<s:div name="divRicMetadati" cssclass="divRicMetadati">
	
							<input type="hidden" name="prova"
										value=0 />
							
							<s:div name="divRicercaTitleName" cssclass="divRicTitle">Imposta Servizio</s:div>
	
							<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
								<s:div name="divElement1" cssclass="floatleft">
									
									<input type="hidden" name="impostaservizio_codiceImpostaServizio"
										value="<c:out value="${impostaservizio_codiceImpostaServizio}"/>" />
										<input type="hidden" name="impostaservizio_companyCode"
										value="<c:out value="${impostaservizio_companyCode}"/>" />
									
											<input type="hidden" name="impostaservizio_codiceTipologiaServizio"
										value="<c:out value="${impostaservizio_codiceTipologiaServizio}"/>" />
													
										<s:dropdownlist label="Societ&agrave; / Tipologia Servizio:" name="ddlSocTpServ" disable="true" cssclasslabel="label250 bold textright"
											cssclass="floatleft" cachedrowset="tipologiaservizios" usexml="true" valueselected="${impostaservizio_companyCode}|${impostaservizio_codiceTipologiaServizio}">
											<s:ddloption text="Selezionare uno degli elementi" value="" />
											<s:ddloption value="{1}|{2}" text="{3} / {4}"/>
											</s:dropdownlist>
											 
									</s:div>
	
								<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
								</s:div>
	
							</s:div>
	
							<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeftDouble">
								<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
								
									<s:textbox bmodify="false"
											validator="required;accept=^[0-9A-Z]{1,20}$"
											maxlenght="20" showrequired="true"
											label="Cod. Tipologia Servizio Sistema Esterno:"
											cssclasslabel="label250 bold floatleft textright"
											name="txtImpServizio"
											text="${requestScope.impostaservizio_codiceImpostaServizio}"
											message="[accept=Imposta Serv.: ${msg_configurazione_alfanumerici_maiuscolo}]"
											cssclass="textareaman colordisabled" />
								</s:div>
							</s:div>
								
							<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
								<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true"
										maxlenght="256"
										validator="ignore;accept=${configurazione_descrizione256_regex}"
										label="Descrizione:"
										cssclasslabel="label85 bold textright" 
										name="impostaservizio_descrizioneImpostaServizio"
										message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"
										text="${requestScope.impostaservizio_descrizioneImpostaServizio}"
										cssclass="textareaman" />
								</s:div>					
							</s:div>
						</s:div>
					</c:when>
					
					
					<c:otherwise>
					
					
					<s:div name="divRicMetadati" cssclass="divRicMetadati">
						
						<input type="hidden" name="prova"
									value=1 />
							
							<input type="hidden" name="codop"
									value="${typeRequest.addScope}" />

						<s:div name="divRicercaTitleName" cssclass="divRicTitle">Imposta Servizio</s:div>
						
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
						
						<s:div name="strUsers_var">
										<s:dropdownlist label="Societ&agrave; / Tipologia Servizio:" name="impostaservizio_strTipologiaServizios" disable="false" cssclasslabel="label250 bold textright"
				 						validator="required" showrequired="true"
										cssclass="floatleft" cachedrowset="tipologiaservizios" usexml="true" valueselected="${impostaservizio_companyCode}">
										<s:ddloption text="Selezionare uno degli elementi" value="" />
										<s:ddloption value="{1}|{2}" text="{3} / {4}"/>
										</s:dropdownlist>
									</s:div>
									
						</s:div>
						
						
						
						<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeftDouble">

						<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
								
								
						<s:textbox bmodify="true"
										validator="required;accept=^[0-9A-Z]{1,20}$"
										maxlenght="20" showrequired="true"
										label="Cod. Tipologia Servizio Sistema Esterno:"
										cssclasslabel="label250 bold floatleft textright"
										name="impostaservizio_codiceImpostaServizio"
										text="${requestScope.impostaservizio_codiceImpostaServizio}"
										message="[accept=Imposta Serv.: ${msg_configurazione_alfanumerici_maiuscolo}]"
										cssclass="textareaman" />
										</s:div>

						</s:div>
						
						<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">

						<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
								
								
						<s:textbox bmodify="true"
							validator="ignore;accept=${configurazione_descrizione256_regex}"
							maxlenght="256"
							label="Descrizione:"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman"
							name="impostaservizio_descrizioneImpostaServizio"
							message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"
							text="${requestScope.impostaservizio_descrizioneImpostaServizio}" />
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
						<s:button id="tx_button_reset" onclick="" text="Reset"
							type="submit" cssclass="btnStyle" validate="false"/>
						<s:button id="save_btn" type="submit" text="Salva" onclick=""
							cssclass="btnStyle" />
					</s:div>
					


				</s:form>

			</s:div>
		</c:when>

		<c:when test="${richiestacanc != null}">
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Imposta Servizio</s:div>
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

									<s:form name="indietro" action="impostaservizio.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
									</s:form>


								</s:td>
								<s:td>

									<s:form name="form_Cancella"
										action="impostaservizio.do?action=cancel&impostaservizio_companyCode=${requestScope.companyCode}&impostaservizio_codiceTipologiaServizio=${requestScope.codiceTipologiaServizio}&impostaservizio_codiceImpostaServizio=${requestScope.codiceImpostaServizio}"
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
				method="post" action="impostaservizio.do?action=search">
				<center><c:if test="${error != null}">
					<s:label name="lblErrore" text="${message}" cssclass="lblMessage" />
				</c:if> <c:if test="${error == null}">
					<s:label name="lblMessage" text="${message}" cssclass="lblMessage" />
				</c:if> <s:div name="divPdf">
				<br /><br />
					<s:form name="indietro" action="impostaservizio.do?action=search"
						method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
						<s:button id="tx_button_stampa_pdf" onclick="" text="Indietro"
							cssclass="btnStyle" type="submit" />
					</s:form>
				</s:div></center>
			
			</s:form>

		</c:otherwise>
	</c:choose>
</s:div>



