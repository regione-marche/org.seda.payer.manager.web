<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="costitransazionebanca" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>


<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Costi Transazione - Banca</s:div>

		<s:form name="form_ricerca" action="costitransazionebanca.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />

			<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
				<s:div name="divElement1" cssclass="divRicMetadatiCenterSmall">
					<s:textbox bmodify="true" name="costiTransazioneBanca_SearchDescrizioneSocieta"
						label="Societ&agrave;:" maxlenght="100"
						cssclasslabel="label65 bold textright"
						cssclass="textareaman" text="${costiTransazioneBanca_SearchDescrizioneSocieta}" />

				</s:div>
				<s:div name="divElement1" cssclass="divRicMetadatiCenterSmall">
					<s:textbox bmodify="true" name="costiTransazioneBanca_SearchDescrizioneUtente"
						label="Utente:" maxlenght="100"
						cssclasslabel="label65 bold textright"
						cssclass="textareaman" text="${costiTransazioneBanca_SearchDescrizioneUtente}" />

				</s:div>
				<s:div name="divElement2" cssclass="divRicMetadatiCenterSmall">
					<s:textbox bmodify="true" name="costiTransazioneBanca_SearchDescrizioneGateway"
						label="Gateway:" maxlenght="256"
						cssclasslabel="label65 bold textright"
						cssclass="textareaman" text="${costiTransazioneBanca_SearchDescrizioneGateway}"  />
				</s:div>
				
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					<s:button id="tx_button_cerca" onclick="" text="Cerca"
						type="submit" cssclass="btnStyle"/>
					<s:button id="tx_button_reset" onclick="" text="Reset"
						type="submit" cssclass="btnStyle" validate="false"/>
					
			</s:div>
		</s:form>
		
	</s:div>

	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Costi Transazione - Banca
	</s:div>
</s:div>
