<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>

<m:view_state id="bollettino" encodeAttributes="true"/>

<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />


<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<c:choose>

		<c:when test="${done == null && richiestacanc == null}">

			<s:div name="divRicercaTopName" cssclass="divRicercaTop">

				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
					hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
					method="post" action="bollettino.do">
					
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


								<s:div name="divRicercaTitleName" cssclass="divRicTitle">Tipo Bollettino</s:div>
								
								<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
									<s:div name="divElement1" cssclass="divRicMetadatiLeft">						
							
									
											<s:textbox bmodify="false"
												validator="required;minlength=4;accept=^[a-zA-Z]{4}$;maxlength=4" label="Tipo:"
												maxlenght="4" showrequired="true"
												name="typeBoll"
												text="${requestScope.bollettino_bollettinoType}"
												cssclass="textareaman colordisabled" 
												message="[accept=Tipo: ${msg_configurazione_testo}]"
												cssclasslabel="label85 bold floatleft textright" />
										
									</s:div>
									

								</s:div>

								<s:div name="divElement11" cssclass="divRicMetadatiLeft">
										<s:textbox bmodify="true"
											cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
											maxlenght="256" label="Descrizione:"
											name="bollettino_bollettinoDescription"
											text="${requestScope.bollettino_bollettinoDescription}"
											message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"
											cssclasslabel="label85 bold textright"  />
									</s:div>

									<s:div name="divElement2" cssclass="divRicMetadatiCenter">
										<s:dropdownlist label="Compilazione:"
											cssclasslabel="label85 bold textright" cssclass="textareaman"
											name="bollettino_bollettinoTypeComp" disable="false"
											valueselected="${requestScope.bollettino_bollettinoTypeComp}">
											<s:ddloption value="A" text="Automatica" />
											<s:ddloption value="M" text="Manuale" />
										</s:dropdownlist>
									</s:div>
									
									<s:div name="divElement3" cssclass="divRicMetadatiRight">

										<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
											<s:textbox bmodify="true"
												validator="required;minlength=3;accept=^[a-zA-Z]{3};maxlength=3" maxlenght="3"
												label="Tipo Flusso:" name="bollettino_bollettinoTypeFlow"
												text="${requestScope.bollettino_bollettinoTypeFlow}"
												cssclasslabel="label85 bold textright" showrequired="true"
												message="[accept=Tipo Flusso: ${msg_configurazione_testo}]"
												cssclass="textareaman" />
										</s:div>
										
										<input type="hidden" name="bollettino_bollettinoType"
										value="<c:out value="${bollettino_bollettinoType}"/>" />
									
									</s:div>

							</s:div>

						</c:when>


						<c:otherwise>
						<input type="hidden" name="codop" value="${typeRequest.addScope}" />
						<s:div name="divRicMetadati" cssclass="divRicMetadati">
						<s:div name="divRicercaTitleName" cssclass="divRicTitle">Tipo Bollettino</s:div>
						
							<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
								<s:div name="divElement12" cssclass="divRicMetadatiLeft">						
							
									
										<s:textbox bmodify="true"
											validator="required;minlength=4;accept=^[a-zA-Z]{4}$;maxlength=4" label="Tipo:"
											maxlenght="4" showrequired="true"
											name="bollettino_bollettinoType"
											text="${requestScope.bollettino_bollettinoType}"
											cssclass="textareaman" 
											message="[accept=Tipo: ${msg_configurazione_testo}]"
											cssclasslabel="label85 bold floatleft textright" />
									
								</s:div>

						</s:div>
						
						<s:div name="divElement2" cssclass="divRicMetadatiLeft">
								<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
											maxlenght="256"
											label="Descrizione:" name="bollettino_bollettinoDescription"
											text="${requestScope.bollettino_bollettinoDescription}"
											message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold textright"
									 />
								</s:div>

							</s:div>
							<s:div name="divElement3" cssclass="divRicMetadatiCenter">

								<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
									<s:dropdownlist label="Compilazione:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="bollettino_bollettinoTypeComp" disable="false"  
								    	valueselected="${requestScope.bollettino_bollettinoTypeComp}">
										<s:ddloption value="A" text="Automatica"/>
										<s:ddloption value="M" text="Manuale"/>
										</s:dropdownlist>
								</s:div>

							</s:div>
						<s:div name="divRicercaLeft" cssclass="divRicMetadatiRight">
							<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
										validator="required;minlength=3;accept=^[a-zA-Z]{3};maxlength=3"
										maxlenght="3" showrequired="true"
										label="Tipo Flusso:" name="bollettino_bollettinoTypeFlow"
										text="${requestScope.bollettino_bollettinoTypeFlow}"
										cssclass="textareaman" 
										message="[accept=Tipo Flusso: ${msg_configurazione_testo}]"
										cssclasslabel="label85 bold textright"  />
										
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
					<!--<s:textbox name="fired_button_hidden" label="fired_button_hidden"
											bmodify="true" text="" cssclass="rend_display_none"
											cssclasslabel="rend_display_none" />
											
										
										<s:div name="tx_button_indietro" cssclass="divRicBottoni">
										<s:button id="tx_button_indietro" type="submit" text="New Ind" onclick=""
											cssclass="btnStyle" />	
									</s:div>-->


				</s:form>


				<!--<s:div name="div_stampa_down" cssclass="div_align_center divRisultati divRicBottoniNoJs">
					<s:form name="indietro"
						action="anagprovcom.do?action=search" method="post"
						hasbtn1="false" hasbtn2="false" hasbtn3="false">
						<s:button id="tx_button_stampa_pdf" onclick="" text="Indietro" cssclass="btnStyle"
							type="submit" />
					</s:form>
				</s:div>
			
		
		-->
			</s:div>
		</c:when>

		<c:when test="${richiestacanc != null}">
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Tipo Bollettino</s:div>
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

									<s:form name="indietro" action="bollettino.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
									</s:form>


								</s:td>
								<s:td>

									<s:form name="form_Cancella"
										action="bollettino.do?action=cancel&bollettino_bollettinoType=${requestScope.bollettinoType}"
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

				<!--<s:div name="button_container_var" cssclass="divRicBottoni">
				
									<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
					
					
						<s:button id="tx_button_indietro" type="submit" text="Indietro" onclick="" 
											cssclass="btnStyle" validate="false" />
											
										<s:button id="save_btn" type="submit" text="Salva" onclick=""
											cssclass="btnStyle" />
									</s:div>
				
								-->
				<!--<s:hyperlink name="cancButton"
									imagesrc="../applications/templates/configurazione/img/canc_icon.gif"
									href="/manager/configurazione/anagprovcom.do?action=cancel&anagprovcom_codiceBelfiore=${requestScope.anagprovcom_codiceBelfiore}"
									text="" cssclass="image_hyperlink" />
									
									<s:form name="canc"
												action="anagprovcom.do?action=cancel&anagprovcom_codiceBelfiore=${requestScope.anagprovcom_codiceBelfiore}"
												method="post" hasbtn1="false" hasbtn2="false"
												hasbtn3="false">

												<s:button id="button_indietro" cssclass="btnStyle"
													type="submit" text="Canc" onclick="" />
									</s:form>-->


				<!--<s:div name="divCentered" cssclass="divCenteredButtons">
					<s:form name="canc"
						action="anagprovcom.do?action=cancel&anagprovcom_codiceBelfiore=${requestScope.anagprovcom_codiceBelfiore}"
						 method="post"
						hasbtn1="false" hasbtn2="false" hasbtn3="false">
						<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
						<s:button id="tx_button_indietro" type="submit" text="Back" onclick="" 
											cssclass="btnStyle" />
						<s:button id="canc" onclick="" text="Canc" cssclass="btnStyle"
							type="submit" />
					</s:form>
				
			</s:div>
		
								-->
				<!--<s:hyperlink name="backButton2"
									imagesrc="../applications/templates/configurazione/img/back_icon.gif"
									href="/manager/configurazione/anagprovcom.do" text=""
									cssclass="image_hyperlink" />
							-->

			</s:div>

		</c:when>
		<c:otherwise>

			<s:form name="frmConfirm" hasbtn3="false" hasbtn2="false"
				hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
				method="post" action="bollettino.do?action=search">
				<center><c:if test="${error != null}">
					<s:label name="lblErrore" text="${message}" cssclass="lblMessage" />
				</c:if> <c:if test="${error == null}">
					<s:label name="lblMessage" text="${message}" cssclass="lblMessage" />
				</c:if> <s:div name="divPdf">
				<br /><br />
					<s:form name="indietro" action="bollettino.do?action=search"
						method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
						<s:button id="tx_button_stampa_pdf" onclick="" text="Indietro"
							cssclass="btnStyle" type="submit" />
					</s:form>
				</s:div></center>
				<!--<s:hyperlink name="backButton2"
					imagesrc="../applications/templates/configurazione/img/back_icon.gif"
					href="anagprovcom.do?action=search" text=""
					cssclass="image_hyperlink" />-->
			</s:form>

		</c:otherwise>
	</c:choose>
</s:div>



