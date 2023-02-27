package org.seda.payer.manager.configurazione.actions;


import java.rmi.RemoteException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.DispatchHtmlAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.CodiceFiscalePIVA;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.FiltriEstrattoContoDeleteRequest;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.FiltriEstrattoContoDetailRequest;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.FiltriEstrattoContoDetailResponse;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.FiltriEstrattoContoSaveRequest;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.FiltriEstrattoContoUpdateRequest;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.FiltroEstrattoConto;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.RecuperaListaEntiDdlRequest;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.RecuperaListaEntiDdlResponse;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.RecuperaListaFiltriEstrattoContoXmlRequest;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.RecuperaListaFiltriEstrattoContoXmlResponse;
import com.seda.payer.pgec.webservice.filtriestrattoconto.dati.StatusResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

public class FiltriEstrattoContoAction extends DispatchHtmlAction {

	private static final long serialVersionUID = 1L;
	private int rowsPerPage;
	private int pageNumber;
	UserBean user = null;
	
	@Override
	public void start(HttpServletRequest request) {
		rowsPerPage = (request.getParameter("rowsPerPage") == null) 
				   ? getDefaultListRows() : Integer.parseInt(request.getParameter("rowsPerPage"));
		pageNumber = (request.getParameter("pageNumber") == null) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		
		HttpSession session = request.getSession();
		user = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);

		//Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		replyAttributes(false, request,"pageNumber","rowsPerPage","order");

		
		String firedButton = (String)request.getAttribute("tx_button_nuovo");

