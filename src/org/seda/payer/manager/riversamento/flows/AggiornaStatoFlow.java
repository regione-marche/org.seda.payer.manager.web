package org.seda.payer.manager.riversamento.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;


public class AggiornaStatoFlow extends BaseManagerAction implements FlowManager {

	private static final long serialVersionUID = 1L;
	private FiredButton firedButton;
	
	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException 
	{
		firedButton = getFiredButton(request);
		
		switch(firedButton) 
		{
				case TX_BUTTON_SET_FLAG_CBI:
				case TX_BUTTON_AVANZA_STATO:
					return "alertRiv";
				case TX_BUTTON_CERCA:
					return "ricerca";
		}
		
		return null;
	}

	public void start(HttpServletRequest request) {
		/*
		 * Individuo la richiesta dell'utente
		 */
		
	}
}