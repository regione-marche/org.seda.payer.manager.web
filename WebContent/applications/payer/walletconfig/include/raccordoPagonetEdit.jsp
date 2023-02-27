<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="raccordopagonetEdit" encodeAttributes="true" />

<script type="text/javascript">
	function setFired() {
			var buttonFired = document.getElementById('fired_button_hidden');
			buttonFired.value = 'tx_button_societa_changed';
	}
</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_form" action="raccordopagonetEdit.do?vista=${vista}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">CONFIGURAZIONE RACCORDO PAGONET</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">CONFIGURAZIONE RACCORDO PAGONET</s:div>
			</c:if>
									
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<%-- TODO da verificare la ddl che segue ripresa dal prototipo e non dalla pagina originale di riferimento --%>
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTopDouble">
					<s:div name="divElement1top" cssclass="divRicMetadatiSingleRow">
						<!--<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label85 bold textright" validator="required;"
									cssclass="floatleft" disable="${codop == 'edit'}"
									onchange="setFired();this.form.submit();"
									   cachedrowset="listaSocietaUtenteEnte" usexml="true" 
									   valueselected="${ddlSocietaUtenteEnte}">
   							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
							<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}"/>
						</s:dropdownlist>-->
						
						<s:dropdownlist name="ddlSocietaUtenteEnte"  
							label="Societ&agrave;/Utente/Ente: " showrequired="true"
									cssclasslabel="label160 bold textright floatleft" validator="required;"
									cssclass="seda-ui-ddl tbddlMax780 floatleft" disable="${codop == 'edit'}"
									onchange="setFired();this.form.submit();"
									   cachedrowset="listaSocietaUtenteEnte" usexml="true" 
									   valueselected="${ddlSocietaUtenteEnte}">
   							<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
							<s:ddloption value="{1}|{2}|{3}" text="{5} / {6} / {4}"/>
						</s:dropdownlist>
						
						<c:if test="${codop == 'add'}">
							<noscript>
								<s:button id="tx_button_societa_changed" 
									onclick="" text="" 
									validate="false"
									type="submit" cssclass="btnimgStyle"  title="Aggiorna" />
							</noscript>
						</c:if>
									
					</s:div>
				</s:div>
						
				<s:div name="divRicercaMetadatiLeft" cssclass="divRicMetadatiLeft">
			            <s:div name="divElement1" cssclass="divRicMetadatiSingleRow">
			            	<s:dropdownlist name="tx_tipologia_servizio"
												disable=""
												label="Tipol. Serv.:"
												cssclass="textareaman"
												cssclasslabel="label85 bold textright"
												onchange=""
												cachedrowset="utentetiposervizios2" usexml="true"
												valueselected="${tx_tipologia_servizio_h}">
												<s:ddloption text="Selezionare elemento" value="" />
												<s:ddloption  value="{1}" text="{1}/{2}"/>
							</s:dropdownlist>
						</s:div>
						
				</s:div>
				
				<s:div name="divRicercaMetadatiCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement41" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="100" 
									validator="ignore;maxlength=256;accept=${configurazione_descrizione256_regex}"
									label="Aut Boll.896:" name="tx_autbollettino"
									text="${tx_autbollettino}"
									message="[accept=Aut Boll.896: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
					</s:div>
					<s:div name="divElement41" cssclass="divRicMetadatiSingleRow">
								<s:textbox bmodify="true"
									maxlenght="100" 
									validator="ignore;maxlength=256;accept=${configurazione_descrizione256_regex}"
									label="Aut Boll.674:" name="tx_autbollettino_2"
									text="${tx_autbollettino_2}"
									message="[accept=Aut Boll. 674: ${msg_configurazione_descrizione_regex}]"
									cssclasslabel="label85 bold textright" cssclass="textareaman" />
							</s:div>
				
				</s:div>
				
				
											
		</s:div>
			<br/> 
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button id="tx_button_indietro" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />
				<!-- 
				<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
				 -->
				<c:if test="${codop == 'add'}">
					<s:button id="tx_button_aggiungi" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${codop == 'edit'}">
					<s:button id="tx_button_edit" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
			</s:div>
				<input type="hidden" name="codop" value="${codop}" />
				<input type="hidden" name="tx_societa" value="${tx_societa}" />
				<input type="hidden" name="tx_utente" value="${tx_utente}" />
				<input type="hidden" name="tx_ente" value="${tx_ente}" />
				<input type="hidden" name="tx_tipologia_servizio_h" value="${tx_tipologia_servizio_h}" />
				
	</s:form>
	</s:div>
	
</s:div>
