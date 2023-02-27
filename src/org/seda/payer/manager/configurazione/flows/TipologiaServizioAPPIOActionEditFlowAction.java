package org.seda.payer.manager.configurazione.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.configurazione.actions.TipologiaServiziAPPIOBaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class TipologiaServizioAPPIOActionEditFlowAction extends TipologiaServiziAPPIOBaseManagerAction implements FlowManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FiredButton firedButton;
	
	@Override
	public void start(HttpServletRequest request) {
		firedButton = getFiredButton(request);
	}

	@Override
	public String process(HttpServletRequest request) throws FlowException {
		String result = null;
		switch(firedButton) {
		
		case TX_BUTTON_NULL:
		case TX_BUTTON_EDIT_END:
		case TX_BUTTON_SOCIETA_CHANGED:
		case TX_BUTTON_RESET:
		case TX_BUTTON_ADD:
			result = "Search";
			break;
		
		case TX_BUTTON_EDIT:
		case TX_BUTTON_AGGIUNGI:
			String key = (String)request.getAttribute("tx_error_message");
			if(key != null && key.equals("Errore: Tipologia gi&agrave; esistente.")) {
				result = "Search";
			}else {
				result = "ritorna";				
			}
			break;
		case TX_BUTTON_INDIETRO:
			result ="ritorna";
			break;
		default:
			break;
			
		}
		return result;
	}

	@Override
	public void end(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}

}
