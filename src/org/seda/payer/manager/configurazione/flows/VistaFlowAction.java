package org.seda.payer.manager.configurazione.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

@SuppressWarnings("serial")
public class VistaFlowAction  extends BaseManagerAction implements FlowManager {

	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException 
	{
		String vista = (String)request.getAttribute("vista");
		return vista;
	}

	public void start(HttpServletRequest arg0) {
	}

}
