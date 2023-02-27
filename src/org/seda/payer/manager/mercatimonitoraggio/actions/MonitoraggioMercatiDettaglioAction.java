package org.seda.payer.manager.mercatimonitoraggio.actions;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.mercatomanager.actions.MercatoBaseManagerAction;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.string.Convert;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.mercato.bean.MonitoraggioMercati;
import com.seda.payer.core.mercato.dao.MercatoDAOFactory;
import com.seda.payer.core.mercato.dao.MonitoraggioMercatiDAO;


import com.sun.rowset.WebRowSetImpl;



public class MonitoraggioMercatiDettaglioAction extends MercatoBaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		/*
		 * Individuo la richiesta dell'utente
		 */
		
		HttpSession sessionlocal = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		//operatore = user.getUserName();
		
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
		//recupero i dettagli dal DB
			MonitoraggioMercati monitoraggio = null;
			try {
				monitoraggio = selectMonitoraggio(request);
			} catch (DaoException e) {
		// TODO Auto-generated catch block
				e.printStackTrace();
				setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
			}
			request.setAttribute("tx_numero_autor",monitoraggio.getCodiceAutorizzazione());
			request.setAttribute("tx_nome_autorizz",monitoraggio.getNominativoAutorizzazione());
			request.setAttribute("tx_codice_fisc",monitoraggio.getCodiceFiscaleAutorizzazione());
			request.setAttribute("tx_codice_mercato",monitoraggio.getCodiceMercato());
			request.setAttribute("tx_desc_mercato", monitoraggio.getDescrizioneMercato());
			request.setAttribute("tx_cod_piazzola", monitoraggio.getCodicePiazzola());
			request.setAttribute("tx_desc_piazzola", monitoraggio.getDescrizionePiazzola());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String temp = sdf.format(monitoraggio.getDataPrenotazione().getTime());
			request.setAttribute("tx_data_prenotazione", temp);
			int tmp = 0;
			if (monitoraggio.getGiornoSettimana() == null) {
				tmp=0;
			} else {
				tmp = Integer.parseInt(monitoraggio.getGiornoSettimana());
			}
			String tmpDay="";
			switch (tmp) {
			case 1:
				tmpDay = "Lunedì";
				break;
			case 2:
				tmpDay = "Martedì";
				break;
			case 3:
				tmpDay = "Mercoledì";
				break;
			case 4:
				tmpDay = "Giovedì";
				break;
			case 5:
				tmpDay = "Venerdì";
				break;
			case 6:
				tmpDay = "Sabato";
				break;
			case 7:
				tmpDay = "Domenica";
				break;
			default:
				tmpDay = "//";
				break;
			}
			request.setAttribute("tx_giorno_sett", tmpDay);
			request.setAttribute("tx_tipo_banco", monitoraggio.getTipologiaBanco());
			request.setAttribute("tx_periodo_giorn", monitoraggio.getPeriodoGiornaliero());
			String Double="";
			Double = String.valueOf(monitoraggio.getImportoTari());
			/*Double = Double.replace(".", ",");
			String[] row1 = Double.split(",");
			if (row1[1].trim().length()<2) {
				if (row1[1].trim().length()==0) {
					Double = Double + "00";
				} else {
					Double = Double + "0";
				}
			} */
			request.setAttribute("tx_tariffa_tari", Double);
			Double="";
			Double = String.valueOf(monitoraggio.getImportoCosap());
			/*Double = Double.replace(".", ",");
			String[] row2 = Double.split(",");
			if (row2[1].trim().length()<2) {
				if (row2[1].trim().length()==0) {
					Double = Double + "00";
				} else {
					Double = Double + "0";
				}
			}*/
			request.setAttribute("tx_tariffa_cosap", Double);
			if (monitoraggio.getPagato()==null) {
				request.setAttribute("tx_flag_pagato", "NO");
			} else if (monitoraggio.getPagato().equals("Y")) {
				request.setAttribute("tx_flag_pagato", "SI");
			} else {
				request.setAttribute("tx_flag_pagato", "NO");
			}
			Double="";
			Double = String.valueOf(monitoraggio.getImportoDovuto());
			request.setAttribute("tx_impo_pagato", Double);
			Double="";
			Double = String.valueOf(monitoraggio.getImportoCompenso());
			request.setAttribute("tx_impo_compenso", Double);
			temp="";
			temp = sdf.format(monitoraggio.getDataOraPagamento().getTime());
			request.setAttribute("tx_data_pagamento", temp);

			break;
		
		case TX_BUTTON_INDIETRO:
			request.setAttribute("tx_button_cerca", "Search");	
			break;		
		}
		
		request.setAttribute("codop","Search");
		return null;
	}
	
	private MonitoraggioMercati selectMonitoraggio(HttpServletRequest request) throws DaoException {
		MonitoraggioMercatiDAO monitoraggioMercatiDAO;
		String chiaveTariffa = (String)request.getAttribute("chiave_tariffa");
		String codPrenotazione = (String)request.getAttribute("cod_prenotazione");
		
		MonitoraggioMercati monitor = new MonitoraggioMercati();
		monitor.setCodiceTariffa(chiaveTariffa);
		monitor.setCodicePrenotazione(codPrenotazione);
		//inizio LP PG21XX04 Leak
		//monitoraggioMercatiDAO = MercatoDAOFactory.getMonitoraggioMercati(getMercatoDataSource(), getMercatoDbSchema());
		//monitor = monitoraggioMercatiDAO.getPerKey(monitor);
		Connection conn = null;
		try {
			conn = getMercatoDataSource().getConnection();
			monitoraggioMercatiDAO = MercatoDAOFactory.getMonitoraggioMercati(conn, getMercatoDbSchema());
			monitor = monitoraggioMercatiDAO.getPerKey(monitor);
		} catch (Exception e) {
			throw new DaoException(e);
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
		
		return monitor;
		
	}
	
//Termine della classe
}
