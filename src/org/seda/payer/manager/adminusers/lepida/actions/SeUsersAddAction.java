package org.seda.payer.manager.adminusers.lepida.actions;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PayerCommand;
import org.seda.payer.manager.util.PayerCommandException;
import org.seda.payer.manager.ws.WSCache;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.security.webservice.dati.InserisciUtentePIVARequestType;
import com.seda.security.webservice.dati.InserisciUtentePIVARequestTypeFlagAbilitazioneAutomatica;
import com.seda.security.webservice.dati.InserisciUtentePIVAResponseType;
import com.seda.security.webservice.dati.ResponseType;
import com.seda.security.webservice.dati.UtentePIVA;

@SuppressWarnings("serial")
public class SeUsersAddAction extends BaseAdminusersAction implements PayerCommand{

	private static String codop = "add";
	private HashMap<String,Object> parametri = null;

	public Object execute(HttpServletRequest request) throws PayerCommandException {
		String retCode = null;
		String retMessage = null;
		HttpSession session = request.getSession();
		String esito = "";
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		/*
		 * Azzero il campo hidden "fired_button_hidden" per evitare
		 * che possa essere riproposto nel caso in cui interviene 
		 * il "validator"
		 */
		request.setAttribute("fired_button_hidden", "");
		//
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET) && 
				!firedButton.equals(FiredButton.TX_BUTTON_NULL) &&
				!firedButton.equals(FiredButton.TX_BUTTON_NUOVO)) 
		{
			/*
			 *  Ricavo i parametri della request
			 */
			parametri = getSeUsersFormParameters(request, codop);
			/*
			 *  Setto i flag
			 */
			setSeUsersFormFlags(request, parametri);
			/*
			 * Controllo che siano stati riempiti
			 * tutti i campi obbligatori
			 */
			esito = checkSeUsersInputFields(parametri,codop);
		}
		/*
		 * Il campo userName è modificabile
		 */
		request.setAttribute("usernameModificabile", true);
		/*
		 * Compio le azioni corrispondenti alla richiesta dell'utente
		 */
		switch(firedButton)
		{
			/*
			 * Vado allo STEP2	
			 */
			case TX_BUTTON_AGGIUNGI:
				if (esito.equals("OK")) 
				{
					// INSERISCO IL NUOVO UTENTE
					
					InserisciUtentePIVARequestType in = newInserisciUtentePIVARequestType(request, parametri, codop);
					
					try {
						InserisciUtentePIVAResponseType out = WSCache.securityServer.inserisciUtentePIVA(in, request);
						ResponseType response =	out.getResponse();
						if (response != null)
						{
							/*
							 * Setto l'attributo "addUserRetCode" perchè il flow sappia
							 * dove redirigere la richiesta: sulla pagina di ricerca
							 * se l'inserimento è terminato correttamente
							 * oppure ancora sulla pagina di inserimento
							 * 
							 * Se l'esito è positivo metto anche in sessione
							 * il messaggio da visualizzare all'utente sulla
							 * maschera di ricerca.
							 * 
							 */
							retCode = response.getRetCode();
							retMessage = response.getRetMessage();
							request.setAttribute("addUserRetCode", retCode);
							if (retCode.equals("00"))
							{
								session.setAttribute("addUserRetMessage", retMessage);
							}
							else
								setFormMessage("var_form", retCode + " - " + retMessage, request);
						}
						else
							setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
						
					} catch (FaultType e) {
						setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
						e.printStackTrace();
					} catch (RemoteException e) {
						setFormMessage("var_form", "Errore: " + e.getMessage(), request);
						e.printStackTrace();
					}
				}
				else
				{
					setFormMessage("var_form", esito, request);
				}
				break;
			
			case TX_BUTTON_PASSWORD_AUTOGENERATA_CHANGED:
				request.setAttribute("tx_button_password_autogenerata_changed", "XX");
				break;
				
			case TX_BUTTON_SCADENZA_CHANGED:
				request.setAttribute("tx_button_scadenza_changed", "XX");
				if (((String)parametri.get("nessunaScadenza")).equalsIgnoreCase("Y"))
					request.setAttribute("dataScadenza", null);
				break;

			case TX_BUTTON_RESET:
			case TX_BUTTON_NULL: 
			case TX_BUTTON_NUOVO: 
				resetParametri(request);
				request.setAttribute("dataScadenza", null);
				/*
				 * IMPOSTAZIONI DI PARTENZA
				 */
				// La password è autogenerata
				request.setAttribute("chk_passwordAutogenerata", true);
				// L'utenza è attiva
				request.setAttribute("chk_utenzaAttiva",true);
				// L'utenza non scade mai
				request.setAttribute("chk_nessunaScadenza",true);
				break;
				
			/*
			 * Torno al form di ricerca	
			 */
			case TX_BUTTON_CERCA:
				request.setAttribute("vista","seusers_search");
				break;
		}
		/*
		 * Setto l'action della form di "seUsers_var.jsp",
		 * e il tipo di operazione 
		 */
		request.setAttribute("do_command_name","seUsersAdd.do");
		request.setAttribute("codop",codop);
		return null;
	}
	
	private InserisciUtentePIVARequestType newInserisciUtentePIVARequestType(HttpServletRequest request, HashMap<String, Object> parametri, String codop) {
		InserisciUtentePIVARequestType req = new InserisciUtentePIVARequestType();
		req.setFlagAbilitazioneAutomatica(InserisciUtentePIVARequestTypeFlagAbilitazioneAutomatica.fromString((String)parametri.get("utenzaAttiva")));
		req.setPasswordAutogenerata((String)parametri.get("passwordAutogenerata"));
		req.setPassword("");
		req.setUrlSito(request.getServerName());
		UtentePIVA utentePIVA=newUtentePIVA(request, parametri, codop);
		req.setUtentePIVA(utentePIVA);
		
		/*com.seda.security.webservice.dati.SecurityUserBean bean = newSecurityUserBean(request, parametri, codop);
		bean.setUsername(bean.getCodiceFiscale());
		req.setSecurityUserBean(bean);*/
		
		
		return req;
	}
	
	
}
