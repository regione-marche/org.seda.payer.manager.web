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
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamento;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoCancelRequest;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoDetailRequest;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoDetailResponse;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoResponse;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoResponsePageInfo;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoSaveRequest;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoSearchRequest;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoSearchResponse;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;




public class CanalePagamentoAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order,usernameAutenticazione;

	@Override
	public void start(HttpServletRequest request) {
		rowsPerPage = (request.getParameter("rowsPerPage") == null) 
		   ? getDefaultListRows() : Integer.parseInt(request.getParameter("rowsPerPage"));
		   pageNumber = (request.getParameter("pageNumber") == null) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		   order = (request.getParameter("order") == null) ? "" : request.getParameter("order");

		   HttpSession session = request.getSession();
		   UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);

		   if( user!=null) 
			   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
		   usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
		   
		   /*
			 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
			 */
			replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		   
		   String firedButton = (String)request.getAttribute("tx_button_indietro");
		   String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			//System.out.println(firedButton+"index");
		   if (firedButton!=null|| firedButtonReset!=null){
			   request.setAttribute("canalepagamento_chiaveCanalePagamento", null);
			   request.setAttribute("canalepagamento_descrizioneCanalePagamento", null);
			   try {
					add(request);
				} catch (ActionException e) {
					e.printStackTrace();
				}
		   }
			firedButton = (String)request.getAttribute("tx_button_nuovo");
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
	}

	@Override
	public Object index(HttpServletRequest request) throws ActionException {
		search(request);
		return null;
	}

	public Object search(HttpServletRequest request) throws ActionException {
		try {
				String chiaveCanalePagamento = null;
				String descrizioneCanalePagamento = null;
				String firedButtonIndietro = (String)request.getAttribute("tx_button_indietro");
				if (firedButtonIndietro!=null){
						descrizioneCanalePagamento = request.getParameter("");
						chiaveCanalePagamento = request.getParameter("");
				}
				String firedButtonReset = (String)request.getAttribute("tx_button_reset");
				if (firedButtonReset!=null){				
					resetParametri(request);
					descrizioneCanalePagamento="";
					chiaveCanalePagamento="";
					
				}
				
				String firedButtonN = (String)request.getAttribute("tx_button_nuovo");
				//System.out.println(firedButton+"search");
				if (firedButtonN!=null|| firedButtonReset!=null||firedButtonIndietro!=null){
					request.setAttribute("canalepagamento_chiaveCanalePagamento", null);
					//request.setAttribute("canalepagamento_descrizioneCanalePagamento", null);
					request.setAttribute("canalepagamento_descrizioneCanalePagamento", null);
				}
				else{
					chiaveCanalePagamento = ((String)request.getAttribute("canalepagamento_chiaveCanalePagamento")==null ? "":  (String)request.getAttribute("canalepagamento_chiaveCanalePagamento"));
					descrizioneCanalePagamento = ((String)request.getAttribute("canalepagamento_descrizioneCanalePagamento")==null ? "":  (String)request.getAttribute("canalepagamento_descrizioneCanalePagamento"));
					//request.setAttribute("canalepagamento_descrizioneCanalePagamento2", null);
				}
			/*
			String chiaveCanalePagamento = request.getParameter("canalepagamento_chiaveCanalePagamento");
			String descrizioneCanalePagamento = request.getParameter("canalepagamento_descrizioneCanalePagamento");*/
			
			CanalePagamentoSearchResponse searchResponse = null;
				
			if (firedButtonIndietro!=null||firedButtonReset!=null)
						searchResponse = getCanalePagamentoSearchResponse(null,null, rowsPerPage, pageNumber, order, request);
			else  searchResponse = getCanalePagamentoSearchResponse(chiaveCanalePagamento,descrizioneCanalePagamento, rowsPerPage, pageNumber, order, request);
					
				
			//CanalePagamentoSearchResponse searchResponse = getCanalePagamentoSearchResponse(chiaveCanalePagamento,descrizioneCanalePagamento, rowsPerPage, pageNumber, order);
			CanalePagamentoResponse canalepagamentoResponse = searchResponse.getResponse();
			CanalePagamentoResponsePageInfo responsePageInfo = canalepagamentoResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			//request.setAttribute("canalepagamento", searchRequest);
			request.setAttribute("canalepagamento_chiaveCanalePagamento", chiaveCanalePagamento);
			request.setAttribute("canalepagamento_descrizioneCanalePagamento", descrizioneCanalePagamento);
			request.setAttribute("canalepagamentos", canalepagamentoResponse.getListXml());
			request.setAttribute("canalepagamentos.pageInfo", pageInfo);
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
		request.setAttribute("chiaveCanalePagamento",request.getParameter("chiaveCanalePagamento"));
		return null; 
	}		
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			
			CanalePagamentoDetailResponse response = getCanalePagamentoDetailSearchResponse((String)request.getAttribute("canalepagamento_chiaveCanalePagamento"), request);
			//request.setAttribute("canalepagamento", response.getCanalepagamento());
			request.setAttribute("canalepagamento_chiaveCanalePagamento", response.getCanalepagamento().getChiaveCanalePagamento());
			request.setAttribute("canalepagamento_descrizioneCanalePagamento", response.getCanalepagamento().getDescrizioneCanalePagamento());
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
						request.setAttribute("canalepagamento_chiaveCanalePagamento", null);
						request.setAttribute("canalepagamento_descrizioneCanalePagamento", null);
						index(request);
			}else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){					
					resetParametri(request);
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
			//System.out.println(firedButton+" saveedit");
			if (firedButton!=null){
						request.setAttribute("canalepagamento_chiaveCanalePagamento", null);
						request.setAttribute("canalepagamento_descrizioneCanalePagamento", null);
						index(request);
			}else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){					
					edit(request);
				}
			}else save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}
	
	private void save(HttpServletRequest request) throws ActionException {
		String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		String chiaveCanalePagamento = request.getParameter("canalepagamento_chiaveCanalePagamento");
	    String descrizioneCanalePagamento = request.getParameter("canalepagamento_descrizioneCanalePagamento");
	    //CanalePagamentoImplementationStub stub = null;
		try {
			
			CanalePagamento canalepagamento = new CanalePagamento(chiaveCanalePagamento, descrizioneCanalePagamento,usernameAutenticazione);
			/* we prepare object for save */
			CanalePagamentoSaveRequest saveRequest = new CanalePagamentoSaveRequest(canalepagamento,codOp);
			/* we save object */
			saveCanalePagamento(saveRequest, request);

		} catch (Exception e) {
			try {
			    CanalePagamentoDetailResponse detailResponse = getCanalePagamento(chiaveCanalePagamento, request);
				request.setAttribute("canalepagamento_chiaveCanalePagamento", detailResponse.getCanalepagamento().getChiaveCanalePagamento());
				request.setAttribute("canalepagamento_descrizioneCanalePagamento", detailResponse.getCanalepagamento().getDescrizioneCanalePagamento());		
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.GENERIC_ERR.format());
			request.setAttribute("error", "error"); 
			System.out.println(e.getMessage());
		}
	}
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String chiaveCanalePagamento = request.getParameter("canalepagamento_chiaveCanalePagamento");
	    //CanalePagamentoImplementationStub stub = null;
		try {
			request.setAttribute("varname", "canalepagamento");
			/* we prepare object for cancel */
			CanalePagamentoCancelRequest cancelRequest = new CanalePagamentoCancelRequest(chiaveCanalePagamento);
			/* we cancel object */
			cancelCanalePagamento(cancelRequest, request);
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
	
	private CanalePagamentoSearchResponse getCanalePagamentoSearchResponse(
			String chiaveCanalePagamento, String descrizioneCanalePagamento, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		CanalePagamentoSearchResponse res = null;
		CanalePagamentoSearchRequest in = new CanalePagamentoSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setChiaveCanalePagamento(chiaveCanalePagamento == null ? "" : chiaveCanalePagamento);
		in.setDescrizioneCanalePagamento(descrizioneCanalePagamento == null ? "" : descrizioneCanalePagamento);
		
		res = WSCache.canPagamentoServer.getCanalePagamentos(in, request);
		return res;
	}
	
	private CanalePagamentoDetailResponse getCanalePagamentoDetailSearchResponse(String chiaveCanalePagamento, HttpServletRequest request) throws FaultType, RemoteException
	{
		 
		CanalePagamentoDetailResponse res = null;
		CanalePagamentoDetailRequest in = new CanalePagamentoDetailRequest(chiaveCanalePagamento);
		in.setChiaveCanalePagamento(chiaveCanalePagamento == null ? "" : chiaveCanalePagamento);
		res = WSCache.canPagamentoServer.getCanalePagamento(in, request);
		return res;
	}
	
	private void  saveCanalePagamento(CanalePagamentoSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.canPagamentoServer.save(saveRequest, request);
		
	}
	
	private void  cancelCanalePagamento(CanalePagamentoCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.canPagamentoServer.cancel(cancelRequest, request);
		
	}
	
	private CanalePagamentoDetailResponse getCanalePagamento(String chiaveCanalePagamento, HttpServletRequest request) throws com.seda.payer.pgec.webservice.srv.FaultType, RemoteException
	{
		CanalePagamentoDetailRequest detailRequest = new CanalePagamentoDetailRequest(chiaveCanalePagamento);
		detailRequest.setChiaveCanalePagamento(chiaveCanalePagamento == null ? "" : chiaveCanalePagamento);
		return WSCache.canPagamentoServer.getCanalePagamento(detailRequest, request);
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