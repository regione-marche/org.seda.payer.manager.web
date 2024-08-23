package org.seda.payer.manager.walletconfig.actions;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.seda.j2ee5.jndi.JndiProxy;
import com.seda.j2ee5.jndi.JndiProxyException;
import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;

import com.seda.data.page.Page;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.ConfigurazioneEvoIntimazioni;
import com.seda.payer.core.wallet.bean.ConfigurazioneSolleciti;
import com.seda.payer.core.wallet.bean.Servizio;
import com.seda.payer.core.wallet.bean.Solleciti;
import com.seda.payer.core.wallet.bean.Wallet;
import com.seda.payer.core.wallet.bean.WalletPageList;
import com.seda.payer.core.wallet.dao.ConfigurazioneEvoIntimazioniDAO;
import com.seda.payer.core.wallet.dao.ConfigurazioneSollecitiDAO;
import com.seda.payer.core.wallet.dao.ServizioDAO;
import com.seda.payer.core.wallet.dao.WalletDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;
import com.seda.payer.core.wallet.dao.WalletDAOImpl;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaFlussiRendRequestType;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaFlussiRendResponseType;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaFlussiRendResponseTypePageInfo;
import com.seda.payer.pgec.webservice.commons.dati.ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.tag.core.DdlOption;


import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.Messages;

import org.seda.payer.manager.walletmanager.actions.WalletBaseManagerAction;

import sun.awt.image.PixelConverter.Bgrx;

/*
 * --------------------------------------------------------------------
 * VBRUNO - 5 agosto 2011
 * Modificato il caricamento della DDL UtentiEnti per tenere conto
 * dell'opzione "Multiutente" dello user loggato
 * -------------------------------------------------------------------
 */

