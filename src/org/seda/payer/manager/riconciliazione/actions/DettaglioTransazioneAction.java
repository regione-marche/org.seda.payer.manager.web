package org.seda.payer.manager.riconciliazione.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.Field;

import com.seda.j2ee5.maf.core.action.ActionException;



public class DettaglioTransazioneAction extends BaseRiconciliazioneAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);

		HttpSession session = request.getSession();
		
		loadDettaglioTransazione(request, session);
		
		request.setAttribute(Field.TX_MESSAGE.format(), getMessage());
		request.setAttribute(Field.TX_ERROR_MESSAGE.format(), getErrorMessage());
		return null;
	}
}
