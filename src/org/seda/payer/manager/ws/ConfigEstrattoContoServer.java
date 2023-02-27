package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.configestrattoconto.dati.*;
import com.seda.payer.pgec.webservice.configestrattoconto.source.*;


public class ConfigEstrattoContoServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConfigEstrattoContoImplementationStub configEstrattoContoCaller = null;
	
	private void setCodSocietaHeader(ConfigEstrattoContoImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public ConfigEstrattoContoServer(String endPoint) {
		ConfigEstrattoContoServiceLocator lsService = new ConfigEstrattoContoServiceLocator();
		lsService.setConfigEstrattoContoPortEndpointAddress(endPoint);
		try {
			configEstrattoContoCaller = (ConfigEstrattoContoImplementationStub)lsService.getConfigEstrattoContoPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public ConfigEstrattoContoSearchResponse getConfigEstrattoContos(ConfigEstrattoContoSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(configEstrattoContoCaller, request);
		return configEstrattoContoCaller.getConfigEstrattoContos(in);
	}
    public ConfigEstrattoContoDetailResponse getConfigEstrattoConto(ConfigEstrattoContoDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(configEstrattoContoCaller, request);
    	return configEstrattoContoCaller.getConfigEstrattoConto(in);
    }
    public StatusResponse save(ConfigEstrattoContoSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(configEstrattoContoCaller, request);
    	return configEstrattoContoCaller.save(in);
    }
    public StatusResponse cancel(ConfigEstrattoContoCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(configEstrattoContoCaller, request);
    	return configEstrattoContoCaller.cancel(in);
    }
	
	

}
