package org.seda.payer.manager.adminusers.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

@SuppressWarnings("serial")
public class EditFlowAction extends BaseManagerAction implements FlowManager {
	private FiredButton firedButton;

	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		String result = null;
		switch(firedButton)
		{
			case TX_BUTTON_EDIT:
				String editUserRetCode = (String) request.getAttribute("editUserRetCode");
				if (editUserRetCode != null && editUserRetCode.equals("00"))
				{
					result="ritorna";
					request.setAttribute("vista","adminusers_search");
				}
				else
					result =  "userEdit2";
				break;
			
			case TX_BUTTON_EDIT_END:
			case TX_BUTTON_EDIT_OPERATORE:	
				editUserRetCode = (String) request.getAttribute("editUserRetCode");
				if (editUserRetCode != null && editUserRetCode.equals("00"))
				{
					result="ritorna";
					request.setAttribute("vista","adminusers_search");
				}
				else
					result =  "userEdit1";
				break;

			case TX_BUTTON_STEP1:
				result =  "userEdit1";
				break;

			case TX_BUTTON_SCADENZA_CHANGED:
				result =  "userEdit1";
				break;

			case TX_BUTTON_RESET:
			case TX_BUTTON_NULL: 
				String step = (String)request.getAttribute("Step");
				result =  (step.equals("1") ? "userEdit1" : "userEdit2");
				break;

			case TX_BUTTON_AVANTI:
				String esito = (String)request.getAttribute("inputFieldChecked");
				result = ((esito != null && esito.equals("OK")) ? "userEdit2" : "userEdit1");
				break;
				
			case TX_BUTTON_CERCA:
				result = "ritorna";
				break;

			case TX_BUTTON_PROFILO_UTENTE_CHANGED:
			case TX_BUTTON_SOCIETA_CHANGED:
			case TX_BUTTON_UTENTE_CHANGED:
			case TX_BUTTON_ENTE_CHANGED:
			case TX_BUTTON_ADD:
			case TX_BUTTON_REMOVE:
				result =  "userEdit1";
				break;
				
		}
		return result;
	}

	public void start(HttpServletRequest request) {
		/*
		 * Individuo la richiesta dell'utente
		 */
		firedButton = getFiredButton(request);
	}

}
