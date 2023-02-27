<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="adminusers_var" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>


<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>




<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="enable_form" action="seUsersEnable.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false" >
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Abilita Utenze
			</s:div>
			
				<c:if test="${sessionScope.enable_step!='step2'}">
					<s:div name="divElement50" cssclass="div_align_center">
			           Sei sicuro di voler 
			           <c:if test="${!sessionScope.profilo.utenzaAttiva}">
			              abilitare  <input type="hidden" name="utenzaAttiva" value="Y" />
			           </c:if>
			           <c:if test="${sessionScope.profilo.utenzaAttiva}">
			              disabilitare <input type="hidden" name="utenzaAttiva" value="N" />
			           
			           </c:if>
			           l'utenza  <span style="font-weight: bold;"> ${sessionScope.profilo.username}</span>
			           ?     
					</s:div>
				</c:if>
				<br/>
				<br/>	
				
				<input type="hidden" name="tx_username" value="${sessionScope.profilo.username}" />
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">			  
			 	  
			  <c:if test="${sessionScope.enable_step!='step2'}">
			      <s:button  id="tx_button_avanti" onclick="" text="Conferma" type="submit" cssclass="btnStyle" /> 
			  </c:if>	
			   <s:button id="tx_button_cerca" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />	
			</s:div>
	</s:form>

	</s:div>
	
</s:div>




