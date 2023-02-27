package org.seda.payer.manager.walletmanager.actions;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.walletmanager.actions.WalletBaseManagerAction;


import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.Wallet;
import com.seda.payer.core.wallet.bean.ConfigurazioneRaccordoPagonet;
import com.seda.payer.core.wallet.bean.EsitoRisposte;
import com.seda.payer.core.wallet.bean.Servizio;
import com.seda.payer.core.wallet.bean.WalletPageList;
import com.seda.payer.core.wallet.dao.ConfigurazioneRaccordoPagonetDAO;
import com.seda.payer.core.wallet.dao.ServizioDAO;
import com.seda.payer.core.wallet.dao.WalletDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;

import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest2;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest3;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.tag.core.DdlOption;


@SuppressWarnings("serial")
public class WalletServizioEditAction extends WalletBaseManagerAction{
	private static String codop = "edit";
	
	private String codiceSocieta = "";
	private String codiceUtente = "";
	private String chiaveEnte = "";
	private String operatore = "";
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		
		HttpSession session = request.getSession();
		/*
		 * Individuo la richiesta dell'utente
		 */
		
		HttpSession sessionlocal = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		operatore = user.getUserName();
		
		FiredButton firedButton = getFiredButton(request);
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
		case TX_BUTTON_NULL:
			/*
			 * Carico i valori dal WS
			 */
			codop = "edit";
			String idWallet = (String)request.getAttribute("IdwalletInfo");
			String codServ = (String)request.getAttribute("codServ");
			String tributo = (String)request.getAttribute("tributo");
			String amcar = (String)request.getAttribute("amcar");
			
			dividiSocUtenteEnte(request);
			request.setAttribute("tx_idwallet_h",idWallet);
			
			
			if ( !idWallet.equals("") && !idWallet.equals("")) {
				Wallet wallet = null;
				String walletSrvDett = ""; 
				try {
					
					wallet = selezionaWallet(request,idWallet);
					walletSrvDett = listaDettSrv(request,idWallet,codServ,tributo,amcar);
					
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
				}
				// Assegno l'oggetto wallet all'attributo wallet
				request.setAttribute("wallet", wallet);
					
				
				
			}
			
			
			break;
			
		case TX_BUTTON_EDIT:
			try {
				updateWalletFlagEsclusione(request);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setFormMessage("var_form", "Errore: " + e.getLocalizedMessage(), request);
			}
			session.setAttribute("recordUpdate", "OK");
			//setFormMessage("form_selezione", Messages.UPDT_OK.format(), request);
			break;
		
		
			
		case TX_BUTTON_INDIETRO:
			//request.setAttribute("tx_button_cerca", "Search");	
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
			request.setAttribute("tx_societa", codiceSocieta);
			request.setAttribute("tx_utente", codiceUtente);
			request.setAttribute("tx_ente", chiaveEnte);
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
	
	private String listaDettSrv(HttpServletRequest request, String idWallet,String codServ,String tributo,String amcar) throws DaoException {
		WalletDAO walletDAO;
		String  walletSrvDett = "";
		//inizio LP PG21XX04 Leak
		//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
		//walletSrvDett = walletDAO.selectDetSrv(idWallet,codServ,tributo,amcar);
		//request.setAttribute("lista_dettaglio_servizi", walletSrvDett);
		//return walletSrvDett;
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			walletSrvDett = walletDAO.selectDetSrv(idWallet,codServ,tributo,amcar);
			request.setAttribute("lista_dettaglio_servizi", walletSrvDett);
			return walletSrvDett;
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
			walletPageList = walletDAO.ListaServiziWalletManager(wallet, null);	//Tk 2017033088000055
			
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

}

