package org.seda.payer.manager.monitoraggio.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.pgec.webservice.commons.dati.ModificaEmailSmsRequest;
import com.seda.payer.pgec.webservice.commons.dati.ModificaEmailSmsResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioneXmlRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioneXmlResponse;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class ModificaEmailSmsAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		
		boolean changed = false;
		HttpSession session = request.getSession(false);
		
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		
		String codiceTransazione = request.getParameter(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format());
		
		request.setAttribute(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format(), codiceTransazione);
		
		String newUserEmail = request.getParameter(Field.TX_NEW_USER_EMAIL.format());
		String newUserSms = request.getParameter(Field.TX_NEW_USER_SMS.format());
		
		switch(getFiredButton(request)) {
			case TX_BUTTON_EDIT: 
				ModificaEmailSmsResponse resMod = null;
//				if(!"".equals(newUserEmail) && !"".equals(newUserSms)) {
					try {
						WSCache.logWriter.logDebug("modifica email ed sms: id transazione='"+codiceTransazione+"' utente='"+user.getUserName()+"'");
						ModificaEmailSmsRequest in = new ModificaEmailSmsRequest(codiceTransazione, newUserEmail, newUserSms, user.getUserName());
						resMod =  WSCache.commonsServer.modificaEmailSms(in, request);
						changed = resMod.getResponse().getRetCode().equals(ResponseTypeRetCode.value1);
					} 
					catch (Exception e) {
						setFormMessage("frmUpdateEmailSms", Messages.TX_EDIT_EMAIL_SMS_KO.format(), request);
						//setErrorMessage(e.getMessage());
						WSCache.logWriter.logError("modifica email ed sms fallita: id transazione='"+codiceTransazione+"' utente='"+user.getUserName()+"'", e);
					}
//				}
				if(changed) {
//					setMessage(Messages.TX_EDIT_EMAIL_SMS_OK.format());
					setFormMessage("frmUpdateEmailSms", Messages.TX_EDIT_EMAIL_SMS_OK.format(), request);
					WSCache.logWriter.logDebug("modifica email ed sms eseguita: id transazione='"+codiceTransazione+"' utente='"+user.getUserName()+"' esito='"+resMod.getResponse().getRetMessage()+"'");
				}
				else {
//					setMessage(Messages.TX_EDIT_EMAIL_SMS_KO.format());
					setFormMessage("frmUpdateEmailSms", Messages.TX_EDIT_EMAIL_SMS_KO.format(), request);
					WSCache.logWriter.logDebug("modifica email ed sms fallita: id transazione='"+codiceTransazione+"' utente='"+user.getUserName()+"' esito='"+resMod.getResponse().getRetMessage()+"'");
				}
				/*
				 * Carico il record relativo alla transazione selezionata
				 */
				RecuperaTransazione(codiceTransazione, request);
				break;

			case TX_BUTTON_NULL: 
				request.setAttribute(Field.TX_NEW_USER_EMAIL.format(), newUserEmail);	
				request.setAttribute(Field.TX_NEW_USER_SMS.format(), newUserSms);	
				/*
				 * Carico il record relativo alla transazione selezionata
				 */
				RecuperaTransazione(codiceTransazione, request);
				break;
		}
		
	
