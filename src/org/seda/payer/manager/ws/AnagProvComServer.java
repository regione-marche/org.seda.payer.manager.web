package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComCancelRequest;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComDetailRequest;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComDetailResponse;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComSaveRequest;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComSearchRequest;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComSearchResponse;
import com.seda.payer.pgec.webservice.anagprovcom.dati.StatusResponse;
import com.seda.payer.pgec.webservice.anagprovcom.source.AnagProvComImplementationStub;
import com.seda.payer.pgec.webservice.anagprovcom.source.AnagProvComServiceLocator;
import com.seda.payer.pgec.webservice.srv.FaultType;


public class AnagProvComServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AnagProvComImplementationStub anagProvComCaller = null;
	
	private void setCodSocietaHeader (AnagProvComImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	
	public AnagProvComServer(String endPoint) {
		AnagProvComServiceLocator lsService = new AnagProvComServiceLocator();
		lsService.setAnagProvComPortEndpointAddress(endPoint);
		try {
			anagProvComCaller = (AnagProvComImplementationStub)lsService.getAnagProvComPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public AnagProvComSearchResponse getAnagProvComs(AnagProvComSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(anagProvComCaller, request);
		return anagProvComCaller.getAnagProvComs(in);
	}
    public AnagProvComDetailResponse getAnagProvCom(AnagProvComDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(anagProvComCaller, request);
    	return anagProvComCaller.getAnagProvCom(in);
    }
    public StatusResponse save(AnagProvComSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(anagProvComCaller, request);
    	return anagProvComCaller.save(in);
    }
    public StatusResponse cancel(AnagProvComCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(anagProvComCaller, request);
    	return anagProvComCaller.cancel(in);
    }
	
	

}
