package org.seda.payer.manager.mercatoconfig.actions;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.mercatomanager.actions.MercatoBaseManagerAction;

import com.seda.commons.security.TokenGenerator;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.mercato.bean.ConfigurazioneAreaMercantile;
import com.seda.payer.core.mercato.bean.ConfigurazionePiazzola;
import com.seda.payer.core.mercato.bean.ControlloPiazzoleResponse;
import com.seda.payer.core.mercato.bean.GestioneTariffeResponse;
//import com.seda.payer.core.mercato.bean.ControlloTariffeResponse;
import com.seda.payer.core.mercato.bean.EsitoRisposte;
import com.seda.payer.core.mercato.dao.ConfigurazioneAreaMercantileDAO;
import com.seda.payer.core.mercato.dao.ConfigurazionePiazzolaDAO;
import com.seda.payer.core.mercato.dao.MercatoDAOFactory;
import org.seda.payer.manager.mercatoconfig.actions.*;
import org.seda.payer.manager.mercatoconfig.service.*;
//import org.seda.payer.manager.mercatoconfig.service.MercatoServiceInterface;

import com.seda.tag.core.DdlOption;


@SuppressWarnings("serial")
public class PiazzolaEditAction extends MercatoBaseManagerAction{
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
			if (codop.equals(("edit"))) {
				String chiaverecord="";
				String chiave2rec = "";
				if (request.getAttribute("tx_piazzola")==null) {
					chiaverecord = (String)request.getAttribute("tx_piazzola_h");
				} else {
					chiaverecord = (String)request.getAttribute("tx_piazzola");
				}
				if (request.getAttribute("tx_area_mercantile")==null) {
					chiave2rec   = (String)request.getAttribute("tx_area_mercantile_h");
				} else {
					chiave2rec   = (String)request.getAttribute("tx_area_mercantile");
				}
				dividiSocUtenteEnte(request);
			//getAreeMercataliDDL(request, session);
				if (request.getAttribute("tx_key_piazzola")==null) {
					//
				} else {
					String chiave3rec = (String)request.getAttribute("tx_key_piazzola");
					request.setAttribute("tx_key_piazzola_h", chiave3rec);
				}
				request.setAttribute("tx_piazzola_h",chiaverecord);
				request.setAttribute("tx_area_mercantile_h", chiave2rec);
			
				if ( !(chiaverecord == null) ) {
					ConfigurazionePiazzola  configurazionePiazzola = null; 
					try {
						configurazionePiazzola = selectConfigurazionePiazzola(request);
					} catch (DaoException e) {
					// TODO Auto-generated catch block
						e.printStackTrace();
						setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
					}
					request.setAttribute("tx_area_mercantile", configurazionePiazzola.getCodiceKeyAreaMercantile());
					request.setAttribute("tx_piazzola", configurazionePiazzola.getCodicePiazzola());
					request.setAttribute("tx_descr_piazzola", configurazionePiazzola.getDescrizionePiazzola());
					request.setAttribute("tx_descr_piazzola_h", configurazionePiazzola.getDescrizionePiazzola());
					request.setAttribute("tx_Ang1Lat", configurazionePiazzola.getCoordLatAng1());
					request.setAttribute("tx_Ang1Lon", configurazionePiazzola.getCoordLonAng1());
					request.setAttribute("tx_Ang2Lat", configurazionePiazzola.getCoordLatAng2());
					request.setAttribute("tx_Ang2Lon", configurazionePiazzola.getCoordLonAng2());
					request.setAttribute("tx_Ang3Lat", configurazionePiazzola.getCoordLatAng3());
					request.setAttribute("tx_Ang3Lon", configurazionePiazzola.getCoordLonAng3());
					request.setAttribute("tx_Ang4Lat", configurazionePiazzola.getCoordLatAng4());
					request.setAttribute("tx_Ang4Lon", configurazionePiazzola.getCoordLonAng4());				
					request.setAttribute("tx_dval_ini", configurazionePiazzola.getDataInizioValidita());
					request.setAttribute("tx_dval_fin", configurazionePiazzola.getDataFineValidita());
				}
			} else {
				if (firedButton.equals(FiredButton.TX_BUTTON_RESET)) {
					request.setAttribute("tx_piazzola", "");
					request.setAttribute("tx_descr_piazzola", "");
					request.setAttribute("tx_Ang1Lat", "0,0");
					request.setAttribute("tx_Ang1Lon", "0,0");
					request.setAttribute("tx_Ang2Lat", "0,0");
					request.setAttribute("tx_Ang2Lon", "0,0");
					request.setAttribute("tx_Ang3Lat", "0,0");
					request.setAttribute("tx_Ang3Lon", "0,0");
					request.setAttribute("tx_Ang4Lat", "0,0");
					request.setAttribute("tx_Ang4Lon", "0,0");				
					request.setAttribute("tx_dval_ini", "");
					request.setAttribute("tx_dval_fin", "");
				}
			}
			
			break;
			
