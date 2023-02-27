package org.seda.payer.manager.components.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.PropertiesPath;


import com.seda.j2ee5.maf.core.application.ApplicationsData;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;

/**
 * Servlet Filter implementation class UserSessionFilter
 */
public class TemplateFilter implements Filter {

    /**
     * Default constructor. 
     */
    public TemplateFilter() {
    }

    public void doFilter(ServletRequest servletrequest, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		HttpServletRequest request = (HttpServletRequest)servletrequest;
		HttpSession session = request.getSession(false);
		
		if (session != null && session.getAttribute(SignOnKeys.USER_BEAN) == null)
		{	
			UserBean userBean = new UserBean();
			
			setTemplateToUserBean(userBean, session, request);

			session.setAttribute(SignOnKeys.USER_BEAN, userBean);
		}
		
		chain.doFilter(request, response);
	}

    public static void setTemplateToUserBean(UserBean userBean, HttpSession session, HttpServletRequest request)
    {
    	//recupero il dns del chiamante
		//String url = request.getRequestURL().toString();
		
		ServletContext context = session.getServletContext();
		String servername = request.getServerName();
		
		String templateName = null;
		
		//recupero il name del template dal file di properties
		if (ManagerStarter.configuration != null)
		{
			templateName = ManagerStarter.configuration.getProperty(PropertiesPath.templateBase.format() + servername);
			
			if (templateName != null && !templateName.equals(""))
			{
				//setto il templateName nello userBean
				ApplicationsData applicationsData = (ApplicationsData)context.getAttribute(MAFAttributes.APPLICATIONS);
				for (String applicationName : applicationsData.getApplicationsKeySet())
					userBean.setTemplate(applicationName, templateName);
			}
			if (templateName != null && templateName.equals("trentrisc")){
				if (ManagerStarter.configuration.getProperty(PropertiesPath.scriptGoogle.format()) != null)
					session.setAttribute("googleAnalytics", ManagerStarter.configuration.getProperty(PropertiesPath.scriptGoogle.format()));
			}	
		}
    }
    
	public void init(FilterConfig arg0) throws ServletException {
		
	}

	public void destroy() {
		
	}

}
