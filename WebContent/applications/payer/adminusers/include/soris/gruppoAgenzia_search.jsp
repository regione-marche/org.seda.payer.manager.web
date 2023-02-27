<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="seagenzie_search" encodeAttributes="true" />

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

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="search_form" action="gruppoAgenziaSearch.do?vista=seagenzie_search" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false" >
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Gruppo Agenzia
			</s:div>
			<s:div cssclass="seda-ui-div divRicMetadatiTop" name="divRicercaMetadatiTop"> 
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_codiceGruppoAgenzia"
							label="Cod. Agenzia:" maxlenght="3" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_codiceGruppoAgenzia}" />
					</s:div>
			
				</s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					
					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_descrizioneGruppoAgenzia"
							label="Descrizione:" maxlenght="50" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_descrizioneGruppoAgenzia}" />
					</s:div>
					
				</s:div>

			</s:div>
			</s:div>
			
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<br />
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button  id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_nuovo" onclick="" text="Nuovo" type="submit" cssclass="btnStyle" />
<!--				<c:if test="${!empty listaGruppiAgenzia}">-->
<!--					<s:button id="tx_button_download" validate="false" onclick="" text="Download" type="submit" cssclass="btnStyle" />-->
<!--				</c:if>-->
			</s:div>
	</s:form>

	</s:div>
	
</s:div>

<c:if test="${!empty listaGruppiAgenzia}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill">
			Elenco Gruppi Agenzia
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="listaGruppiAgenzia"  
			action="seAgenzieSearch.do?vista=seagenzie_search" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

		<%-- Codice Gruppo Agenzie --%> 
			<s:dgcolumn index="1" label="Codice Gruppo Agenzia"/>

		<%-- Descrizione Gruppo Agenzia --%>
			<s:dgcolumn index="2" label="Descrizione Gruppo Agenzia"/>
	
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;" css="textcenter">
		<%-- Modifica gruppo agenzia --%>
				<s:hyperlink
					cssclass="hlStyle" 
					href="gruppoAgenziaEdit.do?vista=seagenzie_search&codop=edit&tx_codiceGruppoAgenzia={1}"
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Modifica Gruppo {2}" text="" />
				<s:hyperlink
					cssclass="hlStyle" 
					href="gruppoAgenziaEdit.do?vista=seagenzie_search&tx_button_cancel=&tx_codiceGruppoAgenzia={1}"
					imagesrc="../applications/templates/shared/img/cancel.png"
					alt="Elimina Gruppo {2}" text="" />
		
			</s:dgcolumn>

		</s:datagrid>
	</s:div>

</c:if>
