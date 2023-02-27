<%--  **  IMPORT JAVA TAG LIB  **  --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>

<%--  **  My View State  **  --%>
<m:view_state id="listaGruppo" encodeAttributes="true" />

<!-- ** JQuery Functions Library ** -->
<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js" type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js" type="text/javascript"></script>

<!-- ** Page Functions ** -->
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="form_ricerca" action="gruppo.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />
			<input type="hidden" name="reset" value="false" />
			<input type="hidden" name="datePattern" value="yyyy-MM-dd" />
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Gruppi</s:div>
			<s:div name="divRicercaMetadatiName" cssclass="divRicMetadatiTop">
				<s:div name="divElement1" cssclass="divRicMetadatiLeft">
					<s:textbox bmodify="true"
					name="gruppo_searchcodiceGruppo" label="Codice:"
				    validator="ignore;accept=^[\d]{2}$"
				    message="[accept=Codice Tipo Ente: Stringa numerica di lunghezza 2]"								   
					maxlenght="2" 
					cssclasslabel="label85 bold textright"
					cssclass="textareaman"
					text="${gruppo_searchcodiceGruppo}" />
				</s:div>
				<s:div name="divElement2" cssclass="divRicMetadatiCenter">
					<s:textbox bmodify="true"
						name="gruppo_searchdescrizioneLinguaItaliana"
						label="Descrizione:" maxlenght="100"
						cssclasslabel="label85 bold textright"
						cssclass="textareaman"
						text="${gruppo_searchdescrizioneLinguaItaliana}" />
				</s:div>
				<s:div name="divElement3" cssclass="divRicMetadatiRight">
				</s:div>
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" text="Reset" type="submit" cssclass="btnStyle" validate="false" />
				<s:button id="tx_button_nuovo" onclick="" text="Nuovo" type="submit" cssclass="btnStyle" validate="false" />
			</s:div>
		</s:form>
	</s:div>
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">Elenco Gruppi</s:div>
</s:div>
