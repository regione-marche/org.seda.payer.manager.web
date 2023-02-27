package org.seda.payer.manager.adminusers.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

@SuppressWarnings("serial")
public class SeEnableFlowAction extends BaseManagerAction implements FlowManager{
	private FiredButton firedButton;

	public void end(HttpServletRequest request) {
	}

	public String process(HttpServletRequest arg0) throws FlowException {
		String result = null;
		switch(firedButton)
		{
			case TX_BUTTON_NULL:
			case TX_BUTTON_AVANTI:
				result =  "enable";
				break;

			case TX_BUTTON_INDIETRO:
				result =  "ritorna";
				break;
			case TX_BUTTON_CERCA:
				result =  "ritorna";
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
