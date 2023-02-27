<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:setBundle name="com.seda.portale.i18n.resources.TemplateStrings" />
	<%-- <c:if test="${!empty federa_filter}"> --%>
		<br/>
		<s:div name="div_selezione" cssclass="divLogin div_align_center">
			<s:div name="divRicercaTopName">
				<div class="divIntestazioneLogin"><c:if test="${!empty message}">Accesso negato&nbsp;-&nbsp;${message}</c:if></div>
				<br/>
				<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Benvenuto sull'applicazione manager di Pagonet</s:div>
				<br/>
				<s:div name="divRicercaTitleName" cssclass="div_align_center">Per poter accedere a questa applicazione devi essere abilitato dagli amministratori di Pagonet.<br/><br/>Prima di procedere scegli il sistema di autenticazione da utilizzare.</s:div>

				<br/>
				<br/>
				
				<s:form name="trentrisc_login" cssclass="padding_bottom_20"
					action="sceltaAut.do" method="post"
					hasbtn1="false" hasbtn2="false" hasbtn3="false">
					<br/>
					<br/>
					
					<s:div name="div_tipo_autenticazione_pdp" cssclass="divProfilo">
						<s:list name="rb_aut_proprietaria" cssclass="checkleft" bradio="true" groupname="autenticazione" 
								text="Login Pagamenti On-Line" cssclasslabel="rbAutenticazioneLbl" 
								bchecked="true" value="aut_pdp"
								validator="ignore" />
					</s:div>
					<s:div name="div_tipo_autenticazione_federata" cssclass="divProfilo">
						<s:list name="rb_aut_federata" cssclass="checkleft" bradio="true" groupname="autenticazione" 
								text="Login carta provinciale dei servizi" cssclasslabel="rbAutenticazioneLbl" 
								bchecked="false" value="aut_federata" disable="false"
								validator="ignore" />	
						<%-- 
						<s:div name="divDescr_${profiloItem.chiaveUtente}" cssclass="divDescrProfilo">
							Autenticazione proprietaria della Piattaforma di Pagamento
						</s:div> 
						--%>
					</s:div>
					<s:div name="divAvanti" cssclass="divRicBottoni">
						<s:button id="button_avanti" onclick="" text="Avanti" cssclass="btnStyle" type="submit" />
					</s:div>
				</s:form>
			</s:div>
		</s:div>
	<%-- </c:if> --%>