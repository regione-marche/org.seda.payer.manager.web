package org.seda.payer.manager.rendicontazione.actions;

import java.rmi.RemoteException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaFlussiRendPdfRequestType;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaFlussiRendPdfResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class StampaReportPdf extends BaseManagerAction {

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
		String pathname_file_pdf = null;
		String nome_file_pdf = null;
		String codiceSocieta = getParamCodiceSocieta();
		String codiceUtente = getParamCodiceUtente();
		String codiceEnte = getParamCodiceEnte();
		
		String order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
		//String codiceUtenteEnte = ((String)request.getAttribute("tx_UtenteEnte") == null) ? "" : (String)request.getAttribute("tx_UtenteEnte");
		String codiceProvincia = ((String)request.getAttribute("tx_provincia") == null) ? "" : (String)request.getAttribute("tx_provincia");
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
		/*String descrizioneCriteri = recuperaDescrizioni(request, codiceSocieta, codiceUtenteEnte, codiceCanale, chiaveRendicontazione, codiceGateway, cal_dataPagamentoDa, cal_dataPagamentoA, codiceTipologiaServizio, codiceStrumento, tipoFlusso, cal_dataCreazioneDa, cal_dataCreazioneA, codiceProvincia);
		System.out.println("Descrizione Criteri: " + descrizioneCriteri);*/
		
		String idMovimentoCassa = ((String)request.getAttribute("idMovimentoCassa") == null) ? "" : (String)request.getAttribute("idMovimentoCassa");
		
		try {
			RecuperaFlussiRendPdfResponseType res = getFlussiRendPdf(order, codiceSocieta, codiceUtente, codiceEnte, codiceTransazione, codiceCanale, chiaveRendicontazione, codiceGateway, dataPagamentoDa, dataPagamentoA, codiceTipologiaServizio, codiceStrumento, tipoFlusso, dataCreazioneDa, dataCreazioneA, codiceProvincia, "", idMovimentoCassa, codicePSP, request);
			ResponseType response = res.getResponse();
			ResponseTypeRetCode rc = response.getRetCode();
			/*
			 * Se il WS restituisce un errore oppure il path è nullo
			 * invio all'utente un file PDF con un messaggio di errore
			 */
			if(rc.equals(ResponseTypeRetCode.value1))
			{
				pathname_file_pdf = res.getPathNameFilePdf();
				nome_file_pdf="ListaFlussi.pdf";
				if(pathname_file_pdf == null) 
				{
					com.seda.j2ee5.maf.core.action.ActionException e = new ActionException("Errore nella generazione della stampa");
					throw e;
				}
			}
			else 
			{
				com.seda.j2ee5.maf.core.action.ActionException e = new ActionException("Errore nella geneazione della stampa - " + response.getRetMessage() );
				throw e;
			}
			request.setAttribute("pathname_file_pdf", pathname_file_pdf);
			request.setAttribute("nome_file_pdf", nome_file_pdf);
		} catch (FaultType e) {
			throw new com.seda.j2ee5.maf.core.action.ActionException(e);
		} catch (RemoteException e) {
			throw new com.seda.j2ee5.maf.core.action.ActionException(e);
		}
		return null;
	}
	
	private RecuperaFlussiRendPdfResponseType getFlussiRendPdf
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
		String descrizioneCriteri, 
		String idMovimentoCassa,
		String codicePSP,
		HttpServletRequest request
	) throws FaultType, RemoteException
	{
		RecuperaFlussiRendPdfResponseType res = null;
		RecuperaFlussiRendPdfRequestType in = new RecuperaFlussiRendPdfRequestType();
		in.setOrder(order == null ? "" : order);
		in.setCodiceSocieta(codiceSocieta == null ? "" : codiceSocieta);
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
		in.setDescrizioneCriteri(descrizioneCriteri == null ? "" : descrizioneCriteri);
		in.setIdMovimentoCassa(idMovimentoCassa == null ? "" : idMovimentoCassa);
		in.setCodiceUtente(codiceUtente == null ? "" : codiceUtente);

		res = WSCache.commonsServer.recuperaFlussiRendPdf(in, request);
		return res;
	}

	/*protected String recuperaDescrizioni(HttpServletRequest request,String codiceSocieta,String codiceUtenteEnte,
			String codiceCanale,String chiaveRendicontazione,
			String codiceGateway,Calendar cal_dataPagamentoDa,Calendar cal_dataPagamentoA,String codiceTipologiaServizio,
			String codiceStrumento,String tipoFlusso,Calendar cal_dataCreazioneDa,Calendar cal_dataCreazioneA,String codiceProvincia)
	{
		HttpSession session = request.getSession();
		StringBuffer sb = new StringBuffer();
		
		String descr_societa = getDdlItemDescription((String) session.getAttribute("listaSocieta"), codiceSocieta, 1, 2);
		sb.append("Società|").append(descr_societa).append(";");
		String descr_codiceUtenteEnte = getDdlItemDescription((String) session.getAttribute("listaUtentiEnti"), codiceUtenteEnte, 1, 2);
		sb.append("Ente|").append(descr_codiceUtenteEnte).append(";");
		String descr_codiceGateway = getDdlItemDescription((String) session.getAttribute("listaGateway"), codiceGateway, 1, 2);;
		sb.append("Carta|").append(descr_codiceGateway).append(";");
		String dataPagamentoDa = formatDate(cal_dataPagamentoDa,"dd/MM/yyyy");
		sb.append("DataPagamentoDa|").append(dataPagamentoDa).append(";");
		String dataPagamentoA = formatDate(cal_dataPagamentoA,"dd/MM/yyyy");
		sb.append("DataPagamentoA|").append(dataPagamentoA).append(";");
		String descr_codiceTipologiaServizio = getDdlItemDescription((String) session.getAttribute("listaTipologieServizio"), codiceTipologiaServizio, 1, 2);;
		sb.append("TipologiaServizio|").append(descr_codiceTipologiaServizio).append(";");
		String descr_codiceStrumento = getDdlItemDescription((String) session.getAttribute("listaStrumenti"), codiceStrumento, 1, 2);;
		sb.append("StrumentoPagamento|").append(descr_codiceStrumento).append(";");
		String descr_tipoFlusso = getDdlItemDescription((String) session.getAttribute("listaBollettini"), tipoFlusso, 5, 2);;
		sb.append("TipoFlusso|").append(descr_tipoFlusso).append(";");
		String dataCreazioneDa = formatDate(cal_dataCreazioneDa,"dd/MM/yyyy");
		sb.append("DataCreazioneDa:|").append(dataCreazioneDa).append(";");
		String dataCreazioneA = formatDate(cal_dataCreazioneA,"dd/MM/yyyy");
		sb.append("DataCreazioneA:|").append(dataCreazioneA).append(";");
		String descr_provincia = getDdlItemDescription((String) session.getAttribute("listaProvince"), codiceProvincia, 1, 2);;
		sb.append("Provincia|").append(descr_provincia);
	
		return sb.toString();
		
	}*/
}
