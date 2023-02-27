package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.configrtente.dati.*;
import com.seda.payer.pgec.webservice.configrtente.source.*;


public class ConfigRtEnteServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConfigRtEnteImplementationStub configRtEnteCaller = null;
	
	private void setCodSocietaHeader(ConfigRtEnteImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public ConfigRtEnteServer(String endPoint) {
		ConfigRtEnteServiceLocator lsService = new ConfigRtEnteServiceLocator();
		lsService.setConfigRtEntePortEndpointAddress(endPoint);
		try {
			configRtEnteCaller = (ConfigRtEnteImplementationStub)lsService.getConfigRtEntePort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public ConfigRtEnteSearchResponse getConfigRtEntes(ConfigRtEnteSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(configRtEnteCaller, request);
		return configRtEnteCaller.getConfigRtEntes(in);
	}
    public ConfigRtEnteDetailResponse getConfigRtEnte(ConfigRtEnteDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(configRtEnteCaller, request);
    	return configRtEnteCaller.getConfigRtEnte(in);
    }
    public StatusResponse save(ConfigRtEnteSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(configRtEnteCaller, request);
    	return configRtEnteCaller.save(in);
    }
    public StatusResponse cancel(ConfigRtEnteCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(configRtEnteCaller, request);
    	return configRtEnteCaller.cancel(in);
    }
	
	

}
