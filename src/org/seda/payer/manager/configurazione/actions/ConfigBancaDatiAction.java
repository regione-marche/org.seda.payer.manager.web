package org.seda.payer.manager.configurazione.actions;


import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import java.util.Calendar;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
//import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoConto;
//import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoCancelRequest;
//import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoDetailRequest;
//import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoDetailResponse;
//import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoResponse;
//import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoResponsePageInfo;
//import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoSaveRequest;
//import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoSearchRequest;
import com.seda.payer.pgec.webservice.configbancadati.dati.ConfigBancaDati;
import com.seda.payer.pgec.webservice.configbancadati.dati.ConfigBancaDatiCancelRequest;
import com.seda.payer.pgec.webservice.configbancadati.dati.ConfigBancaDatiDetailRequest;
import com.seda.payer.pgec.webservice.configbancadati.dati.ConfigBancaDatiDetailResponse;
import com.seda.payer.pgec.webservice.configbancadati.dati.ConfigBancaDatiResponse;
import com.seda.payer.pgec.webservice.configbancadati.dati.ConfigBancaDatiResponsePageInfo;
import com.seda.payer.pgec.webservice.configbancadati.dati.ConfigBancaDatiSaveRequest;
import com.seda.payer.pgec.webservice.configbancadati.dati.ConfigBancaDatiSearchRequest;
import com.seda.payer.pgec.webservice.configbancadati.dati.ConfigBancaDatiSearchResponse;
//import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoDetailResponse;
//import com.seda.payer.pgec.webservice.configestrattoconto.dati.ConfigEstrattoContoSearchResponse;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest2;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;
import com.seda.payer.pgec.webservice.user.dati.UserSearchRequest;
import com.seda.payer.pgec.webservice.user.dati.UserSearchResponse;




