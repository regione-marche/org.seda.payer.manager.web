<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js" type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js" type="text/javascript"></script>


<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>

<script type="text/javascript">
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();

	$(function() {
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#tx_dataCreazione_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_dataCreazione_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_dataCreazione_day_id").val(dateText.substr(0,2));
												$("#tx_dataCreazione_month_id").val(dateText.substr(3,2));
												$("#tx_dataCreazione_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_dataCreazione_day_id",
				                            "tx_dataCreazione_month_id",
				                            "tx_dataCreazione_year_id",
				                            "tx_dataCreazione_hidden");
			}
		});
		
	});

</script>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="form_selezione" action="blackboxpos.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA POSIZIONI BLACKBOX
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_idDominio" 
							label="Id Dominio:" bdisable="false"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_idDominio}" />
					</s:div>
					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_idDocumento" 
							label="Id Documento:" bdisable="false"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_idDocumento}" />
					</s:div>
					
					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:div name="divData" cssclass="seda-ui-date divData">
						 	<s:date label="Data Creazione:" prefix="tx_dataCreazione" yearbegin="2000" 
								yearend="2030" locale="IT-it" descriptivemonth="false" 
								separator="/" showrequired="false" calendar="${tx_dataCreazione}"
								cssclasslabel="label85 bold textright" >
							</s:date>				
							<input type="hidden" id="tx_dataCreazione_hidden" value=""/>
						</s:div>
					</s:div>
					
				</s:div>
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="${userProfile!='AMEN'}" name="tx_idEnte" 
							label="Codice Ente:" bdisable="false"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_idEnte}" />
					</s:div>
					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_codFisc" 
							label="Codice Fiscale:" bdisable="false"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_codFisc}" />
					</s:div>
					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_annoRif" 
							label="Anno Riferimento:" bdisable="false"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_annoRif}" />
					</s:div>
				</s:div>
				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_numeroAvviso" 
							label="Numero Avviso:" bdisable="false"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_numeroAvviso}" />
					</s:div>
					
					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
					
						<s:dropdownlist name="tx_flagPagato"
							disable="false" 
							label="Flag Pagato:"
							cssclasslabel="label85 bold floatleft textright"
							cssclass="tbddl floatleft"
							usexml="false"
							valueselected="${tx_flagPagato}">
							<s:ddloption value="T" text="Tutti" />
							<s:ddloption value="" text="No"/>
							<s:ddloption value="X" text="Si"/>
						</s:dropdownlist>
					
					</s:div>
				</s:div>
				
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
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

<c:if test="${!empty lista_blackbox}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO POSIZIONI BLACK BOX
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_blackbox"
			action="blackboxpos.do" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			
			<s:action>
			<c:url value="blackboxpos.do">
				<c:if test="${!empty param.tx_idDominio}">
					<c:param name="tx_idDominio">${param.tx_idDominio}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_idEnte}">
					<c:param name="tx_idEnte">${param.tx_idEnte}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_numeroAvviso}">
					<c:param name="tx_numeroAvviso">${param.tx_numeroAvviso}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_idDocumento}">
					<c:param name="tx_idDocumento">${param.tx_idDocumento}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_codFisc}">
					<c:param name="tx_codFisc">${param.tx_codFisc}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_flagPagato}">
					<c:param name="tx_flagPagato">${param.tx_flagPagato}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_dataCreazione}">
					<c:param name="tx_dataCreazione">${param.tx_dataCreazione}</c:param>
				</c:if>
				<c:if test="${!empty param.tx_annoRif}">
					<c:param name="tx_annoRif">${param.tx_annoRif}</c:param>
				</c:if>
			</c:url>
		</s:action>
			
			<s:dgcolumn index="1" label="Id Dominio" css="text_align_left" ></s:dgcolumn>
			<s:dgcolumn index="2" label="Codice Ente"></s:dgcolumn>
			<s:dgcolumn index="3" label="Numero Avviso"></s:dgcolumn>
			<s:dgcolumn index="4" label="ID FLusso"></s:dgcolumn>
			<s:dgcolumn index="7" label="ID Documento"></s:dgcolumn>
			<s:dgcolumn index="10" label="Codice Fiscale"></s:dgcolumn>
			<%-- inizio LP PG200370 --%>
			<%-- s:dgcolumn index="11" label="Importo"></s:dgcolumn --%>
			<s:dgcolumn index="11" label="Importo" css="text_align_right" format="#,##0.00"></s:dgcolumn>
			<%-- fine LP PG200370 --%>
			<s:dgcolumn label="Pagato">
				<s:if left="{20}" control="eq" right="X">
					<s:then>
						Si
					</s:then>
					<s:else>
						No
					</s:else>
				</s:if>
			</s:dgcolumn>
			<%-- inizio LP PG200370 --%>
			<%-- s:dgcolumn index="21" label="Importo Pagato" format="###,###.###"></s:dgcolumn --%>
			<s:dgcolumn index="21" label="Importo Pagato" css="text_align_right" format="#,##0.00"></s:dgcolumn>
			<%-- fine LP PG200370 --%>
			<s:dgcolumn label="Azioni">
				<s:if left="{20}" control="ne" right="X">
					<s:then>
						<s:hyperlink
							href="blackboxpos.do?codOp=edit&tx_idDominio_hidden={1}&tx_idEnte_hidden={2}&tx_numeroAvviso_hidden={3}&tx_idIuv_hidden={19}"
							imagesrc="../applications/templates/shared/img/edit.png"
							alt="Modifica" text=""
							cssclass="blacklink hlStyle" />
						<s:hyperlink
							href="blackboxpos.do?codOp=delete&tx_idDominio_hidden={1}&tx_idEnte_hidden={2}&tx_numeroAvviso_hidden={3}"
							imagesrc="../applications/templates/configurazione/img/cancel.png"
							alt="Cancella" text="" cssclass="blacklink hlStyle" />
					</s:then>
					<s:else>
						&nbsp;
					</s:else>
				</s:if>
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
	
</c:if>
