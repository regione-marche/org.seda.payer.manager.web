package org.seda.payer.manager.riconciliazione.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class RiconciliazioneTransazioniFlow extends BaseManagerAction implements FlowManager {

	private static final long serialVersionUID = 1L;
	private FiredButton firedButton;
	
	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		switch(firedButton) 
		{
				case TX_BUTTON_STAMPA:
					return "stampa";
				default:
					return "ricerca";
		}
				
	}

	public void start(HttpServletRequest request) {
		/*
		 * Individuo la richiesta dell'utente
		 */
		firedButton = getFiredButton(request);		
	}
}