//		request.setAttribute(Field.TX_MESSAGE.format(), getMessage());
//		request.setAttribute(Field.TX_ERROR_MESSAGE.format(), getErrorMessage());
		return null;
	}
	
	
	private void RecuperaTransazione(String codiceTransazione, HttpServletRequest request){
		try {
			RecuperaTransazioneXmlResponse listaRT;
			//listaRT = getListaTransazioni(1, 1, "", "","","","","", codiceTransazione, "", "", "", "","","","","", "", "", "", "", "", "",0);
			listaRT = getTransazioneXml(codiceTransazione, request);
			String lista = listaRT.getListXml();
			if (listaRT.getResponse().getRetCode().equals(ResponseTypeRetCode.value1)) {
				request.setAttribute(Field.TX_TRANSAZIONE.format(), lista);
			}
			else {
//				setMessage(Messages.TX_NOT_FOUND.format(codiceTransazione));
				setFormMessage("frmUpdateEmailSms", Messages.TX_NOT_FOUND.format(codiceTransazione), request);
			}
		} 
		catch (Exception e) {
			setErrorMessage(e.getMessage());
		}
		
	}
	
	private RecuperaTransazioneXmlResponse getTransazioneXml (String chiaveTransazione, HttpServletRequest request) throws FaultType, RemoteException  {
		RecuperaTransazioneXmlRequest in=new RecuperaTransazioneXmlRequest(chiaveTransazione);
		RecuperaTransazioneXmlResponse out;
		
		out=WSCache.commonsServer.recuperaTransazioneXml(in, request);
		return out;
	}
	
	/*
	private RecuperaTransazioniResponseType getListaTransazioni(int rowsPerPage, int pageNumber, String order, 
			String tx_societa, String tx_provincia, String tx_utente, String tx_ente, String tx_mostra, String tx_codice_transazione,
			String tx_indirizzo_ip, String tx_user_email, String tx_user_sms, String tx_canale_pagamento,
			String tx_strumento, String tx_tipo_carta, String tx_id_bollettino, String tx_servizio,
			String tx_stato_rendicontazione, String tx_stato_riconciliazione,
			String tx_importo_da, String tx_importo_a, String tx_data_da,String tx_data_a,int chiaveQuadratura) throws FaultType, RemoteException {

		RecuperaTransazioneXmlRequest in=new RecuperaTransazioneXmlRequest();
		
		WSCache.commonsServer.recuperaTransazioneXml(in);
		
		
		RecuperaTransazioniRequestType recuperaTransazioniRequest = new RecuperaTransazioniRequestType();
		recuperaTransazioniRequest.setOrder(order);
		recuperaTransazioniRequest.setPageNumber(pageNumber);
		recuperaTransazioniRequest.setRowsPerPage(rowsPerPage);
		recuperaTransazioniRequest.setTx_societa(tx_societa);
		recuperaTransazioniRequest.setTx_provincia(tx_provincia);
		recuperaTransazioniRequest.setTx_utente(tx_utente);
		recuperaTransazioniRequest.setTx_ente(tx_ente);
		recuperaTransazioniRequest.setTx_mostra(tx_mostra);
		recuperaTransazioniRequest.setTx_codice_transazione(tx_codice_transazione);
		recuperaTransazioniRequest.setTx_indirizzo_ip(tx_indirizzo_ip);
		recuperaTransazioniRequest.setTx_user_email(tx_user_email);
		recuperaTransazioniRequest.setTx_user_sms(tx_user_sms);
		recuperaTransazioniRequest.setTx_canale_pagamento(tx_canale_pagamento);
		recuperaTransazioniRequest.setTx_strumento(tx_strumento);
		recuperaTransazioniRequest.setTx_tipo_carta(tx_tipo_carta);
		recuperaTransazioniRequest.setTx_id_bollettino(tx_id_bollettino);
		recuperaTransazioniRequest.setTx_servizio(tx_servizio);
		recuperaTransazioniRequest.setTx_stato_rendicontazione(tx_stato_rendicontazione);
		recuperaTransazioniRequest.setTx_stato_riconciliazione(tx_stato_riconciliazione);
		recuperaTransazioniRequest.setTx_importo_da(tx_importo_da);
		recuperaTransazioniRequest.setTx_importo_a(tx_importo_a);
		recuperaTransazioniRequest.setTx_data_da(tx_data_da);
		recuperaTransazioniRequest.setTx_data_a(tx_data_a);
		recuperaTransazioniRequest.setTx_chiave_quadratura(chiaveQuadratura);
		return WSCache.commonsServer.recuperaTransazioni(recuperaTransazioniRequest);
	}
*/
}
