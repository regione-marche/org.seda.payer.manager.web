/**
 * 
 */
package org.seda.payer.manager.login.actions;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.components.filters.TemplateFilter;
import org.seda.payer.manager.components.security.Profilo;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.defaults.actions.SceltaProfiloAction;
import org.seda.payer.manager.util.Crypto;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.security.ChryptoServiceException;
import com.seda.commons.string.Convert;
import com.seda.j2ee5.maf.core.security.SecuritySignOn;
import com.seda.j2ee5.maf.core.security.SignOnException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.pgec.webservice.adminusers.dati.UserBeanType;
import com.seda.payer.pgec.webservice.adminusers.dati.UserProfileListRequest;
import com.seda.payer.pgec.webservice.adminusers.dati.UserProfileListResponse;
import com.seda.payer.pgec.webservice.commons.dati.InvalidateLogRequestType;
import com.seda.payer.pgec.webservice.commons.dati.InvalidateLogResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ManagerLogRequestType;
import com.seda.payer.pgec.webservice.commons.dati.ManagerLogResponseType;
import com.seda.security.webservice.dati.LoginRequestType;
import com.seda.security.webservice.dati.LoginResponseType;
import com.seda.security.webservice.dati.UtentePIVA;


/**
 * Questa classe implementa la richiesta di sign on proveniente da form
 * @author Seda S.p.A.
 *
 */
public class SignOnSupport implements SecuritySignOn {

	//private final String cambioPasswordUrl = "../login/cambioPswd.do";

