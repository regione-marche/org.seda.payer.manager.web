<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>

<m:view_state id="funzpagutentetiposervizioentes" encodeAttributes="true" />

<jsp:useBean id="typeRequest"
	class="com.seda.payer.commons.bean.TypeRequest" />

<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
	//PG130040
	function ableOrDisable(id, valore){  
		document.getElementById(id).disabled=valore; 
	}
	
</script>

<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
<c:choose>
	<c:when test="${done == null && richiestacanc == null}">	
		<s:div name="divRicercaTopName" cssclass="divRicercaTop">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle">Funzione Pagamento Utente - Ente - Tipologia Servizio</s:div>
		

			<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
				hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
				method="post" action="funzpagutentetiposervizioente.do">

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
						
							<input type="hidden" name="funzpagutentetiposervizioente_userCode" value="${funzpagutentetiposervizioente_userCode}" />
							<input type="hidden" name="funzpagutentetiposervizioente_companyCode" value="${funzpagutentetiposervizioente_companyCode}" />
							<input type="hidden" name="funzpagutentetiposervizioente_codiceTipologiaServizio" value="${funzpagutentetiposervizioente_codiceTipologiaServizio}" />
							<input type="hidden" name="funzpagutentetiposervizioente_chiaveEnte" value="${funzpagutentetiposervizioente_chiaveEnte}" />
							<input type="hidden" name="funzpagutentetiposervizioente_tipoboll" value="${funzpagutentetiposervizioente_tipoboll}" />
							
							<c:choose>
								<c:when test="${action == 'edit' || action == 'saveedit'}">
									<input type="hidden" name="codop" value="${typeRequest.editScope}" />
									<input type="hidden" name="codop" value="${typeRequest.editScope}" />
									<s:dropdownlist name="ddlSocUteEntServ"  
										label="Societ&agrave;/Utente/Ente/Serv.:"
												cssclasslabel="label150 bold textright floatleft" 
												cssclass="floatleft widthtriple" disable="true"
												   cachedrowset="listaSocietaUtenteServizioEnte" usexml="true"
												   valueselected="${funzpagutentetiposervizioente_companyCode}|${funzpagutentetiposervizioente_userCode}|${funzpagutentetiposervizioente_chiaveEnte}|${funzpagutentetiposervizioente_codiceTipologiaServizio}|${funzpagutentetiposervizioente_tipoboll}">
										<s:ddloption value="{1}|{2}|{3}|{4}|{19}" text="{16} / {17} / {15} / {18}"/>
									</s:dropdownlist>																	
								</c:when>
								<c:otherwise>
									<input type="hidden" name="codop" value="${typeRequest.addScope}" /> 
									
									<s:div name="top" >
										<s:dropdownlist name="funzpagutentetiposervizio_strUtentetiposervizioentes"  
											label="Societ&agrave;/Utente/Ente/Serv.:"
													cssclasslabel="label150 bold textright floatleft" 
													cssclass="floatleft widthtriple" disable="${action == 'edit' || action == 'saveedit'}"
													   cachedrowset="listaSocietaUtenteServizioEnte" usexml="true"
													   onchange="setFiredButton('tx_button_changed');this.form.submit();"
													   validator="required;" showrequired="true"
													   valueselected="${funzpagutentetiposervizio_strUtentetiposervizioentes}">
				   							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
											<s:ddloption value="{1}|{2}|{3}|{4}|{19}" text="{16} / {17} / {15} / {18}"/>
										</s:dropdownlist>
										<noscript>
											<s:button id="tx_button_changed" onclick="" text="" validate="false"
												type="submit" cssclass="btnimgStyle floatleft"  title="Aggiorna"  />
										</noscript>						
									</s:div>					
								</c:otherwise>
							</c:choose>				
					</s:div>
				
					<%--
					<c:if test="${requestScope.campiFormCaricati != null || action == 'edit' || action == 'saveedit'}">
						<s:div name="divTitleCampiObbligatori" cssclass="divTitleCampiObbligatori">
							Campi obbligatori:
						</s:div>
						<s:div name="divListCampi" cssclass="divListCampi">
							--%><%--Inserisco 2 checkbox nascosti in modo da avere sempre un array String[] lato server --%><%--
							<s:list bradio="false" groupname="grCampi" name="hidden1" text="hidden1" value="hidden1" bchecked="true" cssclass="divDisplayNone" cssclasslabel="divDisplayNone" />
							<s:list bradio="false" groupname="grCampi" name="hidden2" text="hidden2" value="hidden2" bchecked="true" cssclass="divDisplayNone" cssclasslabel="divDisplayNone" />
							<s:repeater usexml="true" cachedrowset="listacampiform">
								<s:div name="divCb${cont}" cssclass="divSingleCb">
									<s:if left="{3}" control="eq" right="Y">
										<s:then>
											<s:list bradio="false" groupname="grCampi" name="{1}" text="{2}" value="{1}" bchecked="true" cssclass="singleCbCampo" cssclasslabel="singleCbCampoLbl"/>
										</s:then>
										<s:else>
											<s:list bradio="false" groupname="grCampi" name="{1}" text="{2}" value="{1}" bchecked="false" cssclass="singleCbCampo" cssclasslabel="singleCbCampoLbl"/>
										</s:else>
									</s:if>
								</s:div>
							</s:repeater>
						</s:div>
					</c:if>--%>
					

					<%-- PG130040 --%>
					<%--Inserisco 2 checkbox nascosti in modo da avere sempre un array String[] lato server --%>
					<s:list bradio="false" groupname="grCampi" name="hidden1" text="hidden1" value="hidden1" bchecked="true" cssclass="divDisplayNone" cssclasslabel="divDisplayNone" />
					<s:list bradio="false" groupname="grCampi" name="hidden2" text="hidden2" value="hidden2" bchecked="true" cssclass="divDisplayNone" cssclasslabel="divDisplayNone" />
					
					<c:if test="${not empty listaFunzPagamentoConf}">
						<s:div name="divTitleCampiObbligatori" cssclass="divTitleCampiObbligatori">
							&nbsp;
						</s:div>
						<s:div name="divListCampi" cssclass="divListCampi">
							<table id="listaFunzPagamentoConfTable">
						
							<c:forEach var="funzPagamentoConf" items="${listaFunzPagamentoConf}">
							
								<tr> 
									<c:choose>
											<c:when test="${funzPagamentoConf.flagBloccoObbligatorioModificabile}">
												<c:set var="bloccoObbligatorioModificabileDisabled" value="false"></c:set>
											</c:when>
											<c:otherwise>
												<c:set var="bloccoObbligatorioModificabileDisabled" value="true"></c:set>
												<c:if test="${funzPagamentoConf.obbligatorieta == 'Y'}">
													<s:list bradio="false" groupname="grCampi" name="hidden2" text="hidden2" value="${funzPagamentoConf.chiave}" bchecked="true" cssclass="divDisplayNone" cssclasslabel="divDisplayNone" />
												</c:if> 
											</c:otherwise>
									</c:choose>
									
									<c:choose>
											<c:when test="${funzPagamentoConf.flagBloccoTipoValoreModificabile}">
												<c:set var="bloccoTipoValoreModificabileDisabled" value="false"></c:set>
											</c:when>
											<c:otherwise>
												<c:set var="bloccoTipoValoreModificabileDisabled" value="true"></c:set>
											</c:otherwise>
									</c:choose>
								
									<td>
										<s:div name="${funzPagamentoConf.chiave}Descrizione" cssclass="divRiga primaRiga">
											<s:label name="campo" text="${funzPagamentoConf.descrizione}" cssclass="labelCampo"/>
										</s:div>
										<s:div name="${funzPagamentoConf.chiave}Obbligatorieta" cssclass="divRiga">
											<c:if test="${bloccoObbligatorioModificabileDisabled}">
											  <c:if test="${funzPagamentoConf.obbligatorieta == 'Y'}">
												<input type="hidden" name="grCampi" value="${funzPagamentoConf.chiave}"></input>
											  </c:if>
											</c:if>
											<s:list disable="${bloccoObbligatorioModificabileDisabled}" bradio="false" groupname="grCampi" name="grCampi" text="Obbligatorio" value="${funzPagamentoConf.chiave}" bchecked="${funzPagamentoConf.obbligatorieta == 'Y'}" cssclass="" cssclasslabel="obbligatorietaLbl"/>
										</s:div>
										<s:div name="${funzPagamentoConf.chiave}TipoValoreDiv1" cssclass="divRiga">
											<s:list disable="${bloccoTipoValoreModificabileDisabled}" bradio="true" groupname="FlagTipoValore_${funzPagamentoConf.chiave}" name="FlagTipoValore_${funzPagamentoConf.chiave}1" text="numerico" value="N"  bchecked="${funzPagamentoConf.tipoValore == 'N'}" cssclass="" cssclasslabel="numericoLbl"/>
										</s:div>
										<s:div name="${funzPagamentoConf.chiave}TipoValoreDiv2" cssclass="divRiga">
											<s:list disable="${bloccoTipoValoreModificabileDisabled}" bradio="true" groupname="FlagTipoValore_${funzPagamentoConf.chiave}" name="FlagTipoValore_${funzPagamentoConf.chiave}2" text="alfanumerico" value="A" bchecked="${funzPagamentoConf.tipoValore != 'N'}" cssclass="" cssclasslabel="alfanumericoLbl"/>
										</s:div>
									</td>
									
									<c:choose>
											<c:when test="${funzPagamentoConf.flagBloccoLunghezzaModificabile}">
												<c:set var="bloccoLunghezzaDisabled" value="false"></c:set>
											</c:when>
											<c:otherwise>
												<c:set var="bloccoLunghezzaDisabled" value="true"></c:set> 
											</c:otherwise>
									</c:choose>
									
									<td>
									
										<s:div name="primaRiga1${funzPagamentoConf.chiave}" cssclass="primaRiga"> &nbsp; </s:div>
									
										<s:div name="Lunghezza${funzPagamentoConf.chiave}" cssclass="divRiga margine">
											<s:label name="lunghezza" text="Lunghezza" cssclass="labelLunghezza"/>
										</s:div>
										
										<s:div name="riga1${funzPagamentoConf.chiave}" cssclass="riga">
										
											<s:div name="flagDiv1${funzPagamentoConf.chiave}" cssclass="flagDiv">
												<s:div name="${funzPagamentoConf.chiave}FlagLunghezza1" cssclass="divRiga">
													<s:list disable="${bloccoLunghezzaDisabled}" bradio="true" groupname="flagLunghezza_${funzPagamentoConf.chiave}" name="flagLunghezza_${funzPagamentoConf.chiave}1" text="fissa" value="F" bchecked="${funzPagamentoConf.tipoLunghezza == 'F'}" cssclass="" cssclasslabel="fissaLbl"/>
												</s:div>
												<s:div name="${funzPagamentoConf.chiave}FlagLunghezza2" cssclass="divRiga" >
													<s:list disable="${bloccoLunghezzaDisabled}" bradio="true" groupname="flagLunghezza_${funzPagamentoConf.chiave}" name="flagLunghezza_${funzPagamentoConf.chiave}2" text="variabile" value="V" bchecked="${funzPagamentoConf.tipoLunghezza != 'F'}" cssclass="" cssclasslabel="variabileLbl"/>
												</s:div> 
											</s:div>	
												
											
											<s:div name="textBoxDiv2${funzPagamentoConf.chiave}" cssclass="textBoxDiv">	
											
												<s:div name="${funzPagamentoConf.chiave}r1" cssclass="divRiga">&nbsp;</s:div>
												<s:div name="${funzPagamentoConf.chiave}Lunghezza" cssclass="divRiga">
													<s:textbox bdisable="${bloccoLunghezzaDisabled}" name="lunghezza_${funzPagamentoConf.chiave}" bmodify="true" label="" cssclasslabel="lunghezzaLbl" 
													text="${funzPagamentoConf.lunghezza}" cssclass="" maxlenght="3" showrequired="true"
													validator="minlength=0;maxlength=3"/>
													<s:label name="lunghezzaMax1" text="(max. ${funzPagamentoConf.lunghezzaMax})"/>
												</s:div> 
											</s:div>	
										</s:div>
										
										
										
									</td>
									 
									<c:choose>
											<c:when test="${funzPagamentoConf.flagBloccoDescrizioneAlternativaModificabile}">
												<c:set var="bloccoDescrizioneAlternativaDisabled" value="false"></c:set>
											</c:when>
											<c:otherwise>
												<c:set var="bloccoDescrizioneAlternativaDisabled" value="true"></c:set> 
											</c:otherwise>
									</c:choose>
									
									
									
									<td>
					 

										<s:div name="primaRiga2${funzPagamentoConf.chiave}" cssclass="primaRiga"> &nbsp; </s:div>
									
									
										<s:div name="descrizioneAlt" cssclass="divRiga margine">
											<s:label name="descrAlt" text="Descrizione alternativa" cssclass="labeldescrAlt"/>
										</s:div> 
										
										<s:div name="riga2${funzPagamentoConf.chiave}" cssclass="riga">
										
											<s:div name="flagDiv2${funzPagamentoConf.chiave}" cssclass="flagDiv">
												<s:div name="${funzPagamentoConf.chiave}FlagDescrAlternativa1" cssclass="divRiga">
													<s:list disable="${bloccoDescrizioneAlternativaDisabled}" bradio="true" groupname="FlagDescrAlternativa_${funzPagamentoConf.chiave}" name="FlagDescrAlternativa_${funzPagamentoConf.chiave}1" text="default" value="D" 
													bchecked="${funzPagamentoConf.tipoDescrizioneAlternativa == 'D'}" cssclass="" cssclasslabel="defaultLbl" 
													onclick="ableOrDisable('descrizioneAlternativa_${funzPagamentoConf.chiave}', true);" />
												</s:div>
												<s:div name="${funzPagamentoConf.chiave}FlagDescrAlternativa2" cssclass="divRiga">
													<s:list disable="${bloccoDescrizioneAlternativaDisabled}"  bradio="true" groupname="FlagDescrAlternativa_${funzPagamentoConf.chiave}" name="FlagDescrAlternativa_${funzPagamentoConf.chiave}2" text="alternativa" value="A" 
													bchecked="${funzPagamentoConf.tipoDescrizioneAlternativa != 'D'}" cssclass="" cssclasslabel="alternativaLbl"
													onclick="ableOrDisable('descrizioneAlternativa_${funzPagamentoConf.chiave}', false);"/>
												</s:div>
											
											</s:div> 
											 
											<s:div name="textBoxDiv2${funzPagamentoConf.chiave}" cssclass="textBoxDiv">	
												<s:div name="${funzPagamentoConf.chiave}r2" cssclass="divRiga">&nbsp;</s:div>
												<s:div name="${funzPagamentoConf.chiave}DescrizioneAlternativa" cssclass="divRiga">
													<s:textbox bmodify="true" bdisable="${bloccoDescrizioneAlternativaDisabled or funzPagamentoConf.tipoDescrizioneAlternativa == 'D'}" 
													 	name="descrizioneAlternativa_${funzPagamentoConf.chiave}" label="" cssclasslabel="descAltLbl" 
														text="${funzPagamentoConf.descrizioneAlternativa}" cssclass="" 
														maxlenght="100" 
														showrequired="true"
														validator="minlength=0;maxlength=100"/>
												</s:div>
											</s:div>
											
										</s:div> 
										
										

									</td>
								</tr>
						 
							</c:forEach>
							</table>
				 
						</s:div>
					</c:if>
					<%-- FINE PG130040 --%>
				
				
					
					<s:div name="button_container_var" cssclass="divRicBottoni">
						
						<s:button id="tx_button_indietro" type="submit" text="Indietro"
							onclick="" cssclass="btnStyle" validate="false" />
						<s:button id="tx_button_reset" onclick="" text="Reset"
							type="submit" cssclass="btnStyle" validate="false"/>
						<s:button id="save_btn" type="submit" text="Salva" onclick=""
							cssclass="btnStyle" />
							
						<s:textbox name="fired_button_hidden" label="fired_button_hidden"
							bmodify="true" text="" cssclass="rend_display_none"
							cssclasslabel="rend_display_none" />
					</s:div>
					
				</s:div>
			</s:form>
		</s:div>		
	</c:when>
	<c:when test="${richiestacanc != null}">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:div name="divRicercaTitleName2" cssclass="divRicTitle">Funzione Pagamento Utente - Ente - Tipologia Servizio</s:div>	

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
						
						<s:form name="indietro" action="funzpagutentetiposervizioente.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
						</s:form>
						</s:td>
						<s:td>
						
							<s:form name="form_Cancella"
										action="funzpagutentetiposervizioente.do?action=cancel&funzpagutentetiposervizioente_companyCode=${requestScope.funzpagutentetiposervizioente_companyCode}&funzpagutentetiposervizioente_userCode=${requestScope.funzpagutentetiposervizioente_userCode}&funzpagutentetiposervizioente_codiceTipologiaServizio=${requestScope.funzpagutentetiposervizioente_codiceTipologiaServizio}&funzpagutentetiposervizioente_nomeForm=${requestScope.funzpagutentetiposervizioente_nomeForm}&funzpagutentetiposervizioente_chiaveEnte=${requestScope.funzpagutentetiposervizioente_chiaveEnte}"
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
			method="post" action="funzpagutentetiposervizioente.do?action=search">
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

