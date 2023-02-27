<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="seusers_search" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>


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
		$("#dataScadenza_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#dataScadenza_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#dataScadenza_da_day_id").val(dateText.substr(0,2));
												$("#dataScadenza_da_month_id").val(dateText.substr(3,2));
												$("#dataScadenza_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("dataScadenza_da_day_id",
				                            "dataScadenza_da_month_id",
				                            "dataScadenza_da_year_id",
				                            "dataScadenza_da_hidden");
			}
		});
		$("#dataScadenza_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#dataScadenza_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#dataScadenza_a_day_id").val(dateText.substr(0,2));
												$("#dataScadenza_a_month_id").val(dateText.substr(3,2));
												$("#dataScadenza_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("dataScadenza_a_day_id",
				                            "dataScadenza_a_month_id",
				                            "dataScadenza_a_year_id",
				                            "dataScadenza_a_hidden");
			}
		});
	});
</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="search_form" action="seUsersSearch.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false" >
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Utenze
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_username"
							label="UserId&nbsp;(CF):" maxlenght="20" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_username}" />
					</s:div>

				</s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">

					
					<s:div name="divElement11" cssclass="divRicMetadatiDoubleRow">
						<s:div name="divElement11a" cssclass="labelData">
							<s:label name="label_data_scadenza" cssclass="seda-ui-label label85 bold textright"
								text="Data Scadenza" />
						</s:div>							
						<s:div name="divElement11b" cssclass="floatleft">
							<s:div name="divDataDa_scadenza" cssclass="divDataDa">
								<s:date label="Da:" prefix="dataScadenza_da" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${dataScadenza_da}"></s:date>
								<input type="hidden" id="dataScadenza_da_hidden" value="" />
							</s:div>
							<s:div name="divDataA_scadenza" cssclass="divDataA">
								<s:date label="A:" prefix="dataScadenza_a" yearbegin="${ddlDateAnnoDa}"
									cssclasslabel="labelsmall"
									cssclass="dateman"
									yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
									separator="/" calendar="${dataScadenza_a}"></s:date>
								<input type="hidden" id="dataScadenza_a_hidden" value="" />
							</s:div>
						</s:div>
					</s:div>
				</s:div>

				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">

					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_utenzaattiva" disable="false"
							cssclass="tbddlMax floatleft" label="Stato Utenza:"
							cssclasslabel="label85 bold textright"
							valueselected="${tx_utenzaattiva}">
							<s:ddloption text="Tutte le Utenze" value="" />
							<s:ddloption text="Utenze Attive" value="Y" />
							<s:ddloption text="Utenze non Attive" value="N" />
						</s:dropdownlist>
					</s:div>

				</s:div>
				
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button  id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
			</s:div>
	</s:form>

	</s:div>
	
</s:div>

<c:if test="${!empty listaUsers}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill">
			Elenco Utenze
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="listaUsers"  
			action="seUsersSearch.do?vista=seusers_search" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

		<!-- USERID -->
			<s:dgcolumn label="UserId&nbsp;(CF)" asc="SE_USERID_A" desc="SE_USERID_D" >
				<s:hyperlink href="../adminusers/usersSearch.do?tx_username={5}&tx_button_cerca=cerca" 
					 cssclass="blacklink" text="{5}" alt="Profili associati a {5}" />
			</s:dgcolumn>

		<!-- DATA DI INSERIMENTO UTENZA -->
			<s:dgcolumn index="1" label="Data<br/>Inserimento" asc="SE_INS_A" desc="SE_INS_D" format="dd/MM/yyyy HH:mm:ss" css="textcenter"/>
			
		<!-- DATA DI ATTIVAZIONE UTENZA -->
			<s:dgcolumn index="2" label="Data<br/>Attivazione" asc="SE_INIZ_A" desc="SE_INIZ_D" format="dd/MM/yyyy HH:mm:ss" css="textcenter"/>

		<!-- DATA DI SCADENZA -->
			<s:dgcolumn index="3" label="Data<br/>Scadenza" asc="SE_FINE_A" desc="SE_FINE_D" format="dd/MM/yyyy HH:mm:ss" css="textcenter"/>

		<!-- UTENTE ATTIVO/INATTIVO -->
			<s:dgcolumn label="Attivo" asc="SE_ATTI_A" desc="SE_ATTI_D" css="textcenter">
				<!-- ATTIVO -->
				<s:if right="{4}" control="eq" left="Y" >
					<s:then>
						<s:image height="16" width="16" alt="User {5} attivo" 
							src="../applications/templates/adminusers/img/circle_green.png"/>
					</s:then>
				</s:if>
				<!-- INATTIVO -->
				<s:if right="{4}" control="eq" left="N" >
					<s:then>
						<s:image height="16" width="16" alt="User {5} non attivo" 
							src="../applications/templates/adminusers/img/circle_red.png"/>
					</s:then>
				</s:if>
			</s:dgcolumn>

			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;" css="textcenter">
		<!-- MODIFICA UTENTE -->
						<s:hyperlink
							cssclass="hlStyle" 
							href="seUsersEdit.do?tx_username_hidden={5}"
							imagesrc="../applications/templates/shared/img/modifica.gif"
							alt="Modifica User {5}" text="" />
		<!-- SBLOCCO UTENTE -->
				
				<%-- <s:if right="{6}" control="eq" left="1">
					<s:then> 
						<s:hyperlink
							cssclass="hlStyle" 
							href="seUsersSblocca.do?tx_username={5}"
							imagesrc="../applications/templates/adminusers/img/chain.png"
							alt="Sblocca User {5}" text=""   />
					</s:then>
				</s:if> --%>
			</s:dgcolumn>
			
		</s:datagrid>
	</s:div>
	<s:div name="div_riepilogo" cssclass="div_align_center divRiepilogo">
		<s:div name="divRicercaLeft1" cssclass="divRicMetadatiLeft">
		&nbsp;
		<br/>
		&nbsp;
		</s:div>

		<s:div name="divRicercaCenter1" cssclass="divRicMetadatiCenter">
			<s:div name="divRicercaFillName1" cssclass="divRicercaFill">
					Riepilogo Statistico
			</s:div>
			<s:datagrid viewstate="true" cachedrowset="riepilogoUtenze"  
				action="seUsersSearch.do" border="1" usexml="true" >
				<s:dgcolumn index="1" label="Utenze" />
				<s:dgcolumn index="2" label="Valore"/>
			</s:datagrid>
		</s:div>
	</s:div>

</c:if>
