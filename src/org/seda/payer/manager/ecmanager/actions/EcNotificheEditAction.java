package org.seda.payer.manager.ecmanager.actions;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.ecmanager.actions.util.Notifica;
import org.seda.payer.manager.ecmanager.actions.util.NotificaResponseWrapper;
import org.seda.payer.manager.ecmanager.actions.util.NotificatoreHelper;
import org.seda.payer.manager.util.ManagerKeys;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.seda.commons.logger.CustomLoggerManager;
import com.seda.commons.logger.LoggerWrapper;
import com.seda.j2ee5.maf.core.action.ActionException;

@SuppressWarnings("serial")
public class EcNotificheEditAction extends AnaBollettinoManagerBaseManagerAction  {
	private static LoggerWrapper logger = CustomLoggerManager.get(EcNotificheListaAction.class);
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		
		HttpSession session = request.getSession();
		
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
			
			Notifica notifica = getDetail(request);
			request.setAttribute("notificaDetail", notifica.getMessaggio());
			
			break;
			
		case TX_BUTTON_INDIETRO:
			session.setAttribute("vista", "anagraficanotifiche");
			break;
		
		}

		return null;
	}

	private Notifica getDetail(HttpServletRequest request) {
		
		String chiaveTabellaNotifica = (String)request.getAttribute("chiaveTabellaNotifica");		
		
		Notifica notifica = new Notifica();
		
//			String urlNotificatore = "http://10.10.80.12:8280/notificatore-service";
//			String bearer = "Bearer eyJ0eXAiOiAiSldUIiwiYWxnIjogIkhTMjU2In0=.eyAiaXNzIjogIkRZVmlXbVlwdlUrTFRpWHN0SVlMZ2dpWDZYTy9ScnlRa3l2TERYUnJjMUE9IDY0NzcyNTZmLThmOWEtNGNkMi1hMWY4LWRiOGIyMDgyNDZkOSIsImp0aSI6IjcrZzlKazlyNFNDSmxIU3VrTTJQeDFWaU9MS3J1aU9BQldlYnNUQ0E5QVRvSDBacHUrUzVsbE1JMjc1ZDBLZDJzbVJiSlNGNUNUVWJwT0FLSUp3L2k5dWxQbStzNGNQU3ZrU1JadGtrZkxjPSIsImV4cCI6IjI1MzQ1MDU1MTYwMDAwMCJ9.Qz1vdxlG9uCu+q14/4mYvZSifhKnFCECHzRP0vWea/w=";
		String urlNotificatore = (String) context.getAttribute(ManagerKeys.NOTIFICHE_URL);
		String bearer = (String) context.getAttribute(ManagerKeys.NOTIFICHE_BEARER);
		
		String operationNotificatore = "/gestioneNotifiche/cercaDettaglio/"+chiaveTabellaNotifica+".json";
		try {			
			NotificatoreHelper notificatore = new NotificatoreHelper(urlNotificatore, operationNotificatore, false, "", 0);

			ResponseEntity<NotificaResponseWrapper> rssResponse = notificatore.selNotifica(notifica,bearer);
			if (rssResponse!=null)
				System.out.println("Esito chiamata a servizio json: " + rssResponse.toString());
			NotificaResponseWrapper out = rssResponse.getBody();
			if(out!=null){
				System.out.println("Status: " + out.getWebServiceOutput().getStatus());
				System.out.println("ErrorCode: " + out.getWebServiceOutput().getErrorCode());
			}
			if(rssResponse.getStatusCode()==HttpStatus.OK){
				notifica = convertRssResponseToNotifica(rssResponse);
			}

		} catch (Throwable e) {
			System.out.println("Errore in chiamata a servizio json: " + e.getCause());
		}
		return notifica;
	}

	private Notifica convertRssResponseToNotifica(ResponseEntity<NotificaResponseWrapper> rssResponse) {

		Notifica not = new Notifica();

		not.setCodiceEnte(rssResponse.getBody().getWebServiceOutput().getElement().getCodiceEnte());
		not.setCodiceSocieta(rssResponse.getBody().getWebServiceOutput().getElement().getCodiceSocieta());
		not.setCodiceUtente(rssResponse.getBody().getWebServiceOutput().getElement().getCodiceUtente());
		not.setFlagTipoMessaggio(rssResponse.getBody().getWebServiceOutput().getElement().getFlagTipoMessaggio());
		not.setFlagTipoServizio(rssResponse.getBody().getWebServiceOutput().getElement().getFlagTipoServizio());
		not.setCodiceFiscale(rssResponse.getBody().getWebServiceOutput().getElement().getCodiceFiscale());
		not.setMittente(rssResponse.getBody().getWebServiceOutput().getElement().getMittente());
		not.setDestinatario(rssResponse.getBody().getWebServiceOutput().getElement().getDestinatario());
		not.setConoscenzaNascosta(rssResponse.getBody().getWebServiceOutput().getElement().getConoscenzaNascosta());
		not.setMessaggio(rssResponse.getBody().getWebServiceOutput().getElement().getMessaggio());
		not.setDataInvio(rssResponse.getBody().getWebServiceOutput().getElement().getDataInvio());

		return not;
	}
	

	
}
