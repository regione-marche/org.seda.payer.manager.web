package org.seda.payer.manager.adminusers.actions;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseAdminusersAction;
import org.seda.payer.manager.adminusers.util.Profilo;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PayerCommand;
import org.seda.payer.manager.util.PayerCommandException;

@SuppressWarnings("serial")
public class SeUsersAddAction extends BaseAdminusersAction implements PayerCommand{

	private static String codop = "add";
	private static String step = "seuseradd";
	private String stepOld;
	
	private Profilo profilo = new Profilo();

	public Object execute(HttpServletRequest request) throws PayerCommandException {
		HttpSession session = request.getSession();
		String esito = "OK";
		Calendar cal = Calendar.getInstance();
		request.setAttribute("yearNow", cal.get(Calendar.YEAR));
		
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
		//prendo il profilo dalla session
		if (session.getAttribute(ManagerKeys.PROFILO_UTENTEPIVA) != null)
			profilo = (Profilo) session.getAttribute(ManagerKeys.PROFILO_UTENTEPIVA);
		
		
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET) && 
				!firedButton.equals(FiredButton.TX_BUTTON_NULL) &&
				!firedButton.equals(FiredButton.TX_BUTTON_STEP1) &&
				!firedButton.equals(FiredButton.TX_BUTTON_NUOVO)) 
		{
			/*
			 *  Ricavo i parametri della request
			 */
			profilo = getSeUsersFormParametersUtentePIVA(request, codop, profilo);


			/*
			 * Controllo che siano stati riempiti
			 * tutti i campi obbligatori
			 */
			esito = checkSeUsersInputFieldsUtentePIVA(profilo,codop, request);
			request.setAttribute("inputFieldChecked", esito);
		}
		/*
		 * Il campo userName è modificabile
		 */
		request.setAttribute("usernameModificabile", true);
		request.setAttribute("tipologiaDisabled", false);
		/*
		 * Compio le azioni corrispondenti alla richiesta dell'utente
		 */
		switch(firedButton)
		{
			/* PROVENGO DALLO STEP1 DELLA PERSONA FISICA O PERSONA GIURIDICA*/
			case TX_BUTTON_STEP1:
				
				//if (session.getAttribute(ManagerKeys.PROFILO_UTENTEPIVA) != null)
					//profilo = (Profilo) session.getAttribute(ManagerKeys.PROFILO_UTENTEPIVA);
				
				
				break;
			
			/*
			 * Vado all'inserimento della Persona Giuridica o all'inserimento della Persona Fisica 
			 * a seconda della tipologia utente da inserire.	
			 */
			case TX_BUTTON_AVANTI:
				if (esito.equals("OK")) 
					stepOld="seuseradd";
				else	
					setFormMessage("var_form", esito, request);
				
				break;
			case TX_BUTTON_PASSWORD_AUTOGENERATA_CHANGED:	
				profilo.setPassword("");
				break;
				
			case TX_BUTTON_SCADENZA_CHANGED:
				
				break;
			case TX_BUTTON_TIPOLOGIA_CHANGED:
				String tipologiaUtente = (String) request.getAttribute("tipologiaUtente");
				resetPersonaGiuridica(profilo);
				//profilo=new Profilo();
				profilo.setTipologiaUtente(tipologiaUtente);
				break;
			case TX_BUTTON_RESET:
			case TX_BUTTON_NULL: 
			case TX_BUTTON_NUOVO: 
				resetParametri(request);
				profilo=new Profilo();
				
				
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
		
		AddProfiloInSession(session, profilo);
		MoveProfiloInRequest(request, profilo);
		
		request.setAttribute("do_command_name","seUsersAdd.do");
		request.setAttribute("codop",codop);
		session.setAttribute("Step", step);
		session.setAttribute("Stepold", stepOld == null ? session.getAttribute("Stepold") : stepOld);
		request.setAttribute("Step", session.getAttribute("Step"));
		request.setAttribute("Stepold", session.getAttribute("Stepold"));
		return null;
	}
}
