package org.seda.payer.manager.configurazione.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.configurazione.actions.BaseAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class EntiInvioRTFlowAction extends BaseAction implements FlowManager{

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
		
		case TX_BUTTON_DELETE:
		case TX_BUTTON_ADD:
		case TX_BUTTON_INDIETRO:
		case TX_BUTTON_CERCA:
		case TX_BUTTON_SOCIETA_CHANGED:
		case TX_BUTTON_NULL:
			result="search";
			break;
		case TX_BUTTON_CANCEL:
			result="cancel";
			break;
		case TX_BUTTON_RESET:
			result="reset";
			break;
		case TX_BUTTON_AGGIUNGI:
			result="add";
			break;
		default:
			break;
			
		}
		return result;
	}

	@Override
	public void end(HttpServletRequest request) {
		
	}

}
