package org.seda.payer.manager.mercatoconfig.actions;

import java.sql.Connection;
import java.sql.SQLException;
//import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.mercatomanager.actions.MercatoBaseManagerAction;

//import com.seda.commons.security.TokenGenerator;
import com.seda.j2ee5.maf.components.validation.ValidationContext;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.mercato.bean.ConfigurazioneTipologiaBanco;
import com.seda.payer.core.mercato.bean.EsitoRisposte;
import com.seda.payer.core.mercato.dao.ConfigurazioneTipologiaBancoDAO;
import com.seda.payer.core.mercato.dao.MercatoDAOFactory;


@SuppressWarnings("serial")
public class TipologiaBancoEditAction extends MercatoBaseManagerAction{
	private static String codop = "edit";
	private static String ritornaViewstate = "tipologiabanco";
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
				if (request.getAttribute("tx_tipologia_banco_h")==null) {
					chiaverecord = (String)request.getAttribute("tx_tipologia_banco");
				} else {
					chiaverecord = (String)request.getAttribute("tx_tipologia_banco_h");
				}
				dividiSocUtenteEnte(request);
				request.setAttribute("tx_tipologia_banco_h",chiaverecord);
			
				if ( !(chiaverecord == null) ) {
				//ConfigurazioneSolleciti  configurazioneSolleciti = null;
					ConfigurazioneTipologiaBanco  configurazioneTipologiaBanco = null; 
					try {
						configurazioneTipologiaBanco = selectConfigurazioneTipologiaBanco(request);
					} catch (DaoException e) {
					// TODO Auto-generated catch block
						e.printStackTrace();
						setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
					}
					request.setAttribute("tx_tipologia_banco", configurazioneTipologiaBanco.getCodiceTipologiaBanco());
					request.setAttribute("tx_descr_tipo_banco", configurazioneTipologiaBanco.getDescrizioneTipologiaBanco());
				}
			} else {
				if (firedButton.equals(FiredButton.TX_BUTTON_RESET)) {
					request.setAttribute("tx_tipologia_banco", "");
					request.setAttribute("tx_descr_tipo_banco", "");
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
			request.setAttribute("vista",ritornaViewstate);
			//setFormMessage("var_form", Messages.UPDT_OK.format(), request);
			
			break;
		
		case TX_BUTTON_ADD:
			codop = "add";
			
			//request.setAttribute("tx_button_cerca", "Search");	
			break;
		case TX_BUTTON_AGGIUNGI:
			try {
				dividiSocUtenteEnte(request);
				EsitoRisposte esito = new EsitoRisposte();
				esito = inserisciTipologiaBanco(request);
				if(esito.getCodiceMessaggio().equals("OK")) {
					//session.setAttribute("recordInsert", "OK");
					setFormMessage("var_form", Messages.INS_OK.format(), request);
					request.setAttribute("tx_tipologia_banco", "");
					request.setAttribute("tx_descr_tipo_banco", "");
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
			request.setAttribute("vista",ritornaViewstate);
			break;
		
		case TX_BUTTON_CANCEL:
			dividiSocUtenteEnte(request);
			String chiaveRecord = (String)request.getAttribute("tx_tipologia_banco");
			request.setAttribute("tx_tipologia_banco_h",chiaveRecord);
			request.setAttribute("tx_button_cancel", "cancella");
			
			break;
		case TX_BUTTON_DELETE:
			try 
				{
					EsitoRisposte esito = new EsitoRisposte();
					esito = deleteTipologiaBanco(request);
					if(esito.getCodiceMessaggio().equals("OK"))
					{
						session.setAttribute("recordDelete", "OK");
						//setFormMessage("form_selezione", Messages.CANC_OK.format(), request);
					}
					else
					{
						session.setAttribute("recordDelete", "KO");
						session.setAttribute("messaggio.recordDelete", esito.getDescrizioneMessaggio());
						//setFormMessage("form_selezione", esito.getDescrizioneMessaggio(), request);
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

	private ConfigurazioneTipologiaBanco selectConfigurazioneTipologiaBanco(HttpServletRequest request) throws DaoException {
		
		String codiceTipBanco  = (String)request.getAttribute("tx_tipologia_banco_h");
		ConfigurazioneTipologiaBancoDAO configurazioneTipologiaBancoDAO;
		ConfigurazioneTipologiaBanco configurazioneTipologiaBanco = new ConfigurazioneTipologiaBanco();
		configurazioneTipologiaBanco.setCodiceSocieta(codiceSocieta);
		configurazioneTipologiaBanco.setCuteCute(codiceUtente);
		configurazioneTipologiaBanco.setChiaveEnte(chiaveEnte);
		configurazioneTipologiaBanco.setCodiceTipologiaBanco(codiceTipBanco);
		//inizio LP PG21XX04 Leak
		//configurazioneTipologiaBancoDAO = MercatoDAOFactory.getConfigurazioneTipologiaBanco(getMercatoDataSource(), getMercatoDbSchema());
		//configurazioneTipologiaBanco = configurazioneTipologiaBancoDAO.select(configurazioneTipologiaBanco);
		//
		//return configurazioneTipologiaBanco;
		Connection conn = null;
		try {
			conn = getMercatoDataSource().getConnection();
			configurazioneTipologiaBancoDAO = MercatoDAOFactory.getConfigurazioneTipologiaBanco(conn, getMercatoDbSchema());
			configurazioneTipologiaBanco = configurazioneTipologiaBancoDAO.select(configurazioneTipologiaBanco);
			
			return configurazioneTipologiaBanco;
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
		//Date dataValString =  Date.valueOf((String)request.getAttribute("tx_dval_sms_h").toString().substring(0,10));
		String codiceTipBanco = (String)request.getAttribute("tx_tipologia_banco_h");
		String descrTipBanco = (String)request.getAttribute("tx_descr_tipo_banco");


		ConfigurazioneTipologiaBancoDAO configurazioneTipologiaBancoDAO;
		
		ConfigurazioneTipologiaBanco configurazioneTipologiaBanco = new ConfigurazioneTipologiaBanco();
		configurazioneTipologiaBanco.setCodiceSocieta(societa);
		configurazioneTipologiaBanco.setCuteCute(utente);
		configurazioneTipologiaBanco.setChiaveEnte(ente);
		configurazioneTipologiaBanco.setCodiceTipologiaBanco(codiceTipBanco);
		configurazioneTipologiaBanco.setDescrizioneTipologiaBanco(descrTipBanco);
		
		//inizio LP PG21XX04 Leak
		//configurazioneTipologiaBancoDAO = MercatoDAOFactory.getConfigurazioneTipologiaBanco(getMercatoDataSource(), getMercatoDbSchema());
		//int aggiornati = configurazioneTipologiaBancoDAO.update(configurazioneTipologiaBanco);
		//return aggiornati;
		Connection conn = null;
		try {
			conn = getMercatoDataSource().getConnection();
			configurazioneTipologiaBancoDAO = MercatoDAOFactory.getConfigurazioneTipologiaBanco(conn, getMercatoDbSchema());
			int aggiornati = configurazioneTipologiaBancoDAO.update(configurazioneTipologiaBanco);
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
	
private EsitoRisposte inserisciTipologiaBanco(HttpServletRequest request) throws DaoException {
		
		String codiceTipBanco = (String)request.getAttribute("tx_tipologia_banco");
		String descrTipBanco = (String)request.getAttribute("tx_descr_tipo_banco");
		/*
		// Gestione della chiave a 64 con chiamata a routine java esterna, qui non usata.
		 String chiaveTransazione = "";
			try {
				chiaveTransazione = TokenGenerator.generateUUIDToken();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}	
		*/
		ConfigurazioneTipologiaBancoDAO configurazioneTipologiaBancoDAO;
		
		ConfigurazioneTipologiaBanco configurazioneTipologiaBanco = new ConfigurazioneTipologiaBanco();
		configurazioneTipologiaBanco.setCodiceSocieta(codiceSocieta);
		configurazioneTipologiaBanco.setCuteCute(codiceUtente);
		configurazioneTipologiaBanco.setChiaveEnte(chiaveEnte);
		configurazioneTipologiaBanco.setCodiceTipologiaBanco(codiceTipBanco);
		configurazioneTipologiaBanco.setDescrizioneTipologiaBanco(descrTipBanco);
		UserBean user = (UserBean)request.getSession().getAttribute(SignOnKeys.USER_BEAN);
		//configurazionePagamentoServizio.setOperatore(user.getUserName());
		
		//inizio LP PG21XX04 Leak
		//configurazioneTipologiaBancoDAO = MercatoDAOFactory.getConfigurazioneTipologiaBanco(getMercatoDataSource(), getMercatoDbSchema());
		//return configurazioneTipologiaBancoDAO.insert(configurazioneTipologiaBanco);
		Connection conn = null;
		try {
			conn = getMercatoDataSource().getConnection();
			configurazioneTipologiaBancoDAO = MercatoDAOFactory.getConfigurazioneTipologiaBanco(conn, getMercatoDbSchema());
			EsitoRisposte esito = configurazioneTipologiaBancoDAO.insert(configurazioneTipologiaBanco); 
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

private EsitoRisposte deleteTipologiaBanco(HttpServletRequest request) throws DaoException {
	String  societa = (String)request.getAttribute("tx_societa");
	String  utente = (String)request.getAttribute("tx_utente");
	String  ente = (String)request.getAttribute("tx_ente");
	
	String codiceTipBanco = (String)request.getAttribute("tx_tipologia_banco_h");
	
	ConfigurazioneTipologiaBancoDAO configurazioneTipologiaBancoDAO;
	
	ConfigurazioneTipologiaBanco configurazioneTipologiaBanco = new ConfigurazioneTipologiaBanco();
	configurazioneTipologiaBanco.setCodiceSocieta(societa);
	configurazioneTipologiaBanco.setCuteCute(utente);
	configurazioneTipologiaBanco.setChiaveEnte(ente);
	configurazioneTipologiaBanco.setCodiceTipologiaBanco(codiceTipBanco);
	
	//inizio LP PG21XX04 Leak
	//configurazioneTipologiaBancoDAO = MercatoDAOFactory.getConfigurazioneTipologiaBanco(getMercatoDataSource(), getMercatoDbSchema());
	//return configurazioneTipologiaBancoDAO.delete(configurazioneTipologiaBanco);
	Connection conn = null;
	try {
		conn = getMercatoDataSource().getConnection();
		configurazioneTipologiaBancoDAO = MercatoDAOFactory.getConfigurazioneTipologiaBanco(conn, getMercatoDbSchema());
		return configurazioneTipologiaBancoDAO.delete(configurazioneTipologiaBanco);
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

}

