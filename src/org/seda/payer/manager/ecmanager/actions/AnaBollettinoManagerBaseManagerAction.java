package org.seda.payer.manager.ecmanager.actions;

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

public class AnaBollettinoManagerBaseManagerAction extends BaseManagerAction  {
	private static final long serialVersionUID = 1L;
	
	private DataSource anagraficaDataSource;
	private DataSource sepaDataSource;
	private String dbSchemaCodSocieta;
	private String anagraficaDbSchema;
	private String sepaDbSchema;
	protected String dataSourceName;
	protected DataSource getAnagraficaDataSource(){return this.anagraficaDataSource;}
	protected String getAnagraficaDbSchema(){return this.anagraficaDbSchema;}
	protected DataSource getSepaDataSource(){return this.sepaDataSource;}
	protected String getSepaDbSchema(){return this.sepaDbSchema;}
	
	//PG170280 CT --
	private String emailWelcomekitFrom = "";
	private String emailWelcomekitSubject = "";
	private String emailWelcomekitBody = "";
	private String welcomekitTemplatePath = "";
	private String welcomekitOutputPath = "";
	protected String getEmailWelcomekitFrom(){return this.emailWelcomekitFrom;}
	protected String getEmailWelcomekitSubject(){return this.emailWelcomekitSubject;}
	protected String getEmailWelcomekitBody(){return this.emailWelcomekitBody;}
	protected String getWelcomekitTemplatePath(){return this.welcomekitTemplatePath;}
	protected String getWelcomekitOutputPath(){return this.welcomekitOutputPath;}
	

	//-- PG170280
	
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		System.out.println("AnaBollettinoManagerBaseManagerAction - sono dentro il service");
		
		super.service(request);
		HttpSession session = request.getSession();
		
		dbSchemaCodSocieta = (String)session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		
		System.out.println("AnaBollettinoManagerBaseManagerAction - DBSCHEMA_CODSOCIETA : " + dbSchemaCodSocieta);
		
		PropertiesTree configuration; 
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		System.out.println("AnaBollettinoManagerBaseManagerAction - configuration : " + configuration!=null?configuration.getProterties():"null");
		this.dataSourceName =  configuration.getProperty(PropertiesPath.dataSourceWallet.format(dbSchemaCodSocieta));
		System.out.println("AnaBollettinoManagerBaseManagerAction - dataSourceName : " + dataSourceName);
		String dataSourceNameSepa =  configuration.getProperty(PropertiesPath.dataSourceSepa.format(dbSchemaCodSocieta));
		System.out.println("AnaBollettinoManagerBaseManagerAction - dataSourceNameSepa : " + dataSourceNameSepa);
		this.anagraficaDbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(dbSchemaCodSocieta));
		System.out.println("AnaBollettinoManagerBaseManagerAction - this.anagraficaDbSchema : " + this.anagraficaDbSchema);
		this.sepaDbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaSepa.format(dbSchemaCodSocieta));
		System.out.println("AnaBollettinoManagerBaseManagerAction - this.sepaDbSchema : " + this.sepaDbSchema);
		
		try {	
			this.anagraficaDataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(dataSourceName));
			this.sepaDataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(dataSourceNameSepa));
		} catch (ServiceLocatorException e) {
			System.out.println("AnaBollettinoManagerBaseManagerAction - Sono dentro il catch del ServiceLocator. getLocalizedMessage : " + e.getLocalizedMessage() );
			System.out.println("AnaBollettinoManagerBaseManagerAction - Sono dentro il catch del ServiceLocator. getMessage : " + e.getMessage() );			
			throw new ActionException("ServiceLocator error " + e.getMessage(),e);
		}
		
		if(configuration.getProperty(PropertiesPath.emailWelcomekitFrom.format(dbSchemaCodSocieta)) != null)
			this.emailWelcomekitFrom = configuration.getProperty(PropertiesPath.emailWelcomekitFrom.format(dbSchemaCodSocieta));
		if(configuration.getProperty(PropertiesPath.emailWelcomekitSubject.format(dbSchemaCodSocieta)) != null)
			this.emailWelcomekitSubject = configuration.getProperty(PropertiesPath.emailWelcomekitSubject.format(dbSchemaCodSocieta));
		if(configuration.getProperty(PropertiesPath.emailWelcomekitBody.format(dbSchemaCodSocieta)) != null)
			this.emailWelcomekitBody = configuration.getProperty(PropertiesPath.emailWelcomekitBody.format(dbSchemaCodSocieta));
		if(configuration.getProperty(PropertiesPath.welcomekitTemplatePath.format(dbSchemaCodSocieta)) != null)
			this.welcomekitTemplatePath = configuration.getProperty(PropertiesPath.welcomekitTemplatePath.format(dbSchemaCodSocieta));
		if(configuration.getProperty(PropertiesPath.welcomekitOutputPath.format(dbSchemaCodSocieta)) != null)
			this.welcomekitOutputPath = configuration.getProperty(PropertiesPath.welcomekitOutputPath.format(dbSchemaCodSocieta));
		
		
		System.out.println("AnaBollettinoManagerBaseManagerAction - this.emailWelcomekitFrom : " + this.emailWelcomekitFrom);
		System.out.println("AnaBollettinoManagerBaseManagerAction - this.emailWelcomekitSubject : " + this.emailWelcomekitSubject);
		System.out.println("AnaBollettinoManagerBaseManagerAction - this.emailWelcomekitBody : " + this.emailWelcomekitBody);
		System.out.println("AnaBollettinoManagerBaseManagerAction - this.welcomekitTemplatePath : " + this.welcomekitTemplatePath);
		System.out.println("AnaBollettinoManagerBaseManagerAction - this.welcomekitOutputPath : " + this.welcomekitOutputPath);
		
		
		if(configuration.getProperty(PropertiesPath.notifiche_Url.format().concat("."+ dbSchemaCodSocieta))!=null)
			context.setAttribute(ManagerKeys.NOTIFICHE_URL, new String(configuration.getProperty(PropertiesPath.notifiche_Url.format().concat("."+ dbSchemaCodSocieta))));
		
		if(configuration.getProperty(PropertiesPath.notifiche_Bearer.format().concat("."+ dbSchemaCodSocieta))!=null)
			context.setAttribute(ManagerKeys.NOTIFICHE_BEARER, new String(configuration.getProperty(PropertiesPath.notifiche_Bearer.format().concat("."+ dbSchemaCodSocieta))));
		
		return null;
	}
	public void init(ServletContext context) throws ActionException{
		super.init(context);
	}
}
