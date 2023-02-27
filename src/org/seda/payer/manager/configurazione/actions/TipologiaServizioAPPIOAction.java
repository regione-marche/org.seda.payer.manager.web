package org.seda.payer.manager.configurazione.actions;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.bean.TipologiaServizioAPPIO;
import com.seda.payer.core.bean.TipologiaServizioAPPIOList;
import com.seda.payer.core.dao.TipologiaServizioAPPIODao;
import com.seda.payer.core.exception.DaoException;

public class TipologiaServizioAPPIOAction extends TipologiaServiziAPPIOBaseManagerAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5346158422873032763L;
	protected String descrizioneTipologiaServizi= ""; 
	protected String codiceTipologiaServizi= ""; 
		
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		FiredButton firedButton = getFiredButton(request);
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) {
			setProfile(request);
		}
		if(firedButton.equals(FiredButton.TX_BUTTON_EDIT) || firedButton.equals(FiredButton.TX_BUTTON_INDIETRO)){
			descrizioneTipologiaServizi = (String) request.getAttribute("tx_descrizioneTipologiaServizi");
			codiceTipologiaServizi = (String) request.getAttribute("tx_codiceTipologiaServizi");

//			cutecute = (String) session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);	
		}
		switch(firedButton)
		{
			case TX_BUTTON_DELETE:
				
				delete(session,request);
				resetParamsSessioneRequest(session, request);
			
			case TX_BUTTON_AGGIUNGI:
			case TX_BUTTON_EDIT:
			case TX_BUTTON_INDIETRO:
				
				if(descrizioneTipologiaServizi == null || descrizioneTipologiaServizi.equals("")){ 
					descrizioneTipologiaServizi = (String) session.getAttribute("tx_descrizioneTipologiaServizi");
				}
				if(codiceTipologiaServizi == null || codiceTipologiaServizi.equals("")) {
					codiceTipologiaServizi = (String) session.getAttribute("tx_codiceTipologiaServizi");
				}
				
				search(session,request);
								
				break;
			case TX_BUTTON_CERCA: 
				mantieniFiltriRicerca(request);
				descrizioneTipologiaServizi = (String) session.getAttribute("tx_descrizioneTipologiaServizi");
				codiceTipologiaServizi = (String) session.getAttribute("tx_codiceTipologiaServizi");

				search(session,request);
				break;
					
			case TX_BUTTON_RESET:
				resetParamsSessioneRequest(session, request);
				setProfile(request);				
				break;
				
			case TX_BUTTON_NULL: 
				session.setAttribute("codop", null);
				resetParamsSessioneRequest(session, request);
				break;
			
			default:
				break;
		}
		
		return null;
	}
	
	private void resetParamsSessioneRequest(HttpSession session, HttpServletRequest request) {

		request.setAttribute("tx_codiceTipologiaServizi", null);
		session.setAttribute("tx_codiceTipologiaServizi", null);

		request.setAttribute("tx_descrizioneTipologiaServizi", null);
		session.setAttribute("tx_descrizioneTipologiaServizi", null);
		
		request.setAttribute("tx_codice", null);
		session.setAttribute("tx_codice", null);

		
		
	}
	
	private void delete(HttpSession session,HttpServletRequest request) {
		
		String codice = request.getParameter("tx_codice");
		try (Connection conn = payerDataSource.getConnection()) {
			if (conn.getAutoCommit())
				conn.setAutoCommit(false);
			TipologiaServizioAPPIODao tipologiaServizioAPPIODao = new TipologiaServizioAPPIODao(conn, payerDbSchema);
			tipologiaServizioAPPIODao.deleteTipologiaServizioAPPIO(codice);
			request.setAttribute("tx_message", "Tipologia cancellata con successo.");
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("tx_error_message", e.getMessage());
		}
	}
	private void search(HttpSession session,HttpServletRequest request) {
		
		try (Connection conn = payerDataSource.getConnection()) {
			
			TipologiaServizioAPPIODao tipologiaServizioAPPIODao = new TipologiaServizioAPPIODao(conn, payerDbSchema);
			TipologiaServizioAPPIO tipologiaServizioAPPIO = new TipologiaServizioAPPIO();
			
			tipologiaServizioAPPIO.setCodiceTipologiaServizio(codiceTipologiaServizi);
			tipologiaServizioAPPIO.setDescrizioneTipologiaServizio(descrizioneTipologiaServizi);
			
			TipologiaServizioAPPIOList list = tipologiaServizioAPPIODao.tipologiaServizioAPPIOList(
					tipologiaServizioAPPIO, pageNumber, rowsPerPage, order);
					
			request.setAttribute("lista_tipologieServiziAPPIO", list.getTipologiaServizioAPPIOListXml());
			request.setAttribute("lista_tipologieServiziAPPIO.pageInfo", list);
			
							
		} catch (DaoException e) {

			session.setAttribute("tx_error_message", e.getMessage());
			
			e.printStackTrace();
		} catch (SQLException e1) {

			session.setAttribute("tx_error_message", e1.getMessage());
			e1.printStackTrace();
		}
	}
	
	
	private void mantieniFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		if(request.getAttribute("tx_codiceTipologiaServizi")!=null)
			session.setAttribute("tx_codiceTipologiaServizi", request.getAttribute("tx_codiceTipologiaServizi"));

		if(request.getAttribute("tx_descrizioneTipologiaServizi")!=null)
			session.setAttribute("tx_descrizioneTipologiaServizi", request.getAttribute("tx_descrizioneTipologiaServizi"));
	
	}
	
}
