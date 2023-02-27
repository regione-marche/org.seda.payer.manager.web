package org.seda.payer.manager.wisconfig.actions;

import java.rmi.RemoteException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTipologiaStrutturaRicettivaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTipologiaStrutturaRicettivaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTipologiaStrutturaRicettivaCsvRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTipologiaStrutturaRicettivaCsvResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ImpostaSoggiornoTipologiaStruttureSearchAction extends BaseManagerAction {
	private static String codop = "search";
	String descrizionetipologiaStrutture = null;
//	String descrizioneSoggetto = null;
//	String esenzione = null;
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
		
		descrizionetipologiaStrutture = ((String)request.getAttribute("tipologiastrutture_descrizioneTipologia") == null ? "" : (String)request.getAttribute("tipologiastrutture_descrizioneTipologia"));
		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		//order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
		
		if (firedButton.equals(FiredButton.TX_BUTTON_DOWNLOAD))
		{
			String file="";
			try {
				RecuperaTipologiaStrutturaRicettivaCsvRequest reqCsv = new RecuperaTipologiaStrutturaRicettivaCsvRequest();
				reqCsv.setDescrizioneTipologiaStruttura(descrizionetipologiaStrutture);
				RecuperaTipologiaStrutturaRicettivaCsvResponse resCsv = WSCache.impostaSoggiornoConfigServer.recuperaTipologiaStrutturaRicettivaCsv(reqCsv, request);
				if (resCsv != null && resCsv.getResponse() != null && resCsv.getResponse().getRetCode().getValue().equals("00"))
				{
					file = resCsv.getStringaCsv();
					//aggiustamento carattere \r perso nel webservice 
					file = file.replaceAll("\n", "\r\n");
					Calendar cal = Calendar.getInstance();
					String timestamp = GenericsDateNumbers.formatCalendarData(cal, "yyyyMMdd_HHmmss");
					request.setAttribute("filename",timestamp + "_TipologiaStruttureRicettive.csv");
					request.setAttribute("fileCsv", file);
				}
				else
				{
					setFormMessage("search_form", "Si è verificato un errore durante l'esportazione dei dati" , request);
				}
			} catch (FaultType e1) {
				setFormMessage("search_form", e1.getMessage() , request);
				e1.printStackTrace();
			} catch (RemoteException e1) {
				setFormMessage("search_form", e1.getMessage() , request);
				e1.printStackTrace();
			}
		}
		else
		{
			/*
			 * Cerco i dati
			 */
				
			try {
				RecuperaListaTipologiaStrutturaRicettivaResponse res = null;
				RecuperaListaTipologiaStrutturaRicettivaRequest in = new RecuperaListaTipologiaStrutturaRicettivaRequest(); 
				in.setPageNumber(pageNumber);
				in.setRowsPerPage(rowsPerPage);
				//in.setOrder(order);	//TODO da considerare in successive implementazioni		
				in.setDescrizioneTipologiaStruttura(descrizionetipologiaStrutture);
				res = WSCache.impostaSoggiornoConfigServer.ricercaTipologiaStrutture(in, request);
				if (res.getPageInfo() != null)
				{
					if (res.getPageInfo().getNumRows() > 0)
					{
						PageInfo pageInfo = new PageInfo();
						pageInfo.setFirstRow(res.getPageInfo().getFirstRow());
						pageInfo.setLastRow(res.getPageInfo().getLastRow());
						pageInfo.setNumPages(res.getPageInfo().getNumPages());
						pageInfo.setNumRows(res.getPageInfo().getNumRows());
						pageInfo.setPageNumber(res.getPageInfo().getPageNumber());
						pageInfo.setRowsPerPage(rowsPerPage);

						request.setAttribute("listaConfig", res.getListXml());
						request.setAttribute("listaConfig.pageInfo", pageInfo);
					}
					else setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
				}
				else setFormMessage("search_form", Messages.NO_DATA_FOUND.format(), request);
			} catch (FaultType e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}			
		}
		return null;	
	}

}
