package org.seda.payer.manager.login.actions;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.filters.TemplateFilter;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Crypto;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.security.ChryptoServiceException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFUtil;
import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.security.webservice.dati.CambioPWDRequestType;
import com.seda.security.webservice.dati.CambioPWDResponseType;
import com.seda.security.webservice.dati.ResponseType;
import com.seda.security.webservice.dati.SendMailCodiciRequestType;
import com.seda.security.webservice.dati.SendMailCodiciResponseType;
import com.seda.security.webservice.dati.TipoEmail;

@SuppressWarnings("serial")
public class CambioPswdAction extends BaseManagerAction {
	private String userName = null;
	private String puk = null;
	private String password = null;
	private String nuovaPassword = null;
	private String confermaNuovaPassword = null;
	private String emailNotifiche = null;
	private String emailPEC = null;
	private CambioPWDRequestType in = null;
	private CambioPWDResponseType out = null;
	private ResponseType response = null;
	private HttpSession session = null;
	private UserBean userBean = null;
	
	public Object service(HttpServletRequest request) throws ActionException {
		//
		String retCode = null;
		String retMessage = null;
		session = request.getSession();
		userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		/*
		 * Compio le azioni corrispondenti alla richiesta dell'utente
		 */
		/*
		 * Rendo indisponibile il pulsante di
		 * login fino a modifica password effettuata
		 */
		request.setAttribute("cambioEffettuato", false);
		//
		switch (firedButton)
		{
		case TX_BUTTON_EDIT:
			userName = (String)request.getAttribute("tx_username");
			puk = (String)request.getAttribute("puk");
			password = (String)request.getAttribute("tx_password");
			nuovaPassword = (String)request.getAttribute("tx_nuova_password");
			confermaNuovaPassword = (String)request.getAttribute("tx_conferma_nuova_password");
			emailNotifiche = (String)request.getAttribute("emailNotifiche");
			emailPEC = (String)request.getAttribute("postacertificata");
			
			userName = (userName == null ? "" : userName);
			puk = (puk == null ? "" : puk);
			password = (password == null ? "" : password);
			nuovaPassword = (nuovaPassword == null ? "" : nuovaPassword);
			confermaNuovaPassword = (confermaNuovaPassword == null ? "" : confermaNuovaPassword);
			emailNotifiche = (emailNotifiche == null ? "" : emailNotifiche);

			try {
				if (userName.equals("") || 
					puk.equals("") || 
					password.equals("") ||
					nuovaPassword.equals("") || 
					confermaNuovaPassword.equals(""))
					setFormMessage("cambio_pswd_form", Messages.CAMPI_OBBLIGATORI.format("*"), request);
				else
				{
					in = new CambioPWDRequestType();
					in.setUserName(userName);
					in.setPuk(Crypto.encrypt(puk));
					in.setVecchiaPWD(Crypto.encrypt(password));
					in.setNuovaPWD(Crypto.encrypt(nuovaPassword));
					in.setConfermaNuovaPWD(Crypto.encrypt(confermaNuovaPassword));
					in.setEmail(emailNotifiche==null ? "|REP" : emailNotifiche+"|REP");
					in.setCertificata(emailPEC == null ? "NO" : "SI");
					
					in.setUrlSito(request.getServerName());
					out = WSCache.securityServer.cambioPWD(in, request);
					response = out.getResponse();
					if (response != null)
					{
						retCode = response.getRetCode();
						retMessage = response.getRetMessage();
						if (retCode.equals("00")) 
						{
							request.setAttribute("cambioEffettuato", true);
							/*
							 * Se lo UserBean è valido si tratta di un cambio password
							 * voluto dall'utente dopo il login altrimenti si tratta
							 * del cambio password forzato dopo un primo accesso
							 */
							if (UserBean.isValid(userBean))
								setFormMessage("cambio_pswd_form", Messages.CAMBIO_PASSWORD_UTENTE_OK.format(userName), request);
							else
								setFormMessage("cambio_pswd_form", Messages.CAMBIO_PASSWORD_PRIMO_ACCESSO_OK.format(userName), request);
						}
						else
							setFormMessage("cambio_pswd_form", Messages.CAMBIO_PASSWORD_KO.format(userName,retMessage), request);
					}
					else
						setFormMessage("cambio_pswd_form", Messages.CAMBIO_PASSWORD_KO.format(userName,"Errore generico"), request);
				}
				
			} catch (FaultType e) {
				setFormMessage("cambio_pswd_form", "Errore: " + e.getLocalizedMessage(), request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("cambio_pswd_form", "Errore: " + e.getMessage(), request);
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				setFormMessage("cambio_pswd_form", "Errore: " + e.getMessage(), request);
				e.printStackTrace();
			} catch (ChryptoServiceException e) {
				setFormMessage("cambio_pswd_form", "Errore: " + e.getMessage(), request);
				e.printStackTrace();
			}
			
			break;
			
		case TX_BUTTON_RESET:
			break;
			
		case TX_BUTTON_NULL:
			//request.setAttribute("tx_username_hidden", request.getParameter("tx_username_hidden"));
			/*
			 * Se lo UserBean non è valido si tratta di un cambio password forzato al
			 * primo accesso e si proviene da "SignOnSupport"
			 */
			if(!UserBean.isValid(userBean))
			{
				if (request.getParameter("msg") != null)
					setFormMessage("cambio_pswd_form", request.getParameter("msg"), request);
				else
					setFormMessage("cambio_pswd_form", Messages.CAMBIO_PASSWORD_FORZATO.format(userBean.getName()), request);
			}
			break;
			
		case TX_BUTTON_LOGIN:
			/*
			 * Rigenero la sessione per reimpostare la action dopo il login
			 * al valore di default
			 */
			MAFUtil.regenerateSession(request, true);
			session = request.getSession();
			
			//ricreo lo userbean per settare il template
			userBean = new UserBean();
			TemplateFilter.setTemplateToUserBean(userBean, session, request);
			session.setAttribute(SignOnKeys.USER_BEAN, userBean);
			break;
		case TX_BUTTON_REINVIAPUK: //PG170280 CT
			userName = (String)request.getAttribute("tx_username");
			
			SendMailCodiciRequestType in=new SendMailCodiciRequestType();
			in.setTipoEmail(TipoEmail.PUK);
			in.setUserName(userName);
			in.setUrlSito(request.getServerName());
			
			SendMailCodiciResponseType out;
			try {
				out = WSCache.securityServer.sendMailCodici(in, request);
				
				if (out!=null)
				{
					ResponseType responseType =out.getResponse();
					
					if (responseType.getRetCode().equals("00"))
						setFormMessage("cambio_pswd_form", "Reinvio PUK effettuato con successo" , request);
					else
						setFormMessage("cambio_pswd_form", "Errore nel reinvio del PUK" , request);
				}
				else
					setFormMessage("cambio_pswd_form", "Errore nel reinvio del PUK" , request);
				
			} catch (Exception e) {
				setFormMessage("cambio_pswd_form", "Errore nel reinvio del PUK" , request);
				e.printStackTrace();
			}			
			break;
		}
		return null;
	}

}
