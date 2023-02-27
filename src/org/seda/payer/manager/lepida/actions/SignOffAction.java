/**
 * 
 */
package org.seda.payer.manager.lepida.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFUtil;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.filters.TemplateFilter;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.PayerCommand;

/**
 * @author Seda Lab
 *
 */
public class SignOffAction extends BaseManagerAction implements PayerCommand{

	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see com.seda.j2ee5.maf.core.action.ActionService#service(javax.servlet.http.HttpServletRequest)
	 */
	public Object service(HttpServletRequest request) throws ActionException {
		return null;
	}

	public Object execute(HttpServletRequest request) {
		MAFUtil.regenerateSession(request, true);
		HttpSession session = request.getSession();
		/*
		 * Ricreo lo userbean per settare il template
		 */
		UserBean userBean = new UserBean();
		TemplateFilter.setTemplateToUserBean(userBean, session, request);
		session.setAttribute(SignOnKeys.USER_BEAN, userBean);
		/*
		 * Visualizzo il messaggio sulla pagina di login: se il messaggio è nullo
		 * si tratta di un logoff effettuato dall'utente
		 */
		String logoffMessage = (String)request.getAttribute("logoffMessage");
		setFormMessage("lepida_login", (logoffMessage == null ? "Logout effettuato correttamente" : logoffMessage), request);
		/*
		 * Setto comunque l'attributo "logoffMessage" per evitare che venga 
		 * visualizzato il welcome message per gli utenti Federa
		 */
		if (logoffMessage == null)
		{
			request.setAttribute("logoffMessage", "Logout effettuato correttamente");
		}
		
		return null;
	}

}
