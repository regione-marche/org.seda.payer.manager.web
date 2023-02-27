package org.seda.payer.manager.mercatoconfig.actions;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.GenericsDateNumbers;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.mercato.bean.ConfigurazioneAreaMercantile;
import com.seda.payer.core.mercato.bean.MercatoPageList;
import com.seda.payer.core.mercato.dao.ConfigurazioneAreaMercantileDAO;
import com.seda.payer.core.mercato.dao.MercatoDAOFactory;

import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.mercatomanager.actions.MercatoBaseManagerAction;


public class AreaMercantileAction extends MercatoBaseManagerAction{

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
		
		loadSocietaUtenteEnteXml_DDL(request, session);
		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		
		switch(firedButton)
		{
			
			case TX_BUTTON_CERCA: 
				try {
					dividiSocUtenteEnte(request);
					MercatoPageList mercatoPageListlist = listaConfigurazioneAreaMercantile(request);
					
					PageInfo pageInfo = mercatoPageListlist.getPageInfo();
					
					if (mercatoPageListlist.getRetCode()!="00") {
						setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
					} else {
						if(pageInfo != null)
						{
							if(pageInfo.getNumRows() > 0)
							{
								request.setAttribute("lista_configurazioni_areamerc", mercatoPageListlist.getMercatoListXml());
								request.setAttribute("lista_configurazioni_areamerc.pageInfo", pageInfo);
							}
							else { 
								request.setAttribute("lista_configurazioni_areamerc", null);
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
				request.setAttribute("tx_descr_area_merc", null);
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
				file = listaAreaMercantileCSV(request);
				Calendar cal = Calendar.getInstance();
				String timestamp = GenericsDateNumbers.formatCalendarData(cal, "yyyyMMdd_HHmmss");
				request.setAttribute("filename",timestamp + "_AreeMercantili.csv");
				request.setAttribute("fileCsv", file);				
				break;
		}
		return null;
	}
	
	private MercatoPageList listaConfigurazioneAreaMercantile(HttpServletRequest request) {
		ConfigurazioneAreaMercantileDAO configurazioneAreaMercantileDAO;
		ConfigurazioneAreaMercantile configurazioneAreaMercantile = new ConfigurazioneAreaMercantile();
		if (request.getAttribute("vista").equals("areamercantileLista")) {
			request.setAttribute("vista", "areamercantile");
			replyAttributes(false, request,"pageNumber","rowsPerPage","order");
			request.setAttribute("vista", "areamercantileLista");
		}				
		configurazioneAreaMercantile.setCodiceSocieta(codiceSocieta);
		configurazioneAreaMercantile.setCuteCute(cutecute);
		configurazioneAreaMercantile.setChiaveEnte(chiaveEnte);
		configurazioneAreaMercantile.setCodiceAreaMercantile((String)request.getAttribute("tx_codiceareamerc"));
		String descr = (String)request.getAttribute("tx_desc_area");
		if (descr==null) {
			configurazioneAreaMercantile.setDescrizioneAreaMercantile("");
		} else {
			descr = descr.trim();
			configurazioneAreaMercantile.setDescrizioneAreaMercantile(descr.toUpperCase());
		}
		
		MercatoPageList mercatoPageList = new MercatoPageList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneAreaMercantileDAO = MercatoDAOFactory.getConfigurazioneAreaMercantile(getMercatoDataSource(), getMercatoDbSchema());
			conn = getMercatoDataSource().getConnection();
			configurazioneAreaMercantileDAO = MercatoDAOFactory.getConfigurazioneAreaMercantile(conn, getMercatoDbSchema());
			//fine LP PG21XX04 Leak
			mercatoPageList = configurazioneAreaMercantileDAO.ListConfigurazioneAreaMercantile(configurazioneAreaMercantile, rowsPerPage, pageNumber, "");
			
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
	private String listaAreaMercantileCSV(HttpServletRequest request) {
		String listaCSV = "";
		ConfigurazioneAreaMercantileDAO configurazioneAreaMercantileDAO;
		ConfigurazioneAreaMercantile configurazioneAreaMercantile = new ConfigurazioneAreaMercantile();
		configurazioneAreaMercantile.setCodiceSocieta(codiceSocieta);
		configurazioneAreaMercantile.setCuteCute(cutecute);
		configurazioneAreaMercantile.setChiaveEnte(chiaveEnte);
		configurazioneAreaMercantile.setCodiceAreaMercantile((String)request.getAttribute("tx_codiceareamerc"));
		String descr = (String)request.getAttribute("tx_desc_area");
		if (descr==null) {
			configurazioneAreaMercantile.setDescrizioneAreaMercantile("");
		} else {
			descr = descr.trim();
			configurazioneAreaMercantile.setDescrizioneAreaMercantile(descr.toUpperCase());
		}
		//configurazioneAreaMercantile.setDescrizioneAreaMercantile(descr.toUpperCase());
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
		if (configurazioneAreaMercantileList.size()>0) {
			listaCSV = "Codice Area Mercantile;Descrizione Area Mercantile;Data Inizio Validità; Data Fine Validità\r\n";
		}
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		for (Iterator iterator = configurazioneAreaMercantileList.iterator(); iterator.hasNext();) {
			ConfigurazioneAreaMercantile item = (ConfigurazioneAreaMercantile) iterator.next();
			listaCSV = listaCSV + item.getCodiceAreaMercantile() + ";" + item.getDescrizioneAreaMercantile() 
			+ ";" + format1.format(item.getDataInizioValidita().getTime()) + ";";
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
				//chiaveEnte = codici[1];
				cutecute = codici[1];
				chiaveEnte = codici[2];
				request.setAttribute("tx_societa", codiceSocieta);
				request.setAttribute("tx_utente", chiaveEnte);
				request.setAttribute("tx_ente", chiaveEnte);
			}
		
		}
	}
	
	
}

