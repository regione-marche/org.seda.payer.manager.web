package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.anagservizi.dati.*;
import com.seda.payer.pgec.webservice.anagservizi.source.*;


public class AnagServiziServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AnagServiziImplementationStub anagServiziCaller = null;
	
	private void setCodSocietaHeader (AnagServiziImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public AnagServiziServer(String endPoint) {
		AnagServiziServiceLocator lsService = new AnagServiziServiceLocator();
		lsService.setAnagServiziPortEndpointAddress(endPoint);
		try {
			anagServiziCaller = (AnagServiziImplementationStub)lsService.getAnagServiziPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public AnagServiziSearchResponse getAnagServizis(AnagServiziSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(anagServiziCaller, request);
		return anagServiziCaller.getAnagServizis(in);
	}
    public AnagServiziDetailResponse getAnagServizi(AnagServiziDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(anagServiziCaller, request);
    	return anagServiziCaller.getAnagServizi(in);
    }
    public StatusResponse save(AnagServiziSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(anagServiziCaller, request);
    	return anagServiziCaller.save(in);
    }
    public StatusResponse cancel(AnagServiziCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(anagServiziCaller, request);
    	return anagServiziCaller.cancel(in);
    }
	
	

}
