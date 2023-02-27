<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"  %>
<m:setBundle name="org.seda.payer.manager.resources.TemplateStrings" />

 
 <s:div name="divFooterContainer" cssclass="divFooterContainer">
	 <s:div name="divFooterLeft" cssclass="footerLeft">
	 	<s:image src="../applications/templates/shared/img/LogoLepida_small.png" alt="Payer Lepida" width="92" height="35" cssclass="imgLogoFooterLepida" />
	 </s:div>
	 <s:div name="divFooterRight" cssclass="footerRight">
		 <s:div cssclass="footerTop" name="divFooterImmagini">
		 	|&nbsp;&nbsp;&nbsp;&nbsp;<s:hyperlink href="../sitoistituzionale/privacy.do" text="PRIVACY" cssclass="blacklinksimple"/>&nbsp;&nbsp;&nbsp;&nbsp;|
		 	<%--&nbsp;&nbsp;&nbsp;&nbsp;<s:hyperlink href="../sitoistituzionale/mappasito.do" text="MAPPA DEL SITO" cssclass="blacklinksimple"/>&nbsp;&nbsp;&nbsp;&nbsp;|
		 	&nbsp;&nbsp;&nbsp;&nbsp;<s:hyperlink href="../sitoistituzionale/notelegali.do" text="NOTE LEGALI" cssclass="blacklinksimple"/>&nbsp;&nbsp;&nbsp;&nbsp;|
		 	&nbsp;&nbsp;&nbsp;&nbsp;<s:hyperlink href="../sitoistituzionale/infoazienda.do" text="INFO AZIENDA" cssclass="blacklinksimple"/>&nbsp;&nbsp;&nbsp;&nbsp;|
		 	&nbsp;&nbsp;&nbsp;&nbsp;<s:hyperlink href="../sitoistituzionale/credits.do" text="CREDITS" cssclass="blacklinksimple"/>&nbsp;&nbsp;&nbsp;&nbsp;| --%>

		 </s:div> 
		 <s:div name="divFooterSociale" cssclass="footersociale"> <m:message key="footer.message.lepida"/></s:div>
	</s:div>
 </s:div> 

