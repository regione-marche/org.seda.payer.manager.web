<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>

<m:view_state id="costitransazionebanca" encodeAttributes="true" />

<jsp:useBean id="typeRequest"
	class="com.seda.payer.commons.bean.TypeRequest" />

<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<c:choose>
		<c:when test="${done == null && richiestacanc == null}">
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Costo Transazione - Banca</s:div>

				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
					hasbtn1="false" btn1text="..." btn1onclick=""
					method="post" action="costitransazionebanca.do">

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
							<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
									<c:choose>
										<c:when test="${action == 'edit' || action == 'saveedit'}">
											<input type="hidden" name="codop" value="${typeRequest.editScope}" />										
											<input type="hidden"
												name="costiTransazioneBanca_ChiaveGatewayPagamento"
												value="<c:out value="${costiTransazioneBanca_ChiaveGatewayPagamento}"/>" />
												
											<!--
											<s:textbox bmodify="false"
													validator="required;minlength=10;maxlength=10" label="Gateway:"
													name="costiTransazioneBanca_ChiaveGatewayPagamento" showrequired="true"
													text="${costiTransazioneBanca_ChiaveGatewayPagamento}"
													cssclasslabel="label85 bold textright" cssclass="textareaman" />
											
											-->
											<s:dropdownlist label="Gateway:" name="ddlGateways" disable="true" cssclasslabel="label85 bold textright" 
												cssclass="floatleft" cachedrowset="gatewaypagamentos" usexml="true" valueselected="${costiTransazioneBanca_ChiaveGatewayPagamento}">
												<s:ddloption text="Selezionare uno degli elementi" value="" />
												<s:ddloption value="{1}" text="{2}"/>
											</s:dropdownlist>
											
										</c:when>
										<c:otherwise>
											<input type="hidden" name="codop"
												value="${typeRequest.addScope}" />
											<s:div name="socValue_var">
											<s:dropdownlist label="Gateway:" name="costiTransazioneBanca_ChiaveGatewayPagamento" disable="false" cssclasslabel="label85 bold textright"
											    validator="required" showrequired="true"
												cssclass="floatleft" cachedrowset="gatewaypagamentos" usexml="true" valueselected="${costiTransazioneBanca_ChiaveGatewayPagamento}">
												<s:ddloption text="Selezionare uno degli elementi" value="" />
												<s:ddloption value="{1}" text="{2}"/>
											</s:dropdownlist>
												
											</s:div>
										</c:otherwise>
									</c:choose>
									
								</s:div>
								<s:div name="divElement1" cssclass="divRicMetadatiCenter">
									<c:choose>
										<c:when test="${action == 'edit' || action == 'saveedit'}">
											<input type="hidden" name="codop" value="${typeRequest.editScope}" />
											<input type="hidden"
												name="costiTransazioneBanca_ChiaveFasciaCosto"
												value="<c:out value="${costiTransazioneBanca_ChiaveFasciaCosto}"/>" />
																					
											<!--
											<s:textbox bmodify="false"
													validator="required;minlength=10;maxlength=10" label="Chiave F.Costo:"
													name="costiTransazioneBanca_ChiaveFasciaCosto" showrequired="true"
													text="${costiTransazioneBanca_ChiaveFasciaCosto}"
													cssclasslabel="label85 bold textright" cssclass="textareaman" />
											-->
										</c:when>
										<c:otherwise>
											<input type="hidden" name="codop" value="${typeRequest.addScope}" />										
										</c:otherwise>
									</c:choose>
									
									
								</s:div>
							
							</s:div>
							<s:div name="divRicercaMetadatiLeft" cssclass="divRicMetadatiLeft">							
								<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true"
										validator="ignore;accept=^[0-9]{1,8}\,[0-9]{2}?$;maxlength=11"
										maxlenght="11"
										label="Imp. Minimo:"
										name="costiTransazioneBanca_ImportoMinFasciaDa"
										text="${costiTransazioneBanca_ImportoMinFasciaDa}"
										message="[accept=Imp. Minimo: ${msg_configurazione_importo_8_2}]"
										cssclasslabel="label85 bold textright" cssclass="textareaman" />
								</s:div>
								<s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true"
										validator="ignore;accept=^[0-9]{1,8}\,[0-9]{2}?$;maxlength=11"
										maxlenght="11"
										label="Importo Fisso:"
										name="costiTransazioneBanca_ImportoFissoFascia"
										text="${costiTransazioneBanca_ImportoFissoFascia}"
										message="[accept=Imp. Fisso: ${msg_configurazione_importo_8_2}]"
										cssclasslabel="label85 bold textright" cssclass="textareaman" />
								</s:div>
							</s:div>
							<s:div name="divRicercaMetadatiCenter" cssclass="divRicMetadatiCenter">
								<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true"
										validator="ignore;accept=^[0-9]{1,13}\,[0-9]{2}?$;maxlength=16"
										maxlenght="16"
										label="Imp. Massimo:"
										name="costiTransazioneBanca_ImportoMaxFasciaDa"
										text="${costiTransazioneBanca_ImportoMaxFasciaDa}"
										message="[accept=Imp. Massimo: ${msg_configurazione_importo_13_2}]"
										cssclasslabel="label85 bold textright" cssclass="textareaman" />
								</s:div>
								<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true"
										validator="ignore;accept=^[0-9]{1,3}\,[0-9]{2}?$;maxlength=6"
										maxlenght="6"
										label="Percentuale:"
										name="costiTransazioneBanca_PercentualeFascia"
										text="${costiTransazioneBanca_PercentualeFascia}"
										message="[accept=Percentuale: ${msg_configurazione_importo_3_2}]"
										cssclasslabel="label85 bold textright" cssclass="textareaman" />
								</s:div>
							</s:div>
							<s:div name="divRicercaMetadatiRight" cssclass="divRicMetadatiRight">
								<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
									<s:dropdownlist label="Tipo Costo:" name="costiTransazioneBanca_TipoCosto" disable="false" cssclasslabel="label85 bold textright"
													  cssclass="textareaman"
													  valueselected="${costiTransazioneBanca_TipoCosto}">
										<s:ddloption value="G" text="Banca"/>
										<s:ddloption value="T" text="Transazione"/>
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
				<s:div name="divRicercaTitleName2" cssclass="divRicTitle">Costo Transazione - Banca</s:div>

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
								
								<s:form name="indietro" action="costitransazionebanca.do?action=search"
												method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		
												<s:button id="button_indietro" cssclass="btnStyle"
													type="submit" text="Indietro" onclick="" />
								</s:form>
							</s:td>
							<s:td>
								
									<s:form name="form_Cancella"
										action="costitransazionebanca.do?action=cancel&costiTransazioneBanca_ChiaveFasciaCosto=${costiTransazioneBanca_ChiaveFasciaCosto}&costiTransazioneBanca_ChiaveGatewayPagamento=${costiTransazioneBanca_ChiaveGatewayPagamento}"
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
				<center>
					<c:if test="${error != null}">
						<s:label name="lblErrore" text="${message}" />
					</c:if> <c:if test="${error == null}">
						<s:label name="lblMessage" text="${message}" />
					</c:if>
					<br />
					<br />
					<s:form name="indietro" action="costitransazionebanca.do?action=search"
											method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
	
											<s:button id="button_indietro" cssclass="btnStyle"
												type="submit" text="Indietro" onclick="" />
					</s:form>
				</center>
		</c:otherwise>
	</c:choose>
</s:div>

