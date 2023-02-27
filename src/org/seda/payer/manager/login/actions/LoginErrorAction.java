package org.seda.payer.manager.login.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.action.HtmlAction;

public class LoginErrorAction extends HtmlAction {

	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		
		HttpSession session = request.getSession();
		if(session.getAttribute("message")!=null) {
			request.setAttribute("messageError", session.getAttribute("message"));
			session.removeAttribute("message");
		}
		

		return null;
	}

}
