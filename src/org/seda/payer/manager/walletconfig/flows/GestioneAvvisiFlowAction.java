package org.seda.payer.manager.walletconfig.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class GestioneAvvisiFlowAction extends BaseManagerAction
		implements FlowManager {
    
	
	private static final long serialVersionUID = 1L;
	private FiredButton firedButton;
	public void end(HttpServletRequest request) {
		// TODO Auto-generated method stub

	}

	public String process(HttpServletRequest request) throws FlowException {
		String result = null;
		switch(firedButton){
	    case TX_BUTTON_CERCA:
	    result = "Search";
	    break;
	    case TX_BUTTON_RESET:
	    result = "Search";
	    break;
	    case TX_BUTTON_NULL:
		result = "Search";
		break;
	    case TX_BUTTON_ENTE_CHANGED:
	    result = "Search";
		break;
	    case TX_BUTTON_ADD:
	    result = "add";
		break;
		}
		return result;
	}

	public void start(HttpServletRequest request) {
		// TODO Auto-generated method stub
		firedButton = getFiredButton(request);
	}

}
