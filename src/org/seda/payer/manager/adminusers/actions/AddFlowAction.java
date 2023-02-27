package org.seda.payer.manager.adminusers.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

@SuppressWarnings("serial")
public class AddFlowAction extends BaseManagerAction implements FlowManager {
	private FiredButton firedButton;

	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		String result = null;
		switch(firedButton)
		{
			case TX_BUTTON_NUOVO:
				result =  "userAdd1";
				break;

			case TX_BUTTON_AGGIUNGI: 
				String addUserRetCode = (String) request.getAttribute("addUserRetCode");
				if (addUserRetCode != null && addUserRetCode.equals("00"))
				{
					result="ritorna";
					request.setAttribute("vista","adminusers_search");
				}
				else
					result = "userAdd2";
				break;
				
			case TX_BUTTON_AGGIUNGI_END:
			case TX_BUTTON_AGGIUNGI_OPERATORE:
				addUserRetCode = (String) request.getAttribute("addUserRetCode");
				if (addUserRetCode != null && addUserRetCode.equals("00"))
				{
					result="ritorna";
					request.setAttribute("vista","adminusers_search");
				}
				else
					result = "userAdd1";
				break;
				
			case TX_BUTTON_STEP1:
				result =  "userAdd1";
				break;
				
			case TX_BUTTON_PASSWORD_AUTOGENERATA_CHANGED:
			case TX_BUTTON_PROFILO_UTENTE_CHANGED:
			case TX_BUTTON_SCADENZA_CHANGED:
			case TX_BUTTON_SOCIETA_CHANGED:
			case TX_BUTTON_UTENTE_CHANGED:
			case TX_BUTTON_ADD:
			case TX_BUTTON_REMOVE:
			case TX_BUTTON_ENTE_CHANGED:	
				result =  "userAdd1";
				break;

			case TX_BUTTON_RESET:
			case TX_BUTTON_NULL: 
				String step = (String)request.getAttribute("Step");
				result =  (step.equals("1") ? "userAdd1" : "userAdd2");
				break;

			case TX_BUTTON_AVANTI:
				String esito = (String)request.getAttribute("inputFieldChecked");
				result = ((esito != null && esito.equals("OK")) ? "userAdd2" : "userAdd1");
				break;
				
			case TX_BUTTON_CERCA:
				result = "ritorna";
				break;

		}
/*		System.out.println("AddFlowAction ".concat(firedButton.toString()));
		System.out.println("");
*/		return result;
	}

	public void start(HttpServletRequest request) {
		/*
		 * Individuo la richiesta dell'utente
		 */
		firedButton = getFiredButton(request);
	}

}
