package org.seda.payer.manager.wisconfig.actions;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaConfigurazioneImpSoggRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaConfigurazioneImpSoggResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ImpostaSoggiornoConfigurazioneImpostaSearch extends BaseManagerAction {
	private static String codop = "search";
	String descrizioneComune = null;
	String order = null;
	int rowsPerPage = 0;
	int pageNumber = 0;
	//inizio LP PG1800XX_016
	String templateName = "";
	//fine LP PG1800XX_016
	
	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		//inizio LP PG1800XX_016
		MAFRequest mafReq = new MAFRequest(request);
		String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
		UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		templateName = userBean.getTemplate(applicationName);
		//fine LP PG1800XX_016
		/*
		 * Resetto l'eventuale messaggio rimasto nel viewstate
		 */
		//resetFormMessage(request);
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
		
		//inizio LP PG1800XX_016
		if (templateName.equals("sassari"))
		{
			request.setAttribute("descrizioneComune","SASSARI");
		}
		//fine LP PG1800XX_016
		/*
		 * Setto l'attributo relativo all'operazione
		 */
		request.setAttribute("codop",codop);
		/*
		 * Cerco i dati
		 */
				descrizioneComune = ((String)request.getAttribute("descrizioneComune") == null ? "" : (String)request.getAttribute("descrizioneComune"));
				rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
				pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
				//order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
			try {
				RecuperaListaConfigurazioneImpSoggResponse res = null;
				RecuperaListaConfigurazioneImpSoggRequest in = new RecuperaListaConfigurazioneImpSoggRequest();
				in.setPageNumber(pageNumber);
				in.setRowsPerPage(rowsPerPage);
				//in.setOrder(order);	//TODO da considerare in successive implementazioni		
				in.setDescrizioneComune(descrizioneComune);
				res = WSCache.impostaSoggiornoConfigServer.ricercaConfigurazioneImposta(in, request);
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
		return null;	
	}

}
