package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ConfRendUtenteServizioEnteCancelRequestType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ConfRendUtenteServizioEnteCancelResponseType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ResponseType;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ConfRendUtenteServizioEnteCancel extends BaseManagerAction{
	private static String codop = "cancel";
	private static String ritornaViewstate = "ConfRendUtenteServizioEnte_search";

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
			String codiceTipologiaServizio = (request.getAttribute("tx_codiceTipologiaServizio") == null ? "" : (String)request.getAttribute("tx_codiceTipologiaServizio"));
			if (!codiceSocieta.equals("") 
					&& !codiceUtente.equals("")
					&& !chiaveEnte.equals("")
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
			ConfRendUtenteServizioEnteCancelRequestType in = new ConfRendUtenteServizioEnteCancelRequestType();
			in.setCodiceSocieta((String) request.getAttribute("tx_societa"));
			in.setCodiceUtente((String) request.getAttribute("tx_utente"));
			in.setChiaveEnte((String) request.getAttribute("tx_ente"));
			in.setTipologiaServizio((String) request.getAttribute("tx_codiceTipologiaServizio"));
			try {
				ConfRendUtenteServizioEnteCancelResponseType out = WSCache.confRendUtenteServizioEnteServer.cancel(in, request);
				if (out != null)
				{
					ResponseType response = out.getResponse();
					if (response != null)
					{
						if (response.getRetCode().equals("00"))
						{
							request.setAttribute("confermaDisabilitata", true);
							setFormMessage("delete_form", Messages.CANC_OK.format(), request);
						}
						else 
							setFormMessage("delete_form", response.getRetMessage(), request);
					}
					else setFormMessage("delete_form", Messages.GENERIC_ERROR.format(), request);
				}
				else setFormMessage("delete_form", Messages.GENERIC_ERROR.format(), request);
			} catch (FaultType e) {
				setFormMessage("delete_form", "Errore: " + e.getLocalizedMessage(), request);
				e.printStackTrace();
			} catch (RemoteException e) {
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
