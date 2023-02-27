package org.seda.payer.manager.wisconfig.actions;

import java.rmi.RemoteException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaAnagraficaStrutturaCsvRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaAnagraficaStrutturaCsvResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaAnagraficaStrutturaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaAnagraficaStrutturaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTipologiaStrutturaRicettivaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTipologiaStrutturaRicettivaResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.AnagraficaStrutturaRicettiva;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ImpostaSoggiornoAnagStrutturaSearchAction extends BaseManagerAction {
	private static String codop = "search";
	String descrizioneComune = null;
	String numeroAutorizzazione = null;
	String codFiscale = null;
	String descrizioneInsegna = null;
	String strutturaRicettiva = null;
	String order = null;
	int rowsPerPage = 0;
	int pageNumber = 0;
	
	//PG170240
	String flagSubentro = null;
	Calendar dataValiditaInizio = null;
	Calendar dataValiditaFine = null;
	Calendar dataObbligoComunicazioneInizio = null;
	Calendar dataObbligoComunicazioneFine = null;
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
		loadListaTipoStrutture(request);	
		
		descrizioneComune = ((String)request.getAttribute("descrizioneComune") == null ? "" : (String)request.getAttribute("descrizioneComune"));
		numeroAutorizzazione = ((String)request.getAttribute("numeroAutorizzazione") == null ? "" : (String)request.getAttribute("numeroAutorizzazione"));
//		codFiscale = ((String)request.getAttribute("codFiscale") == null ? "" : (String)request.getAttribute("codFiscale"));
		codFiscale = ((String)request.getAttribute("codiceFiscale") == null ? "" : (String)request.getAttribute("codiceFiscale"));
		descrizioneInsegna = ((String)request.getAttribute("descrizioneInsegna") == null ? "" : (String)request.getAttribute("descrizioneInsegna"));
		strutturaRicettiva = ((String)request.getAttribute("strutturaRicettiva") == null ? "" : (String)request.getAttribute("strutturaRicettiva"));
		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		//order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");

		//PG170240
		flagSubentro = ((String)request.getAttribute("flagSubentro") == null ? "" : (String)request.getAttribute("flagSubentro"));
		dataValiditaInizio = (request.getAttribute("tx_data_da") == null ? null : (Calendar)request.getAttribute("tx_data_da"));
		dataValiditaFine = (request.getAttribute("tx_data_a") == null ? null : (Calendar)request.getAttribute("tx_data_a"));
		dataObbligoComunicazioneInizio = (request.getAttribute("tx_data_obbl_da") == null ? null : (Calendar)request.getAttribute("tx_data_obbl_da"));
		dataObbligoComunicazioneFine = (request.getAttribute("tx_data_obbl_a") == null ? null : (Calendar)request.getAttribute("tx_data_obbl_a"));

		
		if (firedButton.equals(FiredButton.TX_BUTTON_DOWNLOAD))
		{
			String file="";
			try {
				RecuperaAnagraficaStrutturaCsvRequest reqCsv = new RecuperaAnagraficaStrutturaCsvRequest();
				reqCsv.setDescrizioneComune(descrizioneComune);
				AnagraficaStrutturaRicettiva anagraficaStrutturaRicettiva = getAnagraficaStrutturaRicerca();
				reqCsv.setAnagraficaStruttura(anagraficaStrutturaRicettiva);
				RecuperaAnagraficaStrutturaCsvResponse resCsv = WSCache.impostaSoggiornoConfigServer.recuperaAnagraficaStrutturaCsv(reqCsv, request);
				if (resCsv != null && resCsv.getResponse() != null && resCsv.getResponse().getRetCode().getValue().equals("00"))
				{
					file = resCsv.getStringaCsv();
					//aggiustamento carattere \r perso nel webservice 
					file = file.replaceAll("\n", "\r\n");
					Calendar cal = Calendar.getInstance();
					String timestamp = GenericsDateNumbers.formatCalendarData(cal, "yyyyMMdd_HHmmss");
					request.setAttribute("filename",timestamp + "_AanagraficaStruttureRicettive.csv");
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
			try {
				RecuperaListaAnagraficaStrutturaResponse res = null;
				RecuperaListaAnagraficaStrutturaRequest in = new RecuperaListaAnagraficaStrutturaRequest();
				in.setPageNumber(pageNumber);
				in.setRowsPerPage(rowsPerPage);
				//in.setOrder(order);			
				in.setDescrizioneComune(descrizioneComune);
				AnagraficaStrutturaRicettiva anagraficaStrutturaRicettiva = getAnagraficaStrutturaRicerca();
				in.setAnagraficaStruttura(anagraficaStrutturaRicettiva);
				res = WSCache.impostaSoggiornoConfigServer.ricercaAnagraficheStrutture(in, request);
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
	
	private AnagraficaStrutturaRicettiva getAnagraficaStrutturaRicerca()
	{
		return new AnagraficaStrutturaRicettiva("", "", numeroAutorizzazione,codFiscale,"",descrizioneInsegna,"","","","",strutturaRicettiva,"","","","","","",null,"",""
				,flagSubentro, dataValiditaInizio, dataValiditaFine, dataObbligoComunicazioneInizio, dataObbligoComunicazioneFine, //PG170240
				"",null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);	//PG180170	
		//PG160040 GG 19052016
	}
	
	private void loadListaTipoStrutture(HttpServletRequest request)
	{
		RecuperaListaTipologiaStrutturaRicettivaResponse res = new RecuperaListaTipologiaStrutturaRicettivaResponse();
		RecuperaListaTipologiaStrutturaRicettivaRequest in = new RecuperaListaTipologiaStrutturaRicettivaRequest();
		in.setDescrizioneTipologiaStruttura("");
		try {
			res = WSCache.impostaSoggiornoConfigServer.ricercaTipologiaStrutture(in, request);
		} catch (com.seda.payer.impostasoggiorno.webservice.srv.FaultType e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		request.setAttribute("listStrutture", res.getListXml());
	}


}
