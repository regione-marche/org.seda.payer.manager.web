package org.seda.payer.manager.posfisico.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
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

public class PosFisico extends BaseManagerAction {


	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		
		HttpSession session = request.getSession();
		
		try
		{
			if (request.getParameter("btnLoginOperatore") != null)
			{
				String sCodFiscaleContribuente = request.getParameter("tbEleCodFiscale").toUpperCase();	
				
				String sRes = CodiceFiscalePIVA.controlloCodiceFiscale_PartitaIva_NomeEmpty(sCodFiscaleContribuente, "Codice fiscale/Partita Iva");
				if (sRes != null && sRes.length() > 0)
				{
					setFormMessage("form_posfisico", sRes, request);
					return null;
				}
				
				PropertiesTree configuration = (PropertiesTree)context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
				
				// inizializza le proprietà
				UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);				
				String sNome = user.getNome();
				String sCognome = user.getCognome();
				String sCodFiscale = user.getCodiceFiscale();
				
				//setto il tokencsrf
				String tokenName = CSRFContext.getInstance().getTokenName();
				String tokenValue = CSRFUtil.getTokenFromSession(session, CSRFContext.getInstance().getTokenNameSave());
				String tokenCsrf = tokenName + "=" + tokenValue;
				
				//recupero la url corrente
				String currentUrl = request.getRequestURL().toString();
				
				String queryString = "app=posfisico" + "&nome=" + sNome + "&cognome=" + sCognome + "&cf=" + sCodFiscale 
				+ "&cfcontribuente=" + sCodFiscaleContribuente.toUpperCase() + "&managerurl=" + currentUrl + "&" + tokenCsrf; 
				
				//crypto la querystring
				TripleDESChryptoService desChrypto = new TripleDESChryptoService();
				desChrypto.setIv(WSCache.securityIV);
				desChrypto.setKeyValue(WSCache.securityKey);
				queryString = desChrypto.encryptBASE64(queryString);
		
				//url estratto conto prelevata dal file di configurazione
				String sUrl = configuration.getProperty(PropertiesPath.urlOnline.format(request.getServerName()));
				
				//setto l'azione nella request per essere recuperata dal DispachFlow ed applicare il 
				request.setAttribute("urlRedirectPOS", sUrl + "?" + queryString);
				request.setAttribute(ManagerKeys.REQUEST_ACTION_FLOW, "avantiPOS");
				
			}		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
