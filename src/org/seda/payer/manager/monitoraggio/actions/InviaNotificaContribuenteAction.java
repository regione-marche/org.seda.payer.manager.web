package org.seda.payer.manager.monitoraggio.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.notifiche.webservice.dati.NotificaAutorizzazioneBancaResponseType;
import com.seda.payer.notifiche.webservice.dati.NotificaQuietanzaBollettiniResponseType;
import com.seda.payer.notifiche.webservice.dati.ResponseRetcode;

public class InviaNotificaContribuenteAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		String codiceTransazione = (String)request.getAttribute(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format());

//		com.seda.payer.component.notifiche.webservice.dati.NotificaAutorizzazioneBancaResponseType notificaAutorizzazioneBancaResponseType;
//		com.seda.payer.component.notifiche.webservice.dati.NotificaQuietanzaBollettiniResponseType notificaQuietanzaBollettiniResponseType;
		
		NotificaAutorizzazioneBancaResponseType notificaAutorizzazioneBancaResponseType = null;
//		com.seda.payer.component.notifiche.webservice.dati.NotificaAutorizzazioneBancaResponseType notificaAutorizzazioneBancaResponseType2;
		NotificaQuietanzaBollettiniResponseType notificaQuietanzaBollettiniResponseType = null;
		
		// MATTEO PROVA NOTIFICHE
//		notificaAutorizzazioneBancaResponseType2=WSCache.notificheServerComponent.notificaAutorizzazioneBanca(codiceTransazione, "T", "C", request);
//		notificaQuietanzaBollettiniResponseType=WSCache.notificheServer.notificaQuietanzaBollettini(codiceTransazione, "U", request);
		// FINE MATTEO PROVA
		
		String templateName="";
		
		MAFRequest mafReq = new MAFRequest(request);
		String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
		UserBean userBean = (UserBean)request.getSession().getAttribute(SignOnKeys.USER_BEAN);
		templateName = userBean.getTemplate(applicationName);
		
		
		notificaAutorizzazioneBancaResponseType=WSCache.notificheServer.notificaAutorizzazioneBanca(codiceTransazione, "T", "C", request);
		//if(!templateName.equals("aosta")) //PG170260 CT
		if(!templateName.equals("aosta") && !templateName.equals("bolzano") && !templateName.equals("bolzanoDE")) //PG180130 GG
			notificaQuietanzaBollettiniResponseType=WSCache.notificheServer.notificaQuietanzaBollettini(codiceTransazione, "U", request);
		
//		replyAttributes(true, request);
		replyAttributes(true, request,"validator.message");
		
		if (notificaAutorizzazioneBancaResponseType!=null && ((templateName.equals("aosta") || templateName.equals("bolzano") || templateName.equals("bolzanoDE")) || notificaQuietanzaBollettiniResponseType!=null))
		{
			
			ResponseRetcode responseRetcodeAutBanca=notificaAutorizzazioneBancaResponseType.getResponse().getRetcode();
			ResponseRetcode responseRetcodeQuietBoll = null;
			//if(!templateName.equals("aosta")) //PG170260 CT
			if(!templateName.equals("aosta") && !templateName.equals("bolzano") && !templateName.equals("bolzanoDE")) //PG180130 GG
				responseRetcodeQuietBoll=notificaQuietanzaBollettiniResponseType.getResponse().getRetcode();
			
//			com.seda.payer.component.notifiche.webservice.dati.ResponseRetcode responseRetcodeAutBanca=notificaAutorizzazioneBancaResponseType.getResponse().getRetcode();
//			com.seda.payer.component.notifiche.webservice.dati.ResponseRetcode responseRetcodeQuietBoll=notificaQuietanzaBollettiniResponseType.getResponse().getRetcode();
			
			if (responseRetcodeAutBanca.getValue().equals(ResponseRetcode._value1) && 
					((templateName.equals("aosta") || templateName.equals("bolzano") || templateName.equals("bolzanoDE")) || responseRetcodeQuietBoll.getValue().equals(ResponseRetcode._value1))) //PG170260 CT
				setFormMessage("monitoraggioTransazioniForm", "Invio notifica effettuato con successo" , request);
			else
				setFormMessage("monitoraggioTransazioniForm", "Errore nell'invio della notifica" , request);
		}
		else
			setFormMessage("monitoraggioTransazioniForm", "Errore nell'invio della notifica" , request);
		
		
		
		return null;
	}

}
