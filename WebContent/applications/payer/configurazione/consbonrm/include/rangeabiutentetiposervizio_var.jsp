<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="Rangeabiutentetiposervizio_var" encodeAttributes="true" />

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="var_form" action="${do_command_name}" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<c:if test="${codop == 'add'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">RANGE UTENTE - TIPOLOGIA SERVIZIO</s:div>
				<input type="hidden" name="prova" value=0 />
			</c:if>
			<c:if test="${codop == 'edit'}">
				<s:div name="divRicercaTitleName" cssclass="divRicTitle">RANGE UTENTE - TIPOLOGIA SERVIZIO</s:div>
				<input type="hidden" name="prova" value=1 />
			</c:if>
						
			<s:div name="divRicMetadati" cssclass="divRicMetadati">

				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="top" >
					<c:choose>
						<c:when test="${codop == 'add'}">

						 		<s:dropdownlist name="rangeabiutentetiposervizio_strConfigutentetiposervizios" 
						 				disable="false" 
										label="Societ&agrave;/Utente/Tipol. Serv.:" 
										cssclasslabel="label180 bold textright floatleft" cssclass="seda-ui-ddl tbddlMax780 floatleft"
										cachedrowset="configutentetiposervizios" usexml="true"
										validator="required" showrequired="true"
										valueselected="">
										<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
										<s:ddloption value="{1}|{2}|{3}" text="{14} / {15} / {16}"/>
								</s:dropdownlist>
								
						</c:when>	
						<c:when test="${codop == 'edit'}">
							
					 		<s:dropdownlist name="ddlSocUteTpServ" 
					 				disable="true" 
									label="Societ&agrave;/Utente/Tipol. Serv.:" 
									cssclasslabel="label180 bold textright floatleft" cssclass="seda-ui-ddl tbddlMax780 floatleft"
									cachedrowset="configutentetiposervizios" usexml="true" 
									valueselected="${tx_societa}|${tx_utente}|${tx_codiceTipologiaServizio}">
									<s:ddloption text="Selezionare uno degli elementi della lista" value="" />
									<s:ddloption value="{1}|{2}|{3}" text="{14} / {15} / {16}"/>
							</s:dropdownlist>
							
						</c:when>
					</c:choose>
					</s:div>                    	
				</s:div>

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true"
							label="Iniz. Rg Da:"
							validator="maxlength=20"
							name="rangeabiutentetiposervizio_inizioRangeDa"
							text="${requestScope.rangeabiutentetiposervizio_inizioRangeDa}"
							cssclasslabel="label85 bold floatleft textright"
							cssclass="textareaman" maxlenght="20"/>
					</s:div>
					
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true"
							validator="maxlength=20"
							label="Iniz. Rg Per:" name="rangeabiutentetiposervizio_inizioRangePer"
							text="${requestScope.rangeabiutentetiposervizio_inizioRangePer}"
							cssclasslabel="label85 bold textright" cssclass="textareaman" maxlenght="20"/>
					</s:div>
				</s:div>
				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">
					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true"
							validator="maxlength=20"
							label="Fine Range:" name="rangeabiutentetiposervizio_fineRangeA"
							text="${requestScope.rangeabiutentetiposervizio_fineRangeA}"
							cssclasslabel="label85 bold textright" cssclass="textareaman" maxlenght="20"/>
					</s:div>
					
					<s:div name="divElement6" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist label="Tipo Range:" name="rangeabiutentetiposervizio_tipoRange" disable="false"
									cssclasslabel="label85 bold textright"
									cssclass="textareaman" valueselected="${rangeabiutentetiposervizio_tipoRange}">
									<s:ddloption value="B" text="Bollettino" />
									<s:ddloption value="D" text="Documento" />
									<s:ddloption value="V" text="Verbale" />
						</s:dropdownlist>
					</s:div>
					</s:div>
					<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
						<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
							<s:dropdownlist label="Flag Cin:" 
									cssclasslabel="label85 bold textright" cssclass="textareaman" 
									name="rangeabiutentetiposervizio_flagCin" disable="false"  
							    	valueselected="${requestScope.rangeabiutentetiposervizio_flagCin}">
									<s:ddloption value="Y" text="Si"/>
									<s:ddloption value="N" text="No"/>
							</s:dropdownlist>
						</s:div>
					</s:div>
				</s:div>
			<br/>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:button id="tx_button_indietro" type="submit" text="Indietro"	onclick="" cssclass="btnStyle" validate="false" />
				<%-- inizio LP PG21Xx04 Bug la jsp non era visualizzata --%>
				<%--  s:button id="tx_button_reset" type="submit" text="Reset"onclick="" cssclass="btnStyle" validate="false" / --%>
				<s:button id="tx_button_reset" type="submit" text="Reset" onclick="" cssclass="btnStyle" validate="false" />
				<%-- fine LP PG21Xx04 Bug la jsp non era visualizzata --%>
				<c:if test="${codop == 'add'}">
					<s:button id="tx_button_aggiungi" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
				<c:if test="${codop == 'edit'}">
					<s:button id="tx_button_edit" onclick="" text="Salva" type="submit" cssclass="btnStyle" />
				</c:if>
			</s:div>
			<input type="hidden" name="codop" value="${codop}" />
			<input type="hidden" name="tx_societa" value="${tx_societa}" />
			<input type="hidden" name="tx_utente" value="${tx_utente}" />
			<input type="hidden" name="tx_codiceTipologiaServizio" value="${tx_codiceTipologiaServizio}" />
			<input type="hidden" name="rangeabiutentetiposervizio_chiaveRangeTipoServizio" value="<c:out value="${rangeabiutentetiposervizio_chiaveRangeTipoServizio}"/>"/>	
	</s:form>
	</s:div>
	
</s:div>
