package org.seda.payer.manager.monitoraggionn.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.monitoraggionn.actions.BaseMonitoraggioNnAction;

import com.seda.j2ee5.maf.components.validation.ValidationContext;
import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;


public class MonitoraggioNnNodoFlow extends BaseMonitoraggioNnAction implements FlowManager {

	private static final long serialVersionUID = 1L;
	
	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		if (request.getAttribute("action") != null)
		{
			String sAction = (String)request.getAttribute("action");
			/*if (sAction.equals("ricerca"))
				return "ricerca";*/
			if (sAction.equals("dettaglio"))
			{
				if (request.getAttribute("idtrans") != null && !request.getAttribute("idtrans").equals(""))
				{
					String chiaveTransazione = (String)request.getAttribute("idtrans");
					recuperaListaMPS(chiaveTransazione, request);
					String viewStateId = replyAttributes(false, request, ValidationContext.getInstance().getValidationMessage(), "listaMPS", "listaMPS.pageInfo");		
					return viewStateId;
				}
			}
		}
		
		//return null;

		String viewStateId = replyAttributes(false, request, ValidationContext.getInstance().getValidationMessage());		
		return viewStateId;
		
	}

	public void start(HttpServletRequest request) {
		
	}
}