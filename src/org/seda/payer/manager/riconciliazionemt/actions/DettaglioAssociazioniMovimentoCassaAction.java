package org.seda.payer.manager.riconciliazionemt.actions;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.riconciliazionemt.bean.FlussiAbbinatiList;
import com.seda.payer.core.riconciliazionemt.bean.GiornaleDiCassa;
import com.seda.payer.core.riconciliazionemt.bean.PspList;
import com.seda.payer.core.riconciliazionemt.bean.TransazioniAbbinateList;
import com.seda.payer.core.riconciliazionemt.dao.GiornaleDiCassaDAO;
import com.seda.payer.core.riconciliazionemt.dao.GiornaleDiCassaDAOFactory;

public class DettaglioAssociazioniMovimentoCassaAction extends BaseRiconciliazioneTesoreriaAction{

	private static final long serialVersionUID = 1L;
	private long idMovimentoDiCassa;
	
	
	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		super.service(request);
		
		String idMdc = (String)request.getAttribute("idmdc");
		if (idMdc != null && idMdc != "") {
			idMovimentoDiCassa = Long.parseLong(idMdc);
		} else {
			idMovimentoDiCassa = 0;
		}
		
		setGdcParameters(request, session);
		dettaglioMovimentoCassa(request);
		
		try {
			getFlussiAbbinati(request);
			getTransazioniAbbinate(request);
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void getFlussiAbbinati(HttpServletRequest request) throws DaoException, ParseException {
		GiornaleDiCassaDAO giornaleDiCassaDAO;
		
		FlussiAbbinatiList flussiList = new FlussiAbbinatiList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(getGdcDataSource(), getGdcDbSchema());
			conn = getGdcDataSource().getConnection();
			giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(conn, getGdcDbSchema());
			//fine LP PG21XX04 Leak
			
			flussiList = giornaleDiCassaDAO.FlussiAbbinati(idMovimentoDiCassa);	
		} catch (DaoException e1) {
			e1.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		request.setAttribute("lista_flussi_abbinati", flussiList.getFlussiAbbinatiListXml());
	}
	
	private void getTransazioniAbbinate(HttpServletRequest request) throws DaoException, ParseException {
		GiornaleDiCassaDAO giornaleDiCassaDAO;
		
		TransazioniAbbinateList transazioniList = new TransazioniAbbinateList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(getGdcDataSource(), getGdcDbSchema());
			conn = getGdcDataSource().getConnection();
			giornaleDiCassaDAO = GiornaleDiCassaDAOFactory.getGiornaleDiCassaDAO(conn, getGdcDbSchema());
			//fine LP PG21XX04 Leak
			transazioniList = giornaleDiCassaDAO.TransazioniAbbinate(idMovimentoDiCassa);	
		} catch (DaoException e1) {
			e1.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
	
		request.setAttribute("lista_transazioni_abbinate", transazioniList.getTransazioniAbbinateListXml());
	}

}