package org.seda.payer.manager.blackboxconfig.actions;

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
import com.seda.payer.core.dao.BlackBoxDAOFactory;
import com.seda.payer.core.dao.ConfigurazioneBlackBoxDao;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.ConfigurazioneSolleciti;
import com.seda.payer.core.wallet.bean.EsitoRisposte;
import com.seda.payer.core.wallet.dao.ConfigurazioneSollecitiDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;
@SuppressWarnings("serial")
public class BlackBoxEditAction  extends BlackBoxBaseManagerAction {


	/**
	 * 
	 */
	private static String codop = "edit";
	//private static String ritornaViewstate = "ConfigurazioneInvioSolleciti_edit";
	private static String ritornaViewstate = "configurazioneblackbox";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;
	private String codiceSocieta = "";
	private String codiceUtente = "";
	private String  chiaveEnte= "";
	private String codiceIdentificativoDominio="";
	private String chiaveDataValidita = "";
	private String chiaveTipologiaSMS = "";
	private String operatore = "";
	private String messaggioErrore = "";
	private Integer appRet;
	private String  chiaveEnteda5= "";
	
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
//			dividiSocUtenteEnte(request);
			codiceIdentificativoDominio= (String)request.getAttribute("tx_idDominio");
			chiaveEnteda5 = (String)request.getAttribute("tx_enteda5");
			
				ConfigurazioneBlackBox configurazioneBlackBox = null;
				try {
					configurazioneBlackBox = selectConfigurazioneBlackBox(request);
				} catch (DaoException e) {
					e.printStackTrace();
					setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
				}

				
				
				request.setAttribute("tx_enteda5_h",chiaveEnteda5);
				request.setAttribute("tx_idDominio_h",codiceIdentificativoDominio);
				request.setAttribute("tx_codiceServizio_h",configurazioneBlackBox.getCodiceServizio());
				request.setAttribute("tx_codiceSegregazione_h",configurazioneBlackBox.getCodiceSegregazione());
				request.setAttribute("tx_applicationCode_h",configurazioneBlackBox.getCodiceApplicationCode());
				request.setAttribute("tx_auxDigit_h",configurazioneBlackBox.getAuxDigit());
					
				request.setAttribute("tx_enteda5", configurazioneBlackBox.getCodiceEnte());
				request.setAttribute("tx_idDominio", configurazioneBlackBox.getCodiceIdentificativoDominio());
				request.setAttribute("tx_applicationCode", configurazioneBlackBox.getCodiceApplicationCode());
				request.setAttribute("tx_codiceSegregazione", configurazioneBlackBox.getCodiceSegregazione());
				request.setAttribute("tx_auxDigit", configurazioneBlackBox.getAuxDigit());
				request.setAttribute("tx_calcoloIuv", configurazioneBlackBox.getFlagIuv());
				request.setAttribute("tx_urlFtp", configurazioneBlackBox.getUrlFtp());
				request.setAttribute("tx_usrFtp", configurazioneBlackBox.getUsrFtp());
				request.setAttribute("tx_passwordFtp", configurazioneBlackBox.getPasswordFtp());
				request.setAttribute("tx_dirFtpDownload", configurazioneBlackBox.getDirectoryFtpDownload());
				request.setAttribute("tx_dirFtpUpload", configurazioneBlackBox.getDirectoryFtpUpload());
				request.setAttribute("tx_pathInput", configurazioneBlackBox.getPathLocaleInput());
				request.setAttribute("tx_pathScarti", configurazioneBlackBox.getPathLocaleScarti());
				request.setAttribute("tx_pathOutput", configurazioneBlackBox.getPathLocaleOutput());
				request.setAttribute("tx_codiceServizio", configurazioneBlackBox.getCodiceServizio());
				request.setAttribute("tx_flagPoste", configurazioneBlackBox.getFlagPoste());
				request.setAttribute("tx_emailPoste", configurazioneBlackBox.getEmailPoste());
			
				
				

			break;
			
		case TX_BUTTON_EDIT:
			
