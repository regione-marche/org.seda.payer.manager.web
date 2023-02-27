package org.seda.payer.manager.entrate.actions;

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
import com.seda.payer.bancadati.webservice.srv.FaultType;

public class DettaglioEmissioneAction extends BaseEntrateAction {
	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {

		salvaStato(request);

		super.service(request);
        switch(getFiredButton(request)) 
		{
		case  TX_BUTTON_CERCA:
			try 
			{
	         ricercaDettaglioEmissione(request);
			}
		catch (FaultType fte) 
		{
			WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
			fte.printStackTrace();
			setFormMessage("dettaglioDocumento", "errore: " + decodeMessaggio(fte), request);
		}
		catch (RemoteException af)
		{
			WSCache.logWriter.logError("errore: " + af.getMessage(),af);
			af.printStackTrace();
			setFormMessage("dettaglioDocumento", testoErroreColl, request);					
		}
		catch (Exception e) 
		   {
			WSCache.logWriter.logError("errore: " + e.getMessage(),e);
			//setErrorMessage(request, e.getMessage());
			setFormMessage("dettaglioDocumento", "errore: " + testoErrore, request);
			}
		break;
		}			
		return null;
	}

    public void ricercaDettaglioEmissione(HttpServletRequest request) throws Exception
    {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	RecuperaDettaglioEmissioneResponse out;
		RecuperaDettaglioEmissioneRequest in;
		in = prepareRicercaDettaglio(request);
						
		WSCache.logWriter.logDebug("Pagina dettaglio emissione");
		out =  WSCache.entrateBDServer.dettaglioEmissione(in, request); //.//RecuperaD(in);
        
		//request.setAttribute("dettaglioDoc", out.getListXml());
		request.setAttribute("dettImpostaServ",out.getImpostaServizio());
		request.setAttribute("dettUffImp",out.getUfficioImpositore());
		request.setAttribute("dettAnnoEmis",out.getAnnoEmissione());
		request.setAttribute("dettNumEmis",out.getNumeroEmissione());
		request.setAttribute("dettTipoServ",out.getTipologiaServizio());
		request.setAttribute("dettTotDocRend",out.getNumDocRendicontato());
		request.setAttribute("dettTotDoc",out.getNumDocumenti());

		request.setAttribute("dettSoc",out.getDescSocieta());
		request.setAttribute("dettUte",out.getDescUtente());
		request.setAttribute("dettEnte",out.getDescEnte());
		
		/*
		<s:td cssclass="seda-ui-datagridcell">Società: ${dettSoc}</s:td>
		<s:td cssclass="seda-ui-datagridcell">Utente: ${dettUte}</s:td>
		<s:td cssclass="seda-ui-datagridcell"icol="2">Ente: ${dettEnte}</s:td>
		*/
		request.setAttribute("totImportoCarico", out.getTotCarico());
		request.setAttribute("totImportoRendiconto", out.getTotRendicontato().toString());
		//request.setAttribute("totImportoFinCarico", out.getTotFinCarico().toString());
		request.setAttribute("totImportoDiminuzioneCarico", out.getTotDimensioneCarico().toString());
		request.setAttribute("totImportoRiscosso", out.getTotRiscosso().toString());
		request.setAttribute("totImportoTotaleRimborso", out.getTotRimborso().toString());
		request.setAttribute("totImportoTotaleResiduo", out.getTotResiduo().toString());
		request.setAttribute("totResScaduto", out.getTotResScaduto().toString());

		request.setAttribute("percRiscossoCarico", out.getPercRiscossoCarico());
		request.setAttribute("percRendicontatoCarico", out.getPercRendicontatoCarico());
		request.setAttribute("percRimborsoCarico", out.getPercRimborsoCarico());
		request.setAttribute("percSgravatoCarico", out.getPercSgravatoCarico());
		request.setAttribute("percResiduoCarico", out.getPercResiduoCarico());
		request.setAttribute("percResiduoScadCarico", out.getPercResScadutoCarico());
    
    }
    
    
	private RecuperaDettaglioEmissioneRequest prepareRicercaDettaglio(HttpServletRequest request)
	{

		RecuperaDettaglioEmissioneRequest rec = new RecuperaDettaglioEmissioneRequest();
		//chiaveCodUte={7}&chiaveCodEnte={8}&chiaveTipoUff={4}&chiaveCodUff={5}&chiaveIS={1}&chiaveAnnoE={2}&chiaveNumeroE={3}
		
		
		
		rec.setCodiceUtente(request.getParameter("chiaveCodUte"));
		rec.setCodiceEnte(request.getParameter("chiaveCodEnte"));
		rec.setTipoUfficio(request.getParameter("chiaveTipoUff"));
		rec.setCodiceUfficio(request.getParameter("chiaveCodUff"));
		rec.setImpostaServizio(request.getParameter("chiaveIS"));
		rec.setNumeroEmissione(request.getParameter("chiaveNumeroE"));
		rec.setAnnoEmissione(request.getParameter("chiaveAnnoE"));

		return rec;
		
	}
	
    private String getFormatDate(java.util.Calendar data){
    	SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
    	return dateformat.format(data.getTime());
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
