/**
 * 
 */
package org.seda.payer.manager.actions;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;

import com.seda.commons.properties.tree.PropertiesNodeException;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.compatibility.SystemVariable;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.action.HtmlAction;
import com.seda.j2ee5.maf.core.security.SignOnKeys;


/**
 * @author Seda
 *
 */
//inizio LP PG200060
//PG14001X_001 GG 25112014 - inizio
//public class SignOnAction extends HtmlAction {
public class SignOnAction extends BaseManagerAction {
//PG14001X_001 GG 25112014 - fine	
//fine LP PG200060

	private static final long serialVersionUID = 1L;
	/* (non-Javadoc)
	 * @see com.seda.j2ee5.maf.core.action.ActionService#service(javax.servlet.http.HttpServletRequest)
	 */
	public Object service(HttpServletRequest request) throws ActionException {
		
		/*HttpSession session=request.getSession();
		session.getId();*/
		//inizio LP PG200060
		//PG14001X_001 GG 25112014 - inizio
		
		//PG200300
		String template="";
		HttpSession session = request.getSession();
		template = getTemplateCurrentApplication(request, request.getSession())==null?"":getTemplateCurrentApplication(request, request.getSession());
		//inizio SB PG21XX04 
		session.setAttribute("template", template);
		PropertiesTree configuration = null;
//		SystemVariable sv = new SystemVariable();
//		
//		String rootPath=sv.getSystemVariableValue("MANAGER_CONFIG_ROOT");
//		sv=null;	 
//		if (rootPath!=null) {
//			// caricamento configurazioni esterne
//			try {
//				configuration = new PropertiesTree(rootPath);
//				
//				if (getTemplateCurrentApplication(request, request.getSession()).equals("regmarche") || getTemplateCurrentApplication(request, request.getSession()).equals("regmarchesvecc")) {
//					if(configuration.getProperty(PropertiesPath.flagFedera.format()) !=null)
//						request.getSession().setAttribute("flagFedera",configuration.getProperty(PropertiesPath.flagFedera.format()).trim());
//				}
//				
//				session.setAttribute(ManagerKeys.NASCONDI_CREDENZIALI, "N");
//				if(configuration.getProperty(PropertiesPath.nascondiCredenziali.format(template))!=null) {
//					session.setAttribute(ManagerKeys.NASCONDI_CREDENZIALI, configuration.getProperty(PropertiesPath.nascondiCredenziali.format(template)));
//				}
//				
//			} catch (PropertiesNodeException e) {
//				e.printStackTrace();
//			}
//		}
		
		configuration = (PropertiesTree) (request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		
		if (getTemplateCurrentApplication(request, request.getSession()).equals("regmarche") || getTemplateCurrentApplication(request, request.getSession()).equals("regmarchesvecc")) {
			if(configuration.getProperty(PropertiesPath.flagFedera.format()) !=null)
				request.getSession().setAttribute("flagFedera",configuration.getProperty(PropertiesPath.flagFedera.format()).trim());
		}
		
		session.setAttribute(ManagerKeys.NASCONDI_CREDENZIALI, "N");
		if(configuration.getProperty(PropertiesPath.nascondiCredenziali.format(template))!=null) {
			session.setAttribute(ManagerKeys.NASCONDI_CREDENZIALI, configuration.getProperty(PropertiesPath.nascondiCredenziali.format(template)));
		}
		//fine SB PG21XX04
		//FINE PG200300
		
		//LUCAP_28092021
		session.setAttribute("listaVisibileProfili", "N");
		if(configuration.getProperty(PropertiesPath.listaVisibileProfili.format(template))!=null) {
			session.setAttribute("listaVisibileProfili", configuration.getProperty(PropertiesPath.listaVisibileProfili.format(template)));
			String userKey = request.getParameter("profilo");
			session.setAttribute("userBeanInitProf", userKey);
		}		
		//FINE LUCAP_28092021

		//PG14001X_001 GG 25112014 - fine
		//fine LP PG200060
		/*
		 * Abilito la visualizzazione del link
		 * di reset password nell'header della pagina
		 * di login
		 */
		request.setAttribute("enableResetPswd",true);
		/*
		 * Il welcome message per gli utenti Federa viene visualizzato 
		 * solo quando l'utente ha richiesto esplicitamente il login.
		 * 
		 * Quando l'utente ha effettuato il logoff oppure si è verificato un errore
		 * il messaggio di welcome non viene visualizzato
		 */
		if (request.getAttribute("logoffMessage") == null) request.setAttribute("welcome_message", "Y");
		return null;
	}
	
	
	
}
