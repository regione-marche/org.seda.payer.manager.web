<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>

<m:view_state id="anagprovcom" encodeAttributes="true" />

<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	} 
</script>
<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<c:choose>

		<c:when test="${done == null && richiestacanc == null}">

			<s:div name="divRicercaTopName" cssclass="divRicercaTop">

				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
					hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
					method="post" action="anagprovcom.do">
					
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
					<input type="hidden" name="codop" value="${typeRequest.editScope}" />
					<s:div name="divRicMetadati" cssclass="divRicMetadati">
						

						<s:div name="divRicercaTitleName" cssclass="divRicTitle">Anagrafica Provincia Comune</s:div>

						<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
							<s:div name="divElement1" cssclass="divRicMetadatiLeft">
								
								<s:textbox bmodify="false"
										label="Cod. Belfiore:" name="txtCodBelfiore"
										maxlenght="4" 
										text="${anagprovcom_codiceBelfiore}"
										cssclasslabel="label85 bold textright"
										cssclass="textareaman colordisabled" />
							</s:div>
							
							<s:div name="divElement1bis" cssclass="divRicMetadatiCenter">
								<s:textbox bmodify="true"
									validator="ignore;digits;minlength=3;maxlength=3" label="Provincia:"
									name="anagprovcom_codiceProvincia"
									maxlenght="3"
									text="${requestScope.anagprovcom_codiceProvincia}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman"  />
								
								</s:div>

							<s:div name="divElement2" cssclass="divRicMetadatiRight">

								<s:textbox bmodify="true"
									validator="ignore;digits;minlength=3;maxlength=3"
									maxlenght="3"
									label="Comune:" name="anagprovcom_codiceComune"
									text="${anagprovcom_codiceComune}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
						</s:div>
							
							
						<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

							<s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
								cssclass="textareaman" validator="required;"
									maxlenght="100" showrequired="true"
									label="Desc.Comune:" name="anagprovcom_descrizioneComune"
									text="${anagprovcom_descrizioneComune}"
									message="[accept=Descr.Comune: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold textright"  />
							</s:div>
							
							<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
								cssclass="textareaman" validator="required;accept=${configurazione_descrizione_regex100}"
									maxlenght="100" showrequired="true"
									label="Descr.Prov.:" name="anagprovcom_descrizioneProvincia"
									text="${requestScope.anagprovcom_descrizioneProvincia}"
									message="[accept=Descr.Prov.: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold floatleft textright"
									/>

							</s:div>
							
							<s:div name="divElement112" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
								cssclass="textareaman" validator="ignore;"
									maxlenght="100" showrequired="true"
									label="Descr.Prov.DE:" name="anagprovcom_descrizioneProvinciaDE"
									text="${requestScope.anagprovcom_descrizioneProvinciaDE}"
									message="[accept=Descr.Prov.DE: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold floatleft textright"
									/>

							</s:div>

						</s:div>

						<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">

							<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;digits;minlength=5;maxlength=5" label="Cap:"
									maxlenght="5"
									name="anagprovcom_cap" text="${requestScope.anagprovcom_cap}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>

							<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione_regex100}"
									maxlenght="100"
									label="Descr.Regione:" name="anagprovcom_descrizioneRegione"
									text="${anagprovcom_descrizioneRegione}"
									message="[accept=Descr.Regione: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold textright"  />
							</s:div>
							
							<s:div name="divElement111" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
								cssclass="textareaman" validator="ignore;"
									maxlenght="100" showrequired="true"
									label="Desc.Comune DE:" name="anagprovcom_descrizioneComuneDE"
									text="${requestScope.anagprovcom_descrizioneComuneDE}"
									message="[accept=Desc.Comune: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold floatleft textright"
									/>

							</s:div>

						</s:div>
						
						<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">

							<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="required;minlength=2;accept=^[A-Z]{2}$;maxlength=2"
									maxlenght="2" showrequired="true"
									label="Sigla Prov.:" name="anagprovcom_siglaProvincia"
									text="${anagprovcom_siglaProvincia}"
									message="[accept=Sigla Prov.: ${msg_configurazione_testo_maiuscolo}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>

							<s:div name="divElement81" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;accept=^\w{1,4}$"
									maxlenght="4"
									label="Cod. Catast.:" name="anagprovcom_codiceCatastale"
									text="${anagprovcom_codiceCatastale}"
									message="[accept=Cod. Catast.: ${msg_configurazione_alfanumerici}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							
										<input type="hidden" name="codop"
											value="${typeRequest.editScope}" />
										<input type="hidden" name="anagprovcom_codiceBelfiore"
											value="<c:out value="${anagprovcom_codiceBelfiore}"/>" />
										<p><b><c:out value=" " /></b></p>
							
						</s:div>

					</s:div>
					
					</c:when>
					
					
					<c:otherwise>
					
					<s:div name="divRicMetadati" cssclass="divRicMetadati">
						<input type="hidden" name="codop" value="${typeRequest.addScope}" />

						<s:div name="divRicercaTitleName" cssclass="divRicTitle">Anagrafica Provincia Comune</s:div>
						
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
							<s:div name="divElement1" cssclass="divRicMetadatiLeft">
								
								<c:choose>
									<c:when test="${action == 'edit'}">
										<input type="hidden" name="codop"
											value="${typeRequest.editScope}" />
										<input type="hidden" name="anagprovcom_codiceBelfiore"
											value="<c:out value="${anagprovcom_codiceBelfiore}"/>" />
										<p><b><c:out value=" " /></b></p>
									</c:when>
									<c:otherwise>
										<input type="hidden" name="codop"
											value="${typeRequest.addScope}" />
										<s:div name="socValue_var">
											<s:textbox bmodify="true"
												validator="required;accept=^[a-zA-Z][0-9]{3}$;minlength=4;maxlength=4"
												label="Cod. Belfiore:" name="anagprovcom_codiceBelfiore"
												maxlenght="4" showrequired="true"
												text="${requestScope.anagprovcom_codiceBelfiore}"
												cssclasslabel="label85 bold textright"
												message="[accept=Cod. Belfiore: Inserire un codice Belfiore valido]"
												cssclass="textareaman" />
										</s:div>
									</c:otherwise>
								</c:choose>

							</s:div>

							<s:div name="divElement2" cssclass="divRicMetadatiCenter">

								<s:textbox bmodify="true"
									validator="ignore;digits;minlength=3;maxlength=3" label="Provincia:"
									name="anagprovcom_codiceProvincia"
									maxlenght="3"
									text="${requestScope.anagprovcom_codiceProvincia}"
									cssclasslabel="label85 bold floatleft textright"
									cssclass="textareaman" />

							</s:div>
							<s:div name="divElement10" cssclass="divRicMetadatiRight">

								
								<s:textbox bmodify="true"
									validator="ignore;digits;minlength=3;maxlength=3"
									maxlenght="3"
									label="Comune:" name="anagprovcom_codiceComune"
									text="${anagprovcom_codiceComune}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />

							</s:div>

						</s:div>

						<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

							<s:div name="divElemen11" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
								cssclass="textareaman" validator="required;"
									maxlenght="100" showrequired="true"
									label="Desc.Comune:" name="anagprovcom_descrizioneComune"
									text="${anagprovcom_descrizioneComune}"
									message="[accept=Descr.Comune: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold textright"  />
							</s:div>

							<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
								cssclass="textareaman" validator="required;accept=${configurazione_descrizione_regex100}"
									maxlenght="100" showrequired="true"
									label="Descr. Prov.:" name="anagprovcom_descrizioneProvincia"
									text="${requestScope.anagprovcom_descrizioneProvincia}"
									message="[accept=Descr. Prov.: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold floatleft textright" />
							</s:div>

						</s:div>

						<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">

							<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;digits;minlength=5;maxlength=5" label="Cap:"
									maxlenght="5"
									name="anagprovcom_cap" text="${requestScope.anagprovcom_cap}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>

							<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione_regex100}"
									maxlenght="100"
									label="Descr.Regione:" name="anagprovcom_descrizioneRegione"
									text="${anagprovcom_descrizioneRegione}"
									message="[accept=Descr.Regione: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold textright" />
							</s:div>

						</s:div>

						<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">

							<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="required;minlength=2;accept=^[A-Z]{2}$;maxlength=2"
									maxlenght="2" showrequired="true"
									label="Sigla Prov.:" name="anagprovcom_siglaProvincia"
									text="${anagprovcom_siglaProvincia}"
									message="[accept=Sigla Prov.: ${msg_configurazione_testo_maiuscolo}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>

							<s:div name="divElement82" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;accept=^\w{1,4}$"
									maxlenght="4"
									label="Cod. Catast.:" name="anagprovcom_codiceCatastale"
									text="${anagprovcom_codiceCatastale}"
									message="[accept=Cod. Catast.: ${msg_configurazione_alfanumerici}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
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
						<s:button id="tx_button_reset" type="submit" text="Reset"
							onclick="" cssclass="btnStyle" validate="false" />
						<s:button id="save_btn" type="submit" text="Salva" onclick=""
							cssclass="btnStyle" />
					</s:div>
				</s:form>
			</s:div>
		</c:when>

		<c:when test="${richiestacanc != null}">
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Anagrafica Provincia Comune</s:div>
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

									<s:form name="indietro" action="anagprovcom.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
									</s:form>


								</s:td>
								<s:td>

									<s:form name="form_Cancella"
										action="anagprovcom.do?action=cancel&anagprovcom_codiceBelfiore=${requestScope.anagprovcom_codiceBelfiore}"
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
				method="post" action="anagprovcom.do?action=search">
				<center><c:if test="${error != null}">
					<s:label name="lblErrore" text="${message}" cssclass="lblMessage" />
				</c:if> <c:if test="${error == null}">
					<s:label name="lblMessage" text="${message}" cssclass="lblMessage" />
				</c:if> <s:div name="divPdf">
				<br /><br />
					<s:form name="indietro" action="anagprovcom.do?action=search"
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



