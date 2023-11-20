package org.seda.payer.manager.mercatomanager.actions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
//import org.seda.payer.manager.util.Generics;
//import org.seda.payer.manager.util.CodiceFiscalePIVA;
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
import com.seda.payer.core.mercato.bean.ConfigurazioneAnagAutorizzazione;
import com.seda.payer.core.mercato.dao.ConfigurazioneAnagAutorizzazioneDAO;
import com.seda.payer.core.mercato.dao.MercatoDAOFactory;
//import com.seda.payer.core.wallet.bean.Wallet;
//import com.seda.payer.core.wallet.dao.WalletDAO;
//import com.seda.payer.core.wallet.dao.WalletDAOFactory;

public class MercatiManager extends MercatoBaseManagerAction {

	
	private static final long serialVersionUID = 1L;

	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		HttpSession session = request.getSession();
		//Generics.setSessionSubMenuECManager(true, request);
		
		try
		{
			if (request.getParameter("btnMercatiManager") != null || (request.getParameter("tbEleCodFiscale") != null ))
			{
				String codiceFiscale = request.getParameter("tbEleCodFiscale").toUpperCase();
				// Verifico su AnagAutorizzazione la presenza del codice fiscale
				ConfigurazioneAnagAutorizzazione autor = null; 
				autor = SelezionaAutor(codiceFiscale);
				
				String sCodFiscaleContribuente = autor.getCodiceFiscAnagAutorizzazione();
				
				if((sCodFiscaleContribuente==null)||(sCodFiscaleContribuente.equals(""))) {
					setFormMessage("form_mercatimanager", "Codice Fiscale non trovato", request);
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
				
				String queryString = "app=mercatimanager" + "&nome=" + sNome + "&cognome=" + sCognome + "&cf=" + sCodFiscale 
				+ "&cfcontribuente=" + sCodFiscaleContribuente.toUpperCase() + "&managerurl=" + currentUrl + "&" + tokenCsrf;
				
				//crypto la querystring
				TripleDESChryptoService desChrypto = new TripleDESChryptoService();
				desChrypto.setIv(WSCache.securityIV);
				desChrypto.setKeyValue(WSCache.securityKey);
				queryString = desChrypto.encryptBASE64(queryString);
		
				//url estratto conto prelevata dal file di configurazione
				String sUrl = configuration.getProperty(PropertiesPath.urlOnline.format(request.getServerName()));
				
				//setto l'azione nella request per essere recuperata dal DispachFlow ed applicare il 
				//sUrl="http://10.10.80.6:9261/pagonet2/estrattoconto/j_sso_do";
				request.setAttribute("urlRedirectWallet", sUrl + "?" + queryString);
				request.setAttribute(ManagerKeys.REQUEST_ACTION_FLOW, "avantiMercati");
				
			} 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	

	private ConfigurazioneAnagAutorizzazione SelezionaAutor(String codiceFiscale) { 
		
		ConfigurazioneAnagAutorizzazioneDAO autorDAO;
		ArrayList<ConfigurazioneAnagAutorizzazione> autormodel = new ArrayList<ConfigurazioneAnagAutorizzazione>();
		ConfigurazioneAnagAutorizzazione autor = new ConfigurazioneAnagAutorizzazione();
		if(!codiceFiscale.equals(""))
			autor.setCodiceFiscAnagAutorizzazione(codiceFiscale);
				
		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//autorDAO = MercatoDAOFactory.getConfigurazioneAnagAutorizzazione(getMercatoDataSource(), getMercatoDbSchema());
			conn = getMercatoDataSource().getConnection();
			autorDAO = MercatoDAOFactory.getConfigurazioneAnagAutorizzazione(conn, getMercatoDbSchema());
			//fine LP PG21XX04 Leak
			autormodel = autorDAO.listPerCF(autor);
			
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
		if (autormodel!=null && !autormodel.isEmpty()) {
			return autormodel.get(0);
		} else {
			autor.setCodiceFiscAnagAutorizzazione("");
			return autor;
		}

	} 
}
