package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.beneficiario.dati.*;
import com.seda.payer.pgec.webservice.beneficiario.source.*;


public class BeneficiarioServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BeneficiarioImplementationStub servizio = null;
	
	private void setCodSocietaHeader (BeneficiarioImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public BeneficiarioServer(String endPoint) {
		BeneficiarioServiceLocator lsService = new BeneficiarioServiceLocator();
		lsService.setBeneficiarioPortEndpointAddress(endPoint);
		try {
			servizio = (BeneficiarioImplementationStub)lsService.getBeneficiarioPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public BeneficiarioSearchResponse getBeneficiari(BeneficiarioSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(servizio, request);
		return servizio.getBeneficiari(in);
	}
    public BeneficiarioDetailResponse getBeneficiario(BeneficiarioDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.getBeneficiario(in);
    }
    public StatusResponse save(BeneficiarioSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.save(in);
    }
    public StatusResponse cancel(BeneficiarioCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.cancel(in);
    }

	
}
