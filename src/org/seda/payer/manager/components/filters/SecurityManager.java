package org.seda.payer.manager.components.filters;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

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
import org.seda.payer.manager.components.security.MenuRequestParams;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.application.ApplicationData;
import com.seda.j2ee5.maf.core.application.ApplicationsData;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFUtil;
import com.seda.payer.pgec.webservice.commons.dati.GetLogRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetLogResponseType;

public class SecurityManager  implements Filter{

	private final String defaultApplication = "default";
	private ServletContext context = null;
	public void destroy() {
	}

	/**
	 * Questo filtro controlla che la request sia relativa ad una applicazione
	 * abilitata per l'utente corrente - In caso contrario la sessione viene chiusa 
	 * e all'utente viene riproposta la pagina di login.
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException 
	{
		ApplicationData ad = null;
		UserBean userBean = null;
		String currentApplication = null;
		HttpServletRequest hreq = (HttpServletRequest)request;
		HttpSession session = hreq.getSession();
		userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		currentApplication = (String)hreq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
		String sTemplate="";
		if (ManagerStarter.configuration != null) {
			System.out.println("ManagerStarter");
			sTemplate = ManagerStarter.configuration.getProperty(PropertiesPath.templateBase.format() + request.getServerName());	
		}
		//System.out.println("SecurityManager fired - " + hreq.getRequestURL());
		
		/*Controllo se la sessione è stata invalidata da un'altro login per la stessa utenza.
		 * Se la sessione corrente non è più valida, la distruggo
		 */
		if(sTemplate.equals("soris") && userBean!=null && userBean.getSession()!=null && userBean.getSession().length()>0) {
			GetLogResponseType res = WSCache.commonsServer.getLogBySessionId(new GetLogRequestType(userBean.getSession()), hreq);
			if(res!=null) {
				if(res.getStartSession()!=null && res.getEndSession()!=null && !res.getStartSession().equals(res.getEndSession())) {
					System.out.println("Sessione Invalidata da altro accesso");
					request.setAttribute("logoffMessage", "La tua sessione è stata disconnessa in quanto un altro operatore sta utilizzando le credenziali da te utilizzate");
					request.setAttribute(MAFAttributes.CURRENT_APPLICATION,"login");
					request.getRequestDispatcher("logoff.do").forward(request, response);
					return;
				}
				
			}
		}

		
		if(UserBean.isValid(userBean) && userBean.getApplicazioni() != null && currentApplication != null)
		{
			/*
			 * La sessione viene distrutta se l'applicazione corrente 
			 * è protetta e non è nella lista di quelle abilitate per l'utente
			 * ed è diversa dall'appicazione di default 
			 * 
			 */
			ApplicationsData applicationsData = (ApplicationsData) context.getAttribute(MAFAttributes.APPLICATIONS);
			ad = applicationsData.getApplication(currentApplication);

			//if(!userBean.getApplicazioni().contains(currentApplication) && !WSCache.listaApplicazioniAbilitate.contains(currentApplication))
			if(!userBean.getApplicazioni().contains(currentApplication) && ad.isProtected() && !currentApplication.equals(defaultApplication))
			{
				System.out.println(" SecurityManager: " + Messages.APPLICAZIONE_NON_ABILITATA.format(currentApplication,userBean.getUserName()));
				/*
				 * Viene distrutta la sessione correntre e ne viene generata una nuova.
				 * NOTA: è necessario che il secondo parametro valga "true" per annullare tutti
				 * gli attributi di quella precedente (in caso contrario mantiene anche il 
				 * vecchio UserBean)
				 * 
				 *MAFUtil.regenerateSession(hreq, true); 
				 */
				/*
				 * NOTA: La sessione adesso viene rigenerata nella
				 * SignOffAction
				 */
				/*
				 * Rimuovo gli attributi necessari alla costruzione del menu
				 * nel caso siano rimasti settati
				 */
				MenuRequestParams.clear(hreq);
				/*
				 * Faccio la redirect alla pagina di login senza fare "chain"
				 */
				request.setAttribute("logoffMessage", Messages.APPLICAZIONE_NON_ABILITATA.format(currentApplication,userBean.getUserName()));

				request.setAttribute(MAFAttributes.CURRENT_APPLICATION,"login");
				request.getRequestDispatcher("logoff.do").forward(request, response);
				return;
			}
		}
		chain.doFilter(request,response);
		return;        			
	}

	public void init(FilterConfig arg0) throws ServletException {
		context = arg0.getServletContext();
	}

}
