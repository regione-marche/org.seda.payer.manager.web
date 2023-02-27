package org.seda.payer.manager.blackboxconfig.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;


import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class BlackBoxPosFlowAction extends BaseManagerAction implements FlowManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FiredButton firedButton;

	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		String result = null;
		switch(firedButton) {
		
			case TX_BUTTON_CERCA: 
			case TX_BUTTON_RESET:
			case TX_BUTTON_DELETE:
			case TX_BUTTON_INDIETRO:
			case TX_BUTTON_EDIT:
				result = "Search";
				break;
				
			case TX_BUTTON_NULL: {
				result = "Search";
				String codOp = request.getParameter("codOp")!=null?((String)request.getParameter("codOp")).trim():"";
				if(codOp.equals("edit")) {
					result = "Edit";
				}
				if(codOp.equals("delete")) {
					result = "Delete";
				}
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

