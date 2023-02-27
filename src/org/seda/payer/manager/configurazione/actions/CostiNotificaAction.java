package org.seda.payer.manager.configurazione.actions;


import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;


import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoSearchRequest;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoSearchResponse;
import com.seda.payer.pgec.webservice.cartapagamento.dati.CartaPagamentoSearchRequest;
import com.seda.payer.pgec.webservice.cartapagamento.dati.CartaPagamentoSearchResponse;
import com.seda.payer.pgec.webservice.costinotifica.dati.CostiNotifica;
import com.seda.payer.pgec.webservice.costinotifica.dati.CostiNotificaCancelRequest;
import com.seda.payer.pgec.webservice.costinotifica.dati.CostiNotificaDetailRequest;
import com.seda.payer.pgec.webservice.costinotifica.dati.CostiNotificaDetailResponse;
import com.seda.payer.pgec.webservice.costinotifica.dati.CostiNotificaResponse;
import com.seda.payer.pgec.webservice.costinotifica.dati.CostiNotificaResponsePageInfo;
import com.seda.payer.pgec.webservice.costinotifica.dati.CostiNotificaSaveRequest;
import com.seda.payer.pgec.webservice.costinotifica.dati.CostiNotificaSearchRequest;
import com.seda.payer.pgec.webservice.costinotifica.dati.CostiNotificaSearchResponse;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.user.dati.UserSearchRequest;
import com.seda.payer.pgec.webservice.user.dati.UserSearchResponse;





