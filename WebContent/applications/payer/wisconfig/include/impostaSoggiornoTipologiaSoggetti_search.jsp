<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="impostaSoggiornoTipologiaSoggetti_search" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="search_form" action="ImpostaSoggiornoTipologiaSoggettiSearch.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA TIPOLOGIE SOGGETTI
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				
					<s:div name="divElement1" cssclass="divRicMetadatiLeft">
						<s:textbox bmodify="true" name="descrizioneComune"
								label="Comune: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${descrizioneComune}" message="[accept=Comune: ${msg_configurazione_descrizione_regex}]"/>
					</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="descrizioneSoggetto"
								label="Soggetto: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${descrizioneSoggetto}" message="[accept=Soggetto: ${msg_configurazione_descrizione_regex}]"/>
						</s:div>	
							
					</s:div>
					
					<s:div name="divElement3" cssclass="divRicMetadatiRight">
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist label="Esente:"
								cssclasslabel="label85 bold textright" cssclass="textareaman"
								name="esenzione" disable="false"
								valueselected="${esenzione}">
								<s:ddloption value="" text="Selezionare uno degli elementi" />
								<s:ddloption value="Y" text="Si" />
								<s:ddloption value="N" text="No" />
							</s:dropdownlist>
						</s:div>
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
			ELENCO TIPOLOGIE SOGGETTI
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="listaConfig"  
			action="ImpostaSoggiornoTipologiaSoggettiSearch.do?vista=impostaSoggiornoTipologiaSoggetti_search" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

		<!-- DESCRIZIONE COMUNE -->
			<s:dgcolumn label="Comune" index="3" />
			
			<s:dgcolumn label="Codice" index="7" />
			
		<!-- DESCRIZIONE SOGGETTO -->
			<s:dgcolumn label="Soggetto" index="4" />

		<!-- DESCRIZIONE ESENTE --> 
			<s:dgcolumn label="Esente" index="5" />

		<!-- AZIONI -->
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">

		<!-- MODIFICA RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="ImpostaSoggiornoTipologiaSoggettoEdit.do?tx_comune={2}&tx_chiave_soggetti={1}&tx_provincia={6}"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />
		<!-- CANCELLAZIONE RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="ImpostaSoggiornoTipologiaSoggettoCancel.do?tx_comune={2}&tx_chiave_soggetti={1}&tx_provincia={6}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Elimina" text=""   />
			</s:dgcolumn>
			
		</s:datagrid>
	</s:div>

</c:if>
