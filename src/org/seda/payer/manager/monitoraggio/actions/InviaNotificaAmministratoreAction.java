package org.seda.payer.manager.monitoraggio.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.notifiche.webservice.dati.NotificaAutorizzazioneBancaResponseType;
import com.seda.payer.notifiche.webservice.dati.ResponseRetcode;

public class InviaNotificaAmministratoreAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {

		String codiceTransazione = (String)request.getAttribute(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format());
		NotificaAutorizzazioneBancaResponseType notificaAutorizzazioneBancaResponseType;
		
		notificaAutorizzazioneBancaResponseType=WSCache.notificheServer.notificaAutorizzazioneBanca(codiceTransazione, "T", "A", request);
		
//		replyAttributes(true, request);
		replyAttributes(true, request,"validator.message");
		
		if (notificaAutorizzazioneBancaResponseType!=null)
		{
			ResponseRetcode responseRetcode=notificaAutorizzazioneBancaResponseType.getResponse().getRetcode();
			
			if (responseRetcode.getValue().equals(ResponseRetcode._value1))
				setFormMessage("monitoraggioTransazioniForm", "Invio notifica effettuato con successo" , request);
			else
				setFormMessage("monitoraggioTransazioniForm", "Errore nell'invio della notifica" , request);
		}
		else
			setFormMessage("monitoraggioTransazioniForm", "Errore nell'invio della notifica" , request);
		
		
		
		return null;
	}

}
