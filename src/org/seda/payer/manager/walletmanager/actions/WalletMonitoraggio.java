package org.seda.payer.manager.walletmanager.actions;

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
import javax.sql.rowset.WebRowSet;

import com.seda.j2ee5.jndi.JndiProxy;
import com.seda.j2ee5.jndi.JndiProxyException;
import org.omg.CORBA.Request;
import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;

import com.seda.commons.string.Convert;
import com.seda.data.page.Page;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.Servizio;
import com.seda.payer.core.wallet.bean.Wallet;
import com.seda.payer.core.wallet.bean.WalletPageList;
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
import com.sun.rowset.internal.WebRowSetXmlReader;


import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;

import org.seda.payer.manager.ws.WSCache;

/*
 * --------------------------------------------------------------------
 * VBRUNO - 5 agosto 2011
 * Modificato il caricamento della DDL UtentiEnti per tenere conto
 * dell'opzione "Multiutente" dello user loggato
 * -------------------------------------------------------------------
 */

public class WalletMonitoraggio extends WalletBaseManagerAction {

	private static final long serialVersionUID = 1L;
	private String operatore="";
	private String codiceSocieta="";
	private String cutecute="";
	private String chiaveEnte="";
	private String walletListCsv;

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
		
		loadSocietaUtenteEnteXml_DDL(request, session);
		
//		String codiceTransazione = ((String)request.getAttribute("tx_codice_transazione") == null) ? "" : (String)request.getAttribute("tx_codice_transazione");
//		String codiceCanale = ((String)request.getAttribute("tx_canale_pagamento") == null) ? "" : (String)request.getAttribute("tx_canale_pagamento");
//		String chiaveRendicontazione = ((String)request.getAttribute("tx_chiave_rendicontazione") == null) ? "" : (String)request.getAttribute("tx_chiave_rendicontazione");
//		String codiceGateway = ((String)request.getAttribute("tx_codice_gateway") == null) ? "" : (String)request.getAttribute("tx_codice_gateway");
//		Calendar cal_dataPagamentoDa = (Calendar)request.getAttribute("tx_data_pag_da");
//		Calendar cal_dataPagamentoA = (Calendar)request.getAttribute("tx_data_pag_a");
		//String dataPagamentoDa = formatDate(cal_dataPagamentoDa, "yyyy-MM-dd");
		//String dataPagamentoA = formatDate(cal_dataPagamentoA, "yyyy-MM-dd");
		//String codiceTipologiaServizio = ((String)request.getAttribute("tx_tipologia_servizio") == null) ? "" : (String)request.getAttribute("tx_tipologia_servizio");
		
		//String codiceTipologiaServizio = getTipologiaServizio(request,session);
		
		//String codiceStrumento = ((String)request.getAttribute("tx_strumento") == null) ? "" : (String)request.getAttribute("tx_strumento");
		//String tipoFlusso = ((String)request.getAttribute("tx_tipo_flusso") == null) ? "" : (String)request.getAttribute("tx_tipo_flusso");
		Calendar cal_dataCreazioneDa = (Calendar)request.getAttribute("tx_data_cre_da");
		Calendar cal_dataCreazioneA = (Calendar)request.getAttribute("tx_data_cre_a");
		//String dataCreazioneDa = formatDate(cal_dataCreazioneDa, "yyyy-MM-dd");
		//String dataCreazioneA = formatDate(cal_dataCreazioneA, "yyyy-MM-dd");
		String siglaProvincia = ((String)request.getAttribute("tx_provincia") == null) ? "" : (String)request.getAttribute("tx_provincia");
		
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		operatore = user.getUserName();
		
		if ((String) session.getAttribute("recordInsert") != null) {
			if(session.getAttribute("recordInsert").toString().equals("OK"))
				setFormMessage("form_selezione", Messages.INS_OK.format(), request);
			else
				setFormMessage("form_selezione", session.getAttribute("messaggio.recordInsert").toString(), request);
			
			session.removeAttribute("recordInsert");
		}
		
		if (session.getAttribute("recordUpdate") != null) {
			if(session.getAttribute("recordUpdate").toString().equals("OK"))
				setFormMessage("form_selezione", Messages.UPDT_OK.format(), request);
			else
				setFormMessage("form_selezione", session.getAttribute("messaggio.recordUpdate").toString(), request);
			
			session.removeAttribute("recordUpdate");
		}