			//appRet = ControlliCampi(request, session);
			// Aggiungere controlli sui dati inseriti
			appRet = 1;
			if (appRet > 0 ){
				
				try {
					updateConfigurazioneBlackBox(request);
					session.setAttribute("recordUpdate", "OK");
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
					session.setAttribute("recordUpdate", "KO");
					session.setAttribute("messaggio.recordInsert", e.getMessage());
					request.setAttribute("tx_button_edit", null);
					request.setAttribute("tx_button_edit_end", "ok");
				}
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
					esito = inserisciBlackbox(request);
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
//			dividiSocUtenteEnte(request);

			
			//request.setAttribute("tx_dval_sms_h",chiaveDataValidita);
			request.setAttribute("tx_button_cancel", "cancella");
			
			break;
		case TX_BUTTON_DELETE:
			try 
				{
					EsitoRisposte esito = new EsitoRisposte();
					esito = deleteBlackBox(request);
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
				chiaveEnteda5 = codici[3].substring(1, 6);
				request.setAttribute("tx_societa", codiceSocieta);
				request.setAttribute("tx_utente", codiceUtente);
				request.setAttribute("tx_ente", chiaveEnte);
				request.setAttribute("tx_enteda5", chiaveEnteda5);
				
			}
		}
	}

	private ConfigurazioneBlackBox selectConfigurazioneBlackBox(HttpServletRequest request) throws DaoException 
	{
		ConfigurazioneBlackBoxDao configurazioneBlackBoxDAO;
		ConfigurazioneBlackBox blackbox = new ConfigurazioneBlackBox();
		blackbox.setCodiceEnte(chiaveEnteda5);
		blackbox.setCodiceIdentificativoDominio(codiceIdentificativoDominio);
		//PG21XX09 SB - inizio
		blackbox.setCodiceApplicationCode((request.getAttribute("tx_applicationCode") == null ? "" : (String)request.getAttribute("tx_applicationCode")));
		blackbox.setAuxDigit((request.getAttribute("tx_auxDigit") == null ? "" : (String)request.getAttribute("tx_auxDigit")));
		blackbox.setCodiceSegregazione((request.getAttribute("tx_codiceSegregazione") == null ? "" : (String)request.getAttribute("tx_codiceSegregazione")));
		blackbox.setCodiceServizio((request.getAttribute("tx_codiceServizio") == null ? "" : (String)request.getAttribute("tx_codiceServizio")));
		//PG21XX09 SB - fine
		//inizio LP PG21XX04 Leak
		//configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(getBlackBoxDataSource(), getBlackBoxDbSchema());
		//blackbox = configurazioneBlackBoxDAO.select(blackbox);
		Connection connection = null;
		try {
			connection = getBlackBoxDataSource().getConnection();
			configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(connection, getBlackBoxDbSchema());
			blackbox = configurazioneBlackBoxDAO.select(blackbox);
		} catch (DaoException e) {
			throw new DaoException(e);
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		
		return blackbox; 
		//configurazioneSolleciti;
	}
	
	private Integer updateConfigurazioneBlackBox(HttpServletRequest request) throws DaoException {
		
		String codiceEnte =(request.getParameter("tx_enteda5_h") == null ? "" : (String)request.getParameter("tx_enteda5_h"));
		String codiceIdentificativoDominio = (request.getParameter("tx_idDominio_h") == null ? "" : (String)request.getParameter("tx_idDominio_h"));
		String codiceApplicationCode = (request.getParameter("tx_applicationCode_h") == null ? "" : (String)request.getParameter("tx_applicationCode_h"));
		String codiceSegregazione = (request.getParameter("tx_codiceSegregazione_h") == null ? "" : (String)request.getParameter("tx_codiceSegregazione_h"));
		String auxDigit = (request.getParameter("tx_auxDigit_h") == null ? "" : (String)request.getParameter("tx_auxDigit_h"));
		String flagIuv = (request.getAttribute("tx_calcoloIuv") == null ? "" : (String)request.getAttribute("tx_calcoloIuv"));
		String urlFtp = (request.getAttribute("tx_urlFtp") == null ? "" : (String)request.getAttribute("tx_urlFtp"));
		String usrFtp = (request.getAttribute("tx_usrFtp") == null ? "" : (String)request.getAttribute("tx_usrFtp"));
		String passwordFtp = (request.getAttribute("tx_passwordFtp") == null ? "" : (String)request.getAttribute("tx_passwordFtp"));
		String directoryFtpDownload = (request.getAttribute("tx_dirFtpDownload") == null ? "" : (String)request.getAttribute("tx_dirFtpDownload"));
		String directoryFtpUpload = (request.getAttribute("tx_dirFtpUpload") == null ? "" : (String)request.getAttribute("tx_dirFtpUpload"));
		String pathLocaleInput = (request.getAttribute("tx_pathInput") == null ? "" : (String)request.getAttribute("tx_pathInput"));
		String pathLocaleScarti = (request.getAttribute("tx_pathScarti") == null ? "" : (String)request.getAttribute("tx_pathScarti"));
		String pathLocaleOutput = (request.getAttribute("tx_pathOutput") == null ? "" : (String)request.getAttribute("tx_pathOutput"));
		String codiceServizio = (request.getAttribute("tx_codiceServizio") == null ? "" : (String)request.getAttribute("tx_codiceServizio"));
		String flagPoste = (request.getAttribute("tx_flagPoste") == null ? "N" : (String)request.getAttribute("tx_flagPoste"));
		String emailPoste = (request.getAttribute("tx_emailPoste") == null ? "" : (String)request.getAttribute("tx_emailPoste"));
		
		ConfigurazioneBlackBoxDao configurazioneBlackBoxDAO;
		ConfigurazioneBlackBox blackbox = new ConfigurazioneBlackBox();
		blackbox.setCodiceEnte(codiceEnte);
		blackbox.setCodiceIdentificativoDominio(codiceIdentificativoDominio);
		blackbox.setCodiceApplicationCode(codiceApplicationCode);
		blackbox.setCodiceSegregazione(codiceSegregazione);
		blackbox.setAuxDigit(auxDigit);
		blackbox.setFlagIuv(flagIuv);
		blackbox.setUrlFtp(urlFtp);
		blackbox.setUsrFtp(usrFtp);
		blackbox.setPasswordFtp(passwordFtp);
		blackbox.setDirectoryFtpDownload(directoryFtpDownload);
		blackbox.setDirectoryFtpUpload(directoryFtpUpload);
		blackbox.setPathLocaleInput(pathLocaleInput);
		blackbox.setPathLocaleScarti(pathLocaleScarti);
		blackbox.setPathLocaleOutput(pathLocaleOutput);
		blackbox.setCodiceServizio(codiceServizio);
		blackbox.setFlagPoste(flagPoste);
		blackbox.setEmailPoste(emailPoste);
			
		//inizio LP PG21XX04 Leak
		//configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(getBlackBoxDataSource(), getBlackBoxDbSchema());
		//int aggiornati = configurazioneBlackBoxDAO.update(blackbox);

		//return aggiornati;		
		Connection connection = null;
		try {
			connection = getBlackBoxDataSource().getConnection();
			configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(connection, getBlackBoxDbSchema());
			return configurazioneBlackBoxDAO.update(blackbox);
		} catch (DaoException e) {
			throw new DaoException(e);
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			//fine LP PG21XX04 Leak
		}
	}

	private EsitoRisposte inserisciBlackbox(HttpServletRequest request) throws DaoException 
	{
		
		String codiceEnte = (request.getAttribute("tx_enteda5") == null ? "" : (String)request.getAttribute("tx_enteda5"));
		String codiceIdentificativoDominio = (request.getAttribute("tx_idDominio") == null ? "" : (String)request.getAttribute("tx_idDominio"));
		String codiceApplicationCode = (request.getAttribute("tx_applicationCode") == null ? "" : (String)request.getAttribute("tx_applicationCode"));
		String codiceSegregazione = (request.getAttribute("tx_codiceSegregazione") == null ? "" : (String)request.getAttribute("tx_codiceSegregazione"));
		String auxDigit = (request.getAttribute("tx_auxDigit") == null ? "" : (String)request.getAttribute("tx_auxDigit"));
		String flagIuv = (request.getAttribute("tx_calcoloIuv") == null ? "" : (String)request.getAttribute("tx_calcoloIuv"));
		String urlFtp = (request.getAttribute("tx_urlFtp") == null ? "" : (String)request.getAttribute("tx_urlFtp"));
		String usrFtp = (request.getAttribute("tx_usrFtp") == null ? "" : (String)request.getAttribute("tx_usrFtp"));
		String passwordFtp = (request.getAttribute("tx_passwordFtp") == null ? "" : (String)request.getAttribute("tx_passwordFtp"));
		String directoryFtpDownload = (request.getAttribute("tx_dirFtpDownload") == null ? "" : (String)request.getAttribute("tx_dirFtpDownload"));
		String directoryFtpUpload = (request.getAttribute("tx_dirFtpUpload") == null ? "" : (String)request.getAttribute("tx_dirFtpUpload"));
		String pathLocaleInput = (request.getAttribute("tx_pathInput") == null ? "" : (String)request.getAttribute("tx_pathInput"));
		String pathLocaleScarti = (request.getAttribute("tx_pathScarti") == null ? "" : (String)request.getAttribute("tx_pathScarti"));
		String pathLocaleOutput = (request.getAttribute("tx_pathOutput") == null ? "" : (String)request.getAttribute("tx_pathOutput"));
		String codiceServizio = (request.getAttribute("tx_codiceServizio") == null ? "" : (String)request.getAttribute("tx_codiceServizio"));
		String flagPoste = (request.getAttribute("tx_flagPoste") == null ? "N" : (String)request.getAttribute("tx_flagPoste"));
		String emailPoste = (request.getAttribute("tx_emailPoste") == null ? "" : (String)request.getAttribute("tx_emailPoste"));
		

		ConfigurazioneBlackBoxDao configurazioneBlackBoxDAO;
		ConfigurazioneBlackBox blackbox = new ConfigurazioneBlackBox();
		blackbox.setCodiceEnte(codiceEnte);
		blackbox.setCodiceIdentificativoDominio(codiceIdentificativoDominio);
		blackbox.setCodiceApplicationCode(codiceApplicationCode);
		blackbox.setCodiceSegregazione(codiceSegregazione);
		blackbox.setAuxDigit(auxDigit);
		blackbox.setFlagIuv(flagIuv);
		blackbox.setUrlFtp(urlFtp);
		blackbox.setUsrFtp(usrFtp);
		blackbox.setPasswordFtp(passwordFtp);
		blackbox.setDirectoryFtpDownload(directoryFtpDownload);
		blackbox.setDirectoryFtpUpload(directoryFtpUpload);
		blackbox.setPathLocaleInput(pathLocaleInput);
		blackbox.setPathLocaleScarti(pathLocaleScarti);
		blackbox.setPathLocaleOutput(pathLocaleOutput);
		blackbox.setCodiceServizio(codiceServizio);
		blackbox.setFlagPoste(flagPoste);
		blackbox.setEmailPoste(emailPoste);
		
		//inizio LP PG21XX04 Leak
		//configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(getBlackBoxDataSource(), getBlackBoxDbSchema());
		////EsitoRisposte esitorisposte = new EsitoRisposte();
		//return configurazioneBlackBoxDAO.insert(blackbox);				
		Connection connection = null;
		try {
			connection = getBlackBoxDataSource().getConnection();
			configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(connection, getBlackBoxDbSchema());
			return configurazioneBlackBoxDAO.insert(blackbox);				
		} catch (DaoException e) {
			throw new DaoException(e);
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			//fine LP PG21XX04 Leak
		}
	}

	private EsitoRisposte deleteBlackBox(HttpServletRequest request) throws DaoException 
	{
		String  ente = (String)request.getAttribute("tx_ente");
		chiaveTipologiaSMS = (request.getAttribute("tx_tipo_sms") == null ? "" : (String)request.getAttribute("tx_tipo_sms"));
		
		ConfigurazioneBlackBoxDao configurazioneBlackBoxDAO;
		ConfigurazioneBlackBox blackbox = new ConfigurazioneBlackBox();
		blackbox.setCodiceEnte(ente);
		//PG21XX09 SB - inizio
		blackbox.setCodiceApplicationCode((request.getAttribute("tx_applicationCode") == null ? "" : (String)request.getAttribute("tx_applicationCode")));
		blackbox.setAuxDigit((request.getAttribute("tx_auxDigit") == null ? "" : (String)request.getAttribute("tx_auxDigit")));
		blackbox.setCodiceSegregazione((request.getAttribute("tx_codiceSegregazione") == null ? "" : (String)request.getAttribute("tx_codiceSegregazione")));
		blackbox.setCodiceServizio((request.getAttribute("tx_codiceServizio") == null ? "" : (String)request.getAttribute("tx_codiceServizio")));
		//PG21XX09 SB - fine
		
		//inizio LP PG21XX04 Leak
		//configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(getBlackBoxDataSource(), getBlackBoxDbSchema());
		//return configurazioneBlackBoxDAO.delete(blackbox);
		Connection connection = null;
		try {
			connection = getBlackBoxDataSource().getConnection();
			configurazioneBlackBoxDAO = BlackBoxDAOFactory.getBlackBox(connection, getBlackBoxDbSchema());
			return configurazioneBlackBoxDAO.delete(blackbox);
		} catch (DaoException e) {
			throw new DaoException(e);
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			if(connection != null) {
				try {
					connection.close();
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
