package org.seda.payer.manager.actions;

import java.io.StringReader;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.RowSetMetaDataImpl;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.adminusers.actions.GruppoAgenziaBaseManagerAction;
import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.string.Convert;
import com.seda.commons.validator.Validation;
import com.seda.commons.validator.ValidationException;
import com.seda.commons.validator.ValidationMessage;
import com.seda.j2ee5.maf.components.defender.bfl.BFLContext;
import com.seda.j2ee5.maf.components.defender.bfl.BFLUser;
import com.seda.j2ee5.maf.components.validation.ValidationContext;
import com.seda.j2ee5.maf.components.validation.ValidationErrorMap;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.action.HtmlAction;
import com.seda.j2ee5.maf.core.application.ApplicationData;
import com.seda.j2ee5.maf.core.application.ApplicationsData;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.taglibs.ViewStateManager;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.core.bean.GruppoAgenziaPageList;
import com.seda.payer.core.dao.GruppoAgenziaDAOFactory;
import com.seda.payer.core.dao.GruppoAgenziaDao;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ConfRendUtenteServizioEnteType;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoResponse;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoSearchRequest;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoSearchResponse;
import com.seda.payer.pgec.webservice.commons.dati.GetCanaliPagamentoDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.GetCanaliPagamentoDDLResponse;
import com.seda.payer.pgec.webservice.commons.dati.GetEntiDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.GetEntiDDLResponse;
import com.seda.payer.pgec.webservice.commons.dati.GetListaProvinceXml_DDLRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaProvinceXml_DDLResponseType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaSocietaXml_DDLRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaSocietaXml_DDLResponseType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaTipoCartaXml_DDLRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaTipoCartaXml_DDLResponseType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaTipologiaServizioAPPIOXml_DDL_RequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaTipologiaServizioAPPIOXml_DDL_ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaTipologiaServizioXml_DDLRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaTipologiaServizioXml_DDLResponseType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaTipologiaServizioXml_DDL_2RequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaTipologiaServizioXml_DDL_2ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaUtentiEntiXml_DDLRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaUtentiEntiXml_DDLResponseType;
import com.seda.payer.pgec.webservice.commons.dati.GetProvinceDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.GetProvinceDDLResponse;
import com.seda.payer.pgec.webservice.commons.dati.GetStaticDDLListsRequest;
import com.seda.payer.pgec.webservice.commons.dati.GetStaticDDLListsResponse;
import com.seda.payer.pgec.webservice.commons.dati.GetUtentiDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.GetUtentiDDLResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioneFiltrataRequestType;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaTransazioneFiltrataResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSearchRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSearchResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest2;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaOneriRequest;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaOneriResponse;
import com.sun.rowset.WebRowSetImpl;

@SuppressWarnings("serial")
public class BaseManagerAction extends HtmlAction {

	protected static String RENDICONTAZIONE = "rendicontazione";
	protected static String ADMINUSERS = "adminusers";
	protected static String MONITORAGGIO = "monitoraggio";
	protected static String RICONCILIAZIONE = "riconciliazione";
	protected static String RIVERSAMENTO = "riversamento";


	private String message = "";

	/**
	 * La CURRENT_APPLICATION del MAF
	 */
	@SuppressWarnings("unused")
	private String applicazioneCorrente = null;
	/**
	 * Lo UserBean utente
	 */
	protected UserBean userBean = null;
	/**
	 * La sessione utente
	 */
	private HttpSession httpSession = null;
	/**
	 * Area invio della pagina di gestione flussi di rendicontazione Abilitata
	 * (true/false)
	 */
	private boolean areaInvioRendicontazioneEnabled = false;
	/**
	 * Drop-Down-List "Società" disabilitata (true/false)
	 */
	protected boolean ddlSocietaDisabled = false;
	/**
	 * Drop-Down-List "Provincia" disabilitata (true/false)
	 */
	protected boolean ddlProvinciaDisabled = false;
	/**
	 * Drop-Down-List "Utente/Ente" disabilitata (true/false)
	 */
	protected boolean ddlUtenteEnteDisabled = false;
	/**
	 * Drop-Down-List "Utente" disabilitata (true/false)
	 */
	protected boolean ddlUtenteDisabled = false;

	protected boolean multiUtenteEnabled = false;

	public boolean isMultiUtenteEnabled() {
		return multiUtenteEnabled;
	}

	/**
	 * I codici Società,Utente ed Ente che tiene conto del parametro della
	 * request e del profilo utente
	 */
	protected String paramCodiceSocieta = null;
	protected String paramCodiceUtente = null;
	protected String paramCodiceEnte = null;
	protected String userProfile = null;

	private boolean esportaDatiEnabled = false;

	private boolean richiesteElaborazioniEnabled = false;

	public static enum FiredButton {
		TX_BUTTON_DEFAULT, TX_BUTTON_EDIT, TX_BUTTON_EDIT_END, TX_BUTTON_EDIT_OPERATORE, TX_BUTTON_CERCA, TX_BUTTON_RESET,
		TX_BUTTON_RESET_INS, TX_BUTTON_RESET_MOD,
		TX_BUTTON_STAMPA, TX_BUTTON_DOWNLOAD, TX_BUTTON_NULL, 
		TX_BUTTON_SOCIETA_CHANGED, TX_BUTTON_PROVINCIA_CHANGED, 
		TX_BUTTON_UTENTE_CHANGED, TX_BUTTON_IMPOSITORE_CHANGED, TX_BUTTON_CANALE_CHANGED,
		TX_BUTTON_TIPO_SERVIZIO_CHANGED,
		TX_BUTTON_AGGIUNGI, TX_BUTTON_AGGIUNGI_END, TX_BUTTON_AGGIUNGI_OPERATORE, TX_BUTTON_AVANTI,TX_BUTTON_STEP1,
		TX_BUTTON_MOVIMENTO_CHANGED, TX_BUTTON_MOVIMENTO_CHANGED_NO_JS, TX_BUTTON_DELETE, TX_BUTTON_DELETE_END, 
		TX_BUTTON_CANCEL, TX_BUTTON_PASSWORD_AUTOGENERATA_CHANGED, 
		TX_BUTTON_PROFILO_UTENTE_CHANGED, TX_BUTTON_SCADENZA_CHANGED, TX_BUTTON_INDIETRO, TX_BUTTON_NUOVO,
		TX_BUTTON_DETAIL_IMP, TX_BUTTON_DETAIL_CONTR, TX_BUTTON_CERCA_EXP, TX_BUTTON_LOGIN,
		TX_BUTTON_CERCA_SCAD,TX_BUTTON_CERCA_PAG,TX_BUTTON_CERCA_TRIB,
		TX_BUTTON_SEND_PDF_MAV, TX_BUTTON_ENTE_CHANGED, TX_BUTTON_MEGA_RIV, TX_BUTTON_CONFERMA_STORNO, TX_BUTTON_CONFERMA_RICONCILIAZIONE,TX_BUTTON_RICHIESTA_CONFERMA,
		TX_BUTTON_AVANZA_STATO, TX_BUTTON_SET_FLAG_CBI, TX_BUTTON_ADD, TX_BUTTON_REMOVE, TX_BUTTON_WIS, 
		TX_BUTTON_ComuneNascitaEstero, TX_BUTTON_ProvinciaNascita, TX_BUTTON_ProvinciaResidenza, TX_BUTTON_ComuneResidenzaEstero, 
		TX_BUTTON_ProvinciaDomicilio, TX_BUTTON_ComuneDomicilioEstero, TX_BUTTON_ProvinciaSedeLegale, TX_BUTTON_ComuneSedeLegaleEstero, 
		TX_BUTTON_ProvinciaSedeOperativa, TX_BUTTON_FamigliaMerceologica, TX_BUTTON_TIPOLOGIA_CHANGED,
		TX_BUTTON_SOCIETA_BEN_CHANGED, TX_BUTTON_UTENTE_BEN_CHANGED,TX_BUTTON_PROVINCIA_BEN_CHANGED, TX_BUTTON_AGGIORNA,TX_BUTTON_AGGIORNA2,
		TX_BUTTON_MODELLO_CHANGED, TX_BUTTON_ALLINEA, RB_PROPRIETARIA, RB_FEDERATA
		,TX_BUTTON_DOWNLOAD_RT, TX_BUTTON_DOWNLOAD_RPT, TX_BUTTON_COMUNE_CHANGED	//PG150180_001 GG
		,TX_BUTTON_DISCARICO, TX_BUTTON_SALVA, TX_BUTTON_TIPOLSTRUT_CHANGED
		,TX_BUTTON_TipologiaAlloggio 	//PG180050_001 GG
		,TX_BUTTON_REINVIAPUK //PG170280 CT
		,TX_BUTTON_ImpostaSoggiorno,
		TX_BUTTON_GRUPPO_AGENZIA_CHANGED  //RE180181 SB
		,TX_BUTTON_PROVINCIA_TITOLARE_CHANGED  //PG190300 SB
		,TX_BUTTON_PROVINCIA_GESTORE_CHANGED  //PG190300 SB
		,TX_BUTTON_RINOTIFICA   //PG190480 SB
		,TX_BUTTON_RIGENERACODICEATTIVAZIONE //LP PG200130
		,TX_BUTTON_UPLOAD
		,TX_BUTTON_CREATE1 // PG210160 KD
		,TX_BUTTON_CREATE2
		,TX_BUTTON_ESPORTADATI
		,TX_BUTTON_CERCA_PRENOTAZIONE
	}

	public static enum ProfiloUtente {
		AMMI, AMSO, AMUT, AMEN, AMNULL, PYCO   //EP200510_PRE_JAVA1_8 SB
	}

	public ProfiloUtente getProfiloUtente(String profilo) {
		if (profilo.equals("AMMI"))
			return ProfiloUtente.AMMI;
		if (profilo.equals("AMSO"))
			return ProfiloUtente.AMSO;
		if (profilo.equals("AMUT"))
			return ProfiloUtente.AMUT;
		if (profilo.equals("AMEN"))
			return ProfiloUtente.AMEN;
		if (profilo.equals("PYCO"))			 //EP200510_PRE_JAVA1_8 SB - inizio
			return ProfiloUtente.PYCO;		//EP200510_PRE_JAVA1_8 SB - fine
		return ProfiloUtente.AMNULL;
	}

