<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="adminusers_search" encodeAttributes="true" />

<script type="text/javascript">
	function setFiredButton(buttonName) {
		var buttonFired = document.getElementById('fired_button_hidden');
		if (buttonFired != null)
			buttonFired.value = buttonName;
	}
</script>

<s:div name="div_selezione" cssclass="div_align_center divSelezione">
	<s:div name="divRicercaTopName" cssclass="divRicercaTop">
	<s:form name="search_form" action="usersSearch.do" method="post"
		hasbtn1="false" hasbtn2="false" hasbtn3="false" >
			<s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca Profili Amministratori
			</s:div>
			<s:div name="divRicMetadati" cssclass="divRicMetadati">
				<s:div name="divRicMetadatiTop" cssclass="divRicMetadatiTop">
					<s:div name="divElement1" cssclass="divRicMetadatiTopLeft">
						<s:dropdownlist name="tx_societa" disable="${ddlSocietaDisabled}"
							cssclass="tbddl floatleft" label="Societ&agrave;:"
							cssclasslabel="label85 bold floatleft textright"
							cachedrowset="listaSocieta" usexml="true"
							onchange="setFiredButton('tx_button_societa_changed');this.form.submit();"
							valueselected="${tx_societa}">
							<s:ddloption text="Tutte le Societ&agrave;" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_societa_changed"
								disable="${ddlSocietaDisabled}" onclick="" text=""
								type="submit" cssclass="btnimgStyle"  validate="false"/>
						</noscript>
					</s:div>
					
					<s:div name="divElement2" cssclass="divRicMetadatiCenter">
						<s:dropdownlist name="tx_provincia"
							disable="${ddlProvinciaDisabled}" cssclass="tbddl floatleft"
							label="Provincia:"
							cssclasslabel="label65 bold floatleft textright"
							cachedrowset="listaProvince" usexml="true"
							onchange="setFiredButton('tx_button_provincia_changed');this.form.submit();"
							valueselected="${tx_provincia}">
							<s:ddloption text="Tutte le Province" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
						<noscript>
							<s:button id="tx_button_provincia_changed"
								disable="${ddlProvinciaDisabled}" onclick="" text=""
								type="submit" cssclass="btnimgStyle" validate="false"
								 />
						</noscript>
					</s:div>
					
					<s:div name="divElement3" cssclass="divRicMetadatiRight">
						<s:dropdownlist name="tx_UtenteEnte"
							disable="${ddlUtenteEnteDisabled}" cssclass="tbddlMax floatleft"
							label="Ente:" cssclasslabel="label65 bold textright"
							cachedrowset="listaUtentiEnti" usexml="true"
							valueselected="${tx_UtenteEnte}">
							<s:ddloption text="Tutti gli Enti" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
				</s:div>

				<s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

					<s:div name="divElement4" cssclass="divRicMetadatiSingleRow">
						<s:textbox bmodify="true" name="tx_username"
							label="User Id.:" maxlenght="50" 
							cssclasslabel="label85 bold textright"
							cssclass="textareaman" 
							text="${tx_username}" />
					</s:div>


					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_utenzaattiva" disable="false"
							cssclass="tbddlMax floatleft" label="Profilo Attivo:"
							cssclasslabel="label85 bold textright"
							valueselected="${tx_utenzaattiva}">
							<s:ddloption text="Tutti i Profili" value="" />
							<s:ddloption text="Profili Attivi" value="Y" />
							<s:ddloption text="Profili non Attivi" value="N" />
						</s:dropdownlist>
					</s:div>

					
				</s:div>

				<s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter">

					<s:div name="divElement5" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_userprofile" disable="false"
							cssclass="tbddl"
							cssclasslabel="label85 bold textright"
							label="Tipologia User:" valueselected="${tx_userprofile}">
							<s:ddloption text="Tutti i Tipi" value="" />
							<s:ddloption text="AMMI - Amm.C.S.I." value="AMMI" />
							<s:ddloption text="AMSO - Amm.Societ&agrave;" value="AMSO" />
							<s:ddloption text="AMUT - Amm.Utente" value="AMUT" />
							<s:ddloption text="AMEN - Amm.Ente" value="AMEN" />
							<s:ddloption text="PYCO - Contribuente" value="PYCO" />
							<s:ddloption text="OPER - Operatore" value="OPER" />
						</s:dropdownlist>
					</s:div>
					
					<s:div name="divElement9" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_pertinenza" disable="false"
							cssclass="tbddl"
							cssclasslabel="label85 bold textright"
							label="Pertinenza:" valueselected="${tx_pertinenza}">
							<s:ddloption text="Tutti gli enti" value="" />
							<s:ddloption text="Comune di Torino" value="06954" />
							<s:ddloption text="Regione Piemonte" value="73348" />
							<!-- SVILUPPO_001_LP -->
							<s:ddloption text="Sanzioni C.D.S Ingiunzioni" value="81711" />
							<!-- FINE SVILUPPO_001_LP -->
						</s:dropdownlist>
					</s:div>
				</s:div>

				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement7" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_applicazione" disable="false"
							cssclass="tbddlMax floatleft" label="Servizio:"
							cssclasslabel="label85 bold textright"
							cachedrowset="listaApplicazioniPayer" usexml="true"
							valueselected="${tx_applicazione}">
							<s:ddloption text="Tutte le tipologie" value="" />
							<s:ddloption text="{1}" value="{2}" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				
				<s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
					<s:div name="divElement80" cssclass="divRicMetadatiSingleRow">
						<s:dropdownlist name="tx_gruppo_agenzia" disable="false"
							cssclass="tbddlMax floatleft" label="Gruppo Agenzia:"
							cssclasslabel="label85 bold textright"
							cachedrowset="listaGruppiAgenzia" usexml="true"
							valueselected="${tx_gruppo_agenzia}">
							<s:ddloption text="Tutti i Gruppi" value="" />
							<s:ddloption text="{2}" value="{1}" />
						</s:dropdownlist>
					</s:div>
				</s:div>
				
			</s:div>
			<s:div name="divRicercaBottoni" cssclass="divRicBottoni">
				<s:textbox name="fired_button_hidden" label="fired_button_hidden"
					bmodify="true" text="" cssclass="rend_display_none"
					cssclasslabel="rend_display_none" />
				<s:button  id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
				<s:button id="tx_button_nuovo" validate="false" onclick="" text="Nuovo" type="submit" cssclass="btnStyle" />
			</s:div>
	</s:form>

	</s:div>
	
