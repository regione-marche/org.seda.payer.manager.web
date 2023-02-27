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
import org.seda.payer.manager.mercatoconfig.service.MercatoServiceImpl;
import org.seda.payer.manager.mercatomanager.actions.MercatoBaseManagerAction;

import com.seda.commons.security.TokenGenerator;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.mercato.bean.ConfigurazioneAreaMercantile;
import com.seda.payer.core.mercato.bean.ConfigurazionePeriodoGiornaliero;
import com.seda.payer.core.mercato.bean.ConfigurazionePiazzola;
import com.seda.payer.core.mercato.bean.ConfigurazioneTariffe;
import com.seda.payer.core.mercato.bean.ConfigurazioneTipologiaBanco;
import com.seda.payer.core.mercato.bean.ControlloTariffeResponse;
import com.seda.payer.core.mercato.bean.EsitoRisposte;
import com.seda.payer.core.mercato.bean.GestioneTariffeResponse;
import com.seda.payer.core.mercato.dao.ConfigurazioneAreaMercantileDAO;
import com.seda.payer.core.mercato.dao.ConfigurazionePeriodoGiornalieroDAO;
import com.seda.payer.core.mercato.dao.ConfigurazionePiazzolaDAO;
import com.seda.payer.core.mercato.dao.ConfigurazioneTariffeDAO;
import com.seda.payer.core.mercato.dao.ConfigurazioneTipologiaBancoDAO;
import com.seda.payer.core.mercato.dao.ConfigurazioneCompensoDAO;
import com.seda.payer.core.mercato.bean.ConfigurazioneCompenso;
import com.seda.payer.core.mercato.dao.MercatoDAOFactory;
import com.seda.payer.core.mercato.dao.ConfigurazionePrenotazioniDAO;
import com.seda.tag.core.DdlOption;


@SuppressWarnings("serial")
public class TariffeEditAction extends MercatoBaseManagerAction{
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
				String chiave1rec = "";
				String chiave2rec = "";
				String chiave3rec = "";
				String chiave4rec = "";
				String chiave5rec = "";
				String chiave6rec = "";
				if (request.getAttribute("tx_area_mercantile")==null) {
					chiave1rec  = (String)request.getAttribute("tx_area_mercantile_h");
				} else {
					chiave1rec  = (String)request.getAttribute("tx_area_mercantile");
				}
				if (request.getAttribute("tx_piazzola")==null) {
					chiave2rec  = (String)request.getAttribute("tx_piazzola_h");
				} else {
					chiave2rec  = (String)request.getAttribute("tx_piazzola");
				}
				if (request.getAttribute("tx_num_autor")==null) {
					chiave3rec  = (String)request.getAttribute("tx_num_autor_h");
				} else {
					chiave3rec  = (String)request.getAttribute("tx_num_autor");
				}
				if (request.getAttribute("tx_tipologia_banco")==null) {
					chiave4rec  = (String)request.getAttribute("tx_tipologia_banco_h");
				} else {
					chiave4rec  = (String)request.getAttribute("tx_tipologia_banco");
				}
				if (request.getAttribute("tx_giorno_settimana")==null) {
					chiave5rec  = (String)request.getAttribute("tx_giorno_settimana_h");
				} else { 
					chiave5rec  = (String)request.getAttribute("tx_giorno_settimana");
				}
				chiave6rec  = (String)request.getAttribute("tx_cod_key_tariffa_h");
			
				dividiSocUtenteEnte(request);
				getAreeMercataliDDL(request, session);
				getTipologieBancoDDL(request, session);
				getPeriodoGiornalieroDDL(request, session);
				getPiazzolaDDL(request, session);
				request.setAttribute("tx_area_mercantile_h", chiave1rec);
				request.setAttribute("tx_piazzola_h", chiave2rec);
				request.setAttribute("tx_num_autor_h", chiave3rec);
				request.setAttribute("tx_tipologia_banco_h", chiave4rec);
				request.setAttribute("tx_giorno_settimana_h", chiave5rec);
				request.setAttribute("tx_cod_key_tariffa_h", chiave6rec);
			
