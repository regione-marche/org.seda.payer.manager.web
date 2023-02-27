<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="adminusers" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="reset_pswd_form" action="resetPswd.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Reset Password
			</s:div>
			<br/>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

					<!-- 
					     I CAMPI DI INPUT VENGONO NASCOSTI QUANDO 
					     IL CAMBIO PASSWORD E' STATO EFFETTUATO
					 -->
				<c:if test="${!cambioEffettuato}">
					<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenterCPWD">
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_username"
								label="UserId:" maxlenght="50" 
								cssclasslabel="label150 bold textright" showrequired="true"
								cssclass="textareaman" validator="required;minlength=8;accept=^[a-zA-Z0-9]{8,20}$;maxlength=20"
								message="[accept=UserId: ${msg_configurazione_alfanumerici}]"
								text="" />
						</s:div>
						<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="puk"
								label="Codice PUK:" maxlenght="256" 
								cssclasslabel="label150 bold textright" showrequired="true"
								cssclass="textareaman" validator="required;minlength=5;maxlength=256"
								text="" bpassword="true"/>
						</s:div>
						<s:div name="divElement10" cssclass="divRicMetadatiSingleRow">
								<!--<s:label cssclass="seda-ui-label label150 " text="" name="lblEmpty" />-->
								<s:button id="tx_button_reinviapuk" type="submit" text="Reinvia PUK" onclick="" cssclass="btnHyperlink"  validate="false"/>
								<s:label name="lblPuk" text=", verrà inoltrato all'indirizzo mail utilizzato in fase di registrazione" cssclass="lblPuk"/>
						</s:div>
						<s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="emailNotifiche"
								label="Indirizzo email:" maxlenght="50" 
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
					<!-- 
					     IL PULSANTE DI MODIFICA VIENE NASCOSTO QUANDO 
					     IL CAMBIO PASSWORD E' STATO EFFETTUATO
					 -->
				<c:if test="${!cambioEffettuato}">
					<s:button id="tx_button_edit" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				</c:if>
				<s:button id="tx_button_login" validate="false" onclick="" text="Login" cssclass="btnStyle" type="submit" />
			</s:div>
	</s:form>
	</s:div>
	
</s:div>

