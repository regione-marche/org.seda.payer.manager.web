<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>

<m:view_state id="companies" encodeAttributes="true" />

<jsp:useBean id="typeRequest"
	class="com.seda.payer.commons.bean.TypeRequest" />

<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
<c:choose>
	<c:when test="${done == null && richiestacanc == null}">	
		<s:div name="divRicercaTopName" cssclass="divRicercaTop">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle">Societ&agrave;</s:div>

			<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
				hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
				method="post" action="company.do">

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
						<s:div name="divElement1" cssclass="divRicMetadatiLeft">
							<c:choose>
								<c:when test="${action == 'edit' || action == 'saveedit'}">
									<input type="hidden" name="codop" value="${typeRequest.editScope}" />									
									<input type="hidden" name="company_companyCode"
										value="<c:out value="${company_companyCode}"/>" />
									<s:textbox bmodify="false"
											label="Societ&agrave;:"
											maxlenght="5"
											name="txtSoc"
											text="${company_companyCode}"
											cssclasslabel="label85 bold textright" 
											cssclass="textareaman colordisabled"/>
								</c:when>
								<c:otherwise>
									<input type="hidden" name="codop" value="${typeRequest.addScope}" /> 
									<s:div name="socValue_var">
										<s:textbox bmodify="true"
											validator="required;minlength=5;accept=^[0-9A-Z]{5}$;maxlength=5" label="Societ&agrave;:"
											maxlenght="5" showrequired="true"
											name="company_companyCode"
											text="${company_companyCode}"
											message="[accept=Societ&agrave;: ${msg_configurazione_alfanumerici_maiuscolo}]"
											cssclasslabel="label85 bold textright" cssclass="textareaman"/>
									</s:div>
								</c:otherwise>
							</c:choose>
						</s:div>

						<s:div name="divElement2" cssclass="divRicMetadatiCenter">
							<s:textbox bmodify="true"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								label="Descrizione:" 
								maxlenght="256"
								name="company_companyDescription"
								text="${company_companyDescription}"
								message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"
								cssclasslabel="label85 bold textright"  />
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
					<!--<s:div name="button_container_var">
						<center><s:table border="0" cssclass="container_btn">
							<s:thead>
								<s:tr />
							</s:thead>
							<s:tbody>
								<s:tr>
									<s:td>
										<s:button id="save_btn" type="submit" text="Salva" onclick="" cssclass="btnStyle"/>
									</s:td>
									<s:td>									
										<s:hyperlink name="backButton"
										imagesrc="../applications/templates/configurazione/img/back_icon.gif"
												href="company.do?action=search" text="" cssclass="hlStyle" />
									</s:td>
								</s:tr>
							</s:tbody>
						</s:table></center>
					</s:div>
				--></s:div>
			</s:form>
		</s:div>
	</c:when>
	<c:when test="${richiestacanc != null}">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:div name="divRicercaTitleName2" cssclass="divRicTitle">Societ&agrave;</s:div>	

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
						
						<s:form name="indietro" action="company.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
						</s:form>
						</s:td>
						<s:td>
						
							<s:form name="form_Cancella"
										action="company.do?action=cancel&company_companyCode=${company_companyCode}"
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
			method="post" action="company.do?action=search">
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

