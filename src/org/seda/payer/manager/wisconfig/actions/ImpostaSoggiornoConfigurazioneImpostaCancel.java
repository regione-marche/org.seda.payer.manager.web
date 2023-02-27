package org.seda.payer.manager.wisconfig.actions;

import java.rmi.RemoteException;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaConfigurazioneImpSoggRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaConfigurazioneImpSoggResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.ConfigurazioneImpostaSoggiorno;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ImpostaSoggiornoConfigurazioneImpostaCancel extends BaseManagerAction{
	private static String codop = "cancel";
	private static String ritornaViewstate = "impostaSoggiornoConfigurazioneImposta_search";

	public Object service(HttpServletRequest request) throws ActionException {
		//HttpSession session = request.getSession();
		String codiceComune = "";
		String codiceEnte = "";
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
			codiceComune = (request.getAttribute("tx_comune") == null ? "" : (String)request.getAttribute("tx_comune"));
			codiceEnte = (request.getAttribute("tx_ente") == null ? "" : (String)request.getAttribute("tx_ente"));
			//String[] chiave = codiceEnte.split("|");
			String[] chiave= new String[3]; //= (parametri.get("codEnte").toString()).split("|");
			
			StringTokenizer st = new StringTokenizer(codiceEnte,"|");
		    int i = 0; 
			while (st.hasMoreTokens()) {
				chiave[i] = st.nextToken();
				//System.out.println(chiave[i]);
				i++;
		     }

			ConfigurazioneImpostaSoggiorno configurazione = new ConfigurazioneImpostaSoggiorno(
					codiceComune,chiave[0],chiave[1],chiave[2],"","","",null,"","","");

			CancellaConfigurazioneImpSoggRequest cancelRequest = new CancellaConfigurazioneImpSoggRequest();
			cancelRequest.setConfigurazioneImpostaSoggiorno(configurazione);
			try {
				CancellaConfigurazioneImpSoggResponse out = WSCache.impostaSoggiornoConfigServer.cancellaConfigurazioneImposta(cancelRequest, request);
				
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
