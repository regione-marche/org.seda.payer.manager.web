package org.seda.payer.manager.monitoraggiosoap.actions;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.NotificaSoap;
import com.seda.payer.core.dao.NotificheSoap;
import com.seda.payer.core.exception.DaoException;

public class MonitoraggioSoapDettaglioAction extends BaseMonitoraggioSoapAction {

	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		
		String chiaveTransazione = request.getParameter("chiaveTransazione_hidden");
		request.setAttribute("chiaveTransazione_hidden", chiaveTransazione);
		String chiaveDettaglioTransazione = request.getParameter("chiaveDettaglioTransazione_hidden");
		request.setAttribute("chiaveDettaglioTransazione_hidden", chiaveDettaglioTransazione);
		


		resetFormMessage(request);
		
//		non serve lo switch essendo per ora un unico caso in cui entra anche con null
//		switch(getFiredButton(request)) {
//
//			case TX_BUTTON_NULL: 
//			case TX_BUTTON_CERCA:
				
				
			try (Connection conn = payerDataSource.getConnection()) {

//				Calendar calendar = Calendar.getInstance();
				NotificheSoap notificheSoap= new NotificheSoap(conn, payerDbSchema);
				NotificaSoap detailNotificaSoap = new NotificaSoap();
				
				detailNotificaSoap = notificheSoap.doDetail(chiaveTransazione,chiaveDettaglioTransazione);
				
				if ( detailNotificaSoap == null ) {
					setFormMessage("monitoraggioSoapDetailForm","Errore caricamento dettaglio notifica soap" , request);
				}
				
				request.setAttribute("dataNotificaUserFormat",'-');
				request.setAttribute("dataRispostaNotificaUserFormat",'-');
				request.setAttribute("dataPagamentoUserFormat",'-');
//				non utilizzate:
//				request.setAttribute("dataInvioNotificaAnnulloTecnicoUserFormat",'-');
//				request.setAttribute("dataRispostaNotificaAnnulloTecnicoUserFormat",'-');
				
				request.setAttribute("detail_notifica_soap", detailNotificaSoap);
				

				
				if (detailNotificaSoap.getDataNotifica() != null) {
					request.setAttribute("dataNotificaUserFormat",  tsToString(detailNotificaSoap.getDataNotifica(), "dd/MM/yyyy hh:mm:ss"));
				}

				if (detailNotificaSoap.getDataRispostaNotifica() != null) {
					request.setAttribute("dataRispostaNotificaUserFormat",  tsToString(detailNotificaSoap.getDataRispostaNotifica(), "dd/MM/yyyy hh:mm:ss"));
				}
				
				if (detailNotificaSoap.getDataPagamento() != null) {
					request.setAttribute("dataPagamentoUserFormat",  tsToString(detailNotificaSoap.getDataPagamento(), "dd/MM/yyyy hh:mm:ss"));
				}

//				non utilizzate:
//				if (detailNotificaSoap.getDataInvioNotificaAnnulloTecnico() != null ) {
//					request.setAttribute("dataInvioNotificaAnnulloTecnicoUserFormat",  tsToString(detailNotificaSoap.getDataInvioNotificaAnnulloTecnico(), "dd/MM/yyyy hh:mm:ss"));
//				} 		
//				
//				if (detailNotificaSoap.getDataRispostaNotificaAnnulloTecnico() != null ) {
//					request.setAttribute("dataRispostaNotificaAnnulloTecnicoUserFormat",  tsToString(detailNotificaSoap.getDataRispostaNotificaAnnulloTecnico(), "dd/MM/yyyy hh:mm:ss"));
//				} 
				
			} catch (DaoException e) {
				setFormMessage("monitoraggioSoapDetailForm", e.getMessage() , request);
				e.printStackTrace();
			} catch (SQLException e1) {
				setFormMessage("monitoraggioSoapDetailForm", e1.getMessage() , request);
				e1.printStackTrace();
			} catch(Exception e) {
				setFormMessage("monitoraggioSoapDetailForm", e.getMessage() , request);
				e.printStackTrace();
			}
//				break;
//		}
	
		return null;
	}

}
