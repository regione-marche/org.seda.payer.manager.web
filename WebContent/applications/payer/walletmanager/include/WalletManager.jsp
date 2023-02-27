<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="viewstate" encodeAttributes="true" />


<s:div name="div_selezione" cssclass="divECContainer">
	
	<s:div name="divECTopName" cssclass="divECTop">
	</s:div>
	<s:div name="divECFillName" cssclass="divECFill">
		<s:form name="form_walletmanager" action="walletmanager.do"  method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:div name="divECTitleName" cssclass="divECTitle">BORSELLINO MANAGER
			</s:div>
			<s:div name="divECContainerGrayName" cssclass="divECContainerGray">
				<s:div name="divECCodiceFiscale" cssclass="divECCodiceFiscale">
					<s:textbox name="tbIdWallet" bmodify="true" label="Codice Borsellino: " maxlenght="18" cssclasslabel="labelWalletManager" 
								text="${tbIdWallet}" cssclass="tbWalletBottomManager" showrequired="false"
								tabindex="1" validator="maxlength=18;"
								message="[accept=Codice Borsellino: ${msg_IDWallet}]"/>
					<!-- validator="ignore;accept=^[0-9]{11}|[a-zA-Z]{6}\d\d[a-zA-Z]\d\d[a-zA-Z]\d\d\d[a-zA-Z]$;maxlength=16"	-->
					<s:textbox name="tbEleCodFiscale" bmodify="true" label="Codice fiscale/Partita Iva: " maxlenght="16" cssclasslabel="labelWalletManager" 
								text="${CFMAN}" cssclass="tbWalletBottomManager" showrequired="false"
								tabindex="1" 
								validator="ignore;maxlength=16"
								message="[accept=Codice fiscale/Partita Iva: ${msg_configurazione_codicefiscale_piva}]"/>
				</s:div>
				<s:div name="divWalletLink" cssclass="divECLink">
					<s:button id="btnWalletManager" type="submit" validate="true" tabindex="2" text="VAI A BORSELLINO" onclick="" cssclass="btnVaiEC"/>
				</s:div>
			</s:div>
		</s:form>
	</s:div>		
	
</s:div>





