package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.integraente.webservice.dati.RecuperaCodFiscHostRequest;
import com.seda.payer.integraente.webservice.dati.RecuperaCodFiscHostResponse;
import com.seda.payer.integraente.webservice.source.IntegraEntePgSOAPBindingStub;
import com.seda.payer.integraente.webservice.source.IntegraEntePgServiceLocator;
import com.seda.payer.integraente.webservice.srv.FaultType;

public class IntegraEntePgServer extends BaseServer {
	private IntegraEntePgSOAPBindingStub pgCaller = null;

	private void setCodSocietaHeader(IntegraEntePgSOAPBindingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	
	public IntegraEntePgServer(String endPoint)
	{
		IntegraEntePgServiceLocator lsService = new IntegraEntePgServiceLocator();
		lsService.setIntegraentePortEndpointAddress(endPoint);
		try { 
			// istanzio l'interfaccia del webservice
			pgCaller = (IntegraEntePgSOAPBindingStub)lsService.getIntegraentePort();			
			
		} catch (ServiceException e) {
//			LogWriter.logError("IntegraEntePgServer constructor - endpoint: " + endPoint, e);
		}
	}
	
	
//	public RecuperaDatiBollettinoResponse recuperaDatiBollettino(RecuperaDatiBollettinoRequest rq, HttpServletRequest request, String codiceUtenteSeda) throws FaultType, RemoteException
//	{
//		//nel caso di integrazione seda, nel soap binding passo il codice utente seda 
//		//per recuperare dinamicamente la connessione corretta
//		if (codiceUtenteSeda != null && codiceUtenteSeda.trim().length() > 0)
//		{
//			pgCaller.clearHeaders();
//			pgCaller.setHeader("",DBSCHEMACODSOCIETA,codiceUtenteSeda);	
//		}
//		else
//			setCodSocietaHeader(pgCaller, request);
//		return this.pgCaller.recuperaDatiBollettino(rq);
//	}
	
	public RecuperaCodFiscHostResponse recuperaCodFiscHost(RecuperaCodFiscHostRequest rq, HttpServletRequest request, String codiceUtenteSeda) throws FaultType, RemoteException
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
		return this.pgCaller.recuperaCodFiscHost(rq);
	}
}
