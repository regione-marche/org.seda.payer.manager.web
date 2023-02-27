package org.seda.payer.manager.modello3config.actions;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.bean.ConfigurazioneBlackBox;
import com.seda.payer.core.bean.ConfigurazioneModello3;
import com.seda.payer.core.dao.BlackBoxDAOFactory;
import com.seda.payer.core.dao.ConfigurazioneBlackBoxDao;
import com.seda.payer.core.dao.ConfigurazioneModello3DAOFactory;
import com.seda.payer.core.dao.ConfigurazioneModello3Dao;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.ConfigurazioneSolleciti;
import com.seda.payer.core.wallet.bean.EsitoRisposte;
import com.seda.payer.core.wallet.dao.ConfigurazioneSollecitiDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;
@SuppressWarnings("serial")
public class Modello3EditAction  extends Modello3BaseManagerAction {


	/**
	 * 
	 */
	private static String codop = "edit";
	//private static String ritornaViewstate = "ConfigurazioneInvioSolleciti_edit";
	private static String ritornaViewstate = "configurazioneconfigurazionemodello3";
	private HashMap<String,Object> parametri = null;
	
	private String codiceSocieta = "";
	private String codiceUtente = "";
	private String chiaveEnte= "";
	private String codiceIdentificativoDominio="";
	private String auxitDigit="";
	private String codiceSegregazione="";
	private String carattereDiServizio=""; //SVILUPPO_002_SB
	
	private String operatore = "";
	private String messaggioErrore = "";
	private Integer appRet;
//	private String  chiaveEnteda5= "";
	
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
		if (request.getAttribute("fired_button_hidden")!= null){
			if (((String)request.getAttribute("fired_button_hidden")).equals("tx_button_tipo_sms")){
				//firedButton =  firedButton.TX_BUTTON_ADD;
				firedButton =  firedButton.TX_BUTTON_EDIT_END;
			}			
		}
		
		
		switch(firedButton)
		{
		case TX_BUTTON_EDIT_END:  // Se cambio il combobox del tipo SMS
			//appRet = ControlliCampi(request, session);
			break;
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
			/*
			 * Carico i valori dal WS
			 */
			codop = "edit";
			dividiSocUtenteEnte(request);
			codiceIdentificativoDominio= (String)request.getAttribute("tx_idDominio");
			auxitDigit = (String)request.getAttribute("tx_auxitDigit");
			codiceSegregazione = (String)request.getAttribute("tx_codiceSegregazione");
			carattereDiServizio = (String)request.getAttribute("tx_carattereDiServizio"); //SVILUPPO_002_SB
			
			
//			chiaveEnteda5 = (String)request.getAttribute("tx_enteda5");
			
			ConfigurazioneModello3 configurazioneModello3 = null;
			try {
				configurazioneModello3 = selectConfigurazioneModello3(request);
			} catch (DaoException e) {
				e.printStackTrace();
				setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
			}

			
			
//			request.setAttribute("tx_enteda5_h",chiaveEnteda5);
			request.setAttribute("tx_societa_h", 	codiceSocieta);
			request.setAttribute("tx_utente_h", 	codiceUtente);
			request.setAttribute("tx_ente_h", 		chiaveEnte);
			request.setAttribute("tx_idDominio_h",	codiceIdentificativoDominio);
			request.setAttribute("tx_idDominio",	configurazioneModello3.getCodiceIdentificativoDominio());
			request.setAttribute("tx_auxDigit", 	configurazioneModello3.getAuxDigit());
			request.setAttribute("tx_codiceSegregazione", configurazioneModello3.getCodiceSegregazione());
			request.setAttribute("tx_carattereDiServizio", configurazioneModello3.getCarattereDiServizio()); //SVILUPPO_002_SB

			request.setAttribute("tx_tipoServizio", configurazioneModello3.getTipologiaServizio());
			request.setAttribute("tx_urlIntegrazione", 	configurazioneModello3.getUrlIntegrazione());
			request.setAttribute("tx_codiceOperatore", 	configurazioneModello3.getCodiceOperatore());
			request.setAttribute("tx_dataAggiornamento",configurazioneModello3.getDataAggiornamento());
			

			break;
			
		case TX_BUTTON_EDIT:
			
			//appRet = ControlliCampi(request, session);
			// Aggiungere controlli sui dati inseriti
			appRet = 1;
			if (appRet > 0 ){
				
				try {
					updateConfigurazioneModello3(request);
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
				}
				session.setAttribute("recordUpdate", "OK");
			}
			
			break;
		
		case TX_BUTTON_ADD:
			codop = "add";
			//request.setAttribute("tx_button_cerca", "Search");	
			break;
		case TX_BUTTON_AGGIUNGI:
			try {
				//appRet = ControlliCampi(request, session);
				appRet=1;
				// Aggiungere controlli sui dati inseriti
				if (appRet > 0 ){
					
					dividiSocUtenteEnte(request);
					
					EsitoRisposte esito = new EsitoRisposte();
					esito = inserisciConfigurazioneModello3(request);
					if(esito.getCodiceMessaggio().equals("OK"))
					{
						session.setAttribute("recordInsert", "OK");
						//setFormMessage("form_selezione", Messages.INS_OK.format(), request);
						
					}
					else
					{
						session.setAttribute("recordInsert", "KO");
						session.setAttribute("messaggio.recordInsert", esito.getDescrizioneMessaggio());
						request.setAttribute("tx_button_edit", null);
						request.setAttribute("tx_button_edit_end", "ok");
					}
				}
				else
				{
					session.setAttribute("recordInsert", "KO");		
					session.setAttribute("messaggio.recordInsert", messaggioErrore);
					request.setAttribute("tx_button_edit", null);
					request.setAttribute("tx_button_edit_end", "ok");
				}
					
					
			}
			catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
				request.setAttribute("tx_button_edit", null);
				request.setAttribute("tx_button_edit_end", "ok");
			}
			
