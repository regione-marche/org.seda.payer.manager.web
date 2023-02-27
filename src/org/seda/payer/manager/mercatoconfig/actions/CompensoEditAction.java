package org.seda.payer.manager.mercatoconfig.actions;

import java.math.BigDecimal;
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

import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.mercatomanager.actions.MercatoBaseManagerAction;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.mercato.bean.ConfigurazioneCompenso;
import com.seda.payer.core.mercato.bean.EsitoRisposte;
import com.seda.payer.core.mercato.dao.ConfigurazioneCompensoDAO;
import com.seda.payer.core.mercato.dao.MercatoDAOFactory;


@SuppressWarnings("serial")
public class CompensoEditAction extends MercatoBaseManagerAction{
	private static String codop = "edit";
	private static String ritornaViewstate = "compenso";
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
				String chiaverecord = (String)request.getAttribute("codiceKeyCompenso");
				dividiSocUtenteEnte(request);
				request.setAttribute("codiceKeyCompenso_h",chiaverecord);
				
				if ( !(chiaverecord == null) ) {
					ConfigurazioneCompenso  configurazioneCompenso = null; 
					try {
						configurazioneCompenso = selectConfigurazioneCompenso(request);
					} catch (DaoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
					}
					String importoFisso = configurazioneCompenso.getImportoFisso().toString().replace(".", ",");
					String percentualeCompenso = (new BigDecimal(configurazioneCompenso.getPercentualeCompenso()).setScale(2,BigDecimal.ROUND_HALF_UP)).toString().replace(".", ",");
					String percentualeIva = (new BigDecimal(configurazioneCompenso.getPercentualeIva()).setScale(2,BigDecimal.ROUND_HALF_UP)).toString().replace(".", ",");
					request.setAttribute("tx_imp_fisso", importoFisso);
					request.setAttribute("tx_perc_compenso", percentualeCompenso);
					request.setAttribute("tx_perc_iva", percentualeIva); 
					request.setAttribute("tx_dval_ini", configurazioneCompenso.getDataInizioValidita());
					request.setAttribute("tx_dval_fin", configurazioneCompenso.getDataFineValidita());
				}
			} else {
				if (firedButton.equals(FiredButton.TX_BUTTON_RESET)) {
					resetParametri(request);
					request.setAttribute("tx_dval_ini", "");
					request.setAttribute("tx_dval_fin", "");
				}
			}
			
			break;
			
		case TX_BUTTON_EDIT:
			String msg="";
			try {
				msg=updateCompenso(request);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
			}
			if (msg.substring(0,2).equals("OK")) {
				session.setAttribute("recordUpdate", "OK");
				request.setAttribute("vista",ritornaViewstate);	//TODO da verificare
			} else {
				session.setAttribute("recordUpdate", "KO");
				session.setAttribute("messaggio.recordUpdate", msg.substring(3));
				setFormMessage("var_form", msg.substring(3), request);	//TODO da verificare
			}
			break;
		
		case TX_BUTTON_ADD:
			codop = "add";
			break;
		case TX_BUTTON_AGGIUNGI:
			try {
				dividiSocUtenteEnte(request);
				EsitoRisposte esito = new EsitoRisposte();
				esito = inserisciCompenso(request);
				if(esito.getCodiceMessaggio().equals("OK"))
				{
					//session.setAttribute("recordInsert", "OK");
					setFormMessage("var_form", Messages.INS_OK.format(), request);
				}
				else
				{
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
			request.setAttribute("vista",ritornaViewstate);
			break;
		
		case TX_BUTTON_CANCEL:
			dividiSocUtenteEnte(request);
			String chiaveRecord = (String)request.getAttribute("codiceKeyCompenso");
			request.setAttribute("codiceKeyCompenso_h",chiaveRecord);
			request.setAttribute("tx_button_cancel", "cancella");
			
			break;
		case TX_BUTTON_DELETE:
			try 
				{
					EsitoRisposte esito = new EsitoRisposte();
					esito = deleteCompenso(request);
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

	private ConfigurazioneCompenso selectConfigurazioneCompenso(HttpServletRequest request) throws DaoException {
		
		String codiceKeyCompenso = (String)request.getAttribute("codiceKeyCompenso");
		ConfigurazioneCompensoDAO configurazioneCompensoDAO;
		ConfigurazioneCompenso configurazioneCompenso = new ConfigurazioneCompenso();
		configurazioneCompenso.setCodiceKeyCompenso(codiceKeyCompenso);
		//inizio LP PG21XX04 Leak
		//configurazioneCompensoDAO = MercatoDAOFactory.getConfigurazioneCompenso(getMercatoDataSource(), getMercatoDbSchema());
		//configurazioneCompenso = configurazioneCompensoDAO.select(configurazioneCompenso);
		//
		//return configurazioneCompenso;
		Connection conn = null;
		try {
			conn = getMercatoDataSource().getConnection();
			configurazioneCompensoDAO = MercatoDAOFactory.getConfigurazioneCompenso(conn, getMercatoDbSchema());
			configurazioneCompenso = configurazioneCompensoDAO.select(configurazioneCompenso);
			
			return configurazioneCompenso;
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
	
	private String updateCompenso(HttpServletRequest request) throws DaoException {
		
		String  societa = (String)request.getAttribute("tx_societa");
		String  utente = (String)request.getAttribute("tx_utente");
		String  ente = (String)request.getAttribute("tx_ente");
		//String codiceCompenso = (String)request.getAttribute("tx_compenso_h");
		String codiceKeyCompenso = (String)request.getAttribute("codiceKeyCompenso_h");
		//Campi aggiornabili
		BigDecimal importoFisso = BigDecimal.ZERO;
		Double percentualeCompenso=0.0;
		Double percentualeIva=null;
		if (((String)request.getAttribute("tx_perc_compenso")==null)||((String)request.getAttribute("tx_perc_compenso")=="")) {
			//
		} else {
			percentualeCompenso = Double.parseDouble(((String)request.getAttribute("tx_perc_compenso")).replace(",","."));
		}
		if (((String)request.getAttribute("tx_perc_iva")==null)||((String)request.getAttribute("tx_perc_compenso")=="")) {
			//
		} else {
			percentualeIva  = Double.parseDouble(((String)request.getAttribute("tx_perc_iva")).replace(",","."));
		}
		if (((String)request.getAttribute("tx_imp_fisso")==null)||((String)request.getAttribute("tx_imp_fisso")=="")) {
			//
		} else {
			importoFisso = new BigDecimal(((String)request.getAttribute("tx_imp_fisso")).replace(",","."));
		}
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

		if (dataIniValCalendar.compareTo(dataFinValCalendar)>0) {
			return "KO Data inizio validità superiore a data fine validità";
		} else {		
			ConfigurazioneCompensoDAO configurazioneCompensoDAO;
		
			ConfigurazioneCompenso configurazioneCompenso = new ConfigurazioneCompenso();
			configurazioneCompenso.setCodiceKeyCompenso(codiceKeyCompenso);
			configurazioneCompenso.setCodiceSocieta(societa);
			configurazioneCompenso.setCuteCute(utente);
			configurazioneCompenso.setChiaveEnte(ente);
			configurazioneCompenso.setImportoFisso(importoFisso);
			configurazioneCompenso.setPercentualeCompenso(percentualeCompenso);
			configurazioneCompenso.setPercentualeIva(percentualeIva);
			configurazioneCompenso.setDataInizioValidita(dataIniValCalendar);
			configurazioneCompenso.setDataFineValidita(dataFinValCalendar);
		
			//inizio LP PG21XX04 Leak
			//configurazioneCompensoDAO = MercatoDAOFactory.getConfigurazioneCompenso(getMercatoDataSource(), getMercatoDbSchema());
			//int aggiornati = configurazioneCompensoDAO.update(configurazioneCompenso);
			int aggiornati = 0;
			Connection conn = null;
			try {
				conn = getMercatoDataSource().getConnection();
				configurazioneCompensoDAO = MercatoDAOFactory.getConfigurazioneCompenso(conn, getMercatoDbSchema());
				aggiornati = configurazioneCompensoDAO.update(configurazioneCompenso);
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
				return "OK";
			} else {
				return "KO Errore interno";
			}
		}
		
	}
	
private EsitoRisposte inserisciCompenso(HttpServletRequest request) throws DaoException {
		boolean Check=true;
		EsitoRisposte esitoRisposte = new EsitoRisposte();
		//String codiceCompenso = (String)request.getAttribute("tx_compenso");
		//Campi aggiornabili
		BigDecimal importoFisso = BigDecimal.ZERO;
		Double percentualeCompenso=0.0;
		Double percentualeIva=null;
		if (((String)request.getAttribute("tx_perc_compenso")==null)||((String)request.getAttribute("tx_perc_compenso")=="")) {
			//
		} else {
			percentualeCompenso = Double.parseDouble(((String)request.getAttribute("tx_perc_compenso")).replace(",","."));
		}
		if (((String)request.getAttribute("tx_perc_iva")==null)||((String)request.getAttribute("tx_perc_iva")=="")) {
			//
		} else {
			percentualeIva  = Double.parseDouble(((String)request.getAttribute("tx_perc_iva")).replace(",","."));
		}
		if (((String)request.getAttribute("tx_imp_fisso")==null)||((String)request.getAttribute("tx_imp_fisso")=="")) {
			//
		} else {
			importoFisso = new BigDecimal(((String)request.getAttribute("tx_imp_fisso")).replace(",","."));
		}
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
		Calendar dataIniValCalendar;
		if (!(datavalidita.equals("--"))) {
			dataIniValCalendar  = Calendar.getInstance();
			try {
				dataIniValCalendar.setTime(df.parse(datavalidita));
			} catch (ParseException e1) {
			// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			dataIniValCalendar = null;
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
		if (dataIniValCalendar==null || dataFinValCalendar==null) {
			Check=false;
			String msg = "";
			if (dataIniValCalendar==null) {
				msg=msg.concat("Data inizio validità obbligatoria. ");
			} 
			if (dataFinValCalendar==null) {
				msg=msg.concat("Data fine validità obbligatoria. ");
			}
			esitoRisposte.setCodiceMessaggio("KO");
			esitoRisposte.setDescrizioneMessaggio(msg);
		} else {
			if (dataFinValCalendar.compareTo(dataIniValCalendar) <= 0) {
				Check=false;
				esitoRisposte.setCodiceMessaggio("KO");
				esitoRisposte.setDescrizioneMessaggio("Data fine validità inferiore o uguale a data inizio validità");
			}
		}
		if (Check) {		
			ConfigurazioneCompensoDAO configurazioneCompensoDAO;
		
			ConfigurazioneCompenso configurazioneCompenso = new ConfigurazioneCompenso();
			configurazioneCompenso.setCodiceSocieta(codiceSocieta);
			configurazioneCompenso.setCuteCute(codiceUtente);
			configurazioneCompenso.setChiaveEnte(chiaveEnte);
			configurazioneCompenso.setImportoFisso(importoFisso);
			configurazioneCompenso.setPercentualeCompenso(percentualeCompenso);
			configurazioneCompenso.setPercentualeIva(percentualeIva);
			configurazioneCompenso.setDataInizioValidita(dataIniValCalendar);
			configurazioneCompenso.setDataFineValidita(dataFinValCalendar);
			UserBean user = (UserBean)request.getSession().getAttribute(SignOnKeys.USER_BEAN);
		
			//inizio LP PG21XX04 Leak
			//configurazioneCompensoDAO = MercatoDAOFactory.getConfigurazioneCompenso(getMercatoDataSource(), getMercatoDbSchema());
			//return configurazioneCompensoDAO.insert(configurazioneCompenso);
			Connection conn = null;
			try {
				conn = getMercatoDataSource().getConnection();
				configurazioneCompensoDAO = MercatoDAOFactory.getConfigurazioneCompenso(conn, getMercatoDbSchema());
				EsitoRisposte esito = configurazioneCompensoDAO.insert(configurazioneCompenso);; 
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

private EsitoRisposte deleteCompenso(HttpServletRequest request) throws DaoException {
	String codiceKeyCompenso = (String)request.getAttribute("codiceKeyCompenso_h");
		
	ConfigurazioneCompensoDAO configurazioneCompensoDAO;
	
	ConfigurazioneCompenso configurazioneCompenso = new ConfigurazioneCompenso();
	configurazioneCompenso.setCodiceKeyCompenso(codiceKeyCompenso);
		
	//inizio LP PG21XX04 Leak
	//configurazioneCompensoDAO = MercatoDAOFactory.getConfigurazioneCompenso(getMercatoDataSource(), getMercatoDbSchema());
	//return configurazioneCompensoDAO.delete(configurazioneCompenso);
	Connection conn = null;
	try {
		conn = getMercatoDataSource().getConnection();
		configurazioneCompensoDAO = MercatoDAOFactory.getConfigurazioneCompenso(conn, getMercatoDbSchema());
		EsitoRisposte esito = configurazioneCompensoDAO.delete(configurazioneCompenso); 
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

