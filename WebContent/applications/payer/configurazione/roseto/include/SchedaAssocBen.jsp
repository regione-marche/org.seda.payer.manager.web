<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="SchedaAssocBen" encodeAttributes="true" />

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

<script type="text/javascript">
	var annoDa = ${ddlDateAnnoDa};
	var annoA = ${ddlDateAnnoA};
	var today = new Date();
	
	$(function() {
		$.datepicker.setDefaults($.datepicker.regional["it"]);
		$("#tx_data_validita_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
		$("#tx_data_validita_hidden").datepicker( {
			minDate: new Date(annoDa, 0, 1),
			maxDate: new Date(annoA, 11, 31),
			yearRange: annoDa + ":" + annoA,
			showOn : "button",
			buttonImage : "../applications/templates/shared/img/calendar.gif",
			buttonImageOnly : true,
			onSelect : function(dateText, inst) {
				$("#tx_data_validita_day_id").val(dateText.substr(0, 2));
				$("#tx_data_validita_month_id").val(dateText.substr(3, 2));
				$("#tx_data_validita_year_id").val(dateText.substr(6, 4));
			},
			beforeShow: function(input, inst) {
				//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	            updateValoreDatePickerFromDdl("tx_data_validita_day_id",
				                            "tx_data_validita_month_id",
				                            "tx_data_validita_year_id",
				                            "tx_data_validita_hidden");
			}
		});
	});
</script>


