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
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioResponse;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioResponsePageInfo;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioSearchRequest;
import com.seda.payer.pgec.webservice.rangeabiutentetiposervizio.dati.RangeAbiUtenteTipoServizioSearchResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

/**
 * @author lmontesi
 *
 */
@SuppressWarnings("serial")
public class RangeAbiUtenteTipoServizioActionSearch extends BaseManagerAction {
	
	private static String codop = "search";
	String urlAccesso = null;
	String descrizione = null;
	String order = null;
	int rowsPerPage = 0;
	int pageNumber = 0;
	private String strCodiceSocieta,strCodiceUtente;

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
			
				String strDescrSocieta = (String)request.getAttribute("rangeabiutentetiposervizio_strDescrSocieta");
				String strDescrUtente = (String)request.getAttribute("rangeabiutentetiposervizio_strDescrUtente");
				String strDescrTipologiaServizio = (String)request.getAttribute("rangeabiutentetiposervizio_strDescrTipologiaServizio");
					
				rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
				pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
			try {

				RangeAbiUtenteTipoServizioSearchRequest in = new RangeAbiUtenteTipoServizioSearchRequest();
				
				in.setStrDescrSocieta(strDescrSocieta);
				in.setStrDescrUtente(strDescrUtente);
				in.setStrDescrTipologiaServizio(strDescrTipologiaServizio);
				in.setChiaveRangeTipoServizio("");
				in.setCompanyCode("");
				in.setCodiceUtente("");
				in.setCodiceTipologiaServizio("");
				in.setInizioRangeDa("");
				in.setFineRangeA("");
				in.setInizioRangePer("");
				in.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
				in.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
				in.setOrder(order == null ? "" : order);

				RangeAbiUtenteTipoServizioSearchResponse searchResponse = null;
				searchResponse = WSCache.rangeAbiUtenteTipoServizioServer.getRangeAbiUtenteTipoServizios(in, request);
				
				RangeAbiUtenteTipoServizioResponse rangeAbiUtenteTipoServizioResponse = searchResponse.getResponse();
				
				RangeAbiUtenteTipoServizioResponsePageInfo wsPageInfo = rangeAbiUtenteTipoServizioResponse.getPageInfo();
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

						request.setAttribute("rangeabiutentetiposervizios", rangeAbiUtenteTipoServizioResponse.getListXml());
						request.setAttribute("rangeabiutentetiposervizios.pageInfo", pageInfo);

					}
					else setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
				}
				else setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
				
			} catch (FaultType e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		return null;
		
	}

}
