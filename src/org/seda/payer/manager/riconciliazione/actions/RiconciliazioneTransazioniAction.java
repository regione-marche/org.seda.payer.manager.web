package org.seda.payer.manager.riconciliazione.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//import org.seda.payer.manager.actions.BaseManagerAction;
//import org.seda.payer.manager.components.config.ProfileManager;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
//import com.seda.payer.pgec.webservice.commons.dati.RecuperaMovimentiCBIRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaMovimentiCBIResponse;
import com.seda.payer.pgec.webservice.commons.dati.RiepilogoMovimentiCBI;
import com.seda.payer.pgec.webservice.commons.dati.StampaMovimentiPdfRequest;
import com.seda.payer.pgec.webservice.commons.dati.StampaMovimentiPdfResponse;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class RiconciliazioneTransazioniAction extends BaseRiconciliazioneAction {

	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		
		tx_SalvaStato(request);

		super.service(request);
        aggiornamentoCombo(request, session);	
		
		String exp="0";
        boolean actExp = false;
		        
        switch(getFiredButton(request)) {
			case TX_BUTTON_CERCA_EXP: 
				if (request.getParameter("ext") != null && request.getParameter("ext").equals("0"))
					exp = "1";
				else
					exp = "0";
				actExp = true;

				request.setAttribute("rowsPerPage", request.getParameter("hRowsPerPage"));
				request.setAttribute("pageNumber", request.getParameter("hPageNumber"));
				request.setAttribute("order", request.getParameter("hOrder"));
			case TX_BUTTON_CERCA: 
				// gestione compatibilita' richiamo tramite eventuale hyperlink				
				if (request.getAttribute("ext")==null)
					request.setAttribute("ext","0");

				int totMov = 0;
				double totImpMov = 0.0;
				int totTran =0;
				

				if (request.getAttribute("ext")=="" || actExp)
					request.setAttribute("ext",exp);

				try {
					RecuperaMovimentiCBIResponse movimentiCBI = getListaMovimenti(request);
					PageInfo pageInfo = getMovimentiPageInfo(movimentiCBI.getPageInfo());
					if(pageInfo.getNumRows() > 0) {
						String lista = movimentiCBI.getMovimentiCBIXml();					
						request.setAttribute(Field.LISTA_MOVIMENTI.format(), lista);
						request.setAttribute(Field.LISTA_MOVIMENTI_PAGEINFO.format(), pageInfo);
						RiepilogoMovimentiCBI[] listaGrouped= movimentiCBI.getRiepilogoMovimentiCBI();
						request.setAttribute(Field.LISTA_MOVIMENTI_GROUPED.format(), listaGrouped);

						//todo calcolo totali
						for (RiepilogoMovimentiCBI i: listaGrouped)
						{
							totMov = totMov + Integer.parseInt(i.getNumeroMovimenti());
							totImpMov = totImpMov + Double.parseDouble(i.getImportoMovimenti());
							totTran = totTran + Integer.parseInt(i.getNumeroTransazioni());
						}
							
						request.setAttribute("totMov",totMov);
						request.setAttribute("totImpMov",totImpMov);
						request.setAttribute("totTran",totTran);
					}
					else setFormMessage("riconciliazioneTransazioniForm", Messages.TX_NO_TRANSACTIONS.format() , request);

				} 
				catch (Exception e) {
					setFormMessage("riconciliazioneTransazioniForm", e.getMessage() , request);
				}
				break;
			case TX_BUTTON_STAMPA:
				try 
				{
					String filename = getListaMovimentiPdf(request);
					request.setAttribute("pdfMovimenti", filename);
				}
				catch (Exception e) 
				{
					setFormMessage("riconciliazioneTransazioniForm", e.getMessage() , request);
				}
				break;
			case TX_BUTTON_ESPORTADATI:
				// TODO
				break;
        }
        
    				

		return null;
	}
     
	
	
	private String getListaMovimentiPdf(HttpServletRequest request) throws FaultType, RemoteException {
		StampaMovimentiPdfResponse stampaMovimentiPdfResponse;
		StampaMovimentiPdfRequest  stampaMovimentiPdfRequest = new StampaMovimentiPdfRequest();

// stampa singolo gateway o multigateway	0 no	1 si
		String multiGTW = request.getAttribute("multiGTW")!=null?(String)request.getAttribute("multiGTW"):"0";
		
		
		
		String chiaveQuad = (request.getParameter(Field.CHIAVE_MOVIMENTO.format()) == null) ? "0" : request.getParameter(Field.CHIAVE_MOVIMENTO.format());
		if (chiaveQuad.equals("")) chiaveQuad = "0"; 

		stampaMovimentiPdfRequest.setChiaveMovimento(Integer.parseInt(chiaveQuad));
		stampaMovimentiPdfRequest.setCodiceSocieta(getParamCodiceSocieta());
		stampaMovimentiPdfRequest.setCodiceProvincia(codiceProvincia);
		stampaMovimentiPdfRequest.setCodiceUtente(getParamCodiceUtente());
		stampaMovimentiPdfRequest.setMostraMovimenti(request.getParameter(Field.MOSTRA_MOVIMENTI.format()));
		stampaMovimentiPdfRequest.setTipoCarta(request.getParameter(Field.TX_TIPO_CARTA.format()));
		stampaMovimentiPdfRequest.setCodiceABI(request.getParameter(Field.CODICE_ABI.format()));
		stampaMovimentiPdfRequest.setCodiceCAB(request.getParameter(Field.CODICE_CAB.format()));
		stampaMovimentiPdfRequest.setCodiceSIA(request.getParameter(Field.CODICE_SIA.format()));
		stampaMovimentiPdfRequest.setCRO(request.getParameter(Field.CRO.format()));
		stampaMovimentiPdfRequest.setCCB(request.getParameter(Field.CCB.format()));
		stampaMovimentiPdfRequest.setNomeSupporto(request.getParameter(Field.NOME_SUPPORTO.format()));
		String importo_mon_da = request.getParameter(Field.IMPORTO_MOVIMENTO_DA.format()) == null ? "" : request.getParameter(Field.IMPORTO_MOVIMENTO_DA.format());
		String importo_mon_a = request.getParameter(Field.IMPORTO_MOVIMENTO_A.format()) == null ? "" : request.getParameter(Field.IMPORTO_MOVIMENTO_A.format());
		importo_mon_da = importo_mon_da.replaceAll("\\.", "");
		importo_mon_da = importo_mon_da.replaceAll(",", ".");
		importo_mon_a = importo_mon_a.replaceAll("\\.", "");
		importo_mon_a = importo_mon_a.replaceAll(",", ".");
		stampaMovimentiPdfRequest.setImportoA(importo_mon_a);
		stampaMovimentiPdfRequest.setImportoDa(importo_mon_da);
	
		stampaMovimentiPdfRequest.setDataCreazioneDa(getData(request.getParameter(Field.DATA_CREAZIONE_DA_DAY.format()),request.getParameter(Field.DATA_CREAZIONE_DA_MONTH.format()),request.getParameter(Field.DATA_CREAZIONE_DA_YEAR.format())));
		stampaMovimentiPdfRequest.setDataCreazioneA(getData(request.getParameter(Field.DATA_CREAZIONE_A_DAY.format()),request.getParameter(Field.DATA_CREAZIONE_A_MONTH.format()),request.getParameter(Field.DATA_CREAZIONE_A_YEAR.format())));
		stampaMovimentiPdfRequest.setDataValutaDa(getData(request.getParameter(Field.DATA_VALUTA_DA_DAY.format()),request.getParameter(Field.DATA_VALUTA_DA_MONTH.format()),request.getParameter(Field.DATA_VALUTA_DA_YEAR.format())));
		stampaMovimentiPdfRequest.setDataValutaA(getData(request.getParameter(Field.DATA_VALUTA_A_DAY.format()),request.getParameter(Field.DATA_VALUTA_A_MONTH.format()),request.getParameter(Field.DATA_VALUTA_A_YEAR.format())));
		stampaMovimentiPdfRequest.setDataContabileDa(getData(request.getParameter(Field.DATA_CONTABILE_DA_DAY.format()),request.getParameter(Field.DATA_CONTABILE_DA_MONTH.format()),request.getParameter(Field.DATA_CONTABILE_DA_YEAR.format())));
		stampaMovimentiPdfRequest.setDataContabileA(getData(request.getParameter(Field.DATA_CONTABILE_A_DAY.format()),request.getParameter(Field.DATA_CONTABILE_A_MONTH.format()),request.getParameter(Field.DATA_CONTABILE_A_YEAR.format())));
		
		stampaMovimentiPdfRequest.setMultiGTW(multiGTW);
		
		stampaMovimentiPdfResponse = WSCache.commonsServer.stampaMovimentiPdf(stampaMovimentiPdfRequest, request);
		return stampaMovimentiPdfResponse.getFileName();
	}

	

}
