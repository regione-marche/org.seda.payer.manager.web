package org.seda.payer.manager.adminusers.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseAdminusersAction;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.components.defender.csrf.CSRFContext;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.security.webservice.dati.ResponseType;
import com.seda.security.webservice.dati.SendMailCodiciRequestType;
import com.seda.security.webservice.dati.SendMailCodiciResponseType;
import com.seda.security.webservice.dati.TipoEmail;

public class SeUsersReinvioPUKAction extends BaseAdminusersAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		
		String tokenName = CSRFContext.getInstance().getTokenName();
		
		String userName = (String)request.getAttribute("tx_username");
		
		SendMailCodiciRequestType in=new SendMailCodiciRequestType();
		in.setTipoEmail(TipoEmail.PUK);
		in.setUserName(userName);
		in.setUrlSito(request.getServerName());
		
		replyAttributes(true, request,"validator.message",tokenName);
		
		SendMailCodiciResponseType out;
		try {
			out = WSCache.securityServer.sendMailCodici(in, request);
			
			if (out!=null)
			{
				ResponseType responseType =out.getResponse();
				
				if (responseType.getRetCode().equals("00"))
					setFormMessage("search_form", "Reinvio PUK effettuato con successo" , request);
				else
					setFormMessage("search_form", "Errore nel reinvio del PUK" , request);
			}
			else
				setFormMessage("search_form", "Errore nel reinvio del PUK" , request);
			
		} catch (Exception e) {
			setFormMessage("search_form", "Errore nel reinvio del PUK" , request);
			e.printStackTrace();
		}

		
		
		return null;
	}
}



