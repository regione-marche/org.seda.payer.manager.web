package org.seda.payer.manager.riconciliazionemt.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

/**
 * @author fbarnocchi
 *
 */
public class RitornaFlowManager extends BaseManagerAction implements FlowManager {
     
	private static final long serialVersionUID = 1L;

	public String process(HttpServletRequest request) throws FlowException {

		String viewStateId = replyAttributes(true, request, "validator.message");
		return viewStateId;
	}

	public void end(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		
	}

	public void start(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		
	}
	
	}