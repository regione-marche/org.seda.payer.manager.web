package org.seda.payer.manager.wismanager.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.security.TripleDESChryptoService;
import com.seda.j2ee5.maf.components.defender.csrf.CSRFContext;
import com.seda.j2ee5.maf.components.defender.csrf.CSRFUtil;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.impostasoggiorno.webservice.wis.dati.VerificaAutorizzazioneStrutturaRequest;
import com.seda.payer.impostasoggiorno.webservice.wis.dati.VerificaAutorizzazioneStrutturaResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaComuniBySiglaProvinciaRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaComuniBySiglaProvinciaResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaProvinceResponse;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class WisManager extends BaseManagerAction {

	
	private static final long serialVersionUID = 1L;

	
	public Object service(HttpServletRequest request) throws ActionException {
		
		HttpSession session = request.getSession();
		
		PropertiesTree configuration = (PropertiesTree)context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		
		MAFRequest mafReq = new MAFRequest(request);
		String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
		UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		String templateName = userBean.getTemplate(applicationName);
		
		if (templateName.equals("trentrisc"))
			request.setAttribute("ddlProvincia", "TN");
        //inizio LP PG1800XX_016
		boolean bSimuloTorino = false;
		/*
		String currentUrl0 = request.getRequestURL().toString();
		if(currentUrl0.indexOf("localhost:9862") != -1) {
			bSimuloTorino = true;9
		}
		*/
		if (templateName.equals("sassari")) {
			if(bSimuloTorino) {
				request.setAttribute("ddlProvincia", "TO");
				request.setAttribute("ddlComune", "L219");
			} else {
				request.setAttribute("ddlProvincia", "SS");
				request.setAttribute("ddlComune", "I452");
			}
		}
        //fine LP PG1800XX_016
	
		if (request.getAttribute("back") != null)
		{
			try {
				String siglaProvincia = "";
				if (!templateName.equals("trentrisc"))
				{
					loadListaProvince(request);
					//ritorno dal web: devo ricaricare le dropdownlist
					siglaProvincia = request.getParameter("ddlProvincia");
			        //inizio LP PG1800XX_016
					if (siglaProvincia == null && templateName.equals("sassari")) {
						siglaProvincia = (String) request.getAttribute("ddlProvincia");
					}
			        //fine LP PG1800XX_016
				}
				else
				{
					siglaProvincia = (String) request.getAttribute("ddlProvincia");					
				}
				loadListaComuni(siglaProvincia,request);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
		{
			FiredButton firedButton = getFiredButton(request);
			
			try
			{
				switch(firedButton) {
					case TX_BUTTON_WIS:
						// inizializza le propriet�
						UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);				
						String sNome = user.getNome();
						String sCognome = user.getCognome();
						String sCodFiscale = user.getCodiceFiscale();
				
						String codiceBelfiore = request.getParameter("ddlComune");
						String numeroAutorizzazione = request.getParameter("tbNumeroAutorizzazione");
						String siglaProvincia = request.getParameter("ddlProvincia");
						
				        //inizio LP PG1800XX_016
						if (templateName.equals("sassari")) {
							codiceBelfiore = (String) request.getAttribute("ddlComune");
							siglaProvincia = (String) request.getAttribute("ddlProvincia");
							if(codiceBelfiore == null) {
								if(bSimuloTorino) {
									siglaProvincia = "TO";
									codiceBelfiore = "L219";
								} else {
									siglaProvincia = "SS";
									codiceBelfiore = "I452";
								}
							}
						}
		
				        //fine LP PG1800XX_016
						
						
						if(controlloAutorizzazione(codiceBelfiore,numeroAutorizzazione, request)) {
						
							//setto il tokencsrf
							String tokenName = CSRFContext.getInstance().getTokenName();
							String tokenValue = CSRFUtil.getTokenFromSession(session, tokenName);
							String tokenCsrf = tokenName + "=" + tokenValue;
							
							//recupero la url corrente
							String currentUrl = request.getRequestURL().toString();
							
							String queryString = "app=wismanager" + "&nome=" + sNome + "&cognome=" + sCognome + "&cf=" + sCodFiscale 
							+ "&provincia=" + siglaProvincia + "&codbelfiore=" + codiceBelfiore + "&numautorizzazione=" + numeroAutorizzazione + "&managerurl=" + currentUrl + "&" + tokenCsrf; 
							
							//crypto la querystring
							TripleDESChryptoService desChrypto = new TripleDESChryptoService();
							desChrypto.setIv(WSCache.securityIV);
							desChrypto.setKeyValue(WSCache.securityKey);
							queryString = desChrypto.encryptBASE64(queryString);
					
							//url estratto conto prelevata dal file di configurazione
							String sUrl = configuration.getProperty(PropertiesPath.urlOnline.format(request.getServerName()));
							
							//setto l'azione nella request per essere recuperata dal DispachFlow ed applicare il 
							request.setAttribute("urlRedirectWIS", sUrl + "?" + queryString);
						}else {
							firedButton = FiredButton.TX_BUTTON_NULL;
							request.setAttribute("btnWis", null);
							request.setAttribute("ddlComune", codiceBelfiore);
							request.setAttribute("ddlProvincia", (String)request.getAttribute("ddlProvincia"));
							
							loadListaComuni((String)request.getAttribute("ddlProvincia"),request);
							
							//String lst = (String)request.getAttribute("listcomuni");
						}
					break;	
					case TX_BUTTON_PROVINCIA_CHANGED:
						siglaProvincia = request.getParameter("ddlProvincia");
						loadListaComuni(siglaProvincia,request);
					break;
					case TX_BUTTON_NULL:
						if (!templateName.equals("trentrisc"))
				        //inizio LP PG1800XX_016
						{
				        //fine LP PG1800XX_016
							loadListaProvince(request);
				        //inizio LP PG1800XX_016
							if (templateName.equals("sassari")) {
								siglaProvincia = (String) request.getAttribute("ddlProvincia");
								loadListaComuni(siglaProvincia,request);
							}
						}
				        //fine LP PG1800XX_016
						else
						{
							siglaProvincia = (String) request.getAttribute("ddlProvincia");
							loadListaComuni(siglaProvincia,request);
						}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	
	private void loadListaProvince(HttpServletRequest request) throws FaultType, RemoteException
	{
		RecuperaProvinceResponse getProvinceRes = WSCache.commonsServer.recuperaProvince(request);
		request.setAttribute("listprovince", getProvinceRes.getListXml());
	}
	
	private void loadListaComuni(String siglaProvincia, HttpServletRequest request) throws FaultType, RemoteException
	{
		loadListaProvince(request);
		if (siglaProvincia != null && siglaProvincia.length() > 0)
		{
			RecuperaComuniBySiglaProvinciaResponse res = WSCache.commonsServer.recuperaComuni_SiglaProvincia(new RecuperaComuniBySiglaProvinciaRequest(siglaProvincia), request);
			request.setAttribute("listcomuni", res.getListXml());
		}
	}
	
	
	private boolean controlloAutorizzazione(String codiceBelfiore, String codiceAutorizzazione, HttpServletRequest request)
	{
		//controllo autorizzazione
		try {

				VerificaAutorizzazioneStrutturaRequest req = new VerificaAutorizzazioneStrutturaRequest();
				req.setCodiceBelfiore(codiceBelfiore);
				req.setCodiceAutorizzazione(codiceAutorizzazione);
				System.out.println("controlloAutorizzazione pre verificaAutorizzazioneStruttura: codiceBelfiore = <" + req.getCodiceBelfiore() + "> codiceAutorizzazione = <" + req.getCodiceAutorizzazione() + ">");
				VerificaAutorizzazioneStrutturaResponse res = WSCache.impostaSoggiornoServer.verificaAutorizzazioneStruttura(req, request);
				if (res != null)
				{
					if (res.getRetCode().equals("00"))
					{
						if (res.getAnagraficaStruttura() != null)
						{
							System.out.println("controlloAutorizzazione post verificaAutorizzazioneStruttura: codiceAutorizzazione = <" + res.getAnagraficaStruttura().getCodiceAutorizzazione() + ">");
							System.out.println("verificaAutorizzazioneStruttura - OK - Struttura censita");
							return true;
						}
						else
						{
							System.out.println("verificaAutorizzazioneStruttura - KO - Struttura non censita");
							request.setAttribute("errorMessage", "Struttura non censita, controllare la combinazione Comune/Numero Autorizzazione.");
							return false;
						}
					}
					else if (res.getRetCode().equals("02"))
					{
						System.out.println("verificaAutorizzazioneStruttura - KO - Struttura non censita - 02");
						request.setAttribute("errorMessage", "Struttura non censita, controllare la combinazione Comune/Numero Autorizzazione.");
						return false;
					}
					else
					{
						System.out.println("verificaAutorizzazioneStruttura - ERRORE - " + res.getRetCode());
						request.setAttribute("errorMessage", "Si � verificato un errore durante il controllo della struttura. Riprovare pi� tardi.");
						return false;
					}
				}
				else
				{
					System.out.println("verificaAutorizzazioneStruttura - ERRORE");
					request.setAttribute("errorMessage", "Si � verificato un errore durante il controllo della struttura. Riprovare pi� tardi.");
					return false;
				}
		} catch (Exception e) {
			System.out.println("verificaAutorizzazioneStruttura - ERRORE : " + e.getMessage());
			request.setAttribute("errorMessage", "Errore generico.");
			return false;
		}
	}

}
