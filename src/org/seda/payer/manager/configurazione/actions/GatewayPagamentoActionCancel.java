package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoCancelRequest;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.Response;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.ResponseReturncode;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.StatusResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class GatewayPagamentoActionCancel extends BaseManagerAction{
	private static String codop = "cancel";
	private static String ritornaViewstate = "Gatewaypagamentos_search";

	public Object service(HttpServletRequest request) throws ActionException {
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
		case TX_BUTTON_NULL:
			/*
			 * Recupero la chiave del record per verifica
			 */
			String chiaveGateway = (request.getAttribute("chiaveGateway") == null ? "" : (String)request.getAttribute("chiaveGateway"));
			if (!chiaveGateway.equals(""))
			{
				setFormMessage("delete_form", Messages.CANCELLA_O_ABBANDONA.format(), request);
			}
			else
			{
				request.setAttribute("confermaDisabilitata", true);
				setFormMessage("delete_form", Messages.DATI_NON_VALIDI.format(), request);
			}
			break;
			
		case TX_BUTTON_DELETE:
			chiaveGateway = (request.getAttribute("chiaveGateway") == null ? "" : (String)request.getAttribute("chiaveGateway"));			
			GatewayPagamentoCancelRequest cancelRequest = new GatewayPagamentoCancelRequest(chiaveGateway);
			
			try {
				StatusResponse cancelResponse = WSCache.gatewayPagamentoServer.cancel(cancelRequest, request);
						
				if (cancelResponse != null)
				{
					Response response = cancelResponse.getResponse();
					if (response != null)
					{
						if (response.getReturncode().getValue().equals(ResponseReturncode.value1.getValue()))
						{
							request.setAttribute("confermaDisabilitata", true);
							setFormMessage("delete_form", Messages.CANC_OK.format(), request);
						}
						else 
							setFormMessage("delete_form", response.getReturnmessage(), request);
					}
					else setFormMessage("delete_form", Messages.GENERIC_ERROR.format(), request);
				}
				else setFormMessage("delete_form", Messages.GENERIC_ERROR.format(), request);
			} catch (FaultType e) {
				setFormMessage("delete_form", "Errore: " + Messages.CANCEL_ERRDIP.format(), request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("delete_form", "Errore: " + e.getMessage(), request);
				e.printStackTrace();
			}
			
			break;
			
		case TX_BUTTON_INDIETRO:
			request.setAttribute("vista",ritornaViewstate);
			break;
		}
		request.setAttribute("codop",codop);
		return null;
		
	}

}

