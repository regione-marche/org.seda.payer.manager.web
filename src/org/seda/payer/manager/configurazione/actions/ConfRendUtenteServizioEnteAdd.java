package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ConfRendUtenteServizioEnteSaveRequestType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ConfRendUtenteServizioEnteSaveResponseType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ResponseType;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ConfRendUtenteServizioEnteAdd extends BaseManagerAction
{
	private static String codop = "add";
	private static String ritornaViewstate = "ConfRendUtenteServizioEnte_search";
	private HashMap<String,Object> parametri = null;

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		/*
		 * Eseguo le azioni
		 */
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
			resetParametri(request);
			request.setAttribute("disableCarico", true);
			break;

		//aggiunta PG110260 
		case TX_BUTTON_AGGIORNA:
			parametri = getConfRendUtenteServizioEnteFormParameters(request,codop);		
			String esitoRicarica = checkInputConfRend(parametri, request);
			if (!esitoRicarica.equals("OK"))
			{
				setFormMessage("var_form", esitoRicarica, request);
			} 
			break;
		//fine aggiunta PG110260
			
//		case TX_BUTTON_AGGIORNA2:
////			parametri = getConfRendUtenteServizioEnteFormParameters(request,codop);		
////			esitoRicarica = checkInputConfRend(parametri);
////			if (!esitoRicarica.equals("OK"))
////			{
////				setFormMessage("var_form", esitoRicarica, request);
////			} 
//		break;
			
		case TX_BUTTON_NUOVO:
			request.setAttribute("disableCarico", true);
			request.setAttribute("ente", "");
			request.setAttribute("servizio", ""); 
			request.setAttribute("codTributo", "");		//PG180260 GG 04122018 
			break;
			
		case TX_BUTTON_NULL:
			break;
			
		case TX_BUTTON_INDIETRO:
			request.setAttribute("vista",ritornaViewstate);
			break;

		case TX_BUTTON_AGGIUNGI:
			/*
			 * Recupero i parametri dalla form
			 */
			parametri = getConfRendUtenteServizioEnteFormParameters(request,codop);
			/*
			 * Valido il contenuto
			 */
			String esito = checkInputConfRend(parametri, request);
			/*
			 * Se OK effttuo il salvataggio
			 */
			if (esito.equals("OK"))
			{
				
				ConfRendUtenteServizioEnteSaveRequestType in = new ConfRendUtenteServizioEnteSaveRequestType();
				in.setCodOp("1");
				//inizio LP PG200060	
				//in.setBean(getConfRendUtenteServizioEnteBean(parametri,session));
				String template = getTemplateCurrentApplication(request, session);
				in.setBean(getConfRendUtenteServizioEnteBean(parametri, session, template));
				//fine LP PG200060	
				try {
					ConfRendUtenteServizioEnteSaveResponseType out = WSCache.confRendUtenteServizioEnteServer.save(in, request);
					if (out != null)
					{
						ResponseType response = out.getResponse();
						if (response != null)
						{
							request.setAttribute("insertRetCode", response.getRetCode());
							if(response.getRetCode().equals("00"))
							{
								session.setAttribute("recordInserted", "OK");
								request.setAttribute("vista",ritornaViewstate);
							}
							else setFormMessage("var_form", response.getRetMessage(), request);
						}
						else setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
					}
					else setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
				} catch (FaultType e) {
					setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
					e.printStackTrace();
				} catch (RemoteException e) {
					setFormMessage("var_form", "Errore: " + e.getMessage(), request);
					e.printStackTrace();
				}
			}
			else
				setFormMessage("var_form", esito, request);
			break;
		
		}
		/*
		 * Se non esco dalla pagina carico la drop-down-list
		 */
		if (!firedButton.equals(FiredButton.TX_BUTTON_INDIETRO))
			loadSocietaUtenteServizioEnteXml_DDL(request, session);
		/*
		 * Setto l'action della form ed il tipo di operazione
		 */
		request.setAttribute("do_command_name","ConfRendUtenteServizioEnteAdd.do");
		request.setAttribute("codop",codop);
		return null;
		
	}
	


}
