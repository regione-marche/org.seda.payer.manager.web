package org.seda.payer.manager.login.actions;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Crypto;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.security.ChryptoServiceException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.security.webservice.dati.ResetPWDRequestType;
import com.seda.security.webservice.dati.ResetPWDResponseType;
import com.seda.security.webservice.dati.ResponseType;
import com.seda.security.webservice.dati.SendMailCodiciRequestType;
import com.seda.security.webservice.dati.SendMailCodiciResponseType;
import com.seda.security.webservice.dati.TipoEmail;

@SuppressWarnings("serial")
public class ResetPswdAction extends BaseManagerAction{

	ResetPWDRequestType in = null;
	ResetPWDResponseType out = null;
	ResponseType response = null;
	String username = null;
	String puk = null;
	String emailNotifiche = null;
	String emailPEC = null;
	String retCode = null;
	String retMessage = null;

	public Object service(HttpServletRequest request) throws ActionException {
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		/*
		 * Ricavo i parametri
		 */
		emailNotifiche = (String)request.getAttribute("emailNotifiche");
		emailPEC = (String)request.getAttribute("postacertificata");
		puk = (String)request.getAttribute("puk");
		username = (String)request.getAttribute("tx_username");
		emailNotifiche = (emailNotifiche == null ? "" : emailNotifiche);
		puk = (puk == null ? "" : puk);
		username = (username == null ? "" : username);
		//
		switch(firedButton)
		{
		case TX_BUTTON_EDIT:
			if (puk.equals("") || username.equals(""))
				setFormMessage("reset_pswd_form", Messages.CAMPI_OBBLIGATORI.format("*"), request);
			else
			{
				try {
					in = new ResetPWDRequestType();
					in.setPuk(Crypto.encrypt(puk));
					in.setUserName(username);
					in.setEmail(emailNotifiche);
					in.setUrlSito(request.getServerName());
					in.setCertificata(emailPEC == null ? "NO" : "SI");
					out = WSCache.securityServer.resetPWD(in, request);
					response = out.getResponse();
					if (response != null)
					{
						retCode = response.getRetCode();
						retMessage = response.getRetMessage();
						if (retCode.equals("00"))
						{
							request.setAttribute("cambioEffettuato", true);
							setFormMessage("reset_pswd_form", retMessage, request);
						}
						else
							setFormMessage("reset_pswd_form", retCode + " - " + retMessage, request);
					}
					else
						setFormMessage("reset_pswd_form", Messages.GENERIC_ERROR.format(), request);
				} catch (FaultType e) {
					setFormMessage("reset_pswd_form", "Errore: " + e.getLocalizedMessage(), request);
					e.printStackTrace();
				} catch (RemoteException e) {
					setFormMessage("reset_pswd_form", "Errore: " + e.getMessage(), request);
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					setFormMessage("reset_pswd_form", "Errore: " + e.getMessage(), request);
					e.printStackTrace();
				} catch (ChryptoServiceException e) {
					setFormMessage("reset_pswd_form", "Errore: " + e.getMessage(), request);
					e.printStackTrace();
				}
			}
			break;
		case TX_BUTTON_REINVIAPUK: //PG170280 CT
			username = (String)request.getAttribute("tx_username");
			
			SendMailCodiciRequestType in=new SendMailCodiciRequestType();
			in.setTipoEmail(TipoEmail.PUK);
			in.setUserName(username);
			in.setUrlSito(request.getServerName());
			
			SendMailCodiciResponseType out;
			try {
				out = WSCache.securityServer.sendMailCodici(in, request);
				
				if (out!=null)
				{
					ResponseType responseType =out.getResponse();
					
					if (responseType.getRetCode().equals("00"))
						setFormMessage("reset_pswd_form", "Reinvio PUK effettuato con successo" , request);
					else
						setFormMessage("reset_pswd_form", "Errore nel reinvio del PUK" , request);
				}
				else
					setFormMessage("reset_pswd_form", "Errore nel reinvio del PUK" , request);
				
			} catch (Exception e) {
				setFormMessage("reset_pswd_form", "Errore nel reinvio del PUK" , request);
				e.printStackTrace();
			}			
			break;
		case TX_BUTTON_NULL:
			break;
		}
		return null;
	}
	
}