		case TX_BUTTON_EDIT:
			EsitoRisposte esito = new EsitoRisposte();
			try {
				esito = updatePiazzola(request);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
			}
			if(esito.getCodiceMessaggio().equals("OK")) {
				session.setAttribute("recordUpdate", "OK");
				if (esito.getDescrizioneMessaggio().length()>1) {
					setFormMessage("var_form", esito.getDescrizioneMessaggio(), request);
					//session.setAttribute("messaggio.recordUpdate", esito.getDescrizioneMessaggio());
				} else {
					setFormMessage("var_form", Messages.UPDT_OK.format(), request);
					String chiaveRecord = (String)request.getAttribute("tx_piazzola_h");
					request.setAttribute("tx_piazzola",chiaveRecord);
					String chiave2Rec = (String)request.getAttribute("tx_area_mercantile_h");
					request.setAttribute("tx_area_mercantile", chiave2Rec);
					String chiave3Rec = (String)request.getAttribute("tx_descr_piazzola_h");
					request.setAttribute("tx_descr_piazzola", chiave3Rec);
					String ddlSocietaUtenteEnte = request.getAttribute("tx_societa") + "|" + request.getAttribute("tx_utente") +"|" + request.getAttribute("tx_ente");
					request.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);		
				}
			} else {
				session.setAttribute("recordUpdate", "KK");
				//session.setAttribute("messaggio.recordUpdate", esito.getDescrizioneMessaggio());
				setFormMessage("var_form", esito.getDescrizioneMessaggio(), request);
				//setFormMessage("var_form", Messages.UPDT_OK.format(), request);
				String chiaveRecord = (String)request.getAttribute("tx_piazzola_h");
				request.setAttribute("tx_piazzola",chiaveRecord);
				String chiave2Rec = (String)request.getAttribute("tx_area_mercantile_h");
				request.setAttribute("tx_area_mercantile", chiave2Rec);
				String chiave3Rec = (String)request.getAttribute("tx_descr_piazzola_h");
				request.setAttribute("tx_descr_piazzola", chiave3Rec);
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
				esito = new EsitoRisposte();
				esito = inserisciPiazzola(request);
				if(esito.getCodiceMessaggio().equals("OK")) {
					setFormMessage("var_form", Messages.INS_OK.format(), request);
					request.setAttribute("tx_piazzola", "");
					request.setAttribute("tx_descr_piazzola", "");
					request.setAttribute("tx_Ang1Lat", "0,0");
					request.setAttribute("tx_Ang1Lon", "0,0");
					request.setAttribute("tx_Ang2Lat", "0,0");
					request.setAttribute("tx_Ang2Lon", "0,0");
					request.setAttribute("tx_Ang3Lat", "0,0");
					request.setAttribute("tx_Ang3Lon", "0,0");
					request.setAttribute("tx_Ang4Lat", "0,0");
					request.setAttribute("tx_Ang4Lon", "0,0");				
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
			String chiaveRecord = (String)request.getAttribute("tx_key_piazzola");
			request.setAttribute("tx_key_piazzola_h",chiaveRecord);
			String chiave2Rec = (String)request.getAttribute("tx_area_mercantile");
			request.setAttribute("tx_area_mercantile_h", chiave2Rec);
			request.setAttribute("tx_button_cancel", "cancella");
			
			break;
		case TX_BUTTON_DELETE:
			try 
				{
					esito = new EsitoRisposte();
					esito = deletePiazzola(request);
					if(esito.getCodiceMessaggio().equals("OK")) {
						session.setAttribute("recordDelete", "OK");
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
			getAreeMercataliDDL(request, session);
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


	private ConfigurazionePiazzola selectConfigPiazzolaForUpd(HttpServletRequest request) throws DaoException {
		String codicePiazzola  = (String)request.getAttribute("tx_piazzola_h");
		String codiceAreaMerc  = (String)request.getAttribute("tx_area_mercantile_h");
		ConfigurazionePiazzolaDAO configurazionePiazzolaDAO;
		ConfigurazionePiazzola configurazionePiazzola = new ConfigurazionePiazzola();
		configurazionePiazzola.setCodiceSocieta((String)request.getAttribute("tx_societa"));
		configurazionePiazzola.setCuteCute((String)request.getAttribute("tx_utente"));
		configurazionePiazzola.setChiaveEnte((String)request.getAttribute("tx_ente"));
		configurazionePiazzola.setCodicePiazzola(codicePiazzola);
		configurazionePiazzola.setCodiceKeyAreaMercantile(codiceAreaMerc);
		
		//inizio LP PG21XX04 Leak
		//configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(getMercatoDataSource(), getMercatoDbSchema());
		//configurazionePiazzola = configurazionePiazzolaDAO.select(configurazionePiazzola);
		
		//return configurazionePiazzola;
		Connection conn = null;
		try {
			conn = getMercatoDataSource().getConnection();
			configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(conn, getMercatoDbSchema());
			configurazionePiazzola = configurazionePiazzolaDAO.select(configurazionePiazzola);
			
			return configurazionePiazzola;
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new DaoException(e1);
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
	
	private ConfigurazionePiazzola selectConfigurazionePiazzola(HttpServletRequest request) throws DaoException {
		String codicePiazzola  = (String)request.getAttribute("tx_piazzola_h");
		String codiceAreaMerc  = (String)request.getAttribute("tx_area_mercantile_h");
		ConfigurazionePiazzolaDAO configurazionePiazzolaDAO;
		ConfigurazionePiazzola configurazionePiazzola = new ConfigurazionePiazzola();
		configurazionePiazzola.setCodiceSocieta(codiceSocieta);
		configurazionePiazzola.setCuteCute(codiceUtente);
		configurazionePiazzola.setChiaveEnte(chiaveEnte);
		configurazionePiazzola.setCodicePiazzola(codicePiazzola);
		configurazionePiazzola.setCodiceKeyAreaMercantile(codiceAreaMerc);
		
		//inizio LP PG21XX04 Leak
		//configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(getMercatoDataSource(), getMercatoDbSchema());
		//configurazionePiazzola = configurazionePiazzolaDAO.select(configurazionePiazzola);
		//
		//return configurazionePiazzola;
		Connection conn = null;
		try {
			conn = getMercatoDataSource().getConnection();
			configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(conn, getMercatoDbSchema());
			configurazionePiazzola = configurazionePiazzolaDAO.select(configurazionePiazzola);
			
			return configurazionePiazzola;
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new DaoException(e1);
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
	
	private EsitoRisposte updatePiazzola(HttpServletRequest request) throws DaoException {
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		boolean Check = true;
		boolean dataNonMod = false;
		boolean presenzaErrori = false;		
		String Messaggi = "";
		EsitoRisposte esitoRisposte = new EsitoRisposte();
		esitoRisposte.setCodiceMessaggio("OK");
		esitoRisposte.setDescrizioneMessaggio("-");
		Calendar today = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
		today.set(Calendar.HOUR_OF_DAY, 00);
		today.set(Calendar.MINUTE, 00);
		today.set(Calendar.SECOND, 00);
		today.set(Calendar.MILLISECOND, 000);
		String  societa = (String)request.getAttribute("tx_societa");
		String  utente = (String)request.getAttribute("tx_utente");
		String  ente = (String)request.getAttribute("tx_ente");
		String codicePiazzola = (String)request.getAttribute("tx_piazzola_h");
		String codiceAreaMerc = (String)request.getAttribute("tx_area_mercantile_h");
	// Recupero i dati attuali per i vari check
		ConfigurazionePiazzola  configPiazzola = null; 
		try {
			configPiazzola = selectConfigPiazzolaForUpd(request);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
		}
	// Campi aggiornabili	
		Double Ang1Lat = null;
		Double Ang1Lon = null;
		Double Ang2Lat = null;
		Double Ang2Lon = null;
		Double Ang3Lat = null;
		Double Ang3Lon = null;
		Double Ang4Lat = null;
		Double Ang4Lon = null;
		if (((String)request.getAttribute("tx_Ang1Lat") == null) ||((String)request.getAttribute("tx_Ang1Lat") == "")) {
			//
		} else {
			if (isNumeric(((String)request.getAttribute("tx_Ang1Lat")).replace(",","."))) {
				Ang1Lat = Double.parseDouble(((String)request.getAttribute("tx_Ang1Lat")).replace(",","."));
			} else {
				presenzaErrori=true;
				Messaggi="Valore Non Numerico \r\n";
			}
		}
		if (((String)request.getAttribute("tx_Ang1Lon") == null) ||((String)request.getAttribute("tx_Ang1Lon") == "")) {
			//
		} else {
			if (isNumeric(((String)request.getAttribute("tx_Ang1Lon")).replace(",","."))) {
				Ang1Lon = Double.parseDouble(((String)request.getAttribute("tx_Ang1Lon")).replace(",","."));
			} else {
				presenzaErrori=true;
				Messaggi="Valore Non Numerico \r\n";
			}
		}
		if (((String)request.getAttribute("tx_Ang2Lat") == null) ||((String)request.getAttribute("tx_Ang2Lat") == "")) {
			//
		} else {
			if (isNumeric(((String)request.getAttribute("tx_Ang2Lat")).replace(",","."))) {
				Ang2Lat = Double.parseDouble(((String)request.getAttribute("tx_Ang2Lat")).replace(",","."));
			} else {
				presenzaErrori=true;
				Messaggi="Valore Non Numerico \r\n";
			}
		}
		if (((String)request.getAttribute("tx_Ang2Lon") == null) ||((String)request.getAttribute("tx_Ang2Lon") == "")) {
			//
		} else {
			if (isNumeric(((String)request.getAttribute("tx_Ang2Lon")).replace(",","."))) {
				Ang2Lon = Double.parseDouble(((String)request.getAttribute("tx_Ang2Lon")).replace(",","."));
			} else {
				presenzaErrori=true;
				Messaggi="Valore Non Numerico \r\n";
			}
		}
		if (((String)request.getAttribute("tx_Ang3Lat") == null) ||((String)request.getAttribute("tx_Ang3Lat") == "")) {
			//
		} else {
			if (isNumeric(((String)request.getAttribute("tx_Ang3Lat")).replace(",","."))) {
				Ang3Lat = Double.parseDouble(((String)request.getAttribute("tx_Ang3Lat")).replace(",","."));
			} else {
				presenzaErrori=true;
				Messaggi="Valore Non Numerico \r\n";
			}
		}
		if (((String)request.getAttribute("tx_Ang3Lon") == null) ||((String)request.getAttribute("tx_Ang3Lon") == "")) {
			//
		} else {
			if (isNumeric(((String)request.getAttribute("tx_Ang3Lon")).replace(",","."))) {
				Ang3Lon = Double.parseDouble(((String)request.getAttribute("tx_Ang3Lon")).replace(",","."));
			} else {
				presenzaErrori=true;
				Messaggi="Valore Non Numerico \r\n";
			}
		}
		if (((String)request.getAttribute("tx_Ang4Lat") == null) ||((String)request.getAttribute("tx_Ang4Lat") == "")) {
			//
		} else {
			if (isNumeric(((String)request.getAttribute("tx_Ang4Lat")).replace(",","."))) {
				Ang4Lat = Double.parseDouble(((String)request.getAttribute("tx_Ang4Lat")).replace(",","."));
			} else {
				presenzaErrori=true;
				Messaggi="Valore Non Numerico \r\n";
			}
		}
		if (((String)request.getAttribute("tx_Ang4Lon") == null) ||((String)request.getAttribute("tx_Ang4Lon") == "")) {
			//
		} else {
			if (isNumeric(((String)request.getAttribute("tx_Ang4Lon")).replace(",","."))) {
				Ang4Lon = Double.parseDouble(((String)request.getAttribute("tx_Ang4Lon")).replace(",","."));
			} else {
				presenzaErrori=true;
				Messaggi="Valore Non Numerico \r\n";
			}
		}			
	// Gestione Data inizio e fine validita
		String anno = (String)request.getAttribute("tx_dval_ini_year");
		String mese = (String)request.getAttribute("tx_dval_ini_month");
		String giorno =(String)request.getAttribute("tx_dval_ini_day");
		String datavalidita="";
		if (!(anno.equals(""))) {
			datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		} else {
			datavalidita="--";
		}
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
		anno = (String)request.getAttribute("tx_dval_fin_year");
		mese = (String)request.getAttribute("tx_dval_fin_month");
		giorno =(String)request.getAttribute("tx_dval_fin_day");
		datavalidita="";
		if (!(anno.equals(""))) {
			datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		} else {
			datavalidita="--";
		}
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

	//Controlli sulla data di inizio e fine validità
		if (dataIniValCalendar.compareTo(today)<0)  {
			if (!(dataFinValCalendar == null)) {
				if (dataFinValCalendar.compareTo(today) > 0) {
					Calendar TmpData = Calendar.getInstance();
					TmpData.setTimeInMillis(configPiazzola.getDataInizioValidita().getTimeInMillis());
					TmpData.set(Calendar.HOUR_OF_DAY, 0);
					TmpData.set(Calendar.MINUTE, 0);
					TmpData.set(Calendar.SECOND, 0);
					TmpData.set(Calendar.MILLISECOND, 0);
					if (dataIniValCalendar.compareTo(TmpData)==0) {
						//Data non modificata
						dataNonMod=true;
					} else {
						Messaggi = Messaggi + "Data inizio validità non corretta \r\n";
						dataIniValCalendar.setTimeInMillis(configPiazzola.getDataInizioValidita().getTimeInMillis());
						presenzaErrori = true;
					}
				}
			}
		}
		if (!(dataFinValCalendar == null)) { 
			if (dataFinValCalendar.compareTo(today) < 0) {
				Messaggi = Messaggi + "Data fine validità non corretta \r\n";
				dataIniValCalendar.setTimeInMillis(configPiazzola.getDataInizioValidita().getTimeInMillis());
				if (configPiazzola.getDataFineValidita()==null) {
					dataFinValCalendar=null;
				} else {
					dataFinValCalendar.setTimeInMillis(configPiazzola.getDataFineValidita().getTimeInMillis());
				}
				presenzaErrori = true;
			}	
		}
		if (dataIniValCalendar.compareTo(dataFinValCalendar)>0) {
			Messaggi = Messaggi + "Data fine validità inferiore alla data inizio validità \r\n";
			presenzaErrori = true;
			Check=false; //Non faccio il check se ho già riscontrato un errore.
		}
		if (dataFinValCalendar.compareTo(configPiazzola.getDataFineValidita())==0) {
			if (dataNonMod) {
				//Le date non sono state variate, non le verifico!
				Check=false;
			}
		}
		//Accedo all'area Mercatale per un controllo incrociato sulle date validità
		ConfigurazioneAreaMercantileDAO configurazioneMercatoDAO;
		ConfigurazioneAreaMercantile mercato = new ConfigurazioneAreaMercantile();
		mercato.setCodiceSocieta(societa);
		mercato.setCuteCute(utente);
		mercato.setChiaveEnte(ente);
		mercato.setCodiceKeyAreaMercantile(codiceAreaMerc);
		System.out.println("societa       =" + societa);
		System.out.println("utente        =" + utente);
		System.out.println("ente          =" + ente);
		System.out.println("codiceAreaMerc=" + codiceAreaMerc);	
		//inizio LP PG21XX04 Leak
		//configurazioneMercatoDAO = MercatoDAOFactory.getConfigurazioneAreaMercantile(getMercatoDataSource(), getMercatoDbSchema());
		//mercato = configurazioneMercatoDAO.select(mercato);
		try {
			conn = getMercatoDataSource().getConnection();
			configurazioneMercatoDAO = MercatoDAOFactory.getConfigurazioneAreaMercantile(conn, getMercatoDbSchema());
			mercato = configurazioneMercatoDAO.select(mercato);
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new DaoException(e1);
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
		
		System.out.println("DataFinValCalendar=" + dataFinValCalendar.getTimeInMillis());
		System.out.println("Mercato Fine Valid=" + mercato.getDataFineValidita().getTimeInMillis());
		if (dataIniValCalendar.compareTo(mercato.getDataInizioValidita())<0) {
			Check=false;
			presenzaErrori=true;
			Messaggi = Messaggi + "Data Inizio validità inferiore alla data inizio validità dell'Area Mercantile \r\n";
		} else if (dataIniValCalendar.compareTo(mercato.getDataFineValidita())>0) {
			Check=false;
			presenzaErrori=true;
			Messaggi = Messaggi + "Data Inizio validità superiore alla data fine validità dell'Area Mercantile \r\n";
		}
		if ((Check)&&(!(dataFinValCalendar==null))) {
			if (dataFinValCalendar.compareTo(mercato.getDataInizioValidita())<0){
				Check=false;
				presenzaErrori=true;
				Messaggi = Messaggi + "Data Fine validità inferiore alla data inizio validità dell'Area Mercantile \r\n";				
			} else if (dataFinValCalendar.compareTo(mercato.getDataFineValidita())>0){
				Check=false;
				presenzaErrori=true;
				Messaggi = Messaggi + "Data Fine validità superiore alla data fine validità dell'Area Mercantile \r\n";								
			}
		}
		

		ConfigurazionePiazzolaDAO configurazionePiazzolaDAO;
			//Recupero il codiceKey			
		ConfigurazionePiazzola configurazionePiazzola = new ConfigurazionePiazzola();
		configurazionePiazzola.setCodiceSocieta(societa);
		configurazionePiazzola.setCuteCute(utente);
		configurazionePiazzola.setChiaveEnte(ente);
		configurazionePiazzola.setCodicePiazzola(codicePiazzola);
		configurazionePiazzola.setCodiceKeyAreaMercantile(codiceAreaMerc);

		if (Check) {  //Faccio il check delle date solo se variate e non ho riscontrato errori in precedenza
			//inizio LP PG21XX04 Leak
			//configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(getMercatoDataSource(), getMercatoDbSchema());
			//configurazionePiazzola = configurazionePiazzolaDAO.select(configurazionePiazzola);
			//inizio LP PG21XX04 Leak2
			Connection connS = null;
			//fine LP PG21XX04 Leak2
			try {
				//inizio LP PG21XX04 Leak2
				//conn = getMercatoDataSource().getConnection();
				//configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(conn, getMercatoDbSchema());
				connS = getMercatoDataSource().getConnection();
				configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(connS, getMercatoDbSchema());
				//fine LP PG21XX04 Leak2
				configurazionePiazzola = configurazionePiazzolaDAO.select(configurazionePiazzola);
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new DaoException(e1);
			} finally {
				//inizio LP PG21XX04 Leak2
				//if (conn != null) {
				//	try {
				//		conn.close();
				//	} catch (SQLException e) {
				//		e.printStackTrace();
				//	}
				//}
				if (connS != null) {
					try {
						connS.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				//fine LP PG21XX04 Leak2
			}
			//fine LP PG21XX04 Leak
			
			MercatoServiceImpl mercatoService = new MercatoServiceImpl();
			mercatoService.setMercatoDataSource(getMercatoDataSource());
			mercatoService.setMercatoDbSchema(getMercatoDbSchema());
			ArrayList <GestioneTariffeResponse> esito = new ArrayList<GestioneTariffeResponse>();
			esito = mercatoService.gestisciPiazzola(configurazionePiazzola.getCodiceKeyPiazzola(), dataIniValCalendar, dataFinValCalendar);

			if (esito!=null && !esito.isEmpty()) {
				for (Iterator iterator = esito.iterator(); iterator.hasNext();) {
					GestioneTariffeResponse item = (GestioneTariffeResponse) iterator.next();
					if (item.isEsito()) {
					} else {
						presenzaErrori=true;
						Messaggi = Messaggi + item.getMessaggio() + "\n\r";
					}
				}
			}
		}
		if (!(presenzaErrori)) {	//Se non ho errori aggiorno
				configurazionePiazzola.setCodiceSocieta(societa);
				configurazionePiazzola.setCuteCute(utente);
				configurazionePiazzola.setChiaveEnte(ente);
				configurazionePiazzola.setCodicePiazzola(codicePiazzola);
				configurazionePiazzola.setCodiceKeyAreaMercantile(codiceAreaMerc);
				configurazionePiazzola.setCoordLatAng1(Ang1Lat);
				configurazionePiazzola.setCoordLonAng1(Ang1Lon);
				configurazionePiazzola.setCoordLatAng2(Ang2Lat);
				configurazionePiazzola.setCoordLonAng2(Ang2Lon);
				configurazionePiazzola.setCoordLatAng3(Ang3Lat);
				configurazionePiazzola.setCoordLonAng3(Ang3Lon);
				configurazionePiazzola.setCoordLatAng4(Ang4Lat);
				configurazionePiazzola.setCoordLonAng4(Ang4Lon);		
				configurazionePiazzola.setDataInizioValidita(dataIniValCalendar);
				configurazionePiazzola.setDataFineValidita(dataFinValCalendar);
			
				//inizio LP PG21XX04 Leak
				//configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(getMercatoDataSource(), getMercatoDbSchema());
				//int aggiornati = configurazionePiazzolaDAO.update(configurazionePiazzola);
				int aggiornati = 0;
				//inizio LP PG21XX04 Leak2
				Connection connS = null;
				//fine LP PG21XX04 Leak2
				try {
					//inizio LP PG21XX04 Leak2
					//conn = getMercatoDataSource().getConnection();
					//configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(conn, getMercatoDbSchema());
					connS = getMercatoDataSource().getConnection();
					configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(connS, getMercatoDbSchema());
					//fine LP PG21XX04 Leak2
					aggiornati = configurazionePiazzolaDAO.update(configurazionePiazzola);
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new DaoException(e1);
				} finally {
					//inizio LP PG21XX04 Leak2
					//if (conn != null) {
					//	try {
					//		conn.close();
					//	} catch (SQLException e) {
					//		e.printStackTrace();
					//	}
					//}
					if (connS != null) {
						try {
							connS.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					//fine LP PG21XX04 Leak2
				}
				//fine LP PG21XX04 Leak
				
				if (aggiornati > 0) {
					return esitoRisposte;
				} else {
					esitoRisposte.setCodiceMessaggio("KO");
					esitoRisposte.setDescrizioneMessaggio("Aggiornamento non riuscito");
					return esitoRisposte;
				}
			} else {
				esitoRisposte.setCodiceMessaggio("KO");
				esitoRisposte.setDescrizioneMessaggio(Messaggi);
				return esitoRisposte;
			}
		
	}
	
private EsitoRisposte inserisciPiazzola(HttpServletRequest request) throws DaoException {
		boolean Check = true;
		EsitoRisposte esitoRisposte = new EsitoRisposte();
	//Chiave primaria autogenerata da classe
		String chiavePiazzola = "";
		try {
			chiavePiazzola = TokenGenerator.generateUUIDToken();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}			
		String codicePiazzola = (String)request.getAttribute("tx_piazzola");
		String descrPiazzola = (String)request.getAttribute("tx_descr_piazzola");
		String codiceKeyAreaMercantile = (String)request.getAttribute("tx_area_mercantile");
		Double Ang1Lat = null;
		Double Ang1Lon = null;
		Double Ang2Lat = null;
		Double Ang2Lon = null;
		Double Ang3Lat = null;
		Double Ang3Lon = null;
		Double Ang4Lat = null;
		Double Ang4Lon = null;
		if (((String)request.getAttribute("tx_Ang1Lat") == null) ||((String)request.getAttribute("tx_Ang1Lat") == "")) {
			//
		} else {
			((String)request.getAttribute("tx_Ang1Lat")).replace(",",".");
			Ang1Lat = Double.parseDouble(((String)request.getAttribute("tx_Ang1Lat")).replace(",","."));
		}
		if (((String)request.getAttribute("tx_Ang1Lon") == null) ||((String)request.getAttribute("tx_Ang1Lon") == "")) {
			//
		} else {
			Ang1Lon = Double.parseDouble(((String)request.getAttribute("tx_Ang1Lon")).replace(",","."));
		}
		if (((String)request.getAttribute("tx_Ang2Lat") == null) ||((String)request.getAttribute("tx_Ang2Lat") == "")) {
			//
		} else {
			Ang2Lat = Double.parseDouble(((String)request.getAttribute("tx_Ang2Lat")).replace(",","."));
		}
		if (((String)request.getAttribute("tx_Ang2Lon") == null) ||((String)request.getAttribute("tx_Ang2Lon") == "")) {
			//
		} else {
			Ang2Lon = Double.parseDouble(((String)request.getAttribute("tx_Ang2Lon")).replace(",","."));
		}
		if (((String)request.getAttribute("tx_Ang3Lat") == null) ||((String)request.getAttribute("tx_Ang3Lat") == "")) {
			//
		} else {
			Ang3Lat = Double.parseDouble(((String)request.getAttribute("tx_Ang3Lat")).replace(",","."));
		}
		if (((String)request.getAttribute("tx_Ang3Lon") == null) ||((String)request.getAttribute("tx_Ang3Lon") == "")) {
			//
		} else {
			Ang3Lon = Double.parseDouble(((String)request.getAttribute("tx_Ang3Lon")).replace(",","."));
		}
		if (((String)request.getAttribute("tx_Ang4Lat") == null) ||((String)request.getAttribute("tx_Ang4Lat") == "")) {
			//
		} else {
			Ang4Lat = Double.parseDouble(((String)request.getAttribute("tx_Ang4Lat")).replace(",","."));
		}
		if (((String)request.getAttribute("tx_Ang4Lon") == null) ||((String)request.getAttribute("tx_Ang4Lon") == "")) {
			//
		} else {
			Ang4Lon = Double.parseDouble(((String)request.getAttribute("tx_Ang4Lon")).replace(",","."));
		}
		// Gestione Data Inizio Validita
		String anno;
		String mese;
		String giorno;
		Calendar today = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
		if ((String)request.getAttribute("tx_dval_ini_year") == null) {
			anno = Integer.toString(today.get(today.YEAR));
		} else {
			anno = (String)request.getAttribute("tx_dval_ini_year");
		}
		if ((String)request.getAttribute("tx_dval_ini_month") == null) {
			mese = Integer.toString(today.get(today.MONTH));
		} else {
			mese = (String)request.getAttribute("tx_dval_ini_month");
		}
		if ((String)request.getAttribute("tx_dval_ini_day") == null) {
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
	//La data di fine validità deve essere successiva alla data di inizio validità
		if (!(dataFinValCalendar==null)) {
			if (dataFinValCalendar.compareTo(dataIniValCalendar) <= 0) {
				Check=false;
				esitoRisposte.setCodiceMessaggio("KO");
				esitoRisposte.setDescrizioneMessaggio("Data fine validità inferiore o uguale a data inizio validità");
			}
		}
	//Accedo all'area Mercatale per un controllo incrociato sulle date validità
		ConfigurazioneAreaMercantileDAO configurazioneMercatoDAO;
		ConfigurazioneAreaMercantile mercato = new ConfigurazioneAreaMercantile();
		mercato.setCodiceSocieta(codiceSocieta);
		mercato.setCuteCute(codiceUtente);
		mercato.setChiaveEnte(chiaveEnte);
		mercato.setCodiceKeyAreaMercantile(codiceKeyAreaMercantile);
		//inizio LP PG21XX04 Leak
		//configurazioneMercatoDAO = MercatoDAOFactory.getConfigurazioneAreaMercantile(getMercatoDataSource(), getMercatoDbSchema());
		//mercato = configurazioneMercatoDAO.select(mercato);
		Connection conn = null;
		try {
			conn = getMercatoDataSource().getConnection();
			configurazioneMercatoDAO = MercatoDAOFactory.getConfigurazioneAreaMercantile(conn, getMercatoDbSchema());
			mercato = configurazioneMercatoDAO.select(mercato);
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new DaoException(e1);
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
		System.out.println("DataFinValCalendar=" + dataFinValCalendar.getTimeInMillis());
		System.out.println("Mercato Fine Valid=" + mercato.getDataFineValidita().getTimeInMillis());
		if (dataIniValCalendar.compareTo(mercato.getDataInizioValidita())<0) {
			Check=false;
			esitoRisposte.setCodiceMessaggio("KO");
			esitoRisposte.setDescrizioneMessaggio("Data Inizio validità inferiore alla data inizio validità dell'Area Mercantile");
		} else if (dataIniValCalendar.compareTo(mercato.getDataFineValidita())>0) {
			Check=false;
			esitoRisposte.setCodiceMessaggio("KO");
			esitoRisposte.setDescrizioneMessaggio("Data Inizio validità superiore alla data fine validità dell'Area Mercantile");
		}
		if ((Check)&&(!(dataFinValCalendar==null))) {
			if (dataFinValCalendar.compareTo(mercato.getDataInizioValidita())<0){
				Check=false;
				esitoRisposte.setCodiceMessaggio("KO");
				esitoRisposte.setDescrizioneMessaggio("Data Fine validità inferiore alla data inizio validità dell'Area Mercantile");				
			} else if (dataFinValCalendar.compareTo(mercato.getDataFineValidita())>0){
				Check=false;
				esitoRisposte.setCodiceMessaggio("KO");
				esitoRisposte.setDescrizioneMessaggio("Data Fine validità superiore alla data fine validità dell'Area Mercantile");								
			}
		}
		
		if (Check) {
			ConfigurazionePiazzolaDAO configurazionePiazzolaDAO;
		
			ConfigurazionePiazzola configurazionePiazzola = new ConfigurazionePiazzola();
			configurazionePiazzola.setCodiceKeyPiazzola(chiavePiazzola);
			configurazionePiazzola.setCodiceSocieta(codiceSocieta);
			configurazionePiazzola.setCuteCute(codiceUtente);
			configurazionePiazzola.setChiaveEnte(chiaveEnte);
			configurazionePiazzola.setCodicePiazzola(codicePiazzola);
			configurazionePiazzola.setDescrizionePiazzola(descrPiazzola);
			configurazionePiazzola.setCodiceKeyAreaMercantile(codiceKeyAreaMercantile);
			configurazionePiazzola.setCoordLatAng1(Ang1Lat);
			configurazionePiazzola.setCoordLonAng1(Ang1Lon);
			configurazionePiazzola.setCoordLatAng2(Ang2Lat);
			configurazionePiazzola.setCoordLonAng2(Ang2Lon);
			configurazionePiazzola.setCoordLatAng3(Ang3Lat);
			configurazionePiazzola.setCoordLonAng3(Ang3Lon);
			configurazionePiazzola.setCoordLatAng4(Ang4Lat);
			configurazionePiazzola.setCoordLonAng4(Ang4Lon);
			configurazionePiazzola.setDataInizioValidita(dataIniValCalendar);
			configurazionePiazzola.setDataFineValidita(dataFinValCalendar);
			UserBean user = (UserBean)request.getSession().getAttribute(SignOnKeys.USER_BEAN);
		
			//inizio LP PG21XX04 Leak
			//configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(getMercatoDataSource(), getMercatoDbSchema());
			//return configurazionePiazzolaDAO.insert(configurazionePiazzola);
			//inizio LP PG21XX04 Leak2
			Connection connS = null;
			//fine LP PG21XX04 Leak2
			try {
				//inizio LP PG21XX04 Leak2
				//conn = getMercatoDataSource().getConnection();
				//configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(conn, getMercatoDbSchema());
				connS = getMercatoDataSource().getConnection();
				configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(connS, getMercatoDbSchema());
				//fine LP PG21XX04 Leak2
				EsitoRisposte esito = configurazionePiazzolaDAO.insert(configurazionePiazzola);
				return esito;
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new DaoException(e1);
			} finally {
				//inizio LP PG21XX04 Leak2
				//if (conn != null) {
				//	try {
				//		conn.close();
				//	} catch (SQLException e) {
				//		e.printStackTrace();
				//	}
				//}
				if (connS != null) {
					try {
						connS.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				//fine LP PG21XX04 Leak2
			}
			//fine LP PG21XX04 Leak
			
		} else {
			return esitoRisposte;
		}
		
		
		
	}

private EsitoRisposte deletePiazzola(HttpServletRequest request) throws DaoException {
	String  societa = (String)request.getAttribute("tx_societa");
	String  utente = (String)request.getAttribute("tx_utente");
	String  ente = (String)request.getAttribute("tx_ente");
	
	String codicePiazzola = (String)request.getAttribute("tx_key_piazzola_h");
	String codiceAreaMerc = (String)request.getAttribute("tx_area_mercantile_h");
	

	ConfigurazionePiazzolaDAO configurazionePiazzolaDAO;
	
	ConfigurazionePiazzola configurazionePiazzola = new ConfigurazionePiazzola();
	configurazionePiazzola.setCodiceSocieta(societa);
	configurazionePiazzola.setCuteCute(utente);
	configurazionePiazzola.setChiaveEnte(ente);
	configurazionePiazzola.setCodiceKeyPiazzola(codicePiazzola);
	configurazionePiazzola.setCodiceKeyAreaMercantile(codiceAreaMerc);
	
	//inizio LP PG21XX04 Leak
	//configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(getMercatoDataSource(), getMercatoDbSchema());
	//return configurazionePiazzolaDAO.delete(configurazionePiazzola);
	Connection conn = null;
	try {
		conn = getMercatoDataSource().getConnection();
		configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(conn, getMercatoDbSchema());
		EsitoRisposte esito = configurazionePiazzolaDAO.delete(configurazionePiazzola);
		return esito;
	} catch (Exception e1) {
		e1.printStackTrace();
		throw new DaoException(e1);
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


public boolean isNumeric(String s) {
	return s != null && s.matches("[-+]?\\d*\\.?\\d+");
}

@SuppressWarnings("unchecked")
private void getAreeMercataliDDL(HttpServletRequest request, HttpSession session) {
	//dividiSocUtenteEnte(request);	
	List<DdlOption> areemercDDLList  = new ArrayList<DdlOption>();
	ConfigurazioneAreaMercantileDAO configurazioneAreaMercantileDAO;
	ConfigurazioneAreaMercantile configurazioneAreaMercantile = new ConfigurazioneAreaMercantile();
// Se non ho selezionato un elemento SocUtenteEnte non devo esporre nessun mercato!
	if (((String)request.getAttribute("ddlSocietaUtenteEnte")==null)||((String)request.getAttribute("ddlSocietaUtenteEnte")=="")) {
		//
	} else {
		dividiSocUtenteEnte(request);
	}
	if (((codiceSocieta==null)||(codiceSocieta.equals(""))) &&
		((codiceUtente==null)||(codiceUtente.equals(""))) &&
		((chiaveEnte==null)||(chiaveEnte.equals("")))) {
		if (((String)request.getAttribute("tx_societa")==null) || ((String)request.getAttribute("tx_societa")=="")) {
			configurazioneAreaMercantile.setCodiceSocieta("xx");
			configurazioneAreaMercantile.setCuteCute("xx");
			configurazioneAreaMercantile.setChiaveEnte("xx");
		} else {
			configurazioneAreaMercantile.setCodiceSocieta((String)request.getAttribute("tx_societa"));
			configurazioneAreaMercantile.setCuteCute((String)request.getAttribute("tx_utente"));
			configurazioneAreaMercantile.setChiaveEnte((String)request.getAttribute("tx_ente"));
		}
	} else {
		configurazioneAreaMercantile.setCodiceSocieta(codiceSocieta);
		configurazioneAreaMercantile.setCuteCute(codiceUtente);
		configurazioneAreaMercantile.setChiaveEnte(chiaveEnte);
	}
	ArrayList<ConfigurazioneAreaMercantile> configurazioneAreaMercantileList = null;
	//inizio LP PG21XX04 Leak
	Connection conn = null;
	//fine LP PG21XX04 Leak
	try {
		//inizio LP PG21XX04 Leak
		//configurazioneAreaMercantileDAO = MercatoDAOFactory.getConfigurazioneAreaMercantile(getMercatoDataSource(), getMercatoDbSchema());
		conn = getMercatoDataSource().getConnection();
		configurazioneAreaMercantileDAO = MercatoDAOFactory.getConfigurazioneAreaMercantile(conn, getMercatoDbSchema());
		//fine LP PG21XX04 Leak
		configurazioneAreaMercantileList = configurazioneAreaMercantileDAO.listAreaMercantile(configurazioneAreaMercantile);
		for (Iterator iterator = configurazioneAreaMercantileList.iterator(); iterator.hasNext();) {
			ConfigurazioneAreaMercantile item = (ConfigurazioneAreaMercantile) iterator.next();
			DdlOption optionAreaMerc = new DdlOption();
			optionAreaMerc.setSValue(item.getCodiceKeyAreaMercantile());
			optionAreaMerc.setSText(item.getDescrizioneAreaMercantile());
			areemercDDLList.add(optionAreaMerc);
		}
	} catch (DaoException e1) {
		e1.printStackTrace();
	}
	//inizio LP PG21XX04 Leak
	catch (Exception e1) {
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
	request.setAttribute("areemercDDLList", areemercDDLList); 
}

//Termine della classe
}

