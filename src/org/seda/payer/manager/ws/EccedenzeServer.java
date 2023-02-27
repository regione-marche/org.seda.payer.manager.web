package org.seda.payer.manager.ws;
/*
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.eccedenze.webservice.srv.FaultType;
import com.seda.payer.eccedenze.webservice.dati.*;
import com.seda.payer.eccedenze.webservice.source.*;


public class EccedenzeServer extends BaseServer
{
	private static final long serialVersionUID = 1L;
	private EccedenzeSOAPBindingStub servizio = null;
	
	private void setCodSocietaHeader(EccedenzeSOAPBindingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public EccedenzeServer(String endPoint) {
		EccedenzeServiceLocator lsService = new EccedenzeServiceLocator();
		lsService.setEccedenzePortEndpointAddress(endPoint);
		try {
			servizio = (EccedenzeSOAPBindingStub)lsService.getEccedenzePort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

    public RecuperaRimborsiResponse recuperaRimborsi(RecuperaRimborsiRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaRimborsi(in);    	
    }

    public RecuperaDettaglioRimborsoResponse RecuperaDettaglioRimborso(RecuperaDettaglioRimborsoRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaDettaglioRimborso(in);    	
    }

}
*/