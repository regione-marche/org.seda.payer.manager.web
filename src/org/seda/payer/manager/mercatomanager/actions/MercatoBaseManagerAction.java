package org.seda.payer.manager.mercatomanager.actions;
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



public class MercatoBaseManagerAction extends BaseManagerAction  {
	private static final long serialVersionUID = 1L;
	
	private DataSource mercatoDataSource;
	private DataSource sepaDataSource;
	private String dbSchemaCodSocieta;
	private String mercatoDbSchema;
	private String sepaDbSchema;
	protected DataSource getMercatoDataSource(){return this.mercatoDataSource;}
	protected String getMercatoDbSchema(){return this.mercatoDbSchema;}
	protected DataSource getSepaDataSource(){return this.sepaDataSource;}
	protected String getSepaDbSchema(){return this.sepaDbSchema;}
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		dbSchemaCodSocieta = (String)session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		
		PropertiesTree configuration; 
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		String dataSourceName =  configuration.getProperty(PropertiesPath.dataSourceWallet.format(dbSchemaCodSocieta));
		String dataSourceNameSepa =  configuration.getProperty(PropertiesPath.dataSourceSepa.format(dbSchemaCodSocieta));
		this.mercatoDbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(dbSchemaCodSocieta));
		this.sepaDbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaSepa.format(dbSchemaCodSocieta));
		
		System.out.println("mercatoDbSchema = " + this.mercatoDbSchema);
		System.out.println("sepaDbSchema = " + this.sepaDbSchema);
		
		try {
			System.out.println("istanzia  mercatoDataSource ");
			this.mercatoDataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(dataSourceName));
			System.out.println("istanzia  mercatoDataSource ");
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
