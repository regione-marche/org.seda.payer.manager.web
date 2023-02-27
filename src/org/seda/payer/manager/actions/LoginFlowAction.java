package org.seda.payer.manager.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class LoginFlowAction extends BaseManagerAction implements FlowManager{

	private static final long serialVersionUID = 1L;

	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		if (request.getAttribute("urlPortale") != null && !request.getAttribute("urlPortale").equals(""))
			return "redirect";
		else
			return "login";
	}

	public void start(HttpServletRequest arg0) {
	}

}

