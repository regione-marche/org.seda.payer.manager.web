package org.seda.payer.manager.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.DispatchHtmlAction;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class DispatchFlow extends DispatchHtmlAction implements FlowManager {

	private static final long serialVersionUID = 1L;
	private FiredButton firedButton;
	
	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		
		String action = request.getParameter("action");
			
		firedButton = getFiredButton(request);
		
		if(firedButton.equals(FiredButton.TX_BUTTON_NUOVO))
			action = "add";
		if(firedButton.equals(FiredButton.TX_BUTTON_INDIETRO))
			action = "search";
		if(firedButton.equals(FiredButton.TX_BUTTON_WIS))
			action = "avantiWIS";
		if(firedButton.equals(FiredButton.TX_BUTTON_PROVINCIA_CHANGED))
			action = "cambiaProvincia";
		if(firedButton.equals(FiredButton.TX_BUTTON_DOWNLOAD)) {
			if (request.getAttribute("download") == "N")
				return "search";
			else
				return "download";
		}
			
		
		action = (action == null ? "search" : action);
		request.setAttribute("action", action);
		return action;
	}
	
	
	public void start(HttpServletRequest request) {
		//firedButton = getFiredButton(request);
		
	}

	@Override
	public Object index(HttpServletRequest request) throws ActionException {
		//firedButton = getFiredButton(request);
		return null;
	}
	
	/*protected FiredButton getFiredButton(HttpServletRequest request) {

		if (request.getAttribute("tx_button_indietro") != null)
			return FiredButton.TX_BUTTON_INDIETRO;
		
		if (request.getAttribute("tx_button_nuovo") != null)
			return FiredButton.TX_BUTTON_NUOVO;
		
		if (request.getAttribute("tx_button_changed") != null)
			return FiredButton.TX_BUTTON_CHANGED;

		return FiredButton.TX_BUTTON_NULL;
	}*/
}