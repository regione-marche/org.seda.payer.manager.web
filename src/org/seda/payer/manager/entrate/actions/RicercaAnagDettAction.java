package org.seda.payer.manager.entrate.actions;

import java.rmi.RemoteException;

import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.bancadati.webservice.dati.Paginazione;
import com.seda.payer.bancadati.webservice.dati.RicercaDocumentiAnagraficaRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaDocumentiAnagraficaResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioAnagraficaRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioAnagraficaResponse;
//import com.seda.payer.bancadati.webservice.dati.EccedenzaType;
import com.seda.payer.bancadati.webservice.srv.FaultType;

public class RicercaAnagDettAction extends BaseEntrateAction {

	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {

		salvaStato(request);

		super.service(request);

		String exp="0";
        boolean actExp = false;
		

		switch(getFiredButton(request)) 
			{
			case TX_BUTTON_CERCA_EXP: 
				if (request.getParameter("ext") != null && request.getParameter("ext").equals("0"))
					exp = "1";
				else
					exp = "0";
				actExp = true;
				
			case TX_BUTTON_CERCA: 
				if (request.getAttribute("ext")==null)
					request.setAttribute("ext","0");

				if (request.getAttribute("ext")=="" || actExp)
					request.setAttribute("ext",exp);

				try 
					{
			         ricercaDettaglioAnagPage(request);
					}
				catch (FaultType fte) 
				{
					WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
					fte.printStackTrace();
					setFormMessage("ricercaDettaglioAnagForm", "errore: " + decodeMessaggio(fte), request);
				}
				catch (RemoteException af)
				{
					WSCache.logWriter.logError("errore: " + af.getMessage(),af);
					af.printStackTrace();
					setFormMessage("ricercaDettaglioAnagForm", testoErroreColl, request);					
				}
				catch (Exception e) 
				   {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					//setErrorMessage(request, e.getMessage());
					setFormMessage("ricercaDettaglioAnagForm", "errore: " + testoErrore, request);
					}
				break;
			}

		return null;
	}


    
    public void ricercaDettaglioAnag(HttpServletRequest request) throws Exception
    {
    	//ricerca dettaglio anagrafica
    	    	RicercaDettaglioAnagraficaResponse out;
    	    	RicercaDettaglioAnagraficaRequest in;
    			in = prepareRicercaDettaglioAnag(request);
    							
    			WSCache.logWriter.logDebug("Ricerca dettaglio anagrafica Entrate");
    			out =  WSCache.entrateBDServer.ricercaDettaglioAnagrafica(in, request);
    	 
    			request.setAttribute("dettaglioAnag", out.getListXml());
    }    

    public void ricercaDettaglioAnagPage(HttpServletRequest request) throws Exception
    {
    	ricercaDettaglioAnag(request);

		//ricerca dettaglio documenti
		
		RicercaDocumentiAnagraficaResponse out2;
		RicercaDocumentiAnagraficaRequest in2;
		in2 = prepareRicercaDocumentiAnag(request);
					
		WSCache.logWriter.logDebug("Ricerca Documenti anagrafica Entrate");
		out2 =  WSCache.entrateBDServer.ricercaDocumentiAnagrafica(in2, request);

		
		if(out2.getPInfo().getNumRows() > 0) 
		{
			request.setAttribute("documentiAnag", out2.getListXml());

			request.setAttribute("documentiAnag.pageInfo", getPageInfo(out2.getPInfo(), in2.getPagina().getRowsPerPage()));		
		}
		else
		{
			String messaggio="";
			if (request.getAttribute("messaggioRis")!=null)
				messaggio = request.getAttribute("messaggioRis").toString() + " - ";
			setFormMessage("ricercaDettaglioAnagForm", messaggio + Messages.NO_DATA_FOUND.format(), request);
		}
        		
    }
    
    
	private RicercaDettaglioAnagraficaRequest prepareRicercaDettaglioAnag(HttpServletRequest request)
	{

		RicercaDettaglioAnagraficaRequest ris = new RicercaDettaglioAnagraficaRequest();
		
		ris.setProgressivoFlusso(request.getParameter("chiaveAnaFlusso"));
        ris.setCodiceUtente(request.getParameter("chiaveCodUte"));
        ris.setDataCreazioneFlusso(request.getParameter("chiaveAnaDataCrea"));
        ris.setTipoServizio(request.getParameter("chiaveAnaServ"));
        ris.setCodiceEnte(request.getParameter("chiaveCodEnte"));      
        ris.setTipoUfficio(request.getParameter("chiaveTipoUff"));
        ris.setCodiceUfficio(request.getParameter("chiaveCodUff"));        
        ris.setImpostaServizio(request.getParameter("chiaveIS"));
        ris.setCodiceFiscale(request.getParameter("chiaveCodFisc"));
        ris.setCodiceTomb(request.getParameter("chiaveTomb"));

		return ris;		
	}
	
	
	private RicercaDocumentiAnagraficaRequest prepareRicercaDocumentiAnag(HttpServletRequest request)
	{
		PropertiesTree configuration; 
		
		RicercaDocumentiAnagraficaRequest ris = new RicercaDocumentiAnagraficaRequest();

		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

		int rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

//      if (request.getParameter("rowsPerPage")!=null)	
	      if (request.getAttribute("rowsPerPage")!=null&&!((String)request.getAttribute("rowsPerPage")).equals("")&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
	      {
	      	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
	      }
       
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();

		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
		ris.setProgressivoFlusso(request.getParameter("chiaveAnaFlusso"));
        ris.setCodiceUtente(request.getParameter("chiaveCodUte"));
        ris.setDataCreazioneFlusso(request.getParameter("chiaveAnaDataCrea"));
        ris.setTipoServizio(request.getParameter("chiaveAnaServ"));
        ris.setCodiceEnte(request.getParameter("chiaveCodEnte"));      
        ris.setTipoUfficio(request.getParameter("chiaveTipoUff"));
        ris.setCodiceUfficio(request.getParameter("chiaveCodUff"));        
        ris.setImpostaServizio(request.getParameter("chiaveIS"));
        ris.setCodiceFiscale(request.getParameter("chiaveCodFisc"));
        ris.setCodiceTomb(request.getParameter("chiaveTomb"));

		return ris;
		
		
		
	}
	
	
/*	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
		request.setAttribute("data_riversamento_da", getCalendar(request, "data_riversamento_da"));
		request.setAttribute("data_riversamento_a", getCalendar(request, "data_riversamento_a"));
	}
	
	private void requestData(HttpServletRequest request, String chiaveS, String chiaveD)
	{
		String data = request.getParameter(chiaveS);
		if (data != null && data.length()>0 && !data.contains("1000-01-01"))
		{
			data = data.substring(0,10);
			request.setAttribute(chiaveD, data.substring(8,10) + "/" + 
					data.substring(5,7) + "/" + data.substring(0,4));
		}
		else
			request.setAttribute(chiaveD, "");

//		java.util.Calendar cal = getCalendarS(request, data);		
//		request.setAttribute("c1d", cal);

	}
*/
}
