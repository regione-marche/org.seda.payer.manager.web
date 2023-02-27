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
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSearchRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizio.dati.ConfigUtenteTipoServizioSearchResponse;
import com.seda.payer.pgec.webservice.impostaservizio.dati.ImpostaServizioSearchResponse;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizio;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioCancelRequest;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioDetailRequest;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioDetailResponse;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioResponse;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioResponsePageInfo;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioSaveRequest;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioSearchRequest;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioSearchResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;


public class RangeAbiUtenteTipoServizioAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private String strCodiceSocieta,strCodiceUtente,usernameAutenticazione;

	@Override
	public void start(HttpServletRequest request) {
		rowsPerPage = (request.getParameter("rowsPerPage") == null) 
		   ? getDefaultListRows() : Integer.parseInt(request.getParameter("rowsPerPage"));
		   pageNumber = (request.getParameter("pageNumber") == null) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		   order = (request.getParameter("order") == null) ? "" : request.getParameter("order");

		   HttpSession session = request.getSession();
		   UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		if( user!=null) {
			   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
			   request.setAttribute("userAppl_codiceSocieta",user.getCodiceSocieta().trim());
			   request.setAttribute("userAppl_codiceUtente",user.getCodiceUtente().trim());
			   //request.setAttribute("userAppl_chiaveEnteCon",userAppl.getChiaveEnteCon().trim());
			   //request.setAttribute("userAppl_chiaveEnteEnt",userAppl.getChiaveEnteEnt().trim());
			}
		    usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
			strCodiceSocieta = (String)request.getAttribute("userAppl_codiceSocieta");
			strCodiceUtente = (String)request.getAttribute("userAppl_codiceUtente");
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
			String codiceSocieta = (strCodiceSocieta!=null && strCodiceSocieta.length()>0)?strCodiceSocieta:request.getParameter("rangeabiutentetiposervizio_companyCode");
			String codiceUtente = (strCodiceUtente!=null && strCodiceUtente.length()>0)?strCodiceUtente:request.getParameter("rangeabiutentetiposervizio_codiceUtente");
			String codiceTipologiaServizio = request.getParameter("rangeabiutentetiposervizio_codiceTipologiaServizio");
			String inizioRangeDa = request.getParameter("rangeabiutentetiposervizio_inizioRangeDa");
			String fineRangeA = request.getParameter("rangeabiutentetiposervizio_fineRangeA");
			String inizioRangePer = request.getParameter("rangeabiutentetiposervizio_inizioRangePer");
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				codiceSocieta="";
				codiceUtente="";
				codiceTipologiaServizio="";
				inizioRangeDa="";
				fineRangeA="";
				inizioRangePer="";
				
			}
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
					codiceTipologiaServizio = request.getParameter("");
					codiceSocieta = request.getParameter("");
					codiceUtente = request.getParameter("");
					inizioRangeDa=request.getParameter("");
					fineRangeA=request.getParameter("");
					inizioRangePer= request.getParameter("");
				}
			}
			RangeAbiUtenteTipoServizioSearchResponse searchResponse = null;
			if (firedButton!=null)
				 if(firedButton.equals("Indietro"))
					 searchResponse = getRangeAbiUtenteTipoServizioSearchResponse("",null,null,null,null,null,null,"","","", rowsPerPage, pageNumber, order, request);
				 if (firedButton==null)  searchResponse = getRangeAbiUtenteTipoServizioSearchResponse("",codiceSocieta,codiceUtente,codiceTipologiaServizio,inizioRangeDa,fineRangeA,inizioRangePer,"","","", rowsPerPage, pageNumber, order, request);
			
			//RangeAbiUtenteTipoServizioImplementationStub stub = new RangeAbiUtenteTipoServizioImplementationStub(new URL(getRangeabiutentetiposervizioManagerEndpointUrl()), null);
			//RangeAbiUtenteTipoServizioSearchRequest searchRequest = new RangeAbiUtenteTipoServizioSearchRequest("",codiceSocieta,codiceUtente,codiceTipologiaServizio,inizioRangeDa,fineRangeA,inizioRangePer,"","","", rowsPerPage, pageNumber, order);
			//RangeAbiUtenteTipoServizioSearchResponse searchResponse = getRangeAbiUtenteTipoServizioSearchResponse("",codiceSocieta,codiceUtente,codiceTipologiaServizio,inizioRangeDa,fineRangeA,inizioRangePer,"","","", rowsPerPage, pageNumber, order);
			RangeAbiUtenteTipoServizioResponse rangeabiutentetiposervizioServizioResponse = searchResponse.getResponse();
			RangeAbiUtenteTipoServizioResponsePageInfo responsePageInfo = rangeabiutentetiposervizioServizioResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			//request.setAttribute("rangeabiutentetiposervizio", searchRequest);
		      request.setAttribute("rangeabiutentetiposervizio_chiaveRangeTipoServizio", request.getParameter("rangeabiutentetiposervizio_chiaveRangeTipoServizio"));
			  request.setAttribute("rangeabiutentetiposervizio_companyCode", codiceSocieta);
			  request.setAttribute("rangeabiutentetiposervizio_codiceUtente",codiceUtente);
			  request.setAttribute("rangeabiutentetiposervizio_codiceTipologiaServizio", codiceTipologiaServizio);
			  request.setAttribute("rangeabiutentetiposervizio_inizioRangeDa", inizioRangeDa);
			  request.setAttribute("rangeabiutentetiposervizio_fineRangeA",fineRangeA);
			  request.setAttribute("rangeabiutentetiposervizio_inizioRangePer",inizioRangePer);	
			
			request.setAttribute("rangeabiutentetiposervizios", rangeabiutentetiposervizioServizioResponse.getListXml());
			request.setAttribute("rangeabiutentetiposervizios.pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			/* we retry all configutentetiposervizio */ 
			String codiceSocieta = (strCodiceSocieta!=null && strCodiceSocieta.length()>0)?strCodiceSocieta:"";
			String codiceUtente = (strCodiceUtente!=null && strCodiceUtente.length()>0)?strCodiceUtente:"";
			//ConfigUtenteTipoServizioImplementationStub stubConfigUtenteTipoServizio = new ConfigUtenteTipoServizioImplementationStub(new URL(getConfigutentetiposervizioManagerEndpointUrl()), null);
			ConfigUtenteTipoServizioSearchResponse configUtenteTipoServizioSearchResponse = getConfigUtenteTipoServizioSearchResponse(codiceSocieta,codiceUtente,"", 0, 0, "", request);
			request.setAttribute("configutentetiposervizios", configUtenteTipoServizioSearchResponse.getResponse().getListXml());
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {
		  request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		  request.setAttribute("chiaveRangeTipoServizio",request.getParameter("chiaveRangeTipoServizio"));
		return null; 
	}
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			//RangeAbiUtenteTipoServizioImplementationStub stub = new RangeAbiUtenteTipoServizioImplementationStub(new URL(getRangeabiutentetiposervizioManagerEndpointUrl()), null);
			RangeAbiUtenteTipoServizioDetailResponse response = 
				getRangeAbiUtenteTipoServizioDetailSearchResponse(request.getParameter("rangeabiutentetiposervizio_chiaveRangeTipoServizio"), request);
			//request.setAttribute("rangeabiutentetiposervizio", response.getRangeabiutentetiposervizio());
		      request.setAttribute("rangeabiutentetiposervizio_chiaveRangeTipoServizio", response.getRangeabiutentetiposervizio().getChiaveRangeTipoServizio());
			  request.setAttribute("rangeabiutentetiposervizio_companyCode", response.getRangeabiutentetiposervizio().getCompanyCode());
			  request.setAttribute("rangeabiutentetiposervizio_codiceUtente", response.getRangeabiutentetiposervizio().getCodiceUtente());
			  request.setAttribute("rangeabiutentetiposervizio_codiceTipologiaServizio", response.getRangeabiutentetiposervizio().getCodiceTipologiaServizio());
			  request.setAttribute("rangeabiutentetiposervizio_inizioRangeDa", response.getRangeabiutentetiposervizio().getInizioRangeDa());
			  request.setAttribute("rangeabiutentetiposervizio_fineRangeA", response.getRangeabiutentetiposervizio().getFineRangeA());
			  request.setAttribute("rangeabiutentetiposervizio_inizioRangePer", response.getRangeabiutentetiposervizio().getInizioRangePer());
			  request.setAttribute("rangeabiutentetiposervizio_tipoRange", response.getRangeabiutentetiposervizio().getTipoRange());
			  request.setAttribute("rangeabiutentetiposervizio_flagCin", response.getRangeabiutentetiposervizio().getFlagCin());
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
						request.setAttribute("rangeabiutentetiposervizio_codiceTipologiaServizio", null);
						request.setAttribute("rangeabiutentetiposervizio_companyCode", null);
						request.setAttribute("rangeabiutentetiposervizio_codiceUtente", null);
						request.setAttribute("rangeabiutentetiposervizio_inizioRangeDa", null);
						request.setAttribute("rangeabiutentetiposervizio_fineRangeA", null);
						request.setAttribute("rangeabiutentetiposervizio_inizioRangePer", null);
						index(request);
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
			request.setAttribute("message", Messages.INS_OK.format());
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButton!=null){
				if(firedButton.equals("Indietro")){
						request.setAttribute("rangeabiutentetiposervizio_codiceTipologiaServizio", null);
						request.setAttribute("rangeabiutentetiposervizio_companyCode", null);
						request.setAttribute("rangeabiutentetiposervizio_codiceUtente", null);
						request.setAttribute("rangeabiutentetiposervizio_inizioRangeDa", null);
						request.setAttribute("rangeabiutentetiposervizio_fineRangeA", null);
						request.setAttribute("rangeabiutentetiposervizio_inizioRangePer", null);
						index(request);
				}
			}
			else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset"))					
					edit(request);
			}else save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}
	
	private void save(HttpServletRequest request) throws ActionException {
		String companyCode="";
		String codiceUtente = "";
		String codiceTipologiaServizio = "";
		String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		String cod = request.getParameter("prova");
		String arrStr = request.getParameter("rangeabiutentetiposervizio_strConfigutentetiposervizios");
		if (cod.equals("0") && arrStr!=null && arrStr.length()>0)
		{
			  String[] strConfigutentetiposervizios = arrStr.split("\\|");
              companyCode = strConfigutentetiposervizios[0];
 		      codiceUtente= strConfigutentetiposervizios[1];
 		      codiceTipologiaServizio= strConfigutentetiposervizios[2];
 		}
		if (cod.equals("1"))
		{
              companyCode = request.getParameter("rangeabiutentetiposervizio_companyCode");
              codiceUtente= request.getParameter("rangeabiutentetiposervizio_codiceUtente");
              codiceTipologiaServizio= request.getParameter("rangeabiutentetiposervizio_codiceTipologiaServizio");
		}
		String chiaveRangeTipoServizio = request.getParameter("rangeabiutentetiposervizio_chiaveRangeTipoServizio");
		String inizioRangeDa = request.getParameter("rangeabiutentetiposervizio_inizioRangeDa");
		String fineRangeA = request.getParameter("rangeabiutentetiposervizio_fineRangeA");
		String inizioRangePer = request.getParameter("rangeabiutentetiposervizio_inizioRangePer");
		String tipoRange = request.getParameter("rangeabiutentetiposervizio_tipoRange");
		String flagCin = request.getParameter("rangeabiutentetiposervizio_flagCin");
		try {
			RangeAbiUtenteTipoServizio rangeAbiUtente = new RangeAbiUtenteTipoServizio(chiaveRangeTipoServizio,companyCode,codiceUtente,codiceTipologiaServizio,tipoRange,inizioRangeDa,fineRangeA,inizioRangePer,flagCin,usernameAutenticazione);
			/* we prepare object for save */
			RangeAbiUtenteTipoServizioSaveRequest saveRequest = new RangeAbiUtenteTipoServizioSaveRequest(rangeAbiUtente,codOp);
			/* we save object */
			saveRangeAbiUtenteTipoServizio(saveRequest, request);
			
		} catch (Exception e) {
			try {
				RangeAbiUtenteTipoServizioDetailRequest detailRequest = new RangeAbiUtenteTipoServizioDetailRequest(chiaveRangeTipoServizio);
				RangeAbiUtenteTipoServizioDetailResponse detailResponse = getRangeAbiUtenteTipoServizio(detailRequest, request);
				//request.setAttribute("rangeabiutentetiposervizio", detailResponse.getRangeabiutentetiposervizio());
			      request.setAttribute("rangeabiutentetiposervizio_chiaveRangeTipoServizio", detailResponse.getRangeabiutentetiposervizio().getChiaveRangeTipoServizio());
				  request.setAttribute("rangeabiutentetiposervizio_companyCode", detailResponse.getRangeabiutentetiposervizio().getCompanyCode());
				  request.setAttribute("rangeabiutentetiposervizio_codiceUtente", detailResponse.getRangeabiutentetiposervizio().getCodiceUtente());
				  request.setAttribute("rangeabiutentetiposervizio_codiceTipologiaServizio", detailResponse.getRangeabiutentetiposervizio().getCodiceTipologiaServizio());
				  request.setAttribute("rangeabiutentetiposervizio_inizioRangeDa", detailResponse.getRangeabiutentetiposervizio().getInizioRangeDa());
				  request.setAttribute("rangeabiutentetiposervizio_fineRangeA", detailResponse.getRangeabiutentetiposervizio().getFineRangeA());
				  request.setAttribute("rangeabiutentetiposervizio_inizioRangePer", detailResponse.getRangeabiutentetiposervizio().getInizioRangePer());
				  request.setAttribute("rangeabiutentetiposervizio_tipoRange", detailResponse.getRangeabiutentetiposervizio().getTipoRange());
				  request.setAttribute("rangeabiutentetiposervizio_flagCin", detailResponse.getRangeabiutentetiposervizio().getFlagCin());
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERR.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.GENERIC_ERR.format());
			request.setAttribute("error", "error"); 
			System.out.println(e.getMessage());
		}
	}
	
	
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String chiaveRangeTipoServizio = request.getParameter("rangeabiutentetiposervizio_chiaveRangeTipoServizio");
		try {
			request.setAttribute("varname", "rangeabiutentetiposervizio");
			//stub = new RangeAbiUtenteTipoServizioImplementationStub(new URL(getRangeabiutentetiposervizioManagerEndpointUrl()), null);
			/* we prepare object for cancel */
			RangeAbiUtenteTipoServizioCancelRequest cancelRequest = new RangeAbiUtenteTipoServizioCancelRequest(chiaveRangeTipoServizio);
			/* we cancel object */
			cancelRangeAbiUtenteTipoServizio(cancelRequest, request);
			request.setAttribute("message", Messages.CANC_OK.format());
		} catch (FaultType f) {
			request.setAttribute("message", Messages.valueOf("ERR_" + f.getCode()).format());
		} catch (Exception e) {
			request.setAttribute("message", Messages.GENERIC_ERR.format());
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
	
	private RangeAbiUtenteTipoServizioSearchResponse getRangeAbiUtenteTipoServizioSearchResponse(
			String chiaveRange,String codiceSocieta, String codiceUtente, String codiceTipologiaServizio,
			String inizioRangeDa,String fineRangeA,String inizioRangePer, String strDescrSocieta, 
			String strDescrUtente, String strDescrTipologiaServizio,  int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		RangeAbiUtenteTipoServizioSearchResponse res = null;
		RangeAbiUtenteTipoServizioSearchRequest in = new RangeAbiUtenteTipoServizioSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		
		
		in.setChiaveRangeTipoServizio(chiaveRange == null ? "" : chiaveRange);
		in.setCompanyCode(codiceSocieta == null ? "": codiceSocieta);
		in.setCodiceUtente(codiceUtente == null ? "": codiceUtente);
		in.setCodiceTipologiaServizio(codiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		in.setInizioRangeDa(inizioRangeDa == null ? "" :inizioRangeDa);
		in.setFineRangeA(fineRangeA == null ? "": fineRangeA);
		in.setInizioRangePer(inizioRangePer == null ? "": inizioRangePer);
		in.setStrDescrSocieta(strDescrSocieta == null ? "" : strDescrSocieta);
		in.setStrDescrUtente(strDescrUtente == null ? "" : strDescrUtente);
		in.setStrDescrTipologiaServizio(strDescrTipologiaServizio == null ? "" :strDescrTipologiaServizio);
		
		res = WSCache.rangeAbiUtenteTipoServizioServer.getRangeAbiUtenteTipoServizios(in, request);
		return res;
	}
	
	private RangeAbiUtenteTipoServizioDetailResponse getRangeAbiUtenteTipoServizioDetailSearchResponse(String chiaveRangeTipoServizio, HttpServletRequest request) throws FaultType, RemoteException
	{
		 
		RangeAbiUtenteTipoServizioDetailResponse res = null;
		RangeAbiUtenteTipoServizioDetailRequest in = new RangeAbiUtenteTipoServizioDetailRequest(chiaveRangeTipoServizio);
		in.setChiaveRangeTipoServizio(chiaveRangeTipoServizio == null ? "" : chiaveRangeTipoServizio);
		
		res = WSCache.rangeAbiUtenteTipoServizioServer.getRangeAbiUtenteTipoServizio(in, request);
		return res;
	}
	private void  saveRangeAbiUtenteTipoServizio(RangeAbiUtenteTipoServizioSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.rangeAbiUtenteTipoServizioServer.save(saveRequest, request);
		
	}

	private void  cancelRangeAbiUtenteTipoServizio(RangeAbiUtenteTipoServizioCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.rangeAbiUtenteTipoServizioServer.cancel(cancelRequest, request);
		
	}
	
	private ConfigUtenteTipoServizioSearchResponse getConfigUtenteTipoServizioSearchResponse(
			String companyCode, String codiceTipologiaServizio, String codiceUtente,  int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigUtenteTipoServizioSearchResponse res = null;
		ConfigUtenteTipoServizioSearchRequest in = new ConfigUtenteTipoServizioSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setCodiceTipologiaServizio(codiceTipologiaServizio == null ? "" : codiceTipologiaServizio);
		in.setCodiceUtente(codiceUtente== null ? "" : codiceUtente);
		
		res = WSCache.configUtenteTipoServizioServer.getConfigUtenteTipoServizios(in, request);
		return res;
	}
	
	private RangeAbiUtenteTipoServizioDetailResponse getRangeAbiUtenteTipoServizio (RangeAbiUtenteTipoServizioDetailRequest detailRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		return WSCache.rangeAbiUtenteTipoServizioServer.getRangeAbiUtenteTipoServizio(detailRequest, request);
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