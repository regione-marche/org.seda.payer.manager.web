package org.seda.payer.manager.adminusers.actions;


import javax.servlet.http.HttpServletRequest;
import org.seda.payer.manager.actions.BaseAdminusersAction;
import org.seda.payer.manager.adminusers.util.ProfiloUtil;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.components.defender.csrf.CSRFContext;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.security.webservice.dati.ResponseType;
import com.seda.security.webservice.dati.SendMailCodiciRequestType;
import com.seda.security.webservice.dati.SendMailCodiciResponseType;
import com.seda.security.webservice.dati.TipoEmail;

public class SeUsersReinvioModuloAutorizzazioneAction extends BaseAdminusersAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		
		String tokenName = CSRFContext.getInstance().getTokenName();
		
		String userName = (String)request.getAttribute("tx_username");
		
		String comuneResidenza = (String)request.getAttribute("CR");
		String comuneDomicilio = (String)request.getAttribute("CD");
		String comuneNascita = (String)request.getAttribute("CN");
		String comuneSedeLegale = (String)request.getAttribute("CSL");
		String comuneSedeOperativa = (String)request.getAttribute("CSO");
		
		
		
		SendMailCodiciRequestType in=new SendMailCodiciRequestType();
		in.setTipoEmail(TipoEmail.AUT);
		in.setUserName(userName);
		in.setUrlSito(request.getServerName());
		in.setDescrComuneDomicilio(ProfiloUtil.getDescrizioneComune(comuneDomicilio, request));
		in.setDescrComuneNascita(ProfiloUtil.getDescrizioneComune(comuneNascita, request));
		in.setDescrComuneResidenza(ProfiloUtil.getDescrizioneComune(comuneResidenza, request));
		in.setDescrComuneSedeLegale(ProfiloUtil.getDescrizioneComune(comuneSedeLegale, request));
		in.setDescrComuneSedeOperativa(ProfiloUtil.getDescrizioneComune(comuneSedeOperativa, request));
		
		replyAttributes(true, request,"validator.message",tokenName);
		
		SendMailCodiciResponseType out;
		try {
			out = WSCache.securityServer.sendMailCodici(in, request);
			
			if (out!=null)
			{
				ResponseType responseType =out.getResponse();
				
				if (responseType.getRetCode().equals("00"))
					setFormMessage("search_form", "Reinvio Modulo Autorizzazione effettuato con successo" , request);
				else
					setFormMessage("search_form", "Errore nel reinvio del Modulo Autorizzazione" , request);
			}
			else
				setFormMessage("search_form", "Errore nel reinvio del Modulo Autorizzazione" , request);
			
		} catch (Exception e) {
			setFormMessage("search_form", "Errore nel reinvio del Modulo Autorizzazione" , request);
			e.printStackTrace();
		}

		
		
		return null;
	}
}



