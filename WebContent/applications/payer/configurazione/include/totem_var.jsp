<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>

<m:view_state id="totem" encodeAttributes="true" />

<jsp:useBean id="typeRequest"
	class="com.seda.payer.commons.bean.TypeRequest" />


<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<c:choose>
		<c:when test="${done == null && richiestacanc == null}">
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">
				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
					hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
					method="post" action="totem.do">

					<c:choose>
						<c:when test="${requestScope.action=='saveedit'}">
							<input type="hidden" name="action"
								value="<c:out value="${requestScope.action}"/>" />
						</c:when>
						<c:when test="${requestScope.action=='saveadd'}">
							<input type="hidden" name="action"
								value="<c:out value="${requestScope.action}"/>" />
						</c:when>
						<c:otherwise>
							<input type="hidden" name="action"
								value="save<c:out value="${requestScope.action}"/>" />
						</c:otherwise>
					</c:choose>

					<c:choose>
						<c:when test="${action == 'edit'|| action == 'saveedit'}">
							<input type="hidden" name="codop"
								value="${typeRequest.editScope}" />
							<s:div name="divRicMetadati" cssclass="divRicMetadati">
								<s:div name="divRicercaTitleName" cssclass="divRicTitle">Totem tipologia imposta</s:div>
								<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
									<s:div name="divElement1" cssclass="divRicMetadatiLeft">
										<!-- validator -->
										<s:textbox bmodify="false"
											validator="required;minlength=6;accept=^[a-zA-Z0-9]{6}$;maxlength=6"
											label="Codice Ente:" maxlenght="6" showrequired="true"
											name="codice_ente" text="${requestScope.codice_ente}"
											cssclass="textareaman colordisabled"
											message="[accept=Codice Ente: ${msg_configurazione_codice_ente}"
											cssclasslabel="label85 bold floatleft textright" />
									</s:div>
									<s:div name="divElement2" cssclass="divRicMetadatiLeft">
										<s:textbox bmodify="false"
											validator="required;minlength=2;accept=^[0-9]{2,4}$;maxlength=4"
											label="Imposta Servizio" maxlenght="4" showrequired="true"
											name="imposta_servizio"
											text="${requestScope.imposta_servizio}"
											cssclass="textareaman colordisabled"
											message="[accept=Imposta Servizio: ${msg_configurazione_imposta_servizio}]"
											cssclasslabel="label85 bold floatleft textright" />
									</s:div>
								</s:div>

								<s:div name="divElement2" cssclass="divRicMetadatiCenter">
									<s:dropdownlist label="Tipologia Imposta:"
										cssclasslabel="label85 bold textright" cssclass="textareaman"
										name="tipologia_imposta" disable="false" valueselected="">
										<s:ddloption value="" text="" />
										<s:ddloption value="01" text="01 - TARSU" />
										<s:ddloption value="02" text="02 - COSAP" />
										<s:ddloption value="03" text="03 - CIMP" />
										<s:ddloption value="04" text="04 - INGIUNZIONI" />
										<s:ddloption value="05" text="05 - VERBALI_CDS" />
										<s:ddloption value="06" text="06 - SANZIONI_CDS" />
										<s:ddloption value="07" text="07 - ICI_VIOLAZIONE" />
										<s:ddloption value="08" text="08 - BORSELLINO" />
										<s:ddloption value="09" text="09 - BOLLO" />
									</s:dropdownlist>
								</s:div>
							</s:div>

						</c:when>

						<c:otherwise>
							<input type="hidden" name="codop" value="${typeRequest.addScope}" />
							<s:div name="divRicMetadati" cssclass="divRicMetadati">
								<s:div name="divRicercaTitleName" cssclass="divRicTitle">Totem tipologia imposta</s:div>

								<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
									<s:div name="divElement12" cssclass="divRicMetadatiLeft">
										<s:textbox bmodify="true"
											validator="required;minlength=6;accept=^[a-zA-Z0-9\*]{6}$;maxlength=6"
											label="Codice ente:" maxlenght="6" showrequired="true"
											name="codice_ente" text="${requestScope.codice_ente}"
											cssclass="textareaman"
											message="[accept=Codice Ente: ${msg_configurazione_codice_ente}]"
											cssclasslabel="label85 bold floatleft textright" />
									</s:div>
									<s:div name="divElement_" cssclass="divRicMetadatiLeft">
										<s:textbox bmodify="true"
											validator="required;minlength=2;accept=^[0-9\*]{2,4}$;maxlength=4"
											label="Imposta Servizio:" maxlenght="4" showrequired="true"
											name="imposta_servizio"
											text="${requestScope.imposta_servizio}"
											cssclass="textareaman"
											message="[accept=Imposta Servizio: ${msg_configurazione_imposta_servizio}]"
											cssclasslabel="label85 bold floatleft textright" />
									</s:div>
								</s:div>

								<s:div name="divElement3" cssclass="divRicMetadatiCenter">
									<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
										<s:dropdownlist label="Tipologia imposta:"
											cssclasslabel="label85 bold textright" cssclass="textareaman"
											name="tipologia_imposta" disable="false" valueselected="">
											<s:ddloption value="" text="" />
											<s:ddloption value="01" text="01 - TARSU" />
											<s:ddloption value="02" text="02 - COSAP" />
											<s:ddloption value="03" text="03 - CIMP" />
											<s:ddloption value="04" text="04 - INGIUNZIONI" />
											<s:ddloption value="05" text="05 - VERBALI_CDS" />
											<s:ddloption value="06" text="06 - SANZIONI_CDS" />
											<s:ddloption value="07" text="07 - ICI_VIOLAZIONE" />
											<s:ddloption value="08" text="08 - BORSELLINO" />
											<s:ddloption value="09" text="09 - BOLLO" />
										</s:dropdownlist>
									</s:div>
								</s:div>
								<s:div name="divRicercaLeft" cssclass="divRicMetadatiRight">
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
							type="submit" cssclass="btnStyle" validate="false" />
						<s:button id="save_btn" type="submit" text="Salva" onclick=""
							cssclass="btnStyle" />
					</s:div>

				</s:form>
			</s:div>
		</c:when>

		<c:when test="${richiestacanc != null}">
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Totem tipologia imposta</s:div>
				<center>
					<s:label name="lblCanc" text="${message}" cssclass="lblMessage" />
					<s:label name="lblCancmsg"
						text="Sei sicuro di voler cancellare il record selezionato?"
						cssclass="lblMessage" />
				</center>

				<br />
				<br />

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

									<s:form name="indietro" action="totem.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
									</s:form>


								</s:td>
								<s:td>

									<s:form name="form_Cancella"
										action="totem.do?action=cancel&codice_ente=${requestScope.codice_ente}&imposta_servizio=${requestScope.imposta_servizio}"
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
				method="post" action="totem.do?action=search">
				<center>
					<c:if test="${error != null}">
						<s:label name="lblErrore" text="${message}" cssclass="lblMessage" />
					</c:if>
					<c:if test="${error == null}">
						<s:label name="lblMessage" text="${message}" cssclass="lblMessage" />
					</c:if>
					<s:div name="divPdf">
						<br />
						<br />
						<s:form name="indietro" action="totem.do?action=search"
							method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
							<s:button id="tx_button_stampa_pdf" onclick="" text="Indietro"
								cssclass="btnStyle" type="submit" />
						</s:form>
					</s:div>
				</center>
			</s:form>

		</c:otherwise>
	</c:choose>
</s:div>

