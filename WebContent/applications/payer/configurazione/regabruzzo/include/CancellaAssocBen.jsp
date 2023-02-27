<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="CancellaAssocBen" encodeAttributes="true" />


<s:div name="div_delete" cssclass="div_align_center divSelezione">
	<s:div name="divDeleteTopName" cssclass="divRicercaTop">
	<s:form name="delete_form" action="AssocBenActionCancel.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
		<s:div name="divRicercaTitleName" cssclass="divRicTitle">
		Riversamento Beneficiario Portali
		</s:div>
		<c:if test="${!confermaDisabilitata}">
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
					<s:div name="divRicercaMetadatiTop" cssclass="divRicMetadatiTop">
						<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
							<s:dropdownlist name="codSocieta" disable="true" 
											label="Societ&aacute;:" 
											validator="ignore"
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
									disable="true" onclick="" text="" 
									type="submit" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
							</noscript>
						</s:div>
						
						<s:div name="tipServLabel_srch" cssclass="divRicMetadatiTopCenter">
							<s:dropdownlist name="codUtente" disable="true" 
													validator="ignore"
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
									disable="true" onclick="" text="" 
									type="submit" cssclass="btnimgStyle"  title="Aggiorna" validate="false" />
							</noscript>
						</s:div>

						<s:div name="divElement2" cssclass="divRicMetadatiTopRight">
							<s:dropdownlist name="codBeneficiario" disable="true" 
													validator="ignore"
													cssclass="tbddl floatleft" 
													label="Benefic.:" 
													cssclasslabel="label65 bold floatleft textright"
													cachedrowset="listaUtentiEntiAll" usexml="true" 
													valueselected="${codBeneficiario}">
								<s:ddloption text="" value=""/>
								<s:ddloption text="{2}" value="{1}" />
							</s:dropdownlist>
						</s:div>
					
							
					</s:div>
				
				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
								<s:div name="divDataValidita" cssclass="divDataDa">
									<s:date label="Data Validit&agrave;:" prefix="tx_data_validita" yearbegin="${ddlDateAnnoDa}"
										yearend="${ddlDateAnnoA}" locale="IT-it" descriptivemonth="false"
										separator="/" calendar="${tx_data_validita}"
										cssclasslabel="label85 bold textright"
										cssclass="dateman"
										disabled="true" 
										validator="ignore">
									</s:date>
									<input type="hidden" id="tx_data_validita_hidden" value="" />
								</s:div>
						</s:div>

						<s:div name="divElement12" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist  name="strumRiversamento" disable="true" label="Strum. Rivers.:" 
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
							<s:textbox name="annoRifDa" label="Da:" bmodify="false" text="${requestScope.annoRifDa}" 
									   cssclasslabel="bold textright floatleft" 
									   cssclass="textboxman_small floatleft colordisabled" validator="ignore"
							           maxlenght="4"/>
				
							<s:textbox name="annoRifA" label="A:" bmodify="false" text="${requestScope.annoRifA}" 
									   cssclass="textboxman_small floatleft colordisabled" cssclasslabel="bold textright floatleft"  
							           maxlenght="4" validator="ignore"/>
						</s:div>

						<s:div name="divElement13" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist  name="metodoRend" disable="true" label="Metodo Rend.:" 
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
							<s:dropdownlist  name="tipoRendicontazione" disable="true" label="Tipo Rend.:" 
											 valueselected="${tipoRendicontazione}" 
											 cssclasslabel="label85 bold textright"
											 cssclass="tbddlMax floatleft">
								<s:ddloption value="R" text="Riversamento"/>
								<s:ddloption value="C" text="Rendicontazione"/>
							</s:dropdownlist>
						</s:div>
				</s:div>

			</s:div>			
		</c:if>	
		<br/>
		<br/>
		<s:div name="divRicMetadati" cssclass="divRicMetadati">
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_indietro" onclick="" text="Indietro" cssclass="btnStyle" type="submit" />
				<s:button id="tx_button_delete_end" disable="${confermaDisabilitata}" onclick="" text="Cancella" type="submit" cssclass="btnStyle" />
			</s:div>
		</s:div>
	</s:form>

</s:div>
	
</s:div>
