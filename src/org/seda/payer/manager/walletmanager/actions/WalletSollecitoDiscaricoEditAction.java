package org.seda.payer.manager.walletmanager.actions;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PresenzeAbilitate;
import org.seda.payer.manager.walletmanager.actions.WalletBaseManagerAction;


import com.seda.commons.security.TokenGenerator;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.AnagraficaFiglioMense;
import com.seda.payer.core.wallet.bean.PresenzeGiornaliere;
import com.seda.payer.core.wallet.bean.Wallet;
import com.seda.payer.core.wallet.bean.ConfigurazioneRaccordoPagonet;
import com.seda.payer.core.wallet.bean.EsitoRisposte;
import com.seda.payer.core.wallet.bean.Servizio;
import com.seda.payer.core.wallet.bean.WalletPageList;
import com.seda.payer.core.wallet.dao.AnagraficaFiglioMenseDAO;
import com.seda.payer.core.wallet.dao.ConfigurazioneRaccordoPagonetDAO;
import com.seda.payer.core.wallet.dao.PresenzeGiornaliereDAO;
import com.seda.payer.core.wallet.dao.ServizioDAO;
import com.seda.payer.core.wallet.dao.WalletDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;

import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest2;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest3;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.tag.core.DdlOption;

import groovyjarjarantlr4.v4.parse.ANTLRParser.throwsSpec_return;


@SuppressWarnings("serial")
public class WalletSollecitoDiscaricoEditAction extends WalletBaseManagerAction{
	private static String codop = "edit";
	
	private String codiceSocieta = "";
	private String codiceUtente = "";
	private String chiaveEnte = "";
	private String operatore = "";
	
	public static enum FiredButtonSollecito {
		TX_BUTTON_DISCARICO,
		TX_BUTTON_ANNULLAMENTO,
		TX_BUTTON_ANNULLA,
		TX_BUTTON_DISCARICA,
		TX_BUTTON_NOACTION,
		TX_BUTTON_PRESENZE,
		TX_BUTTON_CALENDARIO
	}
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		
		HttpSession session = request.getSession();
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		//replyAttributes(false, request,"pageNumber","rowsPerPage","order"); 
		/*
		 * Individuo la richiesta dell'utente
		 */
		//action può essere o sollecito o presenze
		String action=getCurrentAction(request);
		
		//gestisciParametriDatagrid(request);
		//
		