		if (session.getAttribute("recordDelete") != null) {
			if(session.getAttribute("recordDelete").toString().equals("OK"))
				setFormMessage("form_selezione", Messages.CANC_OK.format(), request);
			else
				setFormMessage("form_selezione", session.getAttribute("messaggio.recordDelete").toString(), request);
			
			session.removeAttribute("recordDelete");
		}
		if (request.getAttribute("tx_daanacont") != null) {
			if (request.getAttribute("tx_daanacont").equals("Y")) {
				firedButton = FiredButton.TX_BUTTON_CERCA;
			}
		}
		
		switch(firedButton) {
			case TX_BUTTON_CERCA: 
				try {
					dividiSocUtenteEnte(request);
					loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
					//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), false);
					loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), false);
					loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), false);
					//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);
					LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), false);
					WalletPageList walletPageListlist = listWallet(request,false);
					
					PageInfo pageInfo = walletPageListlist.getPageInfo();
					
					if (walletPageListlist.getRetCode()!="00") {
						setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
					} else {
						if(pageInfo != null) {
							if(pageInfo.getNumRows() > 0) {
								session.setAttribute("numRows",pageInfo.getNumRows()); 
//								String walletRiepilogoListXml = walletRiepilogoList(request);
								String walletRiepilogoListXml = walletPageListlist.getWalletListXmlRiep();
								request.setAttribute("lista_wallet", walletPageListlist.getWalletListXml());
								request.setAttribute("lista_wallet.pageInfo", pageInfo);
								request.setAttribute("lista_wallet_riepilogo", walletRiepilogoListXml);
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
				request.setAttribute("tx_periodo_da",null);
				request.setAttribute("tx_periodo_a",null);
				
				setProfile(request);
				siglaProvincia="";
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), true);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), true);
				break;
				
			case TX_BUTTON_SOCIETA_CHANGED:
				String societa = getParamCodiceSocieta();
				loadProvinciaXml_DDL(request, session, societa,true);
				//LoadListaUtentiEntiXml_DDL(request, session, societa, (societa.equals("") ? "" : siglaProvincia), (societa.equals("") ? "" : getParamCodiceEnte()), (societa.equals("") ? "" : getParamCodiceUtente()), true);
				LoadListaUtentiEntiXml_DDL(request, session, societa, (societa.equals("") ? "" : siglaProvincia), (societa.equals("") ? "" : getParamCodiceEnte()), (societa.equals("") ? "" : (isMultiUtenteEnabled() ? "" : getParamCodiceUtente())), true);
				//loadTipologiaServizioXml_DDL(request, session, societa, true);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), true);
				loadListaGatewayXml_DDL(request, session, societa, (societa.equals("") ? "" : getParamCodiceUtente()), true);
				request.setAttribute("tx_provincia","");
				request.setAttribute("tx_UtenteEnte", "");
				break;
				
			case TX_BUTTON_PROVINCIA_CHANGED:
				loadProvinciaXml_DDL(request, session, null,false);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, (siglaProvincia.equals("") ? "" : getParamCodiceEnte()), (siglaProvincia.equals("") ? "" : getParamCodiceUtente()), true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, (siglaProvincia.equals("") ? "" : getParamCodiceEnte()), (siglaProvincia.equals("") ? "" : (isMultiUtenteEnabled() ? "" : getParamCodiceUtente())), true);
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
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), true);
				break;
				
			case TX_BUTTON_STAMPA:
				break;
				
			case TX_BUTTON_DOWNLOAD:
				request.setAttribute("download", "Y");
				dividiSocUtenteEnte(request);
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
				//loadTipologiaServizioXml_DDL(request, session, getParamCodiceSocieta(), false);
				loadTipologiaServizioXml_DDL_2(request, session, getParamCodiceSocieta(),getParamCodiceUtente(),getParamCodiceEnte(), false);
				loadListaGatewayXml_DDL(request, session, getParamCodiceSocieta(), getParamCodiceUtente(), false);
				//LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), (isMultiUtenteEnabled() ? "" : getParamCodiceUtente()), false);
				
				Integer maxRigheDownload = (Integer) context.getAttribute(ManagerKeys.MAXRIGHE_DOWNLOAD_BORSELLINO);
				if ((Integer) session.getAttribute("numRows") <= maxRigheDownload ){
					//Tk 2017033088000055 20170406 - inizio
//					listWallet(request,true);
//					Calendar cal = Calendar.getInstance();
//					String timestamp = GenericsDateNumbers.formatCalendarData(cal, "yyyyMMdd_HHmmss");
//					request.setAttribute("nome_file_csv",timestamp + "_MonitoraggioWallet.txt");
//					request.setAttribute("monitoraggio_wallet_csv", walletListCsv);
					//Controllare che il range di date selezionate per il download non superi l'anno scolastico AAAA-09-01/AAAA+1-06-30
					//String dataDa = "1900-01-01";
					//String dataA = "2100-01-01";
					String dataDa = "";
					String dataA = "";
					if(request.getAttribute("tx_periodo_da") !="")
					{
						dataDa = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_periodo_da"), "yyyy-MM-dd");
					}
						
					if(request.getAttribute("tx_periodo_a") !="")
					{
						dataA = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_periodo_a"), "yyyy-MM-dd");
					}
					//Inserire controllo tra date
					if (dataDa.trim().equals("") || dataA.trim().equals("")) {
						setFormMessage("form_selezione", Messages.RANGE_DATA_DWN_NON_VALORIZZATO.format(), request);
						request.setAttribute("download", "N");
						break;
					} else {
						if (dataDa.trim().compareTo(dataA.trim()) > 0) {
							setFormMessage("form_selezione", Messages.VALORIZZAZIONE_RANGE_DA_A.format(""), request);
							request.setAttribute("download", "N");
							break;
						} else {
							if (checkRangeData(dataDa,dataA)) {
								listWallet(request,true);
								Calendar cal = Calendar.getInstance();
								String timestamp = GenericsDateNumbers.formatCalendarData(cal, "yyyyMMdd_HHmmss");
								request.setAttribute("nome_file_csv",timestamp + "_MonitoraggioWallet.txt");
								request.setAttribute("monitoraggio_wallet_csv", walletListCsv);
							} else {
								setFormMessage("form_selezione", Messages.SUPERATO_RANGE_DATA_DWN.format(), request);
								request.setAttribute("download", "N");
								break;
							}
						}
					}
					//Tk 2017033088000055 20170406 - fine
				} else {
					setFormMessage("form_selezione", Messages.SUPERATO_MAXRIGHE.format(), request);
					request.setAttribute("download", "N");
					break;
				}
				break;
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
			conn = getWalletDataSource().getConnection();
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
		request.setAttribute("serviziDDLList", serviziDDLList);
		//session.setAttribute("serviziDDLList", serviziDDLList);
	}
	
	private String walletRiepilogoList(HttpServletRequest request) {
		String walletRiepilogoListXml = null;
		WalletDAO walletDAO;
		Wallet wallet = new Wallet();  

		wallet.setCodiceSocieta(codiceSocieta);
		wallet.setCuteCute(cutecute);
		wallet.setChiaveEnte(chiaveEnte);
		wallet.setIdWallet(((String)request.getAttribute("tx_idwallet") == null) ? "" : (String)request.getAttribute("tx_idwallet"));
		wallet.setCodiceFiscaleGenitore(((String)request.getAttribute("tx_codicefiscalegenitore") == null) ? "" : (String)request.getAttribute("tx_codicefiscalegenitore"));
		String dataDa = "1900-01-01";
		String dataA = "01-01-2100";
		if(request.getAttribute("tx_periodo_da") !="") {
			dataDa = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_periodo_da"), "yyyy-MM-dd");
		}
			
		if(request.getAttribute("tx_periodo_a") !="") {
			dataA = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_periodo_a"), "yyyy-MM-dd");
		}
		
		wallet.setAttribute(WalletDAO.PERIDOCARICO_DA , dataDa);
		wallet.setAttribute(WalletDAO.PERIDOCARICO_A , dataA);
		String tipoServizio = ((String)request.getAttribute("tx_tipologia_servizio") == null) ? "" : (String)request.getAttribute("tx_tipologia_servizio");
		String tipoSollecito = ((String)request.getAttribute("tx_tiposollecito") == null) ? "" : (String)request.getAttribute("tx_tiposollecito");
		String flagrendicontato = ((String)request.getAttribute("tx_flagrendicontato") == null) ? "" : (String)request.getAttribute("tx_flagrendicontato");
		String presenzaOneri = ((String)request.getAttribute("tx_presenzaoneri") == null) ? "" : (String)request.getAttribute("tx_presenzaoneri");
		String FlagEsclusioneSMSCortesia = (String)request.getAttribute("flagEsclusioneSMSCortesia");
		if (FlagEsclusioneSMSCortesia!= null ) {
			if (FlagEsclusioneSMSCortesia.equals("Y"))
				wallet.setFlagEsclusioneSMSCortesia(true);
			else	 {
				if (FlagEsclusioneSMSCortesia.equals("N")) {
					wallet.setFlagEsclusioneSMSCortesia(false);
				} else  {
				wallet.setFlagEsclusioneSMSCortesia(null);
				}
			}
		}
		
		String FlagEsclusioneSMSSollecito = (String)request.getAttribute("flagEsclusioneSMSSollecito");
		if (FlagEsclusioneSMSSollecito!= null ) {
			if (FlagEsclusioneSMSSollecito.equals("Y"))
			wallet.setFlagEsclusioneSMSSollecito(true);
			else {
				if (FlagEsclusioneSMSSollecito.equals("N")) {
				wallet.setFlagEsclusioneSMSSollecito(false);
				} else  {
				wallet.setFlagEsclusioneSMSSollecito(null);
				}
			}
		}
			
		
		String FlagEsclusioneSollecitoCartaceo = (String)request.getAttribute("flagEsclusioneSollecitoCartaceo");
		if(FlagEsclusioneSollecitoCartaceo != null) {
			if ( FlagEsclusioneSollecitoCartaceo.equals("Y"))
				wallet.setFlagEsclusioneSollecitoCartaceo(true);
			else {
				if (FlagEsclusioneSollecitoCartaceo.equals("N")) {
					wallet.setFlagEsclusioneSollecitoCartaceo(false);
				} else  {
					wallet.setFlagEsclusioneSollecitoCartaceo(null);
				}
			}
		}
		
		String FlagEsclusioneEvoluzioneIntimazione = (String)request.getAttribute("flagEsclusioneEvoluzioneIntimazione");
		if (FlagEsclusioneEvoluzioneIntimazione != null) {
			if (FlagEsclusioneEvoluzioneIntimazione != null && FlagEsclusioneEvoluzioneIntimazione.equals("Y"))
				wallet.setFlagEsclusioneEvoluzioneIntimazione(true);
			else {
				if (FlagEsclusioneEvoluzioneIntimazione.equals("N")) {
					wallet.setFlagEsclusioneEvoluzioneIntimazione(false);
				} else  {
					wallet.setFlagEsclusioneEvoluzioneIntimazione(null);
				}
			}
		}
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
			conn = getWalletDataSource().getConnection();
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			walletRiepilogoListXml = walletDAO.walletRiepilogo(wallet, tipoServizio, tipoSollecito, flagrendicontato, presenzaOneri);
			
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
		return walletRiepilogoListXml;
	}
	
	private WalletPageList listWallet(HttpServletRequest request, boolean download ) {
		WalletDAO walletDAO;
		Wallet wallet = new Wallet();
		wallet.setCodiceSocieta(codiceSocieta);
		wallet.setCuteCute(cutecute);
		wallet.setChiaveEnte(chiaveEnte);
		wallet.setIdWallet(((String)request.getAttribute("tx_idwallet") == null) ? "" : (String)request.getAttribute("tx_idwallet"));
		wallet.setCodiceFiscaleGenitore(((String)request.getAttribute("tx_codicefiscalegenitore") == null) ? "" : (String)request.getAttribute("tx_codicefiscalegenitore"));
		String dataDa = "1900-01-01";
		String dataA = "01-01-2100";
		if(request.getAttribute("tx_periodo_da") !="") {
			dataDa = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_periodo_da"), "yyyy-MM-dd");
			request.setAttribute("data_carico_da", dataDa);		//Tk 2017033088000055
		}
			
		if(request.getAttribute("tx_periodo_a") !="") {
			dataA = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_periodo_a"), "yyyy-MM-dd");
			request.setAttribute("data_carico_a", dataA);		//Tk 2017033088000055
		}
		
		wallet.setAttribute(WalletDAO.PERIDOCARICO_DA , dataDa);
		wallet.setAttribute(WalletDAO.PERIDOCARICO_A , dataA);
		String tipoServizio = ((String)request.getAttribute("tx_tipologia_servizio") == null) ? "" : (String)request.getAttribute("tx_tipologia_servizio");
		request.setAttribute("tipologia_servizio", tipoServizio); 	//Tk 2017033088000055
		String tipoSollecito = ((String)request.getAttribute("tx_tiposollecito") == null) ? "" : (String)request.getAttribute("tx_tiposollecito");
		String flagrendicontato = ((String)request.getAttribute("tx_flagrendicontato") == null) ? "" : (String)request.getAttribute("tx_flagrendicontato");
		String presenzaOneri = ((String)request.getAttribute("tx_presenzaoneri") == null) ? "" : (String)request.getAttribute("tx_presenzaoneri");
		
		String FlagEsclusioneSMSCortesia = (String)request.getAttribute("flagEsclusioneSMSCortesia");
		if (FlagEsclusioneSMSCortesia!= null ) {
			if (FlagEsclusioneSMSCortesia.equals("Y"))
				wallet.setFlagEsclusioneSMSCortesia(true);
			else	 {
				if (FlagEsclusioneSMSCortesia.equals("N")) {
					wallet.setFlagEsclusioneSMSCortesia(false);
				} else  {
				wallet.setFlagEsclusioneSMSCortesia(null);
				}
			}
		}
		
		String FlagEsclusioneSMSSollecito = (String)request.getAttribute("flagEsclusioneSMSSollecito");
		if (FlagEsclusioneSMSSollecito!= null ) {
			if (FlagEsclusioneSMSSollecito.equals("Y"))
			wallet.setFlagEsclusioneSMSSollecito(true);
			else {
				if (FlagEsclusioneSMSSollecito.equals("N")) {
				wallet.setFlagEsclusioneSMSSollecito(false);
				} else  {
				wallet.setFlagEsclusioneSMSSollecito(null);
				}
			}
		}
			
		
		String FlagEsclusioneSollecitoCartaceo = (String)request.getAttribute("flagEsclusioneSollecitoCartaceo");
		if(FlagEsclusioneSollecitoCartaceo != null) {
			if ( FlagEsclusioneSollecitoCartaceo.equals("Y"))
				wallet.setFlagEsclusioneSollecitoCartaceo(true);
			else {
				if (FlagEsclusioneSollecitoCartaceo.equals("N")) {
					wallet.setFlagEsclusioneSollecitoCartaceo(false);
				} else  {
					wallet.setFlagEsclusioneSollecitoCartaceo(null);
				}
			}
		}
		
		String FlagEsclusioneEvoluzioneIntimazione = (String)request.getAttribute("flagEsclusioneEvoluzioneIntimazione");
		if (FlagEsclusioneEvoluzioneIntimazione != null) {
			if (FlagEsclusioneEvoluzioneIntimazione != null && FlagEsclusioneEvoluzioneIntimazione.equals("Y"))
				wallet.setFlagEsclusioneEvoluzioneIntimazione(true);
			else {
				if (FlagEsclusioneEvoluzioneIntimazione.equals("N")) {
					wallet.setFlagEsclusioneEvoluzioneIntimazione(false);
				} else  {
					wallet.setFlagEsclusioneEvoluzioneIntimazione(null);
				}
			}
		}
		
		// paginazione ed ordinamento
		int rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		int pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		String order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");

		WalletPageList walletPageList = new WalletPageList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			//inizio LP PG21XX04 Leak
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			
			if (download) {
				walletListCsv = walletDAO.walletListCsv2(wallet, tipoServizio, tipoSollecito, flagrendicontato, presenzaOneri, 0, 0, order).toString();
			} else {
				walletPageList = walletDAO.walletList(wallet, tipoServizio, tipoSollecito, flagrendicontato, presenzaOneri, rowsPerPage, pageNumber, order);
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
		return walletPageList;		
	}
	
	private void dividiSocUtenteEnte(HttpServletRequest request) {
		// TODO Auto-generated method stub
		if(request.getAttribute("ddlSocietaUtenteEnte")!=null) {
			String ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
			if (!ddlSocietaUtenteEnte.equals("")) {
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
	
	//Tk 2017033088000055 20170406 - inizio
	private boolean checkRangeData(String dataDa, String dataA) {
		boolean validRange = true;
		Calendar calDataDa = GenericsDateNumbers.getCalendarFromDateString(dataDa, "yyyy-MM-dd");
		Calendar calDataA = GenericsDateNumbers.getCalendarFromDateString(dataA, "yyyy-MM-dd");
		calDataDa.add(Calendar.YEAR, 1); 
		if (calDataA.compareTo(calDataDa)>=0) {
			validRange = false;
		}
		return validRange;
	}
	//Tk 2017033088000055 20170406 - fine

}

