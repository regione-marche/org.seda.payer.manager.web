package org.seda.payer.manager.modello3config.actions;
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


public class Modello3BaseManagerAction extends BaseManagerAction  {
	private static final long serialVersionUID = 1L;
	
	private DataSource configurazioneModello3DataSource;
	private String dbSchemaCodSocieta;
	private String configurazioneModello3DbSchema;
	protected DataSource getConfigurazioneModello3DataSource(){return this.configurazioneModello3DataSource;}
	protected String getConfigurazioneModello3DbSchema(){return this.configurazioneModello3DbSchema;}
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		dbSchemaCodSocieta = (String)session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		
		PropertiesTree configuration; 
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		//PG180080 TODO Verificare se va bene usare dataSourceWallet e dataSourceSchemaWallet oppure è meglio creare nuove voci dataSourceConfigurazioneModello3 che però si connette sempre al payerDataSource
		String configurazioneModello3DataSource =  configuration.getProperty(PropertiesPath.dataSourceWallet.format(dbSchemaCodSocieta));
		
		System.out.println("configurazioneModello3DataSource =" + configurazioneModello3DataSource);
		this.configurazioneModello3DbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(dbSchemaCodSocieta));
		System.out.println("configurazioneModello3DbSchema =" + configurazioneModello3DbSchema);
		
		try {	
			this.configurazioneModello3DataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(configurazioneModello3DataSource));
			System.out.println("connessione eseguita");
			
		} catch (ServiceLocatorException e) {
			//inizio LP PG21X007
			//throw new ActionException("ServiceLocator error " + e.getMessage(),e);
			System.out.println("connessione per configurazioneModello3 non eseguita (1)!");
			try {	
				this.configurazioneModello3DataSource = ServiceLocator.getInstance().getDataSource("java:/".concat(configurazioneModello3DataSource));
				System.out.println("connessione eseguita");
			} catch (ServiceLocatorException e1) {
				System.out.println("connessione per configurazioneModello3 non eseguita (2)!");
			//fine LP PG21X007
				throw new ActionException("ServiceLocator error " + e.getMessage(),e1);
			//inizio LP PG21X007
			}
			//fine LP PG21X007
		}
		return null;
	}
	public void init(ServletContext context) throws ActionException{
		super.init(context);
	}
}



