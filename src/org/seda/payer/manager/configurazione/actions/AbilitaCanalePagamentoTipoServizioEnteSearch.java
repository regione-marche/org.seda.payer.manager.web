package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteResponse;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteResponsePageInfo;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteSearchRequest;
import com.seda.payer.pgec.webservice.abilitacanalepagamentotiposervizioente.dati.AbilitaCanalePagamentoTipoServizioEnteSearchResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class AbilitaCanalePagamentoTipoServizioEnteSearch extends BaseManagerAction {
	private static String codop = "search";
	String descrizioneSocieta = null;
	String descrizioneUtente = null;
	String descrizioneEnte = null;
	String descrizioneCanalePagamento = null;
	String descrizioneTipologiaServizio = null;
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
		 * correttamente un utente e devo visualizzare il messaggio
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
				descrizioneSocieta = ((String)request.getAttribute("descrizioneSocieta") == null ? "" : (String)request.getAttribute("descrizioneSocieta"));
				descrizioneUtente = ((String)request.getAttribute("descrizioneUtente") == null ? "" : (String)request.getAttribute("descrizioneUtente"));
				descrizioneEnte = ((String)request.getAttribute("descrizioneEnte") == null ? "" : (String)request.getAttribute("descrizioneEnte"));
				descrizioneCanalePagamento = ((String)request.getAttribute("descrizioneCanalePagamento") == null ? "" : (String)request.getAttribute("descrizioneCanalePagamento"));
				descrizioneTipologiaServizio = ((String)request.getAttribute("descrizioneTipologiaServizio") == null ? "" : (String)request.getAttribute("descrizioneTipologiaServizio"));
				rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
				pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
				order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
			try {
				AbilitaCanalePagamentoTipoServizioEnteSearchResponse searchResponse = null;
				AbilitaCanalePagamentoTipoServizioEnteResponse abilitaCanalePagamentoTipoServizioEnteResponse = null;
				AbilitaCanalePagamentoTipoServizioEnteSearchRequest in = new AbilitaCanalePagamentoTipoServizioEnteSearchRequest();
				in.setPageNumber(pageNumber);
				in.setRowsPerPage(rowsPerPage);
				//in.setOrder(order);	//TODO da considerare in successive implementazioni		
				in.setStrDescrSocieta(descrizioneSocieta);
				in.setStrDescrUtente(descrizioneUtente);
				in.setStrDescrEnte(descrizioneEnte);
				in.setStrDescrCanalePagamento(descrizioneCanalePagamento);
				in.setStrDescrTipologiaServizio(descrizioneTipologiaServizio);
								
				//AbilitaCanalePagamentoTipoServizioEnteSearchResponse searchResponse = WSCache.abilitazTributiServer.getAbilitaCanalePagamentoTipoServizioEntes(in);
				searchResponse = WSCache.abilitazTributiServer.getAbilitaCanalePagamentoTipoServizioEntes(in,request);
				abilitaCanalePagamentoTipoServizioEnteResponse = searchResponse.getResponse();
				
				AbilitaCanalePagamentoTipoServizioEnteResponsePageInfo wsPageInfo = abilitaCanalePagamentoTipoServizioEnteResponse.getPageInfo();
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

						request.setAttribute("listaConfig", abilitaCanalePagamentoTipoServizioEnteResponse.getListXml());
						request.setAttribute("listaConfig.pageInfo", pageInfo);
					}
					else setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
				}
				else setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
			} catch (FaultType e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		return null;	
	}

}
