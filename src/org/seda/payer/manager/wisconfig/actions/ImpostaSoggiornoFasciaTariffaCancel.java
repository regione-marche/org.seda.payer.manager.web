package org.seda.payer.manager.wisconfig.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.configurazione.actions.BaseConfigurazioneAction;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaFasciaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaFasciaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.ResponseTypeRetCode;
import com.seda.payer.impostasoggiorno.webservice.dati.FasciaTariffaImpostaSoggiorno;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ImpostaSoggiornoFasciaTariffaCancel extends BaseConfigurazioneAction{

	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request, "impostaSoggiornoFasciaTariffa_cancel");

		switch(firedButton)
		{
			case TX_BUTTON_NULL:
	    	case TX_BUTTON_CANCEL:
				break;

			case TX_BUTTON_DELETE:			
				request.setAttribute("confermaDisabilitata", false);
				break;

			case TX_BUTTON_DELETE_END:
				eliminaFasciaTariffa(request);
				break;

			case TX_BUTTON_INDIETRO:
				break;
		}
		return null;
	}

	private void eliminaFasciaTariffa(HttpServletRequest request)
	{
		FasciaTariffaImpostaSoggiorno fasciaTariffa = new FasciaTariffaImpostaSoggiorno();
	    Object par = request.getAttribute("tx_chiaveFasciaTar");
		Long chiaveFasciaTariffa = (par == null ? new Long(0) : new Long(par.toString()));
		fasciaTariffa.setChiaveFasciaTariffa(chiaveFasciaTariffa);
		CancellaFasciaTariffaRequest cancelRequest = new CancellaFasciaTariffaRequest();
		cancelRequest.setTariffa(fasciaTariffa);
		try {
			CancellaFasciaTariffaResponse out = WSCache.impostaSoggiornoConfigServer.cancellaFasciaTariffa(cancelRequest, request);
			
			if(out.getRisultato().getRetCode().getValue().equals(ResponseTypeRetCode._value1)) {
				request.setAttribute("confermaDisabilitata", true);
			}
			setFormMessage("delete_fascia_form", out.getRisultato().getRetMessage(), request);
		} catch (FaultType e) {
			setFormMessage("delete_fascia_form", "Errore: " + e.getLocalizedMessage(), request);
			e.printStackTrace();
		} catch (RemoteException e) {
			setFormMessage("delete_fascia_form", "Errore: " + e.getMessage(), request);
			e.printStackTrace();
		}
	}
}
