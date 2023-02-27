<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<m:view_state id="configutentetiposervizios" encodeAttributes="true" />

<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />

<script type="text/javascript">
	function setFired() {
			var buttonFired = document.getElementById('fired_button_hidden');
			buttonFired.value = 'tx_button_changed';
	}
</script>



<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<c:choose>

		<c:when test="${done == null && richiestacanc == null}">

			<s:div name="divRicercaTopName" cssclass="divRicercaTop">

				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
					hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
					method="post" action="configutentetiposervizio.do">
					
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
					
					<s:div name="divRicMetadati" cssclass="divRicMetadati">
						<c:choose>
							<c:when test="${action == 'edit'|| action == 'saveedit'}">
								<%--MODIFICA --%>
								<input type="hidden" name="cod" value=1 />
							
								<s:div name="divRicercaTitleName" cssclass="divRicTitle">Utente - Tipologia Servizio</s:div>
		
								<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
									<s:div name="divElement1" cssclass="divRicMetadatiLeft">
										
									 		<s:dropdownlist label="Soc./Ute:" name="ddlSocUte" 
								 						   disable="true"
														   cachedrowset="utentetiposervizios" usexml="true" 
														   valueselected="${configutentetiposervizio_companyCode}|${configutentetiposervizio_codiceUtente}" 
														   cssclasslabel="label85 bold textright"
														   onchange="setFired();this.form.submit();"
														   cssclass="textareaman">
													<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
													<s:ddloption value="{1}|{2}" text="{3} / {4}"/>
											</s:dropdownlist>
									</s:div>
									<input type="hidden" name="configutentetiposervizio_strUtentetiposervizios" value="<c:out value="${configutentetiposervizio_companyCode}|${configutentetiposervizio_codiceUtente}"/>"/>
		
									<s:div name="divElement2" cssclass="divRicMetadatiCenter">
										
										<s:dropdownlist name="ddlTipolServ"
												disable="true"
												label="Tipol. Serv.:"
												cssclass="textareaman"
												cssclasslabel="label85 bold textright"
												onchange="setFired();this.form.submit();"
												cachedrowset="utentetiposervizios2" usexml="true"
												valueselected="${configutentetiposervizio_codiceTipologiaServizio}">
												<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
												<s:ddloption  value="{1}" text="{1}/{2}"/>
											</s:dropdownlist>
											
											<input type="hidden" name="configutentetiposervizio_codiceTipologiaServizio" value="<c:out value="${configutentetiposervizio_codiceTipologiaServizio}"/>"/>
									</s:div>
		
									<s:div name="divElement3" cssclass="divRicMetadatiRight">
										<s:dropdownlist name="configutentetiposervizio_tipoBoll"
												disable="false"
												label="Tipo Boll.:"
												cssclass="textareaman" 
												cssclasslabel="seda-ui-label label75 bold textright"
												onchange="setFired();this.form.submit();"
												cachedrowset="bollettini" usexml="true"
												validator="required" showrequired="true"
												valueselected="${configutentetiposervizio_tipoBoll}">
												<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
												<s:ddloption  value="{1}" text="{1}"/>
											</s:dropdownlist>
									</s:div>
		
								</s:div>
							</c:when>
							<c:otherwise>
								<%--INSERIMENTO --%>
								<input type="hidden" name="cod" value=0 />
		
								<s:div name="divRicercaTitleName" cssclass="divRicTitle">Utente - Tipologia Servizio</s:div>
								
								<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
									<s:div name="divElement11" cssclass="divRicMetadatiTopLeft">
										<s:div name="strUtentetiposervizios_var">
									 		<s:dropdownlist label="Soc./Ute:" name="configutentetiposervizio_strUtentetiposervizios" 
									 							disable="${ddlSocietaDisabled}"
															   cachedrowset="utentetiposervizios" usexml="true" 
															   valueselected="${configutentetiposervizio_strUtentetiposervizios}" 
															   cssclasslabel="label85 bold textright"
															   validator="required" showrequired="true"
															   onchange="setFired();this.form.submit();"
															   cssclass="textareaman">
													<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
													<s:ddloption value="{1}|{2}" text="{3} / {4}"/>
											</s:dropdownlist>
											<noscript>
												<s:button id="tx_button_changed" 
													disable="${ddlSocietaDisabled}" onclick="" text="" 
													validate="false"
													type="submit" cssclass="btnimgStyle"  title="Aggiorna" />
											</noscript>
										</s:div>
									</s:div>
		
									<s:div name="divElement22" cssclass="divRicMetadatiCenter">
										
										<s:dropdownlist name="configutentetiposervizio_codiceTipologiaServizio"
												disable="false"
												label="Tipol.Serv:"
												cssclass="textareaman"
												cssclasslabel="label65 bold textright"
												onchange="setFired();this.form.submit();"
												cachedrowset="utentetiposervizios2" usexml="true"
												validator="required" showrequired="true"
												valueselected="${configutentetiposervizio_codiceTipologiaServizio}">
												<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
												<s:ddloption  value="{1}" text="{1}/{2}"/>
											</s:dropdownlist>
									</s:div>
									<s:div name="divElement33" cssclass="divRicMetadatiRight">
										<s:dropdownlist name="configutentetiposervizio_tipoBoll"
												disable="false"
												label="Tipo Boll.:"
												cssclass="textareaman" 
												cssclasslabel="seda-ui-label label75 bold textright"
												onchange="setFired();this.form.submit();"
												cachedrowset="bollettini" usexml="true"
												validator="required" showrequired="true"
												valueselected="${configutentetiposervizio_tipoBoll}">
												<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
												<s:ddloption  value="{1}" text="{1}"/>
											</s:dropdownlist>
									</s:div>
								</s:div>
							</c:otherwise>
						</c:choose>
	
						<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
								
							<s:div name="divElement411" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" 
									maxlenght="20" showrequired="true"
									validator="required;minlength=10;maxlength=20" label="Numero CC:"
									name="configutentetiposervizio_numCC"
									text="${requestScope.configutentetiposervizio_numCC}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman"  />
							</s:div>
							
							<s:div name="divElement41" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="100" 
									validator="ignore;maxlength=100;accept=${configurazione_emailListBySemicolon}"
									label="Email Dest.:" name="configutentetiposervizio_mailDest"
									text="${configutentetiposervizio_mailDest}"
									message="[accept=Email Dest.: ${msg_configurazione_lista_email}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
	
							<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="100" 
										validator="ignore;accept=${configurazione_descrizione_regex100}"
										label="Logo Ente:" name="configutentetiposervizio_desMitt"
									text="${configutentetiposervizio_desMitt}"
									message="[accept=Logo Ente: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							
							<s:div name="divElement82" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Pag. Protetto:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="configutentetiposervizio_flagFunzPagProt" disable="false"  
								    	valueselected="${requestScope.configutentetiposervizio_flagFunzPagProt}">
									<s:ddloption value="N" text="No"/>
									<s:ddloption value="Y" text="Si"/>
								</s:dropdownlist>
							</s:div>
							
							<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Flag Range:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="configutentetiposervizio_flagRangeAbi" disable="false"  
								    	valueselected="${requestScope.configutentetiposervizio_flagRangeAbi}">
									<s:ddloption value="Y" text="Si"/>
									<s:ddloption value="N" text="No"/>
								</s:dropdownlist>
							</s:div>
							
							<s:div name="divElement931" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Integ. Entrate:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="configutentetiposervizio_flagIntegrazioneSeda" disable="false"  
								    	valueselected="${requestScope.configutentetiposervizio_flagIntegrazioneSeda}">
										<s:ddloption value="N" text="No"/>
										<s:ddloption value="Y" text="Si"/>
										<s:ddloption value="C" text="CUP"/>
										<s:ddloption value="1" text="SPOM1"/>
										<s:ddloption value="2" text="SPOM2"/>
								</s:dropdownlist>
							</s:div>

							
							<s:div name="divElement931" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Notifica Pagamento:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="configutentetiposervizio_flagNotificaPagamento" disable="false"  
								    	valueselected="${requestScope.configutentetiposervizio_flagNotificaPagamento}">
										<s:ddloption value="" text=""/>
										<s:ddloption value="N" text="No"/>
										<s:ddloption value="Y" text="Si"/>
								</s:dropdownlist>
							</s:div>
						</s:div>
	
						<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
							
							<s:div name="divElement412" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="256" showrequired="true"
									validator="required;accept=${configurazione_descrizione256_regex}"
									label="Intest. Cc:" name="configutentetiposervizio_intCC"
									text="${configutentetiposervizio_intCC}"
									message="[accept=Intest. Cc: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
	
							<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="100" 
									validator="ignore;maxlength=100;accept=${configurazione_emailListBySemicolon}"
									label="Email CCN:"
									name="configutentetiposervizio_mailCCNascosta" text="${requestScope.configutentetiposervizio_mailCCNascosta}"
									message="[accept=Email CCN: ${msg_configurazione_lista_email}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							
							<s:div name="divElement91" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="100" 
									validator="ignore;accept=${configurazione_email_regex};maxlength=100" label="Email Mitt.:"
									name="configutentetiposervizio_mailMitt" text="${requestScope.configutentetiposervizio_mailMitt}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							
							<s:div name="divElement92" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Visualizza:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="configutentetiposervizio_flagAll" disable="false"  
								    	valueselected="${requestScope.configutentetiposervizio_flagAll}">
										<s:ddloption value="Y" text="Si"/>
										<s:ddloption value="N" text="No"/>
										</s:dropdownlist>
							</s:div>
							
							<s:div name="divElement93" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Tipo Pag.:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="configutentetiposervizio_flagTpPag" disable="false"  
								    	valueselected="${requestScope.configutentetiposervizio_flagTpPag}">
										<s:ddloption value="S" text="Spontaneo"/>
										<s:ddloption value="" text="Altro"/>
										</s:dropdownlist>
							</s:div>
							
							<s:div name="divElement721" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="5" 
									validator="ignore"
									label="Cod. Entrate:" name="configutentetiposervizio_codiceUtenteSeda"
									text="${configutentetiposervizio_codiceUtenteSeda}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							
							<s:div name="divElement73" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="256" 
									validator="ignore;url;maxlength=256"
									label="Url Notifica Pagamento:" name="configutentetiposervizio_urlServizioWebNotificaPagamento"
									text="${configutentetiposervizio_urlServizioWebNotificaPagamento}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
														
						</s:div>
	
						<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
							
							<s:div name="divElement413" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="3" 
									validator="ignore;minlength=3;maxlength=3"
									label="Tipo Doc.:" name="configutentetiposervizio_tipoDoc"
									text="${requestScope.configutentetiposervizio_tipoDoc}"
									cssclasslabel="label85 bold floatleft textright"
									cssclass="textareaman" />
							</s:div>
							
							<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="5"
									validator="ignore;minlength=5;maxlength=5"
									label="Codice SIA:" name="configutentetiposervizio_codiceSia"
									text="${configutentetiposervizio_codiceSia}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							<c:if test="${fn:substring(configutentetiposervizio_tipoBoll,0,3)!='FRE'}" >
								<s:div name="divElement71" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true"
										maxlenght="27"
										validator="ignore;accept=^(IT|SM)\d{2}[a-zA-Z]\d{10}[0-9a-zA-Z]{12}|(IT|SM)\d{2}[a-zA-Z]\d{22}$;minlength=27;maxlength=27"
										label="IBAN:" name="configutentetiposervizio_codiceIban"
										text="${configutentetiposervizio_codiceIban}"
										message="[accept=IBAN: ${msg_configurazione_IBAN}]"
										cssclasslabel="label85 bold textright" cssclass="textareaman" />
								</s:div>
								<s:div name="divElement71a" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true"
										maxlenght="27"
										validator="ignore;accept=^(IT|SM)\d{2}[a-zA-Z]\d{10}[0-9a-zA-Z]{12}|(IT|SM)\d{2}[a-zA-Z]\d{22}$;minlength=27;maxlength=27"
										label="Secondo IBAN:" name="configutentetiposervizio_secondoCodiceIban"
										text="${configutentetiposervizio_secondoCodiceIban}"
										message="[accept=Secondo IBAN: ${msg_configurazione_IBAN}]"
										cssclasslabel="label85 bold textright" cssclass="textareaman" />
								</s:div>  
							</c:if>	
							<c:if test="${fn:substring(configutentetiposervizio_tipoBoll,0,3)=='FRE'}" >
								<s:div name="divElement71" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true"
										maxlenght="27" showrequired="true"
										validator="required;accept=^(IT|SM)\d{2}[a-zA-Z]\d{10}[0-9a-zA-Z]{12}|(IT|SM)\d{2}[a-zA-Z]\d{22}$;minlength=27;maxlength=27"
										label="IBAN:" name="configutentetiposervizio_codiceIban"
										text="${configutentetiposervizio_codiceIban}"
										message="[accept=IBAN: ${msg_configurazione_IBAN}]"
										cssclasslabel="label85 bold textright" cssclass="textareaman" />
								</s:div>
								<s:div name="divElement71a" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true"
										maxlenght="27" showrequired="true"
										validator="required;accept=^(IT|SM)\d{2}[a-zA-Z]\d{10}[0-9a-zA-Z]{12}|(IT|SM)\d{2}[a-zA-Z]\d{22}$;minlength=27;maxlength=27"
										label="Secondo IBAN:" name="configutentetiposervizio_secondoCodiceIban"
										text="${configutentetiposervizio_secondoCodiceIban}"
										message="[accept=Secondo IBAN: ${msg_configurazione_IBAN}]"
										cssclasslabel="label85 bold textright" cssclass="textareaman" />
								</s:div>
							</c:if>
							
							<s:div name="divElement72" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="256" 
									validator="ignore;minlength=1;maxlength=256"
									label="Funz Pag:" name="configutentetiposervizio_funzPag"
									text="${configutentetiposervizio_funzPag}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							
							<s:div name="divElement73" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="256" 
									validator="ignore;url;maxlength=256"
									label="Url Integraz.:" name="configutentetiposervizio_urlWeb"
									text="${configutentetiposervizio_urlWeb}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>

							

							<input type="hidden" name="codop" value="${typeRequest.editScope}" />
	                  		<input type="hidden" name="configutentetiposervizio_companyCode" value="${configutentetiposervizio_companyCode}"/>
							<input type="hidden" name="configutentetiposervizio_codiceUtente" value="<c:out value="${configutentetiposervizio_codiceUtente}"/>"/>
							
							<p><b><c:out value=""/></b></p>
						</s:div>
					
						<%-- inizio LP PG200360 --%>
						<s:div name="divElementUnicaTassonomia" cssclass="divRicMetadatiUnicaTassonomiaCFECFS">
							<s:div name="divElementU1" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="configutentetiposervizio_chiaveTassonomia"
										disable="false"
										label="Tassonomia:"
										cssclass="seda-ui-ddl tbddlMaxTassonomia870 floatleft" 
										cssclasslabel="seda-ui-label label85 bold textright"
										cachedrowset="tassonomie" usexml="true"
										validator="required" showrequired="true"
										valueselected="${configutentetiposervizio_chiaveTassonomia}">
										<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
										<s:ddloption value="{12}" text="{3} / {5} / {8} / {10} => {12}"/>
								</s:dropdownlist>
							</s:div>
						</s:div>
						<%-- fine LP PG200360 --%>
					
					</s:div>

					<%-- inizio LP PG200360 --%>
					<c:if test="${sessionScope.gestioneCausali}">
						<c:if test="${fn:substring(configutentetiposervizio_tipoBoll,0,4) == 'SPOM'}">
					      <s:div name="sezioneCausali" cssclass="divDettaglio">
					        <s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
					        	Elenco Causali&nbsp;
								<s:button id="button_add_causale" type="submit" text="Nuova" onclick="" cssclass="btnStyle btnStyle2" validate="false" disable="false"/>
					        </s:div>
							<c:if test="${not empty listaCausali}">
					          <s:table border="1" cellspacing="0" cellpadding="3" cssclass="seda-ui-datagrid">
					            <s:thead>
					              <s:tr cssclass="seda-ui-datagridrowpari">
					                <s:th cssclass="seda-ui-datagridheadercell width-100perc">Causale</s:th>
					                <s:th cssclass="seda-ui-datagridheadercell"></s:th>
					              </s:tr>
					            </s:thead>
					            <s:tbody>
					              <c:forEach items="${listaCausali}" var="u" varStatus="loop">
					                <s:tr cssclass="seda-ui-datagridrowpari">
					                  <s:td cssclass="seda-ui-datagridcell">
					                    <s:textbox
					                      name="causale_${loop.index}_testo"
					                      label=""
					                      text="${u}"
					    		          bdisable="false" 
					                      maxlenght="40"
					                      validator="ignore;maxlength=40"
					                      showrequired="false"
					                      bmodify="true"
					                      cssclass="textareaman width-100perc"
					                      cssclasslabel="display_none" />
					                  </s:td>
					                  <s:td cssclass="seda-ui-datagridcell">
					                    <s:button
					                      id="button_delete_causale"
					    		          disable="false" 
					                      title="Elimina"
					                      onclick=""
					                      text="${loop.index}"
					                      validate="false"
					                      type="submit"
					                      cssclass="btnimgDeletable" />
					                  </s:td>
					                </s:tr>
					              </c:forEach>
					            </s:tbody>
					          </s:table>
					          </c:if>
					      </s:div>
						  <br />
						</c:if>
					</c:if>
					<%-- fine LP PG200360 --%>

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
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Utente - Tipologia Servizio</s:div>
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

									<s:form name="indietro" action="configutentetiposervizio.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
									</s:form>


								</s:td>
								<s:td>

									<s:form name="form_Cancella"
										action="configutentetiposervizio.do?action=cancel&configutentetiposervizio_companyCode=${requestScope.companyCode}&configutentetiposervizio_codiceUtente=${requestScope.codiceUtente}&configutentetiposervizio_codiceTipologiaServizio=${requestScope.codiceTipologiaServizio}"
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
				method="post" action="configutentetiposervizio.do?action=search">
				<center>
					<c:if test="${error != null}">
						<s:label name="lblErrore" text="${message}" cssclass="lblMessage" />
					</c:if> 
					<c:if test="${error == null}">
						<s:label name="lblMessage" text="${message}" cssclass="lblMessage" />
					</c:if> 
					<s:div name="divPdf">
					<br /><br />
					<s:form name="indietro" action="configutentetiposervizio.do?action=search"
						method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
						<s:button id="tx_button_stampa_pdf" onclick="" text="Indietro"
							cssclass="btnStyle" type="submit" />
					</s:form>
					</s:div>
				</center>
			</s:form>

		</c:otherwise>
	</c:choose>
</s:div>



