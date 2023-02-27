package org.seda.payer.manager.entrate.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.entrate.actions.BaseEntrateAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;


public class DettaglioDocumentoFlow extends BaseEntrateAction implements FlowManager {

	private static final long serialVersionUID = 1L;
	private FiredButton firedButton;
	
	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		switch(firedButton) {
				case TX_BUTTON_CERCA:
					return request.getParameter("pagPrec");
				case TX_BUTTON_CERCA_EXP:
					return "dettaglioDocumento";
		}
		
		return null;
		
	}

	public void start(HttpServletRequest request) {
		/*
		 * Individuo la richiesta dell'utente
		 */
		firedButton = getFiredButton(request);
		
	}
}