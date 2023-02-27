package org.seda.payer.manager.monitoraggiocup.actions;

import javax.servlet.http.HttpServletRequest;
import com.seda.j2ee5.maf.core.action.ActionException;

public class MonitoraggioCupDettaglioAction extends BaseMonitoraggioCupAction {

	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		
		String chiaveTransazione = request.getParameter("chiave_transazione_hidden");
		request.setAttribute("chiave_transazione_hidden", chiaveTransazione);
		String codiceFiscale = request.getParameter("codiceFiscale_hidden");
		request.setAttribute("codiceFiscale_hidden", codiceFiscale);
		String codicePagamento = request.getParameter("codicePagamento_hidden");
		request.setAttribute("codicePagamento_hidden", codicePagamento);


		resetFormMessage(request);
		
		switch(getFiredButton(request)) {

			case TX_BUTTON_NULL: 
			case TX_BUTTON_CERCA:
				
			recuperaDettaglioMIC(chiaveTransazione,codiceFiscale, codicePagamento, request);
				recuperaListaMCS(chiaveTransazione,codiceFiscale, codicePagamento, request);
				break;
		}
	
		return null;
	}

}
