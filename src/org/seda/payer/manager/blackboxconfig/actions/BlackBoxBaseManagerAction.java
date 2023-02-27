package org.seda.payer.manager.blackboxconfig.actions;
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


public class BlackBoxBaseManagerAction extends BaseManagerAction  {
	private static final long serialVersionUID = 1L;
	
	private DataSource blackBoxDataSource;
	private String dbSchemaCodSocieta;
	private String blackBoxDbSchema;
	protected DataSource getBlackBoxDataSource(){return this.blackBoxDataSource;}
	protected String getBlackBoxDbSchema(){return this.blackBoxDbSchema;}
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		System.out.println("configurazioen balckbox");
		HttpSession session = request.getSession();
		dbSchemaCodSocieta = (String)session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		
		PropertiesTree configuration; 
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		String blackBoxDataSource =  configuration.getProperty(PropertiesPath.dataSourceBlackBox.format(dbSchemaCodSocieta));
		this.blackBoxDbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaBlackBox.format(dbSchemaCodSocieta));
		System.out.println("this.blackBoxDbSchema = " + this.blackBoxDbSchema);
		System.out.println("blackBoxDataSource = " + blackBoxDataSource);
		try {	
			this.blackBoxDataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(blackBoxDataSource));
			
		} catch (ServiceLocatorException e) {
			//inizio LP PG200370
			System.out.println("configurazioen blackbox errore accesso DataSource (java:comp/env/" + blackBoxDataSource +"): " + e.getMessage());
			try {	
				this.blackBoxDataSource = ServiceLocator.getInstance().getDataSource("java:/".concat(blackBoxDataSource));
			} catch (ServiceLocatorException e1) {
				System.out.println("configurazioen blackbox errore accesso DataSource (java:/" + blackBoxDataSource +"): " + e1.getMessage());
			//fine LP PG200370
			throw new ActionException("ServiceLocator error " + e1.getMessage(), e1);
			//inizio LP PG200370
			}
			//fine LP PG200370
		}
		return null;
	}
	public void init(ServletContext context) throws ActionException{
		super.init(context);
	}
}