public class EvoluzioneIntimazioneAction extends WalletBaseManagerAction{

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
				if ( session.getAttribute("messaggio.recordInsert")!=null) {
					setFormMessage("form_selezione", session.getAttribute("messaggio.recordInsert").toString(), request);
				}
			session.removeAttribute("recordInsert");
		}
		
		if ((String) session.getAttribute("recordUpdate") != null) 
		{
			firedButton = firedButton.TX_BUTTON_CERCA; 
			if(session.getAttribute("recordUpdate").toString().equals("OK"))
				setFormMessage("form_selezione", Messages.UPDT_OK.format(), request);
			else
				if (session.getAttribute("messaggio.recordUpdate")!=null){
					setFormMessage("form_selezione", session.getAttribute("messaggio.recordUpdate").toString(), request);
				}
			
			session.removeAttribute("recordUpdate");
		}
		if ((String) session.getAttribute("recordDelete") != null) 
		{
			firedButton = firedButton.TX_BUTTON_CERCA; 
			if(session.getAttribute("recordDelete").toString().equals("OK"))
				setFormMessage("form_selezione", Messages.CANC_OK.format(), request);
			else
				if (session.getAttribute("messaggio.recordDelete")!=null){
					setFormMessage("form_selezione", session.getAttribute("messaggio.recordDelete").toString(), request);
				}
			
			session.removeAttribute("recordDelete");
		}
		
		
		//loadStaticXml_DDL(request, session);
		loadSocietaUtenteEnteXml_DDL(request, session);
		
		String siglaProvincia = ((String)request.getAttribute("tx_provincia") == null) ? "" : (String)request.getAttribute("tx_provincia");
		
		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		
		getServiziDDL_All(request);
		switch(firedButton)
		{
			
			case TX_BUTTON_CERCA: 
				try {
					dividiSocUtenteEnte(request);
					//loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
					WalletPageList walletPageListlist = listaConfigurazioneEvoluzioneIntimazione(request);
					
					PageInfo pageInfo = walletPageListlist.getPageInfo();
					
					if (walletPageListlist.getRetCode()!="00") {
						setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
					} else {
						if(pageInfo != null)
						{
							if(pageInfo.getNumRows() > 0)
							{
								request.setAttribute("lista_configurazioni_evoluzioni", walletPageListlist.getWalletListXml());
								request.setAttribute("lista_configurazioni_evoluzioni.pageInfo", pageInfo);
							}
							else {
								request.setAttribute("lista_configurazioni_evoluzioni", null);

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
				request.setAttribute("tx_data_pag_da", null);
				request.setAttribute("tx_data_pag_a", null);
				request.setAttribute("tx_data_cre_da", null);
				request.setAttribute("tx_data_cre_a", null);
				setProfile(request);
				
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), "", getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), true);
				break;
				
			case TX_BUTTON_SOCIETA_CHANGED:
				String societa = getParamCodiceSocieta();
				loadProvinciaXml_DDL(request, session, societa,true);
				//LoadListaUtentiEntiXml_DDL(request, session, societa, (societa.equals("") ? "" : siglaProvincia), (societa.equals("") ? "" : getParamCodiceEnte()), (societa.equals("") ? "" : getParamCodiceUtente()), true);
				//LoadListaUtentiEntiXml_DDL(request, session, societa, (societa.equals("") ? "" : siglaProvincia), (societa.equals("") ? "" : getParamCodiceEnte()), (societa.equals("") ? "" : (isMultiUtenteEnabled() ? "" : getParamCodiceUtente())), true);
				//loadTipologiaServizioXml_DDL(request, session, societa, true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				loadListaGatewayXml_DDL(request, session, societa, (societa.equals("") ? "" : getParamCodiceUtente()), true);
				request.setAttribute("tx_provincia","");
				request.setAttribute("tx_UtenteEnte", "");
				break;
				
			case TX_BUTTON_PROVINCIA_CHANGED:
				loadProvinciaXml_DDL(request, session, null,false);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, (siglaProvincia.equals("") ? "" : getParamCodiceEnte()), (siglaProvincia.equals("") ? "" : getParamCodiceUtente()), true);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, (siglaProvincia.equals("") ? "" : getParamCodiceEnte()), (siglaProvincia.equals("") ? "" : (isMultiUtenteEnabled() ? "" : getParamCodiceUtente())), true);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				request.setAttribute("tx_UtenteEnte", "");
				break;
			
			case TX_BUTTON_ENTE_CHANGED:
				loadProvinciaXml_DDL(request, session, "",false);
				LoadListaUtentiEntiXml_DDL(request, session, "" , "", "", "", false);
				//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				break;
				
				
			case TX_BUTTON_NULL: 
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), true);
				break;
				
			case TX_BUTTON_STAMPA:
				break;
				
			case TX_BUTTON_DOWNLOAD:
				break;
		}
		return null;
	}
 
	
	private WalletPageList listaConfigurazioneEvoluzioneIntimazione(HttpServletRequest request) {
		ConfigurazioneEvoIntimazioniDAO configurazioneEvoIntimazioniDAO;
		ConfigurazioneEvoIntimazioni  ConfigurazioneEvoIntimazioni = new ConfigurazioneEvoIntimazioni();
		ConfigurazioneEvoIntimazioni.setCodiceSocieta(codiceSocieta);
		ConfigurazioneEvoIntimazioni.setCuteCute(cutecute);
		ConfigurazioneEvoIntimazioni.setChiaveEnte(chiaveEnte);
		//ConfigurazioneEvoIntimazioni.setImportoResiduo((BigDecimal)request.getAttribute("tx_codiceoneresollecito"));
		ConfigurazioneEvoIntimazioni.setSmsSollecito((String)request.getAttribute("tx_sms_sollecito"));
		String dataDa = "1900-01-01";
		String dataA = "2100-01-01";
		if(request.getAttribute("tx_validita_da") !=null)
		{
			dataDa = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_validita_da"), "yyyy-MM-dd");
		}
			
		if(request.getAttribute("tx_validita_a") !=null)
		{
			dataA = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_validita_a"), "yyyy-MM-dd");
		}
		
		ConfigurazioneEvoIntimazioni.setAttribute(ConfigurazioneSollecitiDAO.VALIDITA_DA, dataDa);
		ConfigurazioneEvoIntimazioni.setAttribute(ConfigurazioneSollecitiDAO.VALIDITA_A, dataA);
		
		ConfigurazioneEvoIntimazioni.setFlagSollecitoCartaceo((String)request.getAttribute("tx_flg_sollecito_cartaceo"));
		ConfigurazioneEvoIntimazioni.setTipoServizio((String)request.getAttribute("tx_tipoServizio_search"));
		//ConfigurazioneEvoIntimazioni.setIntervalloGiorniEvoluzione((Integer)request.getAttribute("tx_intervallo_gg"));
		
		WalletPageList walletPageList = new WalletPageList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneEvoIntimazioniDAO = WalletDAOFactory.getConfigurazioneEvoIntimazione(getWalletDataSource(), getWalletDbSchema());
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			configurazioneEvoIntimazioniDAO = WalletDAOFactory.getConfigurazioneEvoIntimazione(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			walletPageList = configurazioneEvoIntimazioniDAO.ListConfigurazioneEvoIntimazioni(ConfigurazioneEvoIntimazioni, rowsPerPage, pageNumber, "");
			
		} catch (DaoException e1) {
			e1.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		catch (JndiProxyException e1) {
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
		return walletPageList;
		
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
				chiaveEnte = codici[2];
				request.setAttribute("tx_societa", codiceSocieta);
				request.setAttribute("tx_utente", chiaveEnte);
				request.setAttribute("tx_ente", chiaveEnte);
			}
		
		}
	}
	@SuppressWarnings("unchecked")
	private void getServiziDDL_All(HttpServletRequest request) {
	//creo ddl option:
	ArrayList<Servizio> listaServizi = null;
	List<DdlOption> serviziDDLList  = new ArrayList<DdlOption>();
	ServizioDAO servizioDAO;
	//inizio LP PG21XX04 Leak
	Connection conn = null;
	//fine LP PG21XX04 Leak
	try {
		//inizio LP PG21XX04 Leak
		//servizioDAO = WalletDAOFactory.getServizioDAO(getWalletDataSource(), getWalletDbSchema());
		conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
		servizioDAO = WalletDAOFactory.getServizioDAO(conn, getWalletDbSchema());
		//fine LP PG21XX04 Leak
		listaServizi = servizioDAO.listServizi();
		
		DdlOption optionServizio = new DdlOption();
		optionServizio.setSValue("  ");
		optionServizio.setSText("Tutti");
		serviziDDLList.add(optionServizio);
		
		for (Iterator iterator = listaServizi.iterator(); iterator.hasNext();) {
			Servizio servizio = (Servizio) iterator.next();
			optionServizio = new DdlOption();
			optionServizio.setSValue(servizio.getCodiceServizio());
			optionServizio.setSText(servizio.getDescrizioneServizio());
			serviziDDLList.add(optionServizio);
		}
	} catch (DaoException e1) {
		e1.printStackTrace();
	}
	//inizio LP PG21XX04 Leak
	catch (JndiProxyException e1) {
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
	request.setAttribute("serviziDDLList_All", serviziDDLList);
}

	
}

