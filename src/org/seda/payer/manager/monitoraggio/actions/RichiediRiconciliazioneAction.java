package org.seda.payer.manager.monitoraggio.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.gateways.webservice.dati.AllineaManualeTransazioneResponse;

public class RichiediRiconciliazioneAction extends BaseManagerAction {

	private static final long serialVersionUID = 1L;
	private String chiaveGateway;
	
public Object service(HttpServletRequest request) throws ActionException {
		
		HttpSession session = request.getSession(false);
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		String operatore = user.getUserName();
		request.getParameter(operatore);
		String codiceTransazione = request.getParameter(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format());
		request.setAttribute(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format(), codiceTransazione);
		
		switch(getFiredButton(request)) {
			case TX_BUTTON_CONFERMA_RICONCILIAZIONE: 
				try {
					WSCache.logWriter.logDebug("richiesta riconciliazione: id transazione='"+codiceTransazione+"data transazione='");
					AllineaManualeTransazioneResponse allineaManualeTransazioneResponse;
					allineaManualeTransazioneResponse = WSCache.gatewaysServer.allineaManualeTransazione(codiceTransazione, chiaveGateway, request);
					
					if(allineaManualeTransazioneResponse.getResponse() != null)
					{
						request.setAttribute("message", Messages.TX_RICHIESTA_RICONCILIAZIONE_ESITO_OK.format());
						request.setAttribute("tx_button_conferma_riconciliazione_visible", false);
					}
					else
					{
						request.setAttribute("message", Messages.TX_RICHIESTA_RICONCILIAZIONE_ESITO_KO.format());
					}
				} 
				catch (Exception e) {
					request.setAttribute("message", Messages.TX_RICHIESTA_RICONCILIAZIONE_KO.format());
					WSCache.logWriter.logError("richiesta riconciliazione fallita: id transazione='"+codiceTransazione+"data transazione='", e);
				}
				break;
	
			case TX_BUTTON_NULL: 
				request.setAttribute("tx_button_conferma_riconciliazione_visible", true);
				break;
		}
		return null;
	}
}
