/**
 * 
 */
package org.seda.payer.manager.configurazione.actions;


import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoSearchRequest;
import com.seda.payer.pgec.webservice.canalepagamento.dati.CanalePagamentoSearchResponse;
import com.seda.payer.pgec.webservice.cartapagamento.dati.CartaPagamentoSearchRequest;
import com.seda.payer.pgec.webservice.cartapagamento.dati.CartaPagamentoSearchResponse;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamento;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoSaveRequest;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.Response;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.ResponseReturncode;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.StatusResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.user.dati.UserSearchRequest;
import com.seda.payer.pgec.webservice.user.dati.UserSearchResponse;

/**
 * 
 * @author mmontisano
 *
 */

@SuppressWarnings("serial")
public class GatewayPagamentoActionAdd extends BaseManagerAction {

	private static String codop = "add";
	private static String ritornaViewstate = "Gatewaypagamentos_search";
	private HashMap<String,Object> parametri = null;
	private String usernameAutenticazione = null;
	
	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		/*
		 * Eseguo le azioni
		 */
		switch(firedButton)
		{
		case TX_BUTTON_RESET:
			resetParametri(request);
			break;
			
		case TX_BUTTON_NULL:
			break;
			
		case TX_BUTTON_INDIETRO:
			request.setAttribute("vista",ritornaViewstate);
			break;

		case TX_BUTTON_AGGIUNGI:	
			/*
			 * Recupero i parametri dalla form
			 */
			String esito = "OK";
			/*
			 * Se OK effettuo il salvataggio
			 */
			if (esito.equals("OK"))
			{	
				//Recupero del codice operatore
				UserBean user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
				if( user!=null) 
				   request.setAttribute("userAppl_usernameAutenticazione",user.getUserName().trim());
				
				    usernameAutenticazione = (String)request.getAttribute("userAppl_usernameAutenticazione");
								
					String companyCode = "";
					String userCode = "";
					
					String codOp=request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
					request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
					request.setAttribute("error", null);
					String cod = request.getParameter("prova");
					String arrStr = request.getParameter("gatewaypagamento_strUsers");
					if (cod.equals("0") && arrStr!=null && arrStr.length()>0)
					{
						  String[] strUsers = arrStr.split("\\|");
						  companyCode = strUsers[0];
						  userCode= strUsers[1];
			 		}
					if (cod.equals("1"))  
					{
						  companyCode = request.getParameter("gatewaypagamento_companyCode");
						  userCode = request.getParameter("gatewaypagamento_userCode");
					}
					
					String chiaveCanalePagamento = request.getParameter("gatewaypagamento_chiaveCanalePagamento");
					String codiceCartaPagamento = request.getParameter("gatewaypagamento_codiceCartaPagamento");
					String chiaveGateway = request.getParameter("gatewaypagamento_chiaveGateway");
			        String  descrizioneGateway = request.getParameter("gatewaypagamento_descrizioneGateway");
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
			        if (strImportoScostamento != null && !strImportoScostamento.equals(""))        
			        	importoScostamento = BigDecimal.valueOf(Double.parseDouble(strImportoScostamento.replace(",", ".")));
			        
			        String urlRedirectPayerPerQuietanza = request.getParameter("gatewaypagamento_urlRedirectPayerPerQuietanza");
			        
					GatewayPagamento gatewayPagamento = new GatewayPagamento(chiaveGateway,companyCode,userCode,chiaveCanalePagamento,descrizioneGateway,urlSitoWebGateway,tipoGateway,emailNotificaAdmin,urlApiEndpoint,apiUser,apiPassword,apiSignature,urlApiImage,apiVersion,urlApiRedirect,codiceNegozio,codiceMacAvvio,codiceMacEsito,tipoAutorizzazione,tipoContabilizzazione,opzioniAggiuntive,codiceCartaPagamento,flagAttivazione,usernameAutenticazione, pathImgLogo, urlApiCancel, codiceSIAAziendaDestinataria, codiceCINBancaMittente, codiceABIBancaMittente, codiceCABBancaMittente, codiceContoCorrente, deltaGiorniDataContabile, importoScostamento, urlRedirectPayerPerQuietanza);
					/* we prepare object for save */
					GatewayPagamentoSaveRequest saveRequest = new GatewayPagamentoSaveRequest(gatewayPagamento,codOp);
								
				try {
					StatusResponse out = WSCache.gatewayPagamentoServer.save(saveRequest, request);
					if (out != null)
					{
						Response response = out.getResponse();
						if (response != null)
						{
							String insertRetCode = response.getReturncode().toString();
							if (insertRetCode.equals(ResponseReturncode.value1.getValue()))
								insertRetCode = "00";
							request.setAttribute("insertRetCode", insertRetCode);
							if(response.getReturncode().toString().equals(ResponseReturncode.value1.getValue()))
							{
								session.setAttribute("recordInserted", "OK");
								request.setAttribute("vista",ritornaViewstate);
							}
							else setFormMessage("var_form", response.getReturnmessage(), request);
						}
						else setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
					}
					else setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
				} catch (FaultType e) {
					setFormMessage("var_form", "Errore: " + Messages.INS_ERRD.format(), request);
					e.printStackTrace();
				} catch (RemoteException e) {
					setFormMessage("var_form", "Errore: " + e.getMessage(), request);
					e.printStackTrace();
				}
			}
			else
				setFormMessage("var_form", esito, request);
			break;
		
		}
		if (!firedButton.equals(FiredButton.TX_BUTTON_INDIETRO))
			add(request);

		/*
		 * Setto l'action della form ed il tipo di operazione
		 */
		request.setAttribute("do_command_name","gatewaypagamento_add.do");
		request.setAttribute("codop",codop);
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
}
