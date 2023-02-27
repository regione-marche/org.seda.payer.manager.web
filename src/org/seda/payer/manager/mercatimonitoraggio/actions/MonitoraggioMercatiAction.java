package org.seda.payer.manager.mercatimonitoraggio.actions;

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
import com.seda.payer.core.mercato.bean.ConfigurazioneAreaMercantile;
import com.seda.payer.core.mercato.bean.ConfigurazionePiazzola;
import com.seda.payer.core.mercato.bean.MercatoPageList;
import com.seda.payer.core.mercato.bean.MonitoraggioMercati;
//import com.seda.payer.core.mercato.dao.ConfigurazioneAnagAutorizzazioneDAO;
import com.seda.payer.core.mercato.dao.ConfigurazioneAreaMercantileDAO;
import com.seda.payer.core.mercato.dao.ConfigurazionePiazzolaDAO;
import com.seda.payer.core.mercato.dao.MercatoDAOFactory;
import com.seda.payer.core.mercato.dao.MonitoraggioMercatiDAO;

import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.mercatomanager.actions.MercatoBaseManagerAction;
import com.seda.tag.core.DdlOption;


public class MonitoraggioMercatiAction extends MercatoBaseManagerAction{

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
		//getTipologieBancoDDL(request, session);
		//getPeriodoGiornalieroDDL(request, session);
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
	//	if ((String) session.getAttribute("recordInsert") != null) 
	//	{
	//		firedButton = firedButton.TX_BUTTON_CERCA; 
	//		if(session.getAttribute("recordInsert").toString().equals("OK"))
	//			setFormMessage("form_selezione", Messages.INS_OK.format(), request);
	//		else
	//			setFormMessage("form_selezione", session.getAttribute("messaggio.recordInsert").toString(), request);
	//		
	//		session.removeAttribute("recordInsert");
	//	}
		
