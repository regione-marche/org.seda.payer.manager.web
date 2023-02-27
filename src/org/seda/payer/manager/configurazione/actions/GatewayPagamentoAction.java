/**
 * 
 */
package org.seda.payer.manager.configurazione.actions;


import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;


import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;

import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamento;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoCancelRequest;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoDetailRequest;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoDetailResponse;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoResponse;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoResponsePageInfo;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoSaveRequest;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoSearchRequest;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoSearchResponse;

import com.seda.payer.pgec.webservice.srv.FaultType;

import com.seda.payer.pgec.webservice.user.dati.UserSearchRequest;
import com.seda.payer.pgec.webservice.user.dati.UserSearchResponse;


import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoSearchRequest;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoSearchResponse;


import com.seda.payer.pgec.webservice.cartapagamento.dati.CartaPagamentoSearchRequest;
import com.seda.payer.pgec.webservice.cartapagamento.dati.CartaPagamentoSearchResponse;




/**
 * 
 * @author mmontisano
 *
 */
public class GatewayPagamentoAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	private String usernameAutenticazione;

	@Override
	public void start(HttpServletRequest request) {
		rowsPerPage = (request.getParameter("rowsPerPage") == null) 
		   ? getDefaultListRows() : Integer.parseInt(request.getParameter("rowsPerPage"));
		   pageNumber = (request.getParameter("pageNumber") == null) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		   order = (request.getParameter("order") == null) ? "" : request.getParameter("order");
		
		   HttpSession session = request.getSession();
		   UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);

			if( user!=null){ 
			   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());			   
			}

		    usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
		    String firedButton = (String)request.getAttribute("tx_button_nuovo");
			//System.out.println(firedButton+"index");
			if (firedButton!=null){
				if(firedButton.equals("Nuovo")){
					request.setAttribute("gatewaypagamento_companyCode", "");
					request.setAttribute("gatewaypagamento_userCode", "");
					request.setAttribute("gatewaypagamento_chiaveCanalePagamento", "");
					request.setAttribute("gatewaypagamento_descrizioneGateway", "");
					request.setAttribute("gatewaypagamento_strDescrCartaPagamento", "");
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
			String codiceSocieta = request.getParameter("gatewaypagamento_companyCode");
			String userCode = request.getParameter("gatewaypagamento_userCode");
			String chiaveCanalePagamento = request.getParameter("gatewaypagamento_chiaveCanalePagamento");
			String descrizioneGateway = request.getParameter("gatewaypagamento_descrizioneGateway");
			String strDescrCartaPagamento = request.getParameter("gatewaypagamento_strDescrCartaPagamento");
			String strDescrSocieta = request.getParameter("gatewaypagamento_strDescrSocieta");
			
			String firedButton = (String)request.getAttribute("tx_button_indietro");
			String firedButtonN = (String)request.getAttribute("tx_button_nuovo");
			if (firedButton!=null||firedButtonN!=null){
				
				request.setAttribute("gatewaypagamento_companyCode", "");
				request.setAttribute("gatewaypagamento_userCode", "");
				request.setAttribute("gatewaypagamento_chiaveCanalePagamento", "");
				request.setAttribute("gatewaypagamento_descrizioneGateway", "");
				request.setAttribute("gatewaypagamento_strDescrCartaPagamento", "");
				request.setAttribute("gatewaypagamento_strDescrSocieta", "");
				codiceSocieta="";
				userCode="";
				chiaveCanalePagamento="";
				descrizioneGateway="";
				strDescrCartaPagamento="";
			}
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);
				codiceSocieta="";
				userCode="";
				chiaveCanalePagamento="";
				descrizioneGateway="";
				strDescrCartaPagamento="";
				
			}
					
			GatewayPagamentoSearchRequest searchRequest = new GatewayPagamentoSearchRequest("",codiceSocieta,userCode,chiaveCanalePagamento,"",descrizioneGateway,"","",strDescrCartaPagamento,strDescrSocieta,"",rowsPerPage,pageNumber,order);
			GatewayPagamentoSearchResponse searchResponse = getGatewayPagamentos(searchRequest, request);
			GatewayPagamentoResponse gatewaypagamentoServizioResponse = searchResponse.getResponse();
			GatewayPagamentoResponsePageInfo responsePageInfo = gatewaypagamentoServizioResponse.getPageInfo();
			PageInfo pageInfo = new PageInfo(); {
				pageInfo.setFirstRow(responsePageInfo.getFirstRow());
				pageInfo.setLastRow(responsePageInfo.getLastRow());
				pageInfo.setNumPages(responsePageInfo.getNumPages());
				pageInfo.setNumRows(responsePageInfo.getNumRows());
				pageInfo.setPageNumber(responsePageInfo.getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
			}
			//request.setAttribute("gatewaypagamento", searchRequest);
			request.setAttribute("gatewaypagamento_companyCode",searchRequest.getCompanyCode());
		    request.setAttribute("gatewaypagamento_userCode",searchRequest.getUserCode());
			request.setAttribute("gatewaypagamento_chiaveCanalePagamento",searchRequest.getChiaveCanalePagamento());
			request.setAttribute("gatewaypagamento_codiceCartaPagamento",searchRequest.getCodiceCartaPagamento());
			request.setAttribute("gatewaypagamento_chiaveGateway",searchRequest.getChiaveGateway());
			request.setAttribute("gatewaypagamento_descrizioneGateway",searchRequest.getDescrizioneGateway());
			request.setAttribute("gatewaypagamento_strDescrCartaPagamento",strDescrCartaPagamento);
			request.setAttribute("gatewaypagamento_strDescrSocieta",strDescrSocieta);
			
			request.setAttribute("gatewaypagamentos", gatewaypagamentoServizioResponse.getListXml());
			request.setAttribute("gatewaypagamentos.pageInfo", pageInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {	  
		request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		request.setAttribute("chiaveGateway",request.getParameter("chiaveGateway"));
		return null; 
	}	
	
	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			
			GatewayPagamentoDetailResponse response = 
				getGatewayPagamento(new GatewayPagamentoDetailRequest(request.getParameter("gatewaypagamento_chiaveGateway")), request);
			//request.setAttribute("gatewaypagamento", response.getGatewaypagamento());
			request.setAttribute("gatewaypagamento_companyCode",response.getGatewaypagamento().getCompanyCode());
		    request.setAttribute("gatewaypagamento_userCode",response.getGatewaypagamento().getUserCode());
			request.setAttribute("gatewaypagamento_chiaveCanalePagamento",response.getGatewaypagamento().getChiaveCanalePagamento());
			request.setAttribute("gatewaypagamento_codiceCartaPagamento",response.getGatewaypagamento().getCodiceCartaPagamento());
			request.setAttribute("gatewaypagamento_chiaveGateway",response.getGatewaypagamento().getChiaveGateway());
			request.setAttribute("gatewaypagamento_descrizioneGateway",response.getGatewaypagamento().getDescrizioneGateway());
			request.setAttribute("gatewaypagamento_urlSitoWebGateway",response.getGatewaypagamento().getUrlSitoWebGateway());
			request.setAttribute("gatewaypagamento_tipoGateway",response.getGatewaypagamento().getTipoGateway());
		    request.setAttribute("gatewaypagamento_emailNotificaAdmin",response.getGatewaypagamento().getEmailNotificaAdmin());
			request.setAttribute("gatewaypagamento_urlApiEndpoint",response.getGatewaypagamento().getUrlApiEndpoint());
			request.setAttribute("gatewaypagamento_apiUser",response.getGatewaypagamento().getApiUser());
			request.setAttribute("gatewaypagamento_apiPassword",response.getGatewaypagamento().getApiPassword());
			request.setAttribute("gatewaypagamento_apiSignature",response.getGatewaypagamento().getApiSignature());
			request.setAttribute("gatewaypagamento_urlApiImage",response.getGatewaypagamento().getUrlApiImage());
			request.setAttribute("gatewaypagamento_apiVersion",response.getGatewaypagamento().getApiVersion());
			request.setAttribute("gatewaypagamento_urlApiRedirect",response.getGatewaypagamento().getUrlApiRedirect());
			request.setAttribute("gatewaypagamento_codiceNegozio",response.getGatewaypagamento().getCodiceNegozio());
			request.setAttribute("gatewaypagamento_codiceMacAvvio",response.getGatewaypagamento().getCodiceMacAvvio());
			request.setAttribute("gatewaypagamento_codiceMacEsito",response.getGatewaypagamento().getCodiceMacEsito());
			request.setAttribute("gatewaypagamento_tipoAutorizzazione",response.getGatewaypagamento().getTipoAutorizzazione());
			request.setAttribute("gatewaypagamento_tipoContabilizzazione",response.getGatewaypagamento().getTipoContabilizzazione());
			request.setAttribute("gatewaypagamento_opzioniAggiuntive",response.getGatewaypagamento().getOpzioniAggiuntive());
			request.setAttribute("gatewaypagamento_flagAttivazione",response.getGatewaypagamento().getFlagAttivazione());
						
			request.setAttribute("gatewaypagamento_codiceABIBancaMittente", response.getGatewaypagamento().getCodiceABIBancaMittente());
			request.setAttribute("gatewaypagamento_codiceCABBancaMittente", response.getGatewaypagamento().getCodiceCABBancaMittente());
			request.setAttribute("gatewaypagamento_codiceCINBancaMittente", response.getGatewaypagamento().getCodiceCINBancaMittente());
			request.setAttribute("gatewaypagamento_codiceContoCorrente", response.getGatewaypagamento().getCodiceContoCorrente());		
			request.setAttribute("gatewaypagamento_codiceSIAAziendaDestinataria", response.getGatewaypagamento().getCodiceSIAAziendaDestinataria());
			request.setAttribute("gatewaypagamento_deltaGiorniDataContabile", String.valueOf(response.getGatewaypagamento().getDeltaGiorniDataContabile()));
			request.setAttribute("gatewaypagamento_importoScostamento", response.getGatewaypagamento().getImportoScostamento().toString());			
			request.setAttribute("gatewaypagamento_pathImgLogo", response.getGatewaypagamento().getPathImgLogo());
			request.setAttribute("gatewaypagamento_urlApiCancel", response.getGatewaypagamento().getUrlApiCancel());
			request.setAttribute("gatewaypagamento_urlRedirectPayerPerQuietanza", response.getGatewaypagamento().getUrlRedirectPayerPerQuietanza());
			
			request.setAttribute("descrizioneCartaPagamento", request.getParameter("gatewaypagamento_descrizioneCartaPagamento"));
			
			CanalePagamentoSearchRequest searchRequest2 = new CanalePagamentoSearchRequest("","", 0, 0, "");
			CanalePagamentoSearchResponse searchResponse2 = getCanalePagamentos(searchRequest2, request);
			request.setAttribute("canalepagamentos", searchResponse2.getResponse().getListXml());
			/* we retry all cartapagamentos */ 
			
			CartaPagamentoSearchRequest searchRequest3 = new CartaPagamentoSearchRequest("","", 0, 0, "");
			CartaPagamentoSearchResponse searchResponse3 = getCartaPagamentos(searchRequest3, request);
			request.setAttribute("cartapagamentos", searchResponse3.getResponse().getListXml());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object add(HttpServletRequest request) throws ActionException {
		try {
			/* we retry all users */ 
			UserSearchRequest searchRequest = new UserSearchRequest("","","","","","", 0, 0, "");
			UserSearchResponse searchResponse = getUsers(searchRequest, request);
			request.setAttribute("users", searchResponse.getResponse().getListXml());
			/* we retry all canalepagamentos */ 
			
			CanalePagamentoSearchRequest searchRequest2 = new CanalePagamentoSearchRequest("","", 0, 0, "");
			CanalePagamentoSearchResponse searchResponse2 = getCanalePagamentos(searchRequest2, request);
			request.setAttribute("canalepagamentos", searchResponse2.getResponse().getListXml());
			/* we retry all cartapagamentos */ 
			
			CartaPagamentoSearchRequest searchRequest3 = new CartaPagamentoSearchRequest("","", 0, 0, "");
			CartaPagamentoSearchResponse searchResponse3 = getCartaPagamentos(searchRequest3, request);
			request.setAttribute("cartapagamentos", searchResponse3.getResponse().getListXml());
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
					request.setAttribute("gatewaypagamento_companyCode", "");
					request.setAttribute("gatewaypagamento_userCode", "");
					request.setAttribute("gatewaypagamento_chiaveCanalePagamento", "");
					request.setAttribute("gatewaypagamento_descrizioneGateway", "");
					request.setAttribute("gatewaypagamento_strDescrCartaPagamento", "");
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
					request.setAttribute("gatewaypagamento_companyCode", "");
					request.setAttribute("gatewaypagamento_userCode", "");
					request.setAttribute("gatewaypagamento_chiaveCanalePagamento", "");
					request.setAttribute("gatewaypagamento_descrizioneGateway", "");
					request.setAttribute("gatewaypagamento_strDescrCartaPagamento", "");						
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
		String companyCode = "";
		String userCode = "";
		
		String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		
		String arrStr = request.getParameter("gatewaypagamento_strUsers");
		if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0 && arrStr!=null && arrStr.length()>0)
		{
			  String[] strUsers = arrStr.split("\\|");
			  companyCode = strUsers[0];
			  userCode= strUsers[1];
			  //chiaveCanalePagamento = request.getParameter("gatewaypagamento_strCanalePagamentos");
			  //codiceCartaPagamento = request.getParameter("gatewaypagamento_strCartaPagamentos");
 		}
		if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) 
		{
			  companyCode = request.getParameter("gatewaypagamento_companyCode");
			  userCode = request.getParameter("gatewaypagamento_userCode");
			  //chiaveCanalePagamento = request.getParameter("gatewaypagamento_chiaveCanalePagamento");
			  //codiceCartaPagamento = request.getParameter("gatewaypagamento_codiceCartaPagamento");
		}
		
		String chiaveCanalePagamento = request.getParameter("gatewaypagamento_chiaveCanalePagamento");
		String codiceCartaPagamento = request.getParameter("gatewaypagamento_codiceCartaPagamento");
		String chiaveGateway = request.getParameter("gatewaypagamento_chiaveGateway");
		//inizio LP PG200060
        //String  descrizioneGateway = StringEscapeUtils.escapeHtml((String)request.getAttribute("gatewaypagamento_descrizioneGateway"));
		String template = getTemplateCurrentApplication(request, request.getSession());
		String  descrizioneGateway = "";
		if(template.equalsIgnoreCase("regmarche")) {
	        descrizioneGateway = request.getParameter("gatewaypagamento_descrizioneGateway");
		} else {
			descrizioneGateway = StringEscapeUtils.escapeHtml((String)request.getAttribute("gatewaypagamento_descrizioneGateway"));
		}
		//fine LP PG200060
        String  urlSitoWebGateway = request.getParameter("gatewaypagamento_urlSitoWebGateway");
        String  tipoGateway = request.getParameter("gatewaypagamento_tipoGateway");
        String  emailNotificaAdmin = request.getParameter("gatewaypagamento_emailNotificaAdmin");
        String  urlApiEndpoint = request.getParameter("gatewaypagamento_urlApiEndpoint");
        String  apiUser = request.getParameter("gatewaypagamento_apiUser");
        String  apiPassword = request.getParameter("gatewaypagamento_apiPassword");
        String  apiSignature = request.getParameter("gatewaypagamento_apiSignature");
        String  urlApiImage = request.getParameter("gatewaypagamento_urlApiImage");
        String  apiVersion = request.getParameter("gatewaypagamento_apiVersion");
        String  urlApiRedirect = request.getParameter("gatewaypagamento_urlApiRedirect");
        String  codiceNegozio = request.getParameter("gatewaypagamento_codiceNegozio");
        String  codiceMacAvvio = request.getParameter("gatewaypagamento_codiceMacAvvio");
        String  codiceMacEsito = request.getParameter("gatewaypagamento_codiceMacEsito");
        String  tipoAutorizzazione = request.getParameter("gatewaypagamento_tipoAutorizzazione");
        String  tipoContabilizzazione = request.getParameter("gatewaypagamento_tipoContabilizzazione");
        String  opzioniAggiuntive = request.getParameter("gatewaypagamento_opzioniAggiuntive");       
        String  flagAttivazione = request.getParameter("gatewaypagamento_flagAttivazione");

        String pathImgLogo = request.getParameter("gatewaypagamento_pathImgLogo");
        String urlApiCancel = request.getParameter("gatewaypagamento_urlApiCancel");
        String codiceSIAAziendaDestinataria = request.getParameter("gatewaypagamento_codiceSIAAziendaDestinataria");
        String codiceCINBancaMittente = request.getParameter("gatewaypagamento_codiceCINBancaMittente");
        String codiceABIBancaMittente = request.getParameter("gatewaypagamento_codiceABIBancaMittente");
        String codiceCABBancaMittente = request.getParameter("gatewaypagamento_codiceCABBancaMittente");
        String codiceContoCorrente = request.getParameter("gatewaypagamento_codiceContoCorrente");
        
        int deltaGiorniDataContabile=0;
        String strDeltaGiorniDataContabile = request.getParameter("gatewaypagamento_deltaGiorniDataContabile").trim();
        if (strDeltaGiorniDataContabile != null && !strDeltaGiorniDataContabile.equals("")) 
        	deltaGiorniDataContabile =Integer.parseInt(strDeltaGiorniDataContabile);
        
        BigDecimal importoScostamento = new BigDecimal(0);
        String strImportoScostamento = request.getParameter("gatewaypagamento_importoScostamento").trim();
        if (strImportoScostamento != null && !strDeltaGiorniDataContabile.equals(""))        
        	importoScostamento = BigDecimal.valueOf(Double.parseDouble(strImportoScostamento));
        
        String urlRedirectPayerPerQuietanza = request.getParameter("gatewaypagamento_urlRedirectPayerPerQuietanza");
       
		try {
			
			GatewayPagamento gatewayPagamento = new GatewayPagamento(chiaveGateway,companyCode,userCode,chiaveCanalePagamento,descrizioneGateway,urlSitoWebGateway,tipoGateway,emailNotificaAdmin,urlApiEndpoint,apiUser,apiPassword,apiSignature,urlApiImage,apiVersion,urlApiRedirect,codiceNegozio,codiceMacAvvio,codiceMacEsito,tipoAutorizzazione,tipoContabilizzazione,opzioniAggiuntive,codiceCartaPagamento,flagAttivazione,usernameAutenticazione, pathImgLogo, urlApiCancel, codiceSIAAziendaDestinataria, codiceCINBancaMittente, codiceABIBancaMittente, codiceCABBancaMittente, codiceContoCorrente, deltaGiorniDataContabile, importoScostamento, urlRedirectPayerPerQuietanza);
			/* we prepare object for save */
			GatewayPagamentoSaveRequest saveRequest = new GatewayPagamentoSaveRequest(gatewayPagamento,codOp);
			/* we save object */
			save(saveRequest,request);
			
		} catch (Exception e) {
			try {
				GatewayPagamentoDetailRequest detailRequest = new GatewayPagamentoDetailRequest(chiaveGateway);
				GatewayPagamentoDetailResponse detailResponse = getGatewayPagamento(detailRequest, request);
				//request.setAttribute("gatewaypagamento", detailResponse.getGatewaypagamento());
				request.setAttribute("gatewaypagamento_companyCode",detailResponse.getGatewaypagamento().getCompanyCode());
			    request.setAttribute("gatewaypagamento_userCode",detailResponse.getGatewaypagamento().getUserCode());
				request.setAttribute("gatewaypagamento_chiaveCanalePagamento",detailResponse.getGatewaypagamento().getChiaveCanalePagamento());
				request.setAttribute("gatewaypagamento_codiceCartaPagamento",detailResponse.getGatewaypagamento().getCodiceCartaPagamento());
				request.setAttribute("gatewaypagamento_chiaveGateway",detailResponse.getGatewaypagamento().getChiaveGateway());
				request.setAttribute("gatewaypagamento_descrizioneGateway",detailResponse.getGatewaypagamento().getDescrizioneGateway());
				request.setAttribute("gatewaypagamento_urlSitoWebGateway",detailResponse.getGatewaypagamento().getUrlSitoWebGateway());
				request.setAttribute("gatewaypagamento_tipoGateway",detailResponse.getGatewaypagamento().getTipoGateway());
			    request.setAttribute("gatewaypagamento_emailNotificaAdmin",detailResponse.getGatewaypagamento().getEmailNotificaAdmin());
				request.setAttribute("gatewaypagamento_urlApiEndpoint",detailResponse.getGatewaypagamento().getUrlApiEndpoint());
				request.setAttribute("gatewaypagamento_apiUser",detailResponse.getGatewaypagamento().getApiUser());
				request.setAttribute("gatewaypagamento_apiPassword",detailResponse.getGatewaypagamento().getApiPassword());
				request.setAttribute("gatewaypagamento_apiSignature",detailResponse.getGatewaypagamento().getApiSignature());
				request.setAttribute("gatewaypagamento_urlApiImage",detailResponse.getGatewaypagamento().getUrlApiImage());
				request.setAttribute("gatewaypagamento_apiVersion",detailResponse.getGatewaypagamento().getApiVersion());
				request.setAttribute("gatewaypagamento_urlApiRedirect",detailResponse.getGatewaypagamento().getUrlApiRedirect());
				request.setAttribute("gatewaypagamento_codiceNegozio",detailResponse.getGatewaypagamento().getCodiceNegozio());
				request.setAttribute("gatewaypagamento_codiceMacAvvio",detailResponse.getGatewaypagamento().getCodiceMacAvvio());
				request.setAttribute("gatewaypagamento_codiceMacEsito",detailResponse.getGatewaypagamento().getCodiceMacEsito());
				request.setAttribute("gatewaypagamento_tipoAutorizzazione",detailResponse.getGatewaypagamento().getTipoAutorizzazione());
				request.setAttribute("gatewaypagamento_tipoContabilizzazione",detailResponse.getGatewaypagamento().getTipoContabilizzazione());
				request.setAttribute("gatewaypagamento_opzioniAggiuntive",detailResponse.getGatewaypagamento().getOpzioniAggiuntive());
				request.setAttribute("gatewaypagamento_flagAttivazione",detailResponse.getGatewaypagamento().getFlagAttivazione());
				
				request.setAttribute("gatewaypagamento_codiceABIBancaMittente", detailResponse.getGatewaypagamento().getCodiceABIBancaMittente());
				request.setAttribute("gatewaypagamento_codiceCABBancaMittente", detailResponse.getGatewaypagamento().getCodiceCABBancaMittente());
				request.setAttribute("gatewaypagamento_codiceCINBancaMittente", detailResponse.getGatewaypagamento().getCodiceCINBancaMittente());
				request.setAttribute("gatewaypagamento_codiceContoCorrente", detailResponse.getGatewaypagamento().getCodiceContoCorrente());		
				request.setAttribute("gatewaypagamento_codiceSIAAziendaDestinataria", detailResponse.getGatewaypagamento().getCodiceSIAAziendaDestinataria());
				request.setAttribute("gatewaypagamento_deltaGiorniDataContabile", String.valueOf(detailResponse.getGatewaypagamento().getDeltaGiorniDataContabile()));
				request.setAttribute("gatewaypagamento_importoScostamento", detailResponse.getGatewaypagamento().getImportoScostamento().toString());			
				request.setAttribute("gatewaypagamento_pathImgLogo(", detailResponse.getGatewaypagamento().getPathImgLogo());
				request.setAttribute("gatewaypagamento_urlApiCancel", detailResponse.getGatewaypagamento().getUrlApiCancel());
				request.setAttribute("gatewaypagamento_urlRedirectPayerPerQuietanza", detailResponse.getGatewaypagamento().getUrlRedirectPayerPerQuietanza());
				
				
			} catch (Exception ignore) { }
			if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) request.setAttribute("message", Messages.INS_ERR.format());
			if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) request.setAttribute("message", Messages.UPDT_ERR.format());
			request.setAttribute("error", "error"); 
			//System.out.println(e.getMessage());
		}
	}
	
	
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String chiaveGateway = request.getParameter("gatewaypagamento_chiaveGateway");
		
		try {
			request.setAttribute("varname", "gatewaypagamento");
			
			/* we prepare object for cancel */
			GatewayPagamentoCancelRequest cancelRequest = new GatewayPagamentoCancelRequest(chiaveGateway);
			/* we cancel object */
			cancel(cancelRequest, request);
			request.setAttribute("message", Messages.CANC_OK.format());
		} catch (FaultType f) {
			request.setAttribute("message", Messages.valueOf("ERR_" + f.getCode()).format());
		} catch (Exception e) {
			request.setAttribute("message", Messages.CANCEL_ERR.format());
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

	private void cancel (GatewayPagamentoCancelRequest cancelRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.gatewayPagamentoServer.cancel(cancelRequest, request);
	}
	private void save (GatewayPagamentoSaveRequest saveRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		WSCache.gatewayPagamentoServer.save(saveRequest, request);
	}
	
	private GatewayPagamentoDetailResponse getGatewayPagamento (GatewayPagamentoDetailRequest detailRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		return WSCache.gatewayPagamentoServer.getGatewayPagamento(detailRequest, request);
	}
	private GatewayPagamentoSearchResponse getGatewayPagamentos (GatewayPagamentoSearchRequest searchRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		
		return WSCache.gatewayPagamentoServer.getGatewayPagamentos(searchRequest, request);
	}
	
	private UserSearchResponse getUsers(UserSearchRequest userSearchRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		return WSCache.userServer.getUsers(userSearchRequest, request);
	}
	private CanalePagamentoSearchResponse getCanalePagamentos(CanalePagamentoSearchRequest canalePagamentoSearchRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		return WSCache.canPagamentoServer.getCanalePagamentos(canalePagamentoSearchRequest, request);
	}
	private CartaPagamentoSearchResponse getCartaPagamentos(CartaPagamentoSearchRequest cartaPagamentoSearchRequest, HttpServletRequest request) throws FaultType, RemoteException
	{
		return WSCache.cartaPagamentoServer.getCartaPagamentos(cartaPagamentoSearchRequest, request);
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