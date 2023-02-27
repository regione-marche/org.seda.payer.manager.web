package org.seda.payer.manager.ruoli.actions;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.bancadati.webservice.dati.Paginazione;
import com.seda.payer.bancadati.webservice.dati.RecuperaDettaglioDocumentiRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDettaglioDocumentiResponse;
import com.seda.payer.bancadati.webservice.dati.RecuperaDettaglioEmissioneRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDettaglioEmissioneResponse;
import com.seda.payer.bancadati.webservice.dati.RecuperaDocumentiRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDocumentiResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioRuoloRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioRuoloResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioSRuoloRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioSRuoloResponse;
import com.seda.payer.bancadati.webservice.srv.FaultType;

public class DettaglioRuoloAction extends BaseRuoliAction {
	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {

		salvaStato(request);

		super.service(request);
        switch(getFiredButton(request)) 
		{
		case  TX_BUTTON_CERCA:
			try 
			{
				ricercaDettaglioXmlRuolo(request);
				ricercaDettaglioRuolo(request);
			}
		catch (FaultType fte) 
		{
			WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
			fte.printStackTrace();
			setFormMessage("DettaglioRuoloForm", "errore: " + decodeMessaggio(fte), request);
		}
		catch (RemoteException af)
		{
			WSCache.logWriter.logError("errore: " + af.getMessage(),af);
			af.printStackTrace();
			setFormMessage("DettaglioRuoloForm", testoErroreColl, request);					
		}
		catch (Exception e) 
		   {
			WSCache.logWriter.logError("errore: " + e.getMessage(),e);
			//setErrorMessage(request, e.getMessage());
			setFormMessage("DettaglioRuoloForm", "errore: " + testoErrore, request);
			}
		break;
		}			
		return null;
	}

    public void ricercaDettaglioXmlRuolo(HttpServletRequest request) throws Exception
    {
    	RicercaDettaglioSRuoloResponse out;
		RicercaDettaglioSRuoloRequest in;
		in = prepareRicercaDettaglioS(request);
						
		WSCache.logWriter.logDebug("Pagina dettaglio ruolo");
		out =  WSCache.ruoliBDServer.ricercaDettaglioXmlRuolo(in, request); //.//RecuperaD(in);
        
		request.setAttribute("dettaglioRuo", out.getListXml());
    }
	
	
	
    public void ricercaDettaglioRuolo(HttpServletRequest request) throws Exception
    {
    	RicercaDettaglioRuoloResponse out;
		RicercaDettaglioRuoloRequest in;
		in = prepareRicercaDettaglio(request);
						
		WSCache.logWriter.logDebug("Pagina dettaglio emissione");
		out =  WSCache.ruoliBDServer.ricercaDettaglioRuolo(in, request); //.//RecuperaD(in);
        
		request.setAttribute("dettCodUffImp",out.getCodUfficio());
		request.setAttribute("dettTpUffImp",out.getTipoUfficio());
		request.setAttribute("dettAnnoRuo",out.getAnnoRuolo());
		request.setAttribute("dettNumRuo",out.getNumeroRuolo());
		request.setAttribute("dettTotPart",out.getNumeroPartite());
		request.setAttribute("dettDataMinuta",out.getDataMinuta());
		request.setAttribute("dettNumeroMinuta",out.getNumeroMinuta());
		request.setAttribute("dettDataConsegna",out.getDataConsegna());
		
		request.setAttribute("totImportoCarico", out.getTotCarico());
		request.setAttribute("totImportoDiminuzioneCarico", out.getTotDiminuzioneCarico());
		request.setAttribute("totImportoSgravio", out.getTotSgravio());
		request.setAttribute("totImportoRiscosso", out.getTotRiscosso().toString());
		request.setAttribute("totImportoMora", out.getTotMora());
		request.setAttribute("totImportoVarCarico", out.getTotVariazioneCarico());
    }
    
    private RicercaDettaglioSRuoloRequest prepareRicercaDettaglioS(HttpServletRequest request)
	{

		RicercaDettaglioSRuoloRequest rec = new RicercaDettaglioSRuoloRequest();
		//chiaveCodSoc={1}chiaveCodUte={2}&chiaveCodEnte={3}&chiaveAnnoRuo={4}&chiaveNumRuo={5}&chiaveFlusso{15}&chiaveConc{16}&chiaveTomb{17}
		
		rec.setCodiceUtente(request.getParameter("chiaveCodUte"));
		rec.setCodiceEnte(request.getParameter("chiaveCodEnte"));
		rec.setCodiceSocieta(request.getParameter("chiaveCodSoc"));
		rec.setProgrFlusso(request.getParameter("chiaveFlusso"));
		rec.setCodiceConcessione(request.getParameter("chiaveConc"));
		rec.setCodiceTomb(request.getParameter("chiaveTomb"));
		rec.setNumeroRuolo(request.getParameter("chiaveNumRuo"));
		rec.setAnnoRuolo(request.getParameter("chiaveAnnoRuo"));

		return rec;
		
	}

    private RicercaDettaglioRuoloRequest prepareRicercaDettaglio(HttpServletRequest request)
	{

		RicercaDettaglioRuoloRequest rec = new RicercaDettaglioRuoloRequest();
		//chiaveCodSoc={1}chiaveCodUte={2}&chiaveCodEnte={3}&chiaveAnnoRuo={4}&chiaveNumRuo={5}&chiaveFlusso{15}&chiaveConc{16}&chiaveTomb{17}
		
		rec.setCodiceUtente(request.getParameter("chiaveCodUte"));
		rec.setCodiceEnte(request.getParameter("chiaveCodEnte"));
		rec.setCodiceSocieta(request.getParameter("chiaveCodSoc"));
		rec.setProgrFlusso(request.getParameter("chiaveFlusso"));
		rec.setCodiceConcessione(request.getParameter("chiaveConc"));
		rec.setCodiceTomb(request.getParameter("chiaveTomb"));
		rec.setNumeroRuolo(request.getParameter("chiaveNumRuo"));
		rec.setAnnoRuolo(request.getParameter("chiaveAnnoRuo"));

		return rec;
		
	}
	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
	}
	
}
