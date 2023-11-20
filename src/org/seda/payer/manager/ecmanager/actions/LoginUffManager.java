package org.seda.payer.manager.ecmanager.actions;

import java.math.BigDecimal;
import java.util.SortedMap;
import java.util.TreeMap;

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
import com.seda.payer.integraente.webservice.dati.RecuperaCodFiscHostRequest;
import com.seda.payer.integraente.webservice.dati.RecuperaCodFiscHostResponse;

public class LoginUffManager extends BaseManagerAction {


	private static final long serialVersionUID = 1L;


	public Object service(HttpServletRequest request) throws ActionException {

		HttpSession session = request.getSession();
		//Generics.setSessionSubMenuECManager(true, request);
		try
		{
			if (request.getParameter("btnEstrattoConto") != null)
			{
				String sCodFiscaleContribuente = request.getParameter("tbEleCodFiscale").toUpperCase();	

				if (sCodFiscaleContribuente.trim().length() == 0) {
						//TODO da verificare
						setFormMessage("form_ecmanager", "Nessun Codice Fiscale/P.IVA associato ai dati inseriti", request);
						return null; 
				}

				String sRes = CodiceFiscalePIVA.controlloCodiceFiscale_PartitaIva_NomeEmpty(sCodFiscaleContribuente, "Codice fiscale/Partita Iva");
				if (sRes != null && sRes.length() > 0)
				{
					setFormMessage("form_ecmanager", sRes, request);
					return null;
				}

				PropertiesTree configuration = (PropertiesTree)context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);

				// inizializza le propriet�
				UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);				
				String sNome = user.getNome();
				String sCognome = user.getCognome();
				String sCodFiscale = user.getCodiceFiscale();
				String sEntePertinenza = user.getEntePertinenza();	//EP160510_001 GG 04112016

				//setto il tokencsrf
				String tokenName = CSRFContext.getInstance().getTokenName();
				String tokenValue = CSRFUtil.getTokenFromSession(session, tokenName);
				String tokenCsrf = tokenName + "=" + tokenValue;
				
				//String idDocumento = (String)request.getAttribute("idDoc");
				
				String idDocumento = "";
				String template = getTemplateCurrentApplication(request, request.getSession());
				if  (template.equals("soris")||template.equals("newsoris")) {
					idDocumento = (String)request.getAttribute("numDoc")==null?"":(String)request.getAttribute("numDoc");
					String codEnte = (String)request.getAttribute("codEnte")==null?"":(String)request.getAttribute("codEnte");
				if (!(idDocumento.equals(""))){
					if (codEnte.equals("")){
						idDocumento="081010"+idDocumento;
					}else{
						idDocumento=codEnte+idDocumento;
					}
				}
				}else{
					 idDocumento = (String)request.getAttribute("idDoc")==null?"":(String)request.getAttribute("idDoc");
				}
				String idBollettino  = (String)request.getAttribute("idBoll")==null?"0":(String)request.getAttribute("idBoll");;
				String raccomandata = (String)request.getAttribute("numRacc")==null?"":(String)request.getAttribute("numRacc");;
				String cronologico = (String)request.getAttribute("numCron")==null?"":(String)request.getAttribute("numCron");;
				String barCode = (String)request.getAttribute("barCode")==null?"":(String)request.getAttribute("barCode");;
				String idProcedura = (String)request.getAttribute("idProcedura")==null?"":(String)request.getAttribute("idProcedura");

				//recupero la url corrente
				String currentUrl = request.getRequestURL().toString();
				String usaCodFisc="";
				String appCodFisc =(String)request.getAttribute("tbEleCodFiscale")==null?"":(String)request.getAttribute("tbEleCodFiscale");
				if (!(appCodFisc.length()==0))
					usaCodFisc = "S";
				
				String tipologiaUtenza = "U";	//PG160220_001 GG 25112016
				String queryString="";
				if  (template.equals("soris")||template.equals("newsoris")) {
					
					String codEnte = (String)request.getAttribute("codEnte")==null?"":(String)request.getAttribute("codEnte");
					String numDoc = (String)request.getAttribute("numDoc")==null?"":(String)request.getAttribute("numDoc");
				
				//PG160220_001 GG 25112016 - Introdotto tipologiaUtenza
				//EP160510_001 GG 04112016 - Introdotto entePertinenza
				 queryString = "app=ecmanager" + "&nome=" + sNome + "&cognome=" + sCognome + "&cf=" + sCodFiscale 
				+ "&cfcontribuente=" + sCodFiscaleContribuente.toUpperCase() + "&managerurl=" + currentUrl 
				+ "&idDoc=" + idDocumento +  "&idBoll=" + idBollettino + "&numRacc=" + raccomandata + "&numCron=" + cronologico + "&barCode=" + barCode + "&idProcedura=" + idProcedura
				+ "&provenienza=" + "manager" + "&usaCodFisc=" + usaCodFisc + "&codEnte=" + codEnte + "&numDoc=" + numDoc + "&entePertinenza=" + sEntePertinenza + "&tipologiaUtenza=" + tipologiaUtenza
				+  "&" + tokenCsrf; 
				}else{
					
					 queryString = "app=ecmanager" + "&nome=" + sNome + "&cognome=" + sCognome + "&cf=" + sCodFiscale 
					+ "&cfcontribuente=" + sCodFiscaleContribuente.toUpperCase() + "&managerurl=" + currentUrl 
					+ "&idDoc=" + idDocumento +  "&idBoll=" + idBollettino + "&numRacc=" + raccomandata + "&numCron=" + cronologico + "&barCode=" + barCode + "&idProcedura=" + idProcedura
					+ "&provenienza=" + "manager" + "&usaCodFisc=" + usaCodFisc + "&codEnte=" + " " + "&numDoc=" + ""
					+  "&" + tokenCsrf; 
				}
				//crypto la querystring
				TripleDESChryptoService desChrypto = new TripleDESChryptoService();
				desChrypto.setIv(WSCache.securityIV);
				desChrypto.setKeyValue(WSCache.securityKey);
				queryString = desChrypto.encryptBASE64(queryString);

				//url estratto conto prelevata dal file di configurazione
				String sUrl = configuration.getProperty(PropertiesPath.urlOnline.format(request.getServerName()));

				//setto l'azione nella request per essere recuperata dal DispachFlow ed applicare il 
				request.setAttribute("urlRedirectEC", sUrl + "?" + queryString);
				request.setAttribute(ManagerKeys.REQUEST_ACTION_FLOW, "avantiEC");

			} 
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private String recuperaCodFisc(HttpServletRequest request) {
		String codiceFiscale="";
		
		
		try {
			HttpSession session = request.getSession();

			RecuperaCodFiscHostRequest recuperaCodFiscHostReq=new RecuperaCodFiscHostRequest();
			RecuperaCodFiscHostResponse recuperaCodFiscHostResp = null;
			String cuteCute = (String)session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
			recuperaCodFiscHostReq.setCodiceUtente(cuteCute); 
			recuperaCodFiscHostReq.setIdBarCode((String)request.getAttribute("barCode"));
			String template = getTemplateCurrentApplication(request, request.getSession());
			String  idDocumento="";
			if  (template.equals("soris")||template.equals("newsoris")) {
				 idDocumento = (String)request.getAttribute("numDoc");
				String codEnte = (String)request.getAttribute("codEnte")==null?"":(String)request.getAttribute("codEnte");
			if (!(idDocumento.equals(""))){
				if (codEnte.equals("")){
					idDocumento="081010"+idDocumento;
				}else{
					idDocumento=codEnte+idDocumento;
				}
				recuperaCodFiscHostReq.setIdDocumento(idDocumento);
			}
			}else{
			 idDocumento=(String)request.getAttribute("idDoc");
			recuperaCodFiscHostReq.setIdDocumento(idDocumento);
			}
			recuperaCodFiscHostReq.setIdProcedura((String)request.getAttribute("idProcedura"));
			String idBoll = (String)request.getAttribute("idBoll");
			if (idBoll.trim().equals("")) {
				BigDecimal numBoll = new BigDecimal("0");
				recuperaCodFiscHostReq.setNumeroBollettino(numBoll);
			} else {
				BigDecimal numBoll = new BigDecimal((String)request.getAttribute("idBoll"));
				recuperaCodFiscHostReq.setNumeroBollettino(numBoll);
			}
			recuperaCodFiscHostReq.setNumeroCronologico((String)request.getAttribute("numCron"));
			recuperaCodFiscHostReq.setNumeroRaccomandata((String)request.getAttribute("numRacc"));
			//PG160170_001 GG 02022017 - inizio
			String sProgCoob = (String)request.getAttribute("progCoob")==null?"":(String)request.getAttribute("progCoob");
			if (sProgCoob.trim().equals("")) {
				BigDecimal progCoob = new BigDecimal("0");
				recuperaCodFiscHostReq.setProgCoobbligato(progCoob);
			} else {
				BigDecimal progCoob = new BigDecimal(sProgCoob);
				recuperaCodFiscHostReq.setProgCoobbligato(progCoob);
			}
			//PG160170_001 GG 02022017 - fine

			recuperaCodFiscHostResp=WSCache.integraEntePgServer.recuperaCodFiscHost(recuperaCodFiscHostReq, request, recuperaCodFiscHostReq.getCodiceUtente());
			codiceFiscale = recuperaCodFiscHostResp.getCodiceFiscale();
//			if(codiceFiscale.contains("|"))
//			{
//				String[] listaReturn = codiceFiscale.split("\\|");
//				
//				codiceFiscale = listaReturn[0].trim();
//			}
//			
			
			
			
				
			
			
			//salvo i nuovi attributi in sessione per ritrovarmeli una volta passato su EstrattoConto WEB
			
			if  (template.equals("soris")||template.equals("newsoris")) {
			   request.setAttribute("numDoc", (String)request.getAttribute("numDoc"));
			   request.setAttribute("codEnte",(String)request.getAttribute("codEnte"));
			}else{
				request.setAttribute("idDoc", (String)request.getAttribute("idDoc"));
			}
		
			request.setAttribute("idBoll", (String)request.getAttribute("idBoll"));
			request.setAttribute("barCode", (String)request.getAttribute("barCode"));
			request.setAttribute("idProcedura", (String)request.getAttribute("idProcedura"));
			request.setAttribute("numCron", (String)request.getAttribute("numCron"));
			request.setAttribute("numRacc", (String)request.getAttribute("numRacc"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return codiceFiscale;
	}


}

