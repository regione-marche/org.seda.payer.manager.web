<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="abilitazionediscarichi" encodeAttributes="true" />

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



<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_selezione"
			action="abilitazionediscarichi.do?vista=abilitazionediscarichi"
			method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA CONFIGURAZIONE RACCORDO PAGONET
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						<!--<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label85 bold textright" validator="ignore;"
									cssclass="floatleft" disable="${codop == 'edit'}"
									onchange="setFired();this.form.submit();"
									   cachedrowset="listaSocietaUtenteEnte" usexml="true" 
									   valueselected="${ddlSocietaUtenteEnte}">
   							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
							<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}"/>
						</s:dropdownlist>-->

						<s:dropdownlist name="ddlSocietaUtenteEnte"
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
							cssclasslabel="label160 bold textright floatleft"
							validator="ignore;" cssclass="seda-ui-ddl tbddlMax780 floatleft"
							disable="${codop == 'edit'}"
							onchange="setFiredButton();this.form.submit();"
							cachedrowset="listaSocietaUtenteEnte" usexml="true"
							valueselected="${ddlSocietaUtenteEnte}">
							<s:ddloption text="Selezionare uno degli elementi della lista"
								value="" />
							<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}" />
						</s:dropdownlist>

						<c:if test="${codop == 'add'}">
							<noscript>
								<s:button id="tx_button_societa_changed" onclick="" text=""
									validate="false" type="submit" cssclass="btnimgStyle"
									title="Aggiorna" />
							</noscript>
						</c:if>
					</s:div>
				</s:div>
			</s:div>

			<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
				<s:dropdownlist name="tx_codice_tributo2" disable="false"
					label="Onere/tributo:" multiple="false"
					valueselected="${tx_codice_tributo2}"
					cssclasslabel="label85 bold textright"
					cssclass="tbddlMax floatleft">
					<s:ddloption text="seleziona..." value="" />
					<s:ddloptionbinder options="${tributiDDLList}" />
				</s:dropdownlist>
			</s:div>
			<!-- 
			<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">					
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_tipologia_servizio" disable="" label="Servizio:"
							 multiple="false" valueselected="${tx_tipologia_servizio}" 
							 cssclasslabel="label85 bold textright" cssclass="tbddlMax floatleft">
						 	<s:ddloption text="Tutti" value="" />
							<s:ddloptionbinder options="${serviziDDLList}"/>
						</s:dropdownlist>
					</s:div>
				</s:div>
			 -->

			<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
				<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					<s:dropdownlist name="tx_flag_servizio" disable=""
						label="Discaricabile:" multiple="false"
						valueselected="${tx_flag_servizio}"
						cssclasslabel="label85 bold textright"
						cssclass="tbddlMax floatleft">
						<s:ddloption text="seleziona..." value="" />
						<s:ddloptionbinder options="${flagDDLList}" />
					</s:dropdownlist>
				</s:div>
			</s:div>



			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit"
					cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick=""
					text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_add" validate="false" onclick=""
					text="Nuovo" type="submit" cssclass="btnStyle" />

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

<c:if test="${!empty lista_abilitazione_discarichi}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ABILITAZIONE DISCARICHI
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="" cachedrowset="lista_abilitazione_discarichi"
			action="abilitazionediscarichi.do?vista=abilitazionediscarichi"
			border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

			<s:dgcolumn index="4" label="Codice tributo" css="text_align_left" />
			<s:dgcolumn index="5" label="Descrizione" css="text_align_left" />
			<s:dgcolumn label="Abilitato" css="text_align_left">
				<s:if right="{6}" control="eq" left="Y">
					<s:then>
			    SI
			  </s:then>
					<s:else>
			    NO
			  </s:else>
				</s:if>
			</s:dgcolumn>

			<s:dgcolumn label="Azioni">
				<s:hyperlink
					href="abilitazionediscarichiEdit.do?vista=abilitazionediscarichi&codop=edit&ddlSocietaUtenteEnte={1}|{2}|{3}&tx_codice_tributo2={4}&tx_descrizione_tributo={5}&tx_flag_servizio={6}"
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Modifica" text="" cssclass="blacklink hlStyle" />
				<s:hyperlink
					href="abilitazionediscarichiEdit.do?vista=abilitazionediscarichi&tx_button_cancel=&ddlSocietaUtenteEnte={1}|{2}|{3}&tx_codice_tributo2={4}&tx_descrizione_tributo={5}&tx_flag_servizio={6}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Cancella" text="" cssclass="blacklink hlStyle" />
			</s:dgcolumn>
		</s:datagrid>
	</s:div>


</c:if>





