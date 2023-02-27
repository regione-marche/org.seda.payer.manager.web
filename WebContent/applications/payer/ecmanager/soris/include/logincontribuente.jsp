<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="viewstate" encodeAttributes="true" />


<s:div name="div_selezione" cssclass="divECContainer">
	
	<s:div name="divECTopName" cssclass="divECTop">
	</s:div>
	<s:div name="divECFillName" cssclass="divECFillSoris">
		<s:form name="form_ecmanager" action="logincontribuente.do"  method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divECTitleName" cssclass="divECTitle">ESTRATTO CONTO MANAGER
			</s:div>
			<s:div name="divECContainerGrayName" cssclass="divECContainerGrayEC">
				<s:div name="divECCodiceFiscale" cssclass="divECCodiceFiscaleTrentrisc">
					<s:textbox name="tbEleCodFiscale" bmodify="true" label="Codice fiscale/Partita Iva: " maxlenght="16" cssclasslabel="labelECTop" 
								text="${tbEleCodFiscale}" cssclass="tbECBottomCodFisc" showrequired="false"
								tabindex="1" validator="maxlength=16;ignore;"
								message="[accept=Codice fiscale/Partita Iva: ${msg_configurazione_codicefiscale_piva}]"/>
				</s:div>
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
				<s:div name="divElement8" cssclass="divRicMetadatiSingleRowEC">
						<s:textbox name="codEnte" bmodify="true" label="Cod.Ente: " maxlenght="6" cssclasslabel="labelECTopEnte2" 
									text="${codEnte}" cssclass="tbECBottomEnte2" showrequired="false"
									tabindex="2" validator="maxlength=6;"
									message="[accept=Cod. Ente: ${msg_configurazione_alfanumerici}]"/>
						<s:textbox name="numDoc" bmodify="true" label="Id. Documento:" maxlenght="14" cssclasslabel="labelECTopDocumento2" 
									text="${numDoc}" cssclass="tbECBottomDocumento2" showrequired="false"
									tabindex="2" validator="accept=^[a-zA-Z0-9]{0,20}$;maxlength=14;"
									message="[accept=Id. Documento: ${msg_configurazione_alfanumerici}]"/>
						<s:textbox name="progCoob" bmodify="true" label="Prog. Coob: " maxlenght="3" cssclasslabel="labelECTopProgCoob" 
											text="${progCoob}" cssclass="tbECBottomProgCoob" showrequired="false"
											tabindex="2" validator="accept=^[\d]{0,3}$;maxlength=3;"
											message="[accept=Prog. Coobbligato:${msg_configurazione_numero}]"/>
					</s:div>
				</s:div>
				
				
				
				
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRowEC">
						<s:textbox name="idBoll" bmodify="true" label="Id. Bollettino: " maxlenght="18" cssclasslabel="labelECTop" 
										text="${idBoll}" cssclass="tbECBottom" showrequired="false"
										tabindex="3" validator="accept=^[\d]{0,18}$;maxlength=18;"
										message="[accept=Id. Bollettino:${msg_configurazione_alfanumerici} ]"/>
					</s:div>
				</s:div>
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRowEC">
						<s:textbox name="numRacc" bmodify="true" label="Num. Raccomandata: " maxlenght="12" cssclasslabel="labelECTop" 
									text="${numRacc}" cssclass="tbECBottom" showrequired="false"
									tabindex="4" validator="accept=^[a-zA-Z0-9]{0,12}$;maxlength=12;"
									message="[accept=Num. Raccomandata: ${msg_configurazione_alfanumerici}]"/>
					
					</s:div>
				</s:div>
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRowEC">
						<s:textbox name="numCron" bmodify="true" label="Num. Cronologico: " maxlenght="12" cssclasslabel="labelECTop" 
										text="${numCron}" cssclass="tbECBottom" showrequired="false"
										tabindex="5" validator="accept=^[a-zA-Z0-9]{0,12}$;maxlength=12;"
										message="[accept=Num. Cronologico: ${msg_configurazione_alfanumerici}]"/>
					</s:div>
				</s:div>
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRowEC">
						<s:textbox name="barCode" bmodify="true" label="Id. BarCode: " maxlenght="20" cssclasslabel="labelECTop" 
									text="${barCode}" cssclass="tbECBottom" showrequired="false"
									tabindex="6" validator="accept=^[a-zA-Z0-9]{0,20}$;maxlength=20;"
									message="[accept=Id. Azione Esecutiva: ${msg_configurazione_alfanumerici}]"/>
					
					</s:div>
				</s:div>
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement8" cssclass="divRicMetadatiSingleRowEC">
						<s:textbox name="idProcedura" bmodify="true" label="Id. Procedura Esecutiva: " maxlenght="16" cssclasslabel="labelECTop" 
										text="${idProcedura}" cssclass="tbECBottom" showrequired="false"
										tabindex="7" validator="accept=^[a-zA-Z0-9]{0,16}$;maxlength=16;"
										message="[accept=Id. Procedura Esecutiva: ${msg_configurazione_alfanumerici}]"/>
					</s:div>
				</s:div>
				<s:div name="divECLink" cssclass="divECLink">
					<s:button id="btnEstrattoConto" type="submit" validate="true" tabindex="2" text="VAI A ESTRATTO CONTO" onclick="" cssclass="btnVaiEC"/>
				</s:div>
			</s:div>
			<input type="hidden" name="ecagemanager" value="${ecagemanager}" />
		</s:form>
	</s:div>		
	
</s:div>





