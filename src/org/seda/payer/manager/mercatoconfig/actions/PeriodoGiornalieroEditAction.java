package org.seda.payer.manager.mercatoconfig.actions;

import java.sql.Connection;
import java.sql.SQLException;
//import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.mercatomanager.actions.MercatoBaseManagerAction;

//import com.seda.commons.security.TokenGenerator;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.mercato.bean.ConfigurazionePeriodoGiornaliero;
import com.seda.payer.core.mercato.bean.EsitoRisposte;
import com.seda.payer.core.mercato.dao.ConfigurazionePeriodoGiornalieroDAO;
import com.seda.payer.core.mercato.dao.MercatoDAOFactory;


@SuppressWarnings("serial")
public class PeriodoGiornalieroEditAction extends MercatoBaseManagerAction{
	private static String codop = "edit";
	private static String ritornaViewstate = "mercatomanager";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;
	private String codiceSocieta = "";
	private String codiceUtente = "";
	private String chiaveEnte = "";
	private String operatore = "";
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		
		HttpSession session = request.getSession();
		/*
		 * Individuo la richiesta dell'utente
		 */
		
		HttpSession sessionlocal = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		operatore = user.getUserName();
		
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
			/*
			 * Carico i valori dal WS
			 */
			if (request.getAttribute("codop").equals("edit")) {
				codop = "edit";
			} else {
				if (request.getAttribute("codop").equals("add")) {
					codop="add";
				}
			}
			
			if (codop.equals("edit")) {
				String chiaverecord="";
				if (request.getAttribute("tx_periodo_giorn")==null) {
					chiaverecord = (String)request.getAttribute("tx_periodo_giorn_h");
				} else {
					chiaverecord = (String)request.getAttribute("tx_periodo_giorn");
				}
				dividiSocUtenteEnte(request);
				request.setAttribute("tx_periodo_giorn_h",chiaverecord);
			
				if ( !(chiaverecord == null) ) {
					ConfigurazionePeriodoGiornaliero  configurazionePeriodoGiornaliero = null; 
					try {
						configurazionePeriodoGiornaliero = selectConfigurazionePeriodoGiornaliero(request);
					} catch (DaoException e) {
					// TODO Auto-generated catch block
						e.printStackTrace();
						setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
					}
					request.setAttribute("tx_periodo_giorn", configurazionePeriodoGiornaliero.getCodicePeriodoGiornaliero());
					request.setAttribute("tx_descr_per_giorn", configurazionePeriodoGiornaliero.getDescrizionePeriodoGiornaliero());
				}
			} else {
				if (firedButton.equals(FiredButton.TX_BUTTON_RESET)) {
					request.setAttribute("tx_periodo_giorn", "");
					request.setAttribute("tx_descr_per_giorn", "");
				}
			}
			
			break;
			
		case TX_BUTTON_EDIT:
			try {
				updateTipologiaBanco(request);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
			}
			session.setAttribute("recordUpdate", "OK");
			break;
		
		case TX_BUTTON_ADD:
			codop = "add";
			break;
			
