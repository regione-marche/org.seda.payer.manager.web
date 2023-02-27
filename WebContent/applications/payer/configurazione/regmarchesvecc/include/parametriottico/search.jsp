<%--  **  IMPORT JAVA TAG LIB  **  --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>

<%--  **  My View State  **  --%>
<m:view_state id="listaOttico" encodeAttributes="true" />

<!-- ** JQuery Functions Library ** -->
<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js" type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js" type="text/javascript"></script>
<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_ricerca" action="parametriOttico.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />
			<input type="hidden" name="reset" value="false" />
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Parametri Ottico</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
					<%-- SOCIETA --%>
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:dropdownlist name="codiceSocieta" disable="${enableListaSocieta}"
							cssclass="tbddl floatleft" label="Societ&agrave;:"
							cssclasslabel="label85 bold floatleft textright"
							cachedrowset="listaSocieta" usexml="true"
							onchange="this.form.submit();"
							valueselected="${codiceSocieta}">
							<s:ddloption text="Tutte le Societ&agrave;" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_societa_changed" 
								disable="${enableListaSocieta}" onclick="" text="" 
								type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
						</noscript>
					</s:div>
					<%-- PROVINCIA --%>
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:dropdownlist name="parametriottico_siglaProvincia"
							disable="${enableListaProvince}" cssclass="tbddl floatleft"
							label="Provincia:"
							cssclasslabel="label65 bold floatleft textright"
							cachedrowset="listaProvince" usexml="true"
							onchange="this.form.submit();"
							valueselected="${parametriottico_siglaProvincia}">
							<s:ddloption text="Tutte le Province" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_provincia_changed"
								disable="${enableListaProvince}" onclick="" text=""
								type="submit" cssclass="btnimgStyle" title="Aggiorna" validate="false"
								 />
						</noscript>
					</s:div>
					<%-- UTENTE --%>
					<s:div name="divElement3" cssclass="divRicMetadatiRight">
						<s:dropdownlist name="parametriottico_searchuserCode"
							disable="${enableListaUtenti}" cssclass="tbddl floatleft"
							label="Utente:" cssclasslabel="label65 bold textright"
							cachedrowset="listaUtenti" usexml="true"
							onchange="this.form.submit();"
							valueselected="${parametriottico_searchuserCode}">
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
				</s:div>
				<s:div name="divRicercaMetadatiLeft" cssclass="divRicMetadatiLeft">
					<%-- ENTE --%>
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="parametriottico_searchchiaveEnte"
							disable="${enableListaEnti}" 
							cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright"
							label="Ente:" 
							cachedrowset="listaEntiGenerici" usexml="true"
							valueselected="${parametriottico_searchchiaveEnte}">
							<s:ddloption text="Tutti gli Enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				<s:div name="divRicercaMetadatiCenter" cssclass="divRicMetadatiCenter">
					<%-- SORGENTE IMMAGINI --%>
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="parametriottico_searchsorgenteImmagini" disable="false"
							label="Sorg. Img.:"
							valueselected="${parametriottico_searchsorgenteImmagini}"
							cssclass="tbddl floatleft"
							cssclasslabel="label85 bold floatleft textright">
							<s:ddloption text="Tutte" value="" />
							<s:ddloption value="P" text="Accesso a PayER" />
							<s:ddloption value="A" text="Accesso Esterno" />
						</s:dropdownlist>
					</s:div>
				</s:div>
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" text="Reset" type="submit" cssclass="btnStyle" validate="false" />
				
			</s:div>
		</s:form>
	</s:div>
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">Elenco Parametri Ottico</s:div>
</s:div>