public class CostiNotificaAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private String usernameAutenticazione = null;
	
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
		
		if( user!=null) 
		   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
		usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
		
		String firedButton = (String)request.getAttribute("tx_button_nuovo");
		//System.out.println(firedButton+"index");
		if (firedButton!=null){
			if(firedButton.equals("Nuovo")){
				request.setAttribute("costiNotifica_costiNotificaCompanyCode", "");
				request.setAttribute("costiNotifica_costiNotificaUserCode", "");
				request.setAttribute("costiNotifica_costiNotificaDescCompanyCode", "");
				request.setAttribute("costiNotifica_costiNotificaDescUserCode", "");
				
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
		search(request);
		
		return null;
	}
	
	public Object search(HttpServletRequest request) throws ActionException {
		try {
			String costiNotificaCompanyCode = ((String)request.getAttribute("costiNotifica_costiNotificaCompanyCode")==null ? "":  (String)request.getAttribute("costiNotifica_costiNotificaCompanyCode"));
			String costiNotificaUserCode = ((String)request.getAttribute("costiNotifica_costiNotificaUserCode")==null ? "":  (String)request.getAttribute("costiNotifica_costiNotificaUserCode"));
			String costiNotificaDescCompanyCode = ((String)request.getAttribute("costiNotifica_costiNotificaDescCompanyCode")==null ? "":  (String)request.getAttribute("costiNotifica_costiNotificaDescCompanyCode"));
			String costiNotificaDescUserCode = ((String)request.getAttribute("costiNotifica_costiNotificaDescUserCode")==null ? "":  (String)request.getAttribute("costiNotifica_costiNotificaDescUserCode"));
					
			
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonN = (String)request.getAttribute("tx_button_nuovo");
			if (firedButton!=null||firedButtonN!=null){
					
				request.setAttribute("costiNotifica_costiNotificaCompanyCode", "");
				request.setAttribute("costiNotifica_costiNotificaUserCode", "");
				request.setAttribute("costiNotifica_costiNotificaDescCompanyCode", "");
				request.setAttribute("costiNotifica_costiNotificaDescUserCode", "");
				costiNotificaCompanyCode="";
				costiNotificaUserCode="";
				costiNotificaDescCompanyCode="";
				costiNotificaDescUserCode="";
			}
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				costiNotificaCompanyCode="";
				costiNotificaUserCode="";
				costiNotificaDescCompanyCode="";
				costiNotificaDescUserCode="";
				
			}
			
			CostiNotificaSearchResponse searchResponse = getCostiNotificaSearchResponse(costiNotificaDescCompanyCode, costiNotificaDescUserCode, rowsPerPage, pageNumber, order, request);
			CostiNotificaResponse costiNotificaResponse = searchResponse.getResponse();
			CostiNotificaResponsePageInfo responsePageInfo = costiNotificaResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			
			request.setAttribute("costiNotifica_costiNotificaCompanyCode", costiNotificaCompanyCode);
			request.setAttribute("costiNotifica_costiNotificaUserCode", costiNotificaUserCode);
			request.setAttribute("costiNotifica_costiNotificaDescCompanyCode", costiNotificaDescCompanyCode);
			request.setAttribute("costiNotifica_costiNotificaDescUserCode", costiNotificaDescUserCode);
			
			request.setAttribute("costinotifica", costiNotificaResponse.getListXml());
			request.setAttribute("costinotifica.pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			/* we retry all users */ 
			UserSearchRequest searchRequest = new UserSearchRequest("","","","","","", 0, 0, "");
			UserSearchResponse searchResponse = getUsers(searchRequest, request);
			request.setAttribute("users", searchResponse.getResponse().getListXml());
			/* we retry all canalepagamentos */ 			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {	  
		request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		
		request.setAttribute("costiNotifica_costiNotificaCompanyCode", request.getParameter("costiNotifica_costiNotificaCompanyCode"));
		request.setAttribute("costiNotifica_costiNotificaUserCode", request.getParameter("costiNotifica_costiNotificaUserCode"));
		  
		return null; 
	}
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
						
			/* we retry all users */ 
			UserSearchRequest searchRequest = new UserSearchRequest("","","","","","", 0, 0, "");
			UserSearchResponse searchResponse = getUsers(searchRequest, request);
			request.setAttribute("users", searchResponse.getResponse().getListXml());
			/* we retry all canalepagamentos */ 			
			
			CostiNotificaDetailResponse response = getCostiNotificaDetailSearchResponse((String)request.getAttribute("costiNotifica_costiNotificaCompanyCode"), (String)request.getAttribute("costiNotifica_costiNotificaUserCode"), request);
		
			request.setAttribute("costiNotifica_costiNotificaCompanyCode", response.getCostiNotifica().getCompanyCode());
			request.setAttribute("costiNotifica_costiNotificaUserCode", response.getCostiNotifica().getUserCode());
			
			String costoPostaOrdinaria = response.getCostiNotifica().getCostoPostaOrdinaria().toString();
			costoPostaOrdinaria = costoPostaOrdinaria.replace(".",",");
			request.setAttribute("costiNotifica_costiNotificaCostoPostaOrdinaria", costoPostaOrdinaria);
			
			String costoSms = response.getCostiNotifica().getCostoSms().toString();
			costoSms = costoSms.replace(".",",");
			request.setAttribute("costiNotifica_costiNotificaCostoSms", costoSms);
			
			
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
					request.setAttribute("costiNotifica_costiNotificaCompanyCode", "");
					request.setAttribute("costiNotifica_costiNotificaUserCode", "");
					index(request);					
				}
			}
			else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){					
					resetParametri(request);
					request.setAttribute("action", "add");
					add(request);
				}
			}else 	save(request);
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
					request.setAttribute("costiNotifica_costiNotificaCompanyCode", null);
					request.setAttribute("costiNotifica_costiNotificaUserCode", null);
					
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
		
		String costiNotificaCompanyCode = " ";//request.getParameter("costiNotifica_costiNotificaCompanyCode");
	    String costiNotificaUserCode = " ";//request.getParameter("costiNotifica_costiNotificaUserCode");
	    
	    String arrStr = request.getParameter("costiNotifica_strUsers");
		if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0 && arrStr!=null && arrStr.length()>0)
		{
			  String[] strUsers = arrStr.split("\\|");
			  costiNotificaCompanyCode = strUsers[0];
			  costiNotificaUserCode= strUsers[1];
			  
 		}
		if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) 
		{
			costiNotificaCompanyCode = request.getParameter("costiNotifica_costiNotificaCompanyCode");
			costiNotificaUserCode = request.getParameter("costiNotifica_costiNotificaUserCode");
			  
		}
		
		char oldChar = ',';
        char newChar = '.';
		
	    BigDecimal costiNotificaCostoPostaOrdinaria = new BigDecimal(0);
        String strCostiNotificaCostoPostaOrdinaria = request.getParameter("costiNotifica_costiNotificaCostoPostaOrdinaria").trim();
        strCostiNotificaCostoPostaOrdinaria = strCostiNotificaCostoPostaOrdinaria.replace(oldChar, newChar);
        if (strCostiNotificaCostoPostaOrdinaria != null && !strCostiNotificaCostoPostaOrdinaria.equals(""))  
        	costiNotificaCostoPostaOrdinaria = BigDecimal.valueOf(Double.parseDouble(strCostiNotificaCostoPostaOrdinaria));
	    
        BigDecimal costiNotificaCostoSms = new BigDecimal(0);
        String strCostiNotificaCostoSms = request.getParameter("costiNotifica_costiNotificaCostoSms");
        strCostiNotificaCostoSms = strCostiNotificaCostoSms.replace(oldChar, newChar);
        if (strCostiNotificaCostoSms != null && !strCostiNotificaCostoSms.equals(""))
         	costiNotificaCostoSms = BigDecimal.valueOf(Double.parseDouble(strCostiNotificaCostoSms));
	    
	    
		try {
					
			CostiNotifica costiNotifica = new CostiNotifica(costiNotificaCompanyCode, costiNotificaUserCode, costiNotificaCostoSms, costiNotificaCostoPostaOrdinaria, usernameAutenticazione); // usernameAutenticazione);
			/* we prepare object for save */
			CostiNotificaSaveRequest saveRequest = new CostiNotificaSaveRequest(costiNotifica,codOp);
			/* we save object */
			saveCostiNotifica(saveRequest, request);

		} catch (Exception e) {
			try {
				CostiNotificaDetailRequest detailRequest = new CostiNotificaDetailRequest(costiNotificaCompanyCode, costiNotificaUserCode);
			    CostiNotificaDetailResponse detailResponse = getCostiNotifica(detailRequest, request);
				request.setAttribute("costiNotifica_costiNotificaCompanyCode", detailResponse.getCostiNotifica().getCompanyCode());
				request.setAttribute("costiNotifica_costiNotificaUserCode", detailResponse.getCostiNotifica().getUserCode());
				request.setAttribute("costiNotifica_costiNotificaCostoPostaOrdinaria", detailResponse.getCostiNotifica().getCostoPostaOrdinaria());
				request.setAttribute("costiNotifica_costiNotificaCostoSms", detailResponse.getCostiNotifica().getCostoSms());
				
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.UPDT_ERR.format());
			request.setAttribute("error", "error"); 
			//System.out.println(e.getMessage());
		}
	}
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String costiNotificaCompanyCode = request.getParameter("costiNotifica_costiNotificaCompanyCode");
	    String costiNotificaUserCode = request.getParameter("costiNotifica_costiNotificaUserCode");
	    
		try {
			request.setAttribute("varname", "costinotifica");
			/* we prepare object for cancel */
			CostiNotificaCancelRequest cancelRequest = new CostiNotificaCancelRequest(costiNotificaCompanyCode, costiNotificaUserCode);
			/* we cancel object */			
			cancelCostiNotifica(cancelRequest, request);
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

private CostiNotificaSearchResponse getCostiNotificaSearchResponse(
		String companyCode, String userCode, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
{
	CostiNotificaSearchResponse res = null;
	CostiNotificaSearchRequest in = new CostiNotificaSearchRequest();
	in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
	in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
	in.setOrder(order == null ? "" : order);
	in.setCompanyCode(companyCode == null ? "" : companyCode);
	in.setUserCode(userCode == null ? "" : userCode);
	
	res = WSCache.costiNotificaServer.getCostiNotificas(in, request);
	return res;
}
private void  saveCostiNotifica(CostiNotificaSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
{
	
	WSCache.costiNotificaServer.save(saveRequest, request);
	
}

private void  cancelCostiNotifica(CostiNotificaCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
{
	
	WSCache.costiNotificaServer.cancel(cancelRequest, request);
	
}

private CostiNotificaDetailResponse getCostiNotificaDetailSearchResponse(String costiNotificaCompanyCode, String costiNotificaUserCode, HttpServletRequest request) throws FaultType, RemoteException
{
	 
	CostiNotificaDetailResponse res = null;
	CostiNotificaDetailRequest in = new CostiNotificaDetailRequest(costiNotificaCompanyCode, costiNotificaUserCode);
	in.setCompanyCode(costiNotificaCompanyCode == null ? "" : costiNotificaCompanyCode);
	in.setUserCode(costiNotificaUserCode == null ? "" : costiNotificaUserCode);
	res = WSCache.costiNotificaServer.getCostiNotifica(in, request);
	return res;
}

/*private void cancelCostiNotificaResponse (String costiNotificaCompanyCode, String costiNotificaUserCode) throws FaultType, RemoteException
{
	 
	
	CostiNotificaCancelRequest in = new CostiNotificaCancelRequest(costiNotificaCompanyCode, costiNotificaUserCode);
	in.setCompanyCode(costiNotificaCompanyCode == null ? "" : costiNotificaCompanyCode);
	in.setUserCode(costiNotificaUserCode == null ? "" : costiNotificaUserCode);
	WSCache.costiNotificaServer.cancel(in);
	
}*/

private CostiNotificaDetailResponse getCostiNotifica (CostiNotificaDetailRequest detailRequest, HttpServletRequest request) throws FaultType, RemoteException
{
	
	return WSCache.costiNotificaServer.getCostiNotifica(detailRequest, request);
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
private UserSearchResponse getUsers(UserSearchRequest userSearchRequest, HttpServletRequest request) throws FaultType, RemoteException
{
	return WSCache.userServer.getUsers(userSearchRequest, request);
}
}
