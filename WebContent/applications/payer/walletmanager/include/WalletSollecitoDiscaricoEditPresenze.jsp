<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="walletricaricheborsellinoedit" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>

 
<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
	
</script>
<script type="text/javascript">
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();
	
	$(function() {
		$.datepicker.setDefaults($.datepicker.regional[ "it" ] );
		$("#tx_periodo_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_periodo_da_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_periodo_da_day_id").val(dateText.substr(0,2));
												$("#tx_periodo_da_month_id").val(dateText.substr(3,2));
												$("#tx_periodo_da_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_periodo_da_day_id",
				                            "tx_periodo_da_month_id",
				                            "tx_periodo_da_year_id",
				                            "tx_periodo_da_hidden");
			}
		});
		$("#tx_periodo_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_periodo_a_hidden").datepicker({
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn: "button",
			buttonImage: "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly: true,
			onSelect: function(dateText, inst) {$("#tx_periodo_a_day_id").val(dateText.substr(0,2));
												$("#tx_periodo_a_month_id").val(dateText.substr(3,2));
												$("#tx_periodo_a_year_id").val(dateText.substr(6,4));
												},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_periodo_a_day_id",
				                            "tx_periodo_a_month_id",
				                            "tx_periodo_a_year_id",
				                            "tx_periodo_a_hidden");
			}
		});
		
	});
</script>


<fmt:setLocale value="it_IT" />
<s:form name="frmIndietro" action="walletsollecitodiscarico.do?vista=walletsollecitodiscarico&datagrid_action=cerca" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