public class ConfigBancaDatiAction extends DispatchHtmlAction {

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
			System.out.println("convert long");
			long	idBancaDati	 = (request.getAttribute("configbancadati_searchidbancadati") 	==	null ? 0 : Long.parseLong((String) request.getAttribute("configbancadati_searchidbancadati")));
			System.out.println("fatto");
			String	companyCode	 = ((String)	request.getAttribute("configbancadati_searchcompanyCode")	==	null ? "":  (String)request.getAttribute("configbancadati_searchcompanyCode"));
			String	cuteCute	 = ((String)	request.getAttribute("configbancadati_searchcuteCute")		==	null ? "":  (String)request.getAttribute("configbancadati_searchcuteCute"));
			String	name		 = ((String)	request.getAttribute("configbancadati_searchname")			==	null ? "":  (String)request.getAttribute("configbancadati_searchname"));
			String	url			 = ((String)	request.getAttribute("configbancadati_searchurl")			==	null ? "":  (String)request.getAttribute("configbancadati_searchurl"));
			String	operatorCode = ((String)	request.getAttribute("configbancadati_searchoperatorCode")	==	null ? "":  (String)request.getAttribute("configbancadati_searchoperatorCode"));
			String 	updateDate	 = ((String)	request.getAttribute("configbancadati_searchupdateDate")	==	null ? "":  (String)request.getAttribute("configbancadati_searchupdateDate"));
			String 	userName 	 = ((String)	request.getAttribute("configbancadati_searchusername")		==	null ? "":  (String)request.getAttribute("configbancadati_searchusername"));
			String 	password	 = ((String)	request.getAttribute("configbancadati_searchpassword")		==	null ? "":  (String)request.getAttribute("configbancadati_searchpassword"));
			
			
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			if (firedButton!=null){
				
				request.setAttribute("configbancadati_searchidbancadati", "");
				request.setAttribute("configbancadati_searchcompanyCode", "");
				request.setAttribute("configbancadati_searchcuteCute", "");
				request.setAttribute("configbancadati_sename", "");
				request.setAttribute("configbancadati_searchurl", "");
				request.setAttribute("configbancadati_searchoperatorCode", "");
				request.setAttribute("configbancadati_searchupdateDate", "");
				request.setAttribute("configbancadati_searchusername", "");
				request.setAttribute("configbancadati_searchpassword", "");
				
				idBancaDati	 =	0;
				companyCode	 =	"";
				cuteCute	 =	"";
				name		 =	"";
				url			 =	"";
				operatorCode =	"";
				updateDate	 =	"";
				userName	 =	"";
				password	 =	"";
			}
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				idBancaDati	 =	0;
				companyCode	 =	"";
				cuteCute	 =	"";
				name		 =	"";
				url			 =	"";
				operatorCode =	"";
				updateDate	 =	"";
				userName	 =	"";
				password	 =	"";
				
			}
			
			ConfigBancaDatiSearchResponse searchResponse = getConfigBancaDatiSearchResponse(companyCode, cuteCute, name, rowsPerPage, pageNumber, order, request);
			ConfigBancaDatiResponse configbancadatiResponse = searchResponse.getResponse();
			ConfigBancaDatiResponsePageInfo responsePageInfo = configbancadatiResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			request.setAttribute("configbancadati_searchidbancadati",	idBancaDati);
			request.setAttribute("configbancadati_searchcompanyCode",	companyCode);
			request.setAttribute("configbancadati_searchcuteCute", 		cuteCute);
			request.setAttribute("configbancadati_searchname", 			name);
			request.setAttribute("configbancadati_searchurl", 			url);
			request.setAttribute("configbancadati_searchoperatorCode", 	operatorCode);
			request.setAttribute("configbancadati_searchupdateDate", 	updateDate);
			request.setAttribute("configbancadati_searchusername", 		userName);
			request.setAttribute("configbancadati_searchpassword", 		password);
			
			
			request.setAttribute("searchconfigbancadati", configbancadatiResponse.getListXml()); //configbancadatis == searchbancadati
			request.setAttribute("searchconfigbancadati.pageInfo", pageInfo);
		} catch (Exception e) {
			System.out.println("errore = " + e.getMessage());
			e.printStackTrace();
		}
		 catch (Throwable e) {
				System.out.println("errore = " + e.getMessage());
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
		request.setAttribute("configbancadati_cuteCute",request.getParameter("configbancadati_cuteCute"));
		request.setAttribute("configbancadati_idBancaDati",request.getParameter("configbancadati_idBancaDati"));
		return null; 
	}
	
	private void loadDdlEnteTipoServizio(HttpServletRequest request) throws FaultType, RemoteException
	{
//		ConfigUtenteTipoServizioEnteSearchResponse configUtenteTipoServizioEnteSearchResponse = getConfigUtenteTipoServizioEntes2(userCodiceSocieta,"","","", request);
//		request.setAttribute("entetiposervizios", configUtenteTipoServizioEnteSearchResponse.getResponse().getListXml());

		/* we retry all users */ 
		UserSearchRequest searchRequest = new UserSearchRequest("","","","","","", 0, 0, "");
		UserSearchResponse searchResponse = getUsers(searchRequest, request);
		request.setAttribute("entetiposervizios", searchResponse.getResponse().getListXml());
	}
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			
			loadDdlEnteTipoServizio(request);
			
			String cuteCute = ((String)request.getAttribute("configbancadati_cuteCute")==null ? "":  (String)request.getAttribute("configbancadati_cuteCute"));
			long idBancaDati = Long.parseLong((String)request.getAttribute("configbancadati_idBancaDati"));
			
			ConfigBancaDatiDetailResponse response = getConfigBancaDatiDetailSearchResponse(idBancaDati, cuteCute, request);
			
			request.setAttribute("configbancadati_companyCode",			response.getConfigBancaDati().getCompanyCode());
			request.setAttribute("configbancadati_cuteCute",			response.getConfigBancaDati().getCuteCute());
			request.setAttribute("configbancadati_name",				response.getConfigBancaDati().getName()); //nome banca dati
			request.setAttribute("configbancadati_url", 				response.getConfigBancaDati().getUrl());
			request.setAttribute("configbancadati_operatorCode", 		response.getConfigBancaDati().getOperatorCode());
			request.setAttribute("configbancadati_updateDate", 			response.getConfigBancaDati().getUpdateDate());
			request.setAttribute("configbancadati_username", 			response.getConfigBancaDati().getUsername());
			request.setAttribute("configbancadati_password",			response.getConfigBancaDati().getPassword());