	/**
	 * Questo metodo può essere richiamato da tutte le action per settare
	 * variabili e attributi legati alla profilazione utente: alcuni generali ed
	 * alcuni specifici alle varie applicazioni.
	 * 
	 */
	public void setProfile(HttpServletRequest request)  {
		/*
		 * 29/03/2011 - V.Bruno 
		 * 
		 * 1) eliminato "throws ActionException"
		 * 2) definito "ServletContext context" perchè quello ereditato da HtmlAction è nullo
		 */
		ServletContext context = request.getSession(false).getServletContext();
		httpSession = request.getSession();
		userBean = (UserBean) httpSession.getAttribute(SignOnKeys.USER_BEAN);
		applicazioneCorrente = (String) request.getAttribute(MAFAttributes.CURRENT_APPLICATION);

		userProfile = userBean.getUserProfile();
		multiUtenteEnabled = userBean.getMultiUtenteEnabled();

		/**********************************************************************
		 * QUI I SETTAGGI CHE DOVREBBERO ANDARE BENE PER TUTTE LE APPLICAZIONI
		 **********************************************************************/

		/**********************************************************************
		 * ABILITAZIONE DROP-DOWN-LIST SOCIETà, PROVINCIA E UTENTE/ENTE
		 **********************************************************************/
		/*
		 * Possono essere utilizzati da tutti se l'attributo "disable" delle DDL
		 * viene valorizzato con le variabili settate di seguito.
		 */
		/*
		 * La DDL "Società" è abilitata solo per il ruolo "AMMI"
		 */
		ddlSocietaDisabled = !userProfile.equals("AMMI");
		request.setAttribute("ddlSocietaDisabled", ddlSocietaDisabled);
		request.setAttribute("societaDdlDisabled", ddlSocietaDisabled);

		/*
		 * La DDL "Utente" è abilitata solo i ruoli "AMMI" e "AMSO"
		 */
		ddlUtenteDisabled = !(userProfile.equals("AMMI") || userProfile.equals("AMSO")) ;
		request.setAttribute("ddlUtenteDisabled", ddlUtenteDisabled);
		request.setAttribute("utenteDdlDisabled", ddlUtenteDisabled);

		/*
		 * La DDL Provincia è disabilitata solo per il ruolo "AMEN"
		 */
		ddlProvinciaDisabled = userProfile.equals("AMEN");
		request.setAttribute("ddlProvinciaDisabled", ddlProvinciaDisabled);
		request.setAttribute("provinciaDdlDisabled", ddlProvinciaDisabled);

		/*
		 * La DDL "Utente/Ente" è disabilitata solo per il ruolo "AMEN" senza
		 * l'opzione multiutente.
		 */
		ddlUtenteEnteDisabled = userProfile.equals("AMEN")
		&& !userBean.getMultiUtenteEnabled();
		request.setAttribute("ddlUtenteEnteDisabled", ddlUtenteEnteDisabled);


		/**********************************************************************
		 * ABILITAZIONE APPLICAZIONE RICONCILIAZIONE
		 **********************************************************************/
		ApplicationsData applicazioni = (ApplicationsData)context.getAttribute(MAFAttributes.APPLICATIONS);

		boolean ricAttiva = false;
		if (applicazioni.containsApplication(RICONCILIAZIONE))
		{
			ApplicationData appRiconciliazione = applicazioni.getApplication(RICONCILIAZIONE);
			ricAttiva = appRiconciliazione.isActive();
		}

		request.setAttribute("isRiconciliazioneAct", new Boolean(ricAttiva));

		/****************************************************************
		 * SET PROFILO UTENTE
		 ****************************************************************/

		request.setAttribute("userProfile", userBean.getProfile());

		//abilitazione riconciliazione utente
		/****************************************************************
		 * SET ABILITAZIONE APPLICAZIONI RICONCILIAZIONE, RENDICONTAZIONE 
		 * E MONITORAGGIO< da estendere a tutte le applicazioni>
		 ****************************************************************/
		List<String> applicazioniUte = userBean.getApplicazioni();

		//TODO <chiedere se inserire informazione in sessione>
		request.setAttribute("appRiconciliazioneUteEnabled", false);
		request.setAttribute("appRendicontazioneUteEnabled", false);
		request.setAttribute("appMonitoraggioUteEnabled", false);

		for (String app: applicazioniUte)
		{
			if (app.equals("riconciliazione"))
				request.setAttribute("appRiconciliazioneUteEnabled", true);
			if (app.equals("rendicontazione"))
				request.setAttribute("appRendicontazioneUteEnabled", true);
			if (app.equals("riconciliazionenn"))
				request.setAttribute("appRiconciliazioneUteEnabled", true);
			if (app.equals("monitoraggio"))
				request.setAttribute("appMonitoraggioUteEnabled", true);
		}

		/****************************************************************
		 * QUI I SETTAGGI SPECIFICI PER LE DIVERSE APPLICAZIONI
		 ****************************************************************/
		/****************************************************************
		 * USO "request.getAttribute" E NON "request.getParameter" IN MODO DA
		 * GESTIRE CORRETTAMENTE LA RICHIESTA DI "RESET"
		 ****************************************************************/

		//if (applicazioneCorrente.equals(RENDICONTAZIONE)|| applicazioneCorrente.equals(ADMINUSERS)|| applicazioneCorrente.equals(RENDICONTAZIONE))
		//;
		//{
		/*
		 * I settaggi che seguono potrebbero essere usati da tutte le
		 * applicazioni se i nomi dei campi della FORM hanno gli stessi nomi
		 * riportati di seguito
		 */

		/*
		 * L'area "Invio" della pagina di rendicontazione è abilitata se è
		 * abilitato il download dei flussi o l'invio dei flussi via ftp o
		 * email
		 */
		areaInvioRendicontazioneEnabled = userBean.getDownloadFlussiRendicontazioneEnabled() || userBean.getInvioFlussiRendicontazioneViaFtpEnabled() || userBean.getInvioFlussiRendicontazioneViaEmailEnabled() || userBean.getInvioFlussiRendicontazioneViaWsEnabled();
		request.setAttribute("areaInvioRendicontazioneEnabled",areaInvioRendicontazioneEnabled);

		esportaDatiEnabled = userBean.getFlagPrenotazioneFatturazioneEnabled() || userProfile.equals("AMEN") || userProfile.equals("AMMI");
		request.setAttribute("esportaDatiEnabled", esportaDatiEnabled);

		richiesteElaborazioniEnabled = userBean.getFlagRichiesteElaborazioniEnabled() || userProfile.equals("AMEN") || userProfile.equals("AMMI");
		request.setAttribute("richiesteElaborazioniEnabled", richiesteElaborazioniEnabled);

		String tx_societa = "";
		String tx_utente = "";
		String tx_ente = "";

		boolean isTxtEnteUtenteSelected=false;
		boolean isTxtUtenteSelected=false;

		String tx_UtenteEnte = (String) request.getAttribute("tx_UtenteEnte");
		String tx_Utente = (String) request.getAttribute("tx_utente");

		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, httpSession);
		//fine LP PG200060
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);

		switch(firedButton) {
		case TX_BUTTON_PROVINCIA_CHANGED:
			tx_UtenteEnte="";
			tx_Utente="";
			break;
		case TX_BUTTON_UTENTE_CHANGED:
			tx_UtenteEnte="";
			break;
		}


		if (tx_UtenteEnte != null && tx_UtenteEnte.trim().length() > 0 && tx_UtenteEnte.length() == 20) {
			isTxtEnteUtenteSelected=true;
			tx_societa = tx_UtenteEnte.substring(0, 5);
			tx_utente = tx_UtenteEnte.substring(5, 10);
			tx_ente = tx_UtenteEnte.substring(10);
		}

		if (tx_Utente != null && tx_Utente.trim().length() > 0 && tx_Utente.length() == 10) {
			isTxtUtenteSelected=true;
			tx_societa = tx_Utente.substring(0, 5);
			tx_utente = tx_Utente.substring(5);
		}


		switch (getProfiloUtente(userProfile)) {
		case AMEN:
			paramCodiceSocieta = userBean.getCodiceSocieta();
			request.setAttribute("tx_societa", paramCodiceSocieta);

			paramCodiceEnte = userBean.getChiaveEnteConsorzio();

			// Gestione del Flag MULTI UTENTE per i profili di tipo AMEN	
			if(userBean.getMultiUtenteEnabled()) {
				if (isTxtEnteUtenteSelected || isTxtUtenteSelected) {
					paramCodiceUtente = tx_utente;
				} 
			} else {
				paramCodiceUtente = userBean.getCodiceUtente();

				request.setAttribute("tx_utente", paramCodiceSocieta+paramCodiceUtente); //SVILUPPO_010 SB
				//inizio LP PG200060
				if(template.equals("regmarche")) {
					request.setAttribute("tx_utente", paramCodiceUtente);
				}
				//fine LP PG200060
				request.setAttribute("tx_UtenteEnte", paramCodiceSocieta + paramCodiceUtente + paramCodiceEnte);
			}

			break;
		case AMUT:
			paramCodiceSocieta = userBean.getCodiceSocieta();
			paramCodiceUtente = userBean.getCodiceUtente();
			request.setAttribute("tx_societa", paramCodiceSocieta);
			request.setAttribute("tx_utente", paramCodiceSocieta+paramCodiceUtente);   //SVILUPPO_010 SB
			//inizio LP PG200060
			if(template.equals("regmarche")) {
				request.setAttribute("tx_utente", paramCodiceUtente);
			}


			if (isTxtEnteUtenteSelected) {
				paramCodiceEnte = tx_ente;
			} else if (request.getAttribute("tx_ente")!=null && ((String)request.getAttribute("tx_ente")).trim().length()==10) {
				paramCodiceEnte = (String)request.getAttribute("tx_ente");
			} 
			/*
			if (request.getAttribute("tx_ente")!=null && ((String)request.getAttribute("tx_ente")).trim().length()>0)
				paramCodiceEnte = (String)request.getAttribute("tx_ente");
			else
				paramCodiceEnte = isTxtEnteUtenteSelected ? tx_ente : "";
			 */
			break;
		case AMSO:
			paramCodiceSocieta = userBean.getCodiceSocieta();
			request.setAttribute("tx_societa", paramCodiceSocieta);


			if (isTxtEnteUtenteSelected) {
				paramCodiceUtente = tx_utente;
				paramCodiceEnte = tx_ente;
			} else if (isTxtUtenteSelected) {
				paramCodiceUtente = tx_utente;
			} else if (request.getAttribute("tx_ente")!=null && ((String)request.getAttribute("tx_ente")).trim().length()==10) {
				paramCodiceEnte = (String)request.getAttribute("tx_ente");
			} 


			/*
			if (request.getAttribute("tx_utente")!=null && ((String)request.getAttribute("tx_utente")).trim().length()==5)
				paramCodiceUtente = (String)request.getAttribute("tx_utente");
			else if (request.getAttribute("tx_utente")!=null && ((String)request.getAttribute("tx_utente")).trim().length()==10)
				paramCodiceUtente=((String)request.getAttribute("tx_utente")).substring(5,10);
			else
				paramCodiceUtente = isTxtEnteUtenteSelected ? tx_utente : "";

			if (request.getAttribute("tx_ente")!=null && ((String)request.getAttribute("tx_ente")).trim().length()>0)
				paramCodiceEnte = (String)request.getAttribute("tx_ente");
			else
				paramCodiceEnte = isTxtEnteUtenteSelected ? tx_ente : "";
			 */

			break;
		case AMMI:
			if (request.getAttribute("tx_societa")!=null && ((String)request.getAttribute("tx_societa")).trim().length()>0)
				paramCodiceSocieta = (String)request.getAttribute("tx_societa");
			else
				paramCodiceSocieta = (isTxtEnteUtenteSelected || isTxtUtenteSelected) ? tx_societa : "";


			if (isTxtEnteUtenteSelected) {
				paramCodiceUtente = tx_utente;
				paramCodiceEnte = tx_ente;
			} else if (isTxtUtenteSelected) {
				paramCodiceUtente = tx_utente;
			} else if (request.getAttribute("tx_ente")!=null && ((String)request.getAttribute("tx_ente")).trim().length()==10) {
				paramCodiceEnte = (String)request.getAttribute("tx_ente");
			} 

			/*
			if (request.getAttribute("tx_utente")!=null && ((String)request.getAttribute("tx_utente")).trim().length()>0)
				paramCodiceUtente = (String)request.getAttribute("tx_utente");
			else
				paramCodiceUtente = isTxtEnteUtenteSelected ? tx_utente : "";
			 */
			/*
			if (request.getAttribute("tx_utente")!=null && ((String)request.getAttribute("tx_utente")).trim().length()==5)
				paramCodiceUtente = (String)request.getAttribute("tx_utente");
			else if (request.getAttribute("tx_utente")!=null && ((String)request.getAttribute("tx_utente")).trim().length()==10) {  
				paramCodiceUtente=((String)request.getAttribute("tx_utente")).substring(5,10);
				paramCodiceSocieta=((String)request.getAttribute("tx_utente")).substring(0,5);
			}
			else
				paramCodiceUtente = isTxtEnteUtenteSelected ? tx_utente : "";

			if (request.getAttribute("tx_ente")!=null && ((String)request.getAttribute("tx_ente")).trim().length()>0)
				paramCodiceEnte = (String)request.getAttribute("tx_ente");
			else
				paramCodiceEnte = isTxtEnteUtenteSelected ? tx_ente : "";
			 */
			break;
		}





	}


	protected FiredButton getFiredButton(HttpServletRequest request) {


		if (request.getAttribute("tx_button_default") != null)
			return FiredButton.TX_BUTTON_DEFAULT;


		if (request.getAttribute("tx_button_reset") != null)
			return FiredButton.TX_BUTTON_RESET;

		if (request.getAttribute("tx_button_set_flag_cbi") != null)
			return FiredButton.TX_BUTTON_SET_FLAG_CBI;

		if (request.getAttribute("tx_button_avanza_stato") != null)
			return FiredButton.TX_BUTTON_AVANZA_STATO;

		if (request.getAttribute("tx_button_mega_riv") != null)
			return FiredButton.TX_BUTTON_MEGA_RIV;

		if (request.getAttribute("tx_button_reset_ins") != null)
			return FiredButton.TX_BUTTON_RESET_INS;

		if (request.getAttribute("tx_button_reset_mod") != null)
			return FiredButton.TX_BUTTON_RESET_MOD;

		if (request.getAttribute("tx_button_reset") != null)
			return FiredButton.TX_BUTTON_RESET;

		if (request.getAttribute("tx_button_richiesta_conferma") != null)
			return FiredButton.TX_BUTTON_RICHIESTA_CONFERMA;

		if (request.getAttribute("tx_button_conferma_riconciliazione") != null)
			return FiredButton.TX_BUTTON_CONFERMA_RICONCILIAZIONE;

		if (request.getAttribute("tx_button_cerca_exp") != null)
			return FiredButton.TX_BUTTON_CERCA_EXP;

    if (request.getAttribute("tx_button_discarico") != null)
      return FiredButton.TX_BUTTON_DISCARICO;
    if (request.getAttribute("tx_button_salva2") != null)
      return FiredButton.TX_BUTTON_SALVA;

        
		if (request.getAttribute("tx_button_cerca") != null
				|| request.getAttribute("rowsPerPage") != null
				|| request.getAttribute("pageNumber") != null
				|| request.getAttribute("order") != null)
			return FiredButton.TX_BUTTON_CERCA;

		if (request.getAttribute("tx_button_edit") != null)
			return FiredButton.TX_BUTTON_EDIT;

		//inizio - RE180181 SB
		if (request.getAttribute("tx_button_gruppo_agenzia_changed") != null)
			return FiredButton.TX_BUTTON_GRUPPO_AGENZIA_CHANGED;
		//fine - RE180181 SB
		
		if (request.getAttribute("tx_button_stampa") != null)
			return FiredButton.TX_BUTTON_STAMPA;

		if (request.getAttribute("tx_button_download") != null)
			return FiredButton.TX_BUTTON_DOWNLOAD;

		if (request.getAttribute(Field.TX_BUTTON_SOCIETA_CHANGED.format()) != null
				|| Field.TX_BUTTON_SOCIETA_CHANGED.format().equals(
						request.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_SOCIETA_CHANGED;

		if (request.getAttribute(Field.TX_BUTTON_PROVINCIA_CHANGED.format()) != null
				|| Field.TX_BUTTON_PROVINCIA_CHANGED.format().equals(
						request.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_PROVINCIA_CHANGED;
		
		//PG140470
		if (request.getAttribute(Field.TX_BUTTON_COMUNE_CHANGED.format()) != null
				|| Field.TX_BUTTON_COMUNE_CHANGED.format().equals(
						request.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_COMUNE_CHANGED;

		if (request.getAttribute("tx_button_utente_changed") != null
				|| "tx_button_utente_changed".equals(request
						.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_UTENTE_CHANGED;


		if (request.getAttribute(Field.TX_BUTTON_ENTE_CHANGED.format()) != null
				|| Field.TX_BUTTON_ENTE_CHANGED.format().equals(
						request.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_ENTE_CHANGED;


		if (request.getAttribute(Field.TX_BUTTON_IMPOSITORE_CHANGED.format()) != null
				|| Field.TX_BUTTON_IMPOSITORE_CHANGED.format().equals(
						request.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_IMPOSITORE_CHANGED;

		if (request.getAttribute("tx_button_tipo_servizio_changed") != null
				|| "tx_button_tipo_servizio_changed".equals(request
						.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_TIPO_SERVIZIO_CHANGED;

		if (request.getAttribute("tx_button_indietro") != null)
			return FiredButton.TX_BUTTON_INDIETRO;

		if (request.getAttribute("tx_button_aggiungi") != null)
			return FiredButton.TX_BUTTON_AGGIUNGI;

		if (request.getAttribute("tx_button_delete") != null)
			return FiredButton.TX_BUTTON_DELETE;

		if (request.getAttribute("tx_button_delete_end") != null)
			return FiredButton.TX_BUTTON_DELETE_END;

		if (request.getAttribute("tx_button_avanti") != null)
			return FiredButton.TX_BUTTON_AVANTI;

		if (request.getAttribute("tx_button_step1") != null)
			return FiredButton.TX_BUTTON_STEP1;

		if (request.getAttribute("tx_button_edit_end") != null)
			return FiredButton.TX_BUTTON_EDIT_END;

		if (request.getAttribute("tx_button_aggiungi_end") != null)
			return FiredButton.TX_BUTTON_AGGIUNGI_END;

		if (request.getAttribute("tx_button_edit_operatore") != null)
			return FiredButton.TX_BUTTON_EDIT_OPERATORE;

		if (request.getAttribute("tx_button_aggiungi_operatore") != null)
			return FiredButton.TX_BUTTON_AGGIUNGI_OPERATORE;

		if (request.getAttribute("tx_button_cancel") != null)
			return FiredButton.TX_BUTTON_CANCEL;

		if (request.getAttribute("tx_button_password_autogenerata_changed") != null
				|| "tx_button_password_autogenerata_changed".equals(request
						.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_PASSWORD_AUTOGENERATA_CHANGED;

		if (request.getAttribute("tx_button_profilo_utente_changed") != null
				|| "tx_button_profilo_utente_changed".equals(request
						.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_PROFILO_UTENTE_CHANGED;

		if (request.getAttribute("tx_button_scadenza_changed") != null
				|| "tx_button_scadenza_changed".equals(request
						.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_SCADENZA_CHANGED;

		if (request.getAttribute("tx_button_movimento_changed") != null && ((String)request.getAttribute("tx_button_movimento_changed")).equals("Selezione"))
			return FiredButton.TX_BUTTON_MOVIMENTO_CHANGED;

		if (request.getAttribute("tx_button_movimento_changed_no_js") != null)
			return FiredButton.TX_BUTTON_MOVIMENTO_CHANGED_NO_JS;

		if (request.getAttribute("tx_button_nuovo") != null)
			return FiredButton.TX_BUTTON_NUOVO;

		if (request.getAttribute("tx_button_detail_imp") != null)
			return FiredButton.TX_BUTTON_DETAIL_IMP;

		if (request.getAttribute("tx_button_detail_contr") != null)
			return FiredButton.TX_BUTTON_DETAIL_CONTR;

		if (request.getAttribute("tx_button_login") != null)
			return FiredButton.TX_BUTTON_LOGIN;

		if (request.getAttribute("tx_button_cerca_scad") != null)
			return FiredButton.TX_BUTTON_CERCA_SCAD;

		if (request.getAttribute("tx_button_cerca_pag") != null)
			return FiredButton.TX_BUTTON_CERCA_PAG;

		if (request.getAttribute("tx_button_cerca_trib") != null)
			return FiredButton.TX_BUTTON_CERCA_TRIB;

		if (request.getAttribute("tx_button_send_pdf_mav") != null)
			return FiredButton.TX_BUTTON_SEND_PDF_MAV;

		if (request.getAttribute("tx_button_mega_riv") != null)
			return FiredButton.TX_BUTTON_MEGA_RIV;

		if (request.getAttribute("tx_button_conferma_storno") != null)
			return FiredButton.TX_BUTTON_CONFERMA_STORNO;

		if (request.getAttribute("tx_button_add") != null)
			return FiredButton.TX_BUTTON_ADD;

		if (request.getAttribute("tx_button_remove") != null)
			return FiredButton.TX_BUTTON_REMOVE;

		if (request.getAttribute("btnWis") != null)
			return FiredButton.TX_BUTTON_WIS;
		if (request.getAttribute("tx_button_salva") != null)
			return FiredButton.TX_BUTTON_AGGIUNGI;
		
		//PG180050_001 GG
		if (request.getAttribute(Field.TX_BUTTON_TIPOLSTRUT_CHANGED.format()) != null
				|| Field.TX_BUTTON_TIPOLSTRUT_CHANGED.format().equals(
						request.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_TIPOLSTRUT_CHANGED;

		//persona fisica.
		if (request.getAttribute("btnComuneNascitaEstero") != null)
			return FiredButton.TX_BUTTON_ComuneNascitaEstero;
		if (request.getAttribute("btnAvantiProvinciaNascita") != null)
			return FiredButton.TX_BUTTON_ProvinciaNascita;
		if (request.getAttribute("btnAvantiProvinciaResidenza") != null)
			return FiredButton.TX_BUTTON_ProvinciaResidenza;
		if (request.getAttribute("btnComuneResidenzaEstero") != null)
			return FiredButton.TX_BUTTON_ComuneResidenzaEstero;
		if (request.getAttribute("btnAvantiProvinciaDomicilio") != null)
			return FiredButton.TX_BUTTON_ProvinciaDomicilio;
		if (request.getAttribute("btnComuneDomicilioEstero") != null)
			return FiredButton.TX_BUTTON_ComuneDomicilioEstero;

		//persona giuridica.
		if (request.getAttribute("btnAvantiProvinciaSedeLegale") != null)
			return FiredButton.TX_BUTTON_ProvinciaSedeLegale;
		if (request.getAttribute("btnComuneSedeLegaleEstero") != null)
			return FiredButton.TX_BUTTON_ComuneSedeLegaleEstero;
		if (request.getAttribute("btnAvantiProvinciaSedeOperativa") != null)
			return FiredButton.TX_BUTTON_ProvinciaSedeOperativa;
		if (request.getAttribute("btnAvantiFamigliaMerceologica") != null)
			return FiredButton.TX_BUTTON_FamigliaMerceologica;
		//PG180050_001 GG - inizio
		if (request.getAttribute("btnAvantiTipologiaAlloggio") != null)
			return FiredButton.TX_BUTTON_TipologiaAlloggio;
		//PG180050_001 GG - fine
		if (request.getAttribute("btnImpostaSoggiorno") != null)
			return FiredButton.TX_BUTTON_ImpostaSoggiorno;
		if (request.getAttribute("btnChangeTipologiaSelected") != null
				|| "btnChangeTipologiaSelected".equals(request
						.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_TIPOLOGIA_CHANGED;

		if (request.getAttribute(Field.TX_BUTTON_SOCIETA_BEN_CHANGED.format()) != null
				|| Field.TX_BUTTON_SOCIETA_BEN_CHANGED.format().equals(
						request.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_SOCIETA_BEN_CHANGED;

		if (request.getAttribute(Field.TX_BUTTON_PROVINCIA_BEN_CHANGED.format()) != null
				|| Field.TX_BUTTON_PROVINCIA_BEN_CHANGED.format().equals(
						request.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_PROVINCIA_BEN_CHANGED;

		if (request.getAttribute("tx_button_utente_ben_changed") != null
				|| "tx_button_utente_ben_changed".equals(request
						.getAttribute("fired_button_hidden")))
			return FiredButton.TX_BUTTON_UTENTE_BEN_CHANGED;

		//PG110260
		if (request.getAttribute("tx_button_aggiorna") != null)
			return FiredButton.TX_BUTTON_AGGIORNA;
		
		if (request.getAttribute("tx_button_aggiorna2") != null)
			return FiredButton.TX_BUTTON_AGGIORNA2;

		if (request.getAttribute("tx_button_allinea") != null)
			return FiredButton.TX_BUTTON_ALLINEA;

		if (request.getAttribute("autenticazione") != null && request.getAttribute("autenticazione").equals("aut_pdp"))
			return FiredButton.RB_PROPRIETARIA;

		if (request.getAttribute("autenticazione") != null && request.getAttribute("autenticazione").equals("aut_federata"))
			return FiredButton.RB_FEDERATA;
		
		
		//PG150180_001 GG - inizio
		if (request.getAttribute("tx_button_download_rt") != null)
			return FiredButton.TX_BUTTON_DOWNLOAD_RT;
		if (request.getAttribute("tx_button_download_rpt") != null)
			return FiredButton.TX_BUTTON_DOWNLOAD_RPT;
		//PG150180_001 GG - fine

		if (request.getAttribute("tx_button_reinviapuk") != null) //PG170280 CT
			return FiredButton.TX_BUTTON_REINVIAPUK;
		
		if (request.getAttribute("tx_button_provincia_tit") != null || "tx_button_provincia_tit".equals(request.getAttribute("fired_button_hidden"))) //PG190300_001 SB
			return FiredButton.TX_BUTTON_PROVINCIA_TITOLARE_CHANGED;
		
		if (request.getAttribute("tx_button_provincia_gest") != null || "tx_button_provincia_gest".equals(request.getAttribute("fired_button_hidden"))) //PG190300_001 SB
			return FiredButton.TX_BUTTON_PROVINCIA_GESTORE_CHANGED;
		
		if (request.getAttribute("tx_button_rinotifica") != null)
			return FiredButton.TX_BUTTON_RINOTIFICA;

		//inizio LP PG200130
		if (request.getAttribute("tx_button_rigeneracodiceattivazione") != null)
			return FiredButton.TX_BUTTON_RIGENERACODICEATTIVAZIONE;
		//fine LP PG200130

		//inizio LP PG210160
		if(request.getAttribute("tx_button_upload") != null)
			return FiredButton.TX_BUTTON_UPLOAD;

		if (request.getAttribute("tx_button_esportadati") != null)
			return FiredButton.TX_BUTTON_ESPORTADATI;

		if (request.getAttribute("tx_button_cerca_prenotazione") != null)
			return FiredButton.TX_BUTTON_CERCA_PRENOTAZIONE;

		return FiredButton.TX_BUTTON_NULL;

	}

	/**
	 * Questo metodo restituisce un oggetto calendar a partire dal valore di un
	 * tag SEDA "date" individuato dal parametro "prefix" (lo stesso che viene
	 * utilizzato nel tag "date" di SEDA)
	 * 
	 * @param request
	 * @param prefix
	 * @return calendar
	 */
	protected Calendar getCalendar(HttpServletRequest request, String prefix) {

		Locale locale = (Locale) request.getSession().getAttribute(
				MAFAttributes.LOCALE);
		Calendar cal = null;
		if (prefix != null && !prefix.equals("")) {
			String giorno = request.getParameter(prefix + "_day");
			String mese = request.getParameter(prefix + "_month");
			String anno = request.getParameter(prefix + "_year");
			if (giorno != null && mese != null && anno != null
					&& !giorno.trim().equals("") && !mese.trim().equals("")
					&& !anno.trim().equals("")) {
				cal = Calendar.getInstance(locale);
				cal.set(Integer.parseInt(anno), Integer.parseInt(mese) - 1,
						Integer.parseInt(giorno));
			}
		}
		return cal;
	}


	protected Calendar getFine2099(HttpServletRequest request) {
		Locale locale = (Locale) request.getSession().getAttribute(
				MAFAttributes.LOCALE);
		Calendar cal = Calendar.getInstance(locale);
		cal.set(2099, 11, 31);
		return cal;
	}

	@SuppressWarnings("unchecked")
	protected void tx_SalvaStato(HttpServletRequest request) {
		Enumeration<String> e = request.getParameterNames();
		String p = "";
		while (e.hasMoreElements()) {
			p = e.nextElement();
			request.setAttribute(p, request.getParameter(p));
		}
		/*
		 * Salvo le date che richiedono l'utilizzo di un tipo "Calendar"
		 */
		request.setAttribute(Field.TX_DATA_DA.format(), getCalendar(request,
				Field.TX_DATA_DA.format()));
		request.setAttribute(Field.TX_DATA_A.format(), getCalendar(request,
				Field.TX_DATA_A.format()));
		return;
	}

	protected String getData(String param_data_day, String param_data_month,
			String param_data_year) {
		String data = "";
		if ((param_data_day != null) && (!param_data_day.equals(""))
				&& (param_data_month != null) && (!param_data_month.equals(""))
				&& (param_data_year != null) && (!param_data_year.equals(""))) {
			param_data_month = ("0" + param_data_month)
			.substring(param_data_month.length() - 1);
			param_data_day = ("0" + param_data_day).substring(param_data_day
					.length() - 1);
			data = param_data_year + "-" + param_data_month + "-"
			+ param_data_day;
		}
		return data;
	}

	private String errorMessage = "";

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setFormMessage(String formName, String message, HttpServletRequest request) {
		if (ValidationContext.getInstance().getValidationMessage() != null) 
		{
			ValidationErrorMap vem=new ValidationErrorMap();
			ArrayList<ValidationMessage> messages=new ArrayList<ValidationMessage>();
			ValidationMessage validationMessage=new ValidationMessage("", "", message);
			messages.add(validationMessage);

			vem.setForm(formName);
			vem.setMessages(messages);

			request.setAttribute(ValidationContext.getInstance().getValidationMessage(), vem);

			messages=null;
			validationMessage=null;
			vem=null;
		}
	}

	public void resetFormMessage(HttpServletRequest request) 
	{
		if (request.getAttribute(ValidationContext.getInstance().getValidationMessage())!= null )  
			request.setAttribute(ValidationContext.getInstance().getValidationMessage(), null);
	}


	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	public boolean isAreaInvioRendicontazioneEnabled() {
		return areaInvioRendicontazioneEnabled;
	}

	public void setAreaInvioRendicontazioneEnabled(
			boolean areaInvioRendicontazioneEnabled) {
		this.areaInvioRendicontazioneEnabled = areaInvioRendicontazioneEnabled;
	}

	public boolean isDdlSocietaDisabled() {
		return ddlSocietaDisabled;
	}

	public void setDdlSocietaDisabled(boolean ddlSocietaDisabled) {
		this.ddlSocietaDisabled = ddlSocietaDisabled;
	}

	public boolean isDdlProvinciaDisabled() {
		return ddlProvinciaDisabled;
	}

	public void setDdlProvinciaDisabled(boolean ddlProvinciaDisabled) {
		this.ddlProvinciaDisabled = ddlProvinciaDisabled;
	}

	public boolean isDdlUtenteEnteDisabled() {
		return ddlUtenteEnteDisabled;
	}

	public void setDdlUtenteEnteDisabled(boolean ddlUtenteEnteDisabled) {
		this.ddlUtenteEnteDisabled = ddlUtenteEnteDisabled;
	}

	public String getParamCodiceSocieta() {
		return paramCodiceSocieta;
	}

	public void setParamCodiceSocieta(String paramCodiceSocieta) {
		this.paramCodiceSocieta = paramCodiceSocieta;
	}

	public String getParamCodiceUtente() {
		return paramCodiceUtente;
	}

	public void setParamCodiceUtente(String paramCodiceUtente) {
		this.paramCodiceUtente = paramCodiceUtente;
	}

	public String getParamCodiceEnte() {
		return paramCodiceEnte;
	}

	public void setParamCodiceEnte(String paramCodiceEnte) {
		this.paramCodiceEnte = paramCodiceEnte;
	}

	public String getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}

	public boolean isDdlUtenteDisabled() {
		return ddlUtenteDisabled;
	}

	public void setDdlUtenteDisabled(boolean ddlUtenteDisabled) {
		this.ddlUtenteDisabled = ddlUtenteDisabled;
	}

	public boolean isEsportaDatiEnabled() {
		return esportaDatiEnabled;
	}

	public void setEsportaDatiEnabled(boolean esportaDatiEnabled) {
		this.esportaDatiEnabled = esportaDatiEnabled;
	}

	public boolean isRichiesteElaborazioniEnabled() {
		return richiesteElaborazioniEnabled;
	}

	public void setRichiesteElaborazioniEnabled(boolean richiesteElaborazioniEnabled) {
		this.richiesteElaborazioniEnabled = richiesteElaborazioniEnabled;
	}

	/**
	 * Questo metodo restituisce una stringa che contiene una data nel formato
	 * "yyyy-mm-dd" - Il parametro "prefix" è lo stesso che viene utilizzato nel
	 * tag "date" di SEDA
	 * 
	 * @param prefix
	 * @param request
	 * @return String
	 */
	protected String getDataByPrefix(String prefix, HttpServletRequest request) {
		String yyyymmdd = "";
		//String dd = request.getParameter(prefix + "_day");
		String dd = isNull(request.getAttribute(prefix + "_day"));
		//String mm = request.getParameter(prefix + "_month");
		String mm = isNull(request.getAttribute(prefix + "_month"));
		//String yyyy = request.getParameter(prefix + "_year");
		String yyyy = isNull(request.getAttribute(prefix + "_year"));
		if ((dd != null) && (!dd.equals("")) && (mm != null)
				&& (!mm.equals("")) && (yyyy != null) && (!yyyy.equals(""))) {
			mm = ("0" + mm).substring(mm.length() - 1);
			dd = ("0" + dd).substring(dd.length() - 1);
			yyyymmdd = yyyy + "-" + mm + "-" + dd;
		}
		return yyyymmdd;
	}

	
	protected String isNull(Object object){
		if(object!=null)
		{
			return (String) object;
		} 
		else
			return "";
	}

	protected int isNullInt(Object object){
		int res = 0;

		if(object!=null)
		{
			try {
				return Integer.parseInt(isNull(object));
			} catch (Exception e) {
				return res;
			}
		}
		else
			return res;

	}

	protected void loadDDL(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String codiceSocieta = request.getParameter("tx_societa");
		String codiceProvincia = request.getParameter("tx_provincia");
		String codiceUtente = request.getParameter("tx_utente");
		loadDDLStatic(request, session);
		loadDDLProvincia(request, session, codiceSocieta, false);
		loadDDLUtente(request, session, codiceSocieta, codiceProvincia, false);
		loadDDLEnte(request, session, codiceSocieta, codiceProvincia,
				codiceUtente, false);
	}

	protected void loadDDLProvincia(HttpServletRequest request,
			HttpSession session, String codiceSocieta, boolean forceReload) {
		// se ho già caricato le province convenzionate, le recupero dalla
		// sessione, altrimenti dal db
		if (session.getAttribute("listaProvince") != null && !forceReload) {
			request.setAttribute("listaProvince", (String) session
					.getAttribute("listaProvince"));
		} else {
			try {
				GetProvinceDDLRequest in = new GetProvinceDDLRequest();
				in.setCodiceSocieta(codiceSocieta);
				GetProvinceDDLResponse res = WSCache.commonsServer
				.getProvinceDDL(in, request);
				request.setAttribute("listaProvince", res.getListXml());
				// le metto in sessione per non doverle ricaricare ogni volta
				session.setAttribute("listaProvince", res.getListXml());
			} catch (FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	protected void loadDDLUtente(HttpServletRequest request,
			HttpSession session, String codiceSocieta, String codiceProvincia,
			boolean forceReload) {
		// se ho già caricato le province convenzionate, le recupero dalla
		// sessione, altrimenti dal db
		if (session.getAttribute("listaUtenti") != null && !forceReload) {
			request.setAttribute("listaUtenti", (String) session
					.getAttribute("listaUtenti"));
		} else {
			try {
				GetUtentiDDLRequest in = new GetUtentiDDLRequest();
				in.setCodiceSocieta(codiceSocieta);
				in.setCodiceProvincia(codiceProvincia);
				GetUtentiDDLResponse res = WSCache.commonsServer
				.getUtentiDDL(in, request);
				request.setAttribute("listaUtenti", res.getListXml());
				session.setAttribute("listaUtenti", res.getListXml());
			} catch (FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	protected void loadDDLEnte(HttpServletRequest request, HttpSession session,
			String codiceSocieta, String codiceProvincia, String codiceUtente,
			boolean forceReload) {
		if (session.getAttribute("listaEnti") != null && !forceReload) {
			request.setAttribute("listaEnti", (String) session
					.getAttribute("listaEnti"));
		} else {
			try {
				GetEntiDDLRequest in = new GetEntiDDLRequest();
				in.setCodiceProvincia(codiceProvincia);
				in.setCodiceSocieta(codiceSocieta);
				in.setCodiceUtente(codiceUtente);
				GetEntiDDLResponse res = WSCache.commonsServer.getEntiDDL(in, request);
				request.setAttribute("listaEnti", res.getListXml());
				session.setAttribute("listaEnti", res.getListXml());

			} catch (FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Carica le drop-down-list che non cambiano in base alle selezioni
	 * dell'utente: le cerca in sessione e, se non ci sono, le carica tramite i
	 * WS e le mette in sessione ---
	 * 
	 * 1) Società - 2) Canale di pagamento - 3) Strumenti di pagamento - 4)
	 * Tipologia Bollettini ---
	 * 
	 * NOTA: Questo metodo sostituirà "loadDDLStatic" perchè questo tratta come
	 * statiche alcune DDl che non lo sono.
	 * 
	 * @param request
	 * @param session
	 */
	protected void loadStaticXml_DDL(HttpServletRequest request,
			HttpSession session) {
		/*
		 * Cerco le stringhe XML in sessione
		 */
		if (session.getAttribute("listaSocieta") != null) {
			request.setAttribute("listaSocieta", (String) session.getAttribute("listaSocieta"));
			request.setAttribute("listaCanaliPagamento", (String) session.getAttribute("listaCanaliPagamento"));
			request.setAttribute("listaStrumenti", (String) session.getAttribute("listaStrumenti"));
			request.setAttribute("listaBollettini", (String) session.getAttribute("listaBollettini"));
			request.setAttribute("listaPSP", (String) session.getAttribute("listaPSP"));  //PG190180_001 20190517
			//request.setAttribute("listaTipologieServizio", (String) session.getAttribute("listaTipologieServizio"));
		} else {
			/*
			 * Se non ci sono le carico tramite il WS e poi le metto in sessione
			 */
			try {
				GetStaticDDLListsResponse res = WSCache.commonsServer.getStaticDDLLists(new GetStaticDDLListsRequest(), request);
				request.setAttribute("listaSocieta", res.getListaSocieta());
				request.setAttribute("listaCanaliPagamento", res.getListaCanali());
				request.setAttribute("listaStrumenti", res.getListaStrumenti());
				request.setAttribute("listaBollettini", res.getListaBollettini());
				request.setAttribute("listaPSP", res.getListaPSP());
				//request.setAttribute("listaTipologieServizio", res.getListaTipologieServizio());

				session.setAttribute("listaSocieta", res.getListaSocieta());
				session.setAttribute("listaCanaliPagamento", res.getListaCanali());
				session.setAttribute("listaStrumenti", res.getListaStrumenti());
				session.setAttribute("listaBollettini", res.getListaBollettini());
				session.setAttribute("listaPSP", res.getListaPSP());
				//session.setAttribute("listaTipologieServizio", res.getListaTipologieServizio());
			} catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle DDL statiche: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle DDL statiche: RemoteException - "
						+ e.getMessage());
			}
		}
	}

	protected void loadCanaliPagamentoXml_DDL(HttpServletRequest request) {
		HttpSession session = request.getSession();
		GetCanaliPagamentoDDLRequest in = null;
		GetCanaliPagamentoDDLResponse res = null;
		String xml = null;
		String lista = "listaCanaliPagamento";
		/*
		 * Cerco la lista dei canali di pagamento in sessione
		 */
		if (session.getAttribute(lista) != null)
			request.setAttribute(lista, (String) session
					.getAttribute(lista));
		else
			/*
			 * Se non c'è lo carico tramite il WS e poi lo metto in sessione
			 */
		{
			try {
				in = new GetCanaliPagamentoDDLRequest();
				res = WSCache.commonsServer.getCanaliPagamentoDDL(in, request);
				if (res != null)
				{
					xml = res.getListXml();
					request.setAttribute(lista, xml);
					session.setAttribute(lista, xml);
				}
				else
				{
					session.setAttribute(lista, null);
					setErrorMessage("Errore nel caricamento dei Canali di pagamento: null Response");
				}
			}
			catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento dei Canali di pagamento: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento dei Canali di pagamento: RemoteException - "
						+ e.getLocalizedMessage());
			}
		}

	}

	//22022011 GG - inzio
	protected void loadCanaliPagamentoAbilitazioneXml_DDL(HttpServletRequest request,HttpSession session)
	{
		CanalePagamentoSearchRequest in = null;
		CanalePagamentoSearchResponse res = null;
		CanalePagamentoResponse response = null;

		String xml = null;
		String lista = "listaCanaliPagamentoAbilitazione";
		/*
		 * Cerco la lista  in sessione
		 */
		if (session.getAttribute(lista) != null)
		{
			request.setAttribute(lista, (String)session.getAttribute(lista));
		}
		else
		{
			/*
			 * Se non c'è lo carico tramite il WS e poi lo metto in sessione
			 */
			in = new CanalePagamentoSearchRequest();
			in.setChiaveCanalePagamento("");
			in.setDescrizioneCanalePagamento("");
			in.setOrder("");
			in.setPageNumber(0);
			in.setRowsPerPage(0);
			try {
				res = WSCache.canPagamentoServer.getCanalePagamentos(in, request);
				if (res != null)
				{
					response = res.getResponse();
					if (response != null)
					{
						xml = response.getListXml();
					}
				}
				request.setAttribute(lista, xml);
				session.setAttribute(lista, xml);
			} catch (com.seda.payer.pgec.webservice.srv.FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	//22022011 GG fine

	protected void loadSocietaUtenteServizioXml_DDL(HttpServletRequest request,HttpSession session)
	{
		ConfigUtenteTipoServizioSearchRequest in = null;
		ConfigUtenteTipoServizioSearchResponse res = null;
		com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioResponse response = null;

		String xml = null;
		String lista = "listaSocietaUtenteServizio";
		/*
		 * Cerco la lista  in sessione
		 */
		if (session.getAttribute(lista) != null)
		{
			request.setAttribute(lista, (String)session.getAttribute(lista));
		}
		else
		{
			/*
			 * Se non c'è lo carico tramite il WS e poi lo metto in sessione
			 */
			in = new ConfigUtenteTipoServizioSearchRequest();
			in.setCompanyCode("");
			in.setCodiceUtente("");
			in.setCodiceTipologiaServizio("");
			in.setOrder("");
			in.setPageNumber(0);
			in.setRowsPerPage(0);
			in.setStrDescrSocieta("");
			in.setStrDescrUtente("");
			in.setStrDescrTipologiaServizio("");
			try {
				res = WSCache.configUtenteTipoServizioServer.getConfigUtenteTipoServizios(in, request);
				if (res != null)
				{
					response = res.getResponse();
					if (response != null)
					{
						xml = response.getListXml();
					}
				}
				request.setAttribute(lista, xml);
				session.setAttribute(lista, xml);
			} catch (com.seda.payer.pgec.webservice.srv.FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	protected void loadSocietaUtenteServizioEnteXml_DDL(HttpServletRequest request,HttpSession session)
	{
		ConfigUtenteTipoServizioEnteSearchRequest in = null;
		ConfigUtenteTipoServizioEnteSearchResponse res = null;
		com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteResponse response = null;

		String xml = null;
		String lista = "listaSocietaUtenteServizioEnte";
		/*
		 * Cerco la lista  in sessione
		 */
		if (session.getAttribute(lista) != null)
		{
			request.setAttribute(lista, (String)session.getAttribute(lista));
		}
		else
		{
			/*
			 * Se non c'è lo carico tramite il WS e poi lo metto in sessione
			 */
			in = new ConfigUtenteTipoServizioEnteSearchRequest();
			in.setCompanyCode("");
			in.setCodiceUtente("");
			in.setChiaveEnte("");
			in.setCodiceTipologiaServizio("");
			in.setOrder("");
			in.setPageNumber(0);
			in.setRowsPerPage(0);
			in.setStrDescrSocieta("");
			in.setStrDescrUtente("");
			in.setStrEnte("");
			in.setStrDescrTipologiaServizio("");
			try {
				res = WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEntes(in, request);
				if (res != null)
				{
					response = res.getResponse();
					if (response != null)
					{
						xml = response.getListXml();
					}
				}
				request.setAttribute(lista, xml);
				session.setAttribute(lista, xml);
			} catch (com.seda.payer.pgec.webservice.srv.FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	protected void loadSocietaUtenteEnteXml_DDL(HttpServletRequest request,HttpSession session)
	{
		ConfigUtenteTipoServizioEnteSearchRequest2 in = null;
		ConfigUtenteTipoServizioEnteSearchResponse res = null;
		ConfigUtenteTipoServizioEnteResponse response = null;

		String xml = null;
		String lista = "listaSocietaUtenteEnte";
		/*
		 * Cerco la lista  in sessione
		 */
		if (session.getAttribute(lista) != null)
		{
			request.setAttribute(lista, (String)session.getAttribute(lista));
		}
		else
		{
			/*
			 * Se non c'è lo carico tramite il WS e poi lo metto in sessione
			 */
			in = new ConfigUtenteTipoServizioEnteSearchRequest2();
			in.setCompanyCode("");
			in.setCodiceUtente("");
			in.setChiaveEnte("");
			in.setProcName("");

			try {
				res = WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEntes2(in, request);
				if (res != null)
				{
					response = res.getResponse();
					if (response != null)
					{
						xml = response.getListXml();
					}
				}
				request.setAttribute(lista, xml);
				session.setAttribute(lista, xml);
			} catch (com.seda.payer.pgec.webservice.srv.FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	protected void loadSocietaXml_DDL(HttpServletRequest request) {
		HttpSession session = request.getSession();
		GetListaSocietaXml_DDLRequestType in = null;
		GetListaSocietaXml_DDLResponseType res = null;
		ResponseType response = null;
		String xml = null;
		String lista = "listaSocieta";
		/*
		 * Cerco la lista delle società in sessione
		 */
		if (session.getAttribute("listaSocieta") != null)
			request.setAttribute("listaSocieta", (String) session
					.getAttribute(lista));
		else {
			/*
			 * Se non c'è lo carico tramite il WS e poi lo metto in sessione
			 */
			try {
				in = new GetListaSocietaXml_DDLRequestType();
				res = WSCache.commonsServer.getListaSocietaXml_DDL(in, request);
				response = res.getResponse();
				if (response.getRetCode().equals(ResponseTypeRetCode.value1)) {
					xml = res.getXml();
					request.setAttribute(lista, xml);
					session.setAttribute(lista, xml);
				} else {
					session.setAttribute(lista, null);
					setErrorMessage("Errore nel caricamento delle Società: "
							+ response.getRetMessage());
				}
			}

			catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Società: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Società: RemoteException - "
						+ e.getLocalizedMessage());
			}

		}
	}

	/**
	 * Recupera la lista delle province (sigla, descrizione) --- Le cerca in
	 * sessione e, se non ci sono le carica tramite il WS e poi le mette in
	 * sessione --- NOTA: Sostituirà "loadDDLProvincia"
	 * 
	 * @param request
	 * @param session
	 * @param codiceSocieta
	 * @param forceReload
	 */
	protected void loadProvinciaXml_DDL(HttpServletRequest request,
			HttpSession session, String codiceSocieta, boolean forceReload) {
		GetListaProvinceXml_DDLRequestType in = null;
		GetListaProvinceXml_DDLResponseType res = null;
		ResponseType response = null;
		String xml = null;
		String lista = "listaProvince";
		/*
		 * Cerco la stringa XML in sessions
		 */
		if (session.getAttribute(lista) != null && !forceReload) {
			request.setAttribute(lista, (String) session.getAttribute(lista));
		} else {
			try {
				in = new GetListaProvinceXml_DDLRequestType();
				in.setCodiceSocieta(codiceSocieta);
				res = WSCache.commonsServer.getListaProvinceXml_DDL(in, request);
				response = res.getResponse();
				if (response.getRetCode().equals(ResponseTypeRetCode.value1)) {
					xml = res.getXml();
					request.setAttribute(lista, xml);
					session.setAttribute(lista, xml);
				} else {
					session.setAttribute(lista, null);
					setErrorMessage("Errore nel caricamento delle Province: "
							+ response.getRetMessage());
				}
			} catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Province: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Province: RemoteException - "
						+ e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Recupera la lista delle Tipologie di Servizio (codice, descrizione) ---
	 * Le cerca in sessione e, se non ci sono le carica tramite il WS e poi le
	 * mette in sessione --- "
	 * 
	 * @param request
	 * @param session
	 * @param codiceSocieta
	 * @param forceReload
	 */
	protected void loadTipologiaServizioXml_DDL(HttpServletRequest request,
			HttpSession session, String codiceSocieta, boolean forceReload) {
		GetListaTipologiaServizioXml_DDLRequestType in = null;
		GetListaTipologiaServizioXml_DDLResponseType res = null;
		ResponseType response = null;
		String xml = null;
		String lista = "listaTipologieServizio";
		/*
		 * Cerco la stringa XML in sessions
		 */
		if (session.getAttribute(lista) != null && !forceReload) {
			request.setAttribute(lista, (String) session.getAttribute(lista));
		} else {
			try {
				in = new GetListaTipologiaServizioXml_DDLRequestType();
				in.setCodiceSocieta(codiceSocieta);
				res = WSCache.commonsServer
				.getListaTipologiaServizioXml_DDL(in, request);
				response = res.getResponse();
				if (response.getRetCode().equals(ResponseTypeRetCode.value1)) {
					xml = res.getXml();
					request.setAttribute(lista, xml);
					session.setAttribute(lista, xml);
				} else {
					session.setAttribute(lista, null);
					setErrorMessage("Errore nel caricamento delle Tipologie Servizio: "
							+ response.getRetMessage());
				}
			} catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Tipologie Servizio: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Tipologie Servizio: RemoteException - "
						+ e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Recupera la lista delle Tipologie di Servizio (codice, descrizione) ---
	 * Le cerca in sessione e, se non ci sono le carica tramite il WS e poi le
	 * mette in sessione --- "
	 * 
	 * @param request
	 * @param session
	 * @param codiceSocieta
	 * @param forceReload
	 */
	protected void loadTipologiaServizioXml_DDL_2(HttpServletRequest request,
			HttpSession session, String codiceSocieta, String codiceUtente, String chiaveEnte, boolean forceReload) {
		GetListaTipologiaServizioXml_DDL_2RequestType in = null;
		GetListaTipologiaServizioXml_DDL_2ResponseType res = null;
		ResponseType response = null;
		String xml = null;
		String lista = "listaTipologieServizio";

		/* Nel caso di un utente di tipologia AMEN*/
		String listaTipologiaServizio="";
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		if (user.getProfile().equals("AMEN")) {
			listaTipologiaServizio=user.getListaTipologiaServizioString();
		}

		/*
		 * Cerco la stringa XML in sessions
		 */
		if (session.getAttribute(lista) != null && !forceReload) {
			request.setAttribute(lista, (String) session.getAttribute(lista));
		} else {
			try {
				in = new GetListaTipologiaServizioXml_DDL_2RequestType();
				in.setCodiceSocieta(codiceSocieta);
				in.setCodiceUtente(codiceUtente);
				in.setChiaveEnte(chiaveEnte);
				in.setListaTipologiaServizio(listaTipologiaServizio);

				res = WSCache.commonsServer.getListaTipologiaServizioXml_DDL_2(in, request);

				response = res.getResponse();
				if (response.getRetCode().equals(ResponseTypeRetCode.value1)) {
					xml = res.getXml();
					request.setAttribute(lista, xml);
					session.setAttribute(lista, xml);
				} else {
					session.setAttribute(lista, null);
					setErrorMessage("Errore nel caricamento delle Tipologie Servizio: "
							+ response.getRetMessage());
				}
			} catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Tipologie Servizio: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Tipologie Servizio: RemoteException - "
						+ e.getLocalizedMessage());
			}
		}
	}
	
	protected void loadTipologiaServizioAPPIOXml_DDL(HttpServletRequest request,
			HttpSession session, String codiceSocieta, String codiceUtente , String chiaveEnte, boolean forceReload) {
		GetListaTipologiaServizioAPPIOXml_DDL_RequestType in = null;
		GetListaTipologiaServizioAPPIOXml_DDL_ResponseType res = null;
		ResponseType response = null;
		String xml = null;
		String lista = "listaTipologieServizio";

		/*
		 * Cerco la stringa XML in sessions
		 */
		if (session.getAttribute(lista) != null && !forceReload) {
			request.setAttribute(lista, (String) session.getAttribute(lista));
		} else {
			try {
				in = new GetListaTipologiaServizioAPPIOXml_DDL_RequestType();
				in.setCodiceSocieta(isNull(codiceSocieta));
				in.setCodiceUtente(isNull(codiceUtente));
				in.setChiaveEnte(isNull(chiaveEnte));
				

				res = WSCache.commonsServer.getListaTipologiaServizioAPPIOXml_DDL(in, request);

				response = res.getResponse();
				if (response.getRetCode().equals(ResponseTypeRetCode.value1)) {
					xml = res.getXml();
					request.setAttribute(lista, xml);
					session.setAttribute(lista, xml);
				} else {
					session.setAttribute(lista, null);
					setErrorMessage("Errore nel caricamento delle Tipologie Servizio APPIO : "
							+ response.getRetMessage());
				}
			} catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Tipologie Servizio APPIO: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Tipologie Servizio: RemoteException - "
						+ e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Recupera la lista degli Utenti/enti (codice , descrizione ) --- Le cerca
	 * in sessione e, se non ci sono le carica tramite il WS e poi le mette in
	 * sessione --- "
	 * 
	 * @param request
	 * @param session
	 * @param codiceSocieta
	 * @param forceReload
	 */
	protected void LoadListaUtentiEntiXml_DDL(HttpServletRequest request,
			HttpSession session, String codiceSocieta, String siglaProvincia,
			String codiceEnte, String codiceUtente, boolean forceReload) {
		GetListaUtentiEntiXml_DDLRequestType in = null;
		GetListaUtentiEntiXml_DDLResponseType res = null;
		ResponseType response = null;
		String xml = null;
		String lista = "listaUtentiEnti";
		/*
		 * Cerco la stringa XML in sessions
		 */
		if (session.getAttribute(lista) != null && !forceReload) {
			request.setAttribute(lista, (String) session.getAttribute(lista));
		} else {
			try {
				in = new GetListaUtentiEntiXml_DDLRequestType();
				in.setCodiceSocieta(codiceSocieta);
				in.setCodiceUtente(codiceUtente);
				in.setSiglaProvincia(siglaProvincia);
				in.setCodiceEnte(codiceEnte);
				res = WSCache.commonsServer.getListaUtentiEntiXml_DDL(in, request);
				response = res.getResponse();
				if (response.getRetCode().equals(ResponseTypeRetCode.value1)) {
					xml = res.getXml();
					request.setAttribute(lista, xml);
					session.setAttribute(lista, xml);
				} else {
					session.setAttribute(lista, null);
					setErrorMessage("Errore nel caricamento degli Utenti/Enti: "
							+ response.getRetMessage());
				}
			} catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento degli Utenti/Enti: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento degli Utenti/Enti: RemoteException - "
						+ e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Recupera la lista dei Gateway (codice gateway, descrizione utente +
	 * descrizione gateway) --- Le cerca in sessione e, se non ci sono le carica
	 * tramite il WS e poi le mette in sessione --- "
	 * 
	 * @param request
	 * @param session
	 * @param codiceSocieta
	 * @param codiceUtente
	 * @param forceReload
	 */
	protected void loadListaGatewayXml_DDL(HttpServletRequest request,
			HttpSession session, String codiceSocieta, String codiceUtente,
			boolean forceReload) {
		GetListaTipoCartaXml_DDLRequestType in = null;
		GetListaTipoCartaXml_DDLResponseType res = null;
		ResponseType response = null;
		String xml = null;
		String lista = "listaGateway";
		/*
		 * Cerco la stringa XML in sessions
		 */
		if (session.getAttribute(lista) != null && !forceReload) {
			request.setAttribute(lista, (String) session.getAttribute(lista));
		} else {
			try {
				in = new GetListaTipoCartaXml_DDLRequestType();
				in.setCodiceSocieta(codiceSocieta);
				in.setCodiceUtente(codiceUtente);
				res = WSCache.commonsServer.getListaTipoCartaXml_DDL(in, request);
				response = res.getResponse();
				if (response.getRetCode().equals(ResponseTypeRetCode.value1)) {
					xml = res.getXml();
					request.setAttribute(lista, xml);
					session.setAttribute(lista, xml);
				} else {
					session.setAttribute(lista, null);
					setErrorMessage("Errore nel caricamento delle Tipologie Carta: "
							+ response.getRetMessage());
				}
			} catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Tipologie Carta: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Tipologie Servizio: RemoteException - "
						+ e.getLocalizedMessage());
			}
		}
	}

	protected void loadDDLStatic(HttpServletRequest request, HttpSession session) {
		if (session.getAttribute("listaCartePagamento") != null) {
			request.setAttribute("listaCartePagamento", (String) session
					.getAttribute("listaCartePagamento"));
			request.setAttribute("listaCanaliPagamento", (String) session
					.getAttribute("listaCanaliPagamento"));
			request.setAttribute("listaStrumenti", (String) session
					.getAttribute("listaStrumenti"));
			request.setAttribute("listaTipologieServizio", (String) session
					.getAttribute("listaTipologieServizio"));
			request.setAttribute("listaSocieta", (String) session
					.getAttribute("listaSocieta"));
			//request.setAttribute("listaGateway", (String) session
			//.getAttribute("listaGateway"));
			request.setAttribute("listaBollettini", (String) session
					.getAttribute("listaBollettini"));
		} else {
			try {
				GetStaticDDLListsResponse res = WSCache.commonsServer
				.getStaticDDLLists(new GetStaticDDLListsRequest(), request);
				request
				.setAttribute("listaCartePagamento", res
						.getListaCarte());
				request.setAttribute("listaCanaliPagamento", res
						.getListaCanali());
				request.setAttribute("listaStrumenti", res.getListaStrumenti());
				request.setAttribute("listaTipologieServizio", res
						.getListaTipologieServizio());
				request.setAttribute("listaSocieta", res.getListaSocieta());
				//request.setAttribute("listaGateway", res.getListaGateway());
				request.setAttribute("listaBollettini", res
						.getListaBollettini());
				// lo metto in sessione per risettarlo dopo ogni postback
				session
				.setAttribute("listaCartePagamento", res
						.getListaCarte());
				session.setAttribute("listaCanaliPagamento", res
						.getListaCanali());
				session.setAttribute("listaStrumenti", res.getListaStrumenti());
				session.setAttribute("listaTipologieServizio", res
						.getListaTipologieServizio());
				session.setAttribute("listaSocieta", res.getListaSocieta());
				//session.setAttribute("listaGateway", res.getListaGateway());
				session.setAttribute("listaBollettini", res
						.getListaBollettini());
			} catch (FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Questo metodo restituisce una stringa contenente il path della cartella
	 * in cui sono contenuti i flussi di rendicontazione
	 * @param request TODO
	 * 
	 * @return String
	 */
	protected String getDirectoryFlussi(HttpServletRequest request) {
		String directoryFlussi = null;
		ServletContext context = request.getSession(false).getServletContext();
		PropertiesTree configuration = (PropertiesTree) context
		.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		if (configuration != null)
			directoryFlussi = configuration
			.getProperty(PropertiesPath.directoryFlussi
					.format(PropertiesPath.defaultnode.format()));
		return directoryFlussi;
	}

	/**
	 * Questo metodo restiruisce il valore di default delle righe di una lista
	 * @param request TODO
	 * 
	 * @return int
	 */
	protected int getDefaultListRows(HttpServletRequest request) {
		int defaultListRows = 5;
		ServletContext context = request.getSession(false).getServletContext();
		PropertiesTree configuration = (PropertiesTree) context
		.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		if (configuration != null) {
			String s_defaultListRows = configuration
			.getProperty(PropertiesPath.defaultListRows.format());
			if (s_defaultListRows != null)
				defaultListRows = Integer.parseInt(s_defaultListRows);
		}
		return defaultListRows;
	}



	@SuppressWarnings("unchecked")
	protected void resetParametri(HttpServletRequest request) {
		Enumeration e = request.getParameterNames();
		String p = "";
		while (e.hasMoreElements()) {
			p = (String) e.nextElement();
			request.setAttribute(p, "");
		}
		return;
	}


	/**
	 * Carica in sessione e negli attributi della request (con il nome "listaApplicazioniPayer")
	 * la lista delle applicazioni Payer 
	 *
	 * @param request
	 * @param onlyActive - se "true" seleziona solo le applicazioni attive
	 * @param onlyProtected - se "true" seleziona solo le applicazioni protette
	 * @param useDescription - se "true" usa come descrizione quella di MAF-APPLICATIONS.XML altrimenti il nome dell'applicazione 
	 */
	protected void loadPayerApplicationsXml_DDL
	(
			HttpServletRequest request,
			boolean onlyActive, 
			boolean onlyProtected, 
			boolean useDescription
	) 
	{
		HttpSession session = request.getSession();
		String xml = null;
		String lista = "listaApplicazioniPayer";
		/*
		 * Cerco la lista delle applicazioni in sessione
		 */
		if (session.getAttribute(lista) != null)
			request.setAttribute(lista, (String) session.getAttribute(lista));
		else 
		{
			/*
			 * Se non c'è la carico e poi lo metto in sessione.
			 */
			xml = getPayerApplicationsXml(request,onlyActive, onlyProtected, useDescription);
			request.setAttribute(lista, xml);
			session.setAttribute(lista, xml);
		}
	}

	protected void loadPayerApplicationsXml_DDL_properties (HttpServletRequest request, HttpSession session,
			boolean applicazioniManager, boolean applicazioniWeb)
	{
		String lista = "listaApplicazioniPayer";
		//inizio LP PG21XX04 Leak
		WebRowSet wrs = null;
		//fine LP PG21XX04 Leak
		try {
			String strApplManager = "";
			String strApplWeb = "";
			if (applicazioniManager)
				strApplManager = getListaApplicazioniManager(request, session).trim();
			if (applicazioniWeb) 
				strApplWeb = getListaApplicazioniWeb(request, session).trim();

			String strAppl = strApplManager + strApplWeb;
			String[] listaAppl = strAppl.split(";");

			//le ordino in ordine alfabetico
			SortedMap<String, String> hmSorted = new TreeMap<String, String>();
			for (String appl : listaAppl)
			{
				String[] aVal = appl.trim().split("\\|");
				hmSorted.put(aVal[0], aVal[1]);
			}

			//preparo il webrowset
			//inizio LP PG21XX04 Leak
			//WebRowSet wrs = new WebRowSetImpl();
			wrs = new WebRowSetImpl();
			//fine LP PG21XX04 Leak
			RowSetMetaDataImpl md = new RowSetMetaDataImpl();
			md.setColumnCount(2);
			md.setColumnType(1, Types.VARCHAR);
			md.setColumnType(2, Types.VARCHAR);
			wrs.setMetaData(md);

			for (String appl : hmSorted.keySet())
			{
				wrs.moveToInsertRow();
				wrs.updateString(1, appl);
				wrs.updateString(2, hmSorted.get(appl));
				wrs.insertRow();
			}

			wrs.moveToCurrentRow();
			request.setAttribute(lista, Convert.webRowSetToString(wrs));

		} catch (Exception e) {
			
			WSCache.logWriter.logError("Errore durante il caricamento della dropdownlist dei servizi", e);
		}
		//inizio LP PG21XX04 Leak
		finally
		{
	    	try {
	    		if(wrs != null) {
	    			wrs.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
	}

	private String getListaApplicazioniManager(HttpServletRequest request, HttpSession session)
	{
		if (ManagerStarter.configuration != null)
		{
			try
			{
				String template = getTemplateCurrentApplication(request,session);
				return ManagerStarter.configuration.getProperty(PropertiesPath.servizimanager.format(template));
			} catch (Exception e) {
				WSCache.logWriter.logError("Errore durante la lettura della proprietà payerManagerWeb.servizimanager dal file di properties", e);
			}
		}
		return "";	
	}

	private String getListaApplicazioniWeb(HttpServletRequest request, HttpSession session)
	{
		if (ManagerStarter.configuration != null)
		{
			try
			{
				String template = getTemplateCurrentApplication(request,session);
				return ManagerStarter.configuration.getProperty(PropertiesPath.serviziweb.format(template));
			} catch (Exception e) {
				WSCache.logWriter.logError("Errore durante la lettura della proprietà payerManagerWeb.serviziweb dal file di properties", e);
			}
		}
		return "";	
	}

	/***
	 * Restituisce il nome del template dell'applicazione corrente
	 * @param request
	 * @param session
	 * @return
	 */
	public String getTemplateCurrentApplication(HttpServletRequest request, HttpSession session)
	{
		MAFRequest mafReq = new MAFRequest(request);
		String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
		if (applicationName != null)
		{
			UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
			if (userBean != null)
			{
				String templateName = userBean.getTemplate(applicationName);
				if (templateName != null && !templateName.equals(""))
					return templateName;
				else
					return "default";
			}
		}
		else
		{
			//recupero il name del template dal file di properties
			if (ManagerStarter.configuration != null)
			{
				String templateName = ManagerStarter.configuration.getProperty(PropertiesPath.templateBase.format() + request.getServerName());

				if (templateName != null && !templateName.equals(""))
					return templateName;
			}
		}
		return "default";
	}


	/**
	 * Restituisce la lista delle applicazioni come WebRowSet in formato XML - L'elenco
	 * è ricavato utilizzando l'oggetto "ApplicationsData" del MAF ed è ordinato in base alla descrizione - 
	 * 
	 * @param onlyActive - se "true" seleziona solo le applicazioni attive
	 * @param onlyProtected - se "true" seleziona solo le applicazioni protette
	 * @param useDescription - se "true" usa come descrizione quella di MAF-APPLICATIONS.XML altrimenti il nome dell'applicazione 
	 * @return 
	 */
	protected String getPayerApplicationsXml
	( 
			HttpServletRequest request,
			boolean onlyActive, 
			boolean onlyProtected, 
			boolean useDescription
	)
	{
		String xml = null;
		WebRowSet wrs = null;
		RowSetMetaDataImpl md = null;
		StringWriter swr = null;
		ApplicationData ad = null;
		String applicationName = null;
		String description = null;
		String[] applicazioni = null;
		List<String> lista = null;
		int numAppls = 0;
		ServletContext context = request.getSession(false).getServletContext();
		ApplicationsData applicationsData = (ApplicationsData) context.getAttribute(MAFAttributes.APPLICATIONS);
		if (applicationsData != null)
		{
			lista = new Vector<String>();
			Iterator<String> itr = applicationsData.getApplicationsIterator();
			while (itr.hasNext())
			{
				applicationName = itr.next();
				ad = applicationsData.getApplication(applicationName);
				if (ad != null)
				{
					description = (useDescription ? ad.getDescription() : applicationName);
					description = (description == null || description.equals("") ? applicationName : description);
					if (((onlyActive && ad.isActive()) || !onlyActive) && 
							((onlyProtected && ad.isProtected()) || !onlyProtected))
					{
						lista.add(description.concat("|").concat(applicationName));
						numAppls++;
					}
				}
			}
			if (numAppls > 0)
			{
				applicazioni = getArrayFromList(lista);
				Arrays.sort(applicazioni);
				try {
					wrs = new WebRowSetImpl();
					md = new RowSetMetaDataImpl();
					md.setColumnCount(2);
					md.setColumnName(1, "descrizione");
					md.setColumnName(2, "codice");
					md.setColumnType(1, Types.VARCHAR);
					md.setColumnType(2, Types.VARCHAR);
					wrs.setMetaData(md);
					for(int k = 0; k < numAppls; k++)
					{
						String[] coppia = applicazioni[k].split("\\|");

						wrs.moveToInsertRow();
						wrs.updateString(1, coppia[0]);
						wrs.updateString(2, coppia[1]);
						wrs.insertRow();
					}
					wrs.moveToCurrentRow();
					wrs.beforeFirst();
					swr = new StringWriter();
					wrs.writeXml(swr);
					xml = new String(swr.toString());
					wrs.close();
				} catch (SQLException e) {
					xml = null;
				}
				//inizio LP PG21XX04 Leak
				finally
				{
			    	try {
			    		if(wrs != null) {
			    			wrs.close();
			    		}
			    	} catch (SQLException e) {
			    		e.printStackTrace();
					}
				}
				//fine LP PG21XX04 Leak
			}
		}
		return xml;
	}

	@SuppressWarnings("unchecked")
	protected String getListaParametri(HttpServletRequest request) {
		Enumeration e = request.getParameterNames();
		String p = "";
		int i = 0;
		StringBuilder sb = new StringBuilder();
		while (e.hasMoreElements()) {
			p = (String) e.nextElement();
			sb
			.append("[" + String.valueOf(++i) + "]parametro=" + p + " valore=" + request.getParameter(p) + "\n"); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	protected String getListaAttributi(HttpServletRequest request) {
		Enumeration e = request.getAttributeNames();
		String p = "";
		int i = 0;
		StringBuilder sb = new StringBuilder();
		while (e.hasMoreElements()) {
			p = (String) e.nextElement();
			sb
			.append("(" + String.valueOf(++i) + ")Attributo=" + p + " valore=" + request.getAttribute(p) + "\n"); //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	protected String getSessionAttributes(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		HttpSession session = request.getSession();
		Enumeration e = session.getAttributeNames();
		String p = "";
		int i = 0;
		while (e.hasMoreElements()) {
			p = (String) e.nextElement();
			sb.append("(" + String.valueOf(++i) + ")Attributo=" + p + "\n");
			sb.append(session.getAttribute(p).toString() + "\n\n");
		}
		return sb.toString();
	}

	protected String replyAttributes(boolean replace,
			HttpServletRequest request, String... ignoredStrings) {
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo
		 * dei parameter.
		 */
		ViewStateManager viewStateManager = null;
		String viewStateId = request.getAttribute("vista") == null ? ""
				: request.getAttribute("vista").toString();
		if (!viewStateId.equals("")) {
			viewStateManager = new ViewStateManager(request,viewStateId);
			try {
				viewStateManager.replyAttributes(replace, ignoredStrings);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return viewStateId;
	}

	/**
	 * Verifica se la data in imput corrisponde o meno al 31/12/2099
	 * @param data
	 * @return true se la data in input è il 31/12/2099
	 */
	protected boolean isFine2099(Calendar data)
	{
		if (data == null)return false;
		return((data.get(Calendar.DAY_OF_MONTH) == 31) && (data.get(Calendar.MONTH) == 11) && (data.get(Calendar.YEAR) == 2099 ) );
	}

	/**
	 * Trasforma una istanza di List<String> in una istanza di String[]
	 * @param lista
	 * @return
	 */
	protected String[] getArrayFromList(List<String> lista)
	{
		String[] vettore = null;
		if (lista != null && !lista.isEmpty())
		{
			vettore = new String[lista.size()];
			ListIterator<String> itr = lista.listIterator();
			int i = 0;
			while(itr.hasNext())
			{
				vettore[i] = itr.next();
				i++;
			}
		}
		return vettore;
	}


	public Object service(HttpServletRequest arg0) throws ActionException {
		return null;
	}

	/**
	 * Questo metodo restituisce il campo "text" associato al campo "value" di una DropDownList, 
	 * cercando nel WebRowSet che alimenta la DropDownList.
	 * 
	 * @param xml - WebRowSet in formato XML
	 * @param value - Campo "value" da cercare
	 * @param valueId - Ordinale del campo "value" nel WebRowSet
	 * @param descrId - Ordinale del campo "text" nel WebRowSet
	 * @return Il campo "text" trovato oppure la stringa vuota ("")
	 */
	public String getDdlItemDescription(String xml,String value,int valueId, int descrId)
	{
		String description = "";
		WebRowSet wrs = null;
		StringReader strRdr = null;

		if (xml == null || xml.equals("")) return null;
		try {
			strRdr = new StringReader(xml);
			wrs = new WebRowSetImpl();
			wrs.readXml(strRdr);
			while(wrs.next())
			{
				String itemValue = wrs.getString(valueId);
				if (itemValue != null && itemValue.equals(value))
				{
					description = wrs.getString(descrId);
					break;
				}
			}
			wrs.close();
		} catch (SQLException e) {
			description = null;
		}
		//inizio LP PG21XX04 Leak
		finally
		{
	    	try {
	    		if(wrs != null) {
	    			wrs.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
		return description;
	}

	protected Timestamp toTimestampFromCalendar(Calendar dataInserimento, String formato) {
		
		Timestamp tsInizioChiamata = null; 
		
		if (dataInserimento != null ) {
			try {
				 Date date = new Date();
				if(formato != null && formato.equals("yyyyMMdd000000") ) {
					dataInserimento.set(Calendar.HOUR_OF_DAY, 0);
					dataInserimento.set(Calendar.MINUTE, 0);
					dataInserimento.set(Calendar.SECOND, 0);
					dataInserimento.set(Calendar.MILLISECOND, 0);
					date = dataInserimento.getTime();   
				} else if (formato != null && formato.equals("yyyyMMdd235959") ){
					dataInserimento.set(Calendar.HOUR_OF_DAY, 23);
					dataInserimento.set(Calendar.MINUTE, 59);
					dataInserimento.set(Calendar.SECOND, 59);
					dataInserimento.set(Calendar.MILLISECOND, 999);
					date = dataInserimento.getTime();
				} else {
					date = dataInserimento.getTime();
				}
				
		        tsInizioChiamata = new Timestamp(date.getTime());	
			        
			} catch (Exception  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	    return tsInizioChiamata;
		
	}

	protected String formatDate(Calendar data, String formato)
	{
		String sdata = "";
		if (data != null) 
		{
			SimpleDateFormat sdf = new SimpleDateFormat(formato);
			sdata = sdf.format(data.getTime());
		}
		return sdata;
	}

	protected Calendar parseDate(String data, String formato)
	{
		Calendar cal = new GregorianCalendar(); 
		java.util.Date sdate = null;
		if (data != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(formato);
			try {
				sdate = sdf.parse(data);
			} catch (ParseException e) {
				sdate = new java.util.Date();
			}
			cal.setTime(sdate);
		}
		return cal;
	}

	protected String tsToString(Timestamp ts, String formato)
	{

		String dateString = "Errore in calcolo tsToString()";
		
		if (ts != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(formato);
			dateString = sdf.format( new Date(ts.getTime()));
		}
		
		return dateString;
	}

	protected void  splitDate(HttpServletRequest request, String paramName, String format) {
		String date = request.getParameter(paramName);
		Calendar cal = parseDate(date, format); 

		request.setAttribute(paramName+"_year", cal.get(Calendar.YEAR));
		request.setAttribute(paramName+"_month", cal.get(Calendar.MONTH));
		request.setAttribute(paramName+"_day", cal.get(Calendar.DAY_OF_MONTH));
	}

	/*
	 * Questo metodo verifica se nella
	 * password sono presenti almeno
	 * un carattere maiuscolo, un carattere numerico
	 * ed uno dei caratteri speciali
	 */
	protected boolean verificaComplessitaPWD(String nuovaPWD)
	{
		if (nuovaPWD == null || nuovaPWD.length() < 3) return false;
		boolean bAZ = false;
		boolean b09 = false;
		boolean bSpecial = false;
		final String AZ = "ABCDEFGHIJKLMNOPQRSTUVXYZ";
		final String c09 = "0123456789";
		final String special = ".,!#%";
		char[] password = nuovaPWD.toCharArray();
		for (int i=0; i < password.length; i++)
		{
			if (AZ.indexOf(password[i]) > -1)
			{
				bAZ = true;
				break;
			}
		}
		for (int i=0; i < password.length; i++)
		{
			if (c09.indexOf(password[i]) > -1)
			{
				b09 = true;
				break;
			}
		}
		for (int i=0; i < password.length; i++)
		{
			if (special.indexOf(password[i]) > -1)
			{
				bSpecial = true;
				break;
			}
		}
		return bAZ && b09 && bSpecial;
	}

	/**
	 * 	
	 * Questo metodo, utilizzando una utility del MAF, restituisce una lista
	 * degli "username" che si trovano nello stato "Locked"
	 */
	protected static List<String> getLockedUsers()
	{
		List<String> lockedUsersList = new Vector<String>();
		String username = null;
		BFLContext bflContext = BFLContext.getInstance();
		List<BFLUser> userList = bflContext.show();
		for (BFLUser userItem : userList) 
		{
			if (userItem!=null && userItem.isLocked()) 
			{
				username = userItem.getName().trim();
				lockedUsersList.add(username);
			}
		}
		return lockedUsersList;
	}
	/**
	 * Questa routine costruisce un WebRowSet in formato XML a partire dalla lista degli utenti 
	 * aggiungendo in coda una colonna con l'informazione degli utenti nello stato "locked"
	 * 
	 * @param seXml - Lista degli utenti fornita dal WS
	 * @param lockedUsers - Lista degli utenti nello stato locked
	 * @param jspOrder - Individua l'odinamento
	 * @return
	 */
	protected static String addLockedUsersColumn(String seXml,List<String> lockedUsers, String jspOrder)
	{
		//StringWriter swr = null;
		String xml = null;
		WebRowSet seWrs = null;
		WebRowSet seTemp = null;
		StringReader seStrRdr = null;
		RowSetMetaDataImpl mdSeTemp = null;
		RowSetMetaDataImpl mdSeWrs = null;
		//int userIndex = (jspOrder.equals("SE") ? 5 : 4);
		//int userIndex = 11;
		int ncol = 0;
		try {
			seStrRdr = new StringReader(seXml);
			seTemp = new WebRowSetImpl();
			seTemp.readXml(seStrRdr);
			seWrs = new WebRowSetImpl();
			mdSeWrs = new RowSetMetaDataImpl();
			mdSeTemp = (RowSetMetaDataImpl) seTemp.getMetaData();
			ncol = mdSeTemp.getColumnCount();
			mdSeWrs.setColumnCount(ncol+1);
			/*
			 * Clono i metadati e aggiungo 
			 * una colonna in coda
			 */
			for (int i=0; i < ncol; i++)
			{
				mdSeWrs.setColumnName(i+1, mdSeTemp.getColumnName(i+1));
				mdSeWrs.setColumnType(i+1, mdSeTemp.getColumnType(i+1));
				mdSeWrs.setColumnTypeName(i+1, mdSeTemp.getColumnTypeName(i+1));
			}
			mdSeWrs.setColumnName(ncol+1, "locked");
			mdSeWrs.setColumnType(ncol+1, Types.INTEGER);
			mdSeWrs.setColumnTypeName(ncol+1, "NUMBER");
			seWrs.setMetaData(mdSeWrs);
			/*
			 * Copio i dati inserendo
			 * il valore per la nuova colonna
			 */
			while (seTemp.next())
			{
				seWrs.moveToInsertRow();
				seWrs.updateInt(ncol+1, lockedUsers.contains(seTemp.getString(5)) ? 1 : 0);
				for (int i = 0; i < ncol; i++)
				{
					seWrs.updateObject(i+1, seTemp.getObject(i+1));
				}
				seWrs.insertRow();
			}
			seWrs.moveToCurrentRow();

			xml = Convert.webRowSetToString(seWrs);

			//swr = new StringWriter();
			//seWrs.writeXml(swr);
			//xml = new String(swr.toString());

		} catch (SQLException e) {
			xml = null;
		}
		//inizio LP PG21XX04 Leak
		finally
		{
	    	try {
	    		if(seWrs != null) {
	    			seWrs.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
	    	try {
	    		if(seTemp != null) {
	    			seTemp.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
		return xml;
	}

	/**
	 * Questa routine aggiunge al WebRowSet del riepilogo statistico
	 * il counter degli utenti nello stato "locked"
	 * 
	 * @param seXml - Riepilogo statistico fornito dal WS 
	 * @param counter - Counter del numero di utenti locked
	 * @return
	 */
	protected static String addLockedUsersCounter(String seXml,int counter)
	{
		StringWriter swr = null;
		String xml = null;
		WebRowSet seWrs = null;
		StringReader seStrRdr = null;
		try {
			seStrRdr = new StringReader(seXml);
			seWrs = new WebRowSetImpl();
			seWrs.readXml(seStrRdr);
			/*
			 * Inserisco la nuova riga con il counter
			 */
			seWrs.afterLast();
			seWrs.moveToInsertRow();
			seWrs.updateString(1, "Bloccate");
			seWrs.updateInt(2, counter);
			seWrs.insertRow();
			seWrs.moveToCurrentRow();
			swr = new StringWriter();
			seWrs.writeXml(swr);
			xml = new String(swr.toString());
			seWrs.close();

		} catch (SQLException e) {
			xml = null;
		}
		//inizio LP PG21XX04 Leak
		finally
		{
	    	try {
	    		if(seWrs != null) {
	    			seWrs.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
		return xml;
	}

	/**
	 * Questa routine recupera i parametri dal form di inserimento o modifica
	 * per la tabella PYRESTB - Effettua inoltre il set delle 2 checkbox e del
	 * parametro "valueselected" della dropdownlist perchè in caso di "edit" è disabilitata - 
	 * 
	 * Nel caso della modifica i valori di Societa, Utente, e Tipologia Servizio
	 * non vengono recuperati dalla drop-down-list ma dai campi hidden che sono stati settati
	 * all'atto del caricamento del record da modificare
	 * 
	 * @param request
	 * @param codop - "add" o "edit"
	 * @return
	 */
	protected HashMap<String,Object> getRangeAbiUtenteTipoServizioParameters(HttpServletRequest request, String codop)
	{
		HashMap<String,Object> param = new HashMap<String, Object>();
		String codiceSocieta = "";
		String codiceUtente = "";
		String codiceTipologiaServizio = "";
		/*
		 * INSERIMENTO
		 */
		if (codop.equals("add"))
		{
			/*
			 * Prendo i parametri dalla DDL
			 */
			String strConfigutentetiposervizios = (String)request.getAttribute("rangeabiutentetiposervizio_strConfigutentetiposervizios");
			if (!strConfigutentetiposervizios.equals(""))
			{
				String[] codici = strConfigutentetiposervizios.split("\\|");
				codiceSocieta = codici[0];
				codiceUtente = codici[1];
				codiceTipologiaServizio = codici[2];
			}
		}
		/*
		 * MODIFICA
		 */
		else
		{
			/*
			 * Prendo i valori dai campi hidden che sono stati
			 * valorizzati dal WS e setto il parametro "valueselected" della dropdownlist
			 */
			codiceSocieta = (request.getAttribute("tx_societa") == null ? "" : (String)request.getAttribute("tx_societa"));
			codiceUtente = (request.getAttribute("tx_utente") == null ? "" : (String)request.getAttribute("tx_utente"));
			codiceTipologiaServizio = (request.getAttribute("tx_codiceTipologiaServizio") == null ? "" : (String)request.getAttribute("tx_codiceTipologiaServizio"));
		}
		String inizioRangeDa = (request.getAttribute("rangeabiutentetiposervizio_inizioRangeDa") == null ? "" : (String)request.getAttribute("rangeabiutentetiposervizio_inizioRangeDa"));
		String inizioRangePer = (request.getAttribute("rangeabiutentetiposervizio_inizioRangePer") == null ? "" : (String)request.getAttribute("rangeabiutentetiposervizio_inizioRangePer"));
		String fineRangeA = (request.getAttribute("rangeabiutentetiposervizio_fineRangeA") == null ? "" : (String)request.getAttribute("rangeabiutentetiposervizio_fineRangeA"));
		String tipoRange = (request.getAttribute("rangeabiutentetiposervizio_tipoRange") == null ? "" : (String)request.getAttribute("rangeabiutentetiposervizio_tipoRange"));
		String flagCin = (request.getAttribute("rangeabiutentetiposervizio_flagCin") == null ? "" : (String)request.getAttribute("rangeabiutentetiposervizio_flagCin"));

		param.put("codiceSocieta", codiceSocieta);
		param.put("codiceUtente", codiceUtente);
		param.put("codiceTipologiaServizio", codiceTipologiaServizio);
		param.put("inizioRangeDa", inizioRangeDa);
		param.put("inizioRangePer", inizioRangePer);
		param.put("fineRangeA", fineRangeA);
		param.put("tipoRange", tipoRange);
		param.put("flagCin", flagCin);

		return param;
	}



	/**
	 * Questa routine recupera i parametri dal form di inserimento o modifica
	 * per la tabella PYRESTB - Effettua inoltre il set delle 2 checkbox e del
	 * parametro "valueselected" della dropdownlist perchè in caso di "edit" è disabilitata - 
	 * 
	 * Nel caso della modifica i valori di Societa, Utente, e Tipologia Servizio
	 * non vengono recuperati dalla drop-down-list ma dai campi hidden che sono stati settati
	 * all'atto del caricamento del record da modificare
	 * 
	 * @param request
	 * @param codop - "add" o "edit"
	 * @return
	 */
	protected HashMap<String,Object> getConfRendUtenteServizioFormParameters(HttpServletRequest request, String codop)
	{
		HashMap<String,Object> param = new HashMap<String, Object>();
		String codiceSocieta = "";
		String codiceUtente = "";
		String codiceTipologiaServizio = "";
 		String template = getTemplateCurrentApplication(request, request.getSession());
		/*
		 * INSERIMENTO
		 */
		if (codop.equals("add"))
		{
			/*
			 * Prendo i parametri dalla DDL
			 */
			String ddlSocietaUtenteServizioEnte = (String)request.getAttribute("ddlSocietaUtenteServizio");
			if (!ddlSocietaUtenteServizioEnte.equals(""))
			{
				String[] codici = ddlSocietaUtenteServizioEnte.split("\\|");
				codiceSocieta = codici[0];
				codiceUtente = codici[1];
				codiceTipologiaServizio = codici[2];
			}
		}
		/*
		 * MODIFICA
		 */
		else
		{
			/*
			 * Prendo i valori dai campi hidden che sono stati
			 * valorizzati dal WS e setto il parametro "valueselected" della dropdownlist
			 */
			codiceSocieta = (request.getAttribute("tx_societa") == null ? "" : (String)request.getAttribute("tx_societa"));
			codiceUtente = (request.getAttribute("tx_utente") == null ? "" : (String)request.getAttribute("tx_utente"));
			codiceTipologiaServizio = (request.getAttribute("tx_codiceTipologiaServizio") == null ? "" : (String)request.getAttribute("tx_codiceTipologiaServizio"));
			String ddlSocietaUtenteServizioEnte = codiceSocieta.concat("|").concat(codiceUtente).concat("|").concat(codiceTipologiaServizio);
			request.setAttribute("ddlSocietaUtenteServizio", ddlSocietaUtenteServizioEnte);
		}
		String invioEmail = (request.getAttribute("invioEmail") == null ? "N" : "Y");
		String invioFtp = "";
		String formatoFileRend = (request.getAttribute("formatoFileRend") == null ? "" : (String)request.getAttribute("formatoFileRend"));
		String rendicontazioneSeda = (request.getAttribute("rendicontazioneSeda") == null ? "N" : "Y"); //PG110260
		if (request.getAttribute("senzaCarico") != null && request.getAttribute("senzaCarico").equals("Y") && !template.equals("aosta")){
			invioFtp = "Y";
			rendicontazioneSeda = "Y";
		} else {
			invioFtp = (request.getAttribute("invioFtp") == null ? "N" : "Y");
		}
		
		if(rendicontazioneSeda.equals("Y")) {
			formatoFileRend = "TXT";
		}
		
//		String rendicontazioneSeda = (request.getAttribute("rendicontazioneSeda") == null ? "N" : "Y"); //PG110260
		String emailDestinatario = (request.getAttribute("emailDestinatario") == null ? "" : (String)request.getAttribute("emailDestinatario"));
		String emailCCN = (request.getAttribute("emailCCN") == null ? "" : (String)request.getAttribute("emailCCN"));
		String emailMittente = (request.getAttribute("emailMittente") == null ? "" : (String)request.getAttribute("emailMittente"));
		String descrizioneMittente = (request.getAttribute("descrizioneMittente") == null ? "" : (String)request.getAttribute("descrizioneMittente"));
		String number = (String)request.getAttribute("emailAttachMaxSizeKb");
		int emailAttachMaxSizeKb = ((number == null || number.equals(""))? 0 : Integer.parseInt(number));
		String serverFtp = (request.getAttribute("serverFtp") == null ? "" : (String)request.getAttribute("serverFtp"));
		String utenteFtp = (request.getAttribute("utenteFtp") == null ? "" : (String)request.getAttribute("utenteFtp"));
		String passwordFtp = (request.getAttribute("passwordFtp") == null ? "" : (String)request.getAttribute("passwordFtp"));
		String directoryFtp = (request.getAttribute("directoryFtp") == null ? "" : (String)request.getAttribute("directoryFtp"));
		String senzaCarico = (request.getAttribute("senzaCarico") == null ? "N" : "Y"); 
		String ente = (request.getAttribute("ente") == null ? "" : (String)request.getAttribute("ente"));
		String servizio = (request.getAttribute("servizio") == null ? "" : (String)request.getAttribute("servizio"));
		String numDocCodContrib = (request.getAttribute("codContrib") == null ? "N" : "Y");
		String codTributo = (request.getAttribute("codTributo") == null ? "" : (String)request.getAttribute("codTributo"));		//PG180260_001 GG 04122018
		String invioWebService = (request.getAttribute("invioWebService") == null ? "N" : (String)request.getAttribute("invioWebService"));
		String urlWebService = (request.getAttribute("urlWebService") == null ? "" : (String)request.getAttribute("urlWebService"));
		String utenteWebService = (request.getAttribute("utenteWebService") == null ? "" : (String)request.getAttribute("utenteWebService"));
		String passwordWebService = (request.getAttribute("passwordWebService") == null ? "" : (String)request.getAttribute("passwordWebService"));
		String flagTrcComandiPolizia = (request.getAttribute("flagTrcComandiPolizia") == null ? "N" : "Y");	//PG200280
		String rendicontazioneQuattrocento = (request.getAttribute("rendquattrocento") == null ? "N" : "Y");
		
		param.put("codiceSocieta", codiceSocieta);
		param.put("codiceUtente", codiceUtente);
		param.put("codiceTipologiaServizio", codiceTipologiaServizio);
		param.put("invioEmail", invioEmail);
		param.put("invioFtp", invioFtp);
		param.put("rendicontazioneSeda", rendicontazioneSeda);  //PG110260
		param.put("emailDestinatario", emailDestinatario);
		param.put("emailCCN", emailCCN);
		param.put("emailMittente", emailMittente);
		param.put("descrizioneMittente", descrizioneMittente);
		param.put("emailAttachMaxSizeKb", emailAttachMaxSizeKb);
		param.put("serverFtp", serverFtp);
		param.put("utenteFtp", utenteFtp);
		param.put("passwordFtp", passwordFtp);
		param.put("directoryFtp", directoryFtp);
		param.put("senzaCarico", senzaCarico);
		param.put("ente", ente);
		param.put("servizio", servizio);
		param.put("numDocCodContrib", numDocCodContrib);
		param.put("codTributo", codTributo);	//PG180260_001 GG 04122018
		param.put("invioWebService", invioWebService);
		param.put("formatoFileRend", formatoFileRend);
		param.put("urlWebService", urlWebService);
		param.put("utenteWebService", utenteWebService);
		param.put("passwordWebService", passwordWebService);
		param.put("flagTrcComandiPolizia", flagTrcComandiPolizia);	//PG200280
		param.put("rendquattrocento", rendicontazioneQuattrocento);

		/*
		 * Setto le tre check-box di invio email , invio ftp e invio webservice
		 */
		request.setAttribute("chk_invioEmail",  invioEmail.equals("Y"));
		request.setAttribute("chk_invioFtp",  invioFtp.equals("Y"));
		request.setAttribute("chk_invioWebService",  invioWebService.equals("Y"));
		request.setAttribute("chk_senzaCarico",  senzaCarico.equals("Y"));
		request.setAttribute("chk_codContrib",  numDocCodContrib.equals("Y"));
		request.setAttribute("rendquattrocento",  rendicontazioneQuattrocento.equals("Y"));
		
		request.setAttribute("chk_flagTrcComandiPolizia",  flagTrcComandiPolizia.equals("Y"));	//PG200280
		
		//aggiunta PG110260
		request.setAttribute("chk_rendicontazioneSeda",  rendicontazioneSeda.equals("Y"));
		if ( rendicontazioneSeda.equals("Y") ) {
			request.setAttribute("chk_invioEmail",  false ); 
			request.setAttribute("disabled_invioEmail",  true ); 

			request.setAttribute("chk_invioFtp",  true ); 	//anomalia PG110260
			request.setAttribute("disabled_invioFtp",  true ); //anomalia PG110260
			
			request.setAttribute("chk_invioWebService",  false ); 	// PG180010
			request.setAttribute("disabled_invioWebService",  true ); // PG180010
			request.setAttribute("rendquattrocento",  false ); // PG180010
			
//			//PG200280 GG - inizio
//			request.setAttribute("chk_flagTrcComandiPolizia",  false ); 
//			request.setAttribute("disabled_flagTrcComandiPolizia",  true );
//			//PG200280 GG - fine

		} else{
			request.setAttribute("disabled_invioEmail",  false ); 
			request.setAttribute("disabled_invioFtp",  false ); //anomalia PG110260 
			request.setAttribute("disabled_invioWebService",  false ); // PG180010
//			request.setAttribute("disabled_flagTrcComandiPolizia",  false );	//PG200280
		}
		
		//Fine aggiunta PG110260
		
		

		return param;
	}

	/**
	 * Questa routine recupera i parametri dal form di inserimento o modifica
	 * per la tabella PYREETB - Effettua inoltre il set delle 2 checkbox e del
	 * parametro "valueselected" della dropdownlist perchè in caso di "edit" è disabilitata - 
	 * 
	 * Nel caso della modifica i valori di Societa, Utente, Ente e Tipologia Servizio
	 * non vengono recuperati dalla drop-down-list ma dai campi hidden che sono stati settati
	 * all'atto del caricamento del record da modificare
	 * 
	 * @param request
	 * @param codop - "add" o "edit"
	 * @return
	 */
	protected HashMap<String,Object> getConfRendUtenteServizioEnteFormParameters(HttpServletRequest request, String codop)
	{
		HashMap<String,Object> param = new HashMap<String, Object>();
		String codiceSocieta = "";
		String codiceUtente = "";
		String chiaveEnte = "";
		String codiceTipologiaServizio = "";
		String template = getTemplateCurrentApplication(request, request.getSession());
		/*
		 * INSERIMENTO
		 */
		if (codop.equals("add"))
		{
			/*
			 * Prendo i parametri dalla DDL
			 */
			String ddlSocietaUtenteServizioEnte = (String)request.getAttribute("ddlSocietaUtenteServizioEnte");
			if (!ddlSocietaUtenteServizioEnte.equals(""))
			{
				String[] codici = ddlSocietaUtenteServizioEnte.split("\\|");
				codiceSocieta = codici[0];
				codiceUtente = codici[1];
				chiaveEnte = codici[2];
				codiceTipologiaServizio = codici[3];
			}
		}
		/*
		 * MODIFICA
		 */
		else
		{
			/*
			 * Prendo i valori dai campi hidden che sono stati
			 * valorizzati dal WS e setto il parametro "valueselected" della dropdownlist
			 */
			codiceSocieta = (request.getAttribute("tx_societa") == null ? "" : (String)request.getAttribute("tx_societa"));
			codiceUtente = (request.getAttribute("tx_utente") == null ? "" : (String)request.getAttribute("tx_utente"));
			chiaveEnte = (request.getAttribute("tx_ente") == null ? "" : (String)request.getAttribute("tx_ente"));
			codiceTipologiaServizio = (request.getAttribute("tx_codiceTipologiaServizio") == null ? "" : (String)request.getAttribute("tx_codiceTipologiaServizio"));
			String ddlSocietaUtenteServizioEnte = codiceSocieta.concat("|").concat(codiceUtente).concat("|").concat(chiaveEnte).concat("|").concat(codiceTipologiaServizio);
			request.setAttribute("ddlSocietaUtenteServizioEnte", ddlSocietaUtenteServizioEnte);
		}
		String invioEmail = (request.getAttribute("invioEmail") == null ? "N" : "Y");
		String rendicontazioneSeda = (request.getAttribute("rendicontazioneSeda") == null ? "N" : "Y"); //PG110260
		String formatoFileRend = (request.getAttribute("formatoFileRend") == null ? "" : (String)request.getAttribute("formatoFileRend"));
		String tracciatoQuattrocento = (request.getAttribute("rendquattrocento") == null ? "N" : "Y");

		String invioFtp = "";
		if (request.getAttribute("senzaCarico") != null && request.getAttribute("senzaCarico").equals("Y") && !template.equals("aosta")){
			invioFtp = "Y";
			rendicontazioneSeda = "Y";
		} else {
			/*SVILUPPO_001_LUCAP_01042021
			if(request.getAttribute("invioFtp") == null) {
				invioFtp = (request.getParameter("invioFtpHidden") == null ? "N" : "Y");
			} else {
				invioFtp = (request.getAttribute("invioFtp") == null ? "N" : "Y");
			}
			FINE SVILUPPO_001_LUCAP_01042021*/
			invioFtp = (request.getAttribute("invioFtp") == null ? "N" : "Y");			
		}
		
		/* SVILUPPO_001_LUCAP_01042021
		if(rendicontazioneSeda.equals("Y")){
			formatoFileRend = "TXT";
		}*/

//		String rendicontazioneSeda = (request.getAttribute("rendicontazioneSeda") == null ? "N" : "Y"); //PG110260
		String emailDestinatario = (request.getAttribute("emailDestinatario") == null ? "" : (String)request.getAttribute("emailDestinatario"));
		String emailCCN = (request.getAttribute("emailCCN") == null ? "" : (String)request.getAttribute("emailCCN"));
		String emailMittente = (request.getAttribute("emailMittente") == null ? "" : (String)request.getAttribute("emailMittente"));
		String descrizioneMittente = (request.getAttribute("descrizioneMittente") == null ? "" : (String)request.getAttribute("descrizioneMittente"));
		String number = (String)request.getAttribute("emailAttachMaxSizeKb");
		int emailAttachMaxSizeKb = ((number == null || number.equals(""))? 0 : Integer.parseInt(number));
		String serverFtp = (request.getAttribute("serverFtp") == null ? "" : (String)request.getAttribute("serverFtp"));
		String utenteFtp = (request.getAttribute("utenteFtp") == null ? "" : (String)request.getAttribute("utenteFtp"));
		String passwordFtp = (request.getAttribute("passwordFtp") == null ? "" : (String)request.getAttribute("passwordFtp"));
		String directoryFtp = (request.getAttribute("directoryFtp") == null ? "" : (String)request.getAttribute("directoryFtp"));
		String senzaCarico = (request.getAttribute("senzaCarico") == null ? "N" : "Y"); 
		String ente = (request.getAttribute("ente") == null ? "" : (String)request.getAttribute("ente"));
		String servizio = (request.getAttribute("servizio") == null ? "" : (String)request.getAttribute("servizio"));
		String numDocCodContrib = (request.getAttribute("codContrib") == null ? "N" : "Y"); 
		String codTributo = (request.getAttribute("codTributo") == null ? "" : (String)request.getAttribute("codTributo"));		//PG180260_001 GG 04122018
		String invioWebService = (request.getAttribute("invioWebService") == null ? "N" : (String)request.getAttribute("invioWebService"));
		String urlWebService = (request.getAttribute("urlWebService") == null ? "" : (String)request.getAttribute("urlWebService"));
		String utenteWebService = (request.getAttribute("utenteWebService") == null ? "" : (String)request.getAttribute("utenteWebService"));
		String passwordWebService = (request.getAttribute("passwordWebService") == null ? "" : (String)request.getAttribute("passwordWebService"));
		//inizio LP PG200060
		String passwordZip = (request.getAttribute("passwordZip") == null ? "" : (String)request.getAttribute("passwordZip"));
		//fine LP PG200060
		String flagTrcComandiPolizia = (request.getAttribute("flagTrcComandiPolizia") == null ? "N" : "Y");	//PG200280
		
		param.put("codiceSocieta", codiceSocieta);
		param.put("codiceUtente", codiceUtente);
		param.put("chiaveEnte", chiaveEnte);
		param.put("codiceTipologiaServizio", codiceTipologiaServizio);
		param.put("invioEmail", invioEmail);
		param.put("invioFtp", invioFtp);
		param.put("rendicontazioneSeda", rendicontazioneSeda);  //PG110260
		param.put("emailDestinatario", emailDestinatario);
		param.put("emailCCN", emailCCN);
		param.put("emailMittente", emailMittente);
		param.put("descrizioneMittente", descrizioneMittente);
		param.put("emailAttachMaxSizeKb", emailAttachMaxSizeKb);
		param.put("serverFtp", serverFtp);
		param.put("utenteFtp", utenteFtp);
		param.put("passwordFtp", passwordFtp);
		param.put("directoryFtp", directoryFtp);
		param.put("senzaCarico", senzaCarico);
		param.put("ente", ente);
		param.put("servizio", servizio);
		param.put("numDocCodContrib", numDocCodContrib);
		param.put("codTributo", codTributo);	//PG180260_001 GG 04122018
		param.put("invioWebService", invioWebService);
		param.put("formatoFileRend", formatoFileRend);
		param.put("urlWebService", urlWebService);
		param.put("utenteWebService", utenteWebService);
		param.put("passwordWebService", passwordWebService);
		//inizio LP PG200060
		param.put("passwordZip", passwordZip);
		//fine LP PG200060
		param.put("flagTrcComandiPolizia", flagTrcComandiPolizia);	//PG200280
		param.put("tracciatoQuattrocento", tracciatoQuattrocento);
		
		/*
		 * Setto le tre check-box di invio email , invio ftp e invio webservice
		 */
		request.setAttribute("chk_invioEmail",  invioEmail.equals("Y"));
		request.setAttribute("chk_invioFtp",  invioFtp.equals("Y"));
		request.setAttribute("chk_invioWebService",  invioWebService.equals("Y"));
		
		request.setAttribute("chk_senzaCarico",  senzaCarico.equals("Y"));
		request.setAttribute("chk_codContrib",  numDocCodContrib.equals("Y"));
		
		request.setAttribute("chk_flagTrcComandiPolizia",  flagTrcComandiPolizia.equals("Y"));	//PG200280
		
		if ( senzaCarico.equals("Y") ) {
			request.setAttribute("disableCarico", false);
		} else {
			request.setAttribute("disableCarico", true);
			request.setAttribute("ente", "");
			request.setAttribute("servizio", "");
			request.setAttribute("codTributo", "");		//PG180260_001 GG 04122018	//TODO da verificare
		}

		//aggiunta PG110260
		request.setAttribute("chk_rendicontazioneSeda",  rendicontazioneSeda.equals("Y")); 
		if ( rendicontazioneSeda.equals("Y") ) {
			request.setAttribute("chk_invioEmail",  false ); 
			request.setAttribute("disabled_invioEmail",  true ); 

			request.setAttribute("chk_invioFtp",  true ); 	//anomalia PG110260
			request.setAttribute("disabled_invioFtp",  true ); //anomalia PG110260
			
			request.setAttribute("chk_invioWebService",  false ); 
			request.setAttribute("disabled_invioWebService",  true );
			
			
//			//PG200280 GG - inizio
//			request.setAttribute("chk_flagTrcComandiPolizia",  false ); 
//			request.setAttribute("disabled_flagTrcComandiPolizia",  true );
//			//PG200280 GG - fine

		} else{
			request.setAttribute("disabled_invioEmail",  false );  
			request.setAttribute("disabled_invioFtp",  false ); //anomalia PG110260  
			request.setAttribute("disabled_invioWebService",  false ); 
//			request.setAttribute("disabled_flagTrcComandiPolizia",  false );	//PG200280
			request.setAttribute("rendquattrocento", tracciatoQuattrocento.equals("Y"));

		}
		//Fine aggiunta PG110260
		
		
		return param;
	}

	/**
	 * Questa routine effettua una validazione "applicativa" - 
	 * 
	 * @param parametri : l'HashMap che contiene i parametri di input della form da validare - 
	 * @return : "OK" se la validazione ha avuto esito positivo, altrimenti il messaggio di errore
	 * da visualizzare all'utente
	 */
	protected String checkInputConfRend (HashMap<String,Object> parametri, HttpServletRequest request)
	{
		String errMsg = ""; 
		/*
		 * Controllo che sia stato selezionato un item dalla drop-down-list
		 */
		if (((String)parametri.get("codiceSocieta")).equals(""))
		{
			errMsg += errMsg.equals("") ? Messages.SELEZIONARE_UN_ELEMENTO.format() :"<BR/>" + Messages.SELEZIONARE_UN_ELEMENTO.format(); 
		}
		/*
		 * Controllo comunque che il campo "Email CCN", se presente, sia
		 * una sequenza di indirizzi separati da ";"
		 */
		//if (!((String)parametri.get("emailCCN")).equals("") 
		/*&& !checkEmailList((String)parametri.get("emailCCN")))*/
		//errMsg += errMsg.equals("") ? Messages.SPECIFICA_LISTA_EMAIL_CORRETTA.format("Email CCN") : "<BR/>" +  Messages.SPECIFICA_LISTA_EMAIL_CORRETTA.format("Email CCN");

		//inizio LP PG200060
		HttpSession session = request.getSession();
		String template = getTemplateCurrentApplication(request,session);
		//fine LP PG200060

		/*
		 * PG110260
		 * Se è fleggata la rendicontazione Seda l'invio mail deve essere disabilitato
		 */
		if( ((String)parametri.get("rendicontazioneSeda") ).equals("Y")) {
			if(((String)parametri.get("invioEmail")).equals("Y")) {
				errMsg += errMsg.equals("") ? Messages.INVIO_MAIL_NON_CONSENTITO.format() : "<BR/>" +  Messages.INVIO_MAIL_NON_CONSENTITO.format();
			}
			
			//inizio LP PG200060
			if (!template.equals("regmarche")) {
			//fine LP PG200060
			if(((String)parametri.get("invioWebService")).equals("Y")) {
				errMsg += errMsg.equals("") ? Messages.INVIO_MAIL_NON_CONSENTITO.format() : "<BR/>" +  Messages.INVIO_MAIL_NON_CONSENTITO.format();
			}
			//inizio LP PG200060
			}
			//fine LP PG200060

			//anomalia PG110260
			if(((String)parametri.get("invioFtp")).equals("N")) {
				errMsg += errMsg.equals("") ? Messages.INVIO_FTP_OBBLIGATORIO.format() : "<BR/>" +  Messages.INVIO_FTP_OBBLIGATORIO.format();
			}
		}
		
		//inizio LP PG200060
		/*
		HttpSession session = request.getSession();
		String template = getTemplateCurrentApplication(request,session);
		*/
		//fine LP PG200060
		if (template.equals("aosta")) {
			if(((String)parametri.get("formatoFileRend")).equals("")) {
				errMsg += errMsg.equals("") ? Messages.SPECIFICA_IL_CAMPO.format("Formato file") : "<BR/>" +  Messages.SPECIFICA_IL_CAMPO.format("Formato file");
			}
		}

		/*
		 * Se è settato il flag di abilitazione invio email
		 * faccio i controlli relativi
		 */
		else if(((String)parametri.get("invioEmail")).equals("Y")) 
		{

			if (((String)parametri.get("emailDestinatario")).equals(""))
				errMsg += errMsg.equals("") ? Messages.SPECIFICA_IL_CAMPO.format("Email Dastinatario") : "<BR/>" +  Messages.SPECIFICA_IL_CAMPO.format("Email Dastinatario");
				/*else
			{
				if (!checkEmailList((String)parametri.get("emailDestinatario")))
					errMsg += errMsg.equals("") ? Messages.SPECIFICA_LISTA_EMAIL_CORRETTA.format("Email Destinatario") : "<BR/>" +  Messages.SPECIFICA_LISTA_EMAIL_CORRETTA.format("Email Destinatario");
			}
				 */
				if (((String)parametri.get("emailMittente")).equals(""))
					errMsg += errMsg.equals("") ? Messages.SPECIFICA_IL_CAMPO.format("Email Mittente") : "<BR/>" +  Messages.SPECIFICA_IL_CAMPO.format("Email Mittente");
					if (((String)parametri.get("descrizioneMittente")).equals(""))
						errMsg += errMsg.equals("") ? Messages.SPECIFICA_IL_CAMPO.format("Descrizione Mittente") : "<BR/>" +  Messages.SPECIFICA_IL_CAMPO.format("Descrizione Mittente");
						if (((Integer)parametri.get("emailAttachMaxSizeKb")) == 0)
							errMsg += errMsg.equals("") ? Messages.SPECIFICA_UN_VALORE.format("maggiore di 0","MaxSize Allegato") : "<BR/>" +  Messages.SPECIFICA_UN_VALORE.format("maggiore di 0","MaxSize Allegato");
		}
		/*
		 * Se è settato il flag di abilitazione invio ftp
		 * faccio i controlli relativi
		 */
		if(((String)parametri.get("invioFtp")).equals("Y"))
		{
			if (((String)parametri.get("serverFtp")).equals(""))
				errMsg += errMsg.equals("") ? Messages.SPECIFICA_IL_CAMPO.format("Server FTP") : "<BR/>" +  Messages.SPECIFICA_IL_CAMPO.format("Server FTP");
		}

		//inizio LP PG200060
		if (!template.equals("regmarche")) {
		//fine LP PG200060
		/*
		 * Se è settato il flag di flusso senza carico
		 * faccio i controlli relativi
		 */
		if(((String)parametri.get("senzaCarico")).equals("Y"))
		{
			if (((String)parametri.get("ente")).trim().equals("")) 
				errMsg += errMsg.equals("") ? Messages.SPECIFICA_IL_CAMPO.format("Ente") : "<BR/>" +  Messages.SPECIFICA_IL_CAMPO.format("Ente");				
			if (((String)parametri.get("servizio")).trim().equals(""))
				errMsg += errMsg.equals("") ? Messages.SPECIFICA_IL_CAMPO.format("Imposta Servizio") : "<BR/>" +  Messages.SPECIFICA_IL_CAMPO.format("Imposta Servizio");
//			//PG180260_001 GG 04122018 - inizio
//			if (((String)parametri.get("codTributo")).equals(""))
//				errMsg += errMsg.equals("") ? Messages.SPECIFICA_IL_CAMPO.format("Cod. Tributo") : "<BR/>" +  Messages.SPECIFICA_IL_CAMPO.format("Cod. Tributo");
//			//PG180260_001 GG 04122018 - fine	
		}


		if(((String)parametri.get("invioWebService")).equals("Y"))
		{
			if (((String)parametri.get("urlWebService")).equals(""))
				errMsg += errMsg.equals("") ? Messages.SPECIFICA_IL_CAMPO.format("URL web service") : "<BR/>" +  Messages.SPECIFICA_IL_CAMPO.format("URL web service");
		}
		//inizio LP PG200060
		}
		//fine LP PG200060


		if(errMsg.equals(""))
		{
			return "OK";
		}else
		{
			return errMsg;
		}


	}

	/**
	 * Questa routine effettua una validazione "applicativa" - 
	 * 
	 * @param parametri : l'HashMap che contiene i parametri di input della form da validare - 
	 * @return : "OK" se la validazione ha avuto esito positivo, altrimenti il messaggio di errore
	 * da visualizzare all'utente
	 */
	protected String checkInputRangeAbiUtenteServizio (HashMap<String,Object> parametri )
	{


		String inizioRangeDa = (String)parametri.get("inizioRangeDa");
		String inizioRangePer = (String)parametri.get("inizioRangePer");
		String fineRangeA = (String)parametri.get("fineRangeA");
		String tipoRange = (String)parametri.get("tipoRange");
		String flagCin = (String)parametri.get("flagCin");

		/*
		 * Controllo che solo per i bollettini è possibile 
		 * prevedere il Codice CIN
		 */
		if (((tipoRange.equals("D")) || (tipoRange.equals("V"))) && 
				(flagCin.equals("Y")))
			return Messages.VALORIZZAZIONE_NON_PREVISTA_CIN.format("");

		/*
		 * Controllo che se valorizzato il Range Per, non deveno essere
		 * valorizzati gli altri Reange
		 */
		if ((inizioRangePer.trim().length() > 0) && 
				((inizioRangeDa.trim().length() > 0 ) || (fineRangeA.trim().length() > 0 ))
		)
			return Messages.VALORIZZAZIONE_RANGE_DA_A.format("");

		/*
		 * Controllo che se valorizzato il Range Da A , non deve essere
		 * valorizzato il Reange Per
		 */
		if (  
				((inizioRangeDa.trim().length() > 0 ) && (fineRangeA.trim().length() > 0 )) &&
				(inizioRangePer.trim().length() > 0)
		)
			return Messages.VALORIZZAZIONE_RANGE_PER.format("");

		/*
		 * Se valorizzato il Range Da A allora il Range DA deve essere minore del
		 * Range A
		 */
		if (inizioRangeDa.trim().compareTo(fineRangeA.trim()) > 0 ) {
			return Messages.VALORIZZAZIONE_RANGE_DA_A.format("");
		}

		return "OK";
	}


	/**
	 * Questa routine restituisce il bean necessario al WS per le operazioni di
	 * inserimento e modifica della tabella PYRESTB
	 * 
	 * @param p - parametri di input della request
	 * @param session
	 * @return
	 */
	//inizio LP PG200060
	//protected ConfRendUtenteServizioType getConfRendUtenteServizioBean(HashMap<String,Object> p, HttpSession session)
	protected ConfRendUtenteServizioType getConfRendUtenteServizioBean(HashMap<String,Object> p, HttpSession session, String template)
	//fine LP PG200060
	{
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		ConfRendUtenteServizioType bean = new ConfRendUtenteServizioType();
		bean.setCodiceSocieta((String) p.get("codiceSocieta"));
		bean.setCodiceUtente((String) p.get("codiceUtente"));
		bean.setTipologiaServizio((String) p.get("codiceTipologiaServizio"));
		bean.setFlagAbilitazioneInvioEmail((String) p.get("invioEmail"));
		bean.setFlagAbilitazioneInvioFtp((String) p.get("invioFtp"));
		bean.setEmailDestinatario((String) p.get("emailDestinatario"));
		bean.setEmailConoscenzaNascosta((String) p.get("emailCCN"));
		bean.setEmailMittente((String) p.get("emailMittente"));
		bean.setDescrizioneMittente((String) p.get("descrizioneMittente"));
		bean.setMaxAttachSizeKb((Integer)p.get("emailAttachMaxSizeKb"));
		bean.setServerFtp((String) p.get("serverFtp"));
		bean.setUtenteFtp((String) p.get("utenteFtp"));
		bean.setPasswordFtp((String) p.get("passwordFtp"));
		bean.setDirRemotaServerFtp((String) p.get("directoryFtp"));
		bean.setOperatoreUltimoAggiornamento(user.getUserName());
		bean.setFlagTipoRendicontazione((String) p.get("rendicontazioneSeda")); //PG110260
		//inizio LP PG200060
		if(!template.equalsIgnoreCase("regmarche")) {
		//fine LP PG200060
			bean.setFlagSenzaCarico((String) p.get("senzaCarico"));
			bean.setEnte((String) p.get("ente"));
			bean.setImpServ((String) p.get("servizio"));
			bean.setFlagCodContrib((String) p.get("numDocCodContrib"));
			bean.setCodTributo((String) p.get("codTributo"));	//PG180260_001 GG 04122018
			bean.setFormatoFileRend((String) p.get("formatoFileRend"));
			bean.setFlagAbilitazioneInvioWebService((String) p.get("invioWebService"));
			bean.setUrlWebServiceEnte((String) p.get("urlWebService"));
			bean.setUtenteWebServiceEnte((String) p.get("utenteWebService"));
			bean.setPasswordWebServiceEnte((String) p.get("passwordWebService"));
		//inizio LP PG200060
		} else {
			bean.setFlagSenzaCarico("");
			bean.setEnte("");
			bean.setImpServ("");
			bean.setFlagCodContrib("");
			bean.setCodTributo("");
			bean.setFormatoFileRend((String) p.get("formatoFileRend"));
			bean.setFlagAbilitazioneInvioWebService("");
			bean.setUrlWebServiceEnte("");
			bean.setUtenteWebServiceEnte("");
			bean.setPasswordWebServiceEnte("");
		}
		//fine LP PG200060
		if (template.equalsIgnoreCase("trentrisc")) {
			bean.setFlagTracciatoComandiPolizia((String) p.get("flagTrcComandiPolizia"));
		}
		
		bean.settracciatoQuattrocento((String) p.get("rendquattrocento"));
		return bean;
	}

	/**
	 * Questa routine restituisce il bean necessario al WS per le operazioni di
	 * inserimento e modifica della tabella PYREETB
	 * 
	 * @param p - parametri di input della request
	 * @param session
	 * @return
	 */
	//inizio LP PG200060
	//protected ConfRendUtenteServizioEnteType getConfRendUtenteServizioEnteBean(HashMap<String,Object> p, HttpSession session)
	protected ConfRendUtenteServizioEnteType getConfRendUtenteServizioEnteBean(HashMap<String,Object> p, HttpSession session, String template)
	//fine LP PG200060
	{
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		ConfRendUtenteServizioEnteType bean = new ConfRendUtenteServizioEnteType();
		bean.setCodiceSocieta((String) p.get("codiceSocieta"));
		bean.setCodiceUtente((String) p.get("codiceUtente"));
		bean.setChiaveEnte((String) p.get("chiaveEnte"));
		bean.setTipologiaServizio((String) p.get("codiceTipologiaServizio"));
		bean.setFlagAbilitazioneInvioEmail((String) p.get("invioEmail"));
		bean.setFlagAbilitazioneInvioFtp((String) p.get("invioFtp"));
		bean.setEmailDestinatario((String) p.get("emailDestinatario"));
		bean.setEmailConoscenzaNascosta((String) p.get("emailCCN"));
		bean.setEmailMittente((String) p.get("emailMittente"));
		bean.setDescrizioneMittente((String) p.get("descrizioneMittente"));
		bean.setMaxAttachSizeKb((Integer)p.get("emailAttachMaxSizeKb"));
		bean.setServerFtp((String) p.get("serverFtp"));
		bean.setUtenteFtp((String) p.get("utenteFtp"));
		bean.setPasswordFtp((String) p.get("passwordFtp"));
		bean.setDirRemotaServerFtp((String) p.get("directoryFtp"));
		bean.setOperatoreUltimoAggiornamento(user.getUserName());
		bean.setFlagTipoRendicontazione((String) p.get("rendicontazioneSeda")); //PG110260
		//inizio LP PG200060
		if(!template.equalsIgnoreCase("regmarche")) {
		//fine LP PG200060
		bean.setFlagSenzaCarico((String) p.get("senzaCarico"));
		bean.setEnte((String) p.get("ente"));
		bean.setImpServ((String) p.get("servizio"));
		bean.setFlagCodContrib((String) p.get("numDocCodContrib"));
		bean.setCodTributo((String) p.get("codTributo"));
		
		bean.setFormatoFileRend((String) p.get("formatoFileRend"));
		bean.setFlagAbilitazioneInvioWebService((String) p.get("invioWebService"));
		bean.setUrlWebServiceEnte((String) p.get("urlWebService"));
		bean.setUtenteWebServiceEnte((String) p.get("utenteWebService"));
		bean.setPasswordWebServiceEnte((String) p.get("passwordWebService"));
		//inizio LP PG200060
			bean.setPasswordZip("");
		} else {
			bean.setFlagSenzaCarico("");
			bean.setEnte("");
			bean.setImpServ("");
			bean.setFlagCodContrib("");
			bean.setCodTributo("");
			
			bean.setFormatoFileRend((String) p.get("formatoFileRend"));
			bean.setFlagAbilitazioneInvioWebService("");
			bean.setUrlWebServiceEnte("");
			bean.setUtenteWebServiceEnte("");
			bean.setPasswordWebServiceEnte("");
			bean.setPasswordZip((String) p.get("passwordZip"));
		}
		//fine LP PG200060
		if (template.equalsIgnoreCase("trentrisc")) {
			bean.setFlagTracciatoComandiPolizia((String) p.get("flagTrcComandiPolizia"));
		}
		
		bean.setTracciatoQuattrocento((String) p.get("tracciatoQuattrocento"));
		
		return bean;
	}

	/**
	 * Questa routine controlla se il parametro "emailList" contiene
	 * una sequenza di indirizzidi email separati da ";" - 
	 * 
	 * @param emailList
	 * @return "true" se la condizione è verificata, false se non è verificata o se la stringa è nulla
	 */
	protected boolean checkEmailList(String emailList)
	{
		if (emailList == null || emailList.equals("")) return false;
		Validation validation = null;
		boolean esito = false;
		String lista[] = emailList.split(";");
		if (lista != null && lista.length > 0)
		{
			for (int i=0; i < lista.length; i++)
			{
				try {
					validation = new Validation();
					esito = validation.validate(lista[i].trim(), "email");
				} catch (ValidationException e) {
					esito = false;
				}
				if (!esito) break;
			}
		}
		return esito;

	}

	/**
	 * Questa routine recupera i parametri dal form di inserimento o modifica
	 * per la tabella PYREETB - Effettua inoltre il set delle 2 checkbox e del
	 * parametro "valueselected" della dropdownlist perchè in caso di "edit" è disabilitata - 
	 * 
	 * Nel caso della modifica i valori di Societa, Utente, Ente e Tipologia Servizio
	 * non vengono recuperati dalla drop-down-list ma dai campi hidden che sono stati settati
	 * all'atto del caricamento del record da modificare
	 * 
	 * @param request
	 * @param codop - "add" o "edit"
	 * @return
	 */
	protected HashMap<String,Object> getAbilitaCanalePagamentoTipoServizioEnteFormParameters(HttpServletRequest request, String codop)
	{
		HashMap<String,Object> param = new HashMap<String, Object>();
		String codiceSocieta = "";
		String codiceUtente = "";
		String chiaveEnte = "";
		String codiceCanalePagamento = "";
		String codiceTipologiaServizio = "";
		/*
		 * INSERIMENTO
		 */
		if (codop.equals("add"))
		{
			/*
			 * Prendo i parametri dalle DDL
			 */
			//DDL Società/Utente/Ente/Servizio
			String ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
			if (!ddlSocietaUtenteEnte.equals(""))
			{
				String[] codici = ddlSocietaUtenteEnte.split("\\|");
				codiceSocieta = codici[0];
				codiceUtente = codici[1];
				chiaveEnte = codici[2];
				//codiceTipologiaServizio = codici[3];
			}

			//DDL Tipologia Servizio
			String ddlTipologiaServizio = (String)request.getAttribute("ddlTipologiaServizio");
			if (!ddlTipologiaServizio.equals("")) {
				codiceTipologiaServizio = ddlTipologiaServizio;
			}

			//DDL Canale Pagamento
			String ddlCanalePagamento = (String)request.getAttribute("ddlCanalePagamento");
			if (!ddlSocietaUtenteEnte.equals("")) {
				codiceCanalePagamento = ddlCanalePagamento;
			}
		}
		/*
		 * MODIFICA
		 */
		else
		{
			/*
			 * Prendo i valori dai campi hidden che sono stati
			 * valorizzati dal WS e setto il parametro "valueselected" delle dropdownlists
			 */
			codiceSocieta = (request.getAttribute("tx_societa") == null ? "" : (String)request.getAttribute("tx_societa"));
			codiceUtente = (request.getAttribute("tx_utente") == null ? "" : (String)request.getAttribute("tx_utente"));
			chiaveEnte = (request.getAttribute("tx_ente") == null ? "" : (String)request.getAttribute("tx_ente"));
			String ddlSocietaUtenteEnte = codiceSocieta.concat("|").concat(codiceUtente).concat("|").concat(chiaveEnte);
			request.setAttribute("ddlSocietaUtenteEnte", ddlSocietaUtenteEnte);
			codiceTipologiaServizio = (request.getAttribute("tx_codiceTipologiaServizio") == null ? "" : (String)request.getAttribute("tx_codiceTipologiaServizio"));
			request.setAttribute("ddlTipologiaServizio", codiceTipologiaServizio);
			codiceCanalePagamento = (request.getAttribute("tx_canalePagamento") == null ? "" : (String)request.getAttribute("tx_canalePagamento"));
			String ddlCanalePagamento = codiceCanalePagamento;
			request.setAttribute("ddlCanalePagamento", ddlCanalePagamento);

		}
		String flagAttivazione = (request.getAttribute("flagAttivazione") == null ? "N" : "Y");
		param.put("codiceSocieta", codiceSocieta);
		param.put("codiceUtente", codiceUtente);
		param.put("chiaveEnte", chiaveEnte);
		param.put("codiceCanalePagamento", codiceCanalePagamento);
		param.put("codiceTipologiaServizio", codiceTipologiaServizio);
		param.put("flagAttivazione", flagAttivazione);
		/*
		 * Setto la check-box di attivazione
		 */
		request.setAttribute("chk_flagAttivazione",  flagAttivazione.equals("Y"));

		return param;
	}

	/**
	 * Questa routine effettua una validazione "applicativa" - 
	 * 
	 * @param parametri : l'HashMap che contiene i parametri di input della form da validare - 
	 * @return : "OK" se la validazione ha avuto esito positivo, altrimenti il messaggio di errore
	 * da visualizzare all'utente
	 */
	protected String checkInputAbilitaCanalePagamentoTipoServizioEnte (HashMap<String,Object> parametri )
	{
		/*
		 * Controllo che sia stato selezionato un item dalle due drop-down-list
		 */
		//DDL Società/Utente/Ente/Servizio
		if (((String)parametri.get("codiceSocieta")).equals(""))
			return Messages.SELEZIONARE_UN_ELEMENTO_LISTA.format("Società/Utente/Ente/Serv.");
		//DDL Canale Pagamento
		if (((String)parametri.get("codiceCanalePagamento")).equals(""))
			return Messages.SELEZIONARE_UN_ELEMENTO_LISTA.format("Canale Pag.");

		return "OK";
	}

	/**
	 * Questa routine recupera i parametri dal form di inserimento o modifica
	 * per la tabella PYSECTB - Effettua inoltre il set delle 2 checkbox 
	 * 
	 * Nel caso della modifica i valori di URL e ID Servizio Abilitato
	 * non vengono recuperati dal form ma dai campi hidden che sono stati settati
	 * all'atto del caricamento del record da modificare
	 * 
	 * @param request
	 * @param codop - "add" o "edit"
	 * @return
	 */
	protected HashMap<String,Object> getAbilitaSistemiEsterniSecureSiteFormParameters(HttpServletRequest request, String codop)
	{
		HashMap<String,Object> param = new HashMap<String, Object>();
		String urlAccesso = "";
		String idServizio = "";

		/*
		 * INSERIMENTO
		 */
		if (codop.equals("add"))
		{
			/*
			 * Prendo i parametri dal form
			 */
			urlAccesso = (String)request.getAttribute("urlAccesso");
			idServizio = (String)request.getAttribute("idServizio");
		}
		/*
		 * MODIFICA
		 */
		else
		{
			/*
			 * Prendo i valori dai campi hidden che sono stati valorizzati dal WS
			 */
			urlAccesso = (request.getAttribute("tx_url") == null ? "" : (String)request.getAttribute("tx_url"));
			idServizio = (request.getAttribute("tx_idServizio") == null ? "" : (String)request.getAttribute("tx_idServizio"));
			request.setAttribute("urlAccesso", urlAccesso);
			request.setAttribute("idServizio", idServizio);
		}
		String flagAttivazione = (request.getAttribute("flagAttivazione") == null ? "N" : "Y");
		String flagRedirect = (request.getAttribute("flagRedirect") == null ? "N" : "Y");

		String flagCalcoloCosti = (request.getAttribute("flagCalcoloCosti") == null ? "N" : "Y");
		String flagInvioNotifica = (request.getAttribute("flagInvioNotifica") == null ? "N" : "Y");

		String descrizione = (request.getAttribute("descrizione") == null ? "" : (String)request.getAttribute("descrizione"));
		String pathFileImmagine = (request.getAttribute("pathFileImmagine") == null ? "" : (String)request.getAttribute("pathFileImmagine"));
		String codiceTipologiaServizio = (request.getAttribute("codiceTipologiaServizio") == null ? "" : (String)request.getAttribute("codiceTipologiaServizio"));

		param.put("urlAccesso", urlAccesso);
		param.put("descrizione", descrizione);
		param.put("pathFileImmagine", pathFileImmagine);
		param.put("flagAttivazione", flagAttivazione);
		param.put("flagRedirect", flagRedirect);
		param.put("idServizio", idServizio);
		param.put("codiceTipologiaServizio", codiceTipologiaServizio);
		param.put("flagCalcoloCosti", flagCalcoloCosti);
		param.put("flagInvioNotifica", flagInvioNotifica);
		/*
		 * Setto le due check-box di attivo, redirect, calcolo costi ed invio notifica
		 */
		request.setAttribute("chk_flagAttivazione", flagAttivazione.equals("Y"));
		request.setAttribute("chk_flagRedirect",  flagRedirect.equals("Y"));
		request.setAttribute("chk_flagCalcoloCosti", flagCalcoloCosti.equals("Y"));
		request.setAttribute("chk_flagInvioNotifica",  flagInvioNotifica.equals("Y"));

		return param;
	}

	/**
	 * Questa routine effettua una validazione "applicativa" - 
	 * 
	 * @param parametri : l'HashMap che contiene i parametri di input della form da validare - 
	 * @return : "OK" se la validazione ha avuto esito positivo, altrimenti il messaggio di errore
	 * da visualizzare all'utente
	 */
	protected String checkInputAbilitaSistemiEsterniSecureSite (HashMap<String,Object> parametri )
	{
		String esito = "OK";
		if (((String)parametri.get("urlAccesso")).equals(""))
		{
			esito = Messages.CAMPO_NON_VALORIZZATO.format("Url");
		}
		return esito;
	}

	protected void loadDettaglioTransazione(HttpServletRequest request, HttpSession session)
	{
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		String codiceTransazione = request.getParameter(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format());
		request.setAttribute(Field.TX_CODICE_TRANSAZIONE_HIDDEN.format(), codiceTransazione);

		//test ente
		String codEnte = "";
		if (user.getUserProfile().equals("AMEN"))
		{
			codEnte = user.getChiaveEnteConsorzio();
		}

		/*
		 * Recupero il record dalla tabella transazioni
		 */
		try {
			WSCache.logWriter.logDebug("dettaglio transazione: id transazione='"+codiceTransazione+"' utente='"+user.getUserName()+"'");
			WSCache.logWriter.logDebug("codEnte='"+codEnte);
			WSCache.logWriter.logDebug("getTipologiaServizio(request, session)='"+getTipologiaServizio(request, session));
			//			RecuperaTransazioneResponseType res = WSCache.commonsServer.recuperaTransazione(new RecuperaTransazioneRequestType(codiceTransazione));

			RecuperaTransazioneFiltrataRequestType requestRecuperaTransazioneFiltrataRequestType=new RecuperaTransazioneFiltrataRequestType();
			requestRecuperaTransazioneFiltrataRequestType.setChiave_transazione(codiceTransazione);
			requestRecuperaTransazioneFiltrataRequestType.setCodice_ente(codEnte);
			requestRecuperaTransazioneFiltrataRequestType.setListaTipologieServizio(getTipologiaServizio(request, session));

			RecuperaTransazioneFiltrataResponseType res = WSCache.commonsServer.recuperaTransazioneFiltrata(requestRecuperaTransazioneFiltrataRequestType, request);
			
			//PG200110 - dopo mail Luca P. 20200318
			String template = getTemplateCurrentApplication(request, request.getSession());
			if(template.equals("regabruzzo")) {
				request.setAttribute("spom3", false);
				if(!res.getListIV()[0].getMese_scadenza_bollo_auto().trim().equals("")) {
					request.setAttribute("spom3", true);
					String[] dati = res.getListIV()[0].getNote_premarcato().split("\\|"); //Causale|Anno riferimento|Cespite
					request.setAttribute("causale", dati[0]);
					request.setAttribute("dataRiferimento", res.getListIV()[0].getMese_scadenza_bollo_auto()+"/"+dati[1]);
					request.setAttribute("cespite", dati[2]);
				}
			}
			//FINE PG200110 - dopo mail Luca P. 20200318
			
			request.setAttribute(Field.TX_TRANSAZIONE.format(), res.getBeanTransazioni().getBeanTransazioni());
			request.setAttribute(Field.TX_LISTA_ICI.format(), res.getListIci());
			request.setAttribute(Field.TX_LISTA_FRECCIA.format(), res.getListFreccia());
			request.setAttribute(Field.TX_LISTA_DETTAGLIO.format(), res.getListIV());
		} 
		catch (Exception e) {
			setErrorMessage(e.getMessage());
		}

		//recupero la lista oneri
		try {

			RecuperaListaOneriRequest req = new RecuperaListaOneriRequest(codiceTransazione);
			RecuperaListaOneriResponse res = WSCache.mipServer.recuperaListaOneri(req, request);

			request.setAttribute(Field.TX_LISTA_ONERI.format(), res.getListOneriXml());
		} 
		catch (Exception e) {
			setErrorMessage(e.getMessage());
		}
	}

	protected String getTipologiaServizio (HttpServletRequest request, HttpSession session) {
		String tipologiaServizio=isNull(request.getAttribute(Field.TX_TIPOLOGIA_SERVIZIO.format()));
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		if (user.getProfile().equals("AMEN")) {
			if (tipologiaServizio.equals(""))
				tipologiaServizio=user.getListaTipologiaServizioString();

		}
		return tipologiaServizio;

	}
	
	protected GruppoAgenziaPageList getGruppoAgenziaListDDL(HttpServletRequest request) {
		GruppoAgenziaDao gruppoAgenziaDAO;
	
		GruppoAgenziaPageList gruppiAgenziaList = null;
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			
			GruppoAgenziaBaseManagerAction gruppoAgenziaBaseManager=new GruppoAgenziaBaseManagerAction();
			gruppoAgenziaBaseManager.service(request);
			//inizio LP PG21XX04 Leak
			//gruppoAgenziaDAO = GruppoAgenziaDAOFactory.getGruppoAgenzia(gruppoAgenziaBaseManager.getGruppoAgenziaDataSource(), gruppoAgenziaBaseManager.getGruppoAgenziaDbSchema());
			conn = gruppoAgenziaBaseManager.getGruppoAgenziaDataSource().getConnection();
			gruppoAgenziaDAO = GruppoAgenziaDAOFactory.getGruppoAgenzia(conn, gruppoAgenziaBaseManager.getGruppoAgenziaDbSchema());
			//fine LP PG21XX04 Leak
			gruppiAgenziaList = gruppoAgenziaDAO.gruppoAgenziaListDDL();
		} catch (DaoException e1) {
			e1.printStackTrace();
		} catch (ActionException e) {
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		catch (SQLException e) {
			e.printStackTrace();
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
		return gruppiAgenziaList;
		
	}
	
}