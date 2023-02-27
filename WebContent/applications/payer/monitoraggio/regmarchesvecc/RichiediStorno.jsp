<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="richiediStorno" encodeAttributes="true" />

<c:if test="${!empty tx_transazione}">
	<s:div name="divMessage" cssclass="seda-ui-divvalidator">
		<c:choose>
			<c:when test="${!empty message}">
				<s:label name="message" text="${message}" cssclass="seda-ui-spanvalidator" />
			</c:when>
			<c:otherwise>
				<s:label name="messageConferma" text="Sei sicuro di inviare la richiesta di storno per la seguente transazione?" cssclass="seda-ui-spanvalidator" />
			</c:otherwise>
		</c:choose>
	</s:div>

	<s:datagrid cssclass="tableMargin" viewstate=""
		cachedrowset="tx_transazione" action="richiediStorno.do" border="1"
		usexml="true" rowperpage="${applicationScope.rowsPerPage}">
		<s:dgcolumn index="1" label="Id Transazione" />
		<s:dgcolumn index="5" label="Data Eff.Pag."
			format="dd/MM/yyyy HH:mm:ss" />
		<s:dgcolumn index="7" label="Id Operazione" />
		<s:dgcolumn format="#,##0.00" index="24" label="Importo totale"
			css="text_align_right" />
		<s:dgcolumn format="#,##0.00" index="25" label="Costi Transazione"
			css="text_align_right" />
	</s:datagrid>

	<s:div name="divConfermaStorno" cssclass="divRicBottoni">
		<s:div name="divHlOuter" cssclass="divHlOuter">
			<s:hyperlink name="hlIndietro" text="Indietro" href="../monitoraggio/ritorna.do?vista=monitoraggiotransazioni" cssclass="hlStyleButtonLeft" />
			<c:if test="${tx_button_conferma_storno_visible}">
				<s:hyperlink name="hlConferma" text="Conferma" href="../monitoraggio/richiediStorno.do?tx_codice_transazione_hidden=${tx_codice_transazione_hidden}&tx_canale_pagamento=${tx_canale_pagamento}&tx_button_conferma_storno=S" cssclass="hlStyleButtonRight" />
			</c:if>
		</s:div>
	</s:div>
	
	<%-- 
	
		<center><s:table border="0" cssclass="container_btn">
			<s:thead>
				<s:tr>
					<s:td></s:td>
				</s:tr>
			</s:thead>
			<s:tbody>
				<s:tr>
					<s:td>
						<s:form name="frmIndietro"
							action="ritorna.do?vista=monitoraggiotransazioni" method="post"
							hasbtn1="false" hasbtn2="false" hasbtn3="false">
							<s:div name="divIndietro" cssclass="divButton1">
								<s:button id="tx_button_transazioni" type="submit"
									text="Indietro" onclick="" cssclass="btnStyle" />
							</s:div>
							<s:div name="divHidden" cssclass="divHidden">
							</s:div>
						</s:form>
					</s:td>
					<s:td>
						<s:form name="frmRichiediStorno" action="richiediStorno.do"
							method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

							<c:if test="${tx_button_conferma_storno_visible}">
								<s:div name="divButtonLeft" cssclass="divButton1">
									<s:button id="tx_button_conferma_storno" type="submit"
										text="Conferma" onclick="" cssclass="btnStyle" />
								</s:div>
							</c:if>
							<s:div name="divHidden" cssclass="divHidden">
								<s:textbox name="tx_codice_transazione_hidden" label=""
									bmodify="true" text="${tx_codice_transazione_hidden}"
									cssclass="rend_display_none btnStyle" />
								<s:textbox name="tx_canale_pagamento" label="" bmodify="true"
									text="${tx_canale_pagamento}"
									cssclass="rend_display_none btnStyle" />
							</s:div>
						</s:form>
					</s:td>
				</s:tr>
			</s:tbody>
		</s:table></center>
		
		--%>

</c:if>
