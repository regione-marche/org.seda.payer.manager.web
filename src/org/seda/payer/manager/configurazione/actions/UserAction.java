/**
 * 
 */
package org.seda.payer.manager.configurazione.actions;


import java.rmi.RemoteException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;

import com.seda.payer.commons.bean.TypeRequest;

import com.seda.payer.pgec.webservice.company.dati.CompanySearchRequest;
import com.seda.payer.pgec.webservice.company.dati.CompanySearchResponse;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.user.dati.User;
import com.seda.payer.pgec.webservice.user.dati.UserCancelRequest;
import com.seda.payer.pgec.webservice.user.dati.UserDetailRequest;
import com.seda.payer.pgec.webservice.user.dati.UserDetailResponse;
import com.seda.payer.pgec.webservice.user.dati.UserResponse;
import com.seda.payer.pgec.webservice.user.dati.UserResponsePageInfo;
import com.seda.payer.pgec.webservice.user.dati.UserSaveRequest;
import com.seda.payer.pgec.webservice.user.dati.UserSearchRequest;
import com.seda.payer.pgec.webservice.user.dati.UserSearchResponse;

//import com.seda.payer.pgec.webservice.userappl.dati.UserAppl;
import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.iban.IBAN;
import org.seda.payer.manager.ws.WSCache;

/**
 * 
 * @author mmontisano 
 *
 */
