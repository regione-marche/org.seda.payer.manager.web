package org.seda.payer.manager.monitoraggiocruss.actions;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;


public class BaseMonitoraggioCrussAction extends BaseManagerAction  {
	private static final long serialVersionUID = 1L;
	
	private DataSource monitoraggioCrussDataSource;
	private String dbSchemaCodSocieta;
	private String monitoraggioCrussDbSchema;
	protected DataSource getMonitoraggioCrussDataSource(){return this.monitoraggioCrussDataSource;}
	protected String getMonitoraggioCrussDbSchema(){return this.monitoraggioCrussDbSchema;}
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		dbSchemaCodSocieta = (String)session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		
		PropertiesTree configuration; 
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		String monitoraggioCrussDataSource =  configuration.getProperty(PropertiesPath.dataSourceWallet.format(dbSchemaCodSocieta)); 
		this.monitoraggioCrussDbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(dbSchemaCodSocieta));
		
		try {	
			this.monitoraggioCrussDataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(monitoraggioCrussDataSource));
			
		} catch (ServiceLocatorException e) {
			throw new ActionException("ServiceLocator error " + e.getMessage(),e);
		}
		return null;
	}
	public void init(ServletContext context) throws ActionException{
		super.init(context);
	}
}



