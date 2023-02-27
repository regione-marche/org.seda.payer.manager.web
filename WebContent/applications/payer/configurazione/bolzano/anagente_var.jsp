<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>

<m:view_state id="anagente" encodeAttributes="true" />


<jsp:useBean id="typeRequest"
	class="com.seda.payer.commons.bean.TypeRequest" />


<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>

<script type="text/javascript">
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();

	$(function() {
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#tx_data_pag_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_pag_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_data_pag_da_day_id").val(dateText.substr(0,2));
												$("#tx_data_pag_da_month_id").val(dateText.substr(3,2));
												$("#tx_data_pag_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_pag_da_day_id",
				                            "tx_data_pag_da_month_id",
				                            "tx_data_pag_da_year_id",
				                            "tx_data_pag_da_hidden");
			}
		
		});
	});

	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>



<c:url value="" var="formParameters">
	<c:param name="test">ok</c:param>
	<c:if test="${!empty tx_data_pag_da_day}">  
		<c:param name="tx_data_pag_da_day">${tx_data_pag_da_day}</c:param>
	</c:if>
	<c:if test="${!empty tx_data_pag_da_month}">  
		<c:param name="tx_data_pag_da_month">${tx_data_pag_da_month}</c:param>
	</c:if>
	<c:if test="${!empty tx_data_pag_da_year}">  
		<c:param name="tx_data_pag_da_year">${tx_data_pag_da_year}</c:param>
	</c:if>
</c:url>


