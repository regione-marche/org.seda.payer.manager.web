<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="impostaSoggiornoAnagStruttura_search" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="search_form" action="ImpostaSoggiornoAnagStrutturaSearch.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA ANAGRAFICHE STRUTTURE
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				
					<s:div name="divElement1" cssclass="divRicMetadatiLeft">
						<s:textbox bmodify="true" name="descrizioneComune"
								label="Comune:" maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${descrizioneComune}" message="[accept=Comune: ${msg_configurazione_descrizione_regex}]"/>
					</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:textbox bmodify="true" name="numeroAutorizzazione"
								label="Num. Autor.: " maxlenght="100" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore"
								text="${numeroAutorizzazione}" message="[accept=Numero Autorizzazione: ${msg_configurazione_descrizione_regex}]"/>
					</s:div>
					
					<s:div name="divElement3" cssclass="divRicMetadatiRight">
						<s:textbox bmodify="true" name="codiceFiscale"
								label="Cod.Fisc./P.IVA:" maxlenght="16" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore"
								text="${codiceFiscale}" message="[accept=Codice Fiscale: ${msg_configurazione_descrizione_regex}]"/>
					</s:div>
				</s:div>

					
					<s:div name="divLeft" cssclass="divRicMetadatiLeft">
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="descrizioneInsegna"
								label="Insegna:" maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_ragionesociale_regex}"
								text="${descrizioneInsegna}" message="[accept=Insegna: ${msg_configurazione_ragionesociale_regex}]"/>
						</s:div>	
					</s:div>
					
					<s:div name="divCenter" cssclass="divRicMetadatiCenter">
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="strutturaRicettiva"
									disable="false" 
									label="Tipol. Strut.:"
									cssclasslabel="label85 bold textright"
									cssclass="tbddl"
									cachedrowset="listStrutture" usexml="true"
									validator="ignore" showrequired="true"
									valueselected="${strutturaRicettiva}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption  value="{1}" text="{2}"/>
								</s:dropdownlist>
						</s:div>
					</s:div>
					
					<s:div name="divRight" cssclass="divRicMetadatiRight">
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							
						</s:div>
					</s:div>
			</s:div>	
				<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_nuovo" validate="false"  onclick="" text="Nuovo" type="submit" cssclass="btnStyle" />
				<c:if test="${!empty listaConfig}">
					<s:button id="tx_button_download" validate="false" onclick="" text="Download" type="submit" cssclass="btnStyle" />
				</c:if>
			</s:div>
	</s:form>

	</s:div>
	
</s:div>

<c:if test="${!empty listaConfig}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO ANAGRAFICHE STRUTTURE
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="listaConfig"  
			action="ImpostaSoggiornoAnagStrutturaSearch.do?vista=impostaSoggiornoAnagStruttura_search" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

		<!-- DESCRIZIONE COMUNE -->
			<s:dgcolumn label="Comune" index="3" />
			
		<!-- NUMERO AUTORIZZAZIONE -->
			<s:dgcolumn label="Nr. Autorizzazione" index="4" />

		<!-- DESCRIZIONE CODICE FISCALE --> 
			<s:dgcolumn label="Codice Fiscale/ P.IVA" index="5" />
		
		<!-- DESCRIZIONE INSEGNA --> 
			<s:dgcolumn label="Insegna" index="6" />

		<!-- DESCRIZIONE STRUTTURA RICETTIVA --> 
			<s:dgcolumn label="Tipologia Struttura" index="7" />

		<!-- AZIONI -->
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">

		<!-- MODIFICA RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="ImpostaSoggiornoAnagStrutturaEdit.do?tx_comune={2}&tx_anag_struttura={1}&tx_provincia={8}"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />
		<!-- CANCELLAZIONE RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="ImpostaSoggiornoAnagStrutturaCancel.do?tx_comune={2}&tx_anag_struttura={1}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Elimina" text=""   />
			</s:dgcolumn>
			
		</s:datagrid>
	</s:div>

</c:if>