	//	if ((String) session.getAttribute("recordUpdate") != null) 
	//	{
	//		firedButton = firedButton.TX_BUTTON_CERCA; 
	//		if(session.getAttribute("recordUpdate").toString().equals("OK"))
	//			setFormMessage("form_selezione", Messages.UPDT_OK.format(), request);
	//		else if (!(session.getAttribute("messaggio.recordUpdate")==null))
	//			setFormMessage("form_selezione", session.getAttribute("messaggio.recordUpdate").toString(), request);
	//		
	//		session.removeAttribute("recordUpdate");
	//	}
	//	if ((String) session.getAttribute("recordDelete") != null) 
	//	{
	//		firedButton = firedButton.TX_BUTTON_CERCA; 
	//		if(session.getAttribute("recordDelete").toString().equals("OK"))
	//			setFormMessage("form_selezione", Messages.CANC_OK.format(), request);
	//		else
	//			setFormMessage("form_selezione", session.getAttribute("messaggio.recordDelete").toString(), request);
	//		
	//		session.removeAttribute("recordDelete");
	//	}
		
		
		switch(firedButton)
		{
			
			case TX_BUTTON_CERCA: 
				try {
					dividiSocUtenteEnte(request);
					MercatoPageList mercatoPageListlist = listaMonitoraggioMercato(request);
					
					PageInfo pageInfo = mercatoPageListlist.getPageInfo();
					
					if (mercatoPageListlist.getRetCode()!="00") {
						setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
					} else {
						if(pageInfo != null)
						{
							if(pageInfo.getNumRows() > 0)
							{
								request.setAttribute("lista_monitor_mercati", mercatoPageListlist.getMercatoListXml());
								request.setAttribute("lista_monitor_mercati.pageInfo", pageInfo);
							}
							else { 
								request.setAttribute("lista_monitor_mercati", null);
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
				//getTipologieBancoDDL(request, session);
				//getPeriodoGiornalieroDDL(request, session);
				getPiazzolaDDL(request, session);
				request.setAttribute("tx_data_da", null);
				request.setAttribute("tx_data_a", null);
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
				String file="";
				file = listaMonitoraggioMercatiCSV(request);
				Calendar cal = Calendar.getInstance();
				String timestamp = GenericsDateNumbers.formatCalendarData(cal, "yyyyMMdd_HHmmss");
				request.setAttribute("filename",timestamp + "_MonitoraggioMercati.csv");
				request.setAttribute("fileCsv", file);				
				break;
		}

		return null;
	}
	
	private MercatoPageList listaMonitoraggioMercato(HttpServletRequest request) {
		MonitoraggioMercatiDAO monitoraggioMercatiDAO;
		MonitoraggioMercati monitoraggioMercati = new MonitoraggioMercati();
		if (request.getAttribute("vista").equals("monitoraggiomercatiLista")) {
			request.setAttribute("vista", "monitoraggiomercati");
			replyAttributes(false, request,"pageNumber","rowsPerPage","order");
			request.setAttribute("vista", "monitoraggiomercatiLista");
		}
		monitoraggioMercati.setCodiceSocieta(codiceSocieta);
		monitoraggioMercati.setCodUt(cutecute);
		monitoraggioMercati.setDescrizioneEnte(chiaveEnte);
		// Gestione Data Periodo DA
		String anno;
		String mese;
		String giorno;
		String datavalidita = "";
		if (((String)request.getAttribute("tx_data_da_year") == null)||((String)request.getAttribute("tx_dval_ini_year") == "")) {
			datavalidita="0001-01-01 00:00:00.0";
		} else {			
			anno = (String)request.getAttribute("tx_data_da_year");
			mese = (String)request.getAttribute("tx_data_da_month");
			giorno = (String)request.getAttribute("tx_data_da_day");
			datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dataDaPerCalendar  = Calendar.getInstance();
		if (datavalidita.equals("--")) {
			datavalidita = "0001-01-01 00:00:00.0";
		}
		try {
			dataDaPerCalendar.setTime(df.parse(datavalidita));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		// Gestione Data Periodo A
		anno="";
		mese="";
		giorno="";
		datavalidita="";
		if (((String)request.getAttribute("tx_data_a_year") == null)||((String)request.getAttribute("tx_dval_a_ini_year") == "")) {
			datavalidita = "3999-12-31 23:59:59.0";
		} else {			
			anno = (String)request.getAttribute("tx_data_a_year");
			mese = (String)request.getAttribute("tx_data_a_month");
			giorno = (String)request.getAttribute("tx_data_a_day");
			datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		}
		Calendar dataAPerCalendar  = Calendar.getInstance();
		if (datavalidita.equals("--")) {
			datavalidita = "3999-12-31 23:59:59.0";
		}
		try {
			dataAPerCalendar.setTime(df.parse(datavalidita));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		dataAPerCalendar.set(Calendar.HOUR_OF_DAY, 23);
		dataAPerCalendar.set(Calendar.MINUTE, 59);
		dataAPerCalendar.set(Calendar.SECOND, 59);
		monitoraggioMercati.setCodiceAutorizzazione((String)request.getAttribute("tx_num_autor"));
		monitoraggioMercati.setCodiceFiscaleAutorizzazione((String)request.getAttribute("tx_cod_fisc"));
		monitoraggioMercati.setCodiceKeyPiazzola((String)request.getAttribute("tx_piazzola"));
		monitoraggioMercati.setCodiceKeyMercato((String)request.getAttribute("tx_area_mercantile"));
		monitoraggioMercati.setDataInizio(dataDaPerCalendar);
		monitoraggioMercati.setDataFine(dataAPerCalendar);
		monitoraggioMercati.setPagato((String)request.getAttribute("tx_stato_pagam"));	
		MercatoPageList mercatoPageList = new MercatoPageList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//monitoraggioMercatiDAO = MercatoDAOFactory.getMonitoraggioMercati(getMercatoDataSource(), getMercatoDbSchema());
			conn = getMercatoDataSource().getConnection();
			monitoraggioMercatiDAO = MercatoDAOFactory.getMonitoraggioMercati(conn, getMercatoDbSchema());
			//fine LP PG21XX04 Leak
			
			mercatoPageList = monitoraggioMercatiDAO.ListMonitoraggioMercati(monitoraggioMercati, rowsPerPage, pageNumber, "");
			
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

	@SuppressWarnings("unchecked")
	private String listaMonitoraggioMercatiCSV(HttpServletRequest request) {
		String listaCSV = "";
		MonitoraggioMercatiDAO monitoraggioMercatiDAO;
		MonitoraggioMercati monitoraggio = new MonitoraggioMercati();
		monitoraggio.setCodiceSocieta(codiceSocieta);
		monitoraggio.setCodUt(cutecute);
		monitoraggio.setDescrizioneEnte(chiaveEnte);
		// Gestione Data Periodo DA
		String anno;
		String mese;
		String giorno;
		String datavalidita = "";
		if (((String)request.getAttribute("tx_data_da_year") == null)||((String)request.getAttribute("tx_dval_ini_year") == "")) {
			datavalidita="0001-01-01 00:00:00.0";
		} else {			
			anno = (String)request.getAttribute("tx_data_da_year");
			mese = (String)request.getAttribute("tx_data_da_month");
			giorno = (String)request.getAttribute("tx_data_da_day");
			datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dataDaPerCalendar  = Calendar.getInstance();
		if (datavalidita.equals("--")) {
			datavalidita = "0001-01-01 00:00:00.0";
		}
		try {
			dataDaPerCalendar.setTime(df.parse(datavalidita));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		// Gestione Data Periodo A
		anno="";
		mese="";
		giorno="";
		datavalidita="";
		if (((String)request.getAttribute("tx_data_a_year") == null)||((String)request.getAttribute("tx_dval_a_ini_year") == "")) {
			datavalidita = "3999-12-31 23:59:59.0";
		} else {			
			anno = (String)request.getAttribute("tx_data_a_year");
			mese = (String)request.getAttribute("tx_data_a_month");
			giorno = (String)request.getAttribute("tx_data_a_day");
			datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		}
		Calendar dataAPerCalendar  = Calendar.getInstance();
		if (datavalidita.equals("--")) {
			datavalidita = "3999-12-31 23:59:59.0";
		}
		try {
			dataAPerCalendar.setTime(df.parse(datavalidita));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		dataAPerCalendar.set(Calendar.HOUR_OF_DAY, 23);
		dataAPerCalendar.set(Calendar.MINUTE, 59);
		dataAPerCalendar.set(Calendar.SECOND, 59);
		monitoraggio.setCodiceAutorizzazione((String)request.getAttribute("tx_num_autor"));
		monitoraggio.setCodiceFiscaleAutorizzazione((String)request.getAttribute("tx_cod_fisc"));
		monitoraggio.setCodiceKeyPiazzola((String)request.getAttribute("tx_piazzola"));
		monitoraggio.setCodiceKeyMercato((String)request.getAttribute("tx_area_mercantile"));
		monitoraggio.setDataInizio(dataDaPerCalendar);
		monitoraggio.setDataFine(dataAPerCalendar);
		monitoraggio.setPagato((String)request.getAttribute("tx_stato_pagam"));		
		
		List<MonitoraggioMercati> monitoraggioMercatiList = null;
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//monitoraggioMercatiDAO = MercatoDAOFactory.getMonitoraggioMercati(getMercatoDataSource(), getMercatoDbSchema());
			conn = getMercatoDataSource().getConnection();
			monitoraggioMercatiDAO = MercatoDAOFactory.getMonitoraggioMercati(conn, getMercatoDbSchema());
			//fine LP PG21XX04 Leak
			monitoraggioMercatiList = monitoraggioMercatiDAO.doList(monitoraggio);
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
		
		String flpag = "";
		if (request.getAttribute("tx_stato_pagam")==null) {
			//
		} else {
			flpag=(String)request.getAttribute("tx_stato_pagam");
		}

		if (monitoraggioMercatiList.size()>0) {
			listaCSV = "Num. Autorizzazione;Nominativo Collegato;Codice Fiscale;" +
					   "Codice Mercato; Descrizione Mercato; Codice Piazzola; Descrizione Piazzola;" +
					   "Tipologia Banco; Periodo Giornaliero; Giorno della Settimana; Importo Cosap; Importo Tari;" +
			           "Data Prenotazione; Flag Pagato";
			if (!flpag.equals("N")) {
				listaCSV = listaCSV + ";Importo Pagato; Importo Compenso; Data Pagamento";
			}
			listaCSV = listaCSV + "\r\n";
		}
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		for (Iterator iterator = monitoraggioMercatiList.iterator(); iterator.hasNext();) {
			MonitoraggioMercati item = (MonitoraggioMercati) iterator.next();
			String flgPagato="";
			if (item.getPagato().equals("Y")) flgPagato="Si"; else flgPagato="No";
			listaCSV = listaCSV + item.getCodiceAutorizzazione() + ";" + item.getNominativoAutorizzazione()  
			+ ";" + item.getCodiceFiscaleAutorizzazione() + ";" + item.getCodiceMercato() + ";" + item.getDescrizioneMercato()  
			+ ";" + item.getCodicePiazzola() + ";" + item.getDescrizionePiazzola() + ";" + item.getTipologiaBanco()
			+ ";" + item.getPeriodoGiornaliero() + ";" + item.getGiornoSettimana() + ";" + String.valueOf(item.getImportoCosap()).replace(".",",") + ";" + String.valueOf(item.getImportoTari()).replace(".",",")
			+ ";" + format1.format(item.getDataPrenotazione().getTime()) + ";" + flgPagato;
			if (flgPagato.equals("Si")) {
				listaCSV = listaCSV + ";" + String.valueOf(item.getImportoDovuto()).replace(".", ",") + ";" + String.valueOf(item.getImportoCompenso()).replace(".",",") + ";" + format1.format(item.getDataOraPagamento().getTime());
			} else if (!flpag.equals("N")){
				listaCSV = listaCSV + ";;;;";
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

