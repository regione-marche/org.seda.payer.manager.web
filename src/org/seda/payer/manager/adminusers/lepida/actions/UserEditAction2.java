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

import com.seda.payer.pgec.webservice.adminusers.dati.AggiornaUtenteRequestType;
import com.seda.payer.pgec.webservice.adminusers.dati.AggiornaUtenteResponseType;
import com.seda.payer.pgec.webservice.adminusers.dati.PyUserType;
import com.seda.payer.pgec.webservice.adminusers.dati.ResponseType;
import com.seda.payer.pgec.webservice.adminusers.dati.SelezionaUtenteRequestType;
import com.seda.payer.pgec.webservice.adminusers.dati.SelezionaUtenteResponseType;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class UserEditAction2 extends BaseAdminusersAction  implements PayerCommand{

	private static String codop = "edit";

	AggiornaUtenteRequestType aggiornaReq = null;
	AggiornaUtenteResponseType aggiornaRes = null;
	SelezionaUtenteRequestType selezionaReq = null;
	SelezionaUtenteResponseType selezionaRes = null;
	ResponseType aggiornaResponse = null;
	ResponseType selezionaResponse = null;
	PyUserType pyUser = null;
	
	public Object execute(HttpServletRequest request) throws PayerCommandException {
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
		//
		if (!firedButton.equals(FiredButton.TX_BUTTON_NULL) )
		{
			/*
			 * Recupero i parametri dello STEP 1 dalla sessione
			 */
			getAdd1ParametersFromSession(request,session);
			/*
			 *  Ricavo i parametri della request
			 */
			getForm2Parameters(request,session);
			setForm2Flags(request);
		}
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
				break;
			/*
			 * LA RICHIESTA ARRIVA DALLO STEP 1
			 * 
			 * 1) Se trovo in sessione l'oggetto "firstStep2" 
			 *    è la prima volta che eseguo lo STEP2: 
			 *    utilizzo l'oggetto e poi lo cancello dalla sessione
			 * 2) Se l'oggetto "firstStep2" è nullo
			 *    non è la prima volta che eseguo lo STEP2:
			 *    utilizzo i dati recuperati dalla form nello STEP2 
			 *    nel passaggio precedente e messi in sessione
			 */
			case TX_BUTTON_AVANTI:
				request.setAttribute("inputFieldChecked", "OK");
				selezionaRes = (SelezionaUtenteResponseType)session.getAttribute("selezionaRes"); 
				if (session.getAttribute("firstStep2") != null) 
				{
					session.removeAttribute("firstStep2");
					if (selezionaRes != null) checkProfileAndSetForm2AttributesFromWs(request, selezionaRes);
				}
				else
				{
					getAdd2ParametersFromSession(request,session);
					//setForm2Flags(request);
					checkProfileAndSetForm2Flags(request,selezionaRes);
				}
				
				break;

			case TX_BUTTON_EDIT: 
				modificaUtente(request, session, "var2_form", false);
				
			break;
			
			case TX_BUTTON_RESET:
				selezionaRes = (SelezionaUtenteResponseType)session.getAttribute("selezionaRes"); 
				if (selezionaRes != null) checkProfileAndSetForm2AttributesFromWs(request, selezionaRes);
				break;
				
			case TX_BUTTON_NULL: 
				break;
		}
		request.setAttribute("Step", "2");
		/*
		 * Setto gli attributi relativi ai campi disabilitati
		 * ricavati dai campi hidden
		 */
		request.setAttribute("tx_username", username);
		request.setAttribute("tx_userprofile", userProfile);
		request.setAttribute("tx_societa", codiceSocieta);
		request.setAttribute("tx_utente", codiceUtente);
		request.setAttribute("tx_ente", codiceEnte);
		request.setAttribute("tx_nome", nome);
		request.setAttribute("tx_cognome", cognome);
		request.setAttribute("tx_codiceFiscale", codiceFiscale);
		/*
		 * Setto l'action della form di "adminusers_var.jsp"
		 * ed il tipo di operazione
		 */
		request.setAttribute("do_command_name","userEdit2.do");
		request.setAttribute("codop",codop);
		return null;
	}
	
	public void modificaUtente(HttpServletRequest request, HttpSession session, String sFormName, boolean bOperatore)
	{
		String aggiornaRetCode = null;
		String aggiornaRetMessage = null;
		
		if (bOperatore)
		{
			//ho cliccato salva dallo step 1 per il profilo PYCO			
			getAdd1ParametersFromSession(request,session);
		}
		/*
		 * effettuo la modifica
		 */
		try {
			Calendar now = Calendar.getInstance();
			aggiornaReq = new AggiornaUtenteRequestType();
			PyUserType pyUser = getPyUser(request,session,now);
			aggiornaReq.setPyUser(pyUser);
			if (bOperatore)
			{
				listaApplicazioni = new ArrayList<String>();
				listaApplicazioni.add("posfisico");
			}
			if (listaApplicazioni != null && listaApplicazioni.size() > 0)
				aggiornaReq.setApplicazioni(getArrayFromList(listaApplicazioni));
			else
				aggiornaReq.setApplicazioni(new String[0]);
			aggiornaRes = WSCache.adminUsersServer.aggiornaUtente(aggiornaReq, request);
			aggiornaResponse = aggiornaRes.getResponse();
			if (aggiornaResponse != null)
			{
				aggiornaRetCode = aggiornaResponse.getRetCode();
				aggiornaRetMessage = aggiornaResponse.getRetMessage();
				request.setAttribute("editUserRetCode", aggiornaRetCode);
				if (aggiornaRetCode.equals("00"))
				{
					session.setAttribute("editUserRetMessage", aggiornaRetMessage);
					session.removeAttribute("selezionaRes");
				}
				else
				{
					if (aggiornaRetCode.equals("03"))
					{
						if (pyUser.getUserProfile().equals("AMMI") || pyUser.getUserProfile().equals("PYCO") || pyUser.getUserProfile().equals("OPER"))
							setFormMessage(sFormName, Messages.PROFILO_PRESENTE_NOSCOUTEENT.format(pyUser.getUserName(), pyUser.getUserProfile()), request);
						else
							setFormMessage(sFormName, Messages.PROFILO_PRESENTE.format(pyUser.getUserName(), pyUser.getUserProfile()), request);
					}
					else
						setFormMessage(sFormName, aggiornaRetCode + " - " + aggiornaRetMessage, request);
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
