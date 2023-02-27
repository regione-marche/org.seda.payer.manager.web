<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>

<m:view_state id="tipologiaServizios" encodeAttributes="true" />
<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />


<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<c:choose>

		<c:when test="${done == null && richiestacanc == null}">

			<s:div name="divRicercaTopName" cssclass="divRicercaTop">

				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
					hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
					method="post" action="tipologiaservizio.do">
					
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
					
					<s:div name="divRicMetadati" cssclass="divRicMetadati">

							<input type="hidden" name="codop"
													value="${typeRequest.editScope}" />
						<s:div name="divRicercaTitleName" cssclass="divRicTitle">Tipologia Servizio</s:div>

						<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
							<s:div name="divElement1" cssclass="divRicMetadatiLeft">
							
								<input type="hidden" name="tipologiaservizio_companyCode"
									value="<c:out value="${tipologiaservizio_companyCode}"/>" />
									
								<s:dropdownlist name="ddlSocieta"
										disable="true"
										label="Societ&agrave;:"
										cssclass="textareaman" 
										cssclasslabel="label85 bold textright"
										cachedrowset="companies" usexml="true"
										valueselected="${tipologiaservizio_companyCode}">
										<s:ddloption text="Selezionare uno degli elementi" value="" />
										<s:ddloption  value="{1}" text="{2}"/>
									</s:dropdownlist>
									
								</s:div>

							<s:div name="divElement2" cssclass="divRicMetadatiCenter">
								
								<input type="hidden"
									name="tipologiaservizio_codiceTipologiaServizio"
									value="<c:out value="${tipologiaservizio_codiceTipologiaServizio}"/>" />
								
								
								<s:textbox bmodify="false"
										maxlenght="3"
										label="Tipol. Serv.:"
										cssclass="textareaman colordisabled" 
										cssclasslabel="label85 bold textright"
										name="txtCodTipologiaServizio"
										text="${tipologiaservizio_codiceTipologiaServizio}"/>
								
								
								<!--<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
										<s:textbox bmodify="true"
										validator="required;minlength=1;maxlength=200"
										label="Path File Immagine:" showrequired="true"
										name="abilitaSistemi_pathFileImmagine"
										text="${abilitaSistemi_pathFileImmagine}"
										cssclasslabel="label85 bold textright" cssclass="textareaman" />
									</s:div>

							--></s:div>
							<s:div name="divElement3" cssclass="divRicMetadatiRight">
								
								<s:textbox bmodify="true"
									label="Descrizione:"
									cssclasslabel="label85 bold textright"
									validator="ignore;accept=${configurazione_descrizione256_regex}"
									name="tipologiaservizio_descrizioneTipologiaServizio"
									text="${tipologiaservizio_descrizioneTipologiaServizio}"
									message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"
									cssclass="textareaman" />
							</s:div>

						</s:div>

					</s:div>
					
					</c:when>
					
					
					<c:otherwise>
					
					
					<s:div name="divRicMetadati" cssclass="divRicMetadati">


						<s:div name="divRicercaTitleName" cssclass="divRicTitle">Tipologia Servizio</s:div>
						
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
							
							<s:div name="divElement1" cssclass="divRicMetadatiLeft">

							
												<input type="hidden" name="codop"
													value="${typeRequest.addScope}" />
													
									<s:dropdownlist name="tipologiaservizio_companyCode"
										disable="false"
										label="Societ&agrave;:"
										cssclass="textareaman" 
										cssclasslabel="label85 bold textright"
										cachedrowset="companies" usexml="true"
										validator="required" showrequired="true"
										valueselected="${tipologiaservizio_companyCode}">
										<s:ddloption text="Selezionare uno degli elementi" value="" />
										<s:ddloption  value="{1}" text="{2}"/>
									</s:dropdownlist>
									
							</s:div>
													
							<s:div name="divElement2" cssclass="divRicMetadatiCenter">
									
									<s:textbox bmodify="true"
										validator="required;minlength=3;accept=^[a-zA-Z]{3}$;maxlength=3"
										maxlenght="3" showrequired="true"
										label="Tipol. Serv.:"
										cssclass="textareaman" 
										cssclasslabel="label85 bold textright"
										name="tipologiaservizio_codiceTipologiaServizio"
										message="[accept=Tipol. Serv.: ${msg_configurazione_testo}]"
										text="${tipologiaservizio_codiceTipologiaServizio}"/>

							</s:div>
							
							<s:div name="divElement3" cssclass="divRicMetadatiRight">

								
								<s:textbox bmodify="true"
							validator="ignore;accept=${configurazione_descrizione256_regex}"
							label="Descrizione:"
							maxlenght="256"
							cssclasslabel="label85 bold textright"
							name="tipologiaservizio_descrizioneTipologiaServizio"
							text="${tipologiaservizio_descrizioneTipologiaServizio}"
							message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"
							cssclass="textareaman" />

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
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Tipologia servizio</s:div>
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

									<s:form name="indietro" action="tipologiaservizio.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
									</s:form>


								</s:td>
								<s:td>

									<s:form name="form_Cancella"
										action="tipologiaservizio.do?action=cancel&tipologiaservizio_companyCode=${requestScope.companyCode}&tipologiaservizio_codiceTipologiaServizio=${requestScope.codiceTipologiaServizio}"
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
				method="post" action="tipologiaservizio.do?action=search">
				<center><c:if test="${error != null}">
					<s:label name="lblErrore" text="${message}" cssclass="lblMessage" />
				</c:if> <c:if test="${error == null}">
					<s:label name="lblMessage" text="${message}" cssclass="lblMessage" />
				</c:if> <s:div name="divPdf">
				<br /><br />
					<s:form name="indietro" action="tipologiaservizio.do?action=search"
						method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
						<s:button id="tx_button_stampa_pdf" onclick="" text="Indietro"
							cssclass="btnStyle" type="submit" />
					</s:form>
				</s:div></center>
			
			</s:form>

		</c:otherwise>
	</c:choose>
</s:div>



