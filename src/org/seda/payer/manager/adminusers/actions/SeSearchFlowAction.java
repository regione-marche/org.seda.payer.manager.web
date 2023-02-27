package org.seda.payer.manager.adminusers.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

@SuppressWarnings("serial")
public class SeSearchFlowAction extends BaseManagerAction implements FlowManager{

	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		String result = null;
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
			case TX_BUTTON_NUOVO: 
				result = "add";
				break;

			case TX_BUTTON_RESET:
			case TX_BUTTON_CERCA:
				result = "search";
				break;
			
			case TX_BUTTON_DOWNLOAD:
				result = "download";
				break;
				
			/*
			 * Serve quando la pagina di ricerca viene richiamata dal menu	
			 */
			default:
				result = "search";
				break;
		}
		return result;
	}

	public void start(HttpServletRequest arg0) {
	}

}
