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
import com.seda.payer.bancadati.webservice.dati.RecuperaDocumentiRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDocumentiResponse;
import com.seda.payer.bancadati.webservice.srv.FaultType;

public class DettaglioDocumentoAction extends BaseEntrateAction {
	private static final long serialVersionUID = 1L;
	private final String RICERCA_DOCUMENTI = "ricercaDocumenti";
	
	public Object service(HttpServletRequest request) throws ActionException {

		salvaStato(request);

		super.service(request);
        switch(getFiredButton(request)) 
		{
		case TX_BUTTON_CERCA: 
/*
			String chiamante = request.getParameter("pagPrec");
			if(chiamante == null )
				//errore
			if(chiamante.equals(RICERCA_DOCUMENTI))	{
				request.setAttribute("rowsPerPage", request.getParameter("r2RowsPerPage"));
				request.setAttribute("pageNumber", request.getParameter("r2PageNumber"));
				request.setAttribute("order", request.getParameter("r2Order"));
			}
			break;
			*/
		case  TX_BUTTON_CERCA_EXP:
			try 
			{
	         ricercaDettaglioDocumento(request);
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

    public void ricercaDettaglioDocumento(HttpServletRequest request) throws Exception
    {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	RecuperaDettaglioDocumentiResponse out;
		RecuperaDettaglioDocumentiRequest in;
		in = prepareRicercaDettaglio(request);
						
		WSCache.logWriter.logDebug("Pagina dettaglio riversamento");
		out =  WSCache.entrateBDServer.dettaglioDocumento(in, request); //.//RecuperaD(in);
        
		//request.setAttribute("dettaglioDoc", out.getListXml());
		request.setAttribute("dettImpostaServ",out.getImpostaServizio());
		request.setAttribute("dettUffImp",out.getUfficioImpositore());
		request.setAttribute("dettAnnoEmis",out.getAnnoEmissione());
		request.setAttribute("dettNumEmis",out.getNumeroEmissione());
		request.setAttribute("dettTipoServ",out.getTipologiaServizio());
		request.setAttribute("dettDocRend",out.getDocRendicontato());
		request.setAttribute("dettCodFisc",out.getCodiceFiscale());
		request.setAttribute("dettNumDoc",out.getNumeroDocumento());
		request.setAttribute("dettNumRate",out.getNumeroRate());
		request.setAttribute("dettScadPRata",out.getScadenzaPrimaRata()== null?"":getFormatDate(out.getScadenzaPrimaRata()));
		request.setAttribute("dettSosp",out.getSospensione());
		request.setAttribute("dettDataNotif",out.getDataNotifica()== null?"":getFormatDate(out.getDataNotifica()));
		request.setAttribute("dettProcEse",out.getProcEsecutive());
		request.setAttribute("dettNumDocColl",out.getNumeroDocumentoColl());
		request.setAttribute("dettTotIntMora",out.getIntMora());
		request.setAttribute("dettTotEcc",out.getTotEccedenza());
		request.setAttribute("dettSoc",out.getDescSocieta());
		request.setAttribute("dettUte",out.getDescUtente());
		request.setAttribute("dettEnte",out.getDescEnte());
		request.setAttribute("codSoc", out.getCodSocieta());
		request.setAttribute("codUte", out.getCodUtente());
		request.setAttribute("codEnte", out.getCodEnte());
		request.setAttribute("codTipoServ", out.getCodTipoServizio());
		request.setAttribute("numeroBollettino", out.getNumeroBollettino());
		request.setAttribute("numeroIUV", out.getNumeroIUV());
		
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

		String chiaveTipoUff = request.getAttribute("chiaveTipoUff")==null?"":(String)request.getAttribute("chiaveTipoUff");
		String chiaveCodUff = request.getAttribute("chiaveCodUff")==null?"":(String)request.getAttribute("chiaveCodUff");
		if (chiaveTipoUff.equals("")&&chiaveCodUff.equals(""))
			request.setAttribute("ufficio","");
		else
			request.setAttribute("ufficio", chiaveTipoUff + "/" +  chiaveCodUff);
    }
    
    
	private RecuperaDettaglioDocumentiRequest prepareRicercaDettaglio(HttpServletRequest request)
	{

		RecuperaDettaglioDocumentiRequest rec = new RecuperaDettaglioDocumentiRequest();
		//chiaveIS{1}&chiaveDoc={8}&chiaveTipoUff={5}&chiaveCodUff={6}&chiaveCodUte={9}&chiaveCodEnte={10}&chiaveServ={22}&chiaveTomb={23}&chiaveFlusso={21}	
		rec.setImpostaServizio(request.getParameter("chiaveIS"));
		rec.setNumeroDocumento(request.getParameter("chiaveDoc"));
		rec.setTipoUfficio(request.getParameter("chiaveTipoUff"));
		rec.setCodiceUfficio(request.getParameter("chiaveCodUff"));
		rec.setCodiceUtente(request.getParameter("chiaveCodUte"));
		rec.setCodiceEnte(request.getParameter("chiaveCodEnte"));
		rec.setTipologiaServizio(request.getParameter("chiaveServ"));
		rec.setProgressivoFlusso(request.getParameter("chiaveFlusso"));
		rec.setCodiceTomb(request.getParameter("chiaveTomb"));

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
