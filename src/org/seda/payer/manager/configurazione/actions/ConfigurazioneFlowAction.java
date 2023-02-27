package org.seda.payer.manager.configurazione.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

@SuppressWarnings("serial")
public class ConfigurazioneFlowAction extends BaseManagerAction implements FlowManager {
	private FiredButton firedButton;

	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		String result = null;
		String codop = (String)request.getAttribute("codop");
		switch(firedButton)
		{
			case TX_BUTTON_NULL:
				result=codop;
				break;
				
			case TX_BUTTON_RESET:
				result=codop;
				break;
				
			case TX_BUTTON_EDIT:
				String updateRetCode = (String) request.getAttribute("updateRetCode");
				if (updateRetCode != null && updateRetCode.equals("00"))
					result="ritorna";
				else
					result="edit";
				break;
				
			case TX_BUTTON_DELETE:
				result = "cancel";
				break;
				
			case TX_BUTTON_NUOVO:
				result="add";
				break;

			case TX_BUTTON_CERCA:
				result = "search";
				break;
				
			case TX_BUTTON_AGGIUNGI:
				String insertRetCode = (String) request.getAttribute("insertRetCode");
				if (insertRetCode != null && insertRetCode.equals("00"))
					result="ritorna";
				else
					result="add";
				break;
				
			case TX_BUTTON_INDIETRO:
				result="ritorna";
				break;
				
			case TX_BUTTON_DOWNLOAD:
				result="download";
				break;
				
			case TX_BUTTON_AGGIORNA: //PG110260
				result=codop;
				break;
				
			case TX_BUTTON_PROVINCIA_TITOLARE_CHANGED:
				result=codop;
			case TX_BUTTON_PROVINCIA_GESTORE_CHANGED:
				result=codop;
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
