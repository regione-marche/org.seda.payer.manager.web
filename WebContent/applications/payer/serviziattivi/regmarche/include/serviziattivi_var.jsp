<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<m:view_state id="configutentetiposervizioentes" encodeAttributes="true" />
<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>

<script type="text/javascript">
	function setEditAction() {
		var action = document.getElementById('action');
		if (action != null) {
			action.value = 'edit';
		}
	}
</script>

<style>
#divElementUnicaTassonomia{
	float:none;
}
#divElementU1{
	float:left;
}
</style>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<c:choose>
		<c:when test="${done == null && richiestacanc == null}">
			<s:div name="divRicercaTopName" cssclass="divRicercaTop">
				<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
					hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
					method="post" action="serviziattivi.do">
					
					<c:choose>
						<c:when test="${requestScope.action=='saveedit'}">
							<input type="hidden" name="action" id="action" value="<c:out value="${requestScope.action}"/>" />
						</c:when>
						<c:otherwise>
							<input type="hidden" name="action" id="action" value="save<c:out value="${requestScope.action}"/>" />
						</c:otherwise>
					</c:choose>
					
					<s:div name="divRicMetadati" cssclass="divRicMetadati">				
						<c:choose>
							<c:when test="${action == 'edit'|| action == 'saveedit'}">
								<!--                           M O D I F I C A                     -->
								<input type="hidden" name="cod" value=1 />
								<s:div name="divRicercaTitleName" cssclass="divRicTitle">Servizi Attivi</s:div>
								<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTopDouble">
									<s:div name="divElement100" cssclass="divRicMetadatiTopLeft">
										<s:dropdownlist name="tx_societa_d" disable="true"
											cssclass="tbddl floatleft" label="Societ&agrave;:"
											cssclasslabel="label85 bold floatleft textright"
											cachedrowset="listaSocieta" usexml="true"
											valueselected="${tx_societa}">
											<s:ddloption text="Tutte le Societ&agrave;" value="" />
											<s:ddloption text="{2}" value="{1}" />
										</s:dropdownlist>
									</s:div>
									<s:div name="divElement200" cssclass="divRicMetadatiTopCenter">
										<s:dropdownlist name="tx_provincia_d"
											disable="true" cssclass="tbddl floatleft"
											label="Provincia:"
											cssclasslabel="label65 bold floatleft textright"
											cachedrowset="listaProvince" usexml="true"
											valueselected="${tx_provincia}">
											<s:ddloption text="Tutte le Province" value="" />
											<s:ddloption text="{2}" value="{1}" />
										</s:dropdownlist>
									</s:div>
									<s:div name="divElement300" cssclass="divRicMetadatiTopRight">
										<s:dropdownlist name="tx_UtenteEnte_d"
											disable="true" cssclass="tbddlMax floatleft"
											label="Ente:" cssclasslabel="label65 bold textright"
											cachedrowset="listaUtentiEnti" usexml="true"
											valueselected="${tx_UtenteEnte}">
											<s:ddloption text="Tutti gli Enti" value="" />
											<s:ddloption text="{2}" value="{1}" />
										</s:dropdownlist>
									</s:div>
								</s:div>
							</c:when>
							<c:otherwise>
								<!--                           I N S E R I M E N T O                     -->
							</c:otherwise>
						</c:choose>
						
						<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
							<s:div name="divElement1a" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="false"
									validator="ignore;"
									label="Aux Digit:" name="modello3_auxDigit"
									text="${modello3_auxDigit}"
									cssclasslabel="label85 bold floatleft textright"
									cssclass="textareaman colordisabled" />
							</s:div>
							<s:div name="divElement1b" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="false"
									validator="ignore;"
									label="Url Mod. 1:" name="configutentetiposervizioente_urlServWeb"
									text="${configutentetiposervizioente_urlServWeb}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman colordisabled" />
							</s:div>
							<s:div name="divElement1c" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="false"
									validator="ignore"
									label="IBAN Accredito:" name="configutentetiposervizioente_codiceIban"
									text="${configutentetiposervizioente_codiceIban}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman colordisabled"/>
							</s:div>
							<s:div name="divElement1d" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="ddlTipoServ"
									disable="true"
									label="Tip. Servizio:"
									cssclass="textareaman"
									cssclasslabel="label85 bold textright"
									cachedrowset="utentetiposervizios2" usexml="true"
									valueselected="${configutentetiposervizioente_codiceTipologiaServizio}">
									<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
									<s:ddloption  value="{1}" text="{1}/{2}"/>
								</s:dropdownlist>
							</s:div>
							<%-- inizio SB PG210140 --%>
							<s:div name="divElement937" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="false"
									validator="ignore"
									label="Email Invio Flusso:" name="configrentutentetiposervizioente_emaildestinatario"
									text="${configrentutentetiposervizioente_emaildestinatario}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman colordisabled"/>
							</s:div>
							
							<c:if test="${fn:substring(configutentetiposervizioente_tipoBol,0,4) == 'SPOM'}">
								<s:div name="divElement934" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="256"
									label="Codice Contabilità:"
									name="configutentetiposervizioente_codiceContabilita"
									text="${configutentetiposervizioente_codiceContabilita}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman"  />
								</s:div>
							</c:if>
							<%-- fine SB PG210140 --%>
							<%-- inizio SB PG210170 --%>
							<s:div name="divElement725" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="128" 
									validator="ignore"
									label="Dicitura/Data pagamento" name="configutentetiposervizioente_datapagamento"
									text="${configutentetiposervizioente_datapagamento}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
							<%-- fine SB PG210170 --%>
						</s:div>
		
						<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
							<s:div name="divElement2a" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="false"
									validator="ignore;"
									label="Cod. Segreg.:" name="modello3_codiceSegregazione"
									text="${modello3_codiceSegregazione}"
									cssclasslabel="label85 bold floatleft textright"
									cssclass="textareaman colordisabled"/>
							</s:div>
							<s:div name="divElement2b" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="false"
									validator="ignore"
									label="Url Mod. 3:" name="modello3_urlIntegrazione"
									text="${modello3_urlIntegrazione}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman colordisabled"/>
							</s:div>
							<s:div name="divElement2c" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="false"
									validator="ignore"
									label="IBAN Appoggio:" name="configutentetiposervizioente_secondoCodiceIban"
									text="${configutentetiposervizioente_secondoCodiceIban}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman colordisabled"/>
							</s:div>
							<s:div name="divElement2d" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="configutentetiposervizioente_tipoBol"
									disable="true"
									label="Tipo Boll.:"
									cssclass="textareaman" 
									cssclasslabel="label85 bold textright"
									cachedrowset="bollettini" usexml="true"
									validator="ignore" showrequired="false"
									valueselected="${configutentetiposervizioente_tipoBol}">
									<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
									<s:ddloption  value="{1}" text="{1}"/>
								</s:dropdownlist>
							</s:div>
							
							<%-- inizio SB PG210140 --%>
							<c:if test="${fn:substring(configutentetiposervizioente_tipoBol,0,4) == 'SPOM'}">
								<s:div name="divElement935" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="256"
									label="Capitolo:"
									name="configutentetiposervizioente_capitolo"
									text="${configutentetiposervizioente_capitolo}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman"  />
								</s:div>
								<s:div name="divElement937" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true"
										maxlenght="4"
										label="Anno Competenza:"
										name="configutentetiposervizioente_annoCompetenza"
										text="${configutentetiposervizioente_annoCompetenza}"
										cssclasslabel="label85 bold textright"
										cssclass="textareaman"  />
								</s:div>
							</c:if>
							<%-- fine SB PG210140 --%>
														
						</s:div>
		
						<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
							<s:div name="divElement3a" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="false"
									validator="ignore;"
									label="Cod. Servizio:" name="modello3_carattereDiServizio"
									text="${modello3_carattereDiServizio}"
									cssclasslabel="label85 bold floatleft textright"
									cssclass="textareaman colordisabled"/>
							</s:div>
							<s:div name="divElement3b" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="false"
									validator="ignore;"
									label="Url Notifica:" name="configutentetiposervizioente_urlServizioWebNotificaPagamento"
									text="${configutentetiposervizioente_urlServizioWebNotificaPagamento}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman colordisabled"/>
							</s:div>
							<s:div name="divElement3c" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="false"
									validator="ignore;"
									label="Url Rendicont.:" name="ente_urlRendicontazione"
									text="${ente_urlRendicontazione}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman colordisabled"/>
							</s:div>
							<s:div name="divElement3d" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="false"
									validator="ignore;"
									label="Funz. Pagamento:" name="configutentetiposervizioente_funzionePag"
									text="${configutentetiposervizioente_funzionePag}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman colordisabled" />
							</s:div>
							
							<%-- inizio SB PG210140 --%>
							<c:if test="${fn:substring(configutentetiposervizioente_tipoBol,0,4) == 'SPOM'}">
								<s:div name="divElement936" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="256"
									label="Articolo:"
									name="configutentetiposervizioente_articolo"
									text="${configutentetiposervizioente_articolo}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman"  />
								</s:div>
								<s:div name="divElement936" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="false"
									maxlenght="256"
									label="Stampa Avviso PagoPA:"
									name="configutentetiposervizioente_flagStampaAvvisoPagoPa"
									text="${configutentetiposervizioente_flagStampaAvvisoPagoPa}"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman colordisabled"  />
								</s:div>
							</c:if>
							<%-- fine SB PG210140 --%>
							
							<input type="hidden" name="codop" value="${typeRequest.editScope}" />
		                  	<input type="hidden" name="configutentetiposervizioente_companyCode" value="${configutentetiposervizioente_companyCode}"/>
							<input type="hidden" name="configutentetiposervizioente_codiceUtente" value="${configutentetiposervizioente_codiceUtente}"/>
		                  	<input type="hidden" name="configutentetiposervizioente_chiaveEnte" value="${configutentetiposervizioente_chiaveEnte}"/>
							<input type="hidden" name="configutentetiposervizioente_codiceTipologiaServizio" value="${configutentetiposervizioente_codiceTipologiaServizio}"/>

		                  	<input type="hidden" name="tx_societa" value="${tx_societa}"/>
							<input type="hidden" name="tx_provincia" value="${tx_provincia}"/>
		                  	<input type="hidden" name="tx_UtenteEnte" value="${tx_UtenteEnte}"/>
							<%-- inizio LP PG21XX06 - Bug ButtonIndietro --%>
							<%-- input type="hidden" name="tx_tipologia_servizio" value="${configutentetiposervizioente_codiceTipologiaServizio}"/ --%>
							<input type="hidden" name="tx_tipologia_servizio" value="${tx_tipologia_servizio}"/>
							<%-- fine LP PG21XX06 - Bug ButtonIndietro --%>
						
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

					<c:if test="${sessionScope.gestioneCausali}">
						<c:if test="${fn:substring(configutentetiposervizioente_tipoBol,0,4) == 'SPOM'}">
							<s:div name="sezioneCausali" cssclass="divDettaglio">
								<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
								Elenco Causali&nbsp;
								<s:button id="button_add_causale" type="submit" text="Nuova" onclick="setEditAction();" cssclass="btnStyle btnStyle2" validate="false" disable="false"/>
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
							                      onclick="setEditAction();"
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
					
					<s:div name="button_container_var" cssclass="divRicBottoni">
						<s:textbox name="fired_button_hidden" label="fired_button_hidden" bmodify="true" text="" cssclass="rend_display_none" cssclasslabel="rend_display_none" />
						<s:button id="tx_button_indietro" type="submit" text="Indietro" onclick="" cssclass="btnStyle" validate="false" />
						<s:button id="tx_button_reset" onclick="setEditAction();" text="Reset" type="submit" cssclass="btnStyle" validate="false"/>
						<s:button id="tx_button_salva" type="submit" text="Salva" onclick="" cssclass="btnStyle" />
					</s:div>
				</s:form>


			</s:div>
		</c:when>
		<c:otherwise>

			<s:form name="frmConfirm" hasbtn3="false" hasbtn2="false" hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();" method="post" action="serviziattivi.do?action=search">
				<center>
				<c:if test="${error != null}">
					<s:label name="lblErrore" text="${message}" cssclass="lblMessage"/>
				</c:if>
				 <c:if test="${error == null}">
					<s:label name="lblMessage" text="${message}" cssclass="lblMessage"/>
				</c:if>
			    <s:div name="divPdf">
				<br /><br />
					<s:form name="indietro" action="serviziattivi.do?action=search" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
						<%-- inizio LP PG21XX06 - Bug ButtonIndietro --%>
						<%-- s:button id="tx_button_stampa_pdf" onclick="" text="Indietro" cssclass="btnStyle" type="submit"/ --%>
						<s:button id="tx_button_indietro" onclick="" text="Indietro" cssclass="btnStyle" type="submit"/>
						<%-- fine LP PG21XX06 - Bug ButtonIndietro --%>
					</s:form>
				</s:div></center>
			</s:form>

		</c:otherwise>
	</c:choose>
</s:div>



