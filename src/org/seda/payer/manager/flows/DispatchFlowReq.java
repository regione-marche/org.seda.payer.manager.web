package org.seda.payer.manager.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.DispatchHtmlAction;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class DispatchFlowReq extends DispatchHtmlAction implements FlowManager {

	private static final long serialVersionUID = 1L;
	private FiredButton firedButton;
	
	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		
		String action = (String)request.getAttribute("action");
			
		firedButton = getFiredButton(request);
		
		if(firedButton.equals(FiredButton.TX_BUTTON_NUOVO))
			action = "add";
		if(firedButton.equals(FiredButton.TX_BUTTON_INDIETRO))
			action = "search";
		if(firedButton.equals(FiredButton.TX_BUTTON_PROVINCIA_CHANGED))
			action = "cambiaProvincia";
		
		action = ((action == null || action.length() == 0) ? "search" : action);
		request.setAttribute("action", action);
		return action;
	}
	
	
	public void start(HttpServletRequest request) {
		
	}

	@Override
	public Object index(HttpServletRequest request) throws ActionException {
		return null;
	}
	
	
}