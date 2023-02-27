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
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioPartitaRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioPartitaResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioRuoloRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioRuoloResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioSPartitaRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioSPartitaResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioSRuoloRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaDettaglioSRuoloResponse;
import com.seda.payer.bancadati.webservice.srv.FaultType;

public class DettaglioPartitaAction extends BaseRuoliAction {
	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {

		salvaStato(request);

		super.service(request);
        switch(getFiredButton(request)) 
		{
		case  TX_BUTTON_CERCA:
			break;
		case  TX_BUTTON_CERCA_EXP:
			try 
			{
				/*DettaglioRuoloAction dettaglioRuolo = new DettaglioRuoloAction();
				dettaglioRuolo.ricercaDettaglioXmlRuolo(request);*/

				ricercaDettaglioXmlPartita(request);
				ricercaDettaglioPartita(request);
			}
		catch (FaultType fte) 
		{
			WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
			fte.printStackTrace();
			setFormMessage("DettaglioPartitaForm", "errore: " + decodeMessaggio(fte), request);
		}
		catch (RemoteException af)
		{
			WSCache.logWriter.logError("errore: " + af.getMessage(),af);
			af.printStackTrace();
			setFormMessage("DettaglioPartitaForm", testoErroreColl, request);					
		}
		catch (Exception e) 
		   {
			WSCache.logWriter.logError("errore: " + e.getMessage(),e);
			//setErrorMessage(request, e.getMessage());
			setFormMessage("DettaglioPartitaForm", "errore: " + testoErrore, request);
			}
		break;
		}			
		return null;
	}

    public void ricercaDettaglioXmlPartita(HttpServletRequest request) throws Exception
    {
    	RicercaDettaglioSPartitaResponse out;
		RicercaDettaglioSPartitaRequest in;
		in = prepareRicercaDettaglioS(request);
						
		WSCache.logWriter.logDebug("Pagina dettaglio partita");
		out =  WSCache.ruoliBDServer.ricercaDettaglioXmlPartita(in, request); //.//RecuperaD(in);
        
		request.setAttribute("dettaglioPart", out.getListXml());
    }
	
    public void ricercaDettaglioPartita(HttpServletRequest request) throws Exception
    {
    	RicercaDettaglioPartitaResponse out;
		RicercaDettaglioPartitaRequest in;
		in = prepareRicercaDettaglio(request);
						
		WSCache.logWriter.logDebug("Pagina dettaglio partita");
		out =  WSCache.ruoliBDServer.ricercaDettaglioPartita(in, request); //.//RecuperaD(in);
        
		request.setAttribute("dettCodUffImp",out.getCodUfficio());
		request.setAttribute("dettTpUffImp",out.getTipoUfficio());
		request.setAttribute("dettAnnoRuo",out.getAnnoRuolo());
		request.setAttribute("dettNumRuo",out.getNumeroRuolo());
		request.setAttribute("dettDataConsegna",out.getDataConsegna());
		request.setAttribute("dettCodFis", out.getCodiceFiscale());
		request.setAttribute("dettIdPartita", out.getCodicePartita());
		
    }
    
    private RicercaDettaglioSPartitaRequest prepareRicercaDettaglioS(HttpServletRequest request)
	{

		RicercaDettaglioSPartitaRequest rec = new RicercaDettaglioSPartitaRequest();
		//chiaveCodSoc={1}chiaveCodUte={2}&chiaveCodEnte={3}&chiaveAnnoRuo={4}&chiaveNumRuo={5}&chiaveFlusso{15}&chiaveConc{16}&chiaveTomb{17}
		
		rec.setCodiceUtente(request.getParameter("chiaveCodUte"));
		rec.setCodiceEnte(request.getParameter("chiaveCodEnte"));
		rec.setCodiceSocieta(request.getParameter("chiaveCodSoc"));
		rec.setProgrFlusso(request.getParameter("chiaveFlusso"));
		rec.setCodiceConcessione(request.getParameter("chiaveConcPar"));
		rec.setCodiceTomb(request.getParameter("chiaveTombPar"));
		rec.setNumeroRuolo(request.getParameter("chiaveNumRuo"));
		rec.setAnnoRuolo(request.getParameter("chiaveAnnoRuo"));
		rec.setNumeroPartita(request.getParameter("chiaveNumPar"));

		return rec;
		
	}

    private RicercaDettaglioPartitaRequest prepareRicercaDettaglio(HttpServletRequest request)
	{

		RicercaDettaglioPartitaRequest rec = new RicercaDettaglioPartitaRequest();
		//chiaveCodSoc={1}chiaveCodUte={2}&chiaveCodEnte={3}&chiaveAnnoRuo={4}&chiaveNumRuo={5}&chiaveFlusso{15}&chiaveConc{16}&chiaveTomb{17}
		
		rec.setCodiceUtente(request.getParameter("chiaveCodUte"));
		rec.setCodiceEnte(request.getParameter("chiaveCodEnte"));
		rec.setCodiceSocieta(request.getParameter("chiaveCodSoc"));
		rec.setProgrFlusso(request.getParameter("chiaveFlusso"));
		rec.setCodiceConcessione(request.getParameter("chiaveConcPar"));
		rec.setCodiceTomb(request.getParameter("chiaveTombPar"));
		rec.setNumeroRuolo(request.getParameter("chiaveNumRuo"));
		rec.setAnnoRuolo(request.getParameter("chiaveAnnoRuo"));
		rec.setNumeroPartita(request.getParameter("chiaveNumPar"));

		return rec;
		
	}
	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
	}
	
}
