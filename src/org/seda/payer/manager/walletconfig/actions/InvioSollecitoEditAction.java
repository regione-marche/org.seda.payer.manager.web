package org.seda.payer.manager.walletconfig.actions;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.walletmanager.actions.WalletBaseManagerAction;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;

import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.ConfigurazioneSolleciti;
import com.seda.payer.core.wallet.dao.ConfigurazioneSollecitiDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;
import com.seda.payer.core.wallet.bean.EsitoRisposte;
import com.thoughtworks.xstream.io.binary.Token.Value;

@SuppressWarnings("serial")
public class InvioSollecitoEditAction extends WalletBaseManagerAction{
	private static String codop = "edit";
	//private static String ritornaViewstate = "ConfigurazioneInvioSolleciti_edit";
	private static String ritornaViewstate = "configurazioneinviosolleciti";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;
	private String codiceSocieta = "";
	private String codiceUtente = "";
	private String chiaveEnte = "";
	private String chiaveDataValidita = "";
	private String chiaveTipologiaSMS = "";
	private String operatore = "";
	private String messaggioErrore = "";
	private Integer appRet;
	
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
			appRet = ControlliCampi(request, session);
			break;
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
			/*
			 * Carico i valori dal WS
			 */
			codop = "edit";
			dividiSocUtenteEnte(request);
			chiaveDataValidita = (String)request.getAttribute("tx_dval_sms");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar_dval_sms  = Calendar.getInstance();			
			try {
				calendar_dval_sms.setTime(df.parse(chiaveDataValidita));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			chiaveTipologiaSMS = (request.getAttribute("tx_tipo_sms") == null ? "" : (String)request.getAttribute("tx_tipo_sms"));
			
			request.setAttribute("tx_dval_sms",calendar_dval_sms);
			request.setAttribute("tx_dval_sms_h",chiaveDataValidita);
			request.setAttribute("tx_tipo_sms_h",chiaveTipologiaSMS);
			
			if ( !chiaveDataValidita.equals("") && !chiaveTipologiaSMS.equals("")) {
				ConfigurazioneSolleciti configurazioneSolleciti = null;
				try {
					configurazioneSolleciti = selectConfigurazioneInvioSolleciti(request);
				} catch (DaoException e) {
					e.printStackTrace();
					setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
				}

				request.setAttribute("tx_tipo_sms", chiaveTipologiaSMS);
//				request.setAttribute("tx_dval_sms", chiaveDataValidita);
				
				String importostr = configurazioneSolleciti.getImportoResiduo().toString().replace(".", ",");
				String importoonere = configurazioneSolleciti.getImportoOnere().toString().replace(".", ",");
				
				request.setAttribute("tx_importo_residuo", importostr);
				
				//request.setAttribute("tx_importo_residuo", configurazioneSolleciti.getImportoResiduo());
				request.setAttribute("tx_intervallo_gg", configurazioneSolleciti.getIntervalloGiorniTraInviiSuccessivi());
				request.setAttribute("tx_tipo_schedulazione", configurazioneSolleciti.getTipoSchedulazione());
				request.setAttribute("tx_mese_inizio", configurazioneSolleciti.getMeseInizioInvioSMS());
				request.setAttribute("tx_mese_fine", configurazioneSolleciti.getMeseFineInvioSMS());
				request.setAttribute("tx_inviomail", configurazioneSolleciti.getInvioEmail());
				request.setAttribute("tx_codiceonere_cart", configurazioneSolleciti.getCodiceOnereSollecitoCartaceo());
				request.setAttribute("tx_desconere_cart", configurazioneSolleciti.getDescrOnereSollecitoCartaceo());
				request.setAttribute("tx_importo_onere", importoonere);
				request.setAttribute("tx_ggrivis_ana", configurazioneSolleciti.getGiorniRivestizioneAnagrafica());
				request.setAttribute("tx_conto_postale", configurazioneSolleciti.getContoCorrentePostale());
				request.setAttribute("tx_conto_postale_desc", configurazioneSolleciti.getDescrContoCorrentePostale());
				request.setAttribute("tx_intervallo_gg_sol", configurazioneSolleciti.getIntervalloGiorniInvioSollecito());
				request.setAttribute("tx_tribX_evoluzione", configurazioneSolleciti.getTribXEvoluzione());
				request.setAttribute("tx_flagAttivazione", configurazioneSolleciti.getFlagAttivazione());
				
				//request.setAttribute("tx_intervallo_rivestiz_gg", configurazioneSolleciti.geti);
				
				
			}
			break;
			
		case TX_BUTTON_EDIT:
			
			//appRet = ControlliCampi(request, session);
			// Aggiungere controlli sui dati inseriti
			appRet = 1;
			if (appRet > 0 ){
				
				try {
					updateConfigurazioneInvioSolleciti(request);
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
				appRet = ControlliCampi(request, session);
				// Aggiungere controlli sui dati inseriti
				if (appRet > 0 ){
					
					dividiSocUtenteEnte(request);
					
					EsitoRisposte esito = new EsitoRisposte();
					esito = inserisciInvioSollecito(request);
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

			chiaveDataValidita = (String)request.getAttribute("tx_dval_sms");
			df = new SimpleDateFormat("yyyy-MM-dd");
			calendar_dval_sms  = Calendar.getInstance();			
			try {
				calendar_dval_sms.setTime(df.parse(chiaveDataValidita));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			chiaveTipologiaSMS = (request.getAttribute("tx_tipo_sms") == null ? "" : (String)request.getAttribute("tx_tipo_sms"));
			
			//request.setAttribute("tx_dval_sms_h",chiaveDataValidita);
			request.setAttribute("tx_button_cancel", "cancella");
			
			break;
		case TX_BUTTON_DELETE:
			try 
				{
					EsitoRisposte esito = new EsitoRisposte();
					esito = deleteInvioSollecito(request);
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

	private ConfigurazioneSolleciti selectConfigurazioneInvioSolleciti(HttpServletRequest request) throws DaoException 
	{
		ConfigurazioneSollecitiDAO configurazioneSollecitiDAO;
		ConfigurazioneSolleciti configurazioneSolleciti = new ConfigurazioneSolleciti();
		configurazioneSolleciti.setCodiceSocieta(codiceSocieta);
		configurazioneSolleciti.setCuteCute(codiceUtente);
		configurazioneSolleciti.setChiaveEnte(chiaveEnte);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar_dval_sms  = Calendar.getInstance();
		try {
			//calendar_dval_sms.setTime(df.parse((String)request.getAttribute("tx_dval_sms")));
			calendar_dval_sms.setTime(df.parse(chiaveDataValidita));
		} catch (	ParseException e1) {
			e1.printStackTrace();
		}
		
		configurazioneSolleciti.setDataValidita(calendar_dval_sms);
		
		//chiaveTipologiaSMS = (String)request.getAttribute("tx_tipo_sms");
		configurazioneSolleciti.setSmsTipoCortesiaSollecito(chiaveTipologiaSMS);
		
		//inizio LP PG21XX04 Leak
		//configurazioneSollecitiDAO = WalletDAOFactory.getConfigurazioneSollecitiDAO(getWalletDataSource(), getWalletDbSchema());
		//configurazioneSolleciti = configurazioneSollecitiDAO.select(configurazioneSolleciti);
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			configurazioneSollecitiDAO = WalletDAOFactory.getConfigurazioneSollecitiDAO(conn, getWalletDbSchema());
			configurazioneSolleciti = configurazioneSollecitiDAO.select(configurazioneSolleciti);
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
		return configurazioneSolleciti;
	}
	
	private Integer updateConfigurazioneInvioSolleciti(HttpServletRequest request) throws DaoException {
		
		String  societa = (String)request.getAttribute("tx_societa");
		String  utente = (String)request.getAttribute("tx_utente");
		String  ente = (String)request.getAttribute("tx_ente");
				
		String dataValCalendarStr = (String)request.getAttribute("tx_dval_sms_h").toString().substring(0,10);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dataValCalendar  = Calendar.getInstance();
		try {
			dataValCalendar.setTime(df.parse(dataValCalendarStr));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		String tiposms = (request.getAttribute("tx_tipo_sms_h") == null ? "" : (String)request.getAttribute("tx_tipo_sms_h"));

		String importo_residuo = "0";
		
		if(request.getAttribute("tx_importo_residuo")!=null)
		{
			if(!request.getAttribute("tx_importo_residuo").toString().trim().equals(""))
				importo_residuo = ((String)request.getAttribute("tx_importo_residuo")).replace(",", ".");
		}
		
		
		
		
		BigDecimal importoResiduoSegno = new BigDecimal(importo_residuo);
		if( tiposms.equals("P") ||  tiposms.equals("S"))  // Cartaceo
		{
			if(importoResiduoSegno.signum()>0)
				importo_residuo = "-".concat(((String)request.getAttribute("tx_importo_residuo")).replace(",", "."));
			
		}
		String descrizioneOnere = "";
		if(tiposms.equals("P"))  // Cartaceo
			descrizioneOnere = (String)request.getAttribute("tx_desconere_cart");
		
		
				
		BigDecimal importoResiduo = new BigDecimal(importo_residuo);
		
		String invioEmail = (request.getAttribute("tx_inviomail") == null ? "" : (String)request.getAttribute("tx_inviomail"));
		String tipoSchedulazione = (request.getAttribute("tx_tipo_schedulazione") == null ? "" : (String)request.getAttribute("tx_tipo_schedulazione"));
		String codiceOnereSollecitoCartaceo = (request.getAttribute("tx_codiceonere_cart") == null ? "" : (String)request.getAttribute("tx_codiceonere_cart"));

		String contoCorrentePostale = (request.getAttribute("tx_conto_postale") == null ? "" : (String)request.getAttribute("tx_conto_postale"));
		String descrContoCorrentePostale = (request.getAttribute("tx_conto_postale_desc") == null ? "" : (String)request.getAttribute("tx_conto_postale_desc"));
		
		
		Integer giorniRivestizioneAnagrafica = 0;
		if((String)request.getAttribute("tx_ggrivis_ana")!=null && (String)request.getAttribute("tx_ggrivis_ana") != "")
			giorniRivestizioneAnagrafica = Integer.valueOf((String)request.getAttribute("tx_ggrivis_ana"));
		
		String importo_onere = "0";
		if((String)request.getAttribute("tx_importo_onere")!=null && (String)request.getAttribute("tx_importo_onere")!="")
			importo_onere = ((String)request.getAttribute("tx_importo_onere")).replace(",", ".");
		BigDecimal importoOnere = new BigDecimal(importo_onere);

		
		
		Integer intervalloGiorniInvioSollecito = 0;
		if((String)request.getAttribute("tx_intervallo_gg_sol")!=null && (String)request.getAttribute("tx_intervallo_gg_sol") != "")
			intervalloGiorniInvioSollecito = Integer.valueOf((String)request.getAttribute("tx_intervallo_gg_sol"));
		
		String tx_tribX_evoluzione = (request.getAttribute("tx_tribX_evoluzione") == null ? "" : (String)request.getAttribute("tx_tribX_evoluzione"));
		
		Integer intervalloGiorniTraInviiSuccessivi = 0;
		if((String)request.getAttribute("tx_intervallo_gg")!=null && (String)request.getAttribute("tx_intervallo_gg") != "")
			intervalloGiorniTraInviiSuccessivi = Integer.valueOf((String)request.getAttribute("tx_intervallo_gg"));

		Integer meseInizioInvioSMS = Integer.valueOf((request.getAttribute("tx_mese_inizio") == null ? "1" : (String)request.getAttribute("tx_mese_inizio")));
		Integer meseFineInvioSMS = Integer.valueOf((request.getAttribute("tx_mese_fine") == null ? "1" : (String)request.getAttribute("tx_mese_fine")));
		
		
		String flagAttivazione = "";
		flagAttivazione = (String)request.getAttribute("tx_flagAttivazione");
		if(flagAttivazione.equals(""))
		{
			flagAttivazione="N";
		}
		
		
		ConfigurazioneSollecitiDAO configurazioneSollecitiDAO;
		ConfigurazioneSolleciti configurazioneSolleciti = new ConfigurazioneSolleciti();
		configurazioneSolleciti.setCodiceSocieta(societa);
		configurazioneSolleciti.setCuteCute(utente);
		configurazioneSolleciti.setChiaveEnte(ente);
		configurazioneSolleciti.setDataValidita(dataValCalendar);
		configurazioneSolleciti.setSmsTipoCortesiaSollecito(tiposms);
		configurazioneSolleciti.setIntervalloGiorniTraInviiSuccessivi(intervalloGiorniTraInviiSuccessivi);
		configurazioneSolleciti.setImportoResiduo(importoResiduo);
		configurazioneSolleciti.setInvioEmail(invioEmail);
		configurazioneSolleciti.setTipoSchedulazione(tipoSchedulazione);
		configurazioneSolleciti.setCodiceOnereSollecitoCartaceo(codiceOnereSollecitoCartaceo);
		configurazioneSolleciti.setDescrOnereSollecitoCartaceo(descrizioneOnere);
		configurazioneSolleciti.setContoCorrentePostale(contoCorrentePostale);
		configurazioneSolleciti.setDescrContoCorrentePostale(descrContoCorrentePostale);
		configurazioneSolleciti.setGiorniRivestizioneAnagrafica(giorniRivestizioneAnagrafica);
		configurazioneSolleciti.setMeseInizioInvioSMS(meseInizioInvioSMS);
		configurazioneSolleciti.setMeseFineInvioSMS(meseFineInvioSMS);		
		configurazioneSolleciti.setImportoOnere(importoOnere);
		configurazioneSolleciti.setDescrContoCorrentePostale(descrContoCorrentePostale);
		configurazioneSolleciti.setOperatore(operatore);
		configurazioneSolleciti.setIntervalloGiorniInvioSollecito(intervalloGiorniInvioSollecito);
		configurazioneSolleciti.setFlagAttivazione(flagAttivazione);
		configurazioneSolleciti.setTribXEvoluzione(tx_tribX_evoluzione);

		//inizio LP PG21XX04 Leak
		//configurazioneSollecitiDAO = WalletDAOFactory.getConfigurazioneSollecitiDAO(getWalletDataSource(), getWalletDbSchema());
		//int aggiornati = configurazioneSollecitiDAO.update(configurazioneSolleciti);
		//
		//return aggiornati;
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			configurazioneSollecitiDAO = WalletDAOFactory.getConfigurazioneSollecitiDAO(conn, getWalletDbSchema());
			int aggiornati = configurazioneSollecitiDAO.update(configurazioneSolleciti);
			
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

	private EsitoRisposte inserisciInvioSollecito(HttpServletRequest request) throws DaoException 
	{
		String  societa = (String)request.getAttribute("tx_societa");
		String  utente = (String)request.getAttribute("tx_utente");
		String  ente = (String)request.getAttribute("tx_ente");
		
/*				
		String dataValCalendarStr = (String)request.getAttribute("tx_dval_sms").toString().substring(0,10);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dataValCalendar  = Calendar.getInstance();
		try {
			dataValCalendar.setTime(df.parse(dataValCalendarStr));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
*/		
		String anno = (String)request.getAttribute("tx_dval_sms_year");
		String mese =(String)request.getAttribute("tx_dval_sms_month");
		String giorno =(String)request.getAttribute("tx_dval_sms_day");
		chiaveTipologiaSMS = (request.getAttribute("tx_tipo_sms") == null ? "" : (String)request.getAttribute("tx_tipo_sms"));
		String datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dataValCalendar  = Calendar.getInstance();
		try {
			dataValCalendar.setTime(df.parse(datavalidita));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String tiposms = (request.getAttribute("tx_tipo_sms") == null ? "" : (String)request.getAttribute("tx_tipo_sms"));

		String importo_residuo = "0";
		if(request.getAttribute("tx_importo_residuo")!=null)
		{
			if(!request.getAttribute("tx_importo_residuo").toString().trim().equals(""))
				importo_residuo = ((String)request.getAttribute("tx_importo_residuo")).replace(",", ".");
		}
		
		if( chiaveTipologiaSMS.equals("P") ||  chiaveTipologiaSMS.equals("S"))  // Cartaceo
			importo_residuo = (((String)request.getAttribute("tx_importo_residuo")).replace(",", "."));
		BigDecimal importoResiduo = new BigDecimal(importo_residuo);
		
		String invioEmail = (request.getAttribute("tx_inviomail") == null ? "" : (String)request.getAttribute("tx_inviomail"));
		String tipoSchedulazione = (request.getAttribute("tx_tipo_schedulazione") == null ? "" : (String)request.getAttribute("tx_tipo_schedulazione"));
		String codiceOnereSollecitoCartaceo = (request.getAttribute("tx_codiceonere_cart") == null ? "" : (String)request.getAttribute("tx_codiceonere_cart"));

		String contoCorrentePostale = (request.getAttribute("tx_conto_postale") == null ? "" : (String)request.getAttribute("tx_conto_postale"));
		String descrContoCorrentePostale = (request.getAttribute("tx_conto_postale_desc") == null ? "" : (String)request.getAttribute("tx_conto_postale_desc"));
		
		
		Integer giorniRivestizioneAnagrafica = 0;
		if((String)request.getAttribute("tx_ggrivis_ana")!=null && (String)request.getAttribute("tx_ggrivis_ana") != "")
			giorniRivestizioneAnagrafica = Integer.valueOf((String)request.getAttribute("tx_ggrivis_ana"));
		
		String importo_onere = "0";
		if((String)request.getAttribute("tx_importo_onere")!=null && (String)request.getAttribute("tx_importo_onere")!="")
			importo_onere = ((String)request.getAttribute("tx_importo_onere")).replace(",", ".");
		BigDecimal importoOnere = new BigDecimal(importo_onere);

		
		Integer intervalloGiorniInvioSollecito = 0;
		if((String)request.getAttribute("tx_intervallo_gg_sol")!=null && (String)request.getAttribute("tx_intervallo_gg_sol") != "")
			intervalloGiorniInvioSollecito = Integer.valueOf((String)request.getAttribute("tx_intervallo_gg_sol"));
		
		
		String tx_tribX_evoluzione = (request.getAttribute("tx_tribX_evoluzione") == null ? "" : (String)request.getAttribute("tx_tribX_evoluzione"));
		
		
		Integer intervalloGiorniTraInviiSuccessivi = 0;
		if((String)request.getAttribute("tx_intervallo_gg")!=null && (String)request.getAttribute("tx_intervallo_gg") != "")
			intervalloGiorniTraInviiSuccessivi = Integer.valueOf((String)request.getAttribute("tx_intervallo_gg"));
		
		Integer meseInizioInvioSMS = 1;
		Integer meseFineInvioSMS = 1;
		if((String)request.getAttribute("tx_mese_inizio")!=null && (String)request.getAttribute("tx_mese_inizio") != "")
			meseInizioInvioSMS = Integer.valueOf((String)request.getAttribute("tx_mese_inizio"));
		
		if((String)request.getAttribute("tx_mese_fine")!=null && (String)request.getAttribute("tx_mese_fine") != "")
			meseFineInvioSMS = Integer.valueOf((String)request.getAttribute("tx_mese_fine"));
		
		
		
		String descrOnereSollecitoCartaceo;
		descrOnereSollecitoCartaceo = (request.getAttribute("tx_desconere_cart") == null ? "" : (String)request.getAttribute("tx_desconere_cart"));
		
		ConfigurazioneSollecitiDAO configurazioneSollecitiDAO;
		ConfigurazioneSolleciti configurazioneSolleciti = new ConfigurazioneSolleciti();
		configurazioneSolleciti.setCodiceSocieta(societa);
		configurazioneSolleciti.setCuteCute(utente);
		configurazioneSolleciti.setChiaveEnte(ente);
		configurazioneSolleciti.setDataValidita(dataValCalendar);
		configurazioneSolleciti.setSmsTipoCortesiaSollecito(tiposms);
		configurazioneSolleciti.setIntervalloGiorniTraInviiSuccessivi(intervalloGiorniTraInviiSuccessivi);
		configurazioneSolleciti.setImportoResiduo(importoResiduo);
		configurazioneSolleciti.setInvioEmail(invioEmail);
		configurazioneSolleciti.setTipoSchedulazione(tipoSchedulazione);
		configurazioneSolleciti.setCodiceOnereSollecitoCartaceo(codiceOnereSollecitoCartaceo);
		configurazioneSolleciti.setContoCorrentePostale(contoCorrentePostale);
		configurazioneSolleciti.setDescrContoCorrentePostale(descrContoCorrentePostale);
		configurazioneSolleciti.setDescrOnereSollecitoCartaceo(descrOnereSollecitoCartaceo);														
		configurazioneSolleciti.setGiorniRivestizioneAnagrafica(giorniRivestizioneAnagrafica);
		configurazioneSolleciti.setMeseInizioInvioSMS(meseInizioInvioSMS);
		configurazioneSolleciti.setMeseFineInvioSMS(meseFineInvioSMS);
		configurazioneSolleciti.setImportoOnere(importoOnere);
		configurazioneSolleciti.setDescrContoCorrentePostale(descrContoCorrentePostale);
		configurazioneSolleciti.setOperatore(operatore);
		configurazioneSolleciti.setIntervalloGiorniInvioSollecito(intervalloGiorniInvioSollecito);
		configurazioneSolleciti.setTribXEvoluzione(tx_tribX_evoluzione);
		
		//inizio LP PG21XX04 Leak
		//configurazioneSollecitiDAO = WalletDAOFactory.getConfigurazioneSollecitiDAO(getWalletDataSource(), getWalletDbSchema());
		////EsitoRisposte esitorisposte = new EsitoRisposte();
		//return configurazioneSollecitiDAO.insert(configurazioneSolleciti);			
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			configurazioneSollecitiDAO = WalletDAOFactory.getConfigurazioneSollecitiDAO(conn, getWalletDbSchema());
			return configurazioneSollecitiDAO.insert(configurazioneSolleciti);			
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

	private EsitoRisposte deleteInvioSollecito(HttpServletRequest request) throws DaoException 
	{
		String  societa = (String)request.getAttribute("tx_societa");
		String  utente = (String)request.getAttribute("tx_utente");
		String  ente = (String)request.getAttribute("tx_ente");
		
		String datavalidita = (String)request.getAttribute("tx_dval_sms").toString().substring(0,10);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar_dval_sms  = Calendar.getInstance();		
		try {
			calendar_dval_sms.setTime(df.parse(datavalidita));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		chiaveTipologiaSMS = (request.getAttribute("tx_tipo_sms") == null ? "" : (String)request.getAttribute("tx_tipo_sms"));
		
		ConfigurazioneSollecitiDAO configurazioneSollecitiDAO;
		ConfigurazioneSolleciti configurazioneSolleciti = new ConfigurazioneSolleciti();
		configurazioneSolleciti.setCodiceSocieta(societa);
		configurazioneSolleciti.setCuteCute(utente);
		configurazioneSolleciti.setChiaveEnte(ente);
		configurazioneSolleciti.setDataValidita(calendar_dval_sms);
		configurazioneSolleciti.setSmsTipoCortesiaSollecito(chiaveTipologiaSMS);
		
		//inizio LP PG21XX04 Leak
		//configurazioneSollecitiDAO = WalletDAOFactory.getConfigurazioneSollecitiDAO(getWalletDataSource(), getWalletDbSchema());
		//return configurazioneSollecitiDAO.delete(configurazioneSolleciti);
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			configurazioneSollecitiDAO = WalletDAOFactory.getConfigurazioneSollecitiDAO(conn, getWalletDbSchema());
			return configurazioneSollecitiDAO.delete(configurazioneSolleciti);
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

	private Integer ControlliCampi(HttpServletRequest request, HttpSession session)  
	{
		Integer ret=1;
		// Aggiungere controlli sui dati inseriti
		//chiaveTipologiaSMS = (request.getAttribute("tx_tipo_sms") == null ? "" : (String)request.getAttribute("tx_tipo_sms"));
		chiaveTipologiaSMS = (request.getAttribute("tx_tipo_sms_h") == null ? "" : (String)request.getAttribute("tx_tipo_sms_h"));
		chiaveTipologiaSMS = (request.getAttribute("tx_tipo_sms") == null ? "" : (String)request.getAttribute("tx_tipo_sms"));
		String importo_residuo = "0.00";
		if(request.getAttribute("tx_importo_residuo")!=null)
		{
			if(!request.getAttribute("tx_importo_residuo").toString().trim().equals(""))
				importo_residuo = ((String)request.getAttribute("tx_importo_residuo")).replace(",", ".");
		}
		BigDecimal importoResiduo = new BigDecimal(importo_residuo);

//		<s:ddloption value="C" text="SMS di Cortesia"/>
//		<s:ddloption value="S" text="SMS di Sollecito"/>
//		<s:ddloption value="P" text="Sollecito Cartaceo"/>
		
		if( chiaveTipologiaSMS.equals("P") )  // Cartaceo
		{
			request.setAttribute("tx_intervallo_gg","");
			request.setAttribute("tx_tipo_schedulazione","");
			request.setAttribute("tx_mese_inizio","");
			request.setAttribute("tx_mese_fine","");
		}
			
		if( !chiaveTipologiaSMS.equals("P") )
		{
			request.setAttribute("tx_codiceonere_cart","");
			request.setAttribute("tx_desconere_cart","");
			request.setAttribute("tx_importo_onere","");
			request.setAttribute("tx_ggrivis_ana","");
			request.setAttribute("tx_conto_postale","");
			request.setAttribute("tx_conto_postale_desc","");
			request.setAttribute("tx_intervallo_gg_sol","");
			request.setAttribute("tx_tribX_evoluzione","");
			Integer meseInizioInvioSMS = Integer.valueOf((request.getAttribute("tx_mese_inizio") == null ? "1" : (String)request.getAttribute("tx_mese_inizio")));
			Integer meseFineInvioSMS = Integer.valueOf((request.getAttribute("tx_mese_fine") == null ? "1" : (String)request.getAttribute("tx_mese_fine")));
			if (meseInizioInvioSMS > meseFineInvioSMS ){
				messaggioErrore = "Data invio SMS: il mese iniziale non può essere successivo a quello finale";
				setFormMessage("var_form", "Errore: " + messaggioErrore, request);
				ret = -1;	
			}
		}

		if( chiaveTipologiaSMS.equals("C") && importoResiduo.signum() < 0  )	{
			messaggioErrore = "l'importo residuo non può essere negativo con SMS di Cortesia";
			setFormMessage("var_form", "Errore: " + messaggioErrore, request);
			ret = -1;
		}
		if( chiaveTipologiaSMS.equals("S") && importoResiduo.signum() > 0  )	{
			messaggioErrore = "l'importo residuo non può essere positivo con SMS di Sollecito";
			setFormMessage("var_form", "Errore: " + messaggioErrore, request);
			ret = -1;
		}
			
		if( chiaveTipologiaSMS.equals("P") && importoResiduo.signum() > 0  )	{
			messaggioErrore = "l'importo residuo non può essere positivo con Sollecito Cartaceo";
			setFormMessage("var_form", "Errore: " + messaggioErrore, request);
			ret = -1;
		}

		if( ret == -1)
		{
			request.setAttribute("tx_button_edit", null);
			request.setAttribute("tx_button_aggiungi", null);
			request.setAttribute("tx_button_edit_end", "ok");
			// GC_2013_06_19 PORCO !!!!!!!!!!!!!
			
			//chiaveDataValidita = (String)request.getAttribute("tx_dval_sms");
			
			
			String anno = (String)request.getAttribute("tx_dval_sms_year");
			String mese =(String)request.getAttribute("tx_dval_sms_month");
			String giorno =(String)request.getAttribute("tx_dval_sms_day");
			chiaveDataValidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar_dval_sms  = Calendar.getInstance();			
			try {
				calendar_dval_sms.setTime(df.parse(chiaveDataValidita));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//chiaveTipologiaSMS = (request.getAttribute("tx_tipo_sms_h") == null ? "" : (String)request.getAttribute("tx_tipo_sms_h"));
			chiaveTipologiaSMS = (request.getAttribute("tx_tipo_sms") == null ? "" : (String)request.getAttribute("tx_tipo_sms"));
			request.setAttribute("tx_dval_sms",calendar_dval_sms);
			request.setAttribute("tx_dval_sms_h",chiaveDataValidita);
			request.setAttribute("tx_tipo_sms_h",chiaveTipologiaSMS);
			request.setAttribute("tx_tipo_sms",chiaveTipologiaSMS);		
		}
		return ret;
	}
}