<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
<c:choose>
	<c:when test="${done == null && richiestacanc == null}">	
		<s:div name="divRicercaTopName" cssclass="divRicercaTop">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle">Anagrafica Ente</s:div>
			
			<s:form name="frmAction" hasbtn3="false" hasbtn2="false"
				hasbtn1="false" method="post" action="anagente.do">
				
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
				<c:choose>
					<c:when test="${requestScope.action=='edit'|| requestScope.action=='saveedit'}">
				     	<input type="hidden" name="codop" value="${typeRequest.editScope}" />
					</c:when>
					<c:otherwise>
						<input type="hidden" name="codop" value="${typeRequest.addScope}" />
					</c:otherwise>
				</c:choose>
				

				<s:div name="divRicMetadati" cssclass="divRicMetadati">
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
							<s:dropdownlist name="anagente_siglaprovincia"
								disable="false" 
								label="Provincia:"
								cssclasslabel="label85 bold floatleft textright"
								cssclass="tbddl floatleft"
								cachedrowset="listprovince" usexml="true"
								onchange="setFiredButton('tx_button_changed');this.form.submit();"
								validator="required" showrequired="true"
								valueselected="${anagente_siglaprovincia}">
								<s:ddloption text="Selezionare uno degli elementi" value="" />
								<s:ddloption  value="{2}" text="{1}"/>
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_changed" onclick="" text="" 
									type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
							</noscript>
						</s:div>
						
						<s:div name="tipServLabel_srch" cssclass="divRicMetadatiCenterSmall">
							<s:dropdownlist name="anagente_codiceBelfiore"
									disable="false" 
									label="Cod. Belf:"
									cssclasslabel="label65 bold textright"
									cssclass="tbddl"
									cachedrowset="listbelfiore" usexml="true"
									validator="required" showrequired="true"
									valueselected="${anagente_codiceBelfiore}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption  value="{1}" text="{1} - {4}"/>
								</s:dropdownlist>
						</s:div>
						
						<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
							<c:if test="${requestScope.action=='edit'|| requestScope.action=='saveedit'}">
								<s:textbox bmodify="false"
								label="Chiave Ente:" name="txtChiaveEnte"
								text="${anagente_chiaveEnte}"
								cssclasslabel="label85 bold textright" cssclass="textareaman colordisabled" />
							</c:if>
						</s:div>
						
						</s:div>
						
						<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
						
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
								validator="required;digits;minlength=5;maxlength=5"
								maxlenght="5" showrequired="true"
								label="Cod. Ente:" name="anagente_codiceEnte"
								text="${anagente_codiceEnte}"
								cssclasslabel="label85 bold textright" cssclass="textareaman" />

								
								
								
							</s:div>
							
							<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true" maxlenght="256"
								cssclass="textareaman" validator="ignore"
									label="Descr.Ente:" name="anagente_descrizioneEnte"
									text="${anagente_descrizioneEnte}"
									cssclasslabel="label85 bold textright" />
								
								<!--<s:textbox bmodify="true"
									validator="required;minlength=0;maxlength=1"
									label="Tipo Ufficio:" name="anagente_tipoUfficio"
									maxlenght="1" showrequired="true"
									text="${anagente_tipoUfficio}"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							--></s:div>
							
							<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
								
								<%--<s:textbox bmodify="true"
									validator="ignore;minlength=1;maxlength=2"
									label="Tipo Ente:" name="anagente_codiceTipoEnte"
									text="${anagente_codiceTipoEnte}"
									maxlenght="2"
									cssclasslabel="label85 bold textright" cssclass="textareaman"  />
								--%>
								<s:dropdownlist name="anagente_codiceTipoEnte" disable="false"
									valueselected="${anagente_codiceTipoEnte}"
								 label="Tipo Ente:" cssclasslabel="label85 bold textright" cssclass="tbddl">
									<s:ddloption value="VA" text="AMMINISTRAZIONE PUBBLICA" />
									<s:ddloption value="AP" text="AUTORITA' PORTUALE" />
									<s:ddloption value="AT" text="AZIENDA DI TRASPORTO" />
									<s:ddloption value="AS" text="AZIENDA SANITARIA" />
									<s:ddloption value="AC" text="AZIENDA SERVIZI COMUNALI" />
									<s:ddloption value="M" text="CAMERA DI COMMERCIO" />
									<s:ddloption value="F" text="CAPITANERIA DI PORTO" />
									<s:ddloption value="CC" text="CARABINIERI" />
									<s:ddloption value="Z" text="CASSA DI PREVIDENZA" />
									<s:ddloption value="G" text="COLLEGIO PROFESSIONALE" />
									<s:ddloption value="C" text="COMUNE" />
									<s:ddloption value="N" text="CONSORZIO" />
									<s:ddloption value="D" text="DIREZIONE PROVINCIALE DEL LAVORO" />
									<s:ddloption value="A" text="ENTE LOCALE" />
									<s:ddloption value="VN" text="ENTE NAZIONALE" />
									<s:ddloption value="EP" text="ENTE PREVIDENZIALE" />
									<s:ddloption value="PU" text="ENTE PUBBLICO" />
									<s:ddloption value="V" text="ENTI VARI" />
									<s:ddloption value="H" text="FOGLIO ANNUNZI LEGALI" />
									<s:ddloption value="GF" text="GUARDIA DI FINANZA" />
									<s:ddloption value="IL" text="INAIL" />
									<s:ddloption value="IS" text="INPS" />
									<s:ddloption value="IN" text="ISTITUTO DI PREVIDENZA" />
									<s:ddloption value="MG" text="MINISTERO DELLA GIUSTIZIA" />
									<s:ddloption value="E" text="MINISTERO DELLE FINANZE" />
									<s:ddloption value="O" text="ORDINE PROFESSIONALE" />
									<s:ddloption value="PS" text="POLIZIA DELLA STRADA" />
									<s:ddloption value="T" text="PREFETTURA" />
									<s:ddloption value="P" text="PROVINCIA" />
									<s:ddloption value="AU" text="PROVINCIA AUTONOMA" />
									<s:ddloption value="R" text="REGIONE" />
									<s:ddloption value="S" text="SEZIONE TERRITORIALE POLSTRADA" />
									<s:ddloption value="K" text="SIAE" />
									<s:ddloption value="SG" text="SOCIETA' GESTIONE FONDO SOLIDARIETA'" />
									<s:ddloption value="US" text="UFFICI STATALI" />
									<s:ddloption value="UC" text="UNIONE DEI COMUNI" />
									<s:ddloption value="U" text="UPICA" />
									<s:ddloption value="YY" text="ALTRO..." />
								</s:dropdownlist>
								
							</s:div>
							
							</s:div>
						<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
						
						<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
								
								<s:textbox bmodify="true"
									validator="ignore;accept=^[a-zA-Z0-9 ]$;minlength=1;maxlength=1"
									label="Tipo Ufficio:" name="anagente_tipoUfficio"
									text="${anagente_tipoUfficio}"
									maxlenght="1" message="[accept=Tipo Ufficio: ${msg_configurazione_alfanumerici_spazio}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
								
								</s:div>
							
							<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								maxlenght="256"
								label="Descr. Ufficio:" name="anagente_descrizioneUfficio"
								text="${anagente_descrizioneUfficio}"
								cssclasslabel="label85 bold textright" message="[accept=Descr. Ufficio: ${msg_configurazione_descrizione_regex}]" />
							</s:div>	
								
								<s:div name="divElement12" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true"
								validator="ignore;minlength=4;accept=^[a-zA-Z0-9]{4}$;maxlength=4"
								label="Ruoli Erariali:" name="anagente_codiceRuoliErariali"
								text="${anagente_codiceRuoliErariali}"
								maxlenght="4" message="[accept=Ruoli Erariali: ${msg_configurazione_alfanumerici}]"
								cssclasslabel="label85 bold textright" cssclass="textareaman"  />
							
						</s:div>
						
						
							
							<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
								<input type="hidden" name="codop"
										value="${typeRequest.editScope}" />
									<input type="hidden" name="anagente_chiaveEnte"
										value="<c:out value="${anagente_chiaveEnte}"/>" />
									<p><b><c:out value="" /></b></p>
							</s:div>
							
							</s:div>
							
						<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
							
							<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true"
								validator="ignore;accept=^[\d\w ]{1,6}$"
								maxlenght="6"
								label="Cod. Ufficio:" name="anagente_codiceUfficio"
								text="${anagente_codiceUfficio}"
								message="[accept=Cod. Ufficio: ${msg_configurazione_alfanumerici_spazio}]"
								cssclasslabel="label85 bold textright" cssclass="textareaman" />
							
							
						</s:div>
						
						<s:div name="divElement13" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									validator="ignore;minlength=2;accept=[a-zA-Z0-9]{2};maxlength=2"
									maxlenght="2"
									label="Ufficio Statale:" name="anagente_ufficioStatale"
									text="${requestScope.anagente_ufficioStatale}"
									message="[accept=Ufficio Statale: ${msg_configurazione_alfanumerici}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />					
							
						</s:div>
						
						<s:div name="divElement11b" cssclass="divRicMetadatiSingleRow">
							<s:div name="divDataDa" cssclass="floatleft">
							<s:div name="divDataDa_scadenza" cssclass="divDataDa">
								<s:date label="Decorrenza:"  prefix="tx_data_pag_da" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="label85"
									cssclass="dateman" validator="required;dateISO" showrequired="true"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${tx_data_pag_da}"
									message="[dateISO=Decorrenza: ${msg_dataISO_valida}]">
								</s:date>
								<input type="hidden" id="tx_data_pag_da_hidden" value="" />
							</s:div>
							</s:div>
							</s:div>
							</s:div>
						</s:div>
					
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
	<s:div name="divRicercaTitleName" cssclass="divRicTitle">Anagrafica Ente</s:div>	

			<s:label name="lblCanc" text="${message}"
				cssclass="lblMessage" />
				<s:label name="lblCancmsg" text="Sei sicuro di voler cancellare il record selezionato?"
				cssclass="lblMessage" />
				
			<br />
			<br />
           
           
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

									<s:form name="indietro" action="anagente.do?action=search"
										method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

										<s:button id="button_indietro" cssclass="btnStyle"
											type="submit" text="Indietro" onclick="" />
									</s:form>


								</s:td>
								<s:td>

									<s:form name="form_Cancella"
										action="anagente.do?action=cancel&anagente_chiaveEnte=${requestScope.chiaveEnte}"
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
			method="post" action="anagente.do?action=search">
			<center><c:if test="${error != null}">
				<s:label name="lblErrore" text="${message}"/>
			</c:if> <c:if test="${error == null}">
				<s:label name="lblMessage" text="${message}"/>
			</c:if></center>
			<br />
			<br />
			<center>
			<s:form name="indietro" action="anagente.do?action=search"
						method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
						<s:button id="tx_button_stampa_pdf" onclick="" text="Indietro"
							cssclass="btnStyle" type="submit" />
					</s:form>
				
			</center>
		</s:form>

	</c:otherwise>
</c:choose>
</s:div>

