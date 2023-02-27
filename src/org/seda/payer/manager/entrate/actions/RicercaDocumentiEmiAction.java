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
import com.seda.payer.bancadati.webservice.dati.RicercaDocumentiEmissioneRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaDocumentiEmissioneResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioEmissioneSRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioEmissioneSResponse;
//import com.seda.payer.bancadati.webservice.dati.EccedenzaType;
import com.seda.payer.bancadati.webservice.srv.FaultType;


public class RicercaDocumentiEmiAction extends BaseEntrateAction {

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
			         ricercaDettaglioEmiPage(request);
					}
				catch (FaultType fte) 
				{
					WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
					fte.printStackTrace();
					setFormMessage("ricercaDettaglioEmiForm", "errore: " + decodeMessaggio(fte), request);
				}
				catch (RemoteException af)
				{
					WSCache.logWriter.logError("errore: " + af.getMessage(),af);
					af.printStackTrace();
					setFormMessage("ricercaDettaglioEmiForm", testoErroreColl, request);					
				}
				catch (Exception e) 
				   {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					//setErrorMessage(request, e.getMessage());
					setFormMessage("ricercaDettaglioEmiForm", "errore: " + testoErrore, request);
					}
				break;
			}

		return null;
	}


    
    public void ricercaDettaglioEmi(HttpServletRequest request) throws Exception
    {
//ricerca dettaglio anagrafica
    	RicercaDettaglioEmissioneSResponse out;
    	RicercaDettaglioEmissioneSRequest in;
		in = prepareRicercaDettaglioEmi(request);
						
		WSCache.logWriter.logDebug("Ricerca dettaglio emissione small Entrate");
		out =  WSCache.entrateBDServer.ricercaDettaglioEmissioneS(in, request);
 
		request.setAttribute("dettaglioEmi", out.getListXml());
    }

    public void ricercaDettaglioEmiPage(HttpServletRequest request) throws Exception
    {
//ricerca dettaglio anagrafica
    	ricercaDettaglioEmi(request);

		//ricerca dettaglio documenti
		
		RicercaDocumentiEmissioneResponse out2;
		RicercaDocumentiEmissioneRequest in2;
		in2 = prepareRicercaDocumentiEmi(request);
					
		WSCache.logWriter.logDebug("Ricerca Documenti emissione Entrate");
		out2 =  WSCache.entrateBDServer.ricercaDocumentiEmissione(in2, request);

		
		if(out2.getPInfo().getNumRows() > 0) 
		{
			request.setAttribute("documentiEmi", out2.getListXml());

			request.setAttribute("documentiEmi.pageInfo", getPageInfo(out2.getPInfo(), in2.getPagina().getRowsPerPage()));		
		}
		else
		{
			String messaggio="";
			if (request.getAttribute("messaggioRis")!=null)
				messaggio = request.getAttribute("messaggioRis").toString() + " - ";
			setFormMessage("ricercaDettaglioEmiForm", messaggio + Messages.NO_DATA_FOUND.format(), request);
		}
        		
    }
    
    
	private RicercaDettaglioEmissioneSRequest prepareRicercaDettaglioEmi(HttpServletRequest request)
	{

		RicercaDettaglioEmissioneSRequest ris = new RicercaDettaglioEmissioneSRequest();
		
        ris.setCodiceUtente(request.getParameter("chiaveCodUte"));
        ris.setCodiceEnte(request.getParameter("chiaveCodEnte"));      
        ris.setTipoUfficio(request.getParameter("chiaveTipoUff"));
        ris.setCodiceUfficio(request.getParameter("chiaveCodUff"));        
        ris.setImpostaServizio(request.getParameter("chiaveIS"));
        ris.setAnnoEmissione(request.getParameter("chiaveAnnoE"));	//nuova
        ris.setNumeroEmissione(request.getParameter("chiaveNumeroE")); //nuova
        ris.setCodiceTomb(request.getParameter("chiaveTomb"));

		return ris;		
	}
	
	
	private RicercaDocumentiEmissioneRequest prepareRicercaDocumentiEmi(HttpServletRequest request)
	{
		PropertiesTree configuration; 
		
		RicercaDocumentiEmissioneRequest ris = new RicercaDocumentiEmissioneRequest();

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
        ris.setCodiceUtente(request.getParameter("chiaveCodUte"));
        ris.setCodiceEnte(request.getParameter("chiaveCodEnte"));      
        ris.setTipoUfficio(request.getParameter("chiaveTipoUff"));
        ris.setCodiceUfficio(request.getParameter("chiaveCodUff"));        
        ris.setImpostaServizio(request.getParameter("chiaveIS"));
        ris.setAnnoEmissione(request.getParameter("chiaveAnnoE"));	//nuova
        ris.setNumeroEmissione(request.getParameter("chiaveNumeroE")); //nuova
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
