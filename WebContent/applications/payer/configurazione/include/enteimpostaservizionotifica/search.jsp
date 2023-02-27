<%--  **  IMPORT JAVA TAG LIB  **  --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>

<%--  **  My View State  **  --%>
<m:view_state id="listaRecDB" encodeAttributes="true" />

<!-- ** JQuery Functions Library ** -->
<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js" type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js" type="text/javascript"></script>
<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_ricerca" action="enteImpostaServizioNotifica.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />
			<input type="hidden" name="reset" value="false" />
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Ente-Imposta Servizio Notifica</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:dropdownlist name="enteimpostaservizionotifica_searchuserCode"
							disable="${enableListaUtenti}" cssclass="tbddl floatleft"
							label="Utente:" cssclasslabel="label65 bold textright"
							cachedrowset="listaUtenti" usexml="true"
							onchange="this.form.submit();"
							valueselected="${enteimpostaservizionotifica_searchuserCode}">
							<s:ddloption text="Tutti gli Utenti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_utente_changed"
								disable="${enableListaUtenti}" onclick="" text=""
								type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false"
								 />
						</noscript>
					</s:div>
					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
						<s:dropdownlist name="enteimpostaservizionotifica_searchchiaveEnte"
							disable="${enableListaEnti}" 
							cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright"
							label="Ente:" 
							cachedrowset="listaEntiGenerici" usexml="true"
							valueselected="${enteimpostaservizionotifica_searchchiaveEnte}">
							<s:ddloption text="Tutti gli Enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:textbox bmodify="true" maxlenght="4" validator="ignore;minlength=2;maxlength=4;"
							label="Cod. Imp. Serv.:" name="enteimpostaservizionotifica_searchcodiceImpostaServizio"
							text="${enteimpostaservizionotifica_serarchcodiceImpostaServizio}" bpassword="false" 
							cssclasslabel="label85 bold textright" cssclass="textareaman" />
					</s:div>
				</s:div>
				<s:div name="divElement4" cssclass="divRicMetadatiLeft">
					<s:dropdownlist name="enteimpostaservizionotifica_searchflagNotificaAllegato" disable="false"
						label="Flag Allegati su Notifica:"
						valueselected="${enteimpostaservizionotifica_searchflagNotificaAllegato}"
						cssclass="tbddl floatleft"
						cssclasslabel="label85 bold floatleft textright">
						<s:ddloption text="Tutte" value="" />
						<s:ddloption value="Y" text="Abilitato" />
						<s:ddloption value="N" text="NON Abilitato" />
					</s:dropdownlist>
				</s:div>
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" text="Reset" type="submit" cssclass="btnStyle" validate="false" />
				<s:button id="tx_button_nuovo" onclick="" text="Nuovo" type="submit" cssclass="btnStyle" validate="false" />
			</s:div>
		</s:form>
	</s:div>
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">Elenco Ente-Imposta Servizio Notifica</s:div>
</s:div>
