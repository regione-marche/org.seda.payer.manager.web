package org.seda.payer.manager.configurazione.actions;


import java.net.URLDecoder;
import java.rmi.RemoteException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnte;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteCancelRequest;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteDetailRequest;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteDetailResponse;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteResponse;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteResponsePageInfo;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteSaveRequest;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteSearchRequest;
import com.seda.payer.pgec.webservice.configrtente.dati.ConfigRtEnteSearchResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest2;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;

import org.apache.commons.codec.binary.Base64;


public class ConfigRtEnteAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private String usernameAutenticazione = null;
	private String userCodiceSocieta = null;
	
	@Override
	public void start(HttpServletRequest request) {
		rowsPerPage = (request.getParameter("rowsPerPage") == null) 
				   ? getDefaultListRows() : Integer.parseInt(request.getParameter("rowsPerPage"));
		pageNumber = (request.getParameter("pageNumber") == null) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		order = (request.getParameter("order") == null) ? "" : request.getParameter("order");
		
		HttpSession session = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		if (user != null)
			   userCodiceSocieta = (user.getProfile().equals("AMMI") ? "" : user.getCodiceSocieta());
		   else
			   userCodiceSocieta = "XXXXX";
		
		
		if( user!=null) 
		   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
		usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
		
		String firedButton = (String)request.getAttribute("tx_button_nuovo");
		//System.out.println(firedButton+"index");
		if (firedButton!=null){
			if(firedButton.equals("Nuovo")){
				try {
					add(request);
				} catch (ActionException e) {
					e.printStackTrace();
				}
			}
		}
		firedButton = (String)request.getAttribute("tx_button_indietro");
		if (firedButton!=null){
			if(firedButton.equals("Indietro")){
				try {
					index(request);
				} catch (ActionException e) {
					e.printStackTrace();
				}
			}
		}	
		
	}

	@Override
	public Object index(HttpServletRequest request) throws ActionException {
		//saveadd(request);
		search(request);
		
		return null;
	}
	
	public Object search(HttpServletRequest request) throws ActionException {
		try {
			String companyCode = ((String)request.getAttribute("configrtente_searchcompanyCode")==null ? "":  (String)request.getAttribute("configrtente_searchcompanyCode"));
			String userCode = ((String)request.getAttribute("configrtente_searchuserCode")==null ? "":  (String)request.getAttribute("configrtente_searchuserCode"));
			String chiaveEnte = ((String)request.getAttribute("configrtente_searchchiaveEnte")==null ? "":  (String)request.getAttribute("configrtente_searchchiaveEnte"));
			String codiceIdpa = ((String)request.getAttribute("configrtente_searchcodiceIdpa")==null ? "":  (String)request.getAttribute("configrtente_searchcodiceIdpa"));
			
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			if (firedButton!=null){
					
				request.setAttribute("configrtente_searchcompanyCode", "");
				request.setAttribute("configrtente_searchuserCode", "");
				request.setAttribute("configrtente_searchchiaveEnte", "");
				request.setAttribute("configrtente_searchcodiceIdpa", "");
				companyCode="";
				userCode="";
				chiaveEnte="";
				codiceIdpa="";
			}
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				companyCode="";
				userCode="";
				chiaveEnte="";
				codiceIdpa="";
				
			}
			
			ConfigRtEnteSearchResponse searchResponse = getConfigRtEnteSearchResponse(companyCode, userCode, chiaveEnte, codiceIdpa, rowsPerPage, pageNumber, order, request);
			ConfigRtEnteResponse configrtenteResponse = searchResponse.getResponse();
			ConfigRtEnteResponsePageInfo responsePageInfo = configrtenteResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			//request.setAttribute("configrtente", searchRequest);
			request.setAttribute("configrtente_searchcompanyCode", companyCode);
			request.setAttribute("configrtente_searchuserCode", userCode);
			request.setAttribute("configrtente_searchchiaveEnte", chiaveEnte);
			request.setAttribute("configrtente_searchcodiceIdpa", codiceIdpa);
			
			request.setAttribute("configrtentes", configrtenteResponse.getListXml());
			request.setAttribute("configrtentes.pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			
			loadDdlEnteTipoServizio(request);
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {	  
		try {
			request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
			request.setAttribute("configrtente_companyCode",request.getParameter("configrtente_companyCode"));
			request.setAttribute("configrtente_userCode",request.getParameter("configrtente_userCode"));		  
			request.setAttribute("configrtente_chiaveEnte",request.getParameter("configrtente_chiaveEnte"));
			//SB - 20210720 - Problema di lettura caratteri speciali - inizio
			String queryString = request.getQueryString();
			System.out.println("QS: " + queryString);
			String idpa = queryString.substring(queryString.indexOf("configrtente_codiceIdpa")+24, queryString.indexOf("&csrfToken"));
			String codiceIdpa = URLDecoder.decode(idpa ,"ISO-8859-1");
			System.out.println("idPA: " + codiceIdpa);
			request.setAttribute("configrtente_codiceIdpa", codiceIdpa);
			//SB - 20210720 - Problema di lettura caratteri speciali - fine
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null; 
	}
	
	private void loadDdlEnteTipoServizio(HttpServletRequest request) throws FaultType, RemoteException {
		ConfigUtenteTipoServizioEnteSearchResponse configUtenteTipoServizioEnteSearchResponse = getConfigUtenteTipoServizioEntes2(userCodiceSocieta,"","","", request);
		request.setAttribute("entetiposervizios", configUtenteTipoServizioEnteSearchResponse.getResponse().getListXml());
	}
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			
			loadDdlEnteTipoServizio(request);
			
			String codSocieta = ((String)request.getAttribute("configrtente_companyCode")==null ? "":  (String)request.getAttribute("configrtente_companyCode"));
			String userCode = ((String)request.getAttribute("configrtente_userCode")==null ? "":  (String)request.getAttribute("configrtente_userCode"));
			String chiaveEnte = ((String)request.getAttribute("configrtente_chiaveEnte")==null ? "":  (String)request.getAttribute("configrtente_chiaveEnte"));
			String codiceIdpa = ((String)request.getAttribute("configrtente_codiceIdpa")==null ? "":  URLDecoder.decode((String)request.getAttribute("configrtente_codiceIdpa"),"ASCII"));
			//SB - 20210720 - Problema di lettura caratteri speciali - inizio
			String queryString = request.getQueryString();
			System.out.println("QS: " + queryString);
			String idpa = queryString.substring(queryString.indexOf("configrtente_codiceIdpa")+24, queryString.indexOf("&csrfToken"));
			codiceIdpa = URLDecoder.decode(idpa ,"ISO-8859-1");
			System.out.println("idPA: " + codiceIdpa);
			request.setAttribute("configrtente_codiceIdpa", codiceIdpa);
			//SB - 20210720 - Problema di lettura caratteri speciali - fine
			ConfigRtEnteDetailResponse response = getConfigRtEnteDetailSearchResponse(codSocieta, chiaveEnte,userCode, codiceIdpa, request);
			
			//
			request.setAttribute("configrtente_companyCode", response.getConfigRtEnte().getCompanyCode());
			request.setAttribute("configrtente_userCode", response.getConfigRtEnte().getUserCode());
			request.setAttribute("configrtente_chiaveEnte", response.getConfigRtEnte().getChiaveEnte());
			request.setAttribute("configrtente_codiceIdpa", response.getConfigRtEnte().getCodiceIdpa());
			request.setAttribute("configrtente_userInvioRt", response.getConfigRtEnte().getUserInvioRt());
			Base64 base64 = new Base64();
			String PwdTemp = new String(base64.decode(response.getConfigRtEnte().getPasswInvioRt().getBytes()));
			request.setAttribute("configrtente_passwInvioRt", PwdTemp);
			request.setAttribute("configrtente_urlInvioRt", response.getConfigRtEnte().getUrlInvioRt());
			request.setAttribute("configrtente_mailInvioEsitoInvioRt", response.getConfigRtEnte().getMailInvioEsitoInvioRt());
			request.setAttribute("configrtente_urlRecuperoEsitoInvio", response.getConfigRtEnte().getUrlRecuperoEsitoInvio());
			request.setAttribute("configrtente_mailInvioEsitoRecuperoEsitoRt", response.getConfigRtEnte().getMailInvioEsitoRecuperoEsitoRt());
			if (response.getConfigRtEnte().getFlagAttivo()) {
				request.setAttribute("configrtente_flagAttivo", "Y");
			} else {
				request.setAttribute("configrtente_flagAttivo", "N");
			}
			if (response.getConfigRtEnte().getFlagAbilitato()) {
				request.setAttribute("configrtente_flagAbilitato", "Y");
			} else {
				request.setAttribute("configrtente_flagAbilitato", "N");
			}
			request.setAttribute("configrtente_codiceUtenteSeda", response.getConfigRtEnte().getCodiceOperatore().trim());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object saveadd(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("message", Messages.INS_OK.format());
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					request.setAttribute("configrtente_companyCode", "");
					request.setAttribute("configrtente_userCode", "");
					request.setAttribute("configrtente_chiaveEnte", "");
					request.setAttribute("configrtente_codiceIdpa", "");
					index(request);					
				}
			}
			else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){					
					resetParametri(request);
					request.setAttribute("action", "add");
					add(request);
				}
			}else save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}
	public Object saveedit(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("message", Messages.UPDT_OK.format());
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					request.setAttribute("configrtente_userCode", null);
					request.setAttribute("configrtente_chiaveEnte", null);						
					index(request);					
				}
			}
			else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){
					request.setAttribute("action", "edit");
					edit(request);					
				}
			}
			else save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}
	
	private void save(HttpServletRequest request) throws ActionException {
		String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		
		String userCode = request.getParameter("configrtente_userCode");
		String companyCode = request.getParameter("configrtente_companyCode");
		String chiaveEnte = request.getParameter("configrtente_chiaveEnte");
		
		String arrStr = request.getParameter("configrtente_strEntetiposervizios");
		
		if (codOp!=null && codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0   && arrStr!=null && arrStr.length()>0)
		{
			  String[] strEntetiposervizios = arrStr.split("\\|");
			  companyCode = strEntetiposervizios[0];
			  userCode= strEntetiposervizios[1];
 		      chiaveEnte= strEntetiposervizios[2];
 		}
		
		String codiceIdpa = (String)request.getAttribute("configrtente_codiceIdpa");
		String userInvioRt = (String)request.getAttribute("configrtente_userInvioRt");
		String tmpPass = (String)request.getAttribute("configrtente_passwInvioRt");
		Base64 base64 = new Base64();
		String passwInvioRt = new String(base64.encode(tmpPass.getBytes()));
		String urlInvioRt = (String)request.getAttribute("configrtente_urlInvioRt");
		String mailInvioEsitoInvioRt = (String)request.getAttribute("configrtente_mailInvioEsitoInvioRt");
		String urlRecuperoEsitoInvio = (String)request.getAttribute("configrtente_urlRecuperoEsitoInvio");
		String mailInvioEsitoRecuperoEsitoRt = (String)request.getAttribute("configrtente_mailInvioEsitoRecuperoEsitoRt");
		String flAttivo = (String)request.getAttribute("configrtente_flagAttivo");
		Boolean flagAttivo=false;
		if (flAttivo.toUpperCase().equals("Y")) {
			flagAttivo = true; //;
		} 
		Boolean flagAbilitato = false;
		String flAbil = (String)request.getAttribute("configrtente_flagAbilitato");
		if (flAbil.toUpperCase().equals("Y")) {
			flagAbilitato = true;//
		}
	    String codiceUtenteSeda = (String)request.getAttribute("configrtente_codiceUtenteSeda");
	    
		try {
			ConfigRtEnte configrtente = new ConfigRtEnte(companyCode, userCode, chiaveEnte, 
					codiceIdpa, userInvioRt, passwInvioRt, urlInvioRt, mailInvioEsitoInvioRt,
					urlRecuperoEsitoInvio, mailInvioEsitoRecuperoEsitoRt, 
					flagAttivo, flagAbilitato, usernameAutenticazione);
			/* we prepare object for save */
			ConfigRtEnteSaveRequest saveRequest = new ConfigRtEnteSaveRequest(configrtente,codOp);
			/* we save object */
			saveConfigRtEnte(saveRequest, request);
		} catch (Exception e) {
			try {		
				ConfigRtEnteDetailResponse detailResponse = getConfigRtEnte(companyCode, chiaveEnte, userCode, codiceIdpa, request);
				//request.setAttribute("configrtente", detailResponse.getConfigRtEnte());
				request.setAttribute("configrtente_companyCode", detailResponse.getConfigRtEnte().getCompanyCode());
				request.setAttribute("configrtente_userCode", detailResponse.getConfigRtEnte().getUserCode());
				request.setAttribute("configrtente_chiaveEnte", detailResponse.getConfigRtEnte().getChiaveEnte());
				request.setAttribute("configrtente_codiceIdpa", detailResponse.getConfigRtEnte().getCodiceIdpa());
				request.setAttribute("configrtente_userInvioRt", detailResponse.getConfigRtEnte().getUserInvioRt());
				request.setAttribute("configrtente_passwInvioRt", detailResponse.getConfigRtEnte().getPasswInvioRt());
				request.setAttribute("configrtente_urlInvioRt", detailResponse.getConfigRtEnte().getUrlInvioRt());
				request.setAttribute("configrtente_mailInvioEsitoInvioRt", detailResponse.getConfigRtEnte().getMailInvioEsitoInvioRt());
				request.setAttribute("configrtente_urlRecuperoEsitoInvio", detailResponse.getConfigRtEnte().getUrlRecuperoEsitoInvio());
				request.setAttribute("configrtente_mailInvioEsitoRecuperoEsitoRt", detailResponse.getConfigRtEnte().getMailInvioEsitoRecuperoEsitoRt());
				if (detailResponse.getConfigRtEnte().getFlagAttivo()) {
					request.setAttribute("configrtente_flagAttivo", "Y");
				} else {
					request.setAttribute("configrtente_flagAttivo", "N");
				}
				if (detailResponse.getConfigRtEnte().getFlagAbilitato()) {
					request.setAttribute("configrtente_flagAbilitato", "Y");
				} else {
					request.setAttribute("configrtente_flagAbilitato", "N");
				}
				request.setAttribute("configrtente_codiceUtenteSeda", detailResponse.getConfigRtEnte().getCodiceOperatore().trim());
			} catch (Exception ignore) { }
				if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
				if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.UPDT_ERR.format());
				request.setAttribute("error", "error"); 
				System.out.println(e.getMessage());
			}
	}
		
	public Object cancel(HttpServletRequest request) throws ActionException {
		String codSocieta = request.getParameter("configrtente_companyCode");
		String userCode = request.getParameter("configrtente_userCode");
		String chiaveEnte = request.getParameter("configrtente_chiaveEnte");
		
		try {
			
			//SB - 20210720 - Problema di lettura caratteri speciali - inizio
			String queryString = request.getQueryString();
			System.out.println("QS: " + queryString);
			String idpa = queryString.substring(queryString.indexOf("configrtente_codiceIdpa")+24, queryString.length());
			String codiceIdpa = URLDecoder.decode(idpa ,"ISO-8859-1");
			System.out.println("idPA: " + codiceIdpa);
			request.setAttribute("configrtente_codiceIdpa", codiceIdpa);
			//SB - 20210720 - Problema di lettura caratteri speciali - fine
			
			request.setAttribute("varname", "configrtente");
			/* we prepare object for cancel */
			ConfigRtEnteCancelRequest cancelRequest = new ConfigRtEnteCancelRequest(codSocieta, userCode, chiaveEnte, codiceIdpa);
			/* we cancel object */			
			cancelConfigRtEnte(cancelRequest, request);
			request.setAttribute("message", Messages.CANC_OK.format());
		} catch (FaultType f) {
			request.setAttribute("message", Messages.valueOf("ERR_" + f.getCode()).format());
		} catch (Exception e) {
			request.setAttribute("message", Messages.CANCEL_ERRDIP.format());
		}
		return null;
	}	

	
	protected int getDefaultListRows()
	{
		int defaultListRows = 4;
		PropertiesTree configuration = (PropertiesTree) context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		if(configuration != null)
		{
			String s_defaultListRows = configuration.getProperty(PropertiesPath.defaultListRows.format());
			if (s_defaultListRows != null) defaultListRows = Integer.parseInt(s_defaultListRows);
		}
		return defaultListRows;
	}
	
	private ConfigRtEnteSearchResponse getConfigRtEnteSearchResponse(
			String companyCode, String userCode, String chiaveEnte, String codiceIdpa, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigRtEnteSearchResponse res = null;
		ConfigRtEnteSearchRequest in = new ConfigRtEnteSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setUserCode(userCode == null ? "" : userCode);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		in.setCodiceIdpa(codiceIdpa == null ? "" : codiceIdpa);
		
		res = WSCache.configRtEnteServer.getConfigRtEntes(in, request);
		return res;
	}
	private void  saveConfigRtEnte(ConfigRtEnteSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException {
		
		WSCache.configRtEnteServer.save(saveRequest, request);
		
	}
	
	private void  cancelConfigRtEnte(ConfigRtEnteCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException {
		
		WSCache.configRtEnteServer.cancel(cancelRequest, request);
		
	}
	
	private ConfigRtEnteDetailResponse getConfigRtEnteDetailSearchResponse(String codSocieta, String chiaveEnte, String userCode, String codiceIdpa, HttpServletRequest request) throws FaultType, RemoteException {
		 
		ConfigRtEnteDetailResponse res = null;
		ConfigRtEnteDetailRequest in = new ConfigRtEnteDetailRequest(codSocieta, chiaveEnte, userCode, codiceIdpa);
		in.setCodiceSocieta(codSocieta == null ? "" : codSocieta);
		in.setUserCode(userCode == null ? "" : userCode);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		in.setCodiceIdpa(codiceIdpa == null ? "": codiceIdpa);
		res = WSCache.configRtEnteServer.getConfigRtEnte(in, request);
		return res;
	}


	private ConfigRtEnteDetailResponse getConfigRtEnte(String codSocieta, String chiaveEnte, String userCode, String codiceIdpa, HttpServletRequest request) throws com.seda.payer.pgec.webservice.srv.FaultType, RemoteException {
		ConfigRtEnteDetailRequest detailRequest = new ConfigRtEnteDetailRequest(codSocieta, chiaveEnte, userCode, codiceIdpa);
		detailRequest.setCodiceSocieta(codSocieta == null ? "" : codSocieta);
		detailRequest.setUserCode(userCode == null ? "" : userCode);
		detailRequest.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		detailRequest.setCodiceIdpa(codiceIdpa == null ? "" : codiceIdpa);
	
		
		return WSCache.configRtEnteServer.getConfigRtEnte(detailRequest, request);
	}
	
	@SuppressWarnings("unchecked")
	protected void resetParametri(HttpServletRequest request) {
		Enumeration e = request.getParameterNames();
		String p = "";
		while (e.hasMoreElements()) {
			
			p = (String) e.nextElement();
			request.setAttribute(p, "");
		}
		return;
	}
	
	private ConfigUtenteTipoServizioEnteSearchResponse getConfigUtenteTipoServizioEntes2(String codiceSocieta,String codiceUtente,String chiaveEnte, String procName, HttpServletRequest request) throws FaultType, RemoteException{
		ConfigUtenteTipoServizioEnteSearchResponse res = null;
		ConfigUtenteTipoServizioEnteSearchRequest2 in = new ConfigUtenteTipoServizioEnteSearchRequest2();
		
		
		in.setCompanyCode(codiceSocieta == null ? "" : codiceSocieta);
		in.setCodiceUtente(codiceUtente == null ? "" : codiceUtente);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		in.setProcName(procName == null ? "" : procName);
		
		res = WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEntes2(in, request);
		
		return res;
	}

}
