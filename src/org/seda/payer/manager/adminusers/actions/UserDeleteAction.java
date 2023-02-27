package org.seda.payer.manager.adminusers.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.adminusers.dati.EliminaUtenteRequestType;
import com.seda.payer.pgec.webservice.adminusers.dati.EliminaUtenteResponseType;
import com.seda.payer.pgec.webservice.adminusers.dati.ResponseType;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class UserDeleteAction extends BaseManagerAction{
	EliminaUtenteRequestType in = null;
	EliminaUtenteResponseType out = null;
	ResponseType response = null;
	Long chiaveutente = null;
	String retCode = null;
	String RetMessage = null;
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);

		switch(firedButton)
		{
		case TX_BUTTON_DELETE:
			/*
			 * Recupero lo username dell'utente da cancellare
			 */
			chiaveutente = Long.parseLong((String)request.getAttribute("tx_chiaveutente_hidden"));
			try {
				in = new EliminaUtenteRequestType();
				in.setChiaveUtente(chiaveutente);
				out = WSCache.adminUsersServer.eliminaUtente(in, request);
				response = out.getResponse();
				if (response != null)
				{
					retCode = response.getRetCode();
					RetMessage = response.getRetMessage();
					if (retCode.equals("00"))
					{
						request.setAttribute("confermaDisabilitata", true);
						setFormMessage("delete_form", Messages.CANCELLAZIONE_PROFILO_OK.format(), request);
					}
					else
					{
						setFormMessage("delete_form", Messages.CANCELLAZIONE_PROFILO_KO.format(RetMessage), request);
					}
				}
				else
					setFormMessage("delete_form", Messages.CANCELLAZIONE_PROFILO_KO.format("Errore generico"), request);
			} catch (FaultType e) {
				setFormMessage("delete_form", "Errore: " + e.getLocalizedMessage(), request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("delete_form", "Errore: " + e.getMessage(), request);
				e.printStackTrace();
			}
			break;
			
		case TX_BUTTON_NULL:
			/*
			 * Recupero lo username dell'utente da cancellare
			 */
			String chiaveutenteApp = (String)request.getAttribute("tx_chiaveutente");
			chiaveutenteApp = (chiaveutenteApp == null ? "" : chiaveutenteApp);
			chiaveutente = Long.parseLong(chiaveutenteApp);
			if (chiaveutente.longValue() <= 0)
			{
				request.setAttribute("confermaDisabilitata", true);
				setFormMessage("delete_form", Messages.UTENZA_NON_VALIDA.format(), request);
			}
			else
			{
				request.setAttribute("tx_chiaveutente_hidden", chiaveutente);
				//setFormMessage("delete_form", Messages.CANCELLA_O_ABBANDONA.format(), request);
				//inizio LP PG200060
				String template = getTemplateCurrentApplication(request, request.getSession()); 
				if(template.equalsIgnoreCase("regmarche")) {
					setFormMessage("delete_form", Messages.CANCELLA_O_ABBANDONA.format(), request);
				}
				//fine LP PG200060
			}
			break;
			
		}
		return null;
	}
	
	
}
