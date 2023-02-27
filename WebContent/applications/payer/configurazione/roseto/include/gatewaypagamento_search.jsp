<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="Gatewaypagamentos_search" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>


<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Gateway Pagamento</s:div>
		<s:form name="search_form" action="gatewaypagamentosearch.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />
			
			<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">

				<s:div name="divElement1" cssclass="divRicMetadatiLeft">
					<s:textbox bmodify="true" name="gatewaypagamento_strDescrSocieta"
						label="Societ&agrave;:" maxlenght="256"
						cssclasslabel="label85 bold textright" cssclass="textareaman"
						validator="ignore;accept=${configurazione_descrizione256_regex}"
						message="[accept=Societ&agrave;: ${msg_configurazione_descrizione_regex}]"
						text="${gatewaypagamento_strDescrSocieta}" />
				</s:div>


				<s:div name="divElement2" cssclass="divRicMetadatiCenter">
					<s:textbox bmodify="true" name="gatewaypagamento_userDesc"
						label="Utente:" maxlenght="256"
						cssclasslabel="label85 bold textright" cssclass="textareaman"
						validator="ignore;accept=${configurazione_descrizione256_regex}"
						message="[accept=Utente: ${msg_configurazione_descrizione_regex}]"
						text="${gatewaypagamento_userDesc}" />
				</s:div>


				<s:div name="divElement3" cssclass="divRicMetadatiRight">
					<s:textbox bmodify="true"
						name="gatewaypagamento_strDescrCanalePagamento"
						label="Canale Pag.:" maxlenght="256"
						cssclasslabel="label85 bold textright" cssclass="textareaman"
						validator="ignore;accept=${configurazione_descrizione256_regex}"
						message="[accept=Canale Pag.: ${msg_configurazione_descrizione_regex}]"
						text="${gatewaypagamento_strDescrCanalePagamento}" />
				</s:div>
			</s:div>
			<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">	
				<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true"
						name="gatewaypagamento_strDescrCartaPagamento"
						label="Carta Pag.:" maxlenght="128"
						cssclasslabel="label85 bold textright" cssclass="textareaman"
						validator="ignore;accept=${configurazione_descrizione_regex128}"
						message="[accept=Carta Pag.: ${msg_configurazione_descrizione_regex}]"
						text="${gatewaypagamento_strDescrCartaPagamento}" />
				</s:div>
			</s:div>
			<s:div name="divRicercaLeft1" cssclass="divRicMetadatiCenter">	
				<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true"
						name="gatewaypagamento_descrGateway"
						label="Gateway:" maxlenght="256"
						cssclasslabel="label85 bold textright" cssclass="textareaman"
						validator="ignore;accept=${configurazione_descrizione256_regex}"
						message="[accept=Gateway: ${msg_configurazione_descrizione_regex}]"
						text="${gatewaypagamento_descrGateway}" />
				</s:div>
			</s:div>
			

			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit"
					cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" text="Reset" type="submit"
					cssclass="btnStyle" validate="false" />
				<s:button id="tx_button_nuovo" type="submit" text="Nuovo" onclick=""
					cssclass="btnStyle" validate="false" />
			</s:div>


		</s:form>

	</s:div>

<c:if test="${gatewaypagamentos != null}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Gateway Pagamento
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="gatewaypagamentos"  
			action="gatewaypagamentosearch.do?vista=Gatewaypagamentos_search" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
					   
    	    <s:dgcolumn index="37" label="Societ&agrave;" />
			<s:dgcolumn index="34" label="Utente" />
			<s:dgcolumn index="35" label="Canale di Pagamento" />
			<s:dgcolumn index="36" label="Carta di Pagamento" />
			<s:dgcolumn index="5" label="Gateway" />			
			<s:dgcolumn label="Azioni">
				 <s:hyperlink
					cssclass="hlStyle" 
					href="gatewaypagamento_edit.do?action=edit&gatewaypagamento_chiaveGateway={1}&gatewaypagamento_codiceCartaPagamento={24}&gatewaypagamento_chiaveCanalePagamento={4}"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />
		    
				<s:hyperlink
					cssclass="hlStyle" 
					href="gatewaypagamento_cancel.do?action=richiestacanc&richiestacanc=y&chiaveGateway={1}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text="" />
								  
			</s:dgcolumn>
		</s:datagrid>
	</s:div>

</c:if>
	
	
</s:div>

