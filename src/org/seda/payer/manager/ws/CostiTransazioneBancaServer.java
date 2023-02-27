package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.costitransazionebanca.dati.*;
import com.seda.payer.pgec.webservice.costitransazionebanca.source.*;


public class CostiTransazioneBancaServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CostiTransazioneBancaImplementationStub costiTransazioneBancaCaller = null;

	private void setCodSocietaHeader(CostiTransazioneBancaImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public CostiTransazioneBancaServer(String endPoint) {
		CostiTransazioneBancaServiceLocator lsService = new CostiTransazioneBancaServiceLocator();
		lsService.setCostiTransazioneBancaPortEndpointAddress(endPoint);
		try {
			costiTransazioneBancaCaller = (CostiTransazioneBancaImplementationStub)lsService.getCostiTransazioneBancaPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public CostiTransazioneBancaSearchResponse getCostiTransazioneBancas(CostiTransazioneBancaSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(costiTransazioneBancaCaller, request);
		return costiTransazioneBancaCaller.getCostiTransazioneBancas(in);
	}
    public CostiTransazioneBancaDetailResponse getCostiTransazioneBanca(CostiTransazioneBancaDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(costiTransazioneBancaCaller, request);
    	return costiTransazioneBancaCaller.getCostiTransazioneBanca(in);
    }
    public StatusResponse save(CostiTransazioneBancaSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(costiTransazioneBancaCaller, request);
    	return costiTransazioneBancaCaller.save(in);
    }
    public StatusResponse cancel(CostiTransazioneBancaCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(costiTransazioneBancaCaller, request);
    	return costiTransazioneBancaCaller.cancel(in);
    }
	
	

}
