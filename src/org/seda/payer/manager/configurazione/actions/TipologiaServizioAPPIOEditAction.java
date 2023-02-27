package org.seda.payer.manager.configurazione.actions;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.core.security.UserBeanSupport;
import com.seda.payer.core.bean.TipologiaServizioAPPIO;
import com.seda.payer.core.dao.TipologiaServizioAPPIODao;
import com.seda.payer.core.exception.DaoException;
public class TipologiaServizioAPPIOEditAction extends TipologiaServiziAPPIOBaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

    private java.lang.String codiceTipologiaServizio;
    private java.lang.String descrizioneTipologiaServizio;
    private java.lang.String codiceOperatore;

	private String codop = "";

	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		FiredButton firedButton = getFiredButton(request);

		UserBeanSupport userBean = (UserBeanSupport)session.getAttribute(SignOnKeys.USER_BEAN);
		codop = (String)request.getAttribute("codop");
		if(codop != null) {
			if (codop.equalsIgnoreCase("edit")) {
				
				String codice = request.getParameter("tx_codice");
				
				try (Connection conn = payerDataSource.getConnection()) {
					
					TipologiaServizioAPPIODao tipologiaServizioAPPIODao = new TipologiaServizioAPPIODao(conn, payerDbSchema);
					TipologiaServizioAPPIO tipologiaServizioAPPIO = 
							tipologiaServizioAPPIODao.detailTipologiaAPPIO(codice);
					
					session.setAttribute("up_codiceTipologiaServizi_old", request.getParameter("tx_codice"));
					session.setAttribute("up_descrizioneTipologiaServizi_old", tipologiaServizioAPPIO.getDescrizioneTipologiaServizio());
					session.setAttribute("up_codiceOperatore_old", tipologiaServizioAPPIO.getCodiceOperatore());

					request.setAttribute("up_codiceTipologiaServizi", tipologiaServizioAPPIO.getCodiceTipologiaServizio());
					request.setAttribute("up_descrizioneTipologiaServizi", tipologiaServizioAPPIO.getDescrizioneTipologiaServizio());
					request.setAttribute("up_codiceOperatore", tipologiaServizioAPPIO.getCodiceOperatore());

				} catch (DaoException e) {

					session.setAttribute("tx_error_message", e.getMessage());
					
					e.printStackTrace();
				} catch (SQLException e1) {

					session.setAttribute("tx_error_message", e1.getMessage());
					e1.printStackTrace();
				}
				
			}
		}
		
		switch(firedButton) {
			case TX_BUTTON_RESET:
				if(session.getAttribute("codop") != null && session.getAttribute("codop").equals("edit")) {
					request.setAttribute("up_codiceTipologiaServizi",
							session.getAttribute("up_codiceTipologiaServizi_old"));
					request.setAttribute("up_descrizioneTipologiaServizi",
									session.getAttribute("up_descrizioneTipologiaServizi_old"));
					request.setAttribute("up_codiceOperatore",
							session.getAttribute("up_codiceOperatore_old"));
					
				} else {
					resetParamsSessioneRequest(session, request);
					session.setAttribute("codop", null);
				}
				break;
			case TX_BUTTON_NULL:
				if(request.getAttribute("codop").equals("edit")) {
					session.setAttribute("codop", request.getAttribute("codop"));
				}
				break;
			case TX_BUTTON_EDIT:
				
				int nRigheEdit = 0;
				
				codiceTipologiaServizio = (String) request.getAttribute("up_codiceTipologiaServizi");
				descrizioneTipologiaServizio = (String) request.getAttribute("up_descrTipologiaServizi");
				codiceOperatore = userBean.getName();
				
				try (Connection conn = payerDataSource.getConnection()) {
					if (conn.getAutoCommit())
						conn.setAutoCommit(false);
					TipologiaServizioAPPIODao tipologiaServizioAPPIODao = new TipologiaServizioAPPIODao(conn, payerDbSchema);
					TipologiaServizioAPPIO tipologiaServizioAPPIO = tipologiaServizioAPPIODao.detailTipologiaAPPIO(codiceTipologiaServizio);
					
					tipologiaServizioAPPIO= new TipologiaServizioAPPIO();
					tipologiaServizioAPPIO.setCodiceTipologiaServizio(codiceTipologiaServizio);
					tipologiaServizioAPPIO.setDescrizioneTipologiaServizio(descrizioneTipologiaServizio);
					tipologiaServizioAPPIO.setCodiceOperatore(codiceOperatore);
					nRigheEdit = tipologiaServizioAPPIODao.updateTipologiaServizio(tipologiaServizioAPPIO);
					
					conn.commit();
					if(nRigheEdit != 0) {
						request.setAttribute("tx_message", "Tipologia aggiornata correttamente.");
						session.setAttribute("codop", "add");
					}
				} catch (DaoException | SQLException e) {
					request.setAttribute("tx_message","Errore:"+e.getMessage());
					e.printStackTrace();
				} 
				break;
			case TX_BUTTON_AGGIUNGI:
				
				codiceTipologiaServizio = (String) request.getAttribute("up_codiceTipologiaServizi");
				descrizioneTipologiaServizio = (String) request.getAttribute("up_descrTipologiaServizi");
				codiceOperatore = userBean.getName();
						
//				(String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
				
				String Addcodice = "";
		
				try (Connection conn = payerDataSource.getConnection()) {
					if (conn.getAutoCommit())
						conn.setAutoCommit(false);
					TipologiaServizioAPPIODao tipologiaServizioAPPIODao = new TipologiaServizioAPPIODao(conn, payerDbSchema);
					TipologiaServizioAPPIO tipologiaServizioAPPIO = tipologiaServizioAPPIODao.detailTipologiaAPPIO(codiceTipologiaServizio);
					
					if(tipologiaServizioAPPIO != null) {
						request.setAttribute("tx_error_message", "Errore: Tipologia gi&agrave; esistente.");
					}else {
						
						tipologiaServizioAPPIO= new TipologiaServizioAPPIO();
						tipologiaServizioAPPIO.setCodiceTipologiaServizio(codiceTipologiaServizio);
						tipologiaServizioAPPIO.setDescrizioneTipologiaServizio(descrizioneTipologiaServizio);
						tipologiaServizioAPPIO.setCodiceOperatore(codiceOperatore);
						Addcodice = tipologiaServizioAPPIODao.insertTipologiaServizio(tipologiaServizioAPPIO);
					}
					conn.commit();
					if(Addcodice != "") {
						request.setAttribute("tx_message", "Tipologia inserita correttamente.");
						session.setAttribute("codop", "add");
					}
				} catch (DaoException | SQLException e) {
					request.setAttribute("tx_message","Errore:"+e.getMessage());
					e.printStackTrace();
				} 
				
				break;
			case TX_BUTTON_INDIETRO:
				session.setAttribute("codop", null);
				session.setAttribute("up_codiceTipologiaServizi", null);
				session.setAttribute("up_descrizioneTipologiaServizi", null);
				session.setAttribute("up_codiceOperatore", null);
				

				session.setAttribute("up_codiceTipologiaServizi_old", null);
				session.setAttribute("up_descrizioneTipologiaServizi_old", null);
				session.setAttribute("up_codiceOperatore_old", null);
				
				
				//resetParamsSessioneRequest(session, request);
				break;
			case TX_BUTTON_CANCEL:
				break;
			case TX_BUTTON_DELETE:
				break;
			default:
				break;
		}
				
		return null;
	}

	private void resetParamsSessioneRequest(HttpSession session, HttpServletRequest request) {
		request.setAttribute("up_codiceTipologiaServizi", null);
		session.setAttribute("up_codiceTipologiaServizi", null);

		request.setAttribute("up_descrTipologiaServizi", null);
		session.setAttribute("up_descrTipologiaServizi", null);
		
		request.setAttribute("up_codiceOperatore", null);
		session.setAttribute("up_codiceOperatore", null);
			
		}
	
	
}


