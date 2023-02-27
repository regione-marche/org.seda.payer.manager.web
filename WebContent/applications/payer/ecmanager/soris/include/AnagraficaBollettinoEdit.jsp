<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="ecanagraficaedit" encodeAttributes="true" />

<fmt:setLocale value="it_IT" />

<script type="text/javascript">
	function showCodici()
	{
		var valc = $('#statoVis').val();
		if(valc == 0) {
			$('#statoVis').val(1);
			$('#tx_visualizza').text('Nascondi');
			$('#altriCodici').show();
		} else {
			$('#statoVis').val(0);
			$('#tx_visualizza').text('Visualizza');
			$('#altriCodici').hide();
		}
	}

</script>

<s:form name="frmIndietro" action="ecanagraficaedit.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

<s:div name="div_messaggi" cssclass="div_align_center ad_color_red"> 
	<c:if test="${!empty tx_message}"> 
		<s:div name="div_messaggio_info"> 
			<hr /> 
			<s:label name="tx_message" text="${tx_message}" /> 
			<hr /> 
		</s:div> 
	</c:if>
	<c:if test="${!empty tx_error_message}">
		<s:div name="div_messaggio_errore" >
			<hr />
			<s:label name="tx_error_message" text="${tx_error_message}" />
			<hr />
		</s:div>
	</c:if>
</s:div>

<s:div name="divDettaglio" cssclass="divDettaglio">
	<s:table id="tableTestataComunicazione" border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglio1">
		<s:thead>
			<s:tr>
				<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="4">Dettaglio Anagrafica Contribuente Selezionata</s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Codice Fiscale:</s:td>
				<s:td>${anagrafica.codiceFiscale}</s:td>
				<s:td cssclass="seda-ui-cellheader">Cognome</s:td>
				<s:td><s:textbox bmodify="true" name="tx_Cognome"
						label="" maxlenght="50" 
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" validator="maxlength=61"
						text="${anagrafica.cognome}" />
				</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Nome:</s:td>
				<s:td>
					<s:textbox bmodify="true" name="tx_Nome"
						label="" maxlenght="50" 
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" validator="maxlength=61"
						text="${anagrafica.nome}" />
				
				</s:td>
				
				<s:td cssclass="seda-ui-cellheader">Numero cellulare:</s:td>
				<s:td>${anagrafica.cellulare}</s:td>
				
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">E-mail:</s:td>
				<s:td>${anagrafica.mail}</s:td>
				
				<s:td cssclass="seda-ui-cellheader">E.mail PEC:</s:td>
				<s:td>${anagrafica.mailPec}</s:td>
				
				
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Indirizzo:</s:td>
				<s:td>
					<s:textbox bmodify="true" name="tx_indirizzo"
						label="" maxlenght="50" 
						cssclasslabel="label85 bold textright"
						cssclass="textareaman"
						message="[accept=Indirizzo: ${msg_configurazione_descrizione_3}]"
						text="${anagrafica.indirizzo}" />				
				</s:td>
				<s:td cssclass="seda-ui-cellheader">Comune:</s:td>
				<s:td>
					<s:textbox bmodify="true" name="tx_comune"
						label="" maxlenght="50" 
						cssclasslabel="label85 bold textright"
						cssclass="textareaman"
						message="[accept=Comune: ${msg_configurazione_descrizione_3}]"
						text="${anagrafica.comune}" />				
				</s:td>
			</s:tr>

			<s:tr>
				<s:td cssclass="seda-ui-cellheader">CAP:</s:td>
				<s:td>
					<s:textbox bmodify="true" name="tx_cap"
						label="" maxlenght="50" 
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" validator="ignore;accept=^[0-9]{1,18}$"
						message="[accept=Cap: ${msg_configurazione_numero}]"
						text="${anagrafica.cap}" />				
				</s:td>
				<s:td cssclass="seda-ui-cellheader">Provincia:</s:td>
				<s:td>
					<s:textbox bmodify="true" name="tx_provincia"
						label="" maxlenght="50" 
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" validator="ignore;accept=^[a-zA-Z\-]{1,18}$"
						message="[accept=Provincia: ${msg_configurazione_descrizione_3}]"
						text="${anagrafica.provincia}" />				
				</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Codice SDI:</s:td>
				<s:td>${anagrafica.codiceSDI}</s:td>		
				<s:td></s:td>
				<s:td>
				</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Stato Attivazione:</s:td>
				<s:td>
					<c:choose>
							<c:when test="${anagrafica.flgFirstAccess==true}">
								Non Attivo  
							</c:when>
							<c:when test="${anagrafica.flgFirstAccess==false}">
								Attivo
							</c:when>
						</c:choose>			
				</s:td>
				<s:td cssclass="seda-ui-cellheader">Welcome Kit</s:td>
				<s:td>
					<c:choose>
						<c:when test="${anagrafica.flgFwlk==true}">
							Prodotto 
						</c:when>
						<c:when test="${anagrafica.flgFwlk==false}">
							Non Prodotto
						</c:when>
					</c:choose>	
				
				</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Borsellino:</s:td>
				<s:td>
					<c:choose>
						<c:when test="${anagrafica.flagBorsellino==true}">
							Presente 
						</c:when>
						<c:when test="${anagrafica.flagBorsellino==false}">
							Non Presente
						</c:when>
					</c:choose>			
				</s:td>
				<s:td cssclass="seda-ui-cellheader"></s:td>
				<s:td>
				</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Notifica Mail:</s:td>
				<s:td>
					<c:choose>
						<c:when test="${anagrafica.flgNotificaMail == 'Y'}">
							Attiva 
						</c:when>
						<c:when test="${anagrafica.flgNotificaMail == 'N'}">
							Non Attiva
						</c:when>
						<c:otherwise>
							Non Attiva
						</c:otherwise>
					</c:choose>			
				</s:td>
				<s:td cssclass="seda-ui-cellheader">Notifica SMS:</s:td>
				<s:td>
					<c:choose>
						<c:when test="${anagrafica.flgNotificaSms == 'Y'}">
							Attiva  
						</c:when>
						<c:when test="${anagrafica.flgNotificaSms == 'N'}">
							Non Attiva
						</c:when>
						<c:otherwise>
							Non Attiva
						</c:otherwise>
					</c:choose>			
				</s:td>
			</s:tr>
			
			<c:choose>
				<c:when test="${anagrafica.flgFirstAccess==true}">
					<s:tr>
						<s:td cssclass="seda-ui-cellheader">Codice Di Attivazione:</s:td>
						<s:td>${anagrafica.codiceAttivazione}</s:td>
						<s:td cssclass="seda-ui-cellheader">Lista Codici Attivazione Rigenerati:</s:td>
						<s:td>
							&nbsp;&nbsp;&nbsp;&nbsp;<s:button id="tx_visualizza" type="button" text="Visualizza" onclick="showCodici()" validate="false"  cssclass="btnStyle" />
							<input id="statoVis" type="hidden" value="0"/>   
						</s:td>
					</s:tr>
				</c:when>
				<c:otherwise>
					<s:tr>
						<s:td cssclass="seda-ui-cellheaderred">Codice Di Attivazione Disabilitato:</s:td>
						<s:td><span class="seda-ui-spanred">${anagrafica.codiceAttivazione}&nbsp;&nbsp;&nbsp;&nbsp;</span>
							<s:button id="tx_button_rigeneracodiceattivazione" type="submit" text="Rigenera" onclick="" validate="false"  cssclass="btnStyle" />
						</s:td>
						<s:td cssclass="seda-ui-cellheader">Lista Codici Attivazione Rigenerati:</s:td>
						<s:td>
							&nbsp;&nbsp;&nbsp;&nbsp;<s:button id="tx_visualizza" type="button" text="Visualizza" onclick="showCodici()" validate="false"  cssclass="btnStyle" />
							<input id="statoVis" type="hidden" value="0"/>   
						</s:td>
					</s:tr>
				</c:otherwise>
			</c:choose>
						
		</s:tbody>
	</s:table>