		HttpSession sessionlocal = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		
		replyHeaderAttributes(request);
		operatore = user.getUserName();
		FiredButton firedButton = getFiredButton(request);
		FiredButtonSollecito firedButtonSollecito=getFiredButtonSollecito(request);
        if(request.getParameter("tx_presenze_action")!=null&&(((String)request.getParameter("tx_presenze_action")).equals("refresh")
        		||((String)request.getParameter("tx_presenze_action")).equals("calendario")
        		||((String)request.getParameter("tx_presenze_action")).equals("presenze"))){
        	firedButton=FiredButton.TX_BUTTON_NULL;
        	//firedButtonSollecito=FiredButtonSollecito.TX_BUTTON_NOACTION;
		}
		switch(firedButtonSollecito)
		{
	    case TX_BUTTON_DISCARICO:
	    	if(action.trim().equals("sollecito")){
	    	discaricaSollecito(request);
	    	}
	    	
		break;
		case TX_BUTTON_ANNULLAMENTO:
			if(action.trim().equals("sollecito")){
			annullaSollecito(request);
			}
		break;	
		
		case TX_BUTTON_DISCARICA:
			if(action.trim().equals("sollecito")){
			insertRowDiscarico(request);
			}else if(action.trim().equals("calendario")){
			saveConfermaParameters(request);	
			//TX_BUTTON_RICHIESTA_CONFERMA	
			firedButton=FiredButton.TX_BUTTON_RESET;
			request.setAttribute("tx_button_annulla",null);
			request.setAttribute("tx_button_discarica",null);
			request.setAttribute("tx_button_presenze",null);
			request.setAttribute("tx_button_richiesta_conferma","tx_button_richiesta_conferma");
			insertRowDiscaricoPresenze(request);	
			}
			break;
		
		case TX_BUTTON_ANNULLA:
			if(action.trim().equals("sollecito")){
			insertRowAnnullamento(request);
			}else if(action.trim().equals("calendario")){
			saveConfermaParameters(request);
			//TX_BUTTON_RICHIESTA_CONFERMA
			firedButton=FiredButton.TX_BUTTON_RESET;
			request.setAttribute("tx_button_richiesta_conferma","tx_button_richiesta_conferma");
			request.setAttribute("tx_button_annulla",null);
			request.setAttribute("tx_button_discarica",null);
			request.setAttribute("tx_button_presenze",null);
			insertRowDiscaricoPresenze(request);	
			}
			break;
		
		
		}
		switch(firedButton)
		{ 
		case TX_BUTTON_RESET:
			break;
		case TX_BUTTON_NULL:
			
			/*
			 * Carico le info del borsellino
			 */
			codop = "edit";
			String idWallet = (String)request.getAttribute("IdwalletInfo");
			if(idWallet==null){
				idWallet=(String) request.getAttribute("tx_idwallet_h");
			}
			dividiSocUtenteEnte(request);
			request.setAttribute("tx_idwallet_h",idWallet);
			
			if(idWallet==null){
				idWallet=(String) request.getAttribute("tx_idwallet_h");
			}
			if ( !idWallet.equals("") && !idWallet.equals("")) {
				
				request.setAttribute("id_wallet_sollecito",idWallet);
				try {
					String walletListaSolleciti = "";
					listaDettSollecito(request,idWallet);
					walletListaSolleciti = listaSolleciti(request,idWallet);
					
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setFormMessage("form_selezione", Messages.GENERIC_ERROR.format(), request);
				}

				
			}
			
			//se l'azione è sollecito
			if(action.trim().equals("sollecito")){
			String azioneSollecito=(String)request.getAttribute("azione_sollecito");
			String progSollecito=(String)request.getAttribute("prog_sollecito");
			String dataSollecito=(String)request.getAttribute("data_sollecito");
			if(azioneSollecito!=null){
				request.setAttribute("prog_sollecito",progSollecito);
				request.setAttribute("data_sollecito",dataSollecito);
				if(azioneSollecito.trim().equals("discarico")){
					
					request.setAttribute("Show_Dialog_Discarico",true);
					String chosenRow=selectSolleciti(request,dataSollecito,idWallet,progSollecito);
					if(request.getAttribute("caricoEnte")!=null){
					request.setAttribute("tx_flagDiscaricoSollecito",request.getAttribute("caricoEnte"));
					}
					//lista dei solleciti
				}
				if(azioneSollecito.trim().equals("annullamento")){
					request.setAttribute("Show_Dialog_Annullamento",true);
					String chosenRow=selectSolleciti(request,dataSollecito,idWallet,progSollecito);
					if(request.getAttribute("caricoEnte")!=null){
						request.setAttribute("tx_flagDiscaricoSollecito",request.getAttribute("caricoEnte"));
						}
				}
			}else{
				request.setAttribute("Show_Dialog_Discarico",false);
				request.setAttribute("Show_Dialog_Annullamento",false);
			}
			}
			if(action.trim().equals("presenze")){
				//TX_BUTTON_PRESENZE
				request.setAttribute("flagPresenzeCalendario", false);
				request.setAttribute("tx_button_presenze","tx_button_presenze");
				setCalendarioRequestParameters(request);
				loadListPresenzeDatagrid(request);
			}
			
			if(action.trim().equals("calendario")){
				//TX_BUTTON_PRESENZE
				request.setAttribute("flagPresenzeCalendario", true);
				request.setAttribute("tx_button_presenze","tx_button_presenze");
				request.setAttribute("tx_idwallet_h", request.getParameter("IdwalletInfo"));
				loadListCalendarioDatagrid(request);
			}
			
			if(action.trim().equals("applica_discarico")||action.trim().equals("annulla_discarico")){
				//TX_BUTTON_RICHIESTA_CONFERMA
				request.setAttribute("flagPresenzeCalendario", true);
				request.setAttribute("tx_button_richiesta_conferma","tx_button_richiesta_conferma");
				saveConfermaParameters(request);
				
			}
			
			break;
		
		case TX_BUTTON_CERCA:
			if(action.trim().equals("presenze")){
			request.setAttribute("flagPresenzeCalendario", false);
			request.setAttribute("tx_button_presenze","tx_button_presenze");
			loadListCalendarioDatagrid(request);
			}
			break;
			
		case TX_BUTTON_EDIT:
			try {
				updateWalletFlagEsclusione(request);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setFormMessage("frmIndietro", "Errore: " + e.getLocalizedMessage(), request);
			}
			session.setAttribute("recordUpdate", "OK");
			//setFormMessage("form_selezione", Messages.UPDT_OK.format(), request);
			break;
		
		
			
		case TX_BUTTON_INDIETRO:
			//request.setAttribute("tx_button_cerca", "Search");	
			break;
		
		case TX_BUTTON_RICHIESTA_CONFERMA:
			saveConfermaParameters(request);
			//TX_BUTTON_PRESENZE
			request.setAttribute("flagPresenzeCalendario", true);
			//request.setAttribute("tx_button_presenze","tx_button_presenze");
			request.setAttribute("tx_button_calendario","tx_button_calendario");
			loadListCalendarioDatagrid(request);
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
		// TODO Auto-generated method stub
		if(request.getAttribute("ddlSocietaUtenteEnte")!=null)
		{
		String ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
		if (!ddlSocietaUtenteEnte.equals(""))
		{
			String[] codici = ddlSocietaUtenteEnte.split("\\|");
			codiceSocieta = codici[0];
			codiceUtente = codici[1];
			chiaveEnte = codici[2];
			request.getSession().setAttribute("tx_societa", codiceSocieta);
			request.getSession().setAttribute("tx_utente", codiceUtente);
			request.getSession().setAttribute("tx_ente", chiaveEnte);
		}
		}
	}

	private Wallet selezionaWallet(HttpServletRequest request, String IdWallet) throws DaoException {
		WalletDAO walletDAO;
		Wallet wallet = new Wallet(); 
		wallet.setCodiceSocieta(codiceSocieta);
		wallet.setCuteCute(codiceUtente);
		wallet.setChiaveEnte(chiaveEnte);
		wallet.setIdWallet(IdWallet);
		//inizio LP PG21XX04 Leak
		//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
		//wallet = walletDAO.select(wallet);
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			wallet = walletDAO.select(wallet);
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
		request.setAttribute("chk_FlagEsclusioneSMSCortesia",  wallet.isFlagEsclusioneSMSCortesia());
		request.setAttribute("chk_FlagEsclusioneSMSSollecito",  wallet.isFlagEsclusioneSMSSollecito());
		request.setAttribute("chk_FlagEsclusioneSollecitoCartaceo",  wallet.isFlagEsclusioneSollecitoCartaceo());
		request.setAttribute("chk_FlagEsclusioneEvoluzioneIntimazione",  wallet.isFlagEsclusioneEvoluzioneIntimazione());
		WalletPageList walletPageListServiziWallet = listServiziWallet(request,IdWallet);
		PageInfo pageInfo = walletPageListServiziWallet.getPageInfo();
		if (walletPageListServiziWallet.getRetCode()!="00") {
			setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
		} 
		else 
		{
			request.setAttribute("lista_servizi_wallet", walletPageListServiziWallet.getWalletListXml());
		}
		
		return wallet;
		
	}
	
