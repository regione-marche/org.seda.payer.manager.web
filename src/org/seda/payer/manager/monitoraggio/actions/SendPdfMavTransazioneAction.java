package org.seda.payer.manager.monitoraggio.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.notifiche.webservice.dati.ResponseRetcode;
import com.seda.payer.pgec.webservice.commons.dati.EliminaTransazioneRequest;
import com.seda.payer.pgec.webservice.commons.dati.EliminaTransazioneResponse;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class SendPdfMavTransazioneAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		String codiceTransazione = (String)request.getAttribute(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format());
		
		try 
		{
			if (WSCache.notificheServer.notificaMAVRecupero(codiceTransazione, request))
				setFormMessage("monitoraggioTransazioniForm", "L'invio mail, con PDF per il MAV relativo alla Transazione:"+codiceTransazione+", è stato eseguito con successo" , request);
			else
				setFormMessage("monitoraggioTransazioniForm", "Errore nell'invio mail con PDF per il MAV relativo alla Transazione, nel webservices Notifiche", request);
			
		} catch (Exception e) {
			e.printStackTrace();
			setFormMessage("monitoraggioTransazioniForm", "Errore nell'invio mail con PDF per il MAV relativo alla Transazione, errore generico "+e , request);
		}
		
		return null;
	}

}
