<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="impostaSoggiornoConfigurazioneImposta_search" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="search_form" action="ImpostaSoggiornoConfigurazioneImpostaSearch.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA RACCORDI GESTIONALE
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				
					<s:div name="divElement1" cssclass="divRicMetadatiLeft">
						
							<s:textbox bmodify="false" name="descrizioneComune"
								label="Comune: " maxlenght="256" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								text="${descrizioneComune}" message="[accept=Comune: ${msg_configurazione_descrizione_regex}]"/>
						
					</s:div>
					
				</s:div>

			</s:div>
			<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_nuovo" validate="false"  onclick="" text="Nuovo" type="submit" cssclass="btnStyle" />
			</s:div>
	</s:form>

	</s:div>
	
</s:div>

<c:if test="${!empty listaConfig}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO RACCORDI GESTIONALE
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="listaConfig"  
			action="ImpostaSoggiornoConfigurazioneImpostaSearch.do?vista=impostaSoggiornoConfigurazioneImposta_search" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

		<!-- DESCRIZIONE COMUNE -->
			<s:dgcolumn label="Comune" index="5" />
			
		<!-- DESCRIZIONE SOCIETA -->
			<s:dgcolumn label="Societa" index="6" />

		<!-- DESCRIZIONE UTENTE --> 
			<s:dgcolumn label="Utente" index="7" />

		<!-- DESCRIZIONE ENTE --> 
		<s:dgcolumn  label="Ente" >
		  <s:if left="{8}{9}" control="eq" right="">
			<s:then>
			</s:then>
			<s:else>
				{8} {9}
			</s:else>
		 </s:if>		    							
		</s:dgcolumn>

		<!-- AZIONI -->
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">

		<!-- MODIFICA RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="ImpostaSoggiornoConfigurazioneImpostaEdit.do?tx_comune={1}&tx_ente={2}|{3}|{4}&tx_provincia={10}"
					imagesrc="../applications/templates/configurazione/img/edit.png"
					alt="Modifica" text="" />
		<!-- CANCELLAZIONE RECORD -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="ImpostaSoggiornoConfigurazioneImpostaCancel.do?tx_comune={1}&tx_ente={2}|{3}|{4}&tx_provincia={10}"
					imagesrc="../applications/templates/configurazione/img/cancel.png"
					alt="Elimina" text=""   />
			</s:dgcolumn>
			
		</s:datagrid>
	</s:div>

</c:if>
