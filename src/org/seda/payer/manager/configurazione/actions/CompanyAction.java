package org.seda.payer.manager.configurazione.actions;


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
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.company.dati.CompanyResponse;
import com.seda.payer.pgec.webservice.company.dati.CompanyResponsePageInfo;
import com.seda.payer.pgec.webservice.company.dati.CompanySearchRequest;
import com.seda.payer.pgec.webservice.company.dati.CompanySearchResponse;
import com.seda.payer.pgec.webservice.company.dati.CompanyDetailRequest;
import com.seda.payer.pgec.webservice.company.dati.CompanyDetailResponse;

import com.seda.payer.pgec.webservice.company.dati.Company;
import com.seda.payer.pgec.webservice.company.dati.CompanySaveRequest;
import com.seda.payer.pgec.webservice.company.dati.CompanyCancelRequest;




public class CompanyAction extends DispatchHtmlAction {

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
				request.setAttribute("company_companyCode", "");
				request.setAttribute("company_companyDescription", "");
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
			String companyCode = ((String)request.getAttribute("company_companyCode")==null ? "":  (String)request.getAttribute("company_companyCode"));
			String companyDescription = ((String)request.getAttribute("company_companyDescription")==null ? "":  (String)request.getAttribute("company_companyDescription"));
					
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonN = (String)request.getAttribute("tx_button_nuovo");
			if (firedButton!=null||firedButtonN!=null){
					
				request.setAttribute("company_companyCode", "");
				request.setAttribute("company_companyDescription", "");
				companyCode="";
				companyDescription="";
			}
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				companyCode="";
				companyDescription="";
				
			}
			
			CompanySearchResponse searchResponse = getCompanySearchResponse(companyCode,companyDescription, rowsPerPage, pageNumber, order, request);
			CompanyResponse companyResponse = searchResponse.getResponse();
			CompanyResponsePageInfo responsePageInfo = companyResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			//request.setAttribute("company", searchRequest);
			request.setAttribute("company_companyCode", companyCode);
			request.setAttribute("company_companyDescription", companyDescription);
			
			request.setAttribute("companys", companyResponse.getListXml());
			request.setAttribute("companys.pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public Object add(HttpServletRequest request) throws ActionException {
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {	  
		request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		  request.setAttribute("companyCode",request.getParameter("companyCode"));		  
		return null; 
	}
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			
			CompanyDetailResponse response = getCompanyDetailSearchResponse(request.getParameter("company_companyCode"), request);
			
			//request.setAttribute("company", response.getCompany());
			request.setAttribute("company_companyCode", response.getCompany().getCompanyCode());
			request.setAttribute("company_companyDescription", response.getCompany().getCompanyDescription());
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
					request.setAttribute("company_companyCode", "");
					request.setAttribute("company_companyDescription", "");
					index(request);					
				}
			}
			else if(firedButtonReset!=null){
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
					request.setAttribute("company_companyCode", null);
					request.setAttribute("company_companyDescription", null);						
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
		String companyCode = request.getParameter("company_companyCode");
	    String companyDescription = request.getParameter("company_companyDescription");
	    
		try {
					
			Company company = new Company(companyCode, companyDescription, usernameAutenticazione);
			/* we prepare object for save */
			CompanySaveRequest saveRequest = new CompanySaveRequest(company,codOp);
			/* we save object */
			saveCompany(saveRequest, request);

		} catch (Exception e) {
			try {
				
			    CompanyDetailResponse detailResponse = getCompany(companyCode, request);
				//request.setAttribute("company", detailResponse.getCompany());
				request.setAttribute("company_companyCode", detailResponse.getCompany().getCompanyCode());
				request.setAttribute("company_companyDescription", detailResponse.getCompany().getCompanyDescription());
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.UPDT_ERR.format());
			request.setAttribute("error", "error"); 
			System.out.println(e.getMessage());
		}
	}
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String companyCode = request.getParameter("company_companyCode");
	    
		try {
			request.setAttribute("varname", "company");
			/* we prepare object for cancel */
			CompanyCancelRequest cancelRequest = new CompanyCancelRequest(companyCode);
			/* we cancel object */			
			cancelCompany(cancelRequest, request);
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

private CompanySearchResponse getCompanySearchResponse(
		String companyCode, String companyDescription, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
{
	CompanySearchResponse res = null;
	CompanySearchRequest in = new CompanySearchRequest();
	in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
	in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
	in.setOrder(order == null ? "" : order);
	in.setCompanyCode(companyCode == null ? "" : companyCode);
	in.setCompanyDescription(companyDescription == null ? "" : companyDescription);
	
	res = WSCache.companyServer.getCompanys(in, request);
	return res;
}
private void  saveCompany(CompanySaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
{
	
	WSCache.companyServer.save(saveRequest, request);
	
}

private void  cancelCompany(CompanyCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
{
	
	WSCache.companyServer.cancel(cancelRequest, request);
	
}

private CompanyDetailResponse getCompanyDetailSearchResponse(String companyCode, HttpServletRequest request) throws FaultType, RemoteException
{
	 
	CompanyDetailResponse res = null;
	CompanyDetailRequest in = new CompanyDetailRequest(companyCode);
	in.setCompanyCode(companyCode == null ? "" : companyCode);
	res = WSCache.companyServer.getCompany(in, request);
	return res;
}

/*private void cancelCompanyResponse (String companyCode) throws FaultType, RemoteException
{
	 
	
	CompanyCancelRequest in = new CompanyCancelRequest(companyCode);
	in.setCompanyCode(companyCode == null ? "" : companyCode);
	WSCache.companyServer.cancel(in);
	
}*/
private CompanyDetailResponse getCompany(String companyCode, HttpServletRequest request) throws com.seda.payer.pgec.webservice.srv.FaultType, RemoteException
{
	CompanyDetailRequest detailRequest = new CompanyDetailRequest(companyCode);
	detailRequest.setCompanyCode(companyCode == null ? "" : companyCode);
	return WSCache.companyServer.getCompany(detailRequest, request);
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

}
