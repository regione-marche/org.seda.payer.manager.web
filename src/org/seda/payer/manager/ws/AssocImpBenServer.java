package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.associmpben.dati.*;
import com.seda.payer.pgec.webservice.associmpben.source.*;


public class AssocImpBenServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AssocImpBenImplementationStub servizio = null;
	
	private void setCodSocietaHeader (AssocImpBenImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public AssocImpBenServer(String endPoint) {
		AssocImpBenServiceLocator lsService = new AssocImpBenServiceLocator();
		lsService.setAssocImpBenPortEndpointAddress(endPoint);
		try {
			servizio = (AssocImpBenImplementationStub)lsService.getAssocImpBenPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public AssocImpBenSearchResponse getAssociazioniImpBen(AssocImpBenSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(servizio, request);
		return servizio.getAssociazioniImpBen(in);
	}
    public AssocImpBenDetailResponse getAssociazioneImpBen(AssocImpBenDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.getAssociazioneImpBen(in);
    }
    public StatusResponse save(AssocImpBenSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.save(in);
    }
    public StatusResponse cancel(AssocImpBenCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.cancel(in);
    }
	
}