		if (firedButton!=null){
			if(firedButton.equals("Nuovo")){
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
		firedButton = (String)request.getAttribute("tx_button_delete");
		if (firedButton!=null){
			if(firedButton.equals("Cancella")){
				try {
					cancel(request);
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
			//Giulia 25032013--
//			loadDdlEntiRicerca(request);
			
			String firedButtonReset = (String)request.getAttribute("tx_button_reset");
			if (firedButtonReset!=null){				
				resetParametri(request);				
			}
			
			String codiceFiscale = ((String)request.getAttribute("fec_codiceFiscale_ric") == null ? "" :  (String)request.getAttribute("fec_codiceFiscale_ric"));
			//Giulia 23032013--inizio
//			String ddlSocUteEnt = ((String)request.getAttribute("fec_ddlSocUteEnt_ric") == null ? "" :  (String)request.getAttribute("fec_ddlSocUteEnt_ric"));
			String chiaveEnte = ((String)request.getAttribute("fec_ente_ric") == null ? "" :  (String)request.getAttribute("fec_ente_ric"));
			String codiceSocieta = "";
			String codiceUtente = "";
			//String chiaveEnte = "";
//			if (ddlSocUteEnt.length() > 0)
//			{
//				String[] aSocUteEnt = ddlSocUteEnt.split("\\|");
//				if (aSocUteEnt.length == 3)
//				{
//					codiceSocieta = aSocUteEnt[0];
//					codiceUtente= aSocUteEnt[1];
//					chiaveEnte= aSocUteEnt[2];
//				}
//			}
			//Giulia 23032013--fine
			String impostaServizio = ((String)request.getAttribute("fec_impostaServizio_ric") == null ? "" :  (String)request.getAttribute("fec_impostaServizio_ric"));
			String numeroEmissione = ((String)request.getAttribute("fec_numeroEmissione_ric") == null ? "" :  (String)request.getAttribute("fec_numeroEmissione_ric"));
			String numeroDocumento = ((String)request.getAttribute("fec_numeroDocumento_ric") == null ? "" :  (String)request.getAttribute("fec_numeroDocumento_ric"));
			
			RecuperaListaFiltriEstrattoContoXmlResponse searchResponse = getFiltriEstrattoContoSearchResponse(codiceFiscale, 
					codiceSocieta, codiceUtente, chiaveEnte, impostaServizio, numeroEmissione, numeroDocumento, 
					rowsPerPage, pageNumber, request);
			
			
			
			
			
			if (searchResponse != null && searchResponse.getRetCode().equals("00"))
			{
				PageInfo pageInfo = new PageInfo(); 
				pageInfo.setFirstRow(searchResponse.getPageInfo().getFirstRow());
				pageInfo.setLastRow(searchResponse.getPageInfo().getLastRow());
				pageInfo.setNumPages(searchResponse.getPageInfo().getNumPages());
				pageInfo.setNumRows(searchResponse.getPageInfo().getNumRows());
				pageInfo.setPageNumber(searchResponse.getPageInfo().getPageNumber());
				pageInfo.setRowsPerPage(rowsPerPage);
				
				request.setAttribute("listFiltriEstrattoConto", searchResponse.getListaFiltriEstrattoContoXml());
				request.setAttribute("listFiltriEstrattoConto.pageInfo", pageInfo);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	public Object add(HttpServletRequest request) throws ActionException {
		try {
			//Giulia 25032013
			//loadDdlEntiNuovoModifica(request);
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return null;
	}
	
	public Object richiestacanc(HttpServletRequest request) {	  
		request.setAttribute("richiestacanc", request.getParameter("richiestacanc"));
		request.setAttribute("fec_chiaveFiltroEstrattoConto",request.getParameter("fec_chiaveFiltroEstrattoConto"));		  
		return null; 
	}
	

	public Object edit(HttpServletRequest request) throws ActionException {
		try {
			
			//Giulia 25032013
			//loadDdlEntiNuovoModifica(request);
			
			String chiaveFiltroEstrattoConto = (String)request.getAttribute("fec_chiaveFiltroEstrattoConto");
			
			FiltriEstrattoContoDetailResponse res = getFiltroEstrattoContoDetailResponse(chiaveFiltroEstrattoConto, request);
			if (res != null && res.getRetCode().equals("00") && res.getFiltroEstrattoConto() != null)
			{
				FiltroEstrattoConto fec = res.getFiltroEstrattoConto();
				
				String codiceFiscale = fec.getCodiceFiscale().trim();
				String impostaServizio = fec.getCodiceImpostaServizio().trim();
				String numeroEmissione = fec.getNumeroEmissione().trim();
				String numeroDocumento = fec.getNumeroDocumento().trim();
				//Giulia 250313
				String ente = fec.getChiaveEnte().trim();
				request.setAttribute("fec_chiaveFiltroEstrattoConto", fec.getChiaveFiltroEstrattoConto());
				request.setAttribute("fec_codiceFiscale", codiceFiscale.equals("*") ? "" : codiceFiscale);
				request.setAttribute("fec_ente_ric", ente);
				
				//Giulia 250313
//				if (!fec.getChiaveEnte().trim().equals("*"))
//					request.setAttribute("fec_ddlSocUteEnt", fec.getCodiceSocieta() + "|" + fec.getCodiceUtente() + "|" + fec.getChiaveEnte());
//				else
//					request.setAttribute("fec_ddlSocUteEnt", "");
				
				request.setAttribute("fec_impostaServizio", impostaServizio.equals("*") ? "" : impostaServizio);
				request.setAttribute("fec_numeroEmissione", numeroEmissione.equals("*") ? "" : numeroEmissione);
				request.setAttribute("fec_numeroDocumento", numeroDocumento.equals("*") ? "" : numeroDocumento);
			}
			
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
					index(request);					
				}
			}
			else if(firedButtonReset != null){
				if(firedButtonReset.equals("Reset")){					
					resetParametri(request);
					request.setAttribute("action", "add");
					add(request);
				}
			}
			else 
				save(request);
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
					index(request);					
				}
			}
			else if(firedButtonReset!=null){
				if(firedButtonReset.equals("Reset")){
					request.setAttribute("action", "edit");
					edit(request);					
				}
			}
			else 
				save(request);
		} catch (Exception ignore) { // errore gestito dalla catch del metodo save
		}
		return null;
	}
	
	//Giulia 250313--inizio
//	private void loadDdlEntiRicerca(HttpServletRequest request)
//	{
//		try
//		{
//			RecuperaListaEntiDdlRequest req = new RecuperaListaEntiDdlRequest();
//			req.setFormSearch("Y");
//			RecuperaListaEntiDdlResponse res = WSCache.filtriEstrattoContoServer.recuperaListEntiDdl(req, request);
//			if (res != null && res.getRetCode().equals("00"))
//				request.setAttribute("listaSocietaUtenteEnteSearch", res.getListaEntiXml());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
//	private void loadDdlEntiNuovoModifica(HttpServletRequest request)
//	{
//		try
//		{
//			RecuperaListaEntiDdlRequest req = new RecuperaListaEntiDdlRequest();
//			req.setFormSearch("N");
//			RecuperaListaEntiDdlResponse res = WSCache.filtriEstrattoContoServer.recuperaListEntiDdl(req, request);
//			if (res != null && res.getRetCode().equals("00"))
//				request.setAttribute("listaSocietaUtenteEnteNewEdit", res.getListaEntiXml());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	//Giulia 250313--fine
	private void save(HttpServletRequest request) throws ActionException 
	{
		String codOp = request.getParameter("codop"); //codice operativo da passare al dao(add o edit)
		request.setAttribute("done", codOp); // imposta done !=null per la jsp di var
		request.setAttribute("error", null);
		
		String codiceFiscale = ((String)request.getAttribute("fec_codiceFiscale") == null ? "" :  ((String)request.getAttribute("fec_codiceFiscale")).toUpperCase());
//		String ddlSocUteEnt = ((String)request.getAttribute("fec_ddlSocUteEnt") == null ? "" :  (String)request.getAttribute("fec_ddlSocUteEnt"));
		String chiaveEnte = ((String)request.getAttribute("fec_ente_ric") == null ? "" :  (String)request.getAttribute("fec_ente_ric"));
		String codiceSocieta = "";
		String codiceUtente = "";
		//Giulia
		//String chiaveEnte = "";
//		if (ddlSocUteEnt.length() > 0)
//		{
//			String[] aSocUteEnt = ddlSocUteEnt.split("\\|");
//			if (aSocUteEnt.length == 3)
//			{
//				codiceSocieta = aSocUteEnt[0];
//				codiceUtente= aSocUteEnt[1];
//				chiaveEnte= aSocUteEnt[2];
//			}
//		}
		String impostaServizio = ((String)request.getAttribute("fec_impostaServizio") == null ? "" :  (String)request.getAttribute("fec_impostaServizio"));
		String numeroEmissione = ((String)request.getAttribute("fec_numeroEmissione") == null ? "" :  (String)request.getAttribute("fec_numeroEmissione"));
		String numeroDocumento = ((String)request.getAttribute("fec_numeroDocumento") == null ? "" :  (String)request.getAttribute("fec_numeroDocumento"));
		
		
		if (checkForm(codiceFiscale, request))
		{
			try {
				
				FiltroEstrattoConto fec = new FiltroEstrattoConto();
				fec.setCodiceFiscale(codiceFiscale.length() == 0 ? "*" : codiceFiscale);
				fec.setCodiceSocieta(codiceSocieta.length() == 0 ? "*" : codiceSocieta);
				fec.setCodiceUtente(codiceUtente.length() == 0 ? "*" : codiceUtente);
				fec.setChiaveEnte(chiaveEnte.length() == 0 ? "*" : chiaveEnte);
				fec.setCodiceImpostaServizio(impostaServizio.length() == 0 ? "*" : impostaServizio);
				fec.setNumeroEmissione(numeroEmissione.length() == 0 ? "*" : numeroEmissione);
				fec.setNumeroDocumento(numeroDocumento.length() == 0 ? "*" : numeroDocumento);
				fec.setOperatoreUltimoAggiornamento(user.getUserName().trim());
				
				StatusResponse res = null;
				if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) 
				{
					FiltriEstrattoContoSaveRequest req = new FiltriEstrattoContoSaveRequest();
					req.setFiltroEstrattoConto(fec);
					res = WSCache.filtriEstrattoContoServer.save(req, request);
				}
				else
				{
					FiltriEstrattoContoUpdateRequest req = new FiltriEstrattoContoUpdateRequest();
					String chiaveFiltroEstrattoConto = ((String)request.getAttribute("fec_chiaveFiltroEstrattoConto"));
					fec.setChiaveFiltroEstrattoConto(chiaveFiltroEstrattoConto);
					req.setFiltroEstrattoConto(fec);
					res = WSCache.filtriEstrattoContoServer.update(req, request);
				}
				
				if (res != null)
				{
					if (res.getRetCode().equals("00"))
					{
						request.setAttribute("done", codOp);
						request.setAttribute("error", null);
					}
					else if (res.getRetCode().equals("02"))
					{
						//stampo il messaggio proveniente dal facade
						request.setAttribute("done", null);
						//request.setAttribute("error", res.getRetMessage());
						setFormMessage("frmAction", res.getRetMessage(), request);
						//Giulia 250313
						//loadDdlEntiNuovoModifica(request);
					}
				}
	
			} catch (Exception e) {
		
				if (codOp.compareTo(TypeRequest.ADD_SCOPE.scope())==0) 
					request.setAttribute("message", Messages.INS_ERRD.format());
				if (codOp.compareTo(TypeRequest.EDIT_SCOPE.scope())==0) 
					request.setAttribute("message", Messages.UPDT_ERR.format());
				request.setAttribute("error", "error"); 
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	    else
	    {
	    	try {
	    		//Giulia 250313
	    		//loadDdlEntiNuovoModifica(request);
	    	} catch (Exception e){ e.printStackTrace(); }
	    	request.setAttribute("done", null);
	    }
	}
	
	private boolean checkForm(String codiceFiscale, HttpServletRequest request)
	{
		String sRes = CodiceFiscalePIVA.controlloCodiceFiscale_PartitaIva_NomeEmpty(codiceFiscale, "Cod. Fiscale");
		if (sRes != null && sRes.length() > 0)
		{
			setFormMessage("frmAction", sRes, request);
			return false;
		}

		return true;
	}
	
	public Object cancel(HttpServletRequest request) throws ActionException {
		String chiaveFiltroEstrattoConto = request.getParameter("fec_chiaveFiltroEstrattoConto");
		try {
			request.setAttribute("varname", "filtriec");
			FiltriEstrattoContoDeleteRequest req = new FiltriEstrattoContoDeleteRequest();
			req.setChiaveFiltroEstrattoConto(chiaveFiltroEstrattoConto);
			StatusResponse res = WSCache.filtriEstrattoContoServer.delete(req, request);
			if (res != null && res.getRetCode().equals("00"))
				request.setAttribute("message", Messages.CANC_OK.format());
			else
				request.setAttribute("message", Messages.CANCEL_ERR.format());
			
			request.setAttribute("action", "cancel");
		} catch (FaultType f) {
			request.setAttribute("message", Messages.valueOf("ERR_" + f.getCode()).format());
		} catch (Exception e) {
			request.setAttribute("message", Messages.CANCEL_ERRDIP.format());
		}
		return null;
	}	

	
	protected int getDefaultListRows()
	{
		int defaultListRows = 4;
		PropertiesTree configuration = (PropertiesTree)context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		if(configuration != null)
		{
			String s_defaultListRows = configuration.getProperty(PropertiesPath.defaultListRows.format());
			if (s_defaultListRows != null) defaultListRows = Integer.parseInt(s_defaultListRows);
		}
		return defaultListRows;
	}
	
	private RecuperaListaFiltriEstrattoContoXmlResponse getFiltriEstrattoContoSearchResponse (String codiceFiscale, 
			String codiceSocieta, String codiceUtente, String chiaveEnte, String impostaServizio, String numeroEmissione,
			String numeroDocumento, int rowsPerPage, int pageNumber, HttpServletRequest request) throws FaultType, RemoteException
	{
		RecuperaListaFiltriEstrattoContoXmlResponse res = null;
		RecuperaListaFiltriEstrattoContoXmlRequest req = new RecuperaListaFiltriEstrattoContoXmlRequest();
		req.setCodiceFiscale(codiceFiscale);
		req.setCodiceSocieta(codiceSocieta);
		req.setCodiceUtente(codiceUtente);
		req.setChiaveEnte(chiaveEnte);
		req.setCodiceImpostaServizio(impostaServizio);
		req.setNumeroEmissione(numeroEmissione);
		req.setNumeroDocumento(numeroDocumento);
		req.setRowsPerPage(rowsPerPage < 0 ? 0 : rowsPerPage);
		req.setPageNumber(pageNumber < 0 ? 0 : pageNumber);
		
		res = WSCache.filtriEstrattoContoServer.recuperaListaFiltriEstrattoContoXml(req, request);
		return res;
	}
	
	
	private FiltriEstrattoContoDetailResponse getFiltroEstrattoContoDetailResponse(String chiaveFiltroEstrattoConto,HttpServletRequest request) throws FaultType, RemoteException
	{
		FiltriEstrattoContoDetailRequest req = new FiltriEstrattoContoDetailRequest();
		req.setChiaveFiltroEstrattoConto(chiaveFiltroEstrattoConto);
		FiltriEstrattoContoDetailResponse res = WSCache.filtriEstrattoContoServer.recuperaFiltroEstrattoConto(req, request);
		return res;
	}
	
	@SuppressWarnings("unchecked")
	protected void resetParametri(HttpServletRequest request) {
		Enumeration e = request.getParameterNames();
		String p = "";
		while (e.hasMoreElements()) {
			
			p = (String) e.nextElement();
			request.setAttribute(p, "");
		}
		return;
	}

}