		case TX_BUTTON_AGGIUNGI:
			try {
				dividiSocUtenteEnte(request);
				EsitoRisposte esito = new EsitoRisposte();
				esito = inserisciPeriodoGiornaliero(request);
				if(esito.getCodiceMessaggio().equals("OK")) {
					//session.setAttribute("recordInsert", "OK");
					setFormMessage("var_form", Messages.INS_OK.format(), request);
					request.setAttribute("tx_periodo_giorn", "");
					request.setAttribute("tx_descr_per_giorn", "");					
				} else {
					//session.setAttribute("recordInsert", "KO");
					//session.setAttribute("messaggio.recordInsert", esito.getDescrizioneMessaggio());
					setFormMessage("var_form", esito.getDescrizioneMessaggio(), request);
				}
			}
			catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
			}
			break;
			
		case TX_BUTTON_INDIETRO:
			request.setAttribute("tx_button_cerca", "Search");	
			break;
		
		case TX_BUTTON_CANCEL:
			dividiSocUtenteEnte(request);
			String chiaveRecord = (String)request.getAttribute("tx_periodo_giorn");
			request.setAttribute("tx_periodo_giorn_h",chiaveRecord);
			request.setAttribute("tx_button_cancel", "cancella");
			break;
			
		case TX_BUTTON_DELETE:
			try 
				{
					EsitoRisposte esito = new EsitoRisposte();
					esito = deletePeriodoGiornaliero(request);
					if(esito.getCodiceMessaggio().equals("OK"))
					{
						session.setAttribute("recordDelete", "OK");
					}
					else
					{
						session.setAttribute("recordDelete", "KO");
						session.setAttribute("messaggio.recordDelete", esito.getDescrizioneMessaggio());
					}
				}
			catch (DaoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						setFormMessage("form_selezione", "Errore: " + e.getLocalizedMessage(), request);
					}
			break;
			
		}
		if (!firedButton.equals(FiredButton.TX_BUTTON_INDIETRO))
		{	
			loadSocietaUtenteEnteXml_DDL(request, session);
		}
		/*
		 * Setto l'action della form ed il tipo di operazione
		 */
		request.setAttribute("codop",codop);
		return null;
	}

	private void dividiSocUtenteEnte(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
		if ((ddlSocietaUtenteEnte==null)||(ddlSocietaUtenteEnte.equals(""))) {
			if (request.getAttribute("tx_societa")==null) {
				//
			} else {
				ddlSocietaUtenteEnte = request.getAttribute("tx_societa") + "|" + request.getAttribute("tx_utente") +"|" + request.getAttribute("tx_ente");
				request.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);
				String[] codici = ddlSocietaUtenteEnte.split("\\|");
				codiceSocieta = codici[0];
				codiceUtente = codici[1];
				chiaveEnte = codici[2];
				request.setAttribute("tx_societa", codiceSocieta);
				request.setAttribute("tx_utente", codiceUtente);
				request.setAttribute("tx_ente", chiaveEnte);
			}
		} else {
			String[] codici = ddlSocietaUtenteEnte.split("\\|");
			codiceSocieta = codici[0];
			codiceUtente = codici[1];
			chiaveEnte = codici[2];
			request.setAttribute("tx_societa", codiceSocieta);
			request.setAttribute("tx_utente", codiceUtente);
			request.setAttribute("tx_ente", chiaveEnte);
		}
	}
	
	private ConfigurazionePeriodoGiornaliero selectConfigurazionePeriodoGiornaliero(HttpServletRequest request) throws DaoException {
		
		String codicePerGiorn  = (String)request.getAttribute("tx_periodo_giorn_h");
		ConfigurazionePeriodoGiornalieroDAO configurazionePeriodoGiornalieroDAO;
		ConfigurazionePeriodoGiornaliero configurazionePeriodoGiornaliero = new ConfigurazionePeriodoGiornaliero();
		configurazionePeriodoGiornaliero.setCodiceSocieta(codiceSocieta);
		configurazionePeriodoGiornaliero.setCuteCute(codiceUtente);
		configurazionePeriodoGiornaliero.setChiaveEnte(chiaveEnte);
		configurazionePeriodoGiornaliero.setCodicePeriodoGiornaliero(codicePerGiorn);
		//inizio LP PG21XX04 Leak
		//configurazionePeriodoGiornalieroDAO = MercatoDAOFactory.getConfigurazionePeriodoGiornaliero(getMercatoDataSource(), getMercatoDbSchema());
		//configurazionePeriodoGiornaliero = configurazionePeriodoGiornalieroDAO.select(configurazionePeriodoGiornaliero);
		//
		//return configurazionePeriodoGiornaliero;
		Connection conn = null;
		try {
			conn = getMercatoDataSource().getConnection();
			configurazionePeriodoGiornalieroDAO = MercatoDAOFactory.getConfigurazionePeriodoGiornaliero(conn, getMercatoDbSchema());
			configurazionePeriodoGiornaliero = configurazionePeriodoGiornalieroDAO.select(configurazionePeriodoGiornaliero);
			
			return configurazionePeriodoGiornaliero;
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
	}
	
	private Integer updateTipologiaBanco(HttpServletRequest request) throws DaoException {
		
		String  societa = (String)request.getAttribute("tx_societa");
		String  utente = (String)request.getAttribute("tx_utente");
		String  ente = (String)request.getAttribute("tx_ente");
		String codicePerGiorn = (String)request.getAttribute("tx_periodo_giorn_h");
		String descrPerGiorn = (String)request.getAttribute("tx_descr_per_giorn");


		ConfigurazionePeriodoGiornalieroDAO configurazionePeriodoGiornalieroDAO;
		
		ConfigurazionePeriodoGiornaliero configurazionePeriodoGiornaliero = new ConfigurazionePeriodoGiornaliero();
		configurazionePeriodoGiornaliero.setCodiceSocieta(societa);
		configurazionePeriodoGiornaliero.setCuteCute(utente);
		configurazionePeriodoGiornaliero.setChiaveEnte(ente);
		configurazionePeriodoGiornaliero.setCodicePeriodoGiornaliero(codicePerGiorn);
		configurazionePeriodoGiornaliero.setDescrizionePeriodoGiornaliero(descrPerGiorn);
		
		//inizio LP PG21XX04 Leak
		//configurazionePeriodoGiornalieroDAO = MercatoDAOFactory.getConfigurazionePeriodoGiornaliero(getMercatoDataSource(), getMercatoDbSchema());
		//int aggiornati = configurazionePeriodoGiornalieroDAO.update(configurazionePeriodoGiornaliero);
		//return aggiornati;
		Connection conn = null;
		try {
			conn = getMercatoDataSource().getConnection();
			configurazionePeriodoGiornalieroDAO = MercatoDAOFactory.getConfigurazionePeriodoGiornaliero(conn, getMercatoDbSchema());
			int aggiornati = configurazionePeriodoGiornalieroDAO.update(configurazionePeriodoGiornaliero);
			return aggiornati;
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
	}
	
private EsitoRisposte inserisciPeriodoGiornaliero(HttpServletRequest request) throws DaoException {
		
		String codicePerGiorn = (String)request.getAttribute("tx_periodo_giorn");
		String descrPerGiorn = (String)request.getAttribute("tx_descr_per_giorn");
		ConfigurazionePeriodoGiornalieroDAO configurazionePeriodoGiornalieroDAO;
		
		ConfigurazionePeriodoGiornaliero configurazionePeriodoGiornaliero = new ConfigurazionePeriodoGiornaliero();
		configurazionePeriodoGiornaliero.setCodiceSocieta(codiceSocieta);
		configurazionePeriodoGiornaliero.setCuteCute(codiceUtente);
		configurazionePeriodoGiornaliero.setChiaveEnte(chiaveEnte);
		configurazionePeriodoGiornaliero.setCodicePeriodoGiornaliero(codicePerGiorn);
		configurazionePeriodoGiornaliero.setDescrizionePeriodoGiornaliero(descrPerGiorn);
		UserBean user = (UserBean)request.getSession().getAttribute(SignOnKeys.USER_BEAN);
		//configurazionePagamentoServizio.setOperatore(user.getUserName());
		
		//inizio LP PG21XX04 Leak
		//configurazionePeriodoGiornalieroDAO = MercatoDAOFactory.getConfigurazionePeriodoGiornaliero(getMercatoDataSource(), getMercatoDbSchema());
		//return configurazionePeriodoGiornalieroDAO.insert(configurazionePeriodoGiornaliero);
		Connection conn = null;
		try {
			conn = getMercatoDataSource().getConnection();
			configurazionePeriodoGiornalieroDAO = MercatoDAOFactory.getConfigurazionePeriodoGiornaliero(conn, getMercatoDbSchema());
			EsitoRisposte esito = configurazionePeriodoGiornalieroDAO.insert(configurazionePeriodoGiornaliero); 
			return esito;
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
	}

private EsitoRisposte deletePeriodoGiornaliero(HttpServletRequest request) throws DaoException {
	String  societa = (String)request.getAttribute("tx_societa");
	String  utente = (String)request.getAttribute("tx_utente");
	String  ente = (String)request.getAttribute("tx_ente");
	
	String codicePerGiorn = (String)request.getAttribute("tx_periodo_giorn_h");
	
	ConfigurazionePeriodoGiornalieroDAO configurazionePeriodoGiornalieroDAO;
	
	ConfigurazionePeriodoGiornaliero configurazionePeriodoGiornaliero = new ConfigurazionePeriodoGiornaliero();
	configurazionePeriodoGiornaliero.setCodiceSocieta(societa);
	configurazionePeriodoGiornaliero.setCuteCute(utente);
	configurazionePeriodoGiornaliero.setChiaveEnte(ente);
	configurazionePeriodoGiornaliero.setCodicePeriodoGiornaliero(codicePerGiorn);
	
	//inizio LP PG21XX04 Leak
	//configurazionePeriodoGiornalieroDAO = MercatoDAOFactory.getConfigurazionePeriodoGiornaliero(getMercatoDataSource(), getMercatoDbSchema());
	//return configurazionePeriodoGiornalieroDAO.delete(configurazionePeriodoGiornaliero);
	Connection conn = null;
	try {
		conn = getMercatoDataSource().getConnection();
		configurazionePeriodoGiornalieroDAO = MercatoDAOFactory.getConfigurazionePeriodoGiornaliero(conn, getMercatoDbSchema());
		EsitoRisposte esito = configurazionePeriodoGiornalieroDAO.delete(configurazionePeriodoGiornaliero); 
		return esito;
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
}

//Termine della Classe
}

