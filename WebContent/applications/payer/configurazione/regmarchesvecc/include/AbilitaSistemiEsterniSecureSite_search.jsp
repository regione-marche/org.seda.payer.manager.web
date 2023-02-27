<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="AbilitaSistemiEsterniSecureSite_search" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="search_form" action="AbilitaSistemiEsterniSecureSiteSearch.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA ABILITAZIONI SISTEMI ESTERNI
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="urlAccesso"
								label="Url: " maxlenght="512" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;url"
								text="${urlAccesso}" />
						</s:div>
					</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="descrizione"
								label="Descrizione: " maxlenght="100" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione_regex100}"
								text="${descrizione}" message="[accept=Descrizione: ${msg_configurazione_descrizione_regex}]"/>
						</s:div>
					</s:div>
					
					<s:div name="divElement3" cssclass="divRicMetadatiRight">
						<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="idServizio"
								label="Id. Servizio: " maxlenght="64" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=^\w{1,64}$"
								text="${idServizio}" message="[accept=Id. Servizio: ${msg_configurazione_alfanumerici}]"/>
						</s:div>
					</s:div>
					
				</s:div>

			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				
			</s:div>
	</s:form>

	</s:div>
	
</s:div>

<c:if test="${!empty listaConfig}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO ABILITAZIONI SISTEMI ESTERNI
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="listaConfig"  
			action="AbilitaSistemiEsterniSecureSiteSearch.do?vista=AbilitaSistemiEsterniSecureSite_search" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

		<!--  URL ACCESSO  -->
			<s:dgcolumn label="Url Sistema Esterno" index="1" />
			
		<!--  DESCRIZIONE  -->
			<s:dgcolumn label="Descrizione" index="2" />
			
		<!--  ID SERVIZIO  -->
			<s:dgcolumn label="Identificativo Servizio" index="6" />	

		<!--  FLAG ATTIVAZIONE  -->
			<s:dgcolumn label="Attivo" >
			<s:if right="{4}" control="eq" left="Y">
					<s:then>SI</s:then>
					<s:else>NO</s:else>
				</s:if>
			</s:dgcolumn>
			
		<!--  AZIONI  -->
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">

		
			</s:dgcolumn>
			
		</s:datagrid>
	</s:div>

</c:if>
