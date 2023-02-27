<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="modello3" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="form_uffici" action="gestioneuffici.do" method="get"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA CONFIGURAZIONI UFFICI
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				
								<!--
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label160 bold textright floatleft" validator="ignore;"
									cssclass="seda-ui-ddl tbddlMax780 floatleft" disable="${codop == 'edit'}"
									onchange="setFired();this.form.submit();"
									   cachedrowset="listaSocietaUtenteEnte" usexml="true" 
									   valueselected="${ddlSocietaUtenteEnte}">
   							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
							<s:ddloption value="{1}|{2}|{3}|{4}" text="{5} / {6} / {4}"/>
						</s:dropdownlist>
						
						
						
						
						<c:if test="${codop == 'add'}">
							<noscript>
								<s:button id="tx_button_societa_changed" 
									onclick="" text="" 
									validate="false"
									type="submit" cssclass="btnimgStyle"  title="Aggiorna" />
							</noscript>
						</c:if>									
					</s:div>					
				</s:div>	
								 -->			

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiRight">
				
					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
					
						<s:textbox bmodify="true" name="tx_codufficio"
							label="Codice Ufficio:" maxlenght="6"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=^[0-9]{0,6}$"
							message=""
							text="${requestScope.tx_codufficio}" />

					</s:div>
				
				</s:div>

				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiLeft">
					
					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
					
						<s:textbox bmodify="true" name="tx_descrit"
							label="Descrizione IT:" maxlenght="100" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=^[0-9a-zA-Z\-]{1,100}$"
							message=""
							text="${requestScope.tx_descrit}" />

					</s:div>
					
				</s:div>
				
				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiCenter">
					
					<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">

						<s:textbox bmodify="true" name="tx_descrde"
							label="Descrizione DE:" maxlenght="100" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=^[0-9a-zA-Z\-]{1,100}$"
							message=""
							text="${requestScope.tx_descrde}" />

					</s:div>
					
				</s:div>
				
				
			</s:div>
			
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_add" validate="false"  onclick="" text="Nuovo" type="submit" cssclass="btnStyle" />
				
			</s:div>
			
	</s:form> 
	</s:div> 
	
</s:div>
 
<s:div name="div_messaggi" cssclass="div_align_center"> 
	<c:if test="${!empty tx_message}"> 
		<s:div name="div_messaggio_info"> 
			<hr /> 
			<s:label name="tx_message" text="${tx_message}" /> 
			<hr /> 
		</s:div> 
	</c:if>
	<c:if test="${!empty tx_error_message}">
		<s:div name="div_messaggio_errore">
			<hr />
			<s:label name="tx_error_message" text="${tx_error_message}" />
			<hr />
		</s:div>
	</c:if>
</s:div>

<c:if test="${!empty lista_uffici}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO UFFICI
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_uffici"
			action="" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			
			<s:dgcolumn index="1" label="ID Ufficio"></s:dgcolumn>
			<s:dgcolumn index="2" label="Codice Ufficio"></s:dgcolumn>
			<s:dgcolumn index="3" label="Descrizione IT"></s:dgcolumn>
			<s:dgcolumn index="4" label="Descrizione DE"></s:dgcolumn>
			
			<s:dgcolumn label="Azioni">
				<s:hyperlink					
					href="gestioneuffici.do?codop=edit&idufficio={1}" 
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Modifica" text=""
					cssclass="blacklink hlStyle" />
				<s:hyperlink
					href="gestioneuffici.do?codop=delete&idufficio={1}" 
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text=""
					cssclass="blacklink hlStyle" />
			</s:dgcolumn>
			
		</s:datagrid>
	</s:div>
</c:if>