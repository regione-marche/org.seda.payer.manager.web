package org.seda.payer.manager.components.filters.bolzano;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;


import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;



/**
 * Servlet Filter implementation class AuthenticationFilter
 */
public class AuthenticationFilter implements Filter {


	private FilterConfig filterConfig;
	private String serviceUrl;
	private boolean isAuthenticationRequest=false;
	

	public static Logger logger = Logger.getLogger(AuthenticationFilter.class);

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filter) throws IOException, ServletException {
			HttpServletRequest request=(HttpServletRequest) servletRequest;	
			HttpSession session=((HttpServletRequest) servletRequest).getSession(false);	
			String tipoAutenticazione=null;
			if(session!=null){
				UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
				/*
				 * Se è presente uno UserBean valido si tratta di una richiesta relativa 
				 * ad sessione già autenticata e trattata precedentemente da questo filtro
				 * e faccio il chain
				 */
				if(isValid(userBean))
				{
					filter.doFilter(servletRequest,servletResponse);
					return;
				}
				String autenticazione=(String) request.getAttribute("autenticazione");
				if(autenticazione!=null&&autenticazione.equals("aut_federata")){
					tipoAutenticazione="F";
				}
				if(tipoAutenticazione==null){
					tipoAutenticazione= (String) session.getAttribute("pagonetTipoAutenticazione");
				}
				if(tipoAutenticazione!=null && tipoAutenticazione.equalsIgnoreCase("F")){
					bolzanoAuthentication((HttpServletRequest)servletRequest,servletResponse);
					isAuthenticationRequest=true;
					return;
				}else{
					filter.doFilter(servletRequest,servletResponse);
				}

			}else{
				filter.doFilter(servletRequest,servletResponse);
			}
         
		
	}



	private void bolzanoAuthentication(HttpServletRequest request,ServletResponse servletResponse) {
		try {
			HttpServletResponse response=(HttpServletResponse) servletResponse;
			//pulisco l'autenticazione dall sessione
			request.getSession().setAttribute("pagonetTipoAutenticazione","F"); 
			serviceUrl=filterConfig.getInitParameter("bolzanoServiceApp");
			PropertiesTree configuration=(PropertiesTree)request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
			String template=getTemplateCurrentApplication(request, request.getSession());
			serviceUrl=configuration.getProperty(PropertiesPath.bolzanoURL.format(template));
			if(serviceUrl!=null){
				response.sendRedirect(serviceUrl);
				
			}
		} catch (IOException e) {
			logger.error("Si sono verificati errori nella redirect per l'autenticazione di bolzano : "+serviceUrl);
			e.printStackTrace();
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig=filterConfig;

	}

	public static boolean isValid(UserBean userBean)
	{
		if(userBean == null) 
			return false;
		return (userBean.getUserName() != null && userBean.getCodiceFiscale() != null); //&& userBean.getUserToken() != null);
	}


	
	public String getTemplateCurrentApplication(HttpServletRequest request, HttpSession session)
	{
		MAFRequest mafReq = new MAFRequest(request);
		String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
		if (applicationName != null)
		{
			UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
			if (userBean != null)
			{
				String templateName = userBean.getTemplate(applicationName);
				if (templateName != null && !templateName.equals(""))
					return templateName;
				else
					return "default";
			}
		}
		else
		{
			//recupero il name del template dal file di properties
			if (ManagerStarter.configuration != null)
			{
				String templateName = ManagerStarter.configuration.getProperty(PropertiesPath.templateBase.format() + request.getServerName());

				if (templateName != null && !templateName.equals(""))
					return templateName;
			}
		}
		return "default";
	}

}
