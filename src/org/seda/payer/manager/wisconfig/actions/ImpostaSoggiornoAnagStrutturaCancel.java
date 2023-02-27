package org.seda.payer.manager.wisconfig.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaAnagraficaStrutturaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaAnagraficaStrutturaResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.AnagraficaStrutturaRicettiva;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ImpostaSoggiornoAnagStrutturaCancel extends BaseManagerAction{
	private static String codop = "cancel";
	private static String ritornaViewstate = "impostaSoggiornoAnagStruttura_search";

	public Object service(HttpServletRequest request) throws ActionException {
		//HttpSession session = request.getSession();
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
			
			/*TipologiaSoggetto tipoSoggetto = new TipologiaSoggetto((String) request.getAttribute("tx_chiave_soggetti"),
																		(String) request.getAttribute("tx_comune"), 
																			"",
																				"", 
																					"", 
																						 null, "");
			 */
			AnagraficaStrutturaRicettiva anagraficaStrutturaRicettiva = new AnagraficaStrutturaRicettiva(
					(String) request.getAttribute("tx_anag_struttura"), 
						(String) request.getAttribute("tx_comune"),"","","","","","","","","","","","","","","",null,"","","",
						null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);	//PG160040 GG 19052016 //PG170240 CT //PG180170	//PG190300_001 SB

			CancellaAnagraficaStrutturaRequest cancelRequest = new CancellaAnagraficaStrutturaRequest();
			cancelRequest.setAnagraficaStruttura(anagraficaStrutturaRicettiva);
			try {
				CancellaAnagraficaStrutturaResponse out = WSCache.impostaSoggiornoConfigServer.cancellaAnagraficaStruttura(cancelRequest, request);
				
				if (out != null && out.getRetCode() != null)
				{
					if(out.getRetCode().equals("00")){
						request.setAttribute("confermaDisabilitata", true);
						setFormMessage("delete_form", Messages.CANC_OK.format(), request);
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