			break;
			
			
		case TX_BUTTON_INDIETRO:
			request.setAttribute("tx_button_cerca", "Search");	
			break;
		
		case TX_BUTTON_CANCEL:
			dividiSocUtenteEnte(request);

			
			//request.setAttribute("tx_dval_sms_h",chiaveDataValidita);
			request.setAttribute("tx_button_cancel", "cancella");
			
			break;
		case TX_BUTTON_DELETE:
			try 
				{
					EsitoRisposte esito = new EsitoRisposte();
					esito = deleteConfigurazioneModello3(request);
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
		
		
		if(request.getAttribute("ddlSocietaUtenteEnte")!=null)
		{
			String ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
			if (!ddlSocietaUtenteEnte.equals(""))
			{
				String[] codici = ddlSocietaUtenteEnte.split("\\|");
				codiceSocieta = codici[0];
				codiceUtente = codici[1];
				chiaveEnte = codici[2];
				//chiaveEnteda5 = codici[3].substring(1, 6);
				request.setAttribute("tx_societa", codiceSocieta);
				request.setAttribute("tx_utente", codiceUtente);
				request.setAttribute("tx_ente", chiaveEnte);
				//request.setAttribute("tx_enteda5", chiaveEnteda5);
				
			}
		}
	}

	private ConfigurazioneModello3 selectConfigurazioneModello3(HttpServletRequest request) throws DaoException 
	{
		ConfigurazioneModello3Dao configurazioneModello3DAO;
		ConfigurazioneModello3 configurazioneModello3 = new ConfigurazioneModello3();
		configurazioneModello3.setCodiceSocieta(codiceSocieta);
		configurazioneModello3.setCodiceUtente(codiceUtente);
		configurazioneModello3.setChiaveEnte(chiaveEnte);
		configurazioneModello3.setCodiceIdentificativoDominio(codiceIdentificativoDominio);
		configurazioneModello3.setAuxDigit(auxitDigit);
		configurazioneModello3.setCodiceSegregazione(codiceSegregazione);
		configurazioneModello3.setCarattereDiServizio(carattereDiServizio);
		
		//inizio LP PG21XX04 Leak
		//configurazioneModello3DAO = ConfigurazioneModello3DAOFactory.getConfigurazioneModello3(getConfigurazioneModello3DataSource(), getConfigurazioneModello3DbSchema());
		//configurazioneModello3 = configurazioneModello3DAO.select(configurazioneModello3);
		//
		//return configurazioneModello3;
		Connection conn = null;
		try {
			conn = getConfigurazioneModello3DataSource().getConnection();
			configurazioneModello3DAO = ConfigurazioneModello3DAOFactory.getConfigurazioneModello3(conn, getConfigurazioneModello3DbSchema());
			configurazioneModello3 = configurazioneModello3DAO.select(configurazioneModello3);
			
			return configurazioneModello3;
		} catch (Exception e) {
			throw new DaoException(e);
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
	}
	
	private Integer updateConfigurazioneModello3(HttpServletRequest request) throws DaoException {
		
		String codiceSocieta		= (request.getAttribute("tx_societa_h")		== null? "" : (String)request.getAttribute("tx_societa_h"));
		String codiceUtente			= (request.getAttribute("tx_utente_h")		== null? "" : (String)request.getAttribute("tx_utente_h"));
		String chiaveEnte			= (request.getAttribute("tx_ente_h")				== null? "" : (String)request.getAttribute("tx_ente_h"));
		String codiceIdentificativoDominio = (request.getAttribute("tx_idDominio")	== null? "" : (String)request.getAttribute("tx_idDominio"));
		String auxDigit				= (request.getAttribute("tx_auxDigit")		== null? "" : (String)request.getAttribute("tx_auxDigit"));
		String codiceSegregazione	= (request.getAttribute("tx_codiceSegregazione")== null? "" : (String)request.getAttribute("tx_codiceSegregazione"));
		String carattereDiServizio	= (request.getAttribute("tx_carattereDiServizio")== null? "" : (String)request.getAttribute("tx_carattereDiServizio")); //SVILUPPO_002_SB
		String tipologiaServizio	= (request.getAttribute("tx_tipoServizio") == null? "" : (String)request.getAttribute("tx_tipoServizio"));
		String urlIntegrazione		= (request.getAttribute("tx_urlIntegrazione")	== null? "" : (String)request.getAttribute("tx_urlIntegrazione"));
		String codiceOperatore		= operatore; //(request.getAttribute("tx_codiceOperatore")	== null? "" : (String)request.getAttribute("tx_codiceOperatore"));
		
		ConfigurazioneModello3Dao configurazioneModello3DAO;
		ConfigurazioneModello3 configurazioneModello3 = new ConfigurazioneModello3();
		configurazioneModello3.setCodiceSocieta(codiceSocieta);
		configurazioneModello3.setCodiceUtente(codiceUtente);
		configurazioneModello3.setChiaveEnte(chiaveEnte);
		configurazioneModello3.setCodiceIdentificativoDominio(codiceIdentificativoDominio);
		configurazioneModello3.setAuxDigit(auxDigit);
		configurazioneModello3.setCodiceSegregazione(codiceSegregazione);
		configurazioneModello3.setCarattereDiServizio(carattereDiServizio); //SVILUPPO_002_SB
		configurazioneModello3.setTipologiaServizio(tipologiaServizio);
		configurazioneModello3.setUrlIntegrazione(urlIntegrazione);
		configurazioneModello3.setCodiceOperatore(codiceOperatore);
		
		//inizio LP PG21XX04 Leak
		//configurazioneModello3DAO = ConfigurazioneModello3DAOFactory.getConfigurazioneModello3(getConfigurazioneModello3DataSource(), getConfigurazioneModello3DbSchema());
		//int aggiornati = configurazioneModello3DAO.update(configurazioneModello3);
		//
		//return aggiornati;
		Connection conn = null;
		try {
			conn = getConfigurazioneModello3DataSource().getConnection();
			configurazioneModello3DAO = ConfigurazioneModello3DAOFactory.getConfigurazioneModello3(conn, getConfigurazioneModello3DbSchema());
			int aggiornati = configurazioneModello3DAO.update(configurazioneModello3);
	
			return aggiornati;		
		} catch (Exception e) {
			throw new DaoException(e);
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
	}

	private EsitoRisposte inserisciConfigurazioneModello3(HttpServletRequest request) throws DaoException 
	{
		
		//String codiceSocieta		= (request.getAttribute("tx_codSocieta")		== null? "" : (String)request.getAttribute("tx_codSocieta"));
		//String codiceUtente			= (request.getAttribute("tx_cuteCute")			== null? "" : (String)request.getAttribute("tx_cuteCute"));
		//String chiaveEnte			= (request.getAttribute("tx_ente")				== null? "" : (String)request.getAttribute("tx_ente"));
		String codiceIdentificativoDominio = (request.getAttribute("tx_idDominio")	== null? "" : (String)request.getAttribute("tx_idDominio"));
		String auxDigit				= (request.getAttribute("tx_auxDigit")			== null? "" : (String)request.getAttribute("tx_auxDigit"));
		String codiceSegregazione	= (request.getAttribute("tx_codiceSegregazione")== null? "" : (String)request.getAttribute("tx_codiceSegregazione"));
		String carattereDiServizio	= (request.getAttribute("tx_carattereDiServizio")== null? "" : (String)request.getAttribute("tx_carattereDiServizio")); //SVILUPPO_002_SB
		String tipologiaServizio	= (request.getAttribute("tx_tipoServizio") == null? "" : (String)request.getAttribute("tx_tipoServizio"));
		String urlIntegrazione		= (request.getAttribute("tx_urlIntegrazione")	== null? "" : (String)request.getAttribute("tx_urlIntegrazione"));
		String codiceOperatore		= operatore; //(request.getAttribute("tx_codiceOperatore")	== null? "" : (String)request.getAttribute("tx_codiceOperatore"));
		
		ConfigurazioneModello3Dao configurazioneModello3DAO;
		ConfigurazioneModello3 configurazioneModello3 = new ConfigurazioneModello3();
		configurazioneModello3.setCodiceSocieta(codiceSocieta);
		configurazioneModello3.setCodiceUtente(codiceUtente);
		configurazioneModello3.setChiaveEnte(chiaveEnte);
		configurazioneModello3.setCodiceIdentificativoDominio(codiceIdentificativoDominio);
		configurazioneModello3.setAuxDigit(auxDigit);
		configurazioneModello3.setCodiceSegregazione(codiceSegregazione);
		configurazioneModello3.setCarattereDiServizio(carattereDiServizio); //SVILUPPO_002_SB
		configurazioneModello3.setTipologiaServizio(tipologiaServizio);
		configurazioneModello3.setUrlIntegrazione(urlIntegrazione);
		configurazioneModello3.setCodiceOperatore(codiceOperatore);
		
		//inizio LP PG21XX04 Leak
		//configurazioneModello3DAO = ConfigurazioneModello3DAOFactory.getConfigurazioneModello3(getConfigurazioneModello3DataSource(), getConfigurazioneModello3DbSchema());
		////EsitoRisposte esitorisposte = new EsitoRisposte();
		//return configurazioneModello3DAO.insert(configurazioneModello3);
		Connection conn = null;
		try {
			conn = getConfigurazioneModello3DataSource().getConnection();
			configurazioneModello3DAO = ConfigurazioneModello3DAOFactory.getConfigurazioneModello3(conn, getConfigurazioneModello3DbSchema());
			return configurazioneModello3DAO.insert(configurazioneModello3);
		} catch (Exception e) {
			throw new DaoException(e);
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
	}

	private EsitoRisposte deleteConfigurazioneModello3(HttpServletRequest request) throws DaoException 
	{
		String  ente = (String)request.getAttribute("tx_ente");
		
		ConfigurazioneModello3Dao configurazioneModello3DAO;
		ConfigurazioneModello3 configurazioneModello3 = new ConfigurazioneModello3();
		configurazioneModello3.setCodiceSocieta((String)request.getAttribute("tx_societa"));
		configurazioneModello3.setCodiceUtente((String)request.getAttribute("tx_utente"));
		configurazioneModello3.setChiaveEnte((String)request.getAttribute("tx_ente"));
		configurazioneModello3.setCodiceIdentificativoDominio((String) request.getAttribute("tx_idDominio"));
		configurazioneModello3.setAuxDigit((String) request.getAttribute("tx_auxDigit"));
		configurazioneModello3.setCodiceSegregazione((String) request.getAttribute("tx_codiceSegregazione"));
		configurazioneModello3.setCarattereDiServizio((String) request.getAttribute("tx_carattereDiServizio")); //SVILUPPO_002_SB
		//inizio LP PG21XX04 Leak
		//configurazioneModello3DAO = ConfigurazioneModello3DAOFactory.getConfigurazioneModello3(getConfigurazioneModello3DataSource(), getConfigurazioneModello3DbSchema());
		//return configurazioneModello3DAO.delete(configurazioneModello3);
		Connection conn = null;
		try {
			conn = getConfigurazioneModello3DataSource().getConnection();
			configurazioneModello3DAO = ConfigurazioneModello3DAOFactory.getConfigurazioneModello3(conn, getConfigurazioneModello3DbSchema());
			return configurazioneModello3DAO.delete(configurazioneModello3);
		} catch (Exception e) {
			throw new DaoException(e);
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
	}

//	private Integer ControlliCampi(HttpServletRequest request, HttpSession session)  
//	{
//		Integer ret=1;
//		// Aggiungere controlli sui dati inseriti
//		//chiaveTipologiaSMS = (request.getAttribute("tx_tipo_sms") == null ? "" : (String)request.getAttribute("tx_tipo_sms"));
//		chiaveEnteda5 = (request.getAttribute("tx_enteda5h") == null ? "" : (String)request.getAttribute("tx_enteda5h"));
//		chiaveEnteda5 = (request.getAttribute("tx_enteda5") == null ? "" : (String)request.getAttribute("tx_enteda5"));
//		
////		<s:ddloption value="C" text="SMS di Cortesia"/>
////		<s:ddloption value="S" text="SMS di Sollecito"/>
////		<s:ddloption value="P" text="Sollecito Cartaceo"/>
		
		
//			
//		
//			
//		
//		if( ret == -1)
//		{
//			request.setAttribute("tx_button_edit", null);
//			request.setAttribute("tx_button_aggiungi", null);
//			request.setAttribute("tx_button_edit_end", "ok");
//			// GC_2013_06_19 PORCO !!!!!!!!!!!!!
//			
//			//chiaveDataValidita = (String)request.getAttribute("tx_dval_sms");
//			
//			
//			String anno = (String)request.getAttribute("tx_dval_sms_year");
//			String mese =(String)request.getAttribute("tx_dval_sms_month");
//			String giorno =(String)request.getAttribute("tx_dval_sms_day");
//			chiaveDataValidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
//			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//			Calendar calendar_dval_sms  = Calendar.getInstance();			
//			try {
//				calendar_dval_sms.setTime(df.parse(chiaveDataValidita));
//			} catch (ParseException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			//chiaveTipologiaSMS = (request.getAttribute("tx_tipo_sms_h") == null ? "" : (String)request.getAttribute("tx_tipo_sms_h"));
//			chiaveTipologiaSMS = (request.getAttribute("tx_tipo_sms") == null ? "" : (String)request.getAttribute("tx_tipo_sms"));
//			request.setAttribute("tx_dval_sms",calendar_dval_sms);
//			request.setAttribute("tx_dval_sms_h",chiaveDataValidita);
//			request.setAttribute("tx_tipo_sms_h",chiaveTipologiaSMS);
//			request.setAttribute("tx_tipo_sms",chiaveTipologiaSMS);		
//		}
//		return ret;
//	}
//
}
