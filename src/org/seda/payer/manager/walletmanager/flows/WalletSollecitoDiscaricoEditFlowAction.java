package org.seda.payer.manager.walletmanager.flows;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.walletmanager.actions.WalletSollecitoDiscaricoEditAction;
import org.seda.payer.manager.walletmanager.actions.WalletSollecitoDiscaricoEditAction.FiredButtonSollecito;

import com.seda.j2ee5.maf.core.flow.FlowException;
import com.seda.j2ee5.maf.core.flow.FlowManager;

@SuppressWarnings("serial")
public class WalletSollecitoDiscaricoEditFlowAction extends WalletSollecitoDiscaricoEditAction  implements FlowManager {

	private FiredButton firedButton;
	private FiredButtonSollecito firedButtonSollecito;

	public void end(HttpServletRequest arg0) { 
	}

	public String process(HttpServletRequest request) throws FlowException {
		String result = null;
		
		switch(firedButtonSollecito)
		{
		case TX_BUTTON_ANNULLAMENTO:
		return "cancella";
		
		case TX_BUTTON_DISCARICO:
		return "cancella";
		
		case TX_BUTTON_ANNULLA:
		return "cancella";
		
		case TX_BUTTON_DISCARICA:
			return "cancella"; 
			
		case TX_BUTTON_PRESENZE:
			return "presenze"; 
		
		case TX_BUTTON_CALENDARIO:
			return "presenze";
			
		}
		
		
		switch(firedButton)
		{
		
		case TX_BUTTON_CERCA: 
		case TX_BUTTON_RESET:
		case TX_BUTTON_SOCIETA_CHANGED:
		case TX_BUTTON_PROVINCIA_CHANGED:
		case TX_BUTTON_ENTE_CHANGED:
		case TX_BUTTON_NULL: 
			result =  "Search";
			break;
			
		case TX_BUTTON_STAMPA:
			result =  "StampaPdf";
			break;
			
		case TX_BUTTON_DOWNLOAD:
			result =  "DownloadCsv";
			break;
		
		case TX_BUTTON_EDIT:
		case TX_BUTTON_INDIETRO:
			request.setAttribute("tx_button_indietro",null);
			result =  "ritorna";
			break;
		
		case TX_BUTTON_RICHIESTA_CONFERMA:
		return "conferma";
		}
		return result;
	}

	public void start(HttpServletRequest request) {
		/*
		 * Individuo la richiesta dell'utente
		 */
		firedButton = getFiredButton(request);
		
		firedButtonSollecito= getFiredButtonSollecito(request);
	}

}

