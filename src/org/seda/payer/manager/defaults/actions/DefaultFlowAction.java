package org.seda.payer.manager.defaults.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.ManagerKeys;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

public class DefaultFlowAction extends BaseManagerAction implements FlowManager{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		HttpSession session = request.getSession();
		if (session.getAttribute(ManagerKeys.NUMERO_PROFILI) != null)
			return (String)session.getAttribute(ManagerKeys.NUMERO_PROFILI);
		
		return null;
	}

	public void start(HttpServletRequest arg0) {
	}

}