//			request.setAttribute("configbancadati_urlIntegrazione", 	response.getConfigBancaDati().getUrlIntegrazione());
//			request.setAttribute("configbancadati_codiceUtenteSeda",	response.getConfigBancaDati().getCodiceUtenteSeda().trim());
			request.setAttribute("configbancadati_tipoIntegrazione", response.getConfigBancaDati().getTipoIntegrazione()); //PG22XX12_SB3

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
					request.setAttribute("configbancadati_cuteCute"	,	"");
					request.setAttribute("configbancadati_idBancaDati",	"");
//					request.setAttribute("configbancadati_companyCode",	"");
//					request.setAttribute("configbancadati_url", 		"");
//					request.setAttribute("configbancadati_operatorCode","");
//					request.setAttribute("configbancadati_updateDate",	"");
//					request.setAttribute("configbancadati_username", 	"");
//					request.setAttribute("configbancadati_password",	"");
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
					request.setAttribute("configbancadati_cuteCute"	,	null);
					request.setAttribute("configbancadati_idBancaDati",	null);
//					request.setAttribute("configbancadati_companyCode", null);
//					request.setAttribute("configbancadati_name"	,		null);
//					request.setAttribute("configbancadati_url",			null);
//					request.setAttribute("configbancadati_operatorCode",null);
//					request.setAttribute("configbancadati_updateDate",	null);
//					request.setAttribute("configbancadati_username"	, 	null);
//					request.setAttribute("configbancadati_password"	, 	null);
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
		
		//String arrStr = request.getParameter("configbancadati_strEntetiposervizios");
		
//		TODO: DA ATTIVARE
		long	idBancaDati	=	request.getParameter("configbancadati_idBancaDati") == null? 0 : Long.parseLong((String)request.getParameter("configbancadati_idBancaDati"));
		String  cuteCute	=	request.getParameter("configbancadati_cuteCute");
		String  companyCode	=	request.getParameter("configbancadati_companyCode");
		String  name		=	request.getParameter("configbancadati_name");
		String  url			=	request.getParameter("configbancadati_url"); //url integrazione
		
		//String  operatorCode=	request.getParameter("configbancadati_operatorCode") == null ? "" : request.getParameter("configbancadati_operatorCode");
        //String  updateDate	=	request.getParameter("configbancadati_updateDate");
        String  userName 	=	request.getParameter("configbancadati_username");
        String  password 	=	request.getParameter("configbancadati_password");
        
        //PG22XX12_SB3 -inizio
        String  tipoIntegrazione =	request.getParameter("configbancadati_tipoIntegrazione")==null ? "I" : request.getParameter("configbancadati_tipoIntegrazione"); 
        //PG22XX12_SB3 -fine

        String arrStr = request.getParameter("configbancadati_strEntetiposervizios");
        
		if (codOp!=null && codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0   && arrStr!=null && arrStr.length()>0)
		{
			  String[] strEntetiposervizios = arrStr.split("\\|");
			  companyCode	= strEntetiposervizios[0];
			  cuteCute		= strEntetiposervizios[1];
// 		      chiaveEnte	= strEntetiposervizios[2];
// 		      idBancaDati 	= Long.parseLong(strEntetiposervizios[2]);
 		}
		
//	    if (checkForm(tipoIntegrazione, codiceUtenteSeda, request))
//	    {
			try {
				//SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				//Date date = sdf.parse(updateDate);
				Calendar cal = Calendar.getInstance();
				//cal.setTime(date);
				
				ConfigBancaDati configbancadati = new ConfigBancaDati(companyCode, cuteCute, idBancaDati, name, url, userName, password, usernameAutenticazione, cal, tipoIntegrazione);
				/* we prepare object for save */
				ConfigBancaDatiSaveRequest saveRequest = new ConfigBancaDatiSaveRequest(configbancadati,usernameAutenticazione);
				/* we save object */
				saveConfigBancaDati(saveRequest, request);
	
			} catch (Exception e) {
				try {
					ConfigBancaDatiDetailResponse detailResponse = getConfigBancaDati(idBancaDati, cuteCute, request);
					request.setAttribute("configbancadati_idbancadati",			detailResponse.getConfigBancaDati().getIdBancaDati());
					request.setAttribute("configbancadati_companyCode",			detailResponse.getConfigBancaDati().getCompanyCode());
					request.setAttribute("configbancadati_cuteCute", 			detailResponse.getConfigBancaDati().getCuteCute());
					request.setAttribute("configbancadati_name", 				detailResponse.getConfigBancaDati().getName());
					request.setAttribute("configbancadati_url", 				detailResponse.getConfigBancaDati().getUrl());
					//request.setAttribute("configbancadati_operatorCode",		detailResponse.getConfigBancaDati().getOperatorCode());
					request.setAttribute("configbancadati_updateDate", 			detailResponse.getConfigBancaDati().getUpdateDate());
					request.setAttribute("configbancadati_username",			detailResponse.getConfigBancaDati().getUsername());
					request.setAttribute("configbancadati_password",			detailResponse.getConfigBancaDati().getPassword());
//					request.setAttribute("configbancadati_codiceUtenteSeda",	detailResponse.getConfigBancaDati().getCodiceUtenteSeda().trim());
				} catch (Exception ignore) { }
				if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERRD.format());
				if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.UPDT_ERR.format());
				request.setAttribute("error", "error"); 
				System.out.println(e.getMessage());
			}
