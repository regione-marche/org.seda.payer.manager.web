/**
 * 
 */
package org.seda.payer.manager.login.bolzano.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.filters.TemplateFilter;
import org.seda.payer.manager.components.security.UserBean;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.action.HtmlAction;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFUtil;

/**
 * @author Seda Lab
 *
 */
public class SignOffAction extends HtmlAction {

	private static final long serialVersionUID = 1L;

	
	/* (non-Javadoc)
	 * @see com.seda.j2ee5.maf.core.action.ActionService#service(javax.servlet.http.HttpServletRequest)
	 */
	public Object service(HttpServletRequest request) throws ActionException {
		
		MAFUtil.regenerateSession(request, true);
		HttpSession session = request.getSession();
		
		//ricreo lo userbean per settare il template
		UserBean userBean = new UserBean();
		TemplateFilter.setTemplateToUserBean(userBean, session, request);
		session.setAttribute(SignOnKeys.USER_BEAN, userBean);
		/*
		 * Visualizzo il messaggio sulla pagina di login: se il messaggio è nullo
		 * si tratta di un logoff effettuato dall'utente
		 */
		String logoffMessage = (String)request.getAttribute("logoffMessage");
		request.setAttribute("message", logoffMessage);
		
		return null;
	}

}
