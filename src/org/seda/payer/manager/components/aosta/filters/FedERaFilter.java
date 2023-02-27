package org.seda.payer.manager.components.aosta.filters;

import it.cefriel.icar.inf3.ICARConstants;
import it.cefriel.icar.inf3.web.beans.AuthenticationSessionBean;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.filters.TemplateFilter;
import org.seda.payer.manager.components.security.Profilo;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.defaults.actions.SceltaProfiloAction;
import org.seda.payer.manager.login.actions.SignOnSupport;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;

import com.seda.j2ee5.maf.core.application.ApplicationBinderFactory;
import com.seda.j2ee5.maf.core.application.ApplicationsData;
import com.seda.j2ee5.maf.core.security.SecurityData;
import com.seda.j2ee5.maf.core.security.SignOnException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.security.webservice.dati.DatiAnagPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiContattiPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiGeneraliPersonaFisicaDelegato;
import com.seda.security.webservice.dati.UtentePIVA;
import com.seda.security.webservice.dati.UtentePIVATipologiaUtente;


/**
 * Questo filtro completa le attività necessarie all'autenticazione Federata  
 * intervenendo dopo "INF-3 Authentication Filter":
 * - 1) Se è presente uno UserBean valido fa semplicemente il chain della request 
 * - 2) Se non è presente uno UserBean valido ma la richiesta è stata autenticata all'esterno 
 *      (AuthBean valido) recupera i dati dell'utente, costruisce uno UserBean valido e lo mette in sessione 
 * - 3) In caso di errore invia ad una pagina predefinita
 * 
 * 											NOTA IMPORTANTE
 * 			********************************************************************************
 * 			*  In questo filtro viene disabilitata la sicurezza e, pertanto, l'accesso ad  * 
 * 			*  una applicazione protetta non comporta il passaggio alla funzione di login  *
 * 			********************************************************************************
 * 
 * @author f.vadicamo
 *
 */
public class FedERaFilter implements Filter{

	private ServletContext context = null;

