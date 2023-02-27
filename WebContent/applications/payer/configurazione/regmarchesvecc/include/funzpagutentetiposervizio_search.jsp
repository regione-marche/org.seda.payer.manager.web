<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="funzpagutentetiposerviziows" encodeAttributes="true"/>

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>



<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Funzioni Pagamento Utente - Tipologia Servizio</s:div>

		<s:form name="form_ricerca" action="funzpagutentetiposervizio.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />

			<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
				<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
					<s:textbox bmodify="true" name="funzpagutentetiposervizio_searchcompanyCode"
						label="Societ&agrave;:" maxlenght="256"
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" text="${funzpagutentetiposervizio_searchcompanyCode}"  />

				</s:div>
				<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
					<s:textbox bmodify="true" name="funzpagutentetiposervizio_searchuserCode"						
						label="Utente:" maxlenght="256"
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" text="${funzpagutentetiposervizio_searchuserCode}" />

				</s:div>
				<s:div name="divRicMetadatiTopRight" cssclass="divRicMetadatiTopRight">
					<s:textbox bmodify="true" name="funzpagutentetiposervizio_searchcodiceTipologiaServizio"						
						label="Tipol. Serv.:" maxlenght="256"
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" text="${funzpagutentetiposervizio_searchcodiceTipologiaServizio}" />
				</s:div>
			</s:div>
			 <s:div name="divRicercaMetadatiLeft" cssclass="divRicMetadatiLeft">
		        		        
			</s:div>
			
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					<s:button id="tx_button_cerca" onclick="" text="Cerca"
						type="submit" cssclass="btnStyle"/>
					<s:button id="tx_button_reset" onclick="" text="Reset"
						type="submit" cssclass="btnStyle" validate="false"/>
					
			</s:div>
		</s:form>
		
	</s:div>

	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Funzioni Pagamento Utente - Tipologia Servizio
	</s:div>
</s:div>
