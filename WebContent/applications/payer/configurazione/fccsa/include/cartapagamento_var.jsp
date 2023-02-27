<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>
<m:view_state id="cartapagamento" encodeAttributes="true" />
<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />

<!--<script type="text/javascript">
function indietro(){
	//alert("OK");
	var frm =document.getElementById("frmAction");
	var a=document.getElementById("carta_codiceCarta");
	alert("1 "+a.value);
	var b =document.getElementById("carta_descrizioneCarta");
	//a.reset();
	alert("2 "+a.value);
	//b.reset();
	frm.submit();
	onclick ="indietro(); return false;"
}
</script>

--><br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<c:choose>
		<c:when test="${done == null && richiestacanc == null}">
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Metodo Pagamento</s:div>

				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
					hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
					method="post" action="cartapagamento.do">
					
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
					
					

					<s:div name="divRicercaTitleName1" cssclass="divRicMetadati">
						<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
							<s:div name="divElement1" cssclass="divRicMetadatiLeft">
								<c:choose>
									<c:when test="${action == 'edit'|| action == 'saveedit'}">
										<s:textbox bmodify="false"
												maxlenght="2"
												label="Cod. Carta:"
												name="codCarta"
												cssclasslabel="label85 bold textright"
												cssclass="textareaman colordisabled"
												text="${requestScope.carta_codiceCarta}"
												 />
												 
										<input type="hidden" name="codop"
											value="${typeRequest.editScope}" />
										<input type="hidden" name="carta_codiceCarta"
											value="<c:out value="${carta_codiceCarta}"/>" />

									</c:when>
									<c:otherwise>
										<input type="hidden" name="codop"
											value="${typeRequest.addScope}" />
										<s:div name="socValue_var">
											<s:textbox bmodify="true"
												validator="required;digits;minlength=2;maxlength=2"
												maxlenght="2" showrequired="true"
												label="Cod. Carta:"
												name="carta_codiceCarta"
												cssclasslabel="label85 bold textright"
												cssclass="textareaman"
												text="${requestScope.carta_codiceCarta}"
												 />
										</s:div>
									</c:otherwise>
								</c:choose>
							</s:div>

							<s:div name="divElement2" cssclass="divRicMetadatiCenter">
								<s:textbox bmodify="true"
									cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione_regex128}"
									maxlenght="128"
									label="Descrizione:" name="carta_descrizioneCarta"
									text="${requestScope.carta_descrizioneCarta}"
									message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold textright"
									 />
							</s:div>
						</s:div>
						
						
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
						
						
						<!--<s:div name="button_container_var">
							<center><s:table border="0" cssclass="container_btn">
								<s:thead>
									<s:tr />
								</s:thead>
								<s:tbody>
									<s:tr>
										<s:td>
											<s:button id="save_btn" type="submit" text="Salva" onclick=""
												cssclass="btnStyle" />
										</s:td>
										<s:td>
											<s:hyperlink name="backButton"
												imagesrc="../applications/templates/configurazione/img/back_icon.gif"
												href="metodopagamento.do?action=search" text=""
												cssclass="hlStyle" />
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
				<s:div name="divRicercaTitleName2" cssclass="divRicTitle">Metodo Pagamento</s:div>

				<s:label name="lblCanc" text="${message}" cssclass="lblMessage" />
				<s:label name="lblCancmsg"
					text="Sei sicuro di voler cancellare il record selezionato?"
					cssclass="lblMessage" />

				<br />
				<br />
				
				<s:div name="button_container_var">
						<center><s:table border="0" cssclass="container_btn">
							<s:thead>
								<s:tr>
									<s:td>
									</s:td>
								</s:tr>
							</s:thead>
							<s:tbody>
								<s:tr>
									<s:td>
									<s:form name="indietro"
												action="cartapagamento.do?action=search"
												method="post" hasbtn1="false" hasbtn2="false"
												hasbtn3="false">

												<s:button id="button_indietro" cssclass="btnStyle"
													type="submit" text="Indietro" onclick="" />
													</s:form>
									
										
										<!--<s:form name="Canc"
												action="canalepagamento.do?action=cancel&canalepagamento_chiaveCanalePagamento=${requestScope.canalepagamento_chiaveCanalePagamento}"
												method="post" hasbtn1="false" hasbtn2="false"
												hasbtn3="false">
									</s:form>
									
										--><!--<s:button id="save_btn" type="submit" cssclass="save_btn"
											text="Salva" onclick="" />
									--></s:td>
									<s:td>
									
									<s:form name="form_Cancella" action="cartapagamento.do?action=cancel&carta_codiceCarta=${requestScope.carta_codiceCarta}"
					method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
					<input type="hidden" name="action" value="canc" />					
						<s:button id="tx_button_cerca" onclick="" text="Cancella"
						type="submit" cssclass="btnStyle"/>						
					</s:form>
									
									<!--<s:form name="form_Indietro" action="canalepagamento.do?action=search"
					method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
					<input type="hidden" name="action" value="Ind" />
						<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
						<s:button id="tx_button_cerca" onclick="" text="Indietro"
						type="submit" cssclass="btnStyle"/>						
				</s:div>
					</s:form>-->
										<!--<s:hyperlink name="backButton"
											imagesrc="../applications/templates/configurazione/img/back_icon.gif"
											href="/manager/configurazione/canalepagamento.do" text=""
											cssclass="image_hyperlink" />
									--></s:td>
								</s:tr>
							</s:tbody>
						</s:table></center>
					</s:div>
				
				
				
				<!--<s:table border="0" cssclass="container_btn">
					<s:thead>
						<s:tr />
					</s:thead>
					<s:tbody>
						<s:tr>
							<s:td>
								<s:hyperlink name="cancButton"
									imagesrc="../applications/templates/configurazione/img/canc_icon.gif"
									href="/manager/configurazione/metodopagamento.do?action=cancel&carta_codiceCarta=${requestScope.carta_codiceCarta}"
									text="" cssclass="image_hyperlink" />
							</s:td>
							<s:td>
								<s:hyperlink name="backButton2"
									imagesrc="../applications/templates/configurazione/img/back_icon.gif"
									href="/manager/configurazione/metodopagamento.do" text=""
									cssclass="image_hyperlink" />
							</s:td>
						</s:tr>
					</s:tbody>
				</s:table>
			--></s:div>

		</c:when>
		<c:otherwise>

			<s:form name="frmConfirm" hasbtn3="false" hasbtn2="false"
				hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
				method="post" action="cartapagamento.do?action=search">
				<center><c:if test="${error != null}">
					<s:label name="lblErrore" text="${message}" />
				</c:if> <c:if test="${error == null}">
					<s:label name="lblMessage" text="${message}" />
				</c:if></center>
				<br />
				<br />
				<center>
				
				<s:form name="indietro"
												action="cartapagamento.do?action=search"
												method="post" hasbtn1="false" hasbtn2="false"
												hasbtn3="false">

												<s:button id="button_indietro" cssclass="btnStyle"
													type="submit" text="Indietro" onclick="" />
													</s:form>
				
				
				<!--<s:hyperlink name="backButton2"
					imagesrc="../applications/templates/configurazione/img/back_icon.gif"
					href="metodopagamento.do?action=search" text=""
					cssclass="image_hyperlink" />
					
					--></center>
			</s:form>

		</c:otherwise>
	</c:choose>
</s:div>

