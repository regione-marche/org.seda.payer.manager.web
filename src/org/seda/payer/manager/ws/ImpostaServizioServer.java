package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.impostaservizio.dati.*;
import com.seda.payer.pgec.webservice.impostaservizio.source.*;


public class ImpostaServizioServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImpostaServizioImplementationStub impostaServizioCaller = null;

	private void setCodSocietaHeader(ImpostaServizioImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public ImpostaServizioServer(String endPoint) {
		ImpostaServizioServiceLocator lsService = new ImpostaServizioServiceLocator();
		lsService.setImpostaServizioPortEndpointAddress(endPoint);
		try {
			impostaServizioCaller = (ImpostaServizioImplementationStub)lsService.getImpostaServizioPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public ImpostaServizioSearchResponse getImpostaServizios(ImpostaServizioSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(impostaServizioCaller, request);
		return impostaServizioCaller.getImpostaServizios(in);
	}
	public ImpostaServizioSearchResponse getImpostaServizios2(ImpostaServizioSearchRequest2 in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(impostaServizioCaller, request);
		return impostaServizioCaller.getImpostaServizios2(in);
	}
    public ImpostaServizioDetailResponse getImpostaServizio(ImpostaServizioDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(impostaServizioCaller, request);
    	return impostaServizioCaller.getImpostaServizio(in);
    }
    public StatusResponse save(ImpostaServizioSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(impostaServizioCaller, request);
    	return impostaServizioCaller.save(in);
    }
    public StatusResponse cancel(ImpostaServizioCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(impostaServizioCaller, request);
    	return impostaServizioCaller.cancel(in);
    }
	
	

}
