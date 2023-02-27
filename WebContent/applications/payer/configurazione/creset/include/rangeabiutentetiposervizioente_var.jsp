<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>
<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />
<m:view_state id="rangeabiutentetiposervizioentes" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<c:choose>

		<c:when test="${done == null && richiestacanc == null}">

			<s:div name="divRicercaTopName" cssclass="divRicercaTop">

				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
					hasbtn1="false" btn1text="..."
					method="post" action="rangeabiutentetiposervizioente.do">
					
					<input type="hidden" name="rangeabiutentetiposervizioente_chiaveRange" value="<c:out value="${rangeabiutentetiposervizioente_chiaveRange}"/>"/>
					
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
					<input type="hidden" name="prova"
									value=1 />
					<s:div name="divRicMetadati" cssclass="divRicMetadati">
						

						<s:div name="divRicercaTitleName" cssclass="divRicTitle">Range Utente - Tipologia Servizio - Ente</s:div>

						<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
							<s:dropdownlist name="ddlSocUteEnte" disable="true" 
								label="Soc./Ute./Ente/Tipol.Serv.:"
										cssclasslabel="label165 bold textright floatleft"
										cssclass="seda-ui-ddl tbddlMax780 floatleft"
										validator="required" showrequired="true"
										   cachedrowset="configutentetiposervizioentes" usexml="true" 
										   valueselected="${rangeabiutentetiposervizioente_companyCode}|${rangeabiutentetiposervizioente_codiceUtente}|${rangeabiutentetiposervizioente_chiaveEnte}|${rangeabiutentetiposervizioente_codiceTipologiaServizio}">
								<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
								<s:ddloption value="{1}|{2}|{3}|{4}" text="{16} / {17} / {15} / {18}"/>
							</s:dropdownlist>
							
						</s:div>

						<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
							
							<s:div name="divElement51" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="required;digits" label="Iniz. Rg Da:"
									maxlenght="20" showrequired="true"
									name="rangeabiutentetiposervizioente_inizioRangeDa"
									text="${requestScope.rangeabiutentetiposervizioente_inizioRangeDa}"
									cssclasslabel="label85 bold floatleft textright"
									cssclass="textareaman" />
									
							</s:div>
							
							<s:div name="divElement510" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;digits"
									maxlenght="20"
									label="Iniz. Rg Per:" name="rangeabiutentetiposervizioente_inizioRangePer"
									text="${requestScope.rangeabiutentetiposervizioente_inizioRangePer}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							

						</s:div>
						
						<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
							
							<s:div name="divElement511" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="required;digits" showrequired="true"
									maxlenght="20"
									label="Fine Range:" name="rangeabiutentetiposervizioente_fineRangeA"
									text="${requestScope.rangeabiutentetiposervizioente_fineRangeA}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							
							<s:div name="divElement512" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Tipo Range:" name="rangeabiutentetiposervizioente_tipoRange" disable="false"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman" valueselected="${rangeabiutentetiposervizioente_tipoRange}">
									<s:ddloption value="B" text="Bollettino" />
									<s:ddloption value="D" text="Documento" />
									<s:ddloption value="V" text="Verbale" />
								</s:dropdownlist>
							</s:div>
							
						</s:div>
						
						<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
							
							<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Flag Cin:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="rangeabiutentetiposervizioente_flagCin" disable="false"  
								    	valueselected="${requestScope.rangeabiutentetiposervizioente_flagCin}">
										<s:ddloption value="Y" text="Si"/>
										<s:ddloption value="N" text="No"/>
										</s:dropdownlist>
							</s:div>
							<s:div name="divElement71" cssclass="divRicMetadatiSingleRow">
								<input type="hidden" name="codop" value="${typeRequest.editScope}" />
                    			<input type="hidden" name="rangeabiutentetiposervizioente_companyCode" value="${rangeabiutentetiposervizioente_companyCode}"/>
								<input type="hidden" name="rangeabiutentetiposervizioente_codiceUtente" value="<c:out value="${rangeabiutentetiposervizioente_codiceUtente}"/>"/>
								<input type="hidden" name="rangeabiutentetiposervizioente_chiaveEnte" value="<c:out value="${rangeabiutentetiposervizioente_chiaveEnte}"/>"/>
								<input type="hidden" name="rangeabiutentetiposervizioente_codiceTipologiaServizio" value="<c:out value="${rangeabiutentetiposervizioente_codiceTipologiaServizio}"/>"/>
								<p><b><c:out value=""/></b></p>
							</s:div>
							
						</s:div>

						
					</s:div>
					
					</c:when>
					
					
					<c:otherwise>
					<input type="hidden" name="prova"
									value=0 />
									<input type="hidden" name="codop"
									value="${typeRequest.addScope}" />
					<s:div name="divRicMetadati" cssclass="divRicMetadati">


						<s:div name="divRicercaTitleName" cssclass="divRicTitle">Range Utente - Tipologia Servizio - Ente</s:div>
						
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
					
					

					<s:div name="top" >
					
						<s:dropdownlist name="rangeabiutentetiposervizioente_strConfigUtenteTipoServizioEntes" disable="false" 
								label="Soc./Ute./Ente/Tipol.Serv.:"
										cssclasslabel="label165 bold textright floatleft"
										cssclass="seda-ui-ddl tbddlMax780 floatleft"
										validator="required" showrequired="true"
										   cachedrowset="configutentetiposervizioentes" usexml="true" valueselected="${rangeabiutentetiposervizioente_companyCode}|${rangeabiutentetiposervizioente_codiceUtente}|${rangeabiutentetiposervizioente_chiaveEnte}|${rangeabiutentetiposervizioente_codiceTipologiaServizio}">
								<s:ddloption text="Selezionare uno degli elementi della lista" value="" />		   
								<s:ddloption value="{1}|{2}|{3}|{4}" text="{16} / {17} / {15} / {18}"/>
							</s:dropdownlist>
				 	
				 	
				 		<!--<s:dropdownlist label="" name="ente_selected" disable="false" cssclasslabel="label85 bold"
								cssclass=""
										   cachedrowset="entes_add" usexml="true" valueselected="">
								<s:ddloption value="{1}|{2}|{3}" text="{6} / {4} / {5} "/> 	
						</s:dropdownlist>
						
						--><!--<s:label name="SUE" text="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"  cssclass="label185 bold"/>
						--></s:div>
				
					
					
					
							<!--<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						
								<s:dropdownlist label="Chiave:" name="rangeabiutentetiposervizioente_strConfigUtenteTipoServizioEntes" disable="false" cssclass="textareaman" cssclasslabel="label85 bold floatleft textright"
										   cachedrowset="configutentetiposervizioentes" usexml="true" valueselected="">
								<s:ddloption value="{1}|{2}|{3}|{4}" text="{1} / {2} / {15} / {4}"/>
						</s:dropdownlist>
								

							</s:div>

							--><!--<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">

								<s:textbox bmodify="true" showrequired="true"
									validator="required;minlength=1;maxlength=20" label="Iniz. Rg Da:"
									name="rangeabiutentetiposervizioente_inizioRangeDa"
									text="${requestScope.rangeabiutentetiposervizioente_inizioRangeDa}"
									cssclasslabel="label85 bold floatleft textright"
									cssclass="textareaman" />

							</s:div>
							--><!--<s:div name="divElement3" cssclass="divRicMetadatiTopRight">

								
								<s:textbox bmodify="true" showrequired="true"
									validator="required;minlength=1;maxlength=20"
									label="Fine Rg:" name="rangeabiutentetiposervizioente_fineRangeA"
									text="${requestScope.rangeabiutentetiposervizioente_fineRangeA}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />

							</s:div>

						--></s:div>

						<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
							
								<s:div name="divElement43" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="required;digits" showrequired="true"
									maxlenght="20" label="Iniz. Rg Da:"
									name="rangeabiutentetiposervizioente_inizioRangeDa"
									text="${requestScope.rangeabiutentetiposervizioente_inizioRangeDa}"
									cssclasslabel="label85 bold floatleft textright"
									cssclass="textareaman" />
							</s:div>
								
							<s:div name="divElement41" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;digits"
									maxlenght="20"
									label="Iniz. Rg Per:" name="rangeabiutentetiposervizioente_inizioRangePer"
									text="${requestScope.rangeabiutentetiposervizioente_inizioRangePer}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							
							<!--<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
								
							</s:div>

						--></s:div>

						<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
							<s:div name="divElement513" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="required;digits" showrequired="true"
									maxlenght="20"
									label="Fine Range:" name="rangeabiutentetiposervizioente_fineRangeA"
									text="${requestScope.rangeabiutentetiposervizioente_fineRangeA}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
								
								
							</s:div>
							
							<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
								
								<s:dropdownlist label="Tipo Range:" name="rangeabiutentetiposervizioente_tipoRange" disable="false"
				cssclasslabel="label85 bold textright"
				cssclass="textareaman" valueselected="${rangeabiutentetiposervizioente_tipoRange}">
									<s:ddloption value="B" text="Bollettino" />
									<s:ddloption value="D" text="Documento" />
									<s:ddloption value="V" text="Verbale" />
				</s:dropdownlist>
								
								
							</s:div>
							
							
						</s:div>

						<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
							<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								
								
								
								<s:dropdownlist label="Flag Cin:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="rangeabiutentetiposervizioente_flagCin" disable="false"  
								    	valueselected="${requestScope.rangeabiutentetiposervizioente_flagCin}">
										<s:ddloption value="Y" text="Si"/>
										<s:ddloption value="N" text="No"/>
										</s:dropdownlist>
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
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Range Utente - Tipologia Servizio - Ente</s:div>
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

									<s:form name="indietro" action="rangeabiutentetiposervizioente.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
									</s:form>


								</s:td>
								<s:td>

									<s:form name="form_Cancella"
										action="rangeabiutentetiposervizioente.do?action=cancel&rangeabiutentetiposervizioente_chiaveRange=${requestScope.chiaveRange}"
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
				method="post" action="rangeabiutentetiposervizioente.do?action=search">
				<center><c:if test="${error != null}">
					<s:label name="lblErrore" text="${message}" cssclass="lblMessage" />
				</c:if> <c:if test="${error == null}">
					<s:label name="lblMessage" text="${message}" cssclass="lblMessage" />
				</c:if> <s:div name="divPdf">
				<br /><br />
					<s:form name="indietro" action="rangeabiutentetiposervizioente.do?action=search"
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



