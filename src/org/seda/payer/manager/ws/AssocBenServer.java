package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.assocben.dati.*;
import com.seda.payer.pgec.webservice.assocben.source.*;


public class AssocBenServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AssocBenImplementationStub servizio = null;
	
	private void setCodSocietaHeader (AssocBenImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public AssocBenServer(String endPoint) {
		AssocBenServiceLocator lsService = new AssocBenServiceLocator();
		lsService.setAssocBenPortEndpointAddress(endPoint);
		try {
			servizio = (AssocBenImplementationStub) lsService.getAssocBenPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public AssocBenSearchResponse getAssociazioniBen(AssocBenSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(servizio, request);
		return servizio.getAssociazioniBen(in);
	}
    public AssocBenDetailResponse getAssociazioneBen(AssocBenDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.getAssociazioneBen(in);
    }
    public StatusResponse save(AssocBenSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.save(in);
    }
    public StatusResponse cancel(AssocBenCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.cancel(in);
    }
	
}
