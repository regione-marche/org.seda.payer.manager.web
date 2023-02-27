<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>

<m:view_state id="searchconfigbancadati" encodeAttributes="true" />

<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />

<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
<c:choose>
	<c:when test="${done == null && richiestacanc == null}">	
		<s:div name="divRicercaTopName" cssclass="divRicercaTop">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle">Banca Dati</s:div>

			<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
				hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
				method="post" action="bancadati.do">

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
									<input type="hidden" name="configbancadati_cuteCute"	value="<c:out value="${configbancadati_cuteCute}"/>" />
									<input type="hidden" name="configbancadati_idBancaDati"	value="<c:out value="${configbancadati_idBancaDati}"/>" />
									<input type="hidden" name="configbancadati_companyCode"	value="<c:out value="${configbancadati_companyCode}"/>" />
									
									<s:dropdownlist label="Societ&agrave;/Utente:" name="ddlSocUteEnt" 
							 						   disable="true"
													   cachedrowset="entetiposervizios" usexml="true" 
													   valueselected="${configbancadati_companyCode}|${configbancadati_cuteCute}" 
													   cssclasslabel="label160 bold textright"
													   cssclass="seda-ui-ddl tbddlMax780 floatleft" >
										<s:ddloption value="{1}|{2}" text="{7} / {4}"/>
									</s:dropdownlist>
																												
								</c:when>
								<c:otherwise>
									<input type="hidden" name="codop" value="${typeRequest.addScope}" /> 
									<s:dropdownlist label="Societ&agrave;/Utente:" name="configbancadati_strEntetiposervizios" 
							 						   disable="${action == 'edit' || action == 'saveedit'}"
													   cachedrowset="entetiposervizios" usexml="true" 
													   valueselected="${configbancadati_strEntetiposervizios}" 
													   cssclasslabel="label160 bold textright"
													   validator="required"
													   cssclass="seda-ui-ddl tbddlMax780 floatleft" showrequired="true" >
										<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
										<s:ddloption value="{1}|{2}" text="{7} / {4} "/>
									</s:dropdownlist>
									</c:otherwise>
							</c:choose>						

					</s:div>
					
					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
						<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true"  showrequired="true" 
									validator="required;maxlength=256"
									label="Nome Banca Dati:" name="configbancadati_name"
									text="${configbancadati_name}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
						</s:div>
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
										validator="ignore;url;maxlength=256"					
										label="Username:" name="configbancadati_username"
										text="${configbancadati_username}"
										cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
					</s:div>
					
					<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
						<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true"  showrequired="true"
									validator="required;url;maxlength=256"
									label="Url:" name="configbancadati_url"
									text="${configbancadati_url}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
						</s:div>
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true" maxlenght="20" validator="ignore;minlength=6;maxlength=20;"
										label="Password:" name="configbancadati_password"
										text="${configbancadati_password}" bpassword="true" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" />
								</s:div>
					</s:div>
						
						<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
							<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Tipo Integrazione:" name="configbancadati_tipoIntegrazione" 
							 						   disable="false"
													   valueselected="${configbancadati_tipoIntegrazione}" 
													   cssclasslabel="label85 bold textright"
													   cssclass="seda-ui-ddl textareaman floatleft" >
										<s:ddloption value="I" text="IMMEDIATA"/>s
										<s:ddloption value="D" text="DIFFERITA"/>
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
	<s:div name="divRicercaTitleName2" cssclass="divRicTitle">Banca Dati</s:div>	

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
						
						<s:form name="indietro" action="bancadati.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
						</s:form>
						</s:td>
						<s:td>
						
							<s:form name="form_Cancella"
										action="bancadati.do?action=cancel&configbancadati_cuteCute=${configbancadati_cuteCute}&configbancadati_idBancaDati=${configbancadati_idBancaDati}"
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
			method="post" action="bancadati.do?action=search">
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