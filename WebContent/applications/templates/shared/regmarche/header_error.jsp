<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:div name="divCommonHeader" cssclass="Header">


	<c:url value="/applications/templates/shared/img/LogoPeople.png" var="logolepida"/>
	<c:url value="/applications/templates/shared/img/titolo.png" var="logotitolo"/>
		
	<s:image src="${logolepida}" alt="MPay Regione Marche" width="105" height="40" cssclass="logomondo posleft block" />
	<s:image src="${logotitolo}" width="278" height="33" alt="Gestione archivi di base" cssclass="logotitolo posleft block" />

	
</s:div>	
