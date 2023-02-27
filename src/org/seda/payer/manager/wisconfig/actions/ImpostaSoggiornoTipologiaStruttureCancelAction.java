package org.seda.payer.manager.wisconfig.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaTipologiaStrutturaRicettivaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaTipologiaStrutturaRicettivaResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.TipologiaStrutturaRicettiva;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ImpostaSoggiornoTipologiaStruttureCancelAction extends BaseManagerAction{
	private static String codop = "cancel";
	private static String ritornaViewstate = "impostasoggiornoTipologiaStrutture_search";
	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
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
			request.setAttribute("tx_chiave_tipologia",(request.getAttribute("tx_codice") == null ? "" : (String)request.getAttribute("tx_codice")));
			request.setAttribute("confermaDisabilitata", false);
			//String codiceTipologiaStruttura = (request.getAttribute("tx_codice") == null ? "" : (String)request.getAttribute("tx_codice"));

			/*String codiceSocieta = (request.getAttribute("tx_societa") == null ? "" : (String)request.getAttribute("tx_societa"));
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
			}*/
			break;
			
		case TX_BUTTON_DELETE:
			//String testo = (String)request.getAttribute("tx_codice");
			TipologiaStrutturaRicettiva tipoStrutturaRicettiva = new TipologiaStrutturaRicettiva(
					                                                       (String)request.getAttribute("tx_chiave_tipologia"),
																		           "", 
																		               null, ""); 



			CancellaTipologiaStrutturaRicettivaRequest cancelRequest = new CancellaTipologiaStrutturaRicettivaRequest();
			cancelRequest.setTipologiaStrutturaRicettiva(tipoStrutturaRicettiva);
			try {
				CancellaTipologiaStrutturaRicettivaResponse out = WSCache.impostaSoggiornoConfigServer.cancellaTipologiaStrutturaRicettiva(cancelRequest, request);			
				if (out != null)
				{
					if(out.getRetCode().equals("00")){
						session.setAttribute("recordDel", out.getRetMessage());
						request.setAttribute("vista",ritornaViewstate);
						request.setAttribute("confermaDisabilitata", true);
						setFormMessage("delete_form", out.getRetMessage(), request);
					}else setFormMessage("delete_form", out.getRetMessage(), request);

				}else setFormMessage("delete_form", Messages.GENERIC_ERROR.format(), request);
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
