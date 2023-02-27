<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:setBundle name="org.seda.payer.i18n.resources.TemplateStrings" />
<m:view_state id="viewstate" encodeAttributes="true"  encodeParameters="true"  />
<script type="text/javascript" >
	$(document).ready( function() {
	   
	});

	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
	
</script>
<c:url value="" var="formParameters">
	<c:if test="${!empty param.conto}">
		<c:param name="conto">${param.conto}</c:param>
	</c:if>
		<c:if test="${!empty param.stato}">
		<c:param name="stato">${param.stato}</c:param>
	</c:if>
	<c:if test="${!empty param.documento}">
		<c:param name="documento">${param.documento}</c:param>
	</c:if>
	<c:if test="${!empty param.cliente}">
		<c:param name="cliente">${param.cliente}</c:param>
	</c:if>
	<c:if test="${!empty param.importo}">
		<c:param name="importo">${param.importo}</c:param>
	</c:if>
	<c:if test="${!empty param.squa}">
		<c:param name="squa">${param.squa}</c:param>
	</c:if>
</c:url>

<s:div name="divSelezione1" cssclass="divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="regolarizzaSospesoForm"
			action="regolarizzaSospeso.do${formParameters}" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:textbox name="DDLChanged" label="DDLChanged" bmodify="true"
				text="" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="ext" label="ext" bmodify="true" text="${ext}"
				cssclass="display_none" cssclasslabel="display_none" />

			<s:textbox name="hRowsPerPage" label="rowsPerPageRic" bmodify="true"
				text="${rowsPerPage}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="hPageNumber" label="pageNumberRic" bmodify="true"
				text="${pageNumber}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="hOrder" label="orderRic" bmodify="true"
				text="${order}" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="idmdc" label="idmdc" bmodify="true"
				text="${idmdc}" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="page" label="page" bmodify="true"
				text="${page}" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="idgdc" label="idgdc" bmodify="true"
				text="${idgdc}" cssclass="display_none" cssclasslabel="display_none" />
				
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
				Regolarizza Movimento
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="">
					<s:div name="divElement1" cssclass="divTableDettaglioOnTop">	
						<s:table cssclass="seda-ui-datagrid" border="1" cellspacing="0" cellpadding="3">
							<s:thead>
								<s:tr cssclass="seda-ui-datagridrowdispari">
									<s:th cssclass="seda-ui-datagridcell">Numero Documento</s:th>
									<s:th cssclass="seda-ui-datagridcell">Importo</s:th>
									<s:th cssclass="seda-ui-datagridcell">Squadratura</s:th>
								</s:tr>
							</s:thead>
							<s:tbody>
								<s:tr cssclass="seda-ui-datagridrowpari">
									<s:td cssclass="seda-ui-datagridcell text_align_center">
										${documento}
									</s:td>
									<s:td cssclass="seda-ui-datagridcell text_align_center">
										${importo}
									</s:td>
									<s:td cssclass="seda-ui-datagridcell text_align_center">
										${squa}
									</s:td>
								</s:tr>
							</s:tbody>
						</s:table>
					</s:div>
				</s:div>
				
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="nota" label="Nota:" text="${nota}" maxlenght="256"
						cssclasslabel="labelsmall" validator=""/>
					</s:div>
				</s:div>
			</s:div>
			
			<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter"></s:div>
			<s:div name="divRicercaRight" cssclass="divRicMetadatiRight"></s:div>
				
			
			<s:div name="divCentered0" cssclass="divRicBottoni">
				<c:if test="${rendicontato != 'Y'}">
				<s:button id="tx_button_cerca" type="submit" text="Regolarizza" onclick=""
					cssclass="btnStyle" />
				</c:if>
				<c:if test="${page == 'dettGior'}">
					<s:hyperlink name="hpAnnulla" cssclass="hlToBtnStyle" href="dettaglioGiornaleCassa.do?tx_button_cerca=cerca" text="Indietro" />						
				</c:if>
				<c:if test="${page == 'dettMov'}">
					<s:hyperlink name="hpAnnulla" cssclass="hlToBtnStyle" href="dettaglioMovimentoCassa.do?idmdc=${idmdc}" text="Indietro" />						
				</c:if>
			</s:div>
			
		</s:form>		
	</s:div>
</s:div>