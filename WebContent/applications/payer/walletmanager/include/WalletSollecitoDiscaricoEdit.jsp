<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="walletricaricheborsellinoedit" encodeAttributes="true" />

<fmt:setLocale value="it_IT" />
<s:form name="frmIndietro" action="walletsollecitodiscarico.do?vista=walletsollecitodiscarico" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
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
				<s:td>${codice_borsellino}</s:td>
				<s:td cssclass="seda-ui-cellheader">Codice Fiscale:</s:td>
				<s:td>${codice_fiscale}</s:td>
			</s:tr>
			<s:tr>
				<s:td cssclass="seda-ui-cellheader">Denominazione:</s:td>
				<s:td>${denominazione}</s:td>
				<s:td cssclass="seda-ui-cellheader" >Residuo Borsellino:</s:td>
				<s:td>${RESIDUO}</s:td>
			</s:tr> 
		</s:tbody>
	</s:table>
		
	<s:div cssclass="seda-ui-datagrid thDettaglioTitle1" name="divTitleDett">Lista solleciti effettuati </s:div>
	<s:datagrid cssclass="lista_servizi_wallet" cachedrowset="lista_dettaglio_solleciti" action="" usexml="true" border="0">
		<s:dgcolumn index="3" label="Data Sollecito" css="textleft"/>
		<s:dgcolumn index="6" label="Descrizione Onere" css="textleft"  />  
		<s:dgcolumn index="7" label="Importo Dovuto" css="textright" format="#0.00" />
		<s:dgcolumn index="8" label="Importo Pagato" css="textright" format="#0.00" />
		
		<s:dgcolumn index="23" label="Data Inserimento" css="textleft" format='dd/MM/yyyy' />  
		<s:dgcolumn index="24" label="Operatore Inserimento" css="textleft"  />  
		
		<s:dgcolumn label="Tipo" css="textleft" >
		<s:if left="{21}" control="eq" right="D" >
			<s:then> Discarico </s:then>
		</s:if>
		<s:if left="{21}" control="eq" right="A" >
			<s:then> Annullamento Discarico</s:then>
		</s:if>
		<s:if left="{21}" control="eq" right=" " operator="and" secondleft="{19}" secondcontrol="eq" secondright="0">
			<s:then> Sollecito Cartaceo </s:then>
		</s:if>
		</s:dgcolumn>
		<s:dgcolumn label="Azioni" css="textleft" >
		<s:if left="{21}" control="eq" right=" " operator="and" secondleft="{26}{27}" secondcontrol="eq" secondright="SY">
			<s:then>
			   <s:hyperlink
					href="walletsollecitodiscaricoedit.do?IdwalletInfo={1}&data_sollecito={3}&data_da={3}&data_a={3}&azione_sollecito=discarico&prog_sollecito={19}&caricoEnte={20}" 
					alt="Applica Discarico" text=""
					imagesrc="../applications/templates/walletmanager/img/azioniRed.png"
					cssclass="blacklink hlStyle"/>
			</s:then>
		</s:if>
		<s:if left="{21}{25}" control="eq" right="D " operator="and" secondleft="{26}{27}" secondcontrol="eq" secondright="SY">
			<s:then>
			   <s:hyperlink
					href="walletsollecitodiscaricoedit.do?IdwalletInfo={1}&data_sollecito={3}&data_da={3}&data_a={3}&azione_sollecito=annullamento&prog_sollecito={19}&caricoEnte={20}"
					alt="Annullamento Discarico" text=""
					imagesrc="../applications/templates/walletmanager/img/azioniGreen.png"
					cssclass="blacklink hlStyle"/>
			</s:then>
		</s:if>
		<s:if left="{21}{25}" control="eq" right="DS" operator="and" secondleft="{26}{27}" secondcontrol="eq" secondright="SY">
			<s:then>
			   <s:hyperlink
					href=""
					alt="Annullamento non consentito in quanto Discarico evoluto in intimazione" text=""
					imagesrc="../applications/templates/walletmanager/img/azioniGrey.png"
					cssclass="blacklink hlStyle"/>
			</s:then>
		</s:if>
		<s:if left="{21}" control="eq" right="A" operator="and" secondleft="{26}{27}" secondcontrol="eq" secondright="SY">
			<s:then>
			   <s:hyperlink
					href="walletsollecitodiscaricoedit.do?IdwalletInfo={1}&data_sollecito={3}&azione_sollecito=discarico&prog_sollecito={19}&caricoEnte={20}"
					alt="Applica Discarico" text=""
					imagesrc="../applications/templates/walletmanager/img/azioniRed.png"
					cssclass="blacklink hlStyle"/>
			</s:then>
		</s:if>
		</s:dgcolumn>
	</s:datagrid>
