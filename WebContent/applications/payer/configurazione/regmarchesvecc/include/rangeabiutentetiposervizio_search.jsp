<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="Rangeabiutentetiposervizio_search" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">

	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	
	
		<s:form name="search_form" action="rangeabiutentetiposerviziosearch.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />
			
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Range Utente - Tipologia Servizio</s:div>
			
			<s:div name="divRicercaTitleName1" cssclass="divRicMetadati">
				<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiLeft">
						<s:textbox bmodify="true" name="rangeabiutentetiposervizio_strDescrSocieta"
							label="Societ&agrave;:" maxlenght="256"
							validator="ignore;accept=${configurazione_descrizione256_regex}"
							cssclasslabel="label85 bold textright" cssclass="textareaman"
							message="[accept=Societ&agrave;: ${msg_configurazione_descrizione_regex}]"
							text="${rangeabiutentetiposervizio_strDescrSocieta}" />

					</s:div>
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:textbox bmodify="true" name="rangeabiutentetiposervizio_strDescrUtente"
							label="Utente:" maxlenght="256"
							validator="ignore;accept=${configurazione_descrizione256_regex}"
							cssclasslabel="label85 bold textright" cssclass="textareaman"
							message="[accept=Utente: ${msg_configurazione_descrizione_regex}]"
							text="${rangeabiutentetiposervizio_strDescrUtente}" />

					</s:div>
					<s:div name="divElement3" cssclass="divRicMetadatiRight">
						<s:textbox bmodify="true" name="rangeabiutentetiposervizio_strDescrTipologiaServizio"
							label="Tipol. Serv.:" maxlenght="256"
							validator="ignore;accept=${configurazione_descrizione256_regex}"
							cssclasslabel="label85 bold textright" cssclass="textareaman"
							message="[accept=Tipol. Serv.: ${msg_configurazione_descrizione_regex}]"
							text="${rangeabiutentetiposervizio_strDescrTipologiaServizio}" />

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
	
	<c:if test="${rangeabiutentetiposervizios != null}">
			<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Range Utente - Tipologia Servizio
	</s:div>
		
		<s:datagrid viewstate="true" cachedrowset="rangeabiutentetiposervizios"  
			action="rangeabiutentetiposerviziosearch.do?vista=Rangeabiutentetiposervizio_search" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

    	    <s:dgcolumn index="10" label="Societ&agrave;" />       
			<s:dgcolumn index="11" label="Utente" />
			<s:dgcolumn index="12" label="Tipologia Servizio" /> 
			<s:dgcolumn index="5" label="Inizio Range Da" />
			<s:dgcolumn index="6" label="Fine Range A" />
			<s:dgcolumn index="7" label="Inizio Range Per" /> 
			<s:dgcolumn label="Azioni">
				
								  
			</s:dgcolumn>
		</s:datagrid>
	</c:if>
</s:div>
