package org.seda.payer.manager.ws;
/*
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.contogestione.webservice.srv.FaultType;
import com.seda.payer.contogestione.webservice.dati.*;
import com.seda.payer.contogestione.webservice.source.*;


public class ContoGestioneServer extends BaseServer
{

	private static final long serialVersionUID = 1L;
	private ContoGestioneSOAPBindingStub servizio = null;
	
	private void setCodSocietaHeader(ContoGestioneSOAPBindingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}

	public ContoGestioneServer(String endPoint) {
		ContoGestioneServiceLocator lsService = new ContoGestioneServiceLocator();
		lsService.setContoGestionePortEndpointAddress(endPoint);
		try {
			servizio = (ContoGestioneSOAPBindingStub)lsService.getContoGestionePort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public AssocBenSearchResponse getAssociazioniBen(AssocBenSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(servizio, request);
		return servizio.getAssociazioniBen(in);
	}

	public ContoGestioneSearchResponse getContiGestione(ContoGestioneSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(servizio, request);
		return servizio.getContiGestione(in);
	}

	public PrendiPathResponse prendiPath(PrendiPathRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(servizio, request);
		return servizio.prendiPath(in);
	}

	public InviaPECResponse inviaPEC(InviaPECRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(servizio, request);
		return servizio.inviaPEC(in);
	}
	
}
*/