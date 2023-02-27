/**
 * 
 */
package org.seda.payer.manager.login.actions;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.filters.TemplateFilter;
import org.seda.payer.manager.components.security.Profilo;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.defaults.actions.SceltaProfiloAction;
import org.seda.payer.manager.util.Crypto;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.security.SecuritySSO;
import com.seda.j2ee5.maf.core.security.SignOnException;
import com.seda.j2ee5.maf.core.security.UserBeanSupport;
import com.seda.security.webservice.dati.ResponseType;
import com.seda.security.webservice.dati.SelezionaUtentePIVARequestType;
import com.seda.security.webservice.dati.SelezionaUtentePIVAResponseType;
import com.seda.security.webservice.dati.UtentePIVA;

public class SSOSupport implements SecuritySSO {
	
	
	public UserBeanSupport authenticate(HttpServletRequest request) throws SignOnException {
		
		HttpSession session = request.getSession(false);
		UserBean userBean = new UserBean();
		UtentePIVA seUser = null;
		
		try
		{
			String queryString = Crypto.decrypt(request.getQueryString());

			HashMap<String, String> queryStringMap = getQueryStringMap(queryString);
			
			//setto i parametri dello userbean dell'utente loggato
			String userName = getMapValue(queryStringMap, "username");
			
			try {
				
				SelezionaUtentePIVARequestType selUteReq = new SelezionaUtentePIVARequestType();
				selUteReq.setUserName(userName);
				
				SelezionaUtentePIVAResponseType selUteRes = WSCache.securityServer.selezionaUtentePIVA(selUteReq, request);
				ResponseType seResponse = selUteRes.getResponse();
				
				if(seResponse.getRetCode().equals("00"))
				{
					seUser = selUteRes.getSelezionaUtentePIVAResponse();
					userBean = new UserBean(seUser);
					
					/*
					 * Chiamo il metodo per recuperare la lista dei profili legata allo username dell'utente
					 * e la metto in sessione
					 */
					List<Profilo> listProfili = SignOnSupport.caricaProfili(session, userName, request);
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
					throw new SignOnException(seResponse.getRetMessage());
				}
			} catch (com.seda.payer.pgec.webservice.srv.FaultType e) {
				e.printStackTrace();
				throw new SignOnException(e.getMessage() == null ? "Errore generico in SSOSupport" : e.getMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				throw new SignOnException(e.getMessage() == null ? "Errore generico in SSOSupport" : e.getMessage());
			} 
			
			TemplateFilter.setTemplateToUserBean(userBean, session, request);
			
			
		}
		catch (Exception e) {
			WSCache.logWriter.logError("SSOSupport - authenticate", e);
		}
		
		return userBean;		
	}
	
	private HashMap<String, String> getQueryStringMap(String queryString)   
	{   
	    String[] params = queryString.split("&");   
	    HashMap<String, String> map = new HashMap<String, String>();   
	    for (String param : params)   
	    {   
	    	String[] splitApp = param.split("=");
	    	if (splitApp != null && splitApp.length == 2)
	    	{
		        String name = splitApp[0];   
		        String value = splitApp[1];   
		        map.put(name, value);   
	    	}
	    }   
	    return map;   
	}  
	
	private String getMapValue(HashMap<String, String> map, String key)
	{
		if (map != null)
		{
			if (map.containsKey(key))
				return map.get(key);
		}
		return "";
	}
	
	public void init(ServletContext context) throws SignOnException {

	}

	public void term() {
		
	}
	
	
}
