package org.seda.payer.manager.adminusers.lepida.actions;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PayerCommand;
import org.seda.payer.manager.util.PayerCommandException;
import org.seda.payer.manager.ws.WSCache;

import com.seda.payer.pgec.webservice.adminusers.dati.InserisciUtenteRequestType;
import com.seda.payer.pgec.webservice.adminusers.dati.InserisciUtenteResponseType;
import com.seda.payer.pgec.webservice.adminusers.dati.PyUserType;
import com.seda.payer.pgec.webservice.adminusers.dati.ResponseType;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class UserAddAction2 extends BaseAdminusersAction implements PayerCommand{

	private static String codop = "add";
	InserisciUtenteRequestType in = null;
	InserisciUtenteResponseType out = null;
	ResponseType response = null;
	
	public Object execute(HttpServletRequest request) throws PayerCommandException{
	
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
		setApplsDisabled(request, session);
		/*
		 *  Ricavo i parametri della request
		 */
		getForm2Parameters(request,session);
		/*
		 *  Setto i flag
		 */
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) setForm2Flags(request);
		/*
		 * Compio le azioni corrispondenti alla richiesta dell'utente
		 */
		switch(firedButton)
		{

			/*
			 * Torno indietro allo STEP 1
			 */
			case TX_BUTTON_STEP1:
				/*
				 * Metto i parametri dello STEP2 in sessione
				 */
				putAdd2ParametersInSession(request,session);
				session.setAttribute("Form2ParametersInSession", "Y");
				break;

				/*
				 * La richiesta arriva dallo STEP1
				 * 
				 * Se trovo in sessione uno degli oggetti relativi allo STEP2
				 * non è la prima volta che lo STEP2 viene eseguito:
				 * recupero dalla sessione gli attributi
				 */
			case TX_BUTTON_AVANTI:
				if (session.getAttribute("Form2ParametersInSession") != null)
				{
					session.removeAttribute("Form2ParametersInSession");
					getAdd2ParametersFromSession(request,session);
					setForm2Flags(request);
				}
				break;

			case TX_BUTTON_AGGIUNGI: 
				inserisciUtente(request, session, "var2_form", false);
			break;
			
			case TX_BUTTON_RESET:
				resetParametri(request);
				break;
				
			case TX_BUTTON_NULL: 
				break;
				
				/*
				 * Torno al form di ricerca	
				 */
				case TX_BUTTON_CERCA:
					request.setAttribute("vista","adminusers_search");
					break;
}
		/*
		 * Setto l'action della form di "adminusers_var2.jsp",
		 * il tipo di operazione e lo step
		 */
		request.setAttribute("do_command_name","userAdd2.do");
		request.setAttribute("codop",codop);
		request.setAttribute("Step", "2");
		return null;
	}
	
	public void inserisciUtente(HttpServletRequest request, HttpSession session, String sFormName, boolean bIsOperatore)
	{
		String retCode = null;
		String retMessage = null;
		try {
	
			Calendar now = Calendar.getInstance();
			getAdd1ParametersFromSession(request,session);
			PyUserType pyUser = getPyUserForLepida(request,session,now);
			in = new InserisciUtenteRequestType();
			in.setPyUser(pyUser);
			if (bIsOperatore)
			{
				listaApplicazioni = new ArrayList<String>();
				listaApplicazioni.add("posfisico");
			}
			in.setApplicazioni(getArrayFromList(listaApplicazioni));
			out = WSCache.adminUsersServer.inserisciUtente(in, request);
			response = out.getResponse();
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
				{
					if (retCode.equals("03"))
					{
						if (pyUser.getUserProfile().equals("AMMI") || pyUser.getUserProfile().equals("PYCO") || pyUser.getUserProfile().equals("OPER"))
							setFormMessage(sFormName, Messages.PROFILO_PRESENTE_NOSCOUTEENT.format(pyUser.getUserName(), pyUser.getUserProfile()), request);
						else
							setFormMessage(sFormName, Messages.PROFILO_PRESENTE.format(pyUser.getUserName(), pyUser.getUserProfile()), request);
					}
					else
						setFormMessage(sFormName, retCode + " - " + retMessage, request);
				}
			}
			else
				setFormMessage(sFormName, Messages.GENERIC_ERROR.format(), request);
		} catch (FaultType e) {
			setFormMessage(sFormName, "Errore: " + e.getLocalizedMessage(), request);
			e.printStackTrace();
		} catch (RemoteException e) {
			setFormMessage(sFormName, "Errore: " + e.getMessage(), request);
			e.printStackTrace();
		}
	}

}
