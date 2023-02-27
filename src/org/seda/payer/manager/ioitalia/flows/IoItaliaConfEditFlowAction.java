package org.seda.payer.manager.ioitalia.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.ioitalia.actions.IoItaliaBaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class IoItaliaConfEditFlowAction extends IoItaliaBaseManagerAction implements FlowManager {

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
			String key = (String)request.getAttribute("tx_messageKey");
			if (key != null && key.equalsIgnoreCase("La chiave primaria è già stata utilizzata, riprova.")) {
				result = "Search";
			} else if(request.getAttribute("tx_messageKey") != null && request.getAttribute("tx_messageKey").equals("Errore: Configurazione gi&agrave; esistente.")) {
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
		
		
	}

}
