package org.seda.payer.manager.adminusers.actions;


import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

@SuppressWarnings("serial")
public class GruppoAgenziaFlowAction extends BaseManagerAction implements FlowManager {

	private FiredButton firedButton;

	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		String result = null;
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
		case TX_BUTTON_CERCA: 
			result = "search";
			break;
		case TX_BUTTON_EDIT:
			result = "edit"; 
			break;
		case TX_BUTTON_AGGIUNGI:
			result = "ritorna"; 
			break;
		case TX_BUTTON_NULL: 
			result =  "edit";
			break;
		case TX_BUTTON_NUOVO: 
			result =  "add";
			break;
			
		case TX_BUTTON_DOWNLOAD:
			result =  "DownloadCsv";
			break;
		
		case TX_BUTTON_CANCEL:
			result =  "cancella";
			request.setAttribute("tx_button_cancel",null);
			break;
		case TX_BUTTON_DELETE:
			result =  "ritorna";
			break;
		
		case TX_BUTTON_INDIETRO:
			result =  "ritorna";
			break;
		
		case TX_BUTTON_EDIT_END:
			result =  "search";
			break;
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