</s:div>
	

<br/>
	<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
		<s:button id="tx_button_indietro" type="submit" text="Indietro" onclick="" cssclass="btnStyle" />
	</s:div>
</s:form>


	<c:if test="${Show_Dialog_Discarico||Show_Dialog_Annullamento}">
	<s:div cssclass="seda-ui-datagrid thDettaglioTitle1" name="divTitleDett">Inserimento Discarico </s:div>
	
	   <c:if test="${Show_Dialog_Discarico}">
	     <s:div name="InserimentoDiscarico" cssclass="bold">
	       
	     </s:div>
	   </c:if> 
	   
	   
	   <c:if test="${Show_Dialog_Annullamento}">
	     <s:div name="InserimentoDiscarico" cssclass="bold">
	       
	     </s:div>
	   </c:if> 
	      
	    <s:datagrid cssclass="lista_servizi_wallet" cachedrowset="select_chosen_sollecito" action="" usexml="true" border="0">
		<s:dgcolumn index="3" label="Data Sollecito" css="textleft"/>
		<s:dgcolumn index="6" label="Descrizione Onere" css="textleft"  />
		<s:dgcolumn index="7" label="Importo Dovuto" css="textright" format="#0.00" />
		<s:dgcolumn index="8" label="Importo Pagato" css="textright" format="#0.00" />	
		<s:dgcolumn index="23" label="Data Inserimento" css="textleft" format='dd/MM/yyyy' />  
		<s:dgcolumn index="24" label="Operatore Inserimento" css="textleft"  /> 				
	</s:datagrid>
	
	<c:if test="${Show_Dialog_Discarico}">
	
	<s:div name="divElement8" cssclass="divRicBottoni marginTop">
	  <s:form name="form_selezione" action="walletsollecitodiscaricoedit.do?IdwalletInfo=${id_wallet_sollecito}&prog_sollecito=${prog_sollecito}&data_a=${data_sollecito}&data_da=${data_sollecito}&data_sollecito=${data_sollecito}" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
						<s:dropdownlist name="tx_flagDiscaricoSollecito" disable="false"
							cssclass="tbddlMax floatleft" label="A carico Ente o Soris:"
							cssclasslabel="label85 bold textright"
							valueselected="${tx_flagDiscaricoSollecito}">
							<s:ddloption text="A carico Ente" value="Y" />
							<s:ddloption text="A carico Soris" value="N" />
						</s:dropdownlist>
						<s:button id="tx_button_discarico" onclick="" text="Conferma" cssclass="btnStyle" type="submit" />
						<s:button id="tx_button_null" onclick="" text="Annulla" cssclass="btnStyle" type="submit" />
	  </s:form>
	</s:div>
	
	</c:if>
	
	
	<c:if test="${Show_Dialog_Annullamento}">
	 
	<s:div name="divElement8" cssclass="divRicBottoni marginTop" >
	  <s:form name="form_selezione" action="walletsollecitodiscaricoedit.do?IdwalletInfo=${id_wallet_sollecito}&prog_sollecito=${prog_sollecito}&data_a=${data_sollecito}&data_da=${data_sollecito}&data_sollecito=${data_sollecito}" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
						<s:dropdownlist name="tx_flagDiscaricoSollecito" disable="true"
							cssclass="tbddlMax floatleft" label="A carico Ente o Soris:"
							cssclasslabel="label85 bold textright"
							valueselected="${tx_flagDiscaricoSollecito}">
							<s:ddloption text="A carico Ente" value="Y" />
							<s:ddloption text="A carico Soris" value="N" />
						</s:dropdownlist>
						<s:button id="tx_button_annullamento" onclick="" text="Conferma" cssclass="btnStyle" type="submit" />
						<s:button id="tx_button_null" onclick="" text="Annulla" cssclass="btnStyle" type="submit" />
	  </s:form>
	</s:div>
	
	</c:if>
	
	
	</c:if>
	
	







