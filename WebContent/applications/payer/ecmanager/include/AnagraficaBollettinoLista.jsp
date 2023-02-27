<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="anagraficaeclista" encodeAttributes="true" />


<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>

<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="form_selezione" action="ecanagrafica.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA ANAGRAFICA ESTRATTO CONTO
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label85 bold textright" validator="ignore;"
									cssclass="floatleft" disable="false"
									onchange="setFired();this.form.submit();"
									   cachedrowset="listaSocietaUtenteEnte" usexml="true" 
									   valueselected="${ddlSocietaUtenteEnte}">
   							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
							<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}"/>
						</s:dropdownlist>
						</s:div>					
				</s:div>	
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_denom"
							label="Denominaz.:" maxlenght="100" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_denom}" />
					</s:div>
				</s:div>
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_codfisc"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							label="Codice Fiscale:" maxlenght="16"
							text="${tx_codfisc}" />
					</s:div>
				</s:div>
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" text="Reset" validate="false" type="submit" cssclass="btnStyle" />
				<c:if test="${!empty lista_anaboll}">
					<s:button id="tx_button_download" onclick="" text="Download" cssclass="btnStyle" type="submit" />
				</c:if>
			</s:div>
	</s:form> 
	</s:div> 
	
</s:div>
 
<s:div name="div_messaggi" cssclass="div_align_center ad_color_red"> 
	<c:if test="${!empty tx_message}"> 
		<s:div name="div_messaggio_info"> 
			<hr /> 
			<s:label name="tx_message" text="${tx_message}" /> 
			<hr /> 
		</s:div> 
	</c:if>
	<c:if test="${!empty tx_error_message}">
		<s:div name="div_messaggio_errore" >
			<hr />
			<s:label name="tx_error_message" text="${tx_error_message}" />
			<hr />
		</s:div>
	</c:if>
</s:div>

<c:if test="${!empty lista_anaboll}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO ANAGRAFICHE ESTRATTO CONTO
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_anaboll"
			action="ecanagrafica.do?vista=AnagraficaLista" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			<s:dgcolumn index="4" label="Codice Fiscale" asc="CFSA" desc="CFSD"></s:dgcolumn>
			<s:dgcolumn index="5" label="Cognome" asc="COGA" desc="COGD"></s:dgcolumn>
			<s:dgcolumn index="6" label="Nome" asc="NOMA" desc="NOMD"></s:dgcolumn>
			<s:dgcolumn index="8" label="Email" asc="N_EMAIL_SOLLECITO_A" desc="N_EMAIL_SOLLECITO_D"></s:dgcolumn>
			<s:dgcolumn index="9" label="Email Pec" asc="N_INVIO_CARTACEO_A" desc="N_INVIO_CARTACEO_D"></s:dgcolumn>
			<s:dgcolumn index="7" label="Cellulare" asc="SMSA" desc="SMSD" ></s:dgcolumn>
			<s:dgcolumn label="Attivo">
				<s:if right="{11}" control="eq" left="Y">
					<s:then>NO</s:then>
				</s:if>
				<s:if right="{11}" control="eq" left="N">
					<s:then>SI</s:then>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn label="Welcome kit prodotto">
				<s:if right="{12}" control="eq" left="Y">
					<s:then>SI</s:then>
				</s:if>
				<s:if right="{12}" control="eq" left="N">
					<s:then>NO</s:then>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;">
				<s:hyperlink
					href="ecanagraficaedit.do?codfisc={4}&ddlSocietaUtenteEnte={1}|{2}|{3}"					
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Dettaglio Anagrafica" text=""
					cssclass="blacklink hlStyle" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>





