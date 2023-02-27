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

public class EliminaTransazioneAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		String codiceTransazione = (String)request.getAttribute(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format());
		EliminaTransazioneResponse eliminaTransazioneResponse;
		EliminaTransazioneRequest eliminaTransazioneRequest=new EliminaTransazioneRequest();
		
		eliminaTransazioneRequest.setCodiceTransazione(codiceTransazione);
		
		//replyAttributes(true, request);
		
		
		try {
			eliminaTransazioneResponse=WSCache.commonsServer.eliminaTransazione(eliminaTransazioneRequest, request);
			
			if (eliminaTransazioneResponse!=null)
			{
				
				ResponseTypeRetCode responseRetcode=eliminaTransazioneResponse.getResponse().getRetCode();
				
				if (responseRetcode.getValue().equals(ResponseRetcode._value1))
					setFormMessage("monitoraggioTransazioniForm", "Transazione eliminata con successo" , request);
				else
					setFormMessage("monitoraggioTransazioniForm", "Errore nell'eliminazione della transazione" , request);
			}
			else
				setFormMessage("monitoraggioTransazioniForm", "Errore nell'eliminazione della transazione" , request);
			
		} catch (FaultType e) {
			e.printStackTrace();
			setFormMessage("monitoraggioTransazioniForm", "Errore nell'eliminazione della transazione" , request);
		} catch (RemoteException e) {
			e.printStackTrace();
			setFormMessage("monitoraggioTransazioniForm", "Errore nell'eliminazione della transazione" , request);
		}

		
		return null;
	}

}
