package org.seda.payer.manager.configurazione.flows;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

//import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.configurazione.actions.GestioneAssocImpBenAction;
//import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;
//import com.seda.payer.pgec.webservice.associmpben.dati.AssocImpBenSearchRequest;
//import com.seda.payer.pgec.webservice.associmpben.dati.AssocImpBenSearchResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;


public class GestioneAssocImpBenFlow extends GestioneAssocImpBenAction implements FlowManager {

	private static final long serialVersionUID = 1L;
	private FiredButton firedButton;
	
	public void end(HttpServletRequest arg0) {
	}

	public String process(HttpServletRequest request) throws FlowException {
		switch(firedButton) 
		{
				case TX_BUTTON_RESET_INS:
				case TX_BUTTON_RESET_MOD:
					return "reset";
				case TX_BUTTON_CERCA:
					return "ricerca";
				case TX_BUTTON_AGGIUNGI:
					return "nuovo";
				case TX_BUTTON_AGGIUNGI_END:
				case TX_BUTTON_EDIT_END:
				case TX_BUTTON_DELETE_END:
					return "azione";
				case TX_BUTTON_INDIETRO:
					request.setAttribute("rowsPerPage", request.getAttribute("rRowsPerPage"));
					request.setAttribute("pageNumber", request.getAttribute("rPageNumber"));
					request.setAttribute("order", request.getAttribute("rOrder"));
					
					try {					
						esecuzioneRicerca(request, true);
					}
					catch (FaultType fte) 
					{
						WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
						fte.printStackTrace();
		//				setErrorMessage(request, fte.getMessage1());
						setFormMessage("ricercaAssocImpBenForm", "errore: " + decodeMessaggio(fte), request);
					}
					catch (RemoteException re)
					{
						WSCache.logWriter.logError("errore: " + re.getMessage(),re);
						re.printStackTrace();
		//				setErrorMessage(request, fte.getMessage1());
						setFormMessage("ricercaAssocImpBenForm", testoErroreColl, request);
						
					}
					catch (Exception e) {
						WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					//	setErrorMessage(request, e.getMessage());
						setFormMessage("ricercaAssocImpBenForm", "errore: " + testoErrore, request);
					}

					return "indietro";
				default:
					return "ricerca";
		}
		
//		return "ricerca";
		
	}

	public void start(HttpServletRequest request) {
		/*
		 * Individuo la richiesta dell'utente
		 */
		firedButton = getFiredButton(request);
		
	}
	

}