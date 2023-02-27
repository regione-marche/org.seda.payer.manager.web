package org.seda.payer.manager.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.util.ManagerKeys;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class DispatchFlowEC implements FlowManager {
	
	private static final long serialVersionUID = 1L;

	public void end(HttpServletRequest request) {		
	}

	public String process(HttpServletRequest request) throws FlowException {
		String action = ((request.getAttribute(ManagerKeys.REQUEST_ACTION_FLOW) == null) || 
				(request.getAttribute(ManagerKeys.REQUEST_ACTION_FLOW).equals(""))) ? 
						"init" : (String)request.getAttribute(ManagerKeys.REQUEST_ACTION_FLOW);
		if (request.getAttribute("download") == "Y") {
			action = "DownloadCsv";
		}
		//for pdf notifiche
		if (request.getAttribute("download") == "OK") {
			action = "download";
		}

		return action;
	}

	public void start(HttpServletRequest request) {
		
	}

}
