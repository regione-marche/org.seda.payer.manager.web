package org.seda.payer.manager.mercatoconfig.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.GenericsDateNumbers;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.mercato.bean.ConfigurazioneCompenso;
import com.seda.payer.core.mercato.bean.MercatoPageList;
import com.seda.payer.core.mercato.dao.ConfigurazioneCompensoDAO;
import com.seda.payer.core.mercato.dao.MercatoDAOFactory;

import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.mercatomanager.actions.MercatoBaseManagerAction;



public class CompensoAction extends MercatoBaseManagerAction{

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
		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		
		switch(firedButton)
		{
			
			case TX_BUTTON_CERCA: 
				try {
					dividiSocUtenteEnte(request);
					MercatoPageList mercatoPageListlist = listaConfigurazioneCompenso(request);
					
					PageInfo pageInfo = mercatoPageListlist.getPageInfo();
					
					if (!mercatoPageListlist.getRetCode().equals("00")) {
						if(mercatoPageListlist.getRetCode().equals("04")) {
							setFormMessage("form_selezione", mercatoPageListlist.getMessage(), request);
						} else {
							setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
						}
					} else {
						if(pageInfo != null)
						{
							if(pageInfo.getNumRows() > 0)
							{
								request.setAttribute("lista_configurazioni_compenso", mercatoPageListlist.getMercatoListXml());
								request.setAttribute("lista_configurazioni_compenso.pageInfo", pageInfo);
							}
							else { 
								request.setAttribute("lista_configurazioni_compenso", null);
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
				request.setAttribute("tx_dval_ini", null);
				request.setAttribute("tx_dval_a_ini", null);
				request.setAttribute("tx_dval_fin", null);
				request.setAttribute("tx_dval_a_fin", null);
				setProfile(request);
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
				file = listaCompensoCSV(request);
				Calendar cal = Calendar.getInstance();
				String timestamp = GenericsDateNumbers.formatCalendarData(cal, "yyyyMMdd_HHmmss");
				request.setAttribute("filename",timestamp + "_Compenso.csv");
				request.setAttribute("fileCsv", file);				
				break;
		}
		return null;
	}

	private MercatoPageList listaConfigurazioneCompenso(HttpServletRequest request) {
		boolean Check=true;
		ConfigurazioneCompensoDAO configurazioneCompensoDAO;
		ConfigurazioneCompenso configurazioneCompenso = new ConfigurazioneCompenso();
		configurazioneCompenso.setCodiceSocieta(codiceSocieta);
		configurazioneCompenso.setCuteCute(cutecute);
		configurazioneCompenso.setChiaveEnte(chiaveEnte);
		String tx_dval_ini = getDataByPrefix("tx_dval_ini",request);
		Calendar dataIniValCalendar  = GenericsDateNumbers.getCalendarFromDateString(tx_dval_ini, "yyyy-MM-dd");
		configurazioneCompenso.setDataInizioValidita(dataIniValCalendar);
		configurazioneCompenso.setAttribute("tx_dval_ini", tx_dval_ini);
		String tx_dval_a_ini = getDataByPrefix("tx_dval_a_ini",request);
		Calendar dataAIniValCalendar  = GenericsDateNumbers.getCalendarFromDateString(tx_dval_a_ini, "yyyy-MM-dd");
		configurazioneCompenso.setDataAInizioValidita(dataAIniValCalendar);
		configurazioneCompenso.setAttribute("tx_dval_a_ini", tx_dval_a_ini);
		String tx_dval_fin = getDataByPrefix("tx_dval_fin",request);
		Calendar dataFinValCalendar  = GenericsDateNumbers.getCalendarFromDateString(tx_dval_fin, "yyyy-MM-dd");
		configurazioneCompenso.setDataFineValidita(dataFinValCalendar);
		configurazioneCompenso.setAttribute("tx_dval_fin", tx_dval_fin);
		String tx_dval_a_fin = getDataByPrefix("tx_dval_a_fin",request);
		Calendar dataAFinValCalendar  = GenericsDateNumbers.getCalendarFromDateString(tx_dval_a_fin, "yyyy-MM-dd");
		configurazioneCompenso.setDataFineValidita(dataAFinValCalendar);		
		configurazioneCompenso.setAttribute("tx_dval_a_fin", tx_dval_a_fin);
		
		String msg = "";
		//Verifica delle data da/a
		if (tx_dval_ini.trim().length()>0 && tx_dval_a_ini.trim().length()>0) {
			if (dataIniValCalendar.compareTo(dataAIniValCalendar)>0) {
				Check=false;
				msg = "Data inizio validità da superiore a data inizio validità a. ";
			}
		}
		if (tx_dval_fin.trim().length()>0 && tx_dval_a_fin.trim().length()>0) {
			if (dataFinValCalendar.compareTo(dataAFinValCalendar)>0) {
				Check=false;
				msg = msg.concat("Data fine validità da superiore a data fine validità a. ");
			}
		}
		if (tx_dval_ini.trim().length()>0 && tx_dval_a_fin.trim().length()>0) {
			if (dataIniValCalendar.compareTo(dataAFinValCalendar)>0) {
				Check=false;
				msg = msg.concat("Data inizio validità da superiore a data fine validità a. ");
			}
		}
		//Introdotti ulteriori controlli
		if (tx_dval_ini.trim().length()>0 && tx_dval_fin.trim().length()>0) {
			if (dataIniValCalendar.compareTo(dataFinValCalendar)>0) {
				Check=false;
				msg = msg.concat("Data inizio validità da superiore a data fine validità da. ");
			}
		}
		if (tx_dval_a_ini.trim().length()>0 && tx_dval_a_fin.trim().length()>0) {
			if (dataAIniValCalendar.compareTo(dataAFinValCalendar)>0) {
				Check=false;
				msg = msg.concat("Data inizio validità a superiore a data fine validità a. ");
			}
		}
		if (tx_dval_a_ini.trim().length()>0 && tx_dval_fin.trim().length()>0) {
			if (dataAIniValCalendar.compareTo(dataFinValCalendar)>0) {
				Check=false;
				msg = msg.concat("Data inizio validità a superiore a data fine validità da. ");
			}
		}
		
		MercatoPageList mercatoPageList = new MercatoPageList();
		
		if (Check) {
			//inizio LP PG21XX04 Leak
			Connection conn = null;
			//fine LP PG21XX04 Leak
			try {
				//inizio LP PG21XX04 Leak
				//configurazioneCompensoDAO = MercatoDAOFactory.getConfigurazioneCompenso(getMercatoDataSource(), getMercatoDbSchema());
				conn = getMercatoDataSource().getConnection();
				configurazioneCompensoDAO = MercatoDAOFactory.getConfigurazioneCompenso(conn, getMercatoDbSchema());
				//fine LP PG21XX04 Leak

				mercatoPageList = configurazioneCompensoDAO.ListConfigurazioneCompenso(configurazioneCompenso, rowsPerPage, pageNumber, "");
			
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
			mercatoPageList.setRetCode("04");
			mercatoPageList.setMessage(msg);
		}
		
		return mercatoPageList;
		
	}

	@SuppressWarnings("unchecked")
	private String listaCompensoCSV(HttpServletRequest request) {
		String listaCSV = "";
		ConfigurazioneCompensoDAO configurazioneCompensoDAO;
		ConfigurazioneCompenso configurazioneCompenso = new ConfigurazioneCompenso();
		configurazioneCompenso.setCodiceSocieta(codiceSocieta);
		configurazioneCompenso.setCuteCute(cutecute);
		configurazioneCompenso.setChiaveEnte(chiaveEnte);
		String tx_dval_ini = getDataByPrefix("tx_dval_ini",request);
		Calendar dataIniValCalendar  = GenericsDateNumbers.getCalendarFromDateString(tx_dval_ini, "yyyy-MM-dd");
		configurazioneCompenso.setDataInizioValidita(dataIniValCalendar);
		configurazioneCompenso.setAttribute("tx_dval_ini", tx_dval_ini);
		String tx_dval_a_ini = getDataByPrefix("tx_dval_a_ini",request);
		Calendar dataAIniValCalendar  = GenericsDateNumbers.getCalendarFromDateString(tx_dval_a_ini, "yyyy-MM-dd");
		configurazioneCompenso.setDataAInizioValidita(dataAIniValCalendar);
		configurazioneCompenso.setAttribute("tx_dval_a_ini", tx_dval_a_ini);
		String tx_dval_fin = getDataByPrefix("tx_dval_fin",request);
		Calendar dataFinValCalendar  = GenericsDateNumbers.getCalendarFromDateString(tx_dval_fin, "yyyy-MM-dd");
		configurazioneCompenso.setDataFineValidita(dataFinValCalendar);
		configurazioneCompenso.setAttribute("tx_dval_fin", tx_dval_fin);
		String tx_dval_a_fin = getDataByPrefix("tx_dval_a_fin",request);
		Calendar dataAFinValCalendar  = GenericsDateNumbers.getCalendarFromDateString(tx_dval_a_fin, "yyyy-MM-dd");
		configurazioneCompenso.setDataFineValidita(dataAFinValCalendar);		
		configurazioneCompenso.setAttribute("tx_dval_a_fin", tx_dval_a_fin);
		
		ArrayList<ConfigurazioneCompenso> configurazioneCompensoList = null;
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneCompensoDAO = MercatoDAOFactory.getConfigurazioneCompenso(getMercatoDataSource(), getMercatoDbSchema());
			conn = getMercatoDataSource().getConnection();
			configurazioneCompensoDAO = MercatoDAOFactory.getConfigurazioneCompenso(conn, getMercatoDbSchema());
			//fine LP PG21XX04 Leak
			configurazioneCompensoList = configurazioneCompensoDAO.listCompenso(configurazioneCompenso);
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
		if (configurazioneCompensoList.size()>0) {
			listaCSV = "Importo Fisso;Percentuale Compenso;Percentuale IVA;Data Inizio Validità; Data Fine Validità\r\n";
		}
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		for (Iterator iterator = configurazioneCompensoList.iterator(); iterator.hasNext();) {
			ConfigurazioneCompenso item = (ConfigurazioneCompenso) iterator.next();
			listaCSV = listaCSV + item.getImportoFisso().toString().replace(".", ",") + ";" + (new BigDecimal(item.getPercentualeCompenso()).setScale(2,BigDecimal.ROUND_HALF_UP)).toString().replace(".", ",")  
			+ ";" + (new BigDecimal(item.getPercentualeIva()).setScale(2,BigDecimal.ROUND_HALF_UP)).toString().replace(".", ",") + ";" + format1.format(item.getDataInizioValidita().getTime()) + ";";
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
				chiaveEnte = codici[1];
				cutecute = codici[1];
				chiaveEnte = codici[2];
				request.setAttribute("tx_societa", codiceSocieta);
				request.setAttribute("tx_utente", chiaveEnte);
				request.setAttribute("tx_ente", chiaveEnte);
			}
		
		}
	}
	
	
}

