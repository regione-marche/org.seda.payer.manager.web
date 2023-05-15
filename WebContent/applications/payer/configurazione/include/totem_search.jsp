<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>

<m:view_state id="totem" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<s:div name="divRicercaTopName" cssclass="divRicercaTop">

		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Tipologia Imposta Totem</s:div>

		<s:form name="form_ricerca" action="totem.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />

			<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">

				<s:div name="divElement1" cssclass="divRicMetadatiLeft">
					<s:textbox bmodify="true" name="codice_ente" label="Codice Ente:"
						maxlenght="6" cssclasslabel="label85 bold textright"
						cssclass="textareaman" text="${codice_ente}" />
				</s:div>

				<s:div name="divElement2" cssclass="divRicMetadatiLeft">
					<s:textbox bmodify="true" name="imposta_servizio"
						label="Imposta Servizio:" maxlenght="4"
						cssclasslabel="label85 bold textright" cssclass="textareaman"
						text="${imposta_servizio}" />
				</s:div>
				<s:div name="divElement3" cssclass="divRicMetadatiLeft">
					<s:dropdownlist label="Tipologia imposta:"
						cssclasslabel="label85 bold textright" cssclass="textareaman"
						name="tipologia_imposta" disable="false" valueselected="">
						<s:ddloption value="" text="" />
						<s:ddloption value="01" text="01-TARSU" />
						<s:ddloption value="02" text="02-COSAP" />
						<s:ddloption value="03" text="03-CIMP" />
						<s:ddloption value="04" text="04-INGIUNZIONI" />
						<s:ddloption value="05" text="05-VERBALI_CDS" />
						<s:ddloption value="06" text="06-SANZIONI_CDS" />
						<s:ddloption value="07" text="07-ICI_VIOLAZIONE" />
						<s:ddloption value="08" text="08-BORSELLINO" />
						<s:ddloption value="09" text="09-BOLLO" />
					</s:dropdownlist>
				</s:div>
			</s:div>

			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit"
					cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" text="Reset" type="submit"
					cssclass="btnStyle" />
				<s:button id="tx_button_nuovo" type="submit" text="Nuovo" onclick=""
					cssclass="btnStyle" />
			</s:div>
		</s:form>
	</s:div>

	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Tipologie Imposta Totem
	</s:div>
</s:div>
