<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="AbilitaCanalePagamentoTipoServizioEnte_var" encodeAttributes="true" />

<script type="text/javascript">
	function setFired() {
			var buttonFired = document.getElementById('fired_button_hidden');
			buttonFired.value = 'tx_button_societa_changed';
	}
</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_form" action="${do_command_name}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">ABILITAZIONE PER CANALE PAGAMENTO - TIPOLOGIA SERVIZIO - ENTE</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">ABILITAZIONE PER CANALE PAGAMENTO - TIPOLOGIA SERVIZIO - ENTE</s:div>
			</c:if>
									
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<%-- TODO da verificare la ddl che segue ripresa dal prototipo e non dalla pagina originale di riferimento --%>
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTopDouble">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label160 bold textright floatleft" validator="required;"
									cssclass="tbddlMax780 floatleft" disable="${codop == 'edit'}"
									onchange="setFired();this.form.submit();"
									   cachedrowset="listaSocietaUtenteEnte" usexml="true" 
									   valueselected="${ddlSocietaUtenteEnte}">
   							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
							<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}"/>
						</s:dropdownlist>
						<c:if test="${codop == 'add'}">
							<noscript>
								<s:button id="tx_button_societa_changed" 
									onclick="" text="" 
									validate="false"
									type="submit" cssclass="btnimgStyle"  title="Aggiorna" />
							</noscript>
						</c:if>
						<!--<s:dropdownlist name="ddlSocietaUtenteServizioEnte"  
							label="Società/Utente/Ente/Serv.: "
									cssclasslabel="label85 bold" validator="ignore;"
									cssclass="" disable="${codop == 'edit'}"
									   cachedrowset="listaSocietaUtenteServizioEnte" usexml="true" 
									   valueselected="${ddlSocietaUtenteServizioEnte}">
   							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
							<s:ddloption value="{1}|{2}|{3}|{4}" text="{16} / {17} / {15} / {18}"/>
						</s:dropdownlist>	-->					
					</s:div>
					<s:div name="divElement1top1" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="ddlTipologiaServizio"
								disable="${codop == 'edit'}"
								label="Tipologia Servizio:"
								cssclass=""
								cssclasslabel="label160 bold textright"
								cachedrowset="listaTipologieServizio" usexml="true"
								validator="required" showrequired="true"
								valueselected="${ddlTipologiaServizio}">
							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
							<s:ddloption  value="{1}" text="{1}/{2}"/>
						</s:dropdownlist>
					</s:div>
				</s:div>

				<s:div name="divRicercaLeft" cssclass="floatleft">
				<!-- CES_KCANKCAN CANALE PAGAMENTO -->
					<s:div name="divElement50" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="ddlCanalePagamento" 
								label="Canale Pag.: " 
										cssclasslabel="label163 bold textright"
										cssclass="textareaman" disable="${codop == 'edit'}"
										   cachedrowset="listaCanaliPagamentoAbilitazione" usexml="true" 
										   validator="required" showrequired="true"
										   valueselected="${ddlCanalePagamento}">
								<%-- <s:ddloption text="Selezionare uno degli elementi della lista" value="" /> --%>
								<s:ddloption text="Seleziona" value="" />
								<s:ddloption value="{1}" text="{2}"/>
						</s:dropdownlist>
					</s:div>

				</s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
				<!-- CES_FCESFATT FLAG ATTIVAZIONE -->				
					<s:div name="divElement50" cssclass="divRicMetadatiSingleRow">
						<s:list bradio="false" bchecked="${chk_flagAttivazione}" validator="ignore;" 
						 cssclasslabel="bold checklabel" cssclass="checkleft"
						name="flagAttivazione" groupname="flagAttivazione" 
						text="Attivazione" value="Y" />
					</s:div>
					
				</s:div>

			</s:div>
			<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_indietro" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
				<c:if test="${codop == 'add'}">
					<s:button id="tx_button_aggiungi" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${codop == 'edit'}">
					<s:button id="tx_button_edit" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
			</s:div>
			<input type="hidden" name="codop" value="${codop}" />
			<input type="hidden" name="tx_societa" value="${tx_societa}" />
			<input type="hidden" name="tx_utente" value="${tx_utente}" />
			<input type="hidden" name="tx_ente" value="${tx_ente}" />
			<input type="hidden" name="tx_canalePagamento" value="${tx_canalePagamento}" />
			<input type="hidden" name="tx_codiceTipologiaServizio" value="${tx_codiceTipologiaServizio}" />
	</s:form>
	</s:div>
	
</s:div>
