package org.seda.payer.manager.mercatimonitoraggio.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;


public class MonitoraggioMercatiFlow extends BaseManagerAction implements FlowManager {

	private static final long serialVersionUID = 1L;
	private FiredButton firedButton;
	
	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		switch(firedButton) {
			case TX_BUTTON_CERCA:
			case TX_BUTTON_RESET:	
			case TX_BUTTON_NULL: 
				return "ricerca";
			case TX_BUTTON_DOWNLOAD:
				if (request.getAttribute("download") == "N")
					return "ricerca";
				else
					return "download";
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