	public UserBean authenticate(String userName, String password,
			HttpServletRequest request)throws SignOnException
	{
		UserBean userBean = null;
		
		//SecurityUserBean seUser = null;
		
		UtentePIVA seUser =null;

		Calendar now = Calendar.getInstance();  //PG190080_001 08052019

		LoginResponseType loginResponse = null;
		LoginRequestType loginRequest = null;
		
		com.seda.security.webservice.dati.ResponseType seResponse = null;
		
		HttpSession session=request.getSession();

		String sTemplate="";
		if (ManagerStarter.configuration != null) {
			System.out.println("ManagerStarter");
			sTemplate = ManagerStarter.configuration.getProperty(PropertiesPath.templateBase.format() + request.getServerName());	
		}

		try {
			loginRequest = new LoginRequestType();
			loginRequest.setUserName(userName);
			loginRequest.setPassword(Crypto.encrypt(password));
			/*
			 *  Chiamo il WS di login per l'autenticazione
			 */
			loginResponse = WSCache.securityServer.login(loginRequest, request);
			seResponse = loginResponse.getResponse();
			
			if(seResponse.getRetCode().equals("00"))
			{
				//seUser = loginResponse.getSecurityUserBean();
				seUser = loginResponse.getUtentePIVA();
				userBean = new UserBean(seUser);
				//chiamo il metodo per invalidare le sessioni attivi per l'utente
				if(sTemplate.equals("soris"))
					invalidateUserSession(seUser.getUsername(),userBean.getUserToken() ,request);
				userBean.setTipoAutenticazione(UserBean.AUTENTICAZIONE_PROPRIETARIA);
				long ggScadenzaPassword = daysBetween(now, seUser.getDataScadenzaPassword());
				session.setAttribute(ManagerKeys.GG_SCADENZA_PASSWORD, ggScadenzaPassword);
				request.setAttribute(ManagerKeys.GG_SCADENZA_PASSWORD, ggScadenzaPassword);

				
				/*
				 * Chiamo il metodo per recuperare la lista dei profili legata allo username dell'utente
				 * e la metto in sessione
				 */
				List<Profilo> listProfili = caricaProfili(session, userName, request);
				if (listProfili.size() == 1)
				{
					session.setAttribute(ManagerKeys.NUMERO_PROFILI, "1");
					//se ho un solo profilo aggiorno direttamente lo userBean
					SceltaProfiloAction.setUserBeanAndMenu(userBean, session, listProfili.get(0).getChiaveUtente(), true, request);
				}
				else if (listProfili.size() > 1)
				{
					//setto un profilo fittizio per permettere l'accesso all'applicazione di default
					userBean.setUserProfile(ManagerKeys.CHIAVE_MULTIPROFILO);
					session.setAttribute(ManagerKeys.NUMERO_PROFILI, "N");
					session.setAttribute(ManagerKeys.PROFILI_UTENTE, listProfili);
				}
				else 
					throw new SignOnException(Messages.NESSUN_PROFILO_PRESENTE.format(userName));
			}
			else
			{
				/*
				 * Se si tratta del primo accesso per l'utenza (05) o se la password è scaduta (10)
				*  vado alla pagina di cambio password
				 */
				if(seResponse.getRetCode().equals("05") || seResponse.getRetCode().equals("10"))
				{
					/*
					 * Creo uno UserBean vuoto per evitare NullPointerException in uscita
					 */
					
					userBean = new UserBean();
					userBean.setName(userName);
					userBean.setTipoAutenticazione(UserBean.AUTENTICAZIONE_PROPRIETARIA);
					//session.setAttribute("tx_username", userName);
					
					MAFRequest mafReq = new MAFRequest(request);
					String urlChangePassword = mafReq.getCurrentURL().replace(mafReq.getAfterContext(), "/login/cambioPswd.do");
					
					if (seResponse.getRetCode().equals("10"))
						urlChangePassword += "?msg=" + seResponse.getRetMessage(); 
					
		            session.setAttribute(SignOnKeys.ORIGINAL_URL, urlChangePassword);
				}
				else 
				{
					/*
					 * Se non si tratta di un primo accesso 
					 * setto l'attributo che consente il reset della password
					 * e viene visualizzato il messaggio relativo al 
					 * fallimento del login
					 */
					request.setAttribute("enableResetPswd", true);
					throw new SignOnException(seResponse.getRetMessage());
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new SignOnException(e.getMessage() == null ? "Errore generico in SignOnSupport" : e.getMessage());
		} catch (ChryptoServiceException e) {
			e.printStackTrace();
			throw new SignOnException(e.getMessage() == null ? "Errore generico in SignOnSupport" : e.getMessage());
		} catch (com.seda.payer.pgec.webservice.srv.FaultType e) {
			e.printStackTrace();
			throw new SignOnException(e.getMessage() == null ? "Errore generico in SignOnSupport" : e.getMessage());
		} catch (RemoteException e) {
			e.printStackTrace();
			throw new SignOnException(e.getMessage() == null ? "Errore generico in SignOnSupport" : e.getMessage());
		} 
		session.setAttribute("loginRetCode", seResponse.getRetCode());
		session.setAttribute("loginRetMessage", seResponse.getRetMessage());
		
		if (userBean == null){
			userBean = new UserBean();
			userBean.setTipoAutenticazione(UserBean.AUTENTICAZIONE_PROPRIETARIA);
		}
		
		//setto la personalizzazione del template
		TemplateFilter.setTemplateToUserBean(userBean, session, request);
		
		return userBean;	
	}

	public static List<Profilo> caricaProfili(HttpSession session, String userName, HttpServletRequest request) throws SignOnException
	{
		UserProfileListResponse userProfileListRes = null;
		UserProfileListRequest userProfileListReq = null;
		
		List<Profilo> listProfili = new ArrayList<Profilo>();
		UserBeanType[] listUserBeanType = null;
		Profilo profilo = null;
		
		try
		{
			userProfileListReq = new UserProfileListRequest(userName);
			userProfileListRes = WSCache.adminUsersServer.getUserProfileListByUserName(userProfileListReq, request);
			if (userProfileListRes != null)
			{
				listUserBeanType = userProfileListRes.getListUserProfile();
				if (userProfileListRes.getResponse() != null && userProfileListRes.getResponse().getRetCode().equals("00") && listUserBeanType != null)
				{
					for (UserBeanType user : listUserBeanType)
					{
						if (!user.getUserProfile().equals("PYCO"))
						{
							profilo = new Profilo();
							profilo.setChiaveUtente(user.getChiaveUtente());
							profilo.setDescProfilo(user.getUserProfile());
							profilo.setDescrSocieta(user.getDescrSocieta());
							profilo.setDescrUtente(user.getDescrUtente());
							profilo.setDescrEnte(user.getDescrEnte());
							listProfili.add(profilo);
						}
					}
				}
				else
				{
					if (userProfileListRes.getResponse() == null || userProfileListRes.getResponse().getRetCode().equals("01"))
						throw new SignOnException(Messages.NESSUN_PROFILO_PRESENTE.format(userName));
					else
						throw new SignOnException(Messages.ERRORE_CARICAMENTO_PROFILI.format(userName));
				}
			}
			else
				throw new SignOnException(Messages.ERRORE_CARICAMENTO_PROFILI.format(userName));
		} 
		catch (RemoteException e) {
			e.printStackTrace();
			throw new SignOnException(Messages.ERRORE_CARICAMENTO_PROFILI.format(userName));
		}
		
		return listProfili;
	}
	
	public void init(ServletContext context) throws SignOnException {}

	public void term() {}

	public static long daysBetween(Calendar startDate, Calendar endDate) {
	    long end = endDate.getTimeInMillis();
	    long start = startDate.getTimeInMillis();
	    return TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
	}
	
	
	public void invalidateUserSession(String username, String userToken, HttpServletRequest request) {
		
		
		try {
			InvalidateLogRequestType req = new InvalidateLogRequestType(username);
			InvalidateLogResponseType res = WSCache.commonsServer.invalidateLog(req, request);
			if(res!=null) {
				System.out.println(res.getResponse().getRetMessage());
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
