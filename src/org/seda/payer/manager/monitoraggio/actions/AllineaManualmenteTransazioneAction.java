package org.seda.payer.manager.monitoraggio.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.gateways.webservice.dati.AllineaManualeTransazioneResponse;
import com.seda.payer.notifiche.webservice.dati.ResponseRetcode;

public class AllineaManualmenteTransazioneAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {

		String codiceTransazione = request.getParameter(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format());
		String chiaveGateway = request.getParameter(Field.TX_CHIAVE_GATEWAY.format());
		
		AllineaManualeTransazioneResponse allineaManualeTransazioneResponse;
		
		
		allineaManualeTransazioneResponse =  WSCache.gatewaysServer.allineaManualeTransazione(codiceTransazione, chiaveGateway, request);

//		replyAttributes(true, request);
		//replyAttributes(true, request,"validator.message");
		
		if (allineaManualeTransazioneResponse!=null)
		{
			com.seda.payer.gateways.webservice.dati.ResponseTypeRetCode responseRetcode=allineaManualeTransazioneResponse.getResponse().getRetCode();
			String statoRPT=allineaManualeTransazioneResponse.getStatoRPT(); //SVILUPPO_009 SB
			
			if (responseRetcode.getValue().equals(ResponseRetcode._value1))
				setFormMessage("monitoraggioTransazioniForm", "Allineamento manuale della transazione effettuato", request);
			else
				setFormMessage("monitoraggioTransazioniForm", "Transazione non allineata per ordine non ancora contabilizzato dal gateway"+ (statoRPT.equals("")?"":": ")+statoRPT , request);	//SVILUPPO_009 SB			
		}
		else
			setFormMessage("monitoraggioTransazioniForm", "Errore nell'allineamento manuale della transazione" , request);
		
		
		
		return null;
	}

}
