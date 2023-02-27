<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>

<m:view_state id="filtriestrattoconto" encodeAttributes="true" />

<jsp:useBean id="typeRequest" class="com.seda.payer.commons.bean.TypeRequest" />

<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
<c:choose>
	<c:when test="${done == null && richiestacanc == null}">	
		<s:div name="divRicercaTopName" cssclass="divRicercaTop">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle">Filtro Estratto Conto</s:div>

			<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
				hasbtn1="false" btn1text="..." btn1onclick="this.form.submit();"
				method="post" action="filtriec.do">

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
				
				<c:choose>
					<c:when test="${action == 'edit' || action == 'saveedit'}">
						<input type="hidden" name="codop" value="${typeRequest.editScope}" />
						<input type="hidden" name="fec_chiaveFiltroEstrattoConto"	value="<c:out value="${fec_chiaveFiltroEstrattoConto}"/>" />
					</c:when>
					<c:otherwise>
						<input type="hidden" name="codop" value="${typeRequest.addScope}" /> 
					</c:otherwise>
				</c:choose>

				<s:div name="divRicercaTitleName1" cssclass="divRicMetadati">
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="divElement1" cssclass="divRicMetadatiLeft">
							<s:textbox bmodify="true" name="fec_codiceFiscale"
								label="Cod. Fiscale:" maxlenght="16"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" text="${fec_codiceFiscale}"/>
		
						</s:div>
						<s:div name="divElement2" cssclass="divRicMetadatiLeftDouble">
						<!-- Giulia 23032013 Inizio -->
						<!--	<s:dropdownlist name="fec_ddlSocUteEnt"  
									label="Societ&agrave;/Utente/Ente: "
											cssclasslabel="label130 textright bold floatleft"
											cssclass="seda-ui-ddl tbddlMax500 floatleft" disable="false"
											   cachedrowset="listaSocietaUtenteEnteNewEdit" usexml="true" 
											   valueselected="${fec_ddlSocUteEnt}">
		   							<s:ddloption value="" text="Tutti gli enti" />
									<s:ddloption value="{1}|{2}|{3}" text="{4}"/>
								</s:dropdownlist>-->
								<s:div name="divElement1" cssclass="divRicMetadatiLeft">
							<s:textbox bmodify="true" name="fec_ente_ric"
								
								cssclasslabel="label85 bold textright"
								label="Ente:" maxlenght="6" validator="maxlength=6;accept=${configurazione_codiceEnteChar_regex}"
								message="[accept=Ente: ${msg_configurazione_alfanumerici_maiuscolo}]"
								cssclass="textareaman" text="${fec_ente_ric}"/>
		
						</s:div>
								
						</s:div>				
					</s:div>
					
					<s:div name="divRicercaMetadatiLeft" cssclass="divRicMetadatiLeft">
				        <s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
				        	<s:textbox bmodify="true" name="fec_impostaServizio"						
								label="Imp. Servizio:" maxlenght="2"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" text="${fec_impostaServizio}" />		        
				        </s:div>
					</s:div>
					<s:div name="divRicercaMetadatiCenter" cssclass="divRicMetadatiCenter">
				        <s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
				        	<s:textbox bmodify="true" name="fec_numeroEmissione"						
								label="N° Emissione:" maxlenght="6"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" text="${fec_numeroEmissione}" />		        
				        </s:div>
					</s:div>
					<s:div name="divRicercaMetadatiRight" cssclass="divRicMetadatiRight">
				        <s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
				        	<s:textbox bmodify="true" name="fec_numeroDocumento"						
								label="N° Docum.:" maxlenght="20"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" text="${fec_numeroDocumento}" />		        
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
			<s:div name="divRicercaTitleName2" cssclass="divRicTitle">Filtro Estratto Conto</s:div>	

			<s:form name="indietro" action="filtriec.do?action=search"
							method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
				<s:label name="lblCanc" text="${message}"
					cssclass="lblMessage" />
				<s:label name="lblCancmsg" text="Sei sicuro di voler cancellare il record selezionato?"
					cssclass="lblMessage" />
					
				<br />
				<br />
		
				<input type="hidden" name="fec_chiaveFiltroEstrattoConto"	value="<c:out value="${fec_chiaveFiltroEstrattoConto}"/>" />		
				<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					<s:button id="button_indietro" cssclass="btnStyle" type="submit" text="Indietro" onclick="" />
					<s:button id="tx_button_delete" onclick="" text="Cancella" type="submit" cssclass="btnStyle" />
				</s:div>
			</s:form>
						

		</s:div>

	</c:when>
	<c:otherwise>
	
		<s:form name="frmConfirm" hasbtn3="false" hasbtn2="false" hasbtn1="false" 
			method="post" action="filtriec.do?action=search">
				<center>
					<c:if test="${error != null}">
						<s:label name="lblErrore" text="${message}"/>
					</c:if> 
					<c:if test="${error == null}">
						<s:label name="lblMessage" text="${message}"/>
					</c:if>
				</center>
			<br />
			<br />
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">		
				<s:button id="button_indietro" cssclass="btnStyle" type="submit" text="Indietro" onclick="" />
			</s:div>
		</s:form>

	</c:otherwise>
</c:choose>
</s:div>