	public void destroy() {}

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,	FilterChain chain) throws IOException, ServletException 
	{
		String authBeanUsername = null;
		HttpSession session = null;
		
		
		UserBean userBean = null;
		AuthenticationSessionBean authBean = null;
		Map serviceContextMap = null;
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		session = request.getSession(false);
		/*
		 * Se la sessione non è ancora stata creata 
		 * faccio il chain direttamente
		 */
		if (session == null)
		{
			chain.doFilter(servletRequest,servletResponse);
			return;
		}
		userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		/*
		 * Se è presente uno UserBean valido si tratta di una richiesta relativa 
		 * ad sessione già autenticata e trattata precedentemente da questo filtro
		 * e faccio il chain
		 */
		if(UserBean.isValid(userBean))
		{
			chain.doFilter(servletRequest,servletResponse);
			return;
		}
		else
		/*
		 * Se manca uno UserBean valido si tratta di una richiesta relativa ad una sessione
		 * già autenticata esternamente ma non ancora trattata da questo filtro
		 */
		{
			serviceContextMap = (Map)session.getAttribute(ICARConstants.SERVICE_CONTEXT_MAP); 
			String serviceURLPrefix = (String)session.getAttribute(ICARConstants.SERVICE_URL_PREFIX); 
			//if (serviceContextMap != null && serviceURLPrefix != null)
			if (serviceContextMap != null)
			{
				/*
				 * Controllo che lo AuthenticationSessionBean sia valido
				 */
				authBean = (AuthenticationSessionBean)serviceContextMap.get(serviceURLPrefix);
				if (isValidAuthBean(authBean))
				{
					/*
					 * Controllo che l'utenza sia censita, attiva e non scaduta
					 * sul DB di payer
					 * NOTA: controllare CF
					 */
					authBeanUsername = getUsernameFromAuthBean(authBean);
					
					
					
					
					String esito = payerUserCheck(authBeanUsername, userBean, request, session);
					if (esito.equals("OK"))
					{
						/*
						 * In tal caso:
						 * 
						 * - ho uno userBean totalmente (un solo profilo) o parzialmente (nel caso di più profili) valorizzato 
						 * - gli assegno il SessionId della sessione corrente e lo metto in sessione;
						 * - setto il flag che indica che l'utente è SIGNED_ON;
						 * - setto il template corretto
						 * - faccio la redirect verso la pagina di default di payer che gestirà il flow per la scelta dei profili (se necessario)
						 */
						//userBean = getNewUserBean();
						userBean.setSession(session.getId());
						userBean.setTipoAutenticazione("F");
						
						UtentePIVA seUser=new UtentePIVA();
						
						seUser.setTipologiaUtente(UtentePIVATipologiaUtente.F);
						seUser.setUsername(authBeanUsername);
						
						
						DatiAnagPersonaFisicaDelegato datiAnagPersonaFisicaDelegato=new DatiAnagPersonaFisicaDelegato();
						DatiGeneraliPersonaFisicaDelegato datiGeneraliPersonaFisicaDelegato=new DatiGeneraliPersonaFisicaDelegato();
						DatiContattiPersonaFisicaDelegato datiContattiPersonaFisicaDelegato=new DatiContattiPersonaFisicaDelegato();
						
						datiGeneraliPersonaFisicaDelegato.setNome(getNomeFromAuthBean(authBean));
						datiGeneraliPersonaFisicaDelegato.setCognome(getCognomeFromAuthBean(authBean));
						datiGeneraliPersonaFisicaDelegato.setCodiceFiscale(getCodiceFiscaleFromAuthBean(authBean));
						datiContattiPersonaFisicaDelegato.setMail(getMailFromAuthBean(authBean));
						datiContattiPersonaFisicaDelegato.setCellulare(getCellulareFromAuthBean(authBean));
						
						datiAnagPersonaFisicaDelegato.setDatiGeneraliPersonaFisicaDelegato(datiGeneraliPersonaFisicaDelegato);
						
						datiAnagPersonaFisicaDelegato.setDatiContattiPersonaFisicaDelegato(datiContattiPersonaFisicaDelegato);
						seUser.setDatiAnagPersonaFisicaDelegato(datiAnagPersonaFisicaDelegato);
					

						userBean.setSeUserBean(seUser);
						
						Map mapAttr = authBean.getAttributesMap();
						if (mapAttr.containsKey("emailAddress") && mapAttr.get("emailAddress") != null) 
							userBean.setEmailNotifiche(mapAttr.get("emailAddress").toString().replace("[","").replace("]", ""));
						
						session.setAttribute(SignOnKeys.USER_BEAN, userBean);
						session.setAttribute(SignOnKeys.SIGNED_ON_USER, new Boolean(true));
						TemplateFilter.setTemplateToUserBean(userBean, session, request);
						
						//loadMenu();
						//ApplicationBinderFactory.instance().getApplicationBinder().bind(request, userBean);
						/*
						 ***************************************************************************** 
						 * Forzo una redirect verso la pagina di default, "/default/default.do/", 
						 * invece di fare "chain", per gestire correttamente il caso in cui l'utente
						 * richiede una pagina dopo che la sessione è scaduta
						 *****************************************************************************
						 */
						request.setAttribute(MAFAttributes.CURRENT_APPLICATION,"default");
						request.getRequestDispatcher("default.do").forward(servletRequest, servletResponse);
						return;        			
					}
					else
					{
						request.setAttribute("logoffMessage", esito);
						request.setAttribute(MAFAttributes.CURRENT_APPLICATION,"login");
						request.getRequestDispatcher("logoffAuthFederata.do").forward(servletRequest, servletResponse);
						return;
					}
				}
				/*
				 * In sessione non è presente uno UserBean Valido, è presente il "serviceContextMap" FEDERA
				 * ma "AuthenticationSessionBean" non è valido (o e nullo o manca lo username) - Faccio la redirect
    			 * alla pagina di Login dopo aver creato uno UserBean necessario a settare il template.
				 */
				else
				{
					request.setAttribute(MAFAttributes.CURRENT_APPLICATION,"login");
					userBean = new  UserBean();
					session.setAttribute(SignOnKeys.USER_BEAN, userBean);
					TemplateFilter.setTemplateToUserBean(userBean, session, request);
					request.getRequestDispatcher("login.do").forward(servletRequest, servletResponse);
					return;
				}
			}
			/*
			 * In sessione non è presente uno UserBean valido e non è presente
			 * neanche il "serviceContextMap" FEDERA - Faccio la redirect
			 * alla pagina di Login dopo aver creato uno UserBean necessario a
			 * settare il template.
			 */
			else
			{
				request.setAttribute(MAFAttributes.CURRENT_APPLICATION,"login");
				userBean = new  UserBean();
				session.setAttribute(SignOnKeys.USER_BEAN, userBean);
				TemplateFilter.setTemplateToUserBean(userBean, session, request);
				request.getRequestDispatcher("login.do").forward(servletRequest, servletResponse);
				return;
			}
		}
	}

	private String getCellulareFromAuthBean(AuthenticationSessionBean authBean) {
		return authBean.getAttributesMap().get("cellulare").toString().replace("[","").replace("]", "");
	
		
	}

	private String getMailFromAuthBean(AuthenticationSessionBean authBean) {
		return authBean.getAttributesMap().get("emailAddress").toString().replace("[","").replace("]", "");
	}

	private String getCodiceFiscaleFromAuthBean(
			AuthenticationSessionBean authBean) {
		return authBean.getAttributesMap().get("CodiceFiscale").toString().replace("[","").replace("]", "");
	}

	private String getCognomeFromAuthBean(AuthenticationSessionBean authBean) {
		return authBean.getAttributesMap().get("cognome").toString().replace("[","").replace("]", "");
	}

	private String getNomeFromAuthBean(AuthenticationSessionBean authBean) {
		
		return authBean.getAttributesMap().get("nome").toString().replace("[","").replace("]", "");
	}

	public void init(FilterConfig config) throws ServletException 
	{
		/*
		 * Viene disabilitata la sicurezza e 
		 * settato un parametro che permette di
		 * conoscere se il filtro è presente o meno
		 * NB: non è necessario disabilitare la sicurezza se Federa è gestito con servlet custom
		 */
		context = config.getServletContext();
		context.setAttribute(ManagerKeys.FEDERA_FILTER, "Y");
		//disableSecurity();
	}
	
	private boolean isValidAuthBean(AuthenticationSessionBean authBean)
	{
		return (authBean != null && getUsernameFromAuthBean(authBean) != null);
	}
	
	private String getUsernameFromAuthBean(AuthenticationSessionBean authBean)
	{
		String username = null;
		if (authBean != null)
		{
			String userid = authBean.getUserID();
			if (userid != null)
			{
				String[] a = userid.split("@");
				if (a != null & a.length > 0) username = a[0];
			}
		}
		return username;
		
	}

	private String payerUserCheck(String username, UserBean userBean, HttpServletRequest request, HttpSession session)
	{
		String esito = "OK";
		/*
		 * Recupero i dati dell'utente censito in PAYER
		 */
		try {
				
			
				
				//verifico nella tabella PYUSRTB
				/*
				 * Chiamo il metodo per recuperare la lista dei profili legata allo username dell'utente
				 * e la metto in sessione
				 */
				List<Profilo> listProfili = SignOnSupport.caricaProfili(session, username, request);
				if (listProfili.size() == 1)
				{
					session.setAttribute(ManagerKeys.NUMERO_PROFILI, "1");
					//se ho un solo profilo aggiorno direttamente lo userBean
					SceltaProfiloAction.setUserBeanAndMenu(userBean, session, listProfili.get(0).getChiaveUtente(), true, request);
					ApplicationBinderFactory.instance().getApplicationBinder().bind(request, userBean);
				}
				else if (listProfili.size() > 1)
				{
					//setto un profilo fittizio per permettere l'accesso all'applicazione di default
					userBean.setUserProfile(ManagerKeys.CHIAVE_MULTIPROFILO);
					session.setAttribute(ManagerKeys.NUMERO_PROFILI, "N");
					session.setAttribute(ManagerKeys.PROFILI_UTENTE, listProfili);
				}
				else 
					esito = Messages.NESSUN_PROFILO_PRESENTE.format(username);
							
		} catch (SignOnException e) {
			esito = e.getMessage();
			e.printStackTrace();
		}
		return esito;
	}
	
	
	@SuppressWarnings("unused")
	private void disableSecurity()
	{
		ApplicationsData applicationsData = (ApplicationsData) context.getAttribute(MAFAttributes.APPLICATIONS);
		if(applicationsData != null)
		{
			SecurityData sd = applicationsData.getSecurityData();
			if (sd != null) sd.setEnabled(false);
		}
	}
	
}
