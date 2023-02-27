package org.seda.payer.manager.integrazioneflussi.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class IntegrazioneFlussiFlowAction extends BaseManagerAction implements FlowManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8791284491114335047L;

	private FiredButton firedButton;

	@Override
	public void end(HttpServletRequest request) {

	}

	@Override
	public String process(HttpServletRequest request) throws FlowException {

		int idFlusso = 0;
		try {
			idFlusso = Integer.parseInt(request.getParameter("idFlusso"));
		} catch (NumberFormatException e) {	}
		
		String idDominio = request.getParameter("idDominio");
		String codiceEnte = request.getParameter("codiceEnte");
		String codiceIuv = request.getParameter("codiceIuv");
		
		String result = null;
		
		switch (firedButton) {
		case TX_BUTTON_CERCA:
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
		default:
			if (idFlusso == 0
				&& (idDominio == null || idDominio.trim().length() == 0)
				&& (codiceEnte == null || codiceEnte.trim().length() == 0)
				&& (codiceIuv == null || codiceIuv.trim().length() == 0)) {
				
				result = "Search";
			}
			
			if (idFlusso > 0) {
				
				result = "SearchDettagli";
			}
			
			if (idDominio != null && idDominio.trim().length() > 0
				&& codiceEnte != null && codiceEnte.trim().length() > 0
				&& codiceIuv != null && codiceIuv.trim().length() > 0) {
				
				result = "SearchDettaglio";
			}
			break;
		case TX_BUTTON_INDIETRO:
			if (idFlusso > 0
				&& (idDominio == null || idDominio.trim().length() == 0)
				&& (codiceEnte == null || codiceEnte.trim().length() == 0)
				&& (codiceIuv == null || codiceIuv.trim().length() == 0)) {
				
				result = "Search";
			}
			
			if (idDominio != null && idDominio.trim().length() > 0
				&& codiceEnte != null && codiceEnte.trim().length() > 0
				&& codiceIuv != null && codiceIuv.trim().length() > 0) {
				
				result = "SearchDettagli";
			}
			break;
		}

		return result;
	}

	@Override
	public void start(HttpServletRequest request) {

		firedButton = getFiredButton(request);
	}

}