	private void listaDettSollecito(HttpServletRequest request, String idWallet) throws DaoException { 
		
		WalletDAO walletDAO;
		WalletPageList  walletRicaricheDett ;
		//inizio LP PG21XX04 Leak
		//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
		//fine LP PG21XX04 Leak
		Wallet wallet = new Wallet();
		wallet.setIdWallet(idWallet);
		
		String dataDa = "";
		String dataA = "";
		String dataDisDa = "";
		String dataDisA = "";
		if(request.getAttribute("data_da") !=null)
		{
			if(request.getAttribute("data_da") !=""){
			wallet.setAttribute("PGBDATA_PAGAMENTO_DA" , request.getAttribute("data_da").toString());
			}
		}
			
		if(request.getAttribute("data_a") !=null)
		{
			if(request.getAttribute("data_a") !=""){
			wallet.setAttribute("PGBDATA_PAGAMENTO_A" , request.getAttribute("data_a").toString());
			}
		}
		if(request.getAttribute("dataDis_da") !=null)
		{
			if(request.getAttribute("dataDis_da") !=""){
			wallet.setAttribute("PGBDATADIS_PAGAMENTO_DA" , request.getAttribute("dataDis_da").toString());
			}
		}
			
		if(request.getAttribute("dataDis_a") !=null)
		{
			if(request.getAttribute("dataDis_a") !=""){
			wallet.setAttribute("PGBDATADis_PAGAMENTO_A" , request.getAttribute("dataDis_a").toString());
			}
		}
		
		ArrayList<String> resultArr = new ArrayList<String>();
		//inizio LP PG21XX04 Leak
		//resultArr = walletDAO.arraySollecitiList(wallet, 1, 15, "");
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			resultArr = walletDAO.arraySollecitiList(wallet, 1, 15, "");
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
		
		if(resultArr.size()>0){
			request.setAttribute("codice_borsellino", resultArr.get(0));
			request.setAttribute("codice_fiscale", resultArr.get(1));
			request.setAttribute("denominazione", resultArr.get(2));
			request.setAttribute("RESIDUO", resultArr.get(3).replace(".", ","));
			
		}
	}
	
