package org.seda.payer.manager.mercatoconfig.actions;

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
import com.seda.payer.core.mercato.bean.ConfigurazioneAreaMercantile;
import com.seda.payer.core.mercato.bean.EsitoRisposte;
import com.seda.payer.core.mercato.dao.ConfigurazioneAreaMercantileDAO;
import com.seda.payer.core.mercato.dao.MercatoDAOFactory;


@SuppressWarnings("serial")
public class AreaMercantileEditAction extends MercatoBaseManagerAction{
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
				String chiave2rec="";
				String chiave3rec="";
				String chiave4rec="";
				if (request.getAttribute("tx_area_mercantile")==null){
					chiaverecord = (String)request.getAttribute("tx_area_mercantile_h");
				} else {
					chiaverecord = (String)request.getAttribute("tx_area_mercantile");
				}
				if (request.getAttribute("tx_codice_area")==null){
					chiave2rec = (String)request.getAttribute("tx_codice_area_h");
				} else {
					chiave2rec = (String)request.getAttribute("tx_codice_area");
				}
				if (request.getAttribute("tx_descr_area_merc")==null){
					chiave3rec = (String)request.getAttribute("tx_descr_area_h");
				} else {
					chiave3rec = (String)request.getAttribute("tx_descr_area_merc");
				}
				if (request.getAttribute("tx_dval_ini")==null){
					chiave4rec = (String)request.getAttribute("tx_data_ini_val_h");
				} else {
					chiave4rec = (String)request.getAttribute("tx_dval_ini");
				}
				
				dividiSocUtenteEnte(request);
				request.setAttribute("tx_area_mercantile_h",chiaverecord);
				request.setAttribute("tx_codice_area_h",chiave2rec);
				request.setAttribute("tx_descr_area_h", chiave3rec);
				request.setAttribute("tx_data_ini_val_h", chiave4rec);
			
