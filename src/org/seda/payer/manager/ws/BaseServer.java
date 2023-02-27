package org.seda.payer.manager.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;

import com.seda.commons.properties.tree.PropertiesTree;


public class BaseServer extends BaseManagerAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PropertiesTree configuration; 
	
	public final String DBSCHEMACODSOCIETA = "dbSchemaCodSocieta";
	
	public String getCodSocieta (HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		String dbSchemaCodSocieta = null;
		if (session != null && session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA) != null)
			dbSchemaCodSocieta = (String) session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		else
		{
			configuration=(PropertiesTree) session.getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
			dbSchemaCodSocieta=configuration.getProperty(PropertiesPath.societa.format(getTemplateCurrentApplication(request, session)));
			session.setAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA,dbSchemaCodSocieta);
		}
		
		return dbSchemaCodSocieta;
		
	}
	
	
	
	
}
