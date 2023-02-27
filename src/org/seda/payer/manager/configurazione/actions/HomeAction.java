package org.seda.payer.manager.configurazione.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.action.HtmlAction;

public class HomeAction extends HtmlAction {

	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {

		HttpSession session = request.getSession();
		String sUrl = request.getRequestURL().toString();
		return null;
	}
	
}
