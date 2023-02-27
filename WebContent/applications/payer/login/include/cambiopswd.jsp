<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="default" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="cambio_pswd_form" action="cambioPswd.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
				<s:div name="divRicercaTitleName1" cssclass="divRicTitle bold">Cambio Password</s:div>
				<br/>
				<s:div name="divRicMetadati" cssclass="divRicMetadati">
					
					<%--
					 
					     I CAMPI DI INPUT VENGONO NASCOSTI QUANDO 
					     IL CAMBIO PASSWORD E' STATO EFFETTUATO
					 --%>
					<c:if test="${!cambioEffettuato}">
						<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenterCPWD">
								<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true" name="tx_username"
										label="UserId: " maxlenght="50" 
										cssclasslabel="label150 bold textright" showrequired="true" 
										cssclass="textareaman"  validator="required;minlength=8;accept=^[a-zA-Z0-9]{8,50}$"
										message="[accept=UserId: ${msg_configurazione_alfanumerici}]"
										text="${sessionScope.j_user_bean.name}" />
								</s:div>
								<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true" name="puk"
										label="Codice PUK: " maxlenght="256" 
										cssclasslabel="label150 bold textright" showrequired="true" 
										cssclass="textareaman" validator="required;minlength=5;maxlength=256"
										text="" bpassword="true"/>
								</s:div>
								<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true" name="tx_password"
										label="Vecchia Password: " maxlenght="15" 
										cssclasslabel="label150 bold textright" showrequired="true" 
										cssclass="textareaman" validator="required;maxlength=15" 
										bpassword="true"
										text="" />
								</s:div>
								<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true" name="tx_nuova_password"
										label="Nuova Password: " maxlenght="15" 
										cssclasslabel="label150 bold textright" showrequired="true" 
										cssclass="textareaman" validator="required;minlength=8;accept=^[a-zA-Z0-9\.,!#%]{8,15}$;maxlength=15" 
										bpassword="true" 
										message="[accept=Nuova Password: ${msg_configurazione_descrizione_2}]"
										text="" />
								</s:div>
								<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true" name="tx_conferma_nuova_password"
										label="Conferma Password: " maxlenght="15" 
										cssclasslabel="label150 bold textright" showrequired="true"
										cssclass="textareaman" validator="required;minlength=8;accept=^[a-zA-Z0-9\.,!#%]{8,15}$;maxlength=15" 
										message="[accept=Conferma Password: ${msg_configurazione_descrizione_2}]"
										bpassword="true"
										text="" />
								</s:div>
								<s:div name="divElement81" cssclass="divRicMetadatiSingleRow">
									<s:textbox bmodify="true" name="emailNotifiche"
										label="Indirizzo email: " maxlenght="50" 
										cssclasslabel="label150 bold textright"
										cssclass="textareaman" validator="ignore;accept=${login_email_regex};maxlength=50" 
										text="" 
										message="[accept=Indirizzo email: ${msg_configurazione_email}]"/>
									<s:div name="divPostaCertificata" cssclass="floatleft divMarginTopPEC">
										<s:list bradio="false" bchecked="${postacertificata}" validator="ignore" 
											cssclasslabel="label110 bold textleft" cssclass="checkLeft1"
							   			  	name="postacertificata" groupname="postacertificata" 
											text="Posta Certificata" value="SI" />
									</s:div>
								</s:div>
							</s:div>
					</c:if>
				</s:div>
				<br /><br />
				<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<%-- 
				     IL PULSANTE "INDIETRO" VIENE VISUALIZZATO SOLO NEL CASO IN CUI SIA
				     STATO L'UTENTE A CHIEDERE IL CAMBIO PASSWORD DOPO IL LOGIN, 
				     QUINDI DEVE ESSERE PRESENTE UNO USER BEAN IN SESSIONE CON LO USERNAME VALORIZZATO                  
				 --%>
					<c:if test="${!empty sessionScope.j_user_bean.nome}">
						<s:button id="tx_button_indietro" validate="false" onclick="" text="Indietro" cssclass="btnStyle" type="submit" />
					</c:if>
				<%-- 
				     IL PULSANTE DI MODIFICA VIENE NASCOSTO QUANDO 
				     IL CAMBIO PASSWORD E' STATO EFFETTUATO
				 --%>
					<c:if test="${!cambioEffettuato}">
						<s:button id="tx_button_edit" onclick="" text="Modifica" type="submit" cssclass="btnStyle" />
					</c:if>
				<%--  
				     NEL CASO DI CAMBIO PASSWORD FORZATO IL PULSANTE DI LOGIN
				     VIENE ABILITATO SOLO DOPO CHE L'UTENTE HA EFFETTUATO IL
				     CAMBIO E NON VIENE VISUALIZZATO AFFATTO PER UN UTENTE CHE
				     HA CHIESTO IL CAMBIO PASSWORD DOPO IL LOGIN
				 --%>
						<c:if test="${empty sessionScope.j_user_bean.nome}">
							<s:button id="tx_button_login" validate="false" disable="${!cambioEffettuato}" onclick="" text="Login" cssclass="btnStyle" type="submit" />
						</c:if>
				</s:div>
				<input type="hidden" name="tx_username_hidden" value="${sessionScope.j_user_bean.name}" />
		</s:form>
	</s:div>
	<br/>
					<%-- 
					     LE REGOLE DI COMPOSIZIONE DELLA PASSWORD VENGONO NASCOSTE
					     QUANDO IL CAMBIO PASSWORD E' STATO EFFETTUATO
					 --%>
	<c:if test="${!cambioEffettuato}">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle ">
			La nuova password puo' essere lunga da 8 a 15 caratteri
			<br/>
			Sono consentiti i caratteri alfabetici (minuscoli e maiuscoli), le cifre (da 0 a 9) ed i caratteri "." "," "!" "#" "%"
			<br/>
			La password deve contenere almeno un carattere maiuscolo, un carattere numerico ed uno dei caratteri "." "," "!" "#" "%"
		</s:div>
	</c:if>
</s:div>
