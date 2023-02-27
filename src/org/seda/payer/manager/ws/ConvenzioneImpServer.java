package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.convenzioneimp.dati.*;
import com.seda.payer.pgec.webservice.convenzioneimp.source.*;


public class ConvenzioneImpServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConvenzioneImpImplementationStub servizio = null;

	private void setCodSocietaHeader(ConvenzioneImpImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public ConvenzioneImpServer(String endPoint) {
		ConvenzioneImpServiceLocator lsService = new ConvenzioneImpServiceLocator();
		lsService.setConvenzioneImpPortEndpointAddress(endPoint);
		try {
			servizio = (ConvenzioneImpImplementationStub)lsService.getConvenzioneImpPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public ConvenzioneImpSearchResponse getConvenzioniImp(ConvenzioneImpSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(servizio, request);
		return servizio.getConvenzioniImp(in);
	}
    public ConvenzioneImpDetailResponse getConvenzioneImp(ConvenzioneImpDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.getConvenzioneImp(in);
    }
    public StatusResponse save(ConvenzioneImpSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.save(in);
    }
    public StatusResponse cancel(ConvenzioneImpCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.cancel(in);
    }

	
}
