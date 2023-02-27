package org.seda.payer.manager.walletmanager.actions;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.Wallet;
import com.seda.payer.core.wallet.dao.SepaDAO;
import com.seda.payer.core.wallet.dao.WalletDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;



@SuppressWarnings("serial")
public class WalletAnagraficaContribuentiEditAction extends WalletBaseManagerAction{
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
				String walletListaBollettini = "";
				try {
					
					wallet = selezionaWallet(request,IdWallet);
					walletListaBollettini = listaBollettini(request,IdWallet);
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
				updateWallet(request);
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
		/*
		 * Setto l'action della form ed il tipo di operazione
		 */
		request.setAttribute("codop",codop);
		return null;
	}

	private void dividiSocUtenteEnte(HttpServletRequest request) 
	{
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

	private Wallet selezionaWallet(HttpServletRequest request, String IdWallet) throws DaoException 
	{
		WalletDAO walletDAO;
		SepaDAO sepaDAO;
		Wallet wallet = new Wallet(); 
		wallet.setCodiceSocieta(codiceSocieta);
		wallet.setCuteCute(codiceUtente);
		wallet.setChiaveEnte(chiaveEnte);
		wallet.setIdWallet(IdWallet);
		//inizio LP PG21XX04 Leak
		//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
		//sepaDAO = WalletDAOFactory.getSepaDAO(getSepaDataSource(), getSepaDbSchema());
		//wallet = walletDAO.select(wallet);
		//wallet = sepaDAO.selectSepa(wallet);
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
		//inizio LP PG21XX04 Leak2
		Connection connS = null;
		//fine LP PG21XX04 Leak2
		try {
			//inizio LP PG21XX04 Leak2
			//conn = getSepaDataSource().getConnection();
			//sepaDAO = WalletDAOFactory.getSepaDAO(conn, getWalletDbSchema());
			connS = getSepaDataSource().getConnection();
			sepaDAO = WalletDAOFactory.getSepaDAO(connS, getWalletDbSchema());
			//fine LP PG21XX04 Leak2
			wallet = sepaDAO.selectSepaWeb(wallet);
		} catch (Exception e) {
			throw new DaoException(e);
		} finally {
			//inizio LP PG21XX04 Leak2
			//if (conn != null) {
			//	try {
			//		conn.close();
			//	} catch (SQLException e) {
			//		e.printStackTrace();
			//	}
			//}
			if (connS != null) {
				try {
					connS.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			//fine LP PG21XX04 Leak2
		}
		//fine LP PG21XX04 Leak
		
		request.setAttribute("chk_FlagEsclusioneSMSCortesia",  wallet.isFlagEsclusioneSMSCortesia());
		request.setAttribute("chk_FlagEsclusioneSMSSollecito",  wallet.isFlagEsclusioneSMSSollecito());
		request.setAttribute("chk_FlagEsclusioneSollecitoCartaceo",  wallet.isFlagEsclusioneSollecitoCartaceo());
		request.setAttribute("chk_FlagEsclusioneEvoluzioneIntimazione",  wallet.isFlagEsclusioneEvoluzioneIntimazione());
		request.setAttribute("chk_FlagStatoAttivazione",  wallet.isFlagPrimoAccesso()?"N":"Y");
		request.setAttribute("tx_noRivestizione",  wallet.isFlagNoRivestizione());
		if(wallet.getAttribute(WalletDAO.FLAG_WELCOMEKIT).equals("Y"))
				request.setAttribute("chk_FlagWelcomeKit", "Y");
		else
				request.setAttribute("chk_FlagWelcomeKit", "N");
		if(wallet.getAttribute(WalletDAO.FLAG_ESTRATTOCONTO).equals("Y"))
			request.setAttribute("chk_FlgEstrattoConto", "Y");
		else
			request.setAttribute("chk_FlgEstrattoConto", "N");
//		WalletPageList walletPageListServiziWallet = listServiziWallet(request,IdWallet);
//		PageInfo pageInfo = walletPageListServiziWallet.getPageInfo();
		
//		if (walletPageListServiziWallet.getRetCode()!="00") 
//		{
//			setFormMessage("form_selezione", "Errore generico - Impossibile recuperare i dati", request);
//		} 
//		else 
//		{
//			request.setAttribute("lista_servizi_wallet", walletPageListServiziWallet.getWalletListXml());
//		}
		
		return wallet;
		
	}
	
	
	
	
	
	private void updateWallet(HttpServletRequest request) throws DaoException 
	{
	
		String societa = (String)request.getAttribute("tx_societa");
		String utente = (String)request.getAttribute("tx_utente");
		String ente = (String)request.getAttribute("tx_ente");
		String idWallet = (String)request.getAttribute("tx_idwallet");
		String indirizzo = (String)request.getAttribute("tx_indirizzo");
		String comune = (String)request.getAttribute("tx_comune");
		String cap = (String)request.getAttribute("tx_cap");
		String provincia = (String)request.getAttribute("tx_provincia");
		String indirizzoMail = (String)request.getAttribute("tx_indirizzoEmail");
		String denominazione = (String)request.getAttribute("tx_denominazioneGenitore");
		String numCell = (String)request.getAttribute("tx_numeroCell");
		
		String noRivestizione = "";
		if(request.getAttribute("tx_noRivestizione")!=null)
			noRivestizione = (String)request.getAttribute("tx_noRivestizione");
		
		
		
		
		WalletDAO walletDAO;
		Wallet wallet = new Wallet();
		wallet.setCodiceSocieta(societa);
		wallet.setCuteCute(utente);
		wallet.setChiaveEnte(ente);
		wallet.setIdWallet(idWallet);
		wallet.setInidirizzoGenitore(indirizzo);
		wallet.setDenominazioneComuneGenitore(comune);
		wallet.setCapComuneGenitore(cap);
		wallet.setProvinciaGenitore(provincia);
		wallet.setOperatore(operatore);
		wallet.setDenominazioneGenitore(denominazione);
		wallet.setIndirizzoEmail(indirizzoMail);
		wallet.setNumeroCell(numCell);
		
		wallet.setFlagNoRivestizione(false);	
		if (noRivestizione!= null )
		{
			if (noRivestizione.equals("Y"))
				wallet.setFlagNoRivestizione(true);
			
		}

		//inizio LP PG21XX04 Leak
		//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
		//walletDAO.updateAnagrafica(wallet);	
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			walletDAO.updateAnagrafica(wallet);	
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
	
	private String listaBollettini(HttpServletRequest request, String idWallet) throws DaoException {
		WalletDAO walletDAO;
		String  walletListaBollettini = "";
		//inizio LP PG21XX04 Leak
		//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
		//walletListaBollettini = walletDAO.listBollettini(idWallet);
		//request.setAttribute("lista_dettaglio_bollettini", walletListaBollettini);
		//return walletListaBollettini;
		Connection conn = null;
		try {
			conn = getWalletDataSource().getConnection();
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			walletListaBollettini = walletDAO.listBollettini(idWallet);
			request.setAttribute("lista_dettaglio_bollettini", walletListaBollettini);
			return walletListaBollettini;
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

