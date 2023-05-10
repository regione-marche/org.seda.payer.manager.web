package org.seda.payer.manager.configurazione.actions;

import com.seda.data.spi.PageInfo;

import java.util.Enumeration;
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.TotemTipologiaImposta.dati.*;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class TotemAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	//private String order;
	private String usernameAutenticazione = null;
	
	@Override
	public void start(HttpServletRequest request) {
		rowsPerPage = (request.getParameter("rowsPerPage") == null) ? getDefaultListRows() : Integer.parseInt(request.getParameter("rowsPerPage"));
		pageNumber = (request.getParameter("pageNumber") == null) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		//order = (request.getParameter("order") == null) ? "" : request.getParameter("order");
		
		replyAttributes(false, request,"pageNumber","rowsPerPage");
		
		HttpSession session = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		
		if( user!=null) 
		   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
		usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
		
		String firedButton = (String)request.getAttribute("tx_button_indietro");
		if (firedButton!=null) {
			if (firedButton.equals("Indietro")){
				request.setAttribute("codice_ente", "");
				request.setAttribute("imposta_servizio", "");
				request.setAttribute("tipologia_imposta", "");
				try {
					index(request);
				} catch (ActionException e) {
					e.printStackTrace();
				}
			}
		}
		String firedButtonNew = (String)request.getAttribute("tx_button_nuovo");
		if (firedButtonNew!=null){
				request.setAttribute("codice_ente", "");
				request.setAttribute("imposta_servizio", "");
				request.setAttribute("tipologia_imposta", "");
		}
	}

	@Override
	public Object index(HttpServletRequest request) throws ActionException {
		search(request);	
		return null;
	}
	
	public Object search(HttpServletRequest request) throws ActionException {
		try {
			String codEnte = ((String)request.getAttribute("codice_ente")==null ? "":  (String)request.getAttribute("codice_ente"));
			String impostaServizio = ((String)request.getAttribute("imposta_servizio")==null ? "":  (String)request.getAttribute("imposta_servizio"));
			String tipologiaImposta = ((String)request.getAttribute("tipologia_imposta")==null ? "":  (String)request.getAttribute("tipologia_imposta"));

			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonN = (String)request.getAttribute("tx_button_nuovo");
			if (firedButton!=null||firedButtonN!=null){
				codEnte = request.getParameter("");
				impostaServizio = request.getParameter("");
				tipologiaImposta = request.getParameter("");
			}
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				codEnte="";
				impostaServizio="";
				tipologiaImposta = "";
			}
			
			TotemTipologiaImpostaSearchResponse searchResponse = null;
			if (firedButton!=null)
				 if(firedButton.equals("Indietro"))
					 searchResponse = getTotemTipologiaImpostaSearchResponse(null, null, null, rowsPerPage, pageNumber, request);
			if (firedButton==null)  searchResponse = getTotemTipologiaImpostaSearchResponse(codEnte,impostaServizio, tipologiaImposta, rowsPerPage, pageNumber, request);
			
			TotemTipologiaImpostaResponse totemResponse = searchResponse.getResponse();
			TotemTipologiaImpostaResponsePageInfo responsePageInfo = totemResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			
			request.setAttribute("codice_ente", codEnte);
			request.setAttribute("imposta_servizio", impostaServizio);
			request.setAttribute("tipologia_imposta", tipologiaImposta);

			request.setAttribute("totem_list", totemResponse.getListXml());
			request.setAttribute("totem_list.pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {	  
		request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		request.setAttribute("codice_ente",request.getParameter("codice_ente"));		  
		request.setAttribute("imposta_servizio",request.getParameter("imposta_servizio"));		  
		return null; 
	}
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			
			TotemTipologiaImpostaDetailResponse response = getTotemTipologiaImpostaDetailSearchResponse(request.getParameter("codice_ente"), request.getParameter("imposta_servizio"), request);		
			request.setAttribute("codice_ente", response.getTotemTipologiaImposta().getCodiceEnte());
			request.setAttribute("imposta_servizio", response.getTotemTipologiaImposta().getImpostaServizio());
			request.setAttribute("tipologia_imposta", response.getTotemTipologiaImposta().getTipologiaImposta());
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
				if(firedButton.equals("Indietro") && (request.getAttribute("codice_ente") != null) && (request.getAttribute("imposta_servizio") != null)){
						request.setAttribute("codice_ente", null);
						request.setAttribute("imposta_servizio", null);
						index(request);
				}
			} else if(firedButtonReset!=null){
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
				if(firedButton.equals("Indietro") && (request.getAttribute("codice_ente") != null) && (request.getAttribute("imposta_servizio") != null)){
					request.setAttribute("codice_ente", null);
					request.setAttribute("imposta_servizio", null);
					index(request);				
				}
			} else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset"))					
					edit(request);
			}else save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}
	
	private void save(HttpServletRequest request) throws ActionException {
		String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		String codiceEnte = request.getParameter("codice_ente");
	    String impostaServizio = request.getParameter("imposta_servizio");
	    String tipologiaImposta = request.getParameter("tipologia_imposta");
		try {
					
			TotemTipologiaImposta totem = new TotemTipologiaImposta(codiceEnte, impostaServizio, tipologiaImposta, usernameAutenticazione);
			TotemTipologiaImpostaSaveRequest saveRequest = new TotemTipologiaImpostaSaveRequest(totem,codOp);
			saveTotemTipologiaImposta(saveRequest, request);

		} catch (Exception e) {
			try {
				TotemTipologiaImpostaDetailResponse detailResponse = getTotemTipologiaImposta(codiceEnte, impostaServizio, request);
				request.setAttribute("codice_ente", detailResponse.getTotemTipologiaImposta().getCodiceEnte());
				request.setAttribute("imposta_servizio", detailResponse.getTotemTipologiaImposta().getImpostaServizio());
				request.setAttribute("tipologia_imposta", detailResponse.getTotemTipologiaImposta().getTipologiaImposta());
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.GENERIC_ERR.format());
			request.setAttribute("error", "error"); 
			System.out.println(e.getMessage());
		}
	}
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String codiceEnte = request.getParameter("codice_ente");
		String impostaServizio = request.getParameter("imposta_servizio");
		try {
			request.setAttribute("varname", "totem");
			TotemTipologiaImpostaCancelRequest cancelRequest = new TotemTipologiaImpostaCancelRequest(codiceEnte, impostaServizio);
			cancelTotemTipologiaImposta(cancelRequest, request);
			request.setAttribute("message", Messages.CANC_OK.format());
		} catch (FaultType f) {
			request.setAttribute("message", Messages.valueOf("ERR_" + f.getCode()).format());
		} catch (Exception e) {
			request.setAttribute("message", Messages.CANCEL_ERRDIP.format());
		}
		return null;
	}	

	private TotemTipologiaImpostaDetailResponse getTotemTipologiaImpostaDetailSearchResponse(String codiceEnte, String impostaServizio, HttpServletRequest request) throws FaultType, RemoteException {
		TotemTipologiaImpostaDetailResponse res = null;
		TotemTipologiaImpostaDetailRequest in = new TotemTipologiaImpostaDetailRequest(codiceEnte, impostaServizio);
		in.setCodiceEnte(codiceEnte == null ? "" : codiceEnte);
		in.setCodiceEnte(impostaServizio == null ? "" : impostaServizio);
		res = WSCache.totemTipologiaImpostaServer.getTotemTipologiaImpostaSelect(in, request);
		return res;
	}
	
	private TotemTipologiaImpostaSearchResponse getTotemTipologiaImpostaSearchResponse(String codEnte, String impostaServizio,  String tipologiaImposta, int rowsPerPage, int pageNumber, HttpServletRequest request) throws FaultType, RemoteException {
		TotemTipologiaImpostaSearchResponse res = null;
		TotemTipologiaImpostaSearchRequest in = new TotemTipologiaImpostaSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setCodiceEnte(codEnte == null ? "" : codEnte);
		in.setImpostaServizio(impostaServizio == null ? "" : impostaServizio);
		in.setTipologiaImposta(tipologiaImposta == null ? "" : tipologiaImposta);

		res = WSCache.totemTipologiaImpostaServer.getTotemTipologiaImpostaList(in, request);
		return res;
	}
	
	private void saveTotemTipologiaImposta(TotemTipologiaImpostaSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException {
		WSCache.totemTipologiaImpostaServer.save(saveRequest, request);
		
	}

	private void cancelTotemTipologiaImposta(TotemTipologiaImpostaCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException {
		WSCache.totemTipologiaImpostaServer.cancel(cancelRequest, request);		
	}
	
	private TotemTipologiaImpostaDetailResponse getTotemTipologiaImposta(String codiceEnte, String impostaServzio, HttpServletRequest request) throws FaultType, RemoteException {
		TotemTipologiaImpostaDetailRequest detailRequest = new TotemTipologiaImpostaDetailRequest(codiceEnte, impostaServzio);
		detailRequest.setCodiceEnte(codiceEnte == null ? "" : codiceEnte);
		detailRequest.setImpostaServizio(impostaServzio == null ? "" : impostaServzio);
		return WSCache.totemTipologiaImpostaServer.getTotemTipologiaImpostaSelect(detailRequest, request);
	}

	protected int getDefaultListRows() {
		int defaultListRows = 4;
		PropertiesTree configuration = (PropertiesTree) context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		if(configuration != null)
		{
			String s_defaultListRows = configuration.getProperty(PropertiesPath.defaultListRows.format());
			if (s_defaultListRows != null) defaultListRows = Integer.parseInt(s_defaultListRows);
		}
		return defaultListRows;
	}
	
	protected void resetParametri(HttpServletRequest request) {
		Enumeration e = request.getParameterNames();
		String p = "";
		while (e.hasMoreElements()) {
			
			p = (String) e.nextElement();
			request.setAttribute(p, "");
		}
	}

}
