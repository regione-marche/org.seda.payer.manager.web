package org.seda.payer.manager.walletmanager.actions;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
//import org.seda.payer.manager.util.Generics;
import org.seda.payer.manager.util.CodiceFiscalePIVA;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.security.TripleDESChryptoService;
import com.seda.j2ee5.maf.components.defender.csrf.CSRFContext;
import com.seda.j2ee5.maf.components.defender.csrf.CSRFUtil;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.core.wallet.bean.Wallet;
import com.seda.payer.core.wallet.dao.WalletDAO;
import com.seda.payer.core.wallet.dao.WalletDAOFactory;

public class WalletManager extends WalletBaseManagerAction {

	
	private static final long serialVersionUID = 1L;

	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		//Generics.setSessionSubMenuECManager(true, request);
		
		try
		{
			if (request.getParameter("btnWalletManager") != null || request.getParameter("tbEleCodFiscale") != null )
			{
				String codiceFiscale = request.getParameter("tbEleCodFiscale").toUpperCase();
				String IDWallet = request.getParameter("tbIdWallet").toUpperCase();
				if (codiceFiscale.trim().length() == 0 && IDWallet.trim().length() == 0) {
					setFormMessage("form_walletmanager", "FORNIRE ALMENO UN PARAMETRO DI RICERCA", request);
					return null;
				}
				// con l'IDWallet devo recuperate il codice Fiscale per permettere il Login in SSO al Borsellino
				Wallet wallet = null; 
				wallet = SelezionaWallet(IDWallet,codiceFiscale);
				
				String sCodFiscaleContribuente = wallet.getCodiceFiscaleGenitore();	
				if(sCodFiscaleContribuente==null)
				{
					setFormMessage("form_walletmanager", "ID Borsellino non trovato", request);
					return null;
				}
					
//				String sRes = CodiceFiscalePIVA.controlloCodiceFiscale_PartitaIva_NomeEmpty(sCodFiscaleContribuente, "Codice fiscale/Partita Iva");
//				if (sRes != null && sRes.length() > 0)
//				{
//					setFormMessage("form_walletmanager", sRes, request);
//					return null;
//				}
				
				PropertiesTree configuration = (PropertiesTree)context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
				
				// inizializza le propriet�
				UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);				
				String sNome = user.getNome();
				String sCognome = user.getCognome();
				String sCodFiscale = user.getCodiceFiscale();
				
				//setto il tokencsrf
				String tokenName = CSRFContext.getInstance().getTokenName();
				String tokenValue = CSRFUtil.getTokenFromSession(session, tokenName);
				String tokenCsrf = tokenName + "=" + tokenValue;
				
				//recupero la url corrente
				String currentUrl = request.getRequestURL().toString();
				
				String queryString = "app=walletmanager" + "&nome=" + sNome + "&cognome=" + sCognome + "&cf=" + sCodFiscale 
				+ "&idwallet=" + IDWallet + "&cfcontribuente=" + sCodFiscaleContribuente.toUpperCase() + "&managerurl=" + currentUrl + "&" + tokenCsrf; 
				
				//crypto la querystring
				TripleDESChryptoService desChrypto = new TripleDESChryptoService();
				desChrypto.setIv(WSCache.securityIV);
				desChrypto.setKeyValue(WSCache.securityKey);
				queryString = desChrypto.encryptBASE64(queryString);
		
				//url estratto conto prelevata dal file di configurazione
				String sUrl = configuration.getProperty(PropertiesPath.urlOnline.format(request.getServerName()));
				
				//setto l'azione nella request per essere recuperata dal DispachFlow ed applicare il 
				request.setAttribute("urlRedirectWallet", sUrl + "?" + queryString);
				request.setAttribute(ManagerKeys.REQUEST_ACTION_FLOW, "avantiWallet");
				
			}		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private Wallet SelezionaWallet(String IDWallet, String codiceFiscale) { 
		
		WalletDAO walletDAO;
		Wallet walletmodel = new Wallet();
		if(!IDWallet.equals(""))
			walletmodel.setIdWallet(IDWallet);
		if(!codiceFiscale.equals(""))
			walletmodel.setCodiceFiscaleGenitore(codiceFiscale);
		
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//walletDAO = WalletDAOFactory.getWalletDAO(getWalletDataSource(), getWalletDbSchema());
			conn = getWalletDataSource().getConnection();
			walletDAO = WalletDAOFactory.getWalletDAO(conn, getWalletDbSchema());
			//fine LP PG21XX04 Leak
			walletmodel = walletDAO.select(walletmodel);
			
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
		return walletmodel;

	}
}
