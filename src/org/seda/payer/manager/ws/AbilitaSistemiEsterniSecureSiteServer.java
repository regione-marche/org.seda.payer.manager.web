package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.dati.*;
import com.seda.payer.pgec.webservice.abilitasistemiesternisecuresite.source.*;


public class AbilitaSistemiEsterniSecureSiteServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AbilitaSistemiEsterniSecureSiteImplementationStub abilitaSistemiEsterniSecureSiteCaller = null;
	
	private void setCodSocietaHeader (AbilitaSistemiEsterniSecureSiteImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	
	public AbilitaSistemiEsterniSecureSiteServer(String endPoint) {
		AbilitaSistemiEsterniSecureSiteServiceLocator lsService = new AbilitaSistemiEsterniSecureSiteServiceLocator();
		lsService.setAbilitaSistemiEsterniSecureSitePortEndpointAddress(endPoint);
		try {
			abilitaSistemiEsterniSecureSiteCaller = (AbilitaSistemiEsterniSecureSiteImplementationStub)lsService.getAbilitaSistemiEsterniSecureSitePort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public AbilitaSistemiEsterniSecureSiteSearchResponse getAbilitaSistemiEsterniSecureSites(AbilitaSistemiEsterniSecureSiteSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(abilitaSistemiEsterniSecureSiteCaller, request);
		return abilitaSistemiEsterniSecureSiteCaller.getAbilitaSistemiEsterniSecureSites(in);
	}
    public AbilitaSistemiEsterniSecureSiteDetailResponse getAbilitaSistemiEsterniSecureSite(AbilitaSistemiEsterniSecureSiteDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(abilitaSistemiEsterniSecureSiteCaller, request);
    	return abilitaSistemiEsterniSecureSiteCaller.getAbilitaSistemiEsterniSecureSite(in);
    }
    public StatusResponse save(AbilitaSistemiEsterniSecureSiteSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(abilitaSistemiEsterniSecureSiteCaller, request);
    	return abilitaSistemiEsterniSecureSiteCaller.save(in);
    }
    public StatusResponse cancel(AbilitaSistemiEsterniSecureSiteCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(abilitaSistemiEsterniSecureSiteCaller, request);
    	return abilitaSistemiEsterniSecureSiteCaller.cancel(in);
    }
	
	

}
