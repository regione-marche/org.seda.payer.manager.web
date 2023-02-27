<%--  **  IMPORT JAVA TAG LIB  **  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>

<%--  **  My View State  **  --%>
<m:view_state id="gruppo" encodeAttributes="true" />

<%--  **  My TypeRequest Bean  **  --%>
<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />

<!-- ** JQuery Functions Library ** -->
<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js" type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js" type="text/javascript"></script>

<!-- ** Page Functions ** -->
<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>
<br />

<c:url value="" var="formParameters">
	<c:param name="test">ok</c:param>
</c:url>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<c:choose>
		<c:when test="${done == null && richiestacanc == null}">
			<%-- VIEW VALIDATION ERROR MESSAGES --%>
			<c:if test="${error != null}"><br/><center><s:label name="lblErrore" text="${message}"/></center><br/></c:if>
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Gruppo</s:div>
				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
						hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
						method="post" action="gruppo.do">
					<s:textbox name="fired_button_hidden" label="fired_button_hidden" bmodify="true" text="" cssclass="display_none" cssclasslabel="display_none" />
					<input type="hidden" name="datePattern" value="yyyy-MM-dd" />
					<%-- CHECK ACTION TYPE --%>
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
					<s:div name="divRicercaTitleName1" cssclass="divRicMetadati">
						<c:choose>
							<c:when test="${action == 'edit' || action == 'saveedit'}">
								<input type="hidden" name="codop" value="${typeRequest.editScope}" />
								<input type="hidden" name="gruppo_chiaveGruppo" value="<c:out value="${gruppo_chiaveGruppo}"/>" />
							</c:when>
							<c:otherwise>
								<input type="hidden" name="codop" value="${typeRequest.addScope}" /> 
							</c:otherwise>
						</c:choose>
						<s:div name="divRicercaCenterGruppo" cssclass="divRicMetadatiUnicaGruppo">
							<%-- CODICE GRUPPO --%>
							<s:div name="divElementCR0" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" name="gruppo_codiceGruppo"
									validator="required" showrequired="true"
									label="Codice Gruppo:" maxlenght="2"
									cssclasslabel="label200 bold textright"
									cssclass="textAreaGruppo"
									text="${gruppo_codiceGruppo}" />
							</s:div>
							<%-- Descrizione Lingua Italiana--%>
							<s:div name="divElementCR1" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" name="gruppo_descrizioneLinguaItaliana"
									validator="required;" showrequired="true"
									label="Descrizione Lingua Italiana:" maxlenght="100"
									cssclasslabel="label200 bold textright"
									cssclass="textAreaGruppo"
									text="${gruppo_descrizioneLinguaItaliana}" />
							</s:div>
							<%-- Descrizione Altra Lingua Italiana--%>
							<s:div name="divElementCR2" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" name="gruppo_descrizioneAltraLingua"
									validator="required;" showrequired="true"
									label="Descrizione Lingua Francese:" maxlenght="100"
									cssclasslabel="label200 bold textright"
									cssclass="textAreaGruppo"
									text="${gruppo_descrizioneAltraLingua}" />
							</s:div>
						</s:div>
						<s:div name="button_container_var" cssclass="divRicBottoniGruppo">
							<s:button id="tx_button_indietro" type="submit" text="Indietro" onclick="" cssclass="btnStyle" validate="false" />
							<s:button id="tx_button_reset" onclick="" text="Reset" type="submit" cssclass="btnStyle" validate="false"/>
							<s:button id="save_btn" type="submit" text="Salva" onclick="" cssclass="btnStyle" />
						</s:div>
					</s:div>
				</s:form>
			</s:div>
		</c:when>

		<%-- ACTION - RICHIESTA CANCELLAZIONE --%>
		<c:when test="${richiestacanc != null}">
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">
				<s:div name="divRicercaTitleName2" cssclass="divRicTitle">Gruppo</s:div>	
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
									<s:form name="indietro" action="gruppo.do?action=search" method="post" 
											hasbtn1="false" hasbtn2="false" hasbtn3="false">
										<s:button id="tx_button_indietro" cssclass="btnStyle" type="submit" text="Indietro" onclick="" />
									</s:form>
								</s:td>
								<s:td>
									<s:form name="form_Cancella" action="gruppo.do?action=cancel&gruppo_chiaveGruppo=${gruppo_chiaveGruppo}"
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
				method="post" action="gruppo.do?action=search">
				<center>
					<c:if test="${error != null}"><s:label name="lblErrore" text="${message}"/></c:if>
					<c:if test="${error == null}"><s:label name="lblMessage" text="${message}"/></c:if>
					<s:div name="divPdf">
					<br /><br />
						<s:form name="indietro" action="gruppo.do?action=search"
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