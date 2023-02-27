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
import com.seda.payer.pgec.webservice.impostaservizio.dati.ImpostaServizio;
import com.seda.payer.pgec.webservice.impostaservizio.dati.ImpostaServizioCancelRequest;
import com.seda.payer.pgec.webservice.impostaservizio.dati.ImpostaServizioDetailRequest;
import com.seda.payer.pgec.webservice.impostaservizio.dati.ImpostaServizioDetailResponse;
import com.seda.payer.pgec.webservice.impostaservizio.dati.ImpostaServizioResponse;
import com.seda.payer.pgec.webservice.impostaservizio.dati.ImpostaServizioResponsePageInfo;
import com.seda.payer.pgec.webservice.impostaservizio.dati.ImpostaServizioSaveRequest;
import com.seda.payer.pgec.webservice.impostaservizio.dati.ImpostaServizioSearchRequest;
import com.seda.payer.pgec.webservice.impostaservizio.dati.ImpostaServizioSearchRequest2;
import com.seda.payer.pgec.webservice.impostaservizio.dati.ImpostaServizioSearchResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;


public class ImpostaServizioAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	@SuppressWarnings("unused")
	private String strCodiceSocieta;
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
		   
		if(user!=null) {
		   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
		   request.setAttribute("userAppl_codiceSocieta",user.getCodiceSocieta().trim());
		   //request.setAttribute("userAppl_codiceUtente",userAppl.getCodiceUtente().trim());
		   //request.setAttribute("userAppl_chiaveEnteCon",userAppl.getChiaveEnteCon().trim());
		   //request.setAttribute("userAppl_chiaveEnteEnt",userAppl.getChiaveEnteEnt().trim());
		}
		usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
		strCodiceSocieta = (String)request.getAttribute("userAppl_codiceSocieta");
		String firedButtonReset = (String)request.getAttribute("tx_button_reset");
		String firedButton = (String)request.getAttribute("tx_button_nuovo");
		//System.out.println(firedButton+"index");
		if (firedButton!=null || firedButtonReset !=null){			
			try {
				add(request);
			} catch (ActionException e) {
				e.printStackTrace();
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
			String companyCode = ((String)request.getAttribute("impostaservizio_companyCode")==null ? "":  (String)request.getAttribute("impostaservizio_companyCode"));
			String codiceImpostaServizio = ((String)request.getAttribute("impostaservizio_codiceImpostaServizio")==null ? "":  (String)request.getAttribute("impostaservizio_codiceImpostaServizio"));
			String codiceTipologiaServizio = ((String)request.getAttribute("impostaservizio_codiceTipologiaServizio")==null ? "":  (String)request.getAttribute("impostaservizio_codiceTipologiaServizio"));//request.getParameter("impostaservizio_codiceTipologiaServizio");
			String descrizioneImpostaServizio = ((String)request.getAttribute("impostaservizio_descrizioneImpostaServizio")==null ? "":  (String)request.getAttribute("impostaservizio_descrizioneImpostaServizio"));//request.getParameter("impostaservizio_descrizioneImpostaServizio");
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				companyCode="";
				codiceTipologiaServizio="";
				descrizioneImpostaServizio="";
				codiceImpostaServizio="";
				
			}
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					codiceTipologiaServizio = request.getParameter("");
					descrizioneImpostaServizio = request.getParameter("");
					companyCode = request.getParameter("");
					codiceImpostaServizio = request.getParameter("");
				}
			}
			ImpostaServizioSearchResponse searchResponse = null;
			if (firedButton!=null)
				 if(firedButton.equals("Indietro"))
					 searchResponse = getImpostaServizioSearchResponse(null,null, null, null,null,null, rowsPerPage, pageNumber, order, request);
				 if (firedButton==null)  searchResponse = getImpostaServizioSearchResponse("","", codiceImpostaServizio, descrizioneImpostaServizio, companyCode, codiceTipologiaServizio, rowsPerPage, pageNumber, order, request);
			
			if(searchResponse != null){
				ImpostaServizioResponse impostaservizioServizioResponse = searchResponse.getResponse();
				ImpostaServizioResponsePageInfo responsePageInfo = impostaservizioServizioResponse.getPageInfo();
				PageInfo pageInfo = new PageInfo(); {
					pageInfo.setFirstRow(responsePageInfo.getFirstRow());
					pageInfo.setLastRow(responsePageInfo.getLastRow());
					pageInfo.setNumPages(responsePageInfo.getNumPages());
					pageInfo.setNumRows(responsePageInfo.getNumRows());
					pageInfo.setPageNumber(responsePageInfo.getPageNumber());
					pageInfo.setRowsPerPage(rowsPerPage);
				}
				request.setAttribute("impostaservizios", impostaservizioServizioResponse.getListXml());
				request.setAttribute("impostaservizios.pageInfo", pageInfo);
			}
			
			//request.setAttribute("impostaservizio", searchRequest);
			request.setAttribute("impostaservizio_companyCode", companyCode);
			request.setAttribute("impostaservizio_codiceImpostaServizio", codiceImpostaServizio);
			request.setAttribute("impostaservizio_descrizioneImpostaServizio", descrizioneImpostaServizio);
			request.setAttribute("impostaservizio_codiceTipologiaServizio", codiceTipologiaServizio);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			//String codiceSocieta = (strCodiceSocieta!=null && strCodiceSocieta.length()>0)?strCodiceSocieta:"";
			
			ImpostaServizioSearchResponse impostaServizioSearchResponse = getImpostaServizioSearchResponse2((new ImpostaServizioSearchRequest2("")), request);
			if(impostaServizioSearchResponse != null){
				request.setAttribute("tipologiaservizios", impostaServizioSearchResponse.getResponse().getListXml());
			}
			/* we retry all tipologiaservizio */
			//String companyCode =(strCodiceSocieta!=null && strCodiceSocieta.length()>0)?strCodiceSocieta:"";
			//TipologiaServizioImplementationStub stubTipologiaServizio = new TipologiaServizioImplementationStub(new URL(getTipologiaservizioManagerEndpointUrl()), null);
			//TipologiaServizioSearchResponse tipologiaServizioSearchResponse = getTipologiaServizioSearchResponse(companyCode,"", "", 0, 0, "");
			//request.setAttribute("tipologiaservizios", tipologiaServizioSearchResponse.getResponse().getListXml());

		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {
		  request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		  request.setAttribute("companyCode", request.getParameter("companyCode"));
		  request.setAttribute("codiceTipologiaServizio", request.getParameter("codiceTipologiaServizio"));
		  request.setAttribute("codiceImpostaServizio", request.getParameter("codiceImpostaServizio"));
		return null; 
	}	
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			ImpostaServizioSearchResponse impostaServizioSearchResponse = getImpostaServizioSearchResponse2((new ImpostaServizioSearchRequest2("")), request);
			request.setAttribute("tipologiaservizios", impostaServizioSearchResponse.getResponse().getListXml());
			
			//ImpostaServizioImplementationStub stub = new ImpostaServizioImplementationStub(new URL(getImpostaservizioManagerEndpointUrl()), null);
			ImpostaServizioDetailResponse response = 
				getImpostaServizioDetailSearchResponse(request.getParameter("impostaservizio_codiceImpostaServizio"),request.getParameter("impostaservizio_codiceTipologiaServizio"), request.getParameter("impostaservizio_companyCode"), request);
			//request.setAttribute("impostaservizio", response.getImpostaservizio());
			request.setAttribute("impostaservizio_companyCode", response.getImpostaservizio().getCompanyCode());
			request.setAttribute("impostaservizio_codiceImpostaServizio", response.getImpostaservizio().getCodiceImpostaServizio());
			request.setAttribute("impostaservizio_descrizioneImpostaServizio", response.getImpostaservizio().getDescrizioneImpostaServizio());
			request.setAttribute("impostaservizio_codiceTipologiaServizio", response.getImpostaservizio().getCodiceTipologiaServizio());
			request.setAttribute("chk_flagPagoPA", response.getImpostaservizio().getFlagPagoPA().equals("Y"));	//TODO da verificare
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
					if(request.getAttribute("impostaservizio_companyCode") !=null){
						request.setAttribute("impostaservizio_companyCode", null);
						request.setAttribute("impostaservizio_codiceImpostaServizio", null);
						request.setAttribute("impostaservizio_descrizioneImpostaServizio", null);
						request.setAttribute("impostaservizio_codiceTipologiaServizio", null);
						index(request);
					}
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
					if(request.getAttribute("impostaservizio_companyCode") !=null){
						request.setAttribute("impostaservizio_companyCode", null);
						request.setAttribute("impostaservizio_codiceImpostaServizio", null);
						request.setAttribute("impostaservizio_descrizioneImpostaServizio", null);
						request.setAttribute("impostaservizio_codiceTipologiaServizio", null);
						index(request);
					}
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
		String companyCode="";
		String codiceTipologiaServizio="";
		String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		String codiceImpostaServizio = request.getParameter("impostaservizio_codiceImpostaServizio");
		String arrStr=request.getParameter("impostaservizio_strTipologiaServizios");
		
		//String firedButton = (String)request.getAttribute("tx_button_nuovo");
			//if(firedButton.equals("Nuovo")){
		
		
		//System.out.println(codOp);
		
		String cod = request.getParameter("prova");
		
		//System.out.println("prova " + prova);
		//if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())== 0 && arrStr!=null && arrStr.length()>0)
		if (cod.equals("1") && arrStr!=null && arrStr.length()>0)
		{
		      String[] strTipologiaServizios = arrStr.split("\\|");
              companyCode = strTipologiaServizios[0];
              System.out.println(companyCode);
 		      codiceTipologiaServizio= strTipologiaServizios[1];
 		      System.out.println(codiceTipologiaServizio);
 		}
			
		
		//if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0)
		if(cod.equals("0"))
		{
              companyCode = request.getParameter("impostaservizio_companyCode");
		      codiceTipologiaServizio= request.getParameter("impostaservizio_codiceTipologiaServizio");
		}
		String descrizioneImpostaServizio = request.getParameter("impostaservizio_descrizioneImpostaServizio");
		
		//Per Trentino devo gestire flagPagoPA
		String flagPagoPA = (request.getAttribute("flagPagoPA") == null ? "" : "Y");
			
		//ImpostaServizioImplementationStub stub = null;
		try {
			//stub = new ImpostaServizioImplementationStub(new URL(getImpostaservizioManagerEndpointUrl()), null);
			ImpostaServizio impostaServizio = new ImpostaServizio(companyCode,codiceTipologiaServizio,codiceImpostaServizio,descrizioneImpostaServizio,usernameAutenticazione,flagPagoPA);
			/* we prepare object for save */
			ImpostaServizioSaveRequest saveRequest = new ImpostaServizioSaveRequest(impostaServizio,codOp);
			/* we save object */
			saveImpostaServizio(saveRequest, request);
			
		} catch (Exception e) {
			try {
				ImpostaServizioDetailRequest detailRequest = new ImpostaServizioDetailRequest(codiceImpostaServizio,codiceTipologiaServizio, companyCode);
				ImpostaServizioDetailResponse detailResponse = getImpostaServizio(detailRequest, request);
				//request.setAttribute("impostaservizio", detailResponse.getImpostaservizio());
				request.setAttribute("impostaservizio_companyCode", detailResponse.getImpostaservizio().getCompanyCode());
				request.setAttribute("impostaservizio_codiceImpostaServizio", detailResponse.getImpostaservizio().getCodiceImpostaServizio());
				request.setAttribute("impostaservizio_descrizioneImpostaServizio", detailResponse.getImpostaservizio().getDescrizioneImpostaServizio());
				request.setAttribute("impostaservizio_codiceTipologiaServizio", detailResponse.getImpostaservizio().getCodiceTipologiaServizio());
				request.setAttribute("chk_flagPagoPA", detailResponse.getImpostaservizio().getFlagPagoPA().equals("Y"));	//TODO da verificare
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERR.format());
			request.setAttribute("error", "error"); 
			System.out.println(e.getMessage());
		}
	}
	
	
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String codiceImpostaServizio = request.getParameter("impostaservizio_codiceImpostaServizio");
		String codiceTipologiaServizio = request.getParameter("impostaservizio_codiceTipologiaServizio");
		String companyCode = request.getParameter("impostaservizio_companyCode");
		try {
			request.setAttribute("varname", "impostaservizio");
			//stub = new ImpostaServizioImplementationStub(new URL(getImpostaservizioManagerEndpointUrl()), null);
			/* we prepare object for cancel */
			ImpostaServizioCancelRequest cancelRequest = new ImpostaServizioCancelRequest(codiceImpostaServizio,codiceTipologiaServizio,companyCode);
			/* we cancel object */
			cancelImpostaServizio(cancelRequest, request);
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
	
	/*private TipologiaServizioSearchResponse getTipologiaServizioSearchResponse(
			String companyCode,String codiceTipologiaServizio,String descrizioneTipologiaServizio, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		TipologiaServizioSearchResponse res = null;
		TipologiaServizioSearchRequest in = new TipologiaServizioSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setCodiceTipologiaServizio(codiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		in.setDescrizioneTipologiaServizio(descrizioneTipologiaServizio == null ? "" :descrizioneTipologiaServizio);
		
		res = WSCache.tipologiaServizioServer.getTipologiaServizios(in, request);
		return res;
	}*/
	private ImpostaServizioDetailResponse getImpostaServizioDetailSearchResponse(String impostaServizioCode, String tipologiaServizioCode,String companyCode, HttpServletRequest request ) throws FaultType, RemoteException
	{
		 
		ImpostaServizioDetailResponse res = null;
		ImpostaServizioDetailRequest in = new ImpostaServizioDetailRequest(impostaServizioCode,tipologiaServizioCode,companyCode);
		in.setCodiceImpostaServizio(impostaServizioCode == null ? "" : impostaServizioCode);
		in.setCodiceTipologiaServizio(tipologiaServizioCode == null ? "" : tipologiaServizioCode);
		in.setCompanyCode(companyCode == null ? "" :companyCode);
		
		res = WSCache.impostaServizioServer.getImpostaServizio(in, request);
		return res;
	}
	private ImpostaServizioSearchResponse getImpostaServizioSearchResponse(
			String companyCode, String codiceTipologiaServizio, String codiceImpostaServizio, String descrizioneImpostaServizio, String strSoc, String strTpServ, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		ImpostaServizioSearchResponse res = null;
		ImpostaServizioSearchRequest in = new ImpostaServizioSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setCodiceImpostaServizio(codiceImpostaServizio== null ? "" :codiceImpostaServizio);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setCodiceTipologiaServizio(codiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		in.setDescrizioneImpostaServizio(descrizioneImpostaServizio == null ? "" : descrizioneImpostaServizio);
		in.setStrDescrSocieta(strSoc == null ? "" :strSoc);
		in.setStrDescrTipologiaServizio(strTpServ == null ? "" : strTpServ);
		
		res = WSCache.impostaServizioServer.getImpostaServizios(in, request);
		return res;
	}
	
	private void  saveImpostaServizio(ImpostaServizioSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.impostaServizioServer.save(saveRequest, request);
		
	}
	
	private void  cancelImpostaServizio(ImpostaServizioCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.impostaServizioServer.cancel(cancelRequest, request);
		
	}
	private ImpostaServizioDetailResponse getImpostaServizio (ImpostaServizioDetailRequest detailRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		return WSCache.impostaServizioServer.getImpostaServizio(detailRequest, request);
	}
	@SuppressWarnings("unchecked")
	protected void resetParametri(HttpServletRequest request) {
		Enumeration e = request.getParameterNames();
		String p = "";
		while (e.hasMoreElements()) {
			
			p = (String) e.nextElement();
			request.setAttribute(p, "");
		}
		return;
	}
	private ImpostaServizioSearchResponse getImpostaServizioSearchResponse2(
			ImpostaServizioSearchRequest2 impostaServizio, HttpServletRequest request) throws FaultType, RemoteException
	{
		ImpostaServizioSearchResponse res = null;
		ImpostaServizioSearchRequest2 in = new ImpostaServizioSearchRequest2();
		in.setCompanyCode(impostaServizio.getCompanyCode() == null ? "" : impostaServizio.getCompanyCode());
		//in.setCodiceTipologiaServizio(configUtenteTipoServizio.g.getcodiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		
		res = WSCache.impostaServizioServer.getImpostaServizios2(in, request);
		return res;
	}

}