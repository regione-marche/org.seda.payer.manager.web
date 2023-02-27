package org.seda.payer.manager.configurazione.actions;


import java.rmi.RemoteException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoConto;
import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoCancelRequest;
import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoDetailRequest;
import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoDetailResponse;
import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoResponse;
import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoResponsePageInfo;
import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoSaveRequest;
import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoSearchRequest;
import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoSearchResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest2;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;




public class ConfigEstrattoContoAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private String usernameAutenticazione = null;
	private String userCodiceSocieta = null;
	
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
		if (user != null)
			   userCodiceSocieta = (user.getProfile().equals("AMMI") ? "" : user.getCodiceSocieta());
		   else
			   userCodiceSocieta = "XXXXX";
		
		
		if( user!=null) 
		   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
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
		//saveadd(request);
		search(request);
		
		return null;
	}
	
	public Object search(HttpServletRequest request) throws ActionException {
		try {
			String companyCode = ((String)request.getAttribute("configestrattoconto_searchcompanyCode")==null ? "":  (String)request.getAttribute("configestrattoconto_searchcompanyCode"));
			String userCode = ((String)request.getAttribute("configestrattoconto_searchuserCode")==null ? "":  (String)request.getAttribute("configestrattoconto_searchuserCode"));
			String chiaveEnte = ((String)request.getAttribute("configestrattoconto_searchchiaveEnte")==null ? "":  (String)request.getAttribute("configestrattoconto_searchchiaveEnte"));
			
			
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			if (firedButton!=null){
					
				request.setAttribute("configestrattoconto_searchcompanyCode", "");
				request.setAttribute("configestrattoconto_searchuserCode", "");
				request.setAttribute("configestrattoconto_searchchiaveEnte", "");
				companyCode="";
				userCode="";
				chiaveEnte="";
			}
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				companyCode="";
				userCode="";
				chiaveEnte="";
				
			}
			
			ConfigEstrattoContoSearchResponse searchResponse = getConfigEstrattoContoSearchResponse(companyCode, userCode, chiaveEnte, rowsPerPage, pageNumber, order, request);
			ConfigEstrattoContoResponse configestrattocontoResponse = searchResponse.getResponse();
			ConfigEstrattoContoResponsePageInfo responsePageInfo = configestrattocontoResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			//request.setAttribute("configestrattoconto", searchRequest);
			request.setAttribute("configestrattoconto_searchcompanyCode", companyCode);
			request.setAttribute("configestrattoconto_searchuserCode", userCode);
			request.setAttribute("configestrattoconto_searchchiaveEnte", chiaveEnte);
			
			request.setAttribute("configestrattocontos", configestrattocontoResponse.getListXml());
			request.setAttribute("configestrattocontos.pageInfo", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			
			loadDdlEnteTipoServizio(request);
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {	  
		request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		  request.setAttribute("configestrattocontoUserCode",request.getParameter("configestrattocontoUserCode"));		  
		  request.setAttribute("configestrattocontoChiaveEnte",request.getParameter("configestrattocontoChiaveEnte"));	
		  return null; 
	}
	
	private void loadDdlEnteTipoServizio(HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigUtenteTipoServizioEnteSearchResponse configUtenteTipoServizioEnteSearchResponse = getConfigUtenteTipoServizioEntes2(userCodiceSocieta,"","","", request);
		request.setAttribute("entetiposervizios", configUtenteTipoServizioEnteSearchResponse.getResponse().getListXml());
		
	}
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			
			loadDdlEnteTipoServizio(request);
			
			String userCode = ((String)request.getAttribute("configestrattoconto_userCode")==null ? "":  (String)request.getAttribute("configestrattoconto_userCode"));
			String chiaveEnte = ((String)request.getAttribute("configestrattoconto_chiaveEnte")==null ? "":  (String)request.getAttribute("configestrattoconto_chiaveEnte"));
			
			ConfigEstrattoContoDetailResponse response = getConfigEstrattoContoDetailSearchResponse(chiaveEnte,userCode, request);
			
			//
			request.setAttribute("configestrattoconto_companyCode", response.getConfigEstrattoConto().getCompanyCode());
			request.setAttribute("configestrattoconto_userCode", response.getConfigEstrattoConto().getUserCode());
			request.setAttribute("configestrattoconto_chiaveEnte", response.getConfigEstrattoConto().getChiaveEnte());
			request.setAttribute("configestrattoconto_urlIntegrazione", response.getConfigEstrattoConto().getUrlIntegrazione());
			request.setAttribute("configestrattoconto_urlCooperazione", response.getConfigEstrattoConto().getUrlCooperazione());
			request.setAttribute("configestrattoconto_tipoIntegrazione", response.getConfigEstrattoConto().getTipoIntegrazione());
			request.setAttribute("configestrattoconto_codiceUtenteSeda", response.getConfigEstrattoConto().getCodiceUtenteSeda().trim());
			//inizio LP PG200060			
			request.setAttribute("configestrattoconto_cupFlag", response.getConfigEstrattoConto().getCupFlag());  //SB PG1900XX_009
			//fine LP PG200060
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
					request.setAttribute("configestrattoconto_userCode", "");
					request.setAttribute("configestrattoconto_chiaveEnte", "");
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
					request.setAttribute("configestrattoconto_userCode", null);
					request.setAttribute("configestrattoconto_chiaveEnte", null);						
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
		
		String userCode = request.getParameter("configestrattoconto_userCode");
		String companyCode = request.getParameter("configestrattoconto_companyCode");
		String chiaveEnte = request.getParameter("configestrattoconto_chiaveEnte");
		
		String arrStr = request.getParameter("configestrattoconto_strEntetiposervizios");
		
		if (codOp!=null && codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0   && arrStr!=null && arrStr.length()>0)
		{
			  String[] strEntetiposervizios = arrStr.split("\\|");
			  companyCode = strEntetiposervizios[0];
			  userCode= strEntetiposervizios[1];
 		      chiaveEnte= strEntetiposervizios[2];
 		}
		
		String urlIntegrazione = (String)request.getAttribute("configestrattoconto_urlIntegrazione");
		String urlCooperazione = (String)request.getAttribute("configestrattoconto_urlCooperazione");
		String tipoIntegrazione = (String)request.getAttribute("configestrattoconto_tipoIntegrazione");    
	    String codiceUtenteSeda = (String)request.getAttribute("configestrattoconto_codiceUtenteSeda");
	    //inizio LP PG200060
	    String cupFlag = "";
		String template = getTemplateCurrentApplication(request, request.getSession()); 
        if(template.equalsIgnoreCase("regmarche")) {
	    	cupFlag = (String)request.getAttribute("configestrattoconto_cupFlag"); //SB PG1900XX_009
        }
	    //fine LP PG200060
	    
	    if (checkForm(tipoIntegrazione, codiceUtenteSeda, request))
	    {
			try {
				//inizio LP PG200060
				/*
				ConfigEstrattoConto configestrattoconto = new ConfigEstrattoConto(companyCode, userCode, chiaveEnte, 
						urlIntegrazione, urlCooperazione, tipoIntegrazione, codiceUtenteSeda, usernameAutenticazione);
				*/
				ConfigEstrattoConto configestrattoconto = new ConfigEstrattoConto(companyCode, userCode, chiaveEnte, 
						urlIntegrazione, urlCooperazione, tipoIntegrazione, codiceUtenteSeda, usernameAutenticazione, cupFlag);
				/* we prepare object for save */
				ConfigEstrattoContoSaveRequest saveRequest = new ConfigEstrattoContoSaveRequest(configestrattoconto,codOp);
				/* we save object */
				saveConfigEstrattoConto(saveRequest, request);
	
			} catch (Exception e) {
				try {
					
				    ConfigEstrattoContoDetailResponse detailResponse = getConfigEstrattoConto(chiaveEnte, userCode, request);
					//request.setAttribute("configestrattoconto", detailResponse.getConfigEstrattoConto());
				    request.setAttribute("configestrattoconto_companyCode", detailResponse.getConfigEstrattoConto().getCompanyCode());
					request.setAttribute("configestrattoconto_userCode", detailResponse.getConfigEstrattoConto().getUserCode());
					request.setAttribute("configestrattoconto_chiaveEnte", detailResponse.getConfigEstrattoConto().getChiaveEnte());
					request.setAttribute("configestrattoconto_urlIntegrazione", detailResponse.getConfigEstrattoConto().getUrlIntegrazione());
					request.setAttribute("configestrattoconto_urlCooperazione", detailResponse.getConfigEstrattoConto().getUrlCooperazione());
					request.setAttribute("configestrattoconto_tipoIntegrazione", detailResponse.getConfigEstrattoConto().getTipoIntegrazione());
					request.setAttribute("configestrattoconto_codiceUtenteSeda", detailResponse.getConfigEstrattoConto().getCodiceUtenteSeda().trim());
				    //inizio LP PG200060
			    	request.setAttribute("configestrattoconto_cupFlag", detailResponse.getConfigEstrattoConto().getCupFlag()); //SB PG1900XX_009
				    //fine LP PG200060
				} catch (Exception ignore) { }
				if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
				if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.UPDT_ERR.format());
				request.setAttribute("error", "error"); 
				System.out.println(e.getMessage());
			}
	    }
	    else
	    {
	    	try {
	    		loadDdlEnteTipoServizio(request);
	    	} catch (Exception e){ e.printStackTrace(); }
	    	request.setAttribute("done", null);
	    }
	}
	
	private boolean checkForm(String tipoIntegrazione, String codiceUtenteSeda, HttpServletRequest request)
	{
		if (tipoIntegrazione.equals("S"))
		{
			if (codiceUtenteSeda.trim().length() == 0)
			{
				setFormMessage("frmAction", "In caso di integrazione IMMEDIATA SEDA è necessario specificare il Codice Utente Seda", request);
				return false;
			}
		}
		else if (tipoIntegrazione.equals("I") || tipoIntegrazione.equals("D"))
		{
			if (codiceUtenteSeda.trim().length() > 0)
			{
				setFormMessage("frmAction", "In caso di integrazione IMMEDIATA o DIFFERITA il Codice Utente Seda non deve essere specificato", request);
				return false;
			}
		}
		return true;
	}
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String userCode = request.getParameter("configestrattoconto_userCode");
		String chiaveEnte = request.getParameter("configestrattoconto_chiaveEnte");
		try {
			request.setAttribute("varname", "estrattoconto");
			/* we prepare object for cancel */
			ConfigEstrattoContoCancelRequest cancelRequest = new ConfigEstrattoContoCancelRequest(chiaveEnte, userCode);
			/* we cancel object */			
			cancelConfigEstrattoConto(cancelRequest, request);
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
	
	private ConfigEstrattoContoSearchResponse getConfigEstrattoContoSearchResponse(
			String companyCode, String userCode, String chiaveEnte, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigEstrattoContoSearchResponse res = null;
		ConfigEstrattoContoSearchRequest in = new ConfigEstrattoContoSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setUserCode(userCode == null ? "" : userCode);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		
		res = WSCache.configEstrattoContoServer.getConfigEstrattoContos(in, request);
		return res;
	}
	private void  saveConfigEstrattoConto(ConfigEstrattoContoSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.configEstrattoContoServer.save(saveRequest, request);
		
	}
	
	private void  cancelConfigEstrattoConto(ConfigEstrattoContoCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.configEstrattoContoServer.cancel(cancelRequest, request);
		
	}
	
	private ConfigEstrattoContoDetailResponse getConfigEstrattoContoDetailSearchResponse(String chiaveEnte, String userCode, HttpServletRequest request) throws FaultType, RemoteException
	{
		 
		ConfigEstrattoContoDetailResponse res = null;
		ConfigEstrattoContoDetailRequest in = new ConfigEstrattoContoDetailRequest(chiaveEnte, userCode);
		in.setUserCode(userCode == null ? "" : userCode);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		res = WSCache.configEstrattoContoServer.getConfigEstrattoConto(in, request);
		return res;
	}


	private ConfigEstrattoContoDetailResponse getConfigEstrattoConto(String chiaveEnte, String userCode, HttpServletRequest request) throws com.seda.payer.pgec.webservice.srv.FaultType, RemoteException
	{
		ConfigEstrattoContoDetailRequest detailRequest = new ConfigEstrattoContoDetailRequest(chiaveEnte, userCode);
		detailRequest.setUserCode(userCode == null ? "" : userCode);
		detailRequest.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
	
		
		return WSCache.configEstrattoContoServer.getConfigEstrattoConto(detailRequest, request);
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
	
	private ConfigUtenteTipoServizioEnteSearchResponse getConfigUtenteTipoServizioEntes2(String codiceSocieta,String codiceUtente,String chiaveEnte, String procName, HttpServletRequest request) throws FaultType, RemoteException{
		ConfigUtenteTipoServizioEnteSearchResponse res = null;
		ConfigUtenteTipoServizioEnteSearchRequest2 in = new ConfigUtenteTipoServizioEnteSearchRequest2();
		
		
		in.setCompanyCode(codiceSocieta == null ? "" : codiceSocieta);
		in.setCodiceUtente(codiceUtente == null ? "" : codiceUtente);
		in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
		in.setProcName(procName == null ? "" : procName);
		
		res = WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEntes2(in, request);
		
		return res;
	}
	//inizio LP PG200060
	private String getTemplateCurrentApplication(HttpServletRequest request, HttpSession session)
	{
		MAFRequest mafReq = new MAFRequest(request);
		String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
		if (applicationName != null)
		{
			UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
			if (userBean != null)
			{
				String templateName = userBean.getTemplate(applicationName);
				if (templateName != null && !templateName.equals(""))
					return templateName;
				else
					return "default";
			}
		}
		else
		{
			//recupero il name del template dal file di properties
			if (ManagerStarter.configuration != null)
			{
				String templateName = ManagerStarter.configuration.getProperty(PropertiesPath.templateBase.format() + request.getServerName());

				if (templateName != null && !templateName.equals(""))
					return templateName;
			}
		}
		return "default";
	}	
	//fine LP PG200060
}
