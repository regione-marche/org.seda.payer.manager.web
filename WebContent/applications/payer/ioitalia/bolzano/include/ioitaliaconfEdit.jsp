<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="ioitaliaconfEdit" encodeAttributes="true" />

<jsp:useBean id="typeRequest"
	class="com.seda.payer.commons.bean.TypeRequest" />


<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>

<script type="text/javascript">
function setFired() {
			var buttonFired = document.getElementById('fired_button_hidden');
			buttonFired.value = 'tx_button_societa_changed';
	}
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}	

	/* YLM PG22XX06 INI  */
	/* var rand = () => {
		return Math.random().toString(36).substr(2);
		};

	var token = () => {
		return rand() + rand();
		};

	function generak1(){
	  	document.getElementById("tx_firstKey_s").value = token();
	  	
		}	
	function generak2(){
	  	document.getElementById("tx_secondKey_s").value = token();
		}	 */
	
		function dec2base36(dec) {
	        return dec.toString(36);
	    }

	    function generateString(len) {
	        var arr = new Uint32Array(100 / 2);
	        window.crypto.getRandomValues(arr);
	        var result = Array.from(arr, dec2base36).join("");
	        return result.substr(0, len);
	    }

	    function generak1(){
		  	document.getElementById("tx_firstKey_s").value = generateString(35);
		  	
			}	
		function generak2(){
		  	document.getElementById("tx_secondKey_s").value = generateString(35);
			}
	    /* YLM PG22XX06 FINE  */

