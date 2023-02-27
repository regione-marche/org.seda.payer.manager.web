<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="notificheeclista" encodeAttributes="true" />


<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js" type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js" type="text/javascript"></script>

<script type="text/javascript">

	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();
	
	$(function() {
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );

		$("#data_invio_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_invio_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_invio_da_day_id").val(dateText.substr(0,2));
												$("#data_invio_da_month_id").val(dateText.substr(3,2));
												$("#data_invio_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_invio_da_day_id",
				                            "data_invio_da_month_id",
				                            "data_invio_da_year_id",
				                            "data_invio_da_hidden");
			}
		});
		$("#data_invio_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#data_invio_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#data_invio_a_day_id").val(dateText.substr(0,2));
												$("#data_invio_a_month_id").val(dateText.substr(3,2));
												$("#data_invio_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("data_invio_a_day_id",
				                            "data_invio_a_month_id",
				                            "data_invio_a_year_id",
				                            "data_invio_a_hidden");
			}
		});

	});
</script>

<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="form_selezione" action="ecnotifiche.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA NOTIFICHE ESTRATTO CONTO</s:div>
		
		<s:div name="divRicMetadati" cssclass="divRicMetadati">
<!-- riga TOP -->			
			
			<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist name="ddlSocietaUtenteEnte" label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label85 bold textright" validator="ignore;" cssclass="floatleft" disable="false"
									onchange="setFired();this.form.submit();" cachedrowset="listaSocietaUtenteEnte" usexml="true" 
									valueselected="${ddlSocietaUtenteEnte}">
   						<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
						<s:ddloption value="{1}|{2}|{3}|{4}|{5}" text="{5} / {6} / {4}"/>
					</s:dropdownlist>
				
				</s:div>					
			</s:div>	

<!-- colonna sinistra -->			

			<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

				<s:div name="divElement1A" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" name="tx_codfisc"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							label="Codice Fiscale:" maxlenght="16"
							text="${tx_codfisc}" />
				</s:div>
				<s:div name="divElement2A" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" name="tx_email"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							label="Mail:" maxlenght="30"
							text="${tx_email}" />
				</s:div>
				<s:div name="divElement1C" cssclass="divRicMetadatiSingleRow">
					 <s:dropdownlist name="tx_tipologiaservizio"
                					 valueselected="${tx_tipologiaservizio}"
                					 label="Tip. Servizio:" disable="false"
                					 cssclasslabel="label85 bold  textright"
                					 cssclass="tbddl "
                					 usexml="false">
                		<s:ddloption text="Tutti" value="" />
                		<s:ddloption text="Avviso Revoca" value="R" />
                		<s:ddloption text="Avviso nuovo Documento" value="D" />
                		<s:ddloption text="Avviso variazione stato Rateazione" value="P" />
              		</s:dropdownlist>
            	</s:div>

			</s:div>

<!-- colonna centro -->			
			
			<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
				
				<s:div name="divElement1B" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" name="tx_numdocumento"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							label="Nr. Documento:" maxlenght="20"
							validator="ignore"
							text="${tx_numdocumento}" />
				</s:div>
				<s:div name="divElement2B" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" name="tx_emailpec"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							label="Mail PEC:" maxlenght="30"
							text="${tx_emailpec}" />
				</s:div>
				<s:div name="divElement1C" cssclass="divRicMetadatiSingleRow">
					 <s:dropdownlist name="tx_tiponotifica"
                					 valueselected="${tx_tiponotifica}"
                					 label="Tipo Notifica:" disable="false"
                					 cssclasslabel="label85 bold  textright"
                					 cssclass="tbddl "
                					 usexml="false">
                		<s:ddloption text="Tutti" value="" />
                		<s:ddloption text="Mail" value="M" />
                		<s:ddloption text="Mail PEC" value="P" />
                		<s:ddloption text="SMS" value="V" />
              		</s:dropdownlist>
            	</s:div>
			
			</s:div>

