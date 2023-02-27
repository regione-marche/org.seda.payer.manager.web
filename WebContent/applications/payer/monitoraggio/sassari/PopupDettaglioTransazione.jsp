<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld"  prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>



<!-- POPUP -->
<s:div cssclass="seda-ui-div" name="popupDettaglio">
	<s:div name="div_table_trans">
		<s:table border="1" cellspacing="0"
			cssclass="seda-ui-datagrid tableDettaglio1">
			<s:thead>
				<s:tr>
					<s:th cssclass="seda-ui-datagrid thDettaglioTitle1" icol="4">Dettaglio Transazione</s:th>
				</s:tr>
			</s:thead>
			<s:tbody>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Id:</s:td>
					<s:td id="dt_chiave_transazione" icol="3"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Importo totale:</s:td>
					<s:td cssclass="text_align_right"
						id="dt_importo_totale_transazione"></s:td>
					<s:td cssclass="seda-ui-cellheader">Costi del servizio:</s:td>
					<s:td cssclass="text_align_right"
						id="dt_importo_costo_transazione"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Codice Autorizzazione Banca:</s:td>
					<s:td id="dt_codice_autorizzazione_banca"></s:td>
					<s:td cssclass="seda-ui-cellheader">Data Eff.Pag.:</s:td>
					<s:td id="dt_data_effettivo_pagamento_su_gateway"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Numero MAV:</s:td>
					<s:td id="dt_numeromav_transazione"></s:td>
					<s:td cssclass="seda-ui-cellheader">Riversamento Automatico:</s:td>
					<s:td id="dt_flag_riversamento_automatico"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Id Terminale POS fisico:</s:td>
					<s:td id="dt_id_terminale_pos"></s:td>
					<s:td cssclass="seda-ui-cellheader" id="dt_operatore_intestazione"></s:td>
					<s:td id="dt_operatore"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Codice IUV:</s:td>
					<s:td id="dt_id_iuv"></s:td>
					<s:td cssclass="seda-ui-cellheader" ></s:td>
					<s:td></s:td>
				</s:tr>
				<!--  inizio LP PG1902020 -->
				
				<s:tr>
					<s:td cssclass="seda-ui-cellheader" id="dt_stato_annulloL">Annullo:</s:td>
					<s:td icol="3" id="dt_stato_annullo"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader" id="dt_email_annulloL">Email Annullo:</s:td>
					<s:td id="dt_email_annullo"></s:td>
					<s:td cssclass="seda-ui-cellheader" id="dt_data_email_annulloL">Data Invio:</s:td>
					<s:td id="dt_data_email_annullo"></s:td>
				</s:tr>
				<!--  fine LP PG1902020 -->
				
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Note:</s:td>
					<s:td icol="3" id="dt_note_transazione"></s:td>
				</s:tr>
			</s:tbody>
		</s:table>
	</s:div>
	<!-- table IV -->
	<s:div name="div_table_iv">
		<s:table border="1" cellspacing="0"
			cssclass="seda-ui-datagrid tableDettaglio2" id="dt_table_iv">
			<s:thead>
				<s:tr>
					<s:th cssclass="seda-ui-datagrid thDettaglioTitle2" icol="4"
						id="dt_thead_iv"></s:th>
				</s:tr>
			</s:thead>
			<s:tbody id="dt_tbody_iv">
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Ente Impositore:</s:td>
					<s:td icol="3" id="iv_ente"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Tipologia Servizio:</s:td>
					<s:td icol="3" id="iv_tipologia_servizio"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_PRE">
					<s:td cssclass="seda-ui-cellheader">Id. Bollettino:</s:td>
					<s:td id="pre_codice_bollettino_premarcato_mav"></s:td>
					<s:td cssclass="seda-ui-cellheader">TD:</s:td>
					<s:td id="pre_tipo_documento_host"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Importo bollettino:</s:td>
					<s:td cssclass="text_align_right"
						id="iv_importo_totale_bollettino"></s:td>
					<s:td cssclass="seda-ui-cellheader">Rendicontato:</s:td>
					<s:td id="iv_rendicontato"></s:td>
					<%--<s:td id="iv_chiave_spedizione"></s:td>  --%>
				</s:tr>
				<%-- 				<s:tr cssclass="tipo_bollettino_PRE">
					<s:td cssclass="seda-ui-cellheader">Id. Bollettino:</s:td>
					<s:td id="pre_codice_bollettino_premarcato_mav"></s:td>
					<s:td cssclass="seda-ui-cellheader">TD:</s:td>
					<s:td id="pre_tipo_documento_host"></s:td>
				</s:tr>	--%>
				<s:tr cssclass="tipo_bollettino_CDS">
					<s:td cssclass="seda-ui-cellheader">Id. Bollettino:</s:td>
					<s:td id="cds_codice_bollettino_premarcato_mav"></s:td>
					<s:td cssclass="seda-ui-cellheader">TD:</s:td>
					<s:td id="cds_tipo_documento_host"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_MAV">
					<s:td cssclass="seda-ui-cellheader">Cod. MAV:</s:td>
					<s:td id="mav_codice_bollettino_premarcato_mav"></s:td>
					<s:td cssclass="seda-ui-cellheader">TD:</s:td>
					<s:td id="mav_tipo_documento_host"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">C/C n.:</s:td>
					<s:td id="iv_numero_conto_corrente"></s:td>
					<s:td cssclass="seda-ui-cellheader">Intestato a:</s:td>
					<s:td id="iv_descrizione_intestatario_conto_corrente"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_BOL">
					<s:td cssclass="seda-ui-cellheader">Importo tassa:</s:td>
					<s:td cssclass="text_align_right" id="bol_importo_oneri"></s:td>
					<s:td cssclass="seda-ui-cellheader">Targa:</s:td>
					<s:td id="bol_targa"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_BOL">
					<s:td cssclass="seda-ui-cellheader">Sanzioni:</s:td>
					<s:td cssclass="text_align_right" id="bol_importo_residuo_totale"></s:td>
					<s:td cssclass="seda-ui-cellheader">Categoria:</s:td>
					<s:td id="bol_categoria_bollo_auto"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_BOL">
					<s:td cssclass="seda-ui-cellheader">Interessi:</s:td>
					<s:td cssclass="text_align_right"
						id="bol_importo_residuo_compenso"></s:td>
					<s:td cssclass="seda-ui-cellheader">Riduzione:</s:td>
					<s:td cssclass="text_align_right"
						id="bol_mesi_riduzione_bollo_auto"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_BOL">
					<s:td cssclass="seda-ui-cellheader">Scadenza:</s:td>
					<s:td id="bol_dettaglio_scadenza_bollo_auto"></s:td>
					<s:td cssclass="seda-ui-cellheader">Mesi di validit&agrave;:</s:td>
					<s:td cssclass="text_align_right"
						id="bol_mesi_validita_bollo_auto"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_BOL">
					<s:td cssclass="seda-ui-cellheader">Codice fiscale/P.IVA:</s:td>
					<s:td id="bol_codice_fiscale"></s:td>
					<s:td cssclass="seda-ui-cellheader">Strumento:</s:td>
					<s:td id="bol_codice_servizio"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_CDS">
					<s:td cssclass="seda-ui-cellheader">Numero verbale:</s:td>
					<s:td id="cds_numero_documento"></s:td>
					<s:td cssclass="seda-ui-cellheader">Data verbale:</s:td>
					<s:td id="cds_data_sanzione"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_CDS">
					<s:td cssclass="seda-ui-cellheader">Codice fiscale/P.IVA:</s:td>
					<s:td id="cds_codice_fiscale"></s:td>
					<s:td cssclass="seda-ui-cellheader">Targa:</s:td>
					<s:td id="cds_targa"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_CDS">
					<s:td cssclass="seda-ui-cellheader">Strumento:</s:td>
					<s:td icol="3" id="cds_codice_servizio"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_MAV">
					<s:td cssclass="seda-ui-cellheader">Riferimento:</s:td>
					<s:td id="mav_numero_documento"></s:td>
					<s:td cssclass="seda-ui-cellheader">Strumento:</s:td>
					<s:td id="mav_codice_servizio"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_PRE">
					<s:td cssclass="seda-ui-cellheader">Numero Documento:</s:td>
					<s:td id="pre_numero_documento"></s:td>
					<s:td cssclass="seda-ui-cellheader">Scadenza:</s:td>
					<s:td id="pre_data_scadenza_rata"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_PRE">
					<s:td cssclass="seda-ui-cellheader">Codice fiscale/P.IVA:</s:td>
					<s:td id="pre_codice_fiscale"></s:td>
					<s:td cssclass="seda-ui-cellheader">Numero Rata:</s:td>
					<s:td id="pre_progressivo_rata"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_PRE">
					<s:td cssclass="seda-ui-cellheader">Strumento:</s:td>
					<s:td icol="3" id="pre_codice_servizio"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_SPO">
					<s:td cssclass="seda-ui-cellheader">Strumento:</s:td>
					<s:td icol="3" id="spo_codice_servizio"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_SPO">
					<s:td cssclass="seda-ui-cellheader">Causale:</s:td>
					<s:td icol="3" id="spo_note_premarcato"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Eseguito da:</s:td>
					<s:td icol="3" id="iv_denominazione"></s:td>
				</s:tr>
			</s:tbody>
		</s:table>
	</s:div>

	<!-- BOLLETTINO ICI -->
	<s:div name="div_table_ici">
		<s:table border="1" cellspacing="0"
			cssclass="seda-ui-datagrid tableDettaglio2" id="dt_table_ici">
			<s:thead>
				<s:tr>
					<s:th cssclass="seda-ui-datagrid thDettaglioTitle2" icol="4"
						id="dt_thead_ici"></s:th>
				</s:tr>
			</s:thead>
			<s:tbody id="dt_tbody_ici">
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Ente Impositore:</s:td>
					<s:td icol="3" id="ici_ente"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Tipologia Servizio:</s:td>
					<s:td icol="3" id="ici_tipologia_servizio"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Importo bollettino:</s:td>
					<s:td cssclass="text_align_right" id="ici_importo_movimento"></s:td>
					<s:td cssclass="seda-ui-cellheader">Rendicontato:</s:td>
					<s:td id="ici_chiave_spedizione">
                          No/Si
                       </s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">C/C n.:</s:td>
					<s:td id="ici_numero_conto_corrente"></s:td>
					<s:td cssclass="seda-ui-cellheader">Intestato a:</s:td>
					<s:td id="ici_descrizione_intestatario_conto_corrente"></s:td>
				</s:tr>

				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Comune Ubicazione:</s:td>
					<s:td id="ici_comune_di_ubicazione_immobile"></s:td>
					<s:td cssclass="seda-ui-cellheader">CAP Ubicazione:</s:td>
					<s:td id="ici_cap_comune_ubicazione_immobile"></s:td>
				</s:tr>


				<!-- sezione ICI -->
				<s:tr cssclass="tipo_bollettino_ICIM">
					<s:td cssclass="seda-ui-cellheader">Terreni Agricoli:</s:td>
					<s:td cssclass="text_align_right"
						id="ici_importo_terreni_agricoli"></s:td>
					<s:td cssclass="seda-ui-cellheader">Numero Fabbricati:</s:td>
					<s:td cssclass="text_align_right" id="ici_numero_fabbricati"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_ICIM">
					<s:td cssclass="seda-ui-cellheader">Aree Fabbricabili:</s:td>
					<s:td cssclass="text_align_right"
						id="ici_importo_aree_fabbricabili"></s:td>
					<s:td cssclass="seda-ui-cellheader">Anno d'Imposta:</s:td>
					<s:td cssclass="text_align_right" id="ici_anno_imposta"></s:td>
				</s:tr>

				<s:tr cssclass="tipo_bollettino_ICIM">
					<s:td cssclass="seda-ui-cellheader">Abitaz. Principale:</s:td>
					<s:td cssclass="text_align_right"
						id="ici_importo_abitazione_principale"></s:td>
					<s:td cssclass="seda-ui-cellheader">Versamento Acconto:</s:td>
					<s:td id="ici_versamento_acconto">
                           Si(0/3)/NO
                       </s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_ICIM">
					<s:td cssclass="seda-ui-cellheader">Altri Fabbricati:</s:td>
					<s:td cssclass="text_align_right"
						id="ici_importo_altri_fabbricati"></s:td>
					<s:td cssclass="seda-ui-cellheader">Versamento Saldo:</s:td>
					<s:td id="ici_versamento_saldo">
                           Si(1/3)/NO
                       </s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_ICIM">
					<s:td cssclass="seda-ui-cellheader">Ravvedimento:</s:td>
					<s:td id="ici_flag_ravvedimento">
                           No/Si
                       </s:td>
					<s:td cssclass="seda-ui-cellheader">Detr.COM. Ab.Princ.:</s:td>
					<s:td cssclass="text_align_right"
						id="ici_importo_detrazione_comunale"></s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_ICIM">
					<s:td cssclass="seda-ui-cellheader">Codice fiscale/P.IVA:</s:td>
					<s:td id="ici_codice_fiscale"></s:td>
					<s:td cssclass="seda-ui-cellheader">Detr.STAT. Ab.Princ.:</s:td>
					<s:td cssclass="text_align_right"
						id="ici_importo_detrazione_statale"></s:td>
				</s:tr>




				<!-- sezione ISCOP -->
				<s:tr cssclass="tipo_bollettino_ISCM">
					<s:td cssclass="seda-ui-cellheader">Imponibile ICI:</s:td>
					<s:td cssclass="text_align_right"
						id="ici_imponibile_ici_per_iscop"></s:td>
					<s:td cssclass="seda-ui-cellheader">Anno d'Imposta:</s:td>
					<s:td cssclass="text_align_right" id="iscop_anno_imposta"></s:td>
				</s:tr>

				<s:tr cssclass="tipo_bollettino_ISCM">
					<s:td cssclass="seda-ui-cellheader">Detrazione:</s:td>
					<s:td cssclass="text_align_right" id="ici_detrazione_per_iscop"></s:td>
					<s:td cssclass="seda-ui-cellheader">Versamento Acconto:</s:td>
					<s:td id="iscop_versamento_acconto">
                           Si(0/3)/NO
                       </s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_ISCM">
					<s:td cssclass="seda-ui-cellheader">Riduzione:</s:td>
					<s:td cssclass="text_align_right" id="ici_riduzione_per_iscop"></s:td>
					<s:td cssclass="seda-ui-cellheader">Versamento Saldo:</s:td>
					<s:td id="iscop_versamento_saldo">
                           Si(1/3)/NO
                       </s:td>
				</s:tr>
				<s:tr cssclass="tipo_bollettino_ISCM">
					<s:td cssclass="seda-ui-cellheader">Codice fiscale/P.IVA:</s:td>
					<s:td id="iscop_codice_fiscale"></s:td>
					<s:td cssclass="seda-ui-cellheader">Ravvedimento:</s:td>
					<s:td id="iscop_flag_ravvedimento">
                           No/Si
                       </s:td>
				</s:tr>
				<!-- sezione comune -->
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Strumento:</s:td>
					<s:td icol="3" id="ici_codice_servizio"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Eseguito da:</s:td>
					<s:td icol="3" id="ici_denominazione"></s:td>
				</s:tr>
			</s:tbody>
		</s:table>
	</s:div>

	<!-- FRECCIA -->

	<s:div name="div_table_freccia">
		<s:table border="1" cellspacing="0"
			cssclass="seda-ui-datagrid tableDettaglio2" id="dt_table_freccia">
			<s:thead>
				<s:tr>
					<s:th cssclass="seda-ui-datagrid thDettaglioTitle2" icol="4"
						id="dt_thead_freccia">Bollettino FRECCIA</s:th>
				</s:tr>
			</s:thead>
			<s:tbody id="dt_tbody_freccia">
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Ente Impositore:</s:td>
					<s:td icol="3" id="fr_ente"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Tipologia Servizio:</s:td>
					<s:td icol="3" id="fr_tipologia_servizio"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Importo bollettino:</s:td>
					<s:td cssclass="text_align_right"
						id="fr_importo_totale_bollettino"></s:td>
					<s:td cssclass="seda-ui-cellheader">Rendicontato:</s:td>
					<s:td id="fr_chiave_spedizione">
                           No/Si
                       </s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Cod. Id. Pagamento:</s:td>
					<s:td id="fr_codice_identificativo_pagamento"></s:td>
					<s:td cssclass="seda-ui-cellheader">Motivo Pagamento:</s:td>
					<s:td id="fr_motivo_del_pagamento"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Coor. Bancarie:</s:td>
					<s:td id="fr_codice_iban"></s:td>
					<s:td cssclass="seda-ui-cellheader">Intestato a:</s:td>
					<s:td id="fr_descrizione_intestatario_conto_corrente"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Codice Freccia:</s:td>
					<s:td id="fr_codice_freccia"></s:td>
					<s:td cssclass="seda-ui-cellheader">Strumento:</s:td>
					<s:td id="fr_codice_servizio"></s:td>
				</s:tr>
				<s:tr>
					<s:td cssclass="seda-ui-cellheader">Eseguito da:</s:td>
					<s:td icol="3" id="fr_eseguito_da"></s:td>
				</s:tr>
			</s:tbody>
		</s:table>
	</s:div>
	<s:div name="div_tableoneri_outer">
		<s:div cssclass="thDettaglioTitle2 divTitleSimilTablePopup" name="divTitleOneri">Ripartizione Oneri</s:div>
		<s:div name="div_tableoneri" cssclass="divTableOneriPopup"></s:div>
	</s:div>
	<%--<br /><br />
	<s:div name="divPopupStampa" cssclass="divRicBottoni">
		<s:button id="tx_button_stampa_popup" onclick="setPopupForPrint(); print(); disablePopup();" text="Stampa" type="button" cssclass="btnStyle" />
	</s:div> --%>
	<s:div name="chiudiPopupDettaglio">
		<input type="image"
			src="../applications/templates/shared/img/close_gray2.png"
			alt="Chiudi" title="Chiudi" style="width: 25px; height: 25px">
		<!--  <input type="button" value="Chiudi" src="" />-->
	</s:div>
</s:div>
<s:div cssclass="seda-ui-div" name="backgroundPopup"> &nbsp; </s:div>





