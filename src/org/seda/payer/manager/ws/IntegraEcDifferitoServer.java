package org.seda.payer.manager.ws;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import org.seda.payer.manager.entrate.actions.util.EntrateUtils;

import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.CancellazioneEcRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.CancellazioneEcResponse;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.DiscaricoEcRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.DiscaricoEcResponse;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.InserimentoEcRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.InserimentoEcResponse;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.RichiestaAvvisoPagoPaRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.RichiestaAvvisoPagoPaResponse;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.VariazioneEcRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.VariazioneEcResponse;

import com.esed.payer.archiviocarichi.webservice.integraecdifferito.source.IntegraEcDifferitoSOAPBindingStub;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.source.IntegraEcDifferitoServiceLocator;
import com.seda.payer.integraente.webservice.srv.FaultType;

public class IntegraEcDifferitoServer extends BaseServer {
  private static final long serialVersionUID = 1L;
  private IntegraEcDifferitoSOAPBindingStub pgCaller = null;

	private void setCodSocietaHeader(IntegraEcDifferitoSOAPBindingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	
	public IntegraEcDifferitoServer(String endPoint)
	{
		IntegraEcDifferitoServiceLocator lsService = new IntegraEcDifferitoServiceLocator();
		lsService.setIntegraEcDifferitoPortEndpointAddress(endPoint);
		try { 
			// istanzio l'interfaccia del webservice
			pgCaller = (IntegraEcDifferitoSOAPBindingStub)lsService.getIntegraEcDifferitoPort();			
			
		} catch (ServiceException e) {
//			LogWriter.logError("IntegraEntePgServer constructor - endpoint: " + endPoint, e);
		}
	}

  public CancellazioneEcResponse deleteDocumentoCarico(CancellazioneEcRequest rq, HttpServletRequest request, String codiceUtenteSeda) throws FaultType, RemoteException
  {
    //nel caso di integrazione seda, nel soap binding passo il codice utente seda 
    //per recuperare dinamicamente la connessione corretta
    if (codiceUtenteSeda != null && codiceUtenteSeda.trim().length() > 0)
    {
      pgCaller.clearHeaders();
      pgCaller.setHeader("",DBSCHEMACODSOCIETA,codiceUtenteSeda); 
    }
    else
      setCodSocietaHeader(pgCaller, request);
    return this.pgCaller.cancellazioneEC(rq);
  }
	
  public VariazioneEcResponse variazioneEC(VariazioneEcRequest rq, HttpServletRequest request, String codiceUtenteSeda) throws FaultType, RemoteException
  {
    //nel caso di integrazione seda, nel soap binding passo il codice utente seda 
    //per recuperare dinamicamente la connessione corretta
    if (codiceUtenteSeda != null && codiceUtenteSeda.trim().length() > 0)
    {
      pgCaller.clearHeaders();
      pgCaller.setHeader("",DBSCHEMACODSOCIETA,codiceUtenteSeda); 
    }
    else
      setCodSocietaHeader(pgCaller, request);
    return this.pgCaller.variazioneEC(rq);
  }
  
  public InserimentoEcResponse addDocumentoCarico(InserimentoEcRequest rq, HttpServletRequest request, String codiceUtenteSeda) throws FaultType, RemoteException
  {
    //nel caso di integrazione seda, nel soap binding passo il codice utente seda 
    //per recuperare dinamicamente la connessione corretta
    if (codiceUtenteSeda != null && codiceUtenteSeda.trim().length() > 0)
    {
      pgCaller.clearHeaders();
      pgCaller.setHeader("",DBSCHEMACODSOCIETA,codiceUtenteSeda); 
    }
    else
      setCodSocietaHeader(pgCaller, request);
    return this.pgCaller.inserimentoEC(rq);
  }

	public DiscaricoEcResponse discaricoTributi(DiscaricoEcRequest rq, HttpServletRequest request, String codiceUtenteSeda) throws FaultType, RemoteException
	{
		//nel caso di integrazione seda, nel soap binding passo il codice utente seda 
		//per recuperare dinamicamente la connessione corretta
		if (codiceUtenteSeda != null && codiceUtenteSeda.trim().length() > 0)
		{
			pgCaller.clearHeaders();
			pgCaller.setHeader("",DBSCHEMACODSOCIETA,codiceUtenteSeda);	
		}
		else
			setCodSocietaHeader(pgCaller, request);
		return this.pgCaller.discaricoEC(rq);
	}
	
	//PG200450 GG 09032021 - inizio
	 public RichiestaAvvisoPagoPaResponse stampaAvvisoPagoPA (RichiestaAvvisoPagoPaRequest rq, HttpServletRequest request, String codiceUtenteSeda) throws FaultType, RemoteException
	  {
	    //nel caso di integrazione seda, nel soap binding passo il codice utente seda 
	    //per recuperare dinamicamente la connessione corretta
	    if (codiceUtenteSeda != null && codiceUtenteSeda.trim().length() > 0)
	    {
	      pgCaller.clearHeaders();
	      pgCaller.setHeader("",DBSCHEMACODSOCIETA,codiceUtenteSeda); 
	    }
	    else
	      setCodSocietaHeader(pgCaller, request);
	    return this.pgCaller.richiestaAvvisoPagoPa(rq);
	  }
	//PG200450 GG 09032021 - inizio
}