				if ( !(chiaverecord == null) ) {
					ConfigurazioneAreaMercantile  configurazioneAreaMercantile = null; 
					try {
						configurazioneAreaMercantile = selectConfigurazioneAreaMercantile(request);
					} catch (DaoException e) {
					// TODO Auto-generated catch block
						e.printStackTrace();
						setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
					}
					request.setAttribute("tx_area_mercantile", configurazioneAreaMercantile.getCodiceKeyAreaMercantile());
					request.setAttribute("tx_codice_area", configurazioneAreaMercantile.getCodiceAreaMercantile());
					request.setAttribute("tx_descr_area_merc", configurazioneAreaMercantile.getDescrizioneAreaMercantile());
					request.setAttribute("tx_dval_ini", configurazioneAreaMercantile.getDataInizioValidita());
					String tmpdata =
					configurazioneAreaMercantile.getDataInizioValidita().get(Calendar.YEAR) + "-" +
					(configurazioneAreaMercantile.getDataInizioValidita().get(Calendar.MONTH)+1) + "-" +
					configurazioneAreaMercantile.getDataInizioValidita().get(Calendar.DAY_OF_MONTH);
					request.setAttribute("tx_data_ini_val_h", tmpdata);
					request.setAttribute("tx_dval_fin", configurazioneAreaMercantile.getDataFineValidita());
					request.setAttribute("tx_codice_area_h", configurazioneAreaMercantile.getCodiceAreaMercantile());
					request.setAttribute("tx_descr_area_h", configurazioneAreaMercantile.getDescrizioneAreaMercantile());
				}
			} else {
				if (firedButton.equals(FiredButton.TX_BUTTON_RESET)) {
					request.setAttribute("tx_area_mercantile", "");
					request.setAttribute("tx_codice_area", "");
					request.setAttribute("tx_descr_area_merc", "");
					request.setAttribute("tx_dval_ini", "");
					request.setAttribute("tx_data_ini_val_h", "");
					request.setAttribute("tx_dval_fin", "");
				}
			}
			
			break;
			
		case TX_BUTTON_EDIT:
			String msg="";
			try {
				msg=updateAreaMercantile(request);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
			}
			if (msg.substring(0,2).equals("OK")) {
				//session.setAttribute("recordUpdate", "OK");
				setFormMessage("var_form", Messages.UPDT_OK.format(), request);
			} else {
				//session.setAttribute("recordUpdate", "KO");
				//session.setAttribute("messaggio.recordUpdate", msg.substring(3));
				setFormMessage("var_form", "Errore: " + msg.substring(3), request);
				String chiaverecord = (String)request.getAttribute("tx_codice_area_h");
				request.setAttribute("tx_codice_area", chiaverecord);
				chiaverecord = (String)request.getAttribute("tx_area_mercantile_h");
				request.setAttribute("tx_area_mercantile", chiaverecord);
				chiaverecord = (String)request.getAttribute("tx_descr_area_h");
				request.setAttribute("tx_descr_area_merc", chiaverecord);
				String ddlSocietaUtenteEnte = request.getAttribute("tx_societa") + "|" + request.getAttribute("tx_utente") +"|" + request.getAttribute("tx_ente");
				request.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);
				chiaverecord = (String)request.getAttribute("tx_data_ini_val_h");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Calendar tmp = Calendar.getInstance();
					tmp.setTime(df.parse(chiaverecord));
					request.setAttribute("tx_dval_ini", tmp);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			break;
		
		case TX_BUTTON_ADD:
			codop = "add";
			break;
		case TX_BUTTON_AGGIUNGI:
			try {
				dividiSocUtenteEnte(request);
				EsitoRisposte esito = new EsitoRisposte();
				esito = inserisciAreaMercantile(request);
				if(esito.getCodiceMessaggio().equals("OK")) {
					//session.setAttribute("recordInsert", "OK");
					setFormMessage("var_form", Messages.INS_OK.format(), request);
					request.setAttribute("tx_area_mercantile", "");
					request.setAttribute("tx_codice_area", "");
					request.setAttribute("tx_descr_area_merc", "");
					request.setAttribute("tx_dval_ini", "");
					request.setAttribute("tx_data_ini_val_h", "");
					request.setAttribute("tx_dval_fin", "");
				} else {
					//session.setAttribute("recordInsert", "KO");
					//session.setAttribute("messaggio.recordInsert", esito.getDescrizioneMessaggio());
					setFormMessage("var_form", esito.getDescrizioneMessaggio(), request);
					String chiaveRecord = (String)request.getAttribute("tx_codice_area");
					request.setAttribute("tx_codice_area", chiaveRecord);
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
			String chiaveRecord = (String)request.getAttribute("tx_area_mercantile");
			request.setAttribute("tx_area_mercantile_h",chiaveRecord);
			request.setAttribute("tx_button_cancel", "cancella");
			
			break;
		case TX_BUTTON_DELETE:
			try 
				{
					EsitoRisposte esito = new EsitoRisposte();
					esito = deleteAreaMercantile(request);
					if(esito.getCodiceMessaggio().equals("OK")) {
						session.setAttribute("recordDelete", "OK");
						session.setAttribute("messaggio.recordDelete", esito.getDescrizioneMessaggio());
					} else {
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


	private ConfigurazioneAreaMercantile selectConfigurazioneAreaMercantile(HttpServletRequest request) throws DaoException {
		
		String codiceAreaMerc  = (String)request.getAttribute("tx_area_mercantile_h");
		ConfigurazioneAreaMercantileDAO configurazioneAreaMercantileDAO;
		ConfigurazioneAreaMercantile configurazioneAreaMercantile = new ConfigurazioneAreaMercantile();
		configurazioneAreaMercantile.setCodiceSocieta(codiceSocieta);
		configurazioneAreaMercantile.setCuteCute(codiceUtente);
		configurazioneAreaMercantile.setChiaveEnte(chiaveEnte);
		configurazioneAreaMercantile.setCodiceKeyAreaMercantile(codiceAreaMerc);
		//inizio LP PG21XX04 Leak
		//configurazioneAreaMercantileDAO = MercatoDAOFactory.getConfigurazioneAreaMercantile(getMercatoDataSource(), getMercatoDbSchema());
		//configurazioneAreaMercantile = configurazioneAreaMercantileDAO.select(configurazioneAreaMercantile);
		//
		//return configurazioneAreaMercantile;
		Connection conn = null;
		try {
			conn = getMercatoDataSource().getConnection();
			configurazioneAreaMercantileDAO = MercatoDAOFactory.getConfigurazioneAreaMercantile(conn, getMercatoDbSchema());
			configurazioneAreaMercantile = configurazioneAreaMercantileDAO.select(configurazioneAreaMercantile);
			
			return configurazioneAreaMercantile;
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
	
	private String updateAreaMercantile(HttpServletRequest request) throws DaoException {
		
		String  societa = (String)request.getAttribute("tx_societa");
		String  utente = (String)request.getAttribute("tx_utente");
		String  ente = (String)request.getAttribute("tx_ente");
		String codiceKeyAreaMerc = (String)request.getAttribute("tx_area_mercantile_h");
		//Gestione Data Inizio Validità (per controllo)
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dataIniValCalendar  = Calendar.getInstance();		
		try {
			dataIniValCalendar.setTime(df.parse((String)request.getAttribute("tx_data_ini_val_h")));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		// Gestione Data Fine Validità
		String anno = (String)request.getAttribute("tx_dval_fin_year");
		String mese = (String)request.getAttribute("tx_dval_fin_month");
		String giorno =(String)request.getAttribute("tx_dval_fin_day");
		String datavalidita="";
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
		}
		if (dataIniValCalendar.compareTo(dataFinValCalendar)>0) {
			return "KO Data inizio validità superiore a data fine validità";
		} else {
			ConfigurazioneAreaMercantileDAO configurazioneAreaMercantileDAO;
		
			ConfigurazioneAreaMercantile configurazioneAreaMercantile = new ConfigurazioneAreaMercantile();
			configurazioneAreaMercantile.setCodiceSocieta(societa);
			configurazioneAreaMercantile.setCuteCute(utente);
			configurazioneAreaMercantile.setChiaveEnte(ente);
			configurazioneAreaMercantile.setCodiceKeyAreaMercantile(codiceKeyAreaMerc);
			//configurazioneAreaMercantile.setCodiceAreaMercantile(codiceAreaMerc);
			configurazioneAreaMercantile.setDataFineValidita(dataFinValCalendar);
		
			//inizio LP PG21XX04 Leak
			//configurazioneAreaMercantileDAO = MercatoDAOFactory.getConfigurazioneAreaMercantile(getMercatoDataSource(), getMercatoDbSchema());
			//int aggiornati = configurazioneAreaMercantileDAO.update(configurazioneAreaMercantile);
			int aggiornati = 0;
			Connection conn = null;
			try {
				conn = getMercatoDataSource().getConnection();
				configurazioneAreaMercantileDAO = MercatoDAOFactory.getConfigurazioneAreaMercantile(conn, getMercatoDbSchema());
				aggiornati = configurazioneAreaMercantileDAO.update(configurazioneAreaMercantile);
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
	
private EsitoRisposte inserisciAreaMercantile(HttpServletRequest request) throws DaoException {
		boolean Check = true;
		EsitoRisposte esitoRisposte = new EsitoRisposte();
		String codiceAreaMerc = (String)request.getAttribute("tx_codice_area");
		String descrAreaMerc = (String)request.getAttribute("tx_descr_area_merc");
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
		try {
			dataIniValCalendar.setTime(df.parse(datavalidita));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
			ConfigurazioneAreaMercantileDAO configurazioneAreaMercantileDAO;
		
			ConfigurazioneAreaMercantile configurazioneAreaMercantile = new ConfigurazioneAreaMercantile();
			configurazioneAreaMercantile.setCodiceSocieta(codiceSocieta);
			configurazioneAreaMercantile.setCuteCute(codiceUtente);
			configurazioneAreaMercantile.setChiaveEnte(chiaveEnte);
			configurazioneAreaMercantile.setCodiceAreaMercantile(codiceAreaMerc);
			configurazioneAreaMercantile.setDescrizioneAreaMercantile(descrAreaMerc);
			configurazioneAreaMercantile.setDataInizioValidita(dataIniValCalendar);
			configurazioneAreaMercantile.setDataFineValidita(dataFinValCalendar);
			UserBean user = (UserBean)request.getSession().getAttribute(SignOnKeys.USER_BEAN);
		
			//inizio LP PG21XX04 Leak
			//configurazioneAreaMercantileDAO = MercatoDAOFactory.getConfigurazioneAreaMercantile(getMercatoDataSource(), getMercatoDbSchema());
			//return configurazioneAreaMercantileDAO.insert(configurazioneAreaMercantile);
			Connection conn = null;
			try {
				conn = getMercatoDataSource().getConnection();
				configurazioneAreaMercantileDAO = MercatoDAOFactory.getConfigurazioneAreaMercantile(conn, getMercatoDbSchema());
				EsitoRisposte esito = configurazioneAreaMercantileDAO.insert(configurazioneAreaMercantile); 
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

private EsitoRisposte deleteAreaMercantile(HttpServletRequest request) throws DaoException {
	String  societa = (String)request.getAttribute("tx_societa");
	String  utente = (String)request.getAttribute("tx_utente");
	String  ente = (String)request.getAttribute("tx_ente");
	
	String codiceKeyAreaMerc = (String)request.getAttribute("tx_area_mercantile_h");
	
	ConfigurazioneAreaMercantileDAO configurazioneAreaMercantileDAO;
	
	ConfigurazioneAreaMercantile configurazioneAreaMercantile = new ConfigurazioneAreaMercantile();
	configurazioneAreaMercantile.setCodiceSocieta(societa);
	configurazioneAreaMercantile.setCuteCute(utente);
	configurazioneAreaMercantile.setChiaveEnte(ente);
	configurazioneAreaMercantile.setCodiceKeyAreaMercantile(codiceKeyAreaMerc);
	
	//inizio LP PG21XX04 Leak
	//configurazioneAreaMercantileDAO = MercatoDAOFactory.getConfigurazioneAreaMercantile(getMercatoDataSource(), getMercatoDbSchema());
	//return configurazioneAreaMercantileDAO.delete(configurazioneAreaMercantile);
	Connection conn = null;
	try {
		conn = getMercatoDataSource().getConnection();
		configurazioneAreaMercantileDAO = MercatoDAOFactory.getConfigurazioneAreaMercantile(conn, getMercatoDbSchema());
		EsitoRisposte esito = configurazioneAreaMercantileDAO.delete(configurazioneAreaMercantile); 
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
/*
	@SuppressWarnings("unchecked")
	private void getServiziDDL(HttpServletRequest request, HttpSession session) {
	//creo ddl option:
	ArrayList<Servizio> listaServizi = null;
	List<DdlOption> serviziDDLList  = new ArrayList<DdlOption>();
	ServizioDAO servizioDAO;
	try {
		servizioDAO = WalletDAOFactory.getServizioDAO(getWalletDataSource(), getWalletDbSchema());
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
	request.setAttribute("serviziDDLList", serviziDDLList);
	//session.setAttribute("serviziDDLList", serviziDDLList);
}*/
}

