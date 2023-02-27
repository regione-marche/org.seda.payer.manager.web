package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.bollettino.dati.*;
import com.seda.payer.pgec.webservice.bollettino.source.*;


public class BollettinoServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BollettinoImplementationStub bollettinoCaller = null;
	
	private void setCodSocietaHeader (BollettinoImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public BollettinoServer(String endPoint) {
		BollettinoServiceLocator lsService = new BollettinoServiceLocator();
		lsService.setBollettinoPortEndpointAddress(endPoint);
		try {
			bollettinoCaller = (BollettinoImplementationStub)lsService.getBollettinoPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public BollettinoSearchResponse getBollettini(BollettinoSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(bollettinoCaller, request);
		return bollettinoCaller.getBollettini(in);
	}
    public BollettinoDetailResponse getBollettino(BollettinoDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(bollettinoCaller, request);
    	return bollettinoCaller.getBollettino(in);
    }
    public StatusResponse save(BollettinoSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(bollettinoCaller, request);
    	return bollettinoCaller.save(in);
    }
    public StatusResponse cancel(BollettinoCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(bollettinoCaller, request);
    	return bollettinoCaller.cancel(in);
    }
	
}