				if ( !(chiave1rec == null) ) {
					ConfigurazioneTariffe  configurazioneTariffe = null; 
					try {
						configurazioneTariffe = selectConfigurazioneTariffe(request);
					} catch (DaoException e) {
					// TODO Auto-generated catch block
						e.printStackTrace();
						setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
					}
					String Double = "";
					Double = String.valueOf(configurazioneTariffe.getTariffaTari());
					Double = Double.replace(".", ",");
					request.setAttribute("tx_cod_key_tariffa_h", configurazioneTariffe.getCodiceKeyTariffa());
					request.setAttribute("tx_area_mercantile", configurazioneTariffe.getCodiceKeyAreaMercantile());
					request.setAttribute("tx_num_autor", chiave3rec);
					request.setAttribute("tx_piazzola", configurazioneTariffe.getCodiceKeyPiazzola());
					Double = String.valueOf(configurazioneTariffe.getTariffaTari());
					Double = Double.replace(".", ",");
					String[] row1 = Double.split(",");
					if (row1[1].trim().length()<2) {
						if (row1[1].trim().length()==0) {
							Double = Double + "00";
						} else {
							Double = Double + "0";
						}
					}
				//request.setAttribute("tx_tariffa_tari", configurazioneTariffe.getTariffaTari());
					request.setAttribute("tx_tariffa_tari", Double);
					Double="";
					Double = String.valueOf(configurazioneTariffe.getTariffaCosap());
					Double = Double.replace(".", ",");
					String[] row2 = Double.split(",");
					if (row2[1].trim().length()<2) {
						if (row2[1].trim().length()==0) {
							Double = Double + "00";
						} else {
							Double = Double + "0";
						}
					}
				//request.setAttribute("tx_tariffa_cosap", configurazioneTariffe.getTariffaCosap());
					request.setAttribute("tx_tariffa_cosap", Double);
					//request.setAttribute("tx_codice_compenso", configurazioneTariffe.getCodiceKeyCompenso());
					request.setAttribute("tx_dval_ini", configurazioneTariffe.getDataInizioValidita());
					request.setAttribute("tx_dval_fin", configurazioneTariffe.getDataFineValidita());
				} else {
					if (firedButton.equals(FiredButton.TX_BUTTON_RESET)) {
						request.setAttribute("tx_area_mercantile", "");
						request.setAttribute("tx_piazzola", "");
						request.setAttribute("tx_tariffa_tari", "0,00");
						request.setAttribute("tx_tariffa_cosap", "0,00");
						//request.setAttribute("tx_codice_compenso", "");
						request.setAttribute("tx_dval_ini", "");
						request.setAttribute("tx_dval_fin", "");
					}
				}
			}
			
			break;
			
		case TX_BUTTON_EDIT:
			String messaggio="";
			try {
				messaggio=updateTariffe(request);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
			}
			if (messaggio.substring(0,2).equals("OK")) {
				//session.setAttribute("recordUpdate", "OK");
				setFormMessage("var_form", Messages.UPDT_OK.format(), request);
				request.setAttribute("tx_area_mercantile", (String)request.getAttribute("tx_area_mercantile_h"));
				request.setAttribute("tx_piazzola", (String)request.getAttribute("tx_piazzola_h"));
				request.setAttribute("tx_num_autor", (String)request.getAttribute("tx_num_autor_h"));
				request.setAttribute("tx_tipologia_banco", (String)request.getAttribute("tx_tipologia_banco_h"));			
			} else {
				//session.setAttribute("recordUpdate", "KO");
				String Response = messaggio.substring(2);
				//session.setAttribute("messaggio.recordUpdate",Response);
				setFormMessage("var_form", Response, request);
				request.setAttribute("tx_area_mercantile", (String)request.getAttribute("tx_area_mercantile_h"));
				request.setAttribute("tx_piazzola", (String)request.getAttribute("tx_piazzola_h"));
				request.setAttribute("tx_num_autor", (String)request.getAttribute("tx_num_autor_h"));
				request.setAttribute("tx_tipologia_banco", (String)request.getAttribute("tx_tipologia_banco_h"));
			}
			
			break;
		
		case TX_BUTTON_ADD:
			codop = "add";
			break;
		case TX_BUTTON_AGGIUNGI:
			try {
				dividiSocUtenteEnte(request);
				EsitoRisposte esito = new EsitoRisposte();
				esito = inserisciTariffe(request);
				if(esito.getCodiceMessaggio().equals("OK"))
				{
					session.setAttribute("recordInsert", "OK");
					esito = schedPrenotazioni(request);
					setFormMessage("var_form", Messages.INS_OK.format(), request);
					//System.out.println("Esito Prenotazioni:" + esito.getCodiceMessaggio());
					if (esito.getCodiceMessaggio().equals(("KO"))) {
						session.setAttribute("messaggio.recordInsert", esito.getDescrizioneMessaggio());
						setFormMessage("var_form", esito.getDescrizioneMessaggio(), request);
					}
					request.setAttribute("tx_area_mercantile", "");
					request.setAttribute("tx_piazzola", "");
					request.setAttribute("tx_num_autor","");
					request.setAttribute("tx_tariffa_tari", "0,00");
					request.setAttribute("tx_tariffa_cosap", "0,00");
					request.setAttribute("tx_giorno_settimana", "0");
					request.setAttribute("tx_periodo_giornal", "");
					request.setAttribute("tx_tipologia_banco", "");
					//request.setAttribute("tx_codice_compenso", "");
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
			String chiave0Rec  = (String)request.getAttribute("tx_cod_key_tariffa_h");
			String chiave1Rec  = (String)request.getAttribute("tx_area_mercantile");
			String chiave2Rec  = (String)request.getAttribute("tx_piazzola");
			//String chiave3Rec  = (String)request.getAttribute("tx_num_autor");
			//String chiave4Rec  = (String)request.getAttribute("tx_giorno_settimana");
			//String chiave5Rec  = (String)request.getAttribute("tx_tipologia_banco");
			request.setAttribute("tx_cod_key_tariffa_h", chiave0Rec);
			request.setAttribute("tx_area_mercantile_h",chiave1Rec);
			request.setAttribute("tx_piazzola_h", chiave2Rec);
			//request.setAttribute("tx_num_autor_h", chiave3Rec);
			//request.setAttribute("tx_giorno_settimana_h", chiave4Rec);
			//request.setAttribute("tx_tipologia_banco_h", chiave5Rec);
			request.setAttribute("tx_button_cancel", "cancella");
			
			break;
		case TX_BUTTON_DELETE:
			try 
				{
					EsitoRisposte esito = new EsitoRisposte();
					esito = deleteTariffe(request);
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
			getAreeMercataliDDL(request, session);
			getTipologieBancoDDL(request, session);
			getPeriodoGiornalieroDDL(request, session);
			getPiazzolaDDL(request, session);
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

	private ConfigurazioneTariffe selectConfigurazioneTariffe(HttpServletRequest request) throws DaoException {
		String codiceKeyTarif  = (String)request.getAttribute("tx_cod_key_tariffa_h");
		//String codiceAreaMerc  = (String)request.getAttribute("tx_area_mercantile");
		//String codicePiazzola  = (String)request.getAttribute("tx_piazzola");
		//String numeroAutorizz  = (String)request.getAttribute("tx_num_autor");
		//String tipologiaBanco  = (String)request.getAttribute("tx_tipologia_banco");
		//int Day  = Integer.parseInt((String)request.getAttribute("tx_giorno_settimana"));
		//String periodoGiornal  = (String)request.getAttribute("tx_periodo_giornal");
		//String anno = (String)request.getAttribute("tx_dval_ini_year");
		//String mese = (String)request.getAttribute("tx_dval_ini_month");
		//String giorno =(String)request.getAttribute("tx_dval_ini_day");
		//String datavalidita="";
		//if ((!(anno==null))&&(!(anno.equals("")))) {
		//	datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		//} else {
		//	datavalidita="--";
		//}
		//DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//Calendar dataIniValCalendar  = Calendar.getInstance();
		//if (!(datavalidita.equals("--"))) {
		//	try {
		//		dataIniValCalendar.setTime(df.parse(datavalidita));
		//	} catch (ParseException e1) {
		//	// TODO Auto-generated catch block
		//		e1.printStackTrace();
		//	}
		//}
		
		ConfigurazioneTariffeDAO configurazioneTariffeDAO;
		ConfigurazioneTariffe configurazioneTariffe = new ConfigurazioneTariffe();
		configurazioneTariffe.setCodiceSocieta(codiceSocieta);
		configurazioneTariffe.setCuteCute(codiceUtente);
		configurazioneTariffe.setChiaveEnte(chiaveEnte);
		configurazioneTariffe.setCodiceKeyTariffa(codiceKeyTarif);
		/*configurazioneTariffe.setCodiceKeyAreaMercantile(codiceAreaMerc);		
		configurazioneTariffe.setCodiceKeyPiazzola(codicePiazzola);
		configurazioneTariffe.setCodiceGiornoSettimana(Day);
		configurazioneTariffe.setCodiceKeyAutorizzazione(numeroAutorizz);
		configurazioneTariffe.setCodiceKeyTipologiaBanco(tipologiaBanco);
		configurazioneTariffe.setCodiceKeyPeriodoGiornal(periodoGiornal);*/
		//configurazioneTariffe.setDataInizioValidita(dataIniValCalendar);		
		
		//inizio LP PG21XX04 Leak
		//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
		//configurazioneTariffe = configurazioneTariffeDAO.getPerKey(configurazioneTariffe);
		Connection conn = null;
		try {
			conn = getMercatoDataSource().getConnection();
			configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
			configurazioneTariffe = configurazioneTariffeDAO.getPerKey(configurazioneTariffe);
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
		
		return configurazioneTariffe;
		
	}
	
	private String updateTariffe(HttpServletRequest request) throws DaoException {
		String  societa = (String)request.getAttribute("tx_societa");
		String  utente = (String)request.getAttribute("tx_utente");
		String  ente = (String)request.getAttribute("tx_ente");
		String codiceKeyTarif  = (String)request.getAttribute("tx_cod_key_tariffa_h");
	// Campi aggiornabili
		Double tariffaTari = (Double.parseDouble(((String)request.getAttribute("tx_tariffa_tari")).replace(",", ".")));
		Double tariffaCosap = (Double.parseDouble(((String)request.getAttribute("tx_tariffa_cosap")).replace(",", ".")));
		int Day = Integer.parseInt((String)request.getAttribute("tx_giorno_settimana"));
		//String codiceCompenso = (String)request.getAttribute("tx_codice_compenso");
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
		Calendar dataFinValCalendar  = Calendar.getInstance();
		if (!(datavalidita.equals("--"))) {
			try {
				dataFinValCalendar.setTime(df.parse(datavalidita));
			} catch (ParseException e1) {
			// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		ConfigurazioneTariffeDAO configurazioneTariffeDAO;
		ConfigurazioneTariffe confTariffeOld = new ConfigurazioneTariffe();
		confTariffeOld.setCodiceSocieta(societa);
		confTariffeOld.setCuteCute(utente);
		confTariffeOld.setChiaveEnte(ente);
		confTariffeOld.setCodiceKeyTariffa(codiceKeyTarif);
		
		//inizio LP PG21XX04 Leak
		//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
		//confTariffeOld = configurazioneTariffeDAO.getPerKey(confTariffeOld);
		Connection conn = null;
		try {
			conn = getMercatoDataSource().getConnection();
			configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
			confTariffeOld = configurazioneTariffeDAO.getPerKey(confTariffeOld);
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
		
		MercatoServiceImpl mercService = new MercatoServiceImpl();
		mercService.setMercatoDataSource(getMercatoDataSource());
		mercService.setMercatoDbSchema(getMercatoDbSchema());
		ArrayList <GestioneTariffeResponse> esito = new ArrayList <GestioneTariffeResponse>();
		esito = mercService.gestisciTariffa(confTariffeOld.getCodiceKeyTariffa(), dataIniValCalendar, dataFinValCalendar, tariffaCosap, tariffaTari, Day, confTariffeOld.getCodiceKeyPeriodoGiornal());
		String Resp = "";
		boolean presenzaErrori = false;
		String Messaggi = "";
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
		if (presenzaErrori) {
			Resp = "KO " + Messaggi;
		} else {
			Resp = "OK ";
		}
		return Resp;	
	}

private EsitoRisposte inserisciTariffe(HttpServletRequest request) throws DaoException {
	//Chiave primaria autogenerata da classe
		String chiaveTariffe = "";
		try {
			chiaveTariffe = TokenGenerator.generateUUIDToken();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}			
		String codiceAreaMerc = (String)request.getAttribute("tx_area_mercantile");
		String codicePiazzola = (String)request.getAttribute("tx_piazzola");
		String numeroAutorizz  = (String)request.getAttribute("tx_num_autor");
		String tipologiaBanco  = (String)request.getAttribute("tx_tipologia_banco");
		Double tariffaTari;
		Double tariffaCosap;
		if (((String)request.getAttribute("tx_tariffa_tari")==null)||((String)request.getAttribute("tx_tariffa_tari")=="")) {
			tariffaTari=null;
		} else {
			tariffaTari = Double.parseDouble(((String)request.getAttribute("tx_tariffa_tari")).replace(",","."));
		}
		if (((String)request.getAttribute("tx_tariffa_cosap")==null)||((String)request.getAttribute("tx_tariffa_cosap")=="")) {
			tariffaCosap=null;
		} else {
			tariffaCosap = Double.parseDouble(((String)request.getAttribute("tx_tariffa_cosap")).replace(",", "."));
		}
		int Day = Integer.parseInt((String)request.getAttribute("tx_giorno_settimana"));
		String periodoGiorno = (String)request.getAttribute("tx_periodo_giornal");
		String codiceCompenso = "";
// Gestione Data Inizio Validita
		String anno;
		String mese;
		String giorno;
		if (((String)request.getAttribute("tx_dval_ini_year") == null)||((String)request.getAttribute("tx_dval_ini_year") == "")) {
			Calendar today = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
			anno = Integer.toString(today.get(today.YEAR));
		} else {
			anno = (String)request.getAttribute("tx_dval_ini_year");
		}
		if (((String)request.getAttribute("tx_dval_ini_month") == null)||((String)request.getAttribute("tx_dval_ini_month") == "")) {
			Calendar today = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
			mese = Integer.toString(today.get(today.MONTH)+1);
		} else {
			mese = (String)request.getAttribute("tx_dval_ini_month");
		}
		if (((String)request.getAttribute("tx_dval_ini_day") == null)||((String)request.getAttribute("tx_dval_ini_year") == "")) {
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
		
		ConfigurazioneTariffeDAO configurazioneTariffeDAO;
		ConfigurazioneTariffe configurazioneTariffe = new ConfigurazioneTariffe();
		configurazioneTariffe.setCodiceKeyTariffa(chiaveTariffe);
		configurazioneTariffe.setCodiceSocieta(codiceSocieta);
		configurazioneTariffe.setCuteCute(codiceUtente);
		configurazioneTariffe.setChiaveEnte(chiaveEnte);
		configurazioneTariffe.setCodiceKeyAreaMercantile(codiceAreaMerc);
		configurazioneTariffe.setCodiceKeyPiazzola(codicePiazzola);
		configurazioneTariffe.setCodiceKeyAutorizzazione(numeroAutorizz);
		configurazioneTariffe.setCodiceKeyTipologiaBanco(tipologiaBanco);
		configurazioneTariffe.setTariffaTari(tariffaTari);
		configurazioneTariffe.setTariffaCosap(tariffaCosap);
		configurazioneTariffe.setCodiceKeyCompenso(codiceCompenso);
		configurazioneTariffe.setCodiceGiornoSettimana(Day);
		configurazioneTariffe.setCodiceKeyPeriodoGiornal(periodoGiorno);
		configurazioneTariffe.setDataInizioValidita(dataIniValCalendar);
		configurazioneTariffe.setDataFineValidita(dataFinValCalendar);
		UserBean user = (UserBean)request.getSession().getAttribute(SignOnKeys.USER_BEAN);
		
		//inizio LP PG21XX04 Leak
		//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
		//return configurazioneTariffeDAO.insert(configurazioneTariffe);
		Connection conn = null;
		try {
			conn = getMercatoDataSource().getConnection();
			configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
			EsitoRisposte esito = configurazioneTariffeDAO.insert(configurazioneTariffe); 
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

private EsitoRisposte schedPrenotazioni(HttpServletRequest request) throws DaoException {

	ConfigurazionePrenotazioniDAO prenotazioniDAO;
	//inizio LP PG21XX04 Leak
	//prenotazioniDAO = MercatoDAOFactory.getPrenotazioni(getMercatoDataSource(), getMercatoDbSchema());
	//fine LP PG21XX04 Leak
	EsitoRisposte esitoPrn = new EsitoRisposte();
	//String codicePiazzola = "1e36150d-2d78-40fd-8c70-69349f0fe75a";
	String codiceKeyPiazzola = (String)request.getAttribute("tx_piazzola");
	String codiceKeyAreaMerc = (String)request.getAttribute("tx_area_mercantile");
	//String codiceSocieta = codiceSocieta;
	//String codiceUtente = codiceUtente;
	//String chiaveEnte = chiaveEnte;
	int tmpday = Integer.parseInt((String)request.getAttribute("tx_giorno_settimana"));
	//Gestione Data Inizio Validita
	String anno;
	String mese;
	String giorno;
	if (((String)request.getAttribute("tx_dval_ini_year") == null)||((String)request.getAttribute("tx_dval_ini_year") == "")) {
		Calendar today = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
		anno = Integer.toString(today.get(today.YEAR));
	} else {
		anno = (String)request.getAttribute("tx_dval_ini_year");
	}
	if (((String)request.getAttribute("tx_dval_ini_month") == null)||((String)request.getAttribute("tx_dval_ini_month") == "")) {
		Calendar today = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"),Locale.ITALY);
		mese = Integer.toString(today.get(today.MONTH)+1);
	} else {
		mese = (String)request.getAttribute("tx_dval_ini_month");
	}
	if (((String)request.getAttribute("tx_dval_ini_day") == null)||((String)request.getAttribute("tx_dval_ini_year") == "")) {
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
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
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
	
	//inizio LP PG21XX04 Leak
	//esitoPrn = prenotazioniDAO.schedulaPrenotazioniDaKeyPiazzola(codiceKeyPiazzola, codiceKeyAreaMerc, codiceSocieta, codiceUtente, chiaveEnte, dataIniValCalendar, dataFinValCalendar, tmpday);
	//return esitoPrn;
	Connection conn = null;
	try {
		conn = getMercatoDataSource().getConnection();
		prenotazioniDAO = MercatoDAOFactory.getPrenotazioni(conn, getMercatoDbSchema());
		esitoPrn = prenotazioniDAO.schedulaPrenotazioniDaKeyPiazzola(codiceKeyPiazzola, codiceKeyAreaMerc, codiceSocieta, codiceUtente, chiaveEnte, dataIniValCalendar, dataFinValCalendar, tmpday);
		return esitoPrn;
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

private EsitoRisposte deleteTariffe(HttpServletRequest request) throws DaoException {
	String  societa = (String)request.getAttribute("tx_societa");
	String  utente = (String)request.getAttribute("tx_utente");
	String  ente = (String)request.getAttribute("tx_ente");
	String codiceKeyTarif = (String)request.getAttribute("tx_cod_key_tariffa_h");
	//String codiceAreaMerc = (String)request.getAttribute("tx_area_mercantile_h");
	//String codicePiazzola = (String)request.getAttribute("tx_piazzola_h");
	//String numeroAutorizz  = (String)request.getAttribute("tx_num_autor_h");
	//String tipologiaBanco  = (String)request.getAttribute("tx_tipologia_banco_h");
	//int Day = Integer.parseInt((String)request.getAttribute("tx_giorno_settimana_h"));

	ConfigurazioneTariffeDAO configurazioneTariffeDAO;
	
	ConfigurazioneTariffe configurazioneTariffe = new ConfigurazioneTariffe();
	configurazioneTariffe.setCodiceSocieta(societa);
	configurazioneTariffe.setCuteCute(utente);
	configurazioneTariffe.setChiaveEnte(ente);
	configurazioneTariffe.setCodiceKeyTariffa(codiceKeyTarif);
	//configurazioneTariffe.setCodiceKeyAreaMercantile(codiceAreaMerc);
	//configurazioneTariffe.setCodiceKeyPiazzola(codicePiazzola);
	//configurazioneTariffe.setCodiceKeyAutorizzazione(numeroAutorizz);
	//configurazioneTariffe.setCodiceKeyTipologiaBanco(tipologiaBanco);
	//configurazioneTariffe.setCodiceGiornoSettimana(Day);	
	
	//inizio LP PG21XX04 Leak
	//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
	//return configurazioneTariffeDAO.delete(configurazioneTariffe);
	Connection conn = null;
	try {
		conn = getMercatoDataSource().getConnection();
		configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
		EsitoRisposte esito = configurazioneTariffeDAO.delete(configurazioneTariffe); 
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


@SuppressWarnings("unchecked")
private void getAreeMercataliDDL(HttpServletRequest request, HttpSession session) {
//	dividiSocUtenteEnte(request);		
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
	request.setAttribute("areemercDDLList", areemercDDLList); 
}

@SuppressWarnings("unchecked")
private void getTipologieBancoDDL(HttpServletRequest request, HttpSession session) {		
	List<DdlOption> tipbancoDDLList  = new ArrayList<DdlOption>();
	ConfigurazioneTipologiaBancoDAO configurazioneTipologiaBancoDAO;
	ConfigurazioneTipologiaBanco configurazioneTipologiaBanco = new ConfigurazioneTipologiaBanco();
	// Se non ho selezionato un elemento SocUtenteEnte non devo esporre nessun elemento!
	if (((String)request.getAttribute("ddlSocietaUtenteEnte")==null)||((String)request.getAttribute("ddlSocietaUtenteEnte")=="")) {
		//
	} else {
		dividiSocUtenteEnte(request);
	}
	if (((codiceSocieta==null)||(codiceSocieta.equals(""))) &&
		((codiceUtente==null)||(codiceUtente.equals(""))) &&
		((chiaveEnte==null)||(chiaveEnte.equals("")))) {
		if (((String)request.getAttribute("tx_societa")==null) || ((String)request.getAttribute("tx_societa")=="")) {
			configurazioneTipologiaBanco.setCodiceSocieta("xx");
			configurazioneTipologiaBanco.setCuteCute("xx");
			configurazioneTipologiaBanco.setChiaveEnte("xx");
		} else {
			configurazioneTipologiaBanco.setCodiceSocieta((String)request.getAttribute("tx_societa"));
			configurazioneTipologiaBanco.setCuteCute((String)request.getAttribute("tx_utente"));
			configurazioneTipologiaBanco.setChiaveEnte((String)request.getAttribute("tx_ente"));
		}
	} else {
		configurazioneTipologiaBanco.setCodiceSocieta(codiceSocieta);
		configurazioneTipologiaBanco.setCuteCute(codiceUtente);
		configurazioneTipologiaBanco.setChiaveEnte(chiaveEnte);
	}
	ArrayList<ConfigurazioneTipologiaBanco> configurazioneTipologiaBancoList = null;
	//inizio LP PG21XX04 Leak
	Connection conn = null;
	//fine LP PG21XX04 Leak
	try {
		//inizio LP PG21XX04 Leak
		//configurazioneTipologiaBancoDAO = MercatoDAOFactory.getConfigurazioneTipologiaBanco(getMercatoDataSource(), getMercatoDbSchema());
		conn = getMercatoDataSource().getConnection();
		configurazioneTipologiaBancoDAO = MercatoDAOFactory.getConfigurazioneTipologiaBanco(conn, getMercatoDbSchema());
		//fine LP PG21XX04 Leak
		configurazioneTipologiaBancoList = configurazioneTipologiaBancoDAO.listTipologiaBanco(configurazioneTipologiaBanco);
		for (Iterator iterator = configurazioneTipologiaBancoList.iterator(); iterator.hasNext();) {
			ConfigurazioneTipologiaBanco item = (ConfigurazioneTipologiaBanco) iterator.next();
			DdlOption optionTipBanco = new DdlOption();
			optionTipBanco.setSValue(item.getCodiceKeyTipologiaBanco());
			optionTipBanco.setSText(item.getDescrizioneTipologiaBanco());
			tipbancoDDLList.add(optionTipBanco);
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
	request.setAttribute("tipbancoDDLList", tipbancoDDLList); 
}

@SuppressWarnings("unchecked")
private void getPeriodoGiornalieroDDL(HttpServletRequest request, HttpSession session) {		
	List<DdlOption> pergiornDDLList  = new ArrayList<DdlOption>();
	ConfigurazionePeriodoGiornalieroDAO configurazionePeriodoGiornalieroDAO;
	ConfigurazionePeriodoGiornaliero configurazionePeriodoGiornaliero = new ConfigurazionePeriodoGiornaliero();
	// Se non ho selezionato un elemento SocUtenteEnte non devo esporre nessun elemento!
	if (((String)request.getAttribute("ddlSocietaUtenteEnte")==null)||((String)request.getAttribute("ddlSocietaUtenteEnte")=="")) {
		//
	} else {
		dividiSocUtenteEnte(request);
	}
	if (((codiceSocieta==null)||(codiceSocieta.equals(""))) &&
		((codiceUtente==null)||(codiceUtente.equals(""))) &&
		((chiaveEnte==null)||(chiaveEnte.equals("")))) {
		if (((String)request.getAttribute("tx_societa")==null) || ((String)request.getAttribute("tx_societa")=="")) {
			configurazionePeriodoGiornaliero.setCodiceSocieta("xx");
			configurazionePeriodoGiornaliero.setCuteCute("xx");
			configurazionePeriodoGiornaliero.setChiaveEnte("xx");
		} else {
			configurazionePeriodoGiornaliero.setCodiceSocieta((String)request.getAttribute("tx_societa"));
			configurazionePeriodoGiornaliero.setCuteCute((String)request.getAttribute("tx_utente"));
			configurazionePeriodoGiornaliero.setChiaveEnte((String)request.getAttribute("tx_ente"));
		}
	} else {
		configurazionePeriodoGiornaliero.setCodiceSocieta(codiceSocieta);
		configurazionePeriodoGiornaliero.setCuteCute(codiceUtente);
		configurazionePeriodoGiornaliero.setChiaveEnte(chiaveEnte);
	}
	ArrayList<ConfigurazionePeriodoGiornaliero> configurazionePeriodoGiornalieroList = null;
	//inizio LP PG21XX04 Leak
	Connection conn = null;
	//fine LP PG21XX04 Leak
	try {
		//inizio LP PG21XX04 Leak
		//configurazionePeriodoGiornalieroDAO = MercatoDAOFactory.getConfigurazionePeriodoGiornaliero(getMercatoDataSource(), getMercatoDbSchema());
		conn = getMercatoDataSource().getConnection();
		configurazionePeriodoGiornalieroDAO = MercatoDAOFactory.getConfigurazionePeriodoGiornaliero(conn, getMercatoDbSchema());
		//fine LP PG21XX04 Leak
		configurazionePeriodoGiornalieroList = configurazionePeriodoGiornalieroDAO.listPeriodoGiornaliero(configurazionePeriodoGiornaliero);
		for (Iterator iterator = configurazionePeriodoGiornalieroList.iterator(); iterator.hasNext();) {
			ConfigurazionePeriodoGiornaliero item = (ConfigurazionePeriodoGiornaliero) iterator.next();
			DdlOption optionPerGiorn = new DdlOption();
			optionPerGiorn.setSValue(item.getCodiceKeyPeriodoGiornaliero());
			optionPerGiorn.setSText(item.getDescrizionePeriodoGiornaliero());
			pergiornDDLList.add(optionPerGiorn);
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
	request.setAttribute("pergiornDDLList", pergiornDDLList); 
}

@SuppressWarnings("unchecked")
private void getPiazzolaDDL(HttpServletRequest request, HttpSession session) {		
	List<DdlOption> piazzolaDDLList  = new ArrayList<DdlOption>();
	ConfigurazionePiazzolaDAO configurazionePiazzolaDAO;
	ConfigurazionePiazzola configurazionePiazzola = new ConfigurazionePiazzola();
	// Se non ho selezionato un elemento SocUtenteEnte non devo esporre nessun elemento!
	if (((String)request.getAttribute("ddlSocietaUtenteEnte")==null)||((String)request.getAttribute("ddlSocietaUtenteEnte")=="")) {
		//
	} else {
		dividiSocUtenteEnte(request);
	}	
	if (((codiceSocieta==null)||(codiceSocieta.equals(""))) &&
		((codiceUtente==null)||(codiceUtente.equals(""))) &&
		((chiaveEnte==null)||(chiaveEnte.equals("")))) {
		if (((String)request.getAttribute("tx_societa")==null) || ((String)request.getAttribute("tx_societa")=="")) {
			configurazionePiazzola.setCodiceSocieta("xx");
			configurazionePiazzola.setCuteCute("xx");
			configurazionePiazzola.setChiaveEnte("xx");
		} else {
			configurazionePiazzola.setCodiceSocieta((String)request.getAttribute("tx_societa"));
			configurazionePiazzola.setCuteCute((String)request.getAttribute("tx_utente"));
			configurazionePiazzola.setChiaveEnte((String)request.getAttribute("tx_ente"));
		}
	} else {
		configurazionePiazzola.setCodiceSocieta(codiceSocieta);
		configurazionePiazzola.setCuteCute(codiceUtente);
		configurazionePiazzola.setChiaveEnte(chiaveEnte);
	}
	configurazionePiazzola.setCodiceKeyAreaMercantile((String)request.getAttribute("tx_area_mercantile"));
	String datavalidita="0001-01-01 00:00:01.0";
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	Calendar dataIniValCalendar  = Calendar.getInstance();
	try {
		dataIniValCalendar.setTime(df.parse(datavalidita));
	} catch (ParseException e) {
		e.printStackTrace();
	}
	configurazionePiazzola.setDataInizioValidita(dataIniValCalendar);
	datavalidita="3999-12-31 23:59:59.0";
	Calendar dataIniValACalendar = Calendar.getInstance();
	try {
		dataIniValACalendar.setTime(df.parse(datavalidita));
	} catch (ParseException e) {
		e.printStackTrace();
	}
	configurazionePiazzola.setDataAInizioValidita(dataIniValACalendar);
	ArrayList<ConfigurazionePiazzola> configurazionePiazzolaList = null;
	//inizio LP PG21XX04 Leak
	Connection conn = null;
	//fine LP PG21XX04 Leak
	try {
		//inizio LP PG21XX04 Leak
		//configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(getMercatoDataSource(), getMercatoDbSchema());
		conn = getMercatoDataSource().getConnection();
		configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(conn, getMercatoDbSchema());
		//fine LP PG21XX04 Leak
		configurazionePiazzolaList = configurazionePiazzolaDAO.listPiazzola(configurazionePiazzola);
		for (Iterator iterator = configurazionePiazzolaList.iterator(); iterator.hasNext();) {
			ConfigurazionePiazzola item = (ConfigurazionePiazzola) iterator.next();
			DdlOption optionPiazzola = new DdlOption();
			optionPiazzola.setSValue(item.getCodiceKeyPiazzola());
			optionPiazzola.setSText(item.getDescrizionePiazzola());
			piazzolaDDLList.add(optionPiazzola);
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
	request.setAttribute("piazzolaDDLList", piazzolaDDLList); 
}


//Termine della Classe
}