	private WalletPageList listServiziWallet(HttpServletRequest request, String IdWallet) {
		WalletDAO walletDAO;
		Wallet wallet = new Wallet();
		wallet.setCodiceSocieta(codiceSocieta);
		wallet.setCuteCute(codiceUtente);
		wallet.setChiaveEnte(chiaveEnte);
		wallet.setIdWallet(IdWallet);
		
		
		WalletPageList walletPageList = new WalletPageList();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
			conn = getWalletDataSource().getConnection();
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			walletPageList = walletDAO.ListaServiziWalletManager(wallet,null);	//TK 2017033088000055
			
		} catch (DaoException e1) {
			e1.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
	    catch (SQLException e1) {
	    	e1.printStackTrace();
	    }
		finally {
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
	
	
	private Integer updateWalletFlagEsclusione(HttpServletRequest request) throws DaoException {
		
		String  societa = (String)request.getAttribute("tx_societa");
		String  utente = (String)request.getAttribute("tx_utente");
		String  ente = (String)request.getAttribute("tx_ente");
		String idWallet = (String)request.getAttribute("tx_idwallet");
		
		WalletDAO walletDAO;
		Wallet wallet = new Wallet();
		wallet.setCodiceSocieta(societa);
		wallet.setCuteCute(utente);
		wallet.setChiaveEnte(ente);
		wallet.setIdWallet(idWallet);
		String FlagEsclusioneSMSCortesia = (String)request.getAttribute("FlagEsclusioneSMSCortesia");
		wallet.setFlagEsclusioneSMSCortesia(false);	
		wallet.setFlagEsclusioneSMSSollecito(false);
		wallet.setFlagEsclusioneSollecitoCartaceo(false);
		wallet.setFlagEsclusioneEvoluzioneIntimazione(false);
		if (FlagEsclusioneSMSCortesia!= null )
		{
			if (FlagEsclusioneSMSCortesia.equals("Y"))
				wallet.setFlagEsclusioneSMSCortesia(true);
			
		}
		
		String FlagEsclusioneSMSSollecito = (String)request.getAttribute("FlagEsclusioneSMSSollecito");
		if (FlagEsclusioneSMSSollecito!= null )
		{
			if (FlagEsclusioneSMSSollecito.equals("Y"))
			wallet.setFlagEsclusioneSMSSollecito(true);
			
		}
			
		
		String FlagEsclusioneSollecitoCartaceo = (String)request.getAttribute("FlagEsclusioneSollecitoCartaceo");
		if(FlagEsclusioneSollecitoCartaceo != null)
		{
			if ( FlagEsclusioneSollecitoCartaceo.equals("Y"))
				wallet.setFlagEsclusioneSollecitoCartaceo(true);
			
		}
		
		String FlagEsclusioneEvoluzioneIntimazione = (String)request.getAttribute("FlagEsclusioneEvoluzioneIntimazione");
		if (FlagEsclusioneEvoluzioneIntimazione != null)
		{
			if (FlagEsclusioneEvoluzioneIntimazione != null && FlagEsclusioneEvoluzioneIntimazione.equals("Y"))
				wallet.setFlagEsclusioneEvoluzioneIntimazione(true);
			
		}
		
		wallet.setOperatore(operatore);
		
		//inizio LP PG21XX04 Leak
		//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
		//int aggiornati = walletDAO.updateFlagEsclusione(wallet);
		//return aggiornati;
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			int aggiornati = walletDAO.updateFlagEsclusione(wallet);
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
	
	private String listaSolleciti(HttpServletRequest request, String idWallet) throws DaoException {
		WalletDAO walletDAO;
		Wallet wallet = new Wallet();
		String  walletListaSolleciti = "";
		wallet.setCodiceSocieta(codiceSocieta);
		wallet.setCuteCute(codiceUtente);
		wallet.setChiaveEnte(chiaveEnte);
		wallet.setIdWallet(idWallet);
		SimpleDateFormat sdf1=new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
		Date temp;
		wallet.setAttribute("PGBDATA_PAGAMENTO_DA" , "");
		wallet.setAttribute("PGBDATA_PAGAMENTO_A" , "");
		wallet.setAttribute("PGBDATADIS_PAGAMENTO_DA" ,"");
		wallet.setAttribute("PGBDATADIS_PAGAMENTO_A" , "");
		if(request.getAttribute("data_da") !=null)
		{    
			///data_da
			String data_da=(String) request.getAttribute("data_da");
			try {
			if(data_da.contains("/")){
				temp=sdf1.parse(data_da);
				data_da=sdf2.format(temp);
				}
			}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			if(request.getAttribute("data_da") !=""){
			wallet.setAttribute("PGBDATA_PAGAMENTO_DA" , data_da);
			}
		}
			
		if(request.getAttribute("data_a") !=null)
		{  
			String data_a=(String) request.getAttribute("data_a");
			try{
			if(data_a.contains("/")){
				temp=sdf1.parse(data_a);
				data_a=sdf2.format(temp);
				}
			}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			if(request.getAttribute("data_a") !=""){
			wallet.setAttribute("PGBDATA_PAGAMENTO_A" , data_a);
			}
		}
		
		
		if(request.getAttribute("dataDis_da") !=null)
		{    
			///data_da
			String data_da=(String) request.getAttribute("dataDis_da");
			try {
			if(data_da.contains("/")){
				temp=sdf1.parse(data_da);
				data_da=sdf2.format(temp);
				}
			}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			if(request.getAttribute("dataDis_da") !=""){
			wallet.setAttribute("PGBDATADIS_PAGAMENTO_DA" , data_da);
			}
		}
			
		if(request.getAttribute("dataDis_a") !=null)
		{  
			String data_a=(String) request.getAttribute("dataDis_a");
			try{
			if(data_a.contains("/")){
				temp=sdf1.parse(data_a);
				data_a=sdf2.format(temp);
				}
			}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			if(request.getAttribute("dataDis_a") !=""){
			wallet.setAttribute("PGBDATADIS_PAGAMENTO_A" , data_a);
			}
		}

		//inizio LP PG21XX04 Leak
		//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
		//walletListaSolleciti = walletDAO.listSolleciti(wallet,-1);
		//request.setAttribute("lista_dettaglio_solleciti", walletListaSolleciti);
		//return walletListaSolleciti;
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			walletListaSolleciti = walletDAO.listSolleciti(wallet,-1);
			request.setAttribute("lista_dettaglio_solleciti", walletListaSolleciti);
			return walletListaSolleciti;
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
	
	
	private String selectSolleciti(HttpServletRequest request,String dataSollecito, String idWallet,String progSollecito)  {
		WalletDAO walletDAO;
		Wallet wallet = new Wallet();
		String  walletSelectSolleciti = "";
		int prog=Integer.valueOf(progSollecito);
		wallet.setCodiceSocieta(codiceSocieta);
		wallet.setCuteCute(codiceUtente);
		wallet.setChiaveEnte(chiaveEnte);
		wallet.setIdWallet(idWallet);
//		wallet.setAttribute("PGBDATA_PAGAMENTO_DA" , dataSollecito);
//		wallet.setAttribute("PGBDATA_PAGAMENTO_A" ,dataSollecito);
		//wallet.setAttribute("PGBDATA_PAGAMENTO_DA" , " ");
		//wallet.setAttribute("PGBDATA_PAGAMENTO_A" ," ");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date data= null;
		try {
			data = sdf.parse(dataSollecito);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd");
		
		System.out.println("PGBDATA_PAGAMENTO_DA = " + sdfnew.format(data));
		System.out.println("PGBDATA_PAGAMENTO_A = " + sdfnew.format(data));
		wallet.setAttribute("PGBDATA_PAGAMENTO_DA" , sdfnew.format(data));
		wallet.setAttribute("PGBDATA_PAGAMENTO_A" ,sdfnew.format(data));
		
		
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
			conn = getWalletDataSource().getConnection();
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			walletSelectSolleciti = walletDAO.listSolleciti(wallet,prog);
		} catch (DaoException e) {
			e.printStackTrace();
			setFormMessage("frmIndietro", Messages.GENERIC_ERROR.format(), request);
		}
		//inizio LP PG21XX04 Leak
		catch (SQLException e) {
			e.printStackTrace();
			setFormMessage("frmIndietro", Messages.GENERIC_ERROR.format(), request);
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		request.setAttribute("select_chosen_sollecito", walletSelectSolleciti);
				
		return walletSelectSolleciti;
	}
	
	public void discaricaSollecito(HttpServletRequest request){
		request.setAttribute("IdwalletInfo",request.getAttribute("IdwalletInfo"));		
		request.setAttribute("tx_flagDiscaricoSollecito",request.getAttribute("tx_flagDiscaricoSollecito"));
		request.setAttribute("prog_sollecito",request.getAttribute("prog_sollecito"));
		request.setAttribute("data_sollecito", request.getAttribute("data_sollecito"));
		request.setAttribute("vista","data_sollecito");
		request.setAttribute("Show_Action_Discarico",true);
		request.setAttribute("Show_Action_Annullamento",false);
		
	}
	
	public void annullaSollecito(HttpServletRequest request){
		request.setAttribute("IdwalletInfo",request.getAttribute("IdwalletInfo"));		
		request.setAttribute("tx_flagAnnullamentoSollecito",request.getAttribute("tx_flagAnnullamentoSollecito"));
		request.setAttribute("prog_sollecito",request.getAttribute("prog_sollecito"));
		request.setAttribute("data_sollecito", request.getAttribute("data_sollecito"));
		request.setAttribute("vista","data_sollecito");
		request.setAttribute("Show_Action_Discarico",false);
		request.setAttribute("Show_Action_Annullamento",true);
		
	}
	
	protected FiredButtonSollecito getFiredButtonSollecito(HttpServletRequest request) {
		if (request.getAttribute("tx_button_discarico") != null )
			return FiredButtonSollecito.TX_BUTTON_DISCARICO;
		if (request.getAttribute("tx_button_annullamento") != null)
			return FiredButtonSollecito.TX_BUTTON_ANNULLAMENTO;
		if (request.getAttribute("tx_button_annulla") != null)
			return FiredButtonSollecito.TX_BUTTON_ANNULLA;
		if (request.getAttribute("tx_button_discarica") != null)
			return FiredButtonSollecito.TX_BUTTON_DISCARICA;
		if (request.getAttribute("tx_button_reset") != null)
			return FiredButtonSollecito.TX_BUTTON_NOACTION;
		if( request.getAttribute("tx_button_indietro")!=null){	
			 if(request.getAttribute("Show_Action_Discarico")!=null&&request.getAttribute("Show_Action_Discarico").equals("true")){
				request.setAttribute("azione_sollecito","discarico");
			 }else if(request.getAttribute("Show_Action_Annullamento")!=null&&request.getAttribute("Show_Action_Annullamento").equals("true")){
				request.setAttribute("azione_sollecito","annullamento");
			 }	
			 if(request.getAttribute("Sollecito_Annullamento_inseriti")!=null&&request.getAttribute("Sollecito_Annullamento_inseriti").equals("true")){
				 request.setAttribute("azione_sollecito",null);
			 }
			return FiredButtonSollecito.TX_BUTTON_NOACTION;
		}
		if (request.getAttribute("tx_button_presenze") != null)
			return FiredButtonSollecito.TX_BUTTON_PRESENZE;
		if (request.getAttribute("tx_button_calendario") != null)
			return FiredButtonSollecito.TX_BUTTON_CALENDARIO;
		return FiredButtonSollecito.TX_BUTTON_NOACTION;
	}

	
	public void insertRowAnnullamento(HttpServletRequest request){
		HttpSession session = request.getSession();
		WalletDAO walletDAO;
		Wallet wallet = new Wallet();
		String  walletListaSolleciti = "";
		wallet.setCodiceSocieta(codiceSocieta);
		wallet.setCuteCute(codiceUtente);
		wallet.setChiaveEnte(chiaveEnte);
		wallet.setIdWallet((String)request.getAttribute("IdwalletInfo"));
		String caricoEnteSoris=((String)request.getAttribute("tx_flagDiscaricoSollecito"));
		int prog=Integer.valueOf((String)request.getAttribute("prog_sollecito"));
		if(request.getAttribute("data_sollecito") !=null)
		{
			if(request.getAttribute("data_sollecito") !=""){
			wallet.setAttribute("PGBDATA_PAGAMENTO_DA" , request.getAttribute("data_sollecito").toString());
			}
		}
			
		if(request.getAttribute("data_sollecito") !=null)
		{
			if(request.getAttribute("data_sollecito") !=""){
			wallet.setAttribute("PGBDATA_PAGAMENTO_A" , request.getAttribute("data_sollecito").toString());
			}
		}
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date data= null;
		try {
			data = sdf.parse(request.getAttribute("data_sollecito").toString());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd");
		
		System.out.println("PGBDATA_PAGAMENTO_DA = " + sdfnew.format(data));
		System.out.println("PGBDATA_PAGAMENTO_A = " + sdfnew.format(data));
		wallet.setAttribute("PGBDATA_PAGAMENTO_DA" , sdfnew.format(data));
		wallet.setAttribute("PGBDATA_PAGAMENTO_A" ,sdfnew.format(data));
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
			conn = getWalletDataSource().getConnection();
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			List<String> sollecitoAsList=walletDAO.getSollecitoAsList(wallet);
			
			UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
			operatore = user.getCodiceFiscale();
			//sollecitoAsList.set(19, caricoEnteSoris); 
			sollecitoAsList.set(23, operatore.toUpperCase());
			request.setAttribute("SollecitoInsertEsito",true);
			
			//11052021 GG Reimposto la connessione in quanto precedentemente chiusa 
			conn = getWalletDataSource().getConnection();
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			
			walletDAO.insertRowAnnullamento(sollecitoAsList,prog);
		} catch (DaoException e) {
			e.printStackTrace();
			setFormMessage("delete_form", Messages.GENERIC_ERROR.format(), request);
			request.setAttribute("SollecitoInsertEsito",false);
		}
		//inizio LP PG21XX04 Leak
		catch (SQLException e) {
			e.printStackTrace();
			setFormMessage("delete_form", Messages.GENERIC_ERROR.format(), request);
			request.setAttribute("SollecitoInsertEsito",false);
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
	
	public void insertRowDiscarico(HttpServletRequest request){
		HttpSession session = request.getSession();
		WalletDAO walletDAO;
		Wallet wallet = new Wallet();
		String  walletListaSolleciti = "";
		wallet.setCodiceSocieta(codiceSocieta);
		wallet.setCuteCute(codiceUtente);
		wallet.setChiaveEnte(chiaveEnte);
		wallet.setIdWallet((String)request.getAttribute("IdwalletInfo"));
		String caricoEnteSoris=((String)request.getAttribute("tx_flagDiscaricoSollecito"));
		int prog=Integer.valueOf((String)request.getAttribute("prog_sollecito"));
		if(request.getAttribute("data_sollecito") !=null)
		{
			if(request.getAttribute("data_sollecito") !=""){
			wallet.setAttribute("PGBDATA_PAGAMENTO_DA" , request.getAttribute("data_sollecito").toString());
			}
		}
			
		if(request.getAttribute("data_sollecito") !=null)
		{
			if(request.getAttribute("data_sollecito") !=""){
			wallet.setAttribute("PGBDATA_PAGAMENTO_A" , request.getAttribute("data_sollecito").toString());
			}
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date data= null;
		try {
			data = sdf.parse(request.getAttribute("data_sollecito").toString());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SimpleDateFormat sdfnew = new SimpleDateFormat("yyyy-MM-dd");
		
		System.out.println("PGBDATA_PAGAMENTO_DA = " + sdfnew.format(data));
		System.out.println("PGBDATA_PAGAMENTO_A = " + sdfnew.format(data));
		wallet.setAttribute("PGBDATA_PAGAMENTO_DA" , sdfnew.format(data));
		wallet.setAttribute("PGBDATA_PAGAMENTO_A" ,sdfnew.format(data));

		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
			conn = getWalletDataSource().getConnection();
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			List<String> sollecitoAsList=walletDAO.getSollecitoAsList(wallet);
			
			UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
			operatore = user.getCodiceFiscale();
			
			sollecitoAsList.set(19, caricoEnteSoris);
			sollecitoAsList.set(23, operatore.toUpperCase());
			
			//11052021 GG Reimposto la connessione in quanto precedentemente chiusa
			conn = getWalletDataSource().getConnection();
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			
			walletDAO.insertRowDiscarico(sollecitoAsList,prog);
			request.setAttribute("SollecitoInsertEsito",true);
		} catch (DaoException e) {
			e.printStackTrace();
			setFormMessage("delete_form", Messages.GENERIC_ERROR.format(), request);
			request.setAttribute("SollecitoInsertEsito",false);
		}
		//inizio LP PG21XX04 Leak
		catch (SQLException e) {
			e.printStackTrace();
			setFormMessage("delete_form", Messages.GENERIC_ERROR.format(), request);
			request.setAttribute("SollecitoInsertEsito",false);
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
	
	public void insertRowDiscaricoPresenze(HttpServletRequest request){
		HttpSession session = request.getSession();
		
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		String operatoreInserimento=user.getUserName();
		
		PresenzeGiornaliere presenzeGiornaliere=new PresenzeGiornaliere();
		
		presenzeGiornaliere.setAttribute("operatoreInserimento", operatoreInserimento);
		
		String identificativoRecord = "";
		try {
			identificativoRecord = TokenGenerator.generateUUIDToken();
			presenzeGiornaliere.setIdentificativoRecord(identificativoRecord);
		} catch (NoSuchAlgorithmException e) {
			setFormMessage("delete_form", Messages.GENERIC_ERROR.format(), request);	
		}
		String causale=(String) request.getAttribute("causale");
		causale=setCausale(causale);
		String idWallet=(String) request.getAttribute("IdwalletInfo");
		String dataPresenza=(String) request.getAttribute("dataPresenza");
		String progressivo=(String) request.getAttribute("progressivo");
		int prog=Integer.valueOf(progressivo)+1;
		progressivo=String.valueOf(prog);
		presenzeGiornaliere.setCausale(causale);
		presenzeGiornaliere.setAttribute("progressivo", progressivo);
	    presenzeGiornaliere.setIdWallet(idWallet);
	    Calendar presenzaScuola=Calendar.getInstance();
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	    try {
			presenzaScuola.setTime(sdf.parse(dataPresenza));
		} catch (ParseException e1) {
			setFormMessage("delete_form", Messages.GENERIC_ERROR.format(), request);	
			e1.printStackTrace();
		}
	    presenzeGiornaliere.setPresenzaScuola(presenzaScuola);
	    PresenzeGiornaliereDAO presenzeGiornaliereDAO;
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//presenzeGiornaliereDAO = WalletDAOFactory.getPresenzeGiornaliereDAO(getWalletDataSource(), getWalletDbSchema());
			conn = getWalletDataSource().getConnection();
			presenzeGiornaliereDAO = WalletDAOFactory.getPresenzeGiornaliereDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			presenzeGiornaliere=presenzeGiornaliereDAO.insertDiscarico(presenzeGiornaliere);
			String errori=(String) presenzeGiornaliere.getAttribute("errori");
			if(errori.trim().equals("KO")){
			setFormMessage("delete_form", Messages.GENERIC_ERROR.format(), request);
			}else{
				if(causale.trim().equals("C")||causale.trim().equals("R")){
				request.setAttribute("SollecitoInsertEsito",true);	
				request.setAttribute("Show_Action_Discarico",true);
				request.setAttribute("tx_presenze_action","annulla_discarico");
				}else{
				request.setAttribute("SollecitoInsertEsito",true);	
				request.setAttribute("Show_Action_Discarico",true);
				request.setAttribute("tx_presenze_action","applica_discarico");	
				}
			}
		} catch (DaoException e) {
			e.printStackTrace();
			setFormMessage("delete_form", Messages.GENERIC_ERROR.format(), request);
			request.setAttribute("SollecitoInsertEsito",false);
		} catch (Exception e) {
			setFormMessage("delete_form", Messages.GENERIC_ERROR.format(), request);
			request.setAttribute("SollecitoInsertEsito",false);
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		finally {
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
	
	
	
	
	private String getCurrentAction(HttpServletRequest request){
		String action=request.getParameter("tx_presenze_action");
		if(action==null){
			action=(String) request.getAttribute("tx_presenze_action");
		}
		if(action==null){
			action="sollecito";
		}
		if(action.trim().equals("refresh")){
			action="presenze";
		}
		request.setAttribute("action",action);
		return action;
		
	}
	
	private void loadListPresenzeDatagrid(HttpServletRequest request){
		codop = "edit";
		String idWallet = (String)request.getAttribute("IdwalletInfo");
		if(idWallet==null){
			idWallet = (String)request.getAttribute("tx_idwallet_h");
		}
		if(idWallet==null){
			idWallet = (String)request.getParameter("tx_idwallet_h");
		}
		HttpSession session = request.getSession();
		codiceSocieta=request.getParameter("codiceSocieta");
		codiceUtente=request.getParameter("codiceUtente");
		chiaveEnte=request.getParameter("chiaveEnte");
		request.setAttribute("codiceSocieta", codiceSocieta);
		request.setAttribute("codiceUtente",codiceUtente);
		request.setAttribute("chiaveEnte",chiaveEnte);
		request.setAttribute("IdwalletInfo",idWallet);
		AnagraficaFiglioMense anagraficaFiglioMense =new AnagraficaFiglioMense();
		anagraficaFiglioMense.setIdWallet(idWallet);
		anagraficaFiglioMense.setAttribute(AnagraficaFiglioMenseDAO.ANAGRAFICA_CODICE_SOCIETA,codiceSocieta );
		anagraficaFiglioMense.setAttribute(AnagraficaFiglioMenseDAO.ANAGRAFICA_CODICE_UTENTE,codiceUtente );
		anagraficaFiglioMense.setAttribute(AnagraficaFiglioMenseDAO.ANAGRAFICA_CHIAVE_ENTE,chiaveEnte );
		AnagraficaFiglioMenseDAO anagraficaFiglioMenseDao;
		WalletPageList walletPageListlist=null;
		int rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		int pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		String order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
		anagraficaFiglioMense.setAttribute("rowsPerPage", rowsPerPage);
		anagraficaFiglioMense.setAttribute("pageNumber", pageNumber);
		anagraficaFiglioMense.setAttribute("order", order);
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//anagraficaFiglioMenseDao = WalletDAOFactory.getAnagraficaFiglioMenseDAO(getWalletDataSource(), getWalletDbSchema());
			conn = getWalletDataSource().getConnection();
			anagraficaFiglioMenseDao = WalletDAOFactory.getAnagraficaFiglioMenseDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
		
		   walletPageListlist = anagraficaFiglioMenseDao.presenzeListPerBorsellino(anagraficaFiglioMense);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
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
		PageInfo pageInfo = walletPageListlist.getPageInfo();
		
		if (walletPageListlist.getRetCode()!="00") {
			setFormMessage("frmIndietro", "Errore generico - Impossibile recuperare i dati", request);
		} else {
			if(pageInfo != null)
			{
				if(pageInfo.getNumRows() > 0)
				{
					session.setAttribute("numRows",pageInfo.getNumRows());
					request.setAttribute("lista_wallet_presenze", walletPageListlist.getWalletListXml());
					//request.setAttribute("lista_wallet_riepilogo", walletPageListlist.getWalletListXmlRiep());
					request.setAttribute("lista_wallet_presenze.pageInfo", pageInfo);
				}
				else setFormMessage("frmIndietro", Messages.NO_DATA_FOUND.format(), request);
			}
			else { 
				setFormMessage("frmIndietro", "Errore generico - Impossibile recuperare i dati", request);
			}
		}
		
		
	}
	
	private void loadListCalendarioDatagrid(HttpServletRequest request){
		setCalendarioRequestParameters(request);
		loadPresenzeMensili(request);
	}
	
	
	private void setCalendarioRequestParameters(HttpServletRequest request){
		HttpSession session = request.getSession();
		session.setAttribute("tx_idwallet_h", request.getParameter("IdwalletInfo")!=null?request.getParameter("IdwalletInfo"):session.getAttribute("tx_idwallet_h"));
		session.setAttribute("IdwalletInfo", request.getParameter("IdwalletInfo")!=null?request.getParameter("IdwalletInfo"):session.getAttribute("IdwalletInfo"));
		session.setAttribute("codiceFiscaleFiglio", request.getParameter("codice_fiscale_figlio")!=null?request.getParameter("codice_fiscale_figlio"):session.getAttribute("codiceFiscaleFiglio"));
		session.setAttribute("denominazione", request.getParameter("denominzione_figlio")!=null?request.getParameter("denominzione_figlio"):session.getAttribute("denominazione"));
		session.setAttribute("servizio", request.getParameter("servizio")!=null?request.getParameter("servizio"):session.getAttribute("servizio"));
		session.setAttribute("mese", request.getParameter("mese")!=null?request.getParameter("mese"):session.getAttribute("mese"));
		session.setAttribute("codiceSocieta", request.getParameter("codiceSocieta")!=null?request.getParameter("codiceSocieta"):session.getAttribute("codiceSocieta"));
		session.setAttribute("codiceUtente", request.getParameter("codiceUtente")!=null?request.getParameter("codiceUtente"):session.getAttribute("codiceUtente"));
		session.setAttribute("chiaveEnte", request.getParameter("chiaveEnte")!=null?request.getParameter("chiaveEnte"):session.getAttribute("chiaveEnte"));
		
		session.setAttribute("anno", request.getAttribute("anno")!=null?request.getParameter("anno"):session.getAttribute("anno"));
		session.setAttribute("codice_anagrafica_figlio", request.getAttribute("codice_anagrafica_figlio")!=null?request.getParameter("codice_anagrafica_figlio"):session.getAttribute("codice_anagrafica_figlio"));
		session.setAttribute("codice_scuola", request.getAttribute("codice_scuola")!=null?request.getParameter("codice_scuola"):session.getAttribute("codice_scuola"));
		session.setAttribute("importo_tariffa", request.getAttribute("importo_tariffa")!=null?request.getParameter("importo_tariffa"):session.getAttribute("importo_tariffa"));
		
		
		request.setAttribute("tx_idwallet_h", request.getParameter("IdwalletInfo")!=null?request.getParameter("IdwalletInfo"):session.getAttribute("tx_idwallet_h"));
		request.setAttribute("IdwalletInfo", request.getParameter("IdwalletInfo")!=null?request.getParameter("IdwalletInfo"):session.getAttribute("IdwalletInfo"));
		request.setAttribute("codiceFiscaleFiglio", request.getParameter("codice_fiscale_figlio")!=null?request.getParameter("codice_fiscale_figlio"):session.getAttribute("codiceFiscaleFiglio"));
		request.setAttribute("denominazione", request.getParameter("denominzione_figlio")!=null?request.getParameter("denominzione_figlio"):session.getAttribute("denominazione"));
		request.setAttribute("servizio", request.getParameter("servizio")!=null?request.getParameter("servizio"):session.getAttribute("servizio"));
		request.setAttribute("mese", request.getParameter("mese")!=null?request.getParameter("mese"):session.getAttribute("mese"));
		request.setAttribute("codiceSocieta", request.getParameter("codiceSocieta")!=null?request.getParameter("codiceSocieta"):session.getAttribute("codiceSocieta"));
		request.setAttribute("codiceUtente", request.getParameter("codiceUtente")!=null?request.getParameter("codiceUtente"):session.getAttribute("codiceUtente"));
		request.setAttribute("chiaveEnte", request.getParameter("chiaveEnte")!=null?request.getParameter("chiaveEnte"):session.getAttribute("chiaveEnte"));
		
		request.setAttribute("anno", request.getAttribute("anno")!=null?request.getParameter("anno"):session.getAttribute("anno"));
		request.setAttribute("codice_anagrafica_figlio", request.getAttribute("codice_anagrafica_figlio")!=null?request.getParameter("codice_anagrafica_figlio"):session.getAttribute("codice_anagrafica_figlio"));
		request.setAttribute("codice_scuola", request.getAttribute("codice_scuola")!=null?request.getParameter("codice_scuola"):session.getAttribute("codice_scuola"));
		request.setAttribute("importo_tariffa", request.getAttribute("importo_tariffa")!=null?request.getParameter("importo_tariffa"):session.getAttribute("importo_tariffa"));
	}
	
	private void replyHeaderAttributes(HttpServletRequest request){
		HttpSession session = request.getSession();
		session.setAttribute("codice_borsellino_header", request.getParameter("IdwalletInfo")!=null?request.getParameter("IdwalletInfo"):session.getAttribute("codice_borsellino_header"));
		session.setAttribute("codice_fiscale_header", request.getParameter("codiceFiscale")!=null?request.getParameter("codiceFiscale"):session.getAttribute("codice_fiscale_header"));
		session.setAttribute("denominazione_header", request.getParameter("denominazione")!=null?request.getParameter("denominazione"):session.getAttribute("denominazione_header"));
		session.setAttribute("RESIDUO_header", request.getParameter("residuoBorsellino")!=null?request.getParameter("residuoBorsellino"):session.getAttribute("RESIDUO_header"));
		if(session.getAttribute("RESIDUO_header")!=null){
			session.setAttribute("RESIDUO_header", ((String)session.getAttribute("RESIDUO_header")).replace(".",","));
		}
	}
	
	private void loadPresenzeMensili(HttpServletRequest request){
		HttpSession session=request.getSession();
		String idWallet=(String) session.getAttribute("IdwalletInfo");
		String anno=(String) session.getAttribute("anno");
		String mese=(String) session.getAttribute("mese");
		String codAnaFiglio=(String) session.getAttribute("codice_anagrafica_figlio");
		String codScuola=(String) session.getAttribute("codice_scuola"); 
		String importoTariffa=(String) session.getAttribute("importo_tariffa");
		PresenzeGiornaliereDAO presenzeGiornaliereDAO;
		HashMap<Calendar, String> calendarCausale=null;
		List<PresenzeAbilitate> listPresenze=new ArrayList<PresenzeAbilitate>();
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//presenzeGiornaliereDAO = WalletDAOFactory.getPresenzeGiornaliereDAO(getWalletDataSource(), getWalletDbSchema());
			conn = getWalletDataSource().getConnection();
			presenzeGiornaliereDAO = WalletDAOFactory.getPresenzeGiornaliereDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			calendarCausale=presenzeGiornaliereDAO.listCausalePresenzeAbilitazione(idWallet, anno, mese, codAnaFiglio, codScuola, importoTariffa);
			if(calendarCausale!=null){
				Iterator iterator=calendarCausale.entrySet().iterator();
				if(!iterator.hasNext()){
					// se non vengono trovate presenze il servizio non è attivo
					request.setAttribute("listaPresenzeMensiliVuota", "si");
				}else{
					request.setAttribute("listaPresenzeMensiliVuota", "no");
				}
				while(iterator.hasNext()){
					Map.Entry pair=(Map.Entry) iterator.next();
					listPresenze.add(new PresenzeAbilitate(pair));
					pair.getKey();
				}
			}
		} catch (DaoException e) {
			// TODO Auto-generated catch block
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
		Collections.sort(listPresenze,new Comparator<PresenzeAbilitate>(){

			public int compare(PresenzeAbilitate p1, PresenzeAbilitate p2) {
				return p1.getDataPresenzaComparator().compareTo(p2.getDataPresenzaComparator());
			}
			
		});
		request.setAttribute("listaPresenzeMensili", listPresenze);
		
	}
	
	
	private void saveConfermaParameters(HttpServletRequest request){
		request.setAttribute("IdwalletInfo", request.getParameter("IdwalletInfo")!=null?request.getParameter("IdwalletInfo"):request.getAttribute("IdwalletInfo"));
		request.setAttribute("causale", request.getParameter("causale")!=null?request.getParameter("causale"):request.getAttribute("causale"));
		request.setAttribute("progressivo", request.getParameter("progressivo")!=null?request.getParameter("progressivo"):request.getAttribute("progressivo"));
		request.setAttribute("dataPresenza", request.getParameter("dataPresenza")!=null?request.getParameter("dataPresenza"):request.getAttribute("dataPresenza"));
		request.setAttribute("tx_presenze_action", request.getParameter("tx_presenze_action")!=null?request.getParameter("tx_presenze_action"):request.getAttribute("tx_presenze_action"));
		request.setAttribute("walletricaricheborsellinoedit", request.getParameter("tx_presenze_action")!=null?request.getParameter("tx_presenze_action"):request.getAttribute("tx_presenze_action"));
	}
	
	
	private String setCausale(String causale){
		String out="";
		if(causale.trim().equals("C")){
			out="D";
		}else if (causale.trim().equals("D")){
			out="R";
		}else if (causale.trim().equals("R")){
			out="S";
		}else if (causale.trim().equals("S")){
			out="R";
	    }
		return out;
	}
	
	
	private void gestisciParametriDatagrid(HttpServletRequest request){
		if(request.getParameter("rowsPerPage")!=null&&!((String)request.getParameter("rowsPerPage")).equals("")){
			request.setAttribute("rowsPerPage", request.getParameter("rowsPerPage"));
		}
		if(request.getParameter("pageNumber")!=null&&!((String)request.getParameter("pageNumber")).equals("")){
			request.setAttribute("pageNumber", request.getParameter("pageNumber"));
		}
	}
}