<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_assocben_form" action="AssocBenActionInsUpd.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Riversamento Beneficiario Portali</s:div>
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">Riversamento Beneficiario Portali</s:div>
			</c:if>
									
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
							<s:dropdownlist name="codSocieta" disable="${societaDdlDisabled || codop == 'edit'}" 
											label="Societ&aacute;:" 
											validator="required" showrequired="true"
											cssclass="tbddl floatleft" 
											cssclasslabel="label85 bold floatleft textright"
											cachedrowset="listaSocieta" usexml="true" 
											onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
											valueselected="${codSocieta}">
								<s:ddloption text="" value=""/>
								<s:ddloption text="{2}" value="{1}"/>
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_societa_changed" 
									disable="${societaDdlDisabled || codop == 'edit'}" onclick="" text="" 
									type="submit" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
							</noscript>
						</s:div>
						
						<s:div name="tipServLabel_srch" cssclass="divRicMetadatiTopCenter">
							<s:dropdownlist name="codUtente" disable="${utenteDdlDisabled || codop == 'edit'}" 
													validator="required" showrequired="true"
													cssclass="tbddlMax floatleft" 
													label="Utente:" 
													cssclasslabel="label65 bold textright floatleft"
													cachedrowset="listaUtenti" usexml="true" 
													onchange="setFiredButton('tx_button_utente_changed');this.form.submit();"
													valueselected="${codUtente}">
								<s:ddloption text="" value=""/>
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
							<noscript>
								<s:button id="tx_button_provincia_changed" 
									disable="${utenteDdlDisabled || codop == 'edit'}" onclick="" text="" 
									type="submit" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
							</noscript>
						</s:div>

						<s:div name="divElement2" cssclass="divRicMetadatiTopRight">
							<s:dropdownlist name="codBeneficiario" disable="${requestScope.beneficiarioDdlDisabled || codop == 'edit'}" 
													validator="required" showrequired="true"
													cssclass="tbddl floatleft" 
													label="Benefic.:" 
													cssclasslabel="label65 bold floatleft textright"
													cachedrowset="listaUtentiEntiAll" usexml="true" 
													valueselected="${codBeneficiario}">
								<s:ddloption text="" value=""/>
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
						</s:div>
					<s:textbox name="rCodSocieta" label="codSocietaRic" bmodify="true" text="${rCodSocieta}" cssclass="display_none" cssclasslabel="display_none"  />
							
					</s:div>
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
				<c:if test="${codop == 'edit'}">

						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:div name="divDataValidita" cssclass="divDataDa">
									<s:date label="Data Validit&agrave;:" prefix="tx_data_validita" yearbegin="${ddlDateAnnoDa}"
										yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
										separator="/" calendar="${tx_data_validita}"
										cssclasslabel="label85 bold textright"
										cssclass="dateman"
										disabled="true" 
										validator="ignore" showrequired="true">
									</s:date>
									<input type="hidden" id="tx_data_validita_hidden" value="" />
								</s:div>
						</s:div>
					</c:if>
					<c:if test="${codop != 'edit'}">
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:div name="divDataValidita" cssclass="divDataDa">
									<s:date label="Data Validit&agrave;:" prefix="tx_data_validita" yearbegin="${ddlDateAnnoDa}"
										yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
										separator="/" calendar="${tx_data_validita}"
										cssclasslabel="label85 bold textright"
										cssclass="dateman"
										disabled="false" 
										validator="dateISO;required" showrequired="true"
										message="[dateISO=Data Validit&agrave;: ${msg_dataISO_valida}]">
									</s:date>
									<input type="hidden" id="tx_data_validita_hidden" value="" />
								</s:div>
						</s:div>
					</c:if>
					

						<s:div name="divElement12" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist  name="strumRiversamento" disable="false" label="Strum. Rivers.:" 
											 valueselected="${strumRiversamento}" 
											 cssclasslabel="label85 bold textright"
											 cssclass="tbddlMax floatleft">
								<s:ddloption value="B" text="Bonifico"/>
								<s:ddloption value="N" text="Altro"/>
							</s:dropdownlist>
						</s:div>
						
				</s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">

						<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
							<s:label name="label_data" cssclass="seda-ui-label label85 bold textright floatleft" text="Anno Rif.:" />
							<s:textbox name="annoRifDa" label="Da:" bmodify="true" text="${requestScope.annoRifDa}" 
									   cssclass="textboxman_small floatleft" cssclasslabel="bold textright floatleft"  
							           maxlenght="4" validator="required;digits;minlength=4;maxlength=4" showrequired="true"/>
				
							<s:textbox name="annoRifA" label="A:" bmodify="true" text="${requestScope.annoRifA}" 
									   cssclass="textboxman_small floatleft" cssclasslabel="bold textright floatleft"  
							           maxlenght="4" validator="required;digits;minlength=4;maxlength=4" showrequired="true"/>
						</s:div>

						<s:div name="divElement13" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist  name="metodoRend" disable="false" label="Metodo Rend.:" 
											 valueselected="${metodoRend}" 
											 cssclasslabel="label85 bold textright"
											 cssclass="tbddlMax floatleft">
								<s:ddloption value="Y" text="E-mail"/>
								<s:ddloption value="N" text="Altro"/>
							</s:dropdownlist>
						</s:div>
						
				</s:div>
				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">

						<s:div name="divElement11" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist  name="tipoRendicontazione" disable="false" label="Tipo Rend.:" 
											 valueselected="${tipoRendicontazione}" 
											 cssclasslabel="label85 bold textright"
											 cssclass="tbddlMax floatleft">
								<s:ddloption value="R" text="Riversamento"/>
								<s:ddloption value="C" text="Rendicontazione"/>
							</s:dropdownlist>
						</s:div>
				</s:div>

			</s:div>
			<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none" cssclasslabel="rend_display_none" />

				<s:button id="tx_button_indietro" onclick="" validate="false" text="Indietro" type="submit" cssclass="btnStyle" />

				<c:if test="${codop == 'add'}">
					<s:button id="tx_button_reset" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
					<s:button id="tx_button_aggiungi_end" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${codop == 'edit'}">
					<s:button id="tx_button_edit" onclick="" validate="false" text="Reset" type="submit" cssclass="btnStyle" />
					<s:button id="tx_button_edit_end" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
			</s:div>
 			
	</s:form>
	</s:div>
	
</s:div>
