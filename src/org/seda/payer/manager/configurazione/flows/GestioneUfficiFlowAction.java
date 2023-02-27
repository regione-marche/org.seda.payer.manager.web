package org.seda.payer.manager.configurazione.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class GestioneUfficiFlowAction extends BaseManagerAction implements FlowManager {

	private static final long serialVersionUID = 1L;
	private FiredButton firedButton;

	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		String result = null;
		switch(firedButton) {
			case TX_BUTTON_CERCA:
				result = "Search";
				break;
			case TX_BUTTON_ADD:
				result = "Add";
				break;
			case TX_BUTTON_AGGIUNGI:
				result = "Search";
				break;
			case TX_BUTTON_EDIT:
				result = "Search";
				break;
			case TX_BUTTON_RESET: 
				result = "Search";
				break;
			case TX_BUTTON_DELETE:
				result = "Search";
				break;
			case TX_BUTTON_INDIETRO:
				result =  "Search";
				break;
			case TX_BUTTON_NULL: {
				String codOp = request.getAttribute("codop")!=null?(String)request.getAttribute("codop"):"";
				result =  "Search";
				if(codOp.trim().equals("edit"))
					result = "Edit";
				if(codOp.trim().equals("delete"))
					result = "Cancel";
				}; break;
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