<s:div name="divDettaglio" cssclass="divDettaglio">
	<s:table id="tableTestataComunicazione" border="1" cellspacing="0" cssclass="seda-ui-datagrid tableDettaglio1">
		<s:thead>
			<s:tr>
				<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="4">Dettaglio Borsellino Selezionato</s:th>
			</s:tr>
		</s:thead>
		<s:tbody>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Codice Borsellino:</s:td>
				<s:td>${sessionScope.codice_borsellino_header}</s:td>
				<s:td cssclass="seda-ui-cellheader">Codice Fiscale:</s:td>
				<s:td>${sessionScope.codice_fiscale_header}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Denominazione:</s:td>
				<s:td>${sessionScope.denominazione_header}</s:td>
				<s:td cssclass="seda-ui-cellheader">Residuo Borsellino:</s:td>
				<s:td>${sessionScope.RESIDUO_header}</s:td>
			</s:tr> 
		</s:tbody>
	</s:table>
	
	
	<c:if test="${!flagPresenzeCalendario}">	
	<s:div cssclass="seda-ui-datagrid thDettaglioTitle1 bold" name="divTitleDett">Lista presenze </s:div>
	
	
	
	<s:div name="div_datagrid">
	
	<c:if test="${lista_wallet_presenze!=null}">
	<s:div name="datagridDiv">
	<s:datagrid viewstate="true"  cachedrowset="lista_wallet_presenze" action="walletsollecitodiscaricoedit.do?vista=walletsollecitodiscarico&tx_presenze_action=refresh&codiceSocieta=${codiceSocieta}&codiceUtente=${codiceUtente}&chiaveEnte=${chiaveEnte}&IdwalletInfo=${IdwalletInfo}" usexml="true" border="1" rowperpage="${applicationScope.rowsPerPage}"   >
		
		<s:dgcolumn index="3" label="Codice Fiscale" css="textleft"  />  
		<s:dgcolumn index="22" label="Denominazione" css="textleft"  />
		<s:dgcolumn index="21" label="Servizio" css="textleft"/>
		<s:dgcolumn label="Mese Presenza" css="textleft"  >
		 <s:if right="1" control="eq" left="{10}" >
			  <s:then>
			     Gennaio
			  </s:then> 
		 </s:if>
		 <s:if right="2" control="eq" left="{10}" >
			  <s:then>
			     Febbraio
			  </s:then> 
		 </s:if>
		 <s:if right="3" control="eq" left="{10}" >
			  <s:then>
			     Marzo
			  </s:then> 
		 </s:if>
		 <s:if right="4" control="eq" left="{10}" >
			  <s:then>
			     Aprile
			  </s:then> 
		 </s:if>
		 <s:if right="5" control="eq" left="{10}" >
			  <s:then>
			     Maggio
			  </s:then> 
		 </s:if>
		 <s:if right="6" control="eq" left="{10}" >
			  <s:then>
			     Giugno
			  </s:then> 
		 </s:if>
		 <s:if right="7" control="eq" left="{10}" >
			  <s:then>
			     Luglio
			  </s:then> 
		 </s:if>
		 <s:if right="8" control="eq" left="{10}" >
			  <s:then>
			     Agosto
			  </s:then> 
		 </s:if>
		 <s:if right="9" control="eq" left="{10}" >
			  <s:then>
			     Settembre
			  </s:then> 
		 </s:if>
		 <s:if right="10" control="eq" left="{10}" >
			  <s:then>
			     Ottobre
			  </s:then> 
		 </s:if>
		 <s:if right="11" control="eq" left="{10}" >
			  <s:then>
			     Novembre
			  </s:then> 
		 </s:if>
		 <s:if right="12" control="eq" left="{10}" >
			  <s:then>
			     Dicembre
			  </s:then> 
		 </s:if>
		</s:dgcolumn>
		 
		 
		
		<s:dgcolumn index="11" label="Anno" css="textright">
		</s:dgcolumn>
		 
		
		
		
		<s:dgcolumn label="&nbsp;&nbsp;&nbsp;" css="textright">
		<s:hyperlink
					href="walletsollecitodiscaricoedit.do?codice_fiscale_figlio={3}&denominzione_figlio={22}&servizio={21}&mese={10}&anno={11}&IdwalletInfo={2}&codice_servizio={20}&codice_scuola={4}&codice_anagrafica_figlio={1}&importo_tariffa={15}&anno_scolastico={9}&tx_presenze_action=calendario&codiceSocieta=${codiceSocieta}&codiceUtente=${codiceUtente}&chiaveEnte=${chiaveEnte}"
					imagesrc="../applications/templates/shared/img/calendar.gif"
					alt="Dettaglio" text=""
					cssclass="blacklink hlStyle" />
		</s:dgcolumn>
		
	</s:datagrid>
	</s:div>
	 </c:if>
	</s:div>
	</c:if>
	
	<c:if test="${flagPresenzeCalendario}">
	   <s:div cssclass="seda-ui-datagrid thDettaglioTitle1 bold" name="divTitleDett">Presenza</s:div>
	   <table class="seda-ui-datagrid lista_servizi_wallet" border="0" cellpadding="0" cellspacing="0">
  <thead>
       <tr>  
         <th class="seda-ui-datagridheadercell textleft"> Codice Fiscale&nbsp;</th>  
         <th class="seda-ui-datagridheadercell textleft"> Denominazione&nbsp;</th>  
         <th class="seda-ui-datagridheadercell textleft"> Servizio&nbsp;</th>  
         <th class="seda-ui-datagridheadercell textleft"> Mese Presenza&nbsp;</th>  
       </tr> 
  </thead> 
  <tbody> 
      <tr class="seda-ui-datagridrowdispari ">  
         <td class="seda-ui-datagridcell   seda-ui-datatype-varchar textleft">${codiceFiscaleFiglio }</td> 
         <td class="seda-ui-datagridcell   seda-ui-datatype-varchar textleft">${denominazione}</td> 
         <td class="seda-ui-datagridcell   seda-ui-datatype-varchar textleft">${servizio}</td> 
         <td class="seda-ui-datagridcell textleft ">  
            <c:if test="${mese==1}" > 
			     Gennaio
		    </c:if>
		    <c:if test="${mese==2}" >	  
			     Febbraio
		    </c:if>
		    <c:if test="${mese==3}" >
			     Marzo
		    </c:if>
		    <c:if test="${mese==4}" >
			     Aprile
		    </c:if>
		    <c:if test="${mese==5}" >
			     Maggio 
		    </c:if>
		    <c:if test="${mese==6}" >
			     Giugno 
		    </c:if>
		    <c:if test="${mese==7}" >
			     Luglio 
		    </c:if>
		    <c:if test="${mese==8}" > 
			     Agosto
		    </c:if>
		    <c:if test="${mese==9}" >
			     Settembre
		    </c:if>
		    <c:if test="${mese==10}" >
			     Ottobre
		    </c:if>
		    <c:if test="${mese==11}" >
			     Novembre
		    </c:if>
		    <c:if test="${mese==12}" >
			     Discembre 
		    </c:if>
         
       
         
         </td>  
     </tr>   
