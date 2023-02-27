<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="posfisico" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="divECContainer">
	
	<s:div name="divTop" cssclass="divECTop">
	</s:div>
	<s:div name="divOuter" cssclass="divPOSOuter">
		<s:form name="form_posfisico" action="posfisico.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divTitle" cssclass="divECTitle">POS FISICO - ACCESSO OPERATORE</s:div>
			
			<s:div name="divPOSLink" cssclass="divPOSLink">
				<s:div name="divPOSCodiceFiscale" cssclass="divPOSCodiceFiscale">
					<s:textbox name="tbEleCodFiscale" bmodify="true" label="Codice fiscale/Partita Iva: " maxlenght="16" cssclasslabel="labelECTop" 
						text="${tbEleCodFiscale}" cssclass="tbECBottom" 
						tabindex="1" validator="ignore;accept=${posfisico_codicefisc_piva_regex};maxlength=16;"
						message="[accept=Codice fiscale/Partita Iva: ${msg_configurazione_codicefiscale_piva}]"/>
				</s:div>
				
				<s:button id="btnLoginOperatore" type="submit" validate="true" tabindex="1" 
					text="ESEGUI IL LOGIN COME OPERATORE POS" onclick="" cssclass="btnVaiPOS"/>
			</s:div>
		</s:form>
	</s:div>		
	
</s:div>





