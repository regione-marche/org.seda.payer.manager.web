package org.seda.payer.manager.login.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

@SuppressWarnings("serial")
public class ResetPswdFlowAction extends BaseManagerAction implements FlowManager{
	private FiredButton firedButton;
	
	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest arg0) throws FlowException {
		String result = null;
		switch(firedButton)
		{
			case TX_BUTTON_NULL:
			case TX_BUTTON_EDIT:
			case TX_BUTTON_REINVIAPUK:
				result =  "resetPswd";
				break;

			case TX_BUTTON_LOGIN:
				result =  "login";
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
