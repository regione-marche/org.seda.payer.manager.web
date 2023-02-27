/**
 * 
 */
package org.seda.payer.manager.configurazione.actions;

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
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoSearchRequest;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoSearchResponse;
import com.seda.payer.pgec.webservice.company.dati.CompanySearchRequest;
import com.seda.payer.pgec.webservice.company.dati.CompanySearchResponse;
import com.seda.payer.pgec.webservice.configsesscarrellosoccanpagamento.dati.ConfigSessCarrelloSocCanPagamento;
import com.seda.payer.pgec.webservice.configsesscarrellosoccanpagamento.dati.ConfigSessCarrelloSocCanPagamentoCancelRequest;
import com.seda.payer.pgec.webservice.configsesscarrellosoccanpagamento.dati.ConfigSessCarrelloSocCanPagamentoDetailRequest;
import com.seda.payer.pgec.webservice.configsesscarrellosoccanpagamento.dati.ConfigSessCarrelloSocCanPagamentoDetailResponse;
import com.seda.payer.pgec.webservice.configsesscarrellosoccanpagamento.dati.ConfigSessCarrelloSocCanPagamentoResponse;
import com.seda.payer.pgec.webservice.configsesscarrellosoccanpagamento.dati.ConfigSessCarrelloSocCanPagamentoResponsePageInfo;
import com.seda.payer.pgec.webservice.configsesscarrellosoccanpagamento.dati.ConfigSessCarrelloSocCanPagamentoSaveRequest;
import com.seda.payer.pgec.webservice.configsesscarrellosoccanpagamento.dati.ConfigSessCarrelloSocCanPagamentoSearchRequest;
import com.seda.payer.pgec.webservice.configsesscarrellosoccanpagamento.dati.ConfigSessCarrelloSocCanPagamentoSearchResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;


