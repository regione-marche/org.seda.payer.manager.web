<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="impostasoggiornoTipologiaStrutture_search" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="search_form" action="tipologiastrutture.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">RICERCA TIPOLOGIE STRUTTURE RICETTIVE
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
			
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				
					<s:div name="divElement1" cssclass="divRicMetadatiCenter">
						
							<s:textbox bmodify="true" name="tipologiastrutture_descrizioneTipologia"
								label="Descrizione: " maxlenght="100" 
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;"
								text="${tipologiastrutture_descrizioneTipologia}"/>
						
					</s:div>
					
				</s:div>

			</s:div>
			<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				
			</s:div>
	</s:form>

	</s:div>
	
</s:div>

<c:if test="${!empty listaConfig}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			ELENCO TIPOLOGIE STRUTTURE RICETTIVE
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="listaConfig"  
			action="tipologiastrutture.do?vista=impostasoggiornoTipologiaStrutture_search" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

		<!-- Codice TIpologia Struttura -->
			<s:dgcolumn label="Codice" index="1" />
			
		<!-- DESCRIZIONE Tipologia Struttura -->
			<s:dgcolumn label="Descrizione" index="2" />


		<!-- AZIONI -->
			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">

			</s:dgcolumn>
			
		</s:datagrid>
	</s:div>

</c:if>
