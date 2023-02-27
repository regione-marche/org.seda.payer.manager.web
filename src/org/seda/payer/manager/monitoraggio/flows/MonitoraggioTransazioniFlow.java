package org.seda.payer.manager.monitoraggio.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;


public class MonitoraggioTransazioniFlow extends BaseManagerAction implements FlowManager {

	private static final long serialVersionUID = 1L;
	private FiredButton firedButton;
	
	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		switch(firedButton) {
				case TX_BUTTON_CERCA:
				case TX_BUTTON_CONFERMA_RICONCILIAZIONE:
					return "ricerca";
				case TX_BUTTON_RESET:
					return "ricerca";
				case TX_BUTTON_SOCIETA_CHANGED:
					return "ricerca";
				case TX_BUTTON_PROVINCIA_CHANGED:
					return "ricerca";
				case TX_BUTTON_ENTE_CHANGED:
					return "ricerca";	
				case TX_BUTTON_NULL: 
					return "ricerca";
				case TX_BUTTON_DOWNLOAD:
					if (request.getAttribute("download") == "N")
						return "ricerca";
					else
						return "download";
				case TX_BUTTON_STAMPA:
					return "stampa";
				case TX_BUTTON_DELETE:
					return "elimina";
				case TX_BUTTON_ALLINEA:
					return "allinea";	
				case TX_BUTTON_SEND_PDF_MAV:
					return "sendpdfmav";
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