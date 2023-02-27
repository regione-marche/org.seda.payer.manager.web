<%--  **  IMPORT JAVA TAG LIB  **  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>

<%--  **  My View State  **  --%>
<m:view_state id="enteimpostaservizionotifica" encodeAttributes="true" />

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
						method="post" action="enteImpostaServizioNotifica.do">
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
						<s:div name="divRicercaTitleName" cssclass="divRicTitle">Ente-Imposta Servizio Notifica</s:div>
						<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
							<c:choose>
								<c:when test="${action == 'edit'|| action == 'saveedit'}">
									<%-- ACTION - AGGIORNAMENTO --%>
									<input type="hidden" name="codop" value="${typeRequest.editScope}" />
									<input type="hidden" name="enteimpostaservizionotifica_userCode"	value="<c:out value="${enteimpostaservizionotifica_userCode}"/>" />
									<input type="hidden" name="enteimpostaservizionotifica_chiaveEnte"	value="<c:out value="${enteimpostaservizionotifica_chiaveEnte}"/>" />
									<input type="hidden" name="enteimpostaservizionotifica_codiceImpostaServizio"	value="<c:out value="${enteimpostaservizionotifica_codiceImpostaServizio}"/>" />
									<s:div name="divElement11" cssclass="divRicMetadatiTopLeft">
										<s:dropdownlist name="listaUtenteReadOnly"
											disable="true" cssclass="textareaman"
											label="Utente:" cssclasslabel="label65 bold textright"
											cachedrowset="listaUtenti" usexml="true"
											valueselected="${enteimpostaservizionotifica_userCode}">
											<s:ddloption text="{2}" value="{1}" />
										</s:dropdownlist>
									</s:div>
									<s:div name="divElement22" cssclass="divRicMetadatiTopCenter">
										<s:dropdownlist name="listaEnteReadOnly"
											disable="true" 
											cssclass="textareaman"
											cssclasslabel="label65 bold textright"
											label="Ente:" 
											cachedrowset="listaEntiGenerici" usexml="true"
											valueselected="${enteimpostaservizionotifica_chiaveEnte}">
											<s:ddloption text="{2}" value="{1}" />
										</s:dropdownlist>
									</s:div>
									<s:div name="divElement33" cssclass="divRicMetadatiTopRight">
										<s:textbox bmodify="false" maxlenght="4" validator="ignore;minlength=2;maxlength=4;"
											label="Cod. Imp. Serv.:" name="enteimpostaservizionotifica_codiceImpostaServizio"
											text="${enteimpostaservizionotifica_codiceImpostaServizio}" bpassword="false" 
											cssclasslabel="label85 bold textright" cssclass="textareaman" />
									</s:div>
								</c:when>
								<c:otherwise>
									<%-- ACTION - INSERIMENTO --%>
									<input type="hidden" name="codop" value="${typeRequest.addScope}" />
									<s:div name="divElement11" cssclass="divRicMetadatiTopLeft">
										<s:dropdownlist name="enteimpostaservizionotifica_userCode"
											disable="${enableListaUtenti}" cssclass="textareaman"
											label="Utente:" cssclasslabel="label65 bold textright"
											cachedrowset="listaUtenti" usexml="true"
											onchange="setFiredButton('tx_button_utente_changed');this.form.submit();"
											valueselected="${enteimpostaservizionotifica_userCode}"
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
									<s:div name="divElement22" cssclass="divRicMetadatiTopCenter">
										<s:dropdownlist name="enteimpostaservizionotifica_chiaveEnte"
											disable="${enableListaEnti}" 
											cssclass="textareaman"
											cssclasslabel="label65 bold textright"
											label="Ente:" 
											cachedrowset="listaEntiGenerici" usexml="true"
											valueselected="${enteimpostaservizionotifica_chiaveEnte}"
											validator="required" showrequired="true">
											<s:ddloption text="Selezionare uno degli elementi della lista" value=""/>
											<s:ddloption text="{2}" value="{1}" />
										</s:dropdownlist>
									</s:div>
									<s:div name="divElement33" cssclass="divRicMetadatiTopRight">
										<s:textbox bmodify="true" maxlenght="4" validator="required;minlength=2;maxlength=4;"
											label="Cod. Imp. Serv.:" name="enteimpostaservizionotifica_codiceImpostaServizio"
											text="${enteimpostaservizionotifica_codiceImpostaServizio}" bpassword="false" 
											cssclasslabel="label85 bold textright" cssclass="textareaman" />
									</s:div>
								</c:otherwise>
							</c:choose>
						</s:div>
						<s:div name="divRicercaLeft2" cssclass="divRicMetadatiCenter">
							<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="enteimpostaservizionotifica_flagNotificaAllegato" disable="false" label="Flag Allegati su Notifica:"
									valueselected="${enteimpostaservizionotifica_flagNotificaAllegato}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman">
									<s:ddloption value="Y" text="Abilitato" />
									<s:ddloption value="N" text="Non Abilitato" />
								</s:dropdownlist>
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
				<s:div name="divRicercaTitleName2" cssclass="divRicTitle">Ente-Imposta Servizio Notifica</s:div>	
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
									<s:form name="indietro" action="enteImpostaServizioNotifica.do?action=search" method="post" 
											hasbtn1="false" hasbtn2="false" hasbtn3="false">
										<s:button id="tx_button_indietro" cssclass="btnStyle" type="submit" text="Indietro" onclick="" />
									</s:form>
								</s:td>
								<s:td>
									<s:form name="form_Cancella" action="enteImpostaServizioNotifica.do?action=cancel&enteimpostaservizionotifica_userCode=${enteimpostaservizionotifica_userCode}&enteimpostaservizionotifica_chiaveEnte=${enteimpostaservizionotifica_chiaveEnte}&enteimpostaservizionotifica_codiceImpostaServizio=${enteimpostaservizionotifica_codiceImpostaServizio}"
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
			<%-- ESITO VARIAZIONE --%>
			<s:form name="frmConfirm" hasbtn3="false" hasbtn2="false"
				hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
				method="post" action="enteImpostaServizioNotifica.do?action=search">
				<center>
					<c:if test="${error != null}"><s:label name="lblErrore" text="${message}"/></c:if>
					<c:if test="${error == null}"><s:label name="lblMessage" text="${message}"/></c:if>
					<s:div name="divPdf">
					<br /><br />
						<s:form name="indietro" action="enteImpostaServizioNotifica.do?action=search"
							method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
							<s:button id="tx_button_indietro" onclick="" text="Indietro"
								cssclass="btnStyle" type="submit" />
						</s:form>
					</s:div>
				</center>
			</s:form>
		</c:otherwise>
	</c:choose>
</s:div>
