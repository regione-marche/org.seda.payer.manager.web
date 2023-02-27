package org.seda.payer.manager.ruoli.actions;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.bancadati.webservice.srv.FaultType;
import com.seda.payer.bancadati.webservice.dati.Paginazione;
import com.seda.payer.bancadati.webservice.dati.RecuperaDocumentiRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDocumentiResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaArticoliRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaArticoliResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaEmissioniRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaEmissioniResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaPagamentiDettRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaPagamentiDettResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaPartiteRuoliRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaPartiteRuoliResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaRuoliRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaRuoliResponse;

public class RicercaPagamentiDettAction extends BaseRuoliAction {

	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		salvaStato(request);

		super.service(request);

		HttpSession session = request.getSession();
		
		switch(getFiredButton(request)) 
			{
			case TX_BUTTON_CERCA:
			break;
			case TX_BUTTON_CERCA_EXP: 
			try {
					ricercaPagamenti(request);
				}
				catch (FaultType fte) 
				{
					WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
					fte.printStackTrace();
					setFormMessage("ricercaArticoliForm", "errore: " + decodeMessaggio(fte), request);
				}
				catch (RemoteException af)
				{
					WSCache.logWriter.logError("errore: " + af.getMessage(),af);
					af.printStackTrace();
					setFormMessage("ricercaArticoliForm", testoErroreColl, request);					
				}
				catch (Exception e) {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					e.printStackTrace();
					setFormMessage("ricercaArticoliForm", "errore ricerca: " + testoErrore, request);
				}
				break;
			}
		
		super.service(request);

		return null;
	}

    public void ricercaPagamenti(HttpServletRequest request) throws Exception
    {
    	
			RicercaPagamentiDettResponse out;
			RicercaPagamentiDettRequest in;
			in = prepareRicerca(request);
						
			WSCache.logWriter.logDebug("Pagina interrogazione di test");
			out =  WSCache.ruoliBDServer.ricercaPagamentiDett(in, request);
			
			if(out.getPInfo().getNumRows() > 0) 
			{
				request.setAttribute("listaPagamenti", out.getListXml());

				request.setAttribute("listaPagamenti.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));		

			}
			else
			{
				String messaggio="";
				if (request.getAttribute("messaggioRis")!=null)
					messaggio = request.getAttribute("messaggioRis").toString() + " - ";
				setFormMessage("ricercaPagamentiForm", messaggio + Messages.NO_DATA_FOUND.format(), request);
			}
		
	
    }

	
	private RicercaPagamentiDettRequest prepareRicerca(HttpServletRequest request)
	{
		PropertiesTree configuration; 
		RicercaPagamentiDettRequest ris;

		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

		int rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

//      if (request.getParameter("rowsPerPage")!=null)	
	      if (request.getAttribute("rowsPerPage")!=null&&!((String)request.getAttribute("rowsPerPage")).equals("")&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
	      {
	      	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
	      }
       
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();

		ris = new RicercaPagamentiDettRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
		//&tx_button_cerca=OK&chiaveCodSoc={1}&chiaveCodUte={2}&chiaveCodEnte={18}&chiaveAnnoRuo={4}&chiaveNumRuo={5}&chiaveFlusso={15}&chiaveConc={16}&chiaveTomb={17}&rRowsPerPage=${param.rowsPerPage}&rPageNumber=${param.pageNumber}&rOrder=${param.order}
		
		ris.setCodiceSocieta(request.getParameter("chiaveCodSoc"));
        ris.setCodiceUtente(request.getParameter("chiaveCodUte"));
        ris.setCodiceEnte(request.getParameter("chiaveCodEnte"));

        ris.setAnnoRuolo(request.getParameter("chiaveAnnoRuo"));
        ris.setNumeroRuolo(request.getParameter("chiaveNumRuo"));
        
        ris.setProgrFlussoRuolo(request.getParameter("chiaveFlusso"));
        ris.setCodiceConcessione(request.getParameter("chiaveConcPar"));
        
		ris.setProgrPartita(request.getParameter("chiaveNumPar"));

        return ris;
		
	}

	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
	}

	

}
