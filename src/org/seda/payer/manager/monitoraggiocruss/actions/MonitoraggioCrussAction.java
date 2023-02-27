package org.seda.payer.manager.monitoraggiocruss.actions;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.monitoraggiocruss.bean.MonitoraggioCruscotto;
import com.seda.payer.core.monitoraggiocruss.dao.MonitoraggioCruscottoDAO;
import com.seda.payer.core.monitoraggiocruss.dao.MonitoraggioCruscottoDAOFactory;

public class MonitoraggioCrussAction extends BaseMonitoraggioCrussAction {
	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String dataDa = formatter.format(DateUtils.addDays(new Date(), -8)).toString();
			String dataAl = formatter.format(DateUtils.addDays(new Date(), -1)).toString();
			request.setAttribute("dataDa", dataDa);
			request.setAttribute("dataAl", dataAl);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		try {
			getMonitoraggioCruscotto(request);
		} catch (DaoException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void getMonitoraggioCruscotto(HttpServletRequest request) throws DaoException, ParseException {
		MonitoraggioCruscottoDAO monitoraggioCruscottoDAO;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dataDa = formatter.format(DateUtils.addDays(new Date(), -8)).toString();
		String dataAl = formatter.format(new Date()).toString();
		//System.out.println("dataDa: "+dataDa);
		//System.out.println("dataAl: "+dataAl);
		
		//dataDa = "2015-01-01";
		//dataAl = "2018-01-01";
		
		MonitoraggioCruscotto monitoraggioCruscotto = new MonitoraggioCruscotto();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//monitoraggioCruscottoDAO = MonitoraggioCruscottoDAOFactory.getMonitoraggioCruscottoDAO(getMonitoraggioCrussDataSource(), getMonitoraggioCrussDbSchema());
			conn = getMonitoraggioCrussDataSource().getConnection();
			monitoraggioCruscottoDAO = MonitoraggioCruscottoDAOFactory.getMonitoraggioCruscottoDAO(conn, getMonitoraggioCrussDbSchema());
			//fine LP PG21XX04 Leak
			monitoraggioCruscotto = monitoraggioCruscottoDAO.listMonitoraggioCruscotto(dataDa, dataAl);
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
		request.setAttribute("lista_pagamenti", monitoraggioCruscotto.getPagamentoList().getPagamentoListXml());
		request.setAttribute("lista_transazioni", monitoraggioCruscotto.getTransazioneList().getTransazioneListXml());
		request.setAttribute("lista_rendicontazioni", monitoraggioCruscotto.getRendicontazioneList().getRendicontazioneListXml());
		request.setAttribute("lista_notifiche", monitoraggioCruscotto.getNotificaList().getNotificaListXml());
	}
	
}
