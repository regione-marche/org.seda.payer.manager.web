package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.AbilitaSistemiEsterniSecureSite;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.AbilitaSistemiEsterniSecureSiteSaveRequest;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.Response;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.ResponseReturncode;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.StatusResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class AbilitaSistemiEsterniSecureSiteAdd extends BaseManagerAction
{
	private static String codop = "add";
	private static String ritornaViewstate = "AbilitaSistemiEsterniSecureSite_search";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;
	
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
			parametri = getAbilitaSistemiEsterniSecureSiteFormParameters(request,codop);
			/*
			 * Valido il contenuto
			 */
			String esito = checkInputAbilitaSistemiEsterniSecureSite(parametri);
			/*
			 * Se OK effettuo il salvataggio
			 */
			if (esito.equals("OK"))
			{	
				//Recupero del codice operatore
				UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
				if( user!=null) 
				   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
				usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
				
				AbilitaSistemiEsterniSecureSite abilitaSistemiEsterniSecureSite = new AbilitaSistemiEsterniSecureSite (parametri.get("urlAccesso").toString(), 
						parametri.get("descrizione").toString(), parametri.get("pathFileImmagine").toString(), parametri.get("flagAttivazione").toString(),  
						parametri.get("flagRedirect").toString(), parametri.get("idServizio").toString(), parametri.get("codiceTipologiaServizio").toString(),
						parametri.get("flagCalcoloCosti").toString(),parametri.get("flagInvioNotifica").toString(),0,  
						usernameAutenticazione);
				
				
				AbilitaSistemiEsterniSecureSiteSaveRequest saveRequest = new AbilitaSistemiEsterniSecureSiteSaveRequest(abilitaSistemiEsterniSecureSite,"1");
								
				try {
					StatusResponse out = WSCache.abilitaSistemiEsterniSecureSiteServer.save(saveRequest, request);
					if (out != null)
					{
						Response response = out.getResponse();
						if (response != null)
						{
							String insertRetCode = response.getReturncode().toString();
							if (insertRetCode.equals(ResponseReturncode.value1.getValue()))
								insertRetCode = "00";
							request.setAttribute("insertRetCode", insertRetCode);
							if(response.getReturncode().toString().equals(ResponseReturncode.value1.getValue()))
							{
								session.setAttribute("recordInserted", "OK");
								request.setAttribute("vista",ritornaViewstate);
								//setFormMessage("var_form", Messages.INS_OK.format(), request);
							}
							else setFormMessage("var_form", response.getReturnmessage(), request);
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
		 * Setto l'action della form ed il tipo di operazione
		 */
		request.setAttribute("do_command_name","AbilitaSistemiEsterniSecureSiteAdd.do");
		request.setAttribute("codop",codop);
		return null;
		
	}

}
