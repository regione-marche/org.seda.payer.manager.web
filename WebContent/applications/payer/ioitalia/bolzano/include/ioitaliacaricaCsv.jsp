<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="ioitalia" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
	type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
	type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
	type="text/javascript"></script>

<script type="text/javascript">

$(document).ready( function() {
	document.getElementById("tx_button_add").disabled = true;     
});

	function enableBtn(){
	   if(document.getElementById("uploadCSV").files.length == 0) {
	       document.getElementById("tx_button_add").disabled = true;
	   }else {
	  		document.getElementById("tx_button_add").disabled = false;
	   }
	}

	function setFiredButtonSelect(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden_select');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
	
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">UPLOAD CSV 
		</s:div>
		<s:form method="post" name="form_select" action="ioitaliaupload.do?vista=ioitalia" hasbtn3="false" hasbtn2="false" hasbtn1="false">
			<s:div name="divRicercaTop" cssclass="divRicercaMetadatiTop">
				<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
					<s:dropdownlist name="tx_societa" disable="${ddlSocietaDisabled}"
						cssclass="tbddl floatleft" label="Societ&agrave;:"
						cssclasslabel="label85 bold floatleft textright"
						cachedrowset="listaSocieta" usexml="true"
						onchange="setFiredButtonSelect('tx_button_societa_changed');this.form.submit();"
						valueselected="${tx_societa}">
						<s:ddloption text="Tutte le Societ&agrave;" value="" />
						<s:ddloption text="{2}" value="{1}" />
					</s:dropdownlist>
				</s:div>
	
				<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
					<s:dropdownlist name="tx_provincia"
						disable="${ddlProvinciaDisabled}" cssclass="tbddl floatleft"
						label="Provincia:"
						cssclasslabel="label65 bold floatleft textright"
						cachedrowset="listaProvince" usexml="true"
						onchange="setFiredButtonSelect('tx_button_provincia_changed');this.form.submit();"
						valueselected="${tx_provincia}">
						<s:ddloption text="Tutte le Province" value="" />
						<s:ddloption text="{2}" value="{1}" />
					</s:dropdownlist>
				</s:div>
	
				<s:div name="divElement3" cssclass="divRicMetadatiTopRight">
					<s:dropdownlist name="tx_UtenteEnte"
						disable="${ddlUtenteEnteDisabled}" cssclass="tbddlMax floatleft"
						label="Ente:" cssclasslabel="label65 bold textright"
						cachedrowset="listaUtentiEnti" usexml="true"
						onchange="setFiredButtonSelect('tx_button_ente_changed');this.form.submit();"
						valueselected="${tx_UtenteEnte}">
						<s:ddloption text="Tutti gli Enti" value="" />
						<s:ddloption text="{2}" value="{1}" />
					</s:dropdownlist>
				</s:div>
			</s:div>
			<s:textbox name="fired_button_hidden_select" label="fired_button_hidden_select"
						bmodify="true" text="" cssclass="rend_display_none"
						cssclasslabel="rend_display_none" />
		</s:form>
		<s:form name="form_selezione" action="ioitaliauploadcsv.do"
			method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false" enctype="multipart/form-data" >
				<s:div name="divRicMetadati" cssclass="divRicMetadati">
					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
																
					</s:div>
					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
						<s:div name="divElement14" cssclass="divRicMetadatiDoubleRow">
							<s:table border="">
								<s:thead>
									<s:tr>
										<s:th cssclass="headercellTransparent"></s:th>
										<s:th cssclass="headercellTransparent"></s:th>
									</s:tr>
								</s:thead>
								<s:tbody>
									<s:tr>
										<s:td><s:label name="" text="File CSV:" cssclass="label65 bold textright"/></s:td>
										<s:td><input class="file-input" type="file" name="uploadCSV" id="uploadCSV" accept=".csv" onchange="enableBtn()"/></s:td>
									</s:tr>														
								</s:tbody>
							</s:table>	
						</s:div>
					</s:div>
					<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
					</s:div>
				</s:div>
				
				<br />
				<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
					<s:textbox name="fired_button_hidden" label="fired_button_hidden"
						bmodify="true" text="" cssclass="rend_display_none"
						cssclasslabel="rend_display_none" />
					<s:button id="tx_button_add" validate="false" onclick=""
						text="Carica" type="submit" cssclass="btnStyle" />
				</s:div>
		</s:form>
	</s:div>
</s:div>


<s:div name="div_messaggi" cssclass="div_align_center">
	<c:if test="${!empty tx_message}">
		<s:div name="div_messaggio">
			<hr />
			<s:label name="tx_message" text="${tx_message}" />
			<hr />
		</s:div>
	</c:if>
</s:div>