public class ConfigSessCarrelloSocCanPagamentoAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private String strCodiceSocieta, strChiaveCanalePagamento;

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

		if(user!=null) {
			   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
			   request.setAttribute("userAppl_codiceSocieta",user.getCodiceSocieta().trim());
			   request.setAttribute("userAppl_chiaveCanalePagamento",user.getCodiceUtente().trim());
			   //request.setAttribute("userAppl_chiaveEnteCon",userAppl.getChiaveEnteCon().trim());
			   //request.setAttribute("userAppl_chiaveEnteEnt",userAppl.getChiaveEnteEnt().trim());
			}
		    
			strCodiceSocieta = (String)request.getAttribute("userAppl_codiceSocieta");
			strChiaveCanalePagamento = (String)request.getAttribute("userAppl_chiaveCanalePagamento");
			String firedButton = (String)request.getAttribute("tx_button_nuovo");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			//System.out.println(firedButton+"index");
			if (firedButton!=null || firedButtonReset!=null){
			//System.out.println(firedButtonReset+"index");
					try {
						add(request);
					} catch (ActionException e) {
						e.printStackTrace();
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
			/*String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){
				resetParametri(request);
				request.setAttribute("configsesscarrellosoccanpagamento_companyCode",null);
				request.setAttribute("configsesscarrellosoccanpagamento_chiaveCanalePagamento",null);
				try {
					index(request);
				} catch (ActionException e) {
					e.printStackTrace();
				}
				
				
			}*/
	}

	@Override
	public Object index(HttpServletRequest request) throws ActionException {
		search(request);
		return null;
	}

	public Object search(HttpServletRequest request) throws ActionException {
		try {
			String codiceSocieta = ((String)request.getAttribute("configsesscarrellosoccanpagamento_companyCode")==null ? "":  (String)request.getAttribute("configsesscarrellosoccanpagamento_companyCode"));
			String chiaveCanalePagamento = ((String)request.getAttribute("configsesscarrellosoccanpagamento_chiaveCanalePagamento")==null ? "":  (String)request.getAttribute("configsesscarrellosoccanpagamento_chiaveCanalePagamento"));
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					codiceSocieta = request.getParameter("");
					chiaveCanalePagamento = request.getParameter("");
				}
			}
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				codiceSocieta="";
				chiaveCanalePagamento="";
				
			}
			ConfigSessCarrelloSocCanPagamentoSearchResponse searchResponse = null;
			if (firedButton!=null)
				 if(firedButton.equals("Indietro"))
					 searchResponse = getConfigSessCarrelloSocCanPagamentoSearchResponse(null,null, rowsPerPage, pageNumber, order, request);
				 if (firedButton==null)  searchResponse = getConfigSessCarrelloSocCanPagamentoSearchResponse(codiceSocieta,chiaveCanalePagamento, rowsPerPage, pageNumber, order, request);
			
			
			//ConfigUtenteTipoServizioImplementationStub stub = new ConfigUtenteTipoServizioImplementationStub(new URL(getConfigutentetiposervizioManagerEndpointUrl()), null);
			//ConfigUtenteTipoServizioSearchRequest searchRequest = new ConfigUtenteTipoServizioSearchRequest(codiceSocieta,codiceUtente,codiceTipologiaServizio,"","","", rowsPerPage, pageNumber, order);
			//ConfigSessCarrelloSocCanPagamentoSearchResponse searchResponse =getConfigSessCarrelloSocCanPagamentoSearchResponse(codiceSocieta,chiaveCanalePagamento, rowsPerPage, pageNumber, order);
			ConfigSessCarrelloSocCanPagamentoResponse configSessCarrelloSocCanPagamentoServizioResponse = searchResponse.getResponse();
			ConfigSessCarrelloSocCanPagamentoResponsePageInfo responsePageInfo = configSessCarrelloSocCanPagamentoServizioResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			request.setAttribute("configsesscarrellosoccanpagamento_companyCode", codiceSocieta);
			request.setAttribute("configsesscarrellosoccanpagamento_chiaveCanalePagamento", chiaveCanalePagamento);
			
			request.setAttribute("configsesscarrellosoccanpagamentos", configSessCarrelloSocCanPagamentoServizioResponse.getListXml());
			request.setAttribute("configsesscarrellosoccanpagamentos.pageInfo", pageInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			
			
			String codiceSocieta = (strCodiceSocieta!=null && strCodiceSocieta.length()>0)?strCodiceSocieta:"";
			String chiaveCanalePagamento = (strChiaveCanalePagamento!=null && strChiaveCanalePagamento.length()>0)?strChiaveCanalePagamento:"";
			
			CompanySearchRequest searchRequest = new CompanySearchRequest("","", 0, 0, "");
			CompanySearchResponse searchResponse = getCompanys(searchRequest, request);
			request.setAttribute("companys", searchResponse.getResponse().getListXml());
						
			CanalePagamentoSearchRequest searchRequest2 = new CanalePagamentoSearchRequest("","",0, 0, "");
			CanalePagamentoSearchResponse searchResponse2 = getCanalePagamentos(searchRequest2, request);
			request.setAttribute("canalepagamentos", searchResponse2.getResponse().getListXml());
			
			ConfigSessCarrelloSocCanPagamentoSearchResponse configSessCarrelloSocCanPagamentoSearchResponse = getConfigSessCarrelloSocCanPagamentoSearchResponse(codiceSocieta,chiaveCanalePagamento,rowsPerPage, pageNumber, "", request);
			request.setAttribute("configsesscarrellosoccanpagamentos", configSessCarrelloSocCanPagamentoSearchResponse.getResponse().getListXml());
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {	  
		request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		request.setAttribute("companyCode",request.getParameter("companyCode"));
        request.setAttribute("chiaveCanalePagamento",request.getParameter("chiaveCanalePagamento"));
		return null; 
	}	
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			loadDdlChiave(request);
			
			//ConfigUtenteTipoServizioImplementationStub stub = new ConfigUtenteTipoServizioImplementationStub(new URL(getConfigutentetiposervizioManagerEndpointUrl()), null);
			ConfigSessCarrelloSocCanPagamentoDetailResponse response = 
				getConfigSessCarrelloSocCanPagamentoDetailSearchResponse(request.getParameter("configsesscarrellosoccanpagamento_companyCode"),request.getParameter("configsesscarrellosoccanpagamento_chiaveCanalePagamento"), request);
			//request.setAttribute("configutentetiposervizio", response.getConfigutentetiposervizio());
			request.setAttribute("configsesscarrellosoccanpagamento_companyCode", response.getConfigsesscarrellosoccanpagamento().getCompanyCode());
			request.setAttribute("configsesscarrellosoccanpagamento_chiaveCanalePagamento", response.getConfigsesscarrellosoccanpagamento().getChiaveCanalePagamento());
			request.setAttribute("configsesscarrellosoccanpagamento_numMaxSessioni", response.getConfigsesscarrellosoccanpagamento().getNumMaxsessioni());
			request.setAttribute("configsesscarrellosoccanpagamento_flagCarrello", response.getConfigsesscarrellosoccanpagamento().getFlagCarrello());
			//add(request);
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
					if(request.getAttribute("configsesscarrellosoccanpagamento_companyCode") !=null){
						request.setAttribute("configsesscarrellosoccanpagamento_companyCode", null);
						request.setAttribute("configsesscarrellosoccanpagamento_chiaveCanalePagamento", null);
						index(request);
					}
				}
			}else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){					
					resetParametri(request);
					request.setAttribute("action", "add");
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
					if(request.getAttribute("configsesscarrellosoccanpagamento_companyCode") !=null){
						request.setAttribute("configsesscarrellosoccanpagamento_companyCode", null);
						request.setAttribute("configsesscarrellosoccanpagamento_chiaveCanalePagamento", null);
						index(request);
					}
				}
			}else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset"))
					edit(request);					
			}
			else save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}
	
	private void save(HttpServletRequest request) throws ActionException {
		String companyCode = "";
		String chiaveCanalePagamento = "";
		String codOp=request.getParameter("codop"); 
		request.setAttribute("error", null);
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		String cod = request.getParameter("prova");
		String arrStr = request.getParameter("configsesscarrellosoccanpagamentos");
		String arrStr2 = request.getParameter("configsesscarrellosoccanpagamentos2");
		//if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0 && arrStr!=null && arrStr.length()>0)
		if (cod.equals("1") && arrStr!=null && arrStr.length()>0)
		{
			  String[] strConfigsesscarrellosoccanpagamentos1 = arrStr.split("\\|");
			  companyCode = strConfigsesscarrellosoccanpagamentos1[0];
 		      
			  String[] strConfigsesscarrellosoccanpagamentos2 = arrStr2.split("\\|");
			  chiaveCanalePagamento= strConfigsesscarrellosoccanpagamentos2[0];
 		     
 		}
		//if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0)
		if(cod.equals("0"))
		{
			  companyCode = request.getParameter("configsesscarrellosoccanpagamento_companyCode");
              chiaveCanalePagamento= request.getParameter("configsesscarrellosoccanpagamento_chiaveCanalePagamento");
		}

		int numMaxSessioni = Integer.parseInt(request.getParameter("configsesscarrellosoccanpagamento_numMaxSessioni"));
		String flagCarrello = request.getParameter("configsesscarrellosoccanpagamento_flagCarrello");
		
		try {
			ConfigSessCarrelloSocCanPagamento configSessCarrelloSocCanPagamento = new ConfigSessCarrelloSocCanPagamento(companyCode,chiaveCanalePagamento,numMaxSessioni,flagCarrello);
			/* we prepare object for save */
			ConfigSessCarrelloSocCanPagamentoSaveRequest saveRequest = new ConfigSessCarrelloSocCanPagamentoSaveRequest(configSessCarrelloSocCanPagamento,codOp);
			/* we save object */
			saveConfigSessCarrelloSocCanPagamento(saveRequest, request);
			
		} catch (Exception e) {
			try {
				//ConfigSessCarrelloSocCanPagamentoDetailRequest detailRequest = new ConfigSessCarrelloSocCanPagamentoDetailRequest(companyCode,chiaveCanalePagamento);
				ConfigSessCarrelloSocCanPagamentoDetailResponse detailResponse = getConfigSessCarrelloSocCanPagamento(companyCode,chiaveCanalePagamento, request);
				//request.setAttribute("configutentetiposervizio", detailResponse.getConfigutentetiposervizio());
				request.setAttribute("configsesscarrellosoccanpagamento_companyCode", detailResponse.getConfigsesscarrellosoccanpagamento().getCompanyCode());
				request.setAttribute("configsesscarrellosoccanpagamento_chiaveCanalePagamento", detailResponse.getConfigsesscarrellosoccanpagamento().getChiaveCanalePagamento());
				request.setAttribute("configsesscarrellosoccanpagamento_numMaxSessioni", detailResponse.getConfigsesscarrellosoccanpagamento().getNumMaxsessioni());
				request.setAttribute("configsesscarrellosoccanpagamento_flagCarrello", detailResponse.getConfigsesscarrellosoccanpagamento().getFlagCarrello());
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) 
			{
				request.setAttribute("message", Messages.GENERIC_ERR.format());
				loadDdlChiave(request);
			}
			request.setAttribute("error", "error"); 
			//System.out.println(e.getMessage());
		}
	}
	
	
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String companyCode = request.getParameter("configsesscarrellosoccanpagamento_companyCode");
        String chiaveCanalePagamento= request.getParameter("configsesscarrellosoccanpagamento_chiaveCanalePagamento");
        //ConfigSessCarrelloSocCanPagamentoImplementationStub stub = null;
		try {
			request.setAttribute("varname", "configsesscarrellosoccanpagamento");
			//stub = new ConfigUtenteTipoServizioImplementationStub(new URL(getConfigutentetiposervizioManagerEndpointUrl()), null);
			/* we prepare object for cancel */
			ConfigSessCarrelloSocCanPagamentoCancelRequest cancelRequest = new ConfigSessCarrelloSocCanPagamentoCancelRequest(companyCode,chiaveCanalePagamento);
			/* we cancel object */
			cancelConfigSessCarrelloSocCanPagamento(cancelRequest, request);
			request.setAttribute("message", Messages.CANC_OK.format());
		} catch (FaultType f) {
			request.setAttribute("message",Messages.CANCEL_ERRDIP.format());
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
	
	private ConfigSessCarrelloSocCanPagamentoSearchResponse getConfigSessCarrelloSocCanPagamentoSearchResponse(
			String companyCode, String chiaveCanalePagamento,  int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigSessCarrelloSocCanPagamentoSearchResponse res = null;
		ConfigSessCarrelloSocCanPagamentoSearchRequest in = new ConfigSessCarrelloSocCanPagamentoSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setChiaveCanalePagamento(chiaveCanalePagamento == null ? "" : chiaveCanalePagamento);
		
		res = WSCache.configSessCarrelloSocCanPagamentoServer.getConfigSessCarrelloSocCanPagamentos(in, request);
		return res;
	}
	
	private ConfigSessCarrelloSocCanPagamentoDetailResponse getConfigSessCarrelloSocCanPagamentoDetailSearchResponse(String companyCode, String chiaveCanalePagamento, HttpServletRequest request ) throws FaultType, RemoteException
	{
		 
		ConfigSessCarrelloSocCanPagamentoDetailResponse res = null;
		ConfigSessCarrelloSocCanPagamentoDetailRequest in = new ConfigSessCarrelloSocCanPagamentoDetailRequest(companyCode,chiaveCanalePagamento);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setChiaveCanalePagamento(chiaveCanalePagamento == null ? "" : chiaveCanalePagamento);
				
		res = WSCache.configSessCarrelloSocCanPagamentoServer.getConfigSessCarrelloSocCanPagamento(in, request);
		return res;
	}
	
	private void  saveConfigSessCarrelloSocCanPagamento(ConfigSessCarrelloSocCanPagamentoSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.configSessCarrelloSocCanPagamentoServer.save(saveRequest, request);
		
	}
	
	private void  cancelConfigSessCarrelloSocCanPagamento(ConfigSessCarrelloSocCanPagamentoCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.configSessCarrelloSocCanPagamentoServer.cancel(cancelRequest, request);
		
	}
	
	private CanalePagamentoSearchResponse getCanalePagamentos(CanalePagamentoSearchRequest canalePagamentoSearchRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		return WSCache.canPagamentoServer.getCanalePagamentos(canalePagamentoSearchRequest, request);
	}
	
	private CompanySearchResponse getCompanys(CompanySearchRequest companySearchRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		return WSCache.companyServer.getCompanys(companySearchRequest, request);
	}
	
	private ConfigSessCarrelloSocCanPagamentoDetailResponse getConfigSessCarrelloSocCanPagamento(String companyCode,String chiaveCanalePagamento, HttpServletRequest request ) throws com.seda.payer.pgec.webservice.srv.FaultType, RemoteException
	{
		ConfigSessCarrelloSocCanPagamentoDetailRequest detailRequest = new ConfigSessCarrelloSocCanPagamentoDetailRequest(companyCode, chiaveCanalePagamento);
		detailRequest.setCompanyCode(companyCode == null ? "" : companyCode);
		detailRequest.setChiaveCanalePagamento(chiaveCanalePagamento == null ? "" : chiaveCanalePagamento);
		return WSCache.configSessCarrelloSocCanPagamentoServer.getConfigSessCarrelloSocCanPagamento(detailRequest, request);
	}
	protected void resetParametri(HttpServletRequest request) {
		Enumeration e = request.getParameterNames();
		String p = "";
		while (e.hasMoreElements()) {
			
			p = (String) e.nextElement();
			request.setAttribute(p, "");
		}
		return;
	}
	
	private void loadDdlChiave(HttpServletRequest request) 
	{
		try {
			CompanySearchRequest searchRequest = new CompanySearchRequest("","", 0, 0, "");
			CompanySearchResponse searchResponse = getCompanys(searchRequest, request);
		
			request.setAttribute("companys", searchResponse.getResponse().getListXml());
						
			CanalePagamentoSearchRequest searchRequest2 = new CanalePagamentoSearchRequest("","",0, 0, "");
			CanalePagamentoSearchResponse searchResponse2 = getCanalePagamentos(searchRequest2, request);
			request.setAttribute("canalepagamentos", searchResponse2.getResponse().getListXml());
		} catch (Exception e) { }
		
	}
	
}