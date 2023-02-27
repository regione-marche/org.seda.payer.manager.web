package org.seda.payer.manager.login.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;
import com.seda.j2ee5.maf.util.MAFRequest;

@SuppressWarnings("serial")
public class SceltaAutenticazioneFlowAction extends BaseManagerAction implements FlowManager{
	private FiredButton firedButton;
	
	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		String result = null;
		HttpSession session=request.getSession();
		switch(firedButton)
		{
		
			case RB_PROPRIETARIA:
				result =  "proprietaria";
				session.setAttribute("pagonetTipoAutenticazione", "P");
				break;

			case RB_FEDERATA:
				result =  "federata";
				session.setAttribute("pagonetTipoAutenticazione", "F");
				
				MAFRequest mafReq = new MAFRequest(request);
				String urlRedirectDefault = mafReq.getCurrentContext() + "/default/defaultprot.do";
				
				request.setAttribute("urlRedirectDefault",urlRedirectDefault);
				break;
				
		}
		return result;
	}

	public void start(HttpServletRequest request) {
		/*
		 * Individuo la richiesta dell'utente
		 */
		firedButton = getFiredButton(request);
	}

}
