<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="form_uffici_var" action="gestioneuffici.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:choose>
				<c:when test="${requestScope.codop == 'edit'}">
					<s:div name="divRicercaTitleName" cssclass="divRicTitle">GESTIONE UFFICI - MODIFICA</s:div>
					<c:set var="idUfficioBmodify" value="false"/>
					<c:set var="idUfficioRequired" value="required"/>
					<c:set var="idUfficioShowRequired" value="false"/>
					<c:set var="idUfficioDisabled" value="true"/>
				</c:when>
				<c:otherwise>
					<s:div name="divRicercaTitleName" cssclass="divRicTitle">GESTIONE UFFICI - NUOVO</s:div>
					<c:set var="idUfficioBmodify" value="true"/>
					<c:set var="idUfficioRequired" value="required"/>
					<c:set var="idUfficioShowRequired" value="true"/>
					<c:set var="idUfficioDisabled" value="false"/>
				</c:otherwise>
			</c:choose>
						
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement33" cssclass="divRicMetadatiLeft">
						<s:textbox bmodify="${idUfficioBmodify}" name="idufficio" maxlenght="6"
							label="ID Ufficio" showrequired="${idUfficioShowRequired}" bdisable="${idUfficioDisabled}"
							cssclasslabel="label85 bold textright"
							cssclass="textareaman " validator="${idUfficioRequired};maxlength=6;accept=^\d+(\.\d+)*$;"
							message="[accept=ID Ufficio: Questo campo e' obbligatorio]"
							text="${requestScope.idufficio}" />
					</s:div>
					
					<c:if test="${requestScope.codop == 'edit'}">
						<input type="hidden" name="idufficio" value="${requestScope.idufficio}"/>
					</c:if>
					
					<s:div name="divElement34" cssclass="divRicMetadatiLeft">
						<s:textbox bmodify="true" name="descrde"
							label="Descrizione DE:" maxlenght="100" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;maxlength=100;"
							text="${requestScope.descrde}" />
					</s:div>
				</s:div>
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement35" cssclass="divRicMetadatiCenter">
						<s:textbox bmodify="true" name="codufficio"
							label="Codice Ufficio:" 
							cssclasslabel="label85 bold textright"
							maxlenght="4"
							cssclass="textareaman" validator="ignore;maxlength=4;accept=^[0-9]*$"
							text="${requestScope.codufficio}" />				
					</s:div>
				</s:div>
				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement16" cssclass="divRicMetadatiRight">
						<s:textbox bmodify="true" name="descrit"
							label="Descrizione IT:" maxlenght="100" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" validator="ignore;maxlength=100;"
							text="${requestScope.descrit}" />
					</s:div>
				</s:div>
				
			</s:div>
			
			<br/>
			
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_indietro" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
				
				<c:choose>
					<c:when test="${requestScope.codop == 'edit'}">
						<s:button id="tx_button_edit" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
					</c:when>
					<c:otherwise>
						<s:button id="tx_button_aggiungi" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
					</c:otherwise>
				</c:choose>

			</s:div>

	</s:form>
	</s:div>
	
</s:div>

<script type="text/javascript">
caricoForm();
</script>
