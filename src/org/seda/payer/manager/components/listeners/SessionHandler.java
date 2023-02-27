package org.seda.payer.manager.components.listeners;


import java.rmi.RemoteException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.seda.payer.manager.analysis.AnalysisUtils;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.core.session.SessionHandlerException;
import com.seda.payer.pgec.webservice.commons.dati.ManagerLogRequestType;
import com.seda.payer.pgec.webservice.commons.dati.ManagerLogResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;


public class SessionHandler extends com.seda.j2ee5.maf.core.session.SessionHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SessionHandler(HttpSessionEvent se) throws SessionHandlerException {
		super(se);		
//		AnalysisUtils.setJVMProperty(se.getSession());	
	}

	@Override
	public Object destroyed(HttpSessionEvent se) throws SessionHandlerException {
		UserBean userBean = null;
		String userToken = null;
		/*
		String sessionId = null;
		String userName = null;
		String userProfile = null;
		String codiceSocieta = null;
		String codiceUtente = null;
		String chiaveEnte = null;
		*/
		ManagerLogResponseType logResponse = null;
		ManagerLogRequestType in = null;
		ResponseType res = null;
		
		HttpSession session = se.getSession();
		userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		if(UserBean.isValid(userBean))
		{
			//System.out.println(">>>>>>>>>> END SESSION - USER BEAN IS VALID - " + session.getId());
			userToken = userBean.getUserToken();
			/*
			sessionId = session.getId();
			userName = userBean.getUserName();
			userProfile = userBean.getUserProfile();
			codiceSocieta = userBean.getCodiceSocieta();
			codiceUtente = userBean.getCodiceUtente();
			chiaveEnte = userBean.getChiaveEnteConsorzio();
			*/
			in = new ManagerLogRequestType();
			in.setUserToken(userToken);
			in.setSessionId(null);
			in.setUserName(null);
			in.setUserProfile(null);
			in.setCodiceSocieta(null);
			in.setCodiceUtente(null);
			in.setChiaveEnte(null);
			in.setCanalePagamento(null);
			in.setIndirizzoIP(null);
			in.setApplicazione(null);
			in.setEndSession("Y");
			try {
				logResponse = WSCache.commonsServer.managerLog(in, session);
				if (logResponse != null)
				{
					res = logResponse.getResponse();
					if(!res.getRetCode().equals(ResponseTypeRetCode.value1)){
						System.out.println("SessionHandler: ERROR - " + res.getRetMessage());
					}
				}
			} catch (FaultType e1) {
				e1.printStackTrace();
				System.out.println("SessionHandler: ERROR - " + e1.getLocalizedMessage());
			} catch (RemoteException e1) {
				e1.printStackTrace();
				System.out.println("SessionHandler: ERROR - " + e1.getMessage());
			}
		}
		//else System.out.println(">>>>>>>>>> END SESSION - USER BEAN NOT VALID - " + session.getId());
		
		// Eliminazione file temporanei cubi jpivot
//		AnalysisUtils.deleteCubeFile(session);
//		AnalysisUtils.deleteJPivotFiles();

		
		return super.destroyed(se);
	}

}
