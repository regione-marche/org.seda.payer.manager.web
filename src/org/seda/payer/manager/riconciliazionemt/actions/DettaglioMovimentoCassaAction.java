package org.seda.payer.manager.riconciliazionemt.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.seda.j2ee5.maf.core.action.ActionException;


public class DettaglioMovimentoCassaAction extends BaseRiconciliazioneTesoreriaAction{

	private static final long serialVersionUID = 1L;
	
	
	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		super.service(request);
		
		setGdcParameters(request, session);
		dettaglioMovimentoCassa(request);
		
		return null;
	}

}