</script>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:form name="var_form" action="ioitaliaconfedit.do?vista=${vista}"
			method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'  || empty codop}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">CONFIGURAZIONE APP IO</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">CONFIGURAZIONE APP IO - ${tx_id}</s:div>
			</c:if>

			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<c:if test="${codop == 'add'  || empty codop}">
					<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="ddlSocietaUtenteEnte"
								label="Societ&agrave;/Utente/Ente: " showrequired="true"
								cssclasslabel="label160 bold textright floatleft"
								validator="required;"
								cssclass="seda-ui-ddl tbddlMax780 floatleft"
								disable="${codop == 'edit'}"
								onchange="setFired();this.form.submit();"
								cachedrowset="listaSocietaUtenteEnte" usexml="true"
								valueselected="${ddlSocietaUtenteEnte}">
								<s:ddloption text="Selezionare uno degli elementi della lista"
									value="" />
								<s:ddloption value="{1}|{2}|{3}|{4}" text="{5} / {6} / {4}" />
							</s:dropdownlist>
						</s:div>
					</s:div>
				
					<!-- Aggiunto cachedrowset e usexml -->
						<s:div name="divRicercaLeft" cssclass="divRicMetadatiTop">
							<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
								<s:dropdownlist name="tx_tipServ" showrequired="true"
									label="Tipologia Servizio AppIO:" disable="${codop == 'edit'}"
									onchange="setRequired();" cachedrowset="listaTipologieServizio" usexml="true" 
									cssclasslabel="label160 bold textright floatleft"
									validator="required;" cssclass="seda-ui-ddl tbddlMax780 floatleft"
									valueselected="${tx_tipServ}">
									<s:ddloption text="Selezionare uno degli elementi della lista"
										value="" />
									<s:ddloption value="{1}" text="{2}" />
								</s:dropdownlist>
							</s:div>
						</s:div>	
				</c:if>
				<c:if test="${codop == 'edit'}">
					<s:div name="divElement4top" cssclass="divRicMetadatiLeft">
						<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_societa"
										bdisable="true" validator="required;" showrequired="true"
										label="Societa:"
										cssclasslabel="label85 bold textright floatleft" cssclass="textareaman"
										text="${tx_desc_societa}"  />
						</s:div>
						<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_tipServ"
										bdisable="true" validator="required;" showrequired="true"
										label="Tipologia Servizio:"
										cssclasslabel="label85 bold textright floatleft" cssclass="textareaman"
										text="${tx_desc_tipServ}" />
						</s:div>
						<br /><br />
					</s:div>
					<s:div name="divElement5top" cssclass="divRicMetadatiCenter">
						<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_utente"
										bdisable="true" validator="required;" showrequired="true"
										label="Utente:"
										cssclasslabel="label85 bold textright floatleft" cssclass="textareaman"
										text="${tx_desc_utente}" />
						</s:div>
						<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
							
						</s:div>
						<br /><br />
					</s:div>
					<s:div name="divElement5top" cssclass="divRicMetadatiRight">
						<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="false" name="tx_ente"
										bdisable="true" validator="required;" showrequired="true"
										label="Ente:"
										cssclasslabel="label85 bold textright floatleft" cssclass="textareaman"
										text="${tx_desc_ente}" />
						</s:div>
						<br /><br />
					</s:div>
				
				</c:if>
			<br />
			<div style="clear: left">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Autorizzazione Web Service ricezione messaggi</s:div>
			</div>
			<br />
					<c:if test="${!empty tx_messageKey}">
						<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
							<div align="left" style="margin-left: 170px;">
							
								<s:label name="tx_messageKey" text="${tx_messageKey}" />
								
							</div>	
						</s:div>
					</c:if>
					<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
					<!-- s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft"-->
						
						<s:textbox bmodify="true" name="tx_firstKey_s"
							label="Chiave Primaria:" maxlenght="35"
							cssclasslabel="label160 bold textright floatleft" cssclass="textareaman2"
							text="${tx_firstKey_s}" showrequired="true"
							validator="required;" />
						<button id="tx_button_edit_end" onclick="generak1()" type="button"
							class="btnStyle" style="margin-left: 8px">Genera</button>
					<!-- /s:div -->
					</s:div>
			
					<s:div name="divElement2" cssclass="divRicMetadatiSingleRow">
					<!--  s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft" -->
						<s:textbox bmodify="true" name="tx_secondKey_s"
							label="Chiave Secondaria:" maxlenght="35" validator="required;" showrequired="true"
							cssclasslabel="label160 bold textright floatleft" cssclass="textareaman2"
							text="${tx_secondKey_s}"  />
						<button id="tx_button_create2" onclick="generak2()" type="button"
							class="btnStyle" style="margin-left: 8px">Genera</button>
					<!--/s:div -->			
		            </s:div>
			<br />
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Autorizzazione Web Service invio messaggi app IO</s:div>
			<br />
		
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
					<!--  s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft" -->
						<s:textbox bmodify="true" name="tx_firstKey2_s"
							label="Chiave Primaria:" maxlenght="35" showrequired="true"
							cssclasslabel="label160 bold textright floatleft" cssclass="textareaman2"
							text="${tx_firstKey2_s}" 
							validator="required;"/>
					<!--  /s:div -->
					</s:div>
					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
					<!-- s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft" -->
						<s:textbox bmodify="true" name="tx_secondKey2_s"
							label="Chiave Secondaria:" maxlenght="35" validator="required;" showrequired="true"
							cssclasslabel="label160 bold textright floatleft" cssclass="textareaman2"
							text="${tx_secondKey2_s}" />
					<!-- /s:div-->
					</s:div>
		
					<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">&nbsp</s:div>
					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
						<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_impMin_s" showrequired="true" maxlenght="15"
								label="Importo Minimo:" validator="required;accept=^\d{1,13}\.\d{2}$|^\d{1,13}\,\d{2}$;minlength=1"
								cssclasslabel="label160 bold floatleft" cssclass="textareaman" message="[accept=Importo Minimo: Inserire un importo valido e compreso tra 0,00 e 99999999999,99 (Includere decimali).]"
								text="${tx_impMin_s}" />
						</s:div>
						<br /><br />
					</s:div>
					
					<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
						<s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_impMax_s" showrequired="true" maxlenght="15"
								label="Importo Massimo:" validator="required;accept=^\d{1,13}\.\d{2}$|^\d{1,13}\,\d{2}$;minlength=1"
								cssclasslabel="label160 bold floatleft" cssclass="textareaman" message="[accept=Importo Massimo: Inserire un importo valido e compreso tra 0,00 e 99999999999,99 (Includere decimali).]"
								text="${tx_impMax_s}" />
						</s:div>
						<br /><br />
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist name="tx_abilitato_s" disable="false" label="Abilitato:" validator="required;" showrequired="true"
							cssclasslabel="label160 bold textright floatleft" valueselected="${tx_abilitato_s}">
							<s:ddloption text="NO" value="false"/>
							<s:ddloption text="SI" value="true"/>
							</s:dropdownlist>
						</s:div>
						<br /><br />
					</s:div>
					
					<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
						<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
							<s:textbox bmodify="true" name="tx_mail_s" validator="required;" showrequired="true"
									label="Mail statistiche elaborazioni:" 
									cssclasslabel="label200 bold floatleft" cssclass="textareaman"
									text="${tx_mail_s}" />
						</s:div>
						<br /><br />
					</s:div>
					<br /><br />
					
			</s:div>
		<br />
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<c:if test="${codop == 'add' || empty codop}">
					<s:button id="tx_button_aggiungi" onclick="return ControllaSalva()"
						text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${codop == 'edit'}">
					<s:button id="tx_button_edit" onclick="return ControllaSalva()"
						text="Aggiorna" type="submit" cssclass="btnStyle" />
				</c:if>
				<s:button id="tx_button_reset" onclick="" validate="false"
					text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_indietro" onclick="" validate="false"
					text="Indietro" type="submit" cssclass="btnStyle" />
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
