package org.seda.payer.manager.configurazione.actions;


import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
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
import com.seda.commons.validator.ValidationMessage;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.components.validation.ValidationContext;
import com.seda.j2ee5.maf.components.validation.ValidationErrorMap;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;


import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.costitransazionebanca.dati.CostiTransazioneBanca;
import com.seda.payer.pgec.webservice.costitransazionebanca.dati.CostiTransazioneBancaCancelRequest;
import com.seda.payer.pgec.webservice.costitransazionebanca.dati.CostiTransazioneBancaDetailRequest;
import com.seda.payer.pgec.webservice.costitransazionebanca.dati.CostiTransazioneBancaDetailResponse;
import com.seda.payer.pgec.webservice.costitransazionebanca.dati.CostiTransazioneBancaResponse;
import com.seda.payer.pgec.webservice.costitransazionebanca.dati.CostiTransazioneBancaResponsePageInfo;
import com.seda.payer.pgec.webservice.costitransazionebanca.dati.CostiTransazioneBancaSaveRequest;
import com.seda.payer.pgec.webservice.costitransazionebanca.dati.CostiTransazioneBancaSearchRequest;
import com.seda.payer.pgec.webservice.costitransazionebanca.dati.CostiTransazioneBancaSearchResponse;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoResponse;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoSearchRequest;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoSearchResponse;