//	    }
//	    else
//	    {
//	    	try {
//	    		loadDdlEnteTipoServizio(request);
//	    	} catch (Exception e){ e.printStackTrace(); }
//	    	request.setAttribute("done", null);
//	    }
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
		String cuteCute = request.getParameter("configbancadati_cuteCute");
		long idBancaDati = Long.parseLong(request.getParameter("configbancadati_idBancaDati"));
				
		try {
			request.setAttribute("varname", "bancadati");
			/* we prepare object for cancel */
			ConfigBancaDatiCancelRequest cancelRequest = new ConfigBancaDatiCancelRequest(cuteCute, idBancaDati);
			/* we cancel object */
			cancelConfigBancaDati(cancelRequest, request);
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
	private ConfigBancaDatiSearchResponse getConfigBancaDatiSearchResponse(
			String companyCode, String cuteCute, String nameBancaDati, int rowsPerPage, int pageNumber, String order, HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigBancaDatiSearchResponse res = null;
		ConfigBancaDatiSearchRequest in = new ConfigBancaDatiSearchRequest();
		in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		in.setOrder(order == null ? "" : order);
		in.setCompanyCode(companyCode == null ? "" : companyCode);
		in.setCuteCute(cuteCute == null ? "" : cuteCute);
		in.setName(nameBancaDati);
		
		res = WSCache.configBancaDatiServer.searchConfigBancaDati(in, request);
		return res;
	}
	
	private void  saveConfigBancaDati(ConfigBancaDatiSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.configBancaDatiServer.save(saveRequest, request);
		
	}
	private void  cancelConfigBancaDati(ConfigBancaDatiCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.configBancaDatiServer.cancel(cancelRequest, request);
		
	}
	
	private ConfigBancaDatiDetailResponse getConfigBancaDatiDetailSearchResponse(long idBancaDati, String cuteCute, HttpServletRequest request) throws FaultType, RemoteException
	{
		 
		ConfigBancaDatiDetailResponse res = null;
		ConfigBancaDatiDetailRequest in = new ConfigBancaDatiDetailRequest(cuteCute, idBancaDati);
		in.setCuteCute(cuteCute == null ? "" : cuteCute);
		in.setIdBancaDati(idBancaDati);

		res = WSCache.configBancaDatiServer.getConfigBancaDati(in, request);
		return res;
	}

	private ConfigBancaDatiDetailResponse getConfigBancaDati(long idBancaDati, String cuteCute, HttpServletRequest request) throws com.seda.payer.pgec.webservice.srv.FaultType, RemoteException
	{
		ConfigBancaDatiDetailRequest detailRequest = new ConfigBancaDatiDetailRequest(cuteCute,idBancaDati);
		detailRequest.setCuteCute(cuteCute == null ? "" : cuteCute);
		detailRequest.setIdBancaDati(idBancaDati);
	
		return WSCache.configBancaDatiServer.getConfigBancaDati(detailRequest, request);
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
	
	//TODO: la chiave ente per questa voce di menu non sarà più presente
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

	private UserSearchResponse getUsers(UserSearchRequest userSearchRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		return WSCache.userServer.getUsers(userSearchRequest, request);
	}

}
