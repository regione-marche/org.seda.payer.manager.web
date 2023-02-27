package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;


import com.seda.payer.pgec.webservice.anagente.dati.*;
import com.seda.payer.pgec.webservice.anagente.source.*;


public class AnagEnteServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AnagEnteImplementationStub anagEnteCaller = null;
	
	private void setCodSocietaHeader (AnagEnteImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}

	public AnagEnteServer(String endPoint) {
		AnagEnteServiceLocator lsService = new AnagEnteServiceLocator();
		lsService.setAnagEntePortEndpointAddress(endPoint);
		try {
			anagEnteCaller = (AnagEnteImplementationStub)lsService.getAnagEntePort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public AnagEnteSearchResponse getAnagEnti(AnagEnteSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(anagEnteCaller, request);
		return anagEnteCaller.getAnagEntes(in);
	}
    public AnagEnteDetailResponse getAnagEnte(AnagEnteDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(anagEnteCaller, request);
    	return anagEnteCaller.getAnagEnte(in);
    }
    public StatusResponse save(AnagEnteSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(anagEnteCaller, request);
    	return anagEnteCaller.save(in);
    }
    public StatusResponse cancel(AnagEnteCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(anagEnteCaller, request);
    	return anagEnteCaller.cancel(in);
    }
	
	

}
