package org.seda.payer.manager.mercatoconfig.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.GenericsDateNumbers;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.mercato.bean.ConfigurazioneTariffe;
import com.seda.payer.core.mercato.bean.MercatoPageList;
import com.seda.payer.core.mercato.dao.ConfigurazioneTariffeDAO;
import com.seda.payer.core.mercato.dao.MercatoDAOFactory;
import com.seda.payer.core.mercato.bean.ConfigurazioneAreaMercantile;
import com.seda.payer.core.mercato.dao.ConfigurazioneAreaMercantileDAO;
import com.seda.payer.core.mercato.dao.ConfigurazioneTipologiaBancoDAO;
import com.seda.payer.core.mercato.bean.ConfigurazioneTipologiaBanco;
import com.seda.payer.core.mercato.dao.ConfigurazionePeriodoGiornalieroDAO;
import com.seda.payer.core.mercato.bean.ConfigurazionePeriodoGiornaliero;
import com.seda.payer.core.mercato.dao.ConfigurazionePiazzolaDAO;
import com.seda.payer.core.mercato.bean.ConfigurazionePiazzola;

import com.seda.tag.core.DdlOption;

import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.mercatomanager.actions.MercatoBaseManagerAction;


public class TariffeAction extends MercatoBaseManagerAction{

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber; 
	private String username;
	private String codiceSocieta="";
	private String cutecute="";
	private String chiaveEnte="";

	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		
		loadSocietaUtenteEnteXml_DDL(request, session);
		dividiSocUtenteEnte(request);
		getAreeMercataliDDL(request, session);
		getTipologieBancoDDL(request, session);
		getPeriodoGiornalieroDDL(request, session);
		getPiazzolaDDL(request, session);
		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));		
		
		FiredButton firedButton = getFiredButton(request);
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) 
			setProfile(request);
		// carico le DDL della società, cutecute ed Ente
		
		HttpSession sessionlocal = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		username = user.getUserName();
		if ((String) session.getAttribute("recordInsert") != null) 
		{
			firedButton = firedButton.TX_BUTTON_CERCA; 
			if(session.getAttribute("recordInsert").toString().equals("OK"))
				setFormMessage("form_selezione", Messages.INS_OK.format(), request);
			else
				setFormMessage("form_selezione", session.getAttribute("messaggio.recordInsert").toString(), request);
			
			session.removeAttribute("recordInsert");
		}
		
		if ((String) session.getAttribute("recordUpdate") != null) 
		{
			firedButton = firedButton.TX_BUTTON_CERCA; 
			if(session.getAttribute("recordUpdate").toString().equals("OK"))
				setFormMessage("form_selezione", Messages.UPDT_OK.format(), request);
			else if (!(session.getAttribute("messaggio.recordUpdate")==null))
				setFormMessage("form_selezione", session.getAttribute("messaggio.recordUpdate").toString(), request);
			
			session.removeAttribute("recordUpdate");
		}
		if ((String) session.getAttribute("recordDelete") != null) 
		{
			firedButton = firedButton.TX_BUTTON_CERCA; 
			if(session.getAttribute("recordDelete").toString().equals("OK"))
				setFormMessage("form_selezione", Messages.CANC_OK.format(), request);
			else
				setFormMessage("form_selezione", session.getAttribute("messaggio.recordDelete").toString(), request);
			
			session.removeAttribute("recordDelete");
		}
		
		
		switch(firedButton)
		{
			
			case TX_BUTTON_CERCA: 
				try {
					dividiSocUtenteEnte(request);
					MercatoPageList mercatoPageListlist = listaConfigurazioneTariffe(request);
					
					PageInfo pageInfo = mercatoPageListlist.getPageInfo();
					
					if (mercatoPageListlist.getRetCode()!="00") {
						setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
					} else {
						if(pageInfo != null)
						{
							if(pageInfo.getNumRows() > 0)
							{
								request.setAttribute("lista_configurazioni_tariffe", mercatoPageListlist.getMercatoListXml());
								request.setAttribute("lista_configurazioni_tariffe.pageInfo", pageInfo);
							}
							else { 
								request.setAttribute("lista_configurazioni_tariffe", null);
								setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
							}
						}
						else { 
							setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
						}
					}
				} catch(Exception e) {
					setErrorMessage(e.getMessage());
					e.printStackTrace();
				}
				break;
				
			case TX_BUTTON_RESET:
				resetParametri(request);
				setProfile(request);
				codiceSocieta="";
				cutecute="";
				chiaveEnte="";
				getAreeMercataliDDL(request, session);
				getTipologieBancoDDL(request, session);
				getPeriodoGiornalieroDDL(request, session);
				getPiazzolaDDL(request, session);
				break;
				
			case TX_BUTTON_SOCIETA_CHANGED:
				String societa = getParamCodiceSocieta();
				request.setAttribute("tx_provincia","");
				request.setAttribute("tx_UtenteEnte", "");
				break;
				
			case TX_BUTTON_PROVINCIA_CHANGED:
				loadProvinciaXml_DDL(request, session, null,false);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				request.setAttribute("tx_UtenteEnte", "");
				break;
			
			case TX_BUTTON_ENTE_CHANGED:
				loadProvinciaXml_DDL(request, session, "",false);
				LoadListaUtentiEntiXml_DDL(request, session, "" , "", "", "", false);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				break;
				
			case TX_BUTTON_NULL: 
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				break;
				
			case TX_BUTTON_STAMPA:
				break;
				
			case TX_BUTTON_DOWNLOAD:
				dividiSocUtenteEnte(request);
				String file="";
				file = listaTariffeCSV(request);

				Calendar cal = Calendar.getInstance();
				String timestamp = GenericsDateNumbers.formatCalendarData(cal, "yyyyMMdd_HHmmss");
				request.setAttribute("filename",timestamp + "_Tariffe.csv");
				request.setAttribute("fileCsv", file);				
				break;
		}
		return null;
	}
	
	private MercatoPageList listaConfigurazioneTariffe(HttpServletRequest request) {		
		ConfigurazioneTariffeDAO configurazioneTariffeDAO;
		ConfigurazioneTariffe configurazioneTariffe = new ConfigurazioneTariffe();
		if (request.getAttribute("vista").equals("tariffeLista")) {
			request.setAttribute("vista", "tariffe");
			replyAttributes(false, request,"pageNumber","rowsPerPage","order");
			request.setAttribute("vista", "tariffeLista");
		}				
		configurazioneTariffe.setCodiceSocieta(codiceSocieta);
		configurazioneTariffe.setCuteCute(cutecute);
		configurazioneTariffe.setChiaveEnte(chiaveEnte);
		configurazioneTariffe.setCodiceKeyAreaMercantile((String)request.getAttribute("tx_area_mercantile"));
		configurazioneTariffe.setCodiceKeyPiazzola((String)request.getAttribute("tx_piazzola"));
		configurazioneTariffe.setCodiceKeyAutorizzazione((String)request.getAttribute("tx_num_autor"));
		int Day = 0;
		if ((String)request.getAttribute("tx_giorno_settimana")==null) {
			//
		} else {
			Day = Integer.parseInt((String)request.getAttribute("tx_giorno_settimana"));
		}
		configurazioneTariffe.setCodiceGiornoSettimana(Day);			
		configurazioneTariffe.setCodiceKeyTipologiaBanco((String)request.getAttribute("tx_tipologia_banco"));
		configurazioneTariffe.setCodiceKeyPeriodoGiornal((String)request.getAttribute("tx_periodo_giornal"));
		
		// Gestione Data Inizio Validita DA
		String anno;
		String mese;
		String giorno;
		String datavalidita = "";
		if (((String)request.getAttribute("tx_dval_ini_year") == null)||((String)request.getAttribute("tx_dval_ini_year") == "")) {
			datavalidita="0001-01-01 00:00:00.0";
		} else {			
			anno = (String)request.getAttribute("tx_dval_ini_year");
			mese = (String)request.getAttribute("tx_dval_ini_month");
			giorno = (String)request.getAttribute("tx_dval_ini_day");
			datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dataIniValCalendar  = Calendar.getInstance();
		if (datavalidita.equals("--")) {
			datavalidita = "0001-01-01 00:00:00.0";
		}
		try {
			dataIniValCalendar.setTime(df.parse(datavalidita));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		configurazioneTariffe.setDataInizioValidita(dataIniValCalendar);
		// Gestione Data Inizio Validita A
		anno="";
		mese="";
		giorno="";
		datavalidita="";
		if (((String)request.getAttribute("tx_dval_a_ini_year") == null)||((String)request.getAttribute("tx_dval_a_ini_year") == "")) {
			datavalidita = "3999-12-31 23:59:59.0";
		} else {			
			anno = (String)request.getAttribute("tx_dval_a_ini_year");
			mese = (String)request.getAttribute("tx_dval_a_ini_month");
			giorno = (String)request.getAttribute("tx_dval_a_ini_day");
			datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		}
		Calendar dataAIniValCalendar  = Calendar.getInstance();
		if (datavalidita.equals("--")) {
			datavalidita = "3999-12-31 23:59:59.0";
		}
		try {
			dataAIniValCalendar.setTime(df.parse(datavalidita));
			dataAIniValCalendar.set(Calendar.HOUR, 23);
			dataAIniValCalendar.set(Calendar.MINUTE, 59);
			dataAIniValCalendar.set(Calendar.SECOND, 59);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		configurazioneTariffe.setDataAInizioValidita(dataAIniValCalendar);
		// Gestione Data Fine Validita DA
		anno="";
		mese="";
		giorno="";
		datavalidita="";
		if (((String)request.getAttribute("tx_dval_fin_year") == null)||((String)request.getAttribute("tx_dval_fin_year") == "")) {
			datavalidita="0001-01-01 00:00:00.0";
		} else {			
			anno = (String)request.getAttribute("tx_dval_fin_year");
			mese = (String)request.getAttribute("tx_dval_fin_month");
			giorno =(String)request.getAttribute("tx_dval_fin_day");
			datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		}
		Calendar dataFinValCalendar;
		if (datavalidita.equals("--")) {
			datavalidita="0001-01-01 00:00:00.0";
		}
		dataFinValCalendar  = Calendar.getInstance();
		try {
			dataFinValCalendar.setTime(df.parse(datavalidita));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		configurazioneTariffe.setDataFineValidita(dataFinValCalendar);
		// Gestione Data Fine Validita A
		datavalidita="";
		if (((String)request.getAttribute("tx_dval_a_fin_year") == null)||((String)request.getAttribute("tx_dval_a_fin_year") == "")) {
			datavalidita = "3999-12-31 23:59:59.0";
		} else {			
			anno = (String)request.getAttribute("tx_dval_a_fin_year");
			mese = (String)request.getAttribute("tx_dval_a_fin_month");
			giorno =(String)request.getAttribute("tx_dval_a_fin_day");
			datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		}
		Calendar dataAFinValCalendar;
		if (datavalidita.equals("--")) {
			datavalidita = "3999-12-31 23:59:59.0";
		}
		dataAFinValCalendar  = Calendar.getInstance();
		try {
			dataAFinValCalendar.setTime(df.parse(datavalidita));
			dataAFinValCalendar.set(Calendar.HOUR_OF_DAY, 23);
			dataAFinValCalendar.set(Calendar.MINUTE, 59);
			dataAFinValCalendar.set(Calendar.SECOND, 59);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		configurazioneTariffe.setDataAFineValidita(dataAFinValCalendar);		
		
		MercatoPageList mercatoPageList = new MercatoPageList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
			conn = getMercatoDataSource().getConnection();
			configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
			//fine LP PG21XX04 Leak
			
			mercatoPageList = configurazioneTariffeDAO.ListConfigurazioneTariffe(configurazioneTariffe, rowsPerPage, pageNumber, "");
			
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
		return mercatoPageList;
		
	}
	
	@SuppressWarnings("unchecked")
	private void getAreeMercataliDDL(HttpServletRequest request, HttpSession session) {		
		List<DdlOption> areemercDDLList  = new ArrayList<DdlOption>();
		ConfigurazioneAreaMercantileDAO configurazioneAreaMercantileDAO;
		ConfigurazioneAreaMercantile configurazioneAreaMercantile = new ConfigurazioneAreaMercantile();
		configurazioneAreaMercantile.setCodiceSocieta(codiceSocieta);
		configurazioneAreaMercantile.setCuteCute(cutecute);
		configurazioneAreaMercantile.setChiaveEnte(chiaveEnte);
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
		configurazioneTipologiaBanco.setCodiceSocieta(codiceSocieta);
		configurazioneTipologiaBanco.setCuteCute(cutecute);
		configurazioneTipologiaBanco.setChiaveEnte(chiaveEnte);
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
		configurazionePeriodoGiornaliero.setCodiceSocieta(codiceSocieta);
		configurazionePeriodoGiornaliero.setCuteCute(cutecute);
		configurazionePeriodoGiornaliero.setChiaveEnte(chiaveEnte);
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
		configurazionePiazzola.setCodiceSocieta(codiceSocieta);
		configurazionePiazzola.setCuteCute(cutecute);
		configurazionePiazzola.setChiaveEnte(chiaveEnte);
		String tmp = (String)request.getAttribute("tx_area_mercantile");
		if ((tmp==null)||(tmp.equals(""))) {
			tmp="xx";
		}
		configurazionePiazzola.setCodiceKeyAreaMercantile(tmp);
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
				optionPiazzola.setSText(item.getCodicePiazzola());
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

	@SuppressWarnings("unchecked")
	private String listaTariffeCSV(HttpServletRequest request) {
		String listaCSV = "";
		ConfigurazioneTariffeDAO configurazioneTariffeDAO;
		ConfigurazioneTariffe configurazioneTariffe = new ConfigurazioneTariffe();
		configurazioneTariffe.setCodiceSocieta(codiceSocieta);
		configurazioneTariffe.setCuteCute(cutecute);
		configurazioneTariffe.setChiaveEnte(chiaveEnte);
		configurazioneTariffe.setCodiceKeyAreaMercantile((String)request.getAttribute("tx_area_mercantile"));
		configurazioneTariffe.setCodiceKeyPiazzola((String)request.getAttribute("tx_piazzola"));
		configurazioneTariffe.setCodiceKeyAutorizzazione((String)request.getAttribute("tx_num_autor"));
		int Day = 0;
		if ((String)request.getAttribute("tx_giorno_settimana")==null) {
			//
		} else {
			Day = Integer.parseInt((String)request.getAttribute("tx_giorno_settimana"));
		}
		configurazioneTariffe.setCodiceGiornoSettimana(Day);			
		configurazioneTariffe.setCodiceKeyTipologiaBanco((String)request.getAttribute("tx_tipologia_banco"));
		configurazioneTariffe.setCodiceKeyPeriodoGiornal((String)request.getAttribute("tx_periodo_giornal"));

		ArrayList<ConfigurazioneTariffe> configurazioneTariffeList = null;
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(getMercatoDataSource(), getMercatoDbSchema());
			conn = getMercatoDataSource().getConnection();
			configurazioneTariffeDAO = MercatoDAOFactory.getConfigurazioneTariffe(conn, getMercatoDbSchema());
			//fine LP PG21XX04 Leak
			configurazioneTariffeList = configurazioneTariffeDAO.listTariffe(configurazioneTariffe);
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
		if (configurazioneTariffeList != null && !configurazioneTariffeList.isEmpty()) {
			listaCSV = "Area Mercatale;Descr. Piazzola;Num. Autorizzazione;Giorno della settimana;" +
					   "Tipologia Banco;PeriodoGiornaliero;Tariffa Tari;Tariffa Cosap;Data Inizio Validità; Data Fine Validità\r\n";
		}
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		for (Iterator iterator = configurazioneTariffeList.iterator(); iterator.hasNext();) {
			ConfigurazioneTariffe item = (ConfigurazioneTariffe) iterator.next();
			String giorno = "";
			switch (item.getCodiceGiornoSettimana()) {
				case 0: giorno = "non selezionato"; 
						break;
				case 1: giorno = "Lunedì";
						break;
				case 2: giorno = "Martedì";
						break;
				case 3: giorno = "Mercoledì";
						break;
				case 4:	giorno = "Giovedì";
						break;
				case 5: giorno = "Venerdì";
						break;
				case 6: giorno = "Sabato";
						break;
				case 7: giorno = "Domenica";
						break;
				default: giorno = "Non Selezionato";
						break;
			}
			listaCSV = listaCSV + item.getDescrAreaMercato() + ";" + item.getDescrPiazzola() + ";" + 
			item.getCodiceKeyAutorizzazione() + ";" + giorno + ";" + item.getDescrTipologiaBanco() + ";" + item.getDescrPeriodoGiorn() + ";" +
			item.getTariffaTari() + ";" + item.getTariffaCosap() + ";" +
			format1.format(item.getDataInizioValidita().getTime()) + ";";
			if (item.getDataFineValidita().getTimeInMillis()>31567434000L) {
				listaCSV=listaCSV + format1.format(item.getDataFineValidita().getTime());
			}
			listaCSV=listaCSV+ "\r\n";
		}
		return listaCSV;
	}	
	
	private void dividiSocUtenteEnte(HttpServletRequest request) {
		// TODO Auto-generated method stub
		if(request.getAttribute("ddlSocietaUtenteEnte")!=null)
		{
			String ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
			if (!ddlSocietaUtenteEnte.equals(""))
			{
				String[] codici = ddlSocietaUtenteEnte.split("\\|");
				codiceSocieta = codici[0];
				cutecute = codici[1];
				//chiaveEnte = codici[1];
				chiaveEnte = codici[2];
				request.setAttribute("tx_societa", codiceSocieta);
				request.setAttribute("tx_utente", cutecute);
				request.setAttribute("tx_ente", chiaveEnte);
			}
		
		}
	}
	
	
}

