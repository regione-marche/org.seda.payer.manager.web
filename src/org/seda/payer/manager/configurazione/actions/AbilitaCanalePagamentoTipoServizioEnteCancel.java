package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteCancelRequest;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.StatusResponse;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.Response;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.ResponseReturncode;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class AbilitaCanalePagamentoTipoServizioEnteCancel extends BaseManagerAction{
	private static String codop = "cancel";
	private static String ritornaViewstate = "AbilitaCanalePagamentoTipoServizioEnte_search";

	public Object service(HttpServletRequest request) throws ActionException {
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
		case TX_BUTTON_NULL:
			/*
			 * Recupero la chiave del record per verifica
			 */
			String codiceSocieta = (request.getAttribute("tx_societa") == null ? "" : (String)request.getAttribute("tx_societa"));
			String codiceUtente = (request.getAttribute("tx_utente") == null ? "" : (String)request.getAttribute("tx_utente"));
			String chiaveEnte = (request.getAttribute("tx_ente") == null ? "" : (String)request.getAttribute("tx_ente"));
			String chiaveCanalePagamento = (request.getAttribute("tx_canalePagamento") == null ? "" : (String)request.getAttribute("tx_canalePagamento"));
			String codiceTipologiaServizio = (request.getAttribute("tx_codiceTipologiaServizio") == null ? "" : (String)request.getAttribute("tx_codiceTipologiaServizio"));
			if (!codiceSocieta.equals("") 
					&& !codiceUtente.equals("")
					&& !chiaveEnte.equals("")
					&& !chiaveCanalePagamento.equals("")
					&& !codiceTipologiaServizio.equals(""))
			{
				setFormMessage("delete_form", Messages.CANCELLA_O_ABBANDONA.format(), request);
			}
			else
			{
				request.setAttribute("confermaDisabilitata", true);
				setFormMessage("delete_form", Messages.DATI_NON_VALIDI.format(), request);
			}
			break;
			
		case TX_BUTTON_DELETE:
			//TODO da verificare il codice per delete per il caso in esame secondo l'esempio sotto riportato
			AbilitaCanalePagamentoTipoServizioEnteCancelRequest cancelRequest = new AbilitaCanalePagamentoTipoServizioEnteCancelRequest();
			cancelRequest.setCompanyCode((String) request.getAttribute("tx_societa"));
			cancelRequest.setUserCode((String) request.getAttribute("tx_utente"));
			cancelRequest.setChiaveEnte((String) request.getAttribute("tx_ente"));
			cancelRequest.setChiaveCanalePagamento((String) request.getAttribute("tx_canalePagamento"));
			cancelRequest.setCodiceTipologiaServizio((String) request.getAttribute("tx_codiceTipologiaServizio"));
			try {
				StatusResponse cancelResponse = WSCache.abilitazTributiServer.cancel(cancelRequest, request);
				
				if (cancelResponse != null)
				{
					Response response = cancelResponse.getResponse();
					if (response != null)
					{
						if (response.getReturncode().getValue().equals(ResponseReturncode.value1.getValue()))
						{
							request.setAttribute("confermaDisabilitata", true);
							setFormMessage("delete_form", Messages.CANC_OK.format(), request);
						}
						else 
							setFormMessage("delete_form", response.getReturnmessage(), request);
					}
					else setFormMessage("delete_form", Messages.GENERIC_ERROR.format(), request);
				}
				else setFormMessage("delete_form", Messages.GENERIC_ERROR.format(), request);
			} catch (FaultType e) {
				request.setAttribute("confermaDisabilitata", true);
				setFormMessage("delete_form", "Errore: " + e.getMessage1(), request);
				e.printStackTrace();
			} catch (RemoteException e) {
				request.setAttribute("confermaDisabilitata", true);
				setFormMessage("delete_form", "Errore: " + e.getMessage(), request);
				e.printStackTrace();
			}
			
			break;
			
		case TX_BUTTON_INDIETRO:
			request.setAttribute("vista",ritornaViewstate);
			break;
		}
		request.setAttribute("codop",codop);
		return null;
		
	}

}
