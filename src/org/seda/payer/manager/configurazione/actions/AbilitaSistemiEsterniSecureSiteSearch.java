package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.AbilitaSistemiEsterniSecureSiteResponse;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.AbilitaSistemiEsterniSecureSiteResponsePageInfo;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.AbilitaSistemiEsterniSecureSiteSearchRequest;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.AbilitaSistemiEsterniSecureSiteSearchResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class AbilitaSistemiEsterniSecureSiteSearch extends BaseManagerAction {
	private static String codop = "search";
	String urlAccesso = null;
	String descrizione = null;
	String idServizio = null;
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
		if (firedButton.equals(FiredButton.TX_BUTTON_NUOVO)) 
		{
			//18022011 GG inizio
			request.setAttribute("urlAccesso", "");
			request.setAttribute("descrizione", "");
			request.setAttribute("idServizio", "");
			request.setAttribute("chk_flagAttivazione", false);
			request.setAttribute("chk_flagRedirect", false);
			//18022011 GG fine
			return null;
		}
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
				urlAccesso = ((String)request.getAttribute("urlAccesso") == null ? "" : (String)request.getAttribute("urlAccesso"));
				descrizione = ((String)request.getAttribute("descrizione") == null ? "" : (String)request.getAttribute("descrizione"));
				idServizio = ((String)request.getAttribute("idServizio") == null ? "" : (String)request.getAttribute("idServizio"));
				rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
				pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
				order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
			try {
				AbilitaSistemiEsterniSecureSiteSearchResponse searchResponse = null;
				AbilitaSistemiEsterniSecureSiteSearchRequest in = new AbilitaSistemiEsterniSecureSiteSearchRequest();
				in.setRowsPerPage(rowsPerPage);
				in.setPageNumber(pageNumber);
				//in.setOrder(order);	//TODO da introdurre con successive implementazioni
				in.setUrlAccesso(urlAccesso);
				in.setDescrizione(descrizione);
				in.setIdServizio(idServizio);
				
				searchResponse = WSCache.abilitaSistemiEsterniSecureSiteServer.getAbilitaSistemiEsterniSecureSites(in, request);
				AbilitaSistemiEsterniSecureSiteResponse abilitaSistemiEsterniSecureSiteResponse = null;
				abilitaSistemiEsterniSecureSiteResponse = searchResponse.getResponse();
				
				AbilitaSistemiEsterniSecureSiteResponsePageInfo wsPageInfo = abilitaSistemiEsterniSecureSiteResponse.getPageInfo();
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

						request.setAttribute("listaConfig", abilitaSistemiEsterniSecureSiteResponse.getListXml());	//TODO da verificare su jsp
						request.setAttribute("listaConfig.pageInfo", pageInfo);										//TODO da verificare su jsp
					}
					else 
					    {
						 request.setAttribute("listaConfig", "");	
						 // request.setAttribute("listaConfig.pageInfo", "");
						 setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
						}
				}
				else 
					{
					 request.setAttribute("listaConfig", "");	
					 // request.setAttribute("listaConfig.pageInfo", "");
					 setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
					}
				
			} catch (FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}

		return null;
		
	}

}
