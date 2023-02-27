<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<m:view_state id="configutentetiposervizioentes" encodeAttributes="true" />
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
					method="post" action="configutentetiposervizioente.do">
					
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
								<!--                           M O D I F I C A                     -->
								<input type="hidden" name="cod" value=1 />
						
								<s:div name="divRicercaTitleName" cssclass="divRicTitle">Utente - Tipologia Servizio - Ente</s:div>
		
								<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTopDouble">
									
									<s:div name="divElement1000" cssclass="divRicMetadatiSingleRow">
										
									 		<s:dropdownlist label="Societ&agrave;/Utente/Ente:" name="ddlSocUteEnte" 
									 							disable="true"
															   cachedrowset="entetiposervizios" usexml="true" 
															   valueselected="${configutentetiposervizioente_companyCode}|${configutentetiposervizioente_codiceUtente}|${configutentetiposervizioente_chiaveEnte}" 
															   cssclasslabel="label160 bold textright floatleft"
															   cssclass="seda-ui-ddl tbddlMax780 floatleft">
												<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
												<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}"/>
											</s:dropdownlist>
									</s:div>
									<input type="hidden" name="configutentetiposervizioente_strEntetiposervizios" value="${configutentetiposervizioente_companyCode}|${configutentetiposervizioente_codiceUtente}|${configutentetiposervizioente_chiaveEnte}"/>
									
									<s:div name="divElement2000" cssclass="divRicMetadatiSingleRow">
											<s:dropdownlist name="ddlTipoServ"
													disable="true"
													label="Tipologia&nbsp;Servizio:"
													cssclass=""
													cssclasslabel="label160 bold textright"
													cachedrowset="utentetiposervizios2" usexml="true"
													valueselected="${configutentetiposervizioente_codiceTipologiaServizio}">
												<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
												<s:ddloption  value="{1}" text="{1}/{2}"/>
											</s:dropdownlist>
											
									</s:div>
									
								
								</s:div>
							</c:when>
							<c:otherwise>
								<!--                           I N S E R I M E N T O                     -->
						
								<input type="hidden" name="cod" value=0 />
						
								<s:div name="divRicercaTitleName" cssclass="divRicTitle">Utente - Tipologia Servizio - Ente</s:div>
								
								<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTopDouble">
									<s:div name="divElement1000" cssclass="divRicMetadatiSingleRow">
										<input type="hidden" name="codop" value="${typeRequest.addScope}" />
									 		<s:dropdownlist label="Societ&agrave;/Utente/Ente:" name="configutentetiposervizioente_strEntetiposervizios" 
									 							disable="${ddlSocietaDisabled}"
															   cachedrowset="entetiposervizios" usexml="true" 
															   valueselected="${configutentetiposervizioente_strEntetiposervizios}" 
															   cssclasslabel="label160 bold textright floatleft"
															   onchange="setFired();this.form.submit();"
															   validator="required" showrequired="true"
															   cssclass="seda-ui-ddl tbddlMax780 floatleft">
												<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
												<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}"/>
											</s:dropdownlist>
											<noscript>
												<s:button id="tx_button_changed" 
													disable="${ddlSocietaDisabled}" onclick="" text="" 
													validate="false"
													type="submit" cssclass="btnimgStyle"  title="Aggiorna" />
											</noscript>
									</s:div>
									
									<s:div name="divElement2000" cssclass="divRicMetadatiSingleRow">
											<s:dropdownlist name="configutentetiposervizio_codiceTipologiaServizio"
													disable="false"
													label="Tipologia&nbsp;Servizio:"
													cssclass=""
													cssclasslabel="label160 bold textright"
													onchange="setFired();this.form.submit();"
													cachedrowset="utentetiposervizios2" usexml="true"
													validator="required" showrequired="true"
													valueselected="${configutentetiposervizio_codiceTipologiaServizio}">
												<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
												<s:ddloption  value="{1}" text="{1}/{2}"/>
											</s:dropdownlist>
											
									</s:div>
									
								</s:div>
							</c:otherwise>
						</c:choose>
	
							
						<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
							
							<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="configutentetiposervizioente_tipoBol"
									disable="false"
									label="Tipo Boll.:"
									cssclass="textareaman" 
									cssclasslabel="label85 bold textright"
									onchange="setFired();this.form.submit();"
									cachedrowset="bollettini" usexml="true"
									validator="required" showrequired="true"
									valueselected="${configutentetiposervizioente_tipoBol}">
									<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
									<s:ddloption  value="{1}" text="{1}"/>
								</s:dropdownlist>
							</s:div>
							
							<s:div name="divElement411" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="3" 
									validator="ignore;minlength=3;maxlength=3"
									label="Tipo Doc.:" name="configutentetiposervizioente_tipoDoc"
									text="${requestScope.configutentetiposervizioente_tipoDoc}"
									cssclasslabel="label85 bold floatleft textright"
									cssclass="textareaman" />
							</s:div>
								
							<s:div name="divElement41" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="100"
									validator="ignore;maxlength=100;accept=${configurazione_emailListBySemicolon}"
									label="Email Dest.:" name="configutentetiposervizioente_emailDest"
									text="${configutentetiposervizioente_emailDest}"
									message="[accept=Email Dest.: ${msg_configurazione_lista_email}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
		
							<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="100" 
									cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione_regex100}"
									label="Logo Ente:" name="configutentetiposervizioente_emailDescTo"
									text="${configutentetiposervizioente_emailDescTo}"
									message="[accept=Logo Ente: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold textright"  />
							</s:div>
							
							<s:div name="divElement82" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Pag. Protetto:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="configutentetiposervizioente_flagPagProtetta" disable="false"  
								    	valueselected="${requestScope.configutentetiposervizioente_flagPagProtetta}">
										<s:ddloption value="N" text="No"/>
										<s:ddloption value="Y" text="Si"/>
								</s:dropdownlist>
							</s:div>
							
							<s:div name="divElement73" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="256" 
									validator="ignore;url;maxlength=256"
									label="Url Integraz.:" name="configutentetiposervizioente_urlServWeb"
									text="${configutentetiposervizioente_urlServWeb}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>				
							
							<!-- SVILUPPO_001_LP -->
							<s:div name="divElement722" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Notifica Pagamento:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="configutentetiposervizioente_flagNotificaPagamento" disable="false"  
								    	valueselected="${configutentetiposervizioente_flagNotificaPagamento}">
										<s:ddloption value="Y" text="Si"/>
										<s:ddloption value="N" text="No"/>
										</s:dropdownlist>
							</s:div>
							<!-- FINE SVILUPPO_001_LP -->
		
						</s:div>
		
						<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
							
							<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="20" showrequired="true"
									validator="required;minlength=10;maxlength=20" label="Numero CC:"
									name="configutentetiposervizioente_numCc"
									text="${requestScope.configutentetiposervizioente_numCc}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman"  />
							</s:div>
							
							<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="100" 
									label="Email CCN:"
									validator="ignore;maxlength=100;accept=${configurazione_emailListBySemicolon}"
									name="configutentetiposervizioente_emailCcn" text="${requestScope.configutentetiposervizioente_emailCcn}"
									message="[accept=Email CCN: ${msg_configurazione_lista_email}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							
							<s:div name="divElement91" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="100" 
									validator="ignore;accept=${configurazione_email_regex};maxlength=100" label="Email Mitt.:"
									name="configutentetiposervizioente_emailTo" text="${requestScope.configutentetiposervizioente_emailTo}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							
							<s:div name="divElement92" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Visualizza:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="configutentetiposervizioente_flagAll" disable="false"  
								    	valueselected="${requestScope.configutentetiposervizioente_flagAll}">
										<s:ddloption value="Y" text="Si"/>
										<s:ddloption value="N" text="No"/>
										</s:dropdownlist>
							</s:div>
							
							<s:div name="divElement93" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Tipo Pag.:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="configutentetiposervizioente_flagTipoPag" disable="false"  
								    	valueselected="${requestScope.configutentetiposervizioente_flagTipoPag}">
										<s:ddloption value="S" text="Spontaneo"/>
										<s:ddloption value="" text="Altro"/>
								</s:dropdownlist>
							</s:div>
							
							<s:div name="divElement931" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Integ. Entrate:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="configutentetiposervizioente_flagIntegrazioneSeda" disable="false"  
								    	valueselected="${requestScope.configutentetiposervizioente_flagIntegrazioneSeda}">
										<s:ddloption value="N" text="No"/>
										<s:ddloption value="Y" text="Si"/>
								</s:dropdownlist>
							</s:div>
							
							<!-- SVILUPPO_001_LP -->
							<s:div name="divElement722" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" 
									validator="ignore"
									label="Url Notifica Pagamento:" name="configutentetiposervizioente_urlServizioWebNotificaPagamento"
									text="${configutentetiposervizioente_urlServizioWebNotificaPagamento}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							<!-- FINE SVILUPPO_001_LP -->
							
						</s:div>
		
						<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
							
							<s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="256" showrequired="true"
									cssclass="textareaman" validator="required;accept=${configurazione_descrizione256_regex}"
									label="Intest. Cc:" name="configutentetiposervizioente_intCc"
									text="${configutentetiposervizioente_intCc}"
									message="[accept=Intest. Cc: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold textright"  />
							</s:div>
							
							<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="5" 
									validator="ignore;minlength=5;maxlength=5"
									label="Codice Sia:" name="configutentetiposervizioente_codiceSia"
									text="${configutentetiposervizioente_codiceSia}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							
							<c:if test="${fn:substring(configutentetiposervizioente_tipoBol,0,3)=='FRE'}" >
							<s:div name="divElement71" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="27" showrequired="true"
									validator="required;accept=^(IT|SM)\d{2}[a-zA-Z]\d{10}[0-9a-zA-Z]{12}|(IT|SM)\d{2}[a-zA-Z]\d{22}$;minlength=27;maxlength=27"
									label="IBAN:" name="configutentetiposervizioente_codiceIban"
									text="${configutentetiposervizioente_codiceIban}"
									message="[accept=IBAN: ${msg_configurazione_IBAN}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							<s:div name="divElement71a" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="27" showrequired="true"
									validator="accept=^(IT|SM)\d{2}[a-zA-Z]\d{10}[0-9a-zA-Z]{12}|(IT|SM)\d{2}[a-zA-Z]\d{22}$;minlength=27;maxlength=27"
									label="Secondo IBAN:" name="configutentetiposervizioente_secondoCodiceIban"
									text="${configutentetiposervizioente_secondoCodiceIban}"
									message="[accept=Secondo IBAN: ${msg_configurazione_IBAN}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							</c:if>
							<c:if test="${fn:substring(configutentetiposervizioente_tipoBol,0,3)!='FRE'}" >
							<s:div name="divElement71" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="27"
									validator="ignore;accept=^(IT|SM)\d{2}[a-zA-Z]\d{10}[0-9a-zA-Z]{12}|(IT|SM)\d{2}[a-zA-Z]\d{22}$;minlength=27;maxlength=27"
									label="IBAN:" name="configutentetiposervizioente_codiceIban"
									text="${configutentetiposervizioente_codiceIban}"
									message="[accept=IBAN: ${msg_configurazione_IBAN}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							<s:div name="divElement71a" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="27"
									validator="ignore;accept=^(IT|SM)\d{2}[a-zA-Z]\d{10}[0-9a-zA-Z]{12}|(IT|SM)\d{2}[a-zA-Z]\d{22}$;minlength=27;maxlength=27"
									label="Secondo IBAN:" name="configutentetiposervizioente_secondoCodiceIban"
									text="${configutentetiposervizioente_secondoCodiceIban}"
									message="[accept=Secondo IBAN: ${msg_configurazione_IBAN}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							</c:if>
							
							<s:div name="divElement72" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="256" 
									validator="ignore;minlength=1;maxlength=256"
									label="Funz. Pag:" name="configutentetiposervizioente_funzionePag"
									text="${configutentetiposervizioente_funzionePag}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							
							
							<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Flag Range:" 
										cssclasslabel="label85 bold textright" cssclass="textareaman" 
										name="configutentetiposervizioente_flagConRange" disable="false"  
								    	valueselected="${requestScope.configutentetiposervizioente_flagConRange}">
										<s:ddloption value="Y" text="Si"/>
										<s:ddloption value="N" text="No"/>
										</s:dropdownlist>
							</s:div>
							
							<s:div name="divElement721" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="5" 
									validator="ignore"
									label="Cod. Entrate:" name="configutentetiposervizioente_codiceUtenteSeda"
									text="${configutentetiposervizioente_codiceUtenteSeda}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							
							<input type="hidden" name="codop" value="${typeRequest.editScope}" />
		                  	<input type="hidden" name="configutentetiposervizioente_companyCode" value="${configutentetiposervizioente_companyCode}"/>
							<input type="hidden" name="configutentetiposervizioente_codiceUtente" value="${configutentetiposervizioente_codiceUtente}"/>
		                  	<input type="hidden" name="configutentetiposervizioente_chiaveEnte" value="${configutentetiposervizioente_chiaveEnte}"/>
							<input type="hidden" name="configutentetiposervizioente_codiceTipologiaServizio" value="${configutentetiposervizioente_codiceTipologiaServizio}"/>
							
						</s:div>

						<%-- inizio LP PG200360 --%>
						<s:div name="divElementUnicaTassonomia" cssclass="divRicMetadatiUnicaTassonomiaCFECFS">
							<s:div name="divElementU1" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="configutentetiposervizioente_chiaveTassonomia"
										disable="false"
										label="Tassonomia:"
										cssclass="seda-ui-ddl tbddlMaxTassonomia870 floatleft" 
										cssclasslabel="seda-ui-label label85 bold textright"
										cachedrowset="tassonomie" usexml="true"
										validator="required" showrequired="true"
										valueselected="${configutentetiposervizioente_chiaveTassonomia}">
										<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
										<s:ddloption value="{12}" text="{3} / {5} / {8} / {10} => {12}"/>
								</s:dropdownlist>
							</s:div>
						</s:div>
						<%-- fine LP PG200360 --%>
					
					</s:div>

					<%-- inizio LP PG200360 --%>
					<c:if test="${sessionScope.gestioneCausali}">
						<c:if test="${fn:substring(configutentetiposervizioente_tipoBol,0,4) == 'SPOM'}">
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
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Tipol. Servizio/Ente</s:div>
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

									<s:form name="indietro" action="configutentetiposervizioente.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
									</s:form>


								</s:td>
								<s:td>

									<s:form name="form_Cancella"
										action="configutentetiposervizioente.do?action=cancel&configutentetiposervizioente_companyCode=${requestScope.companyCode}&configutentetiposervizioente_codiceUtente=${requestScope.codiceUtente}&configutentetiposervizioente_chiaveEnte=${requestScope.chiaveEnte}&configutentetiposervizioente_codiceTipologiaServizio=${requestScope.codiceTipologiaServizio}"
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
				method="post" action="configutentetiposervizioente.do?action=search">
				<center><c:if test="${error != null}">
					<s:label name="lblErrore" text="${message}" cssclass="lblMessage" />
				</c:if> <c:if test="${error == null}">
					<s:label name="lblMessage" text="${message}" cssclass="lblMessage" />
				</c:if> <s:div name="divPdf">
				<br /><br />
					<s:form name="indietro" action="configutentetiposervizioente.do?action=search"
						method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
						<s:button id="tx_button_stampa_pdf" onclick="" text="Indietro"
							cssclass="btnStyle" type="submit" />
					</s:form>
				</s:div></center>
			</s:form>

		</c:otherwise>
	</c:choose>
</s:div>