<!-- colonna destra -->		

			<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
				<s:div name="divElement2C" cssclass="divRicMetadatiSingleRow">
					<s:textbox bmodify="true" name="tx_sms"
							cssclass="textareaman"
							cssclasslabel="label85 bold textright"
							label="Nr. Telefono:" maxlenght="10"
							validator="ignore;accept=^[0-9]{1,18}$"
							text="${tx_sms}" />
				</s:div>
				<s:div name="divElement51" cssclass="divRicMetadatiDoubleRow">
					<s:div name="divElement51a" cssclass="labelData">
						<s:label name="label_data_invio" cssclass="seda-ui-label label85 bold textright" text="Data invio" />
					</s:div>
					<s:div name="divElement51b" cssclass="floatleft">
						<s:div name="divDatainvioDa" cssclass="divDataDa">
							<s:date label="Da:" prefix="data_invio_da" yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
										separator="/" calendar="${data_invio_da}"
										cssclasslabel="labelsmall"
										cssclass="dateman">
							</s:date>
							<input type="hidden" id="data_invio_da_hidden" value="" />
						</s:div>
						<s:div name="divDatainvioA" cssclass="divDataA">
							<s:date label="A:" prefix="data_invio_a" yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
										separator="/" calendar="${data_invio_a}"
										cssclasslabel="labelsmall"
										cssclass="dateman" >
							</s:date>
							<input type="hidden" id="data_invio_a_hidden" value="" />
						</s:div>
					
					</s:div>
				</s:div>
			</s:div>
			<br />

<!-- riga 3 -->			

					
		</s:div>
		
		<!-- bottoni -->
		<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
			<s:textbox name="fired_button_hidden" label="fired_button_hidden" bmodify="true" text="" cssclass="rend_display_none" cssclasslabel="rend_display_none" />
			<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
			<s:button id="tx_button_reset" onclick="" text="Reset" validate="false" type="submit" cssclass="btnStyle" />
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

<c:if test="${!empty lista_notifiche}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">ELENCO NOTIFICHE ESTRATTO CONTO</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_notifiche" action="ecnotifiche.do?vista=NotificheLista" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">
			<s:dgcolumn index="14" label="Data Invio Notifica" asc="DTIN_A" desc="DTIN_D"></s:dgcolumn>
			<s:dgcolumn label="Tipo Notifica"                 asc="TPNO_A" desc="TPNO_D">
				<s:if right="{6}" control="eq" left="M">
					<s:then>Mail</s:then>
				</s:if>
				<s:if right="{6}" control="eq" left="P">
					<s:then>Mail PEC</s:then>
				</s:if>
				<s:if right="{6}" control="eq" left="V">
					<s:then>SMS</s:then>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn label="Tipo Servizio"                 asc="TPSE_A" desc="TPSE_D">
				<s:if right="{7}" control="eq" left="R">
					<s:then>Avviso Revoca</s:then>
				</s:if>
				<s:if right="{7}" control="eq" left="D">
					<s:then>Avviso nuovo Documento</s:then>
				</s:if>
				<s:if right="{7}" control="eq" left="P">
					<s:then>Avviso variazione stato Rateazione</s:then>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn index="5" label="Fiscale"             asc="CFIS_A" desc="CFIS_D"></s:dgcolumn>
			<s:dgcolumn index="8" label="Nr. Documento"       asc="NRDO_A" desc="NRDO_D"></s:dgcolumn>
			<s:dgcolumn index="9" label="Email"               asc="MAIL_A" desc="MAIL_D"></s:dgcolumn>
			<s:dgcolumn index="10" label="Email PEC"          asc="MAIP_A" desc="MAIP_D"></s:dgcolumn>
			<s:dgcolumn index="11" label="Telef."             asc="TELE_A" desc="TELE_D"></s:dgcolumn>
			<s:dgcolumn label="Esito Notif."                  asc="ESNO_A" desc="ESNO_D">
				<s:if right="{12}" control="eq" left="0">
					<s:then>Primo Invio</s:then>
				</s:if>
				<s:if right="{12}" control="eq" left="1" operator="or" 
				secondleft="{12}" secondcontrol="eq" secondright="I">
					<s:then>Inviata</s:then>
				</s:if>
				<s:if right="{12}" control="eq" left="S">
					<s:then>Esito Positivo</s:then>
				</s:if>
				<s:if right="{12}" control="eq" left="N">
					<s:then>Esito Negativo</s:then>
				</s:if>
			</s:dgcolumn>
			<s:dgcolumn label="Data Esito notifica"           asc="DTNO_A" desc="DTNO_D"></s:dgcolumn>
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;">
				<s:if right="{6}" control="ne" left="V">
					<s:then>
						<s:hyperlink href="ecnotifiche.do?chiaveTabellaNotifica={1}&tx_button_download=OK"					
									 imagesrc="../applications/templates/shared/img/pdf.png"
									 alt="Dettaglio Pdf Notifica" text="" />
					 </s:then>
			 	</s:if>
			</s:dgcolumn>
		</s:datagrid>
	</s:div>
</c:if>
