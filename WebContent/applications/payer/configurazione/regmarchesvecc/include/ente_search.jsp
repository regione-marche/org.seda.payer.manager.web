<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="ente" encodeAttributes="true"/>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Enti - Consorzi</s:div>

		<s:form name="form_ricerca" action="ente.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />

			<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
				
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
					
					<s:textbox bmodify="true"
							name="ente_companyCode"
							label="Societ&agrave;:" maxlenght="256"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
							message="[accept=Societ&agrave;: ${msg_configurazione_descrizione_regex}]"
							text="${ente_companyCode}" />
		
		</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
						
						<s:textbox bmodify="true"
									label="Utente:"
									maxlenght="256"
									cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
									name="ente_strDescrUtente" text="${ente_strDescrUtente}"
									message="[accept=Utente: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold textright" />
					</s:div>
					
					<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
					
					<s:textbox bmodify="true"
							name="ente_strEnte"
							label="Ente:" maxlenght="256"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
							message="[accept=Ente: ${msg_configurazione_descrizione_regex}]"
							text="${ente_strEnte}" />
							
						
						
					</s:div>
					
					
					</s:div>
					<s:div name="divElement4" cssclass="divRicMetadatiLeft">
					<s:div name="divElement40" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist label="Tipo Ente:" name="ente_tipoEnte" disable="false"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" valueselected="${ente_tipoEnte}">
								<s:ddloption value="" text="" />
								<s:ddloption value="C" text="Consorzio" />
								<s:ddloption value="E" text="Ente" />
								</s:dropdownlist>
	
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
					
					<!--<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
						<s:button id="tx_button_cerca" onclick="" text="Cerca"
							type="submit" cssclass="btnStyle"/>
						<s:button id="tx_button_reset" onclick="" text="Reset"
							type="reset" cssclass="btnStyle"/>
					</s:div>
			
		--></s:form>
		
		<!--<s:form name="form_ricerca" action="bollettino.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="add" />
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Nuovo"
					type="submit" cssclass="btnStyle"/>
			</s:div>
		</s:form>
	--></s:div>

	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Enti  - Consorzi
	</s:div>
</s:div>
