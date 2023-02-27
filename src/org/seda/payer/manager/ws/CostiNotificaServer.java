package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.costinotifica.dati.*;
import com.seda.payer.pgec.webservice.costinotifica.source.*;


public class CostiNotificaServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CostiNotificaImplementationStub costiNotificaCaller = null;

	private void setCodSocietaHeader(CostiNotificaImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public CostiNotificaServer(String endPoint) {
		CostiNotificaServiceLocator lsService = new CostiNotificaServiceLocator();
		lsService.setCostiNotificaPortEndpointAddress(endPoint);
		try {
			costiNotificaCaller = (CostiNotificaImplementationStub)lsService.getCostiNotificaPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public CostiNotificaSearchResponse getCostiNotificas(CostiNotificaSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(costiNotificaCaller, request);
		return costiNotificaCaller.getCostiNotificas(in);
	}
    public CostiNotificaDetailResponse getCostiNotifica(CostiNotificaDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(costiNotificaCaller, request);
    	return costiNotificaCaller.getCostiNotifica(in);
    }
    public StatusResponse save(CostiNotificaSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(costiNotificaCaller, request);
    	return costiNotificaCaller.save(in);
    }
    public StatusResponse cancel(CostiNotificaCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(costiNotificaCaller, request);
    	return costiNotificaCaller.cancel(in);
    }
}
