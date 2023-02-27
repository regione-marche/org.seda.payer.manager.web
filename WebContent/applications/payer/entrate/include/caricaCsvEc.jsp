<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<m:setBundle name="org.seda.payer.i18n.resources.TemplateStrings" />
<m:view_state id="viewstate" encodeAttributes="true"  encodeParameters="true"  />
<script type="text/javascript" >
$(document).ready( function() {
	document.getElementById("tx_button_upload").disabled = true;     
});

function enableBtn(){
   if(document.getElementById("uploadCSV").files.length == 0) {
       document.getElementById("tx_button_upload").disabled = true;
   }else {
  		document.getElementById("tx_button_upload").disabled = false;
   }
}

function setFiredButton(buttonName) {
	var buttonFired = document.getElementById('fired_button_hidden');
	if (buttonFired != null) {
		buttonFired.value = buttonName;
	}
}

function submitForm(){}
	
</script>

<c:url value="" var="formParameters">
	<c:if test="${!empty param.pageNumber}">
		<c:param name="pageNumber_hidden">${param.pageNumber}</c:param>
	</c:if>
	<c:if test="${!empty rowsPerPage}">
		<c:param name="rowsPerPage_hidden">${param.rowsPerPage}</c:param>
	</c:if>
	<c:if test="${!empty orderBy}">
		<c:param name="orderBy_hidden">${param.orderBy}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_societa}">
		<c:param name="tx_societa">${param.tx_societa}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_provincia}">
		<c:param name="tx_provincia">${param.tx_provincia}</c:param>
	</c:if>
	<c:if test="${!empty param.tx_UtenteEnte}">
		<c:param name="tx_UtenteEnte">${param.tx_UtenteEnte}</c:param>
	</c:if>
	<c:if test="${!empty requestScope.tx_UtenteEnte}">
		<c:param name="tx_UtenteEnte">${requestScope.tx_UtenteEnte}</c:param>
	</c:if>
	
	<c:if test="${!empty param.uploadCSV}">
		<c:param name="uploadCSV">${param.uploadCSV}</c:param>
	</c:if>
</c:url>

<s:div name="divSelezione1" cssclass="divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
		<c:if test="${requestScope.upload != null}">
			<div class="seda-ui-divvalidator"><span class="seda-ui-spanvalidator">${sessionScope.message}<br></span></div>
		</c:if>
		<s:form name="caricaFileCsvForm"
			action="uploadDocumento.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:textbox name="DDLChanged" label="DDLChanged" bmodify="true"
				text="" cssclass="display_none" cssclasslabel="display_none" />
			<s:textbox name="ext" label="ext" bmodify="true" text="${ext}"
				cssclass="display_none" cssclasslabel="display_none" />

			<s:textbox name="hRowsPerPage" label="rowsPerPageRic" bmodify="true"
				text="${rowsPerPage}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="hPageNumber" label="pageNumberRic" bmodify="true"
				text="${pageNumber}" cssclass="display_none"
				cssclasslabel="display_none" />
			<s:textbox name="hOrder" label="orderRic" bmodify="true"
				text="${order}" cssclass="display_none" cssclasslabel="display_none" />

			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">
				Carica CSV Estratto Conto
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
				
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:dropdownlist name="tx_societa" disable="${societaDdlDisabled}"
							cssclass="tbddl floatleft" label="Societ&agrave;:"
							cssclasslabel="label85 bold floatleft textright"
							cachedrowset="listaSocieta" usexml="true"
							onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
							valueselected="${tx_societa}">
							<s:ddloption text="Tutte le Societ&agrave;" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<!-- onchange="setDdl('societa'); callSubmit(this.form);" -->
						<noscript><s:button id="tx_button_societa_changed"
							type="submit" disable="${societaDdlDisabled}" text="" onclick=""
							cssclass="btnimgStyle" title="Aggiorna" validate="false" /></noscript>
					</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiTopCenter">
						<s:dropdownlist name="tx_provincia"
							disable="${provinciaDdlDisabled}" cssclass="tbddl floatleft"
							label="Provincia:"
							cssclasslabel="label65 bold floatleft textright"
							cachedrowset="listaProvince" usexml="true"
							onchange="setFiredButton('tx_button_provincia_changed');this.form.submit();"
							valueselected="${tx_provincia}">
							<s:ddloption text="Tutte le Province" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<!-- onchange="setDdl('provincia'); callSubmit(this.form);" -->
						<noscript>
							<s:button id="tx_button_provincia_changed"
							type="submit" text="" onclick="" cssclass="btnimgStyle"
							title="Aggiorna" validate="false" />
						</noscript>
					</s:div>

					 <s:div name="divElement3" cssclass="divRicMetadatiTopRight">
						<s:dropdownlist name="tx_UtenteEnte"
							disable="${ddlUtenteEnteDisabled}" cssclass="tbddlMax floatleft"
							label="Ente:" cssclasslabel="label65 bold textright"
							cachedrowset="listaUtentiEnti" usexml="true"
							onchange="setFiredButton('tx_button_utente_changed');this.form.submit();"
							valueselected="${tx_UtenteEnte}"
							validator="required">
							<s:ddloption text="Tutti gli Enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_utente_changed"  type="submit" text="" onclick="" cssclass="btnimgStyle" title="Aggiorna" validate="false" />
						</noscript>
					</s:div>
				</s:div>
				
			</s:div>
		</s:form>
		<s:form name="uploadFileCsvForm"
			action="upload.do" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false" enctype="multipart/form-data">
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
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
			</s:div>

			<s:div name="divCentered0" cssclass="divRicBottoni">
				<!-- <s:hyperlink name="hpAnnulla" cssclass="hlToBtnStyle" href="../default/default.do${formParameters}" text="Indietro" /> -->
				<s:hyperlink name="hpAnnulla" cssclass="hlToBtnStyle" href="../default/default.do?mnId=3&mnLivello=1" text="Indietro" />
				<s:button id="tx_button_upload" type="submit" text="Carica" cssclass="btnStyle" onclick=""/>			
			</s:div>
		</s:form>
		
	</s:div>

</s:div>