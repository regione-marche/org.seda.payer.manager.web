package org.seda.payer.manager.adminusers.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

@SuppressWarnings("serial")
public class SearchFlowAction extends BaseManagerAction implements FlowManager {
	private FiredButton firedButton;

	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		String result = null;
		switch(firedButton)
		{
			case TX_BUTTON_NUOVO: 
				result = "userAdd";
				break;

			case TX_BUTTON_RESET:
			case TX_BUTTON_CERCA:
				result = "userSearch";
				break;
			/*
			 * Serve quando la pagina di ricerca viene richiamata dal menu	
			 */
			default:
				result = "userSearch";
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
