package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioGetListaXmlRequestType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioGetListaXmlResponseType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioGetListaXmlResponseTypePageInfo;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ConfRendUtenteServizioSearch extends BaseManagerAction {
	private static String codop = "search";
	String descrizioneSocieta = null;
	String descrizioneUtente = null;
	String descrizioneTipologiaServizio = null;
//	PG22XX09_YL5 INI
	String codiceTipologiaServizio = null;
//	PG22XX09_YL5 FINE
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
				descrizioneTipologiaServizio = ((String)request.getAttribute("descrizioneTipologiaServizio") == null ? "" : (String)request.getAttribute("descrizioneTipologiaServizio"));
//				PG22XX09_YL5 INI
				codiceTipologiaServizio = ((String)request.getAttribute("codiceTipologiaServizio") == null ? "" : (String)request.getAttribute("codiceTipologiaServizio"));
//				PG22XX09_YL5 FINE
				rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
				pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
				order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
			try {
				com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ResponseType response = null;
				ConfRendUtenteServizioGetListaXmlRequestType in = 
					new ConfRendUtenteServizioGetListaXmlRequestType(); 
				in.setPageNumber(pageNumber);
				in.setRowsPerPage(rowsPerPage);
				in.setDescrizioneSocieta(descrizioneSocieta);
				in.setDescrizioneUtente(descrizioneUtente);
				in.setDescrizioneTipologiaServizio(descrizioneTipologiaServizio);
//				PG22XX09_YL5 INI
				in.setCodiceTipologiaServizio(codiceTipologiaServizio);
//				PG22XX09_YL5 fine;
				ConfRendUtenteServizioGetListaXmlResponseType out = 
					WSCache.confRendUtenteServizioServer.getListaXml(in, request);
				response = out.getResponse();
				if (response.getRetCode().equals("00"))
				{
					ConfRendUtenteServizioGetListaXmlResponseTypePageInfo wsPageInfo = out.getPageInfo();
					if (wsPageInfo != null)
					{
						if (wsPageInfo.getNumRows() > 0)
						{
							com.seda.data.spi.PageInfo pageInfo = new PageInfo();
							pageInfo.setFirstRow(wsPageInfo.getFirstRow());
							pageInfo.setLastRow(wsPageInfo.getLastRow());
							pageInfo.setNumPages(wsPageInfo.getNumPages());
							pageInfo.setNumRows(wsPageInfo.getNumRows());
							pageInfo.setPageNumber(wsPageInfo.getPageNumber());
							pageInfo.setRowsPerPage(wsPageInfo.getRowsPerPage());

							request.setAttribute("listaConfig", out.getListaXml());
							request.setAttribute("listaConfig.pageInfo", pageInfo);
						}
						else 
						   {
							request.setAttribute("listaConfig", "");
							setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
						   }
					}
					else 	
					   {
						request.setAttribute("listaConfig", "");
						setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
					   }

				}
				else
				{
					String messaggio = response.getRetMessage(); 
					setFormMessage("search_form", (messaggio == null || messaggio.equals("")) ? "Errore generico" : messaggio, request);
				}
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
