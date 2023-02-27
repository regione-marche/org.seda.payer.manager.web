<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:view_state id="wismanager" encodeAttributes="true" />

<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>


<s:div name="div_selezione" cssclass="divWISContainer">
	<s:form name="form_selezione" action="wismanager.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:div name="divWISTopName" cssclass="divWISTop">
		</s:div>
		<s:div name="divWISFillName" cssclass="divWISFill">
			<s:div name="divWISTitleName" cssclass="divWISTitle">WIS MANAGER</s:div>
			<s:div name="divWISContainerGrayName" cssclass="divWISContainerGray">
				<s:div name="divWISDati" cssclass="divWISDati">
				
					<s:div name="divWISProvincia" cssclass="disWISRow">
						<s:dropdownlist name="ddlProvincia"
									disable="true" tabindex="1"
									label="Provincia: "
									cssclasslabel="labelWISTop"
									cssclass="tbWISBottom"
									cachedrowset="listprovince" usexml="true"
									onchange="setFiredButton('tx_button_provincia_changed');this.form.submit();"
									validator="required" showrequired="true"
									valueselected="${ddlProvincia}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption  value="{2}" text="{1}"/>
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_provincia_changed" onclick="" text="" validate="false"
									type="submit" cssclass="btnimgStyle" title="Aggiorna" />
						</noscript>
					</s:div>
					<s:div name="divWISComune" cssclass="disWISRow">
						<s:dropdownlist name="ddlComune" 
									disable="true" tabindex="2"
									label="Comune: "
									cssclasslabel="labelWISTop"
									cssclass="tbWISBottom"
									cachedrowset="listcomuni" usexml="true"
									validator="required" showrequired="true"
									valueselected="${ddlComune}">
									<s:ddloption text="Selezionare uno degli elementi" value="" />
									<s:ddloption value="{3}" text="{1} ({3})"/>
						</s:dropdownlist>
					</s:div>
					<s:div name="divWISAutorizzazione" cssclass="disWISRow">					
						<s:textbox name="tbNumeroAutorizzazione" bmodify="true" label="Numero Autorizzazione: " maxlenght="100" cssclasslabel="labelWISTop" 
									text="${tbNumeroAutorizzazione}" cssclass="tbWISBottom" showrequired="true"
									tabindex="3" validator="required;maxlength=100"/>
					</s:div>			
					
					<s:textbox name="fired_button_hidden" label="fired_button_hidden"
							bmodify="true" text="" cssclass="rend_display_none"
							cssclasslabel="rend_display_none" />
				</s:div>
				<s:div name="divWISLink" cssclass="divWISLink">
					<s:button id="btnWis" type="submit" validate="true" tabindex="4" text="VAI A WIS" onclick="" cssclass="btnVaiWIS"/>
				</s:div>
			</s:div>
		</s:div>		
	</s:form>
</s:div>





