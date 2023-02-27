package org.seda.payer.manager.configurazione.actions;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;

public class TipologiaServiziAPPIOBaseManagerAction extends BaseManagerAction {

	private static final long serialVersionUID = 1L;
	
	protected String payerDbSchema = null;
	protected DataSource payerDataSource = null;
	protected int rowsPerPage = 0;
	protected int pageNumber = 0;
	protected String order = "";
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		
		PropertiesTree configuration = (PropertiesTree) (request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		String dbSchemaCodSocieta = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		String dataSourceName =  configuration.getProperty(PropertiesPath.dataSourceWallet.format(dbSchemaCodSocieta));
		this.payerDbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(dbSchemaCodSocieta));
		try {	
			this.payerDataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(dataSourceName));
		} catch (ServiceLocatorException e) {
			throw new ActionException("ServiceLocator error " + e.getMessage(),e);
		}

		this.rowsPerPage = ((String) request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String) request.getAttribute("rowsPerPage"));
		this.pageNumber = ((String) request.getAttribute("pageNumber") == null) || (((String) request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String) request.getAttribute("pageNumber"));
		this.order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
				
		return null;
	}
	
	public void init(ServletContext context) throws ActionException{
		super.init(context);
	}
	
	
}