public class UserAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	
	private String usernameAutenticazione;
	

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
		
		if( user!=null){ 
		   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());		   
		}
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
		search(request);
		return null;
	}

	public Object search(HttpServletRequest request) throws ActionException {
		try {
			
	
			String companyCode = ((String)request.getAttribute("user_searchCompanyCode")==null ? "":  (String)request.getAttribute("user_searchCompanyCode"));
			String companyDescription = ((String)request.getAttribute("user_searchCompanyDescription")==null ? "":  (String)request.getAttribute("user_searchCompanyDescription"));
			String userCode = ((String)request.getAttribute("user_searchUserCode")==null ? "":  (String)request.getAttribute("user_searchUserCode"));
			String userDescription = ((String)request.getAttribute("user_searchUserDescription")==null ? "":  (String)request.getAttribute("user_searchUserDescription"));
			String scopeCncCode = ((String)request.getAttribute("user_searchScopeCncCode")==null ? "":  (String)request.getAttribute("user_searchScopeCncCode"));
			
								
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			if (firedButton!=null){
					
				request.setAttribute("user_searchCompanyCode", "");
				request.setAttribute("user_searchCompanyDescription", "");
				request.setAttribute("user_searchUserCode", "");
				request.setAttribute("user_searchUserDescription", "");
				request.setAttribute("user_searchScopeCncCode", "");
				companyCode="";
				companyDescription="";
				userCode="";
				userDescription="";
				scopeCncCode="";
				
				
			}
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				companyCode="";
				companyDescription="";
				userCode="";
				userDescription="";
				scopeCncCode="";
				
			}
			
			UserSearchResponse searchResponse = getUsers(companyCode, companyDescription, userCode, userDescription, scopeCncCode,"", rowsPerPage, pageNumber, order, request);
			UserResponse userResponse = searchResponse.getResponse();
			UserResponsePageInfo responsePageInfo = userResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			//request.setAttribute("user", searchRequest);
			request.setAttribute("user_searchCompanyCode",companyCode);
			request.setAttribute("user_searchCompanyDescription",companyDescription);
			request.setAttribute("user_searchUserCode",userCode);
			request.setAttribute("user_searchUserDescription",userDescription);					
			request.setAttribute("user_searchScopeCncCode",scopeCncCode);
			
			
			request.setAttribute("users", userResponse.getListXml());
			request.setAttribute("users.pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			
			/* we retry all companies */
			CompanySearchResponse companySearchResponse = getCompanySearchResponse("", "", 0, 0, "", request);
			request.setAttribute("companies", companySearchResponse.getResponse().getListXml());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {	  
		request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		request.setAttribute("companyCode",request.getParameter("companyCode"));
        request.setAttribute("userCode",request.getParameter("userCode"));
		return null; 
	}	
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			CompanySearchResponse companySearchResponse = getCompanySearchResponse("", "", 0, 0, "", request);
			request.setAttribute("companies", companySearchResponse.getResponse().getListXml());
			
			UserDetailResponse response = getUser(request.getParameter("user_userCode"), request.getParameter("user_companyCode"), request);
			//request.setAttribute("user", response.getUser());
			request.setAttribute("user_userCode",response.getUser().getUserCode());
			request.setAttribute("user_companyCode",response.getUser().getCompanyCode());
			request.setAttribute("user_scopeCncCode",response.getUser().getScopeCncCode());
			request.setAttribute("user_userDescription",response.getUser().getUserDescription());
			
			request.setAttribute("user_ordinanteDescription",response.getUser().getOrdinanteDescription());                      

			request.setAttribute("user_codiceFiscale",response.getUser().getCodiceFiscale());                      
			request.setAttribute("user_codiceSia",response.getUser().getCodiceSia());                              
			request.setAttribute("user_codiceIban",response.getUser().getCodiceIban());                            
			request.setAttribute("user_utenteFtp",response.getUser().getUtenteFtp());                              
			request.setAttribute("user_passwordFtp",response.getUser().getPasswordFtp());                          
			request.setAttribute("user_serverFtp",response.getUser().getServerFtp());                              
			request.setAttribute("user_dirRemotaServerFtp",response.getUser().getDirRemotaServerFtp());            
			request.setAttribute("user_flagAbilitazioneInvioFtp",response.getUser().getFlagAbilitazioneInvioFtp());
		
			/*request.setAttribute("user_accountNumber",response.getUser().getAccountNumber());
			request.setAttribute("user_accountHolder",response.getUser().getAccountHolder());
			request.setAttribute("user_flagCheckRangeAbi",response.getUser().getFlagCheckRangeAbi());
			request.setAttribute("user_emailFrom",response.getUser().getEmailFrom());
			request.setAttribute("user_emailCcn",response.getUser().getEmailCcn());
			request.setAttribute("user_emailTo",response.getUser().getEmailTo());
			request.setAttribute("user_descrMittente",response.getUser().getDutedmit());
			request.setAttribute("user_attachFlag",response.getUser().getAttachFlag());*/

			
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
					/*request.setAttribute("user_companyCode", "");
					request.setAttribute("user_userCode", "");
					request.setAttribute("user_userDescription", "");
					request.setAttribute("user_scopeCncCode", "");*/
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
					/*request.setAttribute("user_companyCode", "");
					request.setAttribute("user_userCode", "");
					request.setAttribute("user_userDescription", "");
					request.setAttribute("user_scopeCncCode", "");	*/					
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

		
		String userCode = request.getParameter("user_userCode");
	    String companyCode = request.getParameter("user_companyCode");
	    String scopeCncCode = request.getParameter("user_scopeCncCode");
	    String userDescription = request.getParameter("user_userDescription");
	    
	    String ordinanteDescription = request.getParameter("user_ordinanteDescription");
	    
	    String codiceFiscale = request.getParameter("user_codiceFiscale");
	    String codiceSia = request.getParameter("user_codiceSia");
	    String codiceIban = request.getParameter("user_codiceIban");
	    

	    String utenteFtp = request.getParameter("user_utenteFtp");
	    String passwordFtp = request.getParameter("user_passwordFtp");
	    String serverFtp = request.getParameter("user_serverFtp");
	    String dirRemotaServerFtp = request.getParameter("user_dirRemotaServerFtp");
	    String flagAbilitazioneInvioFtp = request.getParameter("user_flagAbilitazioneInvioFtp");

	    try {
		    //test IBAN
		    if (!codiceIban.trim().equals(""))
		    {
			    IBAN iban = new IBAN(codiceIban);
			    if (!iban.isValid())
			    {
			    	throw new ActionException("Codice Iban errato:" + iban.getInvalidCause());
			    }
		    }
		    // fine test IBAN	

	        User user = new User(companyCode, userCode, scopeCncCode, userDescription, ordinanteDescription, codiceFiscale, 
					codiceSia, codiceIban, utenteFtp, passwordFtp, serverFtp, dirRemotaServerFtp, flagAbilitazioneInvioFtp, usernameAutenticazione);
			/* we prepare object for save */
			UserSaveRequest saveRequest = new UserSaveRequest(user,codOp);
			/* we save object */
			saveUser(saveRequest, request);

		} 
		catch (ActionException ae)
		{
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", ae.getMessage());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", ae.getMessage());
			request.setAttribute("error", "error"); 
		}
	    catch (Exception e) 
	    {
			try {				
				UserDetailResponse detailResponse = getUser(userCode, companyCode, request);
				//request.setAttribute("user", detailResponse.getUser());
				request.setAttribute("user_userCode",detailResponse.getUser().getUserCode());
				request.setAttribute("user_companyCode",detailResponse.getUser().getCompanyCode());
				request.setAttribute("user_scopeCncCode",detailResponse.getUser().getScopeCncCode());
				request.setAttribute("user_userDescription",detailResponse.getUser().getUserDescription());
				
				request.setAttribute("user_ordinanteDescription",detailResponse.getUser().getOrdinanteDescription());

				request.setAttribute("user_codiceFiscale",detailResponse.getUser().getCodiceFiscale());
				request.setAttribute("user_codiceSia",detailResponse.getUser().getCodiceSia());    
				request.setAttribute("user_codiceIban",detailResponse.getUser().getCodiceIban());   
				request.setAttribute("user_utenteFtp",detailResponse.getUser().getUtenteFtp());    
				request.setAttribute("user_passwordFtp",detailResponse.getUser().getPasswordFtp());  
				request.setAttribute("user_serverFtp",detailResponse.getUser().getServerFtp());    
				request.setAttribute("user_dirRemotaServerFtp",detailResponse.getUser().getDirRemotaServerFtp());      
				request.setAttribute("user_flagAbilitazioneInvioFtp",detailResponse.getUser().getFlagAbilitazioneInvioFtp());
				
			} 
			catch (Exception ignore) { }

			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.GENERIC_ERR.format());
			request.setAttribute("error", "error"); 
			//System.out.println(e.getMessage());
		}
	}
	
	
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String userCode = request.getParameter("user_userCode");
	    String companyCode = request.getParameter("user_companyCode");	    
		try {
			request.setAttribute("varname", "user");
			
			/* we prepare object for cancel */
			UserCancelRequest cancelRequest = new UserCancelRequest(userCode,companyCode);
			/* we cancel object */
			cancelUser(cancelRequest, request);
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
	private UserDetailResponse getUser(String user_userCode, String user_companyCode, HttpServletRequest request) throws FaultType, RemoteException
	{
		UserDetailRequest in = new UserDetailRequest(user_userCode, user_companyCode);
		in.setUserCode(user_userCode == null ? "" : user_userCode);
		in.setCompanyCode(user_companyCode == null ? "" : user_companyCode);
		return WSCache.userServer.getUser(in, request);
	}
	private UserSearchResponse getUsers(String companyCode, String companyDescription, String userCode, String userDescription, String scopeCncCode, String strDescrSocieta, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		UserSearchRequest in = new UserSearchRequest(companyCode, companyDescription, userCode, userDescription, scopeCncCode, strDescrSocieta, rowsPerPage, pageNumber, order);
		
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setCompanyDescription(companyDescription == null ? "" : companyDescription);
		in.setUserCode(userCode == null ? "" : userCode);
		in.setUserDescription(userDescription == null ? "" :userDescription);
		in.setScopeCncCode(scopeCncCode == null ? "" : scopeCncCode);
		in.setStrDescrSocieta(strDescrSocieta == null ? "" : strDescrSocieta);
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);	
		return WSCache.userServer.getUsers(in, request);
	}

	private void  saveUser(UserSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.userServer.save(saveRequest, request);
		
	}
	private void  cancelUser(UserCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.userServer.cancel(cancelRequest, request);
		
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