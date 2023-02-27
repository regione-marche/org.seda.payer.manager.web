<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="viewstate" encodeAttributes="true" />


<s:div name="div_selezione" cssclass="divECContainer">
	
	<s:div name="divECTopName" cssclass="divECTop">
	</s:div>
	<s:div name="divECFillName" cssclass="divECFill">
		<s:form name="form_mercatimanager" action="mercatimanager.do"  method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divECTitleName" cssclass="divECTitle">MERCATI MANAGER
			</s:div>
			<s:div name="divECContainerGrayName" cssclass="divECContainerGray">
				<s:div name="divECCodiceFiscale" cssclass="divECCodiceFiscale">
					<s:textbox name="tbEleCodFiscale" bmodify="true" label="Codice fiscale/Partita Iva: " maxlenght="16" cssclasslabel="labelWalletManager" 
								text="${CFMAN}" cssclass="tbWalletBottomManager" showrequired="true"
								tabindex="1" 
								validator="required;maxlength=16"
								message="[accept=Codice fiscale/Partita Iva: ${msg_configurazione_codicefiscale_piva}]"/>
				</s:div>
				<s:div name="divMercatoLink" cssclass="divECLink">
					<s:button id="btnMercatiManager" type="submit" validate="true" tabindex="2" text="VAI A MERCATI" onclick="" cssclass="btnVaiEC"/>
				</s:div>
			</s:div>
		</s:form>
	</s:div>		
	
</s:div>





