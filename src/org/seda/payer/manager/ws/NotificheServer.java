package org.seda.payer.manager.ws;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.notifiche.webservice.dati.DownloadQuietanzaBollettiniRequestType;
import com.seda.payer.notifiche.webservice.dati.DownloadQuietanzaBollettiniResponseType;
import com.seda.payer.notifiche.webservice.dati.NotificaAutorizzazioneBancaRequestType;
import com.seda.payer.notifiche.webservice.dati.NotificaAutorizzazioneBancaResponseType;
import com.seda.payer.notifiche.webservice.dati.NotificaMAVRequestType;
import com.seda.payer.notifiche.webservice.dati.NotificaMAVResponseType;
import com.seda.payer.notifiche.webservice.dati.NotificaQuietanzaBollettiniRequestType;
import com.seda.payer.notifiche.webservice.dati.NotificaQuietanzaBollettiniResponseType;
import com.seda.payer.notifiche.webservice.source.NotificheSOAPBindingStub;
import com.seda.payer.notifiche.webservice.source.NotificheServiceLocator;


public class NotificheServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NotificheSOAPBindingStub notificheCaller = null;
	static final String NOTIFY_MOD_BATCH = "B";
	/**
	 * risposta OK nel payer
	 */
	public static final String PAYER_CODE_OK = "00";
	
	private void setCodSocietaHeader(NotificheSOAPBindingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	
	public NotificheServer(String endPoint) {
		NotificheServiceLocator notificheService = new NotificheServiceLocator();
		notificheService.setNotifichePortEndpointAddress(endPoint);
		try 
		{
			// we initialize notificheStub
			notificheCaller = (NotificheSOAPBindingStub)notificheService.getNotifichePort();
		}
		catch (ServiceException e){
			e.printStackTrace();
		}
	} 
	
	public NotificaAutorizzazioneBancaResponseType notificaAutorizzazioneBanca(String codiceTransazione, String tipoNotifica, String tipoDestinatario, HttpServletRequest request) {
		setCodSocietaHeader(notificheCaller, request);
		NotificaAutorizzazioneBancaResponseType res = null;
		NotificaAutorizzazioneBancaRequestType req = new NotificaAutorizzazioneBancaRequestType();
		req.setChiave_transazione(codiceTransazione);
		req.setModalita(NOTIFY_MOD_BATCH);
		req.setTipo_destinatario(tipoDestinatario);
		String template = getTemplateCurrentApplication(request, request.getSession());
		req.setTipo_notifica(tipoNotifica+"||"+template);

		try {
			res = notificheCaller.notificaAutorizzazioneBanca(req);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}

	public NotificaQuietanzaBollettiniResponseType notificaQuietanzaBollettini(String codiceTransazione, String tipoNotifica, HttpServletRequest request) {
		setCodSocietaHeader(notificheCaller, request);
		NotificaQuietanzaBollettiniResponseType res = null;
		NotificaQuietanzaBollettiniRequestType req = new NotificaQuietanzaBollettiniRequestType();

		req.setChiave_transazione(codiceTransazione);
		req.setModalita(NOTIFY_MOD_BATCH);
		req.setTipo_notifica(tipoNotifica);

		try {
			res = notificheCaller.notificaQuietanzaBollettini(req);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}
	
	
	public DownloadQuietanzaBollettiniResponseType downloadQuietanzaBollettini(String codiceTransazione, HttpServletRequest request) {
		setCodSocietaHeader(notificheCaller, request);
		DownloadQuietanzaBollettiniResponseType res = null;
		DownloadQuietanzaBollettiniRequestType req = new DownloadQuietanzaBollettiniRequestType(codiceTransazione);
		try {
			res = notificheCaller.downloadQuietanzaBollettini(req);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}
	
	/**
	 * Recupera in base all'ID della transazione il vecchio pdf del MAV
	 * @param chiaveTransazione
	 * @throws Exception
	 */
	public boolean notificaMAVRecupero(String chiaveTransazione, HttpServletRequest request) 
	{
		setCodSocietaHeader(notificheCaller, request);
		try 
		{			
			NotificaMAVRequestType mav=new NotificaMAVRequestType();
			mav.setChiave_transazione(chiaveTransazione);
			mav.setTipo_notifica("R"); // recupero
			mav.setModalita(NOTIFY_MOD_BATCH);	// manager/batch
			mav.setNumMAV("");
			mav.setPdfBinario("");
		    
			NotificaMAVResponseType resp = notificheCaller.notificaMAV(mav);
			return resp.getResponse().getRetcode().getValue().equals(PAYER_CODE_OK);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
