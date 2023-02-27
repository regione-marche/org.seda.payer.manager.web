package org.seda.payer.manager.components.filters.bolzano;

import it.cefriel.icar.inf3.ICARConstants;
import it.cefriel.icar.inf3.web.beans.AuthenticationSessionBean;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.seda.payer.manager.components.filters.TemplateFilter;
import org.seda.payer.manager.components.security.Profilo;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.defaults.actions.SceltaProfiloAction;
import org.seda.payer.manager.login.actions.SignOnSupport;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.security.ChryptoServiceException;
import com.seda.j2ee5.maf.core.application.ApplicationBinderFactory;
import com.seda.j2ee5.maf.core.application.ApplicationsData;
import com.seda.j2ee5.maf.core.security.SecurityData;
import com.seda.j2ee5.maf.core.security.SignOnException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.payer.pgec.webservice.srv.FaultType;


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
public class FedERaFilterBolzano implements Filter{

	private ServletContext context = null;

	public void destroy() {}

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,	FilterChain chain) throws IOException, ServletException 
	{
		String authBeanUsername = null;
		HttpSession session = null;
		
		
		UserBean userBean = null;
		BolzanoUserBean authBean = null;
		Map serviceContextMap = null;
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse)servletResponse;
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
			authBean=new BolzanoUserBean();
			try {
				authBean.loadUserBean(request);
			} catch (ChryptoServiceException e) {
				invalidateAuthBean(authBean);
				e.printStackTrace();
			} 
			//if (serviceContextMap != null && serviceURLPrefix != null)
			if (authBean.isValidBolzanoAccount())
			{
				/*
				 * Controllo che lo AuthenticationSessionBean sia valido
				 */
				userBean = authBean.createNewUserBean(request, session);
				authBeanUsername=userBean.getCodiceFiscale();
				if (authBean.isValidUserBean())
				{
					/*
					 * Controllo che l'utenza sia censita, attiva e non scaduta
					 * sul DB di payer
					 * NOTA: controllare CF
					 */
					
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
						clearBolzanoCookies(request,response);
						userBean.setSession(session.getId());
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
						request.getRequestDispatcher("logoff.do").forward(servletRequest, servletResponse);
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

	private void invalidateAuthBean(BolzanoUserBean authBean) {
		authBean.setNome(null);
		authBean.setCognome(null);
		authBean.setCodiceFiscale(null);
		
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
		Calendar now = null;
		/*
		 * Recupero i dati dell'utente censito in PAYER
		 */
		try {
			//verifico che lo username appartenga ad utente esistente nella tabella SEUSRTB
			
			/*
			com.seda.security.webservice.dati.SelezionaUtenteRequestType req = new com.seda.security.webservice.dati.SelezionaUtenteRequestType(username);		
			com.seda.security.webservice.dati.SelezionaUtenteResponseType res = WSCache.securityServer.selezionaUtente(req);
			com.seda.security.webservice.dati.ResponseType response = null;
			*/
			com.seda.security.webservice.dati.SelezionaUtentePIVARequestType req = new com.seda.security.webservice.dati.SelezionaUtentePIVARequestType();
			req.setUserName(username);
			com.seda.security.webservice.dati.SelezionaUtentePIVAResponseType res = WSCache.securityServer.selezionaUtentePIVA(req, request);
			com.seda.security.webservice.dati.ResponseType response = null;
			
			if (res != null && res.getResponse() != null)
			{
				response = res.getResponse();
				String retCode = response.getRetCode();
				String retMessage = response.getRetMessage();
				if (retCode.equals("00"))
				{
					if (res.getSelezionaUtentePIVAResponse() == null)
						esito = Messages.UTENZA_NON_VALIDA.format();
					else
					{
						now = Calendar.getInstance();
						Calendar dataFineValiditaUtenza = res.getSelezionaUtentePIVAResponse().getDataFineValiditaUtenza();
						if (dataFineValiditaUtenza.before(now))
							esito = Messages.UTENZA_SCADUTA.format(username);
						else
						{
							if (res.getSelezionaUtentePIVAResponse().getUtenzaAttiva().getValue().equalsIgnoreCase("N"))
								esito = Messages.UTENZA_NON_ATTIVA.format(username);
							else
							{
								//inizializzo lo userbean con la parte dei dati relativa all'utenza definita nella SEUSRTB
								//userBean = new UserBean(res.getSecurityUserBean());
								userBean.setSeUserBean(res.getSelezionaUtentePIVAResponse());
								
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
							}
						}
					}
				}
				else
					esito = retCode + " - " + retMessage;
			}
			else
				Messages.ERRORE_DI_COMUNICAZIONE.format(" - Impossibile accedere ai dati utente");

		} catch (FaultType e) {
			esito = Messages.ERRORE_DI_COMUNICAZIONE.format(" - Impossibile accedere ai dati utente");
			e.printStackTrace();
		} catch (RemoteException e) {
			esito = Messages.ERRORE_DI_COMUNICAZIONE.format(" - Impossibile accedere ai dati utente");
			e.printStackTrace();
		} catch (SignOnException e) {
			esito = e.getMessage();
			e.printStackTrace();
		}
		return esito;
	}
	
	/*private UserBean getNewUserBean()
	{
		UserBean userBean = null;
		if(selezionaRes != null)
		{
			userBean = new UserBean(getUbt(selezionaRes.getPyUser()), selezionaRes.getApplicazioni(), getSub(selezionaRes.getSeUser()));
		}
		return userBean;
		
	}*/
	
	/*private SecurityUserBean getSub(SeUserType seUser)
	{
		SecurityUserBean sub = null;
		if (seUser != null)
		{
			sub = new SecurityUserBean
			(
					seUser.getUsername(), 
					seUser.getNome(), 
					seUser.getCognome(), 
					seUser.getPassword(), 
					seUser.getPassword2(), 
					seUser.getPassword3(), 
					seUser.getPuk(), 
					seUser.getTipologiaUtente(), 
					seUser.getCodiceFiscale(), 
					seUser.getEmailNotifiche(), 
					seUser.getSmsNotifiche(), 
					seUser.getUtenzaAttiva(), 
					seUser.getDataInizioValiditaUtenza(), 
					seUser.getDataFineValiditaUtenza(), 
					seUser.getPrimoAccesso(), 
					seUser.getDataScadenzaPassword(), 
					seUser.getDataUltimoAccesso(), 
					seUser.getDataInserimentoUtenza(), 
					seUser.getNote(), 
					seUser.getDataUltimoAggiornamento(), 
					seUser.getOperatoreUltimoAggiornamento()
					);
		}
		return sub;
	}*/
	
	/*private UserBeanType getUbt(PyUserType pyUser)
	{
		UserBeanType ubt = null;
		if (pyUser != null)
		{
			ubt = new UserBeanType
			(
				pyUser.getUserName(), 
				pyUser.getUserProfile(), 
				pyUser.getFlagAttivazione(), 
				pyUser.getCodiceSocieta(), 
				pyUser.getCodiceUtente(), 
				pyUser.getChiaveEnteConsorzio(), 
				pyUser.getChiaveEnteConsorziato(), 
				pyUser.getDownloadFlussiRendicontazione(), 
				pyUser.getInvioFlussiRendicontazioneViaFtp(), 
				pyUser.getInvioFlussiRendicontazioneViaEmail(), 
				pyUser.getAzioniPerTransazioniOK(), 
				pyUser.getAzioniPerTransazioniKO(), 
				pyUser.getAzioniPerRiconciliazioneManuale(), 
				pyUser.getAttivazioneEstrattoContoManager(), 
				pyUser.getAbilitazioneProfiloRiversamento(), 
				pyUser.getAbilitazioneMultiUtente(), 
				pyUser.getDataUltimoAggiornamento(), 
				pyUser.getOperatoreUltimoAggiornamento()
			);
		}
		return ubt;
	}*/
	
	/*private void loadMenu()
	{
		WebRowSet[] menus = CollectionMenu.getMenuLivello123(authBeanUsername);
		if (menus != null && menus.length == 3)
		{
			try {
				session.setAttribute(MenuKey.MENU_LIVELLO_UNO, (new CollectionMenu(menus[0])).getMenuCollection());
				session.setAttribute(MenuKey.MENU_LIVELLO_DUE, (new CollectionMenu(menus[1])).getMenuCollection());
				session.setAttribute(MenuKey.MENU_LIVELLO_TRE, (new CollectionMenu(menus[2])).getMenuCollection());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}*/
	
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
	
	
	
	private void clearBolzanoCookies(HttpServletRequest request,
			HttpServletResponse response) {
            
		Date expdate= new Date();
		expdate.setTime (expdate.getTime() + (3600 * 1000));
		DateFormat df = new SimpleDateFormat("dd MMM yyyy kk:mm:ss z");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		String now = df.format(expdate);
        response.addHeader("SET-COOKIE","ssoCookieCit=0;" +"path=/;"+"Expires="+now+";"+ "secure;"+"Domain=.egov.bz.it;"+"HttpOnly"); 
	}
	
}