</tbody> 
</table>
 
 <s:div cssclass="seda-ui-datagrid thDettaglioTitle1 bold" name="divTitleDett">Giorni presenze </s:div>
 
 <c:if test="${listaPresenzeMensiliVuota=='no'}">
 
  <table class="seda-ui-datagrid lista_servizi_wallet" border="0" cellpadding="0" cellspacing="0">
  <thead>
       <tr>  
         <th class="seda-ui-datagridheadercell textleft"> Codice Fiscale&nbsp;</th>  
         <th class="seda-ui-datagridheadercell textleft"> Denominazione&nbsp;</th>  
         <th class="seda-ui-datagridheadercell textleft"> Servizio&nbsp;</th>  
         <th class="seda-ui-datagridheadercell textleft"> Descrizione&nbsp;</th>  
         <th class="seda-ui-datagridheadercell textleft"> Giorno Presenza&nbsp;</th>
         <th class="seda-ui-datagridheadercell textright"> Importo&nbsp;</th>
         <th class="seda-ui-datagridheadercell textleft"> Operatore Inserimento&nbsp;</th> 
         <th class="seda-ui-datagridheadercell textleft"> Tipo&nbsp;</th>
         <th class="seda-ui-datagridheadercell textleft"> &nbsp;&nbsp;&nbsp;&nbsp;</th>   
       </tr> 
  </thead> 
  <tbody> 
    <c:forEach items="${listaPresenzeMensili}" var="presenza"  >
      <tr class="seda-ui-datagridrowdispari ">  
         <td class="seda-ui-datagridcell   seda-ui-datatype-varchar textleft">${codiceFiscaleFiglio }</td> 
         <td class="seda-ui-datagridcell   seda-ui-datatype-varchar textleft">${denominazione}</td> 
         <td class="seda-ui-datagridcell   seda-ui-datatype-varchar textleft">${servizio}</td> 
         <td class="seda-ui-datagridcell   seda-ui-datatype-varchar textleft">${presenza.descrizioneTributo}</td> 
         <td class="seda-ui-datagridcell textright "> ${presenza.dataPresenzaPerDatagrid} </td>
         <td class="seda-ui-datagridcell textright "> ${presenza.importo} </td>
         <td class="seda-ui-datagridcell textleft "> ${presenza.operatoreInserimento} </td>
         <td class="seda-ui-datagridcell textleft "> 
           <c:if test="${presenza.causale=='C'}">
             Carico
           </c:if>
           <c:if test="${presenza.causale=='D'}">
             Discarico
           </c:if>
           <c:if test="${presenza.causale=='R'}">
             Ricarico
           </c:if>
           <c:if test="${presenza.causale=='S'}">
             Discarico su Ricarico 
            </c:if>        
         </td>
         <td class="seda-ui-datagridcell textleft ">
	         <c:if test="${presenza.causale=='C'&&presenza.servizioAbilitato=='Y'&&presenza.progressivo==presenza.maxProgressivo}">
	         <s:hyperlink
						href="walletsollecitodiscaricoedit.do?tx_presenze_action=applica_discarico&IdwalletInfo=${IdwalletInfo}&causale=${presenza.causale}&progressivo=${presenza.progressivo}&dataPresenza=${presenza.dataPresenza}"
						alt="Applica Discarico" text=""
						imagesrc="../applications/templates/walletmanager/img/azioniRed.png"
						cssclass="blacklink hlStyle"/> 
			 </c:if> 
		 <c:if test="${presenza.causale!='S'&&presenza.causale=='S'&&presenza.servizioAbilitato=='Y'&&presenza.progressivo==presenza.maxProgressivo}">
         <s:hyperlink
					href="walletsollecitodiscaricoedit.do?tx_presenze_action=annulla_discarico&IdwalletInfo=${IdwalletInfo}&causale=${presenza.causale}&progressivo=${presenza.progressivo}&dataPresenza=${presenza.dataPresenza}"
					alt="Annullamento Discarico" text=""
					imagesrc="../applications/templates/walletmanager/img/azioniGreen.png"
					cssclass="blacklink hlStyle"/> 
		 </c:if>
		 <c:if test="${presenza.causale=='D'&&presenza.servizioAbilitato=='Y'&&presenza.progressivo==presenza.maxProgressivo}">
         <s:hyperlink
					href="walletsollecitodiscaricoedit.do?tx_presenze_action=annulla_discarico&IdwalletInfo=${IdwalletInfo}&causale=${presenza.causale}&progressivo=${presenza.progressivo}&dataPresenza=${presenza.dataPresenza}"
					alt="Annullamento Discarico" text=""
					imagesrc="../applications/templates/walletmanager/img/azioniGreen.png"
					cssclass="blacklink hlStyle"/> 
		 </c:if>
			 <c:if test="${presenza.causale=='R'&&presenza.servizioAbilitato=='Y'&&presenza.progressivo==presenza.maxProgressivo}">
	         <s:hyperlink
						href="walletsollecitodiscaricoedit.do?tx_presenze_action=applica_discarico&IdwalletInfo=${IdwalletInfo}&causale=${presenza.causale}&progressivo=${presenza.progressivo}&dataPresenza=${presenza.dataPresenza}"
						alt="Applica Discarico" text=""
						imagesrc="../applications/templates/walletmanager/img/azioniRed.png"
						cssclass="blacklink hlStyle"/> 
			 </c:if>
         
         </td>
   </tr>
     </c:forEach>
   </tbody>
   
   </table>
 
    </c:if>
    <c:if test="${listaPresenzeMensiliVuota=='si'}"> 
     <s:div name="servizioNonabilitato" cssclass="textred divRicMetadatiSingleRowRead">
      Non ci sono presenze per il mese selezionato
     </s:div>
    </c:if>


	</c:if>
	
	<br/>
	<s:form name="frmIndietro1" action="walletsollecitodiscarico.do?vista=walletsollecitodiscarico&datagrid_action=cerca" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
	<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
	   <c:if test="${!flagPresenzeCalendario}">
		<s:button id="tx_button_indietro" type="submit" text="Indietro" onclick="" cssclass="btnStyle" />
	   </c:if> 
	</s:div>
	</s:form>
	
	
</s:div>
</s:form>




<s:form name="frmIndietroDettaglio" action="walletsollecitodiscaricoedit.do?vista=walletsollecitodiscarico&tx_idwallet_h=${tx_idwallet_h}" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
  <c:if test="${flagPresenzeCalendario}">
      <s:div name="divRicercaBottoni_calendario" cssclass="divRicBottoni">
		<s:button id="tx_button_null" type="submit" text="Indietro" onclick="" cssclass="btnStyle" />
		 <input type="hidden" name="tx_presenze_action" value="presenze" />
		 <input type="hidden" name="codiceSocieta" value="${codiceSocieta} " />
		 <input type="hidden" name="codiceUtente" value="${codiceUtente}" />
		 <input type="hidden" name="chiaveEnte" value="${chiaveEnte}" />
		 <input type="hidden" name="IdwalletInfo" value="${IdwalletInfo}" />
	  </s:div>
   </c:if>
</s:form>


	
	
	







