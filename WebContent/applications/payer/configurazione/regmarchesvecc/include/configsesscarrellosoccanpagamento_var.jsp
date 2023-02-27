<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>

<m:view_state id="configsesscarrellosoccanpagamento" encodeAttributes="true" />

<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<c:choose>

		<c:when test="${done == null && richiestacanc == null}">

			<s:div name="divRicercaTopName" cssclass="divRicercaTop">

				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
					hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
					method="post" action="configsesscarrellosoccanpagamento.do">
					
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
									value=0 />
					<s:div name="divRicMetadati" cssclass="divRicMetadati">


						<s:div name="divRicercaTitleName" cssclass="divRicTitle">Sessione Carrello</s:div>

						<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
							
							<s:div name="divElement1" cssclass="divRicMetadatiLeft">
								<input type="hidden" name="codop"
									value="${typeRequest.addScope}" />
							
								<input type="hidden" name="prova"
									value=1 />
								
									
									<s:dropdownlist label="Societ&agrave;:" name="ddlSocieta"  
									cssclass="tbddl floatleft" disable="true"
									cssclasslabel="label85 bold floatleft textright" 
									cachedrowset="companys" usexml="true" 
									valueselected="${configsesscarrellosoccanpagamento_companyCode}">
								<s:ddloption text="Selezionare uno degli elementi" value="" />	
								<s:ddloption value="{1}" text="{2}"/>
								</s:dropdownlist>

							</s:div>
									

							<s:div name="divElement2" cssclass="divRicMetadatiCenter">
									
									<s:dropdownlist label="Canale:" name="ddlCanale" disable="true" 
									cssclass="tbddl floatleft"
									cssclasslabel="label85 bold floatleft textright" 
									cachedrowset="canalepagamentos" usexml="true" 
									valueselected="${configsesscarrellosoccanpagamento_chiaveCanalePagamento}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption value="{1}" text="{2}"/>
								</s:dropdownlist>
									
									
							</s:div>
						</s:div>
						
						<s:div name="divElement11" cssclass="divRicMetadatiLeft">
								<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" validator="required;accept=^[1-9]$;maxlength=1" label="Numero Max:" 
								maxlenght="1" showrequired="true"
								name="configsesscarrellosoccanpagamento_numMaxSessioni" 
								text="${configsesscarrellosoccanpagamento_numMaxSessioni}" cssclasslabel="label85 bold textright"
								message="[accept=Numero Max: ${msg_configurazione_numero_1_9}]"
								cssclass="textareaman"/>
								
								</s:div>
						</s:div>

						<s:div name="divElement21" cssclass="divRicMetadatiCenter">
							<s:div name="divElement51" cssclass="divRicMetadatiSingleRow">
								 <s:dropdownlist label="Flag StateFull:" name="configsesscarrellosoccanpagamento_flagCarrello" disable="false" 
								 cssclass="textareaman" 
								 cssclasslabel="label85 bold textright"
								    valueselected="${configsesscarrellosoccanpagamento_flagCarrello}">
								<s:ddloption value="Y" text="SI"/>
								<s:ddloption value="N" text="NO"/>
								</s:dropdownlist>
								
								<!--<s:textbox bmodify="true"
							validator="required;minlength=1;maxlength=256"
							label="Descrizione:" showrequired="true"
							cssclasslabel="label65 bold textright" 
							name="impostaservizio_descrizioneImpostaServizio"
							text="${requestScope.impostaservizio_descrizioneImpostaServizio}"
							cssclass="textareaman" />
								
								
							--></s:div>
						</s:div>
								
								
								<s:div name="divElement3" cssclass="divRicMetadatiRight">
								
								<input type="hidden" name="configsesscarrellosoccanpagamento_companyCode" value="${configsesscarrellosoccanpagamento_companyCode}"/>
					<input type="hidden" name="configsesscarrellosoccanpagamento_chiaveCanalePagamento" value="<c:out value="${configsesscarrellosoccanpagamento_chiaveCanalePagamento}"/>"/>
					<p><b><c:out value=""/></b></p>
									
										
							</s:div>

					</s:div>
					
					</c:when>
					
					
					<c:otherwise>
					
					
					<s:div name="divRicMetadati" cssclass="divRicMetadati">


						<s:div name="divRicercaTitleName" cssclass="divRicTitle">Sessione Carrello</s:div>
						
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
							
							<s:div name="divElement12" cssclass="divRicMetadatiLeft">
							<input type="hidden" name="codop"
									value="${typeRequest.addScope}" />
							
							<input type="hidden" name="prova"
									value=1 />
								
									
									<s:dropdownlist label="Societ&agrave;:" name="configsesscarrellosoccanpagamentos" disable="false" 
									cssclass="tbddl floatleft"
									cssclasslabel="label85 bold floatleft textright" 
									validator="required" showrequired="true"
									cachedrowset="companys" usexml="true" valueselected="">
								<s:ddloption text="Selezionare uno degli elementi" value="" />
								<s:ddloption value="{1}" text="{2}"/>
								</s:dropdownlist>

									</s:div>
									

							<s:div name="divElement22" cssclass="divRicMetadatiCenter">
									
									<s:dropdownlist label="Canale:" name="configsesscarrellosoccanpagamentos2" disable="false" 
									cssclass="tbddl floatleft"
									cssclasslabel="label85 bold floatleft textright" 
									validator="required" showrequired="true"
									cachedrowset="canalepagamentos" usexml="true" valueselected="">
								<s:ddloption text="Selezionare uno degli elementi" value="" />
								<s:ddloption value="{1}" text="{2}"/>
								</s:dropdownlist>
									
									
							</s:div>
							
							
							
						</s:div>
							
							<s:div name="divElement3" cssclass="divRicMetadatiLeft">
								
								<s:textbox bmodify="true" validator="required;accept=^[1-9]$;maxlength=1" label="Numero Max:" 
								name="configsesscarrellosoccanpagamento_numMaxSessioni" 
								maxlenght="1" showrequired="true"
								text="${configsesscarrellosoccanpagamento_numMaxSessioni}" cssclasslabel="label85 bold textright"
								message="[accept=Numero Max: ${msg_configurazione_numero_1_9}]"
								cssclass="textareaman"/>
								
																		
							</s:div>
							<s:div name="divRicercaLeft" cssclass="divRicMetadatiCenter">

							<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
								 <s:dropdownlist label="Flag StateFull:" name="configsesscarrellosoccanpagamento_flagCarrello" disable="false" 
								 cssclass="textareaman" 
								 cssclasslabel="label85 bold textright"
								    valueselected="${configsesscarrellosoccanpagamento_flagCarrello}">
								<s:ddloption value="Y" text="SI"/>
								<s:ddloption value="N" text="NO"/>
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
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Sessione Carrello</s:div>
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

									<s:form name="indietro" action="configsesscarrellosoccanpagamento.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
									</s:form>


								</s:td>
								<s:td>

									<s:form name="form_Cancella"
										action="configsesscarrellosoccanpagamento.do?action=cancel&configsesscarrellosoccanpagamento_companyCode=${requestScope.companyCode}&configsesscarrellosoccanpagamento_chiaveCanalePagamento=${requestScope.chiaveCanalePagamento}"
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
				method="post" action="configsesscarrellosoccanpagamento.do?action=search">
				<center><c:if test="${error != null}">
					<s:label name="lblErrore" text="${message}" cssclass="lblMessage" />
				</c:if> <c:if test="${error == null}">
					<s:label name="lblMessage" text="${message}" cssclass="lblMessage" />
				</c:if> <s:div name="divPdf">
				<br /><br />
					<s:form name="indietro" action="configsesscarrellosoccanpagamento.do?action=search"
						method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
						<s:button id="tx_button_stampa_pdf" onclick="" text="Indietro"
							cssclass="btnStyle" type="submit" />
					</s:form>
				</s:div></center>
			
			</s:form>

		</c:otherwise>
	</c:choose>
</s:div>



