package org.seda.payer.manager.monitoraggio.actions;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.string.Convert;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.pgec.webservice.commons.dati.ForzaAllineamentoTransazioneRequest;
import com.seda.payer.pgec.webservice.commons.dati.ForzaAllineamentoTransazioneResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioneXmlRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioneXmlResponse;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class ForzaAllineamentoTransazioneAction extends BaseManagerAction {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//	Metodo che avrà il compito di aggiornare in base dati le informazioni dell'autorizzazione banca,
	//	dell'id Banca, della data/ora pagamento e delle note rispettivamente nei campi
	//	TRA_CTRAAUBA, TRA_CTRAIDBA, TRA_GTRADPAG e TRA_DTRANOTE della tabella
	//	PYTRATB.
	
	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		
		String codiceTransazione = request.getParameter(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format());
		request.setAttribute(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format(), codiceTransazione);

		replyAttributes(true, request,"validator.message");
		
		boolean changed = false;
		switch(getFiredButton(request))	{
			case TX_BUTTON_EDIT: 
				
				String codiceIdentificativoBanca = (String) request.getAttribute(Field.TX_CODICE_IDENTIFICATIVO_BANCA.format());
				String codiceAutorizzazioneBanca = (String) request.getAttribute(Field.TX_CODICE_AUTORIZZAZIONE_BANCA.format());
				String dataPagamento = getDataByPrefix(Field.TX_DATA_PAGAMENTO.format(),request);   
				String oraMinutiPagamento = getOraMinutiPagamento(request);
				String note = (String) request.getAttribute(Field.TX_NOTE.format());
				
				ForzaAllineamentoTransazioneResponse res1 = null;
				try {
					WSCache.logWriter.logDebug("forzatura allineamento transazione: id transazione='"+codiceTransazione+"' utente='"+user.getUserName()+"'");
					ForzaAllineamentoTransazioneRequest in = new ForzaAllineamentoTransazioneRequest(
							codiceTransazione, codiceIdentificativoBanca, codiceAutorizzazioneBanca, dataPagamento, oraMinutiPagamento, note, user.getUserName());
					res1 =  WSCache.commonsServer.forzaAllineamentoTransazione(in, request);
					changed = res1.getResponse().getRetCode().equals(ResponseTypeRetCode.value1);
				} 
				catch (Exception e) {
					setFormMessage("tx_force_form", "Forzatura allineamento transazione fallita", request);
					//setErrorMessage(e.getMessage());
					WSCache.logWriter.logError("forzatura allineamento transazione fallita: id transazione='"+codiceTransazione+"' utente='"+user.getUserName()+"'",e);
				}
				if(changed) {
					setFormMessage("tx_force_form", "Forzatura allineamento transazione eseguita con successo", request);
					//setMessage("La forzatura della transazione è stata eseguita");
					WSCache.logWriter.logDebug("forzatura allineamento transazione eseguita: id transazione='"+codiceTransazione+"' utente='"+user.getUserName()+"' esito='"+res1.getResponse().getRetMessage()+"'");
				}
				else {
					setFormMessage("tx_force_form", "Forzatura allineamento transazione fallita", request);
					//setMessage("La forzatura della transazione è fallita");
					WSCache.logWriter.logDebug("forzatura allineamento transazione fallita: id transazione='"+codiceTransazione+"' utente='"+user.getUserName()+"' esito='"+res1.getResponse().getRetMessage()+"'");
				}
				/*
				 * Carico il record relativo alla transazione selezionata
				 */
				RecuperaTransazione(codiceTransazione, request);
				break;

			case TX_BUTTON_NULL: 
				/*
				 * Carico il record relativo alla transazione selezionata
				 */
				RecuperaTransazione(codiceTransazione, request);
				break;
		}

		return null;
	}
	
	

	private String getOraMinutiPagamento(HttpServletRequest request) {
		String tx_ora_minuti_pagamento = "";
		String tx_ora_pagamento = (String) request.getAttribute(Field.TX_ORA_PAGAMENTO.format());
		String tx_minuti_pagamento = (String) request.getAttribute(Field.TX_MINUTI_PAGAMENTO.format());
		
		if((tx_ora_pagamento != null) && (!tx_ora_pagamento.equals("")) && (tx_minuti_pagamento != null) && (!tx_minuti_pagamento.equals(""))) {
			tx_ora_pagamento = ("0" + tx_ora_pagamento).substring(tx_ora_pagamento.length()-1);
			tx_minuti_pagamento = ("0" + tx_minuti_pagamento).substring(tx_minuti_pagamento.length()-1);
			tx_ora_minuti_pagamento = tx_ora_pagamento + ":" + tx_minuti_pagamento + ":00";
		}
		return tx_ora_minuti_pagamento;
	}
	
	private void RecuperaTransazione(String codiceTransazione, HttpServletRequest request){
		//inizio LP PG21XX04 Leak
		WebRowSet webRowSet = null;
		//fine LP PG21XX04 Leak
		try {
			RecuperaTransazioneXmlResponse listaRT;
			listaRT = getTransazioneXml(codiceTransazione, request);
			String lista = listaRT.getListXml();
			if (listaRT.getResponse().getRetCode().equals(ResponseTypeRetCode.value1)) {
				//inizio LP PG21XX04 Leak
				//WebRowSet webRowSet= Convert.stringToWebRowSet(lista);
				webRowSet = Convert.stringToWebRowSet(lista);
				//fine LP PG21XX04 Leak
				webRowSet.first();
				
				String codIdBanca= webRowSet.getString("TRA_CTRAIDBA");
				String codAutBanca= webRowSet.getString("TRA_CTRAAUBA");
				Timestamp dataPagamento= webRowSet.getTimestamp("TRA_GTRADPAG");
				String note = webRowSet.getString("TRA_DTRANOTE");
				
			
				Calendar cal=Calendar.getInstance();
				cal.setTimeInMillis(dataPagamento.getTime());
				int oraPagamento= cal.get(Calendar.HOUR_OF_DAY);
				int minutiPagamento = cal.get(Calendar.MINUTE);
				
				if (cal.get(Calendar.YEAR)==1000)
				{
					cal.setTimeInMillis(System.currentTimeMillis());
				}
					
				
				request.setAttribute(Field.TX_TRANSAZIONE.format(), lista);
				request.setAttribute(Field.TX_CODICE_AUTORIZZAZIONE_BANCA.format(), codAutBanca);
				request.setAttribute(Field.TX_CODICE_IDENTIFICATIVO_BANCA.format(), codIdBanca);
				request.setAttribute(Field.TX_DATA_PAGAMENTO.format(), cal);
				
				request.setAttribute(Field.TX_ORA_PAGAMENTO.format(), oraPagamento);
				request.setAttribute(Field.TX_MINUTI_PAGAMENTO.format(), minutiPagamento);
				
				request.setAttribute(Field.TX_NOTE.format(), note);
			}
			else {
				
				//setMessage(Messages.TX_NOT_FOUND.format(codiceTransazione));
				setFormMessage("tx_force_form", Messages.TX_NOT_FOUND.format(codiceTransazione), request);
			}
		} 
		catch (Exception e) {
			//setErrorMessage(e.getMessage());
			setFormMessage("tx_force_form", "Errore nel recupero della transazione", request);
		}
		//inizio LP PG21XX04 Leak
		finally {
			try {
				if(webRowSet != null) {
					webRowSet.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
	}
	
	private RecuperaTransazioneXmlResponse getTransazioneXml (String chiaveTransazione, HttpServletRequest request) throws FaultType, RemoteException  {
		RecuperaTransazioneXmlRequest in=new RecuperaTransazioneXmlRequest(chiaveTransazione);
		RecuperaTransazioneXmlResponse out;
		
		out=WSCache.commonsServer.recuperaTransazioneXml(in, request);
		return out;
	}
}
