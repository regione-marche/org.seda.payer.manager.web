package org.seda.payer.manager.mercatoconfig.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

@SuppressWarnings("serial")
public class CompensoEditFlowAction extends BaseManagerAction implements FlowManager {

	private FiredButton firedButton;

	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		String result = null;
		switch(firedButton)
		{
		
		case TX_BUTTON_CERCA: 
			result = "ritorna";
			break;
		case TX_BUTTON_RESET:
			result = "Search";
			break;
		case TX_BUTTON_EDIT:
			result = "Search"; 
			break;
		case TX_BUTTON_AGGIUNGI:
			//result = "ritorna"; 
			result = "Search";
			break;
		case TX_BUTTON_SOCIETA_CHANGED:
		case TX_BUTTON_PROVINCIA_CHANGED:
		case TX_BUTTON_ENTE_CHANGED:
		case TX_BUTTON_NULL: 
			result =  "Search";
			break;
		case TX_BUTTON_ADD: 
			result =  "Search";
			break;
		case TX_BUTTON_STAMPA:
			result =  "StampaPdf";
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
