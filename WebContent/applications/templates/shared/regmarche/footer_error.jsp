<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<m:setBundle name="org.seda.payer.manager.resources.TemplateStrings" />

 
 <s:div name="divFooterContainer" cssclass="divFooterContainer">
	 <s:div name="divFooterLeft" cssclass="footerLeft">
	 	<c:url value="/applications/templates/shared/img/LogoLepida_small.png" var="logolepida"/>
	 	<s:image src="${logolepida}" alt="MPay Regione Marche" width="92" height="35" cssclass="imgLogoFooterLepida" />
	 </s:div>
	 <s:div name="divFooterRight" cssclass="footerRight">
		 <s:div cssclass="footerTop" name="divFooterImmagini">
		 	&nbsp;&nbsp;&nbsp;&nbsp;
		 </s:div> 
		 <s:div name="divFooterSociale" cssclass="footersociale"> <m:message key="footer.message.lepida"/></s:div>
	</s:div>
 </s:div> 

