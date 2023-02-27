package org.seda.payer.manager.mercatoconfig.actions;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.CodiceFiscalePIVA;
import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.mercatomanager.actions.MercatoBaseManagerAction;

import com.seda.commons.security.TokenGenerator;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.mercato.bean.ConfigurazioneAnagAutorizzazione;
import com.seda.payer.core.mercato.bean.EsitoRisposte;
import com.seda.payer.core.mercato.dao.ConfigurazioneAnagAutorizzazioneDAO;
import com.seda.payer.core.mercato.dao.MercatoDAOFactory;


@SuppressWarnings("serial")
public class AnagAutorizzazioneEditAction extends MercatoBaseManagerAction{
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
				String chiaverecord = "";
				if (request.getAttribute("tx_anag_autorizzazione")==null) {
					chiaverecord = (String)request.getAttribute("tx_anag_autorizzazione_h");
				} else {
					chiaverecord = (String)request.getAttribute("tx_anag_autorizzazione");
				}
				dividiSocUtenteEnte(request);
				request.setAttribute("tx_anag_autorizzazione_h",chiaverecord);
				String codfiscasaut = ((String)request.getAttribute("tx_codicefiscanagaut") == null ? "" : (String)request.getAttribute("tx_codicefiscanagaut"));
				request.setAttribute("tx_codicefiscanagaut_h",codfiscasaut);
			
