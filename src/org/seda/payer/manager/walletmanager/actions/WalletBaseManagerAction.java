package org.seda.payer.manager.walletmanager.actions;
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



public class WalletBaseManagerAction extends BaseManagerAction  {
	private static final long serialVersionUID = 1L;
	
	private DataSource walletDataSource;
	private DataSource sepaDataSource;
	private String dbSchemaCodSocieta;
	private String walletDbSchema;
	private String sepaDbSchema;
	protected String dataSourceName = "";
	protected DataSource getWalletDataSource(){return this.walletDataSource;}
	protected String getWalletDbSchema(){return this.walletDbSchema;}
	protected DataSource getSepaDataSource(){return this.sepaDataSource;}
	protected String getSepaDbSchema(){return this.sepaDbSchema;}
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		dbSchemaCodSocieta = (String)session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		
		PropertiesTree configuration; 
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		this.dataSourceName =  configuration.getProperty(PropertiesPath.dataSourceWallet.format(dbSchemaCodSocieta));
		String dataSourceNameSepa =  configuration.getProperty(PropertiesPath.dataSourceSepa.format(dbSchemaCodSocieta));
		this.walletDbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(dbSchemaCodSocieta));
		this.sepaDbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaSepa.format(dbSchemaCodSocieta));
		
		System.out.println("walletDbSchema = " + this.walletDbSchema);
		System.out.println("sepaDbSchema = " + this.sepaDbSchema);
		
		try {
			System.out.println("istanzia  walletDataSource ");
			this.walletDataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(dataSourceName));
			System.out.println("istanzia  walletDataSource ");
			this.sepaDataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(dataSourceNameSepa));
		} catch (ServiceLocatorException e) {
			System.out.println("ServiceLocator error " + e.getMessage());
			throw new ActionException("ServiceLocator error " + e.getMessage(),e);
		}
		return null;
	}
	public void init(ServletContext context) throws ActionException{
		super.init(context);
	}
}
