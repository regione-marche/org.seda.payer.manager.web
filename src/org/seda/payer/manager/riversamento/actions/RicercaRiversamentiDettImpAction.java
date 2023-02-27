package org.seda.payer.manager.riversamento.actions;

/*
import java.rmi.RemoteException;

import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
//import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.riversamento.webservice.srv.FaultType;
import com.seda.payer.riversamento.webservice.dati.Paginazione;
import com.seda.payer.riversamento.webservice.dati.RecuperaTransazioniRiversateRequest;
import com.seda.payer.riversamento.webservice.dati.RecuperaTransazioniRiversateResponse;

public class RicercaRiversamentiDettImpAction extends BaseRiversamentoAction {

	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		super.service(request);
//		setProfile(request);	
	//	HttpSession session = request.getSession();
		
//		salvaStato(request);

		switch(getFiredButton(request)) 
			{

			case TX_BUTTON_CERCA: 
				try 
					{
			         preparazioneDatiDettaglio(request);
			         ricercaDettagliRiv(request, "R");
					}
				catch (FaultType fte) 
				{
					WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
					fte.printStackTrace();
					setFormMessage("ricercaRiversamentiDettImpForm", "errore: " + decodeMessaggio(fte), request);
				}
				catch (RemoteException af)
				{
					WSCache.logWriter.logError("errore: " + af.getMessage(),af);
					af.printStackTrace();
					setFormMessage("ricercaRiversamentiDettImpForm", testoErroreColl, request);					
				}
				catch (Exception e) 
				   {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
//					setErrorMessage(request, e.getMessage());
					setFormMessage("ricercaRiversamentiDettImpForm", "errore: " + testoErrore, request);
					}
				break;
			}

//		request.setAttribute("riv_message", getMessage());
//		request.setAttribute("riv_error_message", getErrorMessage());
		return null;
	}


    
    
    public void preparazioneDatiDettaglio(HttpServletRequest request) 
    {
		requestData(request, "revData", "c1S");	//c1
		requestData(request, "c14", "c14S");
		requestData(request, "c15", "c15S");
		requestData(request, "c16", "c16S");
//		requestData(request, "c17", "c17S");

		String valore = request.getParameter("revTipo");	//c5
        if (valore.equals("R"))
        	request.setAttribute("c5S", "Rivers.");
        else
        	request.setAttribute("c5S", "Rendic.");

		valore = request.getParameter("revRive");	
        if (valore.equals("B"))
        	request.setAttribute("c6S", "Bonifico");
        else
        	request.setAttribute("c6S", "Altro");

        int c = (int)(request.getParameter("c7").charAt(0));
        switch (c)
        {
        case (int)'A': valore = "Aperto";
        		  	   break;
        case (int)'C': valore = "Chiuso";
        			   break;
        case (int)'S': valore = "Sospeso";
        			   break;
        case (int)'E': valore = "Eseguito";
        			   break;
        }
        
        request.setAttribute("c7S", valore);

        valore = request.getParameter("c3") + "/" + request.getParameter("c4");
        if (valore.equals("/"))
        	valore = "";
        
        request.setAttribute("c3S", valore);
    		
    }

    public void ricercaDettagliRiv(HttpServletRequest request, String tipoDettaglio) throws Exception
    {
			RecuperaTransazioniRiversateResponse out;
			RecuperaTransazioniRiversateRequest in;
			in = prepareRicercaDettaglio(request, tipoDettaglio);
						
			WSCache.logWriter.logDebug("Pagina dettaglio riversamento");
			out =  WSCache.riversamentoServer.recuperaRiversamenti(in, request);

			
			if(out.getPInfo().getNumRows() > 0) 
			{
				request.setAttribute("listaDettaglioImp", out.getListXml());
				request.setAttribute("listaDettaglioImp.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));		

				request.setAttribute("TotCommissioneGestore", out.getTotCommissioneGestore().toString());
				request.setAttribute("TotCommissioniGateway", out.getTotCommissioniGateway().toString());
				request.setAttribute("TotImportoCittadino", out.getTotImportoCittadino().toString());
				request.setAttribute("TotImportoContribuenti", out.getTotImportoContribuenti().toString());
				request.setAttribute("TotSpeseNotifica", out.getTotSpeseNotifica().toString());

//				request.setAttribute("TotImportoRiversare", out.getTotImportoRiversare().toString());
//				request.setAttribute("TotImportoRecuperare", out.getTotImportoRecuperare().toString());
//				request.setAttribute("NumRighe", Integer.toString(out.getPInfo().getNumRows()));
			}
//			else setMessage(request, Messages.TX_NO_TRANSACTIONS.format());
//			else setMessage("Nessun risultato");
			else setFormMessage("ricercaRiversamentiDettImpForm", Messages.NO_DATA_FOUND.format(), request);					
			
		
	
    }
    
    
	
	private RecuperaTransazioniRiversateRequest prepareRicercaDettaglio(HttpServletRequest request, String tipoDettaglio)
	{

		PropertiesTree configuration; 
		RecuperaTransazioniRiversateRequest ris;
		int rowsPerPage;
		
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		
		rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

        if (request.getParameter("rowsPerPage")!=null)	
        {
        	rowsPerPage = Integer.parseInt(request.getParameter("rowsPerPage"));
        }
         
		int pageNumber = (request.getParameter("pageNumber") == null) || (request.getParameter("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getParameter("pageNumber"));

		String order = (request.getParameter("order") == null) ? "" : request.getParameter("order");

		ris = new RecuperaTransazioniRiversateRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
    	// preparazione 
	    //revSoc
		//revUte
	    //revData
	    //revBen
	    //revTipo
	    //revRive
	
		ris.setCodiceSocieta(request.getParameter("revSoc"));
        ris.setCodiceUtente(request.getParameter("revUte"));
        ris.setDataRiversamento(request.getParameter("revData").substring(0,10));
        ris.setEnteBeneficiario(request.getParameter("revBen"));
        ris.setTipoRendicontazione(request.getParameter("revTipo"));
        ris.setTipoRiversamento(request.getParameter("revRive"));
		ris.setTipoDettaglio(tipoDettaglio);
		
		return ris;
		
	}
	
	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
		//Salvo le date che richiedono l'utilizzo di un tipo "Calendar"
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
}
*/