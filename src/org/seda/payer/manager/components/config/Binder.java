package org.seda.payer.manager.components.config;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.RuleSetManager;

import com.seda.j2ee5.maf.core.security.UserBeanApplicationBinder;
import com.seda.j2ee5.maf.core.security.UserBeanSupport;

@SuppressWarnings("serial")
public class Binder  implements UserBeanApplicationBinder{

	public String bind(UserBeanSupport userBeanSupport) {
		String ruolo = null;
		if(userBeanSupport instanceof UserBean)
		{
			UserBean userBean = (UserBean)userBeanSupport;
			ruolo = userBean.getUserProfile();	
		}
		else
		{
			ruolo = userBeanSupport.getProfile();
		}
	
		//Questo parametro abilita la modalità debug in "ricercaFlussi.java"
		userBeanSupport.setRegistry("debug_mode","N");
		return ruolo;
	}

	public void end() {
	}

	public void init(HttpServletRequest req) 
	{
		/*
		 * Il metodo "RuleSetManager.putGlobalRuleSetMapInContext(context)"
		 * salva nel contesto le regole definite nel "maf-action.xml" in modo
		 * che possano essere utilizzate nelle pagine jsp dal validator
		 */
		ServletContext context = req.getSession().getServletContext();
		if (context.getAttribute(ManagerKeys.GLOBAL_RULE_SET_MAP) == null)
			RuleSetManager.putGlobalRuleSetMapInContext(context);
	}

}
