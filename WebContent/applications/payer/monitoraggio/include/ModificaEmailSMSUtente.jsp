<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="modificaemailsmsutente" encodeAttributes="true" />

<c:if test="${!empty tx_transazione}">
	<s:datagrid cssclass="tableMargin" viewstate=""
		cachedrowset="tx_transazione" action="modificaEmailSms.do" border="1"
		usexml="true" rowperpage="${applicationScope.rowsPerPage}">
		<s:dgcolumn index="1" label="Id Transazione"/>
		<s:dgcolumn index="4" label="Data Transazione" format="dd/MM/yyyy HH:mm:ss"/>
		<s:dgcolumn index="5" label="Data Eff.Pag." format="dd/MM/yyyy HH:mm:ss"/>
		<s:dgcolumn label="Indirizzo IP<br/>Email<br/>SMS">
				{9}<br />{10}<br />{11}
		</s:dgcolumn>

		<s:dgcolumn format="#,##0.00" index="24" label="Importo totale" css="text_align_right"/>
	</s:datagrid>
	
	<s:div name="divMessage">
			<c:if test="${!empty tx_message}">
				<hr />
				<s:label name="tx_message" text="${tx_message}" />
				<hr />
			</c:if>
			<c:if test="${!empty tx_error_message}">
				<hr />
				<s:label name="tx_error_message" text="${tx_error_message}" />
				<hr />
			</c:if>
	</s:div>
	
	<s:div name="divCentered" cssclass="divContainerLeft">
		<s:form name="frmUpdateEmailSms" action="modificaEmailSms.do"
			method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
				Modifica email e numero SMS utente
			</s:div>
			<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
				<s:textbox validator="required;accept=${monitoraggio_email_regex};maxlength=50" showrequired="true"
					cssclass="textareaman" cssclasslabel="label150 bold textright"
					bmodify="true" name="newUserEmail" label="Indirizzo&nbsp;email:"
					text="${newUserEmail}" maxlenght="50" />
			</s:div>
			<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
				<s:textbox validator="ignore;number;maxlength=20"
					cssclass="textareaman" cssclasslabel="label150 bold textright"
					bmodify="true" name="newUserSms" label="Numero&nbsp;SMS:"
					text="${newUserSms}" maxlenght="20" />
			</s:div>
			
			<s:div name="divButtonLeft" cssclass="divButton1">
				<s:button id="tx_button_edit" type="submit" text="Aggiorna"
					onclick="" cssclass="btnStyle" />
			</s:div>
			
			<s:div name="divHidden" cssclass="divHidden">
				<s:textbox name="tx_codice_transazione_hidden" label="" bmodify="true" text="${tx_codice_transazione_hidden}" cssclass="rend_display_none btnStyle"  />
			</s:div>
		</s:form>
	</s:div>
</c:if>

<s:form name="frmIndietro"
	action="ritorna.do?vista=monitoraggiotransazioni"
	method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
	<s:div name="divIndietro" cssclass="divButton1">
		<s:button id="tx_button_transazioni" type="submit" text="Indietro"
			onclick="" cssclass="btnStyle" />
	</s:div>
</s:form>

