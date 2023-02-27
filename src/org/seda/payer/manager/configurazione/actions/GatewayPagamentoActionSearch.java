/**
 * 
 */
package org.seda.payer.manager.configurazione.actions;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoResponse;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoResponsePageInfo;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoSearchRequest;
import com.seda.payer.pgec.webservice.gatewaypagamento.dati.GatewayPagamentoSearchResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;


/**
 * 
 * @author mmontisano
 *
 */
@SuppressWarnings("serial")
public class GatewayPagamentoActionSearch  extends BaseManagerAction {
	
	private static String codop = "search";
	String urlAccesso = null;
	String descrizione = null;
	String descrizioneSocieta = null;
	String descrizioneUtente = null;
	String descrizioneCanalePagamento = null;
	String descrizioneCartaPagamento = null;
	String descrizioneGateway = null;
	String order = null;
	int rowsPerPage = 0;
	int pageNumber = 0;

	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		/*
		 * Resetto l'eventuale messaggio rimasto nel viewstate
		 */
		resetFormMessage(request);
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		/*
		 * FLOW - Se la richiesta è relativa all'inserimento di un nuovo record
		 */
		if (firedButton.equals(FiredButton.TX_BUTTON_NUOVO)) return null;
		/*
		 * Se è presente in sessione uno degli attributi "recordInserted" 
		 * o "recordUpdated" vuol dire che è stato inserito/modificato
		 * correttamente un sistema esterno e devo visualizzare il messaggio
		 */
		if ((String) session.getAttribute("recordInserted") != null) 
		{
			session.removeAttribute("recordInserted");
			setFormMessage("search_form", Messages.INS_OK.format(), request);
		}
		if ((String) session.getAttribute("recordUpdated") != null) 
		{
			session.removeAttribute("recordUpdated");
			setFormMessage("search_form", Messages.UPDT_OK.format(), request);
		}
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		/*
		 * Reset
		 */
		if (firedButton.equals(FiredButton.TX_BUTTON_RESET)) resetParametri(request);
		/*
		 * Setto l'attributo relativo all'operazione
		 */
		request.setAttribute("codop",codop);
		/*
		 * Cerco i dati
		 */
				descrizioneSocieta = ((String)request.getAttribute("gatewaypagamento_strDescrSocieta") == null ? "" : (String)request.getAttribute("gatewaypagamento_strDescrSocieta"));
				descrizioneUtente = ((String)request.getAttribute("gatewaypagamento_userDesc") == null ? "" : (String)request.getAttribute("gatewaypagamento_userDesc"));
				descrizioneCanalePagamento = ((String)request.getAttribute("gatewaypagamento_strDescrCanalePagamento") == null ? "" : (String)request.getAttribute("gatewaypagamento_strDescrCanalePagamento"));
				descrizioneCartaPagamento = ((String)request.getAttribute("gatewaypagamento_strDescrCartaPagamento") == null ? "" : (String)request.getAttribute("gatewaypagamento_strDescrCartaPagamento"));
				descrizioneGateway = ((String)request.getAttribute("gatewaypagamento_descrGateway") == null ? "" : (String)request.getAttribute("gatewaypagamento_descrGateway"));
				rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
				pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
			try {
				GatewayPagamentoSearchResponse searchResponse = null;
				GatewayPagamentoSearchRequest in = new GatewayPagamentoSearchRequest("","","","","",descrizioneGateway,descrizioneUtente,descrizioneCanalePagamento,descrizioneCartaPagamento,descrizioneSocieta,"",rowsPerPage,pageNumber,order);
				in.setRowsPerPage(rowsPerPage);
				in.setPageNumber(pageNumber);
				in.setOrder(order);					
				searchResponse = WSCache.gatewayPagamentoServer.getGatewayPagamentos(in, request);
				
				GatewayPagamentoResponse gatewayPagamentoResponse = null;
				gatewayPagamentoResponse = searchResponse.getResponse();
				
				GatewayPagamentoResponsePageInfo wsPageInfo = gatewayPagamentoResponse.getPageInfo();
				if (wsPageInfo != null)
				{
					if (wsPageInfo.getNumRows() > 0)
					{
						PageInfo pageInfo = new PageInfo();
						pageInfo.setFirstRow(wsPageInfo.getFirstRow());
						pageInfo.setLastRow(wsPageInfo.getLastRow());
						pageInfo.setNumPages(wsPageInfo.getNumPages());
						pageInfo.setNumRows(wsPageInfo.getNumRows());
						pageInfo.setPageNumber(wsPageInfo.getPageNumber());
						pageInfo.setRowsPerPage(rowsPerPage);

//					    request.setAttribute("gatewaypagamento_userDesc",descrizioneUtente);
//						request.setAttribute("gatewaypagamento_descrGateway",descrizioneGateway);
//						request.setAttribute("gatewaypagamento_strDescrCartaPagamento",descrizioneCartaPagamento);
//						request.setAttribute("gatewaypagamento_strDescrCanalePagamento",descrizioneCanalePagamento);
//						request.setAttribute("gatewaypagamento_strDescrSocieta",descrizioneSocieta);
						
						request.setAttribute("gatewaypagamentos", gatewayPagamentoResponse.getListXml());
						request.setAttribute("gatewaypagamentos.pageInfo", pageInfo);

					}
					else
					   {
						//inizio LP PG200060
						//request.setAttribute("gatewaypagamentos", "");
						request.setAttribute("gatewaypagamentos", gatewayPagamentoResponse.getListXml());
						//fine LP PG200060
						setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
					   }
				}
				else 					   
				   {
					//inizio LP PG200060
					//request.setAttribute("gatewaypagamentos", "");
					request.setAttribute("gatewaypagamentos", gatewayPagamentoResponse.getListXml());
					//fine LP PG200060
					setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
				   }

				
			} catch (FaultType e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		return null;
		
	}



}