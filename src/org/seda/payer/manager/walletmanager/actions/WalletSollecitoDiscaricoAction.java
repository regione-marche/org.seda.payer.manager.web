package org.seda.payer.manager.walletmanager.actions;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.jndi.JndiProxy;
import com.seda.j2ee5.jndi.JndiProxyException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.Servizio;
import com.seda.payer.core.wallet.bean.Tributo;
import com.seda.payer.core.wallet.bean.Wallet;
import com.seda.payer.core.wallet.bean.WalletPageList;
import com.seda.payer.core.wallet.dao.ServizioDAO;
import com.seda.payer.core.wallet.dao.TributoDAO;
import com.seda.payer.core.wallet.dao.WalletDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;
import com.seda.tag.core.DdlOption;


import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;


public class WalletSollecitoDiscaricoAction extends WalletBaseManagerAction {

	private static final long serialVersionUID = 1L;
	private String codiceSocieta="";
	private String cutecute="";
	private String chiaveEnte="";


	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
				
		HttpSession session = request.getSession();
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		/*
		 * Chiamo il metodo per i settaggi relativi al profilo utente
		 */
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) 
			setProfile(request);
		/*
		 * Carico le DDl statiche
		 */
		loadStaticXml_DDL(request, session);
		getServiziDDL(request, session);
		getTributiDDL(request, session);
		loadSocietaUtenteEnteXml_DDL(request, session);
		
		String siglaProvincia = ((String)request.getAttribute("tx_provincia") == null) ? "" : (String)request.getAttribute("tx_provincia");
		
		
		
		
		switch(firedButton)
		{
			case TX_BUTTON_CERCA: 
				try {
					dividiSocUtenteEnte(request);
					loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
					//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), false);
					//loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), false);
					//loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), false);
					//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);
					LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), false);
					WalletPageList walletPageListlist = listWalletRicariche(request,false);
					
					
					PageInfo pageInfo = walletPageListlist.getPageInfo();
					
					if (walletPageListlist.getRetCode()!="00") {
						setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
					} else {
						if(pageInfo != null)
						{
							if(pageInfo.getNumRows() > 0)
							{
								session.setAttribute("numRows",pageInfo.getNumRows());
								request.setAttribute("lista_wallet", walletPageListlist.getWalletListXml());
								request.setAttribute("lista_wallet_riepilogo", walletPageListlist.getWalletListXmlRiep());
								request.setAttribute("lista_wallet.pageInfo", pageInfo);
							}
							else setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
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
				request.setAttribute("tx_periodo_da", null);
				request.setAttribute("tx_periodo_a", null);
				request.setAttribute("tx_periodoDis_da", null);
				request.setAttribute("tx_periodoDis_a", null);
				
				
				setProfile(request);
				siglaProvincia="";
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				//loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), true);
				break;
				
			case TX_BUTTON_SOCIETA_CHANGED:
				String societa = getParamCodiceSocieta();
				loadProvinciaXml_DDL(request, session, societa,true);
				//LoadListaUtentiEntiXml_DDL(request, session, societa, (societa.equals("") ? "" : siglaProvincia), (societa.equals("") ? "" : getParamCodiceEnte()), (societa.equals("") ? "" : getParamCodiceUtente()), true);
				LoadListaUtentiEntiXml_DDL(request, session, societa, (societa.equals("") ? "" : siglaProvincia), (societa.equals("") ? "" : getParamCodiceEnte()), (societa.equals("") ? "" : (isMultiUtenteEnabled() ? "" : getParamCodiceUtente())), true);
				//loadTipologiaServizioXml_DDL(request, session, societa, true);
//				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
//				loadListaGatewayXml_DDL(request, session, societa, (societa.equals("") ? "" : getParamCodiceUtente()), true);
				request.setAttribute("tx_provincia","");
				request.setAttribute("tx_UtenteEnte", "");
				break;
				
			case TX_BUTTON_PROVINCIA_CHANGED:
				loadProvinciaXml_DDL(request, session, null,false);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, (siglaProvincia.equals("") ? "" : getParamCodiceEnte()), (siglaProvincia.equals("") ? "" : getParamCodiceUtente()), true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, (siglaProvincia.equals("") ? "" : getParamCodiceEnte()), (siglaProvincia.equals("") ? "" : (isMultiUtenteEnabled() ? "" : getParamCodiceUtente())), true);
//				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
//				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				request.setAttribute("tx_UtenteEnte", "");
				break;
			
			case TX_BUTTON_ENTE_CHANGED:
				loadProvinciaXml_DDL(request, session, "",false);
				LoadListaUtentiEntiXml_DDL(request, session, "" , "", "", "", false);
				//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
//				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				
//				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				break;
				
				
			case TX_BUTTON_NULL: 
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
//				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
//				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), true);
				break;
				
			case TX_BUTTON_STAMPA:
				break;
			/*	
			case TX_BUTTON_DOWNLOAD:
				try {
					request.setAttribute("download", "Y");
					dividiSocUtenteEnte(request);
					loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
//					loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), false);
//					loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), false);
					LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), false);
					
					Integer maxRigheDownload = (Integer) context.getAttribute(ManagerKeys.MAXRIGHE_DOWNLOAD);
					if ((Integer) session.getAttribute("numRows") <= maxRigheDownload ){
						listWalletRicariche(request,true);
					} else {
						setFormMessage("form_selezione", Messages.SUPERATO_MAXRIGHE.format(), request);
						request.setAttribute("download", "N");
						break;
					}
					
					
					
					
					
					
				} catch(Exception e) {
					setErrorMessage(e.getMessage());
					e.printStackTrace();
				}
				break;*/
		}
		return null;
	}
 
		

	
	@SuppressWarnings("unchecked")
	private void getServiziDDL(HttpServletRequest request, HttpSession session) {
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
			
			for (Iterator iterator = listaServizi.iterator(); iterator.hasNext();) {
				Servizio servizio = (Servizio) iterator.next();
				DdlOption optionServizio = new DdlOption();
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
		request.setAttribute("serviziDDLList", serviziDDLList);
		//session.setAttribute("serviziDDLList", serviziDDLList);
	}
	
	private void getTributiDDL(HttpServletRequest request, HttpSession session) {
		//creo ddl option:
		ArrayList<Tributo> listaTributi = null;
		List<DdlOption> tributiDDLList  = new ArrayList<DdlOption>();
		TributoDAO tributoDAO;
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//tributoDAO = WalletDAOFactory.getTributoDAO(getWalletDataSource(), getWalletDbSchema());
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			tributoDAO = WalletDAOFactory.getTributoDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			
			listaTributi = tributoDAO.listTributo();
			
			for (Iterator iterator = listaTributi.iterator(); iterator.hasNext();) {
				Tributo tribito = (Tributo) iterator.next();
				DdlOption optionServizio = new DdlOption();
				if( !tribito.getCodiceTributo().substring(0,3).equals("000")){
					optionServizio.setSValue(tribito.getCodiceTributo());
					optionServizio.setSText(tribito.getDescrizioneTributo());
					tributiDDLList.add(optionServizio);
				}
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
		request.setAttribute("tributiDDLList", tributiDDLList);
	}
	
	private String walletServizioRiepilogoList(HttpServletRequest request) {
		String walletServizioRiepilogoListXml = null;
		WalletDAO walletDAO;
		Wallet wallet = new Wallet();
		wallet.setCodiceSocieta(codiceSocieta);
		wallet.setCuteCute(cutecute);
		wallet.setChiaveEnte(chiaveEnte);
		wallet.setCodiceFiscaleGenitore((String)request.getAttribute("tx_codicefiscalegenitore"));
		wallet.setIdWallet((String)request.getAttribute("tx_codiceborsellino"));
		String dataDa = "1900-01-01";
		String dataA = "01-01-2100";
		if(request.getAttribute("tx_periodo_da") !="")
		{
			dataDa = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_periodo_da"), "yyyy-MM-dd");
		}
			
		if(request.getAttribute("tx_periodo_a") !="")
		{
			dataA = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_periodo_a"), "yyyy-MM-dd");
		}
		
		wallet.setAttribute(WalletDAO.PERIDOCARICO_DA , dataDa);
		wallet.setAttribute(WalletDAO.PERIDOCARICO_A , dataA);
		String tipoServizio = (String)request.getAttribute("tx_tipologia_servizio");
		String flagrendicontato = (String)request.getAttribute("tx_flagrendicontato");
		String codice_tributo = (String)request.getAttribute("tx_codice_tributo");
		
		// paginazione ed ordinamento
		int rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		int pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		String order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			
			walletServizioRiepilogoListXml = walletDAO.walletServizioRiepilogo(wallet, tipoServizio, codice_tributo, flagrendicontato, rowsPerPage, pageNumber, order);
			
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
		return walletServizioRiepilogoListXml;
	}
	private WalletPageList listWalletRicariche(HttpServletRequest request,boolean download) {
		WalletDAO walletDAO;
		Wallet wallet = new Wallet();
		wallet.setCodiceSocieta(codiceSocieta);
		wallet.setCuteCute(cutecute);
		wallet.setChiaveEnte(chiaveEnte); 
		wallet.setIdWallet((String)(request.getAttribute("tx_codiceborsellino")==null ? "" : request.getAttribute("tx_codiceborsellino")));
		wallet.setCodiceFiscaleGenitore(((String)(request.getAttribute("tx_codicefiscalegenitore")==null ? "" : request.getAttribute("tx_codicefiscalegenitore"))).toUpperCase());
		wallet.setDenominazioneGenitore(((String)(request.getAttribute("tx_denominazione")==null ? "" : request.getAttribute("tx_denominazione"))).toUpperCase());
		String dataDa = "1900-01-01";
		String dataA = "01-01-2100"; 
		String dataDisDa = "1900-01-01";
		String dataDisA = "01-01-2100";
		if(request.getAttribute("tx_periodo_da") !="")
		{
			dataDa = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_periodo_da"), "yyyy-MM-dd");
			wallet.setAttribute("PGBDATA_PAGAMENTO_DA" , dataDa);
			request.setAttribute("data_pagamento_da", dataDa);
		}
			
		if(request.getAttribute("tx_periodo_a") !="")
		{
			dataA = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_periodo_a"), "yyyy-MM-dd");
			wallet.setAttribute("PGBDATA_PAGAMENTO_A" , dataA);
			request.setAttribute("data_pagamento_a", dataA);
		}
		
		
		if(request.getAttribute("tx_periodoDis_da") !="")
		{
			dataDisDa = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_periodoDis_da"), "yyyy-MM-dd");
			wallet.setAttribute("PGBDATADIS_PAGAMENTO_DA" , dataDisDa);
			request.setAttribute("data_pagamentoDis_da", dataDisDa);
		}
			
		if(request.getAttribute("tx_periodoDis_a") !="")
		{
			dataDisA = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_periodoDis_a"), "yyyy-MM-dd");
			wallet.setAttribute("PGBDATADIS_PAGAMENTO_A" , dataDisA);
			request.setAttribute("data_pagamentoDis_a", dataDisA);
		}
		
		String caricoEnte="";
		if(request.getAttribute("tx_flagDiscaricoSollecito")!=null){
			caricoEnte=(String) request.getAttribute("tx_flagDiscaricoSollecito");
		}
		
		String discaricoAttivo="";
		if(request.getAttribute("tx_flagDiscaricoAttivo")!=null){
			discaricoAttivo=(String) request.getAttribute("tx_flagDiscaricoAttivo");
		}
		
		if(caricoEnte.trim().equals("S")){
			caricoEnte="Y";
		}
		wallet.setAttribute("caricoEnte",caricoEnte);
		wallet.setAttribute("discaricoAttivo", discaricoAttivo);
		// paginazione ed ordinamento
		int rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		int pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		String order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
		
		
		WalletPageList walletPageList = new WalletPageList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			
			/*if (download) {
				String walletListCsv = walletDAO.walletRicaricheListCsv(wallet, 0, 0, order).toString();
				Calendar cal = Calendar.getInstance();
				String timestamp = GenericsDateNumbers.formatCalendarData(cal, "yyyyMMdd_HHmmss");
				request.setAttribute("nome_file_csv",timestamp + "_MonitoraggioWalletRicariche.txt");
				request.setAttribute("monitoraggio_walletRicariche_csv", walletListCsv);
				
			} else  {*/ 
				walletPageList = walletDAO.walletSollecitoList(wallet, rowsPerPage, pageNumber, order);
				
			/*}*/
			 
			
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
				cutecute = codici[1];
				chiaveEnte = codici[2];
				//request.setAttribute("tx_societa", codiceSocieta);
				//request.setAttribute("tx_utente", chiaveEnte);
				//request.setAttribute("tx_ente", chiaveEnte);
			}
		
		}
	}

}


