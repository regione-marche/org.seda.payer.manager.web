package org.seda.payer.manager.rendicontazione.actions;

import java.rmi.RemoteException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaFlussiRendRequestType;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaFlussiRendResponseType;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaFlussiRendResponseTypePageInfo;
import com.seda.payer.pgec.webservice.commons.dati.ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;

import org.seda.payer.manager.ws.WSCache;

/*
 * --------------------------------------------------------------------
 * VBRUNO - 5 agosto 2011
 * Modificato il caricamento della DDL UtentiEnti per tenere conto
 * dell'opzione "Multiutente" dello user loggato
 * -------------------------------------------------------------------
 */

public class RicercaFlussi extends BaseManagerAction {

	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		/*
		 * Chiamo il metodo per i settaggi relativi al profilo utente
		 */
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) 
			setProfile(request);
		/*
		 * Carico le DDl statiche
		 */
		loadStaticXml_DDL(request, session);
		
		if(getTemplateCurrentApplication(request, session).equals("regmarche") && request.getAttribute("tx_data_cre_da")==null && request.getAttribute("tx_data_pag_da")==null) 
			request.setAttribute("tx_data_cre_da", Calendar.getInstance());

		int rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		int pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		String order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
		String siglaProvincia = ((String)request.getAttribute("tx_provincia") == null) ? "" : (String)request.getAttribute("tx_provincia");
		String codiceTransazione = ((String)request.getAttribute("tx_codice_transazione") == null) ? "" : (String)request.getAttribute("tx_codice_transazione");
		String codiceCanale = ((String)request.getAttribute("tx_canale_pagamento") == null) ? "" : (String)request.getAttribute("tx_canale_pagamento");
		String chiaveRendicontazione = ((String)request.getAttribute("tx_chiave_rendicontazione") == null) ? "" : (String)request.getAttribute("tx_chiave_rendicontazione");
		String codiceGateway = ((String)request.getAttribute("tx_codice_gateway") == null) ? "" : (String)request.getAttribute("tx_codice_gateway");
		String codicePSP = ((String)request.getAttribute("tx_codice_psp") == null) ? "" : (String)request.getAttribute("tx_codice_psp");
		Calendar cal_dataPagamentoDa = (Calendar)request.getAttribute("tx_data_pag_da");
		Calendar cal_dataPagamentoA = (Calendar)request.getAttribute("tx_data_pag_a");
		String dataPagamentoDa = formatDate(cal_dataPagamentoDa, "yyyy-MM-dd");
		String dataPagamentoA = formatDate(cal_dataPagamentoA, "yyyy-MM-dd");
		//String codiceTipologiaServizio = ((String)request.getAttribute("tx_tipologia_servizio") == null) ? "" : (String)request.getAttribute("tx_tipologia_servizio");
		
		String codiceTipologiaServizio = getTipologiaServizio(request,session);
		
		String codiceStrumento = ((String)request.getAttribute("tx_strumento") == null) ? "" : (String)request.getAttribute("tx_strumento");
		String tipoFlusso = ((String)request.getAttribute("tx_tipo_flusso") == null) ? "" : (String)request.getAttribute("tx_tipo_flusso");
		Calendar cal_dataCreazioneDa = (Calendar)request.getAttribute("tx_data_cre_da");
		Calendar cal_dataCreazioneA = (Calendar)request.getAttribute("tx_data_cre_a");
		String dataCreazioneDa = formatDate(cal_dataCreazioneDa, "yyyy-MM-dd");
		String dataCreazioneA = formatDate(cal_dataCreazioneA, "yyyy-MM-dd");
		
		//PREJAVA18_LUCAP_05082020
		String idMovimentoCassa = ((String)request.getAttribute("idMovimentoCassa") == null) ? ((String)request.getAttribute("idmdc") == null ? "" : (String)request.getAttribute("idmdc")) : (String)request.getAttribute("idMovimentoCassa");
		request.setAttribute("idMovimentoCassa", idMovimentoCassa);
		//FINE PREJAVA18_LUCAP_05082020
		
		String movimentoCassa = ((String)request.getAttribute("movimentoCassa") == null) ? "" : (String)request.getAttribute("movimentoCassa");
		String giornaleCassa = ((String)request.getAttribute("giornaleCassa") == null) ? "" : (String)request.getAttribute("giornaleCassa");
		
			switch(firedButton)
			{
				case TX_BUTTON_CERCA: 
					try {
						
						String messageDate = controlloDate(request);
						if(getTemplateCurrentApplication(request, session).equals("regmarche") && messageDate!=null) {
							setFormMessage("form_selezione", messageDate, request);
						}else {
							loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
							//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), false);
							loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), false);
							loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), false);
							//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);
							LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), false);
		
							RecuperaFlussiRendResponseType res = getFlussiRend(rowsPerPage, pageNumber, order, getParamCodiceSocieta(), getParamCodiceUtente(), getParamCodiceEnte(), codiceTransazione, codiceCanale, chiaveRendicontazione, codiceGateway, dataPagamentoDa, dataPagamentoA, codiceTipologiaServizio, codiceStrumento, tipoFlusso, dataCreazioneDa, dataCreazioneA,siglaProvincia, idMovimentoCassa, codicePSP, movimentoCassa, giornaleCassa, request);
							if(res != null)
							{
								ResponseType response = res.getResponse();
								ResponseTypeRetCode rc = response.getRetCode();
								if(rc.equals(ResponseTypeRetCode.value1))
								{
									PageInfo pageInfo = this.getPageInfo(res.getPageInfo());
									if(pageInfo != null)
									{
										if(pageInfo.getNumRows() > 0)
										{
											request.setAttribute("lista_flussi", res.getFlussiRenXml());
											request.setAttribute("lista_flussi.pageInfo", pageInfo);
											request.setAttribute("lista_flussi_riepilogo", res.getFlussiRenGroupedXml());
										}
										else setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
									}
									else setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
								}
								else
								{
									String messaggio = response.getRetMessage(); 
									setFormMessage("form_selezione", (messaggio == null || messaggio.equals("")) ? "Errore generico" : messaggio, request);
								}
								
							}
							else setFormMessage("form_selezione", Messages.GENERIC_ERROR.format(), request);
						}
					} catch (FaultType e) {
						setErrorMessage(e.getMessage());
						e.printStackTrace();
					} catch (RemoteException e) {
						setErrorMessage(e.getMessage());
						e.printStackTrace();
					} catch(Exception e) {
						setErrorMessage(e.getMessage());
						e.printStackTrace();
					}
					break;
					
				case TX_BUTTON_RESET:
					resetParametri(request);
					request.setAttribute("tx_data_pag_da", null);
					request.setAttribute("tx_data_pag_a", null);
					if(getTemplateCurrentApplication(request, session).equals("regmarche"))
						request.setAttribute("tx_data_cre_da", Calendar.getInstance());
					else
						request.setAttribute("tx_data_cre_da", null);
					request.setAttribute("tx_data_cre_a", null);
					setProfile(request);
					siglaProvincia="";
					loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
					//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
					loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
					loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
					//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
					LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), true);
					break;
					
				case TX_BUTTON_SOCIETA_CHANGED:
					String societa = getParamCodiceSocieta();
					loadProvinciaXml_DDL(request, session, societa,true);
					//LoadListaUtentiEntiXml_DDL(request, session, societa, (societa.equals("") ? "" : siglaProvincia), (societa.equals("") ? "" : getParamCodiceEnte()), (societa.equals("") ? "" : getParamCodiceUtente()), true);
					LoadListaUtentiEntiXml_DDL(request, session, societa, (societa.equals("") ? "" : siglaProvincia), (societa.equals("") ? "" : getParamCodiceEnte()), (societa.equals("") ? "" : (isMultiUtenteEnabled() ? "" : getParamCodiceUtente())), true);
					//loadTipologiaServizioXml_DDL(request, session, societa, true);
					loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
					loadListaGatewayXml_DDL(request, session, societa, (societa.equals("") ? "" : getParamCodiceUtente()), true);
					request.setAttribute("tx_provincia","");
					request.setAttribute("tx_UtenteEnte", "");
					break;
					
				case TX_BUTTON_PROVINCIA_CHANGED:
					loadProvinciaXml_DDL(request, session, null,false);
					//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, (siglaProvincia.equals("") ? "" : getParamCodiceEnte()), (siglaProvincia.equals("") ? "" : getParamCodiceUtente()), true);
					LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, (siglaProvincia.equals("") ? "" : getParamCodiceEnte()), (siglaProvincia.equals("") ? "" : (isMultiUtenteEnabled() ? "" : getParamCodiceUtente())), true);
					loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
					loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
					request.setAttribute("tx_UtenteEnte", "");
					break;
				
				case TX_BUTTON_ENTE_CHANGED:
					loadProvinciaXml_DDL(request, session, "",false);
					LoadListaUtentiEntiXml_DDL(request, session, "" , "", "", "", false);
					//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
					loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
					
					loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
					break;
					
					
				case TX_BUTTON_NULL: 
					loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
					//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
					loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
					loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
					//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
					LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), true);
					break;
					
				case TX_BUTTON_STAMPA:
					break;
					
				case TX_BUTTON_DOWNLOAD:
					break;
			}
		
		return null;
	}
 
	private RecuperaFlussiRendResponseType getFlussiRend
	(
			int rowsPerPage, 
			int pageNumber,
			String order,
			String codiceSocieta,
			String codiceUtente,
			String codiceEnte,
			String codiceTransazione,
			String codiceCanale,
			String chiaveRendicontazione,
			String codiceGateway,
			String dataPagamentoDa,
			String dataPagamentoA,
			String codiceTipologiaServizio,
			String codiceStrumento,
			String tipoFlusso,
			String dataCreazioneDa,
			String dataCreazioneA,
			String codiceProvincia,
			String idMovimentoCassa,
			String codicePSP,
			String movimentoCassa,
			String giornaleCassa,
			HttpServletRequest request
			
	) throws FaultType, RemoteException
	{
		RecuperaFlussiRendResponseType res = null;
		RecuperaFlussiRendRequestType in = new RecuperaFlussiRendRequestType();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setCodiceSocieta(codiceSocieta == null ? "" : codiceSocieta);
		in.setCodiceUtente(codiceUtente == null ? "" : codiceUtente);
		in.setCodiceEnte(codiceEnte == null ? "" : codiceEnte);
		in.setCodiceTransazione(codiceTransazione == null ? "" : codiceTransazione);
		in.setCodiceCanale(codiceCanale == null ? "" : codiceCanale);
		in.setChiaveRendicontazione(chiaveRendicontazione == null ? "" : chiaveRendicontazione);
		in.setCodiceGateway(codiceGateway == null ? "" : codiceGateway);
		in.setDataPagamentoDa(dataPagamentoDa == null ? "" : dataPagamentoDa);
		in.setDataPagamentoA(dataPagamentoA == null ? "" : dataPagamentoA);
		//Giulia 8/05/2014 INIZIO
		
        String serv = request.getAttribute("codTipologiaServizio")!=null?request.getAttribute("codTipologiaServizio").toString():"";
        
        if(!codiceTipologiaServizio.equals("")){
        	serv = codiceTipologiaServizio;
        }
        
        if(serv!=null && !serv.equals("") && userBean.getProfile().equals("AMMI")){
        	in.setCodiceTipologiaServizio(serv.substring(0, 3));
    		if(serv.length()>0){
    			in.setCodiceSocieta(serv.substring(4));
    		}
    		}
    		else {
    			if (serv!=null && !serv.equals("") && serv.indexOf("_", 0) != -1) {
    				
    				serv= serv.replace("'", "");
    				in.setCodiceTipologiaServizio(serv.substring(0, 3));
    			}
    			else
    				in.setCodiceTipologiaServizio(serv);
    		}
        //Giulia 8/05/2014 FINE 
		//in.setCodiceTipologiaServizio(codiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		in.setCodiceStrumento(codiceStrumento == null ? "" : codiceStrumento);
		in.setTipoFlusso(tipoFlusso == null ? "" : tipoFlusso);
		in.setDataCreazioneDa(dataCreazioneDa == null ? "" : dataCreazioneDa);
		in.setDataCreazioneA(dataCreazioneA == null ? "" : dataCreazioneA);
		in.setCodiceProvincia(codiceProvincia == null ? "": codiceProvincia);
		in.setIdMovimentoCassa(idMovimentoCassa == null ? "": idMovimentoCassa);
		in.setCodicePSP(codicePSP == null ? "" : codicePSP); //PG1360180_001 - 201360520
		in.setMovimentoCassa(movimentoCassa == null ? "": movimentoCassa);
		in.setGiornaleCassa(giornaleCassa == null ? "": giornaleCassa);
		
		res = WSCache.commonsServer.recuperaFlussiRend(in, request);
		return res;
	}

	private PageInfo getPageInfo(RecuperaFlussiRendResponseTypePageInfo pi)
	{
		if (pi == null) return null;
		PageInfo pageInfo = new PageInfo();
		pageInfo.setFirstRow(pi.getFirstRow());
		pageInfo.setLastRow(pi.getLastRow());
		pageInfo.setNumPages(pi.getNumPages());
		pageInfo.setNumRows(pi.getNumRows());
		pageInfo.setPageNumber(pi.getPageNumber());
		pageInfo.setRowsPerPage(pi.getRowsPerPages());
		return pageInfo;
		
	}
	
	private String controlloDate(HttpServletRequest request)
	{
		String sDataPagDa = getDataByPrefix("tx_data_pag_da",request);
		String sDataPagA = getDataByPrefix("tx_data_pag_a",request);
		
		boolean sDataPagDaIsNullOrEmpty = sDataPagDa==null || sDataPagDa.equals("");
		boolean sDataPagAIsNullOrEmpty = sDataPagA==null || sDataPagA.equals("");
		
		String sDataFlussoDa = getDataByPrefix("tx_data_cre_da", request);
		String sDataFlussoA = getDataByPrefix("tx_data_cre_a", request);
		
		boolean sDataFlussoDaIsNullOrEmpty = sDataFlussoDa==null || sDataFlussoDa.equals("");
		boolean sDataFlussoAIsNullOrEmpty = sDataFlussoA==null || sDataFlussoA.equals("");

		Calendar dataPagDa = parseDate(sDataPagDa, "yyyy-MM-dd");
        Calendar dataPagA = parseDate(sDataPagA, "yyyy-MM-dd");
        
        
        Calendar dataFlussoDa = parseDate(sDataFlussoDa, "yyyy-MM-dd");
        Calendar dataFlussoA = parseDate(sDataFlussoA, "yyyy-MM-dd");
        
        if(sDataPagDaIsNullOrEmpty && sDataFlussoDaIsNullOrEmpty)
        	return "Data Pagamento da e/o Data Creazione Flusso da obbligatoria";
        if (sDataPagAIsNullOrEmpty)
        	dataPagA = Calendar.getInstance();
        if (!sDataPagDaIsNullOrEmpty && dataPagDa.after(dataPagA))
        	return "La Data Pagamento da deve essere antecedente o uguale alla Data Pagamento a";
        if(!sDataPagDaIsNullOrEmpty) {
	        dataPagDa.add(Calendar.DAY_OF_MONTH, 360);
	        if (dataPagDa.before(dataPagA))
	        	return "Il massimo range di giorni consentito è di 360 giorni";
        }
        
        if(!sDataFlussoDaIsNullOrEmpty && sDataFlussoAIsNullOrEmpty)
        	dataFlussoA = Calendar.getInstance();        
        if (!sDataFlussoDaIsNullOrEmpty && dataFlussoDa.after(dataFlussoA))
        	return "La Data Creazione Flusso da deve essere antecedente o uguale alla Data Creazione Flusso a";
        if(!sDataFlussoDaIsNullOrEmpty) {
	        dataFlussoDa.add(Calendar.DAY_OF_MONTH, 360);
	        if (dataFlussoDa.before(dataFlussoA))
	        	return "Il massimo range di giorni consentito è di 360 giorni";
        }        
        return null;
        
	}
	

}
