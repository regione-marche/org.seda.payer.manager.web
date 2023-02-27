package org.seda.payer.manager.monitoraggionn.actions;

import javax.servlet.http.HttpServletRequest;
import com.seda.j2ee5.maf.core.action.ActionException;

public class MonitoraggioNnDettaglioAction extends BaseMonitoraggioNnAction {

	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		
		String chiaveTransazione = request.getParameter("chiave_transazione_hidden");
		request.setAttribute("chiave_transazione_hidden", chiaveTransazione);
		
		resetFormMessage(request);
		
		switch(getFiredButton(request)) {

			case TX_BUTTON_NULL: 
			case TX_BUTTON_CERCA:
				
				recuperaDettaglioMIN(chiaveTransazione, request);
				recuperaListaMPS(chiaveTransazione, request);
				break;
		}
	
		return null;
	}

}
