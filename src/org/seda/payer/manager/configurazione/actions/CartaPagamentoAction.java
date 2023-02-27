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
import com.seda.payer.pgec.webservice.cartapagamento.dati.CartaPagamento;
import com.seda.payer.pgec.webservice.cartapagamento.dati.CartaPagamentoCancelRequest;
import com.seda.payer.pgec.webservice.cartapagamento.dati.CartaPagamentoDetailRequest;
import com.seda.payer.pgec.webservice.cartapagamento.dati.CartaPagamentoDetailResponse;
import com.seda.payer.pgec.webservice.cartapagamento.dati.CartaPagamentoResponse;
import com.seda.payer.pgec.webservice.cartapagamento.dati.CartaPagamentoResponsePageInfo;
import com.seda.payer.pgec.webservice.cartapagamento.dati.CartaPagamentoSaveRequest;
import com.seda.payer.pgec.webservice.cartapagamento.dati.CartaPagamentoSearchRequest;
import com.seda.payer.pgec.webservice.cartapagamento.dati.CartaPagamentoSearchResponse;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;




public class CartaPagamentoAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private String usernameAutenticazione = null;
	
	@Override
	public void start(HttpServletRequest request) {
		rowsPerPage = request.getParameter("rowsPerPage") == null ? getDefaultListRows() : Integer.parseInt(request.getParameter("rowsPerPage"));

		//pageNumber = request.getAttribute(Field.PAGE_NUMBER.format()) == null ? 1 : isNullInt(request.getAttribute(Field.PAGE_NUMBER.format()));
		//rowsPerPage = (request.getParameter("rowsPerPage") == null) 
		//		   ? getDefaultListRows() : Integer.parseInt(request.getParameter("rowsPerPage"));
		pageNumber = (request.getParameter("pageNumber") == null) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		order = (request.getParameter("order") == null) ? "" : request.getParameter("order");
		
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		
		HttpSession session = request.getSession();
		UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		
		if( user!=null) 
		   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
		usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
		
		
		String firedButton = (String)request.getAttribute("tx_button_indietro");
		   String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			//System.out.println(firedButton+"index");
		   if (firedButton!=null|| firedButtonReset!=null){
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
		/*
		String firedButtonNuovo = (String)request.getAttribute("tx_button_nuovo");
		String firedButtonReset = (String)request.getAttribute("tx_button_reset");
		//System.out.println(firedButton+"index");
		if (firedButtonNuovo!=null||firedButtonReset!=null){
				request.setAttribute("carta_codiceCarta", null);
				request.setAttribute("carta_descrizioneCarta", null);
		}
		String firedButtonIndietro = (String)request.getAttribute("tx_button_indietro");
		if (firedButtonIndietro!=null||firedButtonReset!=null){
				try {
					index(request);
				} catch (ActionException e) {
					e.printStackTrace();
				}
		}*/
	}

	@Override
	public Object index(HttpServletRequest request) throws ActionException {
		//saveadd(request);
		search(request);
		
		return null;
	}
	
	public Object search(HttpServletRequest request) throws ActionException {
		try {
			String codiceCarta = ((String)request.getAttribute("carta_codiceCarta")==null ? "":  (String)request.getAttribute("carta_codiceCarta"));
			String descrizioneCarta = ((String)request.getAttribute("carta_descrizioneCarta")==null ? "":  (String)request.getAttribute("carta_descrizioneCarta"));
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			String firedButtonN = (String)request.getAttribute("tx_button_nuovo");
			if (firedButtonReset!=null){				
				resetParametri(request);
				codiceCarta="";
				descrizioneCarta="";
				request.setAttribute("carta_codiceCarta", "");
				request.setAttribute("carta_descrizioneCarta", "");
				
			}
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			
			if (firedButton!=null||firedButtonN!=null){
					codiceCarta = request.getParameter("");
					descrizioneCarta = request.getParameter("");
			}
			
			CartaPagamentoSearchResponse searchResponse = getCartaPagamentoSearchResponse(codiceCarta,descrizioneCarta, rowsPerPage, pageNumber, order, request);
			CartaPagamentoResponse cartaPagamentoResponse = searchResponse.getResponse();
			CartaPagamentoResponsePageInfo responsePageInfo = cartaPagamentoResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			
			request.setAttribute("carta_codiceCarta", codiceCarta);
			request.setAttribute("carta_descrizioneCarta", descrizioneCarta);
			
			request.setAttribute("cartaPagamentos", cartaPagamentoResponse.getListXml());
			request.setAttribute("cartaPagamentos.pageInfo", pageInfo);
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
		  request.setAttribute("codiceCarta",request.getParameter("codiceCarta"));		  
		return null; 
	}
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			
			CartaPagamentoDetailResponse response = getCartaPagamentoDetailSearchResponse(request.getParameter("carta_codiceCarta"), request);
			
			//request.setAttribute("company", response.getCompany());
			request.setAttribute("carta_codiceCarta", response.getCartapagamento().getCodiceCartaPagamento());
			request.setAttribute("carta_descrizioneCarta", response.getCartapagamento().getDescrizioneCartaPagamento());
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
				if(firedButton.equals("Indietro") /* request.getAttribute("carta_codiceCarta").equals("")*/ ){
					if(request.getAttribute("carta_codiceCarta") !=null){
						request.setAttribute("carta_codiceCarta", null);
						request.setAttribute("carta_descrizioneCarta", null);
						index(request);
					}
				}
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
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					if(request.getAttribute("carta_codiceCarta") !=null){
						request.setAttribute("carta_codiceCarta", null);
						request.setAttribute("carta_descrizioneCarta", null);
						index(request);
					}
				}
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
		String codiceCarta = request.getParameter("carta_codiceCarta");
	    String descrizioneCarta = request.getParameter("carta_descrizioneCarta");
	    //CartaPagamentoImplementationStub stub = null;
		try {
					
			CartaPagamento cartaPagamento= new CartaPagamento(codiceCarta, descrizioneCarta);
			/* we prepare object for save */
			CartaPagamentoSaveRequest saveRequest = new CartaPagamentoSaveRequest(cartaPagamento,codOp);
			/* we save object */
			saveCartaPagamento(saveRequest, request);

		} catch (Exception e) {
			try {
				//CartaPagamentoDetailRequest detailRequest = new CartaPagamentoDetailRequest(codiceCarta);
			    CartaPagamentoDetailResponse detailResponse = getCartaPagamento(codiceCarta, request);
				//request.setAttribute("company", detailResponse.getCompany());
				request.setAttribute("carta_codiceCarta", detailResponse.getCartapagamento().getCodiceCartaPagamento());
				request.setAttribute("carta_descrizioneCarta", detailResponse.getCartapagamento().getCodiceCartaPagamento());
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.GENERIC_ERR.format());
			request.setAttribute("error", "error"); 
			System.out.println(e.getMessage());
		}
	}
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String codiceCarta = request.getParameter("carta_codiceCarta");
		try {
			request.setAttribute("varname", "cartapagamento");
			/* we prepare object for cancel */
			CartaPagamentoCancelRequest cancelRequest = new CartaPagamentoCancelRequest(codiceCarta);
			/* we cancel object */			
			cancelCartaPagamento(cancelRequest, request);
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

private CartaPagamentoSearchResponse getCartaPagamentoSearchResponse(
		String codiceCarta, String descrizioneCarta, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
{
	CartaPagamentoSearchResponse res = null;
	CartaPagamentoSearchRequest in = new CartaPagamentoSearchRequest();
	in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
	in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
	in.setOrder(order == null ? "" : order);
	in.setCodiceCartaPagamento(codiceCarta  == null ? "" : codiceCarta);
	in.setDescrizioneCartaPagamento(descrizioneCarta  == null ? "" : descrizioneCarta);
	
	res = WSCache.cartaPagamentoServer.getCartaPagamentos(in, request);
	return res;
}
private void  saveCartaPagamento(CartaPagamentoSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
{
	
	WSCache.cartaPagamentoServer.save(saveRequest, request);
	
}

private void  cancelCartaPagamento(CartaPagamentoCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
{
	
	WSCache.cartaPagamentoServer.cancel(cancelRequest, request);
	
}

private CartaPagamentoDetailResponse getCartaPagamentoDetailSearchResponse(String codiceCarta, HttpServletRequest request) throws FaultType, RemoteException
{
	 
	CartaPagamentoDetailResponse res = null;
	CartaPagamentoDetailRequest in = new CartaPagamentoDetailRequest(codiceCarta);
	in.setCodiceCartaPagamento(codiceCarta  == null ? "" : codiceCarta);
	
	res = WSCache.cartaPagamentoServer.getCartaPagamento(in, request);
	return res;
}
/*
private void cancelCompanyResponse (String companyCode) throws FaultType, RemoteException
{
	 
	
	CompanyCancelRequest in = new CompanyCancelRequest(companyCode);
	in.setCompanyCode(companyCode == null ? "" : companyCode);
	WSCache.companyServer.cancel(in);
	
}
*/

private CartaPagamentoDetailResponse getCartaPagamento(String codiceCarta, HttpServletRequest request) throws com.seda.payer.pgec.webservice.srv.FaultType, RemoteException
{
	CartaPagamentoDetailRequest detailRequest = new CartaPagamentoDetailRequest(codiceCarta);
	detailRequest.setCodiceCartaPagamento(codiceCarta == null ? "" : codiceCarta);
	return WSCache.cartaPagamentoServer.getCartaPagamento(detailRequest, request);
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
