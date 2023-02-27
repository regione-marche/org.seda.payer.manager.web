package org.seda.payer.manager.rendicontazione.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaFlussiRendCsvRequestType;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaFlussiRendCsvResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;


public class DownloadListaCsv  extends BaseManagerAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		/*
		 * Chiamo il metodo per i settaggi relativi al profilo utente
		 */
		setProfile(request);
		String listaCsv = null;
		String order = (request.getParameter("order") == null) ? "" : request.getParameter("order");
		String codiceSocieta = getParamCodiceSocieta();
		String codiceUtente = getParamCodiceUtente();
		String codiceProvincia = (request.getParameter("tx_provincia") == null) ? "" : request.getParameter("tx_provincia");
		String codiceEnte = getParamCodiceEnte();
		String codiceTransazione = (request.getParameter("tx_codice_transazione") == null) ? "" : request.getParameter("tx_codice_transazione");
		String codiceCanale = (request.getParameter("tx_canale_pagamento") == null) ? "" : request.getParameter("tx_canale_pagamento");
		String chiaveRendicontazione = (request.getParameter("tx_chiave_rendicontazione") == null) ? "" : request.getParameter("tx_chiave_rendicontazione");
		String codiceGateway = (request.getParameter("tx_codice_gateway") == null) ? "" : request.getParameter("tx_codice_gateway");
		String codicePSP = ((String)request.getAttribute("tx_codice_psp") == null) ? "" : (String)request.getAttribute("tx_codice_psp");
		String dataPagamentoDa = (getDataByPrefix("tx_data_pag_da", request));
		String dataPagamentoA = (getDataByPrefix("tx_data_pag_a", request));
		//String codiceTipologiaServizio = (request.getParameter("tx_tipologia_servizio") == null) ? "" : request.getParameter("tx_tipologia_servizio");		
		String codiceTipologiaServizio = getTipologiaServizio(request,session);
		if(codiceTipologiaServizio.length()>3){
			codiceTipologiaServizio = codiceTipologiaServizio.substring(0,3);
    	}
    	
		
		
		String codiceStrumento = (request.getParameter("tx_strumento") == null) ? "" : request.getParameter("tx_strumento");
		String tipoFlusso = (request.getParameter("tx_tipo_flusso") == null) ? "" : request.getParameter("tx_tipo_flusso");
		String dataCreazioneDa = (getDataByPrefix("tx_data_cre_da", request));
		String dataCreazioneA = (getDataByPrefix("tx_data_cre_a", request));
		String idMovimentoCassa = ((String)request.getAttribute("idMovimentoCassa") == null) ? "" : (String)request.getAttribute("idMovimentoCassa");
		String movimentoCassa = ((String)request.getAttribute("movimentoCassa") == null) ? "" : (String)request.getAttribute("movimentoCassa");
		String giornaleCassa = ((String)request.getAttribute("giornaleCassa") == null) ? "" : (String)request.getAttribute("giornaleCassa");

		
		try {
			RecuperaFlussiRendCsvResponseType res = getFlussiRend(order, codiceSocieta, codiceUtente, codiceEnte, codiceTransazione, codiceCanale, chiaveRendicontazione, codiceGateway, dataPagamentoDa, dataPagamentoA, codiceTipologiaServizio, codiceStrumento, tipoFlusso, dataCreazioneDa, dataCreazioneA,codiceProvincia, idMovimentoCassa, codicePSP, movimentoCassa, giornaleCassa, request);
			ResponseType response = res.getResponse();
			ResponseTypeRetCode rc = response.getRetCode();
			request.setAttribute("nome_file_csv", "ListaFlussi.csv");
			if(rc.equals(ResponseTypeRetCode.value1))
			{
				listaCsv = res.getFlussiRendCsv();
				request.setAttribute("lista_flussi_csv", listaCsv);
				if(listaCsv == null)
				{
					com.seda.j2ee5.maf.core.action.ActionException e = new ActionException("Errore nella generazione del file csv");
					throw e;
				}
			}
			else
			{
				com.seda.j2ee5.maf.core.action.ActionException e = new ActionException("Errore nella geneazione del file csv - " + response.getRetMessage() );
				throw e;
			}
		} catch (FaultType e) {
			throw new com.seda.j2ee5.maf.core.action.ActionException(e);
		} catch (RemoteException e) {
			throw new com.seda.j2ee5.maf.core.action.ActionException(e);
		}
		
		return null;
	}

	private RecuperaFlussiRendCsvResponseType getFlussiRend
	(
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
		RecuperaFlussiRendCsvResponseType res = null;
		RecuperaFlussiRendCsvRequestType in = new RecuperaFlussiRendCsvRequestType();
		in.setOrder(order == null ? "" : order);
		in.setCodiceSocieta(codiceSocieta == null ? "" : codiceSocieta);
		in.setCodiceUtente(codiceUtente == null ? "" : codiceUtente);
		in.setCodiceEnte(codiceEnte == null ? "" : codiceEnte);
		in.setCodiceTransazione(codiceTransazione == null ? "" : codiceTransazione);
		in.setCodiceCanale(codiceCanale == null ? "" : codiceCanale);
		in.setChiaveRendicontazione(chiaveRendicontazione == null ? "" : chiaveRendicontazione);
		in.setCodiceGateway(codiceGateway == null ? "" : codiceGateway);
		in.setCodicePSP(codicePSP == null ? "" : codicePSP);
		in.setDataPagamentoDa(dataPagamentoDa == null ? "" : dataPagamentoDa);
		in.setDataPagamentoA(dataPagamentoA == null ? "" : dataPagamentoA);
		in.setCodiceTipologiaServizio(codiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		in.setCodiceStrumento(codiceStrumento == null ? "" : codiceStrumento);
		in.setTipoFlusso(tipoFlusso == null ? "" : tipoFlusso);
		in.setDataCreazioneDa(dataCreazioneDa == null ? "" : dataCreazioneDa);
		in.setDataCreazioneA(dataCreazioneA == null ? "" : dataCreazioneA);
		in.setCodiceProvincia(codiceProvincia == null ? "" : codiceProvincia);
		in.setIdMovimentoCassa(idMovimentoCassa == null ? "": idMovimentoCassa);
		in.setMovimentoCassa(movimentoCassa == null ? "": movimentoCassa);
		in.setGiornaleCassa(giornaleCassa == null ? "": giornaleCassa);

		res = WSCache.commonsServer.recuperaFlussiRendCsv(in, request);
		return res;
	}
}
