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
//import java.util.Locale;
//import java.util.TimeZone;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.GenericsDateNumbers;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.mercato.bean.ConfigurazioneCompenso;
import com.seda.payer.core.mercato.bean.ConfigurazionePiazzola;
import com.seda.payer.core.mercato.bean.MercatoPageList;
import com.seda.payer.core.mercato.dao.ConfigurazioneCompensoDAO;
import com.seda.payer.core.mercato.dao.ConfigurazionePiazzolaDAO;
import com.seda.payer.core.mercato.dao.MercatoDAOFactory;
import com.seda.payer.core.mercato.bean.ConfigurazioneAreaMercantile;
import com.seda.payer.core.mercato.dao.ConfigurazioneAreaMercantileDAO;
import com.seda.tag.core.DdlOption;

import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.mercatomanager.actions.MercatoBaseManagerAction;



public class PiazzolaAction extends MercatoBaseManagerAction{

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
			else
				if(session.getAttribute("recordUpdate").toString().equals("KO"))
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
		loadSocietaUtenteEnteXml_DDL(request, session);
		dividiSocUtenteEnte(request);
		getAreeMercataliDDL(request, session);
		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));		
		
		switch(firedButton)
		{
			case TX_BUTTON_CERCA: 
				try {
					dividiSocUtenteEnte(request);
					MercatoPageList mercatoPageListlist = listaConfigurazionePiazzola(request);
					
					PageInfo pageInfo = mercatoPageListlist.getPageInfo();
					
					if (mercatoPageListlist.getRetCode()!="00") {
						if (mercatoPageListlist.getRetCode()=="01") {
							setFormMessage("form_selezione", "Date inserite nel filtro non congruenti, verificare ", request);
						} else {
							setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
						}
					} else {
						if(pageInfo != null)
						{
							if(pageInfo.getNumRows() > 0)
							{
								request.setAttribute("lista_configurazioni_piazzola", mercatoPageListlist.getMercatoListXml());
								request.setAttribute("lista_configurazioni_piazzola.pageInfo", pageInfo);
							}
							else { 
								request.setAttribute("lista_configurazioni_piazzola", null);
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
				request.setAttribute("tx_descr_piazzola", null);
				request.setAttribute("tx_dval_ini", null);
				request.setAttribute("tx_dval_a_ini", null);
				request.setAttribute("tx_dval_fin", null);
				request.setAttribute("tx_dval_a_fin", null);
				setProfile(request);
				codiceSocieta="";
				cutecute="";
				chiaveEnte="";
				getAreeMercataliDDL(request, session);
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
				file = listaPiazzolaCSV(request);
				Calendar cal = Calendar.getInstance();
				String timestamp = GenericsDateNumbers.formatCalendarData(cal, "yyyyMMdd_HHmmss");
				request.setAttribute("filename",timestamp + "_Piazzole.csv");
				request.setAttribute("fileCsv", file);				
				break;
		}
		return null;
	}
	
	private MercatoPageList listaConfigurazionePiazzola(HttpServletRequest request) {
		boolean Check=true;
		ConfigurazionePiazzolaDAO configurazionePiazzolaDAO;
		ConfigurazionePiazzola configurazionePiazzola = new ConfigurazionePiazzola();
		if (request.getAttribute("vista").equals("piazzolaLista")) {
			request.setAttribute("vista", "piazzola");
			replyAttributes(false, request,"pageNumber","rowsPerPage","order");
			request.setAttribute("vista", "piazzolaLista");
		}						
		configurazionePiazzola.setCodiceSocieta(codiceSocieta);
		configurazionePiazzola.setCuteCute(cutecute);
		configurazionePiazzola.setChiaveEnte(chiaveEnte);
		configurazionePiazzola.setCodicePiazzola((String)request.getAttribute("tx_piazzola"));
		String descr = (String)request.getAttribute("tx_descr_piazzola");
		if (descr==null) {
			descr = "";
		} else {
			descr = descr.trim().toUpperCase();
		}
		configurazionePiazzola.setDescrizionePiazzola(descr);		
		configurazionePiazzola.setCodiceKeyAreaMercantile((String)request.getAttribute("tx_area_mercantile"));
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
		configurazionePiazzola.setDataInizioValidita(dataIniValCalendar);
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
			dataAIniValCalendar.set(Calendar.HOUR_OF_DAY, 23);
			dataAIniValCalendar.set(Calendar.MINUTE, 59);
			dataAIniValCalendar.set(Calendar.SECOND, 59);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		configurazionePiazzola.setDataAInizioValidita(dataAIniValCalendar);
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

		configurazionePiazzola.setDataFineValidita(dataFinValCalendar);
		// Gestione Data Fine Validita A
		datavalidita="";
		if (((String)request.getAttribute("tx_dval_a_fin_year") == null)||((String)request.getAttribute("tx_dval_a_fin_year") == "")) {
			datavalidita = "2999-12-31 23:59:59.0";
		} else {			
			anno = (String)request.getAttribute("tx_dval_a_fin_year");
			mese = (String)request.getAttribute("tx_dval_a_fin_month");
			giorno =(String)request.getAttribute("tx_dval_a_fin_day");
			datavalidita = anno.concat("-").concat(mese.concat("-").concat(giorno));
		}
		Calendar dataAFinValCalendar;
		if (datavalidita.equals("--")) {
			datavalidita = "2999-12-31 23:59:59.0";
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
		configurazionePiazzola.setDataAFineValidita(dataAFinValCalendar);
		//Verifica delle data da/a 
		if (dataFinValCalendar.compareTo(dataAFinValCalendar)>0) {
			Check=false;
		}
		if (dataIniValCalendar.compareTo(dataAIniValCalendar)>0) {
			Check=false;
		}
		if (dataIniValCalendar.compareTo(dataAFinValCalendar)>0) {
			Check=false;
		}
		
		MercatoPageList mercatoPageList = new MercatoPageList();
		if (Check) {
			//inizio LP PG21XX04 Leak
			Connection conn = null;
			//fine LP PG21XX04 Leak
			try {
				//inizio LP PG21XX04 Leak
				//configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(getMercatoDataSource(), getMercatoDbSchema());
				conn = getMercatoDataSource().getConnection();
				configurazionePiazzolaDAO = MercatoDAOFactory.getConfigurazionePiazzola(conn, getMercatoDbSchema());
				//fine LP PG21XX04 Leak
				
				mercatoPageList = configurazionePiazzolaDAO.ListConfigurazionePiazzola(configurazionePiazzola, rowsPerPage, pageNumber, "");
			
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
		} else {
			mercatoPageList.setRetCode("01");
		}
		return mercatoPageList;
		
	}
	
	@SuppressWarnings("unchecked")
	private void getAreeMercataliDDL(HttpServletRequest request, HttpSession session) {
		//dividiSocUtenteEnte(request);	
		List<DdlOption> areemercDDLList  = new ArrayList<DdlOption>();
		ConfigurazioneAreaMercantileDAO configurazioneAreaMercantileDAO;
		ConfigurazioneAreaMercantile configurazioneAreaMercantile = new ConfigurazioneAreaMercantile();
	// Se non ho selezionato un elemento SocUtenteEnte non devo esporre nessun mercato!
//		if (((codiceSocieta==null)||(codiceSocieta.equals(""))) &&
//			((cutecute==null)||(cutecute.equals(""))) &&
//			((chiaveEnte==null)||(chiaveEnte.equals("")))) {
//			configurazioneAreaMercantile.setCodiceSocieta("xx");
//			configurazioneAreaMercantile.setCuteCute("xx");
//			configurazioneAreaMercantile.setChiaveEnte("xx");
//		} else {
			configurazioneAreaMercantile.setCodiceSocieta(codiceSocieta);
			configurazioneAreaMercantile.setCuteCute(cutecute);
			configurazioneAreaMercantile.setChiaveEnte(chiaveEnte);
//		}
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
	private String listaPiazzolaCSV(HttpServletRequest request) {
		String listaCSV = "";
		ConfigurazionePiazzolaDAO configurazionePiazzolaDAO;
		ConfigurazionePiazzola configurazionePiazzola = new ConfigurazionePiazzola();
		configurazionePiazzola.setCodiceSocieta(codiceSocieta);
		configurazionePiazzola.setCuteCute(cutecute);
		configurazionePiazzola.setChiaveEnte(chiaveEnte);
		configurazionePiazzola.setCodicePiazzola((String)request.getAttribute("tx_piazzola"));
		String descr = (String)request.getAttribute("tx_descr_piazzola");
		if (descr==null) {
			descr = "";
		} else {
			descr = descr.trim().toUpperCase();
		}
		configurazionePiazzola.setDescrizionePiazzola(descr);
		configurazionePiazzola.setCodiceKeyAreaMercantile((String)request.getAttribute("tx_area_mercantile"));
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
		configurazionePiazzola.setDataInizioValidita(dataIniValCalendar);
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
			dataAIniValCalendar.set(Calendar.HOUR_OF_DAY, 23);
			dataAIniValCalendar.set(Calendar.MINUTE, 59);
			dataAIniValCalendar.set(Calendar.SECOND, 59);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		configurazionePiazzola.setDataAInizioValidita(dataAIniValCalendar);
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
		configurazionePiazzola.setDataFineValidita(dataFinValCalendar);
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
		configurazionePiazzola.setDataAFineValidita(dataAFinValCalendar);		

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
		if (configurazionePiazzolaList.size()>0) {
			listaCSV = "Area Mercatale;Codice Piazzola;Descr. Piazzola;Cordinate Angolo 1;Coordinate Angolo 2;" +
					   "Coordinate Angolo 3; Coordinate Angolo 4;Data Inizio Validità; Data Fine Validità\r\n";
		}
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		for (Iterator iterator = configurazionePiazzolaList.iterator(); iterator.hasNext();) {
			ConfigurazionePiazzola item = (ConfigurazionePiazzola) iterator.next();
			listaCSV = listaCSV + item.getDescrizioneAreaMerc() + ";" + item.getCodicePiazzola() + ";" +
			    item.getDescrizionePiazzola() + ";" + item.getCoordLatAng1() + "|" + item.getCoordLonAng1() + ";"
			    + item.getCoordLatAng2() + "|" + item.getCoordLonAng2() + ";"
			    + item.getCoordLatAng3() + "|" + item.getCoordLonAng3() + ";"
			    + item.getCoordLatAng4() + "|" + item.getCoordLonAng4() + ";"
			    + format1.format(item.getDataInizioValidita().getTime()) + ";";
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

