<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="filtriestrattoconto" encodeAttributes="true" />

<br />
<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Filtri Estratto Conto</s:div>

		<s:form name="form_ricerca" action="filtriec.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<input type="hidden" name="action" value="search" />

			<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
				<s:div name="divElement1" cssclass="divRicMetadatiLeft">
					<s:textbox bmodify="true" name="fec_codiceFiscale_ric"
						label="Cod. Fiscale:" maxlenght="16"
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" text="${fec_codiceFiscale_ric}"/>

				</s:div>
				<s:div name="divElement2" cssclass="divRicMetadatiLeftDouble">
				<!-- Giulia 23032013 Inizio -->
					<!--<s:dropdownlist name="fec_ddlSocUteEnt_ric"  
							label="Societ&agrave;/Utente/Ente: "
									cssclasslabel="label130 textright bold floatleft"
									cssclass="seda-ui-ddl tbddlMax500 floatleft" disable="false"
									   cachedrowset="listaSocietaUtenteEnteSearch" usexml="true" 
									   valueselected="${fec_ddlSocUteEnt_ric}">
   							<s:ddloption value="" text="Tutti gli enti" />
							<s:ddloption value="{1}|{2}|{3}" text="{4}"/>
						</s:dropdownlist>-->
				<!-- Giulia 23032013 Fine -->		
						<s:textbox bmodify="true" name="fec_ente_ric"
						label="Ente:" maxlenght="6" 
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" text="${fec_ente_ric}"/>
				</s:div>
			</s:div>
			
			<s:div name="divRicercaMetadatiLeft" cssclass="divRicMetadatiLeft">
		        <s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
		        	<s:textbox bmodify="true" name="fec_impostaServizio_ric"						
						label="Imp. Servizio:" maxlenght="2"
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" text="${fec_impostaServizio_ric}" />		        
		        </s:div>
			</s:div>
			<s:div name="divRicercaMetadatiCenter" cssclass="divRicMetadatiCenter">
		        <s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
		        	<s:textbox bmodify="true" name="fec_numeroEmissione_ric"						
						label="N° Emissione:" maxlenght="6"
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" text="${fec_numeroEmissione_ric}" />		        
		        </s:div>
			</s:div>
			<s:div name="divRicercaMetadatiRight" cssclass="divRicMetadatiRight">
		        <s:div name="divElement3" cssclass="divRicMetadatiSingleRow">
		        	<s:textbox bmodify="true" name="fec_numeroDocumento_ric"						
						label="N° Docum.:" maxlenght="20"
						cssclasslabel="label85 bold textright"
						cssclass="textareaman" text="${fec_numeroDocumento_ric}" />		        
		        </s:div>
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
			Elenco Filtri Estratto Conto
	</s:div>
</s:div>
