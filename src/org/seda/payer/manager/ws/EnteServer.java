package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.ente.dati.*;
import com.seda.payer.pgec.webservice.ente.source.*;
import com.seda.payer.pgec.webservice.ente.dati.EnteByCFRequest;


public class EnteServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EnteImplementationStub enteCaller = null;
	
	private void setCodSocietaHeader(EnteImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	
	public EnteServer(String endPoint) {
		EnteServiceLocator lsService = new EnteServiceLocator();
		lsService.setEntePortEndpointAddress(endPoint);
		try {
			enteCaller = (EnteImplementationStub)lsService.getEntePort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public EnteSearchResponse getEntes(EnteSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(enteCaller, request);
		return enteCaller.getEntes(in);
	}
	
	public EnteSearchResponse getEntesForAdd(EnteSearchForAddRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(enteCaller, request);
		return enteCaller.getEntesForAdd(in);
	}
	
    public EnteDetailResponse getEnte(EnteDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(enteCaller, request);
    	return enteCaller.getEnte(in);
    }
    public StatusResponse save(EnteSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(enteCaller, request);
    	return enteCaller.save(in);
    }
    public StatusResponse cancel(EnteCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(enteCaller, request);
    	return enteCaller.cancel(in);
    }
    public EnteByCFResponse getEnteByCF(EnteByCFRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(enteCaller, request);
    	return enteCaller.getEnteByCF(in);
    }
	
	

}
