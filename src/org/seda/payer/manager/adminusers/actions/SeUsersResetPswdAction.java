package org.seda.payer.manager.adminusers.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.security.webservice.dati.DatiContattiPersonaFisicaDelegato;
import com.seda.security.webservice.dati.ResetPWDRequestType;
import com.seda.security.webservice.dati.ResetPWDResponseType;
import com.seda.security.webservice.dati.ResponseType;
import com.seda.security.webservice.dati.SelezionaUtentePIVARequestType;
import com.seda.security.webservice.dati.SelezionaUtentePIVAResponseType;

@SuppressWarnings("serial")
public class SeUsersResetPswdAction extends BaseManagerAction{

	
	//SelezionaUtenteRequestType sin = null;
	//SelezionaUtenteResponseType sout = null;
	SelezionaUtentePIVARequestType sin = null;
	SelezionaUtentePIVAResponseType sout = null;

	ResponseType sresponse = null;
	String sretCode = null;
	String sretMessage = null;
	ResetPWDRequestType in = null;
	ResetPWDResponseType out = null;
	ResponseType response = null;
	String retCode = null;
	String retMessage = null;
	String username = null;

	public Object service(HttpServletRequest request) throws ActionException {
		username = (String)request.getAttribute("tx_username");
		username = (username == null ? "" : username);
		//
		if (!username.equals(""))
		{
			try {
				//sin = new SelezionaUtenteRequestType();
				sin = new SelezionaUtentePIVARequestType();
				sin.setUserName(username);
				sout = WSCache.securityServer.selezionaUtentePIVA(sin, request);
				sresponse = sout.getResponse();
				if (sresponse != null)
				{
					sretCode = sresponse.getRetCode();
					sretMessage = sresponse.getRetMessage();
					if (sretCode.equals("00"))
					{
						in = new ResetPWDRequestType();
						in.setPuk(sout.getPuk());
						in.setUserName(username);
						
						String email;
						DatiContattiPersonaFisicaDelegato contattiPersonaFisicaDelegato=sout.getSelezionaUtentePIVAResponse().getDatiAnagPersonaFisicaDelegato().getDatiContattiPersonaFisicaDelegato();
						if (contattiPersonaFisicaDelegato.getPec()!=null && contattiPersonaFisicaDelegato.getPec().trim().length() > 0) {
							email=contattiPersonaFisicaDelegato.getPec();
						} else {
							email=contattiPersonaFisicaDelegato.getMail();
						}
						
						in.setEmail(email);
						in.setUrlSito(request.getServerName());
						
						out = WSCache.securityServer.resetPWD(in, request);
						response = out.getResponse();
						if (response != null)
						{
							retCode = response.getRetCode();
							retMessage = response.getRetMessage();
							if (retCode.equals("00"))
							{
								request.setAttribute("cambioEffettuato", true);
								setFormMessage("form_indietro", Messages.RESET_PASSWORD_OK.format(username), request);
							}
							else
								setFormMessage("form_indietro", Messages.RESET_PASSWORD_KO.format(username,retMessage), request);
						}
						else
							setFormMessage("form_indietro", Messages.RESET_PASSWORD_KO.format(username,"Errore generico"), request);
					}
					else
						setFormMessage("form_indietro", Messages.RESET_PASSWORD_KO.format(username,sretMessage), request);
				}
				else
					setFormMessage("form_indietro", Messages.RESET_PASSWORD_KO.format(username,"Errore generico"), request);
			} catch (FaultType e) {
				setFormMessage("form_indietro", "Errore: " + e.getLocalizedMessage(), request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("form_indietro", "Errore: " + e.getMessage(), request);
				e.printStackTrace();
			}
		}
		else
		{
			setFormMessage("form_indietro", Messages.UTENZA_NON_VALIDA.format(), request);
		}
		return null;
	}
	
}