</s:div>

<br/>
<div id="altriCodici" style="display: none">
    <table class="seda-ui-datagrid" border="0" cellpadding="0" cellspacing="0">
        <thead>
		  <tr>     
			<th class="seda-ui-headercell seda-ui-datagrid thDettaglioTitle1" colspan="3" rowspan="1">Lista Codici di Attivazione</th>
		  </tr>
          <tr>  
            <th class="seda-ui-headercell"> Codice&nbsp;</th>  
            <th class="seda-ui-headercell"> Data Generazione&nbsp;</th>  
            <th class="seda-ui-headercell lastcol"> Utenza&nbsp;</th>  
          </tr>
        </thead> 
        <tbody>
            <c:set var="indice" value="1" scope="page" /> 
			<c:forEach var="vettCodici" items="${vettoreCodici}">
				<c:set var="clsRows" value="seda-ui-datagridrowdispari" scope="page" />
				<c:if test="${(indice % 2) == 0}">
					<c:set var="clsRows" value="seda-ui-datagridrowpari" scope="page" />
				</c:if>
	            <c:set var="indice" value="${indice + 1}"  scope="page" /> 
				<tr class="${clsRows}">
	           		<td class="seda-ui-datagridcell left">
	         			<span>${vettCodici.codiceAttivazione}</span>
	           		</td>
	           		<td class="seda-ui-datagridcell left">
	           			<span>${vettCodici.dataAggiornamento}</span>
	           		</td>
	           		<td class="seda-ui-datagridcell left lastcol">
	           			<span>${vettCodici.operatore}</span>
					</td>
				</tr>
			  </c:forEach>
		</tbody> 
	</table>
</div>

<br/>
	<input type="hidden" name="codop" value="aggiorna" />
	<input type="hidden" name="tx_societa" value="${anagrafica.codiceSocieta}" />
				<input type="hidden" name="tx_utente" value="${anagrafica.codiceUtente}" />
				<input type="hidden" name="tx_ente" value="${anagrafica.chiaveEnte}" />
				<input type="hidden" name="tx_codfis" value="${anagrafica.codiceFiscale}" />
				<input type="hidden" name="tx_codiceSDI" value="${anagrafica.codiceSDI}" />
				<input type="hidden" name="tx_indirizzoEmail" value="${anagrafica.mail}" />
				<input type="hidden" name="tx_indirizzoEmailPec" value="${anagrafica.mailPec}" />
				<input type="hidden" name="tx_numeroCell" value="${anagrafica.cellulare}" />
				
				
	<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
		<s:button id="tx_button_indietro" type="submit" text="Indietro" onclick="" validate="false"  cssclass="btnStyle" />
		<s:button id="tx_button_edit" type="submit" text="Aggiorna" onclick="" cssclass="btnStyle" />
	</s:div>
	
</s:form>






