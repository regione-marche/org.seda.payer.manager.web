package org.seda.payer.manager.configurazione.actions;


import java.rmi.RemoteException;
import java.util.Enumeration;

import org.seda.payer.manager.util.Messages;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;

import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.anagservizi.dati.AnagServizi;
import com.seda.payer.pgec.webservice.anagservizi.dati.AnagServiziCancelRequest;
import com.seda.payer.pgec.webservice.anagservizi.dati.AnagServiziDetailRequest;
import com.seda.payer.pgec.webservice.anagservizi.dati.AnagServiziDetailResponse;
import com.seda.payer.pgec.webservice.anagservizi.dati.AnagServiziResponse;
import com.seda.payer.pgec.webservice.anagservizi.dati.AnagServiziResponsePageInfo;
import com.seda.payer.pgec.webservice.anagservizi.dati.AnagServiziSaveRequest;
import com.seda.payer.pgec.webservice.anagservizi.dati.AnagServiziSearchRequest;
import com.seda.payer.pgec.webservice.anagservizi.dati.AnagServiziSearchResponse;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;


public class AnagServiziAction extends DispatchHtmlAction {

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
					request.setAttribute("anagservizi_codiceAnagServizi", "");
					request.setAttribute("anagservizi_descrizioneAnagServizi", "");
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
			String codiceAnagServizi = ((String)request.getAttribute("anagservizi_codiceAnagServizi")==null ? "":  (String)request.getAttribute("anagservizi_codiceAnagServizi"));
			String descrizioneAnagServizi = ((String)request.getAttribute("anagservizi_descrizioneAnagServizi")==null ? "":  (String)request.getAttribute("anagservizi_descrizioneAnagServizi"));
			
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonN = (String)request.getAttribute("tx_button_nuovo");
			
			if (firedButton!=null||firedButtonN!=null){
					codiceAnagServizi = request.getParameter("");
					descrizioneAnagServizi = request.getParameter("");
			}
			
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				codiceAnagServizi="";
				descrizioneAnagServizi="";
				
			}
			
			AnagServiziSearchResponse searchResponse = getAnagServiziSearchResponse(codiceAnagServizi, descrizioneAnagServizi, rowsPerPage, pageNumber, order, request);
			AnagServiziResponse anagserviziResponse = searchResponse.getResponse();
			AnagServiziResponsePageInfo responsePageInfo = anagserviziResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			request.setAttribute("anagservizi_codiceAnagServizi", codiceAnagServizi);
			request.setAttribute("anagservizi_descrizioneAnagServizi",descrizioneAnagServizi);
			
			request.setAttribute("anagservizis", anagserviziResponse.getListXml());
			request.setAttribute("anagservizis.pageInfo", pageInfo);
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
        request.setAttribute("codiceAnagServizi",request.getParameter("codiceAnagServizi"));
		return null; 
	}	
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			
			AnagServiziDetailResponse response = 
				getAnagServiziDetailSearchResponse((String)request.getAttribute("anagservizi_codiceAnagServizi"), request);
			request.setAttribute("anagservizi_codiceAnagServizi", response.getAnagservizi().getCodiceAnagServizi());
			request.setAttribute("anagservizi_descrizioneAnagServizi", response.getAnagservizi().getDescrizioneAnagServizi());
			
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
			//System.out.println(firedButton+" saveedit");
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					if(request.getAttribute("anagservizi_codiceAnagServizi") !=null){
						request.setAttribute("anagservizi_codiceAnagServizi", null);
						request.setAttribute("anagservizi_descrizioneAnagServizi", null);
						index(request);
					}
				}if(firedButtonReset!=null){
					if(firedButtonReset.equals("Reset"))					
						resetParametri(request);
				}
			}
			else save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}

	public Object saveedit(HttpServletRequest request) throws ActionException {
		try {
			request.setAttribute("message", Messages.UPDT_OK.format());
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			//System.out.println(firedButton+" saveedit");
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					if(request.getAttribute("anagservizi_codiceAnagServizi") !=null){
						request.setAttribute("anagservizi_codiceAnagServizi", null);
						request.setAttribute("anagservizi_descrizioneAnagServizi", null);
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
		String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		//System.out.println(codOp);
		String codiceAnagServizi = request.getParameter("anagservizi_codiceAnagServizi");
		String descrizioneAnagServizi = request.getParameter("anagservizi_descrizioneAnagServizi");
	    //AnagServiziImplementationStub stub = null;
		try {
			
			AnagServizi anagservizi = new AnagServizi(codiceAnagServizi, descrizioneAnagServizi,usernameAutenticazione);
			/* we prepare object for save */
			AnagServiziSaveRequest saveRequest = new AnagServiziSaveRequest(anagservizi,codOp);
			/* we save object */
			saveAnagServizi(saveRequest, request);

		} catch (Exception e) {
			try {
				//AnagServiziDetailRequest detailRequest = new AnagServiziDetailRequest(codiceAnagServizi);
			    AnagServiziDetailResponse detailResponse = getAnagServizi(codiceAnagServizi, request);
			    request.setAttribute("anagservizi_codiceAnagServizi", detailResponse.getAnagservizi().getCodiceAnagServizi());
				request.setAttribute("anagservizi_descrizioneAnagServizi", detailResponse.getAnagservizi().getDescrizioneAnagServizi());
				
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.GENERIC_ERR.format());
			request.setAttribute("error", "error"); 
			System.out.println(e.getMessage());
		}
	}
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String anagserviziCode = request.getParameter("anagservizi_codiceAnagServizi");
	    //AnagServiziImplementationStub stub = null;
		try {
			request.setAttribute("varname", "anagservizi");
			/* we prepare object for cancel */
			AnagServiziCancelRequest cancelRequest = new AnagServiziCancelRequest(anagserviziCode);
			/* we cancel object */
			cancelAnagServizi(cancelRequest, request);
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
	
	private AnagServiziSearchResponse getAnagServiziSearchResponse(
			String anagServiziCode, String anagServiziDescription, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		AnagServiziSearchResponse res = null;
		AnagServiziSearchRequest in = new AnagServiziSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setCodiceAnagServizi(anagServiziCode == null ? "" : anagServiziCode);
		in.setDescrizioneAnagServizi(anagServiziDescription == null ? "" : anagServiziDescription);
		
		res = WSCache.anagServiziServer.getAnagServizis(in, request);
		return res;
	}
	
	private AnagServiziDetailResponse getAnagServiziDetailSearchResponse(String anagServiziCode, HttpServletRequest request) throws FaultType, RemoteException
	{
		 
		AnagServiziDetailResponse res = null;
		AnagServiziDetailRequest in = new AnagServiziDetailRequest(anagServiziCode);
		in.setCodiceAnagServizi(anagServiziCode == null ? "" : anagServiziCode);
		res = WSCache.anagServiziServer.getAnagServizi(in, request);
		return res;
	}
	
	private void  saveAnagServizi(AnagServiziSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.anagServiziServer.save(saveRequest, request);
		
	}
	
	private void  cancelAnagServizi(AnagServiziCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.anagServiziServer.cancel(cancelRequest, request);
		
	}
	
	private AnagServiziDetailResponse getAnagServizi(String codiceAnagServ, HttpServletRequest request) throws com.seda.payer.pgec.webservice.srv.FaultType, RemoteException
	{
		AnagServiziDetailRequest detailRequest = new AnagServiziDetailRequest(codiceAnagServ);
		detailRequest.setCodiceAnagServizi(codiceAnagServ == null ? "" : codiceAnagServ);
		return WSCache.anagServiziServer.getAnagServizi(detailRequest, request);
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