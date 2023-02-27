package org.seda.payer.manager.ioitalia.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class IoItaliaFlowAction extends BaseManagerAction implements FlowManager{

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
			
			case TX_BUTTON_EDIT_END:
			case TX_BUTTON_CERCA: 
			case TX_BUTTON_RESET:
			case TX_BUTTON_SOCIETA_CHANGED:
			case TX_BUTTON_PROVINCIA_CHANGED:
			case TX_BUTTON_ENTE_CHANGED:
			case TX_BUTTON_TIPOLOGIA_CHANGED:
			case TX_BUTTON_NULL:
			case TX_BUTTON_AGGIUNGI:
			case TX_BUTTON_INDIETRO:
			case TX_BUTTON_ADD:
			case TX_BUTTON_RINOTIFICA:
				request.getSession().setAttribute("ioitaliacheck", null);
				result =  "Search";
				break;

				
			case TX_BUTTON_NUOVO:
				request.getSession().setAttribute("ioitaliacheck", "ioitalia");
				result = "Nuovo";
				break;
			
			default:
				break;
		}
		
		return result;
	}

	@Override
	public void end(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}
	
}
