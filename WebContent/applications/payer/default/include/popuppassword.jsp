<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<link href="../applications/templates/default/css/default.css" rel="stylesheet" type="text/css" >


<c:if test="${ggAvvisoScadenzaPassword != null && ggScadenzaPassword != null}">
	<c:if test="${ggScadenzaPassword <= ggAvvisoScadenzaPassword }">
	<s:form name="sceltaProfilo_form" action="sceltaProfilo.do" method="post" 
	hasbtn1="false" hasbtn2="false" hasbtn3="false" >
		<s:div name="divShadowPasswordScaduta" cssclass="divpopupshadow"></s:div>
		<s:div name="divPopupPasswordScaduta" cssclass="divpopup" >
			<s:div name="divPopupPasswordScaduta"  cssclass="divpopupcontent" >
				<p class="title">
				<s:image src="../applications/templates/default/img/alert_red.png" alt="Password Scaduta" width="" height="" cssclass="iconAlert"/>
				ATTENZIONE</p>
				<s:div name="body"  cssclass="body">
					
					<h2>La password scadrà  
					<c:choose> 
						<c:when test="${ggScadenzaPassword > 0}">
							tra ${ggScadenzaPassword} giorni
						</c:when>
						<c:when test="${ggScadenzaPassword == 0}">
							oggi
						</c:when>
					
					</c:choose>
					</h2>
					<p>Nel caso in cui la password non venga cambiata, l'utenza verrà disattivata</p>
				</s:div>
				<p>
					<s:hyperlink  cssclass="btnBlue" href="../login/cambioPswd.do" text="Cambia Password" />
					<s:button id="tx_button_cancel" type="submit" text="Chiudi" tabindex="3" onclick="" cssclass="btnGray"/>
					<br>
				</p>
				
			</s:div>
		</s:div>
		</s:form>
	</c:if>
	</c:if>
	