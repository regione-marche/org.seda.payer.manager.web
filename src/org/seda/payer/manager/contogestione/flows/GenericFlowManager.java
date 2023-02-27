package org.seda.payer.manager.contogestione.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.configurazione.actions.BaseConfigurazioneAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class GenericFlowManager extends BaseConfigurazioneAction implements FlowManager {
     
	private static final long serialVersionUID = 1L;

	public String process(HttpServletRequest request) throws FlowException {		
		

		switch(getFiredButton(request))
		{
		
			case TX_BUTTON_RESET:
			case TX_BUTTON_NULL: 						
        	case TX_BUTTON_DEFAULT:
				return "default";
			case TX_BUTTON_CERCA:
				return "ricerca";
			case TX_BUTTON_AGGIUNGI:
				return "aggiungi";
			case TX_BUTTON_AGGIUNGI_END:
				return "aggiungiEnd";
			case TX_BUTTON_EDIT:
				return "edit";
			case TX_BUTTON_EDIT_END:
				return "editEnd";
			case TX_BUTTON_DELETE:
				return "delete";
			case TX_BUTTON_DELETE_END:
				return "deleteEnd";
			case TX_BUTTON_SOCIETA_CHANGED:
			case TX_BUTTON_UTENTE_CHANGED:	
			case TX_BUTTON_PROVINCIA_CHANGED:
			case TX_BUTTON_MODELLO_CHANGED:	
				return "combo";
			case TX_BUTTON_AVANTI:
				return "avanti";
			case TX_BUTTON_INDIETRO:
				request.setAttribute("tx_button_default", "A");
				return "ritorna";
			case TX_BUTTON_STEP1:
				return "mail";
			case TX_BUTTON_DOWNLOAD:
				return "download";
			default:
				break;
		}
	    				
		return "ricerca";
	}


	public void start(HttpServletRequest request) {
	}

	public void end(HttpServletRequest request) {
	}



}
