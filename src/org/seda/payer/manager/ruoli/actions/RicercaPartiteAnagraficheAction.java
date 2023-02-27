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
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioSAnagraficaRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioSAnagraficaResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaPartiteAnagraficheRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaPartiteAnagraficheResponse;

public class RicercaPartiteAnagraficheAction extends BaseRuoliAction {

	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		salvaStato(request);

		super.service(request);

		HttpSession session = request.getSession();
		
		String exp="0";
        boolean actExp = false;

		switch(getFiredButton(request)) 
			{
			case TX_BUTTON_NULL: 
				resetDDLSession(request);
				break;
			/*
			case TX_BUTTON_CERCA_EXP: 
				if (request.getParameter("ext") != null && request.getParameter("ext").equals("0"))
					exp = "1";
				else
					exp = "0";
				actExp = true;
			*/
			case TX_BUTTON_CERCA: 
			try {
					ricercaDettaglioXmlAnagrafica(request);
					ricercaPartite(request);
				}
				catch (FaultType fte) 
				{
					WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
					fte.printStackTrace();
					setFormMessage("ricercaPartiteAnagraficheForm", "errore: " + decodeMessaggio(fte), request);
				}
				catch (RemoteException af)
				{
					WSCache.logWriter.logError("errore: " + af.getMessage(),af);
					af.printStackTrace();
					setFormMessage("ricercaPartiteAnagraficheForm", testoErroreColl, request);					
				}
				catch (Exception e) {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					e.printStackTrace();
					setFormMessage("ricercaPartiteAnagraficheForm", "errore ricerca: " + testoErrore, request);
				}
				break;
			}
		
		super.service(request);

		return null;
	}

    public void ricercaPartite(HttpServletRequest request) throws Exception
    {
    	
			RicercaPartiteAnagraficheResponse out;
			RicercaPartiteAnagraficheRequest in;
			in = prepareRicerca(request);
						
			WSCache.logWriter.logDebug("Pagina interrogazione di test");
			out =  WSCache.ruoliBDServer.ricercaPartiteAnagrafiche(in, request);
			
			if(out.getPInfo().getNumRows() > 0) 
			{
				request.setAttribute("listaPartite", out.getListXml());

				request.setAttribute("listaPartite.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));		

			}
			else
			{
				String messaggio="";
				if (request.getAttribute("messaggioRis")!=null)
					messaggio = request.getAttribute("messaggioRis").toString() + " - ";
				setFormMessage("ricercaPartiteRuoliForm", messaggio + Messages.NO_DATA_FOUND.format(), request);
			}
		
	
    }

	
	private RicercaPartiteAnagraficheRequest prepareRicerca(HttpServletRequest request)
	{
		PropertiesTree configuration; 
		RicercaPartiteAnagraficheRequest ris;

		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

		int rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

//      if (request.getParameter("rowsPerPage")!=null)	
	      if (request.getAttribute("rowsPerPage")!=null&&!((String)request.getAttribute("rowsPerPage")).equals("")&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
	      {
	      	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
	      }
       
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();

		ris = new RicercaPartiteAnagraficheRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
		//&tx_button_cerca=OK&chiaveCodSoc={1}&chiaveCodUte={2}&chiaveCodEnte={18}&chiaveAnnoRuo={4}&chiaveNumRuo={5}&chiaveFlusso={15}&chiaveConc={16}&chiaveTomb={17}&rRowsPerPage=${param.rowsPerPage}&rPageNumber=${param.pageNumber}&rOrder=${param.order}
		
		ris.setCodiceSocieta(request.getParameter("chiaveCodSoc"));
        ris.setCodiceUtente(request.getParameter("chiaveCodUte"));
        ris.setCodiceEnte(request.getParameter("chiaveCodEnte"));

        ris.setCodiceTombRuolo(request.getParameter("chiaveTomb"));
        ris.setProgrFlussoRuolo(request.getParameter("chiaveFlusso"));
        ris.setCodiceConcessione(request.getParameter("chiaveConc"));
        
        ris.setCodiceFiscale(request.getParameter("chiaveCodFisc"));

        return ris;
		
	}

	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
		/*
		 * Salvo le date che richiedono l'utilizzo di un tipo "Calendar"
		 */
	}

    private RicercaDettaglioSAnagraficaRequest prepareRicercaDettaglioS(HttpServletRequest request)
	{

		RicercaDettaglioSAnagraficaRequest rec = new RicercaDettaglioSAnagraficaRequest();
		
		rec.setCodiceUtente(request.getParameter("chiaveCodUte"));
		rec.setCodiceEnte(request.getParameter("chiaveCodEnte"));
		rec.setCodiceSocieta(request.getParameter("chiaveCodSoc"));
		rec.setProgrFlussoRuolo(request.getParameter("chiaveFlusso"));
		rec.setCodiceConcessione(request.getParameter("chiaveConc"));
		rec.setCodiceTombRuolo(request.getParameter("chiaveTomb"));
		rec.setCodiceFiscale(request.getParameter("chiaveCodFisc"));

		return rec;
		
	}
    
    public void ricercaDettaglioXmlAnagrafica(HttpServletRequest request) throws Exception
    {
    	RicercaDettaglioSAnagraficaResponse out;
    	RicercaDettaglioSAnagraficaRequest in;
		in = prepareRicercaDettaglioS(request);
						
		WSCache.logWriter.logDebug("Pagina dettaglio anagrafica");
		out =  WSCache.ruoliBDServer.ricercaDettaglioXmlAnagrafica(in, request); //.//RecuperaD(in);
        
		request.setAttribute("dettaglioAnag", out.getListXml());
    }

	

}
