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
import com.seda.payer.pgec.webservice.bollettino.dati.Bollettino;
import com.seda.payer.pgec.webservice.bollettino.dati.BollettinoCancelRequest;
import com.seda.payer.pgec.webservice.bollettino.dati.BollettinoDetailRequest;
import com.seda.payer.pgec.webservice.bollettino.dati.BollettinoDetailResponse;
import com.seda.payer.pgec.webservice.bollettino.dati.BollettinoResponse;
import com.seda.payer.pgec.webservice.bollettino.dati.BollettinoResponsePageInfo;
import com.seda.payer.pgec.webservice.bollettino.dati.BollettinoSaveRequest;
import com.seda.payer.pgec.webservice.bollettino.dati.BollettinoSearchRequest;
import com.seda.payer.pgec.webservice.bollettino.dati.BollettinoSearchResponse;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;





public class BollettinoAction extends DispatchHtmlAction {

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
		if (firedButton!=null){
			if(firedButton.equals("Indietro")){
				request.setAttribute("bollettino_bollettinoType", "");
				request.setAttribute("bollettino_bollettinoDescription", "");
				try {
					index(request);
				} catch (ActionException e) {
					e.printStackTrace();
				}
			}
		}	
		String firedButtonNew = (String)request.getAttribute("tx_button_nuovo");
		//System.out.println(firedButton+"index");
		if (firedButtonNew!=null){
				request.setAttribute("bollettino_bollettinoType", "");
				request.setAttribute("bollettino_bollettinoDescription", "");
		}
	}

	@Override
	public Object index(HttpServletRequest request) throws ActionException {
		search(request);
		
		return null;
	}
	
	public Object search(HttpServletRequest request) throws ActionException {
		try {
			String bollettinoType = ((String)request.getAttribute("bollettino_bollettinoType")==null ? "":  (String)request.getAttribute("bollettino_bollettinoType"));
			String bollettinoDescription = ((String)request.getAttribute("bollettino_bollettinoDescription")==null ? "":  (String)request.getAttribute("bollettino_bollettinoDescription"));
			
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonN = (String)request.getAttribute("tx_button_nuovo");
			if (firedButton!=null||firedButtonN!=null){
					bollettinoType = request.getParameter("");
					bollettinoDescription = request.getParameter("");
			}
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				bollettinoType="";
				bollettinoDescription="";
				
			}
			BollettinoSearchResponse searchResponse = null;
			if (firedButton!=null)
				 if(firedButton.equals("Indietro"))
					 searchResponse = getBollettinoSearchResponse(null,null, rowsPerPage, pageNumber, order, request);
			if (firedButton==null)  searchResponse = getBollettinoSearchResponse(bollettinoType,bollettinoDescription, rowsPerPage, pageNumber, order, request);
			
			//BollettinoSearchResponse searchResponse = getBollettinoSearchResponse(bollettinoType,bollettinoDescription, rowsPerPage, pageNumber, order);
			BollettinoResponse bollettinoResponse = searchResponse.getResponse();
			BollettinoResponsePageInfo responsePageInfo = bollettinoResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			
			request.setAttribute("bollettino_bollettinoType", bollettinoType);
			request.setAttribute("bollettino_bollettinoDescription", bollettinoDescription);
			
			request.setAttribute("bollettini", bollettinoResponse.getListXml());
			request.setAttribute("bollettini.pageInfo", pageInfo);
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
		  request.setAttribute("bollettinoType",request.getParameter("bollettinoType"));		  
		return null; 
	}
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			
			BollettinoDetailResponse response = getBollettinoDetailSearchResponse(request.getParameter("bollettino_bollettinoType"), request);
		
			request.setAttribute("bollettino_bollettinoType", response.getBollettino().getTipoBollettino());
			request.setAttribute("bollettino_bollettinoDescription", response.getBollettino().getDesTipoBollettino());
			request.setAttribute("bollettino_bollettinoTypeComp", response.getBollettino().getModCompBollettino());
			request.setAttribute("bollettino_bollettinoDescriptionTypeComp", response.getBollettino().getDesCompBollettino());
			request.setAttribute("bollettino_bollettinoTypeFlow", response.getBollettino().getTipoFlusso());
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
					if(request.getAttribute("bollettino_bollettinoType") !=null){
						request.setAttribute("bollettino_bollettinoType", null);
						request.setAttribute("bollettino_bollettinoDescription", null);
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
			//System.out.println(firedButton+" saveedit");
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					if(request.getAttribute("bollettino_bollettinoType") !=null){
						request.setAttribute("bollettino_bollettinoType", null);
						request.setAttribute("bollettino_bollettinoDescription", null);
						index(request);
					}
				}
			}else if(firedButtonReset!=null){
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
		String bollettinoType = request.getParameter("bollettino_bollettinoType");
	    String bollettinoDescription = request.getParameter("bollettino_bollettinoDescription");
	    String bollettinoTypeComp = request.getParameter("bollettino_bollettinoTypeComp");
	    String bollettinoDescriptionTypeComp;
	    if(bollettinoTypeComp.equals("A"))
        	bollettinoDescriptionTypeComp = "Modalità Automatica";
	    else
	    	bollettinoDescriptionTypeComp = "Modalità Manuale";
	    String bollettinoTypeFlow = request.getParameter("bollettino_bollettinoTypeFlow");
	    //BollettinoImplementationStub stub = null;
		try {
					
			Bollettino bollettino = new Bollettino(bollettinoType, bollettinoDescription, bollettinoTypeComp, bollettinoDescriptionTypeComp, bollettinoTypeFlow, usernameAutenticazione);
			/* we prepare object for save */
			BollettinoSaveRequest saveRequest = new BollettinoSaveRequest(bollettino,codOp);
			/* we save object */
			saveBollettino(saveRequest, request);

		} catch (Exception e) {
			try {
				//BollettinoDetailRequest detailRequest = new BollettinoDetailRequest(bollettinoType);
			    BollettinoDetailResponse detailResponse = getBollettino(bollettinoType, request);
				request.setAttribute("bollettino_bollettinoType", detailResponse.getBollettino().getTipoBollettino());
				request.setAttribute("bollettino_bollettinoDescription", detailResponse.getBollettino().getDesTipoBollettino());
				request.setAttribute("bollettino_bollettinoTypeComp", detailResponse.getBollettino().getModCompBollettino());
				request.setAttribute("bollettino_bollettinoDescriptionTypeComp", detailResponse.getBollettino().getDesCompBollettino());
				request.setAttribute("bollettino_bollettinoTypeFlow", detailResponse.getBollettino().getTipoFlusso());
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.GENERIC_ERR.format());
			request.setAttribute("error", "error"); 
			System.out.println(e.getMessage());
		}
	}
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String bollettinoType = request.getParameter("bollettino_bollettinoType");
		try {
			request.setAttribute("varname", "bollettino");
			/* we prepare object for cancel */
			BollettinoCancelRequest cancelRequest = new BollettinoCancelRequest(bollettinoType);
			/* we cancel object */			
			cancelBollettino(cancelRequest, request);
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

private BollettinoSearchResponse getBollettinoSearchResponse(
		String typeBollettino, String bollettinoDescription, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
{
	BollettinoSearchResponse res = null;
	BollettinoSearchRequest in = new BollettinoSearchRequest();
	in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
	in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
	in.setOrder(order == null ? "" : order);
	in.setTipoBollettino(typeBollettino == null ? "" : typeBollettino);
	in.setDesTipoBollettino(bollettinoDescription == null ? "" : bollettinoDescription);
	
	res = WSCache.bollettinoServer.getBollettini(in, request);
	return res;
}
private void  saveBollettino(BollettinoSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
{
	
	WSCache.bollettinoServer.save(saveRequest, request);
	
}

private void  cancelBollettino(BollettinoCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
{
	
	WSCache.bollettinoServer.cancel(cancelRequest, request);
	
}

private BollettinoDetailResponse getBollettinoDetailSearchResponse(String bollettinoType, HttpServletRequest request) throws FaultType, RemoteException
{
	 
	BollettinoDetailResponse res = null;
	BollettinoDetailRequest in = new BollettinoDetailRequest(bollettinoType);
	in.setTipoBollettino(bollettinoType == null ? "" : bollettinoType);
	res = WSCache.bollettinoServer.getBollettino(in, request);
	return res;
}
/*
private void cancelBollettinoResponse (String bollettinoType) throws FaultType, RemoteException
{
	 
	
	BollettinoCancelRequest in = new BollettinoCancelRequest(bollettinoType);
	in.setTipoBollettino(bollettinoType == null ? "" : bollettinoType);
	WSCache.bollettinoServer.cancel(in);
	
}
*/
private BollettinoDetailResponse getBollettino(String bollettinoType, HttpServletRequest request) throws com.seda.payer.pgec.webservice.srv.FaultType, RemoteException
{
	BollettinoDetailRequest detailRequest = new BollettinoDetailRequest(bollettinoType);
	detailRequest.setTipoBollettino(bollettinoType == null ? "" : bollettinoType);
	return WSCache.bollettinoServer.getBollettino(detailRequest, request);
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