				if ( !(chiaverecord == null) ) {
					ConfigurazioneAnagAutorizzazione  configurazioneAnagAutorizzazione = null; 
					try {
						configurazioneAnagAutorizzazione = selectConfigurazioneAnagAutorizzazione(request);
					} catch (DaoException e) {
					// TODO Auto-generated catch block
						e.printStackTrace();
						setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
					}
					request.setAttribute("tx_anag_autorizzazione", configurazioneAnagAutorizzazione.getCodiceAnagAutorizzazione());
					request.setAttribute("tx_codfisc_autor", configurazioneAnagAutorizzazione.getCodiceFiscAnagAutorizzazione());
					request.setAttribute("tx_nominat_autor", configurazioneAnagAutorizzazione.getNominativoAnagAutorizzazione()); 
					request.setAttribute("tx_dval_ini", configurazioneAnagAutorizzazione.getDataInizioValidita());
					request.setAttribute("tx_dval_fin", configurazioneAnagAutorizzazione.getDataFineValidita());
				} 
			} else {
				if (firedButton.equals(FiredButton.TX_BUTTON_RESET)) {
					request.setAttribute("tx_anag_autorizzazione", "");
					request.setAttribute("tx_codfisc_autor", "");
					request.setAttribute("tx_nominat_autor", ""); 
					request.setAttribute("tx_dval_ini", "");
					request.setAttribute("tx_dval_fin", "");
				}
			}
			
			break;
			
		case TX_BUTTON_EDIT:
			String msg="";
			try {
				msg=updateAnagAutorizzazione(request);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
			}
			if (msg.substring(0,2).equals("OK")) {
				//session.setAttribute("recordUpdate", "OK");
				setFormMessage("var_form", Messages.INS_OK.format(), request);
			} else {
				//session.setAttribute("recordUpdate", "KO");
		    	//session.setAttribute("messaggio.recordUpdate", msg.substring(3));	
		    	setFormMessage("var_form", msg.substring(3), request);
				String chiaveRecord = (String)request.getAttribute("tx_anag_autorizzazione_h");
				request.setAttribute("tx_anag_autorizzazione",chiaveRecord);
				String ddlSocietaUtenteEnte = request.getAttribute("tx_societa") + "|" + request.getAttribute("tx_utente") +"|" + request.getAttribute("tx_ente");
				request.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);	
			}
			break;
		
		case TX_BUTTON_ADD:
			codop = "add";
			break;
		case TX_BUTTON_AGGIUNGI:
			try {
				dividiSocUtenteEnte(request);
				EsitoRisposte esito = new EsitoRisposte();
				esito = inserisciAnagAutorizzazione(request);
				if(esito.getCodiceMessaggio().equals("OK")) {
					session.setAttribute("recordInsert", "OK");
					setFormMessage("var_form", Messages.INS_OK.format(), request);
					request.setAttribute("tx_anag_autorizzazione", "");
					request.setAttribute("tx_codfisc_autor", "");
					request.setAttribute("tx_nominat_autor", ""); 
					request.setAttribute("tx_dval_ini", "");
					request.setAttribute("tx_dval_fin", "");					
				} else {
					//session.setAttribute("recordInsert", "KO");
					//session.setAttribute("messaggio.recordInsert", esito.getDescrizioneMessaggio());
					setFormMessage("var_form", esito.getDescrizioneMessaggio(), request);
				}	
			} catch (DaoException e) {
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
			String chiaveRecord = (String)request.getAttribute("tx_anag_autorizzazione");
			request.setAttribute("tx_anag_autorizzazione_h",chiaveRecord);
			String codFiscAssoc = (String)request.getAttribute("tx_codicefiscanagaut");
			request.setAttribute("tx_codicefiscanagaut_h", codFiscAssoc);
			request.setAttribute("tx_button_cancel", "cancella");
			
			break;
		case TX_BUTTON_DELETE:
			try {
					EsitoRisposte esito = new EsitoRisposte();
					esito = deleteAnagAutorizzazione(request);
					if(esito.getCodiceMessaggio().equals("OK")) {
						session.setAttribute("recordDelete", "OK");
					} else {
						session.setAttribute("recordDelete", "KO");
						session.setAttribute("messaggio.recordDelete", esito.getDescrizioneMessaggio());
					}
			} catch (DaoException e) {
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

	private ConfigurazioneAnagAutorizzazione selectConfigurazioneAnagAutorizzazione(HttpServletRequest request) throws DaoException {
		
		String codiceAnagAutor  = (String)request.getAttribute("tx_anag_autorizzazione_h");
		String codiceFiscAnagr  = (String)request.getAttribute("tx_codicefiscanagaut_h");
		ConfigurazioneAnagAutorizzazioneDAO configurazioneAnagAutorizzazioneDAO;
		ConfigurazioneAnagAutorizzazione configurazioneAnagAutorizzazione = new ConfigurazioneAnagAutorizzazione();
		configurazioneAnagAutorizzazione.setCodiceSocieta(codiceSocieta);
		configurazioneAnagAutorizzazione.setCuteCute(codiceUtente);
		configurazioneAnagAutorizzazione.setChiaveEnte(chiaveEnte);
		configurazioneAnagAutorizzazione.setCodiceAnagAutorizzazione(codiceAnagAutor);
		configurazioneAnagAutorizzazione.setCodiceFiscAnagAutorizzazione(codiceFiscAnagr);
		//inizio LP PG21XX04 Leak
		//configurazioneAnagAutorizzazioneDAO = MercatoDAOFactory.getConfigurazioneAnagAutorizzazione(getMercatoDataSource(), getMercatoDbSchema());
		//configurazioneAnagAutorizzazione = configurazioneAnagAutorizzazioneDAO.select(configurazioneAnagAutorizzazione);
		//
		//return configurazioneAnagAutorizzazione;
		Connection conn = null;
		try {
			conn = getMercatoDataSource().getConnection();
			configurazioneAnagAutorizzazioneDAO = MercatoDAOFactory.getConfigurazioneAnagAutorizzazione(conn, getMercatoDbSchema());
			configurazioneAnagAutorizzazione = configurazioneAnagAutorizzazioneDAO.select(configurazioneAnagAutorizzazione);
			
			return configurazioneAnagAutorizzazione;
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
	
	private String updateAnagAutorizzazione(HttpServletRequest request) throws DaoException {
		
		String  societa = (String)request.getAttribute("tx_societa");
		String  utente = (String)request.getAttribute("tx_utente");
		String  ente = (String)request.getAttribute("tx_ente");
		String codiceAnagAutor = (String)request.getAttribute("tx_anag_autorizzazione_h");
		String cdfiscAnagAutor = (String)request.getAttribute("tx_codfisc_autor");
		String nominAnagAutor  = (String)request.getAttribute("tx_nominat_autor");
		
		// Gestione Data Inizio Validita
		String anno = (String)request.getAttribute("tx_dval_ini_year");
		String mese = (String)request.getAttribute("tx_dval_ini_month");
		String giorno =(String)request.getAttribute("tx_dval_ini_day");
		String datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dataIniValCalendar  = Calendar.getInstance();
		if (!(datavalidita.equals("--"))) {
			try {
				dataIniValCalendar.setTime(df.parse(datavalidita));
			} catch (ParseException e1) {
			// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		// Gestione Data Fine Validita
		anno="";
		mese="";
		giorno="";
		datavalidita="";
		anno = (String)request.getAttribute("tx_dval_fin_year");
		mese = (String)request.getAttribute("tx_dval_fin_month");
		giorno =(String)request.getAttribute("tx_dval_fin_day");
		if (!(anno.equals(""))) {
			datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		} else {
			datavalidita="--";
		}
		//DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dataFinValCalendar  = Calendar.getInstance();
		if (!(datavalidita.equals("--"))) {
			try {
				dataFinValCalendar.setTime(df.parse(datavalidita));
			} catch (ParseException e1) {
			// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			dataFinValCalendar=null;
		}
		boolean Check = true;
		String msg = "";
		if (dataIniValCalendar.compareTo(dataFinValCalendar)>0) {
			msg = "KO Data inizio validità superiore a data fine validità";
		}
		if (cdfiscAnagAutor.trim().length()>0){
			String sRes = CodiceFiscalePIVA.controlloCodiceFiscale_PartitaIva_NomeEmpty(cdfiscAnagAutor, "Cod.Fisc.");
			if (sRes != null && sRes.length() > 0) {
				Check = false;
				msg = "KO " + sRes;
			}
		}
		if (Check) {
			ConfigurazioneAnagAutorizzazioneDAO configurazioneAnagAutorizzazioneDAO;
		
			ConfigurazioneAnagAutorizzazione configurazioneAnagAutorizzazione = new ConfigurazioneAnagAutorizzazione();
			configurazioneAnagAutorizzazione.setCodiceSocieta(societa);
			configurazioneAnagAutorizzazione.setCuteCute(utente);
			configurazioneAnagAutorizzazione.setChiaveEnte(ente);
			configurazioneAnagAutorizzazione.setCodiceAnagAutorizzazione(codiceAnagAutor);
			configurazioneAnagAutorizzazione.setCodiceFiscAnagAutorizzazione(cdfiscAnagAutor);
			configurazioneAnagAutorizzazione.setNominativoAnagAutorizzazione(nominAnagAutor);
			configurazioneAnagAutorizzazione.setDataInizioValidita(dataIniValCalendar);
			configurazioneAnagAutorizzazione.setDataFineValidita(dataFinValCalendar);
		
			//inizio LP PG21XX04 Leak
			//configurazioneAnagAutorizzazioneDAO = MercatoDAOFactory.getConfigurazioneAnagAutorizzazione(getMercatoDataSource(), getMercatoDbSchema());
			//int aggiornati = configurazioneAnagAutorizzazioneDAO.update(configurazioneAnagAutorizzazione);
			int aggiornati =  0;
			Connection conn = null;
			try {
				conn = getMercatoDataSource().getConnection();
				configurazioneAnagAutorizzazioneDAO = MercatoDAOFactory.getConfigurazioneAnagAutorizzazione(conn, getMercatoDbSchema());
				aggiornati = configurazioneAnagAutorizzazioneDAO.update(configurazioneAnagAutorizzazione);
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
			if (aggiornati>0) {
				msg = "OK";
			} else {
				msg = "KO Errore interno";
			}
		}			
		return msg;
	}
	
private EsitoRisposte inserisciAnagAutorizzazione(HttpServletRequest request) throws DaoException {
		boolean Check = true;
		EsitoRisposte esitoRisposte = new EsitoRisposte();	
		//Chiave primaria autogenerata da classe
		String chiaveAutorizzazione = "";
		try {
			chiaveAutorizzazione = TokenGenerator.generateUUIDToken();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}			
		String codiceAnagAutor = (String)request.getAttribute("tx_anag_autorizzazione");
		String codiceFiscAutor = (String)request.getAttribute("tx_codfisc_autor");
		String nominativoAutor = (String)request.getAttribute("tx_nominat_autor");
// Gestione Data Inizio Validita
		String anno;
		String mese;
		String giorno;
		if ((String)request.getAttribute("tx_dval_ini_year") == null) {
			Calendar today = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
			anno = Integer.toString(today.get(today.YEAR));
		} else {
			anno = (String)request.getAttribute("tx_dval_ini_year");
		}
		if ((String)request.getAttribute("tx_dval_ini_month") == null) {
			Calendar today = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
			mese = Integer.toString(today.get(today.MONTH));
		} else {
			mese = (String)request.getAttribute("tx_dval_ini_month");
		}
		if ((String)request.getAttribute("tx_dval_ini_day") == null) {
			Calendar today = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
			giorno = Integer.toString(today.get(today.DAY_OF_MONTH));
		} else {
			giorno = (String)request.getAttribute("tx_dval_ini_day");
		}

		String datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dataIniValCalendar  = Calendar.getInstance();
		if (!(datavalidita.equals("--"))) {
			try {
				dataIniValCalendar.setTime(df.parse(datavalidita));
			} catch (ParseException e1) {
			// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
// Gestione Data Fine Validita
		datavalidita=null;
		anno = (String)request.getAttribute("tx_dval_fin_year");
		mese = (String)request.getAttribute("tx_dval_fin_month");
		giorno =(String)request.getAttribute("tx_dval_fin_day");
		datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		Calendar dataFinValCalendar;
		if (!(datavalidita.equals("--"))) {
			dataFinValCalendar  = Calendar.getInstance();
			try {
				dataFinValCalendar.setTime(df.parse(datavalidita));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			dataFinValCalendar = null;
		}
		if (!(dataFinValCalendar==null)) {
			if (dataFinValCalendar.compareTo(dataIniValCalendar) <= 0) {
				Check=false;
				esitoRisposte.setCodiceMessaggio("KO");
				esitoRisposte.setDescrizioneMessaggio("Data fine validità inferiore o uguale a data inizio validità");
			}
		}
		if (Check) {		
			ConfigurazioneAnagAutorizzazioneDAO configurazioneAnagAutorizzazioneDAO;
		
			ConfigurazioneAnagAutorizzazione configurazioneAnagAutorizzazione = new ConfigurazioneAnagAutorizzazione();
			configurazioneAnagAutorizzazione.setCodiceKeyAnagAutor(chiaveAutorizzazione);
			configurazioneAnagAutorizzazione.setCodiceSocieta(codiceSocieta);
			configurazioneAnagAutorizzazione.setCuteCute(codiceUtente);
			configurazioneAnagAutorizzazione.setChiaveEnte(chiaveEnte);
			configurazioneAnagAutorizzazione.setCodiceAnagAutorizzazione(codiceAnagAutor);
			configurazioneAnagAutorizzazione.setCodiceFiscAnagAutorizzazione(codiceFiscAutor);
			configurazioneAnagAutorizzazione.setNominativoAnagAutorizzazione(nominativoAutor);
			configurazioneAnagAutorizzazione.setDataInizioValidita(dataIniValCalendar);
			configurazioneAnagAutorizzazione.setDataFineValidita(dataFinValCalendar);
			UserBean user = (UserBean)request.getSession().getAttribute(SignOnKeys.USER_BEAN);
		
			//inizio LP PG21XX04 Leak
			//configurazioneAnagAutorizzazioneDAO = MercatoDAOFactory.getConfigurazioneAnagAutorizzazione(getMercatoDataSource(), getMercatoDbSchema());
			//return configurazioneAnagAutorizzazioneDAO.insert(configurazioneAnagAutorizzazione);
			Connection conn = null;
			try {
				conn = getMercatoDataSource().getConnection();
				configurazioneAnagAutorizzazioneDAO = MercatoDAOFactory.getConfigurazioneAnagAutorizzazione(conn, getMercatoDbSchema());
				EsitoRisposte esito = configurazioneAnagAutorizzazioneDAO.insert(configurazioneAnagAutorizzazione); 
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
		} else {
			return esitoRisposte;
		}
		
	}

private EsitoRisposte deleteAnagAutorizzazione(HttpServletRequest request) throws DaoException {
	String  societa = (String)request.getAttribute("tx_societa");
	String  utente = (String)request.getAttribute("tx_utente");
	String  ente = (String)request.getAttribute("tx_ente");
	
	String codiceAnagAutor = (String)request.getAttribute("tx_anag_autorizzazione_h");
	
	ConfigurazioneAnagAutorizzazioneDAO configurazioneAnagAutorizzazioneDAO;
	
	ConfigurazioneAnagAutorizzazione configurazioneAnagAutorizzazione = new ConfigurazioneAnagAutorizzazione();
	configurazioneAnagAutorizzazione.setCodiceSocieta(societa);
	configurazioneAnagAutorizzazione.setCuteCute(utente);
	configurazioneAnagAutorizzazione.setChiaveEnte(ente);
	configurazioneAnagAutorizzazione.setCodiceAnagAutorizzazione(codiceAnagAutor);
	
	//inizio LP PG21XX04 Leak
	//configurazioneAnagAutorizzazioneDAO = MercatoDAOFactory.getConfigurazioneAnagAutorizzazione(getMercatoDataSource(), getMercatoDbSchema());
	//return configurazioneAnagAutorizzazioneDAO.delete(configurazioneAnagAutorizzazione);
	Connection conn = null;
	try {
		conn = getMercatoDataSource().getConnection();
		configurazioneAnagAutorizzazioneDAO = MercatoDAOFactory.getConfigurazioneAnagAutorizzazione(conn, getMercatoDbSchema());
		EsitoRisposte esito = configurazioneAnagAutorizzazioneDAO.delete(configurazioneAnagAutorizzazione); 
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

//Fine della classe
}

