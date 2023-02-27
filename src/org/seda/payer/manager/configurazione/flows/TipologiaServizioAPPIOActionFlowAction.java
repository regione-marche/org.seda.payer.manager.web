package org.seda.payer.manager.configurazione.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class TipologiaServizioAPPIOActionFlowAction extends BaseManagerAction implements FlowManager {

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
		
			case TX_BUTTON_CERCA: 
			case TX_BUTTON_RESET:
			case TX_BUTTON_NULL:
			case TX_BUTTON_AGGIUNGI:
			case TX_BUTTON_SOCIETA_CHANGED:
			case TX_BUTTON_EDIT:
			case TX_BUTTON_INDIETRO:
			case TX_BUTTON_DELETE:
				request.getSession().setAttribute("tipologiaServizioAPPIOcheck", null);
				result =  "Search";
				break;
				
			case TX_BUTTON_CANCEL:
				result= "Cancella";
				break;
			
			case TX_BUTTON_ADD: 
				result = "Nuovo"; 
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
