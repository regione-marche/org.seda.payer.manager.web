<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>

<m:view_state id="anagprovcom" encodeAttributes="true"/>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	
	
		<s:form name="form_ricerca" action="anagprovcom.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />
			
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Anagrafica Provincia Comune</s:div>
			
			<s:div name="divRicercaTitleName1" cssclass="divRicMetadati">
				<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:textbox bmodify="true" name="anagprovcom_codiceBelfiore"
							label="Cod. Belfiore:" maxlenght="4"
							cssclasslabel="label85 bold textright" cssclass="textareaman"
							text="${anagprovcom_codiceBelfiore}" />

					</s:div>
					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
						<s:textbox bmodify="true" name="anagprovcom_codiceProvincia"
							label="Provincia:" maxlenght="3"
							cssclasslabel="label65 bold textright" cssclass="textareaman"
							text="${anagprovcom_codiceProvincia}" />

					</s:div>
					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:textbox bmodify="true" name="anagprovcom_codiceComune"
							label="Comune:" maxlenght="3"
							cssclasslabel="label65 bold textright" cssclass="textareaman"
							text="${anagprovcom_codiceComune}" />

					</s:div>
				</s:div>
				<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
					
					<s:button id="tx_button_cerca" onclick="" text="Cerca"
						type="submit" cssclass="btnStyle"/>
					<s:button id="tx_button_reset" onclick="" text="Reset"
						type="submit" cssclass="btnStyle"/>
					
				</s:div>
			</s:div>
		</s:form>
		</s:div>
	
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Anagrafica Provincia Comune
	</s:div>
</s:div>
