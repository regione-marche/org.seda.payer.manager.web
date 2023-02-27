package org.seda.payer.manager.wisconfig.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.configurazione.actions.BaseConfigurazioneAction;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.ResponseTypeRetCode;
import com.seda.payer.impostasoggiorno.webservice.dati.TariffaImpostaSoggiorno;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ImpostaSoggiornoTariffaCancel extends BaseConfigurazioneAction{

//	public static String[] labelEx = {"confermaDisabilitata"};
//	public static String[] stringheE = (String[]) ArrayUtils.addAll(labelBottoni, labelEx );
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request, "impostaSoggiornoTariffa_cancel");

		switch(firedButton)
		{
		case TX_BUTTON_NULL:
    	case TX_BUTTON_CANCEL:
			break;
			
		case TX_BUTTON_DELETE:			
			request.setAttribute("confermaDisabilitata", false);
			break;

		case TX_BUTTON_DELETE_END:
			eliminaTariffa(request);
			break;
			
		case TX_BUTTON_INDIETRO:
			break;
		}

		return null;
		
	}

	private void eliminaTariffa(HttpServletRequest request)
	{
		TariffaImpostaSoggiorno tariffa = new TariffaImpostaSoggiorno();
		tariffa.setChiaveTariffa((String)request.getAttribute("tx_chiaveTar"));
		
		CancellaTariffaRequest cancelRequest = new CancellaTariffaRequest();
		cancelRequest.setTariffa(tariffa);
		try {
			CancellaTariffaResponse out = WSCache.impostaSoggiornoConfigServer.cancellaTariffa(cancelRequest, request);
			
			if(out.getRisultato().getRetCode().getValue().equals(ResponseTypeRetCode._value1))
					request.setAttribute("confermaDisabilitata", true);
	
			setFormMessage("delete_form", out.getRisultato().getRetMessage(), request);
		} catch (FaultType e) {
			setFormMessage("delete_form", "Errore: " + e.getLocalizedMessage(), request);
			e.printStackTrace();
		} catch (RemoteException e) {
			setFormMessage("delete_form", "Errore: " + e.getMessage(), request);
			e.printStackTrace();
		}
		
	}
	
	
}
