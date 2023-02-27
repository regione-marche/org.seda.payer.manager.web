<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>

<m:view_state id="costinotifica" encodeAttributes="true" />

<jsp:useBean id="typeRequest"
	class="com.seda.payer.commons.bean.TypeRequest" />

<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<c:choose>
		<c:when test="${done == null && richiestacanc == null}">
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Costi Notifica</s:div>

				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
					hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
					method="post" action="costinotifica.do">

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
										<input type="hidden" name="codop"
											value="${typeRequest.editScope}" />
										<input type="hidden"
											name="costiNotifica_costiNotificaCompanyCode"
											value="<c:out value="${costiNotifica_costiNotificaCompanyCode}"/>" />
										<input type="hidden"
											name="costiNotifica_costiNotificaUserCode"
											value="<c:out value="${costiNotifica_costiNotificaUserCode}"/>" />		
					       				 <s:div name="strUsers_readonly">
					 						<s:dropdownlist label="Soc/Utente:" name="ddlUsers" disable="true" cssclasslabel="label85 bold textright"
											cssclass="floatleft" cachedrowset="users" usexml="true" valueselected="${costiNotifica_costiNotificaCompanyCode}|${costiNotifica_costiNotificaUserCode}">
											<s:ddloption text="Selezionare uno degli elementi" value="" />
											<s:ddloption value="{1}|{2}" text="{7} / {4}"/>
											</s:dropdownlist>
										</s:div>
										
									</c:when>
									<c:otherwise>
										<input type="hidden" name="codop"
											value="${typeRequest.addScope}" />
										
										<s:div name="strUsers_var">
					 						<s:dropdownlist label="Soc/Utente:" name="costiNotifica_strUsers" disable="false" cssclasslabel="label85 bold textright"
					 						validator="required" showrequired="true"
											cssclass="floatleft" cachedrowset="users" usexml="true" valueselected="${costiNotifica_strUsers}">
											<s:ddloption text="Selezionare uno degli elementi" value="" />
											<s:ddloption value="{1}|{2}" text="{7} / {4}"/>
											</s:dropdownlist>
										</s:div>
									</c:otherwise>
								</c:choose>
														
						</s:div>
						
						<c:choose>
							<c:when test="${action != 'edit' && action != 'saveedit'}">	
			       				 <s:div name="divRicercaMetadatiLeft" cssclass="divRicMetadatiLeft">
									<s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
										<s:textbox bmodify="true"
											validator="ignore;accept=^[0-9]{1,4}\,[0-9]{2}?$;maxlength=7"
											maxlenght="7"
											label="Costo Postale:"
											name="costiNotifica_costiNotificaCostoPostaOrdinaria"
											text="${costiNotifica_costiNotificaCostoPostaOrdinaria}"
											message="[accept=Costo Postale: ${msg_configurazione_importo_4_2}]"
											cssclasslabel="label85 bold textright" cssclass="textareaman" />
									</s:div>
								</s:div>
								<s:div name="divRicercaMetadatiLeft1" cssclass="divRicMetadatiCenter">
									<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
										<s:textbox bmodify="true"
											validator="ignore;accept=^[0-9]{1,4}\,[0-9]{2}?$;maxlength=7"
											maxlenght="7"
											label="Costo Sms:"
											name="costiNotifica_costiNotificaCostoSms"
											text="${costiNotifica_costiNotificaCostoSms}"
											message="[accept=Costo Sms: ${msg_configurazione_importo_4_2}]"
											cssclasslabel="label85 bold textright" cssclass="textareaman" />
									</s:div>
								</s:div>
							</c:when>
							<c:otherwise>
								<s:div name="divRicercaMetadatiLeft2" cssclass="divRicMetadatiLeft">
									<s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
										<s:textbox bmodify="true"
											validator="ignore;accept=^[0-9]{1,4}\,[0-9]{2}?$;maxlength=7"
											maxlenght="7"
											label="Costo Postale:"
											name="costiNotifica_costiNotificaCostoPostaOrdinaria"
											text="${costiNotifica_costiNotificaCostoPostaOrdinaria}"
											message="[accept=Costo Postale: ${msg_configurazione_importo_4_2}]"
											cssclasslabel="label85 bold textright" cssclass="textareaman" />
									</s:div>
								</s:div>
								<s:div name="divRicercaMetadatiLeft3" cssclass="divRicMetadatiCenter">
									<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
										<s:textbox bmodify="true"
											validator="ignore;accept=^[0-9]{1,4}\,[0-9]{2}?$;maxlength=7"
											maxlenght="7"
											label="Costo Sms:"
											name="costiNotifica_costiNotificaCostoSms"
											text="${costiNotifica_costiNotificaCostoSms}"
											message="[accept=Costo Sms: ${msg_configurazione_importo_4_2}]"
											cssclasslabel="label85 bold textright" cssclass="textareaman" />
									</s:div>
								</s:div>
							</c:otherwise>
						</c:choose>			
						
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
				<s:div name="divRicercaTitleName2" cssclass="divRicTitle">Costi Notifica</s:div>

				<s:label name="lblCanc" text="${message}" cssclass="lblMessage" />
				<s:label name="lblCancmsg"
					text="Sei sicuro di voler cancellare il record selezionato?"
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
						
						<s:form name="indietro" action="costinotifica.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
						</s:form>
						</s:td>
						<s:td>
						
							<s:form name="form_Cancella"
										action="costinotifica.do?action=cancel&costiNotifica_costiNotificaCompanyCode=${costiNotifica_costiNotificaCompanyCode}&costiNotifica_costiNotificaUserCode=${costiNotifica_costiNotificaUserCode}"
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

			
				<center><c:if test="${error != null}">
					<s:label name="lblErrore" text="${message}" />
				</c:if> <c:if test="${error == null}">
					<s:label name="lblMessage" text="${message}" />
				</c:if></center>
				<br />
				<br />
				<center>
				<s:form name="indietro" action="costinotifica.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
						</s:form>
				</center>
			
		</c:otherwise>
	</c:choose>
</s:div>

