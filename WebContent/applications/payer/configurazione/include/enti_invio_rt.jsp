<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="entiinviort" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>

<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Ente Configurato Invio RT</s:div>

		<s:form name="form_ricerca" action="entiinviort.do?vista=entiinviort" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">


			<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
				<s:div name="divElement1" cssclass="divRicMetadatiLeft">
					<s:textbox bmodify="true" name="tx_societa"
						label="Societ&agrave;:" maxlenght="256"
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" text="${tx_societa}"/>

				</s:div>
				<s:div name="divElement2" cssclass="divRicMetadatiCenter">
					<s:textbox bmodify="true" name="tx_utente"						
						label="Utente:" maxlenght="256"
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" text="${tx_utente}"/>

				</s:div>
				<s:div name="divElement3" cssclass="divRicMetadatiRigth">
					<s:textbox bmodify="true" name="tx_ente"						
						label="Ente:" maxlenght="256"
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" text="${tx_ente}" />
				</s:div>
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					<s:button id="tx_button_cerca" onclick="" text="Cerca"
						type="submit" cssclass="btnStyle" validate="false"/>
					<s:button id="tx_button_reset" onclick="" text="Reset"
						type="submit" cssclass="btnStyle" validate="false"/>
					<s:button id="tx_button_aggiungi" type="submit" text="Nuovo" onclick=""
											cssclass="btnStyle" validate="false"/>
			</s:div>
		</s:form>
		
	</s:div>

</s:div>
<s:div name="div_messaggi" cssclass="div_align_center">
	<c:if test="${!empty tx_message}">
		<s:div name="div_messaggio_info">
			<hr />
			<s:label name="tx_message" text="${tx_message}" />
			<hr />
		</s:div>
	</c:if>
	<c:if test="${!empty tx_error_message}">
		<s:div name="div_messaggio_errore">
			<hr />
			<s:label name="tx_error_message" text="${tx_error_message}" />
			<hr />
		</s:div>
	</c:if>
</s:div>
	<c:if test="${listaEnti != null}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Enti Configurati Invio RT
	</s:div>
		<s:datagrid cachedrowset="listaEnti" border="0" rowperpage="${applicationScope.rowsPerPage}" 
					   action="entiinviort.do?&vista=entiinviort" usexml="true" viewstate="true">

			<s:dgcolumn index="2" label="Societ&agrave;" />
			<s:dgcolumn index="4" label="Utente" />
			<s:dgcolumn index="6" label="Ente" />
			<s:dgcolumn index="7" label="Codice IDPA" />
			<s:dgcolumn label="Azioni">
				
			  <s:hyperlink
				cssclass="hlStyle" 
				href="nuovoenteinviort.do?tx_button_edit&codop=edit&desc_soc={2}&desc_ute={4}&desc_ente={6}&tx_societa={1}&tx_ente={5}&tx_utente={3}&tx_codiceIPA={7}"
				imagesrc="../applications/templates/configurazione/img/edit.png"
				alt="Modifica" text="" />

			   <s:hyperlink
				cssclass="hlStyle" 
				href="entiinviort.do?tx_button_cancel&tx_societa={1}&tx_ente={5}&tx_utente={3}&tx_codiceIPA={7}"
				imagesrc="../applications/templates/configurazione/img/cancel.png"
				alt="Cancella" text="" />
			</s:dgcolumn>
		</s:datagrid>
	</c:if>

	
