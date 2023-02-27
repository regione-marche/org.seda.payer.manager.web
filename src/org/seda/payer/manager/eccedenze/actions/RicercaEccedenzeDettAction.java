package org.seda.payer.manager.eccedenze.actions;
/*
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;

import org.seda.payer.manager.util.Messages;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;

import com.seda.payer.eccedenze.webservice.dati.RecuperaDettaglioRimborsoRequest;
import com.seda.payer.eccedenze.webservice.dati.RecuperaDettaglioRimborsoResponse;
import com.seda.payer.eccedenze.webservice.dati.EccedenzaType;
import com.seda.payer.eccedenze.webservice.srv.FaultType;


public class RicercaEccedenzeDettAction extends BaseEccedenzeAction {

	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {

		salvaStato(request);

		super.service(request);

		switch(getFiredButton(request)) 
			{

			case TX_BUTTON_CERCA: 
				try 
					{
			         ricercaDettagliEccedenze(request);
					}
				catch (FaultType fte) 
				{
					WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
					fte.printStackTrace();
					setFormMessage("ricercaEccedenzeForm", "errore: " + decodeMessaggio(fte), request);
				}
				catch (RemoteException af)
				{
					WSCache.logWriter.logError("errore: " + af.getMessage(),af);
					af.printStackTrace();
					setFormMessage("ricercaEccedenzeForm", testoErroreColl, request);					
				}
				catch (Exception e) 
				   {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					//setErrorMessage(request, e.getMessage());
					setFormMessage("ricercaEccedenzeForm", "errore: " + testoErrore, request);
					}
				break;
			}

		return null;
	}


    
    

    public void ricercaDettagliEccedenze(HttpServletRequest request) throws Exception
    {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	RecuperaDettaglioRimborsoResponse out;
		RecuperaDettaglioRimborsoRequest in;
		in = prepareRicercaDettaglio(request);
						
		WSCache.logWriter.logDebug("Pagina dettaglio riversamento");
		out =  WSCache.eccedenzeServer.RecuperaDettaglioRimborso(in, request);
 
        EccedenzaType eccedenza = out.getEccedenza();
        
		if(eccedenza != null) 
			{
				request.setAttribute("dettaglioEcc", out.getListXml());
				request.setAttribute("ecc", eccedenza);
				if (eccedenza.getDataEsito().getTime().equals(formatter.parse("1000-01-01")))
					request.setAttribute("dataEsitoPres",false);
				else
					request.setAttribute("dataEsitoPres",true);
			}
		else 
			setFormMessage("ricercaEccedenzeForm", Messages.NO_DATA_FOUND.format(), request);							
	
    }
    
    
	private RecuperaDettaglioRimborsoRequest prepareRicercaDettaglio(HttpServletRequest request)
	{

		RecuperaDettaglioRimborsoRequest ris = new RecuperaDettaglioRimborsoRequest();
			
		ris.setChiaveEccedenza(request.getParameter("chiaveEcc"));

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
//}
