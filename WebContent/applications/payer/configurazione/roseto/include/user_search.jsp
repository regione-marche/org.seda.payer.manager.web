<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="users" encodeAttributes="true"/>

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>



<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Utenti</s:div>

		<s:form name="form_ricerca" action="user.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />

			<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
				
						<s:div name="divElement1" cssclass="divRicMetadatiLeft">
							<s:textbox bmodify="true" name="user_searchCompanyCode"
								label="Cod. Societ&agrave;:" maxlenght="5"
								cssclasslabel="label85 bold textright"
								cssclass="textareaman" text="${user_searchCompanyCode}"/>
						</s:div>
					

				<s:div name="divElement2" cssclass="divRicMetadatiCenter">
				
					<s:textbox bmodify="true" name="user_searchUserCode"						
						label="Cod. Utente:" maxlenght="5"
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" text="${user_searchUserCode}" />

				</s:div>
				<s:div name="divElement4" cssclass="divRicMetadatiRight">
						<s:textbox bmodify="true" name="user_searchScopeCncCode"
							label="Ambito:" maxlenght="3"							 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman"
							text="${user_searchScopeCncCode}" />						
				</s:div>
			
			</s:div>
			<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="user_searchCompanyDescription"
							label="Descr.Societ&agrave;:" maxlenght="256"							
							cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								message="[accept=Descr.Societ&agrave;: ${msg_configurazione_descrizione_regex}]"
							text="${user_searchCompanyDescription}" />
					</s:div>
			
			</s:div>
			<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="user_searchUserDescription"
							label="Descr.Utente:" maxlenght="256"							
							cssclasslabel="label85 bold textright"
								cssclass="textareaman" validator="ignore;accept=${configurazione_descrizione256_regex}"
								message="[accept=Descr.Utente: ${msg_configurazione_descrizione_regex}]"
							text="${user_searchUserDescription}" />
					</s:div>
					
			</s:div>
			<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">					
			
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					<s:button id="tx_button_cerca" onclick="" text="Cerca"
						type="submit" cssclass="btnStyle"/>
					<s:button id="tx_button_reset" onclick="" text="Reset"
						type="submit" cssclass="btnStyle" validate="false"/>
					<s:button id="tx_button_nuovo" type="submit" text="Nuovo" onclick=""
											cssclass="btnStyle" validate="false"/>
			</s:div>
		</s:form>		
	</s:div>

	<s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
			Elenco Utenti
	</s:div>
</s:div>