</s:div>

<c:if test="${!empty listaUsers}">
	<s:div name="divRicercaFillName" cssclass="divRicercaFill">
			Elenco Profili Amministratori
	</s:div>
	<s:div name="div_datagrid">
		<s:datagrid viewstate="true" cachedrowset="listaUsers"  
			action="usersSearch.do?vista=adminusers_search" border="1" usexml="true" rowperpage="${applicationScope.rowsPerPage}">

		<!-- ENTE -->
			<s:dgcolumn label="Ente" index="5" asc="PY_ENTE_A" desc="PY_ENTE_D" />

		<!-- USERID -->
			<s:dgcolumn label="UserId" asc="PY_USERID_A" desc="PY_USERID_D">
				<s:hyperlink href="../adminusers/seUsersSearch.do?tx_username={2}&tx_button_cerca=cerca" 
					 cssclass="blacklink" text="{2}" alt="Vai all'utenza {2}" />
			</s:dgcolumn>
			
		<!-- TIPOLOGIA UTENTE -->
			<s:dgcolumn label="Tipologia<br/>User" index="3" asc="PY_PROF_A" desc="PY_PROF_D" />

		<!-- APPLICAZIONI ABILITATE -->
			<s:dgcolumn label="Servizi" index="4" asc="PY_APPL_A" desc="PY_APPL_D" />

		<!-- PROFILO ATTIVO/INATTIVO -->
			<s:dgcolumn label="Attivo" asc="PY_FATT_A" desc="PY_FATT_D" css="textcenter">
				<s:if right="{6}" control="eq" left="Y" >
					<s:then>
						<s:image height="16" width="16" alt="Profilo {3} attivo" 
							src="../applications/templates/adminusers/img/circle_green.png"/>
					</s:then>
					<s:else>
						<s:image height="16" width="16" alt="Profilo {3} non attivo" 
							src="../applications/templates/adminusers/img/circle_red.png"/>
					</s:else>
				</s:if>
			</s:dgcolumn>
			
		<!-- PROFILO MULTIUTENTE -->
			<s:dgcolumn label="Ente Pertinenza" asc="PY_PERTI_A" desc="PY_PERTI_D" css="textcenter">
				<s:if right="{8}" control="eq" left="06954" >
					<s:then>Comune di Torino</s:then>
					<s:else></s:else>
				</s:if>
				
				<s:if right="{8}" control="eq" left="73348" >
					<s:then>Regione Piemonte</s:then>
					<s:else></s:else>
				</s:if>
				
				<s:if right="{8}" control="eq" left="     " >
					<s:then>Tutti&nbsp;gli&nbsp;enti</s:then>
					<s:else></s:else>
				</s:if>
				
			</s:dgcolumn>
			
			<!-- GRUPPO AGENZIA -->
			<s:dgcolumn label="Gruppo Agenzia" index="9" asc="PY_GRAG_A" desc="PY_GRAG_D" />

			<s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;">
			<!-- MODIFICA PROFILO -->
					
				<s:hyperlink
					cssclass="hlStyle" 
					href="userEdit.do?tx_chiaveutente={1}"
					imagesrc="../applications/templates/shared/img/edit.png"
					alt="Modifica Profilo" text="" />
					
			<!-- CANCELLAZIONE PROFILO -->
				<s:hyperlink
					cssclass="hlStyle" 
					href="userDelete.do?tx_chiaveutente={1}"
					imagesrc="../applications/templates/shared/img/cancel.png"
					alt="Elimina Profilo" text="" />

			</s:dgcolumn>
			
		</s:datagrid>
	</s:div>

</c:if>