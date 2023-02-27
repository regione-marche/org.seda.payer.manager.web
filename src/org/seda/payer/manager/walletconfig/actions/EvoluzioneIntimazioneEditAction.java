package org.seda.payer.manager.walletconfig.actions;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jaxen.function.StringFunction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.walletmanager.actions.WalletBaseManagerAction;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.ConfigurazioneEvoIntimazioni;
import com.seda.payer.core.wallet.bean.EsitoRisposte;
import com.seda.payer.core.wallet.bean.Servizio;
import com.seda.payer.core.wallet.dao.ConfigurazioneEvoIntimazioniDAO;
import com.seda.payer.core.wallet.dao.ServizioDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;
import com.seda.tag.core.DdlOption;

@SuppressWarnings("serial")
public class EvoluzioneIntimazioneEditAction extends WalletBaseManagerAction{
	private static String codop = "edit";
	private static String ritornaViewstate = "evoluzioneintimazione";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;
	private String codiceSocieta = "";
	private String codiceUtente = "";
	private String chiaveEnte = "";
	private String operatore ="";
	
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
			codop = "edit";
			String chiaveDataValidita = (String)request.getAttribute("tx_dval_sms");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar_dval_sms  = Calendar.getInstance();

			dividiSocUtenteEnte(request);
			getServiziDDL(request);
			try {
				calendar_dval_sms.setTime(df.parse(chiaveDataValidita));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			request.setAttribute("tx_dval_sms",calendar_dval_sms);
			request.setAttribute("tx_dval_sms_h",chiaveDataValidita);
			
			request.setAttribute("chiaveEvoluzioneIntimazione", chiaveDataValidita);
			if ( !chiaveDataValidita.equals("") && !chiaveDataValidita.equals("")) {
				//ConfigurazioneSolleciti  configurazioneSolleciti = null;
				ConfigurazioneEvoIntimazioni configurazioneEvoIntimazioni = null;
				
				try {
					configurazioneEvoIntimazioni = selectConfigurazioneEvoluzioneIntimazioni(request);
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
				}
				String importostr = configurazioneEvoIntimazioni.getImportoResiduo().toString().replace(".", ",");
				
				request.setAttribute("tx_importo_residuo", importostr);
				request.setAttribute("tx_sms_sollecito", configurazioneEvoIntimazioni.getSmsSollecito());
				request.setAttribute("tx_flg_sollecito_cartaceo", configurazioneEvoIntimazioni.getFlagSollecitoCartaceo());
				request.setAttribute("tx_intervallo_gg", configurazioneEvoIntimazioni.getIntervalloGiorniEvoluzione());
				request.setAttribute("tx_flagAttivazione", configurazioneEvoIntimazioni.getFlagAttivazione());
				request.setAttribute("tx_ente_srv", configurazioneEvoIntimazioni.getCodiceEnteEvoluzione());
				request.setAttribute("tx_is_srv", configurazioneEvoIntimazioni.getImpostaServizioEvoluzione());
				request.setAttribute("tx_tipoServizio_srv", configurazioneEvoIntimazioni.getTipoServizio());
				
			
			}
			
			break;
			
		case TX_BUTTON_EDIT:
			try {
				updateEvoluzioneIntimazione(request);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
			}
			session.setAttribute("recordUpdate", "OK");
			//setFormMessage("form_selezione", Messages.UPDT_OK.format(), request);
			break;
		
		case TX_BUTTON_ADD:
			codop = "add";
			//request.setAttribute("tx_button_cerca", "Search");	
			break;
		case TX_BUTTON_AGGIUNGI:
			try {
				dividiSocUtenteEnte(request);
				getServiziDDL(request);
				EsitoRisposte esito = new EsitoRisposte();
				esito = inserisciEvoluzioneIntimazione(request);
				if(esito.getCodiceMessaggio().equals("OK"))
				{
					session.setAttribute("recordInsert", "OK");
					//setFormMessage("form_selezione", Messages.INS_OK.format(), request);
					
				}
				else
				{
					session.setAttribute("recordInsert", "KO");
					session.setAttribute("messaggio.recordInsert", esito.getDescrizioneMessaggio());
					//setFormMessage("form_selezione", esito.getDescrizioneMessaggio(), request);
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
			String chiaveDataValiditaDel = (String)request.getAttribute("tx_dval_sms");
			String tipoServizioDel= (String)request.getAttribute("tx_tipoServizio_srv");
			DateFormat dfdel = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar_dval_smsDel  = Calendar.getInstance();

			dividiSocUtenteEnte(request);
			getServiziDDL(request);
			try {
				calendar_dval_smsDel.setTime(dfdel.parse(chiaveDataValiditaDel));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			request.setAttribute("tx_dval_sms_h",chiaveDataValiditaDel);
			request.setAttribute("tx_tipoServizio_h",tipoServizioDel);
			request.setAttribute("tx_button_cancel", "cancella");
			
			break;
		case TX_BUTTON_DELETE:
			try 
				{
					EsitoRisposte esito = new EsitoRisposte();
					esito = deleteEvoluzioneIntimazione(request);
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
			getServiziDDL(request);
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
		if (!ddlSocietaUtenteEnte.equals(""))
		{
			String[] codici = ddlSocietaUtenteEnte.split("\\|");
			codiceSocieta = codici[0];
			codiceUtente = codici[1];
			chiaveEnte = codici[2];
			request.setAttribute("tx_societa", codiceSocieta);
			request.setAttribute("tx_utente", codiceUtente);
			request.setAttribute("tx_ente", chiaveEnte);
		}
	}

	@SuppressWarnings("unchecked")
	private void getServiziDDL(HttpServletRequest request) {
	//creo ddl option:
	ArrayList<Servizio> listaServizi = null;
	List<DdlOption> serviziDDLList  = new ArrayList<DdlOption>();
	ServizioDAO servizioDAO;
	//inizio LP PG21XX04 Leak
	Connection conn = null;
	//fine LP PG21XX04 Leak
	try {
		//inizio LP PG21XX04 Leak
		//servizioDAO = WalletDAOFactory.getServizioDAO(getWalletDataSource(), getWalletDbSchema());
		conn = getWalletDataSource().getConnection();
		servizioDAO = WalletDAOFactory.getServizioDAO(conn, getWalletDbSchema());
		//fine LP PG21XX04 Leak
		listaServizi = servizioDAO.listServizi();
		
		for (Iterator iterator = listaServizi.iterator(); iterator.hasNext();) {
			Servizio servizio = (Servizio) iterator.next();
			DdlOption optionServizio = new DdlOption();
			optionServizio.setSValue(servizio.getCodiceServizio());
			optionServizio.setSText(servizio.getDescrizioneServizio());
			serviziDDLList.add(optionServizio);
		}
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
    if (serviziDDLList.size()> 0) {
		DdlOption optionServizio = new DdlOption();
		optionServizio.setSValue("XX");
		optionServizio.setSText("Nidi d'infanzia e Rist.Scolastica");
		serviziDDLList.add(optionServizio);

    }
	request.setAttribute("serviziDDLList", serviziDDLList);
}

	
	private ConfigurazioneEvoIntimazioni selectConfigurazioneEvoluzioneIntimazioni(HttpServletRequest request) throws DaoException {
		
		String  chiaveEvoluzioneIntimazione = (String)request.getAttribute("chiaveEvoluzioneIntimazione");
		Calendar dataValCalendar = (Calendar)request.getAttribute("tx_dval_sms");
		String tipoServizio = (String)request.getAttribute("tx_tipoServizio_srv");
		ConfigurazioneEvoIntimazioniDAO configurazioneEvoIntimazioniDAO;
		ConfigurazioneEvoIntimazioni configurazioneEvoIntimazioni = new ConfigurazioneEvoIntimazioni();
		configurazioneEvoIntimazioni.setCodiceSocieta(codiceSocieta);
		configurazioneEvoIntimazioni.setCuteCute(codiceUtente);
		configurazioneEvoIntimazioni.setChiaveEnte(chiaveEnte);
		configurazioneEvoIntimazioni.setDataValidita(dataValCalendar);
		configurazioneEvoIntimazioni.setTipoServizio(tipoServizio);

		//inizio LP PG21XX04 Leak
		//configurazioneEvoIntimazioniDAO = WalletDAOFactory.getConfigurazioneEvoIntimazione(getWalletDataSource(), getWalletDbSchema());
		//configurazioneEvoIntimazioni = configurazioneEvoIntimazioniDAO.select(configurazioneEvoIntimazioni);
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			configurazioneEvoIntimazioniDAO = WalletDAOFactory.getConfigurazioneEvoIntimazione(conn, getWalletDbSchema());
			configurazioneEvoIntimazioni = configurazioneEvoIntimazioniDAO.select(configurazioneEvoIntimazioni);
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
		return configurazioneEvoIntimazioni;
	}
	
	private Integer updateEvoluzioneIntimazione(HttpServletRequest request) throws DaoException {
		
		String  societa = (String)request.getAttribute("tx_societa");
		String  utente = (String)request.getAttribute("tx_utente");
		String  ente = (String)request.getAttribute("tx_ente");
		String  tipoServizio = (String)request.getAttribute("tx_tipoServizio_h");
		//Date dataValString =  Date.valueOf((String)request.getAttribute("tx_dval_sms_h").toString().substring(0,10));
		String dataValString = (String)request.getAttribute("tx_dval_sms_h").toString().substring(0,10);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar_dval_sms  = Calendar.getInstance();
		try {
			calendar_dval_sms.setTime(df.parse(dataValString));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String flgsms =  (String)request.getAttribute("tx_sms_sollecito");
		String impo = ((String)request.getAttribute("tx_importo_residuo")).replace(",", ".");
		BigDecimal importoresiduointimazione = new BigDecimal(impo);
		
		String FlagSollecitoCartaceo = (String)request.getAttribute("tx_flg_sollecito_cartaceo");
		Integer IntervalloGiorni = Integer.parseInt(request.getAttribute("tx_intervallo_gg").toString());
		
		
		String enteServ = (String)request.getAttribute("tx_ente_srv");
		String imposta_servizio = (String)request.getAttribute("tx_is_srv");
		String flagAttivazione = "";
		flagAttivazione = (String)request.getAttribute("tx_flagAttivazione");
		if(flagAttivazione.equals(""))
		{
			flagAttivazione="N";
		}
		
		
		
		ConfigurazioneEvoIntimazioniDAO configurazioneEvoIntimazioniDAO;
		
		ConfigurazioneEvoIntimazioni configurazioneIntimazioneEvoluzione = new ConfigurazioneEvoIntimazioni();
		configurazioneIntimazioneEvoluzione.setCodiceSocieta(societa);
		configurazioneIntimazioneEvoluzione.setCuteCute(utente);
		configurazioneIntimazioneEvoluzione.setChiaveEnte(ente);
		configurazioneIntimazioneEvoluzione.setDataValidita(calendar_dval_sms);
		configurazioneIntimazioneEvoluzione.setImportoResiduo(importoresiduointimazione);
		configurazioneIntimazioneEvoluzione.setSmsSollecito(flgsms);
		configurazioneIntimazioneEvoluzione.setOperatore(operatore);
		configurazioneIntimazioneEvoluzione.setFlagSollecitoCartaceo(FlagSollecitoCartaceo);
		configurazioneIntimazioneEvoluzione.setIntervalloGiorniEvoluzione(IntervalloGiorni);
		configurazioneIntimazioneEvoluzione.setFlagAttivazione(flagAttivazione);
		configurazioneIntimazioneEvoluzione.setCodiceEnteEvoluzione(enteServ);
		configurazioneIntimazioneEvoluzione.setImpostaServizioEvoluzione(imposta_servizio);
		configurazioneIntimazioneEvoluzione.setTipoServizio(tipoServizio);
		
		//inizio LP PG21XX04 Leak
		//configurazioneEvoIntimazioniDAO = WalletDAOFactory.getConfigurazioneEvoIntimazione(getWalletDataSource(), getWalletDbSchema());
		//int aggiornati = configurazioneEvoIntimazioniDAO.update(configurazioneIntimazioneEvoluzione);
		//return aggiornati;
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			configurazioneEvoIntimazioniDAO = WalletDAOFactory.getConfigurazioneEvoIntimazione(conn, getWalletDbSchema());
			int aggiornati = configurazioneEvoIntimazioniDAO.update(configurazioneIntimazioneEvoluzione);
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
	
private EsitoRisposte inserisciEvoluzioneIntimazione(HttpServletRequest request) throws DaoException {
		
		String anno = (String)request.getAttribute("tx_dval_sms_year");
		String mese =(String)request.getAttribute("tx_dval_sms_month");
		String giorno =(String)request.getAttribute("tx_dval_sms_day");
		String datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar_dval_sms  = Calendar.getInstance();
		try {
			calendar_dval_sms.setTime(df.parse(datavalidita));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String flgsms =  (String)request.getAttribute("tx_sms_sollecito");
		BigDecimal importoresiduointimazione = new BigDecimal(((String)request.getAttribute("tx_importo_residuo")).replace(",", "."));
		
		
		String FlagSollecitoCartaceo = (String)request.getAttribute("tx_flg_sollecito_cartaceo");
		Integer IntervalloGiorni = Integer.parseInt(request.getAttribute("tx_intervallo_gg").toString());
		
		String enteServ = (String)request.getAttribute("tx_ente_srv");
		String tipoServizio = (String)request.getAttribute("tx_tipoServizio_srv");
		String imposta_servizio = (String)request.getAttribute("tx_is_srv");
		String flagAttivazione = "";
		flagAttivazione = (String)request.getAttribute("tx_flagAttivazione");
		if(flagAttivazione.equals(""))
		{
			flagAttivazione="N";
		}
		
		
		
		ConfigurazioneEvoIntimazioniDAO configurazioneEvoIntimazioniDAO;
		
		ConfigurazioneEvoIntimazioni configurazioneIntimazioneEvoluzione = new ConfigurazioneEvoIntimazioni();
		configurazioneIntimazioneEvoluzione.setCodiceSocieta(codiceSocieta);
		configurazioneIntimazioneEvoluzione.setCuteCute(codiceUtente);
		configurazioneIntimazioneEvoluzione.setChiaveEnte(chiaveEnte);
		configurazioneIntimazioneEvoluzione.setDataValidita(calendar_dval_sms);
		configurazioneIntimazioneEvoluzione.setImportoResiduo(importoresiduointimazione);
		configurazioneIntimazioneEvoluzione.setSmsSollecito(flgsms);
		configurazioneIntimazioneEvoluzione.setOperatore(operatore);
		configurazioneIntimazioneEvoluzione.setFlagSollecitoCartaceo(FlagSollecitoCartaceo);
		configurazioneIntimazioneEvoluzione.setIntervalloGiorniEvoluzione(IntervalloGiorni);
		
		configurazioneIntimazioneEvoluzione.setFlagAttivazione(flagAttivazione);
		configurazioneIntimazioneEvoluzione.setCodiceEnteEvoluzione(enteServ);
		configurazioneIntimazioneEvoluzione.setImpostaServizioEvoluzione(imposta_servizio);
		configurazioneIntimazioneEvoluzione.setTipoServizio(tipoServizio);

		//inizio LP PG21XX04 Leak
		//configurazioneEvoIntimazioniDAO = WalletDAOFactory.getConfigurazioneEvoIntimazione(getWalletDataSource(), getWalletDbSchema());
		//EsitoRisposte esitorisposte = new EsitoRisposte();
		//return configurazioneEvoIntimazioniDAO.insert(configurazioneIntimazioneEvoluzione);
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			configurazioneEvoIntimazioniDAO = WalletDAOFactory.getConfigurazioneEvoIntimazione(conn, getWalletDbSchema());
			EsitoRisposte esitorisposte = new EsitoRisposte();
			return configurazioneEvoIntimazioniDAO.insert(configurazioneIntimazioneEvoluzione);
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

private EsitoRisposte deleteEvoluzioneIntimazione(HttpServletRequest request) throws DaoException {
	String  societa = (String)request.getAttribute("tx_societa");
	String  utente = (String)request.getAttribute("tx_utente");
	String  ente = (String)request.getAttribute("tx_ente");
	
	String  tipoServizio = (String)request.getAttribute("tx_tipoServizio_h");
	String datavalidita = (String)request.getAttribute("tx_dval_sms_h").toString().substring(0,10);
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar_dval_sms  = Calendar.getInstance();
	try {
		calendar_dval_sms.setTime(df.parse(datavalidita));
	} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	
	ConfigurazioneEvoIntimazioniDAO configurazioneEvoIntimazioniDAO;
	
	ConfigurazioneEvoIntimazioni configurazioneIntimazioneEvoluzione = new ConfigurazioneEvoIntimazioni();
	configurazioneIntimazioneEvoluzione.setCodiceSocieta(societa);
	configurazioneIntimazioneEvoluzione.setCuteCute(utente);
	configurazioneIntimazioneEvoluzione.setChiaveEnte(ente);
	configurazioneIntimazioneEvoluzione.setDataValidita(calendar_dval_sms);
	configurazioneIntimazioneEvoluzione.setTipoServizio(tipoServizio);
	
	//inizio LP PG21XX04 Leak
	//configurazioneEvoIntimazioniDAO = WalletDAOFactory.getConfigurazioneEvoIntimazione(getWalletDataSource(), getWalletDbSchema());
	//return configurazioneEvoIntimazioniDAO.delete(configurazioneIntimazioneEvoluzione);
	Connection conn = null;
	try {
		conn = getWalletDataSource().getConnection();
		configurazioneEvoIntimazioniDAO = WalletDAOFactory.getConfigurazioneEvoIntimazione(conn, getWalletDbSchema());
		return configurazioneEvoIntimazioniDAO.delete(configurazioneIntimazioneEvoluzione);
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

