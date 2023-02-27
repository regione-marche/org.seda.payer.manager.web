<%--  **  IMPORT JAVA TAG LIB  **  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>

<%--  **  My View State  **  --%>
<m:view_state id="parametriottico" encodeAttributes="true" />

<%--  **  My TypeRequest Bean  **  --%>
<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />

<!-- ** Page Functions ** -->
<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>
<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<c:choose>

		<%-- ACTION - INSERIMENTO & AGGIORNAMENTO --%>

		<c:when test="${done == null && richiestacanc == null}">
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">
				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
						hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
						method="post" action="parametriOttico.do">
					<s:textbox name="fired_button_hidden" label="fired_button_hidden" bmodify="true" text="" cssclass="display_none" cssclasslabel="display_none" />
					<c:choose>
						<c:when test="${action == 'saveedit'}">
							<input type="hidden" name="action" value="<c:out value="${action}"/>" />
						</c:when>
						<c:when test="${action == 'saveadd'}">
							<input type="hidden" name="action" value="<c:out value="${action}"/>" />
						</c:when>	
						<c:otherwise>
							<input type="hidden" name="action" value="save<c:out value="${action}"/>" />
						</c:otherwise>
					</c:choose>
					<s:div name="divRicMetadati" cssclass="divRicMetadati">
						<s:div name="divRicercaTitleName" cssclass="divRicTitle">Parametri Ottico</s:div>
						<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
							<c:choose>
								<c:when test="${action == 'edit'|| action == 'saveedit'}">
		
									<%-- ACTION - AGGIORNAMENTO --%>
		
									<input type="hidden" name="codop" value="${typeRequest.editScope}" />
									<input type="hidden" name="parametriottico_companyCode"	value="<c:out value="${parametriottico_companyCode}"/>" />
									<input type="hidden" name="parametriottico_userCode"	value="<c:out value="${parametriottico_userCode}"/>" />
									<input type="hidden" name="parametriottico_chiaveEnte"	value="<c:out value="${parametriottico_chiaveEnte}"/>" />
									<s:div name="divElement11" cssclass="divRicMetadatiTopLeft">
										<s:div name="strSocieta_var">
											<%-- SOCIETA --%>
											<s:dropdownlist name="listaSociataReadonly"
												label="Societ&agrave;:"
												cssclasslabel="label85 bold textright"
												cssclass="textareaman"
												cachedrowset="listaSocieta" usexml="true"
												valueselected="${parametriottico_companyCode}"
												disable="true">
												<s:ddloption text="{2}" value="{1}" />
											</s:dropdownlist>
										</s:div>
									</s:div>
									<s:div name="divElement22" cssclass="divRicMetadatiCenter">
										<%-- UTENTE --%>
										<s:dropdownlist name="listaUtenteReadOnly"
											disable="true" cssclass="textareaman"
											label="Utente:" cssclasslabel="label65 bold textright"
											cachedrowset="listaUtenti" usexml="true"
											valueselected="${parametriottico_userCode}">
											<s:ddloption text="{2}" value="{1}" />
										</s:dropdownlist>
									</s:div>
									<s:div name="divElement33" cssclass="divRicMetadatiRight">
										<%-- ENTE --%>
										<s:dropdownlist name="listaEnteReadOnly"
											disable="true" 
											cssclass="textareaman"
											cssclasslabel="label65 bold textright"
											label="Ente:" 
											cachedrowset="listaEntiGenerici" usexml="true"
											valueselected="${parametriottico_chiaveEnte}">
											<s:ddloption text="{2}" value="{1}" />
										</s:dropdownlist>
									</s:div>
								</c:when>
								<c:otherwise>
									
									<%-- ACTION - INSERIMENTO --%>
		
									<input type="hidden" name="codop" value="${typeRequest.addScope}" />
									<s:div name="divElement11" cssclass="divRicMetadatiTopLeft">
										<s:div name="strSocieta_var">
											<%-- SOCIETA --%>
											<s:dropdownlist name="parametriottico_companyCode" disable="${enableListaSocieta}"
												label="Societ&agrave;:"
												cssclasslabel="label85 bold textright"
												cssclass="textareaman"
												cachedrowset="listaSocieta" usexml="true"
												onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
												valueselected="${parametriottico_companyCode}"
												validator="required" showrequired="true">
												<s:ddloption text="Selezionare uno degli elementi della lista" value=""/>
												<s:ddloption text="{2}" value="{1}" />
											</s:dropdownlist>
											<noscript>
												<s:button id="tx_button_societa_changed" 
													disable="${enableListaSocieta}" onclick="" text="" validate="false"
													type="submit" cssclass="btnimgStyle" title="Aggiorna" />
											</noscript>
										</s:div>
									</s:div>
									<s:div name="divElement22" cssclass="divRicMetadatiCenter">
										<%-- UTENTE --%>
										<s:dropdownlist name="parametriottico_userCode"
											disable="${enableListaUtenti}" cssclass="textareaman"
											label="Utente:" cssclasslabel="label65 bold textright"
											cachedrowset="listaUtenti" usexml="true"
											onchange="setFiredButton('tx_button_utente_changed');this.form.submit();"
											valueselected="${parametriottico_userCode}"
											validator="required" showrequired="true">
											<s:ddloption text="Selezionare uno degli elementi della lista" value=""/>
											<s:ddloption text="{2}" value="{1}" />
										</s:dropdownlist>
										<noscript>
											<s:button id="tx_button_utente_changed"
												disable="${enableListaUtenti}" onclick="" text="" validate="false"
												type="submit" cssclass="btnimgStyle" title="Aggiorna"
												 />
										</noscript>
									</s:div>
									<s:div name="divElement33" cssclass="divRicMetadatiRight">
										<%-- ENTE --%>
										<s:dropdownlist name="parametriottico_chiaveEnte"
											disable="${enableListaEnti}" 
											cssclass="textareaman"
											cssclasslabel="label65 bold textright"
											label="Ente:" 
											cachedrowset="listaEntiGenerici" usexml="true"
											valueselected="${parametriottico_chiaveEnte}"
											validator="required" showrequired="true">
											<s:ddloption text="Selezionare uno degli elementi della lista" value=""/>
											<s:ddloption text="{2}" value="{1}" />
										</s:dropdownlist>
									</s:div>
								</c:otherwise>
							</c:choose>
						</s:div>
						<s:div name="divRicercaLeft1" cssclass="divRicMetadatiLeft">
							<s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="parametriottico_sorgenteImmagini" disable="false"
									label="Sorg. Img:"
									valueselected="${parametriottico_sorgenteImmagini}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman">
									<s:ddloption value="P" text="Accesso a PayER" />
									<s:ddloption value="A" text="Accesso Esterno" />
								</s:dropdownlist>
							</s:div>
						</s:div>
						<s:div name="divRicercaLeft2" cssclass="divRicMetadatiCenter">
							<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="parametriottico_documento" disable="false" label="Documento:"
									valueselected="${parametriottico_documento}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman">
									<s:ddloption value="N" text="No" />
									<s:ddloption value="I" text="Immagine" />
									<s:ddloption value="T" text="Template" />
								</s:dropdownlist>
							</s:div>
						</s:div>
						<s:div name="divRicercaLeft3" cssclass="divRicMetadatiRight">
							<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="parametriottico_relata" disable="false" label="Relata:"
									valueselected="${parametriottico_relata}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman">
									<s:ddloption value="N" text="No" />
									<s:ddloption value="I" text="Immagine" />
								</s:dropdownlist>
							</s:div>
						</s:div>
						<s:div name="divRicercaLeft4" cssclass="divRicMetadatiLeft">
							<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="parametriottico_bollettino" disable="false"
									label="Bollettino:"
									valueselected="${parametriottico_bollettino}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman" >
									<s:ddloption value="N" text="No" />
									<s:ddloption value="Y" text="Si" />
								</s:dropdownlist>
							</s:div>
						</s:div>
						<s:div name="divRicercaLeft5" cssclass="divRicMetadatiCenter">
							<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="parametriottico_quietanza" disable="false" label="Quietanza:"
									valueselected="${parametriottico_quietanza}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman">
									<s:ddloption value="N" text="No" />
									<s:ddloption value="Y" text="Si" />
								</s:dropdownlist>
							</s:div>
						</s:div>
						<s:div name="divRicercaLeft6" cssclass="divRicMetadatiRight">
							<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" maxlenght="256" 
									validator="ignore;url;maxlength=256;"
									label="Url Integr.:" name="parametriottico_server"
									text="${parametriottico_server}"
									message="[accept=Url Integr.: ${msg_configurazione_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
						</s:div>
						<s:div name="divRicercaLeft7" cssclass="divRicMetadatiLeft">
							<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<!--<s:textbox bmodify="true" maxlenght="50" validator="ignore;minlength=1;maxlength=50;accept=^[\w\_\.\-]{1,50}$"
									label="User Integr.:" name="parametriottico_userServer"
									text="${parametriottico_userServer}"
									message="[accept=User Integr.: ${msg_configurazione_descrizione_1}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />-->
									
								<s:textbox bmodify="true" maxlenght="50" validator="ignore;minlength=1;maxlength=50"
									label="User Integr.:" name="parametriottico_userServer"
									text="${parametriottico_userServer}"
									message="[accept=User Integr.: ${msg_configurazione_descrizione_1}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
									
									
									
							</s:div>
						</s:div>
						<s:div name="divRicercaLeft8" cssclass="divRicMetadatiCenter">
							<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" maxlenght="20" validator="ignore;minlength=6;maxlength=20;"
									label="Pwd Integr.:" name="parametriottico_passwordServer"
									text="${parametriottico_passwordServer}" bpassword="true" 
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
						</s:div>
						<s:div name="divRicercaLeft9" cssclass="divRicMetadatiRight">
							<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" maxlenght="256" validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,256}$"
									label="Dir. In Flussi Dati:" name="parametriottico_dirInputFlussiDati"
									text="${parametriottico_dirInputFlussiDati}"
									message="[accept=Dir. In Flussi Dati: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
						</s:div>
						<s:div name="divRicercaLeft10" cssclass="divRicMetadatiLeft">
							<s:div name="divElement12" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" maxlenght="256" validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,256}$"
									label="Dir. Out Flussi Dati:" name="parametriottico_dirOutputFlussiDati"
									text="${parametriottico_dirOutputFlussiDati}"
									message="[accept=Dir. Out Flussi Dati: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
						</s:div>
						<s:div name="divRicercaLeft11" cssclass="divRicMetadatiCenter">
							<s:div name="divElement13" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" maxlenght="256" validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,256}$"
									label="Dir. In Flussi Img.:" name="parametriottico_dirInputFlussiImmagini"
									text="${parametriottico_dirInputFlussiImmagini}"
									message="[accept=Dir. In Flussi Img: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
						</s:div>
						<s:div name="divRicercaLeft12" cssclass="divRicMetadatiRight">
							<s:div name="divElement14" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" maxlenght="256" validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,256}$"
									label="Dir. Out Flussi Img.:" name="parametriottico_dirOutputFlussiImmagini"
									text="${parametriottico_dirOutputFlussiImmagini}"
									message="[accept=Dir. Out Flussi Img.: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
						</s:div>
						<s:div name="divRicercaLeft13" cssclass="divRicMetadatiLeft">
							<s:div name="divElement15" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" maxlenght="256" validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,256}$"
									label="Dir. Log Elab.:" name="parametriottico_dirLogFlussi"
									text="${parametriottico_dirLogFlussi}"
									message="[accept=Dir. Log Elab.: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
						</s:div>
						<s:div name="divRicercaLeft14" cssclass="divRicMetadatiCenter">
							<s:div name="divElement16" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" maxlenght="256" validator="ignore;accept=^[\\a-zA-Z0-9:_/\\.]{1,256}$"
									label="Dir. Img. Elab.:" name="parametriottico_dirImmagini"
									text="${parametriottico_dirImmagini}"
									message="[accept=Dir. Img. Elab.: ${msg_configurazione_directory_ftp}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
						</s:div>
						<s:div name="divRicercaRight15" cssclass="divRicMetadatiRight">
							<s:div name="divElement17" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" maxlenght="100" validator="ignore;accept=${configurazione_email_regex};maxlength=100"
									label="Email Admin:" name="parametriottico_emailAmministratore"
									text="${parametriottico_emailAmministratore}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
						</s:div>
					</s:div>
					<s:div name="button_container_var" cssclass="divRicBottoni">
						<s:button id="tx_button_indietro" type="submit" text="Indietro" onclick="" cssclass="btnStyle" validate="false" />
						<s:button id="tx_button_reset" onclick="" text="Reset" type="submit" cssclass="btnStyle" validate="false"/>
						<s:button id="save_btn" type="submit" text="Salva" onclick="" cssclass="btnStyle" />
					</s:div>
				</s:form>
			</s:div>
		</c:when>

		<%-- ACTION - RICHIESTA CANCELLAZIONE --%>

		<c:when test="${richiestacanc != null}">
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">
				<s:div name="divRicercaTitleName2" cssclass="divRicTitle">Parametri Ottico</s:div>	
				<s:label name="lblCanc" text="${message}" cssclass="lblMessage" />
				<s:label name="lblCancmsg" text="Sei sicuro di voler cancellare il record selezionato?" cssclass="lblMessage" />
				<br />
				<br />
				<center>
					<s:table border="0" cssclass="container_btn">
						<s:thead>
							<s:tr>
								<s:td></s:td>
							</s:tr>
						</s:thead>
						<s:tbody>
							<s:tr>
								<s:td>
									<s:form name="indietro" action="parametriOttico.do?action=search" method="post" 
											hasbtn1="false" hasbtn2="false" hasbtn3="false">
										<s:button id="button_indietro" cssclass="btnStyle" type="submit" text="Indietro" onclick="" />
									</s:form>
								</s:td>
								<s:td>
									<s:form name="form_Cancella" action="parametriOttico.do?action=cancel&parametriottico_companyCode=${parametriottico_companyCode}&parametriottico_userCode=${parametriottico_userCode}&parametriottico_chiaveEnte=${parametriottico_chiaveEnte}"
											method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
										<input type="hidden" name="action" value="canc" />
										<s:button id="canc" onclick="" text="Cancella" type="submit" cssclass="btnStyle" />
									</s:form>
								</s:td>
							</s:tr>
						</s:tbody>
					</s:table>
				</center>
			</s:div>
		</c:when>
		<c:otherwise>

			<%-- ACTION - CANCELLAZIONE --%>

			<s:form name="frmConfirm" hasbtn3="false" hasbtn2="false"
					hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
					method="post" action="parametriOttico.do?action=search">
				<center>
					<c:if test="${error != null}"><s:label name="lblErrore" text="${message}"/></c:if>
					<c:if test="${error == null}"><s:label name="lblMessage" text="${message}"/></c:if>
				</center>
				<br />
				<br />
				<center><s:button id="button_indietro" cssclass="btnStyle" type="submit" text="Indietro" onclick="" /></center>
			</s:form>
		</c:otherwise>
	</c:choose>
</s:div>
