package org.seda.payer.manager.ioitalia.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class RitornaDueFlowAction extends BaseManagerAction implements FlowManager{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void start(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String process(HttpServletRequest request) throws FlowException {
		// TODO Auto-generated method stub
		return replyAttributes(true, request, "validator.message");
	}

	@Override
	public void end(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
	}


}
