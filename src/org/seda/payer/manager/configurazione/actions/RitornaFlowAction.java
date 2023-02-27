package org.seda.payer.manager.configurazione.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

@SuppressWarnings("serial")
public class RitornaFlowAction  extends BaseManagerAction implements FlowManager {

	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException 
	{
		String viewStateId = replyAttributes(true, request);
		return viewStateId;
	}

	public void start(HttpServletRequest arg0) {
	}

}
