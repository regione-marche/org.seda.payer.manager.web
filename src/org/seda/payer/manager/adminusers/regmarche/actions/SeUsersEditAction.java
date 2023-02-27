package org.seda.payer.manager.adminusers.regmarche.actions;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PayerCommand;
import org.seda.payer.manager.util.PayerCommandException;
import org.seda.payer.manager.ws.WSCache;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.security.webservice.dati.AggiornaUtentePIVARequestType;
import com.seda.security.webservice.dati.AggiornaUtentePIVAResponseType;
import com.seda.security.webservice.dati.DatiContattiPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiGeneraliPersonaFisicaDelegato;
import com.seda.security.webservice.dati.SelezionaUtentePIVARequestType;
import com.seda.security.webservice.dati.SelezionaUtentePIVAResponseType;
import com.seda.security.webservice.dati.UtentePIVA;

@SuppressWarnings("serial")
public class SeUsersEditAction extends BaseAdminusersAction implements PayerCommand{

	private static String codop = "edit";
	private HashMap<String,Object> parametri = null;

	public Object execute(HttpServletRequest request) throws PayerCommandException {
		String retCode = null;
		String retMessage = null;
		String esito = "";
		
		HttpSession session = request.getSession();
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
		/*
		 * FLOW - Se la richiesta è di tornare al form di ricerca esco
		 * dopo aver cancellato i dati, eventualmente, messi in sessione
		 */
		if (firedButton.equals(FiredButton.TX_BUTTON_CERCA))
		{
			request.setAttribute("vista","seusers_search");
			return null;
		}
		if (!firedButton.equals(FiredButton.TX_BUTTON_NULL) 
				&& !firedButton.equals(FiredButton.TX_BUTTON_RESET))
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
		 * Compio le azioni corrispondenti alla richiesta dell'utente
		 */
		switch(firedButton)
		{
			/*
			 * Aggiorno l'utenza
			 */
			case TX_BUTTON_AGGIUNGI:
				if (esito.equals("OK")) 
				{
					
					/*
					com.seda.security.webservice.dati.AggiornaUtenteRequestType in = 
						newAggiornaUtenteRequestType(request,parametri,codop);*/
					
					AggiornaUtentePIVARequestType in = newAggiornaUtentePIVARequestType(request, parametri, codop);
					
				try {
					AggiornaUtentePIVAResponseType out = WSCache.securityServer.aggiornaUtentePIVA(in, request);
					com.seda.security.webservice.dati.ResponseType response = null;
					if (out != null && out.getResponse() != null)
					{
						response = out.getResponse();
						retCode = response.getRetCode();
						retMessage = response.getRetMessage();
						request.setAttribute("editUserRetCode", retCode);
						if (retCode.equals("00"))
							session.setAttribute("editUserRetMessage", retMessage);
						else
							setFormMessage("var_form", retCode + " - " + retMessage, request);
					}
					else
						setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
					} catch (FaultType e) {
						setFormMessage("var_form", Messages.COMUNICAZIONE_KO.format(e.getLocalizedMessage()), request);
						e.printStackTrace();
					} catch (RemoteException e) {
						setFormMessage("var_form", Messages.COMUNICAZIONE_KO.format(e.getMessage()), request);
						e.printStackTrace();
					}
				}
				else 
					setFormMessage("var_form", esito, request);
				break;

			case TX_BUTTON_SCADENZA_CHANGED:
				request.setAttribute("tx_button_scadenza_changed", "XX");
				if (((String)parametri.get("nessunaScadenza")).equalsIgnoreCase("Y"))
					request.setAttribute("dataScadenza", null);
				break;

			case TX_BUTTON_RESET:
			case TX_BUTTON_NULL: 
				/*
				 * Valorizzo i campi della form 
				 * con i valori presenti in archivio
				 */
			try {
				String username = (String)request.getAttribute("tx_username_hidden");
				if (username != null && !username.equals(""))
				{
					
					SelezionaUtentePIVARequestType in =new SelezionaUtentePIVARequestType();
					in.setUserName(username);
					
					SelezionaUtentePIVAResponseType out = WSCache.securityServer.selezionaUtentePIVA(in, request);
					
					com.seda.security.webservice.dati.ResponseType response = null;
					
					if (out != null && out.getResponse() != null)
					{
						response = out.getResponse();
						retCode = response.getRetCode();
						retMessage = response.getRetMessage();
						if (retCode.equals("00"))
						{
							setSeUsersFormFromWs(session, request, out);
						}
						else
							setFormMessage("var_form", retCode + " - " + retMessage, request);
					}
					else
					{
						setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
					}
				}
				else
				{
					setFormMessage("var_form",Messages.UTENZA_NON_VALIDA.format(), request);
				}
			} catch (FaultType e) {
				setFormMessage("var_form", Messages.COMUNICAZIONE_KO.format(e.getLocalizedMessage()), request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("var_form", Messages.COMUNICAZIONE_KO.format(e.getMessage()), request);
				e.printStackTrace();
			}
			break;
		}
		request.setAttribute("usernameModificabile", false);
		request.setAttribute("nomeModificabile", false);
		request.setAttribute("cognomeModificabile", false);
		request.setAttribute("codiceFiscaleModificabile", false);
		request.setAttribute("flagLepida", true);
		/*
		 * Setto l'action della form di "adminusers_var.jsp"
		 * ed il tipo di operazione
		 */
		request.setAttribute("do_command_name","seUsersEdit.do");
		request.setAttribute("codop",codop);
		return null;
	}

	private AggiornaUtentePIVARequestType newAggiornaUtentePIVARequestType(
			HttpServletRequest request, HashMap<String, Object> parametri,
			String codop) 
	{
		AggiornaUtentePIVARequestType req = new AggiornaUtentePIVARequestType();
		req.setUtentePIVA(newUtentePIVA(request, parametri, codop) );
		return req;
	}

	private void setSeUsersFormFromWs(HttpSession session,HttpServletRequest request,
			SelezionaUtentePIVAResponseType out) 
	{
		UtentePIVA utentePIVA= out.getSelezionaUtentePIVAResponse();
		DatiContattiPersonaFisicaDelegato datiContattiPersonaFisicaDelegato=utentePIVA.getDatiAnagPersonaFisicaDelegato().getDatiContattiPersonaFisicaDelegato();
		DatiGeneraliPersonaFisicaDelegato datiGeneraliPersonaFisicaDelegato=utentePIVA.getDatiAnagPersonaFisicaDelegato().getDatiGeneraliPersonaFisicaDelegato();
		/*com.seda.security.webservice.dati.SecurityUserBean bean =
			out.getSecurityUserBean();*/
		
		Calendar dataScadenza = utentePIVA.getDataFineValiditaUtenza();
		request.setAttribute("tx_nome", datiGeneraliPersonaFisicaDelegato.getNome());
		request.setAttribute("tx_cognome", datiGeneraliPersonaFisicaDelegato.getCognome());
		request.setAttribute("tx_username", utentePIVA.getUsername());
		request.setAttribute("tx_codiceFiscale", datiGeneraliPersonaFisicaDelegato.getCodiceFiscale());
		request.setAttribute("note", utentePIVA.getNote());
		request.setAttribute("tipologiaUtente", utentePIVA.getTipologiaUtente().getValue());
		request.setAttribute("emailNotifiche", datiContattiPersonaFisicaDelegato.getMail());
		request.setAttribute("smsNotifiche", datiContattiPersonaFisicaDelegato.getCellulare());
		request.setAttribute("dataScadenza", dataScadenza);
		request.setAttribute("chk_utenzaAttiva", utentePIVA.getUtenzaAttiva().getValue().equalsIgnoreCase("Y"));
		request.setAttribute("chk_nessunaScadenza",isFine2099(dataScadenza));
		/*
		 * Setto i campi hidden relativi alle informazioni
		 * non modificabili dall'utente
		 */
		request.setAttribute("tx_nome_hidden", datiGeneraliPersonaFisicaDelegato.getNome());
		request.setAttribute("tx_cognome_hidden", datiGeneraliPersonaFisicaDelegato.getCognome());
		request.setAttribute("tx_username_hidden", utentePIVA.getUsername());
		request.setAttribute("tx_codiceFiscale_hidden", datiGeneraliPersonaFisicaDelegato.getCodiceFiscale());
		/*
		 * Metto in sessione la data di scadenza presente in archivio
		 * per confrontarla, in fase di salvataggio, con quella digitata
		 * eventualmente dall'utente
		 */
		session.setAttribute("dataScadenzaFromWs", dataScadenza);

	}
	
	
	
}