public class CostiTransazioneBancaAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private String usernameAutenticazione = null;
	HttpSession session;
	@Override
	public void start(HttpServletRequest request) {
		rowsPerPage = (request.getParameter("rowsPerPage") == null) 
				   ? getDefaultListRows() : Integer.parseInt(request.getParameter("rowsPerPage"));
		pageNumber = (request.getParameter("pageNumber") == null) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		order = (request.getParameter("order") == null) ? "" : request.getParameter("order");
		
		session = request.getSession();
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
				request.setAttribute("costiTransazioneBanca_ChiaveFasciaCosto", "");
				request.setAttribute("costiTransazioneBanca_ChiaveGatewayPagamento", "");
				request.setAttribute("costiTransazioneBanca_SearchChiaveFasciaCosto", "");
				request.setAttribute("costiTransazioneBanca_SearchChiaveGatewayPagamento", "");
				
				request.setAttribute("costiTransazioneBanca_SearchChiaveFasciaCosto", "");
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
			String costiTransazioneBancaChiaveFasciaCosto = ((String)request.getAttribute("costiTransazioneBanca_ChiaveFasciaCosto")==null ? "":  (String)request.getAttribute("costiTransazioneBanca_ChiaveFasciaCosto"));
			String costiTransazioneBancaChiaveGatewayPagamento = ((String)request.getAttribute("costiTransazioneBanca_ChiaveGatewayPagamento")==null ? "":  (String)request.getAttribute("costiTransazioneBanca_ChiaveGatewayPagamento"));
			
			String costiTransazioneBancaSearchDescrizioneSocieta = ((String)request.getAttribute("costiTransazioneBanca_SearchDescrizioneSocieta")==null ? "":  (String)request.getAttribute("costiTransazioneBanca_SearchDescrizioneSocieta"));
			String costiTransazioneBancaSearchDescrizioneUtente = ((String)request.getAttribute("costiTransazioneBanca_SearchDescrizioneUtente")==null ? "":  (String)request.getAttribute("costiTransazioneBanca_SearchDescrizioneUtente"));
			String costiTransazioneBancaSearchDescrizioneGateway = ((String)request.getAttribute("costiTransazioneBanca_SearchDescrizioneGateway")==null ? "":  (String)request.getAttribute("costiTransazioneBanca_SearchDescrizioneGateway"));
			//String costiTransazioneBancaSearchChiaveGatewayPagamento = ((String)request.getAttribute("costiTransazioneBanca_SearchChiaveGatewayPagamento")==null ? "":  (String)request.getAttribute("costiTransazioneBanca_SearchChiaveGatewayPagamento"));
			String costiTransazioneBancaSearchChiaveFasciaCosto = ((String)request.getAttribute("costiTransazioneBanca_SearchChiaveFasciaCosto")==null ? "":  (String)request.getAttribute("costiTransazioneBanca_SearchChiaveFasciaCosto"));
			
				
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonN = (String)request.getAttribute("tx_button_nuovo");
			if (firedButton!=null||firedButtonN!=null){
					
				request.setAttribute("costiTransazioneBanca_ChiaveFasciaCosto", "");
				request.setAttribute("costiTransazioneBanca_ChiaveGatewayPagamento", "");
				request.setAttribute("costiTransazioneBanca_SearchChiaveFasciaCosto", "");
				request.setAttribute("costiTransazioneBanca_SearchChiaveGatewayPagamento", "");
				request.setAttribute("costiTransazioneBanca_SearchDescrizioneSocieta", "");
				request.setAttribute("costiTransazioneBanca_SearchDescrizioneUtente", "");
				request.setAttribute("costiTransazioneBanca_SearchDescrizioneGateway", "");
				costiTransazioneBancaChiaveFasciaCosto="";
				costiTransazioneBancaChiaveGatewayPagamento="";
				costiTransazioneBancaSearchDescrizioneSocieta="";
				costiTransazioneBancaSearchDescrizioneUtente="";
				costiTransazioneBancaSearchDescrizioneGateway="";
			}
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				costiTransazioneBancaChiaveFasciaCosto="";
				costiTransazioneBancaChiaveGatewayPagamento="";
				costiTransazioneBancaSearchDescrizioneSocieta="";
				costiTransazioneBancaSearchDescrizioneUtente="";
				costiTransazioneBancaSearchDescrizioneGateway="";
			}
			
			CostiTransazioneBancaSearchResponse searchResponse = getCostiTransazioneBancaSearchResponse(costiTransazioneBancaSearchDescrizioneSocieta, costiTransazioneBancaSearchDescrizioneUtente, costiTransazioneBancaSearchDescrizioneGateway, rowsPerPage, pageNumber, order, request);
			CostiTransazioneBancaResponse costiTransazioneBancaResponse = searchResponse.getResponse();
			CostiTransazioneBancaResponsePageInfo responsePageInfo = costiTransazioneBancaResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			
			request.setAttribute("costiTransazioneBanca_ChiaveFasciaCosto", costiTransazioneBancaChiaveFasciaCosto);
			request.setAttribute("costiTransazioneBanca_ChiaveGatewayPagamento", costiTransazioneBancaChiaveGatewayPagamento);
			request.setAttribute("costiTransazioneBanca_SearchDescrizioneSocieta", costiTransazioneBancaSearchDescrizioneSocieta);
			request.setAttribute("costiTransazioneBanca_SearchDescrizioneUtente", costiTransazioneBancaSearchDescrizioneUtente);
			request.setAttribute("costiTransazioneBanca_SearchDescrizioneGateway", costiTransazioneBancaSearchDescrizioneGateway);
			
			request.setAttribute("costitransazionebanca", costiTransazioneBancaResponse.getListXml());
			request.setAttribute("costitransazionebanca.pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public Object add(HttpServletRequest request) throws ActionException {
		try
		{
		/*		
		*
		GatewayPagamentoSearchRequest searchRequest = new GatewayPagamentoSearchRequest("","","","","","","","","","",0, 0, "");
		GatewayPagamentoSearchResponse searchResponse = getGatewayPagamentos(searchRequest);		
		request.setAttribute("gatewaypagamentos", searchResponse.getResponse().getListXml());
		*/
		loadListaGatewayXml_DDL(request, session, "", "", true);
								
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {	  
		request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		  request.setAttribute("costiTransazioneBanca_ChiaveFasciaCosto",request.getParameter("costiTransazioneBanca_ChiaveFasciaCosto"));
		  	
		return null; 
	}
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			
			CostiTransazioneBancaDetailResponse response = getCostiTransazioneBancaDetailSearchResponse(request.getParameter("costiTransazioneBanca_ChiaveFasciaCosto"), request);
		
			request.setAttribute("costiTransazioneBanca_ChiaveFasciaCosto", response.getCostiTransazioneBanca().getChiaveFasciaCosto());
			request.setAttribute("costiTransazioneBanca_ChiaveGatewayPagamento", response.getCostiTransazioneBanca().getChiaveGatewayPagamento());
			
			String importoFissoFascia =  response.getCostiTransazioneBanca().getImportoFissoFascia().toString();
			importoFissoFascia = importoFissoFascia.replace(".",",");
			request.setAttribute("costiTransazioneBanca_ImportoFissoFascia", importoFissoFascia);
			
			String importoMaxFasciaDa =  response.getCostiTransazioneBanca().getImportoMaxFasciaDa().toString();
			importoMaxFasciaDa = importoMaxFasciaDa.replace(".",",");
			request.setAttribute("costiTransazioneBanca_ImportoMaxFasciaDa", importoMaxFasciaDa);
			
			String importoMinFasciaDa = response.getCostiTransazioneBanca().getImportoMinFasciaDa().toString();
			importoMinFasciaDa = importoMinFasciaDa.replace(".",",");
			request.setAttribute("costiTransazioneBanca_ImportoMinFasciaDa", importoMinFasciaDa);
			
			String percentualeFascia = response.getCostiTransazioneBanca().getPercentualeFascia().toString();
			percentualeFascia = percentualeFascia.replace(".",",");
			request.setAttribute("costiTransazioneBanca_PercentualeFascia", percentualeFascia);
			
			request.setAttribute("costiTransazioneBanca_TipoCosto", response.getCostiTransazioneBanca().getTipoCosto());
			
			loadListaGatewayXml_DDL(request, session, "", "", true);
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
					request.setAttribute("costiTransazioneBanca_ChiaveFasciaCosto", "");
					request.setAttribute("costiTransazioneBanca_ChiaveGatewayPagamento", "");
					request.setAttribute("costiTransazioneBanca_SearchChiaveFasciaCosto", "");
					request.setAttribute("costiTransazioneBanca_SearchChiaveGatewayPagamento", "");
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
					request.setAttribute("costiTransazioneBanca_ChiaveFasciaCosto", "");
					request.setAttribute("costiTransazioneBanca_ChiaveGatewayPagamento", "");	
					request.setAttribute("costiTransazioneBanca_SearchChiaveFasciaCosto", "");
					request.setAttribute("costiTransazioneBanca_SearchChiaveGatewayPagamento", "");
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
		
		
		
		String chiaveFasciaCosto = request.getParameter("costiTransazioneBanca_ChiaveFasciaCosto");
		if (chiaveFasciaCosto == null || chiaveFasciaCosto == "")
			chiaveFasciaCosto="DEFAULT ";
	    String chiaveGatewayPagamento = request.getParameter("costiTransazioneBanca_ChiaveGatewayPagamento");
	    String tipoCosto = request.getParameter("costiTransazioneBanca_TipoCosto");
	    
	    char oldChar = ',';
        char newChar = '.';
        
	    BigDecimal importoFissoFascia = new BigDecimal(0);
	    String strImportoFissoFascia = request.getParameter("costiTransazioneBanca_ImportoFissoFascia");
	    strImportoFissoFascia= strImportoFissoFascia.replace(oldChar, newChar);
	    if (strImportoFissoFascia != null && !strImportoFissoFascia.equals(""))
	    	importoFissoFascia = BigDecimal.valueOf(Double.parseDouble(strImportoFissoFascia));
	    
	    BigDecimal importoMaxFasciaDa = new BigDecimal(0);
	    String strImportoMaxFasciaDa = request.getParameter("costiTransazioneBanca_ImportoMaxFasciaDa");
	    strImportoMaxFasciaDa= strImportoMaxFasciaDa.replace(oldChar, newChar);
	    if (strImportoMaxFasciaDa != null && !strImportoMaxFasciaDa.equals(""))
	    	importoMaxFasciaDa = BigDecimal.valueOf(Double.parseDouble(strImportoMaxFasciaDa));
	    
	    BigDecimal importoMinFasciaDa = new BigDecimal(0);
	    String strImportoMinFasciaDa = request.getParameter("costiTransazioneBanca_ImportoMinFasciaDa");
	    strImportoMinFasciaDa= strImportoMinFasciaDa.replace(oldChar, newChar);
	    if (strImportoMinFasciaDa != null && !strImportoMinFasciaDa.equals(""))
	    	importoMinFasciaDa = BigDecimal.valueOf(Double.parseDouble(strImportoMinFasciaDa));
	    
	    BigDecimal percentualeFascia = new BigDecimal(0);
	    String strPercentualeFascia = request.getParameter("costiTransazioneBanca_PercentualeFascia");
	    strPercentualeFascia= strPercentualeFascia.replace(oldChar, newChar);
	    if (strPercentualeFascia != null && !strPercentualeFascia.equals(""))
	    	percentualeFascia = BigDecimal.valueOf(Double.parseDouble(strPercentualeFascia));
	    
    // codice giuseppe
		if(importoMinFasciaDa.compareTo(importoMaxFasciaDa)>0){
			String esito="";
			esito = Messages.MIN_MAGGIORE_MAX.format();
			setFormMessage("frmAction", esito, request);
			request.setAttribute("done",null);	
			loadListaGatewayXml_DDL(request, session, "", "", true);
		}
    // fine codice giuseppe
		else
		{
			try {
				
				CostiTransazioneBanca costiTransazioneBanca = new CostiTransazioneBanca(chiaveFasciaCosto, chiaveGatewayPagamento, tipoCosto, importoMinFasciaDa, importoMaxFasciaDa, importoFissoFascia, percentualeFascia, usernameAutenticazione); // usernameAutenticazione);
				/* we prepare object for save */
				CostiTransazioneBancaSaveRequest saveRequest = new CostiTransazioneBancaSaveRequest(costiTransazioneBanca,codOp);
				/* we save object */
				saveCostiTransazioneBanca(saveRequest, request);

			} catch (Exception e) {
				try {
					CostiTransazioneBancaDetailRequest detailRequest = new CostiTransazioneBancaDetailRequest(chiaveFasciaCosto);
				    CostiTransazioneBancaDetailResponse detailResponse = getCostiTransazioneBanca(detailRequest, request);
					
				    request.setAttribute("costiTransazioneBanca_ChiaveFasciaCosto", detailResponse.getCostiTransazioneBanca().getChiaveFasciaCosto());
					request.setAttribute("costiTransazioneBanca_ChiaveGatewayPagamento", detailResponse.getCostiTransazioneBanca().getChiaveGatewayPagamento());
					request.setAttribute("costiTransazioneBanca_ImportoFissoFascia", detailResponse.getCostiTransazioneBanca().getImportoFissoFascia().toString());
					request.setAttribute("costiTransazioneBanca_ImportoMaxFasciaDa", detailResponse.getCostiTransazioneBanca().getImportoMaxFasciaDa().toString());
					request.setAttribute("costiTransazioneBanca_ImportoMinFasciaDa", detailResponse.getCostiTransazioneBanca().getImportoMinFasciaDa().toString());
					request.setAttribute("costiTransazioneBanca_PercentualeFascia", detailResponse.getCostiTransazioneBanca().getPercentualeFascia().toString());
					request.setAttribute("costiTransazioneBanca_TipoCosto", detailResponse.getCostiTransazioneBanca().getTipoCosto());
					
				} catch (Exception ignore) { }
				if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERR.format());
				if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.UPDT_ERR.format());
				request.setAttribute("error", "error"); 
				//System.out.println(e.getMessage());
			}
		}
	    
	}
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String costiTransazioneBancaChiaveFasciaCosto = request.getParameter("costiTransazioneBanca_ChiaveFasciaCosto");
	    
	    
		try {
			request.setAttribute("varname", "costitransazionebanca");
			/* we prepare object for cancel */
			CostiTransazioneBancaCancelRequest cancelRequest = new CostiTransazioneBancaCancelRequest(costiTransazioneBancaChiaveFasciaCosto);
			/* we cancel object */			
			cancelCostiTransazioneBanca(cancelRequest, request);
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

private CostiTransazioneBancaSearchResponse getCostiTransazioneBancaSearchResponse(
		String descrizioneSocieta, String descrizioneUtente, String descrizioneGateway, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
{
	CostiTransazioneBancaSearchResponse res = null;
	CostiTransazioneBancaSearchRequest in = new CostiTransazioneBancaSearchRequest();
	in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
	in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
	in.setOrder(order == null ? "" : order);
	
	in.setDescrizioneSocieta(descrizioneSocieta == null ? "" : descrizioneSocieta);
	in.setDescrizioneUtente(descrizioneUtente == null ? "" : descrizioneUtente);
	in.setDescrizioneGateway(descrizioneGateway == null ? "" : descrizioneGateway);
	
	res = WSCache.costiTransazioneBancaServer.getCostiTransazioneBancas(in, request);
	return res;
}

private void  saveCostiTransazioneBanca(CostiTransazioneBancaSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
{
	
	WSCache.costiTransazioneBancaServer.save(saveRequest, request);
	
}

private void  cancelCostiTransazioneBanca(CostiTransazioneBancaCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
{
	
	WSCache.costiTransazioneBancaServer.cancel(cancelRequest, request);
	
}

private CostiTransazioneBancaDetailResponse getCostiTransazioneBancaDetailSearchResponse(String chiaveFasciaCosto, HttpServletRequest request) throws FaultType, RemoteException
{
	 
	CostiTransazioneBancaDetailResponse res = null;
	CostiTransazioneBancaDetailRequest in = new CostiTransazioneBancaDetailRequest();
	in.setChiaveFasciaCosto(chiaveFasciaCosto == null ? "" : chiaveFasciaCosto);	
	res = WSCache.costiTransazioneBancaServer.getCostiTransazioneBanca(in, request);
	return res;
}

/*private void cancelCostiTransazioneBancaResponse (String costiTransazioneBancaCompanyCode, String costiTransazioneBancaUserCode) throws FaultType, RemoteException
{
	 
	
	CostiTransazioneBancaCancelRequest in = new CostiTransazioneBancaCancelRequest(costiTransazioneBancaCompanyCode, costiTransazioneBancaUserCode);
	in.setCompanyCode(costiTransazioneBancaCompanyCode == null ? "" : costiTransazioneBancaCompanyCode);
	in.setUserCode(costiTransazioneBancaUserCode == null ? "" : costiTransazioneBancaUserCode);
	WSCache.costiTransazioneBancaServer.cancel(in);
	
}*/

private CostiTransazioneBancaDetailResponse getCostiTransazioneBanca (CostiTransazioneBancaDetailRequest detailRequest, HttpServletRequest request) throws FaultType, RemoteException
{
	
	return WSCache.costiTransazioneBancaServer.getCostiTransazioneBanca(detailRequest, request);
}

protected void resetParametri(HttpServletRequest request) {
	Enumeration e = request.getParameterNames();
	String p = "";
	while (e.hasMoreElements()) {
		
		p = (String) e.nextElement();
		request.setAttribute(p, "");
	}
	loadListaGatewayXml_DDL(request, session, "", "", true);
	return;
}
private GatewayPagamentoSearchResponse getGatewayPagamentos (GatewayPagamentoSearchRequest searchRequest, HttpServletRequest request) throws FaultType, RemoteException
{
	
	return WSCache.gatewayPagamentoServer.getGatewayPagamentos(searchRequest, request);
}


// codice giuseppe
public void setFormMessage(String formName, String message, HttpServletRequest request) {
	if (ValidationContext.getInstance().getValidationMessage() != null) 
	{
		ValidationErrorMap vem=new ValidationErrorMap();
		ArrayList<ValidationMessage> messages=new ArrayList<ValidationMessage>();
		ValidationMessage validationMessage=new ValidationMessage("", "", message);
		messages.add(validationMessage);
		
		vem.setForm(formName);
		vem.setMessages(messages);
		
		request.setAttribute(ValidationContext.getInstance().getValidationMessage(), vem);
		
		messages=null;
		validationMessage=null;
		vem=null;
	}
}
// fine codice giuseppe

}
