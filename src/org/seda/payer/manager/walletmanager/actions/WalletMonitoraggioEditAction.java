package org.seda.payer.manager.walletmanager.actions;


import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.j2ee5.jndi.JndiProxy;
import com.seda.j2ee5.jndi.JndiProxyException;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.Wallet;
import com.seda.payer.core.wallet.bean.WalletPageList;
import com.seda.payer.core.wallet.dao.WalletDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;


@SuppressWarnings("serial")
public class WalletMonitoraggioEditAction extends WalletBaseManagerAction{
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
			String IdWallet = (String)request.getAttribute("Idwallet");
			dividiSocUtenteEnte(request);
			request.setAttribute("tx_idwallet_h",IdWallet);
			
			
			if ( !IdWallet.equals("") && !IdWallet.equals("")) {
				Wallet wallet = null;
				try {
					
					wallet = selezionaWallet(request,IdWallet);
					
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
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
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
			//Tk 2017033088000055 20170404 - inizio
			String tipologiaServizio = (String) request.getAttribute("tipologia_servizio");
			String dataCaricoDa = (String) request.getAttribute("data_carico_da");
			String dataCaricoA = (String) request.getAttribute("data_carico_a");
			wallet.setAttribute(WalletDAO.PERIDOCARICO_DA , dataCaricoDa);
			wallet.setAttribute(WalletDAO.PERIDOCARICO_A , dataCaricoA);
			//Tk 2017033088000055 20170404 - fine
			//inizio LP PG21XX04 Leak
			//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			walletPageList = walletDAO.ListaServiziWalletManager(wallet, tipologiaServizio);	//Tk 2017033088000055
			
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
			if ( FlagEsclusioneEvoluzioneIntimazione.equals("Y"))
				wallet.setFlagEsclusioneEvoluzioneIntimazione(true);
		}
		
		wallet.setOperatore(operatore);
		
		//inizio LP PG21XX04 Leak
		//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
		//int aggiornati = walletDAO.updateFlagEsclusione(wallet);
		//return aggiornati;
		Connection conn = null;
		try {
			conn = new JndiProxy().getSqlConnection(null, dataSourceName, true);
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

