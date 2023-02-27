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
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import org.seda.payer.manager.util.Messages;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.company.dati.CompanySearchRequest;
import com.seda.payer.pgec.webservice.company.dati.CompanySearchResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.tipologiaservizio.dati.TipologiaServizio;
import com.seda.payer.pgec.webservice.tipologiaservizio.dati.TipologiaServizioCancelRequest;
import com.seda.payer.pgec.webservice.tipologiaservizio.dati.TipologiaServizioDetailRequest;
import com.seda.payer.pgec.webservice.tipologiaservizio.dati.TipologiaServizioDetailResponse;
import com.seda.payer.pgec.webservice.tipologiaservizio.dati.TipologiaServizioResponse;
import com.seda.payer.pgec.webservice.tipologiaservizio.dati.TipologiaServizioResponsePageInfo;
import com.seda.payer.pgec.webservice.tipologiaservizio.dati.TipologiaServizioSaveRequest;
import com.seda.payer.pgec.webservice.tipologiaservizio.dati.TipologiaServizioSearchRequest;
import com.seda.payer.pgec.webservice.tipologiaservizio.dati.TipologiaServizioSearchResponse;



public class TipologiaServizioAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private String strCodiceSocieta,usernameAutenticazione;

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
				request.setAttribute("userAppl_codiceSocieta",user.getCodiceSocieta().trim());
		   }
		   //request.setAttribute("userAppl_codiceUtente",userAppl.getCodiceUtente().trim());
		   //request.setAttribute("userAppl_chiaveEnteCon",userAppl.getChiaveEnteCon().trim());
		   //request.setAttribute("userAppl_chiaveEnteEnt",userAppl.getChiaveEnteEnt().trim());
		usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
		strCodiceSocieta = (String)request.getAttribute("userAppl_codiceSocieta");
		String firedButtonReset = (String)request.getAttribute("tx_button_reset");
		if (firedButtonReset!=null){
			if(firedButtonReset.equals("Reset")){
				try {
					add(request);
				} catch (ActionException e) {
					e.printStackTrace();
				}
				
			}
		}
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
			resetParametri(request);
			if(firedButton.equals("Indietro")){
				request.setAttribute("tipologiaservizio_company", null);
				request.setAttribute("tipologiaservizio_codiceTipologiaServizio", null);
				request.setAttribute("tipologiaservizio_descrizioneTipologiaServizio", null);
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
			String company = ((String)request.getAttribute("tipologiaservizio_company")==null ? "":  (String)request.getAttribute("tipologiaservizio_company"));
			String codiceTipologiaServizio =((String)request.getAttribute("tipologiaservizio_codiceTipologiaServizio")==null ? "":  (String)request.getAttribute("tipologiaservizio_codiceTipologiaServizio")); 
			String descrizioneTipologiaServizio = ((String)request.getAttribute("tipologiaservizio_descrizioneTipologiaServizio")==null ? "":  (String)request.getAttribute("tipologiaservizio_descrizioneTipologiaServizio"));
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			//String firedButtonNuovo = (String)request.getAttribute("tx_button_nuovo");
			if (firedButtonReset!=null){				
				resetParametri(request);
				company="";
				codiceTipologiaServizio="";
				descrizioneTipologiaServizio="";
				
			}
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			
			if (firedButton!=null/*|| firedButtonNuovo!=null*/){
					codiceTipologiaServizio = request.getParameter("");
					descrizioneTipologiaServizio = request.getParameter("");
					company = request.getParameter("");
			}
			TipologiaServizioSearchResponse searchResponse = null;
			if (firedButton!=null){
					 searchResponse = getTipologiaServizioSearchResponse(null,null, null, rowsPerPage, pageNumber, order, request);
			}
			else searchResponse = getTipologiaServizioSearchResponse(company, codiceTipologiaServizio, descrizioneTipologiaServizio, rowsPerPage, pageNumber, order, request);
			TipologiaServizioResponse tipologiaServizioResponse = searchResponse.getResponse();
			TipologiaServizioResponsePageInfo responsePageInfo = tipologiaServizioResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			//request.setAttribute("tipologiaservizio", searchRequest);
			  request.setAttribute("tipologiaservizio_company", company);
			  request.setAttribute("tipologiaservizio_codiceTipologiaServizio", codiceTipologiaServizio);
			  request.setAttribute("tipologiaservizio_descrizioneTipologiaServizio", descrizioneTipologiaServizio);
			
			request.setAttribute("tipologiaservizios", tipologiaServizioResponse.getListXml());
			request.setAttribute("tipologiaservizios.pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			 /*we retry all companies*/ 
			String companyCode =(strCodiceSocieta!=null && strCodiceSocieta.length()>0)?strCodiceSocieta:"";
			CompanySearchResponse searchResponse = getCompanySearchResponse(companyCode, "", 0, 0, "", request);
			//CompanyResponse companyResponse = searchResponse.getResponse()
			//CompanyImplementationStub stubCompany = new CompanyImplementationStub(new URL(getCompanyManagerEndpointUrl()), null);
			//CompanySearchResponse companySearchResponse = stubCompany.getCompanys(new CompanySearchRequest(companyCode, "", 0, 0, ""));
			request.setAttribute("companies", searchResponse.getResponse().getListXml());
			request.setAttribute("tipologiaservizio_codiceTipologiaServizio", null);
			request.setAttribute("tipologiaservizio_descrizioneTipologiaServizio", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {
		  request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		  request.setAttribute("companyCode",request.getParameter("companyCode"));
          request.setAttribute("codiceTipologiaServizio",request.getParameter("codiceTipologiaServizio"));		  
		return null; 
	}	
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			String companyCode =(strCodiceSocieta!=null && strCodiceSocieta.length()>0)?strCodiceSocieta:"";
			CompanySearchResponse searchResponse = getCompanySearchResponse(companyCode, "", 0, 0, "", request);
			request.setAttribute("companies", searchResponse.getResponse().getListXml());
			
			TipologiaServizioDetailResponse response = getTipologiaServizioDetailSearchResponse(request.getParameter("tipologiaservizio_codiceTipologiaServizio"), request.getParameter("tipologiaservizio_companyCode"), request);
			request.setAttribute("tipologiaservizio_companyCode", response.getTipologiaservizio().getCompanyCode());
			request.setAttribute("tipologiaservizio_codiceTipologiaServizio", response.getTipologiaservizio().getCodiceTipologiaServizio());
			request.setAttribute("tipologiaservizio_descrizioneTipologiaServizio", response.getTipologiaservizio().getDescrizioneTipologiaServizio());
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
					//if(request.getAttribute("tipologiaservizio_companyCode") !=null){
						request.setAttribute("tipologiaservizio_companyCode", null);
						request.setAttribute("tipologiaservizio_codiceTipologiaServizio", null);
						request.setAttribute("tipologiaservizio_descrizioneTipologiaServizio", null);
						index(request);
					//}
				}
			}
			else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset"))					
					resetParametri(request);
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
					if(request.getAttribute("tipologiaservizio_companyCode") !=null){
						request.setAttribute("tipologiaservizio_companyCode", null);
						request.setAttribute("tipologiaservizio_codiceTipologiaServizio", null);
						request.setAttribute("tipologiaservizio_descrizioneTipologiaServizio", null);
						index(request);
					}
				}
			}
			else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){
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
		String codiceTipologiaServizio = request.getParameter("tipologiaservizio_codiceTipologiaServizio");
	    String companyCode = request.getParameter("tipologiaservizio_companyCode");
	    String descrizioneTipologiaServizio = request.getParameter("tipologiaservizio_descrizioneTipologiaServizio");
		try {
			
			TipologiaServizio tipologiaservizio = new TipologiaServizio(companyCode, codiceTipologiaServizio, descrizioneTipologiaServizio, usernameAutenticazione);
			/* we prepare object for save */
			TipologiaServizioSaveRequest saveRequest = new TipologiaServizioSaveRequest(tipologiaservizio,codOp);
			/* we save object */
			saveTipologiaServizio(saveRequest, request);
			
		} catch (Exception e) {
			try {
				TipologiaServizioDetailRequest detailRequest = new TipologiaServizioDetailRequest(codiceTipologiaServizio, companyCode);
				TipologiaServizioDetailResponse detailResponse = getTipologiaServizio(detailRequest, request);
				//request.setAttribute("tipologiaservizio", detailResponse.getTipologiaservizio());
				  request.setAttribute("tipologiaservizio_companyCode", detailResponse.getTipologiaservizio().getCompanyCode());
				  request.setAttribute("tipologiaservizio_codiceTipologiaServizio", detailResponse.getTipologiaservizio().getCodiceTipologiaServizio());
				  request.setAttribute("tipologiaservizio_descrizioneTipologiaServizio", detailResponse.getTipologiaservizio().getDescrizioneTipologiaServizio());
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.GENERIC_ERR.format());
			request.setAttribute("error", "error"); 
			System.out.println(e.getMessage());
		}
	}
	
	
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String codiceTipologiaServizio = request.getParameter("tipologiaservizio_codiceTipologiaServizio");
		String companyCode = request.getParameter("tipologiaservizio_companyCode");
		try {
			request.setAttribute("varname", "tipologiaservizio");
			//stub = new TipologiaServizioImplementationStub(new URL(getTipologiaservizioManagerEndpointUrl()), null);
			/* we prepare object for cancel */
			TipologiaServizioCancelRequest cancelRequest = new TipologiaServizioCancelRequest(codiceTipologiaServizio,companyCode);
			/* we cancel object */
			cancelTipologiaServizio(cancelRequest, request);
			request.setAttribute("message", Messages.CANC_OK.format());
		} catch (FaultType f) {
			request.setAttribute("message", Messages.CANCEL_ERRDIP.format());
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
	
	private TipologiaServizioSearchResponse getTipologiaServizioSearchResponse(
			String company,String codiceTipologiaServizio,String descrizioneTipologiaServizio, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		TipologiaServizioSearchResponse res = null;
		TipologiaServizioSearchRequest in = new TipologiaServizioSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		//in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setCodiceTipologiaServizio(codiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		in.setDescrizioneTipologiaServizio(descrizioneTipologiaServizio == null ? "" :descrizioneTipologiaServizio);
		in.setStrDescrSocieta(company == null ? "" : company);
		res = WSCache.tipologiaServizioServer.getTipologiaServizios(in, request);
		return res;
	}
	
	private TipologiaServizioDetailResponse getTipologiaServizioDetailSearchResponse(String tipologiaServizioCode ,String companyCode, HttpServletRequest request ) throws FaultType, RemoteException
	{
		 
		TipologiaServizioDetailResponse res = null;
		TipologiaServizioDetailRequest in = new TipologiaServizioDetailRequest(tipologiaServizioCode,companyCode);
		in.setCodiceTipologiaServizio(tipologiaServizioCode == null ? "" : tipologiaServizioCode);
		in.setCompanyCode(companyCode  == null ? "" : companyCode);
		res = WSCache.tipologiaServizioServer.getTipologiaServizio(in, request);
		return res;
	}
	
	private void  saveTipologiaServizio(TipologiaServizioSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.tipologiaServizioServer.save(saveRequest, request);
		
	}

	private void  cancelTipologiaServizio(TipologiaServizioCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.tipologiaServizioServer.cancel(cancelRequest, request);
		
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
	
	private TipologiaServizioDetailResponse getTipologiaServizio (TipologiaServizioDetailRequest detailRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		return WSCache.tipologiaServizioServer.getTipologiaServizio(detailRequest, request);
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